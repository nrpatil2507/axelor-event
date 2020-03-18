package com.axelor.event.web;

import com.axelor.event.service.EventImportRegistrationServiceImpl;
import com.axelor.inject.Beans;
import com.axelor.meta.db.MetaFile;
import com.axelor.meta.db.repo.MetaFileRepository;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.common.io.Files;
import com.google.inject.Inject;
import java.io.IOException;
import java.util.LinkedHashMap;

public class EventImportController {
  @Inject EventImportRegistrationServiceImpl eventImportRegistrationServiceImpl;

  public void importEventData(ActionRequest request, ActionResponse response) throws IOException {
    @SuppressWarnings("unchecked")
    LinkedHashMap<String, Object> map =
        (LinkedHashMap<String, Object>) (request.getContext().get("metaFile"));
    MetaFile dataFile =
        Beans.get(MetaFileRepository.class).find(((Integer) map.get("id")).longValue());
    Integer id = (Integer) request.getContext().get("_id");

    if (!Files.getFileExtension(dataFile.getFileName()).equals("csv")) {
      response.setError("Select CSV file Only");
    } else {
      eventImportRegistrationServiceImpl.importEventData(dataFile, id);
      response.setNotify("Data Imported");
    }
  }
}
