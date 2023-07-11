package lyzzcw.work.component.authentication.service.token;

import cn.dev33.satoken.jwt.SaJwtTemplate;
import cn.dev33.satoken.jwt.SaJwtUtil;
import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.stp.StpLogic;
import cn.hutool.jwt.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lzy
 * @version 1.0
 * Date: 2022/1/12
 * Time: 20:17
 * Created with IntelliJ IDEA
 * Description: 自定义生成token
 */
@Configuration
public class CustomizeToken {

    // Sa-Token 整合 jwt (Simple 简单模式)
    @Bean
    public StpLogic getStpLogicJwt() {
        return new StpLogicJwtForSimple();
    }

    /**
     * 自定义 SaJwtUtil 生成 token 的算法
     */
//    @Autowired
//    public void setSaJwtTemplate() {
//        SaJwtUtil.setSaJwtTemplate(new SaJwtTemplate() {
//            @Override
//            public String generateToken(JWT jwt, String keyt) {
//                System.out.println("------ 自定义了 token 生成算法");
//                return super.generateToken(jwt, keyt);
//            }
//        });
//    }

}
