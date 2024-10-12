package com.kunj.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
@Slf4j
public class LogInterceptor  implements HandlerInterceptor {

   @Override
   public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) {
       UUID uuid = UUID.randomUUID();
       httpServletRequest.setAttribute("start" , System.currentTimeMillis());
       httpServletRequest.setAttribute("request-id", uuid );
       log.info( "{} - calling {}" , uuid , httpServletRequest.getRequestURI() );
       return true;
   }

   @Override
   public void postHandle( HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) throws Exception {
       log.info( "{} - response in {}ms",
               request.getAttribute("request-id"),
               System.currentTimeMillis() - (long) request.getAttribute("start") );
   }

   @Override
   public void afterCompletion(HttpServletRequest request,
                               HttpServletResponse response,
                               Object handler,
                               Exception exception) throws Exception {
       log.info( "{} - completed in {}ms",
               request.getAttribute("request-id"),
               System.currentTimeMillis() - (long) request.getAttribute("start") );
   }
}