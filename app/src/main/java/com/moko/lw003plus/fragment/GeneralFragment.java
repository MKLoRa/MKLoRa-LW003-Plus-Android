package com.moko.lw003plus.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moko.ble.lib.task.OrderTask;
import com.moko.lw003plus.activity.DeviceInfoActivity;
import com.moko.lw003plus.databinding.Lw003PlusFragmentGeneralBinding;
import com.moko.support.lw003plus.LoRaLW003PlusMokoSupport;
import com.moko.support.lw003plus.OrderTaskAssembler;

import java.util.ArrayList;

public class GeneralFragment extends Fragment {
    private static final String TAG = GeneralFragment.class.getSimpleName();

    private Lw003PlusFragmentGeneralBinding mBind;

    private DeviceInfoActivity activity;

    public GeneralFragment() {
    }


    public static GeneralFragment newInstance() {
        GeneralFragment fragment = new GeneralFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: ");
        mBind = Lw003PlusFragmentGeneralBinding.inflate(inflater, container, false);
        activity = (DeviceInfoActivity) getActivity();
        return mBind.getRoot();
    }

    public void setHeartbeatInterval(int interval) {
        mBind.etHeartbeatInterval.setText(String.valueOf(interval));
    }

    public boolean isValid() {
        final String intervalStr = mBind.etHeartbeatInterval.getText().toString();
        if (TextUtils.isEmpty(intervalStr))
            return false;
        final int interval = Integer.parseInt(intervalStr);
        if (interval < 1 || interval > 14400) {
            return false;
        }
        return true;
    }


    public void saveParams() {
        final String intervalStr = mBind.etHeartbeatInterval.getText().toString();
        final int interval = Integer.parseInt(intervalStr);
        final int enable = mBind.cbContinuityTransferFunction.isChecked() ? 1 : 0;
        ArrayList<OrderTask> orderTasks = new ArrayList<>();
        orderTasks.add(OrderTaskAssembler.setHeartBeatInterval(interval));
        orderTasks.add(OrderTaskAssembler.setContinuityTransferFunctionEnable(enable));
        LoRaLW003PlusMokoSupport.getInstance().sendOrder(orderTasks.toArray(new OrderTask[]{}));
    }

    public void setEnable(int enable) {
        mBind.cbContinuityTransferFunction.setChecked(enable == 1);
    }
}
