package com.moko.lw003plus.activity.strategies;


import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.moko.ble.lib.MokoConstants;
import com.moko.ble.lib.event.ConnectStatusEvent;
import com.moko.ble.lib.event.OrderTaskResponseEvent;
import com.moko.ble.lib.task.OrderTask;
import com.moko.ble.lib.task.OrderTaskResponse;
import com.moko.ble.lib.utils.MokoUtils;
import com.moko.lw003plus.R;
import com.moko.lw003plus.activity.BaseActivity;
import com.moko.lw003plus.adapter.TimePointAdapter;
import com.moko.lw003plus.databinding.Lw003ProActivityScanAlwaysTimingReportBinding;
import com.moko.lib.loraui.dialog.BottomDialog;
import com.moko.lw003plus.entity.TimePoint;
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

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ScanAlwaysTimingReportActivity extends BaseActivity implements BaseQuickAdapter.OnItemChildClickListener {

    private Lw003ProActivityScanAlwaysTimingReportBinding mBind;
    private boolean mReceiverTag = false;
    private boolean savedParamsError;
    private ArrayList<TimePoint> mTimePoints;
    private TimePointAdapter mAdapter;
    private ArrayList<String> mHourValues;
    private ArrayList<String> mMinValues;
    private Handler mHandler;
    private int mCountdown = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBind = Lw003ProActivityScanAlwaysTimingReportBinding.inflate(getLayoutInflater());
        setContentView(mBind.getRoot());
        mHourValues = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            mHourValues.add(String.format("%02d", i));
        }
        mMinValues = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            mMinValues.add(String.format("%02d", i * 10));
        }
        mTimePoints = new ArrayList<>();
        mAdapter = new TimePointAdapter(mTimePoints);
        SwipeToDeleteCallback callback = new SwipeToDeleteCallback();
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mBind.rvTimePoint);

        // 开启滑动删除
//        mAdapter.enableSwipeItem();
//        mAdapter.setOnItemSwipeListener(onItemSwipeListener);
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.openLoadAnimation();
        mBind.rvTimePoint.setLayoutManager(new LinearLayoutManager(this));
        mBind.rvTimePoint.setAdapter(mAdapter);
        mHandler = new Handler(Looper.getMainLooper());
        EventBus.getDefault().register(this);
        // 注册广播接收器
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
        mReceiverTag = true;
        showSyncingProgressDialog();
        mBind.ivTimePointAdd.postDelayed(() -> {
            List<OrderTask> orderTasks = new ArrayList<>();
            orderTasks.add(OrderTaskAssembler.getScanAlwaysTimingReportTimePoint());
            LoRaLW003PlusMokoSupport.getInstance().sendOrder(orderTasks.toArray(new OrderTask[]{}));
        }, 500);
        mHandler.postDelayed(() -> {
            if (!mReceiverTag) return;
            LoRaLW003PlusMokoSupport.getInstance().sendOrder(OrderTaskAssembler.setTime());
        }, 150 * 1000);
    }


    public class SwipeToDeleteCallback extends ItemTouchHelper.Callback {

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            // 不支持拖动
            int dragFlags = 0;
            // 支持从右向左滑动
            int swipeFlags = ItemTouchHelper.START;
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            mTimePoints.remove(position);
            int size = mTimePoints.size();
            for (int i = 1; i <= size; i++) {
                TimePoint point = mTimePoints.get(i - 1);
                point.name = String.format("Time Point %d", i);
            }
            mAdapter.replaceData(mTimePoints);
        }
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
                                    case KEY_TIME_UTC:
                                        mCountdown--;
                                        if (mCountdown == 0) return;
                                        mHandler.postDelayed(() -> {
                                            if (!mReceiverTag) return;
                                            LoRaLW003PlusMokoSupport.getInstance().sendOrder(OrderTaskAssembler.setTime());
                                        }, 150 * 1000);
                                        break;
                                    case KEY_SCAN_ALWAYS_TIMING_REPORT_TIME_POINT:
                                        if (result != 1) {
                                            savedParamsError = true;
                                        }
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
                                    case KEY_SCAN_ALWAYS_TIMING_REPORT_TIME_POINT:
                                        if (length > 0) {
                                            for (int i = 0; i < length; i++) {
                                                int point = value[5 + i] & 0xFF;
                                                int min = point * 10;
                                                int hour = min / 60;
                                                min = min % 60;
                                                TimePoint timePoint = new TimePoint();
                                                timePoint.name = String.format("Time Point %d", i + 1);
                                                if (hour == 24) {
                                                    timePoint.hour = String.format("%02d", 0);
                                                } else {
                                                    timePoint.hour = String.format("%02d", hour);
                                                }
                                                timePoint.min = String.format("%02d", min);
                                                mTimePoints.add(timePoint);
                                                mAdapter.replaceData(mTimePoints);
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
        if (mHandler.hasMessages(0))
            mHandler.removeMessages(0);
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

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (isWindowLocked())
            return;
        TimePoint timePoint = (TimePoint) adapter.getItem(position);
        if (view.getId() == R.id.tv_point_hour) {
            int select = Integer.parseInt(timePoint.hour);
            BottomDialog dialog = new BottomDialog();
            dialog.setDatas(mHourValues, select);
            dialog.setListener(value -> {
                timePoint.hour = mHourValues.get(value);
                adapter.notifyItemChanged(position);
            });
            dialog.show(getSupportFragmentManager());
        }
        if (view.getId() == R.id.tv_point_min) {
            int select = Integer.parseInt(timePoint.min) / 10;
            BottomDialog dialog = new BottomDialog();
            dialog.setDatas(mMinValues, select);
            dialog.setListener(value -> {
                timePoint.min = mMinValues.get(value);
                adapter.notifyItemChanged(position);
            });
            dialog.show(getSupportFragmentManager());
        }
    }

    public void onTimePointAdd(View view) {
        if (isWindowLocked())
            return;
        int size = mTimePoints.size();
        if (size >= 24) {
            ToastUtils.showToast(this, "You can set up to 24 time points!");
            return;
        }
        TimePoint timePoint = new TimePoint();
        timePoint.name = String.format("Time Point %d", size + 1);
        timePoint.hour = String.format("%02d", 0);
        timePoint.min = String.format("%02d", 0);
        mTimePoints.add(timePoint);
        mAdapter.replaceData(mTimePoints);
    }

    public void onSave(View view) {
        savedParamsError = false;
        showSyncingProgressDialog();
        List<OrderTask> orderTasks = new ArrayList<>();
        ArrayList<Integer> points = new ArrayList<>();
        for (TimePoint point : mTimePoints) {
            int hour = Integer.parseInt(point.hour);
            int min = Integer.parseInt(point.min);
            if (hour == 0 && min == 0) {
                points.add(144);
                continue;
            }
            points.add((hour * 60 + min) / 10);
        }
        orderTasks.add(OrderTaskAssembler.setScanAlwaysTimingReportTimePoint(points));
        LoRaLW003PlusMokoSupport.getInstance().sendOrder(orderTasks.toArray(new OrderTask[]{}));
        if (mHandler.hasMessages(0))
            mHandler.removeMessages(0);
        mHandler.postDelayed(() -> {
            if (!mReceiverTag) return;
            LoRaLW003PlusMokoSupport.getInstance().sendOrder(OrderTaskAssembler.setTime());
        }, 150 * 1000);
        mCountdown = 3;
    }
}
