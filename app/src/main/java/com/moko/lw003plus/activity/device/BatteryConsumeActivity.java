package com.moko.lw003plus.activity.device;


import android.os.Bundle;
import android.view.View;

import com.moko.ble.lib.MokoConstants;
import com.moko.ble.lib.event.ConnectStatusEvent;
import com.moko.ble.lib.event.OrderTaskResponseEvent;
import com.moko.ble.lib.task.OrderTask;
import com.moko.ble.lib.task.OrderTaskResponse;
import com.moko.ble.lib.utils.MokoUtils;
import com.moko.lib.loraui.dialog.AlertMessageDialog;
import com.moko.lw003plus.activity.BaseActivity;
import com.moko.lw003plus.databinding.Lw003PlusActivityBatteryConsumeBinding;
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

public class BatteryConsumeActivity extends BaseActivity {

    private Lw003PlusActivityBatteryConsumeBinding mBind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBind = Lw003PlusActivityBatteryConsumeBinding.inflate(getLayoutInflater());
        setContentView(mBind.getRoot());
        EventBus.getDefault().register(this);
        showSyncingProgressDialog();
        mBind.tvAdvTimes.postDelayed(() -> {
            List<OrderTask> orderTasks = new ArrayList<>();
            orderTasks.add(OrderTaskAssembler.getBatteryInfo());
            orderTasks.add(OrderTaskAssembler.getBatteryInfoAll());
            orderTasks.add(OrderTaskAssembler.getBatteryInfoLast());
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
                                    case KEY_BATTERY_RESET:
                                        if (result == 1) {
                                            AlertMessageDialog dialog = new AlertMessageDialog();
                                            dialog.setMessage("Reset Successfully！");
                                            dialog.setConfirm("OK");
                                            dialog.setCancelGone();
                                            dialog.show(getSupportFragmentManager());
                                        }
                                        break;
                                }
                            }
                            if (flag == 0x00) {
                                // read
                                switch (configKeyEnum) {
                                    case KEY_BATTERY_INFO:
                                        if (length == 52) {
                                            int runtime = MokoUtils.toInt(Arrays.copyOfRange(value, 5, 9));
                                            mBind.tvRuntime.setText(String.format("%d s", runtime));
                                            int advTimes = MokoUtils.toInt(Arrays.copyOfRange(value, 9, 13));
                                            mBind.tvAdvTimes.setText(String.format("%d times", advTimes));
                                            int scanDuration = MokoUtils.toInt(Arrays.copyOfRange(value, 13, 17));
                                            mBind.tvScanDuration.setText(String.format("%d s", scanDuration));
                                            int gpsFixDuration = MokoUtils.toInt(Arrays.copyOfRange(value, 17, 21));
                                            mBind.tvGpsFixDuration.setText(String.format("%d s", gpsFixDuration));
                                            int axisAwakeDuration = MokoUtils.toInt(Arrays.copyOfRange(value, 21, 25));
                                            mBind.tvAxisAwakeDuration.setText(String.format("%d s", axisAwakeDuration));
                                            int powerDuration = MokoUtils.toInt(Arrays.copyOfRange(value, 25, 29));
                                            mBind.tvPowerDuration.setText(String.format("%d s", powerDuration));
                                            int bleDuration = MokoUtils.toInt(Arrays.copyOfRange(value, 29, 33));
                                            mBind.tvBleDuration.setText(String.format("%d s", bleDuration));
                                            int loraDuration = MokoUtils.toInt(Arrays.copyOfRange(value, 33, 37));
                                            mBind.tvLoraDuration.setText(String.format("%d s", loraDuration));
                                            int gpsDuration = MokoUtils.toInt(Arrays.copyOfRange(value, 37, 41));
                                            mBind.tvGpsDuration.setText(String.format("%d s", gpsDuration));
                                            int wifiDuration = MokoUtils.toInt(Arrays.copyOfRange(value, 41, 45));
                                            mBind.tvWifiDuration.setText(String.format("%d s", wifiDuration));
                                            int loraTransmissionTimes = MokoUtils.toInt(Arrays.copyOfRange(value, 45, 49));
                                            mBind.tvLoraTransmissionTimes.setText(String.format("%d times", loraTransmissionTimes));
                                            int loraPower = MokoUtils.toInt(Arrays.copyOfRange(value, 49, 53));
                                            mBind.tvLoraPower.setText(String.format("%d mAS", loraPower));
                                            String batteryConsumeStr = MokoUtils.getDecimalFormat("0.###").format(MokoUtils.toInt(Arrays.copyOfRange(value, 53, 57)) * 0.001f);
                                            mBind.tvBatteryConsume.setText(String.format("%s mAH", batteryConsumeStr));
                                        }
                                        break;
                                    case KEY_BATTERY_INFO_ALL:
                                        if (length == 52) {
                                            int runtime = MokoUtils.toInt(Arrays.copyOfRange(value, 5, 9));
                                            mBind.tvRuntimeAll.setText(String.format("%d s", runtime));
                                            int advTimes = MokoUtils.toInt(Arrays.copyOfRange(value, 9, 13));
                                            mBind.tvAdvTimesAll.setText(String.format("%d times", advTimes));
                                            int scanDuration = MokoUtils.toInt(Arrays.copyOfRange(value, 13, 17));
                                            mBind.tvScanDurationAll.setText(String.format("%d s", scanDuration));
                                            int gpsFixDuration = MokoUtils.toInt(Arrays.copyOfRange(value, 17, 21));
                                            mBind.tvGpsFixDurationAll.setText(String.format("%d s", gpsFixDuration));
                                            int axisAwakeDuration = MokoUtils.toInt(Arrays.copyOfRange(value, 21, 25));
                                            mBind.tvAxisAwakeDurationAll.setText(String.format("%d s", axisAwakeDuration));
                                            int powerDuration = MokoUtils.toInt(Arrays.copyOfRange(value, 25, 29));
                                            mBind.tvPowerDurationAll.setText(String.format("%d s", powerDuration));
                                            int bleDuration = MokoUtils.toInt(Arrays.copyOfRange(value, 29, 33));
                                            mBind.tvBleDurationAll.setText(String.format("%d s", bleDuration));
                                            int loraDuration = MokoUtils.toInt(Arrays.copyOfRange(value, 33, 37));
                                            mBind.tvLoraDurationAll.setText(String.format("%d s", loraDuration));
                                            int gpsDuration = MokoUtils.toInt(Arrays.copyOfRange(value, 37, 41));
                                            mBind.tvGpsDurationAll.setText(String.format("%d s", gpsDuration));
                                            int wifiDuration = MokoUtils.toInt(Arrays.copyOfRange(value, 41, 45));
                                            mBind.tvWifiDurationAll.setText(String.format("%d s", wifiDuration));
                                            int loraTransmissionTimes = MokoUtils.toInt(Arrays.copyOfRange(value, 45, 49));
                                            mBind.tvLoraTransmissionTimesAll.setText(String.format("%d times", loraTransmissionTimes));
                                            int loraPower = MokoUtils.toInt(Arrays.copyOfRange(value, 49, 53));
                                            mBind.tvLoraPowerAll.setText(String.format("%d mAS", loraPower));
                                            String batteryConsumeStr = MokoUtils.getDecimalFormat("0.###").format(MokoUtils.toInt(Arrays.copyOfRange(value, 53, 57)) * 0.001f);
                                            mBind.tvBatteryConsumeAll.setText(String.format("%s mAH", batteryConsumeStr));
                                        }
                                        break;
                                    case KEY_BATTERY_INFO_LAST:
                                        if (length == 52) {
                                            int runtime = MokoUtils.toInt(Arrays.copyOfRange(value, 5, 9));
                                            mBind.tvRuntimeLast.setText(String.format("%d s", runtime));
                                            int advTimes = MokoUtils.toInt(Arrays.copyOfRange(value, 9, 13));
                                            mBind.tvAdvTimesLast.setText(String.format("%d times", advTimes));
                                            int scanDuration = MokoUtils.toInt(Arrays.copyOfRange(value, 13, 17));
                                            mBind.tvScanDurationLast.setText(String.format("%d s", scanDuration));
                                            int gpsFixDuration = MokoUtils.toInt(Arrays.copyOfRange(value, 17, 21));
                                            mBind.tvGpsFixDurationLast.setText(String.format("%d s", gpsFixDuration));
                                            int axisAwakeDuration = MokoUtils.toInt(Arrays.copyOfRange(value, 21, 25));
                                            mBind.tvAxisAwakeDurationLast.setText(String.format("%d s", axisAwakeDuration));
                                            int powerDuration = MokoUtils.toInt(Arrays.copyOfRange(value, 25, 29));
                                            mBind.tvPowerDurationLast.setText(String.format("%d s", powerDuration));
                                            int bleDuration = MokoUtils.toInt(Arrays.copyOfRange(value, 29, 33));
                                            mBind.tvBleDurationLast.setText(String.format("%d s", bleDuration));
                                            int loraDuration = MokoUtils.toInt(Arrays.copyOfRange(value, 33, 37));
                                            mBind.tvLoraDurationLast.setText(String.format("%d s", loraDuration));
                                            int gpsDuration = MokoUtils.toInt(Arrays.copyOfRange(value, 37, 41));
                                            mBind.tvGpsDurationLast.setText(String.format("%d s", gpsDuration));
                                            int wifiDuration = MokoUtils.toInt(Arrays.copyOfRange(value, 41, 45));
                                            mBind.tvWifiDurationLast.setText(String.format("%d s", wifiDuration));
                                            int loraTransmissionTimes = MokoUtils.toInt(Arrays.copyOfRange(value, 45, 49));
                                            mBind.tvLoraTransmissionTimesLast.setText(String.format("%d times", loraTransmissionTimes));
                                            int loraPower = MokoUtils.toInt(Arrays.copyOfRange(value, 49, 53));
                                            mBind.tvLoraPowerLast.setText(String.format("%d mAS", loraPower));
                                            String batteryConsumeStr = MokoUtils.getDecimalFormat("0.###").format(MokoUtils.toInt(Arrays.copyOfRange(value, 53, 57)) * 0.001f);
                                            mBind.tvBatteryConsumeLast.setText(String.format("%s mAH", batteryConsumeStr));
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

    public void onBatteryReset(View view) {
        if (isWindowLocked())
            return;
        AlertMessageDialog dialog = new AlertMessageDialog();
        dialog.setTitle("Warning！");
        dialog.setMessage("Are you sure to reset battery?");
        dialog.setConfirm("OK");
        dialog.setOnAlertConfirmListener(() -> {
            showSyncingProgressDialog();
            List<OrderTask> orderTasks = new ArrayList<>();
            orderTasks.add(OrderTaskAssembler.setBatteryReset());
            orderTasks.add(OrderTaskAssembler.getBatteryInfo());
            orderTasks.add(OrderTaskAssembler.getBatteryInfoAll());
            orderTasks.add(OrderTaskAssembler.getBatteryInfoLast());
            LoRaLW003PlusMokoSupport.getInstance().sendOrder(orderTasks.toArray(new OrderTask[]{}));
        });
        dialog.show(getSupportFragmentManager());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    public void onBack(View view) {
        backHome();
    }

    private void backHome() {
        finish();
    }
}
