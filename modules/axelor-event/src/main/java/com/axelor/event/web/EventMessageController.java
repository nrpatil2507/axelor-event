package com.axelor.event.web;

import com.axelor.event.db.Event;
import com.axelor.event.service.EventMessageServiceImpl;
import com.axelor.exception.AxelorException;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import javax.mail.MessagingException;

public class EventMessageController {

  public void sendMailAll(ActionRequest request, ActionResponse response)
      throws MessagingException {
    Event event = request.getContext().asType(Event.class);
    String model = request.getModel();
    try {
      Beans.get(EventMessageServiceImpl.class).sendMessageAll(event, model);
      response.setFlash("send mail successfully");
    } catch (AxelorException e) {
      response.setNotify("Email is already send");
    }
  }
}
