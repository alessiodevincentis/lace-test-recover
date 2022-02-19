package com.woonders.lacemsjsf.view.infoaccount;

import com.woonders.lacemscommon.app.viewmodel.InfoAccountViewModel;
import com.woonders.lacemscommon.service.InfoAccountService;
import com.woonders.lacemscommon.service.impl.InfoAccountServiceImpl;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;

@ManagedBean(name = InfoAccountView.NAME)
@Setter
@Getter
@ViewScoped
public class InfoAccountView implements Serializable {

    public static final String NAME = "infoAccountView";
    public static final String JSF_NAME = "#{" + NAME + "}";
    private static final String ENABLED = "Abilitato";
    private static final String DISABLED = "Disabilitato";

    @ManagedProperty(InfoAccountServiceImpl.JSF_NAME)
    private InfoAccountService infoAccountService;
    private InfoAccountViewModel infoAccountViewModel;

    @PostConstruct
    public void init() {
        infoAccountViewModel = infoAccountService.getOne();
    }

    public String getStateOfFunctionalityFromBoolean(final boolean isActive) {
        return isActive ? ENABLED : DISABLED;
    }

    public String getVersionName() {
        if (infoAccountViewModel != null && infoAccountViewModel.getVersion() != null) {
            return infoAccountViewModel.getVersion().getName();
        }
        return null;
    }

    public int getVersionValue() {
        if (infoAccountViewModel != null && infoAccountViewModel.getVersion() != null) {
            return infoAccountViewModel.getVersion().getValue();
        }
        return 0;
    }

    public long getNumberOfUsersAvailable() {
        if (infoAccountViewModel != null && infoAccountViewModel.getVersion() != null && infoAccountViewModel.getVersion().getValue() != 0) {
            return infoAccountViewModel.getVersion().getValue() - infoAccountService.getOperatorsNumber() + infoAccountViewModel.getAdditionalUsers();
        }
        return 0;
    }

    public long getNumberOfUsersUsed() {
        if (infoAccountViewModel != null && infoAccountViewModel.getVersion() != null && infoAccountViewModel.getVersion().getValue() != 0) {
            return infoAccountService.getOperatorsNumber();
        }
        return 0;
    }
}
