package com.hoangtuan.translatechinesehwriting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by atbic on 2/4/2018.
 */

@SuppressLint("AppCompatCustomView")
public class CustomEdittext extends EditText {
    private int mLastSelectionStart;
    private int mLastSelectionEnd;

    private OnSelectionChanged mOnSelectionChangedListener;

    public interface OnSelectionChanged {
        public void onSelectionChanged(EditText editText, int selStart, int selEnd);
    }

    public CustomEdittext(Context context) {
        super(context);
        init();
    }

    public CustomEdittext(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomEdittext(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        setTextIsSelectable(true);
    }

    public void setOnSelectionChangedListener(OnSelectionChanged l) {
        mOnSelectionChangedListener = l;
    }

    @Override
    public void setSelection(int index) {
        int length = getText().length();
        index = Math.min(index, length);
        mLastSelectionStart = index;
        mLastSelectionEnd = index;
        super.setSelection(index);
    }

    @Override
    public void setSelection(int start, int stop) {
        int length = getText().length();
        start = Math.min(start, length);
        stop = Math.min(stop, length);
        mLastSelectionStart = start;
        mLastSelectionEnd = stop;
        super.setSelection(start, stop);
    }
    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        if (selStart != mLastSelectionStart || selEnd != mLastSelectionEnd) {
            if (mOnSelectionChangedListener != null) {
                mOnSelectionChangedListener.onSelectionChanged(this, selStart, selEnd);
                mLastSelectionStart = selStart;
                mLastSelectionEnd = selEnd;
            }
        }
    }
}
