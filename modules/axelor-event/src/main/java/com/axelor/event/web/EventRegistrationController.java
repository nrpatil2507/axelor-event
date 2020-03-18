package com.axelor.event.web;

import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;
import com.axelor.event.exception.IException;
import com.axelor.event.service.EventRegistrationService;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import java.math.BigDecimal;

public class EventRegistrationController {
  public void setAmount(ActionRequest request, ActionResponse response) throws Exception {
    EventRegistration eventRegistration = request.getContext().asType(EventRegistration.class);
    Event event = checkEvent(request, response);
    BigDecimal amount =
        Beans.get(EventRegistrationService.class).setAmount(event, eventRegistration);
    response.setValue("amount", amount);
  }

  public void checkValidDate(ActionRequest request, ActionResponse response) throws Exception {
    EventRegistration eventRegistration = request.getContext().asType(EventRegistration.class);
    Event event = checkEvent(request, response);
    if (!Beans.get(EventRegistrationService.class)
        .checkEventRegistrationDate(event, eventRegistration)) {
      response.addError("registrationDate", IException.INVALID_REGISTRATION_DATE);
    }
  }

  public void validateEmail(ActionRequest request, ActionResponse response) {
    EventRegistration eventRegistration = request.getContext().asType(EventRegistration.class);
    if (eventRegistration.getEmail() != null) {
      if (!Beans.get(EventRegistrationService.class).checkEmail(eventRegistration)) {
        response.addError("email", IException.INVALID_EMAIL);
      }
    }
  }

  public void checkEventData(ActionRequest request, ActionResponse response) {
    Event event = checkEvent(request, response);
    if (!Beans.get(EventRegistrationService.class).checkCapacity(event)) {
      response.setValue("event", "");
      response.setValue("registrationDate", " ");
      response.setValue("email", " ");
      response.setReload(true);
      response.setError(IException.CAPACITY_EXCEEDS);
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

  public void updateEventData(ActionRequest request, ActionResponse response) {
    EventRegistration eventRegistration = request.getContext().asType(EventRegistration.class);
    Event event = checkEvent(request, response);
    try {
      Beans.get(EventRegistrationService.class).setTotalAmount(event, eventRegistration);
    } catch (Exception e) {
      response.setError(e.getMessage());
    }
  }
}
