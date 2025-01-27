package com.fimi.app.x8s.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

@SuppressLint({"AppCompatCustomView"})
public class TwoStateImageButton extends ImageButton {
    private static final int DISABLED_ALPHA = 102;
    private static final int ENABLED_ALPHA = 255;

    public TwoStateImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TwoStateImageButton(Context context) {
        this(context, null);
    }

    public TwoStateImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            setAlpha(ENABLED_ALPHA);
            getBackground().setAlpha(ENABLED_ALPHA);
            return;
        }
        setAlpha(DISABLED_ALPHA);
        getBackground().setAlpha(DISABLED_ALPHA);
    }
}
