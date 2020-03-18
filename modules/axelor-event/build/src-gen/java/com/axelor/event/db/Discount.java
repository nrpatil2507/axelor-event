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
import com.axelor.db.annotations.Widget;
import com.google.common.base.MoreObjects;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "EVENT_DISCOUNT")
public class Discount extends AuditableModel {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EVENT_DISCOUNT_SEQ")
  @SequenceGenerator(
    name = "EVENT_DISCOUNT_SEQ",
    sequenceName = "EVENT_DISCOUNT_SEQ",
    allocationSize = 1
  )
  private Long id;

  @Widget(title = "Before Days")
  private Integer beforeDays = 0;

  @Widget(title = "Discount Percent")
  private BigDecimal discountPercent = BigDecimal.ZERO;

  @Widget(title = "Discount Amount")
  private BigDecimal discountAmount = BigDecimal.ZERO;

  @Widget(title = "Attributes")
  @Basic(fetch = FetchType.LAZY)
  @Type(type = "json")
  private String attrs;

  public Discount() {}

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public Integer getBeforeDays() {
    return beforeDays == null ? 0 : beforeDays;
  }

  public void setBeforeDays(Integer beforeDays) {
    this.beforeDays = beforeDays;
  }

  public BigDecimal getDiscountPercent() {
    return discountPercent == null ? BigDecimal.ZERO : discountPercent;
  }

  public void setDiscountPercent(BigDecimal discountPercent) {
    this.discountPercent = discountPercent;
  }

  public BigDecimal getDiscountAmount() {
    return discountAmount == null ? BigDecimal.ZERO : discountAmount;
  }

  public void setDiscountAmount(BigDecimal discountAmount) {
    this.discountAmount = discountAmount;
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
    if (!(obj instanceof Discount)) return false;

    final Discount other = (Discount) obj;
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
        .add("beforeDays", getBeforeDays())
        .add("discountPercent", getDiscountPercent())
        .add("discountAmount", getDiscountAmount())
        .omitNullValues()
        .toString();
  }
}
