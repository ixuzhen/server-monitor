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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 
 * </p>
 *
 * @author xz
 * @since 2022-12-02
 */
@TableName("gpu_info")
@ApiModel(value = "GpuInfo对象", description = "")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GpuInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id_gpu_info", type = IdType.AUTO)
    private Long idGpuInfo;

    private Long idHost;

    private String ip;

    private Integer indexGpu;

    private Integer countGpu;

    //private LocalDateTime date;
    private Timestamp dateGpu;

    private String nameGpu;

    private String driverVersion;

    private Integer memoryTotal;

    private Integer memoryUsed;

    private Integer memoryFree;

    private Integer enforcedPowerLimit;

    private Integer powerUsage;

    private Integer fanSpeed;

    private Integer utilizationRate;

    private Integer temperature;

    // GPU 中是否存在计算进程，如果存在该字段就为true
    @TableField(exist = false)
    private Boolean isUsed = false;

}
