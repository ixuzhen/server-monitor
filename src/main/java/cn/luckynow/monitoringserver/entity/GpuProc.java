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
 * @since 2022-12-06
 */
@TableName("gpu_proc")
@ApiModel(value = "GpuProc对象", description = "")
@Data
public class GpuProc implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("每一条 GPU 进程信息的 id")
    @TableId(value = "id_gpu_proc_info", type = IdType.AUTO)
    private Long idGpuProcInfo;

    @ApiModelProperty("主机 id")
    private Long idHost;

    @ApiModelProperty("主机 ip")
    private String ip;

    @ApiModelProperty("GPU 编号")
    private Integer indexGpu;

    @ApiModelProperty("进程的pid")
    private Long pid;

    @ApiModelProperty("进程类型")
    private String typeProc;

    @ApiModelProperty("进程名字")
    private String nameProc;

    @ApiModelProperty("显存用量")
    private Integer memoryUsed;

    @ApiModelProperty("当前工作目录")
    private String cwd;

    @ApiModelProperty("执行的命令")
    private String exe;

    @ApiModelProperty("更新时间")
    private Timestamp dateGpuProc;

    private Timestamp startTime;


}
