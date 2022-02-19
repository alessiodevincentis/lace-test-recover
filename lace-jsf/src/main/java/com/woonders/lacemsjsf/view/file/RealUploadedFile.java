package com.woonders.lacemsjsf.view.file;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Emanuele on 17/01/2017.
 */
@Getter
@Setter
@Builder
/**
 * Ho fatto questa classe perche JSF distrugge gli UploadedFile quando finisce
 * la request ma a noi ci servono per un tempo piu lungo per processarli tutti
 * insieme
 */
public class RealUploadedFile {

	private String fileName;
	private byte[] content;
	private String contentType;
	private long size;
}