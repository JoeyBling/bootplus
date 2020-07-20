package io.github.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import io.github.common.typehandler.MyTypeHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Collection;

/**
 * MybatisPlus Config
 *
 * @author Created by 思伟 on 2020/6/6
 */
@Configuration
@MapperScan("io.github.**.dao*")
@Slf4j
public class MybatisPlusConfig {

    //    @MyAutowired
    private SqlSessionFactory sqlSessionFactoryBean;

    /**
     * mybatis-plus分页插件<br>
     * 文档：https://mybatis.plus/
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
        // paginationInterceptor.setOverflow(false);
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        // paginationInterceptor.setLimit(500);
        // 开启 count 的 join 优化,只针对部分 left join
        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
        return paginationInterceptor;
    }

    @PostConstruct
    public void myBatisHandle() {
        if (null != sqlSessionFactoryBean) {
            org.apache.ibatis.session.Configuration configuration =
                    sqlSessionFactoryBean.getConfiguration();
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            // FIXME ALL_TYPE_HANDLERS_MAP 会覆盖掉相同的key值，应该从 TYPE_HANDLER_MAP 读取
            Collection<TypeHandler<?>> handlers = typeHandlerRegistry.getTypeHandlers();
            if (null != handlers) {
                handlers.forEach(typeHandler -> {
                    // 抽离父类，过滤掉内置类型转换器
                    // if (MyTypeHandler.class.isAssignableFrom(typeHandler.getClass())) {
                    if (typeHandler instanceof MyTypeHandler) {
                        // log.debug("自定义MyBatis类型转换器注册成功:{}", typeHandler);
                    }
                });
            }
        }
    }

}
