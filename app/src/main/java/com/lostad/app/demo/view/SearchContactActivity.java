package com.lostad.app.demo.view;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lostad.app.base.util.EffectUtil;
import com.lostad.app.base.util.PrefManager;
import com.lostad.app.base.view.BaseActivity;
import com.lostad.app.demo.IConst;
import com.lostad.app.demo.MyApplication;
import com.lostad.app.demo.R;
import com.lostad.app.demo.entity.LoginConfig;
import com.lostad.app.demo.task.LoginTask;
import com.lostad.app.demo.view.chatkitapplication.MembersAdapter;
import com.lostad.app.demo.view.chatkitapplication.OnItemClickListener;
import com.lostad.applib.core.MyCallback;
import com.lostad.applib.entity.ILoginConfig;
import com.lostad.applib.util.Validator;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.utils.Exceptions;

import org.w3c.dom.Text;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.LCChatKitUser;
import okhttp3.Call;

public class SearchContactActivity extends BaseActivity {

   @ViewInject(R.id.sc_searchbox)
    private SearchView searchView;
    @ViewInject(R.id.sc_relative)
    private RelativeLayout relativeLayout;
    @ViewInject(R.id.sc_warn)
    private TextView warn;
   @ViewInject(R.id.sc_friend_name)
    private TextView friend_name;
   @ViewInject(R.id.sc_img_friend_avatar)
    private ImageView img;
   @ViewInject(R.id.sc_submit)
    private Button sc_submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchcontact);

        x.view().inject(this);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
            @Override
            public boolean onQueryTextSubmit(String query) {
                String url= IConst.URL_BASE2;
                url+="getUserDetail";
                List<String> queryList=new ArrayList<String>();
                queryList.add(query);
                String params=new Gson().toJson(queryList);
                OkHttpUtils
                        .post()//
                        .addParams("userList",params)
                        .url(url)//
                        .build()//
                        .connTimeOut(5000)
                        .execute(new StringCallback() {

                            @Override
                            public void onError(Call call, Exception e, int id) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                try{
                                    List<LCChatKitUser> userList = new ArrayList<LCChatKitUser>();
                                    Type type = new TypeToken<List<LCChatKitUser>>() {}.getType();
                                    relativeLayout.setVisibility(View.VISIBLE);
                                    final List<LCChatKitUser> users = new Gson().fromJson(response, type);
                                    final LCChatKitUser user=users.get(0);
                                        friend_name.setText(user.getUserId() + "(" + user.getUserName() + ")");
                                        sc_submit.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                submitInsert(user.getUserId());
                                            }
                                        });
                                        Picasso.with(SearchContactActivity.this).load(user.getAvatarUrl()).into(img);
                                    }

                                catch (Exception e){
                                    warn.setVisibility(View.VISIBLE);
                                    return;
                                }


                            }
            });
                return true;
    }
    });

    }
   public void submitInsert(String friendId){
       String hostId=MyApplication.getInstance().getLoginConfig().getUserId();
       String url= IConst.URL_BASE2;
       url+="addNewFriend";
       OkHttpUtils
               .post()//
               .url(url)
               .addParams("hostId",hostId)
               .addParams("friendId",friendId)
               .build()//
               .connTimeOut(5000)
               .execute(new StringCallback() {

                   @Override
                   public void onError(Call call, Exception e, int id) {
                       e.printStackTrace();
                       Toast.makeText(SearchContactActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                   }

                   @Override
                   public void onResponse(String response, int id) {
                       //返回处理结果，200表示处理完成，500服务器内部错误
                       String result = new Gson().fromJson(response, String.class);
                       if(result.equals("200")){
                           Toast.makeText(SearchContactActivity.this,"成功添加好友",Toast.LENGTH_SHORT).show();

                       }
                       if(result.equals("500")){
                           Toast.makeText(SearchContactActivity.this,"你已添加该好友，添加失败",Toast.LENGTH_SHORT).show();

                       }
                   }
               });

   }
}

