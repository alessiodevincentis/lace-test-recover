package com.woonders.lacemscommon.fattureincloud.network.request.newdoc;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

import lombok.*;

/**
 * Created by Emanuele on 21/02/2017.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ExtraAnagrafica implements Serializable {

	@SerializedName("mail")
	public String mail;
	@SerializedName("tel")
	public String tel;
	@SerializedName("fax")
	public String fax;
}
