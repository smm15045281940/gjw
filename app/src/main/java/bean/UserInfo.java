package bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/26.
 */

public class UserInfo implements Serializable {

    private String userName;
    private String avatar;
    private String levelName;
    private String favoritesStore;
    private String favoritersGoods;
    private boolean autoLogin;
    private String key;

    public UserInfo() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getFavoritesStore() {
        return favoritesStore;
    }

    public void setFavoritesStore(String favoritesStore) {
        this.favoritesStore = favoritesStore;
    }

    public String getFavoritersGoods() {
        return favoritersGoods;
    }

    public void setFavoritersGoods(String favoritersGoods) {
        this.favoritersGoods = favoritersGoods;
    }

    public boolean isAutoLogin() {
        return autoLogin;
    }

    public void setAutoLogin(boolean autoLogin) {
        this.autoLogin = autoLogin;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "userName='" + userName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", levelName='" + levelName + '\'' +
                ", favoritesStore='" + favoritesStore + '\'' +
                ", favoritersGoods='" + favoritersGoods + '\'' +
                ", autoLogin=" + autoLogin +
                ", key='" + key + '\'' +
                '}';
    }
}
