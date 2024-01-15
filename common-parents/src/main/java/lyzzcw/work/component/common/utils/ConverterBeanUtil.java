package lyzzcw.work.component.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lzy
 * @version 1.0
 * Date: 2023/12/11 13:53
 * Description: No Description
 */
public class ConverterBeanUtil {


    /**
     * 属性相同的两个类的转换
     *
     * @param source --源类
     * @param target --目标类Class
     * @return
     */
    public static <T> T convertClass (Object source, Class<T> target) {
        return convertProperties(source, target, null);
    }

    /**
     * 属性相同的两个类的转换 --支持过滤不需要转换的变量（一般用于变量名相同，类型不同时）
     *
     * @param source --源类
     * @param target --目标类Class
     * @return
     */
    public static <T> T convertClass (Object source, Class<T> target, String[] filterProperties) {
        return convertProperties(source, target, filterProperties);
    }

    /**
     * 属性相同的两个类集合的转换
     *
     * @param sourceList --源类集合
     * @param target     --目标类Class
     * @return
     */
    public static <T> List<T> convertClassList (List<?> sourceList, Class<T> target) {

        return Optional.ofNullable(sourceList).orElse(new ArrayList<>())
                .stream()
                .map(sourceObj -> convertProperties(sourceObj, target, null))
                .collect(Collectors.toList());
    }

    /**
     * 属性相同的两个类集合的转换 --支持过滤不需要转换的变量（一般用于变量名相同，类型不同时）
     *
     * @param sourceList --源类集合
     * @param target     --目标类Class
     * @return
     */
    public static <T> List<T> convertClassList (List<?> sourceList, Class<T> target, String[] filterProperties) {

        return Optional.ofNullable(sourceList).orElse(new ArrayList<>())
                .stream()
                .map(sourceObj -> convertProperties(sourceObj, target, filterProperties))
                .collect(Collectors.toList());
    }

    /**
     * 对象转Map --支持过滤空值
     *
     * @param object
     * @return
     */
    public static Map<String, Object> beanToMap (Object object, Boolean filterNullProperties) {

        Field[] fields = object.getClass().getDeclaredFields();
        Map<String, Object> map = new HashMap<>(fields.length);

        for (Field field : fields) {
            try {
                if (!Modifier.isPublic(field.getModifiers())) {
                    field.setAccessible(true);
                }
                Object value = field.get(object);

                if (!filterNullProperties || value != null) {
                    map.put(field.getName(), value);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(
                        "Could not beanToMap", e);
            }
        }
        return map;
    }


    /**
     * 转变属性
     *
     * @param source
     * @param target
     */
    public static <T> T convertProperties (Object source, Class<T> target, String[] filterProperties) {
        if (source == null) {
            return null;
        }

        T result = null;

        List<String> filterList = Arrays.asList(Optional.ofNullable(filterProperties).orElse(new String[0]));
        try {
            // 使用反射初始化target对象
            result = target.newInstance();

            // 获得target的所有变量对象的数组
            Field[] targetFields = target.getDeclaredFields();

            for (Field targetField : targetFields) {

                // accessible为true,非Public忽略访问修饰符检查
                if (!Modifier.isPublic(targetField.getModifiers())) {
                    targetField.setAccessible(true);
                }

                // 获取变量的名称
                String fieldName = targetField.getName();

                if (!filterList.contains(fieldName)) {
                    // 获取source的对应变量
                    Field sourceField = source.getClass().getDeclaredField(fieldName);

                    // 获取source对象中对应变量的值
                    if (!Modifier.isPublic(sourceField.getModifiers())) {
                        sourceField.setAccessible(true);
                    }
                    Object value = sourceField.get(source);

                    if (value != null) {
                        // 兼容枚举类的转化
                        Class targetType = targetField.getType();
                        Class sourceType = sourceField.getType();

                        if (targetType == String.class && sourceType.isEnum()) {
                            value = ((Enum<?>) value).name();
                        } else if (targetType.isEnum()
                                && sourceType == String.class) {
                            value = Enum.valueOf(targetType, (String) value);
                        }

                    }
                    // 给已初始化的target对象中对应变量赋值
                    targetField.set(result, value);
                }
            }

        } catch (Throwable ex) {
            throw new RuntimeException(
                    "Could not copy property from source to target", ex);
        }
        return result;
    }

}