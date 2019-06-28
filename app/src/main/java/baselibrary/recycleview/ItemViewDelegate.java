package baselibrary.recycleview;

import java.util.List;

/**
 * @author yangming on 2018/7/20
 */
public interface ItemViewDelegate<T> {

    int getItemViewLayoutId();

    boolean isForViewType(T item, int position);

    void convert(ViewHolder holder, T t, int position);

    void convert(ViewHolder holder, T t, int position, List<Object> payloads);

}
