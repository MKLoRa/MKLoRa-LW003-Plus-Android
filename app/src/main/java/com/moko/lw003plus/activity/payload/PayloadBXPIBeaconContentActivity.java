package com.moko.lw003plus.activity.payload;


import android.os.Bundle;
import android.view.View;

import com.moko.ble.lib.MokoConstants;
import com.moko.ble.lib.event.ConnectStatusEvent;
import com.moko.ble.lib.event.OrderTaskResponseEvent;
import com.moko.ble.lib.task.OrderTask;
import com.moko.ble.lib.task.OrderTaskResponse;
import com.moko.ble.lib.utils.MokoUtils;
import com.moko.lw003plus.activity.BaseActivity;
import com.moko.lw003plus.databinding.Lw003PlusActivityPayloadBxpIbeaconContentBinding;
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

public class PayloadBXPIBeaconContentActivity extends BaseActivity {

    private Lw003PlusActivityPayloadBxpIbeaconContentBinding mBind;
    private boolean savedParamsError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBind = Lw003PlusActivityPayloadBxpIbeaconContentBinding.inflate(getLayoutInflater());
        setContentView(mBind.getRoot());
        EventBus.getDefault().register(this);

        showSyncingProgressDialog();
        mBind.cbMac.postDelayed(() -> {
            List<OrderTask> orderTasks = new ArrayList<>();
            orderTasks.add(OrderTaskAssembler.getPayloadBXPIBeaconContent());
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
                                    case KEY_PAYLOAD_BXP_IBEACON_CONTENT:
                                        savedParamsError |= result != 1;
                                        if (savedParamsError) {
                                            ToastUtils.showToast(PayloadBXPIBeaconContentActivity.this, "Opps！Save failed. Please check the input characters and try again.");
                                        } else {
                                            ToastUtils.showToast(this, "Save Successfully！");
                                        }
                                        break;
                                }
                            }
                            if (flag == 0x00) {
                                // read
                                switch (configKeyEnum) {
                                    case KEY_PAYLOAD_BXP_IBEACON_CONTENT:
                                        if (length > 0) {
                                            int data = MokoUtils.toInt(Arrays.copyOfRange(value, 5, 5 + length));
                                            mBind.cbMac.setChecked((data & 0x01) == 0x01);
                                            mBind.cbRssi.setChecked((data & 0x02) == 0x02);
                                            mBind.cbTimestamp.setChecked((data & 0x04) == 0x04);
                                            mBind.cbUuid.setChecked((data & 0x08) == 0x08);
                                            mBind.cbMajor.setChecked((data & 0x10) == 0x10);
                                            mBind.cbMinor.setChecked((data & 0x20) == 0x20);
                                            mBind.cbRssi1m.setChecked((data & 0x40) == 0x40);
                                            mBind.cbTxPower.setChecked((data & 0x80) == 0x80);
                                            mBind.cbAdvInterval.setChecked((data & 0x0100) == 0x0100);
                                            mBind.cbRawDataAdv.setChecked((data & 0x0200) == 0x0200);
                                            mBind.cbRawDataResp.setChecked((data & 0x0400) == 0x0400);
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
        int flag = (mBind.cbMac.isChecked() ? 0x01 : 0x00)
                | (mBind.cbRssi.isChecked() ? 0x02 : 0x00)
                | (mBind.cbTimestamp.isChecked() ? 0x04 : 0x00)
                | (mBind.cbUuid.isChecked() ? 0x08 : 0x00)
                | (mBind.cbMajor.isChecked() ? 0x10 : 0x00)
                | (mBind.cbMinor.isChecked() ? 0x20 : 0x00)
                | (mBind.cbRssi1m.isChecked() ? 0x40 : 0x00)
                | (mBind.cbTxPower.isChecked() ? 0x80 : 0x00)
                | (mBind.cbAdvInterval.isChecked() ? 0x0100 : 0x00)
                | (mBind.cbRawDataAdv.isChecked() ? 0x0200 : 0x00)
                | (mBind.cbRawDataResp.isChecked() ? 0x0400 : 0x00);
        orderTasks.add(OrderTaskAssembler.setPayloadBXPIBeaconContent(flag));
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
