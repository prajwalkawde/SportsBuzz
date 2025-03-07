package com.fantasy.team.prediction.util.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatButton;

public class MediumButtone extends AppCompatButton {
    public MediumButtone(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        if (!isInEditMode()) {
            setTypeface(Typeface.createFromAsset(getContext().getAssets(), "medium.ttf"));
        }
    }
}
