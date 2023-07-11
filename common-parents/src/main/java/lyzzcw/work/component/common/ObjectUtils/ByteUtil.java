package lyzzcw.work.component.common.ObjectUtils;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * @author: lzy
 * Date: 2022/4/28
 * Time: 11:34
 * Description: 字节处理工具类
 */
public class ByteUtil {
	/**
	 * 获取字节的第几位
	 */
	public static int posValue(byte by, int pos) {
		int num = by >> pos;
		num = num & 1;
		return num;
	}

	/**
	 * 字节反转
	 */
	public static byte[] reverse(byte[] by) {
		byte[] rby = new byte[by.length];
		for (int i = 0, len = rby.length; i < len; i++) {
			rby[i] = by[len - i - 1];
		}
		return rby;
	}

	/**
	 * 字符串字节反转且高低位互换
	 * ABCD 4字节转换为 CDBA
	 * ABCDEF 6字节转换为 EFCDBA
	 */
	public static String reverseString(String string) {
		if (string == null) {
			return null;
		}
		int length = string.length();
		if((length < 2) || (length%2 != 0))
			return string;
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < length - 1; i = i+2 ) {
			stringBuffer.append(string.substring(length-2-i, length-i));
		}
		return stringBuffer.toString();
	}
	
	/**
	 * 二位字节转成int类型
	 * 
	 * @param bl
	 *            最低位
	 * @param bh
	 *            最高位
	 * @return
	 */
	public static int l2ByteToInt(byte bl, byte bh) {
		int s = 0;
		int s0 = bl & 0xff;// 最低位
		int s1 = bh & 0xff;
		s1 <<= 8;
		s = s0 | s1;
		return s;
	}

	/**
	 * Int转成字节，高位在前（二个字节）
	 * 
	 * @param res
	 * @return
	 */
	public static byte[] intToL2HByte(int res) {
		byte[] targets = new byte[2];
		targets[1] = (byte) (res & 0xff);
		targets[0] = (byte) ((res >> 8) & 0xff);
		return targets;
	}

	/**
	 * Int转成字节，低位在前（二个字节）
	 * 
	 * @param res
	 * @return
	 */
	public static byte[] intToL2Byte(int res) {
		byte[] targets = new byte[2];
		targets[0] = (byte) (res & 0xff);
		targets[1] = (byte) ((res >> 8) & 0xff);
		return targets;
	}

	/**
	 * 求传入支付串的模的256 --将每两个字符 转换成 十进制 相加 转换为二进制 ，从右向左序数大于八的 去掉 转换为 十六进制
	 * 
	 * @param hex
	 *            出入的字符串
	 * @return
	 */
	public static String Hex256model(String hex) {
		String[] s = new String[hex.length() / 2];
		int index = 0;
		for (int k = 0; k < hex.length(); k += 2) {
			s[index] = hex.substring(k, k + 2);
			index++;
		}
		int sum = 0;
		for (int k = 0; k < s.length; k++) {
			int num3 = Integer.parseInt(s[k], 16);
			sum = sum + num3;
		}
		String binary = Integer.toBinaryString(sum);
		int blength = binary.length();

		binary = blength > 8 ? binary.substring(blength - 8) : binary;
		return binaryString2hexString(binary).toUpperCase();
	}

	public static String binaryString2hexString(String bString) {
		if (bString == null || bString.equals("") || bString.length() % 8 != 0)
			return null;
		StringBuffer tmp = new StringBuffer();
		int iTmp = 0;
		for (int i = 0; i < bString.length(); i += 4) {
			iTmp = 0;
			for (int j = 0; j < 4; j++) {
				iTmp += Integer.parseInt(bString.substring(i + j, i + j + 1)) << (4 - j - 1);
			}
			tmp.append(Integer.toHexString(iTmp));
		}
		return tmp.toString();
	}

	// bcd

	/**
	 * BCD码转为10进制串(阿拉伯数据)
	 * 
	 * @param bytes(BCD码)
	 * @return 10进制串
	 */
	public static String bcd2Str(byte[] bytes) {
		StringBuffer temp = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			temp.append((byte) ((bytes[i] & 0xf0) >>> 4));
			temp.append((byte) (bytes[i] & 0x0f));
		}
		return temp.toString();
	}
	
	/**
	 * 10进制串转为BCD码
	 * @param asc 10进制串
	 * @return  BCD码
	 */
	public static byte[] str2Bcd(String asc) {
		int len = asc.length();
		int mod = len % 2;
		if (mod != 0) {
			asc = "0" + asc;
			len = asc.length();
		}
		byte abt[] = new byte[len];
		if (len >= 2) {
			len = len / 2;
		}
		byte bbt[] = new byte[len];
		abt = asc.getBytes();
		int j, k;
		for (int p = 0; p < asc.length() / 2; p++) {
			if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
				j = abt[2 * p] - '0';
			} else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
				j = abt[2 * p] - 'a' + 0x0a;
			} else {
				j = abt[2 * p] - 'A' + 0x0a;
			}
			if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
				k = abt[2 * p + 1] - '0';
			} else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
				k = abt[2 * p + 1] - 'a' + 0x0a;
			} else {
				k = abt[2 * p + 1] - 'A' + 0x0a;
			}
			int a = (j << 4) + k;
			byte b = (byte) a;
			bbt[p] = b;
		}
		return bbt;
	}
	
	/**
	 * 转字节转成long类型 四字数组，高位在前
	 * @param b byte[]数组
	 * @return long
	 */
	public static long l4ByteHToLong(byte[] b) {
		long s = 0;
		long s0 = b[3] & 0xff;
		long s1 = b[2] & 0xff;
		long s2 = b[1] & 0xff;
		long s3 = b[0] & 0xff;
		// s0不变
		s1 <<= 8;
		s2 <<= 16;
		s3 <<= 24;
		
		s = s0 | s1 | s2 | s3;
		return s;
	}
	
	/**
	 * 转字节转成long类型 四字数组，低位在前
	 * @param b byte[]数组
	 * @return long
	 */
	public static long l4ByteToLong(byte[] b) {
		long s = 0;
		long s0 = b[0] & 0xff;
		long s1 = b[1] & 0xff;
		long s2 = b[2] & 0xff;
		long s3 = b[3] & 0xff;
		// s0不变
		s1 <<= 8;
		s2 <<= 16;
		s3 <<= 24;
		
		s = s0 | s1 | s2 | s3;
		return s;
	}
	
	/**
	 * long转成字节，高位在前（四个字节）
	 * @param res
	 * @return
	 */
	public static byte[] longToL4HByte(long res) {
		byte[] targets = new byte[4];
		targets[3] = (byte) (res & 0xff);
		targets[2] = (byte) ((res >> 8) & 0xff);
		targets[1] = (byte) ((res >> 16) & 0xff);
		targets[0] = (byte) (res >>> 24);
		return targets; 
	}
	
	/**
	 * 字符串转二进制
	 * @param str 特定的二进制字符串
	 * @return 返回编码过后的字符串
	 */
	public static byte[] hexToByte(String str) {
		if (str == null)
			return null;
		str = str.trim();
		int len = str.length();
		if (len == 0 || len % 2 == 1)
			return new byte[]{};
		byte[] b = new byte[len / 2];
		try {

			for (int i = 0; i < str.length(); i += 2) {

				b[i / 2] = (byte) Integer
						.decode("0x" + str.substring(i, i + 2)).intValue();

			}
			return b;
		} catch (Exception e) {
			e.printStackTrace();
			return null;

		}
	}

	/**
	 * 字节转Hex字符串
	 * @param bts 字节数组
	 * @return 返回Hex字符串
	 */
	public static String bytesToHex(byte[] bts) {
		String des = "";
		String tmp = null;

		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des += "0";
			}
			des += tmp;
		}
		return des;
	}
	
	/**
	 * 转字节转成long类型 低位在前（八个字节）
	 * @param b byte[]数组
	 * @return long
	 */
	public static long byteToLong(byte[] b) {
		long s = 0;
		long s0 = b[0] & 0xff;// 最低位
		long s1 = b[1] & 0xff;
		long s2 = b[2] & 0xff;
		long s3 = b[3] & 0xff;
		long s4 = b[4] & 0xff;// 最低位
		long s5 = b[5] & 0xff;
		long s6 = b[6] & 0xff;
		long s7 = b[7] & 0xff;

		// s0不变
		s1 <<= 8;
		s2 <<= 16;
		s3 <<= 24;
		s4 <<= 8 * 4;
		s5 <<= 8 * 5;
		s6 <<= 8 * 6;
		s7 <<= 8 * 7;
		s = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;
		return s;
	}

	/**
	 * 转字节转成int类型，低位在前（四个字节）
	 * @param b byte[]数组
	 * @return int
	 */
	public static int byteToInt(byte[] b) {
		int s = 0;
		int s0 = b[0] & 0xff;// 最低位
		int s1 = b[1] & 0xff;
		int s2 = b[2] & 0xff;
		int s3 = b[3] & 0xff;
		s3 <<= 24;
		s2 <<= 16;
		s1 <<= 8;
		s = s0 | s1 | s2 | s3;
		return s;
	}
	
	/**
	 * 转字节转成int类型 高位在前(四个字节)
	 * @param b byte[]数组
	 * @return int
	 */
	public static int byteHToInt(byte[] b) {
		int s = 0;
		int s0 = b[3] & 0xff;// 最低位
		int s1 = b[2] & 0xff;
		int s2 = b[1] & 0xff;
		int s3 = b[0] & 0xff;
		s3 <<= 24;
		s2 <<= 16;
		s1 <<= 8;
		s = s0 | s1 | s2 | s3;
		return s;
	}
	
	/**
	 * byte 转成Int
	 * @param b
	 * @return
	 */
	public static int byteToInt(byte b){
		return b & 0xff;
	}
	
	/**
	 * Int转成字节，低位在前（四个字节）
	 * @param res
	 * @return
	 */
	public static byte[] intToByte(int res) {
		byte[] targets = new byte[4];
		targets[0] = (byte) (res & 0xff);// 最低位 
		targets[1] = (byte) ((res >> 8) & 0xff);// 次低位 
		targets[2] = (byte) ((res >> 16) & 0xff);// 次高位 
		targets[3] = (byte) (res >>> 24);// 最高位,无符号右移。 
		return targets; 
	}
	
	/**
	 * long类型转成byte数组 。低位在前。
	 * @param number
	 * @return
	 */
	public static byte[] longToByte(long number){
		long temp = number;
		byte[] b = new byte[8];
		for (int i = 0; i < b.length; i++) {
			b[i] = new Long(temp & 0xff).byteValue();//将最低位保存在最低位 
			temp = temp >> 8; // 向右移8位
		}
		return b;
	}
	
	/**
	 * 拷贝字符数组
	 * @param src 源
	 * @param srcPos 源开始位置
	 * @param dest 目标
	 * @param destPos 目标开始位置
	 * @param length 长度
	 * @return
	 */
	public static byte[] copyBytes(byte[] src,int  srcPos,byte[] dest, int destPos, int length){
		System.arraycopy(src, srcPos, dest, destPos, length);
		return dest;
	}
	
	/**
	 * 合并数组
	 * @param first
	 * @param rest
	 * @return
	 */
	public static byte[] concatAll(byte[] first, byte[]... rest) {
		int totalLength = first.length;
		for (byte[] array : rest) {
			totalLength += array.length;
		}
		byte[] result = Arrays.copyOf(first, totalLength);
		int offset = first.length;
		for (byte[] array : rest) {
			System.arraycopy(array, 0, result, offset, array.length);
			offset += array.length;
		}
		return result;
	}
	
	/**
	 * 比较两个字节的内容是否相同
	 * @return
	 */
	public static boolean compare(byte[] by1 , byte[] by2){
		if(by1.length==by2.length){
			for(int i=0,len=by1.length;i<len;i++){
				if(by1[i]!=by2[i]){
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 转字节转成二进制
	 * @param b
	 * @return
	 */
	public static String byteToBits(byte b) {
		int z = b;
		z |= 256;
		String str = Integer.toBinaryString(z);
		int len = str.length();
		return str.substring(len - 8, len);
	}

	/**
	 * 将二进制字符串转换回字节
	 * @param bString
	 * @return
	 */
	public static byte bitsToByte(String bString) {
		byte result = 0;
		for (int i = bString.length() - 1, j = 0; i >= 0; i--, j++) {
			result += (Byte.parseByte(bString.charAt(i) + "") * Math.pow(2, j));
		}
		return result;
	}
	
	public static String string2Hex(String str){
		char[] chars = "0123456789ABCDEF".toCharArray();
		StringBuffer sb = new StringBuffer();
		byte[] bs = null;
		try {
			bs = str.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		int bit;
		for(int i = 0; i < bs.length; i++){
			bit = (bs[i] & 0x0f0) >> 4;
			sb.append(chars[bit]);
			bit = bs[i] & 0x0f;
			sb.append(chars[bit]);
		}
		return sb.toString();
	}
	/**
	 * 字节数组转为ASCII字符串
	 * @param bs
	 * @return
	 */
	public static String bytesToAsciiString(byte[] bs){
		StringBuffer str=new StringBuffer("");
		for(int i=0;i<bs.length;i++){
			char a=(char) Integer.parseInt(String.valueOf(bs[i]));
			str.append(a);
		}
		return str.toString();
	}
	/**
	 * 字符串转为ASCII，再转为16进制字符串(12---->3132)
	 * @param str
	 * @return
	 */
	public static String getAscii(String str){
		if(StringUtils.isEmpty(str)){
			return null;
		}
		char[] ch=str.toCharArray();
		StringBuffer sb=new StringBuffer("");
		for(int i=0;i<ch.length;i++){
			sb.append(Integer.toHexString(Integer.valueOf(Integer.toString(ch[i]))));
		}
		return sb.toString();
	}
	public static String num2hex(long num, int size, boolean littleEndian) {
		num = num == -1 ? 0 : num;
		String hex = Long.toHexString(num);
		for (int len = hex.length(); len < size * 2; len++) {
			hex = "0" + hex;
		}
		if (littleEndian) {
			byte[] temp = ByteUtil.hexToByte(hex);
			hex = ByteUtil.bytesToHex(ByteUtil.reverse(temp));
		}
		return hex.substring(0, size * 2);
	}
	
	/** 将16进制字符串转换成二进制字符串(低位在前 02---------->01000000)
	 * 	AABBCC----->A7-A0 B7-B0 C7-C0----->CO--C7 B0--B7 A0--A7
	 * */
	public static String StrToBinstr(String str) {
		 byte[] bh= hexToByte(str);
		 String result=new String();;
		 for(int i=0;i<bh.length;i++){
			 result +=byteToBits(bh[i] )+ "";
		 }
		 StringBuffer sb = new StringBuffer(result);
		 result = sb.reverse().toString();
		 return result;
	}
}
