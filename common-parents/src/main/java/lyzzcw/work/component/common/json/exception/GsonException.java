package lyzzcw.work.component.common.json.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: lzy
 * @version: 1.0
 * Date: 2023/4/7 10:17
 * Description: No Description
 */
@Setter
@Getter
public class GsonException extends FormativeException {
    public GsonException() {
        super();
    }

    public GsonException(String message) {
        super(message);
    }

    public GsonException(Throwable cause) {
        super(cause);
    }

    public GsonException(String format, Object... arguments) {
        super(format, arguments);
    }
}
