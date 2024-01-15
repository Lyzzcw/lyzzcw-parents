package lyzzcw.work.component.common.utils;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * @author: lzy
 * Date: 2022/5/5
 * Time: 13:27
 * Description: 参数校验工具
 */
public class VerifyUtil {

    /**
     * 校验身份证
     *
     * @param id
     * @return
     */
    public static boolean verifyCHNID(String id) {

        try {
            boolean result = (boolean) ChinaIDVerify.IDCardValidate(id).get("code");
            return result;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * 根据身份证校验获取年龄
     *
     * @param idCard
     * @return
     */
    public static int getAgeByIdCard(String idCard) {
        int age = 0;
        try {
            Date birthDate = new Date();
            if (idCard.length() == 15) {  //15位身份证
                birthDate = new SimpleDateFormat("yyMMdd").parse(idCard.substring(6, 12));
            }else{
                birthDate = new SimpleDateFormat("yyyyMMdd").parse(idCard.substring(6, 14));
            }
            Calendar cal = Calendar.getInstance();
            if (cal.before(birthDate)) { //出生日期晚于当前时间，无法计算
                return age;
            }
            int yearNow = cal.get(Calendar.YEAR);  //当前年份
            int monthNow = cal.get(Calendar.MONTH);  //当前月份
            int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH); //当前日期
            cal.setTime(birthDate);
            int yearBirth = cal.get(Calendar.YEAR);
            int monthBirth = cal.get(Calendar.MONTH);
            int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
            age = yearNow - yearBirth;   //计算整岁数
            if (monthNow <= monthBirth) {
                if (monthNow == monthBirth) {
                    if (dayOfMonthNow < dayOfMonthBirth) {
                        age--;//当前日期在生日之前，年龄减一
                    }
                } else {
                    age--;//当前月份在生日之前，年龄减一
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return age;
    }

    /**
     * 校验手机号
     *
     * @param mobile
     * @return
     */
    public static boolean verifyCHNMobile(String mobile) {
        if (mobile.length() != 11) {
            return false;
        } else {
            String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(mobile);
            boolean isMatch = m.matches();
            return isMatch;
        }
    }

    /**
     * 校验银行卡卡号
     * 校验过程：
     * 1、从卡号最后一位数字开始，逆向将奇数位(1、3、5等等)相加。
     * 2、从卡号最后一位数字开始，逆向将偶数位数字，先乘以2（如果乘积为两位数，将个位十位数字相加，即将其减去9），再求和。
     * 3、将奇数位总和加上偶数位总和，结果应该可以被10整除。
     */
    public static boolean checkBankCard(String bankCard) {
        if(bankCard.length() < 15 || bankCard.length() > 19) {
            return false;
        }
        char bit = getBankCardCheckCode(bankCard.substring(0, bankCard.length() - 1));
        if(bit == 'N'){
            return false;
        }
        return bankCard.charAt(bankCard.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     * @param nonCheckCodeBankCard
     * @return
     */
    private static char getBankCardCheckCode(String nonCheckCodeBankCard){
        if(nonCheckCodeBankCard == null || nonCheckCodeBankCard.trim().length() == 0
                || !nonCheckCodeBankCard.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeBankCard.trim().toCharArray();
        int luhmSum = 0;
        for(int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if(j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char)((10 - luhmSum % 10) + '0');
    }

    /**
     * 校验对象指定参数不能为空
     * @param obj
     * @param paramMap
     * @throws IllegalAccessException
     */
    public static void checkObjFieldIsNull(Object obj, Map<String,String> paramMap) throws Exception {
        for(Field f : obj.getClass().getDeclaredFields()){
            f.setAccessible(true);
            String name = f.getName();
            if(paramMap.containsKey(name)&& StringUtil.formatString(f.get(obj)).equals("")){
                throw new RuntimeException(paramMap.get(name)+"不能为空！");
            }
        }
    }

    /**
     * 校验Map参数不能为空
     * @param paramObj
     * @param paramMap
     * @throws IllegalAccessException
     */
    public static void checkMapFieldIsNull(Map<String,Object> paramObj, Map<String,String> paramMap) throws Exception {
        for(Map.Entry<String,String> entry : paramMap.entrySet()){
            String name = entry.getKey();
            if(StringUtil.formatString(paramObj.get(name)).equals("")){
                throw new RuntimeException(paramMap.get(name)+"["+name+"]不能为空！");
            }
        }
    }

    public static void main(String[] args) throws ParseException {
        String id = "1381254874";
        System.out.println(verifyCHNID("110109198709121032"));
    }
}
