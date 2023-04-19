package com.homjay.tongtongbiaobaidemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Homjay
 * @date 2022/11/10 10:35
 * @describe
 */
public class DashboardView extends View {
    private int mRadius; // 画布边缘半径（去除padding后的半径）
    private int mStartAngle = 150; // 起始角度
    private int mSweepAngle = 240; // 绘制角度
    private int mMin = 1; // 最小值
    private int mMax = 8; // 最大值
    private int mSection = 8; // 值域（mMax-mMin）等分份数
    private int mPortion = 3; // 一个mSection等分份数
    private String mHeaderText = ""; // 表头
    private int mCreditValue = (int) 8.12; // 信用分
    private int mSparkleWidth; // 亮点宽度
    private int mProgressWidth; // 进度圆弧宽度
    private float mLength1; // 刻度顶部相对边缘的长度
    private int mCalibrationWidth; // 刻度圆弧宽度
    private float mLength2; // 刻度读数顶部相对边缘的长度
    private String statusName = "---";//状态名
    private String statusValue = "---";//状态值

    private int mPadding;
    private float mCenterX, mCenterY; // 圆心坐标
    private Paint mPaint;
    private RectF mRectFProgressArc;
    private RectF mRectFCalibrationFArc;
    private RectF mRectFTextArc;
    private Path mPath;
    private Rect mRectText;
    private String[] mTexts;
    private int mBackgroundColor;
    private int[] mBgColors;


    public DashboardView(Context context) {
        this(context, null);
    }

    public DashboardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DashboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        mSparkleWidth = dp2px(10);
        mProgressWidth = dp2px(3);
        mCalibrationWidth = dp2px(10);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mRectFProgressArc = new RectF();
        mRectFCalibrationFArc = new RectF();
        mRectFTextArc = new RectF();
        mPath = new Path();
        mRectText = new Rect();

        mBackgroundColor = ContextCompat.getColor(getContext(), R.color.colorAccent);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mPadding = Math.max(
                Math.max(getPaddingLeft(), getPaddingTop()),
                Math.max(getPaddingRight(), getPaddingBottom())
        );
        setPadding(mPadding, mPadding, mPadding, mPadding);

        mLength1 = mPadding + mSparkleWidth / 2f + dp2px(8);
        mLength2 = mLength1 + mCalibrationWidth + dp2px(1) + dp2px(5);

        int width = resolveSize(dp2px(220), widthMeasureSpec);
        mRadius = (width - mPadding * 2) / 2;

        setMeasuredDimension(width, width - dp2px(30));

        mCenterX = mCenterY = getMeasuredWidth() / 2f;
        mRectFProgressArc.set(
                mPadding + mSparkleWidth / 2f,
                mPadding + mSparkleWidth / 2f,
                getMeasuredWidth() - mPadding - mSparkleWidth / 2f,
                getMeasuredWidth() - mPadding - mSparkleWidth / 2f
        );

        mRectFCalibrationFArc.set(
                mLength1 + mCalibrationWidth / 2f,
                mLength1 + mCalibrationWidth / 2f,
                getMeasuredWidth() - mLength1 - mCalibrationWidth / 2f,
                getMeasuredWidth() - mLength1 - mCalibrationWidth / 2f
        );

        mPaint.setTextSize(sp2px(10));
        mPaint.getTextBounds("0", 0, "0".length(), mRectText);
        mRectFTextArc.set(
                mLength2 + mRectText.height(),
                mLength2 + mRectText.height(),
                getMeasuredWidth() - mLength2 - mRectText.height(),
                getMeasuredWidth() - mLength2 - mRectText.height()
        );
    }


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawColor(mBackgroundColor);
//        canvas.drawColor(R.color.color_blue);

        /**
         * 画进度圆弧背景
         */
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mProgressWidth);
        mPaint.setAlpha(80);
        mPaint.setColor(Color.WHITE);
        canvas.drawArc(mRectFProgressArc, mStartAngle + 1, mSweepAngle - 2, false, mPaint);

        mPaint.setAlpha(255);
        /**
         * 画刻度圆弧
         */
        mPaint.setShader(null);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.WHITE);
        mPaint.setAlpha(80);
        mPaint.setStrokeCap(Paint.Cap.SQUARE);
        mPaint.setStrokeWidth(mCalibrationWidth);
        mPaint.setColor(mBackgroundColor);


        canvas.save();
        if (mTexts != null) {
            float count = 0;
            for (int i = 0; i < mTexts.length; i++) {
                count += Float.parseFloat(mTexts[i]);
            }
            for (int i = 0; i < mTexts.length; i++) {
                float draw = 0;
                mPaint.setColor(mBgColors[i]);
                if (i>0){
                    for (int j = 0; j <= i-1 ; j++) {
                        draw += Float.parseFloat(mTexts[j]);
                    }
                    float sweept =  mSweepAngle / count * (Float.parseFloat(mTexts[i])) ;
                    canvas.drawArc(mRectFCalibrationFArc, mStartAngle + mSweepAngle  / count * draw  , sweept, false, mPaint);
//                    TVLog.e("countSweep ======= " + sweept  + " draw === " + draw);

                    canvas.restore();
                    // 顺时针到结尾处
                    canvas.save();
                }else {
                    float sweept = mSweepAngle  / count * Float.parseFloat(mTexts[i]);
                    canvas.drawArc(mRectFCalibrationFArc, mStartAngle ,sweept , false, mPaint);
//                    TVLog.e("countSweep ======= " + sweept);
                    canvas.restore();
                    // 顺时针到结尾处
                    canvas.save();
                }
            }

        /**
         * 画长刻度读数
         * 添加一个圆弧path，文字沿着path绘制
         */
        mPaint.setTextSize(sp2px(10));
        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAlpha(160);
        mPaint.setColor(Color.WHITE);



        /**
         * 画健康状态
         */
        mPaint.setAlpha(255);
        mPaint.setTextSize(sp2px(24));
        mPaint.setColor(Color.WHITE);
        mPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(statusName, mCenterX, mCenterY, mPaint);
//        canvas.drawText("良好", mCenterX, mCenterY, mPaint);

        /**
         * 画健康值
         */
        mPaint.setAlpha(255);
        mPaint.setTextSize(sp2px(34));
        mPaint.setColor(Color.WHITE);
        mPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(statusValue, mCenterX, mCenterY + dp2px(40), mPaint);
//        canvas.drawText("100", mCenterX, mCenterY + dp2px(40), mPaint);

//        TVLog.e("status ====== " + statusName  + statusValue);
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                Resources.getSystem().getDisplayMetrics());
    }

    private int sp2px(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                Resources.getSystem().getDisplayMetrics());
    }

    /**
     * 相对起始角度计算所对应的角度大小
     */
    private float calculateRelativeAngleWithValue(int value) {
        float degreePerSection = 1f * mSweepAngle / mSection;
        if (value > 6) {
            return 8 * degreePerSection + 2 * degreePerSection / 250 * (value - 6);
        } else if (value > 5) {
            return 6 * degreePerSection + 2 * degreePerSection / 50 * (value - 5);
        } else if (value > 4) {
            return 4 * degreePerSection + 2 * degreePerSection / 50 * (value - 4);
        } else if (value > 3) {
            return 2 * degreePerSection + 2 * degreePerSection / 50 * (value - 3);
        } else {
            return 2 * degreePerSection / 200 * (value - 1);
        }
    }



    public int getmRadius() {
        return mRadius;
    }

    public void setmRadius(int mRadius) {
        this.mRadius = mRadius;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
        invalidate();

    }

    public String getStatusValue() {
        return statusValue;
    }

    public void setStatusValue(String statusValue) {
        this.statusValue = statusValue;
        invalidate();
    }

    public int getmSection() {
        return mSection;
    }

    public void setmSection(int mSection,String[] mTexts,int[] mBgColors) {
        this.mSection = mSection;
        this.mTexts = mTexts;
        this.mBgColors = mBgColors;
        invalidate();
    }
}