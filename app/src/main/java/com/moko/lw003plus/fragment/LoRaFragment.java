package com.moko.lw003plus.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moko.lw003plus.activity.DeviceInfoActivity;
import com.moko.lw003plus.databinding.Lw003PlusFragmentLoraBinding;

import java.util.ArrayList;

public class LoRaFragment extends Fragment {
    private static final String TAG = LoRaFragment.class.getSimpleName();

    private Lw003PlusFragmentLoraBinding mBind;

    private DeviceInfoActivity activity;


    private ArrayList<String> mUploadMode;
    private ArrayList<String> mRegions;


    private int mSelectedRegion;
    private int mSelectUploadMode;

    public LoRaFragment() {
    }


    public static LoRaFragment newInstance() {
        LoRaFragment fragment = new LoRaFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: ");
        mBind = Lw003PlusFragmentLoraBinding.inflate(inflater, container, false);
        activity = (DeviceInfoActivity) getActivity();
        mUploadMode = new ArrayList<>();
        mUploadMode.add("ABP");
        mUploadMode.add("OTAA");
        mRegions = new ArrayList<>();
        mRegions.add("AS923");
        mRegions.add("AU915");
        mRegions.add("EU868");
        mRegions.add("KR920");
        mRegions.add("IN865");
        mRegions.add("US915");
        mRegions.add("RU864");
        mRegions.add("AS923-1");
        mRegions.add("AS923-2");
        mRegions.add("AS923-3");
        mRegions.add("AS923-4");
        return mBind.getRoot();
    }

    public void setClassType(int classType) {
        String loraInfo = String.format("%s/%s/%s",
                mUploadMode.get(mSelectUploadMode - 1),
                mSelectedRegion < 2 ? mRegions.get(mSelectedRegion) : mRegions.get(mSelectedRegion - 3),
                classType == 0 ? "ClassA" : "ClassC");
        mBind.tvLoraInfo.setText(loraInfo);
    }

    public void setRegion(int region) {
        mSelectedRegion = region;
    }

    public void setUploadMode(int mode) {
        mSelectUploadMode = mode;
    }

    public void setLoraStatus(int networkCheck) {
        String networkCheckDisPlay = "";
        switch (networkCheck) {
            case 0:
                networkCheckDisPlay = "Connecting";
                break;
            case 1:
                networkCheckDisPlay = "Connected";
                break;
        }
        mBind.tvLoraStatus.setText(networkCheckDisPlay);
    }
}
