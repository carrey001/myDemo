package com.carrey.common.view.photopick.likewchat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.carrey.common.R;
import com.carrey.common.util.BitmapTools;
import com.carrey.common.util.SystemUtil;
import com.carrey.common.util.UIUtil;
import com.carrey.common.view.photopick.likewchat.PhotoModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片选择列表
 * PhotoSelectorAdapter
 * chenbo
 * 2015年3月16日 下午4:33:01
 * @version 1.0
 */
public class PhotoSelectorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private int itemWidth;
	private int horizentalNum = 3;
	private onPhotoItemCheckedListener listener;
	private onItemClickListener mCallback;
	
	private BitmapTools mBitmapTools;

    private List<PhotoModel> mItems;
    protected Context mContext;
    protected LayoutInflater mInflater;

	private PhotoSelectorAdapter(Context context, ArrayList<PhotoModel> models) {
        mContext = context;
        mItems = models;
        mInflater = LayoutInflater.from(context);

        mBitmapTools = new BitmapTools(context);
	}

	public PhotoSelectorAdapter(Context context, ArrayList<PhotoModel> models, onPhotoItemCheckedListener listener, onItemClickListener mCallback) {
		this(context, models);
		setItemWidth();
		this.listener = listener;
		this.mCallback = mCallback;
	}

	public static interface onPhotoItemCheckedListener {
        public void onCheckedChanged(PhotoModel photoModel,
                                     CompoundButton buttonView, boolean isChecked);
    }

    public interface onItemClickListener {
        public void onItemClick(int position);
    }

	public void setItemWidth() {
	    int screenWidth = SystemUtil.getScreenWidth();
		int horizentalSpace = mContext.getResources().getDimensionPixelSize(R.dimen.S);
		this.itemWidth = (screenWidth - (horizentalSpace * (horizentalNum - 1))) / horizentalNum;
        mBitmapTools.getBitmapUtils().configDefaultBitmapMaxSize(itemWidth, itemWidth);
	}


	@Override
	public int getItemViewType(int position) {
	    return getItem(position).getType();
	}
    public PhotoModel getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public int getItemCount() {
        if (mItems != null) {
            return mItems.size();
        } else {
            return 0;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewtype) {
	    switch (viewtype) {
        case PhotoModel.TYPE_CAMERA:
            View view = mInflater.inflate(R.layout.view_camera, null);
            view.setLayoutParams(new RecyclerView.LayoutParams(itemWidth, itemWidth));
            return new CameraHolder(view);
        case PhotoModel.TYPE_PHOTO:
        default:
            View item = mInflater.inflate(R.layout.layout_photoitem, null);
            item.setLayoutParams(new RecyclerView.LayoutParams(itemWidth, itemWidth));
            return new PhotoHolder(item);
        }
    }
    /**
     * 刷新列表
     * refreshList
     * @param list
     * @since 1.0
     */
    public void refreshList(List<PhotoModel> list) {
        mItems = list;
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final PhotoModel model = getItem(position);
        switch (model.getType()) {
        case PhotoModel.TYPE_CAMERA:
            CameraHolder cameraHolder = (CameraHolder) holder;
            cameraHolder.cameraText.setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    if (mCallback != null) {
                        mCallback.onItemClick(position);
                    }
                }
            });
            break;
        case PhotoModel.TYPE_PHOTO:
            PhotoHolder photoHolder = (PhotoHolder) holder;
            final ImageView ivPhoto = photoHolder.ivPhoto;
            final CheckBox cbPhoto = photoHolder.cbPhoto;
            final View cover = photoHolder.ivCover;
            mBitmapTools.display(ivPhoto, model.getOriginalPath(), BitmapTools.SizeType.SMALL);
            ivPhoto.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (mCallback != null) {
                        mCallback.onItemClick(position);
                    }
                }
            });
            cbPhoto.setChecked(model.isChecked());
            if (cbPhoto.isChecked()) {
                cover.setVisibility(View.VISIBLE);
            } else {
                cover.setVisibility(View.INVISIBLE);
            }
            cbPhoto.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    model.setChecked(cbPhoto.isChecked());
                    if (cbPhoto.isChecked()) {
                        cover.setVisibility(View.VISIBLE);
                    } else {
                        cover.setVisibility(View.INVISIBLE);
                    }
                    if (listener != null) {
                        listener.onCheckedChanged(model, cbPhoto, cbPhoto.isChecked());
                    }
                }
            });

            break;
        default:
            break;
            
        }
    }
	
	public static class PhotoHolder extends RecyclerView.ViewHolder {
	    private ImageView ivPhoto;
	    private CheckBox cbPhoto;
        private View ivCover;
	    
        public PhotoHolder(View v) {
            super(v);
            ivPhoto = (ImageView) v.findViewById(R.id.iv_photo_lpsi);
            UIUtil.setTouchEffect(ivPhoto);
            cbPhoto = (CheckBox) v.findViewById(R.id.cb_photo_lpsi);
            ivCover = v.findViewById(R.id.iv_cover);
        }
	}
	
	public static class CameraHolder extends RecyclerView.ViewHolder {
	    TextView cameraText;
	    
        public CameraHolder(View v) {
            super(v);
            cameraText = (TextView) v.findViewById(R.id.tv_camera_vc);
        }
	    
	}
}
