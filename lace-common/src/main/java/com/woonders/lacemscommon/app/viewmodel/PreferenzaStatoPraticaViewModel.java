package com.woonders.lacemscommon.app.viewmodel;

import com.woonders.lacemscommon.db.entityenum.StatoPratica;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = { "id" }, callSuper = false)
public class PreferenzaStatoPraticaViewModel extends AbstractBaseViewModel {

	private long id;
	private StatoPratica nomeStatoPratica;
	private int giorniNotifica;
	private AziendaViewModel aziendaViewModel;

	@Override
	protected long getIdToCompare() {
		return id;
	}
}
