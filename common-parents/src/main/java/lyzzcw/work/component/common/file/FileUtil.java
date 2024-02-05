package lyzzcw.work.component.common.file;

import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.component.common.utils.EncryptUtil;
import lyzzcw.work.component.common.utils.StringUtil;
import org.apache.commons.imaging.ImageFormat;
import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;

/**
 * @author lz
 * @date 2021-10-18 13:55
 */
@Slf4j
public class FileUtil {
    public static InputStream getResourcesFileInputStream(String fileName) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream("" + fileName);
    }

    public static String getPath() {
        return FileUtil.class.getResource("/").getPath();
    }

    public static File createNewFile(String pathName) {
        File file = new File(getPath() + pathName);
        if (file.exists()) {
            file.delete();
        } else {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
        }
        return file;
    }

    public static File readFile(String pathName) {
        return new File(getPath() + pathName);
    }

    public static File readUserHomeFile(String pathName) {
        return new File(System.getProperty("user.home") + File.separator + pathName);
    }

    /**
     * 获取文件后缀名, 如 ".png" 或 ".jpg".
     *
     * @param pURI 文件URI
     * @return 包含(.)的后缀名; 无后缀或uri为空则返回""
     */
    public static String getExtension(String pURI) {
        if (!StringUtil.isEmpty(pURI)) {
            int sep = pURI.lastIndexOf(File.separator);
            int dot = pURI.lastIndexOf(".");
            if (dot >= 0 && sep < dot) {
                return pURI.substring(dot);
            }
        }
        return "";
    }

    /**
     * 获取文件后缀名, 如 "png" 或 "jpg".
     *
     * @param pURI 文件URI
     * @return 不包含(.)的后缀名; 无后缀或uri为空则返回""
     */
    public static String getExtensionWithoutDot(String pURI) {
        if (!StringUtil.isEmpty(pURI)) {
            int sep = pURI.lastIndexOf(File.separator);
            int dot = pURI.lastIndexOf(".");
            if (dot >= 0 && sep < dot) {
                return pURI.substring(dot + 1);
            }
        }
        return "";
    }

    /**
     * 转换byte大小为可阅读大小字符串
     *
     * @param pBytes    bytes大小
     * @param pIsSIUnit 是否使用SI标准
     * @return 可阅读的大小字符串, 如：<br/>
     * <table border="0" cellpadding="2" cellspacing="0" style="padding-right:10px;">
     * <tr style="font-weight:bold;"><td>pIsSIUnit</td><td>false</td><td>true</td></tr>
     * <tr><td>0</td><td>0 B</td><td>0 B</td></tr>
     * <tr><td>27</td><td>27 B</td><td>27 B</td></tr>
     * <tr><td>999</td><td>999 B</td><td>999 B</td></tr>
     * <tr><td>1000</td><td>1000 B</td><td>1.00 kB</td></tr>
     * <tr><td>1023</td><td>1023 B</td><td>1.02 kB</td></tr>
     * <tr><td>1024</td><td>1.00 KiB</td><td>1.02 kB</td></tr>
     * <tr><td>1728</td><td>1.69 KiB</td><td>1.73 kB</td></tr>
     * <tr><td>110592</td><td>108.00 KiB</td><td>110.59 kB</td></tr>
     * <tr><td>7077888</td><td>6.75 MiB</td><td>7.08 MB</td></tr>
     * <tr><td>452984832</td><td>432.00 MiB</td><td>452.98 MB</td></tr>
     * <tr><td>28991029248</td><td>27.00 GiB</td><td>28.99 GB</td></tr>
     * <tr><td>1855425871872</td><td>1.69 TiB</td><td>1.86 TB</td></tr>
     * <tr><td>9223372036854775807</td><td>8.00 EiB</td><td>9.22 EB</td></tr>
     * </table>
     */
    public static String getHumanReadableSize(long pBytes, boolean pIsSIUnit) {
        StringBuilder result = new StringBuilder();
        int           unit   = pIsSIUnit ? 1000 : 1024;
        if (pBytes < unit) {
            result.append(pBytes).append(" B");
        } else {
            int exp = (int) (Math.log(pBytes) / Math.log(unit));

            // true 为SI标准，使用Gigabyte单位，原本单位需要增加i，入GiB，考虑老百姓的认知，故去掉i
            // String pre = (pIsSIUnit ? "kMGTPE" : "KMGTPE").charAt(exp - 1) +
            // (pIsSIUnit ? "" : "i");
            String pre = (pIsSIUnit ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (pIsSIUnit ? "" : "");
            result.append(String.format(Locale.getDefault(), "%.1f %sB", pBytes / Math.pow(unit, exp), pre));
        }
        return result.toString();
    }

    /**
     * Base64图片内容保存为文件
     * @param pImageString 图片内容
     * @param pImagePath 文件路径
     * @return true为成功
     */
    public static boolean base64_to_image(String pImageString, String pImagePath) {
        try {
            byte[] decodedBytes = EncryptUtil.base64_decode(pImageString);
            Files.write(Paths.get(pImagePath), decodedBytes);
            return true;
        } catch (IOException e) {
            log.error("base64 to image failed.", e);
        }
        return false;
    }

    /**
     * 文件图片转化为Base64字符串
     * @param pImagePath 图片地址
     * @return Base64图片内容
     */
    public static String image_to_base64(String pImagePath) {
        String result = "";

        try {
            byte[] content = Files.readAllBytes(Paths.get(pImagePath));
            result = EncryptUtil.base64_encode(content);
        } catch (IOException e) {
            log.error("image to base64 failed.", e);
        }

        return result;
    }

    /**
     * 检查图像格式
     *
     * @param imageBytes 图像字节
     * @return {@link String}
     */
    public static String checkImageFormat(byte[] imageBytes){
        try (InputStream inputStream = new ByteArrayInputStream(imageBytes)) {
            ImageInfo imageInfo = Imaging.getImageInfo(inputStream, null);
            // 获取图片格式信息
            ImageFormat imageFormat = imageInfo.getFormat();
            if (imageFormat != null) {
                return imageFormat.getName();
            }
        } catch (IOException | ImageReadException e) {
            e.printStackTrace();
        }
        return null;
    }
}
