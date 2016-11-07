package seedu.todo.guitests;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import seedu.todo.commons.util.DateUtil;
import seedu.todo.commons.util.StringUtil;
import seedu.todo.controllers.ListController;
import seedu.todo.models.Event;
import seedu.todo.models.Task;

/*
 * @@author A0139922Y
 */
public class ListCommandTest extends GuiTest {
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
    private String commandAdd1 = String.format("add task Buy Coco by \"%s 8pm\"", TODAY_STRING);
    private Task task1 = new Task();
    private String commandAdd2 = String.format("add task Buy Milk by \"%s 9pm\"", TOMORROW_STRING);
    private Task task2 = new Task();
    private String commandAdd3 = String.format("add event CS2103 V0.5 Demo from \"%s 4pm\" to \"%s 5pm\"",
            TOMORROW_STRING, TOMORROW_STRING);
    private Event event3 = new Event();
    private String commandAdd4 = String.format("add event buying workshop from \"%s 8pm\" to \"%s 9pm\"",
            THE_DAY_AFTER_TOMORROW_STRING, THE_DAY_AFTER_TOMORROW_STRING);
    private Event event4 = new Event();
    private int expectedNumOfTasks;
    private int expectedNumOfEvents;
    
    // Set up DB
    public ListCommandTest() {
        task1.setName("Buy Coco");
        task1.setCalendarDateTime(DateUtil.parseDateTime(
                String.format("%s 20:00:00", TODAY_ISO_STRING)));
        task1.setCompleted();
        
        task2.setName("Buy Milk");
        task2.setDueDate(DateUtil.parseDateTime(
                String.format("%s 21:00:00", TOMORROW_ISO_STRING)));
        
        event3.setName("CS2103 V0.5 Demo");
        event3.setStartDate(DateUtil.parseDateTime(
                String.format("%s 16:00:00", TOMORROW_ISO_STRING)));
        event3.setEndDate(DateUtil.parseDateTime(
                String.format("%s 17:00:00", TOMORROW_ISO_STRING)));
        
        event4.setName("buying workshop");
        event4.setStartDate(DateUtil.parseDateTime(
                String.format("%s 20:00:00", THE_DAY_AFTER_TOMORROW__ISO_STRING)));
        event4.setEndDate(DateUtil.parseDateTime(
                String.format("%s 21:00:00", THE_DAY_AFTER_TOMORROW__ISO_STRING)));
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
    public void list_all() {
        String command = "list";
        // To check if all tasks and events are still in the view
        assertTaskVisibleAfterCmd(command, task1);
        assertTaskVisibleAfterCmd(command, task2);
        assertEventVisibleAfterCmd(command, event3);
        assertEventVisibleAfterCmd(command, event4);
        
        // For console text area to check output message
        String expectedOutputMessage = ListController.MESSAGE_LIST_SUCCESS;
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
    }
    
    @Test
    public void list_tasks() {
        String command = "list tasks";
        // To check if all tasks are been filtered out as expected
        assertTaskVisibleAfterCmd(command, task1);
        assertTaskVisibleAfterCmd(command, task2);
        assertEventNotVisibleAfterCmd(command, event3);
        assertEventNotVisibleAfterCmd(command, event4);
        
        // For console text area to check output message
        expectedNumOfTasks = 2;
        expectedNumOfEvents = 0;
        String expectedOutputMessage = String.format(ListController.MESSAGE_RESULT_FOUND_FORMAT,
                StringUtil.displayNumberOfTaskAndEventFoundWithPuralizer(expectedNumOfTasks, expectedNumOfEvents));
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
    }
    
    @Test
    public void list_completed_tasks() {
        console.runCommand("complete 1");
        String command = "list complete";
        // To check if task1 is been filtered out as expected
        assertTaskVisibleAfterCmd(command, task1);
        assertTaskNotVisibleAfterCmd(command, task2);
        assertEventNotVisibleAfterCmd(command, event3);
        assertEventNotVisibleAfterCmd(command, event4);
        
        // For console text area to check output message
        expectedNumOfTasks = 1;
        expectedNumOfEvents = 0;
        String expectedOutputMessage = String.format(ListController.MESSAGE_RESULT_FOUND_FORMAT,
                StringUtil.displayNumberOfTaskAndEventFoundWithPuralizer(expectedNumOfTasks, expectedNumOfEvents));
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
    }
    
    @Test 
    public void list_incomplete_tasks() {
        String command = "list incomplete";
        // To check if all tasks are been filtered out as expected
        assertTaskVisibleAfterCmd(command, task1);
        assertTaskVisibleAfterCmd(command, task2);
        assertEventNotVisibleAfterCmd(command, event3);
        assertEventNotVisibleAfterCmd(command, event4);
        
        // For console text area to check output message
        expectedNumOfTasks = 2;
        expectedNumOfEvents = 0;
        String expectedOutputMessage = String.format(ListController.MESSAGE_RESULT_FOUND_FORMAT,
                StringUtil.displayNumberOfTaskAndEventFoundWithPuralizer(expectedNumOfTasks, expectedNumOfEvents));
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
    }
    
    @Test
    public void list_events() {
        String command = "list events";
        // To check if all events are been filtered out as expected
        assertTaskNotVisibleAfterCmd(command, task1);
        assertTaskNotVisibleAfterCmd(command, task2);
        assertEventVisibleAfterCmd(command, event3);
        assertEventVisibleAfterCmd(command, event4);
        
        // For console text area to check output message
        expectedNumOfTasks = 0;
        expectedNumOfEvents = 2;
        String expectedOutputMessage = String.format(ListController.MESSAGE_RESULT_FOUND_FORMAT,
                StringUtil.displayNumberOfTaskAndEventFoundWithPuralizer(expectedNumOfTasks, expectedNumOfEvents));
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
    }
    
    
    @Test
    public void list_over_events() {
        String command = "list over";
        // To check if all tasks events are still in the view
        assertTaskVisibleAfterCmd(command, task1);
        assertTaskVisibleAfterCmd(command, task2);
        assertEventVisibleAfterCmd(command, event3);
        assertEventVisibleAfterCmd(command, event4);
        
        // For console text area to check output message
        String expectedOutputMessage = ListController.MESSAGE_NO_RESULT_FOUND;
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
    }
    
    @Test
    public void list_current_events() {
        String command = "list current";
        // To check if all events are been filtered out as expected
        assertTaskNotVisibleAfterCmd(command, task1);
        assertTaskNotVisibleAfterCmd(command, task2);
        assertEventVisibleAfterCmd(command, event3);
        assertEventVisibleAfterCmd(command, event4);
        
        // For console text area to check output message
        expectedNumOfTasks = 0;
        expectedNumOfEvents = 2;
        String expectedOutputMessage = String.format(ListController.MESSAGE_RESULT_FOUND_FORMAT,
                StringUtil.displayNumberOfTaskAndEventFoundWithPuralizer(expectedNumOfTasks, expectedNumOfEvents));
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
    }
    
    @Test
    public void list_by_single_date() {
        String command = "list today";
        // To check if task1 is been filtered out as expected
        assertTaskVisibleAfterCmd(command, task1);
        assertTaskNotVisibleAfterCmd(command, task2);
        assertEventNotVisibleAfterCmd(command, event3);
        assertEventNotVisibleAfterCmd(command, event4);
        
        // For console text area to check output message
        expectedNumOfTasks = 1;
        expectedNumOfEvents = 0;
        String expectedOutputMessage = String.format(ListController.MESSAGE_RESULT_FOUND_FORMAT,
                StringUtil.displayNumberOfTaskAndEventFoundWithPuralizer(expectedNumOfTasks, expectedNumOfEvents));
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
    }
    
    @Test
    public void list_by_date_range() {
        String command = "list from today";
        // To check if all tasks and events are been filtered out as expected
        assertTaskVisibleAfterCmd(command, task1);
        assertTaskVisibleAfterCmd(command, task2);
        assertEventVisibleAfterCmd(command, event3);
        assertEventVisibleAfterCmd(command, event4);
        
        // For console text area to check output message
        expectedNumOfTasks = 2;
        expectedNumOfEvents = 2;
        String expectedOutputMessage = String.format(ListController.MESSAGE_RESULT_FOUND_FORMAT,
                StringUtil.displayNumberOfTaskAndEventFoundWithPuralizer(expectedNumOfTasks, expectedNumOfEvents));
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
    }
    
    @Test
    public void list_tasks_by_single_date() {
        String command = "list tasks on today";
        // To check if task1 is been filtered out as expected
        assertTaskVisibleAfterCmd(command, task1);
        assertTaskNotVisibleAfterCmd(command, task2);
        assertEventNotVisibleAfterCmd(command, event3);
        assertEventNotVisibleAfterCmd(command, event4);
        
        // For console text area to check output message
        expectedNumOfTasks = 1;
        expectedNumOfEvents = 0;
        String expectedOutputMessage = String.format(ListController.MESSAGE_RESULT_FOUND_FORMAT,
                StringUtil.displayNumberOfTaskAndEventFoundWithPuralizer(expectedNumOfTasks, expectedNumOfEvents));
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
    }
    
    @Test 
    public void list_tasks_by_date_range_with_single_date() {
        String command = "list tasks from today";
        // To check if all tasks are been filtered out as expected
        assertTaskVisibleAfterCmd(command, task1);
        assertTaskVisibleAfterCmd(command, task2);
        assertEventNotVisibleAfterCmd(command, event3);
        assertEventNotVisibleAfterCmd(command, event4);
        
        // For console text area to check output message
        expectedNumOfTasks = 2;
        expectedNumOfEvents = 0;
        String expectedOutputMessage = String.format(ListController.MESSAGE_RESULT_FOUND_FORMAT,
                StringUtil.displayNumberOfTaskAndEventFoundWithPuralizer(expectedNumOfTasks, expectedNumOfEvents));
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
    }
    
    @Test 
    public void list_tasks_by_date_range() {
        String command = "list tasks from today to tmr";
        // To check if all tasks are been filtered out as expected
        assertTaskVisibleAfterCmd(command, task1);
        assertTaskVisibleAfterCmd(command, task2);
        assertEventNotVisibleAfterCmd(command, event3);
        assertEventNotVisibleAfterCmd(command, event4);
        
        // For console text area to check output message
        expectedNumOfTasks = 2;
        expectedNumOfEvents = 0;
        String expectedOutputMessage = String.format(ListController.MESSAGE_RESULT_FOUND_FORMAT,
                StringUtil.displayNumberOfTaskAndEventFoundWithPuralizer(expectedNumOfTasks, expectedNumOfEvents));
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
    }
    
    @Test
    public void list_events_by_single_date() {
        String command = "list events on tomorrow";
        // To check if event3 is been filtered as expected
        assertTaskNotVisibleAfterCmd(command, task1);
        assertTaskNotVisibleAfterCmd(command, task2);
        assertEventVisibleAfterCmd(command, event3);
        assertEventNotVisibleAfterCmd(command, event4);
        
        // For console text area to check output message
        expectedNumOfTasks = 0;
        expectedNumOfEvents = 1;
        String expectedOutputMessage = String.format(ListController.MESSAGE_RESULT_FOUND_FORMAT,
                StringUtil.displayNumberOfTaskAndEventFoundWithPuralizer(expectedNumOfTasks, expectedNumOfEvents));
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
    }
    
    @Test
    public void list_events_by_date_range_with_single_date() {
        String command = "list events from today";
        // To check if all events are been filtered out as expected
        assertTaskNotVisibleAfterCmd(command, task1);
        assertTaskNotVisibleAfterCmd(command, task2);
        assertEventVisibleAfterCmd(command, event3);
        assertEventVisibleAfterCmd(command, event4);
        
        // For console text area to check output message
        expectedNumOfTasks = 0;
        expectedNumOfEvents = 2;
        String expectedOutputMessage = String.format(ListController.MESSAGE_RESULT_FOUND_FORMAT,
                StringUtil.displayNumberOfTaskAndEventFoundWithPuralizer(expectedNumOfTasks, expectedNumOfEvents));
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
    }
    
    @Test
    public void list_events_by_date_range() {
        String command = "list events from today to " + THE_DAY_AFTER_TOMORROW_STRING;
        // To check if all events are been filtered out as expected
        assertTaskNotVisibleAfterCmd(command, task1);
        assertTaskNotVisibleAfterCmd(command, task2);
        assertEventVisibleAfterCmd(command, event3);
        assertEventVisibleAfterCmd(command, event4);
        
        // For console text area to check output message
        expectedNumOfTasks = 0;
        expectedNumOfEvents = 2;
        String expectedOutputMessage = String.format(ListController.MESSAGE_RESULT_FOUND_FORMAT,
                StringUtil.displayNumberOfTaskAndEventFoundWithPuralizer(expectedNumOfTasks, expectedNumOfEvents));
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
    }
    
    @Test
    public void list_invalidTaskSyntax_disambiguate() {
        String command = "list task over";
        console.runCommand(command);
        // For console input text to check controller corrected syntax
        String expectedDisambiguation = ListController.LIST_TASK_SYNTAX;
        assertEquals(console.getConsoleInputText(), expectedDisambiguation);
        
        // For console text area to check error message
        String expectedOutputMessage = formatConsoleOutputTextArea(ListController.MESSAGE_INVALID_TASK_STATUS);
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
    }
    
    @Test
    public void list_invalidEventSyntax_disambiguate() {
        String command = "list event complete";
        console.runCommand(command);
        // For console input text to check controller corrected syntax
        String expectedDisambiguation = ListController.LIST_EVENT_SYNTAX;
        assertEquals(console.getConsoleInputText(), expectedDisambiguation);
        
        // For console text area to check error message
        String expectedOutputMessage = formatConsoleOutputTextArea(ListController.MESSAGE_INVALID_EVENT_STATUS);
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
    }
    
    @Test
    public void list_invalidDateSyntax_disambiguate_with_date_range() {
        String command = "list by today to tml";
        console.runCommand(command);
        // For console input text to check controller corrected syntax
        String expectedDisambiguation = ListController.LIST_DATE_SYNTAX;
        assertEquals(console.getConsoleInputText(), expectedDisambiguation);
        
        // For console text area to check error message
        String expectedOutputMessage = formatConsoleOutputTextArea(ListController.MESSAGE_DATE_CONFLICT);
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
    }
    
    @Test
    public void list_invalidDateSyntax_disambiguate_with_single_date_by_keyword() {
        String command = "list by todar";
        console.runCommand(command);
        // For console input text to check controller corrected syntax
        String expectedDisambiguation = ListController.LIST_DATE_SYNTAX;
        assertEquals(console.getConsoleInputText(), expectedDisambiguation);
        
        // For console text area to check error message
        String expectedOutputMessage = formatConsoleOutputTextArea(ListController.MESSAGE_NO_DATE_DETECTED);
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
    }
    
    @Test
    public void list_invalidDateSyntax_disambiguate_with_single_date() {
        String command = "list todar";
        console.runCommand(command);
        // For console input text to check controller corrected syntax
        String expectedDisambiguation = ListController.LIST_DATE_SYNTAX;
        assertEquals(console.getConsoleInputText(), expectedDisambiguation);
        
        // For console text area to check error message
        String expectedOutputMessage = formatConsoleOutputTextArea(ListController.MESSAGE_NO_DATE_DETECTED);
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
    }
    
    @Test
    public void list_invalidDateSyntax_disambiguate_with_date_conflict() {
        String command = "list today by tmr";
        console.runCommand(command);
        // For console input text to check controller corrected syntax
        String expectedDisambiguation = ListController.LIST_DATE_SYNTAX;
        assertEquals(console.getConsoleInputText(), expectedDisambiguation);
        
        // For console text area to check error message
        String expectedOutputMessage = formatConsoleOutputTextArea(ListController.MESSAGE_DATE_CONFLICT);
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
    }
    
    @Test
    public void list_invalidDateSyntax_disambiguate_with_date_conflict_by_status() {
        String command = "list today over";
        console.runCommand(command);
        // For console input text to check controller corrected syntax
        String expectedDisambiguation = ListController.LIST_DATE_SYNTAX;
        assertEquals(console.getConsoleInputText(), expectedDisambiguation);
        
        // For console text area to check error message
        String expectedOutputMessage = formatConsoleOutputTextArea(ListController.MESSAGE_DATE_CONFLICT);
        assertEquals(console.getConsoleTextArea(), expectedOutputMessage);
    }
}
