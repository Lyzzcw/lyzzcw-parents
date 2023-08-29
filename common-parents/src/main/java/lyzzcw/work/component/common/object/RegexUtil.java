package lyzzcw.work.component.common.object;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * @author: lzy
 * Date: 2022/5/5
 * Time: 13:27
 * Description: 正则工具类
 */
@SuppressWarnings("unused")
public class RegexUtil {

    private static final Pattern PATTERN_HTTP_URL = Pattern.compile("\\b((h|H)(t|T)(t|T)(p|P)s?)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]*[-A-Za-z0-9+&@#/%=~_|]");
    private static final Pattern PATTERN_IP = Pattern.compile("^(?=\\d+\\.\\d+\\.\\d+\\.\\d+$)(?:(?:25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\\.?){4}$");
    private static final Pattern PATTERN_EMAIL = Pattern.compile("^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$");
    private static final Pattern PATTERN_MD5 = Pattern.compile("^[a-fA-F0-9]{32}$");
    private static final Pattern PATTERN_MOBILE_PHONE = Pattern.compile("\\+?(9[976]\\d|8[987530]\\d|6[987]\\d|5[90]\\d|42\\d|3[875]\\d|2[98654321]\\d|9[8543210]|8[6421]|6[6543210]|5[87654321]|4[987654310]|3[9643210]|2[70]|7|1)?\\d{1,14}$");
    private static final Pattern PATTERN_PURE_NUMBER = Pattern.compile("^\\d*$");
    private static final Pattern PATTERN_MONTH_yyyy_MM = Pattern.compile("^(19[0-9]{2}|2[0-9]{3})-(0[1-9]|1[012])$");
    private static final Pattern PATTERN_NAME = Pattern.compile("^[a-zA-Z0-9_\u4e00-\u9fa5]+$");


    /**
     * 字符串input是否是http或https类型的url
     *
     * @param pInput 字符串
     * @return 是否符合
     */
    public static boolean isHttpURL(String pInput) {
        Matcher matcher = PATTERN_HTTP_URL.matcher(pInput);
        return matcher.matches();
    }

    /**
     * 字符串input是否是IP地址
     *
     * @param pInput 字符串
     * @return 是否符合
     */
    public static boolean isIPAddress(String pInput) {
        Matcher matcher = PATTERN_IP.matcher(pInput);
        return matcher.matches();
    }

    /**
     * 字符串input是否是邮箱
     *
     * @param pInput 字符串
     * @return 是否符合
     */
    public static boolean isEmail(String pInput) {
        Matcher matcher = PATTERN_EMAIL.matcher(pInput);
        return matcher.matches();
    }

    /**
     * 字符串input是否是
     *
     * @param pInput 字符串
     * @return 是否符合
     */
    public static boolean isMD5(String pInput) {
        Matcher matcher = PATTERN_MD5.matcher(pInput);
        return matcher.matches();
    }

    /**
     * 字符串input是否是手机号码
     *
     * @param pInput 字符串
     * @return 是否符合
     */
    public static boolean isMobilePhoneNumber(String pInput) {
        Matcher matcher = PATTERN_MOBILE_PHONE.matcher(pInput);
        return matcher.matches();
    }

    /**
     * 字符串input是否是纯数字字符串
     *
     * @param pInput 字符串
     * @return 是否符合
     */
    public static boolean isPureNumberString(String pInput) {
        Matcher matcher = PATTERN_PURE_NUMBER.matcher(pInput);
        return matcher.matches();
    }

    /**
     * 字符串input是否是yyyy-MM格式的月份
     * @param pInput 字符串
     * @return 是否符合
     */
    public static boolean isMonth_yyyy_MM(String pInput) {
        Matcher matcher = PATTERN_MONTH_yyyy_MM.matcher(pInput);
        return matcher.matches();
    }

    /**
     * 字符串是否是 汉字数字字母_
     * @param pInput 字符串
     * @return 是否符合
     */
    public static boolean isName(String pInput) {
        Matcher matcher = PATTERN_NAME.matcher(pInput);
        return matcher.matches();
    }

    /**
     * 字符串是账户名形式，仅包含数字、字母或者是邮箱格式
     * @param pInput 字符串
     * @return 是否符合
     */
    public static boolean isAccount(String pInput, int pMin, int pMax) {
        if(pInput != null) {
            int length = pInput.length();
            if (length < pMin || length > pMax) {
                return false;
            }
            Matcher matcher = Pattern.compile("^[a-zA-Z0-9.@_\\-]+$").matcher(pInput);
            return matcher.matches();
        }
        return false;
    }

    public static void main(String[] args){
        System.out.println(isAccount("test哈哈sssss", 4, 20));
        System.out.println(isAccount("testsssss", 4, 20));
        System.out.println(isAccount("test ssss", 4, 20));
        System.out.println(isAccount(" test哈sssss哈", 4, 20));
        System.out.println(isAccount(" test哈sss", 4, 20));
        System.out.println(isAccount("12a", 4, 20));
        System.out.println(isAccount("timememetest", 4, 20));
        System.out.println(isAccount("timememetest12312312123", 4, 20));
        System.out.println(isAccount("xi.qin@genlot.com", 4, 20));
    }
    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
