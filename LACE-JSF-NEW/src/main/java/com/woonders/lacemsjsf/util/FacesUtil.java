package com.woonders.lacemsjsf.util;

import com.woonders.lacemscommon.config.Constants;
import org.primefaces.context.RequestContext;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Emanuele on 16/09/2016.
 */
public class FacesUtil {

    public static void addMessage(final String message) {
        addMessage(message, FacesMessage.SEVERITY_INFO, MessageShownType.SUMMARY);
    }

    public static void addMessage(final String message, final FacesMessage.Severity severity) {
        addMessage(message, severity, MessageShownType.SUMMARY);
    }

    private static void addMessage(final String message, final FacesMessage.Severity severity,
                                   final MessageShownType messageShownType) {
        switch (messageShownType) {
            case SUMMARY:
                addMessage(null, severity, message, "");
                break;
            case DETAIL:
                addMessage(null, severity, "", message);
                break;
        }
    }

    public static void addMessage(final String clientId, final FacesMessage.Severity severity, final String summary,
                                  final String message) {
        // http://stackoverflow.com/questions/13685633/how-to-show-faces-message-in-the-redirected-page
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        if (severity == null) {
            FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(message));
        } else {
            FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(severity, summary, message));
        }
    }

    public static void showMessageInDialog(final FacesMessage.Severity severity, final String summary,
                                           final String detail) {
        RequestContext.getCurrentInstance().showMessageInDialog(new FacesMessage(severity, summary, detail));
    }

    public static void closeDialog(final DialogType dialogType) {
        RequestContext.getCurrentInstance().execute("PF('" + dialogType.getValue() + "').hide();");
    }

    public static void openDialog(final DialogType dialogType) {
        RequestContext.getCurrentInstance().execute("PF('" + dialogType.getValue() + "').show();");
    }

    public static void openNewWindow(final String windowToOpen) {
        RequestContext.getCurrentInstance().execute("openNewWindow('" + windowToOpen + "')");
    }

    public static void refresh() {
    	FacesUtil.execute("window.location.reload();");
    	//FacesUtil.refreshAfter(0);
    }
    
    public static void refreshAfter(int milliseconds) {
    	class MyTimerTask extends TimerTask  {
    		RequestContext context;

    	     public MyTimerTask(RequestContext param) {
    	         this.context = param;
    	     }

    	     @Override
    	     public void run() {
    	         context.execute("window.location.reload();");
    	     }
    	}
    	
    	Timer t = new java.util.Timer();
    	t.schedule( new MyTimerTask(RequestContext.getCurrentInstance()), milliseconds); 	
    }
    
    public static void update(final String componentid) {
        RequestContext.getCurrentInstance().update(componentid);
    }

    public static void updateSmsInfo() {
        update("smsTotalPanel");
        update("smsDetailPanel");
    }

    public static void execute(final String commandToExecute) {
        RequestContext.getCurrentInstance().execute(commandToExecute);
    }

    /**
     * https://stackoverflow.com/questions/14687910/how-to-avoid-prerenderview-method-call-in-ajax-request?noredirect=1
     * Pensavo fosse utile invece no, ma lo salvo perche magari prima o poi ci serve
     *
     * @return
     */
    public static boolean isNotFirstRequest() {
        return FacesContext.getCurrentInstance().getPartialViewContext().isAjaxRequest() || FacesContext.getCurrentInstance().isPostback();
    }

    public static String getSourceComponentName() {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("javax.faces.source");
    }

    public static boolean doesPageNeedToBeRefreshed() {
        final String source = getSourceComponentName();
        return source == null || Constants.SELECT_ONE_MENU_AZIENDA_NAME.equals(source);
    }

    public static void redirect(final String path) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect(path);
    }

    // questi possono essere usati al posto di PassaggioParametriUtils, da vedere
    public static void putIntoFlash(final String key, final Object value) {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put(key, value);
    }

    public static boolean flashContains(final String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getFlash().containsKey(key);
    }

    public static Object getFromFlash(final String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getFlash().get(key);
    }
    //fine metodi per Flash

    public enum MessageShownType {
        SUMMARY, DETAIL
    }

    public enum DialogType {
        PREVENTIVO_TRATTENUTE("dlgprevtrattenute"), DATASOURCE("newDataSourceDialog"), OPERATOR("newOperatorDialog"),
        FILE_LIST("fileListDialog"), PRATICHE_DA_PERFEZIONARE("praticheDaPerf"),
        INVIA_MAIL_FATTURAZIONE("dialogInviaMailFatturazioneWidget"),
        CONFIRM_SAVE_CALENDARIO_ALTRI_APPUNTAMENTI_PRESENTI("confirmSaveAppuntamentoDialogWidget"),
        MODIFICA_PERMESSI("modificaPermessiDialog"), SIMULATOR_DIALOG_NOMINATIVO("dlgsimulatornominativo"),
        SIMULATOR_DIALOG_PREVENTIVO_CLIENTE("dlgsimulatorpreventivocliente"),
        DIALOG_CONTESTA_NOMINATIVO("dialogContestaNominativo"), DIALOG_ASSEGNA_NOMINATIVO("dialogAssegnaNominativo");
    	
    	
        private final String value;

        DialogType(final String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

}
