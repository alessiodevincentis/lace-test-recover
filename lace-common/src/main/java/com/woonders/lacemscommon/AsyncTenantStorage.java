package com.woonders.lacemscommon;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Emanuele on 25/04/2017.
 */
@Component
@Setter
@Getter
public class AsyncTenantStorage {

	private String tenantToUseName;

}
