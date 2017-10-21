package com.lostad.app.demo.widget.enter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.style.DynamicDrawableSpan;

import com.lostad.app.demo.util.GlobalUtils;

class EmojiconSpan extends DynamicDrawableSpan {
    private final Context mContext;
    private final int mResourceId;
    private Drawable mDrawable;
    //private boolean mIsMonkey;

    public EmojiconSpan(Context context, String iconName) {
        super();
        mContext = context;

//        String name = EmojiFragment.textToMonkdyMap.get(iconName);
//        if (name == null) {
//            name = iconName;
//            //mIsMonkey = false;
//        } else {
//            //mIsMonkey = true;
//        }

        mResourceId = MyImageGetter.getResourceId(iconName);
    }

    @Override
    public Drawable getDrawable() {
        if (mDrawable == null) {
            try {
                mDrawable = mContext.getResources().getDrawable(mResourceId);
                GlobalUtils.zoomDrwable(mDrawable);
            } catch (Exception e) {
            }
        }
        return mDrawable;
    }
}
