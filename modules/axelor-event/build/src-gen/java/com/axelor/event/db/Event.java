/*
 * Axelor Business Solutions
 *
 * Copyright (C) 2005-2020 Axelor (<http://axelor.com>).
 *
 * This program is free software: you can redistribute it and/or  modify
 * it under the terms of the GNU Affero General Public License, version 3,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axelor.event.db;

import com.axelor.auth.db.AuditableModel;
import com.axelor.db.annotations.NameColumn;
import com.axelor.db.annotations.Widget;
import com.google.common.base.MoreObjects;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.Type;

@Entity
@Table(
  name = "EVENT_EVENT",
  indexes = {@Index(columnList = "reference"), @Index(columnList = "venue")}
)
public class Event extends AuditableModel {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EVENT_EVENT_SEQ")
  @SequenceGenerator(name = "EVENT_EVENT_SEQ", sequenceName = "EVENT_EVENT_SEQ", allocationSize = 1)
  private Long id;

  @Widget(title = "Reference")
  @NameColumn
  private String reference;

  @Widget(title = "Start Date")
  private LocalDateTime startDate;

  @Widget(title = "End Date")
  private LocalDateTime endDate;

  @Widget(title = "Venue")
  @ManyToOne(
    fetch = FetchType.LAZY,
    cascade = {CascadeType.PERSIST, CascadeType.MERGE}
  )
  private Address venue;

  @Widget(title = "Registration Open")
  private LocalDate registrationOpen;

  @Widget(title = "Registration Close")
  private LocalDate registrationClose;

  @Widget(title = "Capacity")
  private Integer capacity = 0;

  @Widget(title = "Event Fees")
  private BigDecimal eventFees = BigDecimal.ZERO;

  @Widget(title = "Description", multiline = true)
  private String description;

  @Widget(title = "Discounts")
  @OneToMany(
    fetch = FetchType.LAZY,
    cascade = {CascadeType.PERSIST, CascadeType.MERGE}
  )
  private List<Discount> discountList;

  @Widget(title = "Event Registrations")
  @OneToMany(
    fetch = FetchType.LAZY,
    mappedBy = "event",
    cascade = CascadeType.ALL,
    orphanRemoval = true
  )
  private List<EventRegistration> eventRegistrationList;

  @Widget(title = "Total Entry", readonly = true)
  private Integer totalEntry = 0;

  @Widget(title = "Amount Collected", readonly = true)
  private BigDecimal amountCollected = BigDecimal.ZERO;

  @Widget(title = "Total Discount", readonly = true)
  private BigDecimal totalDiscount = BigDecimal.ZERO;

  @Widget(title = "Attributes")
  @Basic(fetch = FetchType.LAZY)
  @Type(type = "json")
  private String attrs;

  public Event() {}

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public String getReference() {
    return reference;
  }

  public void setReference(String reference) {
    this.reference = reference;
  }

  public LocalDateTime getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDateTime startDate) {
    this.startDate = startDate;
  }

  public LocalDateTime getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDateTime endDate) {
    this.endDate = endDate;
  }

  public Address getVenue() {
    return venue;
  }

  public void setVenue(Address venue) {
    this.venue = venue;
  }

  public LocalDate getRegistrationOpen() {
    return registrationOpen;
  }

  public void setRegistrationOpen(LocalDate registrationOpen) {
    this.registrationOpen = registrationOpen;
  }

  public LocalDate getRegistrationClose() {
    return registrationClose;
  }

  public void setRegistrationClose(LocalDate registrationClose) {
    this.registrationClose = registrationClose;
  }

  public Integer getCapacity() {
    return capacity == null ? 0 : capacity;
  }

  public void setCapacity(Integer capacity) {
    this.capacity = capacity;
  }

  public BigDecimal getEventFees() {
    return eventFees == null ? BigDecimal.ZERO : eventFees;
  }

  public void setEventFees(BigDecimal eventFees) {
    this.eventFees = eventFees;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<Discount> getDiscountList() {
    return discountList;
  }

  public void setDiscountList(List<Discount> discountList) {
    this.discountList = discountList;
  }

  /**
   * Add the given {@link Discount} item to the {@code discountList}.
   *
   * @param item the item to add
   */
  public void addDiscountListItem(Discount item) {
    if (getDiscountList() == null) {
      setDiscountList(new ArrayList<>());
    }
    getDiscountList().add(item);
  }

  /**
   * Remove the given {@link Discount} item from the {@code discountList}.
   *
   * <p>It sets {@code item.null = null} to break the relationship.
   *
   * @param item the item to remove
   */
  public void removeDiscountListItem(Discount item) {
    if (getDiscountList() == null) {
      return;
    }
    getDiscountList().remove(item);
  }

  /**
   * Clear the {@code discountList} collection.
   *
   * <p>It sets {@code item.null = null} to break the relationship.
   */
  public void clearDiscountList() {
    if (getDiscountList() != null) {
      getDiscountList().clear();
    }
  }

  public List<EventRegistration> getEventRegistrationList() {
    return eventRegistrationList;
  }

  public void setEventRegistrationList(List<EventRegistration> eventRegistrationList) {
    this.eventRegistrationList = eventRegistrationList;
  }

  /**
   * Add the given {@link EventRegistration} item to the {@code eventRegistrationList}.
   *
   * <p>It sets {@code item.event = this} to ensure the proper relationship.
   *
   * @param item the item to add
   */
  public void addEventRegistrationListItem(EventRegistration item) {
    if (getEventRegistrationList() == null) {
      setEventRegistrationList(new ArrayList<>());
    }
    getEventRegistrationList().add(item);
    item.setEvent(this);
  }

  /**
   * Remove the given {@link EventRegistration} item from the {@code eventRegistrationList}.
   *
   * @param item the item to remove
   */
  public void removeEventRegistrationListItem(EventRegistration item) {
    if (getEventRegistrationList() == null) {
      return;
    }
    getEventRegistrationList().remove(item);
  }

  /**
   * Clear the {@code eventRegistrationList} collection.
   *
   * <p>If you have to query {@link EventRegistration} records in same transaction, make sure to
   * call {@link javax.persistence.EntityManager#flush() } to avoid unexpected errors.
   */
  public void clearEventRegistrationList() {
    if (getEventRegistrationList() != null) {
      getEventRegistrationList().clear();
    }
  }

  public Integer getTotalEntry() {
    return totalEntry == null ? 0 : totalEntry;
  }

  public void setTotalEntry(Integer totalEntry) {
    this.totalEntry = totalEntry;
  }

  public BigDecimal getAmountCollected() {
    return amountCollected == null ? BigDecimal.ZERO : amountCollected;
  }

  public void setAmountCollected(BigDecimal amountCollected) {
    this.amountCollected = amountCollected;
  }

  public BigDecimal getTotalDiscount() {
    return totalDiscount == null ? BigDecimal.ZERO : totalDiscount;
  }

  public void setTotalDiscount(BigDecimal totalDiscount) {
    this.totalDiscount = totalDiscount;
  }

  public String getAttrs() {
    return attrs;
  }

  public void setAttrs(String attrs) {
    this.attrs = attrs;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) return false;
    if (this == obj) return true;
    if (!(obj instanceof Event)) return false;

    final Event other = (Event) obj;
    if (this.getId() != null || other.getId() != null) {
      return Objects.equals(this.getId(), other.getId());
    }

    return false;
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", getId())
        .add("reference", getReference())
        .add("startDate", getStartDate())
        .add("endDate", getEndDate())
        .add("registrationOpen", getRegistrationOpen())
        .add("registrationClose", getRegistrationClose())
        .add("capacity", getCapacity())
        .add("eventFees", getEventFees())
        .add("description", getDescription())
        .add("totalEntry", getTotalEntry())
        .add("amountCollected", getAmountCollected())
        .add("totalDiscount", getTotalDiscount())
        .omitNullValues()
        .toString();
  }
}
