package seedu.todo.commons.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

import seedu.todo.commons.util.DateUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @@author A0139812A
 * Test cases for {@link DateUtil}.
 * 
 */
public class DateUtilTest {
    LocalDateTime currentDay = LocalDateTime.now();
    LocalDateTime currentDayAt2Pm = currentDay.toLocalDate().atTime(2, 0);
    LocalDateTime nextDay = currentDay.plusDays(1);
    LocalDateTime nextDayAt2Pm = nextDay.toLocalDate().atTime(2, 0);
    LocalDateTime nextDayAt2359 = nextDay.toLocalDate().atTime(23, 59);
    
	@Test
	public void toDate_equalTimestamps() {
		long currTimeMs = java.lang.System.currentTimeMillis();
		Date testDate = new Date(currTimeMs);
		LocalDateTime testDateTime = fromEpoch(currTimeMs);
		Date actualDate = DateUtil.toDate(testDateTime);
		
		assertEquals(testDate.getTime(), actualDate.getTime());
	}
	
	@Test
	public void toDate_differentTimestamps() {
		long currTimeMs = java.lang.System.currentTimeMillis();
		Date testDate = new Date(currTimeMs);
		LocalDateTime testDateTime = fromEpoch(currTimeMs + 1);
		Date actualDate = DateUtil.toDate(testDateTime);
		
		assertNotEquals(testDate.getTime(), actualDate.getTime());
	}
	
	@Test
	public void floorDate_sameDate() {
		long testEpoch1 = 1476099300000l; // 10/10/2016 @ 11:35am (UTC)
		long testEpoch2 = 1476099360000l; // 10/10/2016 @ 11:36am (UTC)
		LocalDateTime testDateTime1 = fromEpoch(testEpoch1);
		LocalDateTime testDateTime2 = fromEpoch(testEpoch2);
		
		assertEquals(DateUtil.floorDate(testDateTime1), DateUtil.floorDate(testDateTime2));
	}
	
	@Test
	public void floorDate_differentDate() {
	    long testEpoch1 = 1476099300000l; // 10/10/2016 @ 11:35am (UTC)
	    long testEpoch2 = 1476185700000l; // 11/10/2016 @ 11:35am (UTC)
	    LocalDateTime testDateTime1 = fromEpoch(testEpoch1);
	    LocalDateTime testDateTime2 = fromEpoch(testEpoch2);
	    
	    assertNotEquals(DateUtil.floorDate(testDateTime1), DateUtil.floorDate(testDateTime2));
	}
	
	@Test
	public void floorDate_nullTest() {
		assertEquals(DateUtil.floorDate(null), null);
	}
	
	//@@author A0139922Y
	@Test
    public void ceilDate_sameDate_equals() {
        LocalDateTime today = LocalDateTime.now().toLocalDate().atTime(10,0);
        LocalDateTime todayPlusAnHour = today.plusHours(1);
        assertEquals(DateUtil.ceilDate(today), DateUtil.ceilDate(todayPlusAnHour));
    }
	
	//@@author A0139922Y
    @Test
    public void ceilDate_sameDate_not_null() {
        LocalDateTime today = LocalDateTime.now().toLocalDate().atTime(10,0);
        LocalDateTime todayPlusAnHour = today.plusHours(1);
        assertNotNull(DateUtil.ceilDate(today));
        assertNotNull(DateUtil.ceilDate(todayPlusAnHour));
        assertEquals(DateUtil.ceilDate(today), DateUtil.ceilDate(todayPlusAnHour));
    }
    
    //@@author A0139922Y
    @Test
    public void ceilDate_differentDate_not_equals() {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime tmr = LocalDateTime.now().plusDays(1);
        assertNotEquals(DateUtil.ceilDate(today), DateUtil.ceilDate(tmr));
    }
    
    //@@author A0139922Y
    @Test
    public void ceilDate_differentDate_not_null() {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime tmr = LocalDateTime.now().plusDays(1);
        assertNotNull(DateUtil.ceilDate(today));
        assertNotNull(DateUtil.ceilDate(tmr));
        assertNotEquals(DateUtil.ceilDate(today), DateUtil.ceilDate(tmr));
    }
    
    //@@author A0139922Y
    public void ceilDate_nullDate_null() {
        LocalDateTime nullDate = null;
        assertEquals(null, DateUtil.ceilDate(nullDate));
    }
    //@@author
	
    //@@author A0139812A
	@Test
	public void formatDayTests() {
	    LocalDateTime now = LocalDateTime.now();
	    assertEquals(DateUtil.formatDay(now), "Today");
	    assertEquals(DateUtil.formatDay(now.plus(1, ChronoUnit.DAYS)), "Tomorrow");
	    
	    // Show day of week for d = {n+2,...,n+6} where n = today
	    for (int i = 2; i <= 6; i++) {
	        String expected = now.plus(i, ChronoUnit.DAYS).getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.US);
	        assertEquals(DateUtil.formatDay(now.plus(i, ChronoUnit.DAYS)), expected);
	    }
	    
	    assertEquals(DateUtil.formatDay(now.minus(1, ChronoUnit.DAYS)), "1 day ago");
	    assertEquals(DateUtil.formatDay(now.minus(6, ChronoUnit.DAYS)), "6 days ago");
	    assertEquals(DateUtil.formatDay(now.minus(14, ChronoUnit.DAYS)), "14 days ago");
	}
	
	//@@author A0139812A
	@Test
	public void formatShortDateTests() {
	    LocalDateTime now = LocalDateTime.now();
	    
	    String expectedToday = now.format(DateTimeFormatter.ofPattern("E dd MMM"));
	    assertEquals(DateUtil.formatShortDate(now), expectedToday);
	    
	    String expectedTomorrow = now.plus(1,  ChronoUnit.DAYS).format(DateTimeFormatter.ofPattern("E dd MMM"));
	    assertEquals(DateUtil.formatShortDate(now.plus(1, ChronoUnit.DAYS)), expectedTomorrow);
	    
	    for (int i = 2; i <= 6; i++) {
	        String expected = now.plus(i,  ChronoUnit.DAYS).format(DateTimeFormatter.ofPattern("dd MMM"));
	        assertEquals(DateUtil.formatShortDate(now.plus(i, ChronoUnit.DAYS)), expected);
	    }
	    
	    // Test dates in the past
	    LocalDateTime pastDate = fromEpoch(946656000000l); // 1 Jan 2000 UTC+8
	    assertEquals(DateUtil.formatShortDate(pastDate), pastDate.format(DateTimeFormatter.ofPattern("E dd MMM")));
	}
	
	//@@author A0139922Y
    @Test
    public void formatShortDateTests_null_test() {
        assertNull(DateUtil.formatShortDate(null));
        assertNotNull(DateUtil.formatShortDate(LocalDateTime.now()));
    }
	
    //@@author A0139812A
	@Test
	public void parseShortDateTests() {
	    // TODO
	}
	
	//@@author A0139812A
	@Test
	public void parseTimeTests() {
	    assertEquals(DateUtil.parseTime("00:00"), LocalTime.of(0, 0));
	    assertEquals(DateUtil.parseTime("00:01"), LocalTime.of(0, 1));
	    assertEquals(DateUtil.parseTime("01:23"), LocalTime.of(1, 23));
	    assertEquals(DateUtil.parseTime("12:00"), LocalTime.of(12, 0));
	    assertEquals(DateUtil.parseTime("13:00"), LocalTime.of(13, 0));
	    assertEquals(DateUtil.parseTime("23:59"), LocalTime.of(23, 59));
	    assertEquals(DateUtil.parseTime("24:00"), LocalTime.of(0, 0));
	}
	
	//@@author A0139812A
	@Test
	public void formatTimeTests() {
	    assertEquals(DateUtil.formatTime(LocalDateTime.of(2016, 1, 1, 0, 0)), "00:00");
	    assertEquals(DateUtil.formatTime(LocalDateTime.of(2016, 1, 1, 2, 0)), "02:00");
	    assertEquals(DateUtil.formatTime(LocalDateTime.of(2016, 1, 1, 2, 33)), "02:33");
	    assertEquals(DateUtil.formatTime(LocalDateTime.of(2016, 1, 1, 13, 0)), "13:00");
	    assertEquals(DateUtil.formatTime(LocalDateTime.of(2016, 1, 1, 23, 59)), "23:59");
	}
	
	//@@author A0139922Y
	@Test
	public void formatTime_null_test() {
	    assertNull(DateUtil.formatTime(null));
	    assertNotNull(DateUtil.formatTime(LocalDateTime.now()));
	}
	//@@author
	
	//@@author A0139812A
	private static LocalDateTime fromEpoch(long epoch) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(epoch), ZoneId.systemDefault());
	}
    
    //@@author A0139922Y	
    @Test
    public void testIfDateExist_false() {
        LocalDateTime currentDate = LocalDateTime.now();
        assertNotNull(DateUtil.checkIfDateExist(currentDate));
        assertFalse(DateUtil.checkIfDateExist(currentDate));
    }
    
    //@@author A0139922Y
    @Test
    public void testCheckIfDateExist_with_year_diff_true() {
        LocalDateTime currentDate = LocalDateTime.now();
        assertNotNull(DateUtil.checkIfDateExist(currentDate));
        // Only year is different
        assertTrue(DateUtil.checkIfDateExist(currentDate.plusYears(1)));
    }
    
    //@@author A0139922Y
    @Test
    public void testCheckIfDateExist_with_month_diff_true() {
        LocalDateTime currentDate = LocalDateTime.now();
        assertNotNull(DateUtil.checkIfDateExist(currentDate));
        // Only month is different
        assertTrue(DateUtil.checkIfDateExist(currentDate.plusMonths(1)));
        assertTrue(DateUtil.checkIfDateExist(currentDate.plusMonths(12)));
    }
    
    //@@author A0139922Y
    @Test
    public void testCheckifDateExist_with_day_diff_true() {
        LocalDateTime currentDate = LocalDateTime.now();
        assertNotNull(DateUtil.checkIfDateExist(currentDate));
        // Only day is different
        assertTrue(DateUtil.checkIfDateExist(currentDate.plusDays(1)));
        assertTrue(DateUtil.checkIfDateExist(currentDate.plusDays(365)));
    }
    
    //@@author A0139922Y
    @Test
    public void testCheckifDateExist_with_day_month_diff_true() {
        LocalDateTime currentDate = LocalDateTime.now();
        assertNotNull(DateUtil.checkIfDateExist(currentDate));  
        // Day and Month are different
        currentDate = currentDate.plusDays(1);
        currentDate = currentDate.plusMonths(1);
        assertTrue(DateUtil.checkIfDateExist(currentDate));
    }
    
    //@@author A0139922Y
    @Test
    public void testCheckifDateExist_with_day_year_diff_true() {
        LocalDateTime currentDate = LocalDateTime.now();
        assertNotNull(DateUtil.checkIfDateExist(currentDate));  
        // Day and Month are different
        currentDate = currentDate.plusDays(1);
        currentDate = currentDate.plusYears(1);
        assertTrue(DateUtil.checkIfDateExist(currentDate));
    }
    
    //@@author A0139922Y
    @Test
    public void testCheckifDateExist_with_month_year_diff_true() {
        LocalDateTime currentDate = LocalDateTime.now();
        assertNotNull(DateUtil.checkIfDateExist(currentDate));  
        // Day and Month are different
        currentDate = currentDate.plusMonths(1);
        currentDate = currentDate.plusYears(1);
        assertTrue(DateUtil.checkIfDateExist(currentDate));
    }
    
    //@@author A0139922Y
    @Test
    public void testCheckifDateExist_equals_false() {
        LocalDateTime currentDate = LocalDateTime.now();
        assertNotNull(DateUtil.checkIfDateExist(currentDate));
        assertFalse(DateUtil.checkIfDateExist(currentDate));
    }
    
    //@@author A0139922Y
    @Test
    public void testCheckIfTimeExist_false() {
        LocalDateTime currentTime = LocalDateTime.now();
        assertNotNull(DateUtil.checkIfTimeExist(currentTime));
        assertFalse(DateUtil.checkIfTimeExist(currentTime));
    }
    
    //@@author A0139922Y
    @Test
    public void testIfTimeExist_true() {
        LocalDateTime currentTime = LocalDateTime.now().toLocalDate().atTime(10,0);
        assertNotNull(DateUtil.checkIfTimeExist(currentTime));
        assertTrue(DateUtil.checkIfTimeExist(currentTime.toLocalDate().atTime(currentTime.getHour() - currentTime.getHour(), 
                currentTime.getMinute())));
        assertTrue(DateUtil.checkIfTimeExist(currentTime.toLocalDate().atTime(currentTime.getHour(), 
                currentTime.getMinute() - currentTime.getMinute())));
        assertTrue(DateUtil.checkIfTimeExist(currentTime.toLocalDate().atTime(currentTime.getHour() - currentTime.getHour(), 
                currentTime.getMinute() - currentTime.getMinute())));
    }
    
    //@@author A0139922Y
    @Test
    public void testParseTimeStamp_task_setTimeToMin() {
        //set the time to 00:00 for task
        assertNotEquals(DateUtil.parseTimeStamp(currentDay, null, true), currentDay);
        assertEquals(DateUtil.parseTimeStamp(currentDay, null, true), DateUtil.floorDate(currentDay));
    }
    
    //@@author A0139922Y
    @Test
    public void testParseTimeStamp_event_setTimeToMin() {
        //will set the time to 00:00 for event
        assertNotEquals(DateUtil.parseTimeStamp(currentDay, nextDay, true), currentDay);
        assertEquals(DateUtil.parseTimeStamp(currentDay, nextDay, true), DateUtil.floorDate(currentDay));
    }
    
    //@@author A0139922Y
    @Test
    public void testParseTimeStamp_event_setTimeToMax() {
        // will set the time to 23:59
        assertNotEquals(DateUtil.parseTimeStamp(nextDay, currentDay, false), nextDay);
        assertEquals(DateUtil.parseTimeStamp(nextDay, currentDay, false), nextDayAt2359);
    }

    //@@author A0139922Y
    @Test
    public void testParseTimeStamp_event_followNextDayTime() {
        // will set the time to follow the nextDay
        assertNotEquals(DateUtil.parseTimeStamp(currentDay, nextDayAt2Pm, true), currentDay);
        assertEquals(DateUtil.parseTimeStamp(currentDay, nextDayAt2Pm, true), currentDayAt2Pm);
    }
    
    //@@author A0139922Y
    @Test
    public void testParseTimeStamp_event_followCurrentDayTime() {
        // will set the time to follow the currentDay
        assertEquals(DateUtil.parseTimeStamp(currentDayAt2Pm, nextDay, true), currentDayAt2Pm);
        assertNotEquals(DateUtil.parseTimeStamp(nextDay, currentDayAt2Pm, false), nextDay);
        assertEquals(DateUtil.parseTimeStamp(nextDay, currentDayAt2Pm, false), nextDayAt2Pm);
    }

    //@@author A0139922Y
    @Test
    public void testParseTimeStamp_event_followGivenDateTime() {
        // if date and time exist, will not overwrite it
        assertEquals(DateUtil.parseTimeStamp(currentDayAt2Pm, nextDay, true), currentDayAt2Pm);
        assertEquals(DateUtil.parseTimeStamp(currentDayAt2Pm, nextDayAt2Pm, true), currentDayAt2Pm);
        assertEquals(DateUtil.parseTimeStamp(nextDayAt2Pm, currentDay, false), nextDayAt2Pm);
        assertEquals(DateUtil.parseTimeStamp(nextDayAt2Pm, currentDayAt2Pm, false), nextDayAt2Pm);
    }
}
