package com.wuruoye.linking2.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.support.v4.app.ActivityCompat;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.wuruoye.linking2.R;
import com.wuruoye.linking2.utils.GraphUtil;
import com.wuruoye.linking2.utils.RandomUtil;

import java.util.List;

/**
 * Created by wuruoye on 2017/5/13.
 * this file is to do
 */
public class LinkingView extends SurfaceView implements SurfaceHolder.Callback {
    private float mWidth;
    private float mHeight;
    private float aLength;
    private SurfaceHolder mHolder;
    private Canvas mCanvas;
    private Paint mPaint;
    private int background;
    private int[] colors;
    private int flag;

    private int[][] num;
    private int[] start;
    private int[] end = new int[2];
    private int xNum;
    private int yNum;
    private int stepSize;
    private boolean isDrawing = false;
    private List<Integer> mPath;
    private PathMeasure mPathMeasure;
    private ValueAnimator valueAnimator;

    private OnLinkGameListener linkGameListener;

    public void resetLink(int x, int y,int stepSize){
        this.stepSize = stepSize;
        getRandomNum(x,y,stepSize);
        mCanvas = mHolder.lockCanvas();
        drawBox();
        mHolder.unlockCanvasAndPost(mCanvas);
    }

    public void refresh(){
        num = RandomUtil.getNum(num, stepSize);
        mCanvas = mHolder.lockCanvas();
        drawBox();
        mHolder.unlockCanvasAndPost(mCanvas);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        aLength = mWidth / xNum;
        mCanvas = mHolder.lockCanvas();
        drawBox();
        mHolder.unlockCanvasAndPost(mCanvas);
    }

    private void initAll() {
        mHolder = getHolder();
        mHolder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);

        initColors();
        initAnimator();

        mPaint = new Paint();
        mPaint.setStrokeWidth(10);

        getRandomNum(xNum,yNum,stepSize);
        background = ActivityCompat.getColor(getContext(), R.color.white_smoke);
    }

    private void initAnimator() {
        valueAnimator = ValueAnimator.ofFloat(0,1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                try {
                    drawAnimator((float)animation.getAnimatedValue());
                } catch (Exception ignored) {

                }
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                try {
                    onAnimatorEnd();
                } catch (Exception ignored) {

                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void initColors() {
        colors = new int[]{
                ActivityCompat.getColor(getContext(),R.color.pale_leaf),
                ActivityCompat.getColor(getContext(),R.color.chinook),
                ActivityCompat.getColor(getContext(),R.color.perfume),
                ActivityCompat.getColor(getContext(),R.color.brilliant_lavender),
                ActivityCompat.getColor(getContext(),R.color.bubble_gum),
                ActivityCompat.getColor(getContext(),R.color.monte_carlo),
                ActivityCompat.getColor(getContext(),R.color.jelly_bean),
                ActivityCompat.getColor(getContext(),R.color.magnesium),
                ActivityCompat.getColor(getContext(),R.color.indigo),
                ActivityCompat.getColor(getContext(),R.color.colorAccent)
        };
    }

    private void drawBox(){
        mCanvas.drawColor(background);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(40);
        int startX;
        int startY;
        for (int i = 0; i < num.length; i ++){
            startY = (int) (aLength * i);
            for (int j = 0; j < num[i].length; j ++){
                startX = (int) (aLength * j);
                if (num[i][j] == 0){
                    mPaint.setColor(background);
                }else {
                    mPaint.setColor(colors[num[i][j] - 1]);
                }
                RectF rectF = new RectF(startY + 10,startX + 10, startY + aLength - 10,startX + aLength - 10);
                mCanvas.drawRoundRect(rectF,10,10,mPaint);
            }
        }
    }

    private void drawLine(){
        Path path = new Path();
        path.moveTo(start[0] * aLength + aLength / 2, start[1] * aLength + aLength / 2);
        for (int a : mPath){
            switch (a){
                case GraphUtil.UP:{
                    path.rLineTo(0,-aLength);
                }break;
                case GraphUtil.DOWN:{
                    path.rLineTo(0,aLength);
                }break;
                case GraphUtil.LEFT:{
                    path.rLineTo(-aLength,0);
                }break;
                case GraphUtil.RIGHT:{
                    path.rLineTo(aLength,0);
                }break;
            }
        }
        mPathMeasure = new PathMeasure(path,false);
        flag = 1;
        valueAnimator.setDuration((long) mPathMeasure.getLength());
        valueAnimator.start();
    }

    private void drawFrame(int x, int y, boolean isUse){
        if (isUse) {
            mPaint.setColor(colors[num[x][y] - 1]);
        }else {
            mPaint.setColor(background);
        }
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.STROKE);
        float xR = x * aLength;
        float yR = y * aLength;
        Path path = new Path();
        path.moveTo(xR,yR);
        path.rLineTo(aLength,0);
        path.rLineTo(0,aLength);
        path.rLineTo(-aLength,0);
        path.rLineTo(0,-aLength);
        mCanvas.drawPath(path,mPaint);
    }

    private void drawAnimator(float progress){
        if (progress == 1){
            isDrawing = false;
        }
        mCanvas = mHolder.lockCanvas();
        switch (flag){
            case 1:{
                Path path = new Path();
                mPathMeasure.getSegment(0,mPathMeasure.getLength()*progress,path,true);
                drawBox();
                mPaint.setStrokeWidth(aLength/20);
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setColor(colors[num[start[0]][start[1]] - 1]);
                mCanvas.drawPath(path,mPaint);
            }break;
            case 2:{

            }break;
        }
        mHolder.unlockCanvasAndPost(mCanvas);
    }

    private void onAnimatorEnd(){
        toast("animator end");
        num[start[0]][start[1]] = 0;
        num[end[0]][end[1]] = 0;
        start = null;
        mCanvas = mHolder.lockCanvas();
        drawBox();
        mHolder.unlockCanvasAndPost(mCanvas);
        isDrawing = false;

        linkGameListener.onLinked();
        if (isGameOver()){
            linkGameListener.onGameOver();
        }
    }

    private void onClick(int xNum, int yNum){
        if (xNum < num.length && yNum < num[0].length && num[xNum][yNum] != 0){
            isDrawing = true;
            if (start == null){
                toast("first click");
                start = new int[2];
                start[0] = xNum;
                start[1] = yNum;

                mCanvas = mHolder.lockCanvas();
                drawBox();
                drawFrame(xNum,yNum,true);
                mHolder.unlockCanvasAndPost(mCanvas);

                isDrawing = false;
            }else{
                if (isLink(num,start,new int[]{xNum,yNum})){
                    end[0] = xNum;
                    end[1] = yNum;
                    drawLine();
                }else {
                    mCanvas = mHolder.lockCanvas();
                    drawBox();
                    drawFrame(start[0],start[1],false);
                    mHolder.unlockCanvasAndPost(mCanvas);
                    start = null;
                    isDrawing = false;
                }
            }
        }
    }

    private void clear(Canvas canvas){
        mPaint.setColor(background);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawRect(0,0,mWidth,mHeight,mPaint);
    }

    private void onTouch(float x, float y){
        int xT = (int) (x / aLength);
        int yT = (int) (y / aLength);
        if (!isDrawing) {
            onClick(xT,yT);
        }
    }

    private boolean isGameOver(){
        int count = 0;
        for (int[] a : num){
            for (int b : a){
                if (b != 0){
                    count ++;
                }
            }
        }
        return count == 0;
    }

    private boolean isLink(int[][] num, int[] startP, int[] endP){
        mPath = null;
        try {
            mPath = GraphUtil.getPath(num,startP,endP);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mPath != null;
    }

    private void getRandomNum(int x, int y, int stepSize){
        num = RandomUtil.getNum(x,y,stepSize);
    }

    public interface OnLinkGameListener {
        void onGameOver();
        void onLinked();
    }

    public LinkingView(Context context, OnLinkGameListener linkGameListener, int x, int y, int stepSize) {
        super(context);
        this.linkGameListener = linkGameListener;
        xNum = x;
        yNum = y;
        this.stepSize = stepSize;
        initAll();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        onTouch(event.getX(),event.getY());
        return super.onTouchEvent(event);
    }

    private void toast(String message){
        boolean is = false;
        if (is){
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}
