package com.fantasy.team.prediction.util.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.material.tabs.TabLayout;

public class CustomTabLayout extends TabLayout {

    /* renamed from: a */
    public Typeface f544a = Typeface.createFromAsset(getContext().getAssets(), "font/Medium.ttf");

    public CustomTabLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void addTab(Tab tab) {
        super.addTab(tab);
        ViewGroup viewGroup = (ViewGroup) ((ViewGroup) getChildAt(0)).getChildAt(tab.getPosition());
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = viewGroup.getChildAt(i);
            if (childAt instanceof TextView) {
                ((TextView) childAt).setTypeface(this.f544a, 0);
            }
        }
    }
}
