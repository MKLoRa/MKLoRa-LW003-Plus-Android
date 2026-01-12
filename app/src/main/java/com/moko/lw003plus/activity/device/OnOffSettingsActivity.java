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
import com.moko.lw003plus.databinding.Lw003ProActivityOnOffSettingsBinding;
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

public class OnOffSettingsActivity extends BaseActivity {

    private Lw003ProActivityOnOffSettingsBinding mBind;
    private boolean savedParamsError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBind = Lw003ProActivityOnOffSettingsBinding.inflate(getLayoutInflater());
        setContentView(mBind.getRoot());


        mBind.ivPowerOff.setOnClickListener(v -> {
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
        });

        EventBus.getDefault().register(this);
        showSyncingProgressDialog();
        mBind.cbOffByButton.postDelayed(() -> {
            List<OrderTask> orderTasks = new ArrayList<>();
            orderTasks.add(OrderTaskAssembler.getOffByButton());
            orderTasks.add(OrderTaskAssembler.getShutdownPayloadEnable());
            orderTasks.add(OrderTaskAssembler.getAutoPowerOn());
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
                                    case KEY_OFF_BY_BUTTON:
                                    case KEY_SHUTDOWN_PAYLOAD_ENABLE:
                                        if (result != 1) {
                                            savedParamsError = true;
                                        }
                                        break;
                                    case KEY_AUTO_POWER_ON_ENABLE:
                                        if (result != 1) {
                                            savedParamsError = true;
                                        }
                                        if (savedParamsError) {
                                            ToastUtils.showToast(OnOffSettingsActivity.this, "Opps！Save failed. Please check the input characters and try again.");
                                        } else {
                                            ToastUtils.showToast(this, "Save Successfully！");
                                        }
                                        break;
                                }
                            }
                            if (flag == 0x00) {
                                // read
                                switch (configKeyEnum) {
                                    case KEY_OFF_BY_BUTTON:
                                        if (length > 0) {
                                            int enable = value[5] & 0xFF;
                                            mBind.cbOffByButton.setChecked(enable == 1);
                                        }
                                        break;
                                    case KEY_SHUTDOWN_PAYLOAD_ENABLE:
                                        if (length > 0) {
                                            int enable = value[5] & 0xFF;
                                            mBind.cbShutDownPayload.setChecked(enable == 1);
                                        }
                                        break;
                                    case KEY_AUTO_POWER_ON_ENABLE:
                                        if (length > 0) {
                                            int enable = value[5] & 0xFF;
                                            mBind.cbAutoPowerOn.setChecked(enable == 1);
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
        saveParams();

    }

    private void saveParams() {
        savedParamsError = false;
        List<OrderTask> orderTasks = new ArrayList<>();
        orderTasks.add(OrderTaskAssembler.setOffByButton(mBind.cbOffByButton.isChecked() ? 1 : 0));
        orderTasks.add(OrderTaskAssembler.setShutdownEnable(mBind.cbShutDownPayload.isChecked() ? 1 : 0));
        orderTasks.add(OrderTaskAssembler.setAutoPowerOnEnable(mBind.cbOffByButton.isChecked() ? 1 : 0));
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
