package edu.rice.comp504.model.util;

import junit.framework.TestCase;

import java.util.Date;

public class DateCasterTest extends TestCase {

    public void testStrToDate() {
        Date date = DateCaster.StrToDate("2021-11-05");
        assertEquals(date.getYear(), 2021 - 1900);
        assertEquals(date.getMonth(), 11 - 1);
        assertEquals(date.getDate(), 5);
    }

    public void testDateToStr() {
        Date date = DateCaster.StrToDate("2021-11-05");
        String dateStr = DateCaster.DateToStr(date);
        System.out.println(dateStr);
        assertEquals(dateStr, "2021-11-05");
    }

    public void testStringToTime() {
        Date date = DateCaster.StringToTime("2021-11-05 10:30:15");

        //System.out.println(date.toString());
        assertEquals(date.getYear(), 2021 - 1900);
        assertEquals(date.getMonth(), 11 - 1);
        assertEquals(date.getDate(), 5);
        assertEquals(date.getHours(), 10);
        assertEquals(date.getMinutes(), 30);
        assertEquals(date.getSeconds(), 15);
    }

    public void testTimeToString() {
        Date date = DateCaster.StringToTime("2021-11-05 10:30:15");
        String dateStr = DateCaster.TimeToString(date);
        assertEquals("2021-11-05 10:30:15", dateStr);
    }
}