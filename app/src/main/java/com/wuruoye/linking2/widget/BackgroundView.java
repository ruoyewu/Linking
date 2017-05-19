package com.wuruoye.linking2.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.support.v4.app.ActivityCompat;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.wuruoye.linking2.R;
import com.wuruoye.linking2.model.Box;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuruoye on 2017/5/12.
 * this file is to do
 */

public class BackgroundView extends SurfaceView implements SurfaceHolder.Callback, Runnable{
    private SurfaceHolder mHolder;
    private Canvas mCanvas;
    private Paint mPaint;
    private boolean isDraw;
    private Bitmap backBitmap;

    private int width;
    private int height;
    private int aLength;

    private int flag;
    private Path[] mPaths;
    private List<Box[]> mBoxes;
    private float progress;
    private PathMeasure pathMeasure;

    public BackgroundView(Context context) {
        super(context);
        init();
    }

    private void init(){
        mHolder = getHolder();
        mHolder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);

    }

    private void draw(){
        mCanvas.drawColor(ActivityCompat.getColor(getContext(),R.color.white_smoke));
        mCanvas.drawBitmap(backBitmap,(width - backBitmap.getWidth()) / 2,aLength * 5,mPaint);
        if (isDraw) {
            if (flag > 0) {
                drawLink();
            }else {
                clear(mCanvas);
                mCanvas.drawBitmap(backBitmap,(width - backBitmap.getWidth()) / 2,aLength * 5,mPaint);
                drawBoxCrop();
            }
        }
    }

    private void drawLink(){
        mCanvas.translate(0,0);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        for (Box b : mBoxes.get(flag - 1)){
            RectF rectF = new RectF(b.startX, b.startY,b.startX + b.width, b.startY + b.height);
            mPaint.setColor(b.color);
            mCanvas.drawRoundRect(rectF,10,10,mPaint);
        }
        mPaint.setStyle(Paint.Style.STROKE);
        Path p = new Path();
        pathMeasure.getSegment(0,pathMeasure.getLength() * progress,p,true);
        mCanvas.drawPath(p,mPaint);
    }

    private void drawBoxCrop(){
        float crop = 1 - progress;
        for (int i = 0; i < mBoxes.get(-flag-1).length; i ++){
            if (i == 1 && flag == -3){
                continue;
            }
            Box b = mBoxes.get(-flag-1)[i];
            mCanvas.save();
            mCanvas.translate(b.startX + aLength/2,b.startY + aLength/2);
            mPaint.setColor(b.color);
            mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            float c = aLength/2*crop;
            RectF r = new RectF(-c,-c,c,c);
            mCanvas.drawRoundRect(r,10,10,mPaint);
            mCanvas.restore();
        }
    }

    private void changeFlag(){
        clear(mCanvas);
        if (flag > 0){
            flag = -flag;
        }else if (flag < 0){
            flag = -flag + 1;
            if (flag == 4){
                flag = 1;
            }
        }
        setMeasure();
    }

    private void setMeasure(){
        if (flag > 0) {
            pathMeasure = new PathMeasure(mPaths[flag - 1],false);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isDraw = true;
        intData();
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (isDraw) {
            try {
                Thread.sleep(10);
                mCanvas = mHolder.lockCanvas();
                if (progress >= 1){
                    progress = 0;
                    changeFlag();
                }else {
                    if (flag > 0) {
                        progress += 0.01;
                    }else if (flag < 0){
                        progress += 0.05;
                    }
                }
                draw();
            } catch (Exception ignored) {
            } finally {
                if (isDraw) {
                    mHolder.unlockCanvasAndPost(mCanvas);
                }
            }
        }
    }

    private void intData() {
        backBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.linking);
        int width = backBitmap.getWidth();
        int height = backBitmap.getHeight();
        float scale = width / height;
        int w = width / 5 * 4;
        int h = (int) (w / scale);
        backBitmap = Bitmap.createScaledBitmap(backBitmap,w,h,true);

        mPaint = new Paint();
        mPaint.setStrokeWidth(5);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mPaths = new Path[3];
        mBoxes = new ArrayList<>();
        initLink_1();
        initLink_2();
        initLink_3();

        pathMeasure = new PathMeasure(mPaths[0],false);
        flag = 1;
        progress = 0;
    }
    private void initLink_1() {
        int startX = (width - 5 * aLength) / 2;
        int startY = height / 7 + aLength;
        int color = ActivityCompat.getColor(getContext(),R.color.indigo);

        Path path1 = new Path();
        path1.moveTo(startX,startY + aLength / 2);
        path1.rLineTo(aLength * 5,0);

        Box[] boxes = new Box[2];
        Box box1 = new Box(aLength,aLength,startX,startY,color);
        Box box2 = new Box(aLength,aLength,startX + aLength * 4, startY, color);
        boxes[0] = box1;
        boxes[1] = box2;

        mPaths[0] = path1;
        mBoxes.add(boxes);
    }
    private void initLink_2(){
        int startX = (width - 5 * aLength) / 2;
        int startY = height / 7;
        int color1 = ActivityCompat.getColor(getContext(),R.color.bubble_gum);

        Path path = new Path();
        path.moveTo(startX,startY + aLength / 2);
        path.rLineTo(aLength * 4 + aLength / 2, 0);
        path.rLineTo(0,aLength);

        Box[] boxes2 = new Box[2];
        Box box1 = new Box(aLength,aLength,startX,startY,color1);
        Box box2 = new Box(aLength,aLength,startX + aLength * 4,startY + aLength,color1);
        boxes2[0] = box1;
        boxes2[1] = box2;

        mPaths[1] = path;
        mBoxes.add(boxes2);
    }
    private void initLink_3(){
        int startX = (width - 5 * aLength) / 2;
        int startY = height / 7 + aLength;
        int color1 = ActivityCompat.getColor(getContext(),R.color.perfume);
        int color2 = ActivityCompat.getColor(getContext(),R.color.chinook);

        Path path = new Path();
        path.moveTo(startX + aLength / 2,startY + aLength / 2);
        path.rLineTo(0,-aLength);
        path.rLineTo(aLength * 4,0);
        path.rLineTo(0,aLength);

        Box[] boxes = new Box[3];
        Box box1 = new Box(aLength,aLength,startX,startY,color1);
        Box box2 = new Box(aLength,aLength,startX + 2 * aLength,startY,color2);
        Box box3 = new Box(aLength,aLength,startX + 4 * aLength,startY,color1);
        boxes[0] = box1;
        boxes[1] = box2;
        boxes[2] = box3;

        mPaths[2] = path;
        mBoxes.add(boxes);
    }

    private void clear(Canvas canvas){
        mPaint.setColor(ActivityCompat.getColor(getContext(),R.color.white_smoke));
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawRect(0,0,width,height,mPaint);
    }

    private void onTouch(float x, float y){

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
        aLength = 100;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isDraw = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        onTouch(event.getX(),event.getY());
        return super.onTouchEvent(event);
    }

}
