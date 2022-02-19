package com.woonders.lacemsjsf.exception.handler;

import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemsjsf.config.PropertiesUtil;
import com.woonders.lacemsjsf.exception.SessionExpiredException;
import com.woonders.lacemsjsf.util.ExceptionResolverUtil;
import com.woonders.lacemsjsf.util.FacesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.security.access.AccessDeniedException;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by Emanuele on 13/04/2017.
 */
@Slf4j
public class LaceExceptionHandler extends ExceptionHandlerWrapper {

    private ExceptionHandler exceptionHandler;
    private PropertiesUtil propertiesUtil;
    private ExceptionResolverUtil exceptionResolverUtil;

    public LaceExceptionHandler(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        propertiesUtil = facesContext.getApplication().evaluateExpressionGet(facesContext, PropertiesUtil.JSF_NAME, PropertiesUtil.class);
        exceptionResolverUtil = facesContext.getApplication().evaluateExpressionGet(facesContext, ExceptionResolverUtil.JSF_NAME, ExceptionResolverUtil.class);
    }

    @Override
    public ExceptionHandler getWrapped() {
        return exceptionHandler;
    }

    @Override
    public void handle() throws FacesException {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        for (Iterator<ExceptionQueuedEvent> iter = getUnhandledExceptionQueuedEvents().iterator(); iter.hasNext();) {
            Throwable throwable = iter.next().getContext().getException(); // There it is!
            //http://stackoverflow.com/questions/17747175/how-can-i-loop-through-exception-getcause-to-find-root-cause-with-detail-messa
            Throwable rootCause = ExceptionUtils.getRootCause(throwable);

           if(throwable instanceof ViewExpiredException || rootCause instanceof ViewExpiredException) {
                try {
                    facesContext.getExternalContext().redirect(Constants.getSessionExpiredPath(true));
                } catch (IOException e) {
                    log.error("Errore nel redirect con ViewExpiredException", e);
                }
            } else if(throwable instanceof AccessDeniedException || rootCause instanceof AccessDeniedException) {
               log.error("Operazione non consentita: non si dispone dei permessi necessari per eseguirla!", throwable);
               FacesUtil.addMessage("Operazione non consentita: non si dispone dei permessi necessari per eseguirla!", FacesMessage.SEVERITY_ERROR);
           } else if(throwable instanceof SessionExpiredException || rootCause instanceof SessionExpiredException) {
               log.error("Errore imprevisto, session expired!", throwable);
               try {
                   facesContext.getExternalContext().redirect(Constants.getSessionExpiredPath(true));
               } catch (IOException e) {
                   log.error("Errore nel redirect con ViewExpiredException", e);
               }
           } else {
               log.error("Errore imprevisto", throwable);

               // You could redirect to an error page (bad practice).
               // Or you could render a full error page (as OmniFaces does).
               // Or you could show a FATAL faces message.
               // Or you could trigger an oncomplete script.
               // etc..
               FacesUtil.addMessage(exceptionResolverUtil.getMessageToShowFromException(rootCause), FacesMessage.SEVERITY_ERROR);
           }

            facesContext.renderResponse();
            iter.remove();
        }

        getWrapped().handle();
    }
}
