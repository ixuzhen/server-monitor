package cn.luckynow.monitoringserver.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2023-08-28
 */
@TableName("ssh_host")
@ApiModel(value = "SshHost对象", description = "")
@Data
public class SshHost implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("用户id")
    @TableField(select = false)  // 设置select属性为false，表示查询时忽略该字段
    private Long userId;

    @ApiModelProperty("主机ip")
    private String address;

    @ApiModelProperty("端口号")
    private String port;

    @ApiModelProperty("主机的username")
    private String username;

    @ApiModelProperty("主机密码")
    @TableField(select = false)  // 设置select属性为false，表示查询时忽略该字段
    private String password;

    @ApiModelProperty("更新时间")
    private Timestamp updateTime;
}
