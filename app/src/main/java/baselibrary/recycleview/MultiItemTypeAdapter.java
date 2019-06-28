package baselibrary.recycleview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * @author yangming on 2018/7/20
 */
public class MultiItemTypeAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    protected Context mContext;
    protected List<T> mDatas;

    protected ItemViewDelegateManager mItemViewDelegateManager;
    protected OnItemClickListener mOnItemClickListener;
    protected OnItemLongClickListener mOnItemLongClickListener;


    public MultiItemTypeAdapter(Context context, List<T> datas) {
        mContext = context;
        mDatas = datas;
        mItemViewDelegateManager = new ItemViewDelegateManager();
    }

    @Override
    public int getItemViewType(int position) {
        if (!useItemViewDelegateManager()) {
            return super.getItemViewType(position);
        }
        return mItemViewDelegateManager.getItemViewType(mDatas.get(position), position);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewDelegate itemViewDelegate = mItemViewDelegateManager.getItemViewDelegate(viewType);
        int layoutId = itemViewDelegate.getItemViewLayoutId();
        ViewHolder holder = ViewHolder.createViewHolder(mContext, parent, layoutId);
        onViewHolderCreated(holder, holder.getConvertView());
        setListener(parent, holder, viewType);
        return holder;
    }

    public void onViewHolderCreated(ViewHolder holder, View itemView) {

    }

    public void convert(ViewHolder holder, T t) {
        mItemViewDelegateManager.convert(holder, t, holder.getAdapterPosition());
    }

    public void convert(ViewHolder holder, T t, List<Object> payloads) {
        mItemViewDelegateManager.convert(holder, t, holder.getAdapterPosition(), payloads);
    }

    protected boolean isEnabled(int viewType) {
        return true;
    }


    protected void setListener(final ViewGroup parent, final ViewHolder viewHolder, int viewType) {
        if (!isEnabled(viewType)) {
            return;
        }
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    if (position == -1) {
                        return;
                    }
                    mOnItemClickListener.onItemClick(v, viewHolder, position);
                }
            }
        });

        viewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemLongClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    if (position == -1)
                        return false;
                    return mOnItemLongClickListener.onItemLongClick(v, viewHolder, position);
                }
                return false;
            }
        });
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        convert(holder, mDatas.get(position));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        convert(holder, mDatas.get(position), payloads);
    }

    @Override
    public int getItemCount() {
        int itemCount = mDatas.size();
        return itemCount;
    }

    public void addData(int position, T t) {
        mDatas.add(position, t);
        notifyItemInserted(position);
        if (position != mDatas.size()) {
            notifyItemRangeChanged(position, mDatas.size() - position);
        }
    }

    /**
     * 在有数据的情况下添加数据集合
     */
    public void addDataList(List<T> datas) {
        int position = mDatas.size() - 1;
        mDatas.addAll(datas);
        notifyItemRangeInserted(position, datas.size());
    }

    public void updateData(List<T> datas) {
        mDatas.clear();
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    public void removeData(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
        // 删除时不回改变删除项之后item的位置信息，所以需要刷新删除项之后item
        if (position != mDatas.size()) {
            notifyItemRangeChanged(position, mDatas.size() - position);
        }
    }

    public void updateItemData(int position, T data) {
        notifyItemChanged(position, data);
    }

    public List<T> getDatas() {
        return mDatas;
    }

    public MultiItemTypeAdapter addItemViewDelegate(ItemViewDelegate<T> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(itemViewDelegate);
        return this;
    }

    public MultiItemTypeAdapter addItemViewDelegate(int viewType, ItemViewDelegate<T> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(viewType, itemViewDelegate);
        return this;
    }

    protected boolean useItemViewDelegateManager() {
        return mItemViewDelegateManager.getItemViewDelegateCount() > 0;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, RecyclerView.ViewHolder holder, int position);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }
}
