package com.woonders.lacemscommon.util;

import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.laceenum.AnalyticsDateEnum;
import com.woonders.lacemscommon.service.OperatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class AnalyticsUtil {

    public static final String NAME = "analyticsUtil";
    public static final String JSF_NAME = "#{" + NAME + "}";

    private static final int ZERO = 0;
    private static final int MINUS_ONE = -1;
    private static final int MINUS_THIRTY = -30;
    private static final int MINUS_SIXTY = -60;
    private static final int MINUS_NINETY = -90;

    @Autowired
    private DateConversionUtil dateConversionUtil;
    @Autowired
    private OperatorService operatorService;


    public LocalDate getFromDateFormPresetDate(final AnalyticsDateEnum analyticsDateEnum) {
        final LocalDate now = LocalDate.now();
        final LocalDate from = dateConversionUtil.addOrSubstractDays(now, MINUS_THIRTY);
        if (analyticsDateEnum != null) {
            switch (analyticsDateEnum) {
                case LAST_30:
                    return from;
                case CURRENT_MONTH:
                    return dateConversionUtil.startMonth(now, ZERO);
                case LAST_60:
                    return dateConversionUtil.addOrSubstractDays(now, MINUS_SIXTY);
                case LAST_90:
                    return dateConversionUtil.addOrSubstractDays(now, MINUS_NINETY);
                case CURRENT_YEAR:
                    return dateConversionUtil.startYear(now, ZERO);
                case LAST_YEAR:
                    return dateConversionUtil.startYear(now, MINUS_ONE);
            }
        }
        return from;
    }

    public LocalDate getToDateFormPresetDate(final AnalyticsDateEnum analyticsDateEnum) {
        final LocalDate now = LocalDate.now();
        if (analyticsDateEnum != null) {
            switch (analyticsDateEnum) {
                case LAST_30:
                case LAST_60:
                case LAST_90:
                    return now;
                case CURRENT_MONTH:
                    return dateConversionUtil.endMonth(now, ZERO);
                case CURRENT_YEAR:
                    return dateConversionUtil.endYear(now, ZERO);
                case LAST_YEAR:
                    return dateConversionUtil.endYear(now, MINUS_ONE);
            }
        }
        return now;
    }

    public List<OperatorViewModel> getOperatorListByOperatoriSelezionatiAndRolename(final Role.RoleName readRoleName,
                                                                                    final List<String> operatoriSelezionati,
                                                                                    final long operatorId, final long aziendaId,
                                                                                    final Long currentAziendaId) {
        final List<OperatorViewModel> operatorViewModelList = new ArrayList<>();
        if (operatoriSelezionati != null && !operatoriSelezionati.isEmpty()) {
            for (final String username : operatoriSelezionati) {
                operatorViewModelList.add(operatorService.findByUsernameViewModel(username));
            }
        } else {
            switch (readRoleName) {
                case NOMINATIVI_READ_OWN:
                case CLIENTI_READ_OWN:
                    operatorViewModelList.add(operatorService.getOne(operatorId));
                    break;
                case NOMINATIVI_READ_ALL:
                case CLIENTI_READ_ALL:
                    operatorViewModelList.addAll(operatorService.findAppOperatorListByAziendaId(aziendaId));
                    break;
                case NOMINATIVI_READ_SUPER:
                case CLIENTI_READ_SUPER:
                    operatorViewModelList.addAll(operatorService.findAppOperatorListByAziendaId(currentAziendaId));
            }

        }
        return operatorViewModelList;
    }
}
