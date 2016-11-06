package seedu.todo.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import seedu.todo.commons.exceptions.InvalidNaturalDateException;
import seedu.todo.commons.exceptions.ParseException;
import seedu.todo.commons.util.DateUtil;
import seedu.todo.commons.util.FilterUtil;
import seedu.todo.commons.util.ParseUtil;
import seedu.todo.commons.util.StringUtil;
import seedu.todo.controllers.concerns.DateParser;
import seedu.todo.controllers.concerns.Renderer;
import seedu.todo.controllers.concerns.Tokenizer;
import seedu.todo.models.CalendarItem;
import seedu.todo.models.Event;
import seedu.todo.models.Task;
import seedu.todo.models.TodoListDB;

/**
 *  @@author A0139922Y
 * Controller to clear task/event by Type
 */
public class ClearController implements Controller {
    
    private static final String NAME = "Clear";
    private static final String DESCRIPTION = "Clear all tasks/events or by specify date.";
    private static final String COMMAND_WORD = "clear";
    
    // Syntax correction to console input
    public static final String COMMAND_SYNTAX = "clear \"task/event\" on \"date\"";
    public static final String CLEAR_DATE_SYNTAX = "clear \"date\" [or from \"date\" to \"date\"]";
    
    // Message output to console text area
    public static final String MESSAGE_CLEAR_SUCCESS_FORMAT = "A total of %s deleted!";
    public static final String MESSAGE_CLEAR_NO_ITEM_FOUND = "No item found!";
    public static final String MESSAGE_CLEAR_ALL_SUCCESS = "All tasks and events have been deleted!\n" + "To undo, type \"undo\".";
    public static final String MESSAGE_CLEAR_UNABLE_TO_SUPPORT = "Unable to clear!\nCannot clear by status!";
    public static final String MESSAGE_DATE_CONFLICT = "Unable to clear!\nMore than 1 date criteria is provided!";
    public static final String MESSAGE_NO_DATE_DETECTED = "Unable to clear!\nThe natural date entered is not supported.";
    public static final String MESSAGE_ITEM_TYPE_CONFLICT = "Unable to clear!\nMore than 1 item type is provided!";
    
    // Use to access parsing of dates
    private static final int NUM_OF_DATES_FOUND_INDEX = 0;
    private static final int COMMAND_INPUT_INDEX = 0;
    private static final int DATE_CRITERIA_INDEX = 0;
    private static final int DATE_ON_INDEX = 1;
    private static final int DATE_FROM_INDEX = 2;
    private static final int DATE_TO_INDEX = 3;

    private static CommandDefinition commandDefinition =
            new CommandDefinition(NAME, DESCRIPTION, COMMAND_SYNTAX); 

    public static CommandDefinition getCommandDefinition() {
        return commandDefinition;
    }

    @Override
    public float inputConfidence(String input) {
        return (StringUtil.splitStringBySpace(input.toLowerCase())[COMMAND_INPUT_INDEX]).equals(COMMAND_WORD) ? 1 : 0;
    }
    
    /**
     * Get the token definitions for use with <code>tokenizer</code>.<br>
     * This method exists primarily because Java does not support HashMap
     * literals...
     * 
     * @return tokenDefinitions
     */
    private static Map<String, String[]> getTokenDefinitions() {
        Map<String, String[]> tokenDefinitions = new HashMap<String, String[]>();
        tokenDefinitions.put(Tokenizer.DEFAULT_TOKEN, new String[] { COMMAND_WORD });
        tokenDefinitions.put(Tokenizer.EVENT_TYPE_TOKEN, Tokenizer.EVENT_TYPE_DEFINITION);
        tokenDefinitions.put(Tokenizer.TIME_TOKEN, Tokenizer.TIME_DEFINITION);
        tokenDefinitions.put(Tokenizer.TASK_STATUS_TOKEN, Tokenizer.TASK_STATUS_DEFINITION);
        tokenDefinitions.put(Tokenizer.EVENT_STATUS_TOKEN, Tokenizer.EVENT_STATUS_DEFINITION);
        tokenDefinitions.put(Tokenizer.TIME_FROM_TOKEN, Tokenizer.TIME_FROM_DEFINITION);
        tokenDefinitions.put(Tokenizer.TIME_TO_TOKEN, Tokenizer.TIME_TO_DEFINITION);
        return tokenDefinitions;
    }
    
    @Override
    public void process(String input) throws ParseException {
        Map<String, String[]> parsedResult;
        parsedResult = Tokenizer.tokenize(getTokenDefinitions(), input);
        TodoListDB db = TodoListDB.getInstance();
        
        if (input.trim().equals(COMMAND_WORD)) {
            db.destroyAllTaskAndEvents();
            Renderer.renderIndex(db, MESSAGE_CLEAR_ALL_SUCCESS);
            return; // Clear all
        }
        
        boolean isItemTypeProvided = !ParseUtil.isTokenNull(parsedResult, Tokenizer.EVENT_TYPE_TOKEN);
        boolean isTaskStatusProvided = !ParseUtil.isTokenNull(parsedResult, Tokenizer.TASK_STATUS_TOKEN);
        boolean isEventStatusProvided = !ParseUtil.isTokenNull(parsedResult, Tokenizer.EVENT_STATUS_TOKEN);
        
        if (isErrorCommand(isTaskStatusProvided, isEventStatusProvided, input)) {
            return; // Break out if found error
        }
        
        boolean isTask = true; //default
        if (isItemTypeProvided) {
            isTask = ParseUtil.doesTokenContainKeyword(parsedResult, Tokenizer.EVENT_TYPE_TOKEN, "task");
        }
        
        LocalDateTime [] validDates = parsingDates(parsedResult);
        if (validDates == null) {
            return; // Break out when date conflict found
        }
        
        // Setting up view
        List<Task> tasks; //default
        List<Event> events; //default
        List<CalendarItem> calendarItems;
        // Filter Task and Event by Type
        if (!isItemTypeProvided) {
            tasks = db.getAllTasks();
            events = db.getAllEvents();
        } else {
            if (isTask) {
                events = new ArrayList<Event>();
                tasks = db.getAllTasks();
            } else {
                tasks = new ArrayList<Task>();
                events = db.getAllEvents();
            }
        }
        
        // Filter Task and Event by date
        calendarItems = filterTasksAndEventsByDate(tasks, events, parsedResult);
        if (calendarItems == null) {
            return; // Date conflict detected
        }
        tasks = FilterUtil.filterOutTask(calendarItems);
        events = FilterUtil.filterOutEvent(calendarItems);
        
        // Show message if no items had been found
        if (tasks.size() == 0 && events.size() == 0) {
            Renderer.renderIndex(db, MESSAGE_CLEAR_NO_ITEM_FOUND);
            return;
        }
        
        deleteSelectedTasksAndEvents(tasks, events, db);
    }
    
    /*====================== Helper Methods to check for Error/Syntax Command ===================*/
    
    /*
     * To be used to parsed dates and check for any dates conflict
     * 
     * @return null if dates conflict detected, else return { dateCriteria, dateOn, dateFrom, dateTo }
     */
    private LocalDateTime[] parsingDates(Map<String, String[]> parsedResult) {
        String[] parsedDates = ParseUtil.parseDates(parsedResult);
        //date enter with COMMAND_WORD e.g list today
        String date = ParseUtil.getTokenResult(parsedResult, Tokenizer.DEFAULT_TOKEN);
        
        if (date != null && parsedDates != null) {
            Renderer.renderDisambiguation(CLEAR_DATE_SYNTAX, MESSAGE_DATE_CONFLICT);
            return null;
        }
        
        LocalDateTime dateCriteria = null;
        LocalDateTime dateOn = null;
        LocalDateTime dateFrom = null;
        LocalDateTime dateTo = null;
        
        if (date != null) {
            try {
                dateCriteria = DateParser.parseNatural(date);
            } catch (InvalidNaturalDateException e) {
                Renderer.renderDisambiguation(CLEAR_DATE_SYNTAX, MESSAGE_NO_DATE_DETECTED);
                return null;
            }
        }
        
        if (parsedDates != null) {
            String naturalOn = parsedDates[DATE_ON_INDEX];
            String naturalFrom = parsedDates[DATE_FROM_INDEX];
            String naturalTo = parsedDates[DATE_TO_INDEX];
            if (naturalOn != null && Integer.parseInt(parsedDates[NUM_OF_DATES_FOUND_INDEX]) > 1) {
                //date conflict detected
                Renderer.renderDisambiguation(CLEAR_DATE_SYNTAX, MESSAGE_DATE_CONFLICT);
                return null;
            }
            // Parse natural date using Natty.
            try {
                dateOn = naturalOn == null ? null : DateUtil.floorDate(DateParser.parseNatural(naturalOn)); 
                dateFrom = naturalFrom == null ? null : DateUtil.floorDate(DateParser.parseNatural(naturalFrom)); 
                dateTo = naturalTo == null ? null : DateUtil.floorDate(DateParser.parseNatural(naturalTo));
            } catch (InvalidNaturalDateException e) {
                    Renderer.renderDisambiguation(CLEAR_DATE_SYNTAX, MESSAGE_NO_DATE_DETECTED);
                    return null;
            }
        }
        return new LocalDateTime[] { dateCriteria, dateOn, dateFrom, dateTo };
    }
    
    /*
     * To be use to check if there are any command syntax error
     * 
     * @return true, if there is error in command syntax, false if syntax is allowed
     */
    private boolean isErrorCommand(boolean isTaskStatusProvided, boolean isEventStatusProvided, String input) {
        // Check if any status is provided
        if (isTaskStatusProvided || isEventStatusProvided) {
            Renderer.renderDisambiguation(COMMAND_SYNTAX, MESSAGE_CLEAR_UNABLE_TO_SUPPORT);
            return true;
        }
        // Check if more than 1 item type is provided
        if (FilterUtil.isItemTypeConflict(input)) {
            Renderer.renderDisambiguation(COMMAND_SYNTAX, MESSAGE_ITEM_TYPE_CONFLICT);
            return true;
        }
        return false;
    }
    
    /* =================== Helper methods to filter out Task and Events ==================*/
    
    /*
     * Filter out the selected tasks and events based on the dates
     * and update tasks and events accordingly
     * 
     * @param tasks
     *            List of Task items
     * @param events           
     *            List of Event items
     * @param parsedResult
     *            parsedResult by Tokenizer
     * @return        
     *            tasks and events in a list form by date or null when date conflict found
     */
    private List<CalendarItem> filterTasksAndEventsByDate(List<Task> tasks, List<Event> events, Map<String, 
            String[]> parsedResult) {
        // Get dates from input
        List<CalendarItem> calendarItems = new ArrayList<CalendarItem>();
        LocalDateTime [] validDates = parsingDates(parsedResult);
        List<Task> filteredTasks = tasks;
        List<Event> filteredEvents = events;
        if (validDates == null) {
            return null; // Break out when date conflict found
        }
        
        // Set dates that are found, if not found value will be null
        LocalDateTime dateCriteria = validDates[DATE_CRITERIA_INDEX];
        LocalDateTime dateOn = validDates[DATE_ON_INDEX];
        LocalDateTime dateFrom = validDates[DATE_FROM_INDEX];
        LocalDateTime dateTo = validDates[DATE_TO_INDEX];
        
        if (dateCriteria != null) {
            // Filter by single date
            assert dateOn == null;
            assert dateFrom == null;
            assert dateTo == null;
            filteredTasks = FilterUtil.filterTaskBySingleDate(tasks, dateCriteria);
            filteredEvents = FilterUtil.filterEventBySingleDate(events, dateCriteria);
        }
        
        if (dateOn != null) {
            // Filter by single date
            filteredTasks = FilterUtil.filterTaskBySingleDate(tasks, dateOn);
            filteredEvents = FilterUtil.filterEventBySingleDate(events, dateOn);
        } else if (dateFrom != null || dateTo != null) {
            // Filter by range
            filteredTasks = FilterUtil.filterTaskWithDateRange(tasks, dateFrom, dateTo);
            filteredEvents =FilterUtil.filterEventWithDateRange(events, dateFrom, dateTo);
        }
        
        calendarItems.addAll(filteredTasks);
        calendarItems.addAll(filteredEvents);
        return calendarItems;
    }    
    
    /* =============== Helper Methods to delete selected Tasks and Events ============*/
    
    /*
     * Delete the selected Tasks and Events , filtered out by helper methods
     * 
     * @param tasks
     *             A list of Task that already been filtered for deletion
     * @param events
     *             A list of Event that already been filtered for deletion
     * @param db
     *             The same instance of db used to filtered out both tasks and events                        
     */
    private void deleteSelectedTasksAndEvents(List<Task> tasks, List<Event> events, TodoListDB db) {
        db.destroyAllTaskAndEventsByList(tasks, events);
        String consoleMessage = String.format(MESSAGE_CLEAR_SUCCESS_FORMAT, 
                StringUtil.displayNumberOfTaskAndEventFoundWithPuralizer(tasks.size(), events.size()));
        Renderer.renderIndex(db, consoleMessage);
    }
}
