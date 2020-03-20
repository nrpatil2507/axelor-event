package com.axelor.event.service;

import com.axelor.event.db.Discount;
import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EventServiceImpl implements EventService {

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
			event.setTotalEntry(event.getEventRegistrationList().size());
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
	@Override
	public Event updateDiscount(Event event)
	{
		if (event.getDiscountList() != null) {
			List<Discount> discountList = event.getDiscountList();
			List<Discount> updatedDiscountList = new ArrayList<>();

			for (Discount discount : discountList) {
				discount.setDiscountAmount(
						event.getEventFees().multiply(discount.getDiscountPercent()).divide(new BigDecimal(100)));
				updatedDiscountList.add(discount);
			}
			event.setDiscountList(updatedDiscountList);
		}
		event = calculateTotalAmount(event);
		return event;
	}
}
