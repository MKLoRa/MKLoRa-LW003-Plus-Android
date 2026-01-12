package com.moko.lw003plus.activity.payload;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.moko.ble.lib.MokoConstants;
import com.moko.ble.lib.event.ConnectStatusEvent;
import com.moko.ble.lib.event.OrderTaskResponseEvent;
import com.moko.ble.lib.task.OrderTaskResponse;
import com.moko.lw003plus.activity.BaseActivity;
import com.moko.lw003plus.databinding.Lw003ProActivityPayloadContentSelectionBinding;
import com.moko.support.lw003plus.entity.OrderCHAR;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class PayloadContentSelectionActivity extends BaseActivity {

    private Lw003ProActivityPayloadContentSelectionBinding mBind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBind = Lw003ProActivityPayloadContentSelectionBinding.inflate(getLayoutInflater());
        setContentView(mBind.getRoot());
        EventBus.getDefault().register(this);
    }


    @Subscribe(threadMode = ThreadMode.POSTING, priority = 200)
    public void onConnectStatusEvent(ConnectStatusEvent event) {
        final String action = event.getAction();
        runOnUiThread(() -> {
            if (MokoConstants.ACTION_DISCONNECTED.equals(action)) {
                finish();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.POSTING, priority = 200)
    public void onOrderTaskResponseEvent(OrderTaskResponseEvent event) {
        final String action = event.getAction();
        if (!MokoConstants.ACTION_CURRENT_DATA.equals(action))
            EventBus.getDefault().cancelEventDelivery(event);
        runOnUiThread(() -> {
            if (MokoConstants.ACTION_ORDER_TIMEOUT.equals(action)) {
            }
            if (MokoConstants.ACTION_ORDER_FINISH.equals(action)) {
            }
            if (MokoConstants.ACTION_ORDER_RESULT.equals(action)) {
                OrderTaskResponse response = event.getResponse();
                OrderCHAR orderCHAR = (OrderCHAR) response.orderCHAR;
                int responseType = response.responseType;
                byte[] value = response.responseValue;
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

    public void onIBeaconContent(View view) {
        if (isWindowLocked()) return;
        startActivity(new Intent(this, PayloadIBeaconContentActivity.class));
    }

    public void onEddystoneUIDContent(View view) {
        if (isWindowLocked()) return;
        startActivity(new Intent(this, PayloadEddystoneUIDContentActivity.class));
    }

    public void onEddystoneURLContent(View view) {
        if (isWindowLocked()) return;
        startActivity(new Intent(this, PayloadEddystoneURLContentActivity.class));
    }

    public void onEddystoneTLMContent(View view) {
        if (isWindowLocked()) return;
        startActivity(new Intent(this, PayloadEddystoneTLMContentActivity.class));
    }

    public void onBXPIBeaconContent(View view) {
        if (isWindowLocked()) return;
        startActivity(new Intent(this, PayloadBXPIBeaconContentActivity.class));
    }

    public void onBXPDeviceInfoContent(View view) {
        if (isWindowLocked()) return;
        startActivity(new Intent(this, PayloadBXPDeviceInfoContentActivity.class));
    }

    public void onBXPAccContent(View view) {
        if (isWindowLocked()) return;
        startActivity(new Intent(this, PayloadBXPAccContentActivity.class));
    }

    public void onBXPTHContent(View view) {
        if (isWindowLocked()) return;
        startActivity(new Intent(this, PayloadBXPTHContentActivity.class));
    }

    public void onBXPButtonContent(View view) {
        if (isWindowLocked()) return;
        startActivity(new Intent(this, PayloadBXPButtonContentActivity.class));
    }

    public void onBXPTagContent(View view) {
        if (isWindowLocked()) return;
        startActivity(new Intent(this, PayloadBXPTagContentActivity.class));
    }

    public void onMKTOFContent(View view) {
        if (isWindowLocked()) return;
        startActivity(new Intent(this, PayloadMKTOFContentActivity.class));
    }

    public void onMKPIRContent(View view) {
        if (isWindowLocked()) return;
        startActivity(new Intent(this, PayloadMKPIRContentActivity.class));
    }

    public void onOtherTypeContent(View view) {
        if (isWindowLocked()) return;
        startActivity(new Intent(this, PayloadOtherTypeContentActivity.class));
    }
}
