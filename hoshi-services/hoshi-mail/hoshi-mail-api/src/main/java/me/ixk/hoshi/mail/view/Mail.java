package me.ixk.hoshi.mail.view;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

/**
 * 邮件
 *
 * @author Otstar Lin
 * @date 2021/7/17 16:44
 */
@Data
@Builder
@ApiModel("邮件")
public class Mail implements Serializable {

    private static final long serialVersionUID = 8424241406721320553L;

    @ApiModelProperty("回复邮箱")
    private String replyTo;

    @ApiModelProperty("发到")
    @Singular("to")
    private List<String> to;

    @ApiModelProperty("抄送")
    @Singular("cc")
    private List<String> cc;

    @ApiModelProperty("密送")
    @Singular("bcc")
    private List<String> bcc;

    @ApiModelProperty("主题")
    private String subject;

    @ApiModelProperty("正文")
    private String text;

    @ApiModelProperty("是否是 HTML")
    private boolean html;

    @ApiModelProperty("附件")
    @Singular("attachment")
    private List<Attachment> attachments;
}
