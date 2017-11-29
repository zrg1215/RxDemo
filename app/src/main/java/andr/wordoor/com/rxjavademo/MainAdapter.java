package andr.wordoor.com.rxjavademo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import andr.wordoor.com.rxjavademo.entity.AndroidResponse;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zrg on 2017/6/27.
 */

public class MainAdapter extends RecyclerViewLoadMoreAdapter {
    private final int TYPE_ITEM = 0x0010;
    private Context mContext;
    private List<AndroidResponse.Result> mList;

    public MainAdapter(Context context, List<AndroidResponse.Result> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position != getItemCount() - 1) {
            return TYPE_ITEM;
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_result, parent, false);
            return new ItemViewHolder(view);
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            AndroidResponse.Result result = mList.get(position);
            if (result != null) {
                itemViewHolder.mTvPublisher.setText(result.who);
                itemViewHolder.mTvPublishTime.setText(result.publishedAt);
                itemViewHolder.mTvDesc.setText(result.desc);
            }
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_publisher)
        TextView mTvPublisher;
        @Bind(R.id.tv_publish_time)
        TextView mTvPublishTime;
        @Bind(R.id.img_cover)
        ImageView mImgCover;
        @Bind(R.id.tv_desc)
        TextView mTvDesc;

        ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
