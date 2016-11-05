package seedu.todo.guitests;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import seedu.todo.guitests.guihandles.TaskListDateItemHandle;
import seedu.todo.models.CalendarItem;
import seedu.todo.models.Event;
import seedu.todo.models.Task;

//@@author A0139922Y
public class ClearCommandTest extends GuiTest {

    // Variables to be use by Test Cases
    private static final String TODAY = "today";
    private static final LocalDateTime TODAY_DATE = LocalDateTime.now();
    private static final String TOMORROW = "tomorrow";
    private static final LocalDateTime TOMORROW_DATE = LocalDateTime.now().plusDays(1);
    private static final String ADD = "add";
    private final List<CalendarItem> calendarItems = getCalendarItems();
    
    @Test
    public void clearAllTaskAndEvent() {
        String command = "clear";
        assertClearSuccess(command);
    }
    
    @Test
    public void clearAllTask() {
        String command = "clear task";
        assertClearSuccessByTask(command);
    }
    
    @Test
    public void clearAllEvent() {
        String command = "clear event";
        assertClearSuccessByEvent(command);
    }
    
    @Test
    public void clearBySingleDate() {
        String command = "clear today";
        assertClearSuccessByDate(command, TODAY_DATE);
        command = "clear by today";
        assertClearSuccessByDate(command, TODAY_DATE);
    }
    
    @Test
    public void clearByDateRange() {
        String command = "clear from today to today";
        assertClearSuccessByDate(command, TODAY_DATE);
        command = "clear from today to tomorrow";
        assertClearSuccessByDate(command, TODAY_DATE);
        assertClearSuccessByDate(command, TOMORROW_DATE); 
    }
    
    @Test
    public void clearWithInvalidDate() {
        String command = "clear toda";
        assertClearFailedByDate(command);
    }
    
    @Test
    public void clearWithInvalidDateSyntax() {
        String command = "clear today on tomorrow";
        assertClearFailedByDate(command);
        command = "clear today to tomrrow";
        assertClearFailedByDate(command);
        command = "clear by today to tomorrow";
        assertClearFailedByDate(command);
    }
    
    @Test
    public void clearWithInvalidStatus() {
        String command = "clear over";
        assertClearFailedBySyntax(command);
        command = "clear complete";
        assertClearFailedBySyntax(command);
    }
    
    @Test
    public void clearWithInvalidItemType() {
        String command = "clear task event";
        assertClearFailedBySyntax(command);
        command = "clear event task";
        assertClearFailedBySyntax(command);
    }
    
    /*
     * Use to generate data for Test DB
     * 
     */
    private List<CalendarItem> getCalendarItems() {
        Event event = new Event();
        event.setName("Presentation in the Future");
        event.setStartDate(TOMORROW_DATE);
        event.setEndDate(TOMORROW_DATE);
        Task task = new Task();
        task.setName("Buy Milk");
        task.setCalendarDateTime(TODAY_DATE);
        List<CalendarItem> calendarItems = new ArrayList<CalendarItem>();
        calendarItems.add(task);
        calendarItems.add(event);
        return calendarItems;
    }
    
    /*
     * Use to initialise Test DB for all different test cases
     * 
     */
    private void initialiseTestDB() {
        String addCommand = "";
        for (int i = 0; i < calendarItems.size(); i ++) {
            CalendarItem calendarItem = calendarItems.get(i);
            if (calendarItems.get(i) instanceof Task) {
                addCommand = formatAddTaskCommand((Task) calendarItem);
            }
            else if (calendarItems.get(i) instanceof Event) {
                addCommand = formatAddEventCommand((Event) calendarItem);
            }
            console.runCommand(addCommand);
        }
    }
    
    /*
     * Formatting to the correct add Event command
     * 
     */
    private String formatAddEventCommand(Event event) {
        String dateFrom = "from";
        String dateTo = "to";
        String eventType = "event";
        return String.format("%s %s %s %s %s %s %s", ADD, eventType, event.getName(), dateFrom, TOMORROW, dateTo, TOMORROW);
    }
    
    /*
     * Formatting to the correct add Task command
     * 
     */
    private String formatAddTaskCommand(Task task) {
        String dateBy = "by";
        return String.format("%s %s %s %s", ADD, task.getName(), dateBy, TODAY);
    }
    
    /**
     * Method for testing if task and events have been cleared from the GUI.
     * This runs a command and checks if TaskList based on date has been cleared by checking null.
     */
    private void assertClearSuccess(String command) {
        // Run the command in the console.
        getCalendarItems();
        initialiseTestDB();
        console.runCommand(command);
        
        for (int i = 0; i < calendarItems.size(); i ++) {
            CalendarItem calendarItem = calendarItems.get(i);
            LocalDate calendarItemStartDate = calendarItem.getCalendarDateTime().toLocalDate();
            TaskListDateItemHandle dateItem = taskList.getTaskListDateItem(calendarItemStartDate);
            assertNull(dateItem);
        }
    }
    
    /**
     * Method for testing if tasks have been cleared from the GUI.
     * This runs a command and checks if TaskList based on Task's date has been cleared by checking null.
     */
    private void assertClearSuccessByTask(String command) {
        getCalendarItems();
        initialiseTestDB();
        console.runCommand(command);
        for (int i = 0; i < calendarItems.size(); i ++) {
            CalendarItem calendarItem = calendarItems.get(i);
            if (calendarItem instanceof Task) {
                LocalDate calendarItemStartDate = calendarItem.getCalendarDateTime().toLocalDate();
                TaskListDateItemHandle dateItem = taskList.getTaskListDateItem(calendarItemStartDate);
                assertNull(dateItem);
            }
        }
    }
    
    /**
     * Method for testing if events have been cleared from the GUI.
     * This runs a command and checks if TaskList based on Event's date has been cleared by checking null.
     */
    private void assertClearSuccessByEvent(String command) {
        getCalendarItems();
        initialiseTestDB();
        console.runCommand(command);
        for (int i = 0; i < calendarItems.size(); i ++) {
            CalendarItem calendarItem = calendarItems.get(i);
            if (calendarItem instanceof Event) {
                LocalDate calendarItemStartDate = calendarItem.getCalendarDateTime().toLocalDate();
                TaskListDateItemHandle dateItem = taskList.getTaskListDateItem(calendarItemStartDate);
                assertNull(dateItem);
            }
        }
    }
    
    /**
     * Method for testing if all tasks and events have been cleared from the GUI by the particular date.
     * This runs a command and checks if TaskList based on date has been cleared by checking null.
     */
    private void assertClearSuccessByDate(String command, LocalDateTime date) {
        getCalendarItems();
        initialiseTestDB();
        console.runCommand(command);
        LocalDate calendarItemStartDate = date.toLocalDate();
        TaskListDateItemHandle dateItem = taskList.getTaskListDateItem(calendarItemStartDate);
        assertNull(dateItem);
    }
    
    /**
     * Method for testing if task and events have been cleared from the GUI.
     * This runs a command and checks if the consoleInputTextField matches the error matches
     */
    public void assertClearFailedByDate(String command) {
        console.runCommand("add test"); //To initialise UI
        console.runCommand(command);
        String expectedDisambiguation = "clear \"date\" [or from \"date\" to \"date\"]";
        assertEquals(console.getConsoleInputText(), expectedDisambiguation); 
    }
    
    /**
     * Method for testing if task and events have been cleared from the GUI.
     * This runs a command and checks if the consoleInputTextField matches the error matches
     */
    public void assertClearFailedBySyntax(String command) {
        console.runCommand("add test"); //To initialise UI
        console.runCommand(command);
        String expectedDisambiguation = "clear \"task/event\" on \"date\"";
        assertEquals(console.getConsoleInputText(), expectedDisambiguation); 
    }
}
