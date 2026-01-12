package com.moko.lw003plus.adapter;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.moko.lw003plus.R;
import com.moko.lw003plus.entity.TimePoint;

import java.util.List;

public class TimePointAdapter extends BaseItemDraggableAdapter<TimePoint, BaseViewHolder> {
    public TimePointAdapter(List<TimePoint> data) {
        super(R.layout.lw003_pro_item_time_point, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TimePoint item) {
        helper.setText(R.id.tv_point_name, item.name);
        helper.setText(R.id.tv_point_hour, item.hour);
        helper.setText(R.id.tv_point_min, item.min);
        helper.addOnClickListener(R.id.tv_point_hour);
        helper.addOnClickListener(R.id.tv_point_min);
    }
}
