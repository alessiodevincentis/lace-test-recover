package com.woonders.lacemsjsf.app.model;

import java.util.Date;

import lombok.Data;

@Data
public class Occupazione {
	private String impiego;
	private String occupazione;
	private String qualifica;
	private String ente;
	private String categoria;
	private Date inizioOccupazioneAssunzione;

}
