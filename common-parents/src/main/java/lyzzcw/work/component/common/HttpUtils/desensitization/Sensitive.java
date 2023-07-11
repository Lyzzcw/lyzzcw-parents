package lyzzcw.work.component.common.HttpUtils.desensitization;

/**
 * @author: lzy
 * @version: 1.0
 * Date: 2022/10/11 10:45
 * Description: 自定义jackson注解，标注在属性上
 */

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@JacksonAnnotationsInside
@JsonSerialize(using = SensitiveJsonSerializer.class)
public @interface Sensitive {
    //脱敏策略
    SensitiveStrategy strategy();
}
