package lyzzcw.work.component.common.http.ip;

import com.alibaba.fastjson2.JSON;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import com.maxmind.geoip2.record.Subdivision;
import lyzzcw.work.component.common.object.FileUtil;

import java.io.File;
import java.net.InetAddress;

/**
 * Created with IntelliJ IDEA.
 * @author: lzy
 * Date: 2022/5/16
 * Time: 9:32
 * Description: No Description
 */
public class GeoIpUtils {
    private static DatabaseReader reader;

    private static void init() {
        try {
            // 创建 GeoLite2 数据库 Reader
            // 这里可以放在本地磁盘，也可以随项目放在resource目录下
            File database = new File(FileUtil.getPath()+ File.separator+"GeoLite2-City.mmdb");
            // 读取数据库内容
            reader = new DatabaseReader.Builder(database).build();
        } catch (Exception ex) {

        }
    }

    public static void getCityByIP(String ip) throws Exception {
        if (null == reader) {
            init();
        }

        InetAddress ipAddress = InetAddress.getByName(ip);

        // 获取查询结果
        CityResponse response = reader.city(ipAddress);

        // 获取国家信息
        Country country = response.getCountry();
        System.out.println("国家信息：" + JSON.toJSONString(country));

        // 获取省份
        Subdivision subdivision = response.getMostSpecificSubdivision();
        System.out.println("省份信息：" + JSON.toJSONString(subdivision));

        //城市
        City city = response.getCity();
        System.out.println("城市信息：" + JSON.toJSONString(city));

        // 获取城市
        Location location = response.getLocation();
        System.out.println("经纬度信息：" + JSON.toJSONString(location));
    }

    public static void main(String[] args) throws Exception {
        String ip = "183.19.101.138";
        GeoIpUtils.getCityByIP(ip);
    }
}
