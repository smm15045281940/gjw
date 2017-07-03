package utils;

import android.content.Context;
import android.content.SharedPreferences;

import bean.UserInfo;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Administrator on 2017/6/27.
 */

public class UserUtils {

    //判断首次打开
    public static boolean isFirstIn(Context context) {
        boolean b;
        SharedPreferences sp = context.getSharedPreferences("data", MODE_PRIVATE);
        String s = sp.getString("isFirstIn", "");
        if (s.equals("no")) {
            b = false;
        } else {
            b = true;
        }
        return b;
    }

    //写入首次状态
    public static void writeFirstIn(Context context) {
        SharedPreferences sp = context.getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor et = sp.edit();
        et.putString("isFirstIn", "no");
        et.commit();
    }

    //判断登录状态
    public static boolean isLogined(Context context) {
        boolean b = false;
        SharedPreferences sp = context.getSharedPreferences("data", MODE_PRIVATE);
        int loginState = sp.getInt("loginState", 0);
        switch (loginState) {
            case 0:
                b = false;
                break;
            case 1:
                b = true;
                break;
        }
        return b;
    }

    //写入用户数据
    public static void writeLogin(Context context, UserInfo userInfo) {
        if (userInfo != null) {
            SharedPreferences sp = context.getSharedPreferences("data", MODE_PRIVATE);
            SharedPreferences.Editor et = sp.edit();
            et.putInt("loginState", 1);
            et.putString("userName", userInfo.getUserName());
            et.putString("avatar", userInfo.getAvatar());
            et.putString("levelName", userInfo.getLevelName());
            et.putString("favoriteGoods", userInfo.getFavoritersGoods());
            et.putString("favoriteStore", userInfo.getFavoritesStore());
            et.putString("key", userInfo.getKey());
            et.commit();
        }
    }

    //读取用户数据
    public static UserInfo readLogin(Context context, boolean isLogined) {
        UserInfo userInfo;
        if (isLogined) {
            userInfo = new UserInfo();
            SharedPreferences sp = context.getSharedPreferences("data", MODE_PRIVATE);
            userInfo.setUserName(sp.getString("userName", ""));
            userInfo.setAvatar(sp.getString("avatar", ""));
            userInfo.setLevelName(sp.getString("levelName", ""));
            userInfo.setFavoritersGoods(sp.getString("favoriteGoods", ""));
            userInfo.setFavoritesStore(sp.getString("favoriteStore", ""));
            userInfo.setKey(sp.getString("key", ""));
        } else {
            userInfo = new UserInfo();
            userInfo.setUserName("点击登录");
            userInfo.setAvatar("");
            userInfo.setLevelName("");
            userInfo.setFavoritersGoods("0");
            userInfo.setFavoritesStore("0");
        }
        return userInfo;
    }

    //清除用户数据
    public static void clearLogin(Context context) {
        SharedPreferences sp = context.getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor et = sp.edit();
        et.remove("loginState");
        et.remove("userName");
        et.remove("avatar");
        et.remove("levelName");
        et.remove("favoriteGoods");
        et.remove("favoriteStore");
        et.remove("key");
        et.commit();
    }
}
