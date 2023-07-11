package lyzzcw.work.component.common.json.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: lzy
 * @version: 1.0
 * Date: 2023/4/7 9:59
 * Description: No Description
 */
@Setter
@Getter
public class FastjsonException extends FormativeException {
    public FastjsonException() {
        super();
    }

    public FastjsonException(String message) {
        super(message);
    }

    public FastjsonException(Throwable cause) {
        super(cause);
    }

    public FastjsonException(String format, Object... arguments) {
        super(format, arguments);
    }
}
