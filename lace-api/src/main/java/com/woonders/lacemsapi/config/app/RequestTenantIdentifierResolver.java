package com.woonders.lacemsapi.config.app;

import com.woonders.lacemsapi.config.Constants;
import com.woonders.lacemsapi.controller.DevisProxPushLeadController;
import com.woonders.lacemsapi.controller.DevisProxPushLeadDisputeControllerV2;
import com.woonders.lacemsapi.controller.PushSmsDeliveryReceiptsController;
import com.woonders.lacemscommon.config.TenantManager;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.*;



/**
 * Created by emanuele on 23/12/16.
 */
@Component
@Slf4j
public class RequestTenantIdentifierResolver
        implements CurrentTenantIdentifierResolver, ApplicationListener<ContextRefreshedEvent> {

    public static final String NAME = "requestTenantIdentifierResolver";
    public static final String JSF_NAME = "#{" + NAME + "}";

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private TenantManager tenantManager;

    private boolean contextLoaded;

    @Override
    public String resolveCurrentTenantIdentifier() {
        return getTenantIdentifier();
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }

    public String getTenantIdentifier() {
        //Throwable t = new Throwable();
        //log.info("StackeTrace: " + ExceptionUtils.getStackTrace(t));

        if (contextLoaded && httpServletRequest != null) {
            final String servletPath = httpServletRequest.getServletPath();
            log.info("--------------------------------------------servletPath: "+servletPath);
            log.info("--------------------------------------------QUERY STRING URL CALLED:" + httpServletRequest.getQueryString());
            String tenantName = null;
            if (!servletPath.endsWith("/error")) {

                if (servletPath.contains((DevisProxPushLeadDisputeControllerV2.PUSH_LEAD_DISPUTE_URL))){
                    return tenantManager
                            .getTenantDtoFromDevisProxId(
                                    httpServletRequest.getParameter(DevisProxPushLeadDisputeControllerV2.DEVISPROX_TENANT_ID))
                            .getTenantName();
                    
                }
                    
                if (servletPath.endsWith(DevisProxPushLeadController.PUSH_LEAD_URL)) {
                    return tenantManager
                            .getTenantDtoFromDevisProxId(
                                    httpServletRequest.getParameter(DevisProxPushLeadController.DEVISPROX_TENANT_ID))
                            .getTenantName();
                } else if (servletPath.endsWith(PushSmsDeliveryReceiptsController.PUSH_SMS_DELIVERY_RECEIPTS_URL)) {
                    // questo campo e nella forma tenant:MessageType
                    tenantName = httpServletRequest.getParameter(PushSmsDeliveryReceiptsController.CUSTOM_STRING)
                            .split(":")[0];
                    if (tenantManager.isTenantAvailable(tenantName)) {
                        return tenantName;
                    } else {
                        throw new RuntimeException("No tenant with the following name exists: " + tenantName);
                    }
                } else {
                    tenantName = httpServletRequest.getParameter("tenantId");
                    if (!StringUtils.isEmpty(tenantName)) {
                        return tenantName;
                    } else {
                        throw new RuntimeException("TenantId cannot be null or empty!");
                    }
                }
            }
        }

        return Constants.EMPTY_DEFAULT_DB_NAME;
    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent contextRefreshedEvent) {
        contextLoaded = true;
    }
}
