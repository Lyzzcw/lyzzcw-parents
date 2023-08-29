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

}
