package com.moko.lw003plus.activity.general;


import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.moko.ble.lib.MokoConstants;
import com.moko.ble.lib.event.ConnectStatusEvent;
import com.moko.ble.lib.event.OrderTaskResponseEvent;
import com.moko.ble.lib.task.OrderTask;
import com.moko.ble.lib.task.OrderTaskResponse;
import com.moko.ble.lib.utils.MokoUtils;
import com.moko.lw003plus.activity.BaseActivity;
import com.moko.lw003plus.databinding.Lw003PlusActivityGpsFixMotionReportBinding;
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

public class GpsFixMotionReportActivity extends BaseActivity {

    private Lw003PlusActivityGpsFixMotionReportBinding mBind;
    private boolean mReceiverTag = false;
    private boolean savedParamsError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBind = Lw003PlusActivityGpsFixMotionReportBinding.inflate(getLayoutInflater());
        setContentView(mBind.getRoot());
        EventBus.getDefault().register(this);
        // 注册广播接收器
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
        mReceiverTag = true;
        showSyncingProgressDialog();
        mBind.cbFixOnStart.postDelayed(() -> {
            List<OrderTask> orderTasks = new ArrayList<>();
            orderTasks.add(OrderTaskAssembler.getMotionStartEnable());
            orderTasks.add(OrderTaskAssembler.getMotionStartFixEnable());
            orderTasks.add(OrderTaskAssembler.getMotionStartNumber());
            orderTasks.add(OrderTaskAssembler.getMotionTripEnable());
            orderTasks.add(OrderTaskAssembler.getMotionTripFixEnable());
            orderTasks.add(OrderTaskAssembler.getMotionTripInterval());
            orderTasks.add(OrderTaskAssembler.getMotionEndEnable());
            orderTasks.add(OrderTaskAssembler.getMotionEndFixEnable());
            orderTasks.add(OrderTaskAssembler.getMotionEndTimeout());
            orderTasks.add(OrderTaskAssembler.getMotionEndNumber());
            orderTasks.add(OrderTaskAssembler.getMotionEndInterval());
            orderTasks.add(OrderTaskAssembler.getMotionStationaryFixEnable());
            orderTasks.add(OrderTaskAssembler.getMotionStationaryInterval());
            LoRaLW003PlusMokoSupport.getInstance().sendOrder(orderTasks.toArray(new OrderTask[]{}));
        }, 500);
    }

    @Subscribe(threadMode = ThreadMode.POSTING, priority = 300)
    public void onConnectStatusEvent(ConnectStatusEvent event) {
        final String action = event.getAction();
        runOnUiThread(() -> {
            if (MokoConstants.ACTION_DISCONNECTED.equals(action)) {
                finish();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.POSTING, priority = 300)
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
                                    case KEY_MOTION_MODE_START_ENABLE:
                                    case KEY_MOTION_MODE_START_FIX_ENABLE:
                                    case KEY_MOTION_MODE_START_NUMBER:
                                    case KEY_MOTION_MODE_TRIP_ENABLE:
                                    case KEY_MOTION_MODE_TRIP_FIX_ENABLE:
                                    case KEY_MOTION_MODE_TRIP_REPORT_INTERVAL:
                                    case KEY_MOTION_MODE_END_ENABLE:
                                    case KEY_MOTION_MODE_END_FIX_ENABLE:
                                    case KEY_MOTION_MODE_END_TIMEOUT:
                                    case KEY_MOTION_MODE_END_NUMBER:
                                    case KEY_MOTION_MODE_END_REPORT_INTERVAL:
                                    case KEY_MOTION_MODE_STATIONARY_FIX_ENABLE:
                                        savedParamsError |= result != 1;
                                        break;
                                    case KEY_MOTION_MODE_STATIONARY_REPORT_INTERVAL:
                                        savedParamsError |= result != 1;
                                        if (savedParamsError) {
                                            ToastUtils.showToast(GpsFixMotionReportActivity.this, "Opps！Save failed. Please check the input characters and try again.");
                                        } else {
                                            ToastUtils.showToast(this, "Save Successfully！");
                                        }
                                        break;
                                }
                            }
                            if (flag == 0x00) {
                                // read
                                switch (configKeyEnum) {
                                    case KEY_MOTION_MODE_START_FIX_ENABLE:
                                        if (length > 0) {
                                            int enable = value[5] & 0xFF;
                                            mBind.cbFixOnStart.setChecked(enable == 1);
                                        }
                                        break;
                                    case KEY_MOTION_MODE_START_ENABLE:
                                        if (length > 0) {
                                            int enable = value[5] & 0xFF;
                                            mBind.cbNotifyOnStart.setChecked(enable == 1);
                                        }
                                        break;
                                    case KEY_MOTION_MODE_START_NUMBER:
                                        if (length > 0) {
                                            int number = value[5] & 0xFF;
                                            mBind.etFixOnStartNumber.setText(String.valueOf(number));
                                        }
                                        break;
                                    case KEY_MOTION_MODE_TRIP_FIX_ENABLE:
                                        if (length > 0) {
                                            int enable = value[5] & 0xFF;
                                            mBind.cbFixInTrip.setChecked(enable == 1);
                                        }
                                        break;
                                    case KEY_MOTION_MODE_TRIP_ENABLE:
                                        if (length > 0) {
                                            int enable = value[5] & 0xFF;
                                            mBind.cbNotifyInTrip.setChecked(enable == 1);
                                        }
                                        break;
                                    case KEY_MOTION_MODE_TRIP_REPORT_INTERVAL:
                                        if (length > 0) {
                                            byte[] intervalBytes = Arrays.copyOfRange(value, 5, 5 + length);
                                            int interval = MokoUtils.toInt(intervalBytes);
                                            mBind.etReportIntervalInTrip.setText(String.valueOf(interval));
                                        }
                                        break;
                                    case KEY_MOTION_MODE_END_FIX_ENABLE:
                                        if (length > 0) {
                                            int enable = value[5] & 0xFF;
                                            mBind.cbFixOnEnd.setChecked(enable == 1);
                                        }
                                        break;
                                    case KEY_MOTION_MODE_END_ENABLE:
                                        if (length > 0) {
                                            int enable = value[5] & 0xFF;
                                            mBind.cbNotifyOnEnd.setChecked(enable == 1);
                                        }
                                        break;
                                    case KEY_MOTION_MODE_END_TIMEOUT:
                                        if (length > 0) {
                                            int timeout = value[5] & 0xFF;
                                            mBind.etTripEndTimeout.setText(String.valueOf(timeout));
                                        }
                                        break;
                                    case KEY_MOTION_MODE_END_NUMBER:
                                        if (length > 0) {
                                            int number = value[5] & 0xFF;
                                            mBind.etFixOnEndNumber.setText(String.valueOf(number));
                                        }
                                        break;
                                    case KEY_MOTION_MODE_END_REPORT_INTERVAL:
                                        if (length > 0) {
                                            byte[] intervalBytes = Arrays.copyOfRange(value, 5, 5 + length);
                                            int interval = MokoUtils.toInt(intervalBytes);
                                            mBind.etReportIntervalOnEnd.setText(String.valueOf(interval));
                                        }
                                        break;
                                    case KEY_MOTION_MODE_STATIONARY_FIX_ENABLE:
                                        if (length > 0) {
                                            int enable = value[5] & 0xFF;
                                            mBind.cbFixOnStationary.setChecked(enable == 1);
                                        }
                                        break;
                                    case KEY_MOTION_MODE_STATIONARY_REPORT_INTERVAL:
                                        if (length > 0) {
                                            byte[] intervalBytes = Arrays.copyOfRange(value, 5, 5 + length);
                                            int interval = MokoUtils.toInt(intervalBytes);
                                            mBind.etReportIntervalOnStationary.setText(String.valueOf(interval));
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
        final String startNumberStr = mBind.etFixOnStartNumber.getText().toString();
        if (TextUtils.isEmpty(startNumberStr))
            return false;
        final int startNumber = Integer.parseInt(startNumberStr);
        if (startNumber < 1 || startNumber > 10)
            return false;
        final String intervalTripStr = mBind.etReportIntervalInTrip.getText().toString();
        if (TextUtils.isEmpty(intervalTripStr))
            return false;
        final int intervalTrip = Integer.parseInt(intervalTripStr);
        if (intervalTrip < 10 || intervalTrip > 86400)
            return false;
        final String endTimeoutStr = mBind.etTripEndTimeout.getText().toString();
        if (TextUtils.isEmpty(endTimeoutStr))
            return false;
        final int endTimeout = Integer.parseInt(endTimeoutStr);
        if (endTimeout < 1 || endTimeout > 180)
            return false;
        final String endNumberStr = mBind.etFixOnEndNumber.getText().toString();
        if (TextUtils.isEmpty(endNumberStr))
            return false;
        final int endNumber = Integer.parseInt(endNumberStr);
        if (endNumber < 1 || endNumber > 10)
            return false;
        final String endIntervalStr = mBind.etReportIntervalOnEnd.getText().toString();
        if (TextUtils.isEmpty(endIntervalStr))
            return false;
        final int endInterval = Integer.parseInt(endIntervalStr);
        if (endInterval < 10 || endInterval > 300)
            return false;
        final String stationaryIntervalStr = mBind.etReportIntervalOnStationary.getText().toString();
        if (TextUtils.isEmpty(stationaryIntervalStr))
            return false;
        final int stationaryInterval = Integer.parseInt(stationaryIntervalStr);
        if (stationaryInterval < 1 || stationaryInterval > 14400)
            return false;
        return true;

    }

    private void saveParams() {
        final String startNumberStr = mBind.etFixOnStartNumber.getText().toString();
        final int startNumber = Integer.parseInt(startNumberStr);
        final String intervalTripStr = mBind.etReportIntervalInTrip.getText().toString();
        final int intervalTrip = Integer.parseInt(intervalTripStr);
        final String endTimeoutStr = mBind.etTripEndTimeout.getText().toString();
        final int endTimeout = Integer.parseInt(endTimeoutStr);
        final String endNumberStr = mBind.etFixOnEndNumber.getText().toString();
        final int endNumber = Integer.parseInt(endNumberStr);
        final String endIntervalStr = mBind.etReportIntervalOnEnd.getText().toString();
        final int endInterval = Integer.parseInt(endIntervalStr);
        final String stationaryIntervalStr = mBind.etReportIntervalOnStationary.getText().toString();
        final int stationaryInterval = Integer.parseInt(stationaryIntervalStr);

        savedParamsError = false;
        List<OrderTask> orderTasks = new ArrayList<>();
        orderTasks.add(OrderTaskAssembler.setMotionStartEnable(mBind.cbNotifyOnStart.isChecked() ? 1 : 0));
        orderTasks.add(OrderTaskAssembler.setMotionStartFixEnable(mBind.cbFixOnStart.isChecked() ? 1 : 0));
        orderTasks.add(OrderTaskAssembler.setMotionStartNumber(startNumber));

        orderTasks.add(OrderTaskAssembler.setMotionTripEnable(mBind.cbNotifyInTrip.isChecked() ? 1 : 0));
        orderTasks.add(OrderTaskAssembler.setMotionTripFixEnable(mBind.cbFixInTrip.isChecked() ? 1 : 0));
        orderTasks.add(OrderTaskAssembler.setMotionTripInterval(intervalTrip));

        orderTasks.add(OrderTaskAssembler.setMotionEndEnable(mBind.cbNotifyOnEnd.isChecked() ? 1 : 0));
        orderTasks.add(OrderTaskAssembler.setMotionEndFixEnable(mBind.cbFixOnEnd.isChecked() ? 1 : 0));
        orderTasks.add(OrderTaskAssembler.setMotionEndTimeout(endTimeout));
        orderTasks.add(OrderTaskAssembler.setMotionEndNumber(endNumber));
        orderTasks.add(OrderTaskAssembler.setMotionEndInterval(endInterval));

        orderTasks.add(OrderTaskAssembler.setMotionStationaryFixEnable(mBind.cbFixOnStationary.isChecked() ? 1 : 0));
        orderTasks.add(OrderTaskAssembler.setMotionStationaryInterval(stationaryInterval));
        LoRaLW003PlusMokoSupport.getInstance().sendOrder(orderTasks.toArray(new OrderTask[]{}));
    }
}
