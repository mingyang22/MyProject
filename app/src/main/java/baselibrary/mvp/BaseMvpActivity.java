package baselibrary.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import baselibrary.api.HttpConstants;
import baselibrary.api.rx.BaseSubscriber;
import baselibrary.api.rx.ExceptionEvent;
import baselibrary.api.rx.RxBus;
import baselibrary.mvp.presenter.BasePresenter;
import baselibrary.mvp.view.IView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author yangming on 2019/3/28
 */
public abstract class BaseMvpActivity<P extends BasePresenter> extends RxAppCompatActivity implements IView {
    protected P mPresenter;
    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        unbinder = ButterKnife.bind(this);
        // 初始化Presenter
        initPresenter();
        subscribeEvent();
    }

    private void initPresenter() {
        mPresenter = createPresenter();
        // 完成Presenter和view的绑定
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    /**
     * 订阅RxBus事件
     */
    private void subscribeEvent() {
        RxBus.getInstance().toObservable(ExceptionEvent.class).compose(this
                .<ExceptionEvent>bindToLifecycle()).subscribe(new BaseSubscriber<ExceptionEvent>() {

            @Override
            public void onSuccess(ExceptionEvent data) {
                switch (data.getEventCode()) {
                    case HttpConstants.CODE_FORCE_LOGOUT:
                        // 账号在其他设备登录
                        break;
                    case HttpConstants.CODE_TOKEN_INVALID:
                        // token过期
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailed(Throwable ex, String code, String msg) {
                subscribeEvent();
            }

            @Override
            public void onError() {
                // 这里注意: 一旦订阅过程中发生异常,走到onError,则代表此次订阅事件完成,后续将收不到onNext()事件,
                // 即 接受不到后续的任何事件,实际环境中,我们需要在onError里 重新订阅事件!
                subscribeEvent();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 将Presenter和view解绑
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
        // 解绑ButterKnife
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    public void showLoading() {
        // 这里实现自己的加载弹框
    }

    @Override
    public void dismissLoading() {
        // 取消弹框
    }

    @Override
    public void onFail(Throwable ex, String code, String msg) {
        // 基础的网络请求失败处理
        dismissLoading();
    }

    @Override
    public void onNetError() {
        // 网络错误处理
        dismissLoading();
    }

    /**
     * 页面初始化数据
     */
    protected void initData() {

    }

    /**
     * 创建Presenter
     *
     * @return Presenter
     */
    protected abstract P createPresenter();

    /**
     * 获取当前activity的id
     *
     * @return 当前xml的布局res ID
     */
    protected abstract int getLayoutId();

    /**
     * 初始化view控件
     */
    protected abstract void initView();

}

