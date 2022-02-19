package com.woonders.lacemscommon.app.viewmodel.creator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.woonders.lacemscommon.app.viewmodel.NominativoLogViewModel;
import com.woonders.lacemscommon.db.entity.NominativoLog;
import com.woonders.lacemscommon.db.entityenum.StatoNominativo;
import com.woonders.lacemscommon.db.entityenum.StatoPratica;
import com.woonders.lacemscommon.service.LogService;

/**
 * Created by Emanuele on 08/02/2017.
 */
@Component
public class NominativoLogViewModelCreator extends AbstractBaseViewModelCreator<NominativoLog, NominativoLogViewModel> {

	public static final int MAX_COMMENT_LENGTH = 100;

	@Autowired
	private OperatorViewModelCreator operatorViewModelCreator;
	@Autowired
	private ClienteViewModelCreator clienteViewModelCreator;
	@Autowired
	private PraticaViewModelCreator praticaViewModelCreator;

	@Override
	public NominativoLog createModel(NominativoLogViewModel viewModel) {
		if (viewModel != null) {
			NominativoLog nominativoLog = NominativoLog.builder().id(viewModel.getId())
					.executionDateTime(viewModel.getExecutionDateTime())
					.operator(operatorViewModelCreator.createModel(viewModel.getOperator()))
					.nominativo(clienteViewModelCreator.createModel(viewModel.getNominativo()))
					.operatoreAssegnato(operatorViewModelCreator.createModel(viewModel.getOperatoreAssegnato()))
					.dataRecall(viewModel.getDataRecall()).commento(viewModel.getCommento())
					.pratica(praticaViewModelCreator.createModel(viewModel.getPratica()))
					.tipoLog(viewModel.getTipoLog()).build();

			if (LogService.TipoLog.NOMINATIVO.equals(viewModel.getTipoLog())) {
				nominativoLog.setStatoNominativo(StatoNominativo.fromValue(viewModel.getStato()));
			} else {
				nominativoLog.setStatoPratica(StatoPratica.fromValue(viewModel.getStato()));
			}
			return nominativoLog;
		}
		return null;
	}

	@Override
	public NominativoLogViewModel createViewModel(NominativoLog model) {
		if (model != null) {
			NominativoLogViewModel nominativoLogViewModel = NominativoLogViewModel.builder().id(model.getId())
					.executionDateTime(model.getExecutionDateTime())
					.operator(operatorViewModelCreator.createViewModel(model.getOperator()))
					.nominativo(clienteViewModelCreator.createViewModel(model.getNominativo()))
					.operatoreAssegnato(operatorViewModelCreator.createViewModel(model.getOperatoreAssegnato()))
					.dataRecall(model.getDataRecall()).commento(model.getCommento())
					.shortCommento(generateShortComment(model.getCommento()))
					.pratica(praticaViewModelCreator.createViewModel(model.getPratica())).tipoLog(model.getTipoLog())
					.build();

			if (LogService.TipoLog.NOMINATIVO.equals(model.getTipoLog())) {
				nominativoLogViewModel.setStato(model.getStatoNominativo().getValue());
			} else {
				nominativoLogViewModel.setStato(model.getStatoPratica().getValue());
			}
			return nominativoLogViewModel;
		}
		return null;
	}

	private String generateShortComment(String comment) {
		if (!StringUtils.isEmpty(comment) && comment.length() > MAX_COMMENT_LENGTH) {
			return StringUtils.truncate(comment, MAX_COMMENT_LENGTH) + "...";
		}
		return comment;
	}
}
