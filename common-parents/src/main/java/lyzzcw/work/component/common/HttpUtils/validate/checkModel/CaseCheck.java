package lyzzcw.work.component.common.HttpUtils.validate.checkModel;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * @author: lzy
 * Date: 2021/12/8
 * Time: 15:10
 * Description: No Description
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
//指定校验器
@Constraint(validatedBy = {CaseCheckValidator.class})
public @interface CaseCheck {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

   CaseMode value() default CaseMode.UPPER;

}
