package com.lostad.app.demo;

import android.util.Log;

import com.android.volley.VolleyError;

import java.util.List;

import com.lostad.app.demo.Model.CommentInfo;
import com.lostad.app.demo.Model.ResultInfo;
import com.lostad.app.demo.Model.TweetInfo;
import com.lostad.app.demo.Model.UserInfo;
import com.lostad.app.demo.Model.ImageInfo;
import com.lostad.app.demo.network.NetworkManager;
import com.lostad.app.demo.manager.DatabaseManager;
import com.lostad.app.demo.Model.FriendsInfo;
import com.lostad.app.demo.Model.PluginInfo;
import com.lostad.app.demo.Model.UpdateUserInfoResult;
import com.lostad.app.demo.network.JsonSyntaxError;
import com.lostad.app.demo.network.ResponseListener;
import com.lostad.app.demo.util.LogUtils;

/**用于封装所有除图片加载外的数据请求(数据库和网络)，将结果提供给UI显示
 */
public class DataPresenter {

    public static final String TAG = "YouJoin";



    public static UserInfo requestUserInfoFromCache(int userId){
        return DatabaseManager.getUserInfoById(userId);
    }

    public static UserInfo requestUserInfoFromCache(String username){
        return DatabaseManager.getUserInfoByUserName(username);
    }

    public static void requestUserInfoAuto(String param, final GetUserInfo q){
        UserInfo cookieInfo = DatabaseManager.getUserInfoAuto(param);
        if(cookieInfo.getResult().equals(NetworkManager.SUCCESS)){
            q.onGetUserInfo(cookieInfo);
        }
        NetworkManager.postRequestUserInfo(param,
                new ResponseListener<UserInfo>() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        LogUtils.e(TAG, volleyError.toString());
                        UserInfo info = new UserInfo();
                        info.setResult(NetworkManager.FAILURE);
                        q.onGetUserInfo(info);
                    }

                    @Override
                    public void onResponse(UserInfo info) {
                        if (info.getResult().equals(NetworkManager.SUCCESS)) {
                            q.onGetUserInfo(info);
                            DatabaseManager.addUserInfo(info);
                        }
                    }
                });
    }

    public static void requestUserInfoById(int userId, final GetUserInfo q){
        UserInfo cookieInfo = DatabaseManager.getUserInfoById(userId);
        if(cookieInfo.getResult().equals(NetworkManager.SUCCESS)){
            q.onGetUserInfo(cookieInfo);
        }

        NetworkManager.postRequestUserInfo(Integer.toString(userId),
                new ResponseListener<UserInfo>() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        LogUtils.e(TAG, volleyError.toString());
                        UserInfo info = new UserInfo();
                        info.setResult(NetworkManager.FAILURE);
                        q.onGetUserInfo(info);
                    }

                    @Override
                    public void onResponse(UserInfo info) {
                        if (info.getResult().equals(NetworkManager.SUCCESS)) {
                            q.onGetUserInfo(info);
                            DatabaseManager.addUserInfo(info);
                        }
                    }
                });
    }

    public static void requestFriendList(int userId, final GetFriendList q){

        // TODO: 2016/5/1 这里有严重bug！！！
//        FriendsInfo cookieInfo = DatabaseManager.getFriendList(userId);
//        if(cookieInfo.getResult().equals(NetworkManager.SUCCESS)){
//            q.onGetFriendList(cookieInfo);
//        }

        //NetworkManager.postRequestFriendList(Integer.toString(MyApplication.getCurrUser().getId()),
        NetworkManager.postRequestFriendList(MyApplication.getCurrUser().getId(),
                new ResponseListener<FriendsInfo>() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if(volleyError.getClass() == JsonSyntaxError.class) return;
                        LogUtils.e(TAG, volleyError.toString());
                        FriendsInfo info = new FriendsInfo();
                        info.setResult(NetworkManager.FAILURE);
                        q.onGetFriendList(info);
                    }

                    @Override
                    public void onResponse(FriendsInfo info) {

                        q.onGetFriendList(info);
                    }
                });
    }

    public static void requestTweets(int userId, String tweetId, String timeType, final GetTweets q){
        NetworkManager.postRequestTweets(Integer.toString(userId), tweetId, timeType
                , new ResponseListener<TweetInfo>() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        LogUtils.e(TAG, volleyError.toString());
                        TweetInfo info = new TweetInfo();
                        info.setResult(NetworkManager.FAILURE);
                        q.onGetTweets(info);
                    }

                    @Override
                    public void onResponse(TweetInfo info) {
                        q.onGetTweets(info);
                    }
                });
    }

    public static void sendTweet(int userId, String content, List<ImageInfo> images,
                                 final SendTweet q){
        NetworkManager.postSendTweet(Integer.toString(userId), content, images,
                new ResponseListener<ResultInfo>() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        LogUtils.e(TAG, volleyError.toString());
                        ResultInfo info = new ResultInfo();
                        info.setResult(NetworkManager.FAILURE);
                        q.onSendTweet(info);
                    }

                    @Override
                    public void onResponse(ResultInfo info) {
                        q.onSendTweet(info);
                    }
                });
    }

    public static void signIn(String username, String password, final SignIn q){
        NetworkManager.postSignIn(username, password, new ResponseListener<UserInfo>() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(TAG, "Signin error! " + volleyError.toString());
                UserInfo info = new UserInfo();
                info.setResult(NetworkManager.FAILURE);
                q.onSign(info);
            }

            @Override
            public void onResponse(UserInfo userInfo) {
                q.onSign(userInfo);
            }
        });
    }

    public static void signUp(String username, String password, String email, final SignUp q){
        NetworkManager.postSignUp(username, password, email, new ResponseListener<UserInfo>() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(TAG, "Signup error!" + volleyError.toString());
                UserInfo info = new UserInfo();
                info.setResult(NetworkManager.FAILURE);
                q.onSign(info);
            }

            @Override
            public void onResponse(UserInfo userInfo) {
                q.onSign(userInfo);
            }
        });
    }

    public static void updateUserInfo(final UserInfo userInfo, String picPath, final UpdateUserInfo q){
        NetworkManager.postUpdateUserInfo(userInfo, picPath, new ResponseListener<UpdateUserInfoResult>() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                UpdateUserInfoResult result = new UpdateUserInfoResult();
                result.setResult(NetworkManager.FAILURE);
                q.onUpdateUserInfo(result);
            }

            @Override
            public void onResponse(UpdateUserInfoResult result) {
                q.onUpdateUserInfo(result);
                DatabaseManager.addUserInfo(userInfo);
            }
        });
    }

    public static void addFriend(final int friendId, final AddFriend q){
        //NetworkManager.postAddFriend(Integer.toString(MyApplication.getCurrUser().getId()),
        NetworkManager.postAddFriend(MyApplication.getCurrUser().getId(),
                Integer.toString(friendId), new ResponseListener<ResultInfo>() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        LogUtils.e(TAG, volleyError.toString());
                        ResultInfo info = new ResultInfo();
                        info.setResult(NetworkManager.FAILURE);
                        q.onAddFriend(info);
                    }

                    @Override
                    public void onResponse(ResultInfo info) {
                        q.onAddFriend(info);
                        if(info.getResult().equals(NetworkManager.SUCCESS)){
                            DatabaseManager.addFriendInfo(friendId);
                        }
                    }
                });
    }

    public static void getCommentList(int tweetId, final GetCommentList q){

        NetworkManager.postRequestComments(Integer.toString(tweetId),
                new ResponseListener<CommentInfo>() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtils.e(TAG, volleyError.toString());
                CommentInfo info = new CommentInfo();
                info.setResult(NetworkManager.FAILURE);
                q.onGetCommentList(info);
            }

            @Override
            public void onResponse(CommentInfo info) {
                q.onGetCommentList(info);
            }
        });
    }

    public static void sendComment(int userId, int tweetId, String commentContent, final SendComment q){

        NetworkManager.postCommentTweet(Integer.toString(userId), Integer.toString(tweetId),
                commentContent, new ResponseListener<ResultInfo>() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        LogUtils.e(TAG, volleyError.toString());
                        ResultInfo info = new ResultInfo();
                        info.setResult(NetworkManager.FAILURE);
                        q.onSendComment(info);
                    }

                    @Override
                    public void onResponse(ResultInfo info) {
                        q.onSendComment(info);
                    }
                });
    }

    public static void requestPluginList(int userId, final GetPluginList q){

        NetworkManager.postRequestPlugin(Integer.toString(userId), new ResponseListener<PluginInfo>() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                PluginInfo info = new PluginInfo();
                info.setResult(NetworkManager.FAILURE);
                q.onGetPluginList(info);
            }

            @Override
            public void onResponse(PluginInfo info) {
                q.onGetPluginList(info);
            }
        });

    }

    public interface GetPluginList{
        void onGetPluginList(PluginInfo info);
    }

    public interface SendComment{
        void onSendComment(ResultInfo info);
    }

    public interface GetCommentList{
        void onGetCommentList(CommentInfo info);
    }

    public interface AddFriend{
        void onAddFriend(ResultInfo info);
    }

    public interface GetUserInfo{
        void onGetUserInfo(UserInfo info);
    }

    public interface GetFriendList{
        void onGetFriendList(FriendsInfo info);
    }

    public interface GetTweets{
        void onGetTweets(TweetInfo info);
    }

    public interface SendTweet{
        void onSendTweet(ResultInfo info);
    }

    public interface SignIn{
        void onSign(UserInfo info);
    }

    public interface SignUp{
        void onSign(UserInfo info);
    }

    public interface UpdateUserInfo{
        void onUpdateUserInfo(UpdateUserInfoResult info);
    }

}
