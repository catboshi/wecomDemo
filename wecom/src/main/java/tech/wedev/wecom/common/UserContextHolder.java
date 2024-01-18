package tech.wedev.wecom.common;

import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tech.wedev.wecom.entity.common.User;
import tech.wedev.wecom.utils.SpringRedisUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class UserContextHolder {

    private static final ThreadLocal<User> CONTEXT = new ThreadLocal<>();

    public static void setUser(User user) {
        CONTEXT.set(user);
    }

    public static User getCurrentUser() {
        return CONTEXT.get();
    }

    public static void refresh() {
        CONTEXT.remove();
    }

    public static String getCurrentLanguage() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User user = (User) request.getAttribute("curUser");
        if (ObjectUtils.isEmpty(user)) {
            user = SpringRedisUtil.getUserObj(request.getHeader("userId"));
        }
        if (ObjectUtils.isEmpty(user)) {
            user = getCurrentUser();
        }
        return SpringRedisUtil.getUserLanguage(user.getUsername());
    }

    public static String getCurUserId() {
        return Optional.ofNullable(getCurrentUser()).map(User::getUserid).orElse(null);
    }

    public static String getCurUsername() {
        return Optional.ofNullable(getCurrentUser()).map(User::getUsername).orElse(null);
    }

}
