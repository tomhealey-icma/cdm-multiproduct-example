package com.finxis.util;

import cdm.base.datetime.AdjustableDate;
import cdm.base.datetime.AdjustableDates;
import cdm.base.datetime.AdjustableOrRelativeDate;
import cdm.base.datetime.AdjustableOrRelativeDates;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.metafields.FieldWithMetaDate;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CdmDates {

  public AdjustableOrRelativeDate createAdjustableDate(String dateStr){

    DateTimeFormatter formatter  = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz");
    ZonedDateTime maturityDate = ZonedDateTime.parse(dateStr, formatter);
    Date date = Date.of(maturityDate.getYear(), maturityDate.getMonthValue(), maturityDate.getDayOfMonth());


    AdjustableOrRelativeDate adjDate = AdjustableOrRelativeDate.builder()
            .setAdjustableDate(AdjustableDate.builder()
                    .setAdjustedDate(FieldWithMetaDate.builder()
                            .setValue(date)))
            .build();

    return adjDate;
  }

  public AdjustableOrRelativeDates createAdjustableDates(String dateStr){

    DateTimeFormatter formatter  = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz");
    ZonedDateTime maturityDate = ZonedDateTime.parse(dateStr, formatter);
    Date date = Date.of(maturityDate.getYear(), maturityDate.getMonthValue(), maturityDate.getDayOfMonth());


    AdjustableOrRelativeDates adjDate = AdjustableOrRelativeDates.builder()
            .setAdjustableDates(AdjustableDates.builder()
                    .setAdjustedDate(List.of(FieldWithMetaDate.builder()
                            .setValue(this.createDate(dateStr)))))
            .build();

    return adjDate;
  }


  public Date createDate(String dateStr){

    DateTimeFormatter formatter  = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz");
    ZonedDateTime zDate = ZonedDateTime.parse(dateStr, formatter);
    Date date= Date.of(zDate.getYear(), zDate.getMonthValue(), zDate.getDayOfMonth());


    return date;
  }

  public Date createCurrentDate(){

    DateTimeFormatter formatter  = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz");
    ZonedDateTime zDate = ZonedDateTime.now();
    Date date= Date.of(zDate.getYear(), zDate.getMonthValue(), zDate.getDayOfMonth());


    return date;
  }
}
