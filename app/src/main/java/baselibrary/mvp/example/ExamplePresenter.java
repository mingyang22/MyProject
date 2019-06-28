package baselibrary.mvp.example;

import java.util.HashMap;
import java.util.Map;

import baselibrary.api.rx.BaseSubscriber;
import baselibrary.api.bean.Test;
import baselibrary.mvp.presenter.BasePresenter;

/**
 * @author yangming on 2019/3/28
 */
public class ExamplePresenter extends BasePresenter<ExampleContract.View, ExampleModel> implements ExampleContract.Presenter {

    @Override
    protected ExampleModel createModule() {
        return new ExampleModel();
    }

    @Override
    public void login(String phone, String password) {

        showLoading();
        Map<String, String> params = new HashMap<>();
        params.put("phone", phone);
        params.put("password", password);

        mModel.login(params, new BaseSubscriber<Test>() {

            @Override
            public void onSuccess(Test data) {
                dismissLoading();
                if (isViewAttach()) {
                    getView().onExampleSuccess(data);
                }
            }

            @Override
            public void onFailed(Throwable ex, String code, String msg) {
                onFail(ex, code, msg);
            }

            @Override
            public void onError() {
                onNetError();
            }
        });
    }


}
