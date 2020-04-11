package com.example.demo.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DruidConfig {

    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource durid() {  // 配置数据源,将spring.datasource所有的属性绑定进此数据源
        return new DruidDataSource();
    }

    // druid 数据源状态监控
    @Bean
    public ServletRegistrationBean statViewServlet() {
        // 配置路径后可通过localhost:8080/druid 对控制页面进行访问，
        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(),
                "/druid/*");
        Map<String, String> initParam = new HashMap<>();
        // 设置控制台管理用户
        initParam.put("loginUsername", "root");
        initParam.put("loginPassword", "root");
        // 设置ip白名单
        initParam.put("allow","127.0.0.1");

        bean.setInitParameters(initParam);
        return bean;
    }

    // 配置druid的filter
    // 与Servlet的filter不同，这里的filter针对sql
    @Bean
    public FilterRegistrationBean webStatFilter() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new WebStatFilter());

        Map<String, String> initParams = new HashMap<>();
        // 忽略过滤的形式
        initParams.put("exclusions", "*.js,*.css,/druid/*");
        bean.setInitParameters(initParams);
        // 设置过滤器过滤路径
        bean.setUrlPatterns(Arrays.asList("/*"));

        return bean;
    }

}
