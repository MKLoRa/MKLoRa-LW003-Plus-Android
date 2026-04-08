package com.moko.lw003plus.activity;


import android.app.AlertDialog;
import android.app.FragmentManager;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.moko.ble.lib.MokoConstants;
import com.moko.ble.lib.event.ConnectStatusEvent;
import com.moko.ble.lib.event.OrderTaskResponseEvent;
import com.moko.ble.lib.task.OrderTask;
import com.moko.ble.lib.task.OrderTaskResponse;
import com.moko.ble.lib.utils.MokoUtils;
import com.moko.lib.loraui.dialog.AlertMessageDialog;
import com.moko.lib.loraui.dialog.ChangePasswordDialog;
import com.moko.lw003plus.AppConstants;
import com.moko.lw003plus.R;
import com.moko.lw003plus.activity.device.ExportDataActivity;
import com.moko.lw003plus.activity.device.IndicatorSettingsActivity;
import com.moko.lw003plus.activity.device.OnOffSettingsActivity;
import com.moko.lw003plus.activity.device.SystemInfoActivity;
import com.moko.lw003plus.activity.filter.BluetoothFilterSettingsActivity;
import com.moko.lw003plus.activity.general.AxisSettingActivity;
import com.moko.lw003plus.activity.general.BleSettingsActivity;
import com.moko.lw003plus.activity.general.GpsFixSettingsActivity;
import com.moko.lw003plus.activity.general.THSettingsActivity;
import com.moko.lw003plus.activity.lora.LoRaAppSettingActivity;
import com.moko.lw003plus.activity.lora.LoRaConnSettingActivity;
import com.moko.lw003plus.activity.payload.AlarmPayloadSettingActivity;
import com.moko.lw003plus.activity.payload.PayloadContentSelectionActivity;
import com.moko.lw003plus.activity.strategies.ScanReportStrategiesActivity;
import com.moko.lw003plus.databinding.Lw003PlusActivityDeviceInfoBinding;
import com.moko.lw003plus.fragment.DeviceFragment;
import com.moko.lw003plus.fragment.GeneralFragment;
import com.moko.lw003plus.fragment.LoRaFragment;
import com.moko.lw003plus.fragment.ScannerFragment;
import com.moko.lw003plus.utils.ToastUtils;
import com.moko.support.lw003plus.LoRaLW003PlusMokoSupport;
import com.moko.support.lw003plus.OrderTaskAssembler;
import com.moko.support.lw003plus.entity.OrderCHAR;
import com.moko.support.lw003plus.entity.ParamsKeyEnum;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.IdRes;

public class DeviceInfoActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    private Lw003PlusActivityDeviceInfoBinding mBind;
    private FragmentManager fragmentManager;
    private LoRaFragment loraFragment;
    private ScannerFragment scannerFragment;
    private GeneralFragment generalFragment;
    private DeviceFragment deviceFragment;
    private boolean mReceiverTag = false;
    private int disConnectType;
    private boolean savedParamsError;
    public int mDeviceType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBind = Lw003PlusActivityDeviceInfoBinding.inflate(getLayoutInflater());
        setContentView(mBind.getRoot());
        mDeviceType = getIntent().getIntExtra(AppConstants.EXTRA_KEY_DEVICE_TYPE, 0);
        fragmentManager = getFragmentManager();
        initFragment();
        mBind.radioBtnLora.setChecked(true);
        mBind.tvTitle.setText(R.string.title_lora);
        mBind.rgOptions.setOnCheckedChangeListener(this);
        EventBus.getDefault().register(this);


        // 注册广播接收器
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
        mReceiverTag = true;
        if (!LoRaLW003PlusMokoSupport.getInstance().isBluetoothOpen()) {
            LoRaLW003PlusMokoSupport.getInstance().enableBluetooth();
        } else {
            showSyncingProgressDialog();
            mBind.frameContainer.postDelayed(() -> {
                List<OrderTask> orderTasks = new ArrayList<>();
                // sync time after connect success;
                orderTasks.add(OrderTaskAssembler.setTime());
                // get lora params
                orderTasks.add(OrderTaskAssembler.getLoraRegion());
                orderTasks.add(OrderTaskAssembler.getLoraUploadMode());
                orderTasks.add(OrderTaskAssembler.getClassType());
                orderTasks.add(OrderTaskAssembler.getLoraNetworkStatus());
                LoRaLW003PlusMokoSupport.getInstance().sendOrder(orderTasks.toArray(new OrderTask[]{}));
            }, 500);
        }
    }

    private void initFragment() {
        loraFragment = LoRaFragment.newInstance();
        scannerFragment = ScannerFragment.newInstance();
        generalFragment = GeneralFragment.newInstance();
        deviceFragment = DeviceFragment.newInstance();
        fragmentManager.beginTransaction().add(R.id.frame_container, loraFragment).add(R.id.frame_container, scannerFragment).add(R.id.frame_container, generalFragment).add(R.id.frame_container, deviceFragment).show(loraFragment).hide(scannerFragment).hide(generalFragment).hide(deviceFragment).commit();
    }

    @Subscribe(threadMode = ThreadMode.POSTING, priority = 100)
    public void onConnectStatusEvent(ConnectStatusEvent event) {
        EventBus.getDefault().cancelEventDelivery(event);
        final String action = event.getAction();
        runOnUiThread(() -> {
            if (MokoConstants.ACTION_DISCONNECTED.equals(action)) {
                if (LoRaLW003PlusMokoSupport.getInstance().exportDatas != null) {
                    LoRaLW003PlusMokoSupport.getInstance().exportDatas.clear();
                    LoRaLW003PlusMokoSupport.getInstance().storeString = null;
                    LoRaLW003PlusMokoSupport.getInstance().startTime = 0;
                    LoRaLW003PlusMokoSupport.getInstance().sum = 0;
                }
                showDisconnectDialog();
            }
            if (MokoConstants.ACTION_DISCOVER_SUCCESS.equals(action)) {
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.POSTING, priority = 100)
    public void onOrderTaskResponseEvent(OrderTaskResponseEvent event) {
        EventBus.getDefault().cancelEventDelivery(event);
        final String action = event.getAction();
        runOnUiThread(() -> {
            if (MokoConstants.ACTION_CURRENT_DATA.equals(action)) {
                OrderTaskResponse response = event.getResponse();
                OrderCHAR orderCHAR = (OrderCHAR) response.orderCHAR;
                int responseType = response.responseType;
                byte[] value = response.responseValue;
                switch (orderCHAR) {
                    case CHAR_DISCONNECTED_NOTIFY:
                        final int length = value.length;
                        if (length != 6) return;
                        int header = value[0] & 0xFF;
                        int flag = value[1] & 0xFF;
                        int cmd = MokoUtils.toInt(Arrays.copyOfRange(value, 2, 4));
                        int len = value[4] & 0xFF;
                        int type = value[5] & 0xFF;
                        if (header == 0xED && flag == 0x02 && cmd == 0x0001 && len == 0x01) {
                            disConnectType = type;
                            if (type == 1) {
                                // valid password timeout
                            } else if (type == 2) {
                                // change password success
                            } else if (type == 3) {
                                // no data exchange timeout
                            } else if (type == 4) {
                                // reset success
                            }
                        }
                        break;
                }
            }
            if (MokoConstants.ACTION_ORDER_TIMEOUT.equals(action)) {
            }
            if (MokoConstants.ACTION_ORDER_FINISH.equals(action)) {
                dismissSyncProgressDialog();
            }
            if (MokoConstants.ACTION_ORDER_RESULT.equals(action)) {
                OrderTaskResponse response = event.getResponse();
                OrderCHAR orderCHAR = (OrderCHAR) response.orderCHAR;
                int responseType = response.responseType;
                byte[] value = response.responseValue;
                switch (orderCHAR) {
                    case CHAR_PARAMS:
                        if (value.length >= 5) {
                            int header = value[0] & 0xFF;// 0xED
                            int flag = value[1] & 0xFF;// read or write
                            int cmd = MokoUtils.toInt(Arrays.copyOfRange(value, 2, 4));
                            if (header != 0xED) return;
                            ParamsKeyEnum configKeyEnum = ParamsKeyEnum.fromParamKey(cmd);
                            if (configKeyEnum == null) {
                                return;
                            }
                            int length = value[4] & 0xFF;
                            if (flag == 0x01) {
                                // write
                                int result = value[5] & 0xFF;
                                switch (configKeyEnum) {
                                    case KEY_TIME_UTC:
                                        if (result == 1)
                                            ToastUtils.showToast(DeviceInfoActivity.this, "Time sync completed!");
                                        break;
                                    case KEY_DUPLICATE_DATA_FILTER:
                                    case KEY_DATA_RETENTION_STRATEGY:
                                    case KEY_HEARTBEAT_INTERVAL:
                                    case KEY_SHUTDOWN_PAYLOAD_ENABLE:
                                    case KEY_TIME_ZONE:
                                    case KEY_LOW_POWER_PAYLOAD_ENABLE:
                                    case KEY_LOW_POWER_REPORT_INTERVAL:
                                    case KEY_LOW_POWER_PERCENT:
                                        savedParamsError |= result != 1;
                                        break;
                                    case KEY_REPORT_DATA_MAX_LENGTH:
                                    case KEY_ADV_REPORT_ONLY_ENABLE:
                                    case KEY_CONTINUITY_TRANSFER_ENABLE:
                                        savedParamsError |= result != 1;
                                        if (savedParamsError) {
                                            ToastUtils.showToast(DeviceInfoActivity.this, "Opps！Save failed. Please check the input characters and try again.");
                                        } else {
                                            ToastUtils.showToast(this, "Save Successfully！");
                                        }
                                        break;
                                }
                            }
                            if (flag == 0x00) {
                                // read
                                switch (configKeyEnum) {
                                    case KEY_LORA_REGION:
                                        if (length > 0) {
                                            final int region = value[5] & 0xFF;
                                            loraFragment.setRegion(region);
                                        }
                                        break;
                                    case KEY_LORA_MODE:
                                        if (length > 0) {
                                            final int mode = value[5];
                                            loraFragment.setUploadMode(mode);
                                        }
                                        break;
                                    case KEY_LORA_CLASS_TYPE:
                                        if (length > 0) {
                                            final int classType = value[5];
                                            loraFragment.setClassType(classType);
                                        }
                                        break;
                                    case KEY_LORA_NETWORK_STATUS:
                                        if (length > 0) {
                                            int networkStatus = value[5] & 0xFF;
                                            loraFragment.setLoraStatus(networkStatus);
                                        }
                                        break;
                                    case KEY_REPORT_DATA_MAX_LENGTH:
                                        if (length > 0) {
                                            int maxLength = value[5] & 0xFF;
                                            scannerFragment.setReportDataMaxLength(maxLength);
                                        }
                                        break;
                                    case KEY_ADV_REPORT_ONLY_ENABLE:
                                        if (length > 0) {
                                            int enable = value[5] & 0xFF;
                                            scannerFragment.setAdvReportOnlyEnable(enable);
                                        }
                                        break;
                                    case KEY_DUPLICATE_DATA_FILTER:
                                        if (length > 0) {
                                            int duplicateData = value[5] & 0xFF;
                                            scannerFragment.setDuplicateDataFilter(duplicateData);
                                        }
                                        break;
                                    case KEY_DATA_RETENTION_STRATEGY:
                                        if (length > 0) {
                                            int strategy = value[5] & 0xFF;
                                            scannerFragment.setDataRetentionStrategy(strategy);
                                        }
                                        break;
                                    case KEY_HEARTBEAT_INTERVAL:
                                        if (length > 0) {
                                            byte[] intervalBytes = Arrays.copyOfRange(value, 5, 5 + length);
                                            generalFragment.setHeartbeatInterval(MokoUtils.toInt(intervalBytes));
                                        }
                                        break;
                                    case KEY_CONTINUITY_TRANSFER_ENABLE:
                                        if (length > 0) {
                                            int enable = value[5] & 0xFF;
                                            generalFragment.setEnable(enable);
                                        }
                                        break;
                                    case KEY_TIME_ZONE:
                                        if (length > 0) {
                                            int timeZone = value[5];
                                            deviceFragment.setTimeZone(timeZone);
                                        }
                                        break;
                                    case KEY_LOW_POWER_PAYLOAD_ENABLE:
                                        if (length > 0) {
                                            int enable = value[5] & 0xFF;
                                            deviceFragment.setLowPowerPayload(enable);
                                        }
                                        break;
                                    case KEY_LOW_POWER_REPORT_INTERVAL:
                                        if (length > 0) {
                                            int interval = value[5] & 0xFF;
                                            deviceFragment.setLowPowerInterval(interval);
                                        }
                                        break;
                                    case KEY_LOW_POWER_PERCENT:
                                        if (length > 0) {
                                            int lowPower = value[5] & 0xFF;
                                            deviceFragment.setLowPower(lowPower);
                                        }
                                        break;
                                }
                            }

                        }
                        break;
                }
            }
        });
    }

    private void showDisconnectDialog() {
        if (disConnectType == 2) {
            AlertMessageDialog dialog = new AlertMessageDialog();
            dialog.setTitle("Change Password");
            dialog.setMessage("Password changed successfully!Please reconnect the device.");
            dialog.setConfirm("OK");
            dialog.setCancelGone();
            dialog.setOnAlertConfirmListener(() -> {
                setResult(RESULT_OK);
                finish();
            });
            dialog.show(getSupportFragmentManager());
        } else if (disConnectType == 3) {
            AlertMessageDialog dialog = new AlertMessageDialog();
            dialog.setMessage("No data communication for 3 minutes, the device is disconnected.");
            dialog.setConfirm("OK");
            dialog.setCancelGone();
            dialog.setOnAlertConfirmListener(() -> {
                setResult(RESULT_OK);
                finish();
            });
            dialog.show(getSupportFragmentManager());
        } else if (disConnectType == 5) {
            AlertMessageDialog dialog = new AlertMessageDialog();
            dialog.setTitle("Factory Reset");
            dialog.setMessage("Factory reset successfully!\nPlease reconnect the device.");
            dialog.setConfirm("OK");
            dialog.setCancelGone();
            dialog.setOnAlertConfirmListener(() -> {
                setResult(RESULT_OK);
                finish();
            });
            dialog.show(getSupportFragmentManager());
        } else if (disConnectType == 4) {
            AlertMessageDialog dialog = new AlertMessageDialog();
            dialog.setTitle("Dismiss");
            dialog.setMessage("Reboot successfully!\nPlease reconnect the device");
            dialog.setConfirm("OK");
            dialog.setCancelGone();
            dialog.setOnAlertConfirmListener(() -> {
                setResult(RESULT_OK);
                finish();
            });
            dialog.show(getSupportFragmentManager());
        } else if (disConnectType == 1) {
            AlertMessageDialog dialog = new AlertMessageDialog();
            dialog.setMessage("The device is disconnected!");
            dialog.setConfirm("OK");
            dialog.setCancelGone();
            dialog.setOnAlertConfirmListener(() -> {
                setResult(RESULT_OK);
                finish();
            });
            dialog.show(getSupportFragmentManager());
        } else {
            if (LoRaLW003PlusMokoSupport.getInstance().isBluetoothOpen()) {
                AlertMessageDialog dialog = new AlertMessageDialog();
                dialog.setTitle("Dismiss");
                dialog.setMessage("The device disconnected!");
                dialog.setConfirm("Exit");
                dialog.setCancelGone();
                dialog.setOnAlertConfirmListener(() -> {
                    setResult(RESULT_OK);
                    finish();
                });
                dialog.show(getSupportFragmentManager());
            }
        }
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent != null) {
                String action = intent.getAction();
                if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                    int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                    switch (blueState) {
                        case BluetoothAdapter.STATE_TURNING_OFF:
                            dismissSyncProgressDialog();
                            AlertDialog.Builder builder = new AlertDialog.Builder(DeviceInfoActivity.this);
                            builder.setTitle("Dismiss");
                            builder.setCancelable(false);
                            builder.setMessage("The current system of bluetooth is not available!");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DeviceInfoActivity.this.setResult(RESULT_OK);
                                    finish();
                                }
                            });
                            builder.show();
                            break;

                    }
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiverTag) {
            mReceiverTag = false;
            // 注销广播
            unregisterReceiver(mReceiver);
        }
        EventBus.getDefault().unregister(this);
    }

    public void onBack(View view) {
        if (isWindowLocked()) return;
        back();
    }

    public void onSave(View view) {
        if (isWindowLocked()) return;
        if (mBind.radioBtnScanner.isChecked()) {
            showSyncingProgressDialog();
            scannerFragment.saveParams();
        }
        if (mBind.radioBtnGeneral.isChecked()) {
            if (generalFragment.isValid()) {
                showSyncingProgressDialog();
                generalFragment.saveParams();
            } else {
                ToastUtils.showToast(this, "Para error!");
            }
        }
        if (mBind.radioBtnDevice.isChecked()) {
            if (deviceFragment.isValid()) {
                showSyncingProgressDialog();
                deviceFragment.saveParams();
            } else {
                ToastUtils.showToast(this, "Para error!");
            }
        }
    }

    private void back() {
        mBind.frameContainer.postDelayed(() -> {
            LoRaLW003PlusMokoSupport.getInstance().disConnectBle();
        }, 500);
    }

    @Override
    public void onBackPressed() {
        if (isWindowLocked()) return;
        back();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        savedParamsError = false;
        if (checkedId == R.id.radioBtn_lora) {
            showLoRaAndGetData();
        } else if (checkedId == R.id.radioBtn_scanner) {
            showScannerAndGetData();
        } else if (checkedId == R.id.radioBtn_general) {
            showGeneralAndGetData();
        } else if (checkedId == R.id.radioBtn_device) {
            showDeviceAndGetData();
        }
    }

    private void showDeviceAndGetData() {
        mBind.tvTitle.setText("Device Settings");
        mBind.ivSave.setVisibility(View.VISIBLE);
        fragmentManager.beginTransaction().hide(loraFragment).hide(scannerFragment).hide(generalFragment).show(deviceFragment).commit();
        showSyncingProgressDialog();
        List<OrderTask> orderTasks = new ArrayList<>();
        // device
        orderTasks.add(OrderTaskAssembler.getTimeZone());
        orderTasks.add(OrderTaskAssembler.getLowPowerPayloadEnable());
        orderTasks.add(OrderTaskAssembler.getLowPowerInterval());
        orderTasks.add(OrderTaskAssembler.getLowPowerPercent());
        LoRaLW003PlusMokoSupport.getInstance().sendOrder(orderTasks.toArray(new OrderTask[]{}));
    }

    private void showGeneralAndGetData() {
        mBind.tvTitle.setText("General Settings");
        mBind.ivSave.setVisibility(View.VISIBLE);
        fragmentManager.beginTransaction().hide(loraFragment).hide(scannerFragment).show(generalFragment).hide(deviceFragment).commit();
        showSyncingProgressDialog();
        List<OrderTask> orderTasks = new ArrayList<>();
        orderTasks.add(OrderTaskAssembler.getHeartBeatInterval());
        orderTasks.add(OrderTaskAssembler.getContinuityTransferFunctionEnable());
        LoRaLW003PlusMokoSupport.getInstance().sendOrder(orderTasks.toArray(new OrderTask[]{}));
    }

    private void showScannerAndGetData() {
        mBind.tvTitle.setText("Bluetooth Gateway Settings");
        mBind.ivSave.setVisibility(View.VISIBLE);
        fragmentManager.beginTransaction().hide(loraFragment).show(scannerFragment).hide(generalFragment).hide(deviceFragment).commit();
        showSyncingProgressDialog();
        List<OrderTask> orderTasks = new ArrayList<>();
        // scanner
        orderTasks.add(OrderTaskAssembler.getDataRetentionStrategy());
        orderTasks.add(OrderTaskAssembler.getReportDataMaxLength());
        orderTasks.add(OrderTaskAssembler.getAdvReportOnlyEnable());
        orderTasks.add(OrderTaskAssembler.getFilterDuplicateData());
        LoRaLW003PlusMokoSupport.getInstance().sendOrder(orderTasks.toArray(new OrderTask[]{}));
    }

    private void showLoRaAndGetData() {
        mBind.tvTitle.setText(R.string.title_lora);
        mBind.ivSave.setVisibility(View.GONE);
        fragmentManager.beginTransaction().show(loraFragment).hide(scannerFragment).hide(generalFragment).hide(deviceFragment).commit();
        showSyncingProgressDialog();
        List<OrderTask> orderTasks = new ArrayList<>();
        // get lora params
        orderTasks.add(OrderTaskAssembler.getLoraRegion());
        orderTasks.add(OrderTaskAssembler.getLoraUploadMode());
        orderTasks.add(OrderTaskAssembler.getLoraNetworkStatus());
        LoRaLW003PlusMokoSupport.getInstance().sendOrder(orderTasks.toArray(new OrderTask[]{}));
    }

    public void onChangePassword(View view) {
        if (isWindowLocked()) return;
        final ChangePasswordDialog dialog = new ChangePasswordDialog(this);
        dialog.setOnPasswordClicked(password -> {
            showSyncingProgressDialog();
            LoRaLW003PlusMokoSupport.getInstance().sendOrder(OrderTaskAssembler.changePassword(password));
        });
        dialog.show();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override

            public void run() {
                runOnUiThread(() -> dialog.showKeyboard());
            }
        }, 200);
    }

    public void onLoRaConnSetting(View view) {
        if (isWindowLocked()) return;
        Intent intent = new Intent(this, LoRaConnSettingActivity.class);
        startLoRaConnSetting.launch(intent);
    }

    private final ActivityResultLauncher<Intent> startLoRaConnSetting = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), callback -> {
        if (callback != null && callback.getResultCode() == RESULT_OK) {
            showSyncingProgressDialog();
            mBind.ivSave.postDelayed(() -> {
                List<OrderTask> orderTasks = new ArrayList<>();
                // setting
                orderTasks.add(OrderTaskAssembler.getLoraRegion());
                orderTasks.add(OrderTaskAssembler.getLoraUploadMode());
                orderTasks.add(OrderTaskAssembler.getLoraNetworkStatus());
                LoRaLW003PlusMokoSupport.getInstance().sendOrder(orderTasks.toArray(new OrderTask[]{}));
            }, 1000);
        }
    });

    public void onLoRaAppSetting(View view) {
        if (isWindowLocked()) return;
        Intent intent = new Intent(this, LoRaAppSettingActivity.class);
        startActivity(intent);
    }


    public void onScanReportStrategies(View view) {
        if (isWindowLocked()) return;
        startActivity(new Intent(this, ScanReportStrategiesActivity.class));
    }

    public void onBluetoothFilter(View view) {
        if (isWindowLocked()) return;
        startActivity(new Intent(this, BluetoothFilterSettingsActivity.class));
    }

    public void onPayloadContentSelection(View view) {
        if (isWindowLocked()) return;
        startActivity(new Intent(this, PayloadContentSelectionActivity.class));
    }

    public void onAlarmPayloadSettings(View view) {
        if (isWindowLocked()) return;
        startActivity(new Intent(this, AlarmPayloadSettingActivity.class));
    }

    public void selectReportDataMaxLength(View view) {
        if (isWindowLocked()) return;
        scannerFragment.selectReportDataMaxLength(view);
    }

    public void selectDataRetentionStrategy(View view) {
        if (isWindowLocked()) return;
        scannerFragment.selectDataRetentionStrategy(view);
    }

    public void selectDuplicateDataFilter(View view) {
        if (isWindowLocked()) return;
        scannerFragment.selectDuplicateDataFilter(view);
    }


    public void onGPSFix(View view) {
        if (isWindowLocked()) return;
        Intent intent = new Intent(this, GpsFixSettingsActivity.class);
        startActivity(intent);
    }

    public void onBleSettings(View view) {
        if (isWindowLocked()) return;
        Intent intent = new Intent(this, BleSettingsActivity.class);
        startActivity(intent);
    }


    public void onTHSettings(View view) {
        if (isWindowLocked()) return;
        Intent intent = new Intent(this, THSettingsActivity.class);
        startActivity(intent);
    }
    public void onAxisSettings(View view) {
        if (isWindowLocked()) return;
        Intent intent = new Intent(this, AxisSettingActivity.class);
        startActivity(intent);
    }

    public void onOnOffSettings(View view) {
        if (isWindowLocked()) return;
        Intent intent = new Intent(this, OnOffSettingsActivity.class);
        startActivity(intent);
    }


    public void onLocalDataSync(View view) {
        if (isWindowLocked()) return;
        startActivity(new Intent(this, ExportDataActivity.class));
    }

    public void onIndicatorSettings(View view) {
        if (isWindowLocked()) return;
        startActivity(new Intent(this, IndicatorSettingsActivity.class));
    }

    public void selectTimeZone(View view) {
        if (isWindowLocked()) return;
        deviceFragment.showTimeZoneDialog();
    }


    public void selectLowPowerPrompt(View view) {
        if (isWindowLocked()) return;
        deviceFragment.showLowPowerDialog();
    }

    public void onDeviceInfo(View view) {
        if (isWindowLocked()) return;
        Intent intent = new Intent(this, SystemInfoActivity.class);
        startSystemInfo.launch(intent);
    }

    private final ActivityResultLauncher<Intent> startSystemInfo = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), callback -> {
        if (callback == null) return;
        if (callback.getResultCode() == RESULT_OK) {
            AlertMessageDialog dialog = new AlertMessageDialog();
            dialog.setTitle("Update Firmware");
            dialog.setMessage("Update firmware successfully!\nPlease reconnect the device.");
            dialog.setConfirm("OK");
            dialog.setCancelGone();
            dialog.setOnAlertConfirmListener(() -> {
                setResult(RESULT_OK);
                finish();
            });
            dialog.show(getSupportFragmentManager());
        }
        if (callback.getResultCode() == RESULT_FIRST_USER) {
            String mac = callback.getData().getStringExtra(AppConstants.EXTRA_KEY_DEVICE_MAC);
            mBind.frameContainer.postDelayed(() -> {
                if (LoRaLW003PlusMokoSupport.getInstance().isConnDevice(mac)) {
                    LoRaLW003PlusMokoSupport.getInstance().disConnectBle();
                    return;
                }
                showDisconnectDialog();
            }, 500);
        }
    });

    public void onFactoryReset(View view) {
        if (isWindowLocked()) return;
        AlertMessageDialog dialog = new AlertMessageDialog();
        dialog.setTitle("Factory Reset!");
        dialog.setMessage("After factory reset,all the data will be reseted to the factory values.");
        dialog.setConfirm("OK");
        dialog.setOnAlertConfirmListener(() -> {
            showSyncingProgressDialog();
            LoRaLW003PlusMokoSupport.getInstance().sendOrder(OrderTaskAssembler.restore());
        });
        dialog.show(getSupportFragmentManager());
    }

    public void onPowerOff(View view) {
        if (isWindowLocked()) return;
        AlertMessageDialog dialog = new AlertMessageDialog();
        dialog.setTitle("Warning!");
        dialog.setMessage("Are you sure to turn off the device? Please make sure the device has a button to turn on!");
        dialog.setConfirm("OK");
        dialog.setOnAlertConfirmListener(() -> {
            showSyncingProgressDialog();
            LoRaLW003PlusMokoSupport.getInstance().sendOrder(OrderTaskAssembler.close());
        });
        dialog.show(getSupportFragmentManager());
    }
}
