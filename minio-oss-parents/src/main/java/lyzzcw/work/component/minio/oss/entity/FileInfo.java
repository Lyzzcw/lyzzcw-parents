package lyzzcw.work.component.minio.oss.entity;


import lombok.Data;

@Data
public class FileInfo {

    String fileSourceName;

    String fileType;

    Integer fileSize;

    String fileHash;

    String fileUrl;
}
