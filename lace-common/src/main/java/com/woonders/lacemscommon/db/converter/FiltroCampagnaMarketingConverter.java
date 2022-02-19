package com.woonders.lacemscommon.db.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.woonders.lacemscommon.app.viewmodel.FiltroCampagnaMarketingViewModel;

/**
 * Created by Emanuele on 27/03/2017.
 */
@Converter
public class FiltroCampagnaMarketingConverter implements AttributeConverter<FiltroCampagnaMarketingViewModel, String> {

	private static final Gson gson = new GsonBuilder().create();

	@Override
	public String convertToDatabaseColumn(FiltroCampagnaMarketingViewModel filtroCampagnaMarketingViewModel) {
		if (filtroCampagnaMarketingViewModel != null) {
			return gson.toJson(filtroCampagnaMarketingViewModel);
		}
		return null;
	}

	@Override
	public FiltroCampagnaMarketingViewModel convertToEntityAttribute(String s) {
		if (!StringUtils.isEmpty(s)) {
			return gson.fromJson(s, FiltroCampagnaMarketingViewModel.class);
		}
		return null;
	}
}
