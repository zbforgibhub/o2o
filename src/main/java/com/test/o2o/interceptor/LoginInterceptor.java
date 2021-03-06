package com.test.o2o.interceptor;


import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {
    private static final String [] IgnoredURL = {"/login"};

    /*
    * 该方法在整个请求完成后执行，用于清理资源工作
    * 该方法也只能在当前Interceptor的preHandle方法返回true时才会执行
    */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,Object handler,Exception exception)throws Exception{
        System.out.println("LoginInterceptor afterCompletion -->");
    }


    /*
     * 该方法将在Controller的方法调用后执行，方法中可以对ModelAndView进行操作
     * 该方法也只能在当前Interceptor的preHandle方法返回true时才会执行
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mv)throws Exception{
        System.out.println("LoginInterceptor postHandle -->");
    }


    /*
     * preHandle方法是进行处理器拦截用的，该方法将在Controller处理之前被调用
     * 该方法的返回值为true时拦截器才会往下继续执行，该方法的返回值为false则整个请求结束
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)throws Exception{
        System.out.println("LoginInterceptor preHandle -->");
        //flag用来判断用户登录，默认为false
        boolean flag = false;
        //获取请求的路径
        String path = request.getServletPath();
        System.out.println("ServletPath:" + path);
        //判断是否要拦截
        for (String s : IgnoredURL) {
            if (path.contains(s)) {
                flag = true;
                break;
            }
        }

        //拦截请求
        try {
            Long user = (Long) request.getSession().getAttribute("user");
            if (!flag){
                //用户未登录
                if ( user == null){
                    System.out.println("LoginInterceptor 拦截请求");
                    response.setContentType("text/html; charset=UTF-8");
                    response.getWriter().print("<html><body><script type='text/javascript'>alert('请先登录后再访问！');window.location.href='/o2o/frontend/login'</script></body></html>");
                    response.getWriter().close();
                    //以下重定向和跳转都不生效，原因不明
                    //request.getRequestDispatcher("/o2o/frontend/login").forward(request,response);
                    //response.sendRedirect("/o2o/frontend/login");
                }
                else {
                    System.out.println("LoginInterceptor 放行请求");
                    flag = true;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }
}
