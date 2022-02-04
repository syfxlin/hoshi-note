package me.ixk.hoshi.mail.view;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.InputStreamSource;

import java.io.*;

/**
 * 附件
 *
 * @author Otstar Lin
 * @date 2021/7/17 16:55
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "附件")
public class Attachment implements Serializable {

    private static final int DEFAULT_BUFFER_SIZE = 8192;

    @Schema(name = "附件名称")
    private String name;

    @Schema(name = "是否嵌入")
    private boolean inline;

    @Schema(name = "内容类型")
    private String contentType;

    @Schema(name = "内容")
    private InputStreamSource source;

    @Serial
    private void readObject(final ObjectInputStream stream) throws ClassNotFoundException, IOException {
        this.setName(stream.readUTF());
        this.setInline(stream.readBoolean());
        this.setContentType(stream.readUTF());
        final int length = stream.readInt();
        final byte[] bytes = new byte[length];
        stream.read(bytes);
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        this.setSource(() -> {
            inputStream.reset();
            return inputStream;
        });
    }

    @Serial
    private void writeObject(final ObjectOutputStream stream) throws IOException {
        stream.writeUTF(this.getName());
        stream.writeBoolean(this.isInline());
        stream.writeUTF(this.getContentType());
        final InputStream inputStream = this.getSource().getInputStream();
        final byte[] bytes = inputStream.readAllBytes();
        stream.writeInt(bytes.length);
        stream.write(bytes);
    }
}
