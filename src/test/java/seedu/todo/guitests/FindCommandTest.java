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
    private final LocalDateTime TODAY = LocalDateTime.now();
    private final String TODAY_STRING = DateUtil.formatDate(TODAY);
    private final String TODAY_ISO_STRING = DateUtil.formatIsoDate(TODAY);
    private final LocalDateTime TOMORROW = LocalDateTime.now().plusDays(1);
    private final String TOMORROW_STRING = DateUtil.formatDate(TOMORROW);
    private final String TOMORROW_ISO_STRING = DateUtil.formatIsoDate(TOMORROW);
    private final LocalDateTime NEXTDAY = LocalDateTime.now().plusDays(2);
    private final String NEXTDAY_STRING = DateUtil.formatDate(NEXTDAY);
    private final String NEXTDAY_ISO_STRING = DateUtil.formatIsoDate(NEXTDAY);
    
    String commandAdd1 = String.format("add task Buy Coco by \"%s 8pm\" tag personal", TODAY_STRING);
    Task task1 = new Task();
    String commandAdd2 = String.format("add task Buy Milk by \"%s 9pm\" tag personal", TOMORROW_STRING);
    Task task2 = new Task();
    
    String commandAdd3 = String.format("add event CS2103 V0.5 Demo from \"%s 4pm\" to \"%s 5pm\" tag event",
            TOMORROW_STRING, TOMORROW_STRING);
    Event event3 = new Event();
    String commandAdd4 = String.format("add event buying workshop from \"%s 8pm\" to \"%s 9pm\" tag buy",
            NEXTDAY_STRING, NEXTDAY_STRING);
    Event event4 = new Event();
    
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
                String.format("%s 20:00:00", NEXTDAY_ISO_STRING)));
        event4.setEndDate(DateUtil.parseDateTime(
                String.format("%s 21:00:00", NEXTDAY_ISO_STRING)));
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
    public void find_task_by_name() {
        String command = "find name buy task";
        assertTaskVisibleAfterCmd(command, task1);
        assertTaskVisibleAfterCmd(command, task2);
        assertEventNotVisibleAfterCmd(command, event3);
        assertEventNotVisibleAfterCmd(command, event4);
    }
    
    @Test
    public void find_event_by_name() {
        String command = "find name buy event";
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
    public void find_task_by_tag() {
        String command = "find tagName personal task";
        assertTaskVisibleAfterCmd(command, task1);
        assertTaskVisibleAfterCmd(command, task2);
        assertEventNotVisibleAfterCmd(command, event3);
        assertEventNotVisibleAfterCmd(command, event4);
    }
    
    @Test
    public void find_event_by_tag() {
        String command = "find tagName buy event";
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
    public void find_task_by_keyword() {
        String command = "find buy task";
        assertTaskVisibleAfterCmd(command, task1);
        assertTaskVisibleAfterCmd(command, task2);
        assertEventNotVisibleAfterCmd(command, event3);
        assertEventNotVisibleAfterCmd(command, event4);
    }
    
    @Test
    public void find_event_by_keyword() {
        String command = "find buy event";
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
    public void find_by_task_complete_status() {
        console.runCommand("complete 1");
        String command = "find buy complete";
        assertTaskVisibleAfterCmd(command, task1);
        assertTaskNotVisibleAfterCmd(command, task2);
        assertEventNotVisibleAfterCmd(command, event3);
        assertEventNotVisibleAfterCmd(command, event4);
    }
    
    @Test
    public void find_by_task_incomplete_status() {
        String command = "find buy incomplete";
        assertTaskVisibleAfterCmd(command, task1);
        assertTaskVisibleAfterCmd(command, task2);
        assertEventNotVisibleAfterCmd(command, event3);
        assertEventNotVisibleAfterCmd(command, event4);
    }
    
    @Test
    public void find_by_event_current_status() {
        String command = "find buy current";
        assertTaskNotVisibleAfterCmd(command, task1);
        assertTaskNotVisibleAfterCmd(command, task2);
        assertEventNotVisibleAfterCmd(command, event3);
        assertEventVisibleAfterCmd(command, event4);
    }
    
    @Test
    public void find_by_event_over_status() {
        String command = "find buy over";
        assertTaskVisibleAfterCmd(command, task1);
        assertTaskVisibleAfterCmd(command, task2);
        assertEventVisibleAfterCmd(command, event3);
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
