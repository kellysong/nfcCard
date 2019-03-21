package com.sinpo.xnfc.adapter;

import android.content.Context;

import com.sinpo.xnfc.R;
import com.sinpo.xnfc.bean.CardInfo;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename CardListAdapter.java
 * @time 2018/6/28 10:00
 * @copyright(C) 2018 song
 */
public class CardListAdapter extends CommonAdapter<CardInfo.ConsumeRecord> {

    public CardListAdapter(Context context, int layoutId, List<CardInfo.ConsumeRecord> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, CardInfo.ConsumeRecord item, int position) {
        viewHolder.setText(R.id.tv_transactionNo, item.getTransactionNo());
        viewHolder.setText(R.id.tv_consumeTime, item.getConsumeTime());
        viewHolder.setText(R.id.tv_consumeMoney, item.getConsumeMoney());
    }
}
