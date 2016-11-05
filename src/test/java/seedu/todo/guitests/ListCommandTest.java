package seedu.todo.guitests;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import seedu.todo.guitests.guihandles.TaskListDateItemHandle;
import seedu.todo.guitests.guihandles.TaskListEventItemHandle;
import seedu.todo.guitests.guihandles.TaskListTaskItemHandle;
import seedu.todo.models.CalendarItem;
import seedu.todo.models.Event;
import seedu.todo.models.Task;

//@@author A0139922Y
public class ListCommandTest extends GuiTest {

//    // Variables to be use by Test Cases
//    private static final String TODAY = "today";
//    private static final LocalDateTime TODAY_DATE = LocalDateTime.now();
//    private static final String TOMORROW = "tomorrow";
//    private static final LocalDateTime TOMORROW_DATE = LocalDateTime.now().plusDays(1);
//    private static final String ADD = "add";
//    //private final List<CalendarItem> calendarItems = getCalendarItems();
//    private static final int TASK_INDEX = 0;
//    private static final int EVENT_INDEX = 1;
//    
//    private static final String LIST_TASK_SYNTAX = "list task \"complete/incomplete\"";
//    private static final String LIST_EVENT_SYNTAX = "list event \"over/current\"";
//    private static final String LIST_DATE_SYNTAX = "list \"date\" [or from \"date\" to \"date\"]";
//    private static final String LIST_COMMAND_SYNTAX = "list \"task complete/incomplete\" or \"event over/ongoing\""
//            + "[on date] or [from date to date]";
    
//    @Test
//    public void listAllTaskAndEvent() {
//        String command = "list";
//        assertListSuccess(command);
//    }
//    
//    @Test
//    public void listAllTask() {
//        String command = "list task";
//        // There is only 1 task by Today Date
//        assertListByDate(command, TODAY_DATE);
//    }
//
//    @Test
//    public void listAllEvent() {
//        String command = "list event";
//        // There is only 1 event by Tomorrow Date
//        assertListByDate(command, TOMORROW_DATE); 
//    }
//    
//    @Test 
//    public void listCurrentEvent() {
//        String command = "list current";
//        // There is only 1 event by Tomorrow Date
//        assertListByDate(command, TOMORROW_DATE); 
//    }
//    
//    @Test 
//    public void listOverEvent() {
//        String command = "list over";
//        // There are no events that are over
//        assertListSuccess(command);
//    }
//
//    @Test
//    public void listCompleteTasks() {
//        String command = "list complete";
//        // There are no task that is completed
//        assertListSuccess(command);
//    }
//    
//    @Test
//    public void listIncompleteTasks() {
//        String command = "list incomplete";
//        // There is only 1 task by today date
//        assertListByDate(command, TODAY_DATE);
//    }
//    
//    @Test
//    public void listByDate() {
//        String command = "list by today";
//        assertListByDate(command, TODAY_DATE);
//        command = "list today";
//        assertListByDate(command, TODAY_DATE);
//    }
//    
//    @Test
//    public void listWithInvalidDateSyntax() {
//        String command = "list by todar";
//        assertListFailed(command, LIST_DATE_SYNTAX);
//        command = "list today over";
//        assertListFailed(command, LIST_DATE_SYNTAX);
//        command = "list todar";
//        assertListFailed(command, LIST_DATE_SYNTAX);
//    }
//    
//    @Test
//    public void listWithTaskSyntaxError() {
//        String command = "list task over";
//        assertListFailed(command, LIST_TASK_SYNTAX);
//        command = "list task current";
//        assertListFailed(command, LIST_TASK_SYNTAX);        
//    }
//    
//    @Test
//    public void listWithEventSyntaxError() {
//        String command = "list event complete";
//        assertListFailed(command, LIST_EVENT_SYNTAX);
//        command = "list event incomplete";
//        assertListFailed(command, LIST_EVENT_SYNTAX);
//    }
//    
//    @Test
//    public void listWithCommandSyntaxError() {
//        String command = "list task event";
//        assertListFailed(command, LIST_COMMAND_SYNTAX);
//        command = "list event task";
//        assertListFailed(command, LIST_COMMAND_SYNTAX);
//    }
//    
//    /*========================================== Initalise DB Methods ==============================*/
//    
//    /*
//     * Use to generate data for Test DB
//     * 
//     */
//    private List<CalendarItem> getCalendarItems() {
//        Event event = new Event();
//        event.setName("Presentation in the Future");
//        event.setStartDate(TOMORROW_DATE);
//        event.setEndDate(TOMORROW_DATE);
//        Task task = new Task();
//        task.setName("Buy Milk");
//        task.setCalendarDateTime(TODAY_DATE);
//        List<CalendarItem> calendarItems = new ArrayList<CalendarItem>();
//        calendarItems.add(task);
//        calendarItems.add(event);
//        return calendarItems;
//    }
//    
//    /*
//     * Use to initialise Test DB for all different test cases
//     * 
//     */
//    private void initialiseTestDB() {
//        String addCommand = "";
//        for (int i = 0; i < calendarItems.size(); i ++) {
//            CalendarItem calendarItem = calendarItems.get(i);
//            if (calendarItems.get(i) instanceof Task) {
//                addCommand = formatAddTaskCommand((Task) calendarItem);
//            }
//            else if (calendarItems.get(i) instanceof Event) {
//                addCommand = formatAddEventCommand((Event) calendarItem);
//            }
//            console.runCommand(addCommand);
//        }
//    }
//    
//    /*
//     * Formatting to the correct add Event command
//     * 
//     */
//    private String formatAddEventCommand(Event event) {
//        String dateFrom = "from";
//        String dateTo = "to";
//        String eventType = "event";
//        return String.format("%s %s %s %s %s %s %s", ADD, eventType, event.getName(), dateFrom, TOMORROW, dateTo, TOMORROW);
//    }
//    
//    /*
//     * Formatting to the correct add Task command
//     * 
//     */
//    private String formatAddTaskCommand(Task task) {
//        String dateBy = "by";
//        return String.format("%s %s %s %s", ADD, task.getName(), dateBy, TODAY);
//    }
//    
//    /*================================= Assert Helper Methods =============================*/
//    
//    /**
//     * Method for testing if task and events to be listed into the GUI.
//     * This runs a command and checks if all item have been listed
//     */
//    private void assertListSuccess(String command) {
//        // Run the command in the console.
//        initialiseTestDB();
//        console.runCommand(command);
//        
//        //To check for both dates
//        LocalDate date = TODAY_DATE.toLocalDate();
//        TaskListDateItemHandle dateItem = taskList.getTaskListDateItem(date);
//        TaskListTaskItemHandle taskItem = dateItem.getTaskListTaskItem(calendarItems.get(TASK_INDEX).getName());
//        assertEquals(taskItem.getName(), calendarItems.get(TASK_INDEX).getName());
//        date = TOMORROW_DATE.toLocalDate();
//        dateItem = taskList.getTaskListDateItem(date);
//        TaskListEventItemHandle eventItem = dateItem.getTaskListEventItem(calendarItems.get(EVENT_INDEX).getName());
//        assertEquals(eventItem.getName(), calendarItems.get(EVENT_INDEX).getName());
//        console.runCommand("clear");
//    }
//    
//    /**
//     * Method for testing if task and events to be listed into the GUI by date.
//     * This runs a command and checks if right item is been listed by date.
//     */
//    private void assertListByDate(String command, LocalDateTime date) {
//        // Run the command in the console.
//        initialiseTestDB();
//        console.runCommand(command);
//        
//        if (TODAY_DATE.equals(date)) {
//            LocalDate localDate = date.toLocalDate();
//            TaskListDateItemHandle dateItem = taskList.getTaskListDateItem(localDate);
//            TaskListTaskItemHandle taskItem = dateItem.getTaskListTaskItem(calendarItems.get(TASK_INDEX).getName());
//            assertEquals(taskItem.getName(), calendarItems.get(TASK_INDEX).getName());
//        } else if (TOMORROW_DATE.equals(date)){
//            LocalDate localDate = date.toLocalDate();
//            TaskListDateItemHandle dateItem = taskList.getTaskListDateItem(localDate);
//            TaskListEventItemHandle eventItem = dateItem.getTaskListEventItem(calendarItems.get(EVENT_INDEX).getName());
//            assertEquals(eventItem.getName(), calendarItems.get(EVENT_INDEX).getName());
//        }
//        console.runCommand("clear");
//    }
//    
//    /**
//     * Method for testing if task and events have failed to list.
//     * This runs a command and checks if the consoleInputTextField matches with the expect error message.
//     */
//    public void assertListFailed(String command, String expectedDisambiguation) {
//        console.runCommand(command);
//        assertEquals(console.getConsoleInputText(), expectedDisambiguation); 
//    }
    
}
