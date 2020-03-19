package com.axelor.event.service;

import com.axelor.event.db.Discount;
import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;
import com.axelor.event.db.repo.EventRepository;
import com.google.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDate;

public class EventServiceImpl implements EventService {

	@Inject
	EventRegistrationService eventRegistrationService;
	@Inject
	EventRepository eventRepo;

	@Override
	public Event calculateTotalAmount(Event event) {
		BigDecimal totalAmount = BigDecimal.ZERO;
		BigDecimal totalDiscount = BigDecimal.ZERO;
		BigDecimal amount=BigDecimal.ZERO;
		BigDecimal diff;
		if (event.getEventRegistrationList() != null) {
			for (EventRegistration eventRegistration : event.getEventRegistrationList()) {
				amount = eventRegistration.getAmount();
				if(!amount.equals(BigDecimal.ZERO))
				{
				diff = event.getEventFees().subtract(amount);
				totalDiscount = totalDiscount.add(diff);
				}
				totalAmount = totalAmount.add(eventRegistration.getAmount());
			}
			event.setTotalDiscount(totalDiscount);
			event.setAmountCollected(totalAmount);
		}
		return event;
	}

	@Override
	public Boolean validateBeforeDays(Event event, Discount discount) throws Exception {
		if (event.getRegistrationOpen() == null || event.getRegistrationClose() == null) {
			return false;
		}
		LocalDate registrationOpen = event.getRegistrationOpen();
		LocalDate registrationCloseDate = event.getRegistrationClose();
		Integer beforeDays = discount.getBeforeDays();
		if (registrationCloseDate.minusDays(beforeDays).isBefore(registrationOpen)) {
			return false;
		}
		return true;
	}
}
