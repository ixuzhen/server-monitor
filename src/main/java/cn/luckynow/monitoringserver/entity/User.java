package cn.luckynow.monitoringserver.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.sql.Timestamp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author xz
 * @since 2023-02-21
 */
@ApiModel(value = "User对象", description = "用户表")
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long idGithub;

    private Long idGitee;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("账号状态（0正常 1停用）")
    private String status;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("手机号")
    private String phonenumber;

    @ApiModelProperty("用户性别（0男，1女，2未知）")
    private String sex;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("用户类型（0管理员，1普通用户）")
    private String userType;

    @ApiModelProperty("创建人的用户id")
    private Long createBy;

    @ApiModelProperty("创建时间")
    private Timestamp createTime;

    @ApiModelProperty("更新人")
    private Long updateBy;

    @ApiModelProperty("更新时间")
    private Timestamp updateTime;

    @ApiModelProperty("删除标志（0代表未删除，1代表已删除）")
    private Integer delFlag;


}
