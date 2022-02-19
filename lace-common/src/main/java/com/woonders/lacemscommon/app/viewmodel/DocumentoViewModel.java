package com.woonders.lacemscommon.app.viewmodel;

import java.util.Date;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = { "codiceDoc" }, callSuper = false)
@ToString
public class DocumentoViewModel extends AbstractBaseViewModel {

	private long codiceDoc;
	private String tipoDocumento;
	private String numeroDoc;
	private Date rilascioDoc;
	private String luogoRilascioDoc;
	private String provinciaRilascio;
	private String nazioneRilascio;
	private Date scadenzaDoc;

	@Override
	protected long getIdToCompare() {
		return codiceDoc;
	}
}
