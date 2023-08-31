package lyzzcw.work.component.minio.oss.template;


import io.minio.*;
import io.minio.http.Method;
import lombok.SneakyThrows;
import lyzzcw.work.component.minio.oss.config.MinioConfiguration;
import lyzzcw.work.component.minio.oss.entity.FileInfo;
import lyzzcw.work.component.minio.oss.enums.MinioTypeEnum;
import lyzzcw.work.component.minio.oss.util.MD5Util;
import lyzzcw.work.component.minio.oss.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.crypto.prng.RandomGenerator;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * minio工具类
 */
public class MinioTemplate {

    private final String visitPath;
    private final String endpoint;
    private final String region;


    private final static Integer defaultExpires = 3600;

    private final MinioClient minioClient;

    public MinioTemplate(MinioClient minioClient, MinioConfiguration configuration) {
        this.minioClient = minioClient;
        this.visitPath=configuration.getVisitPath();
        this.endpoint=configuration.getEndpoint();
        this.region=configuration.getRegion();
    }

    /**
     * @param bucket      桶
     * @param path        文件路径(包括文件名字)
     * @param inputStream 文件流
     */
    public String upload(String bucket, String path, InputStream inputStream) {
        try {
            HashMap<String, String> objectObjectHashMap = new HashMap<>();
            objectObjectHashMap.put("x-amz-acl", "public-read");
            PutObjectArgs.Builder args = PutObjectArgs.builder()
                    .bucket(bucket)
                    .region(region)
                    .stream(inputStream, inputStream.available(), -1)
                    .object(path.startsWith("/") ? path.substring(1) : path)
                    .headers(objectObjectHashMap);
                String type=path.contains(".")?path.substring(path.lastIndexOf(".") + 1): null;
                if(StringUtils.isNotEmpty(type)){
                    args.contentType(MinioTypeEnum.getContentType(type));
                }
                ObjectWriteResponse objectWriteResponse = this.minioClient.putObject(args.build());
                String object = objectWriteResponse.object();
                if (StringUtils.isNotEmpty(object)) {
                    return visitPath + "/" + bucket + (path.startsWith("/")?path:("/"+path));
            }
            throw new RuntimeException("minio异常");
        } catch (Exception e) {
            throw new RuntimeException("minio异常");
        }
    }

    /**
     * @param bucket      桶
     * @param path        文件路径(包括文件名字)
     * @retun 原始文件名为空
     */
    @SneakyThrows
    public FileInfo uploadByInputStream(String bucket, String path, InputStream inputStream) {
        FileInfo fileInfo = new FileInfo();
        //fileInfo.setFileSourceName(file.getName());
        fileInfo.setFileType(Optional.ofNullable(path).filter(fileName -> fileName.contains(".")).map(fileName -> fileName.substring(fileName.lastIndexOf(".") + 1)).orElse(""));
        fileInfo.setFileSize(inputStream.available());
        String url = upload(bucket, path,inputStream);
        fileInfo.setFileHash(MD5Util.digestInputStream(inputStream));
        fileInfo.setFileUrl(url);
        return fileInfo;
    }

    /**
     * @param bucket      桶
     * @param path        文件路径(包括文件名字)
     * @param file 文件
     */
    @SneakyThrows
    public FileInfo upload(String bucket, String path, File file) {
        FileInputStream fileInputStream = null;
        try {
            FileInfo fileInfo = new FileInfo();
            String name = file.getName();
            fileInputStream = new FileInputStream(file);
            fileInfo.setFileSourceName(file.getName());
            fileInfo.setFileType(Optional.ofNullable(name).filter(fileName -> fileName.contains(".")).map(fileName -> fileName.substring(fileName.lastIndexOf(".") + 1)).orElse(""));
            fileInfo.setFileSize(fileInputStream.available());
            String url = upload(bucket, (path.endsWith("/")?path:(path+"/")) + UUID.get32UUID()+ (StringUtils.isNotEmpty(fileInfo.getFileType())?"."+fileInfo.getFileType():""),fileInputStream);
            fileInfo.setFileHash(MD5Util.digestInputStream(fileInputStream));
            fileInfo.setFileUrl(url);
            return fileInfo;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (null != fileInputStream){
                fileInputStream.close();
            }
        }
        throw new RuntimeException("上传文件失败");
    }

    /**
     *
     * @param bucket        桶
     * @param path          文件路径(包括文件名字)
     * @param downloadPath  下载连接
     * @param inputStream   文件流
     * @return              本地下载地址
     */
    public String localUpload(String bucket, String path, String downloadPath, InputStream inputStream) {
        try {
            HashMap<String, String> objectObjectHashMap = new HashMap<>();
            objectObjectHashMap.put("x-amz-acl", "public-read");
            PutObjectArgs args = PutObjectArgs.builder()
                    .bucket(bucket)
                    .region(region)
                    .stream(inputStream, inputStream.available(), -1)
                    .object(path.startsWith("/")?path.substring(1):path)
                    .headers(objectObjectHashMap)
                    .build();
            ObjectWriteResponse objectWriteResponse = this.minioClient.putObject(args);
            String object = objectWriteResponse.object();
            if (StringUtils.isNotEmpty(object)) {
                return visitPath + "/" + downloadPath + "/" + bucket + (path.startsWith("/")?path:("/"+path));
            }
            throw new RuntimeException("minio异常");
        } catch (Exception e) {
            throw new RuntimeException("minio异常");
        }
    }

    /**
     * 下载文件
     *
     * @param bucket 桶
     * @param path   文件路径(包括文件名字)
     * @return 文件流
     */
    public InputStream download(String bucket, String path) {
        GetObjectArgs args = GetObjectArgs.builder()
                .bucket(bucket)
                .region(region)
                .object(path)
                .build();
        try {
            return this.minioClient.getObject(args);
        } catch (Exception e) {
            throw new RuntimeException("minio异常");
        }
    }

    /**
     * 拿到文件的访问路径,直接访问minio服务器获取文件
     *
     * @param bucket  桶
     * @param path    文件路径(包括文件名字)
     * @param expires 过期时间(秒为单位)
     * @return 文件访问路径
     */
    public String presignedUrl(String bucket, String path, Integer expires) {
        GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder()
                .bucket(bucket)
                .region(region)
                .object(path)
                .expiry(expires)
                .method(Method.GET)
                .build();
        try {
            return this.minioClient.getPresignedObjectUrl(args);
        } catch (Exception e) {
            throw new RuntimeException("minio异常");
        }
    }

    /**
     * 删除文件
     *
     * @param bucket 桶
     * @param path   文件路径(包括文件名字)
     * @return: boolean
     */
    public void deleteFile(String bucket, String path) {
        RemoveObjectArgs args = RemoveObjectArgs.builder()
                .bucket(bucket)
                .region(region)
                .object(path)
                .build();
        try {
            this.minioClient.removeObject(args);
        } catch (Exception e) {
            throw new RuntimeException("minio删除文件异常");
        }
    }

    /**
     * 判断 bucket是否存在
     *
     * @param bucketName: 桶名
     * @return: boolean
     */
    public boolean bucketExists(String bucketName) {
        BucketExistsArgs args = BucketExistsArgs.builder()
                .bucket(bucketName)
                .region(region)
                .build();
        try {
            return this.minioClient.bucketExists(args);
        } catch (Exception e) {
            throw new RuntimeException("minio判断桶是否存在异常");
        }
    }

    /**
     * 创建 bucket
     *
     * @param bucketName: 桶名
     * @return: void
     */
    public void createBucket(String bucketName) {
        BucketExistsArgs args = BucketExistsArgs.builder()
                .bucket(bucketName)
                .region(region)
                .build();
        boolean isExist = false;
        try {
            isExist = minioClient.bucketExists(args);
        } catch (Exception e) {
            throw new RuntimeException("minio判断桶是否存在异常");
        }
        try {
            if (!isExist) {
                MakeBucketArgs args1 = MakeBucketArgs.builder()
                        .bucket(bucketName)
                        .build();
                minioClient.makeBucket(args1);
            }
        } catch (Exception e) {
            throw new RuntimeException("minio创建桶异常");
        }
    }

    /**
     * 获取上传url
     *
     * @param bucketName 桶名
     * @param objectName 文件名称
     * @param expires    过期时间
     * @return
     */
    @SneakyThrows
    public Map<String,Object> presignedPutUrl(String bucketName, String objectName, Integer expires) {
        HashMap<String, String> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("x-amz-tagging", "temp=true");
        GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder()
                .bucket(bucketName)
                .region(region)
                .object(objectName.startsWith("/")?objectName.substring(1):objectName)
                .expiry(expires)
                .extraQueryParams(objectObjectHashMap)
                .method(Method.PUT)
                .build();
        try {
            String presignedObjectUrl = this.minioClient.getPresignedObjectUrl(args);
            String downloadUrl=visitPath + "/" + bucketName + (objectName.startsWith("/")?objectName:("/"+objectName));
            Map<String,Object> resultMap=new HashMap<>();
            resultMap.put("uploadPath",presignedObjectUrl);
            resultMap.put("downloadPath",downloadUrl);
            return resultMap;
        } catch (Exception e) {
            throw new RuntimeException("minio异常");
        }
    }

    /**
     * 获取上传url
     *
     * @param bucketName 桶名
     * @param objectName 文件名称
     * @return
     */
    public Map<String,Object> presignedPutUrl(String bucketName, String objectName) {
        return presignedPutUrl(bucketName, objectName, defaultExpires);
    }


    /**
     * 删除文件标签
     *
     * @param bucketName
     * @param objectName
     * @param tags
     */
    public void deleteObjectTags(String bucketName, String objectName, Map<String, String> tags) {
        DeleteObjectTagsArgs args = DeleteObjectTagsArgs.builder().bucket(bucketName).object(objectName).extraQueryParams(tags).build();
        try {
            this.minioClient.deleteObjectTags(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改文件标签
     *
     * @param bucketName
     * @param objectName
     * @param tags
     */
    public void updateObjectTags(String bucketName, String objectName, Map<String, String> tags) {
        SetObjectTagsArgs args = SetObjectTagsArgs.builder().bucket(bucketName).object(objectName).tags(tags).build();
        try {
            this.minioClient.setObjectTags(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断文件是否存在
     *
     * @param bucketName 桶名称
     * @param objectName 文件名称, 如果要带文件夹请用 / 分割, 例如 /help/index.html
     * @return true存在, 反之
     */
    public Boolean checkFileIsExist(String bucketName, String objectName) {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder().bucket(bucketName).object(objectName).build()
            );
        } catch (Exception e) {
            return false;
        }
        return true;
    }



}
