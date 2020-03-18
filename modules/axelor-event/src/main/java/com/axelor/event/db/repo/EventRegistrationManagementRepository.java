package com.axelor.event.db.repo;

import com.axelor.event.db.EventRegistration;
import com.axelor.event.service.EventRegistrationService;
import com.google.inject.Inject;

public class EventRegistrationManagementRepository extends EventRegistrationRepository {
  @Inject EventRegistrationService eventRegistrationService;

  @Override
  public void remove(EventRegistration entity) {
    if (entity.getEvent() != null) {
      eventRegistrationService.removeData(entity);
    }
    super.remove(entity);
  }
}
