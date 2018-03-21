package com.lostad.app.demo.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import com.lostad.app.demo.DataPresenter ;
import com.lostad.app.demo.R;
import com.lostad.app.demo.MyApplication ;
import com.lostad.app.demo.adapter.CommentsAdapter;
import com.lostad.app.demo.adapter.GridPhotoAdapter;
import com.lostad.app.demo.event.RefreshLikeEvent;
import com.lostad.app.demo.Model.CommentInfo;
import com.lostad.app.demo.Model.ResultInfo;
import com.lostad.app.demo.Model.TweetInfo;
import com.lostad.app.demo.Model.UserInfo;
import com.lostad.app.demo.network.NetworkManager;
import com.lostad.app.demo.network.ResponseListener;
import com.lostad.app.demo.util.StringUtils;
import com.lostad.app.demo.widget.enter.AutoHeightGridView;
import com.lostad.app.demo.widget.enter.EmojiFragment;
import com.lostad.app.demo.widget.enter.EnterEmojiLayout;
import com.lostad.app.demo.widget.enter.EnterLayout;

public class TweetDetailActivity extends BaseActivity implements EmojiFragment.EnterEmojiLayout,
        DataPresenter.GetUserInfo, DataPresenter.GetCommentList, DataPresenter.SendComment {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.avatar)
    CircleImageView avatar;
    @Bind(R.id.username)
    TextView nickname;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.content)
    TextView content;
    @Bind(R.id.gridView)
    AutoHeightGridView gridView;
//    @Bind(R.id.btnLike)
//    CheckBox btnLike;
//    @Bind(R.id.like_count)
//    TextView likeCount;
    @Bind(R.id.btnComments)
    ImageButton btnComments;
    @Bind(R.id.comment_count)
    TextView commentCount;
    @Bind(R.id.comments_list)
    ListView commentsList;
    @Bind(R.id.sendmsg)
    ImageButton sendmsg;

    private TweetInfo.TweetsEntity tweetsEntity;
    private static final String INFO = "tweetsEntity";

    private List<CommentInfo.CommentsEntity> commentList = new ArrayList<>();
    private CommentsAdapter adapter;

    EnterEmojiLayout enterLayout;
    EditText msgEdit;
    private boolean mFirstFocus = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        tweetsEntity = getIntent().getParcelableExtra(INFO);

        initViews();

        //GlobalUtils.setListViewHeightBasedOnChildren(commentsList);
        adapter = new CommentsAdapter(TweetDetailActivity.this, commentList);
        commentsList.setAdapter(adapter);
        DataPresenter.getCommentList(tweetsEntity.getTweets_id(), TweetDetailActivity.this);

//        viewHolder.btnLike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
//                NetworkManager.postUpvoteTweet(Integer.toString(
//                        YouJoinApplication.getCurrUser().getId()),
//                        Integer.toString(dataList.get(location).getTweets_id()),
//                        new ResponseListener<ResultInfo>() {
//                            @Override
//                            public void onErrorResponse(VolleyError volleyError) {
//                                LogUtils.e(TAG, volleyError.toString());
//                            }
//
//                            @Override
//                            public void onResponse(ResultInfo tweetsEntity) {
//                                if(tweetsEntity.getResult().equals(NetworkManager.SUCCESS)){
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

        UserInfo userInfo = DataPresenter.requestUserInfoFromCache(tweetsEntity.getFriend_id());
        if (userInfo.getResult().equals(NetworkManager.SUCCESS)
                && userInfo.getImg_url() != null) {
            Picasso.with(MyApplication.getAppContext())
                    .load(StringUtils.getPicUrlList(userInfo.getImg_url()).get(0))
                    .resize(200, 200)
                    .centerCrop()
                    .into(avatar);
            nickname.setText(userInfo.getName());
        } else {
            DataPresenter.requestUserInfoById(tweetsEntity.getFriend_id(), TweetDetailActivity.this);
        }

        List<String> urls = StringUtils.getPicUrlList(tweetsEntity.getTweets_img());
        GridPhotoAdapter adapter = new GridPhotoAdapter(MyApplication.getAppContext(), urls);
        gridView.setAdapter(adapter);

        setSupportActionBar(toolbar);
//        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
//                this.finish();
            default:
                this.finish();
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.sendmsg)
    protected void sendComment(){
        String content = msgEdit.getText().toString();
        if(content.equals("")) return;
        msgEdit.setText("");
        DataPresenter.sendComment(MyApplication.getCurrUser().getUserId(), tweetsEntity.getTweets_id(),
                content, TweetDetailActivity.this);
    }

    @Override
    public void onSendComment(ResultInfo info){
        if(info.getResult().equals(NetworkManager.SUCCESS)){
            DataPresenter.getCommentList(tweetsEntity.getTweets_id(), TweetDetailActivity.this);

        }
    }

    private void initViews() {
        setSupportActionBar(toolbar);
//        likeCount.setText(Integer.toString(tweetsEntity.getUpvote_num()));
        commentCount.setText(Integer.toString(tweetsEntity.getComment_num()));
        content.setText(StringUtils.getEmotionContent(
                MyApplication.getAppContext(), content,
                tweetsEntity.getTweets_content()));
        time.setText(tweetsEntity.getTweets_time());
//        if (tweetsEntity.getUpvote_status() == NetworkManager.UPVOTE_STATUS_NO) {
//            btnLike.setChecked(false);
//        } else {
//            btnLike.setChecked(true);
//        }

//        EventBus.getDefault().post(new RefreshLikeEvent(RefreshLikeEvent.TYPE_INIT));

//        btnLike.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final int type;
//                if(btnLike.isChecked()){
//                    type = RefreshLikeEvent.TYPE_UNUPVOTE;
//                }else{
//                    type = RefreshLikeEvent.TYPE_UPVOTE;
//                }
//               // NetworkManager.postUpvoteTweet(Integer.toString(MyApplication.getCurrUser().getId()),
//                NetworkManager.postRequestIsUpvote(MyApplication.getCurrUser().getUserId(),
//                        tweetsEntity.getTweets_id(), new ResponseListener<ResultInfo>() {
//                            @Override
//                            public void onErrorResponse(VolleyError volleyError) {
//
//                            }
//
//                            @Override
//                            public void onResponse(ResultInfo o) {
//                                if (o.getResult().equals(NetworkManager.SUCCESS)) {
//                                    EventBus.getDefault().post(new RefreshLikeEvent(type));
//                                }else{
//                                    Toast.makeText(TweetDetailActivity.this, getText(R.string.error_network), Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//            }
//        });

        initEnter();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final RefreshLikeEvent event){
       // NetworkManager.postRequestIsUpvote(Integer.toString(MyApplication.getCurrUser().getId()),
//        NetworkManager.postRequestIsUpvote(MyApplication.getCurrUser().getUserId(),
//                tweetsEntity.getTweets_id(), new ResponseListener<ResultInfo>() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//
//                    }
//
//                    @Override
//                    public void onResponse(ResultInfo o) {
//                        if (o.getResult().equals(NetworkManager.SUCCESS)) {
//                            btnLike.setChecked(true);
//                            if(event.type != RefreshLikeEvent.TYPE_INIT){
//                                int num = tweetsEntity.getUpvote_num();
//                                num++;
//                                tweetsEntity.setUpvote_num(num);
//                                likeCount.setText(Integer.toString(num));
//
//                            }
//                        }else{
//                            btnLike.setChecked(false);
//                            if(event.type != RefreshLikeEvent.TYPE_INIT){
//                                int num = tweetsEntity.getUpvote_num();
//                                num--;
//                                tweetsEntity.setUpvote_num(num);
//                                likeCount.setText(Integer.toString(num));
//                            }
//                        }
//                    }
//                });
    }

    @Override
    public void onGetCommentList(CommentInfo info) {
        if (info.getResult().equals(NetworkManager.SUCCESS)) {
            commentList.clear();
            for (CommentInfo.CommentsEntity entity : info.getComments()) {
                commentList.add(entity);
            }
            adapter.notifyDataSetChanged();
        } else {
            //Toast.makeText(TweetDetailActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onGetUserInfo(UserInfo info) {
        if (info.getResult().equals(NetworkManager.SUCCESS)) {
            Picasso.with(MyApplication.getAppContext())
                    .load(StringUtils.getPicUrlList(info.getHeadUrl()).get(0))
                    .resize(200, 200)
                    .centerCrop()
                    .into(avatar);
            nickname.setText(info.getName());
        } else {
            Toast.makeText(TweetDetailActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
        }
    }

    private void initEnter() {
        enterLayout = new EnterEmojiLayout(this, null);
        msgEdit = enterLayout.content;
        enterLayout.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterLayout.popKeyboard();
            }
        });
        enterLayout.content.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (mFirstFocus && hasFocus) {
                    mFirstFocus = false;
                    enterLayout.popKeyboard();
                }
            }
        });

    }

    @Override
    protected void onStop() {
        enterLayout.closeEnterPanel();
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        ButterKnife.unbind(this);
    }
    @Override
    public EnterLayout getEnterLayout() {
        return enterLayout;
    }


    public static void actionStart(Context context, TweetInfo.TweetsEntity info) {
        Intent intent = new Intent(context, TweetDetailActivity.class);
        intent.putExtra(INFO, info);
        context.startActivity(intent);
    }

}
