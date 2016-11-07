package seedu.todo.commons.util;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import seedu.todo.models.CalendarItem;
import seedu.todo.models.Event;
import seedu.todo.models.Task;

//@@author A0139922Y
/**
 * Helper function to help in filtering results
 */

public class FilterUtil {
    
    /*==================== Filtering Methods for Tasks ======================*/
    
    /*
     * Use to filter out Task items from calendarItems
     * 
     * @param calendarItems
     *              List of mixture of Task and Event
     * @return filteredTasks
     *              List containing only Task             
     */
    public static List<Task> filterOutTask(List<CalendarItem> calendarItems) {
        List<Task> filteredTasks = new ArrayList<Task>();
        for (int i = 0; i < calendarItems.size(); i ++) {
            if (calendarItems.get(i) instanceof Task) {
                filteredTasks.add((Task) calendarItems.get(i));
            }
        }
        return filteredTasks;
    }
    
    /**
     * Filter the task list based on matching task name list
     * @param tasks 
     *             Provided list for filtering
     * @param namelist
     *             Search and filter based on the name list
     * @return filteredTasks
     *              List containing only Task that filtered by the item name
     */
    public static List<Task> filterTaskByNames(List<Task> tasks, HashSet<String> nameList) {
        // If search list size is 0 , user not searching based on name
        List<Task> filteredTasks = new ArrayList<Task>();
        if (nameList.size() == 0) {
            return filteredTasks;
        }
        
        // Loop through all the tasks
        for (int i = 0; i < tasks.size(); i ++) {
            Task task = tasks.get(i);
            
            // For every task, loop through all the name list
            Iterator<String> nameListIterator = nameList.iterator();
            while (nameListIterator.hasNext()) {
                String matchingName = nameListIterator.next();
                if (matchWithFullName(task, matchingName) || matchWithSubName(task, matchingName)) {
                    filteredTasks.add(task); // Once found, add and break 
                    break;
                }
            }
            // Reset the name list for other task
            nameListIterator = nameList.iterator();
        }
        return filteredTasks;
    }
    
    /**
     * Filter the task list based on matching tag name list
     * @param tasks 
     *             Provided list for filtering
     * @param namelist
     *             Search and filter based on the name list
     * @return filteredTasks
     *              List containing only Task that filtered by the tag names 
     */
    public static List<Task> filterTaskByTags(List<Task> tasks, HashSet<String> nameList) {
        // If no search list is provided, user not searching based on tags
        List<Task> filteredTasks = new ArrayList<Task>();
        if (nameList.size() == 0) {
            return filteredTasks;
        }
        
        // Lopp through all the tasks
        for (int i = 0; i < tasks.size(); i ++) {
            // Get the task tag list
            Task task = tasks.get(i);
            ArrayList<String> taskTagList = task.getTagList();
            
            // Loop through the tag names list
            Iterator<String> nameListIterator = nameList.iterator();
            while (nameListIterator.hasNext()) {
                String currentMatchingName = nameListIterator.next();
                if (taskTagList.contains(currentMatchingName)) {
                    filteredTasks.add(task); // Once found a matching tag, add and break;
                    break;
                }
            }
            nameListIterator = nameList.iterator();
        }
        return filteredTasks;
    }
    
    /**
     * Filter the task list based on incomplete status
     * @param tasks 
     *             Provided list for filtering
     * @param taskStatus
     *             True if searching for is completed, false if search for incomplete.
     * @return filteredTasks
     *             List containing only Task that filtered by status (e.g. complete or incomplete)
     */
    public static List<Task> filterTasksByStatus(List<Task> tasks, boolean taskStatus) {
        // If tasks is empty, return immediately
        if (tasks.size() == 0) {
            return tasks;
        }
        
        List<Task> filteredTasks = new ArrayList<Task>();
        // Loop through all the tasks
        for (int i = 0; i < tasks.size(); i ++) {
            Task task = tasks.get(i);
            // Check if it is the same as the required task status
            if (task.isCompleted() == taskStatus) {
                filteredTasks.add(task); //Add any task has the same task status
            }
        }
        return filteredTasks;
    }
    
    /**
     * Filter the task list based on single date
     * @param tasks 
     *             Provided list for filtering
     * @param date            
     *             Search based on this date
     * @return filteredTasks
     *              List containing only Task that filtered by a single date
     */
    public static List<Task> filterTaskBySingleDate(List<Task> tasks, LocalDateTime date) {
        // If tasks is empty, return immediately
        if (tasks.size() == 0) {
            return tasks;
        }
        
        ArrayList<Task> filteredTasks = new ArrayList<Task>();
        // Loop through all the tasks
        Iterator<Task> iterator = tasks.iterator();
        while (iterator.hasNext()) {
            Task task = iterator.next();
            // Searched date should not be null, break out if it is.
            assert date != null; 
            // Check if task start date is the same as the searched date
            date = DateUtil.floorDate(date);
            LocalDateTime taskDate = DateUtil.floorDate(task.getCalendarDateTime());
            
            // May have floating tasks, skip floating tasks 
            if (taskDate != null && taskDate.equals(date)) {
                filteredTasks.add(task);
            }
        }
        return filteredTasks;
    }
    
    /**
     * Filter the task list with a date range
     * @param tasks 
     *             Provided list for filtering
     * @param startDate            
     *             Search based on this as starting date
     * @param endDate            
     *             Search based on this as ending date
     * @return filteredTasks
     *              List containing only Task that filtered by a range of two dates
     */
    public static List<Task> filterTaskWithDateRange(List<Task> tasks, LocalDateTime startDate, LocalDateTime endDate) {
        // If tasks is empty, return immediately
        if (tasks.size() == 0) {
            return tasks;
        }
    
        // If start date is null, set it to MIN dateTime, user could searched by "from today"
        if (startDate == null) {
            startDate = LocalDateTime.MIN;
        }
        
        // If end date is null, set it to MAX dateTime, user could searched by "to today"
        if (endDate == null) {
            endDate = LocalDateTime.MAX;
        }
        
        ArrayList<Task> filteredTasks = new ArrayList<Task>();
        // Loop through all the tasks
        Iterator<Task> iterator = tasks.iterator();
        while (iterator.hasNext()) {
            Task task = iterator.next();
            LocalDateTime taskDate = DateUtil.floorDate(task.getDueDate());
            // May have floating task, set the date to MIN dateTime, to avoid been filtered 
            if (taskDate == null) {
                taskDate = LocalDateTime.MIN;
            }
            
            // Set the searched date to its min and max value
            startDate = DateUtil.floorDate(startDate);
            endDate = DateUtil.ceilDate(endDate);
            // Compare if the task date is within the range of start and end date
            if (taskDate.compareTo(startDate) >= 0 && taskDate.compareTo(endDate) <= 0) {
                filteredTasks.add(task); // Add if it is within the range
            }
        }
        return filteredTasks;
    }
    
    /*==================== Filtering Methods for Events ======================*/
    
    /*
     * Use to filter out Event items from calendarItems
     * 
     * @param calendarItems
     *              List of mixture of Task and Event
     * @return filteredTasks
     *              List containing only Event             
     */
    public static List<Event> filterOutEvent(List<CalendarItem> calendarItems) {
        List<Event> filteredEvents = new ArrayList<Event>();
        for (int i = 0; i < calendarItems.size(); i ++) {
            if (calendarItems.get(i) instanceof Event) {
                filteredEvents.add((Event) calendarItems.get(i));
            }
        }
        return filteredEvents;
    }
    
    /**
     * Filter the event list based on event name list
     * @param events 
     *             Provided list for filtering
     * @param namelist
     *             Search and filter based on the name list
     * @return filteredEvents
     *              List containing only Event that event name matches with the item name
     */
    public static List<Event> filterEventByNames(List<Event> events, HashSet<String> nameList) {
        List<Event> filteredEvents = new ArrayList<Event>();
        // If name list size is 0, means not searching by name list 
        if (nameList.size() == 0) {
            return filteredEvents;
        }
        
        // Lopp through all the events
        for (int i = 0; i < events.size(); i ++) {
            Event event = events.get(i);
            // Loop through all the name list
            Iterator<String> nameListIterator = nameList.iterator();
            while (nameListIterator.hasNext()) {
                String matchingName = nameListIterator.next().toLowerCase();
                // If found a match with its full name or sub names, break 
                if (matchWithFullName(event, matchingName) || matchWithSubName(event, matchingName)) {
                    filteredEvents.add(event);
                    break;
                }
            }
            // Reset the nameList for the next event
            nameListIterator = nameList.iterator();
        }
        return filteredEvents;
    }
    
    /**
     * Filter the event list based on tag name list
     * @param events 
     *             Provided list for filtering
     * @param namelist
     *             Search and filter based on the name list
     * @return filteredEvents
     *              List containing only Event that tag names matches with the tag name
     */
    public static List<Event> filterEventByTags(List<Event> events, HashSet<String> nameList) {
        List<Event> filteredEvents = new ArrayList<Event>();
        // If name list size is 0, means not searching by tags 
        if (nameList.size() == 0) {
            return filteredEvents;
        }
        
        // Loop through all the events
        for (int i = 0; i < events.size(); i ++) {
            Event event = events.get(i);
            // Get the tag list of the event
            ArrayList<String> taskTagList = event.getTagList();
            Iterator<String> nameListIterator = nameList.iterator();
            // Loop through the tag names list
            while (nameListIterator.hasNext()) {
                String currentMatchingName = nameListIterator.next();
                // If found a match, add and break
                if (taskTagList.contains(currentMatchingName)) {
                    filteredEvents.add(event);
                    break;
                }
            }
            // Reset the tag names list for the next event
            nameListIterator = nameList.iterator();
        }
        return filteredEvents;
    }
    
    /**
     * Filter the event list if the event date is over
     * @param events 
     *             Provided list for filtering
     * @param isEventOver
     *             True if searching for event that are over, false if searching for current event  
     * @return filteredEvents
     *              List containing only Event that status matches with the status, For e.g. over or current
     */            
    public static List<Event> filterEventsByStatus(List<Event> events, boolean eventStatus) {
        // If events is empty, return immediately
        if (events.size() == 0) {
            return events;
        }
        
        List<Event> filteredEvents = new ArrayList<Event>();
        // Loop through the events
        for (int i = 0; i < events.size(); i ++) {
            Event event = events.get(i);
            // Add into filteredEvents, once the status of the event matches
            if (event.isOver() == eventStatus) {
                filteredEvents.add(event);
            }
        }
        return filteredEvents;
    }
    
    /**
     * Filter the event list based on single date
     * @param events 
     *             Provided list for filtering
     * @param date            
     *             Search based on this date
     * @return filteredEvents
     *              List containing only Event date that matches with searched date
     */
    public static List<Event> filterEventBySingleDate(List<Event> events, LocalDateTime date) {
        // If events is empty, return immediately
        if (events.size() == 0) {
            return events;
        }
        
        ArrayList<Event> filteredEvents = new ArrayList<Event>();
        // Loop through all the events
        Iterator<Event> iterator = events.iterator();
        while (iterator.hasNext()) {
            Event event = iterator.next();
            // Search dates cannot be null
            assert date != null;
            date = DateUtil.floorDate(date);
            
            // Event start date should not be null
            assert event.getStartDate() != null; 
            LocalDateTime eventDate = DateUtil.floorDate(event.getStartDate());
            
            // Check against start date, if equals add it in
            if (eventDate.equals(date)) {
                filteredEvents.add(event);
            }
        }
        return filteredEvents;
    }
    
    /**
     * Filter the event list with range of dates
     * @param events 
     *             Provided list for filtering
     * @param startDate            
     *             Search based on this as starting date
     * @param endDate            
     *             Search based on this as ending date
     * @return filteredEvents
     *              List containing only Event that date fall between startDate and endDate
     */
    public static List<Event> filterEventWithDateRange(List<Event> events, LocalDateTime startDate, LocalDateTime endDate) {
        // If events is empty, return immediately
        if (events.size() == 0) {
            return events;
        }
    
        // Set startDate to MIN, user could search "from Today"
        if (startDate == null) {
            startDate = LocalDateTime.MIN;
        }
        
        // Set endDate to MaX, user could search "to Today"
        if (endDate == null) {
            endDate = LocalDateTime.MAX;
        }
        
        ArrayList<Event> filteredEvents = new ArrayList<Event>();
        Iterator<Event> iterator = events.iterator();
        // Loop through all the events
        while (iterator.hasNext()) {
            Event event = iterator.next();
            // Event dates should not be null
            assert event.getStartDate() != null;
            assert event.getEndDate() != null;
            LocalDateTime eventStartDate = DateUtil.floorDate(event.getStartDate());
            LocalDateTime eventEndDate = DateUtil.floorDate(event.getEndDate());
            
            // Check if the event start dates and end dates is within the search dates
            startDate = DateUtil.floorDate(startDate);
            endDate = DateUtil.ceilDate(endDate);
            if (eventStartDate.compareTo(startDate) >= 0 && eventEndDate.compareTo(endDate) <= 0) {
                filteredEvents.add(event);
            }
        }
        return filteredEvents;
    }
    
    /*==================== Helper Methods to Check Command Conflict ===================================*/
    
    /*
     * To be use to check if there are more than 1 event type entered by user 
     * 
     * @return true if more than 1 event type found, false, if only 1 or 0 event type found
     */
    public static boolean isItemTypeConflict(String input) {
        return input.contains("task") && input.contains("event");
    }
    
    /*==================== Helper Methods for filtering CalendarItem name ======================*/
    
    /*
     * Use to check if calendarItem name starts with the matching name 
     * 
     * @return true if it calendarItem's name starts with the matching name, 
     * false if calendarItem's name does not starts with matching name
     */
    private static boolean matchWithFullName(CalendarItem calendarItem, String matchingName) {
        String taskName = calendarItem.getName().toLowerCase();
        return taskName.startsWith(matchingName.toLowerCase());
    }
    
    /*
     * Use to check if calendarItem name split by space starts with the matching name 
     * 
     * @return true if any of the calendarItem's name that is split by space that starts with the matching name, 
     * false if calendarItem's name that is split by space does not starts with matching name
     */
    private static boolean matchWithSubName(CalendarItem calendarItem, String matchingName) {
        String[] nameBySpace = StringUtil.splitStringBySpace(calendarItem.getName());
        for (int i = 0; i < nameBySpace.length; i ++) {
            if (nameBySpace[i].toLowerCase().startsWith(matchingName.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
    
}
