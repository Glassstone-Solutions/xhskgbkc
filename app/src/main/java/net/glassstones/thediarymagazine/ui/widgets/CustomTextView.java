package net.glassstones.thediarymagazine.ui.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import net.glassstones.thediarymagazine.utils.CustomFontUtils;


/**
 * Created by Thompson on 14/02/2016.
 * For The Diary Magazine
 */
public class CustomTextView extends TextView {
    public CustomTextView(Context context) {
        super(context);
        if (!isInEditMode())
            CustomFontUtils.applyCustomFont(this, context, null);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            CustomFontUtils.applyCustomFont(this, context, attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode())
            CustomFontUtils.applyCustomFont(this, context, attrs);
    }
}
