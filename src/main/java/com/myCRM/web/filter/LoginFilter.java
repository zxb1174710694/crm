package com.myCRM.web.filter;

import com.myCRM.Settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String path = request.getServletPath();

        //第一个是登录页面，第二个是验证登录页面的servlet。这两个直接放行，其他的要验证
        if ("/login.jsp".equals(path) || "/setting/user/login.do".equals(path)){
            filterChain.doFilter(servletRequest,servletResponse);
        }else {

            User user = (User) request.getSession().getAttribute("user");
            //到这里说明访问的不是登录页面
            if (user != null){
                //说明登录过了
                filterChain.doFilter(servletRequest,servletResponse);
            }else {
                //跳转到登录页面
                System.out.println("跳转登录");

                //获取项目名
                String ServiceName = request.getContextPath();
                System.out.println("项目名："+ServiceName);
                response.sendRedirect(ServiceName+"/login.jsp");
            }
        }
    }
}
