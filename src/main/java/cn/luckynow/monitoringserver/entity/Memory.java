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
 * @since 2023-02-28
 */
@ApiModel(value = "Memory对象", description = "")
@Data
public class Memory implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("内存信息的主键")
    @TableId(value = "id_memory", type = IdType.AUTO)
    private Long idMemory;

    @ApiModelProperty("主机的id")
    private Long idHost;

    @ApiModelProperty("主机ip")
    private String ip;

    @ApiModelProperty("总内存")
    private Long total;

    @ApiModelProperty("剩余内存")
    private Long available;

    @ApiModelProperty("使用百分比")
    private Double percent;

    private Long used;

    private Long free;

    private Long active;

    private Long inactive;

    private Long buffers;

    private Long cached;

    private Long shared;

    private Long slab;

    private Timestamp date;



    @Override
    public String toString() {
        return "Memory{" +
            "idMemory=" + idMemory +
            ", idHost=" + idHost +
            ", ip=" + ip +
            ", total=" + total +
            ", available=" + available +
            ", percent=" + percent +
            ", used=" + used +
            ", free=" + free +
            ", active=" + active +
            ", inactive=" + inactive +
            ", buffers=" + buffers +
            ", cached=" + cached +
            ", shared=" + shared +
            ", slab=" + slab +
            ", date=" + date +
        "}";
    }
}
