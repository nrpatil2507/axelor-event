package com.axelor.event.web;

import com.axelor.event.db.Discount;
import com.axelor.event.db.Event;
import com.axelor.event.service.EventService;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class EventController {
  public void calculateAmounts(ActionRequest request, ActionResponse response) {
    Event event = request.getContext().asType(Event.class);
    event = Beans.get(EventService.class).calculateTotalAmount(event);
    response.setValues(event);
  }

  public void checkBeforeDays(ActionRequest request, ActionResponse response) throws Exception {
    Discount discount = request.getContext().asType(Discount.class);
    Event event = request.getContext().getParent().asType(Event.class);
    if (!Beans.get(EventService.class).validateBeforeDays(event, discount)) {
      response.setError("Invalid Before days");
    }
  }
}
