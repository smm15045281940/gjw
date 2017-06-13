package application;

import android.app.Application;

import org.xutils.x;

/**
 * Created by 孙明明 on 2017/5/16.
 */

public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
    }
}
