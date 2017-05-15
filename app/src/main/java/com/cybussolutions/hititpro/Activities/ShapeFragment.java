package com.cybussolutions.hititpro.Activities;

import android.app.Fragment;

/**
 * Created by Rizwan Butt on 14-Apr-17.
 */
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.cybussolutions.hititpro.R;

public class ShapeFragment extends Fragment {

    protected DrawingView mDrawingView;
    public static String inputtext  = "";


    public ShapeFragment(){
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       // View rootView = inflater.inflate(R.layout.fragment_main, container, false);
       // mDrawingView = (DrawingView) rootView.findViewById(R.id.drawingview);
        return null;
    }

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_line:
                mDrawingView.mCurrentShape = DrawingView.LINE;
                mDrawingView.reset();
                break;
            case R.id.action_smoothline:
                mDrawingView.mCurrentShape = DrawingView.SMOOTHLINE;
                mDrawingView.reset();
                break;
            case R.id.action_rectangle:
                mDrawingView.mCurrentShape = DrawingView.RECTANGLE;
                mDrawingView.reset();
                break;
            case R.id.action_square:
                mDrawingView.mCurrentShape = DrawingView.SQUARE;
                mDrawingView.reset();
                break;
            case R.id.action_circle:
                mDrawingView.mCurrentShape = DrawingView.CIRCLE;
                mDrawingView.reset();
                break;
            case R.id.action_triangle:
                mDrawingView.mCurrentShape = DrawingView.TRIANGLE;
                mDrawingView.reset();
                break;
            case R.id.action_arrow:
                mDrawingView.mCurrentShape=DrawingView.ARROW;
                mDrawingView.reset();
                break;
            case R.id.action_text:
                mDrawingView.mCurrentShape=DrawingView.TEXT;
                mDrawingView.reset();
                inputtext = "text";
                break;
        }

        return super.onOptionsItemSelected(item);
    }*/
}