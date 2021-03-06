package com.carrey.mydemo.zxing;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;

import com.carrey.common.util.NetWorkUtil;
import com.carrey.common.util.SchemeUtil;
import com.carrey.common.util.SystemUtil;
import com.carrey.mydemo.R;
import com.carrey.mydemo.zxing.camera.CameraManager;
import com.carrey.mydemo.zxing.decoding.CaptureActivityHandler;
import com.carrey.mydemo.zxing.decoding.InactivityTimer;
import com.carrey.mydemo.zxing.view.ViewfinderView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.lidroid.xutils.util.LogUtils;

import java.io.IOException;
import java.util.Vector;

/**
 * Initial the camera
 *
 * @author Ryan.Tang
 */
public class CaptureActivity extends Activity implements Callback, View.OnClickListener {
    private static final String SCHEME = "com";

    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        initViews();

    }



    protected void initViews() {

        CameraManager.init(getApplication());
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        findViewById(R.id.tv_capture_back).setOnClickListener(this);
    }



//    @Override
//    protected void initTintStatusBar() {
//        super.initTintStatusBar();
//        mStatusBarTintView.setBackgroundResource(android.R.color.background_dark);
//    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    /**
     * 处理扫描结果
     *
     * @param rawResult
     * @param barcode
     */
    public void handleDecode(Result rawResult, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String result = rawResult.getText();
        if (!TextUtils.isEmpty(result) && (result.contains(SCHEME) | NetWorkUtil.checkURL(result))) {

            if (result.contains("for_type=weixin") && result.contains("docDetail")) { //医生详情
                String docId = "";
                String[] split = result.split("\\u003F");
                if (!TextUtils.isEmpty(split[1])) {  //for_type=weixin&docId=3a24cb5b173e4d6597f62a95d04e3340
                    String[] strings = split[1].split("&");
                    for (String s : strings) {
                        String[] split1 = s.split("=");
                        if ("docId".equals(split1[0])) {
                            docId = split1[1];
                            break;
                        }
                    }
                }
                if (!TextUtils.isEmpty(docId)) { ////跳转到医生页面 com.wondersgroup.hs.healthcloud：//patient/doctor_details
                    //com.wondersgroup.hs.healthcloud：//docu/doctor_details
                    String packageName = SystemUtil.getApplicationPackageName(this);
                    StringBuilder builder = new StringBuilder(SCHEME);
                    builder.append("://");
                    builder.append(packageName.substring(packageName.lastIndexOf(".") + 1));
                    builder.append("/doctor_details?doctor_id=").append(docId);
                    LogUtils.d("==="+builder.toString());
                    SchemeUtil.startActivity(this, builder.toString());
                } else {
                    startActivity(new Intent(this, CaptureErrorResultActivity.class));
                }
            } else {
                SchemeUtil.startActivity(this, result);
            }
            finish();
        } else {
//            UIUtil.toastShort(this, "错误扫码");
            startActivity(new Intent(this, CaptureErrorResultActivity.class));
        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats,
                    characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.argon);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_capture_back) {
            finish();
        }
    }
}