package seedu.todo.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import seedu.todo.commons.exceptions.InvalidNaturalDateException;
import seedu.todo.commons.exceptions.ParseException;
import seedu.todo.commons.util.DateUtil;
import seedu.todo.commons.util.FilterUtil;
import seedu.todo.commons.util.ParseUtil;
import seedu.todo.commons.util.StringUtil;
import seedu.todo.controllers.concerns.Tokenizer;
import seedu.todo.controllers.concerns.DateParser;
import seedu.todo.controllers.concerns.Renderer;
import seedu.todo.models.Event;
import seedu.todo.models.Task;
import seedu.todo.models.TodoListDB;

/**
 * @@author A0139922Y
 * Controller to find task/event by keyword
 * 
 */
public class FindController implements Controller {
    
    private static final String NAME = "Find";
    private static final String DESCRIPTION = "Find all tasks and events based on the provided keywords.\n" + 
        "This command will be searching with non-case sensitive keywords.";
    private static final String COMMAND_WORD = "find";
    
    // Syntax correction to console input
    private static final String COMMAND_SYNTAX = "find \"name\" tagName \"tag\" on \"date\" \"task/event\"";
    private static final String FIND_TASK_SYNTAX = "find \"name\" task \"complete/incomplete\"";
    private static final String FIND_EVENT_SYNTAX = "find \"name\" event \"over/ongoing\"";
    
    // Message output to console text area
    private static final String MESSAGE_RESULT_FOUND = "A total of %s found!";
    private static final String MESSAGE_NO_RESULT_FOUND = "No task or event found!";
    private static final String MESSAGE_NO_KEYWORD_FOUND = "No keyword found!";
    private static final String MESSAGE_DATE_CONFLICT = "Unable to find!\nMore than 1 date criteria is provided!";
    private static final String MESSAGE_NO_DATE_DETECTED = "Unable to find!\nThe natural date entered is not supported.";
    private static final String MESSAGE_INVALID_TASK_STATUS = "Unable to find!\nTry searching with complete or incomplete";
    private static final String MESSAGE_INVALID_EVENT_STATUS = "Unable to find!\nTry searching with over or current";
    private static final String MESSAGE_ITEM_TYPE_CONFLICT = "Unable to list!\nMore than 1 item type is provided!";
    
    private static final int COMMAND_INPUT_INDEX = 0;
    //use to access parsing of dates
    private static final int NUM_OF_DATES_FOUND_INDEX = 0;
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
        String command = StringUtil.splitStringBySpace(input.toLowerCase())[COMMAND_INPUT_INDEX];
        return (command).equals(COMMAND_WORD) ? 1 : 0;
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
        tokenDefinitions.put(Tokenizer.ITEM_NAME_TOKEN, Tokenizer.ITEM_NAME_DEFINITION);
        tokenDefinitions.put(Tokenizer.TAG_NAME_TOKEN, Tokenizer.TAG_NAME_DEFINITION);
        return tokenDefinitions;
    }

    @Override
    public void process(String input) throws ParseException {
        
        Map<String, String[]> parsedResult;
        parsedResult = Tokenizer.tokenize(getTokenDefinitions(), input);
        
        HashSet<String> itemNameList = new HashSet<String>();
        HashSet<String> tagNameList = new HashSet<String>();
        HashSet<String> keywordList = new HashSet<String>();
        
        // To be use to be filter out name and tag names
        updateHashList(parsedResult, keywordList, Tokenizer.DEFAULT_TOKEN);
        updateHashList(parsedResult, itemNameList, Tokenizer.ITEM_NAME_TOKEN);
        updateHashList(parsedResult, tagNameList, Tokenizer.TAG_NAME_TOKEN);
        itemNameList.addAll(keywordList);
        tagNameList.addAll(keywordList);
        
        // Show console output message, since no keyword found
        if (keywordList.size() == 0 && itemNameList.size() == 0 && tagNameList.size() == 0) {
            //No keyword provided, display error
            Renderer.renderDisambiguation(COMMAND_SYNTAX, MESSAGE_NO_KEYWORD_FOUND);
            return;
        }
        
        // Check if input includes itemType and itemStatus
        boolean isItemTypeProvided = !ParseUtil.isTokenNull(parsedResult, Tokenizer.EVENT_TYPE_TOKEN);
        boolean isTaskStatusProvided = !ParseUtil.isTokenNull(parsedResult, Tokenizer.TASK_STATUS_TOKEN);
        boolean isEventStatusProvided = !ParseUtil.isTokenNull(parsedResult, Tokenizer.EVENT_STATUS_TOKEN);
        
        // Set item type
        boolean isTask = true; //default
        if (isItemTypeProvided) {
            isTask = ParseUtil.doesTokenContainKeyword(parsedResult, Tokenizer.EVENT_TYPE_TOKEN, "task");
        }
        
        if (isErrorCommand(isTaskStatusProvided, isEventStatusProvided, isTask, isItemTypeProvided, input)) {
            return; // Break out if found error
        }
        
        // Set item status
        boolean isCompleted = false; //default 
        boolean isOver = false; //default
        if (isTaskStatusProvided) {
            isCompleted = !ParseUtil.doesTokenContainKeyword(parsedResult, Tokenizer.TASK_STATUS_TOKEN, "incomplete");
        }
        if (isEventStatusProvided) {
            isOver = ParseUtil.doesTokenContainKeyword(parsedResult, Tokenizer.EVENT_STATUS_TOKEN, "over");
        }
        
        // Get dates from input
        String[] parsedDates = ParseUtil.parseDates(parsedResult);
        LocalDateTime [] validDates = parsingDates(parsedResult, parsedDates);
        if (validDates == null) {
            return; // Break out when date conflict found
        }
        
        // Set dates that are found, if not found value will be null
        LocalDateTime dateOn = validDates[DATE_ON_INDEX];
        LocalDateTime dateFrom = validDates[DATE_FROM_INDEX];
        LocalDateTime dateTo = validDates[DATE_TO_INDEX];
                
        //setting up view
        TodoListDB db = TodoListDB.getInstance();
        List<Task> tasks; //default
        List<Event> events; //default
        
        // Filter out the tasks and events based on type and names
        if (!isItemTypeProvided) {
            tasks = filterByTaskNameAndTagName(itemNameList, tagNameList, db.getAllTasks());
            events = filterByEventNameAndTagName(itemNameList, tagNameList, db.getAllEvents());
        } else {
            if (isTask) {
                events = new ArrayList<Event>();
                tasks = filterByTaskNameAndTagName(itemNameList, tagNameList, db.getAllTasks());
            } else {
                tasks = new ArrayList<Task>();
                events = filterByEventNameAndTagName(itemNameList, tagNameList, db.getAllEvents());
            }
        }
        
        // Filter out by Task Status if provided
        if (isTaskStatusProvided) {
            FilterUtil.filterTasksByStatus(tasks, isCompleted);
            events = new ArrayList<Event>();
        }
        
        // Filter out by Event Status if provided
        if (isEventStatusProvided) {
            FilterUtil.filterEventsByStatus(events, isOver);
            tasks = new ArrayList<Task>();
        }
        
        // Filter Task and Event by date
        filterTasksAndEventsByDate(tasks, events, dateOn, dateFrom, dateTo);
        
        // Show messaeg as no items had been found
        if (tasks.size() == 0 && events.size() == 0) {
            Renderer.renderIndex(db, MESSAGE_NO_RESULT_FOUND);
            return;
        }
        
        String consoleMessage = String.format(MESSAGE_RESULT_FOUND, 
                StringUtil.displayNumberOfTaskAndEventFoundWithPuralizer(tasks.size(), events.size()));
        Renderer.renderSelectedIndex(db, consoleMessage, tasks, events);
    }

    /*
     * Filter out the selected tasks and events based on the date that has been parsed
     * and update tasks and events accordingly
     * 
     * @param tasks
     *            List of Task items
     * @param events           
     *            List of Event items
     */
    private void filterTasksAndEventsByDate(List<Task> tasks, List<Event> events, 
            LocalDateTime dateOn, LocalDateTime dateFrom, LocalDateTime dateTo) {
        
        if (dateOn != null) {
            //filter by single date
            FilterUtil.filterTaskBySingleDate(tasks, dateOn);
            FilterUtil.filterEventBySingleDate(events, dateOn);
        } else {
            //filter by range
            FilterUtil.filterTaskWithDateRange(tasks, dateFrom, dateTo);
            FilterUtil.filterEventWithDateRange(events, dateFrom, dateTo);
        }
    }  

    /*======================== Helper Methods for filtering by names and tags =============================*/
    
    /*
     * Filter out all the events based on the name list that has been parsed.
     * This method also ensure that no duplicate event will be return. 
     * 
     * @param itemNameList
     *            a list of item name that has been parsed from input
     * @param tagNameList           
     *            a List of tag name that has been parsed from input
     * @return a list of Event which names or tag names is filtered with the list
     */
    private List<Event> filterByEventNameAndTagName(HashSet<String> itemNameList, HashSet<String> tagNameList,
            List<Event> events) {
        HashSet<Event> mergedEvents = new HashSet<Event>();
        List<Event> eventsByNames = FilterUtil.filterEventByNames(events, itemNameList);
        List<Event> eventsByTags = FilterUtil.filterEventByTags(events, tagNameList);
        mergedEvents.addAll(eventsByNames);
        mergedEvents.addAll(eventsByTags);
        events = new ArrayList<Event>(mergedEvents);
        return events;
    }

    /*
     * Filter out all the tasks based on the name list that has been parsed.
     * This method also ensure that no duplicate task will be return. 
     * 
     * @param itemNameList
     *            a list of item name that has been parsed from input
     * @param tagNameList           
     *            a List of tag name that has been parsed from input
     * @return a list of Task which names or tag names is filtered with the list
     */
    private List<Task> filterByTaskNameAndTagName(HashSet<String> itemNameList, HashSet<String> tagNameList, 
            List<Task> tasks) {
        HashSet<Task> mergedTasks = new HashSet<Task>();
        List<Task> tasksByNames = FilterUtil.filterTaskByNames(tasks, itemNameList);
        List<Task> tasksByTags = FilterUtil.filterTaskByTags(tasks, tagNameList);
        mergedTasks.addAll(tasksByNames);
        mergedTasks.addAll(tasksByTags);
        tasks = new ArrayList<Task>(mergedTasks);
        return tasks;
    }
    
    /**
     * Extract the parsed result and update into the hashlist
     * @param parsedResult
     */
    private void updateHashList(Map<String, String[]> parsedResult, HashSet<String> hashList, 
            String token) {
      
        String result = ParseUtil.getTokenResult(parsedResult, token);
        //if found any matching , update list
        if (result != null) {
            hashList.add(result);
            String[] resultArray = StringUtil.splitStringBySpace(result);
            for (int i = 0; i < resultArray.length; i ++) {
                hashList.add(resultArray[i]);
            }
        }
    }
    
    /*============================ Helper Methods to check for Error/Syntax Command ===================*/
    
    /*
     * To be use to check if there are any command syntax error
     * 
     * @return true, if there is an error in the command syntax, false if syntax is allowed
     */
    private boolean isErrorCommand(boolean isTaskStatusProvided, boolean isEventStatusProvided, 
            boolean isTask, boolean isItemTypeProvided, String input) {
        // Check if more than 1 item type is provided
        if (FilterUtil.isItemTypeConflict(input)) {
            Renderer.renderDisambiguation(COMMAND_SYNTAX, MESSAGE_ITEM_TYPE_CONFLICT);
            return true;
        }
        if (isItemTypeProvided) {
            // Task and Event Command Syntax detected
            if (isTask && isEventStatusProvided) {
                Renderer.renderDisambiguation(FIND_TASK_SYNTAX, MESSAGE_INVALID_TASK_STATUS);
                return true;
            }
            
            if (!isTask && isTaskStatusProvided) {
                Renderer.renderDisambiguation(FIND_EVENT_SYNTAX, MESSAGE_INVALID_EVENT_STATUS);
                return true;
            }
        }
        return false;
    }
    
    /*
     * To be used to parsed dates and check for any dates conflict
     * 
     * @return null if dates conflict detected, else return { dateCriteria, dateOn, dateFrom, dateTo }
     */
    private LocalDateTime[] parsingDates(Map<String, String[]> parsedResult, String[] parsedDates) {
        
        LocalDateTime dateOn = null;
        LocalDateTime dateFrom = null;
        LocalDateTime dateTo = null;
        
        if (parsedDates != null) {
            String naturalOn = parsedDates[DATE_ON_INDEX];
            String naturalFrom = parsedDates[DATE_FROM_INDEX];
            String naturalTo = parsedDates[DATE_TO_INDEX];
            
            if (naturalOn != null && Integer.parseInt(parsedDates[NUM_OF_DATES_FOUND_INDEX]) > 1) {
                //date conflict detected
                Renderer.renderDisambiguation(COMMAND_SYNTAX, MESSAGE_DATE_CONFLICT);
                return null;
            }
            // Parse natural date using Natty.
            try {
                dateOn = naturalOn == null ? null : DateUtil.floorDate(DateParser.parseNatural(naturalOn)); 
                dateFrom = naturalFrom == null ? null : DateUtil.floorDate(DateParser.parseNatural(naturalFrom)); 
                dateTo = naturalTo == null ? null : DateUtil.floorDate(DateParser.parseNatural(naturalTo));
            } catch (InvalidNaturalDateException e) {
                    Renderer.renderDisambiguation(COMMAND_SYNTAX, MESSAGE_NO_DATE_DETECTED);
                    return null;
            }           
        }
        return new LocalDateTime[] { null, dateOn, dateFrom, dateTo };
    }
}
