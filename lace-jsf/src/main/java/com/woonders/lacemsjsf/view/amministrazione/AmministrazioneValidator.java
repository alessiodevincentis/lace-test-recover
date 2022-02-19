package com.woonders.lacemsjsf.view.amministrazione;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import com.woonders.lacemscommon.app.viewmodel.AmministrazioneViewModel;
import com.woonders.lacemsjsf.config.PropertiesUtil;
import com.woonders.lacemsjsf.view.util.ConstantResolverView;

@ManagedBean
@RequestScoped
public class AmministrazioneValidator implements Validator, Serializable {

	@ManagedProperty(ConstantResolverView.JSF_NAME)
	private ConstantResolverView constantResolverView;

	@ManagedProperty(PropertiesUtil.JSF_NAME)
	private PropertiesUtil propertiesUtil;

	@Override
	public void validate(final FacesContext context, final UIComponent component, final Object value)
			throws ValidatorException {
		final List<AmministrazioneViewModel> amministrazioneViewModelList = (List<AmministrazioneViewModel>) value;
		if (amministrazioneViewModelList != null) {
			if (amministrazioneViewModelList.size() > constantResolverView.getAmministrazioneListMaxSize()) {
				final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						propertiesUtil.getMsgErrorMaxAmministrazioni(), null);
				throw new ValidatorException(msg);
			} else if (amministrazioneViewModelList.size() > 1) {
				final Set<AmministrazioneViewModel> amministrazioneViewModelUniqueHashSet = new HashSet<>();
				amministrazioneViewModelList.stream()
						.filter(amministrazioneViewModel -> !amministrazioneViewModelUniqueHashSet
								.add(amministrazioneViewModel))
						.collect(Collectors.toSet());
				if (amministrazioneViewModelUniqueHashSet.size() != amministrazioneViewModelList.size()) {
					final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
							propertiesUtil.getMsgErrorStessaAmministrazione(), null);
					throw new ValidatorException(msg);
				}
			}
		}
	}

	public void setConstantResolverView(final ConstantResolverView constantResolverView) {
		this.constantResolverView = constantResolverView;
	}

	public void setPropertiesUtil(final PropertiesUtil propertiesUtil) {
		this.propertiesUtil = propertiesUtil;
	}
}