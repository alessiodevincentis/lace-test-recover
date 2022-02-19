package com.woonders.lacemscommon.network;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Emanuele on 17/09/2016.
 */
@Component
public class NetworkUtil {

    @Autowired
    private HttpServletRequest request;

    public String getRequestIp() {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

}
