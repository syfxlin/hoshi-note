package me.ixk.hoshi.mail.view;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.util.List;

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
@Schema(name = "验证码邮件")
public class CodeMail implements Serializable {

    @Schema(name = "回复邮箱")
    private String replyTo;

    @Schema(name = "发到")
    @Singular("to")
    private List<String> to;

    @Schema(name = "抄送")
    @Singular("cc")
    private List<String> cc;

    @Schema(name = "密送")
    @Singular("bcc")
    private List<String> bcc;

    @Schema(name = "主题")
    private String subject;

    @Schema(name = "验证码")
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
