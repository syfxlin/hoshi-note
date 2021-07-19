package me.ixk.hoshi.mail.view;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import lombok.*;

/**
 * 验证码邮件
 *
 * @author Otstar Lin
 * @date 2021/7/18 21:14
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("验证码邮件")
public class CodeMail implements Serializable {

    private static final long serialVersionUID = -5218637746336425247L;

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

    @ApiModelProperty("验证码")
    private String code;

    public Mail toMail(final String content) {
        return Mail
            .builder()
            .replyTo(this.getReplyTo())
            .to(this.getTo())
            .cc(this.getCc())
            .bcc(this.getBcc())
            .subject(this.getSubject())
            .text(content)
            .html(true)
            .build();
    }
}
