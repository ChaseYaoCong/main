package seedu.todo.commons.util;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;

import seedu.todo.models.CalendarItem;
import seedu.todo.models.Event;
import seedu.todo.models.Task;

//@@author A0139922Y
public class FilterUtilTest {
    public static final LocalDateTime TODAY = LocalDateTime.now();
    public static final LocalDateTime TOMORROW = LocalDateTime.now().plusDays(1);
    public static final LocalDateTime YESTERDAY = LocalDateTime.now().minusDays(1);
    Task firstTestTask = getFirstTestTask();
    Task secondTestTask = getSecondTestTask();
    List<Event> overdueEvents = getOverdueEvents();
    List<Event> currentEvents = getCurrentEvents();
    
    /* ======================== Test cases for Filtering Task Methods ========================== */
    
    @Test 
    public void testFilterOutTask_equals() {
        List<CalendarItem> calendarItems = new ArrayList<CalendarItem>();
        calendarItems.add(firstTestTask);
        assertEquals(calendarItems, FilterUtil.filterOutTask(calendarItems));
    }
    
    @Test
    public void testFilterOutTask_not_equals() {
        List<CalendarItem> calendarItems = new ArrayList<CalendarItem>();
        calendarItems.add(firstTestTask);
        calendarItems.addAll(overdueEvents);
        assertNotEquals(calendarItems, FilterUtil.filterOutTask(calendarItems));
    }
    
    @Test
    public void testFilterTaskByNames_filter_with_empty_taskList_equals() {
        List<Task> tasks = getEmptyTaskList();
        HashSet<String> nameList = new HashSet<String>();
        // Empty task list with empty name list
        assertEquals(tasks, FilterUtil.filterTaskByNames(tasks, nameList));
        // Empty task list with name list
        nameList.add("Nothing");
        assertEquals(tasks, FilterUtil.filterTaskByNames(tasks, nameList));
    }
    
    @Test
    public void testFilterTaskByNames_filter_by_fullname_equals() {
        List<Task> tasks = getEmptyTaskList();
        HashSet<String> nameList = new HashSet<String>();
        
        // Filter out first test task
        nameList.add("Buy");
        tasks.add(firstTestTask);
        assertEquals(tasks, FilterUtil.filterTaskByNames(tasks, nameList));
        
        tasks.add(secondTestTask);
        List<Task> filteredResult = getEmptyTaskList();
        filteredResult.add(firstTestTask);
        assertEquals(filteredResult, (FilterUtil.filterTaskByNames(tasks, nameList)));
    }
    
    @Test
    public void testFilterTaskByNames_filter_by_fullname_not_equals() {
        List<Task> tasks = getEmptyTaskList();
        HashSet<String> nameList = new HashSet<String>();
        List<Task> filteredResult = getEmptyTaskList();
        
        nameList.add("Buy");
        tasks.add(firstTestTask);
        tasks.add(secondTestTask);
        filteredResult.add(firstTestTask);
        filteredResult.add(secondTestTask);
        assertNotEquals(filteredResult, (FilterUtil.filterTaskByNames(tasks, nameList)));
    }
    
    @Test
    public void testFilterTaskByNames_filter_by_subname_equals() {
        List<Task> tasks = getEmptyTaskList();
        HashSet<String> nameList = new HashSet<String>();
        List<Task> filteredResult = getEmptyTaskList();
        
        // Filter out first test task
        tasks.add(firstTestTask);
        tasks.add(secondTestTask);
        filteredResult.add(firstTestTask);
        nameList.add("Milk");
        assertEquals(filteredResult, FilterUtil.filterTaskByNames(tasks, nameList));
    }
    
    @Test
    public void testFilterTaskByNames_filter_by_subname_not_equals() {
        List<Task> tasks = getEmptyTaskList();
        HashSet<String> nameList = new HashSet<String>();
        List<Task> filteredResult = getEmptyTaskList();
        
        tasks.add(firstTestTask);
        tasks.add(secondTestTask);
        filteredResult.add(firstTestTask);
        filteredResult.add(secondTestTask);
        nameList.add("Milk");
        assertNotEquals(filteredResult, FilterUtil.filterTaskByNames(tasks, nameList));
    }
    
    @Test
    public void testFilterTaskByTags_with_empty_task_list_equals() {
        List<Task> tasks = getEmptyTaskList();
        HashSet<String> nameList = new HashSet<String>();
        // Empty task list and name list
        assertEquals(tasks, FilterUtil.filterTaskByTags(tasks, nameList));
        // Empty task list
        nameList.add("Nothing");
        assertEquals(tasks, FilterUtil.filterTaskByTags(tasks, nameList));
    }
    
    @Test
    public void testFilterTaskByTags_equals() {
        List<Task> tasks = getEmptyTaskList();
        HashSet<String> nameList = new HashSet<String>();
        List<Task> filteredResult = getEmptyTaskList();
        
        // Filter out first test task
        nameList.add("personal");
        tasks.add(firstTestTask);
        tasks.add(secondTestTask);
        filteredResult.add(firstTestTask);
        assertEquals(filteredResult, FilterUtil.filterTaskByTags(tasks, nameList));
    }
    
    @Test
    public void testFilterTaskByTags_not_equals() {
        List<Task> tasks = getEmptyTaskList();
        HashSet<String> nameList = new HashSet<String>();
        List<Task> filteredResult = getEmptyTaskList();
        
        // Filter out first test task
        nameList.add("Buy");
        tasks.add(firstTestTask);
        tasks.add(secondTestTask);
        filteredResult.add(firstTestTask);
        assertNotEquals(filteredResult, FilterUtil.filterTaskByTags(tasks, nameList));
    }

    @Test
    public void testFilterCompletedTaskList_equals() {
        List<Task> tasks = getEmptyTaskList();
        // Check with empty tasks
        assertEquals(tasks, FilterUtil.filterTasksByStatus(tasks, true));
        tasks.add(firstTestTask);
        tasks.add(secondTestTask);
        
        // Filter out completed tasks
        List<Task> filteredTasks = getEmptyTaskList();
        filteredTasks.add(firstTestTask);
        assertEquals(filteredTasks, FilterUtil.filterTasksByStatus(tasks, true));
    }
    
    @Test
    public void testFilterCompletedTaskList_not_equals() {
        List<Task> tasks = getEmptyTaskList();
        List<Task> filteredResult = getEmptyTaskList();
        tasks.add(firstTestTask);
        tasks.add(secondTestTask);
        filteredResult.add(secondTestTask);
        
        // Filter out completed tasks
        assertNotEquals(filteredResult, FilterUtil.filterTasksByStatus(tasks, true));
    }

    @Test
    public void testFilterIncompletedTaskList_equals() {
        List<Task> tasks = getEmptyTaskList();
        // Check with empty tasks
        assertEquals(tasks, FilterUtil.filterTasksByStatus(tasks, false));
        tasks.add(firstTestTask);
        tasks.add(secondTestTask);
        
        // Filter out incomplete tasks
        List<Task> filteredTasks = getEmptyTaskList();
        filteredTasks.add(secondTestTask);
        assertEquals(filteredTasks, FilterUtil.filterTasksByStatus(tasks, false));
    }
    
    @Test
    public void testFilterIncompletedTaskList_not_equals() {
        List<Task> tasks = getEmptyTaskList();
        tasks.add(firstTestTask);
        tasks.add(secondTestTask);
        
        // Filter out incomplete tasks
        List<Task> filteredTasks = getEmptyTaskList();
        filteredTasks.add(firstTestTask);
        filteredTasks.add(secondTestTask);
        assertNotEquals(filteredTasks, FilterUtil.filterTasksByStatus(tasks, false));
    }
    
    @Test 
    public void testFilterTaskBySingleDate_with_empty_tasks_equals() {
        List<Task> tasks = getEmptyTaskList();
        assertEquals(tasks, FilterUtil.filterTaskBySingleDate(tasks, DateUtil.floorDate(TODAY)));
        assertEquals(tasks, FilterUtil.filterTaskBySingleDate(tasks, null));
    }

    @Test
    public void testFilterTaskBySingleDate_equals() {
        List<Task> tasks = getEmptyTaskList();
        tasks.add(firstTestTask);
        tasks.add(secondTestTask);
        
        // Filter out first task
        List<Task> filteredTasks = getEmptyTaskList();
        filteredTasks.add(firstTestTask);
        assertEquals(filteredTasks, FilterUtil.filterTaskBySingleDate(filteredTasks, DateUtil.floorDate(TODAY)));
    }
    
    @Test
    public void testFilterTaskBySingleDate_not_equals() {
        List<Task> tasks = getEmptyTaskList();
        tasks.add(firstTestTask);
        tasks.add(secondTestTask);
        
        List<Task> filteredTasks = getEmptyTaskList();
        filteredTasks.add(firstTestTask);
        filteredTasks.add(firstTestTask);
        assertNotEquals(filteredTasks, FilterUtil.filterTaskBySingleDate(tasks, TODAY));
    }
    
    @Test 
    public void testFilterTaskWithDateRange_with_empty_tasks_equals() {
        List<Task> tasks = getEmptyTaskList();
        assertEquals(tasks, FilterUtil.filterTaskWithDateRange(tasks, 
                LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(4)));
    }
    
    @Test
    public void testFilterTaskWithDateRange_out_of_range_equals() {
        List<Task> tasks = getEmptyTaskList();
        tasks.add(firstTestTask);
        // Filter out of range of test task
        assertEquals(getEmptyTaskList(), FilterUtil.filterTaskWithDateRange(tasks, 
                LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(4)));
    }

    @Test
    public void testFilterTaskWithDateRange_equals() {
        List<Task> tasks = getEmptyTaskList();
        tasks.add(firstTestTask);
        tasks.add(secondTestTask);
       
        // Filter out first task
        List<Task> filteredTasks = getEmptyTaskList();
        filteredTasks.add(firstTestTask);
        assertEquals(filteredTasks, FilterUtil.filterTaskWithDateRange(tasks, TODAY, TODAY));
    }
    
    @Test
    public void testFilterTaskWithDateRange_with_null_dates_equals() {
        List<Task> tasks = getEmptyTaskList();
        tasks.add(firstTestTask);
        tasks.add(secondTestTask);
        
        List<Task> filteredTasks = getEmptyTaskList();
        filteredTasks.add(firstTestTask);
        filteredTasks.add(secondTestTask);
        // Filter out both task
        assertEquals(filteredTasks, FilterUtil.filterTaskWithDateRange(tasks, null, TOMORROW));
        assertEquals(filteredTasks, FilterUtil.filterTaskWithDateRange(tasks, YESTERDAY, null));
    }
    
    /* ============================= Test cases for Event Filtering Methods ==================== */
    
    @Test
    public void testFilterOutEvent_equals() {
        List<CalendarItem> calendarItems = new ArrayList<CalendarItem>();
        calendarItems.addAll(overdueEvents);
        assertEquals(calendarItems, FilterUtil.filterOutEvent(calendarItems));
    }
    
    @Test
    public void testFilterOutEvent_not_equals() {
        List<CalendarItem> calendarItems = new ArrayList<CalendarItem>();
        calendarItems.add(firstTestTask);
        calendarItems.addAll(overdueEvents);
        assertNotEquals(calendarItems, FilterUtil.filterOutEvent(calendarItems));
    }
    
    @Test
    public void testFilterEventByNames_filter_with_empty_eventList_equals() {
        List<Event> events = getEmptyEventList();
        HashSet<String> nameList = new HashSet<String>();
        // Empty event list and name list
        assertEquals(events, FilterUtil.filterEventByNames(events, nameList));
        // Empty event list
        nameList.add("Nothing");
        assertEquals(events, FilterUtil.filterEventByNames(events, nameList));
    }
    
    @Test
    public void testFilterEventByNames_filter_equals() {
        List<Event> events = getEmptyEventList();
        HashSet<String> nameList = new HashSet<String>();
        List<Event> filteredResult = getEmptyEventList();
        
        // Filter out overdue events
        nameList.add("CS2103");
        nameList.add("Roadshow");
        events.addAll(overdueEvents);
        events.addAll(currentEvents);
        filteredResult.addAll(overdueEvents);
        assertEquals(filteredResult, FilterUtil.filterEventByNames(events, nameList));
    }
    
    @Test
    public void testFilterEventByNames_filter_not_equals() {
        List<Event> events = getEmptyEventList();
        HashSet<String> nameList = new HashSet<String>();
        events.addAll(overdueEvents);
        nameList.add("Nothing");
        assertNotEquals(events, FilterUtil.filterEventByNames(events, nameList));
    }
    
    @Test
    public void testFilterEventByTags_with_empty_event_list_equals() {
        List<Event> events = getEmptyEventList();
        HashSet<String> nameList = new HashSet<String>();
        // Empty event list and name list
        assertEquals(events, FilterUtil.filterEventByTags(events, nameList));
        // Empty event list
        nameList.add("Nothing");
        assertEquals(events, FilterUtil.filterEventByTags(events, nameList));
    }

    @Test
    public void testFilterEventByTags_equals() {
        List<Event> events = getEmptyEventList();
        HashSet<String> nameList = new HashSet<String>();
        List<Event> filteredResult = getEmptyEventList();
        
        // Filter out overdue events
        nameList.add("CS3216");
        nameList.add("CSIT");
        events.addAll(overdueEvents);
        events.addAll(currentEvents);
        filteredResult.addAll(overdueEvents);
        assertEquals(filteredResult, FilterUtil.filterEventByTags(events, nameList));
    }
    
    @Test
    public void testFilterEventByTags_not_equals() {
        List<Event> events = getEmptyEventList();
        HashSet<String> nameList = new HashSet<String>();
        List<Event> filteredResult = getEmptyEventList();

        // Filter out overdue events
        nameList.add("test");
        events.addAll(overdueEvents);
        filteredResult.addAll(overdueEvents);
        assertNotEquals(filteredResult, FilterUtil.filterEventByTags(events, nameList));
    }
    
    @Test 
    public void testFilterIsOverEventList_with_empty_event_list_equals() {
        List<Event> events = new ArrayList<Event>();
        // Filter with empty events list
        assertEquals(events, FilterUtil.filterEventsByStatus(events, true));
    }

    @Test
    public void testFilterIsOverEventList_equals() {
        List<Event> events = new ArrayList<Event>();
        List<Event> filteredResult = getEmptyEventList();
        
        // Filter out overdue events
        events.addAll(overdueEvents);
        events.addAll(currentEvents);
        filteredResult.addAll(overdueEvents);
        assertEquals(filteredResult, FilterUtil.filterEventsByStatus(events, true));
    }
    
    @Test
    public void testFilterIsOverEventList_not_equals() {
        List<Event> events = new ArrayList<Event>();
        List<Event> filteredResult = getEmptyEventList();
        events.addAll(overdueEvents);
        events.addAll(currentEvents);
        filteredResult.addAll(overdueEvents);
        filteredResult.addAll(currentEvents);
        assertNotEquals(filteredResult, FilterUtil.filterEventsByStatus(events, false));
    }
    
    @Test 
    public void testFilterCurrentEventList_with_empty_event_list_equals() {
        List<Event> events = new ArrayList<Event>();
        // Filter with empty events list
        assertEquals(events, FilterUtil.filterEventsByStatus(events, false));
    }

    @Test
    public void testFilterCurrentEventList_equals() {
        List<Event> events = new ArrayList<Event>();
        List<Event> filteredResult = getEmptyEventList();
        events.addAll(overdueEvents);
        events.addAll(currentEvents);
        filteredResult.addAll(currentEvents);
        assertEquals(filteredResult, FilterUtil.filterEventsByStatus(events, false));    
    }
    
    @Test
    public void testFilterCurrentEventList_not_equals() {
        List<Event> events = new ArrayList<Event>();
        List<Event> filteredResult = getEmptyEventList();
        events.addAll(overdueEvents);
        events.addAll(currentEvents);
        filteredResult.addAll(currentEvents);
        filteredResult.addAll(overdueEvents);
        assertNotEquals(filteredResult, FilterUtil.filterEventsByStatus(events, true));
    }

    @Test
    public void testFilterEventsBySingleDate_with_empty_event_list_equals() {
        List<Event> events = new ArrayList<Event>();
        // Events is empty
        assertEquals(events, FilterUtil.filterEventBySingleDate(events, TODAY));
    }
    
    @Test
    public void testFilterEventBySingleDate_equals() {
        List<Event> events = new ArrayList<Event>();
        List<Event> filteredResult = getEmptyEventList();
        // Filter out overdue events 
        events.addAll(overdueEvents);
        events.addAll(currentEvents);
        filteredResult.addAll(overdueEvents);
        assertEquals(filteredResult, FilterUtil.filterEventBySingleDate(events, DateUtil.floorDate(YESTERDAY)));
    }
    
    @Test
    public void testFilterEventBySingleDate_not_equals() {
        List<Event> events = new ArrayList<Event>();
        List<Event> filteredResult = getEmptyEventList();
        
        // Filter out current events 
        events.addAll(overdueEvents);
        events.addAll(currentEvents);
        filteredResult.addAll(currentEvents);
        assertNotEquals(filteredResult, FilterUtil.filterEventBySingleDate(events, DateUtil.floorDate(YESTERDAY)));
    }

    @Test
    public void testFilterEventWithDateRange_empty_event_list_equals() {
        List<Event> events = new ArrayList<Event>();
        // Events is empty
        assertEquals(events, FilterUtil.filterEventWithDateRange(events, YESTERDAY, YESTERDAY));
    }
    
    @Test
    public void testFilterEventWithDateRange_equals() {
        List<Event> events = new ArrayList<Event>();
        List<Event> filteredResult = new ArrayList<Event>();
        // Filter out overdue events 
        events.addAll(overdueEvents);
        events.addAll(currentEvents);
        filteredResult.addAll(overdueEvents);
        assertEquals(filteredResult, 
                FilterUtil.filterEventWithDateRange(events, DateUtil.floorDate(YESTERDAY), DateUtil.ceilDate(YESTERDAY)));
    }
    
    @Test
    public void testFilterEventWithDateRange_null_dates_equals() {
        List<Event> events = new ArrayList<Event>();
        List<Event> filteredResult = new ArrayList<Event>();
        // Filter out both overdue and current events
        events.addAll(overdueEvents);
        events.addAll(currentEvents);
        filteredResult.addAll(overdueEvents);
        filteredResult.addAll(currentEvents);
        assertEquals(events, FilterUtil.filterEventWithDateRange(events, null, DateUtil.ceilDate(TOMORROW)));
        assertEquals(events, FilterUtil.filterEventWithDateRange(events, DateUtil.floorDate(YESTERDAY), null));
        assertEquals(events, FilterUtil.filterEventWithDateRange(events, null, null));
    }
    
    @Test
    public void testFilterEventWithDateRange_not_equals() {
        List<Event> events = new ArrayList<Event>();
        List<Event> filteredResult = new ArrayList<Event>();
        // Filter out overdue events 
        events.addAll(currentEvents);
        events.addAll(overdueEvents);
        filteredResult.addAll(currentEvents);
        assertNotEquals(filteredResult, 
                FilterUtil.filterEventWithDateRange(events, DateUtil.floorDate(YESTERDAY), DateUtil.ceilDate(YESTERDAY)));
    }
    
    /* ===================  Helper methods to be use to generate Task and Event for testing =============== */
    
    private Task getFirstTestTask() {
        Task task = new Task();
        task.setName("Buy Milk");
        task.setCalendarDateTime(TODAY);
        task.addTag("personal");
        task.setCompleted();
        return task;
    }
    
    private Task getSecondTestTask() {
        Task task = new Task();
        task.setName("CS2103");
        task.setCalendarDateTime(TOMORROW);
        task.addTag("CS2103");
        return task;
    }
    
    private List<Task> getEmptyTaskList() {
        return new ArrayList<Task>();
    }
    
    private List<Event> getEmptyEventList() {
        return new ArrayList<Event>();
    }
    
    private List<Event> getOverdueEvents() {
        List<Event> events = new ArrayList<Event>();
        Event event = new Event();
        event.setStartDate(YESTERDAY);
        event.setEndDate(YESTERDAY);
        event.setName("CS2103 V0.5");
        event.addTag("CS2103");
        events.add(event);
        event.removeTag("CS2103");
        event.setName("CSIT roadshow");
        event.addTag("CSIT");
        events.add(event);
        return events;
    }
    
    private List<Event> getCurrentEvents() {
        List<Event> events = new ArrayList<Event>();
        Event event = new Event();
        event.setStartDate(TODAY);
        event.setEndDate(TOMORROW);
        event.setName("CS3216 9th Steps");
        event.addTag("CS3216");
        events.add(event);
        event.removeTag("CS3216");
        event.setName("CS3217 9th Steps");
        event.addTag("CS3217");
        events.add(event);
        return events;
    }
}