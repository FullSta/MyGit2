package cn.itcast.bos.utils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class PageEncodingFilter implements Filter {

    private String encoding = "UTF-8";
    protected FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        //本过滤器默认编码是UTF-8，但也可以在web.xml配置文件里设置自己需要的编码
        if(filterConfig.getInitParameter("encoding") != null)
            encoding = filterConfig.getInitParameter("encoding");
    }

    @Override
    public void doFilter(ServletRequest srequset, ServletResponse sresponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)srequset;
        request.setCharacterEncoding(encoding);
        chain.doFilter(srequset, sresponse);
    }

    @Override
    public void destroy() {
        this.encoding = null;
    }
}
