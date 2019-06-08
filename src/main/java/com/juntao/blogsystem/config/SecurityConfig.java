package com.juntao.blogsystem.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
@EnableGlobalMethodSecurity  //启用方法级别安全设置
public class SecurityConfig  extends WebSecurityConfigurerAdapter {
    private static final String KEY ="juntao.com";

    @Resource
    private UserDetailsService userServiceImpl;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();  //使用BCrypt加密
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userServiceImpl);
        authenticationProvider.setPasswordEncoder(passwordEncoder);  //设置密码的加密方式
        return authenticationProvider;
    }
    /**
         * 自定义配置
         */
    @Override
    protected void  configure(HttpSecurity http) throws Exception {
        //spring security会改变url编码，需要转码成utf-8
        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setEncoding("UTF-8");
        encodingFilter.setForceEncoding(true);
        //添加一个转码拦截
        http.addFilterBefore(encodingFilter, CsrfFilter.class);

        http.authorizeRequests().antMatchers("/css/**","/js/**","/fonts/**","/index").permitAll() //不限制访问
                .antMatchers("/h2-console/**").permitAll()  //不限制访问
                .antMatchers("/admins/**").hasRole("ADMIN") //需要管理员角色
                .antMatchers("/u/**").hasRole("USER") //需要用户角色
                .and().formLogin()  //基于Form表单登录验证
                .loginPage("/login").failureUrl("/login-error") //自定义登录界面
                .and().rememberMe().key(KEY) //启用remember me
                .and().exceptionHandling().accessDeniedPage("/403"); //处理异常，拒绝访问就重定向到403页面
        http.csrf().ignoringAntMatchers("/h2-console/**");  //禁用h2控制台的csrf防护

        http.headers().frameOptions().sameOrigin();       //允许来自同一来源的H2控制台的请求

    }
    /**
     * 认证信息管理
     * @param auth
     * @throws Exception*/
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userServiceImpl);
        auth.authenticationProvider(authenticationProvider());
    }

}
