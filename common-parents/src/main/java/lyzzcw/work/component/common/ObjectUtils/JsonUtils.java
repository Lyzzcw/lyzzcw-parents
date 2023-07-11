package lyzzcw.work.component.common.ObjectUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * @author: lzy
 * Date: 2022/4/29
 * Time: 9:29
 * Description: No Description
 */
@Slf4j
public class JsonUtils {
    private static ObjectMapper mapper = new ObjectMapper();
    private static ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();

    public static <T> T JSONToObject(String json, TypeReference<T> type){
        try {
            return mapper.readValue(json, type);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return null;
    }

    public static<T> T mapToObject(Map<String, String> map, Class<T> clazz){
        return mapper.convertValue(map, clazz);
    }

    public static Map<String,String> ObjectToMap(Object object){
        try {
            ObjectMapper objMapper = new ObjectMapper();
            objMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); // 序列化时 ，属性值为null的忽略
            return objMapper.convertValue(object, new TypeReference<Map<String,String>>(){});
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return null;
    }

    public static Map<String,String> ObjectToMapAllowNull(Object object){
        try {
            ObjectMapper objMapper = new ObjectMapper();
            return objMapper.convertValue(object, new TypeReference<Map<String,String>>(){});
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return null;
    }
    public static String ObjectToJSON(Object object){
        try {
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); // 序列化时 ，属性值为null的忽略
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(),e);
        }
        return null;
    }
    public static ObjectMapper mapper(){
        return mapper;
    }

    public static <T> T readObjectFromJSONFile(String path, TypeReference<T> type){
        String url = path + ".json";
        Resource resource = resourceResolver.getResource(url);
        try {
            if(resource.exists())
                return mapper.readValue(resource.getFile(), type);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return null;
    }

}
