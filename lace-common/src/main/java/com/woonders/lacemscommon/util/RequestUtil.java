package com.woonders.lacemscommon.util;

import com.woonders.lacemscommon.config.Constants;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Created by Emanuele on 31/03/2017.
 */
@Component
public class RequestUtil {

    public static final String NAME = "requestUtil";
    public static final String JSF_NAME = "#{" + NAME + "}";

    public String getTenantName() {
        final ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes();
        // controlliamo anche che ci sia "host"... in caso eccezione? da
        // pensarci!
        if (servletRequestAttributes != null && servletRequestAttributes.getRequest() != null
                && !StringUtils.isEmpty(servletRequestAttributes.getRequest().getHeader("host"))) {
            return servletRequestAttributes.getRequest().getHeader("host").replace(Constants.APP_DOMAIN, "")
                    .replace(".", "");
        } else {
            return null;
        }
    }
}
