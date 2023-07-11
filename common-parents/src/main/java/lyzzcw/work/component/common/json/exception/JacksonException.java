package lyzzcw.work.component.common.json.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: lzy
 * @version: 1.0
 * Date: 2023/4/7 9:48
 * Description: No Description
 */
@Setter
@Getter
public class JacksonException extends FormativeException{
    public JacksonException() {
        super();
    }

    public JacksonException(String message) {
        super(message);
    }

    public JacksonException(Throwable cause) {
        super(cause);
    }

    public JacksonException(String format, Object... arguments) {
        super(format, arguments);
    }
}
