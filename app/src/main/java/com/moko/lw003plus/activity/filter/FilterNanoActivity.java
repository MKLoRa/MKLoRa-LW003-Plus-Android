package com.moko.lw003plus.activity.filter;


import android.os.Bundle;
import android.view.View;

import com.moko.ble.lib.MokoConstants;
import com.moko.ble.lib.event.ConnectStatusEvent;
import com.moko.ble.lib.event.OrderTaskResponseEvent;
import com.moko.ble.lib.task.OrderTask;
import com.moko.ble.lib.task.OrderTaskResponse;
import com.moko.ble.lib.utils.MokoUtils;
import com.moko.lib.loraui.dialog.BottomDialog;
import com.moko.lw003plus.activity.BaseActivity;
import com.moko.lw003plus.databinding.Lw003PlusActivityFilterNanoBinding;
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

public class FilterNanoActivity extends BaseActivity {


    private Lw003PlusActivityFilterNanoBinding mBind;

    private boolean savedParamsError;

    private String[] mAdvType = new String[]{"Null", "Normal Adv Type", "Trigger Adv Type" };
    private String[] mTriggerStatus = new String[]{"Null", "No Alarm", "Cut-off Alarm", "Button Alarm", "Button Alarm and Cut-off Alarm" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBind = Lw003PlusActivityFilterNanoBinding.inflate(getLayoutInflater());
        setContentView(mBind.getRoot());
        EventBus.getDefault().register(this);

        showSyncingProgressDialog();
        mBind.tvTitle.postDelayed(() -> {
            List<OrderTask> orderTasks = new ArrayList<>();
            orderTasks.add(OrderTaskAssembler.getFilterNanoEnable());
            orderTasks.add(OrderTaskAssembler.getFilterNanoAdvType());
            orderTasks.add(OrderTaskAssembler.getFilterNanoTriggerStatus());
            LoRaLW003PlusMokoSupport.getInstance().sendOrder(orderTasks.toArray(new OrderTask[]{}));
        }, 500);
    }


    @Subscribe(threadMode = ThreadMode.POSTING, priority = 400)
    public void onConnectStatusEvent(ConnectStatusEvent event) {
        final String action = event.getAction();
        runOnUiThread(() -> {
            if (MokoConstants.ACTION_DISCONNECTED.equals(action)) {
                finish();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.POSTING, priority = 400)
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
                                    case KEY_FILTER_NANO_ADV_TYPE:
                                    case KEY_FILTER_NANO_TRIGGER_STATUS:
                                        if (result != 1) {
                                            savedParamsError = true;
                                        }
                                        break;
                                    case KEY_FILTER_NANO_ENABLE:
                                        if (result != 1) {
                                            savedParamsError = true;
                                        }
                                        if (savedParamsError) {
                                            ToastUtils.showToast(FilterNanoActivity.this, "Opps！Save failed. Please check the input characters and try again.");
                                        } else {
                                            ToastUtils.showToast(this, "Save Successfully！");
                                        }
                                        break;
                                }
                            }
                            if (flag == 0x00) {
                                // read
                                switch (configKeyEnum) {
                                    case KEY_FILTER_NANO_ADV_TYPE:
                                        if (length > 0) {
                                            int type = value[5] & 0xFF;
                                            mBind.tvNanoType.setText(mAdvType[type]);
                                            mBind.tvNanoType.setTag(type);
                                        }
                                        break;
                                    case KEY_FILTER_NANO_TRIGGER_STATUS:
                                        if (length > 0) {
                                            int status = value[5] & 0xFF;
                                            mBind.tvTriggerStatus.setText(mTriggerStatus[status]);
                                            mBind.tvTriggerStatus.setTag(status);
                                        }
                                        break;
                                    case KEY_FILTER_NANO_ENABLE:
                                        if (length > 0) {
                                            mBind.cbNanoEnable.setChecked(value[5] == 1);
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

    public void onSave(View view) {
        if (isWindowLocked())
            return;
        int type = (int) mBind.tvNanoType.getTag();
        int status = (int) mBind.tvTriggerStatus.getTag();
        showSyncingProgressDialog();
        List<OrderTask> orderTasks = new ArrayList<>();
        orderTasks.add(OrderTaskAssembler.setFilterNanoAdvType(type));
        orderTasks.add(OrderTaskAssembler.setFilterNanoTriggerStatus(status));
        orderTasks.add(OrderTaskAssembler.setFilterNanoEnable(mBind.cbNanoEnable.isChecked() ? 1 : 0));
        LoRaLW003PlusMokoSupport.getInstance().sendOrder(orderTasks.toArray(new OrderTask[]{}));
    }

    public void onNanoType(View view) {
        if (isWindowLocked())
            return;
        int selected = (int) view.getTag();
        BottomDialog dialog = new BottomDialog();
        dialog.setDatas(new ArrayList<>(Arrays.asList(mAdvType)), selected);
        dialog.setListener(value -> {
            view.setTag(value);
            mBind.tvNanoType.setText(mAdvType[value]);
        });
        dialog.show(getSupportFragmentManager());
    }

    public void onTriggerStatus(View view) {
        if (isWindowLocked())
            return;
        int selected = (int) view.getTag();
        BottomDialog dialog = new BottomDialog();
        dialog.setDatas(new ArrayList<>(Arrays.asList(mTriggerStatus)), selected);
        dialog.setListener(value -> {
            view.setTag(value);
            mBind.tvTriggerStatus.setText(mTriggerStatus[value]);
        });
        dialog.show(getSupportFragmentManager());
    }
}
