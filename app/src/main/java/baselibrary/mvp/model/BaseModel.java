package baselibrary.mvp.model;


import baselibrary.mvp.AppConstants;
import baselibrary.api.IApiService;
import baselibrary.api.RetrofitHelper;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * @author yangming on 2018/12/28
 */
public class BaseModel implements IModel {
    protected IApiService mApi;
    private CompositeSubscription mCompositeSubscription;

    public BaseModel() {
        this.mApi = RetrofitHelper.getInstance().createApiService(AppConstants.BASE_SERVER_IP);
    }

    @Override
    public void unSubscribe() {
        if (mCompositeSubscription != null && !mCompositeSubscription.isUnsubscribed()) {
            mCompositeSubscription.clear();
            mCompositeSubscription.unsubscribe();
        }
    }

    @Override
    public void addSubscribe(Subscription subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }
}
