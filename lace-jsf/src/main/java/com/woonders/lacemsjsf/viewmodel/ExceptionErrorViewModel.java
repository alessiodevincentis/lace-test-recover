package com.woonders.lacemsjsf.viewmodel;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Emanuele on 17/09/2016.
 */
@Getter
@Setter
public class ExceptionErrorViewModel implements Serializable{

	private Exception exception;
	private String message;
}
