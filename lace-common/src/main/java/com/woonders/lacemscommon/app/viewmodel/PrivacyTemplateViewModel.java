package com.woonders.lacemscommon.app.viewmodel;


import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@ToString
public class PrivacyTemplateViewModel extends AbstractBaseViewModel  {

	private long id;
	private String provenienza;
	private String fornitoreLead;
    private String privacyText;
    private Date dateCreate = new Date();
	
	@Override
    protected long getIdToCompare() {
        return id;
    }
}
