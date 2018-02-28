package com.lostad.app.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.lostad.app.demo.network.ResponseListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import com.lostad.app.demo.DataPresenter ;
import com.lostad.app.demo.R;
import com.lostad.app.demo.MyApplication ;
import com.lostad.app.demo.Model.CommentInfo;
import com.lostad.app.demo.Model.UserInfo ;
import com.lostad.app.demo.network.NetworkManager;
import com.lostad.app.demo.util.LogUtils ;
import com.lostad.app.demo.util.StringUtils;


public class CommentsAdapter extends BaseAdapter {

    public static final String TAG = "YouJoin";

    private List<CommentInfo.CommentsEntity> dataList;
    private LayoutInflater inflater;
    private Context context;

    public CommentsAdapter(Context context, List<CommentInfo.CommentsEntity> dataList){
        this.dataList = dataList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount(){
        return dataList.size();
    }

    @Override
    public Object getItem(int position){
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final ViewHolder holder;
        if(convertView == null){

            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.comment_list_item, null);
            holder.avatar = (CircleImageView) convertView.findViewById(R.id.avatar);
            holder.content = (TextView) convertView.findViewById(R.id.comment_content);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.nickname = (TextView) convertView.findViewById(R.id.nickname);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        UserInfo info = DataPresenter.requestUserInfoIDFromCache((dataList.get(position).getFriend_id()));
        if(info.getResult().equals(NetworkManager.SUCCESS)
                && info.getImg_url() != null){
            Picasso.with(context)
                    .load(StringUtils.getPicUrlList(info.getImg_url()).get(0))
                    .resize(200, 200)
                    .centerCrop()
                    .into(holder.avatar);
        }else {
            NetworkManager.postRequestUserInfo(dataList.get(position).getFriend_id(),
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
                                    .into(holder.avatar);
                            holder.nickname.setText(info.getNickname());
                        }
                    });
        }

        holder.nickname.setText(info.getNickname());
        holder.time.setText(dataList.get(position).getComment_time());

        holder.content.setText(StringUtils.getEmotionContent(
                MyApplication.getAppContext(), holder.content,
                dataList.get(position).getComment_content()));
        return convertView;
    }

    public final class ViewHolder{
        public CircleImageView avatar;
        public TextView content;
        public TextView time;
        public TextView nickname;
    }
}
