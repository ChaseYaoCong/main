package seedu.todo.guitests;

import static org.junit.Assert.*;
import static seedu.todo.testutil.AssertUtil.assertSameDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import seedu.todo.commons.util.DateUtil;
import seedu.todo.guitests.guihandles.TaskListDateItemHandle;
import seedu.todo.guitests.guihandles.TaskListEventItemHandle;
import seedu.todo.models.CalendarItem;
import seedu.todo.models.Event;
import seedu.todo.models.Task;

//@@author A0139922Y
public class ClearCommandTest extends GuiTest {

    private static final String TODAY = "today";
    private static final LocalDateTime TODAY_DATE = LocalDateTime.now();
    private static final String TOMORROW = "tomorrow";
    private static final LocalDateTime TOMORROW_DATE = LocalDateTime.now().plusDays(1);
    private static final String SPACE = " ";
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
    public void clearByDate() {
        String command = "clear today";
        assertClearSuccessByDate(command, TODAY_DATE);
        command = "clear by today";
        assertClearSuccessByDate(command, TODAY_DATE);
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
    
    private String formatAddEventCommand(Event event) {
        String dateFrom = "from";
        String dateTo = "to";
        String eventType = "event";
        return ADD + SPACE + eventType + SPACE + event.getName() + SPACE + dateFrom + SPACE + TODAY + SPACE + dateTo + SPACE + TOMORROW;
    }
    
    private String formatAddTaskCommand(Task task) {
        String dateBy = "by";
        return ADD + SPACE + task.getName() + SPACE + dateBy + SPACE + TODAY;
    }
    
    /**
     * Method for testing if task and events have been cleared from the GUI.
     * This runs a command and checks if TaskList has been cleared by checking null.
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
    
    private void assertClearSuccessByTask(String command) {
        getCalendarItems();
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
    
    private void assertClearSuccessByEvent(String command) {
        getCalendarItems();
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
     * This runs a command and checks if TaskList has been cleared by checking null.
     */
    private void assertClearSuccessByDate(String command, LocalDate date) {
        // Run the command in the console.
//        initialiseTestDB();
//        console.runCommand(command);
//        
//        for (int i = 0; i < calendarItems.size(); i ++) {
//            CalendarItem calendarItem = calendarItems.get(i);
//            LocalDate calendarItemStartDate = calendarItem.getCalendarDateTime().toLocalDate();
//            TaskListDateItemHandle dateItem = taskList.getTaskListDateItem(calendarItemStartDate);
//            assertNull(dateItem);
//        }
    }
    
    /**
     * Method for testing if task and events have been cleared from the GUI.
     * This runs a command and checks if TaskList has been cleared by checking null.
     */
    public void assertClearFailedByDate(String command) {
        console.runCommand("add test"); //To initialise UI
        console.runCommand(command);
        String expectedDisambiguation = "clear \"date\" [or from \"date\" to \"date\"]";
        assertEquals(console.getConsoleInputText(), expectedDisambiguation); 
    }
    
    /**
     * Method for testing if task and events have been cleared from the GUI.
     * This runs a command and checks if TaskList has been cleared by checking null.
     */
    public void assertClearFailedBySyntax(String command) {
        console.runCommand("add test"); //To initialise UI
        console.runCommand(command);
        String expectedDisambiguation = "clear \"task/event\" on \"date\"";
        assertEquals(console.getConsoleInputText(), expectedDisambiguation); 
    }

//    @Test
//    public void addEvent_eventSameDateInPast_isNotVisible() {
//        // Get formatted string for two days before now, e.g. 13 Oct 2016
//        LocalDateTime twoDaysBeforeNow = LocalDateTime.now().minusDays(2);
//        String twoDaysBeforeNowString = DateUtil.formatDate(twoDaysBeforeNow);
//        String twoDaysBeforeNowIsoString = DateUtil.formatIsoDate(twoDaysBeforeNow);
//        
//        // Creates a task in the same date to make sure that a DateItem is created but not a EventItem
//        console.runCommand("add Task in the same day by " + twoDaysBeforeNowString);
//        
//        String command = String.format("add event Presentation in the Past from %s 2pm to %s 9pm", twoDaysBeforeNowString, twoDaysBeforeNowString);
//        Event event = new Event();
//        event.setName("Presentation in the Past");
//        event.setStartDate(DateUtil.parseDateTime(String.format("%s 14:00:00", twoDaysBeforeNowIsoString)));
//        event.setEndDate(DateUtil.parseDateTime(String.format("%s 19:00:00", twoDaysBeforeNowIsoString)));
//        assertAddNotVisible(command, event);
//    }
//    
//    @Test
//    public void addEvent_missingStartDate_disambiguate() {
//        String command = "add event Presentation that never starts to 9pm";
//        console.runCommand(command);
//        String expectedDisambiguation = "add event \"Presentation that never starts\" from \"<start time>\" to \"9pm\"";
//        assertEquals(console.getConsoleInputText(), expectedDisambiguation);
//    }
//
//    @Test
//    public void addEvent_missingEndDate_disambiguate() {
//        // TODO
//    }
//
//    @Test
//    public void addEvent_missingStartEndDate_disambiguate() {
//        // TODO
//    }
//    
//    @Test
//    public void addEvent_missingName_disambiguate() {
//        String command = "add event from 2pm to 9pm";
//        console.runCommand(command);
//        String expectedDisambiguation = "add event \"<name>\" from \"2pm\" to \"9pm\"";
//        assertEquals(console.getConsoleInputText(), expectedDisambiguation);
//    }
//    
//    @Test
//    public void addEvent_unmatchedQuotes_commandRemains() {
//        String command = "add event \"Presentation from 2pm to 9pm";
//        console.runCommand(command);
//        assertEquals(console.getConsoleInputText(), command);
//    }
//
//    /**
//     * Utility method for testing if event has been successfully added to the GUI.
//     * This runs a command and checks if TaskList contains TaskListEventItem that matches
//     * the task that was just added. <br><br>
//     * 
//     * TODO: Extract out method in AddController that can return task from command,
//     *       and possibly remove the need to have eventToAdd.
//     */
//    private void assertAddSuccess(String command, Event eventToAdd) {
//        // Run the command in the console.
//        console.runCommand(command);
//        
//        // Get the event date.
//        LocalDateTime eventStartDateTime = eventToAdd.getStartDate();
//        if (eventStartDateTime == null) {
//            eventStartDateTime = DateUtil.NO_DATETIME_VALUE;
//        }
//        LocalDate eventStartDate = eventStartDateTime.toLocalDate();
//        
//        // Check TaskList if it contains a TaskListDateItem with the date of the event start date.
//        TaskListDateItemHandle dateItem = taskList.getTaskListDateItem(eventStartDate);
//        assertSameDate(eventStartDate, dateItem);
//        
//        // Check TaskListDateItem if it contains the TaskListEventItem with the same data.
//        TaskListEventItemHandle eventItem = dateItem.getTaskListEventItem(eventToAdd.getName());
//        assertEquals(eventItem.getName(), eventToAdd.getName());
//    }
//    
//    private void assertAddNotVisible(String command, Event eventToAdd) {
//        // Run the command in the console.
//        console.runCommand(command);
//        
//        // Get the event date.
//        LocalDateTime eventStartDateTime = eventToAdd.getStartDate();
//        if (eventStartDateTime == null) {
//            eventStartDateTime = DateUtil.NO_DATETIME_VALUE;
//        }
//        LocalDate eventStartDate = eventStartDateTime.toLocalDate();
//        
//        // Gets the date item that might contain the event
//        TaskListDateItemHandle dateItem = taskList.getTaskListDateItem(eventStartDate);
//        
//        // It's fine if there's not date item, because it's not visible.
//        if (dateItem == null) {
//            return;
//        }
//        
//        TaskListEventItemHandle eventItem = dateItem.getTaskListEventItem(eventToAdd.getName());
//        assertNull(eventItem);
//    }

}
