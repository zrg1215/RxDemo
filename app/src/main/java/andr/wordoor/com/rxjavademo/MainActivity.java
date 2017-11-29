package andr.wordoor.com.rxjavademo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import andr.wordoor.com.rxjavademo.entity.AndroidResponse;
import andr.wordoor.com.rxjavademo.external.http.MainHttp;
import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, RecyclerViewLoadMoreAdapter.OnLoadMoreListener {
    private static final String TAG = "MainActivity";

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipRefreshLayout)
    SwipeRefreshLayout mSwipRefreshLayout;

    protected Subscription subscription;
    private MainAdapter mAdapter;
    private List<AndroidResponse.Result> mList;
    private int mPageNum = 1;

    Subscriber<AndroidResponse.Result> observer = new Subscriber<AndroidResponse.Result>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "onError: ", e);
            mSwipRefreshLayout.setRefreshing(false);
            Toast.makeText(MainActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNext(AndroidResponse.Result reuslts) {
            if (reuslts != null) {
                getResultSuccess(reuslts);
                return;
            }
            stopRefresh(null);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mList = new ArrayList<>();

        mSwipRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        mSwipRefreshLayout.setOnRefreshListener(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MainAdapter(this, mList);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setRecyclerView(mRecyclerView);
        mAdapter.setOnLoadMoreListener(this);

        search();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unsubscribe();
    }

    private void unsubscribe() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    private void search() {
        subscription = MainHttp.getAndroidData(mPageNum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<AndroidResponse, List<AndroidResponse.Result>>() {
                    @Override
                    public List<AndroidResponse.Result> call(AndroidResponse androidResponse) {
                        return androidResponse.results;
                    }
                })
                .flatMap(new Func1<List<AndroidResponse.Result>, Observable<AndroidResponse.Result>>() {
                    @Override
                    public Observable<AndroidResponse.Result> call(List<AndroidResponse.Result> results) {
                        return Observable.from(results);
                    }
                })
                .filter(new Func1<AndroidResponse.Result, Boolean>() {
                    @Override
                    public Boolean call(AndroidResponse.Result result) {
                        return "android".equalsIgnoreCase(result.type);
                    }
                })
                .subscribe(observer);
    }

    private void getResultSuccess(AndroidResponse.Result results) {
        if (isFinishing()) return;
        if (results != null) {
            mList.add(results);

            /*if (mPageNum == 1) {
                mList.clear();
            }
            mList.addAll(results);
            if (results.size() > 0) {
                mPageNum++;
                stopRefresh(null);
                mAdapter.notifyDataSetChanged();
            } else {
                stopRefresh("没有更多数据");
            }*/
        }
    }

    @Override
    public void onRefresh() {
        mPageNum = 1;
        search();
    }

    @Override
    public void OnLoadMore() {
        search();
    }

    private void stopRefresh(String message) {
        mSwipRefreshLayout.setRefreshing(false);
        if (mAdapter != null) {
            mAdapter.setLoading(false);
            mAdapter.setLoadedHint(message);
        }
    }
}
