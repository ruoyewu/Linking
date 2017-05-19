package com.wuruoye.linking2.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.AttributeSet;

import com.wuruoye.linking2.R;

/**
 * Created by wuruoye on 2017/5/13.
 * this file is to do
 */

public class ShineTextView extends android.support.v7.widget.AppCompatTextView {
    private int mWidth;
    private int mTranslate;
    private Matrix mGradientMatrix;
    private LinearGradient mLinearGradient;
    private Paint mPaint;

    private int[] colors = new int[]{
            ActivityCompat.getColor(getContext(), R.color.chinook),
            ActivityCompat.getColor(getContext(),R.color.indigo),
            ActivityCompat.getColor(getContext(),R.color.perfume)
    };
    private int direct = 1;

    public void setColor(int... colors){
        this.colors = colors;
        mLinearGradient = new LinearGradient(0,0,mWidth/2,0,colors,null,Shader.TileMode.CLAMP);
    }

    private void init(){
        mWidth = getMeasuredWidth();
        mPaint = getPaint();
        mLinearGradient = new LinearGradient(0,0,mWidth/2,0,colors,null, Shader.TileMode.CLAMP);
        mPaint.setShader(mLinearGradient);
        mGradientMatrix = new Matrix();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mGradientMatrix != null){
            mTranslate += mWidth * direct / 30;
            if (mTranslate > mWidth || mTranslate < 0){
                direct = -direct;
            }
            mGradientMatrix.setTranslate(mTranslate,0);
            mLinearGradient.setLocalMatrix(mGradientMatrix);
            postInvalidateDelayed(100);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        init();
    }

    public ShineTextView(Context context) {
        super(context);
    }

    public ShineTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ShineTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
