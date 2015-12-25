package com.carrey.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.carrey.common.R;

import java.text.DecimalFormat;

/**
 * 类描述：
 * 创建人：carrey
 * 创建时间：2015/12/25 10:43
 */

public class RulerView extends FrameLayout {


    /**
     * 修正两边
     */
    private static int DELTA_WIDTH = 1;


    /**
     * 水平滑动
     */
    private ScrollAwareHorizontalScrollView mHsvHorizontalScale;
    private ImageRulerView mIvHorizontalSacleBg;
    private ImageView mIvHorizontalScaleThumbBg;
    private ImageView mIvHorizontalScaleTopThumbBg;
    private float mHorizontalMin;
    private float mHorizontalMax;
    private float mHorizontalScale;
    // 获取第一个刻度
    private float mFirstMarkValue;

    // 每刻度间隔值
    private float mDeltaValue;

    // 每刻度间隔
    private int mDeltaPixel;

    private boolean mIsVertical;

    private boolean mSetFlag;

    DecimalFormat dfKedu = new DecimalFormat("#.#");
    Rect rect = new Rect();

    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);

    OnScaleChangeListener mOnScaleChangeListener;


    public RulerView(Context context) {
        this(context, null);
    }

    public RulerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RulerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.RulerView, defStyleAttr, 0);

        mHorizontalMin = ta.getFloat(R.styleable.RulerView_scaleMin, 0);
        mHorizontalMax = ta.getFloat(R.styleable.RulerView_scaleMax, 100);
        mFirstMarkValue = ta.getFloat(R.styleable.RulerView_scaleFirstMarkValue, mHorizontalMin);
        mDeltaValue = ta.getFloat(R.styleable.RulerView_scaleDeltaValue, 1);
        mDeltaPixel = ta.getInt(R.styleable.RulerView_scaleDeltaPixel, 20);
        mIsVertical = ta.getBoolean(R.styleable.RulerView_scaleVertical, false);

        Drawable thumb = ta.getDrawable(R.styleable.RulerView_scaleThumb);
        Drawable topThumb = ta.getDrawable(R.styleable.RulerView_scaleTopThumb);

        ta.recycle();

        ///控件初始化
        mHsvHorizontalScale = new ScrollAwareHorizontalScrollView(context);
        mIvHorizontalSacleBg = new ImageRulerView(context);
        mIvHorizontalScaleThumbBg = new ImageView(context);
        mIvHorizontalScaleTopThumbBg = new ImageView(context);


        if (thumb != null) {
            mIvHorizontalScaleThumbBg.setBackgroundDrawable(thumb);
        }
        if (topThumb != null) {
            mIvHorizontalScaleTopThumbBg.setBackgroundDrawable(topThumb);
        }

        // 水平滚动栏和尺子部分（尺子包含在LinearLayout中）
        LinearLayout ll = new LinearLayout(context);
        LayoutParams tparams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        ll.addView(mIvHorizontalSacleBg);
        mHsvHorizontalScale.setHorizontalScrollBarEnabled(false);
        mHsvHorizontalScale.addView(ll, tparams);
        this.addView(mHsvHorizontalScale, tparams);

        // 指针部分 下指针
        LayoutParams thumbParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        thumbParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
        this.addView(mIvHorizontalScaleThumbBg, thumbParams);
        ///上指针
        LayoutParams topThumbParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        topThumbParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
        this.addView(mIvHorizontalScaleTopThumbBg, topThumbParams);

        if (isInEditMode()) {
            return;
        }

        ///尺子滑动监听
        mHsvHorizontalScale.setOnScrollChangedListener(new OnScrollChangedListener() {

            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) {
                int ivWidth = mIvHorizontalSacleBg.getWidth() - DELTA_WIDTH;
                int scrollX = mHsvHorizontalScale.getScrollX();

                float scale = scrollX * (mHorizontalMax - mHorizontalMin) / ivWidth + mHorizontalMin;
                mHorizontalScale = scale;

                if (mOnScaleChangeListener != null) {
                    mOnScaleChangeListener.OnScaleChange(Math.round(mHorizontalScale));
                    mOnScaleChangeListener.OnScaleChange(mHorizontalScale);
                }
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        mIvHorizontalScaleThumbBg.measure(widthMeasureSpec, heightMeasureSpec);
        mIvHorizontalScaleTopThumbBg.measure(widthMeasureSpec, heightMeasureSpec);

        int halfWidth = MeasureSpec.getSize(widthMeasureSpec) / 2;

        params.setMargins(halfWidth, 0, halfWidth, 0);
        params.height = MeasureSpec.getSize(heightMeasureSpec);
        mIvHorizontalSacleBg.setLayoutParams(params);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mSetFlag) {
            changeHorizontalScroll();
        }

    }

    public OnScaleChangeListener getmOnScaleChangeListener() {
        return mOnScaleChangeListener;
    }

    public void setmOnScaleChangeListener(OnScaleChangeListener mOnScaleChangeListener) {
        this.mOnScaleChangeListener = mOnScaleChangeListener;
    }


    /**
     * 设置水平刻度
     *
     * @param horizontalScale 水平刻度
     */
    public void setScale(float horizontalScale) {

        if (horizontalScale < mHorizontalMin || horizontalScale > mHorizontalMax) {
            return;
        }
        mHorizontalScale = horizontalScale;
        mSetFlag = true;
        changeHorizontalScroll();
    }

    //根据设置的水平刻度滚动到响应的位置
    private void changeHorizontalScroll() {

        int ivWidth = mIvHorizontalSacleBg.getWidth() - DELTA_WIDTH;
        float scrollx = (mHorizontalScale - mHorizontalMin) * ivWidth / (mHorizontalMax - mHorizontalMin);
        mHsvHorizontalScale.scrollTo(Math.round(scrollx), 0);
    }


    public class ImageRulerView extends View {

        public static final String TAG = "ImageRulerView";
        private Paint framePaint;
        private Paint rulerPaint;
        private Paint textPaint;
        private Paint backgroudPaint;

        public ImageRulerView(Context context) {
            this(context, null);
        }

        public ImageRulerView(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }


        public ImageRulerView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init(context, attrs, defStyleAttr);

        }


        private void init(Context context, AttributeSet attrs, int defStyleAttr) {

            //获取焦点
            setFocusable(true);
            //背景画笔
            backgroudPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            backgroudPaint.setColor(Color.WHITE);
            //文字画笔
            textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            textPaint.setColor(Color.parseColor("#999999"));
            textPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.text_size_15));
            textPaint.setTextAlign(Paint.Align.CENTER);

            //尺子画笔
            rulerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            rulerPaint.setColor(Color.parseColor("#999999"));
            rulerPaint.setStrokeWidth(1);
            rulerPaint.setStyle(Paint.Style.STROKE);
            //边框画笔
            framePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            framePaint.setColor(Color.parseColor("#dddddd"));
            framePaint.setStrokeWidth(1);
            framePaint.setStyle(Paint.Style.STROKE);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
            int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);

            int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
            int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

            if (widthSpecMode != MeasureSpec.EXACTLY) {  ///如果模式不是精准的
                widthSpecSize = (int) ((mHorizontalMax - mHorizontalMin) / mDeltaValue * mDeltaPixel)
                        + DELTA_WIDTH;
            }

            if (heightSpecMode != MeasureSpec.EXACTLY) {
                heightSpecSize = 100;
            }
            setMeasuredDimension(widthSpecSize, heightSpecSize);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            int height = getMeasuredHeight();
            int width = getMeasuredWidth();

            // 各线高度，起点值
            int heightText = (int) (height * 0.3);
            int heightRuler = (int) (height * 0.6);
            int heightRuler5 = (int) (height * 0.5);
            int heightRuler10 = (int) (height * 0.4);
            int heightEnd = (int) (height * 0.8);

            canvas.drawRect(0, 0, width, height, backgroudPaint);

            double pos = mFirstMarkValue;
            ///画刻度
            for (int index = 0; Float.compare((float) pos, mHorizontalMax) <= 0; pos += mDeltaValue, index++) {
                int coordX = (int) ((pos - mHorizontalMin) / mDeltaValue * mDeltaPixel);

                if (index % 10 == 0) {
                    textPaint.getTextBounds(dfKedu.format(pos), 0, dfKedu.format(pos).length(), rect);

                    if (rect.width() / 2 > width - coordX) {// 右侧刻度显示不下时右对齐
                        // 画刻度文字
                        textPaint.setTextAlign(Paint.Align.RIGHT);
                        canvas.drawText(dfKedu.format(pos), coordX, heightText, textPaint);
                        textPaint.setTextAlign(Paint.Align.CENTER);
                    } else if (rect.width() / 2 > coordX) {// 左侧刻度显示不下时左对齐
                        // 画刻度文字
                        textPaint.setTextAlign(Paint.Align.LEFT);
                        canvas.drawText(dfKedu.format(pos), coordX, heightText, textPaint);
                        textPaint.setTextAlign(Paint.Align.CENTER);
                    } else {
                        // 画刻度文字
                        canvas.drawText(dfKedu.format(pos), coordX, heightText, textPaint);
                    }

                    canvas.drawLine(coordX, heightRuler10, coordX, heightEnd, rulerPaint);
                } else if (index % 5 == 0) {
                    canvas.drawLine(coordX, heightRuler5, coordX, heightEnd, rulerPaint);
                } else {
                    canvas.drawLine(coordX, heightRuler, coordX, heightEnd, rulerPaint);
                }
            }
            // 画尺子边框
//			canvas.drawLine(0, 0, width, 0, framePaint);// 上
//			canvas.drawLine(0, height - 1, width, height - 1, framePaint);// 下
//			canvas.drawLine(0, 0, 0, height, framePaint);// 左
//			canvas.drawLine(width - 1, 0, width - 1, height, framePaint);// 右

        }
    }

    public class ScrollAwareHorizontalScrollView extends HorizontalScrollView {
        OnScrollChangedListener mOnScrollChangedListener;

        /**
         * @param context
         */
        public ScrollAwareHorizontalScrollView(Context context) {
            super(context);
        }

        /**
         * @param context
         * @param attrs
         */
        public ScrollAwareHorizontalScrollView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        /**
         * @param context
         * @param attrs
         * @param defStyle
         */
        public ScrollAwareHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {
            super.onScrollChanged(l, t, oldl, oldt);

            if (mOnScrollChangedListener != null) {
                mOnScrollChangedListener.onScrollChanged(l, t, oldl, oldt);
            }
        }

        /**
         * @param onScrollChangedListener the onScrollChangedListener to set
         */
        public void setOnScrollChangedListener(OnScrollChangedListener onScrollChangedListener) {
            this.mOnScrollChangedListener = onScrollChangedListener;
        }

    }

    public interface OnScaleChangeListener {
        public void OnScaleChange(int scale);

        public void OnScaleChange(float scale);
    }

    public interface OnScrollChangedListener {
        public void onScrollChanged(int l, int t, int oldl, int oldt);
    }
}
