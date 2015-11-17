package com.qfq.muqing.myvideoplayer.adapters;

/**
 * Created by Administrator on 2015/11/15.
 */

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.qfq.muqing.myvideoplayer.R;

import java.util.ArrayList;

public class StaggeredAdapter extends RecyclerView.Adapter<StaggeredAdapter.VerticalItemHolder> {

    private ArrayList<VideoItem> mItems;

    private AdapterView.OnItemClickListener mOnItemClickListener;

    private Context mContext;

    public StaggeredAdapter(Context context) {
        mContext = context;
        mItems = new ArrayList<VideoItem>();
        generateItems();
    }

    public void removeItem(int position) {
        if (position >= mItems.size())
            return;
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public VerticalItemHolder onCreateViewHolder(ViewGroup container, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(container.getContext());
        View root = layoutInflater.inflate(R.layout.item_staggredview, container, false );
        return new VerticalItemHolder(root, this);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public void onBindViewHolder(VerticalItemHolder itemHolder, int position) {

        final View itemView = itemHolder.videoThumbnail;
        if (position % 4 == 0) {
            itemView.setMinimumHeight(300);
        } else {
            itemView.setMinimumHeight(100);
        }
        VideoItem item = mItems.get(position);
        itemHolder.setVideoTitle(item.videoName);
        itemHolder.setVideoDuration(item.videoDuration);
        itemHolder.setVideoProgreee(item.videoProgress);
    }

    public static class VerticalItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView videoThumbnail;
        private TextView videoTitle;
        private TextView videoDuration;
        private TextView videoProgreee;

        private StaggeredAdapter mAdapter;

        public VerticalItemHolder(View v, StaggeredAdapter adapter) {
            super(v);
            v.setOnClickListener(this);
            mAdapter = adapter;

            videoThumbnail = (ImageView)v.findViewById(R.id.item_staggredview_thumbnail);
            videoTitle = (TextView)v.findViewById(R.id.item_staggredview_video_title);
            videoDuration = (TextView)v.findViewById(R.id.item_staggredview_video_duration);
            videoProgreee = (TextView)v.findViewById(R.id.item_staggredview_video_progress);
        }

        @Override
        public void onClick(View v) {
            mAdapter.onItemHolderClick(this);
        }


        public void setVideoThumbnail(Drawable videoThumbnail) {
            this.videoThumbnail.setImageDrawable(videoThumbnail);
        }

        public void setVideoTitle(String videoTitle) {
            this.videoTitle.setText(videoTitle);
        }

        public void setVideoDuration(String videoDuration) {
            this.videoDuration.setText(videoDuration);
        }

        public void setVideoProgreee(String videoProgreee) {
            this.videoProgreee.setText(videoProgreee);
        }

    }

    public static class VideoItem {
        public String videoId;
        public String videoPath;
        public String videoName;
        public String videoDuration;
        public String videoProgress;

        public VideoItem(String videoId, String videoPath, String videoName, String videoDuration) {
            this.videoPath = videoPath;
            this.videoId = videoId;
            this.videoName =videoName;
            this.videoDuration = videoDuration;
            this.videoProgress = null;
        }
    }

    public void setmOnItemClickListener(AdapterView.OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    private void onItemHolderClick(VerticalItemHolder itemHoler) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(null, itemHoler.itemView,
                    itemHoler.getAdapterPosition(), itemHoler.getItemId());
        }
    }

    private void generateItems() {
        String[] videoColumns = {
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.TITLE,
            MediaStore.Video.Media.DURATION,
        };


        Cursor cursor = mContext.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, videoColumns, null, null,null);
        int totalCount = cursor.getCount();
        Log.i("totalCount.........", "count");
        cursor.moveToFirst();
        for (int i=0; i<totalCount; i++)
        {
            String videoId = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
            String videoPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
            String videoDuration = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
            String videoTitle = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
            VideoItem item = new VideoItem(videoId, videoPath, videoTitle, videoDuration);
            mItems.add(item);
            cursor.moveToNext();
        }
        cursor.close();
    }
}
