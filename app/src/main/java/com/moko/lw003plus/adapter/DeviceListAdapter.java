package com.moko.lw003plus.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.moko.ble.lib.utils.MokoUtils;
import com.moko.lw003plus.R;
import com.moko.lw003plus.entity.AdvInfo;

import java.util.Locale;

public class DeviceListAdapter extends BaseQuickAdapter<AdvInfo, BaseViewHolder> {
    public DeviceListAdapter() {
        super(R.layout.lw003_plus_list_item_device);
    }

    @Override
    protected void convert(BaseViewHolder helper, AdvInfo item) {
        final String rssi = String.format("%ddBm", item.rssi);
        helper.setText(R.id.tv_rssi, rssi);
        final String name = TextUtils.isEmpty(item.name) ? "N/A" : item.name;
        helper.setText(R.id.tv_name, name);
        helper.setText(R.id.tv_mac, String.format("MAC:%s", item.mac));

        helper.setImageResource(R.id.iv_battery, item.lowPowerState == 0 ? R.drawable.ic_battery_full : R.drawable.ic_battery_low);
        if (item.deviceType == 0x00) {
            helper.setText(R.id.tv_battery, item.lowPowerState == 0 ? "Full" : "Low");
        } else {
            helper.setText(R.id.tv_battery, String.format("%d%%", item.batteryPercent));
        }
        final String intervalTime = item.intervalTime == 0 ? "<->N/A" : String.format("<->%dms", item.intervalTime);
        helper.setText(R.id.tv_track_interval, intervalTime);
        helper.setVisible(R.id.tv_temp, !TextUtils.isEmpty(item.temp));
        helper.setVisible(R.id.tv_humi, !TextUtils.isEmpty(item.humidity));
        helper.setText(R.id.tv_temp, String.format("%s℃", TextUtils.isEmpty(item.temp) ? "N/A" : item.temp));
        helper.setText(R.id.tv_humi, String.format("%s%%RH", TextUtils.isEmpty(item.humidity) ? "N/A" : item.humidity));

        helper.setText(R.id.tv_tx_power, item.txPower == Integer.MAX_VALUE ? "Tx Power:N/AdBm" : String.format(Locale.getDefault(), "Tx Power:%ddBm", item.txPower));
        helper.setText(R.id.tv_battery_power, item.batteryPower < 0 ? "N/AV" : String.format(Locale.getDefault(), "%sV", MokoUtils.getDecimalFormat("0.###").format(item.batteryPower * 0.001f)));

        helper.setVisible(R.id.tv_connect, item.connectable);
        helper.addOnClickListener(R.id.tv_connect);

    }
}
