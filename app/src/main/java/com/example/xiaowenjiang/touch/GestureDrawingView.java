package com.example.xiaowenjiang.touch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;


/**
 * Created by XiaowenJiang on 10/28/15.
 */
public class GestureDrawingView extends View {
    private ArrayList<Point> points;
    private ArrayList<ArrayList<Point>> strokes;
    private Paint paint;
    private Handler handler;
    private int pointID;

    public GestureDrawingView(Context context,Handler handler) {
        super(context);
        this.handler = handler;
        init();
    }

    public GestureDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GestureDrawingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init()
    {
        points = new ArrayList<>();
        strokes = new ArrayList<>();
        pointID=0;
        paint = createPaint(Color.BLACK,8);
    }

    private Paint createPaint(int color, float width){
        Paint temp = new Paint();
        temp.setStyle(Paint.Style.STROKE);
        temp.setAntiAlias(true);
        temp.setColor(color);
        temp.setStrokeWidth(width);
        temp.setStrokeCap(Paint.Cap.ROUND);

        return temp;
    }

    @Override
    public void onDraw(Canvas c){
        super.onDraw(c);
        this.setBackgroundColor(Color.WHITE);
        for(ArrayList<Point> obj:strokes)
        {
           drawStroke(obj,c);
        }
            drawStroke(points, c);
    }

    public static class Pos2{
        private float x;
        private float y;
        public Pos2(Pos2 another)
        {
            this.x = another.x;
            this.y = another.y;
        }
        public Pos2(float x,float y){
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
    public boolean onTouchEvent(MotionEvent event){
        //when touch is moving, store the trajectory and
        //draw it on paint
        if(event.getActionMasked() == MotionEvent.ACTION_MOVE){
            //get the pointerID
            pointID = event.getPointerId(0);
            points.add(new Point((int) event.getX(), (int) event.getY()));
            sendmsg(pointID,new Pos2(event.getX(),event.getY()));
            invalidate();
        }
        //when a line is finished, store the line into strokes and
        //create a new points array
        if(event.getActionMasked() == MotionEvent.ACTION_UP){
            this.strokes.add(points);
            points = new ArrayList<Point>();
            invalidate();
        }

        return true;
    }

    private void sendmsg(int pointID,Pos2 pos)
    {
        Message msg = new Message();
        msg.what=2;
        msg.arg1=pointID;
        msg.obj=pos;
        msg.setTarget(handler);
        msg.sendToTarget();
    }

    public void clear()
    {
        strokes = new ArrayList<>();
        sendmsg(pointID,null);
        invalidate();

    }

    private void drawStroke(ArrayList stroke, Canvas c){
        if (stroke.size() > 0) {
            Point p0 = (Point)stroke.get(0);
            for (int i = 1; i < stroke.size(); i++) {
                Point p1 = (Point)stroke.get(i);
                c.drawLine(p0.x, p0.y, p1.x, p1.y, paint);
                p0 = p1;
            }
        }
    }




}
