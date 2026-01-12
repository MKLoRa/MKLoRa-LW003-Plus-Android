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
import com.moko.lw003plus.databinding.Lw003ProActivityMessageTypeSettingsBinding;
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

public class MessageTypeActivity extends BaseActivity {

    private Lw003ProActivityMessageTypeSettingsBinding mBind;
    private boolean savedParamsError;
    private ArrayList<String> mMessagePayloadList;
    private ArrayList<String> mMaxRetransmissionTimesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBind = Lw003ProActivityMessageTypeSettingsBinding.inflate(getLayoutInflater());
        setContentView(mBind.getRoot());
        EventBus.getDefault().register(this);
        mMessagePayloadList = new ArrayList<>();
        mMessagePayloadList.add("Unconfirmed");
        mMessagePayloadList.add("Confirmed");
        mMaxRetransmissionTimesList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            mMaxRetransmissionTimesList.add(String.valueOf(i));
        }
        showSyncingProgressDialog();
        mBind.tvBack.postDelayed(() -> {
            List<OrderTask> orderTasks = new ArrayList<>();
            orderTasks.add(OrderTaskAssembler.getDeviceInfoPayloadSettings());
            orderTasks.add(OrderTaskAssembler.getEventPayloadSettings());
            orderTasks.add(OrderTaskAssembler.getBeaconPayloadSettings());
            orderTasks.add(OrderTaskAssembler.getHeartbeatPayloadSettings());
            orderTasks.add(OrderTaskAssembler.getLowPowerPayloadSettings());
            orderTasks.add(OrderTaskAssembler.getAlarmPayloadSettings());
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
                                    case KEY_BEACON_PAYLOAD:
                                    case KEY_EVENT_PAYLOAD:
                                    case KEY_DEVICE_INFO_PAYLOAD:
                                    case KEY_ALARM_PAYLOAD:
                                    case KEY_LOW_POWER_PAYLOAD:
                                        if (result != 1) {
                                            savedParamsError = true;
                                        }
                                        break;
                                    case KEY_HEARTBEAT_PAYLOAD:
                                        if (result != 1) {
                                            savedParamsError = true;
                                        }
                                        if (savedParamsError) {
                                            ToastUtils.showToast(MessageTypeActivity.this, "Opps！Save failed. Please check the input characters and try again.");
                                        } else {
                                            ToastUtils.showToast(this, "Save Successfully！");
                                        }
                                        break;
                                }
                            }
                            if (flag == 0x00) {
                                // read
                                switch (configKeyEnum) {
                                    case KEY_BEACON_PAYLOAD:
                                        if (length > 0) {
                                            int type = value[5];
                                            int times = value[6] - 1;
                                            mBind.tvBeaconPayloadType.setTag(type);
                                            mBind.tvBeaconPayloadType.setText(mMessagePayloadList.get(type));
                                            mBind.tvBeaconPayloadTimes.setTag(times);
                                            mBind.tvBeaconPayloadTimes.setText(mMaxRetransmissionTimesList.get(times));
                                            mBind.clBeaconPayloadTimes.setVisibility(type == 0 ? View.GONE : View.VISIBLE);
                                        }
                                        break;
                                    case KEY_EVENT_PAYLOAD:
                                        if (length > 0) {
                                            int type = value[5];
                                            int times = value[6] - 1;
                                            mBind.tvEventPayloadType.setTag(type);
                                            mBind.tvEventPayloadType.setText(mMessagePayloadList.get(type));
                                            mBind.tvEventPayloadTimes.setTag(times);
                                            mBind.tvEventPayloadTimes.setText(mMaxRetransmissionTimesList.get(times));
                                            mBind.clEventPayloadTimes.setVisibility(type == 0 ? View.GONE : View.VISIBLE);
                                        }
                                        break;
                                    case KEY_DEVICE_INFO_PAYLOAD:
                                        if (length > 0) {
                                            int type = value[5];
                                            int times = value[6] - 1;
                                            mBind.tvDeviceInfoPayloadType.setTag(type);
                                            mBind.tvDeviceInfoPayloadType.setText(mMessagePayloadList.get(type));
                                            mBind.tvDeviceInfoPayloadTimes.setTag(times);
                                            mBind.tvDeviceInfoPayloadTimes.setText(mMaxRetransmissionTimesList.get(times));
                                            mBind.clDeviceInfoPayloadTimes.setVisibility(type == 0 ? View.GONE : View.VISIBLE);
                                        }
                                        break;
                                    case KEY_HEARTBEAT_PAYLOAD:
                                        if (length > 0) {
                                            int type = value[5];
                                            int times = value[6] - 1;
                                            mBind.tvHeartbeatPayloadType.setTag(type);
                                            mBind.tvHeartbeatPayloadType.setText(mMessagePayloadList.get(type));
                                            mBind.tvHeartbeatPayloadTimes.setTag(times);
                                            mBind.tvHeartbeatPayloadTimes.setText(mMaxRetransmissionTimesList.get(times));
                                            mBind.clHeartbeatPayloadTimes.setVisibility(type == 0 ? View.GONE : View.VISIBLE);
                                        }
                                        break;
                                    case KEY_ALARM_PAYLOAD:
                                        if (length > 0) {
                                            int type = value[5];
                                            int times = value[6] - 1;
                                            mBind.tvAlarmPayloadType.setTag(type);
                                            mBind.tvAlarmPayloadType.setText(mMessagePayloadList.get(type));
                                            mBind.tvAlarmPayloadTimes.setTag(times);
                                            mBind.tvAlarmPayloadTimes.setText(mMaxRetransmissionTimesList.get(times));
                                            mBind.clAlarmPayloadTimes.setVisibility(type == 0 ? View.GONE : View.VISIBLE);
                                        }
                                        break;
                                    case KEY_LOW_POWER_PAYLOAD:
                                        if (length > 0) {
                                            int type = value[5];
                                            int times = value[6] - 1;
                                            mBind.tvLowPowerPayloadType.setTag(type);
                                            mBind.tvLowPowerPayloadType.setText(mMessagePayloadList.get(type));
                                            mBind.tvLowPowerPayloadTimes.setTag(times);
                                            mBind.tvLowPowerPayloadTimes.setText(mMaxRetransmissionTimesList.get(times));
                                            mBind.clLowPowerPayloadTimes.setVisibility(type == 0 ? View.GONE : View.VISIBLE);
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
        int beaconType = (int) mBind.tvBeaconPayloadType.getTag();
        int beaconTimes = (int) mBind.tvBeaconPayloadTimes.getTag();
        int alarmType = (int) mBind.tvAlarmPayloadType.getTag();
        int alarmTimes = (int) mBind.tvAlarmPayloadTimes.getTag();
        int deviceInfoType = (int) mBind.tvDeviceInfoPayloadType.getTag();
        int deviceInfoTimes = (int) mBind.tvDeviceInfoPayloadTimes.getTag();
        int eventType = (int) mBind.tvEventPayloadType.getTag();
        int eventTimes = (int) mBind.tvEventPayloadTimes.getTag();
        int heartbeatType = (int) mBind.tvHeartbeatPayloadType.getTag();
        int heartbeatTimes = (int) mBind.tvHeartbeatPayloadTimes.getTag();
        int lowPowerType = (int) mBind.tvLowPowerPayloadType.getTag();
        int lowPowerTimes = (int) mBind.tvLowPowerPayloadTimes.getTag();
        orderTasks.add(OrderTaskAssembler.setBeaconPayloadSettings(beaconType, beaconTimes + 1));
        orderTasks.add(OrderTaskAssembler.setEventPayloadSettings(eventType, eventTimes + 1));
        orderTasks.add(OrderTaskAssembler.setDeviceInfoPayloadSettings(deviceInfoType, deviceInfoTimes + 1));
        orderTasks.add(OrderTaskAssembler.setAlarmPayloadSettings(alarmType, alarmTimes + 1));
        orderTasks.add(OrderTaskAssembler.setLowPowerPayloadSettings(lowPowerType, lowPowerTimes + 1));
        orderTasks.add(OrderTaskAssembler.setHeartbeatPayloadSettings(heartbeatType, heartbeatTimes + 1));
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

    public void selectBeaconPayloadType(View view) {
        if (isWindowLocked()) return;
        int selected = (int) view.getTag();
        BottomDialog bottomDialog = new BottomDialog();
        bottomDialog.setDatas(mMessagePayloadList, selected);
        bottomDialog.setListener(value -> {
            mBind.tvBeaconPayloadType.setTag(value);
            mBind.tvBeaconPayloadType.setText(mMessagePayloadList.get(value));
            mBind.clBeaconPayloadTimes.setVisibility(value == 0 ? View.GONE : View.VISIBLE);
        });
        bottomDialog.show(getSupportFragmentManager());

    }

    public void selectBeaconPayloadTimes(View view) {
        if (isWindowLocked()) return;
        int selected = (int) view.getTag();
        BottomDialog bottomDialog = new BottomDialog();
        bottomDialog.setDatas(mMaxRetransmissionTimesList, selected);
        bottomDialog.setListener(value -> {
            mBind.tvBeaconPayloadTimes.setTag(value);
            mBind.tvBeaconPayloadTimes.setText(mMaxRetransmissionTimesList.get(value));
        });
        bottomDialog.show(getSupportFragmentManager());
    }

    public void selectEventPayloadType(View view) {
        if (isWindowLocked()) return;
        int selected = (int) view.getTag();
        BottomDialog bottomDialog = new BottomDialog();
        bottomDialog.setDatas(mMessagePayloadList, selected);
        bottomDialog.setListener(value -> {
            mBind.tvEventPayloadType.setTag(value);
            mBind.tvEventPayloadType.setText(mMessagePayloadList.get(value));
            mBind.clEventPayloadTimes.setVisibility(value == 0 ? View.GONE : View.VISIBLE);
        });
        bottomDialog.show(getSupportFragmentManager());

    }

    public void selectEventPayloadTimes(View view) {
        if (isWindowLocked()) return;
        int selected = (int) view.getTag();
        BottomDialog bottomDialog = new BottomDialog();
        bottomDialog.setDatas(mMaxRetransmissionTimesList, selected);
        bottomDialog.setListener(value -> {
            mBind.tvEventPayloadTimes.setTag(value);
            mBind.tvEventPayloadTimes.setText(mMaxRetransmissionTimesList.get(value));
        });
        bottomDialog.show(getSupportFragmentManager());
    }

    public void selectDeviceInfoPayloadType(View view) {
        if (isWindowLocked()) return;
        int selected = (int) view.getTag();
        BottomDialog bottomDialog = new BottomDialog();
        bottomDialog.setDatas(mMessagePayloadList, selected);
        bottomDialog.setListener(value -> {
            mBind.tvDeviceInfoPayloadType.setTag(value);
            mBind.tvDeviceInfoPayloadType.setText(mMessagePayloadList.get(value));
            mBind.clDeviceInfoPayloadTimes.setVisibility(value == 0 ? View.GONE : View.VISIBLE);
        });
        bottomDialog.show(getSupportFragmentManager());

    }

    public void selectDeviceInfoPayloadTimes(View view) {
        if (isWindowLocked()) return;
        int selected = (int) view.getTag();
        BottomDialog bottomDialog = new BottomDialog();
        bottomDialog.setDatas(mMaxRetransmissionTimesList, selected);
        bottomDialog.setListener(value -> {
            mBind.tvDeviceInfoPayloadTimes.setTag(value);
            mBind.tvDeviceInfoPayloadTimes.setText(mMaxRetransmissionTimesList.get(value));
        });
        bottomDialog.show(getSupportFragmentManager());
    }

    public void selectHeartbeatPayloadType(View view) {
        if (isWindowLocked()) return;
        int selected = (int) view.getTag();
        BottomDialog bottomDialog = new BottomDialog();
        bottomDialog.setDatas(mMessagePayloadList, selected);
        bottomDialog.setListener(value -> {
            mBind.tvHeartbeatPayloadType.setTag(value);
            mBind.tvHeartbeatPayloadType.setText(mMessagePayloadList.get(value));
            mBind.clHeartbeatPayloadTimes.setVisibility(value == 0 ? View.GONE : View.VISIBLE);
        });
        bottomDialog.show(getSupportFragmentManager());

    }

    public void selectHeartbeatPayloadTimes(View view) {
        if (isWindowLocked()) return;
        int selected = (int) view.getTag();
        BottomDialog bottomDialog = new BottomDialog();
        bottomDialog.setDatas(mMaxRetransmissionTimesList, selected);
        bottomDialog.setListener(value -> {
            mBind.tvHeartbeatPayloadTimes.setTag(value);
            mBind.tvHeartbeatPayloadTimes.setText(mMaxRetransmissionTimesList.get(value));
        });
        bottomDialog.show(getSupportFragmentManager());
    }

    public void selectAlarmPayloadType(View view) {
        if (isWindowLocked()) return;
        int selected = (int) view.getTag();
        BottomDialog bottomDialog = new BottomDialog();
        bottomDialog.setDatas(mMessagePayloadList, selected);
        bottomDialog.setListener(value -> {
            mBind.tvAlarmPayloadType.setTag(value);
            mBind.tvAlarmPayloadType.setText(mMessagePayloadList.get(value));
            mBind.clAlarmPayloadTimes.setVisibility(value == 0 ? View.GONE : View.VISIBLE);
        });
        bottomDialog.show(getSupportFragmentManager());

    }

    public void selectAlarmPayloadTimes(View view) {
        if (isWindowLocked()) return;
        int selected = (int) view.getTag();
        BottomDialog bottomDialog = new BottomDialog();
        bottomDialog.setDatas(mMaxRetransmissionTimesList, selected);
        bottomDialog.setListener(value -> {
            mBind.tvAlarmPayloadTimes.setTag(value);
            mBind.tvAlarmPayloadTimes.setText(mMaxRetransmissionTimesList.get(value));
        });
        bottomDialog.show(getSupportFragmentManager());
    }

    public void selectLowPowerPayloadType(View view) {
        if (isWindowLocked()) return;
        int selected = (int) view.getTag();
        BottomDialog bottomDialog = new BottomDialog();
        bottomDialog.setDatas(mMessagePayloadList, selected);
        bottomDialog.setListener(value -> {
            mBind.tvLowPowerPayloadType.setTag(value);
            mBind.tvLowPowerPayloadType.setText(mMessagePayloadList.get(value));
            mBind.clLowPowerPayloadTimes.setVisibility(value == 0 ? View.GONE : View.VISIBLE);
        });
        bottomDialog.show(getSupportFragmentManager());

    }

    public void selectLowPowerPayloadTimes(View view) {
        if (isWindowLocked()) return;
        int selected = (int) view.getTag();
        BottomDialog bottomDialog = new BottomDialog();
        bottomDialog.setDatas(mMaxRetransmissionTimesList, selected);
        bottomDialog.setListener(value -> {
            mBind.tvLowPowerPayloadTimes.setTag(value);
            mBind.tvLowPowerPayloadTimes.setText(mMaxRetransmissionTimesList.get(value));
        });
        bottomDialog.show(getSupportFragmentManager());
    }
}
