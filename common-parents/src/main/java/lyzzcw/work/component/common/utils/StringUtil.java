package lyzzcw.work.component.common.utils;

import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Encoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lz
 * @date 2022-07-18 13:55
 * 字符串工具类
 */
@Slf4j
@SuppressWarnings({ "unused", "WeakerAccess" })
public class StringUtil {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter &amp; Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	/**
	 * 获取字符串长度
	 *
	 * @param pString 字符串
	 * @return 长度
	 */
	public static int length(String pString) {
		if (isEmpty(pString)) {
			return 0;
		}
		return pString.length();
	}

	/**
	 * 判断字符串是否为空
	 *
	 * @param pString 字符串
	 * @return 是否为空
	 */
	public static boolean isEmpty(Object pString) {
		return pString == null || "".equals(pString);
	}

	/**
	 * 字符集转换
	 *
	 * @param pString 字符串
	 * @param pFrom   原编码
	 * @param pTo     转化编码
	 * @return 转化结果
	 */
	public static String iconv(String pString, String pFrom, String pTo) {
		pString = pString == null ? "" : pString;
		if (pString.length() > 0) {
			try {
				return new String(pString.getBytes(pFrom), pTo);
			} catch (Exception e) {
				e.printStackTrace();
				return pString;
			}
		}
		return pString;
	}

	/**
	 * 将ISO-8859-1转换为GBK
	 *
	 * @param pString 字符串
	 * @return 转化结果
	 */
	public static String ISOtoGBK(String pString) {
		return iconv(pString, "ISO-8859-1", "GBK");
	}

	/**
	 * 将GBK转换为ISO-8859-1
	 *
	 * @param pString 字符串
	 * @return 转化结果
	 */
	public static String GBKtoISO(String pString) {
		return iconv(pString, "GBK", "ISO-8859-1");
	}

	/**
	 * 将ISO-8859-1转换为UTF-8
	 *
	 * @param pString 字符串
	 * @return 转化结果
	 */
	public static String ISOtoUTF(String pString) {
		return iconv(pString, "ISO-8859-1", "UTF-8");
	}

	/**
	 * 将UTF-8转换为ISO-8859-1
	 *
	 * @param pString 字符串
	 * @return 转化结果
	 */
	public static String UTFtoISO(String pString) {
		return iconv(pString, "UTF-8", "ISO-8859-1");
	}

	/**
	 * 将GBK字符串转换为UTF-8
	 *
	 * @param pString 字符串
	 * @return 转化结果
	 */
	public static String GBKtoUTF(String pString) {
		String l_temp = GBKToUnicode(pString);
		l_temp = UnicodeToUTF(l_temp);

		return l_temp;
	}

	/**
	 * 将UTF-8字符串转换为GBK
	 *
	 * @param pString 字符串
	 * @return 转化结果
	 */
	public static String UTFtoGBK(String pString) {
		String l_temp = UTFtoUnicode(pString);
		l_temp = UnicodeToGBK(l_temp);

		return l_temp;
	}

	/**
	 * 将GBK字符串转换为Unicode
	 *
	 * @param pString 字符串
	 * @return 转化结果
	 */
	public static String GBKToUnicode(String pString) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < pString.length(); i++) {
			char chr1 = pString.charAt(i);

			if (!isNeedConvert(chr1)) {
				result.append(chr1);
				continue;
			}

			result.append("\\u").append(Integer.toHexString((int) chr1));
		}

		return result.toString();
	}

	/**
	 * 将Unicode字符串转换为GBK
	 *
	 * @param pString 字符串
	 * @return 转化结果
	 */
	public static String UnicodeToGBK(String pString) {
		int index = 0;
		StringBuilder buffer = new StringBuilder();

		int li_len = pString.length();
		while (index < li_len) {
			if (index >= li_len - 1 || !"\\u".equals(pString.substring(index, index + 2))) {
				buffer.append(pString.charAt(index));

				index++;
				continue;
			}

			String charStr = pString.substring(index + 2, index + 6);

			char letter = (char) Integer.parseInt(charStr, 16);

			buffer.append(letter);
			index += 6;
		}

		return buffer.toString();
	}

	private static boolean isNeedConvert(char para) {
		return ((para & (0x00FF)) != para);
	}

	/**
	 * UTF-8 转 Unicode
	 *
	 * @param pString 字符串
	 * @return 转化结果
	 */
	public static String UTFtoUnicode(String pString) {
		char[] myBuffer = pString.toCharArray();

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < pString.length(); i++) {
			Character.UnicodeBlock ub = Character.UnicodeBlock.of(myBuffer[i]);
			if (ub == Character.UnicodeBlock.BASIC_LATIN) {
				sb.append(myBuffer[i]);
			} else if (ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
				int j = (int) myBuffer[i] - 65248;
				sb.append((char) j);
			} else {
				short s = (short) myBuffer[i];
				String hexS = Integer.toHexString(s);
				String unicode = "\\u" + (hexS.length() > 4 ? hexS.substring(4) : hexS);
				sb.append(unicode.toLowerCase(Locale.getDefault()));
			}
		}
		return sb.toString();
	}

	/**
	 * Unicode 转 UTF-8
	 *
	 * @param pString 字符串
	 * @return 转化结果
	 */
	public static String UnicodeToUTF(String pString) {
		char aChar;
		int len = pString.length();
		StringBuilder outBuffer = new StringBuilder(len);
		for (int x = 0; x < len;) {
			aChar = pString.charAt(x++);
			if (aChar == '\\') {
				aChar = pString.charAt(x++);
				if (aChar == 'u') {
					// Read the xxxx
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = pString.charAt(x++);
						switch (aChar) {
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
						}
					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't'){
						aChar = '\t';
					}
					else if (aChar == 'r') {
						aChar = '\r';
					} else if (aChar == 'n') {
						aChar = '\n';
					} else if (aChar == 'f') {
						aChar = '\f';
					}
					outBuffer.append(aChar);
				}
			} else {
				outBuffer.append(aChar);
			}
		}
		return outBuffer.toString();
	}

	/**
	 * 获取Unicode编码
	 *
	 * @param pString 字符串
	 * @return 编码结果
	 */
	public static String toUnicode(String pString) {
		StringBuilder sb = new StringBuilder();
		if (pString == null || "".equals(pString)) {
			return "";
		}
		for (int i = 0; i < pString.length(); i++) {
			if ((pString.charAt(i) >= 0x4e00) && (pString.charAt(i) <= 0x9fbb)) {
				sb.append("\\u").append(Integer.toHexString(pString.charAt(i)));
			} else {
				sb.append(pString.charAt(i));
			}
		}
		return sb.toString();
	}

	/**
	 * 生成指定长度的随机字符串
	 *
	 * @param pLength 字符串长度
	 * @return 随机字符串
	 */
	public static String getRandomString(int pLength) { // length表示生成字符串的长度
		String base = "abcdefghijklmnopqrstuvwxyz0123456789!@#$%&*";
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < pLength; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	/**
	 * 生成22位长度BASE64SafeString 根据UUID生成，存在一定重复几率 可以用来批量生成兑换码
	 *
	 * @return 22位字符串
	 */
	public static String generateBase64SafeFromUUID() {
		UUID uuid = UUID.randomUUID();
		ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
		bb.putLong(uuid.getMostSignificantBits());
		bb.putLong(uuid.getLeastSignificantBits());
		String result = new BASE64Encoder().encode(bb.array());

		Pattern p = Pattern.compile("[A-Za-z0-9]{22}");
		Matcher m = p.matcher(result);

		if (!m.find()) {
			return generateBase64SafeFromUUID();
		}
		return result.toUpperCase(Locale.getDefault());
	}

	/**
	 * 生成UUID，过滤-符号，可以用来做唯一KEY，重复几率甚微
	 *
	 * @return UUID
	 */
	public static String generateUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replace("-", "");
	}

	/**
	 * <br/>
	 * 通用签名计算 <br/>
	 * 按照 (pHeadKey@notNull)+pParams(key=value join)+(pTailKey@notNull)
	 * 拼接字符串，+为pSeparator <br/>
	 * 拼接字符串以MD5 小写形式输出
	 * 
	 * @param pParams    参数 TreeMap 会自动按ASCII码排序
	 * @param pSeparator 连接符
	 * @param pHeadKey   参数字符串头部KEY
	 * @param pTailKey   参数字符串尾部KEY
	 * @return 小写MD5签名
	 */
	public static String makeSignCommon(TreeMap<String, String> pParams, String pSeparator, String pHeadKey,
			String pTailKey) {
		StringBuilder s = new StringBuilder();
		if (!isEmpty(pHeadKey)) {
			s.append(pHeadKey).append(pSeparator);
		}
		for (String key : pParams.keySet()) {
			if (isEmpty(pParams.get(key))) {
				continue;
			}
			if (s.length() > 0) {
				s.append(pSeparator);
			}
			s.append(key).append("=").append(pParams.get(key));
		}
		if (!isEmpty(pTailKey)) {
			s.append(pSeparator).append(pTailKey);
		}

		log.info("Sign String [%s]", s.toString());

		return MD5.getMD5(s.toString());
	}

	/**
	 * 获取XML中某个节点中的内容
	 * 
	 * @param pXML  XML文本
	 * @param pNode 节点TAG
	 * @return 该节点中的内容
	 */
	public static String getXMLValue(String pXML, String pNode) {
		if (pXML != null && pNode != null) {
			Pattern pattern = Pattern.compile("<" + pNode + "><!\\[CDATA\\[(.*?)]]></" + pNode + ">");
			Matcher matcher = pattern.matcher(pXML);
			if (matcher.find()) {
				return matcher.group(1);
			} else {
				pattern = Pattern.compile("<" + pNode + ">(.*?)</" + pNode + ">");
				matcher = pattern.matcher(pXML);
				if (matcher.find()) {
					return matcher.group(1);
				}
			}
		}
		return "";
	}

	/**
	 * 获取XML中某个节点中的内容
	 * 
	 * @param pXML  XML文本
	 * @param pNode 节点TAG
	 * @return 该节点中的内容
	 */
	public static List<String> getXMLValues(String pXML, String pNode) {
		ArrayList<String> resList = new ArrayList<>();
		if (pXML != null && pNode != null) {
			Pattern pattern = Pattern.compile("<" + pNode + "><!\\[CDATA\\[(.*?)]]></" + pNode + ">");
			Matcher matcher = pattern.matcher(pXML);
			while (matcher.find()) {
				resList.add(matcher.group(1));
			}
		}
		return resList;
	}

	/**
	 * 使用指定正则式匹配字符串，获取第一个匹配组内容
	 *
	 * @param pRegex  正则式，需要包含组，否则返回空字符
	 * @param pOrigin 字符串
	 * @return 匹配成功时的第一组内容
	 */
	public static String getFirstMatchGroup(String pRegex, String pOrigin) {
		if (!isEmpty(pRegex) && !isEmpty(pOrigin)) {
			Pattern pattern = Pattern.compile(pRegex);
			Matcher matcher = pattern.matcher(pOrigin);
			if (matcher.find() && matcher.groupCount() > 0) {
				return matcher.group(1);
			}
		}
		return "";
	}

	/**
	 * 将为null的字符串处理为""
	 * 
	 * @param pValue 处理的值
	 * @return 处理之后的值
	 */
	public static String formatString(Object pValue) {
		if (isEmpty(pValue)) {
			return "";
		}
		return String.valueOf(pValue).trim();
	}

	/**
	 * 为null的字符串处理,可以设置默认值为defaultValue
	 * 
	 * @param pValue       处理的值
	 * @param defaultValue 默认值
	 * @return 处理后的值
	 */
	public static String formatString(Object pValue, String defaultValue) {
		if (isEmpty(pValue)) {
			return defaultValue;
		}
		return String.valueOf(pValue).trim();
	}

	/**
	 * 如果对象为空，转换为空字符串;如果不为空，去掉前后空格
	 *
	 * @param obj
	 * @return
	 */
	public static String convertObject(Object obj) {
		if (obj == null || "null".equals(obj)) {
			return "";
		} else {
			return obj.toString().trim();
		}
	}

	public static byte[] getBytes(String str) {
		if (null == str) {
			return null;
		} else {
			return str.getBytes();
		}
	}

	public static String toString(byte[] bytes) {
		if (bytes == null) {
			return null;
		}
		String res = new String(bytes);
		return res;
	}

	/**
	 * 生成四位验证码
	 * 
	 * @return
	 */
	public static String createCode() {
		Random random = new Random();
		StringBuffer randomCode = new StringBuffer();
		int red = 0, green = 0, blue = 0;
		for (int i = 0; i < 4; i++) {
			String strRand = String.valueOf(random.nextInt(10));
			randomCode.append(strRand);
		}
		return randomCode.toString();
	}

	/**
	 * 文件上传 获得随机数
	 * 
	 * @return
	 */
	public static String getRandom() {
		Double douRandom = Math.random() * 10000000;
		String strRandom = douRandom.toString();
		return strRandom.substring(0, 5).replace(".", "");
	}

	/**
	 * 默认的空值
	 */
	public static final String EMPTY = "";

	/**
	 * 检查字符串是否为空
	 * 
	 * @param str 字符串
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (str == null) {
			return true;
		} else if (str.length() == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 检查字符串是否为空
	 * 
	 * @param str 字符串
	 * @return
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	/**
	 * 截取并保留标志位之前的字符串
	 * 
	 * @param str  字符串
	 * @param expr 分隔符
	 * @return
	 */
	public static String substringBefore(String str, String expr) {
		if (isEmpty(str) || expr == null) {
			return str;
		}
		if (expr.length() == 0) {
			return EMPTY;
		}
		int pos = str.indexOf(expr);
		if (pos == -1) {
			return str;
		}
		return str.substring(0, pos);
	}

	/**
	 * 截取并保留标志位之后的字符串
	 * 
	 * @param str  字符串
	 * @param expr 分隔符
	 * @return
	 */
	public static String substringAfter(String str, String expr) {
		if (isEmpty(str)) {
			return str;
		}
		if (expr == null) {
			return EMPTY;
		}
		int pos = str.indexOf(expr);
		if (pos == -1) {
			return EMPTY;
		}
		return str.substring(pos + expr.length());
	}

	/**
	 * 截取并保留最后一个标志位之前的字符串
	 * 
	 * @param str  字符串
	 * @param expr 分隔符
	 * @return
	 */
	public static String substringBeforeLast(String str, String expr) {
		if (isEmpty(str) || isEmpty(expr)) {
			return str;
		}
		int pos = str.lastIndexOf(expr);
		if (pos == -1) {
			return str;
		}
		return str.substring(0, pos);
	}

	/**
	 * 截取并保留最后一个标志位之后的字符串
	 * 
	 * @param str
	 * @param expr 分隔符
	 * @return
	 */
	public static String substringAfterLast(String str, String expr) {
		if (isEmpty(str)) {
			return str;
		}
		if (isEmpty(expr)) {
			return EMPTY;
		}
		int pos = str.lastIndexOf(expr);
		if (pos == -1 || pos == (str.length() - expr.length())) {
			return EMPTY;
		}
		return str.substring(pos + expr.length());
	}

	/**
	 * 把字符串按分隔符转换为数组
	 * 
	 * @param string 字符串
	 * @param expr   分隔符
	 * @return
	 */
	public static String[] stringToArray(String string, String expr) {
		return string.split(expr);
	}

	/**
	 * 去除字符串中的空格
	 * 
	 * @param str
	 * @return
	 */
	public static String noSpace(String str) {
		str = str.trim();
		str = str.replace(" ", "_");
		return str;
	}

	/**
	 * 将CLOB数据转化成STRING型
	 * 
	 * @param clob
	 * @return String
	 */
	public static String clobToString(Clob clob) {
		StringBuffer sbResult = new StringBuffer();
		Reader isClob = null;
		if (clob != null) {
			try {
				isClob = clob.getCharacterStream();
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
			BufferedReader bfClob = new BufferedReader(isClob);
			String strClob = "";
			try {
				strClob = bfClob.readLine();
				while (strClob != null) {
					sbResult.append(strClob + "\n");
					strClob = bfClob.readLine();
				}
				bfClob.close();
				isClob.close();
			} catch (IOException ex1) {
			}
		}
		return sbResult.toString();
	}

	/**
	 * 将text型转化成html
	 * 
	 * @param sourcestr
	 * @return String
	 */
	public static String TextToHtml(String sourcestr) {
		int strlen;
		String restring = "", destr = "";
		strlen = sourcestr.length();
		for (int i = 0; i < strlen; i++) {
			char ch = sourcestr.charAt(i);
			switch (ch) {
			case '<':
				destr = "&lt;";
				break;
			case '>':
				destr = "&gt;";
				break;
			case '\"':
				destr = "&quot;";
				break;
			case '&':
				destr = "&amp;";
				break;
			case '\n':
				destr = "<br>";
				break;
			case '\r':
				destr = "<br>";
				break;
			case 32:
				destr = "&nbsp;";
				break;
			default:
				destr = "" + ch;
				break;
			}
			restring = restring + destr;
		}
		return "" + restring;
	}

	/**
	 * 验证是否是数字
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 字符转化为Long
	 */
	public static long parseLong(Object obj) {
		Long value = new Long(0);
		if (obj != null) {
			value = Long.valueOf(toString(obj));
		}
		return value;
	}

	/**
	 * 字符转化为Integer
	 */
	public static Integer parseInt(Object obj) {
		Integer value = new Integer(0);
		if (obj != null) {
			value = Integer.valueOf(toString(obj));
		}
		return value;
	}

	public static String toString(Object obj) {
		if (null == obj) {
			return "";
		} else {
			return obj.toString();
		}
	}

	

	public static void main(String[] args) {
		System.out.println(getFirstMatchGroup("/user/attr/(.+)/history", "/user/attr/exp/history"));
		System.out.println(getFirstMatchGroup("/user/attr/.+/history", "/user/attr/history"));

		System.out.println(generateUUID());
		System.out.println(generateUUID());
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
