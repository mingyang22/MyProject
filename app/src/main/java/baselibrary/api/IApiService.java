package baselibrary.api;

import java.util.Map;

import baselibrary.api.bean.BaseResponse;
import baselibrary.api.bean.Test;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author yangming on 2018/12/29
 */
public interface IApiService {

    /**
     * 测试
     */
    @POST("")
    Observable<BaseResponse<Test>> login(@Body Map params);

}
