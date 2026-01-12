package com.moko.lw003plus.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moko.ble.lib.task.OrderTask;
import com.moko.lw003plus.R;
import com.moko.lw003plus.activity.DeviceInfoActivity;
import com.moko.lw003plus.databinding.Lw003ProFragmentDeviceBinding;
import com.moko.lib.loraui.dialog.BottomDialog;
import com.moko.support.lw003plus.LoRaLW003PlusMokoSupport;
import com.moko.support.lw003plus.OrderTaskAssembler;

import java.util.ArrayList;

public class DeviceFragment extends Fragment {
    private static final String TAG = DeviceFragment.class.getSimpleName();

    private Lw003ProFragmentDeviceBinding mBind;

    private ArrayList<String> mTimeZones;
    private int mSelectedTimeZone;
    private ArrayList<String> mLowPowerPrompts;
    private int mSelectedLowPowerPrompt;
    private ArrayList<String> mChargePriorityValues;
    private int mSelectedChargePriority;
    private boolean mLowPowerPayloadEnable;


    private DeviceInfoActivity activity;

    public DeviceFragment() {
    }


    public static DeviceFragment newInstance() {
        DeviceFragment fragment = new DeviceFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: ");
        mBind = Lw003ProFragmentDeviceBinding.inflate(inflater, container, false);
        activity = (DeviceInfoActivity) getActivity();
        mTimeZones = new ArrayList<>();
        for (int i = -24; i <= 28; i++) {
            if (i < 0) {
                if (i % 2 == 0) {
                    mTimeZones.add(String.format("UTC%d", i / 2));
                } else {
                    mTimeZones.add(i < -1 ? String.format("UTC%d:30", (i + 1) / 2) : "UTC-0:30");
                }
            } else if (i == 0) {
                mTimeZones.add("UTC");
            } else {
                if (i % 2 == 0) {
                    mTimeZones.add(String.format("UTC+%d", i / 2));
                } else {
                    mTimeZones.add(String.format("UTC+%d:30", (i - 1) / 2));
                }
            }
        }
        mLowPowerPrompts = new ArrayList<>();
        mLowPowerPrompts.add("10%");
        mLowPowerPrompts.add("20%");
        mLowPowerPrompts.add("30%");
        mLowPowerPrompts.add("40%");
        mLowPowerPrompts.add("50%");
        mChargePriorityValues = new ArrayList<>();
        mChargePriorityValues.add("DC Priority");
        mChargePriorityValues.add("Solar Priority");
        mBind.clLowPowerPrompt.setVisibility(activity.mDeviceType == 0x00 ? View.GONE : ViewGroup.VISIBLE);
        mBind.clChargePriority.setVisibility(activity.mDeviceType == 0x10 ? View.VISIBLE : ViewGroup.GONE);
        return mBind.getRoot();
    }

    public void setTimeZone(int timeZone) {
        mSelectedTimeZone = timeZone + 24;
        mBind.tvTimeZone.setText(mTimeZones.get(mSelectedTimeZone));
    }

    public void showTimeZoneDialog() {
        BottomDialog dialog = new BottomDialog();
        dialog.setDatas(mTimeZones, mSelectedTimeZone);
        dialog.setListener(value -> {
            mSelectedTimeZone = value;
            mBind.tvTimeZone.setText(mTimeZones.get(value));
        });
        dialog.show(activity.getSupportFragmentManager());
    }

    public void setPriority(int priority) {
        mSelectedChargePriority = priority;
        mBind.tvChargePriority.setText(mChargePriorityValues.get(mSelectedChargePriority));
    }

    public void showChargePriorityDialog() {
        BottomDialog dialog = new BottomDialog();
        dialog.setDatas(mChargePriorityValues, mSelectedChargePriority);
        dialog.setListener(value -> {
            mSelectedChargePriority = value;
            mBind.tvChargePriority.setText(mChargePriorityValues.get(value));
        });
        dialog.show(activity.getSupportFragmentManager());
    }

    public void setLowPowerPayload(int enable) {
        mLowPowerPayloadEnable = enable == 1;
        mBind.cbLowPowerPayload.setChecked(mLowPowerPayloadEnable);
    }

    public void setLowPowerInterval(int interval) {
        mBind.etLowPowerReportInterval.setText(String.valueOf(interval));
    }

    public void setLowPower(int lowPower) {
        mSelectedLowPowerPrompt = lowPower;
        mBind.tvLowPowerPrompt.setText(mLowPowerPrompts.get(mSelectedLowPowerPrompt));
        mBind.tvLowPowerPromptTips.setText(getString(R.string.lw003_v3_low_power_prompt_tips, mLowPowerPrompts.get(mSelectedLowPowerPrompt)));
    }


    public void showLowPowerDialog() {
        BottomDialog dialog = new BottomDialog();
        dialog.setDatas(mLowPowerPrompts, mSelectedLowPowerPrompt);
        dialog.setListener(value -> {
            mSelectedLowPowerPrompt = value;
            mBind.tvLowPowerPrompt.setText(mLowPowerPrompts.get(value));
            mBind.tvLowPowerPromptTips.setText(getString(R.string.lw003_v3_low_power_prompt_tips, mLowPowerPrompts.get(value)));
        });
        dialog.show(activity.getSupportFragmentManager());
    }

    public boolean isValid() {
        final String intervalStr = mBind.etLowPowerReportInterval.getText().toString();
        if (TextUtils.isEmpty(intervalStr)) return false;
        final int interval = Integer.parseInt(intervalStr);
        return interval >= 1 && interval <= 255;
    }

    public void saveParams() {
        final String intervalStr = mBind.etLowPowerReportInterval.getText().toString();
        final int interval = Integer.parseInt(intervalStr);
        ArrayList<OrderTask> orderTasks = new ArrayList<>();
        orderTasks.add(OrderTaskAssembler.setTimeZone(mSelectedTimeZone - 24));
        orderTasks.add(OrderTaskAssembler.setLowPowerReportEnable(mBind.cbLowPowerPayload.isChecked() ? 1 : 0));
        orderTasks.add(OrderTaskAssembler.setLowPowerReportInterval(interval));
        orderTasks.add(OrderTaskAssembler.setLowPowerPercent(mSelectedLowPowerPrompt));
        orderTasks.add(OrderTaskAssembler.setChargePriority(mSelectedChargePriority));
        LoRaLW003PlusMokoSupport.getInstance().sendOrder(orderTasks.toArray(new OrderTask[]{}));
    }

}
