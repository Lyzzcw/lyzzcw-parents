package lyzzcw.work.component.common.http.secret;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with IntelliJ IDEA.
 * @author: lzy
 * Date: 2022/7/27
 * Time: 10:40
 * Description: 响应体加密注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface SecretResponseBody {
}
