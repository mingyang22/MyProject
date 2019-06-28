package baselibrary.recycleview;

import android.support.v4.util.SparseArrayCompat;
import android.util.Log;

import java.util.List;

/**
 * @author yangming on 2018/7/20
 */
public class ItemViewDelegateManager<T> {
    String TAG = "ItemViewDelegateManager";
    SparseArrayCompat<ItemViewDelegate<T>> delegates = new SparseArrayCompat();

    public int getItemViewDelegateCount() {
        return delegates.size();
    }

    public ItemViewDelegateManager<T> addDelegate(ItemViewDelegate<T> delegate) {
        int viewType = delegates.size();
        if (delegate != null) {
            delegates.put(viewType, delegate);
            viewType++;
        }
        return this;
    }

    public ItemViewDelegateManager<T> addDelegate(int viewType, ItemViewDelegate<T> delegate) {
        if (delegates.get(viewType) != null) {
            throw new IllegalArgumentException(
                    "An ItemViewDelegate is already registered for the viewType = "
                            + viewType
                            + ". Already registered ItemViewDelegate is "
                            + delegates.get(viewType));
        }
        delegates.put(viewType, delegate);
        return this;
    }

    public ItemViewDelegateManager<T> removeDelegate(ItemViewDelegate<T> delegate) {
        if (delegate == null) {
            throw new NullPointerException("ItemViewDelegate is null");
        }
        int indexToRemove = delegates.indexOfValue(delegate);

        if (indexToRemove >= 0) {
            delegates.removeAt(indexToRemove);
        }
        return this;
    }

    public ItemViewDelegateManager<T> removeDelegate(int itemType) {
        int indexToRemove = delegates.indexOfKey(itemType);

        if (indexToRemove >= 0) {
            delegates.removeAt(indexToRemove);
        }
        return this;
    }

    public int getItemViewType(T item, int position) {
        int delegatesCount = delegates.size();
        for (int i = delegatesCount - 1; i >= 0; i--) {
            ItemViewDelegate<T> delegate = delegates.valueAt(i);
            if (delegate.isForViewType(item, position)) {
                return delegates.keyAt(i);
            }
        }
        Log.e(TAG, "getItemViewType: ", new Throwable("No ItemViewDelegate added that matches position=" + position + " in data source"));
        return 0;
//        throw new IllegalArgumentException(
//                "No ItemViewDelegate added that matches position=" + position + " in data source");
    }

    public void convert(ViewHolder holder, T item, int position) {
        int delegatesCount = delegates.size();
        for (int i = 0; i < delegatesCount; i++) {
            ItemViewDelegate<T> delegate = delegates.valueAt(i);

            if (delegate.isForViewType(item, position)) {
                delegate.convert(holder, item, position);
                return;
            }
        }
        delegates.valueAt(0).convert(holder, item, position);
        Log.e(TAG, "convert: ", new Throwable("No ItemViewDelegateManager added that matches position=" + position + " in data source"));
//        throw new IllegalArgumentException(
//                "No ItemViewDelegateManager added that matches position=" + position + " in data source");
    }

    public void convert(ViewHolder holder, T item, int position, List<Object> payloads) {
        int delegatesCount = delegates.size();
        for (int i = 0; i < delegatesCount; i++) {
            ItemViewDelegate<T> delegate = delegates.valueAt(i);

            if (delegate.isForViewType(item, position)) {
                delegate.convert(holder, item, position, payloads);
                return;
            }
        }
        delegates.valueAt(0).convert(holder, item, position, payloads);
        Log.e(TAG, "convert: ", new Throwable("No ItemViewDelegateManager added that matches position=" + position + " in data source"));
//        throw new IllegalArgumentException(
//                "No ItemViewDelegateManager added that matches position=" + position + " in data source");
    }

    public ItemViewDelegate getItemViewDelegate(int viewType) {
        return delegates.get(viewType);
    }

    public int getItemViewLayoutId(int viewType) {
        return getItemViewDelegate(viewType).getItemViewLayoutId();
    }

    public int getItemViewType(ItemViewDelegate itemViewDelegate) {
        return delegates.indexOfValue(itemViewDelegate);
    }
}
