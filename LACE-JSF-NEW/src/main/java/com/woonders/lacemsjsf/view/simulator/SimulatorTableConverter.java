package com.woonders.lacemsjsf.view.simulator;

import com.woonders.lacemscommon.app.viewmodel.SimulatorTableViewModel;
import com.woonders.lacemscommon.service.SimulatorTableService;
import com.woonders.lacemscommon.service.impl.SimulatorTableServiceImpl;
import com.woonders.lacemsjsf.util.BaseConverter;
import org.springframework.util.StringUtils;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter("simulatorTableConverter")
public class SimulatorTableConverter extends BaseConverter {

    private final SimulatorTableService simulatorTableService;

    public SimulatorTableConverter() {
        simulatorTableService = findBean(SimulatorTableServiceImpl.JSF_NAME);
    }

    @Override
    public Object getAsObject(final FacesContext context, final UIComponent component, final String value) {
        if (!StringUtils.isEmpty(value)) {
            final SimulatorTableViewModel simulatorTableViewModel = simulatorTableService.findOne(Long.parseLong(value));
            if (simulatorTableViewModel != null) {
                return simulatorTableViewModel;
            } else {
                throw new ConverterException(
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "SimulatorTable Not Valid"));
            }

        } else {
            return null;
        }
    }

    @Override
    public String getAsString(final FacesContext context, final UIComponent component, final Object value) {
        return String.valueOf(((SimulatorTableViewModel) value).getId());
    }
}
