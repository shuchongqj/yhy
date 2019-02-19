package com.quanyan.yhy.ui.spcart.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

/**
 * Created with Android Studio.
 * Title:NoMenuEditText
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2017-1-4
 * Time:11:39
 * Version 1.0
 * Description:
 */
@SuppressLint("NewApi")
public class NoMenuEditText extends EditText {
    private final Context context;


    /**
     * This is a replacement method for the base TextView class' method of the
     * same name. This method is used in hidden class android.widget.Editor to
     * determine whether the PASTE/REPLACE popup appears when triggered from the
     * text insertion handle. Returning false forces this window to never
     * appear.
     *
     * @return false
     */
    boolean canPaste() {
        return false;
    }


    /**
     * This is a replacement method for the base TextView class' method of the
     * same name. This method is used in hidden class android.widget.Editor to
     * determine whether the PASTE/REPLACE popup appears when triggered from the
     * text insertion handle. Returning false forces this window to never
     * appear.
     *
     * @return false
     */
    @Override
    public boolean isSuggestionsEnabled() {
        return false;
    }


    public NoMenuEditText(Context context) {
        super(context);
        this.context = context;
        init();
    }


    public NoMenuEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }


    public NoMenuEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }


    private void init() {
        this.setCustomSelectionActionModeCallback(new ActionModeCallbackInterceptor());
        this.setLongClickable(false);
    }


    /**
     * Prevents the action bar (top horizontal bar with cut, copy, paste, etc.)
     * from appearing by intercepting the callback that would cause it to be
     * created, and returning false.
     */
    private class ActionModeCallbackInterceptor implements ActionMode.Callback {

        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            return false;
        }


        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }


        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return false;
        }


        public void onDestroyActionMode(ActionMode mode) {
        }
    }
}