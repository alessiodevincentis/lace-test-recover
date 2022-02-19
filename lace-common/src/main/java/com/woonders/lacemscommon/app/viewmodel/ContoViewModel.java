package com.woonders.lacemscommon.app.viewmodel;

import java.util.Date;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = { "codiceConto" }, callSuper = false)
@ToString
public class ContoViewModel extends AbstractBaseViewModel {

	private long codiceConto;
	private String banca;
	private String sportello;
	private String iban;
	private Date dataConto;
	private boolean stipendio;

	@Override
	protected long getIdToCompare() {
		return codiceConto;
	}
}
