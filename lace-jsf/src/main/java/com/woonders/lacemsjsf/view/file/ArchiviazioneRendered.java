package com.woonders.lacemsjsf.view.file;

import lombok.Builder;
import lombok.Data;

/**
 * Created by Emanuele on 19/11/2016.
 */
@Data
@Builder
public class ArchiviazioneRendered {

	private boolean documentiClienteRendered;
	// questo viene usato dalle sezioni antiriciclaggio, redditi, pratica
	private boolean documentiPraticaRendered;
}
