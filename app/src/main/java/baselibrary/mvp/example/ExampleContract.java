package baselibrary.mvp.example;

import java.util.Map;

import baselibrary.api.bean.Test;
import baselibrary.mvp.model.IModel;
import baselibrary.mvp.view.IView;
import rx.Subscriber;

/**
 * @author yangming on 2019/3/28
 */
public class ExampleContract {
    interface View extends IView {
        /**
         * 成功
         */
        void onExampleSuccess(Test data);
        /**
         * 失败
         */
        void onExampleFail(Throwable ex, String code, String msg);
    }

    interface Presenter {
        void login(String phone, String password);
    }

    interface Model extends IModel {
        /**
         * 测试
         */
        void login(Map map, Subscriber subscriber);

    }
}
