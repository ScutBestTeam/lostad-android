package com.lostad.app.demo.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.lostad.app.demo.Model.ResultInfo;
import com.lostad.app.demo.manager.DatabaseManager;
import com.lostad.app.demo.view.MainActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.activity.LCIMConversationActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import com.lostad.app.demo.DataPresenter ;
import com.lostad.app.demo.R;
import com.lostad.app.demo.MyApplication ;
import com.lostad.app.demo.Model.TweetInfo ;
import com.lostad.app.demo.Model.UserInfo ;
import com.lostad.app.demo.network.NetworkManager;
import com.lostad.app.demo.network.ResponseListener;
import com.lostad.app.demo.util.LogUtils ;
import com.lostad.app.demo.util.StringUtils;
import com.lostad.app.demo.widget.enter.AutoHeightGridView;


public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder>{

    public static final String TAG = "YouJoin";

    public OnItemClickListener itemClickListener;

    public OnItemClickListener OnContactClick;
    private List<TweetInfo.TweetsEntity> dataList;

    public TweetsAdapter(List<TweetInfo.TweetsEntity> data){
        dataList = data;
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
    public void setOnContactClick(OnItemClickListener OnContactClick){
        this.OnContactClick = OnContactClick;
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
//        public CheckBox btnLike;
        public ImageButton btnComments;
        public TextView nickname;
//        public TextView likeCount;
        public TextView commentCount;
        public TextView tweetContent;
        public TextView time;
        public CircleImageView avatar;
        public AutoHeightGridView gridView;

        public ViewHolder(View itemView){
            super(itemView);
//            likeCount = (TextView) itemView.findViewById(R.id.like_count);
            commentCount = (TextView) itemView.findViewById(R.id.comment_count);
            tweetContent = (TextView) itemView.findViewById(R.id.content);
            avatar = (CircleImageView) itemView.findViewById(R.id.avatar);
//            btnLike = (CheckBox) itemView.findViewById(R.id.btnLike);
            btnComments = (ImageButton) itemView.findViewById(R.id.btnComments);
            gridView = (AutoHeightGridView) itemView.findViewById(R.id.gridView);
            nickname = (TextView) itemView.findViewById(R.id.username);
            time = (TextView) itemView.findViewById(R.id.time);
            avatar.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                              OnContactClick.onItemClick(view,getPosition());
                                          };
        });
        }
        //通过接口回调来实现点击事件
        @Override
        public void onClick(View v){
            if(itemClickListener != null){
                itemClickListener.onItemClick(v, getPosition());
            }
        }


    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i){
        final int location = dataList.size() - i - 1;
        //建立起ViewHolder中视图与数据的关联
//        viewHolder.likeCount.setText(Integer.toString(dataList.get(location).getUpvote_num()));
        viewHolder.commentCount.setText(Integer.toString(dataList.get(location).getComment_num()));
        viewHolder.tweetContent.setText(StringUtils.getEmotionContent(
                MyApplication.getAppContext(), viewHolder.tweetContent,
                dataList.get(location).getTweets_content()));
        viewHolder.time.setText(dataList.get(location).getTweets_time());
//        if(dataList.get(location).getUpvote_status() == NetworkManager.UPVOTE_STATUS_NO){
//            viewHolder.btnLike.setChecked(false);
//        }else {
//            viewHolder.btnLike.setChecked(true);
//        }

//        viewHolder.btnLike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
//                NetworkManager.postUpvoteTweet(
//                        MyApplication.getCurrUser().getUserId(),
//                        dataList.get(location).getTweets_id(),
//                        new ResponseListener<ResultInfo>() {
//                            @Override
//                            public void onErrorResponse(VolleyError volleyError) {
//                                LogUtils.e(TAG, volleyError.toString());
//                            }
//
//                            @Override
//                            public void onResponse(ResultInfo info) {
//                                if(info.getResult().equals(NetworkManager.SUCCESS)){
//                                    viewHolder.btnLike.setChecked(isChecked);
//                                    int k = dataList.get(location).getUpvote_num();
//                                    if(isChecked){
//                                        k++;
//                                    }else{
//                                        k--;
//                                    }
//                                    dataList.get(location).setUpvote_num(k);
//                                    viewHolder.likeCount.setText(Integer.toString(k));
//                                }else{
//                                    viewHolder.btnLike.setChecked(!isChecked);
//                                }
//                            }
//                        });
//
//            }
//        });

        UserInfo info = DataPresenter.requestUserInfoFromCache(dataList.get(location).getFriend_id());
        if(info.getResult().equals(NetworkManager.SUCCESS)
                && info.getImg_url() != null){
            Picasso.with(MyApplication.getAppContext())
                    .load(StringUtils.getPicUrlList(info.getImg_url()).get(0))
                    .resize(200, 200)
                    .centerCrop()
                    .into(viewHolder.avatar);
            viewHolder.nickname.setText(info.getName());
        }else {
            NetworkManager.postRequestUserInfo(dataList.get(location).getFriend_id(),
                    new ResponseListener<UserInfo>() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            LogUtils.e(TAG, volleyError.toString());
                        }

                        @Override
                        public void onResponse(UserInfo info) {
                            Picasso.with(MyApplication.getAppContext())
                                    .load(StringUtils.getPicUrlList(info.getHeadUrl()).get(0))
                                    .resize(200, 200)
                                    .centerCrop()
                                    .into(viewHolder.avatar);
                            viewHolder.nickname.setText(info.getName());
                            DatabaseManager.addUserInfo(info);
                        }
                    });
        }

        List<String> urls = StringUtils.getPicUrlList(dataList.get(location).getTweets_img());
        GridPhotoAdapter adapter = new GridPhotoAdapter(MyApplication.getAppContext(), urls);
        viewHolder.gridView.setAdapter(adapter);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        //将布局转化为view并传递给RecyclerView封装好的ViewHolder
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tweets_list_item,
                viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemCount(){
        return dataList.size();
    }

}
