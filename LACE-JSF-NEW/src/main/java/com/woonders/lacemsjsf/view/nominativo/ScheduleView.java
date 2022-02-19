package com.woonders.lacemsjsf.view.nominativo;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import com.woonders.lacemsjsf.util.FacesUtil;

@ManagedBean
@ViewScoped
public class ScheduleView implements Serializable {

	private ScheduleModel eventModel;
	private ScheduleEvent event = new DefaultScheduleEvent();

	public ScheduleModel getEventModel() {
		return eventModel;
	}

	public void setEventModel(final ScheduleModel eventModel) {
		this.eventModel = eventModel;
	}

	public ScheduleEvent getEvent() {
		return event;
	}

	public void setEvent(final ScheduleEvent event) {
		this.event = event;
	}

	@PostConstruct
	public void init() {
		eventModel = new DefaultScheduleModel();
	}

	public void addEvent(final ActionEvent actionEvent) {
		if (event.getId() == null)
			eventModel.addEvent(event);
		else
			eventModel.updateEvent(event);

		event = new DefaultScheduleEvent();
	}

	public void onEventSelect(final SelectEvent selectEvent) {
		event = (ScheduleEvent) selectEvent.getObject();
	}

	public void onDateSelect(final SelectEvent selectEvent) {
		event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
	}

	public void onEventMove(final ScheduleEntryMoveEvent event) {
		FacesUtil.addMessage("Event moved " +
				"Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());
	}

	public void onEventResize(final ScheduleEntryResizeEvent event) {
		FacesUtil.addMessage("Event resized " +
				"Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());

	}

}
