package com.moko.lw003plus.activity.general;


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
import com.moko.lw003plus.databinding.Lw003ProActivityThSettingsBinding;
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

public class THSettingsActivity extends BaseActivity {

    private Lw003ProActivityThSettingsBinding mBind;
    private boolean savedParamsError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBind = Lw003ProActivityThSettingsBinding.inflate(getLayoutInflater());
        setContentView(mBind.getRoot());
        EventBus.getDefault().register(this);
        showSyncingProgressDialog();
        mBind.cbEnable.postDelayed(() -> {
            List<OrderTask> orderTasks = new ArrayList<>();
            orderTasks.add(OrderTaskAssembler.getTHSampleRateEnable());
            orderTasks.add(OrderTaskAssembler.getTHSampleRate());
            orderTasks.add(OrderTaskAssembler.getTempCurrent());
            orderTasks.add(OrderTaskAssembler.getHumidityCurrent());
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
                                    case KEY_TEMP_SAMPLE_RATE:
                                        if (result != 1) {
                                            savedParamsError = true;
                                        }
                                        break;
                                    case KEY_TEMP_MONITOR_ENABLE:
                                        if (result != 1) {
                                            savedParamsError = true;
                                        }
                                        if (savedParamsError) {
                                            ToastUtils.showToast(THSettingsActivity.this, "Opps！Save failed. Please check the input characters and try again.");
                                        } else {
                                            ToastUtils.showToast(this, "Save Successfully！");
                                        }
                                        break;
                                }
                            }
                            if (flag == 0x00) {
                                // read
                                switch (configKeyEnum) {
                                    case KEY_TEMP_SAMPLE_RATE:
                                        if (length > 0) {
                                            int rate = value[5] & 0xFF;
                                            mBind.etSampleRate.setText(String.valueOf(rate));
                                        }
                                        break;
                                    case KEY_TEMP_MONITOR_ENABLE:
                                        if (length > 0) {
                                            int enable = value[5] & 0xFF;
                                            mBind.cbEnable.setChecked(enable == 1);
                                        }
                                        break;
                                    case KEY_TEMP_CURRENT:
                                        if (length > 0) {
                                            byte[] tempBytes = Arrays.copyOfRange(value, 5, 7);
                                            int temp = MokoUtils.toInt(tempBytes);
                                            String tempStr = "--";
                                            if (temp != 0xFFFF) {
                                                tempStr = MokoUtils.getDecimalFormat("#.##").format(MokoUtils.toIntSigned(tempBytes) * 0.01);
                                            }
                                            mBind.tvTempCurrent.setText(String.format("%s℃", tempStr));
                                        }
                                        break;
                                    case KEY_HUMIDITY_CURRENT:
                                        if (length > 0) {
                                            byte[] humidityBytes = Arrays.copyOfRange(value, 5, 7);
                                            int humidity = MokoUtils.toInt(humidityBytes);
                                            String humidityStr = "--";
                                            if (humidity != 0xFFFF) {
                                                humidityStr = MokoUtils.getDecimalFormat("#.##").format(MokoUtils.toIntSigned(humidityBytes) * 0.01);
                                            }
                                            mBind.tvHumidityCurrent.setText(String.format("%s%%", humidityStr));
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
        final String sampleRateStr = mBind.etSampleRate.getText().toString();
        if (TextUtils.isEmpty(sampleRateStr)) {
            return false;
        }
        final int sampleRate = Integer.parseInt(sampleRateStr);
        if (sampleRate < 1 || sampleRate > 10) {
            return false;
        }
        return true;
    }


    private void saveParams() {
        final boolean isEnable = mBind.cbEnable.isChecked();
        final String sampleRateStr = mBind.etSampleRate.getText().toString();
        final int sampleRate = Integer.parseInt(sampleRateStr);
        savedParamsError = false;
        List<OrderTask> orderTasks = new ArrayList<>();
        orderTasks.add(OrderTaskAssembler.setTHSampleRate(sampleRate));
        orderTasks.add(OrderTaskAssembler.setTHSampleRateEnable(isEnable ? 1 : 0));
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
}
