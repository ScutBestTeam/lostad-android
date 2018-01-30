package com.lostad.app.demo.view.chatkitapplication;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lostad.app.demo.IConst;
import com.lostad.app.demo.MyApplication;
import com.lostad.app.demo.R;
import com.lostad.app.demo.view.SearchContactActivity;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import cn.leancloud.chatkit.LCChatKitUser;
import cn.leancloud.chatkit.activity.LCIMConversationActivity;
import cn.leancloud.chatkit.utils.LCIMConstants;
import cn.leancloud.chatkit.viewholder.LCIMCommonViewHolder;
import okhttp3.Call;

/**
 * Created by wli on 15/11/24.
 */
public class ContactItemHolder extends LCIMCommonViewHolder<LCChatKitUser> implements View.OnClickListener {

  TextView nameView;
  ImageView avatarView;
  RelativeLayout maincontent;
  Button button;
  OnItemClickListener onClickListener;
  public LCChatKitUser lcChatKitUser;

  public ContactItemHolder(Context context, ViewGroup root) {
    super(context, root, R.layout.common_user_item);
    initView();
  }
  public ContactItemHolder(Context context, ViewGroup root,OnItemClickListener onClickListener) {
    super(context, root, R.layout.common_user_item);
    this.onClickListener=onClickListener;
    initView();
  }



  public void initView() {
    nameView = (TextView)itemView.findViewById(R.id.tv_friend_name);
    avatarView = (ImageView)itemView.findViewById(R.id.img_friend_avatar);
    maincontent = (RelativeLayout)itemView.findViewById(R.id.layout_content);
    button=(Button) itemView.findViewById(R.id.tv_delete);
    maincontent.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getContext(), LCIMConversationActivity.class);
        intent.putExtra(LCIMConstants.PEER_ID, lcChatKitUser.getUserId());
        getContext().startActivity(intent);
      }
    });
    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
       onClickListener.onclick(view,lcChatKitUser);
      }
    });
  }

  @Override
  public void bindData(LCChatKitUser lcChatKitUser) {
    this.lcChatKitUser = lcChatKitUser;
    final String avatarUrl = lcChatKitUser.getAvatarUrl();
    if (!TextUtils.isEmpty(avatarUrl)) {
      Picasso.with(getContext()).load(avatarUrl).into(avatarView);
    } else {
      avatarView.setImageResource(R.drawable.lcim_default_avatar_icon);
    }
    nameView.setText(lcChatKitUser.getUserName());
  }

  public static ViewHolderCreator HOLDER_CREATOR = new ViewHolderCreator<ContactItemHolder>() {
    @Override
    public ContactItemHolder createByViewGroupAndType(ViewGroup parent, int viewType) {
      return new ContactItemHolder(parent.getContext(), parent);
    }
  };

  @Override
  public void onClick(View view) {
  }

}