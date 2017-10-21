package com.lostad.app.demo.view.chatkitapplication;
import cn.leancloud.chatkit.LCChatKitUser;
import android.view.View;

/**
 * Created by skyluo on 2017/9/21.
 */

public interface OnItemClickListener {
   void onclick(View view, LCChatKitUser user);

}
