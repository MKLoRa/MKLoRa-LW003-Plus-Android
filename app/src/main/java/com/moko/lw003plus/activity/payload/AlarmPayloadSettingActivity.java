package com.moko.lw003plus.activity.payload;

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
import com.moko.lib.loraui.dialog.BottomDialog;
import com.moko.lw003plus.activity.BaseActivity;
import com.moko.lw003plus.databinding.Lw003PlusActivityAlarmPayloadSettingBinding;
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

public class AlarmPayloadSettingActivity extends BaseActivity {
    private Lw003PlusActivityAlarmPayloadSettingBinding mBind;
    private boolean savedParamsError;
    private boolean mReceiverTag;
    private final ArrayList<String> mValues = new ArrayList<>(8);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBind = Lw003PlusActivityAlarmPayloadSettingBinding.inflate(getLayoutInflater());
        setContentView(mBind.getRoot());
        EventBus.getDefault().register(this);
        mValues.add("No");
        mValues.add("MAC");
        mValues.add("MAC+Data Type");
        mValues.add("MAC+Raw Data");

        mBind.tvDuplicateAlarmFilter.setOnClickListener(v -> {
            if (isWindowLocked()) return;
            int selected = (int) v.getTag();
            BottomDialog dialog = new BottomDialog();
            dialog.setDatas(mValues, selected);
            dialog.setListener(value -> {
                mBind.tvDuplicateAlarmFilter.setTag(value);
                mBind.tvDuplicateAlarmFilter.setText(mValues.get(value));
            });
            dialog.show(getSupportFragmentManager());
        });
        // 注册广播接收器
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
        mReceiverTag = true;
        showSyncingProgressDialog();
        List<OrderTask> orderTasks = new ArrayList<>(2);
        orderTasks.add(OrderTaskAssembler.getAlarmEnable());
        orderTasks.add(OrderTaskAssembler.getDuplicateAlarmFilter());
        orderTasks.add(OrderTaskAssembler.getAlarmFilterPeriod());
        LoRaLW003PlusMokoSupport.getInstance().sendOrder(orderTasks.toArray(new OrderTask[]{}));


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
                byte[] value = response.responseValue;
                if (orderCHAR == OrderCHAR.CHAR_PARAMS) {
                    if (null != value && value.length >= 5) {
                        int header = value[0] & 0xFF;// 0xED
                        int flag = value[1] & 0xFF;// read or write
                        int cmd = MokoUtils.toInt(Arrays.copyOfRange(value, 2, 4));
                        ParamsKeyEnum configKeyEnum = ParamsKeyEnum.fromParamKey(cmd);
                        if (header != 0xED || null == configKeyEnum) return;
                        int length = value[4] & 0xFF;
                        if (flag == 0x01) {
                            // write
                            int result = value[5] & 0xFF;
                            switch (configKeyEnum) {
                                case KEY_ALARM_DATA_ENABLE:
                                case KEY_DUPLICATE_ALARM_DATA_FILTER:
                                    savedParamsError |= result != 1;
                                    break;
                                case KEY_ALARM_DATA_FILTER_PERIOD:
                                    savedParamsError |= result != 1;
                                    if (savedParamsError) {
                                        ToastUtils.showToast(this, "Opps！Save failed. Please check the input characters and try again.");
                                    } else {
                                        ToastUtils.showToast(this, "Save Successfully！");
                                    }
                                    break;
                            }
                        }
                        if (flag == 0x00) {
                            // read
                            switch (configKeyEnum) {
                                case KEY_ALARM_DATA_ENABLE:
                                    if (length == 1) {
                                        int enable = value[5] & 0xff;
                                        mBind.cbAlarmEnable.setChecked(enable == 1);
                                    }
                                    break;
                                case KEY_DUPLICATE_ALARM_DATA_FILTER:
                                    if (length == 1) {
                                        int selected = value[5] & 0xff;
                                        mBind.tvDuplicateAlarmFilter.setTag(selected);
                                        mBind.tvDuplicateAlarmFilter.setText(mValues.get(selected));
                                    }
                                    break;

                                case KEY_ALARM_DATA_FILTER_PERIOD:
                                    if (length == 1) {
                                        int period = value[5] & 0xff;
                                        mBind.etAlarmFilterPeriod.setText(String.valueOf(period));
                                    }
                                    break;

                            }
                        }
                    }
                }
            }
        });
    }

    public void onSave(View view) {
        if (isWindowLocked()) return;
        if (isValid()) {
            showSyncingProgressDialog();
            savedParamsError = false;
            int selected = (int) mBind.tvDuplicateAlarmFilter.getTag();
            int period = Integer.parseInt(mBind.etAlarmFilterPeriod.getText().toString().trim());
            List<OrderTask> orderTasks = new ArrayList<>(2);
            orderTasks.add(OrderTaskAssembler.setAlarmEnable(mBind.cbAlarmEnable.isChecked() ? 1 : 0));
            orderTasks.add(OrderTaskAssembler.setDuplicateAlarmFilter(selected));
            orderTasks.add(OrderTaskAssembler.setAlarmFilterPeriod(period));
            LoRaLW003PlusMokoSupport.getInstance().sendOrder(orderTasks.toArray(new OrderTask[]{}));
        } else {
            ToastUtils.showToast(this, "Para error!");
        }
    }

    private boolean isValid() {
        if (TextUtils.isEmpty(mBind.etAlarmFilterPeriod.getText())) return false;
        int interval = Integer.parseInt(mBind.etAlarmFilterPeriod.getText().toString());
        return interval >= 1 && interval <= 5;
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getAction();
                if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                    int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                    if (blueState == BluetoothAdapter.STATE_TURNING_OFF) {
                        dismissSyncProgressDialog();
                        finish();
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
        finish();
    }
}
