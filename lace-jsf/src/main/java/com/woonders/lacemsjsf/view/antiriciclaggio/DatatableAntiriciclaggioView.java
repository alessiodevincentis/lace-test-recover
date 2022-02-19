package com.woonders.lacemsjsf.view.antiriciclaggio;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.woonders.lacemscommon.app.model.ClientePratica;
import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.viewmodel.FileViewModel;
import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.exception.NoFileFoundException;
import com.woonders.lacemscommon.service.AntiriciclaggioService;
import com.woonders.lacemscommon.service.FileService;
import com.woonders.lacemscommon.service.impl.AmazonS3FileServiceImpl;
import com.woonders.lacemscommon.service.impl.AntiriciclaggioServiceImpl;
import com.woonders.lacemsjsf.config.PropertiesUtil;
import com.woonders.lacemsjsf.db.app.config.RequestTenantIdentifierResolver;
import com.woonders.lacemsjsf.util.FacesUtil;
import com.woonders.lacemsjsf.util.HttpSessionUtil;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils.Parametro;
import com.woonders.lacemsjsf.view.aziendaselection.AziendaSelectionView;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ManagedBean(name = "datatableAntiriciclaggioView")
@ViewScoped
@Getter
@Setter
public class DatatableAntiriciclaggioView implements Serializable {

	@ManagedProperty(PassaggioParametriUtils.JSF_NAME)
	private PassaggioParametriUtils passaggioParametriUtils;
	@ManagedProperty(HttpSessionUtil.JSF_NAME)
	private HttpSessionUtil httpSessionUtil;
	@ManagedProperty(AmazonS3FileServiceImpl.JSF_NAME)
	private FileService fileService;
	@ManagedProperty(PropertiesUtil.JSF_NAME)
	private PropertiesUtil propertiesUtil;
	@ManagedProperty(RequestTenantIdentifierResolver.JSF_NAME)
	private RequestTenantIdentifierResolver requestTenantIdentifierResolver;
	private LazyDataModel<ClientePratica> clientePraticaLazyDataModel;
	@ManagedProperty(AntiriciclaggioServiceImpl.JSF_NAME)
	private AntiriciclaggioService antiriciclaggioService;
	@ManagedProperty(AziendaSelectionView.JSF_NAME)
	private AziendaSelectionView aziendaSelectionView;

	@PostConstruct
	public void init() {
		String cf = (String) passaggioParametriUtils.getParametri().get(Parametro.CF_ANTIRICICLAGGIO_PARAMETER);
		Date dataInizio = (Date) passaggioParametriUtils.getParametri().get(Parametro.DATACARICAMENTO_START_ANTIRICICLAGGIO_PARAMETER);
		Date dataFine = (Date) passaggioParametriUtils.getParametri().get(Parametro.DATACARICAMENTO_END_ANTIRICICLAGGIO_PARAMETER);
		AntiriciclaggioService.TipoRicercaAntiriciclaggio tipoRicerca = (AntiriciclaggioService.TipoRicercaAntiriciclaggio) passaggioParametriUtils.getParametri().get(Parametro.TIPORICERCA_ANTIRICICLAGGIO_PARAMETER);
		clientePraticaLazyDataModel = new LazyDataModel<ClientePratica>() {

			@Override
			public List<ClientePratica> load(int first, int pageSize, String sortField, SortOrder sortOrder,
													 Map<String, Object> filters) {
				Map<QueryDSLHelper.TableField, Object> tableFilters = new HashMap<>();
				for (Map.Entry<String, Object> entry : filters.entrySet()) {
					tableFilters.put(QueryDSLHelper.TableField.fromValue(entry.getKey()), entry.getValue());
				}

				Role.RoleName roleNameWhichReadsAntiriciclaggio;
				if (httpSessionUtil.hasAuthority(Role.RoleName.ANTI_RICICLAGGIO_OUT)) {
					roleNameWhichReadsAntiriciclaggio = Role.RoleName.ANTI_RICICLAGGIO_OUT;
				} else {
					roleNameWhichReadsAntiriciclaggio = httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.CLIENTI, OperatorViewModel.PermissionType.READ);
				}

				ViewModelPage<ClientePratica> viewModelPage = antiriciclaggioService.findPraticheAntiriciclaggio(cf, dataInizio, dataFine, tipoRicerca, first, pageSize, sortField,
						com.woonders.lacemscommon.db.QueryDSLHelper.SortOrder.valueOf(sortOrder.name()), tableFilters, aziendaSelectionView.getCurrentAziendaId(),
						roleNameWhichReadsAntiriciclaggio, httpSessionUtil.getOperatorId());

				// jsf di merda che non prendi i long!
				setRowCount((int) viewModelPage.getTotalElements());
				return viewModelPage.getContent();
			}

		};
	}

	public void preProcessPDF(final Object document) throws IOException, BadElementException, DocumentException {
		final Document pdf = (Document) document;
		pdf.setPageSize(PageSize.A2.rotate());
	}

	public void showAntiriciclaggioPdf(final long codicePratica) {
		try {
			List<FileViewModel> fileViewModelList = fileService.getFileListInCategory(requestTenantIdentifierResolver.getTenantIdentifier(),
					codicePratica,
					FileService.FileCategory.ANTIRICICLAGGIO);
			passaggioParametriUtils.getParametri().put(PassaggioParametriUtils.Parametro.TENANT_ID,
					requestTenantIdentifierResolver.getTenantIdentifier());
			passaggioParametriUtils.getParametri().put(PassaggioParametriUtils.Parametro.CLIENTE_PRATICA_ID,
					codicePratica);
			passaggioParametriUtils.getParametri().put(PassaggioParametriUtils.Parametro.FILE_CATEGORY,
					FileService.FileCategory.ANTIRICICLAGGIO);
			//it will always be of size 1
			passaggioParametriUtils.getParametri().put(PassaggioParametriUtils.Parametro.FILE_NAME,
					fileViewModelList.get(0).getFileName());
			FacesUtil.openNewWindow("/app/secure/antiriciclaggio/antiriciclaggiopdfview.xhtml");
		} catch (final NoFileFoundException e) {
			FacesUtil.addMessage(propertiesUtil.getMsgFileViewNoFileFound(), FacesMessage.SEVERITY_WARN);
		}

	}

}
