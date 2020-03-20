package com.axelor.event.web;

import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;
import com.axelor.event.db.repo.EventRegistrationRepository;
import com.axelor.event.db.repo.EventRepository;
import com.axelor.event.exception.IException;
import com.axelor.event.service.EventRegistrationService;
import com.axelor.i18n.I18n;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

import java.math.BigDecimal;

public class EventRegistrationController {
	@Inject
	EventRepository eventRepo;

	@Inject
	EventRegistrationRepository eventRegistrationrepo;

	public void setAmount(ActionRequest request, ActionResponse response) throws Exception {
		EventRegistration eventRegistration = request.getContext().asType(EventRegistration.class);
		Event event = checkEvent(request, response);
		BigDecimal amount = Beans.get(EventRegistrationService.class).setAmount(event, eventRegistration);
		response.setValue("amount", amount);
	}

	public void checkValidDate(ActionRequest request, ActionResponse response) throws Exception {
		EventRegistration eventRegistration = request.getContext().asType(EventRegistration.class);
		Event event = checkEvent(request, response);
		try {
			if (!Beans.get(EventRegistrationService.class).checkEventRegistrationDate(event, eventRegistration)) {
				response.addError("registrationDate", "Registration date must be between " + event.getRegistrationOpen()
						+ " to " + event.getRegistrationClose());
			}
		} catch (Exception e) {
			response.setError(I18n.get(IException.MISSING_REGISTRATION_DATES));
		}
	}

	public void validateEmail(ActionRequest request, ActionResponse response) {
		EventRegistration eventRegistration = request.getContext().asType(EventRegistration.class);
		if (eventRegistration.getEmail() != null) {
			if (!Beans.get(EventRegistrationService.class).checkEmail(eventRegistration)) {
				response.addError("email", I18n.get(IException.INVALID_EMAIL));
			}
		}
	}

	public void checkEventData(ActionRequest request, ActionResponse response) {
		EventRegistration eventRegistration = request.getContext().asType(EventRegistration.class);
		Event event = checkEvent(request, response);
		if (eventRegistration.getId() == null) {
			if (!Beans.get(EventRegistrationService.class).checkCapacity(event)) {
				response.setReload(true);
				response.setError(I18n.get(IException.CAPACITY_EXCEEDS));
			}
		}
	}

	public Event checkEvent(ActionRequest request, ActionResponse response) {
		EventRegistration eventRegistration = request.getContext().asType(EventRegistration.class);
		Event event = null;
		if (eventRegistration.getEvent() == null && request.getContext().getParent() == null) {
			response.setError("Please Fill Event");
		} else if (request.getContext().getParent() == null) {
			event = eventRegistration.getEvent();
		} else {
			event = request.getContext().getParent().asType(Event.class);
		}
		return event;
	}

	@Transactional
	public void updateEventData(ActionRequest request, ActionResponse response) {
		EventRegistration eventRegistration = request.getContext().asType(EventRegistration.class);
		Event event = checkEvent(request, response);
		try {
			if (eventRegistration.getId() == null) {
				Beans.get(EventRegistrationService.class).setTotalAmount(event, eventRegistration);
			}
		} catch (Exception e) {
			response.setError(e.getMessage());
		}
	}

}
