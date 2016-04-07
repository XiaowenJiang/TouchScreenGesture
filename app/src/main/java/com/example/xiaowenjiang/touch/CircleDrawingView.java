package com.example.xiaowenjiang.touch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


/**
 * Created by XiaowenJiang on 10/27/15.
 */
public class CircleDrawingView extends View {
    private static final String TAG = "CircleDrawingView";
    Context context;
    private Paint circle;
    private CircleArea circlearea;
    private float circlesize;
    private Handler handler;
    private float startx,starty,endx,endy;

    //constructors
    public CircleDrawingView(Context context,Handler handler) {
        super(context);
        this.context = context;
        this.handler = handler;
        init();
    }

    public CircleDrawingView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }
    public CircleDrawingView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context,attrs,defStyle);
        init();
    }

    private static class CircleArea{
        int radius;
        float centerX;
        float centerY;

        //constructor, assign circle position and size
        CircleArea(int radius,float centerX, float centerY)
        {
            this.radius = radius;
            this.centerX = centerX;
            this.centerY = centerY;
        }
    }



    private void init() {
        circle = new Paint(Paint.ANTI_ALIAS_FLAG);
        circle.setColor(Color.BLUE);
        circle.setStyle(Paint.Style.FILL);
        if (circlesize == 0) {
            circlesize = circle.getTextSize();
        } else {
            circle.setTextSize(circlesize);
        }
    }

    @Override
    public void onDraw(final Canvas canv) {
        // background bitmap to cover all area
        //canv.drawBitmap(mBitmap, null, mMeasuredRect, null);
        if(circlearea!=null)
        canv.drawCircle(circlearea.centerX, circlearea.centerY, circlearea.radius, circle);
    }

    public static class Pos{
        private float x;
        private float y;
        public Pos(Pos another)
        {
            this.x = another.x;
            this.y = another.y;
        }
        public Pos(float x,float y){
            this.x = x;
            this.y = y;
        }
        public float getX()
        {
            return this.x;
        }
        public float getY()
        {
            return this.y;
        }
    }



    @Override
    public boolean onTouchEvent(MotionEvent e) {

        float x = e.getX();
        float y = e.getY();
        Message msg = new Message();
        msg.what = 1;
        msg.arg1 = e.getPointerId(0);
        msg.obj = new Pos(x,y);
        msg.setTarget(handler);
        msg.sendToTarget();
        int radius = 150;
        circlearea = new CircleArea(radius,x,y);
        //indicate that the view should be redrawn
        postInvalidate();
        if(e.getActionMasked() == MotionEvent.ACTION_DOWN)
        {
            startx = x;
            starty = y;
        }
        if(e.getActionMasked() == MotionEvent.ACTION_UP)
        {
            endx = x;
            endy = y;
            float x_distance = endx - startx;
            float y_distance = endy - starty;
            Log.d(TAG,"horizontal dist: "+x_distance);
            Log.d(TAG,"vertical_dist: "+y_distance);
        }
        return true;
    }
}