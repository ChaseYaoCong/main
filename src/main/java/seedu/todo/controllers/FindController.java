package seedu.todo.controllers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


import seedu.todo.commons.exceptions.ParseException;
import seedu.todo.commons.util.ParseUtil;
import seedu.todo.commons.util.StringUtil;
import seedu.todo.controllers.concerns.Tokenizer;
import seedu.todo.controllers.concerns.Renderer;
import seedu.todo.models.Event;
import seedu.todo.models.Task;
import seedu.todo.models.TodoListDB;

/**
 * Controller to find task/event by keyword
 * 
 * @@author Tiong YaoCong A0139922Y
 *
 */
public class FindController implements Controller {
    
    private static final String NAME = "Find";
    private static final String DESCRIPTION = "Find all tasks and events based on the provided keywords.\n" + 
    "This command will be search with non-case sensitive keywords.";
    private static final String COMMAND_SYNTAX = "find [name] or/and [on date]";
    private static final String COMMAND_WORD = "find";
    
    private static final String MESSAGE_LISTING_SUCCESS = "A total of %s found!";
    private static final String MESSAGE_LISTING_FAILURE = "No task or event found!";

    //use to access parsing of dates
    private static final int NUM_OF_DATES_FOUND_INDEX = 0;
    private static final int DATE_TO_INDEX = 1;
    private static final int DATE_FROM_INDEX = 2;
    private static final int DATE_ON_INDEX = 3;
    
    private static CommandDefinition commandDefinition =
            new CommandDefinition(NAME, DESCRIPTION, COMMAND_SYNTAX); 

    public static CommandDefinition getCommandDefinition() {
        return commandDefinition;
    }

    @Override
    public float inputConfidence(String input) {
        return (input.toLowerCase().startsWith(COMMAND_WORD)) ? 1 : 0;
    }
    
    private static Map<String, String[]> getTokenDefinitions() {
        Map<String, String[]> tokenDefinitions = new HashMap<String, String[]>();
        tokenDefinitions.put("default", new String[] {"find"});
        tokenDefinitions.put("eventType", new String[] { "event", "events", "task", "tasks"});
        tokenDefinitions.put("status", new String[] { "complete" , "completed", "incomplete", "incompleted"});
        tokenDefinitions.put("time", new String[] { "at", "by", "on", "time" });
        tokenDefinitions.put("timeFrom", new String[] { "from" });
        tokenDefinitions.put("timeTo", new String[] { "to", "before" });
        tokenDefinitions.put("itemName", new String[] { "name" });
        tokenDefinitions.put("tagName", new String [] { "tag" }); 
        return tokenDefinitions;
    }

    @Override
    public void process(String input) throws ParseException {
        
        Map<String, String[]> parsedResult;
        parsedResult = Tokenizer.tokenize(getTokenDefinitions(), input);
        
        HashSet<String> itemNameList = new HashSet<String>();
        HashSet<String> tagNameList = new HashSet<String>();
        HashSet<String> keywordList = new HashSet<String>();
        
        //to be use to be either name or tag
        updateHashList(parsedResult, keywordList, "default");
        updateHashList(parsedResult, itemNameList, "itemName");
        updateHashList(parsedResult, tagNameList, "tagName");
        
        if (keywordList.size() == 0 && itemNameList.size() == 0 && tagNameList.size() == 0) {
            //No name provided display error
            return;
        }
        
        boolean isItemTypeProvided = !ParseUtil.isTokenNull(parsedResult, "eventType");
        boolean isItemStatusProvided = !ParseUtil.isTokenNull(parsedResult, "status");
        
        boolean isTask = true; //default
        //if listing all type , set isTask and isEvent true
        if (isItemTypeProvided) {
            isTask = ParseUtil.doesTokenContainKeyword(parsedResult, "eventType", "task");
        }
        
        
        boolean isCompleted = false; //default 
        //if listing all status, isCompleted will be ignored, listing both complete and incomplete
        if (isItemStatusProvided) {
            isCompleted = !ParseUtil.doesTokenContainKeyword(parsedResult, "status", "incomplete");
        }
        
        String[] parsedDates = ParseUtil.parseDates(parsedResult);
        
        LocalDateTime dateOn = null;
        LocalDateTime dateFrom = null;
        LocalDateTime dateTo = null;
        
        if (parsedDates != null) {
            String naturalOn = parsedDates[DATE_ON_INDEX];
            String naturalFrom = parsedDates[DATE_FROM_INDEX];
            String naturalTo = parsedDates[DATE_TO_INDEX];
            
            if (naturalOn != null && Integer.parseInt(parsedDates[NUM_OF_DATES_FOUND_INDEX]) > 1) {
                //date conflict detected
            }
    
            // Parse natural date using Natty.
            dateOn = naturalOn == null ? null : ParseUtil.parseNatural(naturalOn); 
            dateFrom = naturalFrom == null ? null : ParseUtil.parseNatural(naturalFrom); 
            dateTo = naturalTo == null ? null : ParseUtil.parseNatural(naturalTo);
        }
        //setting up view
        setupView(isTask, isItemTypeProvided, isCompleted, isItemStatusProvided, dateOn, dateFrom, dateTo, itemNameList, tagNameList);
        
    }

    /**
     * Setting up the view 
     * 
     * @param isTask
     *            true if CalendarItem should be a Task, false if Event
     * @param isEvent
     *            true if CalendarItem should be a Event, false if Task  
     * @param listAll
     *            true if listing all type, isTask or isEvent are ignored          
     * @param isCompleted
     *            true if user request completed item
     * @param listAllStatus
     *            true if user did not request any status, isCompleted is ignored
     * @param dateOn
     *            Date if user specify for a certain date
     * @param dateFrom
     *            Due date for Task or start date for Event
     * @param dateTo
     *            End date for Event
     * @param itemNameList 
     *            String of Calendar Item name that user enter as keyword
     * @param tagNameList 
     *            String of Tag Name that user enter as keyword                      
     */
    private void setupView(boolean isTask, boolean listAll, boolean isCompleted,
            boolean listAllStatus, LocalDateTime dateOn, LocalDateTime dateFrom,
            LocalDateTime dateTo, HashSet<String> itemNameList, HashSet<String> tagNameList) {
        TodoListDB db = TodoListDB.getInstance();
        List<Task> tasks = null;
        List<Event> events = null;
        // isTask and isEvent = true, list all type
        if (listAll) {
            //no event or task keyword found
            isTask = false;
            tasks = setupTaskView(isCompleted, listAllStatus, dateOn, dateFrom, dateTo, itemNameList, tagNameList, db);
            events = setupEventView(isCompleted, listAllStatus, dateOn, dateFrom, dateTo, itemNameList, tagNameList, db);
        }
        
        if (isTask) {
            tasks = setupTaskView(isCompleted, listAllStatus, dateOn, dateFrom, dateTo, itemNameList, tagNameList, db);
        } else {
            events = setupEventView(isCompleted, listAllStatus, dateOn, dateFrom, dateTo, itemNameList, tagNameList, db);
        }
        
        // Update console message
        int numTasks = 0;
        int numEvents = 0;
        
        if (tasks != null) {
            numTasks = tasks.size();
        }
        
        if (events != null) {
            numEvents = events.size();
        }
        
        String consoleMessage = MESSAGE_LISTING_FAILURE;
        if (numTasks != 0 || numEvents != 0) {
            consoleMessage = String.format(MESSAGE_LISTING_SUCCESS, formatDisplayMessage(numTasks, numEvents));
        }
        
        Renderer.renderSelected(db, consoleMessage, tasks, events);
       
    }
    
    private String formatDisplayMessage(int numTasks, int numEvents) {
        if (numTasks != 0 && numEvents != 0) {
            return String.format("%s and %s", formatTaskMessage(numTasks), formatEventMessage(numEvents));
        } else if (numTasks != 0) {
            return formatTaskMessage(numTasks);
        } else {
            return formatEventMessage(numEvents);
        }
    }
    
    private String formatEventMessage(int numEvents) {
        return String.format("%d %s", numEvents, StringUtil.pluralizer(numEvents, "event", "events"));
    }
    
    private String formatTaskMessage (int numTasks) {
        return String.format("%d %s", numTasks, StringUtil.pluralizer(numTasks, "task", "tasks"));
    }
    
    private List<Event> setupEventView(boolean isCompleted, boolean listAllStatus, LocalDateTime dateOn, 
            LocalDateTime dateFrom, LocalDateTime dateTo, HashSet<String> itemNameList, HashSet<String> tagNameList, TodoListDB db) {
        final LocalDateTime NO_DATE = null;
        if (dateFrom == null && dateTo == null && dateOn == null) {
            if (listAllStatus && itemNameList.size() == 0 && tagNameList.size() == 0) {
                System.out.println("error"); //TODO : Nothing found
                return null;
            } else if (listAllStatus && (itemNameList.size() != 0 || tagNameList.size() != 0)) {
                return db.getEventByName(db.getAllEvents(), itemNameList, tagNameList);
            }
            else if (isCompleted) {
                return db.getEventByRangeWithName(NO_DATE, LocalDateTime.now(), itemNameList, tagNameList);
            } else {
                return db.getEventByRangeWithName(LocalDateTime.now(), NO_DATE, itemNameList, tagNameList);
            } 
        } else if (dateOn != null) { //by keyword found
            return db.getEventbyDateWithName(dateOn, itemNameList, tagNameList);
        } else {
            return db.getEventByRangeWithName(dateFrom, dateTo, itemNameList, tagNameList);
        }
    }

    private List<Task> setupTaskView(boolean isCompleted, boolean listAllStatus, LocalDateTime dateOn, 
            LocalDateTime dateFrom, LocalDateTime dateTo, HashSet<String> itemNameList, HashSet<String> tagNameList, TodoListDB db) {
        if (dateFrom == null && dateTo == null && dateOn == null) {
            if (listAllStatus && itemNameList.size() == 0 && tagNameList.size() == 0) {
                System.out.println("error"); //TODO : Nothing found
                return null;
            } else {
                //getting all task by the name, dateFrom and dateTo will be null
                return db.getTaskByRangeWithName(dateFrom, dateTo, isCompleted, listAllStatus, itemNameList, tagNameList);
            }
        } else if (dateOn != null) { //by keyword found
            return db.getTaskByDateWithStatusAndName(dateOn, isCompleted, listAllStatus, itemNameList, tagNameList);
        } else {
            return db.getTaskByRangeWithName(dateFrom, dateTo, isCompleted, listAllStatus, itemNameList, tagNameList);
        }
    }
    
    /**
     * Extract the parsed result and update the hash list
     * @param parsedResult
     */
    private void updateHashList(Map<String, String[]> parsedResult, HashSet<String> hashList, 
            String token) {
      
        String result = ParseUtil.getTokenResult(parsedResult, token);
        
        //if found any matching , update list
        if (result != null) {
            hashList.add(result);
            String[] resultArray = StringUtil.convertStringIntoArray(result);
            for (int i = 0; i < resultArray.length; i ++) {
                hashList.add(resultArray[i]);
            }
        }
    }
    
    /**
     * Extracts the intended status type specify from parsedResult.
     * 
     * @param parsedResult
     * @return true if Task or event is not specify, false if either Task or Event specify
     */
    private boolean parseListAllStatus (Map<String, String[]> parsedResult) {
        return !(parsedResult.get("status") != null);
    }
    
    /**
     * Extracts the intended CalendarItem status from parsedResult.
     * 
     * @param parsedResult
     * @return true if uncomplete, false if complete
     */
    private boolean parseIsUncomplete (Map<String, String[]> parsedResult) {
        return parsedResult.get("status")[0].contains("uncomplete");
    }
    
    /**
     * Extracts the intended CalendarItem type from parsedResult.
     * 
     * @param parsedResult
     * @return true if Task, false if Event
     */
    private boolean parseIsTask (Map<String, String[]> parsedResult) {
        return parsedResult.get("eventType")[0].contains("task");
    }
    
    /**
     * Extracts the natural dates from parsedResult.
     * 
     * @param parsedResult
     * @return { naturalOn, naturalFrom, naturalTo }
     */
    private String[] parseDates(Map<String, String[]> parsedResult) {
        String naturalFrom = null;
        String naturalTo = null;
        String naturalOn = null;
        
        if (parsedResult.get("time") == null) {
            if (parsedResult.get("timeFrom") != null) {
                naturalFrom = parsedResult.get("timeFrom")[1];
            }
            if (parsedResult.get("timeTo") != null) {
                naturalTo = parsedResult.get("timeTo")[1];
            }
        } else {
            naturalOn = parsedResult.get("time")[1];
        }
        
        if (naturalFrom == null && naturalTo == null && naturalOn == null) {
            // no date found
            return null;
        }
        return new String[] { naturalOn, naturalFrom, naturalTo };
    }
    

}
