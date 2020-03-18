package com.axelor.event.exception;

public interface IException {

  /** Mail account service and controller */
  static final String CAPACITY_EXCEEDS = /*$$(*/ "Total Event Capacity is Exceeds" /*)*/;

  public static final String REGISTRATION_EXCEED_CAPACITY = /*$$(*/ "Event capacity exceeds" /*)*/;
  static final String INVALID_DATES = /*$$(*/ "Invalid Dates" /*)*/;
  static final String INVALID_REGISTRATION_DATE = /*$$(*/
      "Registration Date must be Between Open and Close Date" /*)*/;
  static final String INVALID_BEFORE_DAYS = /*$$(*/ "Invalid Before Days" /*)*/;
  static final String MISSING_DATES = /*$$(*/ "MISSING DATES" /*)*/;
  static final String MISSING_REGISTRATION_DATES = /*$$(*/
      "MISSING REGISTRATION OPEN OR CLOSE DATES" /*)*/;
  static final String INVALID_EMAIL = /*$$(*/ "Your Entered Email Is Not Valid" /*)*/;
}
