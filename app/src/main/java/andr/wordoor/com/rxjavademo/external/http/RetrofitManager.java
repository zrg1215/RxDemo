package andr.wordoor.com.rxjavademo.external.http;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zhuyifei on 2016/5/18.
 * http://blog.csdn.net/wzgiceman/article/details/51939574
 */
public class RetrofitManager {

    private static final String TAG = RetrofitManager.class.getSimpleName();
    public final static String KEY_TIMEOUT = "key_timeout";

    // 网络请求超时
    private static final long TIME_OUT = 10 * 1000;
    //缓存大小
    private static final long CACHE_SIZE = 20 * 1024 * 1024;

    private RetrofitManager() {
    }

    private static class InstanceHolder {
        private static final RetrofitManager instance = new RetrofitManager();
    }

    public static RetrofitManager getInstance() {
        return InstanceHolder.instance;
    }

    /**
     * 获取一个服务对象，使用Gson转换器，用于json数据的交互
     *
     * @return
     */

    public APIService create(Map<String, String> headers) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://gank.io/api/data/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(getClient(headers))
                .build();
        return retrofit.create(APIService.class);
    }

    /**
     * 获取一个带拦截器的client对象
     * param：headers
     *
     * @return
     */
    private OkHttpClient getClient(Map<String, String> headers) {
        //设置缓存目录
        /*File dir = new File(CachePath.HTTPCACHE);
        Cache cache = new Cache(dir, CACHE_SIZE);*/

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new CoreInterceptor(headers))
                .connectTimeout((headers != null && headers.size() > 0 && headers.containsKey(KEY_TIMEOUT)) ?
                        Long.valueOf(headers.get(KEY_TIMEOUT)) : TIME_OUT, TimeUnit.MILLISECONDS)
                .readTimeout((headers != null && headers.size() > 0 && headers.containsKey(KEY_TIMEOUT)) ?
                        Long.valueOf(headers.get(KEY_TIMEOUT)) : TIME_OUT, TimeUnit.MILLISECONDS)
                //.cache(cache)
                .build();
        return client;
    }

    /**
     * 执行
     *
     * @param call
     * @param callback
     */
    public void enqueue(Call call, Callback callback) {
        call.enqueue(callback);
    }
}
