package lyzzcw.work.component.common.http.ip;

/**
 * @author: lzy
 * @version: 1.0
 * Date: 2023/3/7 11:22
 * Description:
 * 转换字符串IPv4和数值类型
 * 当存储IPv4地址时，应该使用32位的无符号整数（UNSIGNED INT）来存储IP地址，而不是使用字符串
 * MySQL提供了IPv4相应的转换函数,INET_ATON和INET_NTOA
 * MySQL也提供了IPv6相应的转换函数，即INET6_ATON和INET6_NTOA
 */
public class IpLongUtils {
    /**
     * 把字符串IP转换成long
     *
     * @param ipStr 字符串IP
     * @return IP对应的long值
     */
    public static long ip2Long(String ipStr) {
        String[] ip = ipStr.split("\\.");
        return (Long.valueOf(ip[0]) << 24) + (Long.valueOf(ip[1]) << 16)
                + (Long.valueOf(ip[2]) << 8) + Long.valueOf(ip[3]);
    }

    /**
     * 把IP的long值转换成字符串
     *
     * @param ipLong IP的long值
     * @return long值对应的字符串
     */
    public static String long2Ip(long ipLong) {
        StringBuilder ip = new StringBuilder();
        ip.append(ipLong >>> 24).append(".");
        ip.append((ipLong >>> 16) & 0xFF).append(".");
        ip.append((ipLong >>> 8) & 0xFF).append(".");
        ip.append(ipLong & 0xFF);
        return ip.toString();
    }

    public static void main(String[] args) {
        System.out.println(ip2Long("192.168.0.1"));
        System.out.println(long2Ip(3232235521L));
        System.out.println(ip2Long("10.0.0.1"));
        System.out.println(long2Ip(167772161L
        ));
    }

}
