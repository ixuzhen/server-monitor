package cn.luckynow.monitoringserver.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @since 2022-12-11
 */
@TableName("disk_usage")
@Data
@ApiModel(value = "DiskUsage对象", description = "")
public class DiskUsage implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "disk_usage_id", type = IdType.AUTO)
    private Long diskUsageId;

    private Long hostId;

    private String ip;

    private Timestamp dateDisk;

    private String device;

    private String mountpoint;

    private Long total;

    private Long free;

    private Long used;

    private Float percent;

    private String fstype;

}
