package com.yangming.myproject.databinding;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.yangming.myproject.R;

import java.util.List;

/**
 * @author yangming on 2019/4/3
 */
public class SwordsmanAdapter extends RecyclerView.Adapter<SwordsmanAdapter.SwordsmanViewHolder> {
    private List<Swordsman> mList;

    public SwordsmanAdapter(List<Swordsman> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public SwordsmanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        com.yangming.myproject.databinding.ItemSwordsmanBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.item_swordsman, parent, false);
        return new SwordsmanViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SwordsmanViewHolder holder, int position) {
        Swordsman swordsman = mList.get(position);
        holder.getBinding().setSwordsman(swordsman);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class SwordsmanViewHolder extends RecyclerView.ViewHolder {
        com.yangming.myproject.databinding.ItemSwordsmanBinding binding;

        public SwordsmanViewHolder(com.yangming.myproject.databinding.ItemSwordsmanBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public com.yangming.myproject.databinding.ItemSwordsmanBinding getBinding() {
            return binding;
        }
    }

}
