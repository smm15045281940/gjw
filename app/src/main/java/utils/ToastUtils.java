package utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/5/19.
 */

public class ToastUtils {

    public static void toast(Context context, String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    public static void log(Context context, String s) {
        Log.e(context.getClass().getSimpleName(), s);
    }
}
