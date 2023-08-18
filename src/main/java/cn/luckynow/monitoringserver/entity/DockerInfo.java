package cn.luckynow.monitoringserver.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.sql.Timestamp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author xz
 * @since 2023-08-11
 */
@TableName("docker_info")
@ApiModel(value = "DockerInfo对象", description = "")
@Data
public class DockerInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String ip;
    private Timestamp date;

    @ApiModelProperty("容器名称")
    private String name;

    @ApiModelProperty("容器id")
    private String containerId;

    private String cpu;

    private String memeryUsage;

    private String memeryLimit;

    private String memeryUsageRate;

    @ApiModelProperty("网络发送量")
    @TableField("net_io_send")
    private String netIOSend;

    @ApiModelProperty("网络接收量")
    @TableField("net_io_receive")
    private String netIOReceive;

    @ApiModelProperty("磁盘读取量")
    @TableField("block_io_read")
    private String blockIORead;

    @ApiModelProperty("磁盘写入量")
    @TableField("block_io_write")
    private String blockIOWrite;


}
