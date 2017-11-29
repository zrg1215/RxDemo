package andr.wordoor.com.rxjavademo.external.http;

import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zhuyifei on 2016/5/18.
 */
public class CoreInterceptor implements Interceptor {
    private static final String TAG = CoreInterceptor.class.getSimpleName();

    public final static String KEY_NONCE = "Nonce";
    public final static String KEY_TIMESTAMP = "Timestamp";
    public final static String KEY_SIGNATURE = "Signature";

    private Map<String, String> headers;

    public CoreInterceptor(Map<String, String> headers) {
        this.headers = new HashMap<>();
    }


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = null;
        Response response = null;
        Request.Builder builder = chain.request().newBuilder();
        if (headers != null && headers.size() > 0) {
            Set<String> keys = headers.keySet();
            for (String headerKey : keys) {
                builder.addHeader(headerKey, headers.get(headerKey));
                //builder.addHeader(headerKey, headers.get(headerKey)).build();
            }
        }
        request = builder.build();
        response = chain.proceed(request);

        Log.d(TAG, "response.code =" + response.code());
        Log.d(TAG, "----        Request       ---- " + "\n" + request.toString());
        Log.d(TAG, "----        Response      ---- " + "\n" + response.toString());
        return response;
    }
}
