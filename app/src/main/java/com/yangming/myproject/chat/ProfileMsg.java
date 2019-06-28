package com.yangming.myproject.chat;

import com.yangming.myproject.R;

import java.util.List;

import baselibrary.recycleview.ItemViewDelegate;
import baselibrary.recycleview.ViewHolder;

/**
 * @author yangming on 2018/5/4.
 */

public class ProfileMsg implements ItemViewDelegate<MessageInfo> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.xhd_item_onlinechat_me;
    }

    @Override
    public boolean isForViewType(MessageInfo item, int position) {
        int from = item.getFromRobot();
        if (from == 1) {
            return false;
        } else if (from == 2) {
            return true;
        } else if (from == 3) {
            return false;
        }
        return false;
    }

    @Override
    public void convert(ViewHolder holder, MessageInfo messageInfo, int position) {
        holder.setText(R.id.chat_name, messageInfo.getTitle());
        holder.setText(R.id.chat_text_content, messageInfo.getContent());
    }

    @Override
    public void convert(ViewHolder holder, MessageInfo messageInfo, int position, List<Object> payloads) {

    }
}
