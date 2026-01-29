package com.moko.lw003plus.activity.payload;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.moko.ble.lib.MokoConstants;
import com.moko.ble.lib.event.ConnectStatusEvent;
import com.moko.ble.lib.event.OrderTaskResponseEvent;
import com.moko.ble.lib.task.OrderTask;
import com.moko.ble.lib.task.OrderTaskResponse;
import com.moko.ble.lib.utils.MokoUtils;
import com.moko.lw003plus.R;
import com.moko.lw003plus.activity.BaseActivity;
import com.moko.lw003plus.databinding.Lw003PlusActivityPayloadOtherTypeContentBinding;
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

public class PayloadOtherTypeContentActivity extends BaseActivity {

    private Lw003PlusActivityPayloadOtherTypeContentBinding mBind;
    private ArrayList<String> mList;
    private boolean savedParamsError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBind = Lw003PlusActivityPayloadOtherTypeContentBinding.inflate(getLayoutInflater());
        setContentView(mBind.getRoot());
        EventBus.getDefault().register(this);
        showSyncingProgressDialog();
        mBind.cbMac.postDelayed(() -> {
            List<OrderTask> orderTasks = new ArrayList<>();
            orderTasks.add(OrderTaskAssembler.getPayloadOtherContent());
            orderTasks.add(OrderTaskAssembler.getPayloadOtherDataBlockContent());
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
                                    case KEY_PAYLOAD_OTHER_CONTENT:
                                        savedParamsError |= result != 1;
                                        break;
                                    case KEY_PAYLOAD_OTHER_DATA_BLOCK:
                                        savedParamsError |= result != 1;
                                        if (savedParamsError) {
                                            ToastUtils.showToast(PayloadOtherTypeContentActivity.this, "Opps！Save failed. Please check the input characters and try again.");
                                        } else {
                                            ToastUtils.showToast(this, "Save Successfully！");
                                        }
                                        break;
                                }
                            }
                            if (flag == 0x00) {
                                // read
                                switch (configKeyEnum) {
                                    case KEY_PAYLOAD_OTHER_CONTENT:
                                        if (length > 0) {
                                            int data = MokoUtils.toInt(Arrays.copyOfRange(value, 5, 5 + length));
                                            mBind.cbMac.setChecked((data & 0x01) == 0x01);
                                            mBind.cbRssi.setChecked((data & 0x02) == 0x02);
                                            mBind.cbTimestamp.setChecked((data & 0x04) == 0x04);
                                            mBind.cbRawDataAdv.setChecked((data & 0x08) == 0x08);
                                            mBind.cbRawDataResp.setChecked((data & 0x10) == 0x10);
                                        }
                                        break;
                                    case KEY_PAYLOAD_OTHER_DATA_BLOCK:
                                        if (length > 0) {
                                            byte[] dataBlockBytes = Arrays.copyOfRange(value, 5, 5 + length);
                                            int index = 0;
                                            for (int i = 0, l = dataBlockBytes.length; i < l; i += 3, index++) {
                                                String dataBlockStr = MokoUtils.bytesToHexString(Arrays.copyOfRange(dataBlockBytes, i, i + 3));
                                                View view = LayoutInflater.from(this).inflate(R.layout.lw003_plus_item_payload_data_block, mBind.llDataBlock, false);
                                                TextView tvIndex = view.findViewById(R.id.tv_index);
                                                EditText etDataType = view.findViewById(R.id.et_data_type);
                                                EditText etStart = view.findViewById(R.id.et_start);
                                                EditText etEnd = view.findViewById(R.id.et_end);
                                                ImageView ivDel = view.findViewById(R.id.iv_del);
                                                int delIndex = index;
                                                ivDel.setOnClickListener(v -> {
                                                    notifyListUpdateIndex(delIndex);
                                                });
                                                tvIndex.setText(String.format("Data block %d", index + 1));
                                                String dataTypeStr = dataBlockStr.substring(0, 2);
                                                if ("00".equals(dataTypeStr))
                                                    etDataType.setText("");
                                                else
                                                    etDataType.setText(dataTypeStr);
                                                etStart.setText(String.valueOf(Integer.parseInt(dataBlockStr.substring(2, 4), 16)));
                                                etEnd.setText(String.valueOf(Integer.parseInt(dataBlockStr.substring(4, 6), 16)));
                                                mBind.llDataBlock.addView(view);
                                            }
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

    public void onDataBlockAdd(View view) {
        if (isWindowLocked())
            return;
        final int count = mBind.llDataBlock.getChildCount();
        if (count > 9) {
            ToastUtils.showToast(this, "You can set up to 10 data blocks!");
            return;
        }
        View itemView = LayoutInflater.from(this).inflate(R.layout.lw003_plus_item_payload_data_block, mBind.llDataBlock, false);
        TextView tvIndex = itemView.findViewById(R.id.tv_index);
        tvIndex.setText(String.format("Data block %d", count + 1));
        ImageView ivDel = itemView.findViewById(R.id.iv_del);
        ivDel.setOnClickListener(v -> {
            notifyListUpdateIndex(count);
        });
        mBind.llDataBlock.addView(itemView);
    }

    private void notifyListUpdateIndex(int index) {
        mBind.llDataBlock.removeViewAt(index);
        int count = mBind.llDataBlock.getChildCount();
        for (int i = index; i < count; i++) {
            View view = mBind.llDataBlock.getChildAt(i);
            TextView tvIndex = view.findViewById(R.id.tv_index);
            tvIndex.setText(String.format("Data block %d", i + 1));
            ImageView ivDel = view.findViewById(R.id.iv_del);
            int finalI = i;
            ivDel.setOnClickListener(v -> {
                notifyListUpdateIndex(finalI);
            });
        }
    }

    public void onSave(View view) {
        if (isWindowLocked())
            return;
        if (isValid()) {
            showSyncingProgressDialog();
            saveParams();
        } else {
            ToastUtils.showToast(this, "Parameter Error");
        }
    }

    private boolean isValid() {
        final int count = mBind.llDataBlock.getChildCount();
        mList = new ArrayList<>();
        if (count > 0) {
            // 发送设置的过滤RawData
            for (int i = 0; i < count; i++) {
                View v = mBind.llDataBlock.getChildAt(i);
                EditText etDataType = v.findViewById(R.id.et_data_type);
                EditText etStart = v.findViewById(R.id.et_start);
                EditText etEnd = v.findViewById(R.id.et_end);
                final String dataTypeStr = etDataType.getText().toString();
                final String startStr = etStart.getText().toString();
                final String endStr = etEnd.getText().toString();
                final int dataType = TextUtils.isEmpty(dataTypeStr) ? 0 : Integer.parseInt(dataTypeStr, 16);
                if (dataType < 0 || dataType > 0xFF)
                    return false;
                if (TextUtils.isEmpty(startStr) || TextUtils.isEmpty(endStr))
                    return false;
                int start = Integer.parseInt(startStr);
                int end = Integer.parseInt(endStr);
                if (start < 1 || start > 29) {
                    return false;
                }
                if (end < 1 || end > 29) {
                    return false;
                }
                if (end < start) {
                    return false;
                }
                StringBuffer sb = new StringBuffer();
                sb.append(MokoUtils.int2HexString(dataType));
                sb.append(MokoUtils.int2HexString(start));
                sb.append(MokoUtils.int2HexString(end));
                mList.add(sb.toString());
            }
        }
        return true;
    }

    private void saveParams() {
        savedParamsError = false;
        List<OrderTask> orderTasks = new ArrayList<>();
        int flag = (mBind.cbMac.isChecked() ? 0x01 : 0x00)
                | (mBind.cbRssi.isChecked() ? 0x02 : 0x00)
                | (mBind.cbTimestamp.isChecked() ? 0x04 : 0x00)
                | (mBind.cbRawDataAdv.isChecked() ? 0x08 : 0x00)
                | (mBind.cbRawDataResp.isChecked() ? 0x10 : 0x00);
        orderTasks.add(OrderTaskAssembler.setPayloadOtherDataContent(flag));
        orderTasks.add(OrderTaskAssembler.setPayloadOtherDataBlockContent(mList));
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
