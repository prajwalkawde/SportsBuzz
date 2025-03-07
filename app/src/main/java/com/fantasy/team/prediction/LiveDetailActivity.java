package com.fantasy.team.prediction;

import android.os.Bundle;

 

import kotlin.jvm.internal.Intrinsics;


public final class LiveDetailActivity extends MainActivity {
    private final String TAG = "LiveDetailActivity";
    public String key;

    public void _$_clearFindViewByIdCache() {
    }

    public final String getKey() {
        String str = this.key;
        if (str != null) {
            return str;
        }
        Intrinsics.throwUninitializedPropertyAccessException("key");
        return null;
    }

    public final void setKey(String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.key = str;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
//        setTheme(R.style.Theme_IAPTheme);
//        setContentView((int) R.layout.activity_live_detail);
//        String stringExtra = getIntent().getStringExtra("liveMatchKeyData");
//        Intrinsics.checkNotNull(stringExtra);
//        Intrinsics.checkNotNullExpressionValue(stringExtra, "intent.getStringExtra(\"liveMatchKeyData\")!!");
//        setKey(stringExtra);
//        Log.d(this.TAG, Intrinsics.stringPlus("onCreate: ", getKey()));
     //   LiveMatchViewModel liveMatchViewModel = (LiveMatchViewModel) getDefaultViewModelProviderFactory().create(LiveMatchViewModel.class);

    }

}
