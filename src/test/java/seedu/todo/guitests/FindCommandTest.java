package seedu.todo.guitests;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import seedu.todo.commons.util.DateUtil;
import seedu.todo.commons.util.StringUtil;
import seedu.todo.controllers.FindController;
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
    
    private int expectedNumOfTasks;
    private int expectedNumOfEvents;
    
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
    
    //@@author A0139922Y-reused 
    @Before
    public void initFixtures() {
        console.runCommand("clear");
        assertTaskVisibleAfterCmd(commandAdd1, task1);
        assertTaskVisibleAfterCmd(commandAdd2, task2);
        assertEventVisibleAfterCmd(commandAdd3, event3);
        assertEventVisibleAfterCmd(commandAdd4, event4);
    }
    
    //@@author A0139922Y-reused
    @Test
    public void fixtures_test() {
        console.runCommand("clear");
        assertTaskNotVisibleAfterCmd("list", task1);
        assertTaskNotVisibleAfterCmd("list", task2);
        assertEventNotVisibleAfterCmd("list", event3);
        assertEventNotVisibleAfterCmd("list", event4);
    }
    //@@author
    
    //@@author A0139922Y
    @Test
    public void find_by_name() {
        String command = "find name buy";
        // To check if the tasks and events are been filtered as expected
        assertTaskVisibleAfterCmd(command, task1);
        assertTaskVisibleAfterCmd(command, task2);
        assertEventNotVisibleAfterCmd(command, event3);
        assertEventVisibleAfterCmd(command, event4);
        
        // For console text area to check output message
        expectedNumOfTasks = 2;
        expectedNumOfEvents = 1;
        String expectedOutputMessage = String.format(FindController.MESSAGE_RESULT_FOUND_FORMAT,
                StringUtil.displayNumberOfTaskAndEventFoundWithPuralizer(expectedNumOfTasks, expectedNumOfEvents));
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
    }
    
    @Test
    public void find_tasks_by_name() {
        String command = "find name buy tasks";
        // To check if the tasks are been filtered as expected
        assertTaskVisibleAfterCmd(command, task1);
        assertTaskVisibleAfterCmd(command, task2);
        assertEventNotVisibleAfterCmd(command, event3);
        assertEventNotVisibleAfterCmd(command, event4);
        
        // For console text area to check output message
        expectedNumOfTasks = 2;
        expectedNumOfEvents = 0;
        String expectedOutputMessage = String.format(FindController.MESSAGE_RESULT_FOUND_FORMAT,
                StringUtil.displayNumberOfTaskAndEventFoundWithPuralizer(expectedNumOfTasks, expectedNumOfEvents));
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
    }
    
    @Test
    public void find_events_by_name() {
        String command = "find name buy events";
        // To check if the events are been filtered as expected
        assertTaskNotVisibleAfterCmd(command, task1);
        assertTaskNotVisibleAfterCmd(command, task2);
        assertEventNotVisibleAfterCmd(command, event3);
        assertEventVisibleAfterCmd(command, event4);
        
        // For console text area to check output message
        expectedNumOfTasks = 0;
        expectedNumOfEvents = 1;
        String expectedOutputMessage = String.format(FindController.MESSAGE_RESULT_FOUND_FORMAT,
                StringUtil.displayNumberOfTaskAndEventFoundWithPuralizer(expectedNumOfTasks, expectedNumOfEvents));
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
    }
    
    @Test
    public void find_by_invalid_name() {
        String command = "find name tester";
        // To check if the tasks and events are been filtered as expected
        assertTaskVisibleAfterCmd(command, task1);
        assertTaskVisibleAfterCmd(command, task2);
        assertEventVisibleAfterCmd(command, event3);
        assertEventVisibleAfterCmd(command, event4);
        
        // For console text area to check error message
        assertEquals(console.getConsoleTextArea(), FindController.MESSAGE_NO_RESULT_FOUND);
    }
    
    @Test
    public void find_by_tag() {
        String command = "find tagName buy";
        // To check if the tasks and events are been filtered as expected
        assertTaskNotVisibleAfterCmd(command, task1);
        assertTaskNotVisibleAfterCmd(command, task2);
        assertEventNotVisibleAfterCmd(command, event3);
        assertEventVisibleAfterCmd(command, event4);
        
        // For console text area to check output message
        expectedNumOfTasks = 0;
        expectedNumOfEvents = 1;
        String expectedOutputMessage = String.format(FindController.MESSAGE_RESULT_FOUND_FORMAT,
                StringUtil.displayNumberOfTaskAndEventFoundWithPuralizer(expectedNumOfTasks, expectedNumOfEvents));
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
    }
    
    @Test
    public void find_tasks_by_tag() {
        String command = "find tagName personal tasks";
        // To check if the tasks are been filtered as expected
        assertTaskVisibleAfterCmd(command, task1);
        assertTaskVisibleAfterCmd(command, task2);
        assertEventNotVisibleAfterCmd(command, event3);
        assertEventNotVisibleAfterCmd(command, event4);
        
        // For console text area to check output message
        expectedNumOfTasks = 2;
        expectedNumOfEvents = 0;
        String expectedOutputMessage = String.format(FindController.MESSAGE_RESULT_FOUND_FORMAT,
                StringUtil.displayNumberOfTaskAndEventFoundWithPuralizer(expectedNumOfTasks, expectedNumOfEvents));
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
    }
    
    @Test
    public void find_events_by_tag() {
        String command = "find tagName buy events";
        // To check if event 4 is been filtered as expected
        assertTaskNotVisibleAfterCmd(command, task1);
        assertTaskNotVisibleAfterCmd(command, task2);
        assertEventNotVisibleAfterCmd(command, event3);
        assertEventVisibleAfterCmd(command, event4);
        
        // For console text area to check output message
        expectedNumOfTasks = 0;
        expectedNumOfEvents = 1;
        String expectedOutputMessage = String.format(FindController.MESSAGE_RESULT_FOUND_FORMAT,
                StringUtil.displayNumberOfTaskAndEventFoundWithPuralizer(expectedNumOfTasks, expectedNumOfEvents));
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
    }
    
    @Test
    public void find_by_invalid_tag() {
        // To check if all tasks and events are still in the view
        String command = "find tagName tester";
        assertTaskVisibleAfterCmd(command, task1);
        assertTaskVisibleAfterCmd(command, task2);
        assertEventVisibleAfterCmd(command, event3);
        assertEventVisibleAfterCmd(command, event4);
        
        // For console text area to check output message
        assertEquals(console.getConsoleTextArea(), FindController.MESSAGE_NO_RESULT_FOUND);
    }
    
    @Test
    public void find_by_keyword() {
        String command = "find buy";
        // To check if tasks and events are been filtered as expected
        assertTaskVisibleAfterCmd(command, task1);
        assertTaskVisibleAfterCmd(command, task2);
        assertEventNotVisibleAfterCmd(command, event3);
        assertEventVisibleAfterCmd(command, event4);
        
        // For console text area to check output message
        expectedNumOfTasks = 2;
        expectedNumOfEvents = 1;
        String expectedOutputMessage = String.format(FindController.MESSAGE_RESULT_FOUND_FORMAT,
                StringUtil.displayNumberOfTaskAndEventFoundWithPuralizer(expectedNumOfTasks, expectedNumOfEvents));
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
    }
    
    @Test
    public void find_tasks_by_keyword() {
        String command = "find buy tasks";
        // To check if tasks are been filtered as expected
        assertTaskVisibleAfterCmd(command, task1);
        assertTaskVisibleAfterCmd(command, task2);
        assertEventNotVisibleAfterCmd(command, event3);
        assertEventNotVisibleAfterCmd(command, event4);
        
        // For console text area to check output message
        expectedNumOfTasks = 2;
        expectedNumOfEvents = 0;
        String expectedOutputMessage = String.format(FindController.MESSAGE_RESULT_FOUND_FORMAT,
                StringUtil.displayNumberOfTaskAndEventFoundWithPuralizer(expectedNumOfTasks, expectedNumOfEvents));
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
    }
    
    @Test
    public void find_events_by_keyword() {
        String command = "find buy events";
        // To check if event4 is been filtered
        assertTaskNotVisibleAfterCmd(command, task1);
        assertTaskNotVisibleAfterCmd(command, task2);
        assertEventNotVisibleAfterCmd(command, event3);
        assertEventVisibleAfterCmd(command, event4);
        
        // For console text area to check output message
        expectedNumOfTasks = 0;
        expectedNumOfEvents = 1;
        String expectedOutputMessage = String.format(FindController.MESSAGE_RESULT_FOUND_FORMAT,
                StringUtil.displayNumberOfTaskAndEventFoundWithPuralizer(expectedNumOfTasks, expectedNumOfEvents));
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
    }
    
    @Test
    public void find_by_invalid_keyword() {
        String command = "find tester";
        // To check if all tasks and events are still in the view
        assertTaskVisibleAfterCmd(command, task1);
        assertTaskVisibleAfterCmd(command, task2);
        assertEventVisibleAfterCmd(command, event3);
        assertEventVisibleAfterCmd(command, event4);
        
        // For console text area to check output message
        assertEquals(console.getConsoleTextArea(), FindController.MESSAGE_NO_RESULT_FOUND);
    }
    
    @Test
    public void find_by_tasks_complete_status() {
        console.runCommand("complete 1");
        String command = "find buy complete";
        // To check if task1 is been filtered as expected
        assertTaskVisibleAfterCmd(command, task1);
        assertTaskNotVisibleAfterCmd(command, task2);
        assertEventNotVisibleAfterCmd(command, event3);
        assertEventNotVisibleAfterCmd(command, event4);
        
        // For console text area to check output message
        expectedNumOfTasks = 1;
        expectedNumOfEvents = 0;
        String expectedOutputMessage = String.format(FindController.MESSAGE_RESULT_FOUND_FORMAT,
                StringUtil.displayNumberOfTaskAndEventFoundWithPuralizer(expectedNumOfTasks, expectedNumOfEvents));
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
    }
    
    @Test
    public void find_by_tassk_incomplete_status() {
        String command = "find buy incomplete";
        // To check if tasks are been filtered as expected
        assertTaskVisibleAfterCmd(command, task1);
        assertTaskVisibleAfterCmd(command, task2);
        assertEventNotVisibleAfterCmd(command, event3);
        assertEventNotVisibleAfterCmd(command, event4);
        
        // For console text area to check output message
        expectedNumOfTasks = 2;
        expectedNumOfEvents = 0;
        String expectedOutputMessage = String.format(FindController.MESSAGE_RESULT_FOUND_FORMAT,
                StringUtil.displayNumberOfTaskAndEventFoundWithPuralizer(expectedNumOfTasks, expectedNumOfEvents));
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
    }
    
    @Test
    public void find_by_events_current_status() {
        String command = "find buy current";
        // To check if event3 is been filtered as expected
        assertTaskNotVisibleAfterCmd(command, task1);
        assertTaskNotVisibleAfterCmd(command, task2);
        assertEventNotVisibleAfterCmd(command, event3);
        assertEventVisibleAfterCmd(command, event4);
        
        // For console text area to check output message
        expectedNumOfTasks = 0;
        expectedNumOfEvents = 1;
        String expectedOutputMessage = String.format(FindController.MESSAGE_RESULT_FOUND_FORMAT,
                StringUtil.displayNumberOfTaskAndEventFoundWithPuralizer(expectedNumOfTasks, expectedNumOfEvents));
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
    }
    
    @Test
    public void find_by_events_over_status() {
        String command = "find buy over";
        // To check if all tasks and events are still in the view
        assertTaskVisibleAfterCmd(command, task1);
        assertTaskVisibleAfterCmd(command, task2);
        assertEventVisibleAfterCmd(command, event3);
        assertEventVisibleAfterCmd(command, event4);
        
        // For console text area to check output message
        assertEquals(console.getConsoleTextArea(), FindController.MESSAGE_NO_RESULT_FOUND);
    }
    
    @Test
    public void find_by_single_date() {
        String command = "find buy on today";
        // To check if task1 is been filtered as expected
        assertTaskVisibleAfterCmd(command, task1);
        assertTaskNotVisibleAfterCmd(command, task2);
        assertEventNotVisibleAfterCmd(command, event3);
        assertEventNotVisibleAfterCmd(command, event4);
        
        // For console text area to check output message
        expectedNumOfTasks = 1;
        expectedNumOfEvents = 0;
        String expectedOutputMessage = String.format(FindController.MESSAGE_RESULT_FOUND_FORMAT,
                StringUtil.displayNumberOfTaskAndEventFoundWithPuralizer(expectedNumOfTasks, expectedNumOfEvents));
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
    }
    
    @Test
    public void find_by_date_range() {
        String command = "find buy from today";
        // To check if tasks and events are been filtered as expected
        assertTaskVisibleAfterCmd(command, task1);
        assertTaskVisibleAfterCmd(command, task2);
        assertEventNotVisibleAfterCmd(command, event3);
        assertEventVisibleAfterCmd(command, event4);

        // For console text area to check output message
        expectedNumOfTasks = 2;
        expectedNumOfEvents = 1;
        String expectedOutputMessage = String.format(FindController.MESSAGE_RESULT_FOUND_FORMAT,
                StringUtil.displayNumberOfTaskAndEventFoundWithPuralizer(expectedNumOfTasks, expectedNumOfEvents));
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
    }
    
    @Test
    public void find_tasks_by_single_date() {
        String command = "find buy tasks on today";
        
        // To check if task1 is been filtered as expected
        assertTaskVisibleAfterCmd(command, task1);
        assertTaskNotVisibleAfterCmd(command, task2);
        assertEventNotVisibleAfterCmd(command, event3);
        assertEventNotVisibleAfterCmd(command, event4);
        
        // For console text area to check output message
        expectedNumOfTasks = 1;
        expectedNumOfEvents = 0;
        String expectedOutputMessage = String.format(FindController.MESSAGE_RESULT_FOUND_FORMAT,
                StringUtil.displayNumberOfTaskAndEventFoundWithPuralizer(expectedNumOfTasks, expectedNumOfEvents));
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
    }
    
    @Test 
    public void find_tasks_by_date_range() {
        String command = "find buy tasks from today to tmr";
        
        // To check if all tasks are been filtered as expected
        assertTaskVisibleAfterCmd(command, task1);
        assertTaskVisibleAfterCmd(command, task2);
        assertEventNotVisibleAfterCmd(command, event3);
        assertEventNotVisibleAfterCmd(command, event4);
        
        // For console text area to check output message
        expectedNumOfTasks = 2;
        expectedNumOfEvents = 0;
        String expectedOutputMessage = String.format(FindController.MESSAGE_RESULT_FOUND_FORMAT,
                StringUtil.displayNumberOfTaskAndEventFoundWithPuralizer(expectedNumOfTasks, expectedNumOfEvents));
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
    }
    
    @Test
    public void find_tasks_by_date_range_with_single_date() {
        String command = "find buy tasks from today";
        // To check if all tasks are been filtered as expected
        assertTaskVisibleAfterCmd(command, task1);
        assertTaskVisibleAfterCmd(command, task2);
        assertEventNotVisibleAfterCmd(command, event3);
        assertEventNotVisibleAfterCmd(command, event4);
        
        // For console text area to check output message
        expectedNumOfTasks = 2;
        expectedNumOfEvents = 0;
        String expectedOutputMessage = String.format(FindController.MESSAGE_RESULT_FOUND_FORMAT,
                StringUtil.displayNumberOfTaskAndEventFoundWithPuralizer(expectedNumOfTasks, expectedNumOfEvents));
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
    }
    
    @Test
    public void find_events_by_single_date() {
        String command = "find CS2103 events on tomorrow";
        // To check if event3 is been filtered as expected
        assertTaskNotVisibleAfterCmd(command, task1);
        assertTaskNotVisibleAfterCmd(command, task2);
        assertEventVisibleAfterCmd(command, event3);
        assertEventNotVisibleAfterCmd(command, event4);
        
        // For console text area to check output message
        expectedNumOfTasks = 0;
        expectedNumOfEvents = 1;
        String expectedOutputMessage = String.format(FindController.MESSAGE_RESULT_FOUND_FORMAT,
                StringUtil.displayNumberOfTaskAndEventFoundWithPuralizer(expectedNumOfTasks, expectedNumOfEvents));
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
    }
    
    @Test
    public void find_events_by_date_range_with_single_date() {
        String command = "find buy events from today";
        // To check if event4 is been filtered as expected
        assertTaskNotVisibleAfterCmd(command, task1);
        assertTaskNotVisibleAfterCmd(command, task2);
        assertEventNotVisibleAfterCmd(command, event3);
        assertEventVisibleAfterCmd(command, event4);
        
        // For console text area to check output message
        expectedNumOfTasks = 0;
        expectedNumOfEvents = 1;
        String expectedOutputMessage = String.format(FindController.MESSAGE_RESULT_FOUND_FORMAT,
                StringUtil.displayNumberOfTaskAndEventFoundWithPuralizer(expectedNumOfTasks, expectedNumOfEvents));
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
    }
    
    @Test
    public void find_events_by_date_range() {
        String command = "find CS2103 events from today to tmr";
        // To check if event3 is been filtered as expected
        assertTaskNotVisibleAfterCmd(command, task1);
        assertTaskNotVisibleAfterCmd(command, task2);
        assertEventVisibleAfterCmd(command, event3);
        assertEventNotVisibleAfterCmd(command, event4);
        
        // For console text area to check output message
        expectedNumOfTasks = 0;
        expectedNumOfEvents = 1;
        String expectedOutputMessage = String.format(FindController.MESSAGE_RESULT_FOUND_FORMAT,
                StringUtil.displayNumberOfTaskAndEventFoundWithPuralizer(expectedNumOfTasks, expectedNumOfEvents));
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
    }
    
    @Test
    public void find_missingKeywords_disambiguate() {
        String command = "find";
        console.runCommand(command);
        
        // To check if the consoleInputText matches the controller corrected syntax
        String expectedDisambiguation = FindController.COMMAND_SYNTAX;
        assertEquals(console.getConsoleInputText(), expectedDisambiguation);
        
        // For console text area to check error message
        String expectedOutputMessage = formatConsoleOutputTextArea(FindController.MESSAGE_NO_KEYWORD_FOUND);
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
    }
    
    @Test
    public void find_invalidTaskSyntax_disambiguate() {
        String command = "find buy task over";
        console.runCommand(command);
        
        // To check if the consoleInputText matches the controller corrected syntax
        String expectedDisambiguation = FindController.FIND_TASK_SYNTAX;
        assertEquals(console.getConsoleInputText(), expectedDisambiguation);
        
        // For console text area to check error message
        String expectedOutputMessage = formatConsoleOutputTextArea(FindController.MESSAGE_INVALID_TASK_STATUS);
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
    }
    
    @Test
    public void find_invalidEventSyntax_disambiguate() {
        String command = "find buy event complete";
        console.runCommand(command);
        
        // To check if the consoleInputText matches the controller corrected syntax
        String expectedDisambiguation = FindController.FIND_EVENT_SYNTAX;
        assertEquals(console.getConsoleInputText(), expectedDisambiguation);

        // For console text area to check error message
        String expectedOutputMessage = formatConsoleOutputTextArea(FindController.MESSAGE_INVALID_EVENT_STATUS);
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
    }
    
    @Test
    public void find_with_invalid_single_date() {
        String command = "find buy by todar";
        console.runCommand(command);
        
        // To check if the consoleInputText matches the controller corrected syntax
        String expectedDisambiguation = FindController.COMMAND_SYNTAX;
        assertEquals(console.getConsoleInputText(), expectedDisambiguation);
        
        // For console text area to check error message
        String expectedOutputMessage = formatConsoleOutputTextArea(FindController.MESSAGE_NO_DATE_DETECTED);
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
    }
    
    @Test
    public void find_with_date_conflict() {
        String command = "find buy by today from tmr";
        console.runCommand(command);
        
        // To check if the consoleInputText matches the controller corrected syntax
        String expectedDisambiguation = FindController.COMMAND_SYNTAX;
        assertEquals(console.getConsoleInputText(), expectedDisambiguation);
        
        // For console text area to check error message
        String expectedOutputMessage = formatConsoleOutputTextArea(FindController.MESSAGE_DATE_CONFLICT);
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
    }
    
    @Test
    public void find_with_invalid_date_range() {
        String command = "find buy from today to tml";
        console.runCommand(command);
        
        // To check if the consoleInputText matches the controller corrected syntax
        String expectedDisambiguation = FindController.COMMAND_SYNTAX;
        assertEquals(console.getConsoleInputText(), expectedDisambiguation);
        
        // For console text area to check error message
        String expectedOutputMessage = formatConsoleOutputTextArea(FindController.MESSAGE_NO_DATE_DETECTED);
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
    }
}
