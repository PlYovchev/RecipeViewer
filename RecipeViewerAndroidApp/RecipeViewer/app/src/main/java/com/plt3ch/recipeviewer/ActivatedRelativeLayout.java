package com.plt3ch.recipeviewer;

/**
 * Created by plt3ch on 5/10/2015.
 */
import android.R;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;

import android.widget.RelativeLayout;

public class ActivatedRelativeLayout extends RelativeLayout implements Checkable{

    public static final int[] CHECKED_STATE = {R.attr.state_checked};
    private boolean mChecked;

    public ActivatedRelativeLayout(Context context) {
        super(context);
    }

    public ActivatedRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ActivatedRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public void setChecked(boolean b) {
        mChecked = b;
        refreshDrawableState();
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        mChecked = !mChecked;
        refreshDrawableState();
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        int[] states =  super.onCreateDrawableState(extraSpace + 1);
        if (mChecked){
            mergeDrawableStates(states, CHECKED_STATE);
        }
        return states;
    }


}
