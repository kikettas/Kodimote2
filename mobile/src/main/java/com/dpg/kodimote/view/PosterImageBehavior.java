package com.dpg.kodimote.view;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by enriquedelpozogomez on 07/02/16.
 */
public class PosterImageBehavior extends CoordinatorLayout.Behavior<ImageView> {
    private static final String TAG = PosterImageBehavior.class.getSimpleName();
    private int mFinalYPosition = 0;
    private int mStartYPosition = 0;
    private int mStartChildHeight = 0;
    private int mFinalChildHeight = 0;
    private float mStartToolbarPosition = 0;
    private Context mContext;


    public PosterImageBehavior() {
    }

    public PosterImageBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, ImageView child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, ImageView child, View dependency) {


        mStartToolbarPosition = dependency.getY() + (dependency.getHeight() / 2);
        final int maxScrollDistance = (int) (mStartToolbarPosition - getStatusBarHeight());
        float expandedPercentageFactor = dependency.getY() / maxScrollDistance;

        mStartChildHeight = child.getHeight();
        mFinalChildHeight = 30;


            Log.d(TAG, dependency.getClass().getName());
            Log.d(TAG, "Toolbar Y: " + dependency.getY() + "");
            Log.d(TAG, "Height: " + dependency.getHeight());
            Log.d(TAG, "Child Y: " + child.getY());
            Log.d(TAG, "Child Start Height: " + mStartChildHeight);
            Log.d(TAG, "Child Final Height: " + mFinalChildHeight);
            Log.d(TAG, "Expanded % Factor: " + expandedPercentageFactor);
            Log.d(TAG, "maxScrollDistance: " + maxScrollDistance);

//        if(child.getY() <= getStatusBarHeight()){
//            Log.d(TAG, "yep");
//            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
//            lp.height = 200;
//            child.setLayoutParams(lp);
//        }


        return true;
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");

        if (resourceId > 0) {
            result = mContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
