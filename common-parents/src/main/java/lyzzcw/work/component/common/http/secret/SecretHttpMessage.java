package lyzzcw.work.component.common.http.secret;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * @author: lzy
 * Date: 2022/7/27
 * Time: 10:42
 * Description: SecretHttpMessage
 */
@AllArgsConstructor
@NoArgsConstructor
public class SecretHttpMessage implements HttpInputMessage {
    private InputStream body;

    private HttpHeaders httpHeaders;
    @Override
    public InputStream getBody() throws IOException {
        return null;
    }

    @Override
    public HttpHeaders getHeaders() {
        return null;
    }
}
