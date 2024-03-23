package lyzzcw.work.component.common.math;

/**
 * Created with IntelliJ IDEA.
 * @author: lzy
 * Date: 2022/5/5
 * Time: 13:44
 * Description: No Description
 */
public class NumUtil {
    /**
     * 将以字母表示的Excel列数转换成数字表示
     *
     * @param letter
     *            以字母表示的列数，不能为空且只允许包含字母字符
     * @return 返回转换的数字，转换失败返回-1
     */
    public static int letterToNumber(String letter) {
        // 检查字符串是否为空
        if (letter == null || letter.isEmpty()) {
            return -1;
        }
        String upperLetter = letter.toUpperCase(); // 转为大写字符串
        if (!upperLetter.matches("[A-Z]+")) { // 检查是否符合，不能包含非字母字符
            return -1;
        }
        long num = 0; // 存放结果数值
        long base = 1;
        // 从字符串尾部开始向头部转换
        for (int i = upperLetter.length() - 1; i >= 0; i--) {
            char ch = upperLetter.charAt(i);
            num += (ch - 'A' + 1) * base;
            base *= 26;
            if (num > Integer.MAX_VALUE) { // 防止内存溢出
                return -1;
            }
        }
        return (int) num;
    }

    /**
     * 将数字转换成以字母表示的Excel列数
     *
     * @param num
     *            表示列数的数字
     * @return 返回转换的字母字符串，转换失败返回null
     */
    public static String numberToLetter(int num) {
        if (num <= 0) { // 检测列数是否正确
            return null;
        }
        StringBuffer letter = new StringBuffer();
        do {
            --num;
            int mod = num % 26; // 取余
            letter.append((char) (mod + 'A')); // 组装字符串
            num = (num - mod) / 26; // 计算剩下值
        } while (num > 0);
        return letter.reverse().toString(); // 返回反转后的字符串
    }

    /**
     * 换算 num 到 cn
     *
     * @param num 数字
     * @return {@link String}
     */
    public static String convertNumToCN(Long num){
        String [] nums ={"一","二","三","四","五","六","七","八","九"};
        //由于int 类型的占4个字节，所以正整数的范围为 21 4748 3647 ，所以采用 long类型 ，可以转到 千 亿
        String []unit = {"","十","百","千","万","十","百","千","亿","十","百","千"};
        //先将数字转为字符串
        String numStr = String.valueOf(num);
        //将该字符串numStr 存到字符数组中
        char[] chars = numStr.toCharArray();
        //获取该字符数组的长度
        int length = chars.length;
        //定义接获凭借的字符串
        String str = "";
        for (int i = 0; i < length; i++) {
            char cha = chars[i];
            // 将字符 转为 int 类型
            int c = cha - '0';
            //如果数字是就不用处理（不用凭借中文数字字符和中文数字单位）
            //c 不是 0，中文数字： nums[c - 1]
            //         中文单位：unit[length - c - 1]
            if (c != 0){
                str += nums[c - 1] + unit[length - i - 1];
            }
        }
        return str;
    }

}
