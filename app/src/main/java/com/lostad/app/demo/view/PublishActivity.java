package com.lostad.app.demo.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.lostad.app.demo.DataPresenter ;
import com.lostad.app.demo.R;
import com.lostad.app.demo.MyApplication ;
import com.lostad.app.demo.event.SendTweetEvent;
import com.lostad.app.demo.Model.ImageInfo;
import com.lostad.app.demo.Model.ResultInfo ;
import com.lostad.app.demo.network.NetworkManager;
import com.lostad.app.demo.util.GlobalUtils;
import com.lostad.app.demo.widget.enter.EmojiFragment;
import com.lostad.app.demo.widget.enter.EnterEmojiLayout;
import com.lostad.app.demo.widget.enter.EnterLayout;
import com.lostad.app.base.view.BaseActivity;

public class PublishActivity extends BaseActivity
        implements EmojiFragment.EnterEmojiLayout, DataPresenter.SendTweet {

    @Bind(R.id.lay_photo_container)
    LinearLayout layPhotoContainer;
    @Bind(R.id.popPhoto)
    ImageButton btnPopPhoto;
    @Bind(R.id.btn_send)
    ImageButton btnSend;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.inputLayout)
    LinearLayout inputLayout;
    @Bind(R.id.bottom_layout)
    LinearLayout bottomLayout;
    private String type;
    ArrayList<String> mSelectPath;
    ArrayList<ImageInfo> mData = new ArrayList<>();
    EnterEmojiLayout enterLayout;
    EditText msgEdit;


    private boolean mFirstFocus = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        ButterKnife.bind(this);

        btnPopPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.create(PublishActivity.this)
                        .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio(
                        .maxSelectNum(9)// 最大图片选择数量 int
                        .imageSpanCount(4)// 每行显示个数 int
                        .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                        .previewImage(false)// 是否可预览图片 true or false
                        .isCamera(true)// 是否显示拍照按钮 true or false
                        .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                        .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                        .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                        .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (msgEdit.getText().toString().equals("")) {
                    Toast.makeText(PublishActivity.this, "请说点什么吧~", Toast.LENGTH_SHORT).show();
                    return;
                }
                showProgress(true);
                DataPresenter.sendTweet(MyApplication.getCurrUser().getUserId(),
                        msgEdit.getText().toString(), mData,type ,PublishActivity.this);
            }
        });
        Intent intent =getIntent();
        this.type=intent.getStringExtra("type");
        initEnter();

        ThreeBounce threeBounce = new ThreeBounce();
        threeBounce.setColor(getResources().getColor(R.color.colorPrimary));
        progressBar.setIndeterminateDrawable(threeBounce);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
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

    @Override
    public void onSendTweet(ResultInfo info) {
        showProgress(false);
        if (info.getResult() != null && info.getResult().equals(NetworkManager.SUCCESS)) {
            msgEdit.setText("");
            mData.clear();
            layPhotoContainer.removeAllViews();
            GlobalUtils.popSoftkeyboard(PublishActivity.this, msgEdit, false);
            Toast.makeText(PublishActivity.this, getString(R.string.send_tweet_success)
                    , Toast.LENGTH_SHORT).show();
            EventBus.getDefault().post(new SendTweetEvent());
            PublishActivity.this.finish();
        } else {
            Toast.makeText(PublishActivity.this, getString(R.string.error_network)
                    , Toast.LENGTH_SHORT).show();
        }
    }

    private void initEnter() {
        enterLayout = new EnterEmojiLayout(this, null);
        msgEdit = enterLayout.content;
        if(this.type.equals("0"))
            msgEdit.setHint("发布旅行招募");
        else if(this.type.equals("1"))
            msgEdit.setHint("发布闲置信息");
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
        ButterKnife.unbind(this);
    }

    @Override
    public EnterLayout getEnterLayout() {
        return enterLayout;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PictureConfig.CHOOSE_REQUEST) {
            if (resultCode == RESULT_OK) {
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
//                StringBuilder sb = new StringBuilder();
                layPhotoContainer.removeAllViews();
                for (LocalMedia p : selectList) {
//                    sb.append(p);
//                    sb.append("\n");

                    View itemView = View.inflate(PublishActivity.this, R.layout.item_publish_photo, null);
                    ImageView img = (ImageView) itemView.findViewById(R.id.img);
                    itemView.setTag(p.getPath());

                    Picasso.with(PublishActivity.this)
                            .load(new File(p.getPath()))
                            .resize(200, 200)
                            .centerCrop()
                            .into(img);
                    if (layPhotoContainer != null) {
                        layPhotoContainer.addView(itemView,
                                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT));
                    }

                    mData.add(new ImageInfo(p.getPath()));
                }
                //yjPublishEdit.setText(sb.toString());
            }
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            inputLayout.setVisibility(show ? View.GONE : View.VISIBLE);
            bottomLayout.setVisibility(show ? View.GONE : View.VISIBLE);
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        } else {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            inputLayout.setVisibility(show ? View.GONE : View.VISIBLE);
            bottomLayout.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public static void actionStart(Context context,String type) {
        Intent intent = new Intent(context, PublishActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }
}
