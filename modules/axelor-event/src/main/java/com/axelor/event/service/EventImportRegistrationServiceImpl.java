package com.axelor.event.service;

import com.axelor.data.csv.CSVImporter;
import com.axelor.event.db.EventRegistration;
import com.axelor.event.exception.IException;
import com.axelor.inject.Beans;
import com.axelor.meta.MetaFiles;
import com.axelor.meta.db.MetaFile;
import com.google.common.io.Files;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.IOUtils;

public class EventImportRegistrationServiceImpl {

  private File getConfigXmlFile() {

    File configFile = null;
    try {
      configFile = File.createTempFile("inputconfig", ".xml");
      InputStream bindFileInputStream =
          this.getClass().getResourceAsStream("/data-import/event-config.xml");
      if (bindFileInputStream != null) {
        FileOutputStream outputStream = new FileOutputStream(configFile);
        IOUtils.copy(bindFileInputStream, outputStream);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return configFile;
  }

  private File getDataCsvFile(MetaFile dataFile) {

    File csvFile = null;
    try {
      File tempDir = Files.createTempDir();
      csvFile = new File(tempDir, "event_registration.csv");
      Files.copy(MetaFiles.getPath(dataFile).toFile(), csvFile);

    } catch (Exception e) {
      e.printStackTrace();
    }
    return csvFile;
  }

  public void importEventData(MetaFile dataFile, Integer id) throws IOException {
    File configXmlFile = this.getConfigXmlFile();
    Map<String, Object> context = new HashMap<String, Object>();
    context.put("event", id);
    File csvFile = getDataCsvFile(dataFile);
    CSVImporter importer =
        new CSVImporter(configXmlFile.getAbsolutePath(), csvFile.getParent().toString());
    importer.setContext(context);
    importer.run();
  }

  public Object importRegistration(Object bean, Map<String, Object> values) {
    assert bean instanceof EventRegistration;
    EventRegistration eventRegistration = (EventRegistration) bean;
    try {
      if (eventRegistration.getEvent().getTotalEntry()
          < eventRegistration.getEvent().getCapacity()) {
        if (Beans.get(EventRegistrationService.class).checkEmail(eventRegistration)) {
          if (Beans.get(EventRegistrationService.class)
              .checkEventRegistrationDate(eventRegistration.getEvent(), eventRegistration)) {
            Integer totalEntry = eventRegistration.getEvent().getTotalEntry();
            totalEntry++;
            eventRegistration.getEvent().setTotalEntry(totalEntry);
            eventRegistration.setAmount(
                Beans.get(EventRegistrationService.class)
                    .setAmount(eventRegistration.getEvent(), eventRegistration));
            Beans.get(EventRegistrationService.class)
                .setTotalAmount(eventRegistration.getEvent(), eventRegistration);
            return eventRegistration;
          } else {
            System.err.println(IException.INVALID_REGISTRATION_DATE);
          }
        } else {
          System.err.println(IException.INVALID_EMAIL);
        }
      } else {
        System.err.println(IException.CAPACITY_EXCEEDS);
      }
    } catch (Exception e) {
      System.out.println("Error when importing : {}");
    }
    return null;
  }
}
