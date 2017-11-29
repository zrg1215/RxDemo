package andr.wordoor.com.rxjavademo.external.http;

import andr.wordoor.com.rxjavademo.entity.AndroidResponse;
import rx.Observable;

/**
 * Created by zrg on 2017/6/27.
 */

public class MainHttp  {

    public static Observable<AndroidResponse> getAndroidData(int pageNum){
        RetrofitManager manager = RetrofitManager.getInstance();
        return manager.create(null).getAndroidData(pageNum);
    }
}
