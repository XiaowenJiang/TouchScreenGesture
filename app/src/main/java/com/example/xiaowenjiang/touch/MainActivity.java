package com.example.xiaowenjiang.touch;

import android.app.ActionBar;
import android.graphics.Paint;
import android.graphics.PointF;
import android.media.Image;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.xiaowenjiang.touch.CircleDrawingView.Pos;
import com.example.xiaowenjiang.touch.GestureDrawingView.Pos2;

import android.util.Log;
import android.util.Size;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.math.BigDecimal;


public class MainActivity extends AppCompatActivity {
    private static final String TAG ="MainActivity";
    private Button gesture;
    private Button multifingers;
    private Button touchpoint;
    private ImageButton eraser;
    private TextView pointerID;
    private TextView otherpoints;
    private TextView pointIDtxt;
    private TextView pointPostxt;

    public static Handler handlepos;

    private GestureDrawingView gestureDrawingView;
    private CircleDrawingView circleDrawingView;
    private MultiTouchView multiTouchView;

    //round the float to two decimals
    private float digits2(float x){
        BigDecimal b = new BigDecimal(x);
        return b.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final RelativeLayout lv = (RelativeLayout)findViewById(R.id.mainlayout);
        final TextView positiontext = (TextView) findViewById(R.id.touchposition);
        pointerID = (TextView)findViewById(R.id.pointerid);
        otherpoints = (TextView)findViewById(R.id.otherpoints);
        pointIDtxt = (TextView)findViewById(R.id.pointerid_txt);
        pointPostxt = (TextView)findViewById(R.id.touchposition_txt);
        eraser = (ImageButton)findViewById(R.id.erase);
        handlepos = new Handler(){
            public void handleMessage(Message msg){
                if(msg.what ==1)
                {
                    pointerID.setText(msg.arg1+"");
                    Pos position =(Pos)msg.obj;
                    positiontext.setText("x = "+digits2(position.getX())+", y = "+digits2(position.getY()));
                }
                else if(msg.what ==2)
                {
                    pointerID.setText(msg.arg1+"");
                    //the paint is cleared, position part set blank
                    if(msg.obj==null)
                    {
                        positiontext.setText("");
                    }
                    else {
                        Pos2 position = (Pos2) msg.obj;
                        positiontext.setText("x = " + digits2(position.getX()) + ", y = " + digits2(position.getY()));
                    }
                }
                else if(msg.what == 3)
                {
                    SparseArray<PointF> points= (SparseArray<PointF>)msg.obj;
                    String result="";
                    if(points.size()!=0) {
                        for (int size = points.size(), i = 0; i < size; i++) {
                            PointF point = points.valueAt(i);
                            if (point != null) {
                                result += "PointerID: " + i + "\n" + "Position: " +
                                        "x = " + point.x + ", y = " + point.y + "\n";
                                otherpoints.setText(result);
                            }
                        }
                    }
                    else{
                        otherpoints.setText("");
                    }
                }
            }
        };
        circleDrawingView = new CircleDrawingView(this.getApplicationContext(),handlepos);
        //use relative layout to make sure the buttons can be touched
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = 170;
        lv.addView(circleDrawingView, params);
        setContentView(lv);
        gesture = (Button)findViewById(R.id.drawgesture);
        gesture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout lv = (RelativeLayout)findViewById(R.id.mainlayout);
                Log.d(TAG,"gesture");
                if(circleDrawingView!=null) {
                    lv.removeView(circleDrawingView);
                }
                if(multiTouchView!=null)
                {
                    lv.removeView(multiTouchView);
                }
                gestureDrawingView = new GestureDrawingView(getApplicationContext(),handlepos);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.topMargin = 430;
                lv.addView(gestureDrawingView, params);
                positiontext.setText("");
                pointIDtxt.setVisibility(View.VISIBLE);
                pointPostxt.setVisibility(View.VISIBLE);
                eraser.setVisibility(View.VISIBLE);
                setContentView(lv);
                eraser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gestureDrawingView.clear();
                    }
                });
            }
        });
        touchpoint = (Button)findViewById(R.id.touchpoint);
        touchpoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout lv = (RelativeLayout)findViewById(R.id.mainlayout);
                Log.d(TAG,"circle");
                if(gestureDrawingView!=null)
                {
                    lv.removeView(gestureDrawingView);
                }
                if(multiTouchView!=null)
                {
                    lv.removeView(multiTouchView);
                }
                circleDrawingView = new CircleDrawingView(getApplicationContext(),handlepos);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.topMargin = 170;
                lv.addView(circleDrawingView, params);
                setContentView(lv);
                positiontext.setText("");
                pointIDtxt.setVisibility(View.VISIBLE);
                pointPostxt.setVisibility(View.VISIBLE);
                eraser.setVisibility(View.INVISIBLE);
            }
        });
        multifingers = (Button)findViewById(R.id.multipoints);
        multifingers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout lv = (RelativeLayout)findViewById(R.id.mainlayout);
                Log.d(TAG,"multi");
                if(gestureDrawingView!=null)
                {
                    lv.removeView(gestureDrawingView);
                }
                if(circleDrawingView!=null)
                {
                    lv.removeView(circleDrawingView);
                }
                multiTouchView = new MultiTouchView(getApplicationContext(),handlepos);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.topMargin = 170;
                lv.addView(multiTouchView, params);
                setContentView(lv);
                positiontext.setText("");
                pointerID.setText("");
                pointIDtxt.setVisibility(View.INVISIBLE);
                pointPostxt.setVisibility(View.INVISIBLE);
                eraser.setVisibility(View.INVISIBLE);

            }
        });
    }


}
