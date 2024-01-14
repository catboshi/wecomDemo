package tech.wedev.wecom.interceptor;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import tech.wedev.wecom.annos.PassToken;
import tech.wedev.wecom.utils.SpringRedisUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 认证拦截器
 * @version 1.0
 */
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Value("${secretKey}")
    private String secretKey;

    /**
     *  预处理回调方法，实现处理器的预处理，第三个参数为响应的处理器，自定义Controller，
     *  返回值为true表示继续流程（如调用下一个拦截器或处理器）或者接着执行postHandle()和afterCompletion(),
     *  false表示流程中断，不会继续调用其他的拦截器或处理器，中断执行
     *  1.从 http 请求头中取出 token
     *  2.判断是否映射到方法
     *  3.检查是否有passtoken注释，有则跳过认证
     *  4.检查有没有需要用户登录的注解，有则需要取出并验证
     *  5.认证通过则可以访问，不通过会报相关错误信息
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setContentType("text/html;charset=UTF-8");
        String token = "eyJhbGci0iJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb2RlIjoiSFoxMDAzMzYyMCJ9.IyjtPAVSAbCHcJ1KJg5G3AvPIc9F3UoOIK14PUUuFPY";
        //request.getHeader("Authorization");//从 http 请求头中取出 token
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        //检查是否有passtoken注释，有则跳过认证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        } else {
            // 执行认证
            if (token == null) {
                response.getWriter().print(ResultUtil.error("没有登录凭证"));
                return false;
            }
            // 验证 token
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secretKey)).build();
            try {
                jwtVerifier.verify(token);
            } catch (Exception e) {
                response.getWriter().print(ResultUtil.error("token认证不通过"));
                return false;
            }
            // 获取 token 中的 userId
            String userId;
            try {
                userId = JWT.decode(token).getClaim("code").asString();
                User user = SpringRedisUtil.getUserObj(userId);
                if (ObjectUtils.isEmpty(user)) {
                    response.getWriter().print(ResultUtil.error("用户不存在"));
                    return false;
                }
                request.setAttribute("curUser", user);
            } catch (Exception e) {
                response.getWriter().print(ResultUtil.error("找不到用户ID"));
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * 后处理回调方法，实现处理器的后处理DispatcherServlet进行视图返回渲染之前进行调用），
     * 此时我们可以通过modelAndView（模型和视图对象）对模型数据进行处理或对视图进行处理，modelAndView也可能为null.
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // Do nothing because of X and Y.
    }

    /**
     * 整个请求处理完毕回调方法，该方法也是需要当前对应的Interceptor的preHandle()的返回值为true时才会执行，
     * 也就是在DispatcherServlet渲染了对应的视图之后执行，用于进行资源清理。
     * 整个请求处理完毕回调方法，如性能监控中我们可以在此记录结束时间并输出消耗时间，
     * 还可以进行一些资源清理，类似于tru-catch-finally中的finally，但仅调用处理器执行链中
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // Do nothing because of X and Y.
    }
}
