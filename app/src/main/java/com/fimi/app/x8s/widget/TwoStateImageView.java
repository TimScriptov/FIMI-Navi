package com.fimi.app.x8s.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

@SuppressLint({"AppCompatCustomView"})
public class TwoStateImageView extends ImageView {
    private static final int DISABLED_ALPHA = 102;
    private static final int ENABLED_ALPHA = 255;

    public TwoStateImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TwoStateImageView(Context context) {
        this(context, null);
    }

    public TwoStateImageView(Context context, AttributeSet attrs, int defStyleAttr) {
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
