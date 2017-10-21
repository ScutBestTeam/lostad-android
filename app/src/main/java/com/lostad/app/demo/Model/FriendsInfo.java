package com.lostad.app.demo.Model;

import android.support.annotation.NonNull;

import java.util.List;

import com.lostad.app.demo.util.StringUtils;

public class FriendsInfo {
    /**
     * result : success
     * friends : [{"id":"16","nickname":"zzq","img_url":"http://192.168.0.103:8088/youjoin-server/upload/16/20151207053324_lufei.jpg;"}]
     */

    private String result;
    /**
     * id : 16
     * nickname : zzq
     * img_url : http://192.168.0.103:8088/youjoin-server/upload/16/20151207053324_lufei.jpg;
     */

    private List<FriendsEntity> friends;

    public void setResult(String result) {
        this.result = result;
    }

    public void setFriends(List<FriendsEntity> friends) {
        this.friends = friends;
    }

    public String getResult() {
        return result;
    }

    public List<FriendsEntity> getFriends() {
        return friends;
    }

    public static class FriendsEntity implements Comparable<FriendsEntity>{
        private int id;
        private String nickname;
        private String img_url;

        public String getFirstLetter() {
            String letter = StringUtils.getFirstLetters(nickname).toUpperCase().substring(0, 1);
            if(0 <= letter.compareTo("A") && letter.compareTo("Z") <= 0){
                return letter;
            }else{
                return "#";
            }

        }

        public char getFirstChar() {
            String letter = StringUtils.getFirstLetters(nickname).toUpperCase().substring(0, 1);
            if(0 <= letter.compareTo("A") && letter.compareTo("Z") <= 0){
                return letter.charAt(0);
            }else{
                return '#';
            }
        }

        public String getPinyin(){
            return StringUtils.Ch2PinYin(nickname);
        }

        @Override
        public int compareTo(@NonNull FriendsEntity another){
            return getFirstLetter().compareTo(another.getFirstLetter());
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public int getId() {
            return id;
        }

        public String getNickname() {
            return nickname;
        }

        public String getImg_url() {
            return img_url;
        }
    }
}
