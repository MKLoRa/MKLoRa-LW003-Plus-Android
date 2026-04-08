package com.moko.lw003plus.activity.general;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.moko.ble.lib.MokoConstants;
import com.moko.ble.lib.event.ConnectStatusEvent;
import com.moko.ble.lib.event.OrderTaskResponseEvent;
import com.moko.ble.lib.task.OrderTask;
import com.moko.ble.lib.task.OrderTaskResponse;
import com.moko.ble.lib.utils.MokoUtils;
import com.moko.lib.loraui.dialog.BottomDialog;
import com.moko.lw003plus.activity.BaseActivity;
import com.moko.lw003plus.databinding.Lw003PlusActivityGpsFixBinding;
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

public class GpsFixSettingsActivity extends BaseActivity {


    private Lw003PlusActivityGpsFixBinding mBind;

    private boolean savedParamsError;

    private String[] mStragegyValue = {"No Report", "Periodic Report", "Timing Report", "Motion Report", "Report with BLE Payload"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBind = Lw003PlusActivityGpsFixBinding.inflate(getLayoutInflater());
        setContentView(mBind.getRoot());
        EventBus.getDefault().register(this);
        showSyncingProgressDialog();
        mBind.etPdopLimit.postDelayed(() -> {
            List<OrderTask> orderTasks = new ArrayList<>();
            orderTasks.add(OrderTaskAssembler.getDeviceMode());
            orderTasks.add(OrderTaskAssembler.getGPSPosTimeoutL76());
            orderTasks.add(OrderTaskAssembler.getGPSPDOPLimitL76());
            orderTasks.add(OrderTaskAssembler.getGPSExtremeModeL76());
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
                                    case KEY_DEVICE_MODE:
                                    case KEY_GPS_POS_TIMEOUT_L76C:
                                    case KEY_GPS_PDOP_LIMIT_L76C:
                                        savedParamsError |= result != 1;
                                        break;
                                    case KEY_GPS_EXTREME_MODE_L76C:
                                        savedParamsError |= result != 1;
                                        if (savedParamsError) {
                                            ToastUtils.showToast(GpsFixSettingsActivity.this, "Opps！Save failed. Please check the input characters and try again.");
                                        } else {
                                            ToastUtils.showToast(this, "Save Successfully！");
                                        }
                                        break;
                                }
                            }
                            if (flag == 0x00) {
                                // read
                                switch (configKeyEnum) {
                                    case KEY_DEVICE_MODE:
                                        if (length > 0) {
                                            int strategy = value[5] & 0xFF;
                                            mBind.tvGPSFixStrategy.setTag(strategy);
                                            mBind.tvGPSFixStrategy.setText(mStragegyValue[strategy]);
                                        }
                                        break;
                                    case KEY_GPS_POS_TIMEOUT_L76C:
                                        if (length > 0) {
                                            byte[] timeoutBytes = Arrays.copyOfRange(value, 5, 5 + length);
                                            int timeout = MokoUtils.toInt(timeoutBytes);
                                            mBind.etPositionTimeout.setText(String.valueOf(timeout));
                                        }
                                        break;
                                    case KEY_GPS_PDOP_LIMIT_L76C:
                                        if (length > 0) {
                                            int limit = value[5] & 0xFF;
                                            mBind.etPdopLimit.setText(String.valueOf(limit));
                                        }
                                        break;
                                    case KEY_GPS_EXTREME_MODE_L76C:
                                        if (length > 0) {
                                            int enable = value[5] & 0xFF;
                                            mBind.cbGPSExtremeMode.setChecked(enable == 1);
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
        if (isValid()) {
            showSyncingProgressDialog();
            saveParams();
        } else {
            ToastUtils.showToast(this, "Para error!");
        }
    }

    private boolean isValid() {
        final String posTimeoutStr = mBind.etPositionTimeout.getText().toString();
        if (TextUtils.isEmpty(posTimeoutStr))
            return false;
        final int posTimeout = Integer.parseInt(posTimeoutStr);
        if (posTimeout < 30 || posTimeout > 600) {
            return false;
        }
        final String pdopLimitStr = mBind.etPdopLimit.getText().toString();
        if (TextUtils.isEmpty(pdopLimitStr))
            return false;
        final int pdopLimit = Integer.parseInt(pdopLimitStr);
        if (pdopLimit < 5 || pdopLimit > 100) {
            return false;
        }
        return true;

    }


    private void saveParams() {
        int selected = (int) mBind.tvGPSFixStrategy.getTag();
        final String posTimeoutStr = mBind.etPositionTimeout.getText().toString();
        final int posTimeout = Integer.parseInt(posTimeoutStr);
        final String pdopLimitStr = mBind.etPdopLimit.getText().toString();
        final int pdopLimit = Integer.parseInt(pdopLimitStr);
        savedParamsError = false;
        List<OrderTask> orderTasks = new ArrayList<>();
        orderTasks.add(OrderTaskAssembler.setDeviceMode(selected));
        orderTasks.add(OrderTaskAssembler.setGPSPosTimeoutL76C(posTimeout));
        orderTasks.add(OrderTaskAssembler.setGPSPDOPLimitL76C(pdopLimit));
        orderTasks.add(OrderTaskAssembler.setGPSExtremeModeL76C(mBind.cbGPSExtremeMode.isChecked() ? 1 : 0));
        LoRaLW003PlusMokoSupport.getInstance().sendOrder(orderTasks.toArray(new OrderTask[]{}));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

    public void selectGpsFixStrategies(View view) {
        if (isWindowLocked())
            return;
        int selected = (int) view.getTag();
        BottomDialog dialog = new BottomDialog();
        dialog.setDatas(new ArrayList<>(Arrays.asList(mStragegyValue)), selected);
        dialog.setListener(value -> {
            mBind.tvGPSFixStrategy.setTag(value);
            mBind.tvGPSFixStrategy.setText(mStragegyValue[value]);
        });
        dialog.show(getSupportFragmentManager());
    }

    public void onPeriodicReportSettings(View view) {
        if (isWindowLocked()) return;
        startActivity(new Intent(this, GpsFixPeriodicReportActivity.class));
    }

    public void onTimingReportSettings(View view) {
        if (isWindowLocked()) return;
        startActivity(new Intent(this, GpsFixTimingReportActivity.class));
    }

    public void onMotionReportSettings(View view) {
        if (isWindowLocked()) return;
        startActivity(new Intent(this, GpsFixMotionReportActivity.class));
    }
}
