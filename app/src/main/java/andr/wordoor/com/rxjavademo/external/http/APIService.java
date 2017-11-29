package andr.wordoor.com.rxjavademo.external.http;

import andr.wordoor.com.rxjavademo.entity.AndroidResponse;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by zrg on 2017/6/27.
 */

public interface APIService {

    @GET("all/20/{pageNum}")
    Observable<AndroidResponse> getAndroidData(@Path("pageNum") int pageNum);
}
