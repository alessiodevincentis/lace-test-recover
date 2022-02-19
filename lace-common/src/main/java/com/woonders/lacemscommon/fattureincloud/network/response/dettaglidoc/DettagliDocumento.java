package com.woonders.lacemscommon.fattureincloud.network.response.dettaglidoc;

import com.google.gson.annotations.SerializedName;

import lombok.*;

/**
 * Created by Emanuele on 26/02/2017. Ho messo per ora solo il campo per
 * recuperare il link del pdf, tutto il resto non ci interessa
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class DettagliDocumento {

	@SerializedName("link_doc")
	String linkDocumentoPdf;
}
