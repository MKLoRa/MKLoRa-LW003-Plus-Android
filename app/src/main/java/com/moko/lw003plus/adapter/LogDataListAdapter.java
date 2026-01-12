package com.moko.lw003plus.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.moko.lw003plus.R;
import com.moko.lw003plus.entity.LogData;

public class LogDataListAdapter extends BaseQuickAdapter<LogData, BaseViewHolder> {

    public LogDataListAdapter() {
        super(R.layout.lw003_pro_item_log_data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LogData item) {
        helper.setText(R.id.tv_time, item.name);
        helper.setImageResource(R.id.iv_checked, item.isSelected ? R.drawable.ic_selected : R.drawable.ic_unselected);
    }
}
