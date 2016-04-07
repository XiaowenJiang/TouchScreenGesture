package com.example.xiaowenjiang.touch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by XiaowenJiang on 10/28/15.
 */
public class MultiTouchView extends View {
    private static final String TAG = "MultiTouchView";
    private SparseArray<PointF> ActivePointers;
    private Handler handler;
    private Paint paint;
    private float paintsize;

    private int[] colors = { Color.BLUE, Color.GREEN, Color.MAGENTA,
            Color.BLACK, Color.CYAN, Color.GRAY, Color.RED, Color.DKGRAY,
            Color.LTGRAY, Color.YELLOW };

    public MultiTouchView(Context context,Handler handler) {
        super(context);
        this.handler = handler;
        init();
    }

    public MultiTouchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MultiTouchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        ActivePointers = new SparseArray<>();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);

        if (paintsize == 0) {
            paintsize = paint.getTextSize();
        } else {
            paint.setTextSize(paintsize);
        }

    }

    private void sendmsg(SparseArray<PointF> pointers)
    {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = pointers;
        msg.setTarget(handler);
        msg.sendToTarget();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        int pointerIndex = event.getActionIndex();
        int pointerID = event.getPointerId(pointerIndex);
        int maskedaction = event.getActionMasked();

        switch (maskedaction)
        {
            case MotionEvent.ACTION_DOWN:{
                PointF point = new PointF(event.getX(pointerIndex),event.getY(pointerIndex));
                ActivePointers.put(pointerID, point);
                break;
            }
                //when a new touch is pressed, add new point
            case MotionEvent.ACTION_POINTER_DOWN:{
                Log.d(TAG,"action down");

                PointF point = new PointF(event.getX(pointerIndex),event.getY(pointerIndex));
                if(ActivePointers.size()<5) {
                    ActivePointers.put(pointerID, point);
                }
                break;
            }
            //a pointer was moved
            case MotionEvent.ACTION_MOVE:{
                for (int size = event.getPointerCount(), i = 0; i < size; i++) {
                    PointF point = ActivePointers.get(event.getPointerId(i));
                    if (point != null) {
                        point.x = event.getX(i);
                        point.y = event.getY(i);
                    }
                }
                break;
            }
            case MotionEvent.ACTION_POINTER_UP:{
                ActivePointers.remove(pointerID);
                break;
            }
            case MotionEvent.ACTION_UP:{
                ActivePointers.remove(pointerID);
                break;
            }

        }

        invalidate();
        sendmsg(ActivePointers);
        return true;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //this.setBackgroundColor(Color.LTGRAY);
        // draw all pointers
        for (int size = ActivePointers.size(), i = 0; i < size; i++) {
            PointF point = ActivePointers.valueAt(i);
            if (point != null)
                paint.setColor(colors[i % 9]);
            canvas.drawCircle(point.x, point.y, 100, paint);
        }
    }

}
