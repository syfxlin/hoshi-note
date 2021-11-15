package me.ixk.hoshi.mail.view;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import lombok.*;

/**
 * 邮件
 *
 * @author Otstar Lin
 * @date 2021/7/17 16:44
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("邮件")
public class Mail implements Serializable {

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
    @Builder.Default
    private boolean html = true;

    @ApiModelProperty("附件")
    @Singular("attachment")
    private List<Attachment> attachments;
}
