package seedu.todo.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import seedu.todo.commons.exceptions.ParseException;
import seedu.todo.commons.util.DateUtil;
import seedu.todo.commons.util.FilterUtil;
import seedu.todo.commons.util.ParseUtil;
import seedu.todo.commons.util.StringUtil;
import seedu.todo.controllers.concerns.Tokenizer;
import seedu.todo.controllers.concerns.Renderer;
import seedu.todo.models.Event;
import seedu.todo.models.Task;
import seedu.todo.models.TodoListDB;

/**
 * @@author A0139922Y
 * Controller to list CalendarItems.
 *
 */
public class ListController implements Controller {
    
    private static final String NAME = "List";
    private static final String DESCRIPTION = "List all tasks and events by type or status.";
    private static final String COMMAND_SYNTAX = "list \"task complete/incomplete\" or \"event over/ongoing\""
            + "[on date] or [from date to date]";
    private static final String COMMAND_WORD = "list";
    private static final String LIST_TASK_SYNTAX = "list task \"complete/incomplete\"";
    private static final String LIST_EVENT_SYNTAX = "list event \"over/current\"";
    private static final String LIST_DATE_SYNTAX = "list \"date\" [or from \"date\" to \"date\"]";
    
    private static final String MESSAGE_RESULT_FOUND = "A total of %s found!";
    private static final String MESSAGE_NO_RESULT_FOUND = "No task or event found!";
    private static final String MESSAGE_LIST_SUCCESS = "Listing Today's, incompleted tasks and ongoing events";
    private static final String MESSAGE_INVALID_TASK_STATUS = "Unable to list!\nTry listing with complete or incomplete";
    private static final String MESSAGE_INVALID_EVENT_STATUS = "Unable to list!\nTry listing with over or current";
    private static final String MESSAGE_DATE_CONFLICT = "Unable to list!\nMore than 1 date criteria is provided!";
    private static final String MESSAGE_NO_DATE_DETECTED = "Unable to list!\nThe natural date entered is not supported.";
    private static final String MESSAGE_ITEM_TYPE_CONFLICT = "Unable to list!\nMore than 1 item type is provided!";
    
    //use to access parsing of dates
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
            Renderer.renderIndex(db, MESSAGE_LIST_SUCCESS);
            return;
        }
        
        boolean isItemTypeProvided = !ParseUtil.isTokenNull(parsedResult, Tokenizer.EVENT_TYPE_TOKEN);
        boolean isTaskStatusProvided = !ParseUtil.isTokenNull(parsedResult, Tokenizer.TASK_STATUS_TOKEN);
        boolean isEventStatusProvided = !ParseUtil.isTokenNull(parsedResult, Tokenizer.EVENT_STATUS_TOKEN);
        
        boolean isTask = true; //default
        
        if (isItemTypeProvided) {
            isTask = ParseUtil.doesTokenContainKeyword(parsedResult, Tokenizer.EVENT_TYPE_TOKEN, "task");
        }
        
        if (isErrorCommand(isTaskStatusProvided, isEventStatusProvided, isTask, input)) {
            return; // Break out if found error
        }
        
        boolean isCompleted = false; //default 
        boolean isOver = false; //default
        if (isTaskStatusProvided) {
            isCompleted = !ParseUtil.doesTokenContainKeyword(parsedResult, Tokenizer.TASK_STATUS_TOKEN, "incomplete");
        }
        if (isEventStatusProvided) {
            isOver = ParseUtil.doesTokenContainKeyword(parsedResult, Tokenizer.EVENT_STATUS_TOKEN, "over");
        }
        
        String[] parsedDates = ParseUtil.parseDates(parsedResult);
        if (parsedDates != null && isEventStatusProvided) {
            //detect date conflict
            Renderer.renderDisambiguation(LIST_DATE_SYNTAX, MESSAGE_DATE_CONFLICT);
            return;
        }
        
        LocalDateTime [] validDates = parsingDates(parsedResult, parsedDates);
        if (validDates == null) {
            return; // Break out when date conflict found
        }
        
        LocalDateTime dateCriteria = validDates[DATE_CRITERIA_INDEX];
        LocalDateTime dateOn = validDates[DATE_ON_INDEX];
        LocalDateTime dateFrom = validDates[DATE_FROM_INDEX];
        LocalDateTime dateTo = validDates[DATE_TO_INDEX];
        
        //Setting up views
        filterTasksAndEvents(db, isItemTypeProvided, isTaskStatusProvided, isEventStatusProvided, isTask, isCompleted,
                isOver, dateCriteria, dateOn, dateFrom, dateTo);
    }
    
    /*
     * To be used to parsed dates and check for any dates conflict
     * 
     * @return null if dates conflict detected, else return { dateCriteria, dateOn, dateFrom, dateTo }
     */
    private LocalDateTime[] parsingDates(Map<String, String[]> parsedResult, String[] parsedDates) {
        
        //date enter with COMMAND_WORD e.g list today
        String date = ParseUtil.getTokenResult(parsedResult, "default");
        if (date != null && parsedDates != null) {
            Renderer.renderDisambiguation(LIST_DATE_SYNTAX, MESSAGE_DATE_CONFLICT);
            return null;
        }

        LocalDateTime dateCriteria = null;
        LocalDateTime dateOn = null;
        LocalDateTime dateFrom = null;
        LocalDateTime dateTo = null;
        
        if (date != null) {
            dateCriteria = DateUtil.floorDate(DateUtil.parseNatural(date));
            if (dateCriteria == null) {
                Renderer.renderDisambiguation(LIST_DATE_SYNTAX, MESSAGE_NO_DATE_DETECTED);
                return null;
            } 
        }
        
        if (parsedDates != null) {
            String naturalOn = parsedDates[DATE_ON_INDEX];
            String naturalFrom = parsedDates[DATE_FROM_INDEX];
            String naturalTo = parsedDates[DATE_TO_INDEX];
            
            if (naturalOn != null && Integer.parseInt(parsedDates[NUM_OF_DATES_FOUND_INDEX]) > 1) {
                //date conflict detected
                Renderer.renderDisambiguation(LIST_DATE_SYNTAX, MESSAGE_DATE_CONFLICT);
                return null;
            }
    
            // Parse natural date using Natty.
            dateOn = naturalOn == null ? null : DateUtil.floorDate(DateUtil.parseNatural(naturalOn)); 
            dateFrom = naturalFrom == null ? null : DateUtil.floorDate(DateUtil.parseNatural(naturalFrom)); 
            dateTo = naturalTo == null ? null : DateUtil.floorDate(DateUtil.parseNatural(naturalTo));
        }
        
        if (parsedDates != null && dateOn == null && dateFrom == null && dateTo == null) {
            //Natty failed to parse date
            Renderer.renderDisambiguation(LIST_DATE_SYNTAX, MESSAGE_NO_DATE_DETECTED);
            return null;
        }
       
        return new LocalDateTime[] { dateCriteria, dateOn, dateFrom, dateTo };
    }
    
    /*
     * To be use to check if there are any command syntax error
     * 
     * @return true, if there is error in command syntax, false if syntax is allowed
     */
    private boolean isErrorCommand(boolean isTaskStatusProvided, boolean isEventStatusProvided, boolean isTask, String input) {
        // Check if more than 1 item type is provided
        if (FilterUtil.isItemTypeConflict(input)) {
            Renderer.renderDisambiguation(COMMAND_SYNTAX, MESSAGE_ITEM_TYPE_CONFLICT);
            return true;
        }
        
        // Task and Event Command Syntax detected
        if (isTask && isEventStatusProvided) {
            Renderer.renderDisambiguation(LIST_TASK_SYNTAX, MESSAGE_INVALID_TASK_STATUS);
            return true;
        }
        
        if (!isTask && isTaskStatusProvided) {
            Renderer.renderDisambiguation(LIST_EVENT_SYNTAX, MESSAGE_INVALID_EVENT_STATUS);
            return true;
        }
        return false;
    }

    /*
     * Filter out the selected tasks and events based on the search criteria
     * 
     */
    private void filterTasksAndEvents(TodoListDB db, boolean isItemTypeProvided, boolean isTaskStatusProvided,
            boolean isEventStatusProvided, boolean isTask, boolean isCompleted, boolean isOver,
            LocalDateTime dateCriteria, LocalDateTime dateOn, LocalDateTime dateFrom, LocalDateTime dateTo) {
        List<Task> tasks = db.getAllTasks(); 
        List<Event> events = db.getAllEvents();; 
        if (isItemTypeProvided) {
            if (isTask) {
                events = new ArrayList<Event>();
            } else if (!isTask) {
                tasks = new ArrayList<Task>();
            }
        } 
        
        if (isTaskStatusProvided) {
            tasks = FilterUtil.filterTasksByStatus(tasks, isCompleted);
            events = new ArrayList<Event>();
        }
        
        if (isEventStatusProvided) {
            events = FilterUtil.filterEventsByStatus(events, isOver);
            tasks = new ArrayList<Task>();
        }
        
        if (dateCriteria != null) {
            tasks = FilterUtil.filterTaskBySingleDate(tasks, dateCriteria);
            events = FilterUtil.filterEventBySingleDate(events, dateCriteria);
        }
        
        if (dateOn != null) {
            //filter by single date
            tasks = FilterUtil.filterTaskBySingleDate(tasks, dateOn);
            events = FilterUtil.filterEventBySingleDate(events, dateOn);
        } else {
            //filter by range
            tasks = FilterUtil.filterTaskWithDateRange(tasks, dateFrom, dateTo);
            events = FilterUtil.filterEventWithDateRange(events, dateFrom, dateTo);
        }
        
        if (tasks.size() == 0 && events.size() == 0) {
            Renderer.renderIndex(db, MESSAGE_NO_RESULT_FOUND);
            return;
        }
        
        String consoleMessage = String.format(MESSAGE_RESULT_FOUND, 
                StringUtil.displayNumberOfTaskAndEventFoundWithPuralizer(tasks.size(), events.size()));
        Renderer.renderSelectedIndex(db, consoleMessage, tasks, events);
    }
}
