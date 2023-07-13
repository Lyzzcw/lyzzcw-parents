package lyzzcw.work.component.authentication.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created with IntelliJ IDEA.
 * @author: lzy
 * Date: 2022/1/12
 * Time: 16:34
 * Description: 注册拦截器
 * 在高版本 SpringBoot (≥2.6.x) 版本下，需要额外添加 @EnableWebMvc 注解才可以使注册拦截器生效。
 */
@Configuration
@Slf4j
public class SaTakenConfig implements WebMvcConfigurer {

    // 注册Sa-Token的注解拦截器，打开注解式鉴权功能
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册路由拦截器，自定义认证规则
        registry.addInterceptor(new SaInterceptor( handler -> {
            // 登录认证 -- 拦截所有路由，并排除/user/doLogin 用于开放登录
            SaRouter.match("/**", "/user/doLogin", r -> StpUtil.checkLogin());
//            SaRouter.match("/**")
//                    .notMatch("/user/doLogin")
//                    .notMatch("/swagger-ui/**")
//                    .check(r -> StpUtil.checkLogin());
            // 角色认证 -- 拦截以 admin 开头的路由，必须具备 admin 角色或者 super-admin 角色才可以通过认证
            SaRouter.match("/admin/**", r -> StpUtil.checkRoleOr("admin", "super-admin"));

            // 权限认证 -- 不同模块认证不同权限
            SaRouter.match("/user/**", "/user/doLogin",r -> StpUtil.checkPermission("user"));
            SaRouter.match("/admin/**", r -> StpUtil.checkPermission("admin"));
            SaRouter.match("/notice/**", r -> StpUtil.checkPermission("notice"));
            SaRouter.match("/comment/**", r -> StpUtil.checkPermission("comment"));
            //秒杀相关业务
            SaRouter.match("/activity/**", r -> StpUtil.checkPermission("activity"));
            SaRouter.match("/goods/**", r -> StpUtil.checkPermission("goods"));
            SaRouter.match("/order/**", r -> StpUtil.checkPermission("order"));
            // 甚至你可以随意的写一个打印语句
            SaRouter.match("/**", r -> log.info("welcome to the server"));
            // 连缀写法
            SaRouter.match("/**").check(r -> log.info("lyzzcw.work"));

        })).addPathPatterns("/**");
    }

    public void demo(){
        // 基础写法样例：匹配一个path，执行一个校验函数
        SaRouter.match("/user/**").check(r -> StpUtil.checkLogin());

        // 根据 path 路由匹配   ——— 支持写多个path，支持写 restful 风格路由
        SaRouter.match("/user/**", "/goods/**", "/art/get/{id}").check(r -> System.out.println("进入1"));

        // 根据 path 路由排除匹配
        SaRouter.match("/**").notMatch("*.html", "*.css", "*.js").check(r -> System.out.println("进入2"));

        // 根据请求类型匹配
        SaRouter.match(SaHttpMethod.GET).check(r -> System.out.println("进入3") );

        // 根据一个 boolean 条件进行匹配
        SaRouter.match( StpUtil.isLogin() ).check(r -> System.out.println("进入4"));

        // 根据一个返回 boolean 结果的lambda表达式匹配
        SaRouter.match( r -> StpUtil.isLogin() ).check(r -> System.out.println("进入5"));

        // 多个条件一起使用
        SaRouter.match(SaHttpMethod.GET).match("/**").check(r -> System.out.println("进入6"));

        // 可以无限连缀下去
        SaRouter
                .match(SaHttpMethod.GET)
                .match("/admin/**")
                .match("/user/**")
                .notMatch("/**/*.js")
                .notMatch("/**/*.css")
                // ....
                .check(r -> System.out.println("进入7"));


        //使用 SaRouter.stop() 可以提前退出匹配链
        SaRouter.match("/**").check(r -> System.out.println("进入8"));
        SaRouter.match("/**").check(r -> System.out.println("进入9")).stop();
        SaRouter.match("/**").check(r -> System.out.println("进入10"));

        // 执行back函数后将停止匹配，也不会进入Controller，而是直接将 back参数 作为返回值输出到前端
        SaRouter.match("/user/back").back("参数");

        /**
         * SaRouter.stop() 会停止匹配，进入Controller。
         * SaRouter.back() 会停止匹配，直接返回结果到前端。
         */
    }
}
