package tech.wedev.wecom.context;

import tech.wedev.wecom.enums.JwtKeyEnum;
import tech.wedev.wecom.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TokenContextHolder {

    private TokenContextHolder() {

    }

    private static final ThreadLocal<TokenContext> tokenThreadLocal = new ThreadLocal<>();

    public static void set(TokenContext tokenContext) {
        tokenThreadLocal.set(tokenContext);
    }

    public static TokenContext get() {
        return tokenThreadLocal.get();
    }

    public static void remove() {
        tokenThreadLocal.remove();
    }

    public static <T> T getValue(JwtKeyEnum keyEnum, Class<T> valClass) {
        return Optional.ofNullable(TokenContextHolder.get()).map(a -> (T) a.getKey(keyEnum.getCode())).orElse(null);
    }

    public static List<String> getCorpIds() {
        String value = getValue(JwtKeyEnum.CORP_ID, String.class);
        return Optional.ofNullable(value).filter(StringUtil::isNotBlank).map(a -> {
            List<String> corpIds = new ArrayList<>();
            corpIds.add(a);
            corpIds.add("SYSTEM");
            return corpIds;
        }).orElse(new ArrayList<>());
    }
}
