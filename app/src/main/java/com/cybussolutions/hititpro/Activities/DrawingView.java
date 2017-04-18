package com.cybussolutions.hititpro.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;


public class DrawingView extends ImageView {
    MainActivity ma=new MainActivity();
    String message = "No key pressed yet.";

    public static final int LINE = 1;
    public static final int RECTANGLE = 3;
    public static final int SQUARE = 4;
    public static final int CIRCLE = 5;
    public static final int TRIANGLE = 6;
    public static final int SMOOTHLINE = 2;
    public static final int ARROW=7;
    public static final int TEXT=8;
    public static String textInput="";

    public static final float TOUCH_TOLERANCE = 4;
    public static final float TOUCH_STROKE_WIDTH = 5;

    public int mCurrentShape;

    protected Path mPath;
    protected Paint mPaint;
    protected Paint mPaintFinal;
    protected Bitmap mBitmap;
    protected Canvas mCanvas;

    String s;

    /**
     * Indicates if you are drawing
     */
    protected boolean isDrawing = false;

    /**
     * Indicates if the drawing is ended
     */
    protected boolean isDrawingEnded = false;


    protected float mStartX;
    protected float mStartY;

    protected float mx;
    protected float my;
    private PointF startPoint;
    private PointF endPoint;
    private int xStored;
    private int yStored;
    Activity activity;
    private Rect[] rectangles;
    private String mText;
    private int keyCode;
    private EditText et;
    String test;

    public DrawingView(Activity context) {
        super(context);
        activity =context;
        init();
    }

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, 0, 0, mPaint);

        if (isDrawing){
            switch (mCurrentShape) {
                case LINE:
                    onDrawLine(canvas);
                    break;
                case RECTANGLE:
                    onDrawRectangle(canvas);
                    break;
                case SQUARE:
                    onDrawSquare(canvas);
                    break;
                case CIRCLE:
                    onDrawCircle(canvas);
                    break;
                case TRIANGLE:
                    onDrawTriangle(canvas);
                    break;
                case ARROW:
                    onDrawArrow(canvas);
                    break;
                case TEXT:
                    onDrawText(canvas);


            }
        }
    }


    protected void init() {
        mPath = new Path();
        mPaint = new Paint(Paint.DITHER_FLAG);
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(48.0f);
        mPaint.setDither(true);
        mPaint.setColor(getContext().getResources().getColor(android.R.color.holo_red_dark));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(TOUCH_STROKE_WIDTH);


        mPaintFinal = new Paint(Paint.DITHER_FLAG);
        mPaintFinal.setAntiAlias(true);
        mPaintFinal.setDither(true);
        mPaintFinal.setColor(getContext().getResources().getColor(android.R.color.holo_red_dark));
        mPaintFinal.setStyle(Paint.Style.STROKE);
        mPaintFinal.setStrokeJoin(Paint.Join.ROUND);
        mPaintFinal.setStrokeCap(Paint.Cap.ROUND);
        mPaintFinal.setStrokeWidth(TOUCH_STROKE_WIDTH);
    }

    protected void reset() {
        mPath = new Path();
        countTouch=0;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mx = event.getX();
        my = event.getY();
        switch (mCurrentShape) {
            case LINE:
                onTouchEventLine(event);
                break;
            case SMOOTHLINE:
                onTouchEventSmoothLine(event);
                break;
            case RECTANGLE:
                onTouchEventRectangle(event);
                break;
            case SQUARE:
                onTouchEventSquare(event);
                break;
            case CIRCLE:
                onTouchEventCircle(event);
                break;
            case TRIANGLE:
                onTouchEventTriangle(event);
                break;
            case ARROW:
                onTouchEventArrow(event);
                break;
            case TEXT:
                textInput = "text";
                
             //  onTouchEventText(event);
        }
        return true;
    }
    private void onDrawArrow(Canvas canvas) {
        float dx = Math.abs(mx - mStartX);
        float dy = Math.abs(my - mStartY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            //canvas.drawLine(mStartX, mStartY, mx, my, mPaint);
        }

           // canvas.drawText("test",mx,my,mPaint);
    }
    private void onDrawText(Canvas canvas) {
        float dx = Math.abs(mx - mStartX);
        float dy = Math.abs(my - mStartY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            //canvas.drawLine(mStartX, mStartY, mx, my, mPaint);
        }

        // canvas.drawText("test",mx,my,mPaint);
    }
    public void onTouchEventText( MotionEvent event,float x ,float y ,String text) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDrawing = true;
//                mStartX = x;
//                mStartY = y;

                Canvas canvas = new Canvas();

                try {
                    SharedPreferences sp=getContext().getSharedPreferences("prefs", 1);
                   // Toast.makeText(getContext(),sp.getString("text",""),Toast.LENGTH_LONG).show();
                   // et=(EditText)findViewById(R.id.txt1);
                  // et.getText();
                  //  mCanvas.drawText(sp.getString("text",""), mStartX, mStartY, mPaint);
                }catch (Exception e){
                    Toast.makeText(getContext(),e.toString(),Toast.LENGTH_LONG).show();
                }


//                ((InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE)
//                ).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

                break;
            case MotionEvent.ACTION_MOVE:
                invalidate();
                break;


            case MotionEvent.ACTION_UP:
                isDrawing = false;

                mBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
                mCanvas = new Canvas(mBitmap);
                mCanvas.drawText(text, x, y, mPaint);

                invalidate();
                break;
        }
        }

    //------------------------------------------------------------------
    // Line
    //------------------------------------------------------------------

    private void onDrawLine(Canvas canvas) {

        float dx = Math.abs(mx - mStartX);
        float dy = Math.abs(my - mStartY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            canvas.drawLine(mStartX, mStartY, mx, my, mPaint);
        }
    }
    private void onTouchEventArrow(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPath.reset();
                mPath.moveTo(mx, my);
                mStartX = mx;
                mStartY = my;
                startPoint = new PointF(event.getX(), event.getY());
                endPoint = new PointF();
                invalidate();

                //   isDrawing = true;
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(mx - mStartX);
                System.out.println("action move");
                float dy = Math.abs(my - mStartY);
                if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE)
                {
                    //  currentDrawingPath.path.quadTo(mX,mY,(x + mX)/2, (y + mY)/2);
                }
                mStartX = mx;
                mStartY = my;
                endPoint.x = event.getX();
                endPoint.y = event.getY();
                isDrawing = true;

                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                mPath.lineTo(mStartX, mStartY);
                float deltaX =   endPoint.x-startPoint.x;
                float deltaY =   endPoint.y-startPoint.y;
                float frac = (float) 0.1;

                float point_x_1 = startPoint.x + (float) ((1 - frac) * deltaX + frac * deltaY);
                float point_y_1 = startPoint.y + (float) ((1 - frac) * deltaY - frac * deltaX);

                float point_x_2 = endPoint.x;
                float point_y_2 = endPoint.y;

                float point_x_3 = startPoint.x + (float) ((1 - frac) * deltaX - frac * deltaY);
                float point_y_3 = startPoint.y + (float) ((1 - frac) * deltaY + frac * deltaX);



                mPath.moveTo(point_x_1, point_y_1);
                mPath.lineTo(point_x_2, point_y_2);
                mPath.lineTo(point_x_3, point_y_3);
                mPath.lineTo(point_x_1, point_y_1);
                mPath.lineTo(point_x_1, point_y_1);

                mCanvas.drawPath(mPath, mPaint);
                endPoint.x = event.getX();
                endPoint.y = event.getY();
                isDrawing = false;

                invalidate();
                break;
            default:
                break;
//
        }


       /* switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDrawing = true;
                mStartX = mx;
                mStartY = my;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                isDrawing = false;
                mCanvas.drawText("test", mx, my, mPaint);
                invalidate();
                break;
        }*/
    }
//    private  static Path makeArrow(float length, float height) {
//
//        Path p = new Path();
//        p.moveTo(length , height / 2);
//        p.lineTo(length / 2, height );
//        p.lineTo(length / 2, height* 3 / 4);
//        p.lineTo(0, height* 3 / 4);
//        p.lineTo(0, height/ 4);
//        p.lineTo(length / 2, height/ 4);
//        p.lineTo(length / 2,0);
//        p.lineTo(length , height / 2);
//        p.close();
//        return p;
//    }
    private void onTouchEventLine(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDrawing = true;
                mStartX = mx;
                mStartY = my;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                isDrawing = false;
                mCanvas.drawLine(mStartX, mStartY, mx, my, mPaintFinal);
                invalidate();
                break;
        }
    }

    //------------------------------------------------------------------
    // Smooth Line
    //------------------------------------------------------------------


    private void onTouchEventSmoothLine(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDrawing = true;
                mStartX = mx;
                mStartY = my;

                mPath.reset();
                mPath.moveTo(mx, my);

                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:

                float dx = Math.abs(mx - mStartX);
                float dy = Math.abs(my - mStartY);
                if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                    mPath.quadTo(mStartX, mStartY, (mx + mStartX) / 2, (my + mStartY) / 2);
                    mStartX = mx;
                    mStartY = my;
                }
                mCanvas.drawPath(mPath, mPaint);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                isDrawing = false;
                mPath.lineTo(mStartX, mStartY);
                mCanvas.drawPath(mPath, mPaintFinal);
                mPath.reset();
                invalidate();
                break;
        }
    }

    //------------------------------------------------------------------
    // Triangle
    //------------------------------------------------------------------

    int countTouch =0;
    float basexTriangle =0;
    float baseyTriangle =0;

    private void onDrawTriangle(Canvas canvas){

        if (countTouch<3){
            canvas.drawLine(mStartX,mStartY,mx,my,mPaint);
        }else if (countTouch==3){
            canvas.drawLine(mx,my,mStartX,mStartY,mPaint);
            canvas.drawLine(mx,my,basexTriangle,baseyTriangle,mPaint);
        }
    }

    private void onTouchEventTriangle(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                countTouch++;
                if (countTouch==1){
                    isDrawing = true;
                    mStartX = mx;
                    mStartY = my;
                } else if (countTouch==3){
                    isDrawing = true;
                }
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                countTouch++;
                isDrawing = false;
                if (countTouch<3){
                    basexTriangle=mx;
                    baseyTriangle=my;
                    mCanvas.drawLine(mStartX,mStartY,mx,my,mPaintFinal);
                } else if (countTouch>=3){
                    mCanvas.drawLine(mx,my,mStartX,mStartY,mPaintFinal);
                    mCanvas.drawLine(mx,my,basexTriangle,baseyTriangle,mPaintFinal);
                    countTouch =0;
                }
                invalidate();
                break;
        }
    }

    //------------------------------------------------------------------
    // Circle
    //------------------------------------------------------------------

    private void onDrawCircle(Canvas canvas){
        canvas.drawCircle(mStartX, mStartY, calculateRadius(mStartX, mStartY, mx, my), mPaint);
    }

    private void onTouchEventCircle(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDrawing = true;
                mStartX = mx;
                mStartY = my;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                isDrawing = false;
                mCanvas.drawCircle(mStartX, mStartY, calculateRadius(mStartX,mStartY,mx,my), mPaintFinal);
                invalidate();
                break;
        }
    }

    /**
     *
     * @return
     */
    protected float calculateRadius(float x1, float y1, float x2, float y2) {

        return (float) Math.sqrt(
                Math.pow(x1 - x2, 2) +
                        Math.pow(y1 - y2, 2)
        );
    }

    //------------------------------------------------------------------
    // Rectangle
    //------------------------------------------------------------------

    private void onDrawRectangle(Canvas canvas) {
        drawRectangle(canvas,mPaint);
    }

    private void onTouchEventRectangle(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDrawing = true;
                mStartX = mx;
                mStartY = my;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                isDrawing = false;
                drawRectangle(mCanvas,mPaintFinal);
                invalidate();
                break;
        }
        ;
    }

    private void drawRectangle(Canvas canvas,Paint paint){
        float right = mStartX > mx ? mStartX : mx;
        float left = mStartX > mx ? mx : mStartX;
        float bottom = mStartY > my ? mStartY : my;
        float top = mStartY > my ? my : mStartY;
        canvas.drawRect(left, top , right, bottom, paint);
    }

    //------------------------------------------------------------------
    // Square
    //------------------------------------------------------------------

    private void onDrawSquare(Canvas canvas) {
        onDrawRectangle(canvas);
    }

    private void onTouchEventSquare(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDrawing = true;
                mStartX = mx;
                mStartY = my;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                adjustSquare(mx, my);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                isDrawing = false;
                adjustSquare(mx, my);
                drawRectangle(mCanvas,mPaintFinal);
                invalidate();
                break;
        }
    }

    /**
     * Adjusts current coordinates to build a square
     * @param x
     * @param y
     */
    protected void adjustSquare(float x, float y) {
        float deltaX = Math.abs(mStartX - x);
        float deltaY = Math.abs(mStartY - y);

        float max = Math.max(deltaX, deltaY);

        mx = mStartX - x < 0 ? mStartX + max : mStartX - max;
        my = mStartY - y < 0 ? mStartY + max : mStartY - max;
    }


}