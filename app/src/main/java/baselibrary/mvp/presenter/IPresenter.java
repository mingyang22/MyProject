package baselibrary.mvp.presenter;


import baselibrary.mvp.view.IView;

/**
 * @author yangming on 2018/12/25
 */
public interface IPresenter<T extends IView> {
    /**
     * 绑定view
     * @param view view
     */
    void attachView(T view);

    /**
     * 分离view
     */
    void detachView();

    /**
     * 判断view是否已经销毁
     * @return true 未销毁
     */
    boolean isViewAttach();

}
