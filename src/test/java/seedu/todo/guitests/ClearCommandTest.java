package seedu.todo.guitests;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import seedu.todo.commons.util.DateUtil;
import seedu.todo.commons.util.StringUtil;
import seedu.todo.controllers.ClearController;
import seedu.todo.models.Event;
import seedu.todo.models.Task;

/*
 * @@author A0093907W
 */
public class ClearCommandTest extends GuiTest {
    private final LocalDateTime oneDayFromNow = LocalDateTime.now().plusDays(1);
    private final String oneDayFromNowString = DateUtil.formatDate(oneDayFromNow);
    private final String oneDayFromNowIsoString = DateUtil.formatIsoDate(oneDayFromNow);
    private final LocalDateTime twoDaysFromNow = LocalDateTime.now().plusDays(2);
    private final String twoDaysFromNowString = DateUtil.formatDate(twoDaysFromNow);
    private final String twoDaysFromNowIsoString = DateUtil.formatIsoDate(twoDaysFromNow);
    
    private String commandAdd1 = String.format("add task Buy KOI by \"%s 8pm\"", oneDayFromNowString);
    private Task task1 = new Task();
    private String commandAdd2 = String.format("add task Buy Milk by \"%s 9pm\"", twoDaysFromNowString);
    private Task task2 = new Task();
    
    private String commandAdd3 = String.format("add event Some Event from \"%s 4pm\" to \"%s 5pm\"",
            oneDayFromNowString, oneDayFromNowString);
    private Event event3 = new Event();
    private String commandAdd4 = String.format("add event Another Event from \"%s 8pm\" to \"%s 9pm\"",
            twoDaysFromNowString, twoDaysFromNowString);
    private Event event4 = new Event();
    
    private int expectedNumOfTasks;
    private int expectedNumOfEvents;
    
    public ClearCommandTest() {
        task1.setName("Buy KOI");
        task1.setCalendarDateTime(DateUtil.parseDateTime(
                String.format("%s 20:00:00", oneDayFromNowIsoString)));
        task2.setName("Buy Milk");
        
        task2.setDueDate(DateUtil.parseDateTime(
                String.format("%s 21:00:00", twoDaysFromNowIsoString)));
        
        event3.setName("Some Event");
        event3.setStartDate(DateUtil.parseDateTime(
                String.format("%s 16:00:00", oneDayFromNowIsoString)));
        event3.setEndDate(DateUtil.parseDateTime(
                String.format("%s 17:00:00", oneDayFromNowIsoString)));
        
        event4.setName("Another Event");
        event4.setStartDate(DateUtil.parseDateTime(
                String.format("%s 20:00:00", twoDaysFromNowIsoString)));
        event4.setEndDate(DateUtil.parseDateTime(
                String.format("%s 21:00:00", twoDaysFromNowIsoString)));
    }
    
    @Before
    public void initFixtures() {
        console.runCommand("clear");
        assertTaskVisibleAfterCmd(commandAdd1, task1);
        assertTaskVisibleAfterCmd(commandAdd2, task2);
        assertEventVisibleAfterCmd(commandAdd3, event3);
        assertEventVisibleAfterCmd(commandAdd4, event4);
    }
    
    @Test
    public void fixtures_test() {
        console.runCommand("clear");
        assertTaskNotVisibleAfterCmd("list", task1);
        assertTaskNotVisibleAfterCmd("list", task2);
        assertEventNotVisibleAfterCmd("list", event3);
        assertEventNotVisibleAfterCmd("list", event4);
    }
    
    @Test
    public void clear_all_tasks() {
        console.runCommand("clear tasks");
        assertTaskNotVisibleAfterCmd("list", task1);
        assertTaskNotVisibleAfterCmd("list", task2);
        assertEventVisibleAfterCmd("list", event3);
        assertEventVisibleAfterCmd("list", event4);
    }
    
    @Test
    public void clear_all_events() {
        console.runCommand("clear events");
        assertTaskVisibleAfterCmd("list", task1);
        assertTaskVisibleAfterCmd("list", task2);
        assertEventNotVisibleAfterCmd("list", event3);
        assertEventNotVisibleAfterCmd("list", event4);
    }
    //@@author
    
    //@@author A0139922Y
    @Test
    public void clear_tasks_by_single_date() {
        console.runCommand("clear tasks on tmr");
        // For console text area to check output message
        expectedNumOfTasks = 1;
        expectedNumOfEvents = 0;
        String expectedOutputMessage = String.format(ClearController.MESSAGE_CLEAR_SUCCESS_FORMAT,
                StringUtil.displayNumberOfTaskAndEventFoundWithPuralizer(expectedNumOfTasks, expectedNumOfEvents));
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
    
        // Check if Tasks and Events still in the GUI
        assertTaskNotVisibleAfterCmd("list", task1);
        assertTaskVisibleAfterCmd("list", task2);
        assertEventVisibleAfterCmd("list", event3);
        assertEventVisibleAfterCmd("list", event4);
    }
    
    @Test
    public void clear_tasks_by_date_range() {
        console.runCommand("clear tasks from today to tmr");
        // For console text area to check output message
        expectedNumOfTasks = 1;
        expectedNumOfEvents = 0;
        String expectedOutputMessage = String.format(ClearController.MESSAGE_CLEAR_SUCCESS_FORMAT,
                StringUtil.displayNumberOfTaskAndEventFoundWithPuralizer(expectedNumOfTasks, expectedNumOfEvents));
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
        
        // Check if Tasks and Events still in the GUI
        assertTaskNotVisibleAfterCmd("list", task1);
        assertTaskVisibleAfterCmd("list", task2);
        assertEventVisibleAfterCmd("list", event3);
        assertEventVisibleAfterCmd("list", event4);
    }
    
    @Test
    public void clear_tasks_by_date_range_with_single_date() {
        console.runCommand("clear tasks from today");
        // For console text area to check output message
        expectedNumOfTasks = 2;
        expectedNumOfEvents = 0;
        String expectedOutputMessage = String.format(ClearController.MESSAGE_CLEAR_SUCCESS_FORMAT,
                StringUtil.displayNumberOfTaskAndEventFoundWithPuralizer(expectedNumOfTasks, expectedNumOfEvents));
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
        
        // Check if Tasks and Events still in the GUI
        assertTaskNotVisibleAfterCmd("list", task1);
        assertTaskNotVisibleAfterCmd("list", task2);
        assertEventVisibleAfterCmd("list", event3);
        assertEventVisibleAfterCmd("list", event4);
    }
    
    @Test
    public void clear_events_by_single_date() {
        console.runCommand("clear events on tmr");
        // For console text area to check output message        
        expectedNumOfTasks = 0;
        expectedNumOfEvents = 1;
        String expectedOutputMessage = String.format(ClearController.MESSAGE_CLEAR_SUCCESS_FORMAT,
                StringUtil.displayNumberOfTaskAndEventFoundWithPuralizer(expectedNumOfTasks, expectedNumOfEvents));
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
        
        // Check if Tasks and Events still in the GUI
        assertTaskVisibleAfterCmd("list", task1);
        assertTaskVisibleAfterCmd("list", task2);
        assertEventNotVisibleAfterCmd("list", event3);
        assertEventVisibleAfterCmd("list", event4);
    }
    
    @Test
    public void clear_events_by_date_range() {
        console.runCommand("clear events from today to tmr");
        // For console text area to check output message        
        expectedNumOfTasks = 0;
        expectedNumOfEvents = 1;
        String expectedOutputMessage = String.format(ClearController.MESSAGE_CLEAR_SUCCESS_FORMAT,
                StringUtil.displayNumberOfTaskAndEventFoundWithPuralizer(expectedNumOfTasks, expectedNumOfEvents));
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
        
        // Check if Tasks and Events still in the GUI
        assertTaskVisibleAfterCmd("list", task1);
        assertTaskVisibleAfterCmd("list", task2);
        assertEventNotVisibleAfterCmd("list", event3);
        assertEventVisibleAfterCmd("list", event4);
    }
    
    @Test
    public void clear_events_by_date_range_with_single_date() {
        console.runCommand("clear events from today");
        // For console text area to check output message
        expectedNumOfTasks = 0;
        expectedNumOfEvents = 2;
        String expectedOutputMessage = String.format(ClearController.MESSAGE_CLEAR_SUCCESS_FORMAT,
                StringUtil.displayNumberOfTaskAndEventFoundWithPuralizer(expectedNumOfTasks, expectedNumOfEvents));
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
        
        // Check if Tasks and Events still in the GUI
        assertTaskVisibleAfterCmd("list", task1);
        assertTaskVisibleAfterCmd("list", task2);
        assertEventNotVisibleAfterCmd("list", event3);
        assertEventNotVisibleAfterCmd("list", event4);
    }
    
    @Test
    public void clear_by_single_date() {
        console.runCommand("clear on tmr");
        // For console text area to check output message
        expectedNumOfTasks = 1;
        expectedNumOfEvents = 1;
        String expectedOutputMessage = String.format(ClearController.MESSAGE_CLEAR_SUCCESS_FORMAT,
                StringUtil.displayNumberOfTaskAndEventFoundWithPuralizer(expectedNumOfTasks, expectedNumOfEvents));
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
        
        // Check if Tasks and Events still in the GUI
        assertTaskNotVisibleAfterCmd("list", task1);
        assertTaskVisibleAfterCmd("list", task2);
        assertEventNotVisibleAfterCmd("list", event3);
        assertEventVisibleAfterCmd("list", event4);
    }
    
    @Test
    public void clear_by_date_range() {
        console.runCommand("clear from today to tmr");
        // For console text area to check output message
        expectedNumOfTasks = 1;
        expectedNumOfEvents = 1;
        String expectedOutputMessage = String.format(ClearController.MESSAGE_CLEAR_SUCCESS_FORMAT,
                StringUtil.displayNumberOfTaskAndEventFoundWithPuralizer(expectedNumOfTasks, expectedNumOfEvents));
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
        
        // Check if Tasks and Events still in the GUI
        assertTaskNotVisibleAfterCmd("list", task1);
        assertTaskVisibleAfterCmd("list", task2);
        assertEventNotVisibleAfterCmd("list", event3);
        assertEventVisibleAfterCmd("list", event4);
    }
    
    @Test
    public void clear_by_date_range_with_single_date() {
        console.runCommand("clear from today");
        // For console text area to check output message
        expectedNumOfTasks = 2;
        expectedNumOfEvents = 2;
        String expectedOutputMessage = String.format(ClearController.MESSAGE_CLEAR_SUCCESS_FORMAT,
                StringUtil.displayNumberOfTaskAndEventFoundWithPuralizer(expectedNumOfTasks, expectedNumOfEvents));
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
        
        // Check if Tasks and Events still in the GUI
        assertTaskNotVisibleAfterCmd("list", task1);
        assertTaskNotVisibleAfterCmd("list", task2);
        assertEventNotVisibleAfterCmd("list", event3);
        assertEventNotVisibleAfterCmd("list", event4);
    }
    
    @Test
    public void clear_with_invalid_date_syntax() {
        console.runCommand("clear from todar");
        // For console text area to check error message
        String expectedDisambiguation = ClearController.CLEAR_DATE_SYNTAX;
        assertEquals(console.getConsoleInputText(), expectedDisambiguation);
        String expectedOutputMessage = formatConsoleOutputTextArea(ClearController.MESSAGE_NO_DATE_DETECTED);
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
        
        // Check if Tasks and Events still in the GUI
        assertTaskVisibleAfterCmd("list", task1);
        assertTaskVisibleAfterCmd("list", task2);
        assertEventVisibleAfterCmd("list", event3);
        assertEventVisibleAfterCmd("list", event4);
    }
    
    @Test
    public void clear_with_date_conflict() {
        console.runCommand("clear by today to tmr");
        // For console text area to check error message
        String expectedDisambiguation = ClearController.CLEAR_DATE_SYNTAX;
        assertEquals(console.getConsoleInputText(), expectedDisambiguation);
        String expectedOutputMessage = formatConsoleOutputTextArea(ClearController.MESSAGE_DATE_CONFLICT);
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
        
        // Check if Tasks and Events still in the GUI
        assertTaskVisibleAfterCmd("list", task1);
        assertTaskVisibleAfterCmd("list", task2);
        assertEventVisibleAfterCmd("list", event3);
        assertEventVisibleAfterCmd("list", event4);
    }
    
    @Test
    public void clear_with_item_type_conflict() {
        console.runCommand("clear tasks events");
        // For console text area to check error message
        String expectedDisambiguation = ClearController.COMMAND_SYNTAX;
        assertEquals(console.getConsoleInputText(), expectedDisambiguation);
        String expectedOutputMessage = formatConsoleOutputTextArea(ClearController.MESSAGE_ITEM_TYPE_CONFLICT);
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
        
        // Check if Tasks and Events still in the GUI
        assertTaskVisibleAfterCmd("list", task1);
        assertTaskVisibleAfterCmd("list", task2);
        assertEventVisibleAfterCmd("list", event3);
        assertEventVisibleAfterCmd("list", event4);
    }
    
    @Test
    public void clear_with_invalid_command_syntax_by_event_status() {
        console.runCommand("clear current events");
        // For console text area to check error message
        String expectedDisambiguation = ClearController.COMMAND_SYNTAX;
        assertEquals(console.getConsoleInputText(), expectedDisambiguation);
        String expectedOutputMessage = formatConsoleOutputTextArea(ClearController.MESSAGE_CLEAR_UNABLE_TO_SUPPORT);
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
        
        // Check if Tasks and Events still in the GUI
        assertTaskVisibleAfterCmd("list", task1);
        assertTaskVisibleAfterCmd("list", task2);
        assertEventVisibleAfterCmd("list", event3);
        assertEventVisibleAfterCmd("list", event4);
    }
    
    @Test
    public void clear_with_invalid_command_syntax_by_task_status() {
        console.runCommand("clear completed tasks");
        // For console text area to check error message
        String expectedDisambiguation = ClearController.COMMAND_SYNTAX;
        assertEquals(console.getConsoleInputText(), expectedDisambiguation);
        String expectedOutputMessage = formatConsoleOutputTextArea(ClearController.MESSAGE_CLEAR_UNABLE_TO_SUPPORT);
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
        
        // Check if Tasks and Events still in the GUI
        assertTaskVisibleAfterCmd("list", task1);
        assertTaskVisibleAfterCmd("list", task2);
        assertEventVisibleAfterCmd("list", event3);
        assertEventVisibleAfterCmd("list", event4);
    }
}
