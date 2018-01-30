package com.lostad.app.demo.view.mainFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lostad.app.base.view.fragment.BaseFragment;
import com.lostad.app.demo.IConst;
import com.lostad.app.demo.MyApplication;
import com.lostad.app.demo.R;
import com.lostad.app.demo.swipeitemlayout.SwipeItemLayout;
import com.lostad.app.demo.view.LoginActivity;
import com.lostad.app.demo.view.SearchContactActivity;
import com.lostad.app.demo.view.chatkitapplication.CustomUserProvider;
import com.lostad.app.demo.view.chatkitapplication.MemberLetterEvent;
import com.lostad.app.demo.view.chatkitapplication.MembersAdapter;
import com.lostad.app.demo.view.chatkitapplication.OnItemClickListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.leancloud.chatkit.LCChatKitUser;
import cn.leancloud.chatkit.view.LCIMDividerItemDecoration;
import de.greenrobot.event.EventBus;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by wli on 15/12/4.
 * 联系人页面
 */
public class ContactFragment extends BaseFragment {

  protected SwipeRefreshLayout refreshLayout;
  protected RecyclerView recyclerView;
  private MembersAdapter itemAdapter;
  LinearLayoutManager layoutManager;
  private Button btn_add_friends;
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.contact_fragment, container, false);
    refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.contact_fragment_srl_list);
    recyclerView = (RecyclerView) view.findViewById(R.id.contact_fragment_rv_list);
    recyclerView.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(getContext()));
    layoutManager = new LinearLayoutManager(getActivity());
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.addItemDecoration(new LCIMDividerItemDecoration(getActivity()));
    itemAdapter = new MembersAdapter();
    itemAdapter.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onclick(View view, LCChatKitUser user) {
       deleteFriend(user);
      }
    });
    refreshMembers();
    recyclerView.setAdapter(itemAdapter);
    refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
          refreshLayout.setRefreshing(true);
        refreshMembers();

      }
    });
    EventBus.getDefault().register(this);
    btn_add_friends= (Button) view.findViewById(R.id.btn_add_friends);
    btn_add_friends.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(getActivity(), SearchContactActivity.class);
        startActivity(intent);
      }
    });
    return view;
  }


  @Override
  public void onDestroyView() {
    EventBus.getDefault().unregister(this);
    super.onDestroyView();
  }

  @Override
  public void onResume() {
    super.onResume();
    refreshMembers();
  }

  private void refreshMembers() {
      refreshLayout.setRefreshing(true);
      String id= MyApplication.getInstance().getLoginConfig().getUserId();
      //获取该用户联系人
      String url = IConst.URL_BASE2;
      url+="getUserFriend";
      Response response = null;
      OkHttpUtils
              .post()//
              .url(url)//
              .addParams("username",id)
              .build()//
              .connTimeOut(5000)
              .execute(new StringCallback() {

                @Override
                public void onError(Call call, Exception e, int id) {

                    Toast.makeText(getContext(),"网络错误",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                @Override
                public void onResponse(String response, int id) {
                  List<LCChatKitUser> contacts ;
                  Type type = new TypeToken<List<LCChatKitUser>>() {}.getType();
                  contacts = new Gson().fromJson(response, type);
                  itemAdapter.setMemberList(contacts);
                  itemAdapter.notifyDataSetChanged();
                    refreshLayout.setRefreshing(false);
                }
              });



  }
  /**
   * 处理 LetterView 发送过来的 MemberLetterEvent
   * 会通过 MembersAdapter 获取应该要跳转到的位置，然后跳转
   */
  public void onEvent(MemberLetterEvent event) {
    Character targetChar = Character.toLowerCase(event.letter);
    if (itemAdapter.getIndexMap().containsKey(targetChar)) {
      int index = itemAdapter.getIndexMap().get(targetChar);
      if (index > 0 && index < itemAdapter.getItemCount()) {
        layoutManager.scrollToPositionWithOffset(index, 0);
      }
    }
  }
  private void deleteFriend(LCChatKitUser lcChatKitUser) {
    String hostId= MyApplication.getInstance().getLoginConfig().getUserId();
    String friendId=lcChatKitUser.getUserId();
    String url= IConst.URL_BASE2;
    url+="deleteFriend";
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
                Toast.makeText(getContext(),"网络错误",Toast.LENGTH_SHORT).show();
              }

              @Override
              public void onResponse(String response, int id) {
                //返回处理结果，200表示处理完成，500服务器内部错误
                String result = new Gson().fromJson(response, String.class);
                if(result.equals("200")){
                  Toast.makeText(getContext(),"成功删除好友",Toast.LENGTH_SHORT).show();
                    refreshMembers();

                }
                if(result.equals("500")){
                  Toast.makeText(getContext(),"删除好友失败",Toast.LENGTH_SHORT).show();
                }
              }
            });

  }
  }
