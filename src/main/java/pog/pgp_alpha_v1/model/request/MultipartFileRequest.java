package pog.pgp_alpha_v1.model.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

//@ApiModel(description = "MultipartFile request payload")
@Data
public class MultipartFileRequest {

    //@ApiModelProperty(value = "uid", required = true)
    // 用户id
    private String uid;
    //@ApiModelProperty(value = "id", required = true)
    //任务ID
    private String id;
    //@ApiModelProperty(value = "chunks", required = true)
    //总分片数量
    private int chunks;
    //@ApiModelProperty(value = "chunk", required = true)
    //当前为第几块分片
    private int chunk;
    //@ApiModelProperty(value = "size", required = true)
    //当前分片大小
    private long size = 0L;
    //@ApiModelProperty(value = "name", required = true)
    //文件名
    private String name;
    //@ApiModelProperty(value = "file", required = true)
    //分片对象
    private MultipartFile file;
    //@ApiModelProperty(value = "md5", required = true)
    // MD5
    private String md5;
}
