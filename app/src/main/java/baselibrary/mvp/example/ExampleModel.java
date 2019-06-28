package baselibrary.mvp.example;

import java.util.Map;

import baselibrary.api.rx.RxUtil;
import baselibrary.mvp.model.BaseModel;
import rx.Subscriber;
import rx.Subscription;

/**
 * @author yangming on 2019/3/28
 */
public class ExampleModel extends BaseModel implements ExampleContract.Model {
    @Override
    public void login(Map map, Subscriber subscriber) {
        Subscription subscription = mApi.login(map).compose(RxUtil.applySchedulers()).compose(RxUtil.handleResult()).subscribe(subscriber);
        addSubscribe(subscription);
    }
}
