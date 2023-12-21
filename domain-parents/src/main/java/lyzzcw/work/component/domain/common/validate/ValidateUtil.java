package lyzzcw.work.component.domain.common.validate;

import cn.hutool.core.collection.CollectionUtil;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * @author: lzy
 * Date: 2021/12/8
 * Time: 13:59
 * Description: 检验validate不生效 方法，添加依赖后，reload maven生效
 */
public class ValidateUtil {
    private static Validator validator = Validation.buildDefaultValidatorFactory()
            .getValidator();

    public static void beanValidate(Object obj) {
        Map<String, String> validatedMsg = new HashMap<>();
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(obj);
        for (ConstraintViolation<Object> c : constraintViolations) {
            validatedMsg.put(c.getPropertyPath().toString(), c.getMessage());
        }
        if (CollectionUtil.isNotEmpty(constraintViolations)) {
            throw new RuntimeException();
        }

    }
}
