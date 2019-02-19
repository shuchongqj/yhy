package com.newyhy.boom;

import android.content.Context;
import android.util.AttributeSet;

import com.yhy.boombutton.BackgroundView;
import com.yhy.boombutton.BoomMenuButton;

public class MyBoomMenuButton extends BoomMenuButton{

    private MyBackgroundView backgroundView;

    public MyBoomMenuButton(Context context) {
        super(context);
    }

    public MyBoomMenuButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyBoomMenuButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected BackgroundView onCreateBackground(Context context, BoomMenuButton boomMenuButton) {
        if (backgroundView != null){
            return backgroundView;
        }
        return backgroundView = new MyBackgroundView(context, boomMenuButton);
    }

    public MyBackgroundView getMyBackgroundView(){
        return backgroundView;
    }

}
