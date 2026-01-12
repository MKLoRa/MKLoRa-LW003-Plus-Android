package com.moko.lw003plus.activity.filter;


import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import com.moko.ble.lib.MokoConstants;
import com.moko.ble.lib.event.ConnectStatusEvent;
import com.moko.ble.lib.event.OrderTaskResponseEvent;
import com.moko.ble.lib.task.OrderTask;
import com.moko.ble.lib.task.OrderTaskResponse;
import com.moko.ble.lib.utils.MokoUtils;
import com.moko.lw003plus.R;
import com.moko.lw003plus.activity.BaseActivity;
import com.moko.lw003plus.databinding.Lw003ProActivityBluetoothFilterSettingsBinding;
import com.moko.lib.loraui.dialog.BottomDialog;
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

public class BluetoothFilterSettingsActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener {

    private Lw003ProActivityBluetoothFilterSettingsBinding mBind;
    private boolean mReceiverTag = false;
    private boolean savedParamsError;
    private ArrayList<String> mRelationshipValues;
    private ArrayList<String> mScanningTypeValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBind = Lw003ProActivityBluetoothFilterSettingsBinding.inflate(getLayoutInflater());
        setContentView(mBind.getRoot());
        EventBus.getDefault().register(this);

        mRelationshipValues = new ArrayList<>();
        mRelationshipValues.add("Null");
        mRelationshipValues.add("Only MAC");
        mRelationshipValues.add("Only ADV Name");
        mRelationshipValues.add("Only Raw Data");
        mRelationshipValues.add("ADV Name&Raw Data");
        mRelationshipValues.add("MAC&ADV Name&Raw Data");
        mRelationshipValues.add("ADV Name | Raw Data");
        mScanningTypeValues = new ArrayList<>();
        mScanningTypeValues.add("1M PHY(BLE 4.x)");
        mScanningTypeValues.add("1M PHY(BLE 5)");
        mScanningTypeValues.add("1M PHY(BLE 4.x + BLE 5)");
        mScanningTypeValues.add("Coded PHY(BLE 5)");
        mBind.sbRssiFilter.setOnSeekBarChangeListener(this);
        // 注册广播接收器
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
        mReceiverTag = true;
        showSyncingProgressDialog();
        mBind.sbRssiFilter.postDelayed(() -> {
            List<OrderTask> orderTasks = new ArrayList<>();
            orderTasks.add(OrderTaskAssembler.getFilterRSSI());
            orderTasks.add(OrderTaskAssembler.getFilterBleScanPhy());
            orderTasks.add(OrderTaskAssembler.getFilterRelationship());
            LoRaLW003PlusMokoSupport.getInstance().sendOrder(orderTasks.toArray(new OrderTask[]{}));
        }, 500);
    }

    @Subscribe(threadMode = ThreadMode.POSTING, priority = 200)
    public void onConnectStatusEvent(ConnectStatusEvent event) {
        final String action = event.getAction();
        runOnUiThread(() -> {
            if (MokoConstants.ACTION_DISCONNECTED.equals(action)) {
                finish();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.POSTING, priority = 200)
    public void onOrderTaskResponseEvent(OrderTaskResponseEvent event) {
        final String action = event.getAction();
        if (!MokoConstants.ACTION_CURRENT_DATA.equals(action))
            EventBus.getDefault().cancelEventDelivery(event);
        runOnUiThread(() -> {
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
                            if (header != 0xED)
                                return;
                            ParamsKeyEnum configKeyEnum = ParamsKeyEnum.fromParamKey(cmd);
                            if (configKeyEnum == null) {
                                return;
                            }
                            int length = value[4] & 0xFF;
                            if (flag == 0x01) {
                                // write
                                int result = value[5] & 0xFF;
                                switch (configKeyEnum) {
                                    case KEY_FILTER_RSSI:
                                    case KEY_FILTER_PHY:
                                        if (result != 1) {
                                            savedParamsError = true;
                                        }
                                        break;
                                    case KEY_FILTER_RELATIONSHIP:
                                        if (result != 1) {
                                            savedParamsError = true;
                                        }
                                        if (savedParamsError) {
                                            ToastUtils.showToast(BluetoothFilterSettingsActivity.this, "Opps！Save failed. Please check the input characters and try again.");
                                        } else {
                                            ToastUtils.showToast(this, "Save Successfully！");
                                        }
                                        break;
                                }
                            }
                            if (flag == 0x00) {
                                // read
                                switch (configKeyEnum) {
                                    case KEY_FILTER_RSSI:
                                        if (length > 0) {
                                            final int rssi = value[5];
                                            int progress = rssi + 127;
                                            mBind.sbRssiFilter.setProgress(progress);
                                            mBind.tvRssiFilterTips.setText(getString(R.string.rssi_filter, rssi));
                                        }
                                        break;
                                    case KEY_FILTER_PHY:
                                        if (length > 0) {
                                            int type = value[5] & 0xFF;
                                            setScanningType(type);
                                        }
                                        break;
                                    case KEY_FILTER_RELATIONSHIP:
                                        if (length > 0) {
                                            int relationship = value[5] & 0xFF;
                                            setRelationship(relationship);
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

    public void onSave(View view) {
        if (isWindowLocked())
            return;
        showSyncingProgressDialog();
        savedParamsError = false;
        int type = (int) mBind.tvScanningType.getTag();
        int relationship = (int) mBind.tvFilterRelationship.getTag();
        List<OrderTask> orderTasks = new ArrayList<>();
        orderTasks.add(OrderTaskAssembler.setFilterRSSI(mBind.sbRssiFilter.getProgress() - 127));
        orderTasks.add(OrderTaskAssembler.setFilterBleScanPhy(type));
        orderTasks.add(OrderTaskAssembler.setFilterRelationship(relationship));
        LoRaLW003PlusMokoSupport.getInstance().sendOrder(orderTasks.toArray(new OrderTask[]{}));
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
                            finish();
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
        backHome();
    }

    @Override
    public void onBackPressed() {
        backHome();
    }

    private void backHome() {
        setResult(RESULT_OK);
        finish();
    }

    private void setScanningType(int type) {
        mBind.tvScanningType.setTag(type);
        mBind.tvScanningType.setText(mScanningTypeValues.get(type));
    }

    private void setRelationship(int relationship) {
        mBind.tvFilterRelationship.setTag(relationship);
        mBind.tvFilterRelationship.setText(mRelationshipValues.get(relationship));
    }

    public void onScanningType(View view) {
        if (isWindowLocked())
            return;
        int selected = (int) view.getTag();
        BottomDialog dialog = new BottomDialog();
        dialog.setDatas(mScanningTypeValues, selected);
        dialog.setListener(this::setScanningType);
        dialog.show(getSupportFragmentManager());
    }

    public void onFilterRelationship(View view) {
        if (isWindowLocked())
            return;
        int selected = (int) view.getTag();
        BottomDialog dialog = new BottomDialog();
        dialog.setDatas(mRelationshipValues, selected);
        dialog.setListener(this::setRelationship);
        dialog.show(getSupportFragmentManager());
    }

    public void onFilterByMac(View view) {
        if (isWindowLocked())
            return;
        Intent intent = new Intent(this, FilterMacAddressActivity.class);
        startActivity(intent);
    }

    public void onFilterByName(View view) {
        if (isWindowLocked())
            return;
        Intent intent = new Intent(this, FilterAdvNameActivity.class);
        startActivity(intent);
    }

    public void onFilterByRawData(View view) {
        if (isWindowLocked())
            return;
        Intent intent = new Intent(this, FilterRawDataSwitchActivity.class);
        startActivity(intent);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        int rssi = progress - 127;
        mBind.tvRssiFilterValue.setText(String.format("%ddBm", rssi));
        mBind.tvRssiFilterTips.setText(getString(R.string.rssi_filter, rssi));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
