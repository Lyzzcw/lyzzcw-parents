package lyzzcw.work.component.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring 上下文支架
 *
 * @author lzy
 * @date 2023/12/20
 */
@Component
public class SpringContextHolder implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext(){
        checkNull();
        return applicationContext;
    }

    public static <T> T getBean(String beanName){
        checkNull();
        return (T) applicationContext.getBean(beanName);
    }

    public static <T> T getBean(Class<T> requiredType){
        checkNull();
        return applicationContext.getBean(requiredType);
    }

    private static void checkNull(){
        if (applicationContext == null){
            throw new RuntimeException("applicationContext为空");
        }
    }
}
