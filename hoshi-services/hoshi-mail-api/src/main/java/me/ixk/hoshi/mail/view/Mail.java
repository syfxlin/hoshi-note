package me.ixk.hoshi.mail.view;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.util.List;

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
@Schema(name = "邮件")
public class Mail implements Serializable {

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

    @Schema(name = "正文")
    private String text;

    @Schema(name = "是否是 HTML")
    @Builder.Default
    private boolean html = true;

    @Schema(name = "附件")
    @Singular("attachment")
    private List<Attachment> attachments;
}
