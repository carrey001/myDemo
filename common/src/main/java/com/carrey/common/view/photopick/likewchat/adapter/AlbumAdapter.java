package com.carrey.common.view.photopick.likewchat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.carrey.common.R;
import com.carrey.common.util.BitmapTools;
import com.carrey.common.view.photopick.likewchat.AlbumModel;

import java.util.ArrayList;
import java.util.List;


/**
 * 专辑列表
 * AlbumAdapter
 * chenbo
 * 2015年3月16日 下午4:19:43
 * @version 1.0
 */
public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.Holder> {
    
    private BitmapTools mBitmapTools;
    private List<AlbumModel>   mItems;
    protected Context mContext;
    protected LayoutInflater mInflater;

	public AlbumAdapter(Context context, ArrayList<AlbumModel> models) {
        mContext = context;
        mItems = models;
        mInflater = LayoutInflater.from(context);

		mBitmapTools = new BitmapTools(context);
		int w = (int)(context.getResources().getDimensionPixelOffset(R.dimen.albumitem_content_height) * 1.2);
		mBitmapTools.getBitmapUtils().configDefaultBitmapMaxSize(w, w);
	}

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewtype) {
        return new Holder(mInflater.inflate(R.layout.item_album, null));
    }


    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final AlbumModel album = mItems.get(position);
        holder.tvName.setText(album.getName());
        holder.tvCount.setText(album.getCount() + "张");
        holder.ivIndex.setVisibility(album.isCheck() ? View.VISIBLE : View.GONE);
        mBitmapTools.display(holder.ivAlbum, album.getRecent());
    }

    @Override
    public int getItemCount() {
        if (mItems != null) {
            return mItems.size();
        } else {
            return 0;
        }
    }

    public AlbumModel getItem(int position) {
        return mItems.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    static class Holder extends RecyclerView.ViewHolder {
        public ImageView ivAlbum, ivIndex;
        public TextView tvName, tvCount;
        
        public Holder(View v) {
            super(v);
            
            ivAlbum = (ImageView) v.findViewById(R.id.iv_album_la);
            ivIndex = (ImageView) v.findViewById(R.id.iv_index_la);
            tvName = (TextView) v.findViewById(R.id.tv_name_la);
            tvCount = (TextView) v.findViewById(R.id.tv_count_la);
        }
    }
    /**
     * 刷新列表
     * refreshList
     * @param list
     * @since 1.0
     */
    public void refreshList(List<AlbumModel> list) {
        mItems = list;
        notifyDataSetChanged();
    }


}
