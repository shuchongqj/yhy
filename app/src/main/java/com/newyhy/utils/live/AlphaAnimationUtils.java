package com.newyhy.utils.live;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

public class AlphaAnimationUtils {

    public static AlphaAnimation alphaAnimation(View view, float start, float end, long duration, boolean show){
        AlphaAnimation alphaAnimation = new AlphaAnimation(start, end);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
                view.setVisibility(show?View.VISIBLE:View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        view.startAnimation(alphaAnimation);
        return alphaAnimation;
    }

}
