package com.moko.lw003plus.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moko.ble.lib.task.OrderTask;
import com.moko.lib.loraui.dialog.BottomDialog;
import com.moko.lw003plus.activity.DeviceInfoActivity;
import com.moko.lw003plus.databinding.Lw003ProFragmentScannerBinding;
import com.moko.support.lw003plus.LoRaLW003PlusMokoSupport;
import com.moko.support.lw003plus.OrderTaskAssembler;

import java.util.ArrayList;

public class ScannerFragment extends Fragment {
    private static final String TAG = ScannerFragment.class.getSimpleName();

    private Lw003ProFragmentScannerBinding mBind;

    private ArrayList<String> mDuplicateDataFilterValues;
    private ArrayList<String> mStrategyValues;
    private ArrayList<String> mMaxLengthValues;
    private DeviceInfoActivity activity;


    public ScannerFragment() {
    }


    public static ScannerFragment newInstance() {
        ScannerFragment fragment = new ScannerFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: ");
        mBind = Lw003ProFragmentScannerBinding.inflate(inflater, container, false);
        activity = (DeviceInfoActivity) getActivity();
        mDuplicateDataFilterValues = new ArrayList<>();
        mDuplicateDataFilterValues.add("No");
        mDuplicateDataFilterValues.add("MAC");
        mDuplicateDataFilterValues.add("MAC+Data Type");
        mDuplicateDataFilterValues.add("MAC+Raw Data");
        mMaxLengthValues = new ArrayList<>();
        mMaxLengthValues.add("Level 1");
        mMaxLengthValues.add("Level 2");
        mStrategyValues = new ArrayList<>();
        mStrategyValues.add("Current Cycle Priority");
        mStrategyValues.add("Next Cycle Priority");
        return mBind.getRoot();
    }

    public void setDataRetentionStrategy(int strategy) {
        mBind.tvDataRetentionStrategy.setTag(strategy);
        mBind.tvDataRetentionStrategy.setText(mStrategyValues.get(strategy));
    }

    public void setReportDataMaxLength(int maxLength) {
        mBind.tvReportDataMaxLength.setTag(maxLength);
        mBind.tvReportDataMaxLength.setText(mMaxLengthValues.get(maxLength));
    }

    public void setAdvReportOnlyEnable(int enable) {
        mBind.cbAdvReportOnly.setChecked(enable == 1);
    }

    public void setDuplicateDataFilter(int duplicateData) {
        mBind.tvDuplicateDataFilter.setTag(duplicateData);
        mBind.tvDuplicateDataFilter.setText(mDuplicateDataFilterValues.get(duplicateData));
    }


    public void saveParams() {
        ArrayList<OrderTask> orderTasks = new ArrayList<>();
        int maxLength = (int) mBind.tvReportDataMaxLength.getTag();
        int strategy = (int) mBind.tvDataRetentionStrategy.getTag();
        int duplicateData = (int) mBind.tvDuplicateDataFilter.getTag();
        int enable = mBind.cbAdvReportOnly.isChecked() ? 1 : 0;
        orderTasks.add(OrderTaskAssembler.setFilterDuplicateData(duplicateData));
        orderTasks.add(OrderTaskAssembler.setDataRetentionStrategy(strategy));
        orderTasks.add(OrderTaskAssembler.setAdvReportOnlyEnable(enable));
        orderTasks.add(OrderTaskAssembler.setReportDataMaxLength(maxLength));
        LoRaLW003PlusMokoSupport.getInstance().sendOrder(orderTasks.toArray(new OrderTask[]{}));
    }

    public void selectReportDataMaxLength(View view) {
        int selected = (int) view.getTag();
        BottomDialog dialog = new BottomDialog();
        dialog.setDatas(mMaxLengthValues, selected);
        dialog.setListener(this::setReportDataMaxLength);
        dialog.show(activity.getSupportFragmentManager());
    }

    public void selectDataRetentionStrategy(View view) {
        int selected = (int) view.getTag();
        BottomDialog dialog = new BottomDialog();
        dialog.setDatas(mStrategyValues, selected);
        dialog.setListener(this::setDataRetentionStrategy);
        dialog.show(activity.getSupportFragmentManager());
    }

    public void selectDuplicateDataFilter(View view) {
        int selected = (int) view.getTag();
        BottomDialog dialog = new BottomDialog();
        dialog.setDatas(mDuplicateDataFilterValues, selected);
        dialog.setListener(this::setDuplicateDataFilter);
        dialog.show(activity.getSupportFragmentManager());
    }
}
