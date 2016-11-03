package seedu.todo.controllers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import seedu.todo.commons.EphemeralDB;
import seedu.todo.commons.exceptions.ParseException;
import seedu.todo.commons.util.DateUtil;
import seedu.todo.commons.util.ParseUtil;
import seedu.todo.commons.util.StringUtil;
import seedu.todo.controllers.concerns.Renderer;
import seedu.todo.controllers.concerns.Tokenizer;
import seedu.todo.models.CalendarItem;
import seedu.todo.models.Event;
import seedu.todo.models.Task;
import seedu.todo.models.TodoListDB;

/**
 * @@author A0139922Y
 * Controller to update a CalendarItem.
 * 
 */
public class UpdateController implements Controller {
    
    private static final String NAME = "Update";
    private static final String DESCRIPTION = "Updates a task by listed index.";
    private static final String COMMAND_SYNTAX = "update <index> <name> on <deadline> tag <tagName> newTag <tagName>";
    private static final String UPDATE_EVENT_SYNTAX = "update <index> <name> event from <date/time> to <date/time>";
    private static final String UPDATE_TASK_SYNTAX = "update <index> <name> task on <date/time>";
    private static final String UPDATE_TAG_SYNTAX = "update <index> tag <tagName> newTag <tagName>";
    
    private static final String COMMAND_WORD = "update";
       
    private static final String MESSAGE_INDEX_OUT_OF_RANGE = "Could not update task/event: Invalid index provided!";
    private static final String MESSAGE_MISSING_INDEX_AND_PARAMETERS = "Please specify the index of the item and details to update.";
    private static final String MESSAGE_INDEX_NOT_NUMBER = "Index has to be a number!";
    private static final String MESSAGE_INVALID_ITEMTYPE = "Unable to update!\nTry updating with the syntax provided!";
    private static final String MESSAGE_DATE_CONFLICT = "Unable to update!\nMore than 1 date criteria is provided!";
    private static final String MESSAGE_NO_DATE_DETECTED = "Unable to update!\nThe natural date entered is not supported.";
    private static final String MESSAGE_INVALID_TAG = "Unable to update!\nTag name not found!";
    private static final String MESSAGE_UPDATE_SUCCESS = "Item successfully updated!";
    
    private static final int COMMAND_INPUT_INDEX = 0;
    private static final int TOKENIZER_DEFAULT_INDEX = 1;
    private static final int ITEM_INDEX = 0;
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
        tokenDefinitions.put("default", new String[] {"update"});
        //tokenDefinitions.put("eventType", new String[] { "event", "events", "task", "tasks"});
        //tokenDefinitions.put("taskStatus", new String[] { "complete" , "completed", "incomplete", "incompleted"});
        tokenDefinitions.put("time", new String[] { "at", "by", "on", "time", "date" });
        tokenDefinitions.put("timeFrom", new String[] { "from" });
        tokenDefinitions.put("timeTo", new String[] { "to", "before", "until" });
        tokenDefinitions.put("itemName", new String[] { "name" });
        tokenDefinitions.put("tagName", new String [] { "tag" });
        tokenDefinitions.put("newTagName", new String [] { "newTag" });
        return tokenDefinitions;
    }

    @Override
    public void process(String input) throws ParseException {
        
        input = input.replaceFirst(COMMAND_WORD, "");
        if (input.length() <= 0) {
            Renderer.renderDisambiguation(COMMAND_SYNTAX, MESSAGE_MISSING_INDEX_AND_PARAMETERS);
            return;
        }
        
        int index = 0;
        String params = null;
        try {
            index = Integer.decode(StringUtil.splitStringBySpace(input)[ITEM_INDEX]);
            params = input.replaceFirst(Integer.toString(index), "").trim();
            params = COMMAND_WORD + " " + params;
        } catch (NumberFormatException e) {
            Renderer.renderDisambiguation(COMMAND_SYNTAX, MESSAGE_INDEX_NOT_NUMBER);
            return;
        }
        
        Map<String, String[]> parsedResult;
        parsedResult = Tokenizer.tokenize(getTokenDefinitions(), params);
        String itemName = parsedResult.get("default")[TOKENIZER_DEFAULT_INDEX];
        if (ParseUtil.getTokenResult(parsedResult, "itemName") != null && itemName != null) {
            itemName = itemName + ParseUtil.getTokenResult(parsedResult, "itemName");
        } else if (itemName == null) {
            itemName = ParseUtil.getTokenResult(parsedResult, "itemName");
        }
        
        String[] parsedDates = ParseUtil.parseDates(parsedResult);
        String oldTagName = ParseUtil.getTokenResult(parsedResult, "tagName");
        String newTagName = ParseUtil.getTokenResult(parsedResult, "newTagName");
        
        //only 1 tag name detected
        if ((oldTagName != null && newTagName == null) || (oldTagName == null && newTagName != null)) {
            Renderer.renderDisambiguation(UPDATE_TAG_SYNTAX, MESSAGE_INVALID_ITEMTYPE);
            return;
        }
        
        //no details provided to update
        if (parsedDates == null && itemName == null && oldTagName == null && newTagName == null) {
            Renderer.renderDisambiguation(UPDATE_TASK_SYNTAX, MESSAGE_INVALID_ITEMTYPE);
            return ;
        }
        
        int numOfDatesFound = 0;
        LocalDateTime dateOn = null;
        LocalDateTime dateFrom = null;
        LocalDateTime dateTo = null;
        
        if (parsedDates != null) {
            numOfDatesFound = Integer.parseInt(parsedDates[NUM_OF_DATES_FOUND_INDEX]);
            String naturalOn = parsedDates[DATE_ON_INDEX];
            String naturalFrom = parsedDates[DATE_FROM_INDEX];
            String naturalTo = parsedDates[DATE_TO_INDEX];
            
            if (naturalOn != null && numOfDatesFound > 1) {
                //date conflict detected
                Renderer.renderDisambiguation(COMMAND_SYNTAX, MESSAGE_DATE_CONFLICT);
                return;
            }
    
            // Parse natural date using Natty.
            dateOn = naturalOn == null ? null : DateUtil.parseNatural(naturalOn); 
            dateFrom = naturalFrom == null ? null : DateUtil.parseNatural(naturalFrom); 
            dateTo = naturalTo == null ? null : DateUtil.parseNatural(naturalTo);
        }
        
        if (parsedDates != null && dateOn == null && dateFrom == null && dateTo == null) {
            //Natty failed to parse date
            Renderer.renderDisambiguation(COMMAND_SYNTAX, MESSAGE_NO_DATE_DETECTED);
            return ;
        }
        
        // Get record
        EphemeralDB edb = EphemeralDB.getInstance();
        CalendarItem calendarItem = edb.getCalendarItemsByDisplayedId(index);
        TodoListDB db = TodoListDB.getInstance();
        
        if (calendarItem == null) {
            Renderer.renderDisambiguation(COMMAND_SYNTAX, MESSAGE_INDEX_OUT_OF_RANGE);
            return;
        }
        
        boolean isCalendarItemTask = calendarItem instanceof Task;
        
        //update task date , error found
        if (isCalendarItemTask && dateOn == null && numOfDatesFound > 0) {
            Renderer.renderDisambiguation(UPDATE_TASK_SYNTAX, MESSAGE_INVALID_ITEMTYPE);
            return ;
        }
        
        //update event date , error found
        if (!isCalendarItemTask && (dateFrom == null || dateTo == null) && numOfDatesFound > 0) {
            Renderer.renderDisambiguation(UPDATE_EVENT_SYNTAX, MESSAGE_INVALID_ITEMTYPE);
            return ;
        }
        
        //calendarItem does not contain tag
        if (!calendarItem.getTagList().contains(oldTagName) && oldTagName != null && newTagName != null) {
            Renderer.renderDisambiguation(UPDATE_TAG_SYNTAX, MESSAGE_INVALID_TAG);
            return ;
        }
        
        updateCalendarItem(itemName, dateOn, dateFrom, dateTo, calendarItem, isCalendarItemTask, oldTagName, newTagName, db);
        
        db.save();
        
        // Re-render
        Renderer.renderIndex(db, MESSAGE_UPDATE_SUCCESS);
    }

    
    /*
     * Update calendarItem according to user input 
     * 
     */
    private void updateCalendarItem(String itemName, LocalDateTime dateOn, LocalDateTime dateFrom, LocalDateTime dateTo,
            CalendarItem calendarItem, boolean isCalendarItemTask, String oldTagName, String newTagName, TodoListDB db) {
        //update name
        if (itemName != null) {
            calendarItem.setName(itemName);
        }
        
        if (oldTagName != null && newTagName != null) {
            calendarItem.removeTag(oldTagName);
            calendarItem.addTag(newTagName);
            db.addIntoTagList(new String[] { newTagName });
            db.updateTagList(new String[] { oldTagName });
        }
        
        //update task date
        if (isCalendarItemTask && dateOn != null) {
            calendarItem.setCalendarDateTime(DateUtil.parseTimeStamp(dateOn, dateTo, true));
        }
        
        //update event date
        if (dateFrom != null && dateTo !=null && !isCalendarItemTask) {
            LocalDateTime parsedDateFrom = DateUtil.parseTimeStamp(dateFrom, dateTo, true);
            LocalDateTime parsedDateTo = DateUtil.parseTimeStamp(dateTo, dateFrom, false);
            dateFrom = parsedDateFrom;
            dateTo = parsedDateTo;
            Event event = (Event) calendarItem;
            event.setStartDate(dateFrom);
            event.setEndDate(dateTo);
        }
    }
}
