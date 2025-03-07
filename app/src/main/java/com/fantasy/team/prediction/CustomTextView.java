package com.fantasy.team.prediction;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class CustomTextView extends AppCompatTextView {
    public CustomTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(attributeSet);
    }

    public CustomTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(attributeSet);
    }

    public CustomTextView(Context context) {
        super(context);
        init((AttributeSet) null);
    }

    private void init(AttributeSet attributeSet) {
        if (attributeSet != null) {
//            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet,R.styleable.AppBarLayout);
//            int i = obtainStyledAttributes.getInt(0, 0);
            Typeface typeface = null;
//            if (i == 5) {
//                typeface = Typeface.createFromAsset(getContext().getAssets(), "Fonts/noway_regular_webfont.ttf");
//            } else if (i == 7) {
//                typeface = Typeface.createFromAsset(getContext().getAssets(), "Fonts/noway_regular_italic_webfont.ttf");
//            } else if (i == 8) {
//                typeface = Typeface.createFromAsset(getContext().getAssets(), "Fonts/Noway_Regular.otf");
//            }
            setTypeface(typeface);
//            obtainStyledAttributes.recycle();
        }
    }
}
