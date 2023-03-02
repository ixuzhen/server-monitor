package cn.luckynow.monitoringserver.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author xz
 * @since 2023-03-02
 */
@Data
@ApiModel(value = "Port对象", description = "")
public class Port implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("端口信息的id")
    @TableId(value = "id_port", type = IdType.AUTO)
    private Long idPort;

    private Long idHost;

    @ApiModelProperty("IP地址")
    private String ip;

    @ApiModelProperty("更新时间")
    private Timestamp date;

    @ApiModelProperty("本地地址")
    private String localAddress;

    @ApiModelProperty("本地端口")
    private Long localPort;

    @ApiModelProperty("远程地址")
    private String remoteAddress;

    @ApiModelProperty("远程端口")
    private Long remotePort;

    @ApiModelProperty("socket状态")
    private Long state;

    @ApiModelProperty("socket对应的inode")
    private Long inode;

    @ApiModelProperty("对应的进程id")
    private Long pid;

    @ApiModelProperty("对应的进程名称")
    private String pname;

    @ApiModelProperty("连接类型（tcp，udp）")
    private String type;


    @Override
    public String toString() {
        return "Port{" +
            "idPort=" + idPort +
            ", idHost=" + idHost +
            ", ip=" + ip +
            ", date=" + date +
            ", localAddress=" + localAddress +
            ", localPort=" + localPort +
            ", remoteAddress=" + remoteAddress +
            ", remotePort=" + remotePort +
            ", state=" + state +
            ", inode=" + inode +
            ", pid=" + pid +
            ", pname=" + pname +
            ", type=" + type +
        "}";
    }
}
