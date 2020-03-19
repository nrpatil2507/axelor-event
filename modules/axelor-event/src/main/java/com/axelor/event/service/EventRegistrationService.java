package com.axelor.event.service;

import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;
import java.math.BigDecimal;

public interface EventRegistrationService {
  public BigDecimal setAmount(Event event, EventRegistration eventRegistration) throws Exception;

  Boolean checkEventRegistrationDate(Event event, EventRegistration eventReg) throws Exception;

  Boolean checkCapacity(Event event);

  Boolean checkEmail(EventRegistration eventRegistration);

  void setTotalAmount(Event event, EventRegistration eventRegistration);

  void removeData(EventRegistration eventRegistration);

}
