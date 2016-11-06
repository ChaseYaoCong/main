package seedu.todo.guitests;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import seedu.todo.commons.util.DateUtil;
import seedu.todo.models.Event;
import seedu.todo.models.Task;

/*
 * @@author A0139922Y
 */
public class FindCommandTest extends GuiTest {
    // Date variables to be use to initialise DB
    private static final LocalDateTime TODAY = LocalDateTime.now();
    private static final String TODAY_STRING = DateUtil.formatDate(TODAY);
    private static final String TODAY_ISO_STRING = DateUtil.formatIsoDate(TODAY);
    private static final LocalDateTime TOMORROW = LocalDateTime.now().plusDays(1);
    private static final String TOMORROW_STRING = DateUtil.formatDate(TOMORROW);
    private static final String TOMORROW_ISO_STRING = DateUtil.formatIsoDate(TOMORROW);
    private static final LocalDateTime THE_DAY_AFTER_TOMORROW_ = LocalDateTime.now().plusDays(2);
    private static final String THE_DAY_AFTER_TOMORROW_STRING = DateUtil.formatDate(THE_DAY_AFTER_TOMORROW_);
    private static final String THE_DAY_AFTER_TOMORROW__ISO_STRING = DateUtil.formatIsoDate(THE_DAY_AFTER_TOMORROW_);
    
    // Command to be use to initialise DB
    private String commandAdd1 = String.format("add task Buy Coco by \"%s 8pm\" tag personal", TODAY_STRING);
    private Task task1 = new Task();
    private String commandAdd2 = String.format("add task Buy Milk by \"%s 9pm\" tag personal", TOMORROW_STRING);
    private Task task2 = new Task();
    private String commandAdd3 = String.format("add event CS2103 V0.5 Demo from \"%s 4pm\" to \"%s 5pm\" tag event",
            TOMORROW_STRING, TOMORROW_STRING);
    private Event event3 = new Event();
    private String commandAdd4 = String.format("add event buying workshop from \"%s 8pm\" to \"%s 9pm\" tag buy",
            THE_DAY_AFTER_TOMORROW_STRING, THE_DAY_AFTER_TOMORROW_STRING);
    private Event event4 = new Event();
    
    // Set up DB
    public FindCommandTest() {
        task1.setName("Buy Coco");
        task1.setCalendarDateTime(DateUtil.parseDateTime(
                String.format("%s 20:00:00", TODAY_ISO_STRING)));
        task1.addTag("personal");
        task1.setCompleted();
        
        task2.setName("Buy Milk");
        task2.setDueDate(DateUtil.parseDateTime(
                String.format("%s 21:00:00", TOMORROW_ISO_STRING)));
        task2.addTag("personal");
        
        event3.setName("CS2103 V0.5 Demo");
        event3.setStartDate(DateUtil.parseDateTime(
                String.format("%s 16:00:00", TOMORROW_ISO_STRING)));
        event3.setEndDate(DateUtil.parseDateTime(
                String.format("%s 17:00:00", TOMORROW_ISO_STRING)));
        event3.addTag("event");
        
        event4.setName("buying workshop");
        event4.setStartDate(DateUtil.parseDateTime(
                String.format("%s 20:00:00", THE_DAY_AFTER_TOMORROW__ISO_STRING)));
        event4.setEndDate(DateUtil.parseDateTime(
                String.format("%s 21:00:00", THE_DAY_AFTER_TOMORROW__ISO_STRING)));
        event4.addTag("buy");
    }
    
    // Re-use
    @Before
    public void initFixtures() {
        console.runCommand("clear");
        assertTaskVisibleAfterCmd(commandAdd1, task1);
        assertTaskVisibleAfterCmd(commandAdd2, task2);
        assertEventVisibleAfterCmd(commandAdd3, event3);
        assertEventVisibleAfterCmd(commandAdd4, event4);
    }
    
    // Re-use
    @Test
    public void fixtures_test() {
        console.runCommand("clear");
        assertTaskNotVisibleAfterCmd("list", task1);
        assertTaskNotVisibleAfterCmd("list", task2);
        assertEventNotVisibleAfterCmd("list", event3);
        assertEventNotVisibleAfterCmd("list", event4);
    }
    
    @Test
    public void find_by_name() {
        String command = "find name buy";
        assertTaskVisibleAfterCmd(command, task1);
        assertTaskVisibleAfterCmd(command, task2);
        assertEventNotVisibleAfterCmd(command, event3);
        assertEventVisibleAfterCmd(command, event4);
    }
    
    @Test
    public void find_tasks_by_name() {
        String command = "find name buy tasks";
        assertTaskVisibleAfterCmd(command, task1);
        assertTaskVisibleAfterCmd(command, task2);
        assertEventNotVisibleAfterCmd(command, event3);
        assertEventNotVisibleAfterCmd(command, event4);
    }
    
    @Test
    public void find_events_by_name() {
        String command = "find name buy events";
        assertTaskNotVisibleAfterCmd(command, task1);
        assertTaskNotVisibleAfterCmd(command, task2);
        assertEventNotVisibleAfterCmd(command, event3);
        assertEventVisibleAfterCmd(command, event4);
    }
    
    @Test
    public void find_by_invalid_name() {
        String command = "find name tester";
        assertTaskVisibleAfterCmd(command, task1);
        assertTaskVisibleAfterCmd(command, task2);
        assertEventVisibleAfterCmd(command, event3);
        assertEventVisibleAfterCmd(command, event4);
    }
    
    @Test
    public void find_by_tag() {
        String command = "find tagName buy";
        assertTaskNotVisibleAfterCmd(command, task1);
        assertTaskNotVisibleAfterCmd(command, task2);
        assertEventNotVisibleAfterCmd(command, event3);
        assertEventVisibleAfterCmd(command, event4);
    }
    
    @Test
    public void find_tasks_by_tag() {
        String command = "find tagName personal tasks";
        assertTaskVisibleAfterCmd(command, task1);
        assertTaskVisibleAfterCmd(command, task2);
        assertEventNotVisibleAfterCmd(command, event3);
        assertEventNotVisibleAfterCmd(command, event4);
    }
    
    @Test
    public void find_events_by_tag() {
        String command = "find tagName buy events";
        assertTaskNotVisibleAfterCmd(command, task1);
        assertTaskNotVisibleAfterCmd(command, task2);
        assertEventNotVisibleAfterCmd(command, event3);
        assertEventVisibleAfterCmd(command, event4);
    }
    
    @Test
    public void find_by_invalid_tag() {
        String command = "find tagName tester";
        assertTaskVisibleAfterCmd(command, task1);
        assertTaskVisibleAfterCmd(command, task2);
        assertEventVisibleAfterCmd(command, event3);
        assertEventVisibleAfterCmd(command, event4);
    }
    
    @Test
    public void find_by_keyword() {
        String command = "find buy";
        assertTaskVisibleAfterCmd(command, task1);
        assertTaskVisibleAfterCmd(command, task2);
        assertEventNotVisibleAfterCmd(command, event3);
        assertEventVisibleAfterCmd(command, event4);
    }
    
    @Test
    public void find_tasks_by_keyword() {
        String command = "find buy tasks";
        assertTaskVisibleAfterCmd(command, task1);
        assertTaskVisibleAfterCmd(command, task2);
        assertEventNotVisibleAfterCmd(command, event3);
        assertEventNotVisibleAfterCmd(command, event4);
    }
    
    @Test
    public void find_events_by_keyword() {
        String command = "find buy events";
        assertTaskNotVisibleAfterCmd(command, task1);
        assertTaskNotVisibleAfterCmd(command, task2);
        assertEventNotVisibleAfterCmd(command, event3);
        assertEventVisibleAfterCmd(command, event4);
    }
    
    @Test
    public void find_by_invalid_keyword() {
        String command = "find tester";
        assertTaskVisibleAfterCmd(command, task1);
        assertTaskVisibleAfterCmd(command, task2);
        assertEventVisibleAfterCmd(command, event3);
        assertEventVisibleAfterCmd(command, event4);
    }
    
    @Test
    public void find_by_tasks_complete_status() {
        console.runCommand("complete 1");
        String command = "find buy complete";
        assertTaskVisibleAfterCmd(command, task1);
        assertTaskNotVisibleAfterCmd(command, task2);
        assertEventNotVisibleAfterCmd(command, event3);
        assertEventNotVisibleAfterCmd(command, event4);
    }
    
    @Test
    public void find_by_tassk_incomplete_status() {
        String command = "find buy incomplete";
        assertTaskVisibleAfterCmd(command, task1);
        assertTaskVisibleAfterCmd(command, task2);
        assertEventNotVisibleAfterCmd(command, event3);
        assertEventNotVisibleAfterCmd(command, event4);
    }
    
    @Test
    public void find_by_events_current_status() {
        String command = "find buy current";
        assertTaskNotVisibleAfterCmd(command, task1);
        assertTaskNotVisibleAfterCmd(command, task2);
        assertEventNotVisibleAfterCmd(command, event3);
        assertEventVisibleAfterCmd(command, event4);
    }
    
    @Test
    public void find_by_events_over_status() {
        String command = "find buy over";
        assertTaskVisibleAfterCmd(command, task1);
        assertTaskVisibleAfterCmd(command, task2);
        assertEventVisibleAfterCmd(command, event3);
        assertEventVisibleAfterCmd(command, event4);
    }
    
    @Test
    public void find_by_single_date() {
        String command = "find buy on today";
        assertTaskVisibleAfterCmd(command, task1);
        assertTaskNotVisibleAfterCmd(command, task2);
        assertEventNotVisibleAfterCmd(command, event3);
        assertEventNotVisibleAfterCmd(command, event4);
    }
    
    @Test
    public void find_by_date_range() {
        String command = "find buy from today";
        assertTaskVisibleAfterCmd(command, task1);
        assertTaskVisibleAfterCmd(command, task2);
        assertEventNotVisibleAfterCmd(command, event3);
        assertEventVisibleAfterCmd(command, event4);
    }
    
    @Test
    public void find_tasks_by_single_date() {
        String command = "find buy tasks on today";
        assertTaskVisibleAfterCmd(command, task1);
        assertTaskNotVisibleAfterCmd(command, task2);
        assertEventNotVisibleAfterCmd(command, event3);
        assertEventNotVisibleAfterCmd(command, event4);
    }
    
    @Test 
    public void find_tasks_by_date_range() {
        String command = "find buy tasks from today";
        assertTaskVisibleAfterCmd(command, task1);
        assertTaskVisibleAfterCmd(command, task2);
        assertEventNotVisibleAfterCmd(command, event3);
        assertEventNotVisibleAfterCmd(command, event4);
    }
    
    @Test
    public void find_events_by_single_date() {
        String command = "find CS2103 events on tomorrow";
        assertTaskNotVisibleAfterCmd(command, task1);
        assertTaskNotVisibleAfterCmd(command, task2);
        assertEventVisibleAfterCmd(command, event3);
        assertEventNotVisibleAfterCmd(command, event4);
    }
    
    @Test
    public void find_events_by_date_range() {
        String command = "find buy events from today";
        assertTaskNotVisibleAfterCmd(command, task1);
        assertTaskNotVisibleAfterCmd(command, task2);
        assertEventNotVisibleAfterCmd(command, event3);
        assertEventVisibleAfterCmd(command, event4);
    }
    
    @Test
    public void find_missingKeywords_disambiguate() {
        String command = "find";
        console.runCommand(command);
        String expectedDisambiguation = "find \"name\" tagName \"tag\" on \"date\" \"task/event\"";
        assertEquals(console.getConsoleInputText(), expectedDisambiguation);
    }
    
    @Test
    public void find_invalidTaskSyntax_disambiguate() {
        String command = "find buy task over";
        console.runCommand(command);
        String expectedDisambiguation = "find \"name\" task \"complete/incomplete\"";
        assertEquals(console.getConsoleInputText(), expectedDisambiguation);
    }
    
    @Test
    public void find_invalidEventSyntax_disambiguate() {
        String command = "find buy event complete";
        console.runCommand(command);
        String expectedDisambiguation = "find \"name\" event \"over/ongoing\"";
        assertEquals(console.getConsoleInputText(), expectedDisambiguation);
    }
}