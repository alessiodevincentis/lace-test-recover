package com.woonders.lacemscommon.app.viewmodel;

import java.time.LocalDateTime;

import com.woonders.lacemscommon.db.entity.Campagna;

import lombok.*;

/**
 * Created by Emanuele on 28/03/2017.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = { "id" }, callSuper = false)
@ToString
public class CampagnaViewModel extends AbstractBaseViewModel {

	private long id;
	private String name;
	private String description;
	private LocalDateTime creationDateTime;
	private LocalDateTime sentDateTime;
	private String text;
	private Campagna.CampagnaType campagnaType;
	private boolean campagnaImport;
	private OperatorViewModel creatorOperatorViewModel;
	private OperatorViewModel senderOperatorViewModel;
	private FiltroCampagnaMarketingViewModel filtroCampagnaMarketingViewModel;
	private int totaleDestinatari;
	private int totaleMessaggi;

	@Override
	protected long getIdToCompare() {
		return id;
	}
}
