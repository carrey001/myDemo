package com.carrey.mydemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.carrey.common.view.photopick.likewchat.PhotoPicker;

/**
 * 类描述：
 * 创建人：carrey
 * 创建时间：2016/1/13 15:16
 */

public class PicPick extends BaseAct {
    /**
     * 初始化View
     */
    @Override
    protected void initViews() {
        setContentView(R.layout.activity_pic_pick);

    }

    /**
     * 初始化数据
     *
     * @param savedInstanceState
     */
    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    public void single(View view) {
        PhotoPicker picker = new PhotoPicker(this);
        picker.setMaxCount(1);
        picker.pickPhoto();
    }

    public void MultiSelect(View view) {
        PhotoPicker picker = new PhotoPicker(this);
        picker.setMaxCount(4);
        picker.pickPhoto();
    }

    public void cut(View view) {
        PhotoPicker picker = new PhotoPicker(this);
        picker.setNeedCrop(true);
        picker.pickPhoto();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
