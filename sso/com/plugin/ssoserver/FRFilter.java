package com.plugin.ssoserver;
import java.io.IOException;  
import java.io.PrintStream;  
import javax.servlet.Filter;  
import javax.servlet.FilterChain;  
import javax.servlet.FilterConfig;  
import javax.servlet.ServletException;  
import javax.servlet.ServletRequest;  
import javax.servlet.ServletResponse;  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpSession;  
  
public class FRFilter implements Filter  
{  
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)  
            throws IOException, ServletException  
    {  
        HttpServletRequest re = (HttpServletRequest)req;  
        HttpSession session = re.getSession(false);  
        //获取session中保留的信息  
        String user = (String)session.getAttribute("edu.yale.its.tp.cas.client.filter.user");  
  
        re.getSession().setAttribute("fr_username", user);  
        re.getSession().setAttribute("fr_password", user);  
        filterChain.doFilter(req, res);  
        System.out.println("sds:::"+user);  
    }  
  
    public void init(FilterConfig filterconfig)  
            throws ServletException  
    {  
    }  
  
    public void destroy()  
    {  
    }  
}  