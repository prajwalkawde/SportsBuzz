package com.fantasy.team.prediction.util.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatEditText;

public class SemiBoldEditText extends AppCompatEditText {
    public SemiBoldEditText(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        if (!isInEditMode()) {
            try {
                setTypeface(Typeface.createFromAsset(getContext().getAssets(), "medium.ttf"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
