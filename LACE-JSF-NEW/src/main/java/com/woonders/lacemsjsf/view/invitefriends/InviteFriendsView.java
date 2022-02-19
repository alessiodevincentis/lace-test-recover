package com.woonders.lacemsjsf.view.invitefriends;

import com.woonders.lacemscommon.app.viewmodel.AziendaViewModel;
import com.woonders.lacemscommon.exception.UnableToSendEmailException;
import com.woonders.lacemscommon.service.AziendaService;
import com.woonders.lacemscommon.service.impl.AziendaServiceImpl;
import com.woonders.lacemscommon.util.LaceMailSender;
import com.woonders.lacemsjsf.config.PropertiesUtil;
import com.woonders.lacemsjsf.util.ErrorConverter;
import com.woonders.lacemsjsf.util.FacesUtil;
import com.woonders.lacemsjsf.util.HttpSessionUtil;
import lombok.Getter;
import lombok.Setter;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.text.MessageFormat;

import static com.woonders.lacemsjsf.view.invitefriends.InviteFriendsView.NAME;

@ManagedBean(name = NAME)
@ViewScoped
@Getter
@Setter
public class InviteFriendsView implements Serializable {

    public static final String NAME = "inviteFriendsView";
    public static final String JSF_NAME = "#{" + NAME + "}";
    private String recipient;
    @ManagedProperty(LaceMailSender.JSF_NAME)
    private LaceMailSender laceMailSender;
    @ManagedProperty(HttpSessionUtil.JSF_NAME)
    private HttpSessionUtil httpSessionUtil;
    @ManagedProperty(AziendaServiceImpl.JSF_NAME)
    private AziendaService aziendaService;
    @ManagedProperty(PropertiesUtil.JSF_NAME)
    private PropertiesUtil propertiesUtil;
    @ManagedProperty(ErrorConverter.JSF_NAME)
    private ErrorConverter errorConverter;

    public void sendMailToFriend() {
        final AziendaViewModel aziendaViewModel = aziendaService.getOne(httpSessionUtil.getAziendaId());
        final String agency = aziendaViewModel.getNomeAzienda();
        try {
            laceMailSender.sendEmail(recipient, agency, aziendaViewModel.getEmail(), propertiesUtil.getMsgInviteFriendsSubject(), MessageFormat.format(propertiesUtil.getMsgInviteFriendsBody(), agency, null));
            FacesUtil.addMessage(propertiesUtil.getMsgSendMailOk());
        } catch (final UnableToSendEmailException e) {
            FacesUtil.addMessage(errorConverter.convertSendEmailError(e.getUnableToSendEmailError()), FacesMessage.SEVERITY_ERROR);
        }
        recipient = null;
    }

}
