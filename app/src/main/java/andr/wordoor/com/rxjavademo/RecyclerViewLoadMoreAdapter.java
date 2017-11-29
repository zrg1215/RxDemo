package andr.wordoor.com.rxjavademo;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 用于RecyclerView上拉加载更多操作,
 * https://github.com/AndroidStudyNew/AndroidSamples/tree/master/refreshloadmore
 * Created by zhuyifei on 2016/5/30.
 */
public class RecyclerViewLoadMoreAdapter extends Adapter<ViewHolder> {

    private static final int TYPE_FOOTER = 0x11001;

    public void setRecyclerView(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(-1)) {
                    onScrolledToTop();
                } else if (!recyclerView.canScrollVertically(1)) {
                    onScrolledToBottom();
                } else if (dy < 0) {
                    onScrolledUp();
                } else if (dy > 0) {
                    onScrolledDown();
                }
            }

            public void onScrolledUp() {
            }

            public void onScrolledDown() {
            }

            public void onScrolledToTop() {
            }

            private void onScrolledToBottom() {
                if (onLoadMoreListener != null) {
                    setLoading(true);
                    onLoadMoreListener.OnLoadMore();
                }
            }
        });
    }


    public void setLoading(boolean flag) {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(flag ? View.VISIBLE : View.GONE);
        }
        if (mTvLoadingMore != null && flag) {
            mTvLoadingMore.setVisibility(View.VISIBLE);
            mTvLoadingMore.setText("loading...");
        }

    }

    /**
     * 加载结束提示信息
     *
     * @param text 低部显示提示信息
     */
    public void setLoadedHint(CharSequence text) {
        if (null != mProgressBar && mProgressBar.getVisibility() == View.VISIBLE) {
            mProgressBar.setVisibility(View.GONE);
        }
        if (mTvLoadingMore != null) {
            if (!TextUtils.isEmpty(text)) {
                mTvLoadingMore.setText(text);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_foot, parent, false);
            return new FootViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof FootViewHolder) {
            mTvLoadingMore = ((FootViewHolder) holder).loadingTitle;
            mProgressBar = ((FootViewHolder) holder).loadingProgress;
        }
    }

    private TextView mTvLoadingMore;
    private ProgressBar mProgressBar;

    class FootViewHolder extends ViewHolder {

        ProgressBar loadingProgress;
        TextView loadingTitle;

        FootViewHolder(View view) {
            super(view);
            loadingProgress = (ProgressBar) view.findViewById(R.id.pb_load_animate);
            loadingTitle = (TextView) view.findViewById(R.id.tv_load_title);
        }

    }

    public interface OnLoadMoreListener {
        void OnLoadMore();
    }

    private OnLoadMoreListener onLoadMoreListener;

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }
}
