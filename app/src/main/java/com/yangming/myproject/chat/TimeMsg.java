package com.yangming.myproject.chat;

import com.yangming.myproject.R;

import java.util.List;

import baselibrary.recycleview.ItemViewDelegate;
import baselibrary.recycleview.ViewHolder;

/**
 * @author yangming on 2018/5/4.
 */

public class TimeMsg implements ItemViewDelegate<MessageInfo> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.xhd_item_onlinechat_time;
    }

    @Override
    public boolean isForViewType(MessageInfo item, int position) {
        int from = item.getFromRobot();
        if (from == 1) {
            return false;
        } else if (from == 2) {
            return false;
        } else if (from == 3) {
            return true;
        }
        return false;
    }

    @Override
    public void convert(ViewHolder holder, MessageInfo messageInfo, int position) {
        holder.setText(R.id.chat_time, messageInfo.getDate() + " " + messageInfo.getTime());
    }

    @Override
    public void convert(ViewHolder holder, MessageInfo messageInfo, int position, List<Object> payloads) {

    }
}
