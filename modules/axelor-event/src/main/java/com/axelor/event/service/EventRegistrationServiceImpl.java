package com.axelor.event.service;

import com.axelor.event.db.Discount;
import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;
import com.axelor.event.db.repo.EventRepository;
import com.axelor.event.exception.IException;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;

public class EventRegistrationServiceImpl implements EventRegistrationService {
	@Inject
	EventRepository eventRepo;

	@Override
	public BigDecimal setAmount(Event event, EventRegistration eventRegistration) throws Exception {
		LocalDate registrationCloseDate = event.getRegistrationClose();
		LocalDate eventRegistrationDate = eventRegistration.getRegistrationDate().toLocalDate();
		BigDecimal amount = event.getEventFees();
		BigDecimal maximumDiscount = BigDecimal.ZERO;
		List<Discount> discountList = event.getDiscountList();
		if (CollectionUtils.isNotEmpty(discountList)) {
			for (Discount discount : discountList) {
				if (!eventRegistrationDate.isAfter(registrationCloseDate.minusDays(discount.getBeforeDays()))) {
					BigDecimal discountAmount = discount.getDiscountAmount();
					if (discountAmount.compareTo(maximumDiscount) == 1) {
						maximumDiscount = discountAmount;
					}
				}
			}
			amount = amount.subtract(maximumDiscount);
		}
		return amount;
	}

	@Override
	public Boolean checkEventRegistrationDate(Event event, EventRegistration eventReg) throws Exception {
		LocalDate regClose = event.getRegistrationClose();
		LocalDate regOpen = event.getRegistrationOpen();
		LocalDate regDate = eventReg.getRegistrationDate().toLocalDate();
		if (regOpen != null && regClose != null && regDate != null) {
			if (regDate.isAfter(regClose) || regDate.isBefore(regOpen)) {
				return false;
			} else {
				return true;
			}
		} else {
			throw new Exception(IException.MISSING_REGISTRATION_DATES);
		}
	}

	@Override
	public Boolean checkCapacity(Event event) {
		if (event.getTotalEntry() < event.getCapacity())
			return true;
		return false;
	}

	@Override
	@Transactional
	public void setTotalAmount(Event event, EventRegistration eventRegistration) {
		BigDecimal totalDiscount = event.getTotalDiscount();
		BigDecimal amountCollected = event.getAmountCollected();
		BigDecimal amountToAdd = eventRegistration.getAmount();
		if (!event.getEventFees().equals(amountToAdd) && !amountToAdd.equals(BigDecimal.ZERO)) {
			totalDiscount = totalDiscount.add(event.getEventFees().subtract(amountToAdd));
		}
		event.setTotalEntry(event.getTotalEntry() + 1);
		amountCollected = amountCollected.add(amountToAdd);
		event.setTotalDiscount(totalDiscount);
		event.setAmountCollected(amountCollected);
		eventRepo.save(event);
	}

	@Override
	@Transactional
	public void removeData(EventRegistration eventRegistration) {
		Event event = eventRegistration.getEvent();
		event.setTotalEntry(event.getTotalEntry() - 1);
		BigDecimal amountCollected = event.getAmountCollected().subtract(eventRegistration.getAmount());
		BigDecimal totalDiscount = event.getEventFees().multiply(new BigDecimal(event.getTotalEntry()))
				.subtract(amountCollected);
		event.setAmountCollected(amountCollected);
		event.setTotalDiscount(totalDiscount);
		eventRepo.save(event);
	}

	@Override
	public Boolean checkEmail(EventRegistration eventRegistration) {
		if (eventRegistration.getEmail() != null) {
			if (!eventRegistration.getEmail().matches("[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}")) {
				return false;
			}
		}
		return true;
	}
}
