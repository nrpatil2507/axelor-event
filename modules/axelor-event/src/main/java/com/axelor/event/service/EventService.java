package com.axelor.event.service;

import com.axelor.event.db.Discount;
import com.axelor.event.db.Event;
import java.math.BigDecimal;

public interface EventService {
  Boolean validateBeforeDays(Event event, Discount discount) throws Exception;

  Event calculateTotalAmount(Event event);

  BigDecimal computeTotalDiscount(Event event, BigDecimal amountCollected);
}
