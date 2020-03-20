package com.axelor.event.service;

import com.axelor.event.db.Discount;
import com.axelor.event.db.Event;

public interface EventService {
  Boolean validateBeforeDays(Event event, Discount discount) throws Exception;

  Event calculateTotalAmount(Event event);

Event updateDiscount(Event event);
}
