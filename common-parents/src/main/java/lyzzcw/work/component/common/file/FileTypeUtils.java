package lyzzcw.work.component.common.file;

import lyzzcw.work.component.common.utils.EncryptUtil;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;

/**
 * @author lzy
 * @version 1.0
 * Date: 2024/2/19 10:25
 * Description: 通过文件魔数来判断文件类型
 */
public class FileTypeUtils {
    /**
     * 获取图片文件实际类型,若不是图片则返回null]
     * @param file
     * @return fileType
     */
    public final static String getImageFileType(File file) {
        if (isImage(file)) {
            try {
                ImageInputStream iis = ImageIO.createImageInputStream(file);
                Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
                if (!iter.hasNext()) {
                    return null;
                }
                ImageReader reader = iter.next();
                iis.close();
                return reader.getFormatName();
            } catch (IOException e) {
                return null;
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 获取文件类型,包括图片,若格式不是已配置的,则返回null
     * @param file
     * @return fileType
     */
    public final static String getFileTypeByFile(File file) {
        String filetype = null;
        byte[] b = new byte[50];
        try {
            InputStream is = new FileInputStream(file);
            is.read(b);
            filetype = getFileTypeByBytes(b);
            is.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filetype;
    }

    /**
     * 获取文件类型,包括图片,若格式不是已配置的,则返回null
     * @param is
     * @return fileType
     */
    public final static String getFileTypeByStream(InputStream is) {
        String filetype = null;
        byte[] b = new byte[50];
        try {
            is.read(b);
            filetype = getFileTypeByBytes(b);
            is.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filetype;
    }

    /**
     * 通过数据流（二进制数据）判断文件类型
     * @param b
     * @return fileType
     */
    public final static String getFileTypeByBytes(byte[] b) {
        String magicNumberCode = String.valueOf(getFileHexString(b));

        if (StringUtils.isNotBlank(magicNumberCode)) {
            return FileTypeEnum.getByMagicNumberCode(magicNumberCode.toUpperCase()).getFileTypeName();

        }
        return FileTypeEnum.NOT_EXITS_ENUM.getFileTypeName();
    }

    /**
     * isImage,判断文件是否为图片
     * @param file
     * @return true 是 | false 否
     */
    public static final boolean isImage(File file){
        boolean flag = false;
        try {
            BufferedImage bufreader = ImageIO.read(file);
            int width = bufreader.getWidth();
            int height = bufreader.getHeight();
            if(width==0 || height==0){
                flag = false;
            }else {
                flag = true;
            }
        } catch (IOException e) {
            flag = false;
        }catch (Exception e) {
            flag = false;
        }
        return flag;
    }


    /**
     * 通过文件路径判断文件类型
     * @param path
     * @return
     * @throws IOException
     */
    public static String getFileTypeByPath(String path) {
        // 获取文件头
        String magicNumberCode = null;
        try {
            magicNumberCode = getFileHeader(path);
        } catch (Exception e) {
            e.printStackTrace();
            return FileTypeEnum.NOT_EXITS_ENUM.getFileTypeName();
        }

        if (StringUtils.isNotBlank(magicNumberCode)) {
            return FileTypeEnum.getByMagicNumberCode(magicNumberCode.toUpperCase()).getFileTypeName();
        }

        return FileTypeEnum.NOT_EXITS_ENUM.getFileTypeName();
    }


    /**
     * 通过文件路径获取文件头（即文件魔数）
     * @param path
     * @return
     * @throws IOException
     */
    public static String getFileHeader(String path) throws Exception {
        byte[] b = new byte[28];
        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(path);
            inputStream.read(b, 0, 28);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return getFileHexString(b);
    }

    /**
     * 把文件二进制流转换成十六进制数据
     * @param b
     * @return fileTypeHex
     */
    public final static String getFileHexString(byte[] b) {
        StringBuilder builder = new StringBuilder();
        if (b == null || b.length <= 0) {
            return null;
        }

        for (int i = 0; i < b.length; i++) {
            int v = b[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                builder.append(0);
            }
            builder.append(hv);
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        String path = "C:/Users/84428/Desktop/hk.pdf";
        System.out.println(FileTypeUtils.getFileTypeByPath(path).toLowerCase());

        String base64 = FileUtil.image_to_base64(path);
        byte[] decodedBytes = EncryptUtil.base64_decode(base64);
        System.out.println(FileTypeUtils.getFileTypeByBytes(decodedBytes).toLowerCase());
    }
}
