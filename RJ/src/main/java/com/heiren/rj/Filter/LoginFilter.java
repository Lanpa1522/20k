package com.heiren.rj.Filter;

import com.alibaba.fastjson.JSON;
import com.heiren.rj.Common.BaseContextRj;
import com.heiren.rj.Common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否完成登录
 */
@Slf4j
@WebFilter
public class LoginFilter implements Filter {
//    路径匹配器械
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //1.获取本次请求的URI
        String requestURI = request.getRequestURI(); // /backend/index.html
        log.info("拦截到请求：{}",requestURI);

        String[] uris = new String[] {
            "/employee/login",
                    "/employee/logout",
                    "/backend/**",
                    "/front/**"
        };

        //2.判断本此的请求是否需要处理
        boolean check = check(uris, requestURI);

        //3.如果不需要处理，放行
        if (check) {
            log.info("本次请求{}不需要处理",requestURI);
            filterChain.doFilter(request,response);
            return;
        }
        //4.判断登录状态，如果已登录，则放行
        if (request.getSession().getAttribute("employee") != null) {
            log.info("用户已登录: ID为 {}",request.getSession().getAttribute("employee"));
            Long userId = (Long) request.getSession().getAttribute("employee");
            BaseContextRj.setCurrentId(userId);
            filterChain.doFilter(request,response);
            return;
        }

        //5.如果未登录则返回未登录的结果，通过输出流的方式向客户端页面响应
        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    public boolean check(String[] uris,String requestURI) {
        for (String uri : uris) {
            boolean match = PATH_MATCHER.match(uri,requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
