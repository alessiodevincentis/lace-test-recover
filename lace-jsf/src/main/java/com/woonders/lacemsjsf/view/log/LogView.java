package com.woonders.lacemsjsf.view.log;

import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.viewmodel.NominativoLogViewModel;
import com.woonders.lacemscommon.app.viewmodel.creator.NominativoLogViewModelCreator;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.service.LogService;
import com.woonders.lacemscommon.service.impl.LogServiceImpl;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.woonders.lacemsjsf.view.log.LogView.NAME;

/**
 * Created by Emanuele on 08/02/2017.
 */
@Getter
@Setter
@ViewScoped
@ManagedBean(name = NAME)
public class LogView implements Serializable {

	public static final String NAME = "logView";
	public static final String JSF_NAME = "#{" + NAME + "}";

	private LazyDataModel<NominativoLogViewModel> nominativoLogViewModelLazyDataModel;

	@ManagedProperty(LogServiceImpl.JSF_NAME)
	private LogService logService;
	@ManagedProperty(PassaggioParametriUtils.JSF_NAME)
	private PassaggioParametriUtils passaggioParametriUtils;
	private long selectedNominativoId;
	private long selectedPraticaId;
	private LogService.TipoLog tipoLog;
	private String currentCommentToShow;

	@PostConstruct
	public void init() {
		LogDTO logDTO = ((LogDTO) passaggioParametriUtils.getParametri()
				.get(PassaggioParametriUtils.Parametro.ID_NOMINATIVO_ON_FORM_NOMINATIVO_PARAMETER));

		selectedNominativoId = logDTO.getNominativoId();
		selectedPraticaId = logDTO.getPraticaId();
		tipoLog = logDTO.getTipoLog();

		nominativoLogViewModelLazyDataModel = new LazyDataModel<NominativoLogViewModel>() {

			@Override
			public List<NominativoLogViewModel> load(int first, int pageSize, String sortField, SortOrder sortOrder,
					Map<String, Object> filters) {
				Map<QueryDSLHelper.TableField, Object> tableFilters = new HashMap<>();
				for (Map.Entry<String, Object> entry : filters.entrySet()) {
					tableFilters.put(QueryDSLHelper.TableField.fromValue(entry.getKey()), entry.getValue());
				}

				ViewModelPage<NominativoLogViewModel> viewModelPage;

				if (LogService.TipoLog.NOMINATIVO.equals(tipoLog)) {
					viewModelPage = logService.getNominativoLog(selectedNominativoId, first, pageSize, sortField,
							com.woonders.lacemscommon.db.QueryDSLHelper.SortOrder.valueOf(sortOrder.name()),
							tableFilters);
				} else {
					viewModelPage = logService.getPraticaLog(selectedNominativoId, selectedPraticaId, first, pageSize,
							sortField, com.woonders.lacemscommon.db.QueryDSLHelper.SortOrder.valueOf(sortOrder.name()),
							tableFilters);
				}

				// jsf di merda che non prendi i long!
				setRowCount((int) viewModelPage.getTotalElements());
				return viewModelPage.getContent();
			}

		};
	}

	public boolean isRenderedCommandLinkAltro(String comment) {
		return !(StringUtils.isEmpty(comment) || comment.length() <= NominativoLogViewModelCreator.MAX_COMMENT_LENGTH);
	}

}
