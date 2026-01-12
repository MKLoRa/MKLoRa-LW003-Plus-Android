package com.moko.lw003plus.activity.lora;


import android.os.Bundle;
import android.view.View;

import com.moko.ble.lib.MokoConstants;
import com.moko.ble.lib.event.ConnectStatusEvent;
import com.moko.ble.lib.event.OrderTaskResponseEvent;
import com.moko.ble.lib.task.OrderTask;
import com.moko.ble.lib.task.OrderTaskResponse;
import com.moko.ble.lib.utils.MokoUtils;
import com.moko.lw003plus.activity.BaseActivity;
import com.moko.lw003plus.databinding.Lw003ProActivityMulticastGroupBinding;
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

public class MulticastGroupActivity extends BaseActivity {

    private Lw003ProActivityMulticastGroupBinding mBind;
    private boolean savedParamsError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBind = Lw003ProActivityMulticastGroupBinding.inflate(getLayoutInflater());
        setContentView(mBind.getRoot());
        EventBus.getDefault().register(this);
        mBind.cbEnable.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mBind.clMulticastGroupParams.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });
        showSyncingProgressDialog();
        mBind.cbEnable.postDelayed(() -> {
            List<OrderTask> orderTasks = new ArrayList<>();
            orderTasks.add(OrderTaskAssembler.getMulticastGroupEnable());
            orderTasks.add(OrderTaskAssembler.getMulticastGroupAddr());
            orderTasks.add(OrderTaskAssembler.getMulticastAppSKey());
            orderTasks.add(OrderTaskAssembler.getMulticastNwkSKey());
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
                                    case KEY_MULTICAST_GROUP_ADDR:
                                    case KEY_MULTICAST_APP_SKEY:
                                    case KEY_MULTICAST_NWK_SKEY:
                                        if (result != 1) {
                                            savedParamsError = true;
                                        }
                                        break;
                                    case KEY_MULTICAST_GROUP_ENABLE:
                                        if (result != 1) {
                                            savedParamsError = true;
                                        }
                                        if (savedParamsError) {
                                            ToastUtils.showToast(MulticastGroupActivity.this, "Opps！Save failed. Please check the input characters and try again.");
                                        } else {
                                            ToastUtils.showToast(this, "Save Successfully！");
                                        }
                                        break;
                                }
                            }
                            if (flag == 0x00) {
                                // read
                                switch (configKeyEnum) {
                                    case KEY_MULTICAST_GROUP_ADDR:
                                        if (length > 0) {
                                            String mcAddr = MokoUtils.bytesToHexString(Arrays.copyOfRange(value, 5, 5 + length));
                                            mBind.etMcAddr.setText(mcAddr);
                                        }
                                        break;
                                    case KEY_MULTICAST_APP_SKEY:
                                        if (length > 0) {
                                            String mcAppSkey = MokoUtils.bytesToHexString(Arrays.copyOfRange(value, 5, 5 + length));
                                            mBind.etMcAppSkey.setText(mcAppSkey);
                                        }
                                        break;
                                    case KEY_MULTICAST_NWK_SKEY:
                                        if (length > 0) {
                                            String mcNwkSkey = MokoUtils.bytesToHexString(Arrays.copyOfRange(value, 5, 5 + length));
                                            mBind.etMcNwkSkey.setText(mcNwkSkey);
                                        }
                                        break;
                                    case KEY_MULTICAST_GROUP_ENABLE:
                                        if (length > 0) {
                                            int enable = value[5] & 0xFF;
                                            mBind.cbEnable.setChecked(enable == 1);
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
        boolean isEnable = mBind.cbEnable.isChecked();
        if (isEnable) {
            final String mcAddr = mBind.etMcAddr.getText().toString();
            final String mcAppSkey = mBind.etMcAppSkey.getText().toString();
            final String mcNwkSkey = mBind.etMcNwkSkey.getText().toString();
            if (mcAddr.length() != 8) {
                ToastUtils.showToast(this, "Para error!");
                return false;
            }
            if (mcAppSkey.length() != 32) {
                ToastUtils.showToast(this, "Para error!");
                return false;
            }
            if (mcNwkSkey.length() != 32) {
                ToastUtils.showToast(this, "Para error!");
                return false;
            }
        }
        return true;
    }


    private void saveParams() {
        boolean isEnable = mBind.cbEnable.isChecked();
        final String mcAddr = mBind.etMcAddr.getText().toString();
        final String mcAppSkey = mBind.etMcAppSkey.getText().toString();
        final String mcNwkSkey = mBind.etMcNwkSkey.getText().toString();
        savedParamsError = false;
        List<OrderTask> orderTasks = new ArrayList<>();
        if (isEnable) {
            orderTasks.add(OrderTaskAssembler.setMulticastGroupAddr(mcAddr));
            orderTasks.add(OrderTaskAssembler.setMulticastAppSkey(mcAppSkey));
            orderTasks.add(OrderTaskAssembler.setMulticastNwkSkey(mcNwkSkey));
            orderTasks.add(OrderTaskAssembler.setMulticastGroupEnable(1));
        } else {
            orderTasks.add(OrderTaskAssembler.setMulticastGroupEnable(0));
        }
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
