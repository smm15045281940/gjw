package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 孙明明 on 2017/5/16.
 */

public class RegularUtils {

    //数字
    public static boolean isNumber(String s){
        String regularStr = "[0-9]*";
        Pattern p = Pattern.compile(regularStr);
        Matcher m = p.matcher(s);
        return m.matches();
    }

    //邮箱
    public static boolean isEmail(String s) {
        String regularStr = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(regularStr);
        Matcher m = p.matcher(s);
        return m.matches();
    }

    //手机号
    public static boolean isPhonenumber(String str) {
        String regularStr = "^(13[0-9]|14[57]|15[0-35-9]|17[6-8]|18[0-9])[0-9]{8}$";
        Pattern p = Pattern.compile(regularStr);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    //密码：1.数字或字母大小写
    public static boolean isPassword(String s) {
        String regularStr = "^[a-zA-Z0-9]+$";
        Pattern p = Pattern.compile(regularStr);
        Matcher m = p.matcher(s);
        return m.matches();
    }

    //密码：2.至少包含大小写字母及数字中的一种
    public static boolean isLetterOrNumber(String s){
        boolean isLetterOrDigit = false;
        for(int i=0;i<s.length();i++){
            if(Character.isLetterOrDigit(s.charAt(i))){
                isLetterOrDigit = true;
            }
        }
        String regularStr = "^[a-zA-Z0-9]+$";
        boolean isRight = isLetterOrDigit && s.matches(regularStr);
        return isRight;
    }

    //密码：3.至少包含大小写字母及数字中的两种
    public static boolean isLetterNumber(String s){
        boolean isDigit = false;
        boolean isLetter = false;
        for(int i=0;i<s.length();i++){
            if(Character.isDigit(s.charAt(i))){
                isDigit = true;
            }else if(Character.isLetter(s.charAt(i))){
                isLetter = true;
            }
        }
        String regularStr = "^[a-zA-Z0-9]+$";
        boolean isRight = isDigit && isLetter && s.matches(regularStr);
        return isRight;
    }

    //密码：4.必须同时包含大小写字母及数字
    public static boolean isLetterAndNumber(String s){
        boolean isDigit = false;
        boolean isLowerCase = false;
        boolean isUpperCase = false;
        for(int i=0;i<s.length();i++){
            if(Character.isDigit(s.charAt(i))){
                isDigit = true;
            }else if(Character.isLowerCase(s.charAt(i))){
                isLowerCase = true;
            }else if(Character.isUpperCase(s.charAt(i))){
                isUpperCase = true;
            }
        }
        String regularStr = "^[a-zA-Z0-9]+$";
        boolean isRight = isDigit && isLowerCase && isUpperCase && s.matches(regularStr);
        return isRight;
    }

}
