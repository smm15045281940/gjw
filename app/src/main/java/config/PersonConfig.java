package config;

import android.graphics.Color;

/**
 * Created by Administrator on 2017/4/21 0021.
 */

public interface PersonConfig {
    //欢迎页时间（单位：秒）
    public static final int welcome_time = 1;
    //广告页时间（单位：秒）
    public static final int poster_time = 3;
    //轮播切换时间（单位：秒）
    public static final int rotateCut_time = 3;
    //退出提示
    public static final String exitHint = "再次点击退出钢建网";
    //白色
    public static final int white = Color.parseColor("#FFFFFF");
    //登录按钮默认颜色
    public static final int loginBtnDefaultColor = Color.parseColor("#EEEEEE");
    //登录按钮选中颜色
    public static final int loginBtnChooseColor = Color.parseColor("#ED5564");
}
