package seedu.todo.controllers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import seedu.todo.commons.EphemeralDB;
import seedu.todo.commons.exceptions.ParseException;
import seedu.todo.commons.util.StringUtil;
import seedu.todo.controllers.concerns.Renderer;
import seedu.todo.controllers.concerns.Tokenizer;
import seedu.todo.models.CalendarItem;
import seedu.todo.models.TodoListDB;

//@@author A0139922Y
/**
 * Controller to untag a CalendarItem.
 */
public class UntagController implements Controller {
    
    private static final String NAME = "Untag";
    private static final String DESCRIPTION = "Untag a task/event by listed index";
    public static final String COMMAND_SYNTAX = "untag <index> <tag name>";
    private static final String COMMAND_WORD = "untag";
    
    public static final String UNTAG_FORMAT = "untag %d";
    public static final String MESSAGE_UNTAG_SUCCESS = "Item has been untagged successfully.";
    public static final String MESSAGE_INDEX_OUT_OF_RANGE = "Could not untag task/event: Invalid index provided!";
    public static final String MESSAGE_MISSING_INDEX_AND_TAG_NAME = "Please specify the index of the item and the tag name to untag.";
    public static final String MESSAGE_INDEX_NOT_NUMBER = "Index has to be a number!";
    public static final String MESSAGE_TAG_NAME_NOT_FOUND = "Could not untag task/event: Tag name not provided!";
    public static final String MESSAGE_TAG_NAME_DOES_NOT_EXIST = "Could not untag task/event: Tag name does not exist!";
    public static final String MESSAGE_TAG_NAME_EXIST = "Could not untag task/event : Tag name does not exist or Duplicate Tag name detected!";
    
    private static final int COMMAND_INPUT_INDEX = 0;
    private static final int ITEM_INDEX = 0;
    private static final int TOKENIZER_DEFAULT_INDEX = 1;
    
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
        return tokenDefinitions;
    }

    @Override
    public void process(String input) throws ParseException {
        
        Map<String, String[]> parsedResult;
        parsedResult = Tokenizer.tokenize(getTokenDefinitions(), input);
        
        String param = parsedResult.get(Tokenizer.DEFAULT_TOKEN)[TOKENIZER_DEFAULT_INDEX];
        
        if (param == null) {
            Renderer.renderDisambiguation(COMMAND_SYNTAX, MESSAGE_MISSING_INDEX_AND_TAG_NAME);
            return;
        }
        
        String[] parameters = StringUtil.splitStringBySpace(param);
        
        // Get index.
        int index = 0;
        String tagNames = null;
        try {
            index = Integer.decode(parameters[ITEM_INDEX]);
            tagNames = param.replaceFirst(parameters[ITEM_INDEX], "").trim();
        } catch (NumberFormatException e) {
            Renderer.renderDisambiguation(COMMAND_SYNTAX, MESSAGE_INDEX_NOT_NUMBER);
            return;
        }
        
        // Get record
        EphemeralDB edb = EphemeralDB.getInstance();
        CalendarItem calendarItem = edb.getCalendarItemsByDisplayedId(index);
        TodoListDB db = TodoListDB.getInstance();
        
        String[] parsedTagNames = parseTags(tagNames);
        
        if (isErrorCommand(parameters, index, calendarItem, parsedTagNames)) {
            return; // Break out once error
        }
               
        boolean resultOfUntagging = removingTagNames(parsedTagNames, calendarItem);
        
        // Re-render
        if (resultOfUntagging) {
            db.updateTagList(parsedTagNames);
            db.save();
            Renderer.renderSelectedIndex(db, MESSAGE_UNTAG_SUCCESS, edb.getAllDisplayedTasks(), edb.getAllDisplayedEvents());
        } else {
            Renderer.renderDisambiguation(String.format(UNTAG_FORMAT, index), MESSAGE_TAG_NAME_DOES_NOT_EXIST);
        }
    }
    
    /*
     * To be used to check if there are any command syntax errors
     * 
     * @return true if error detected, else false.
     */
    private boolean isErrorCommand(String[] parameters, int index, CalendarItem calendarItem, String[] parsedTagNames) {
        // Check if index is out of range
        if (calendarItem == null) {
            Renderer.renderDisambiguation(String.format(UNTAG_FORMAT, index), MESSAGE_INDEX_OUT_OF_RANGE);
            return true;
        }
        
        // Check if tag name is provided
        if (parameters.length <= 1) {
            Renderer.renderDisambiguation(COMMAND_SYNTAX, MESSAGE_TAG_NAME_NOT_FOUND);
            return true;
        }
        
        // Check if tag name exist
        if (tagNameDoesNotExist(parsedTagNames, calendarItem)) {
            Renderer.renderDisambiguation(String.format(UNTAG_FORMAT, index), MESSAGE_TAG_NAME_EXIST);
            return true;
        }
        return false;
    }
    
    /*
     * To be used to remove tag from the tag list that belong to the CalendarItem
     * @param parsedTagNames
     *                     tag names that are entered by user and not duplicate and belong to the calendarItem
     * @param calendarItem                    
     *                     can be task or event
     *                     
     * @return true if all tags have been removed successfully, false if one of the tags its not removed successfully                     
     * */
    private boolean removingTagNames(String[] parsedTagNames, CalendarItem calendarItem) {
        assert parsedTagNames != null;
        assert calendarItem != null;
        
        boolean result = true;
        for (int i = 0; i < parsedTagNames.length; i ++) {
            result = calendarItem.removeTag(parsedTagNames[i].trim()) & result;
        }
        return result;
    }
    
    /*
     * To be used to check if user enter any duplicate tag name and if calendarItem has the exact tag name
     * @param parsedTagNames
     *                   tag names that has been split into an array
     * @param calendarItem
     *                   calendarItem that can be either a task or event
     * 
     * @return true if tag name does not exist or is entered more than once, false if it exist in the tag list
     */
    private boolean tagNameDoesNotExist(String[] parsedTagNames, CalendarItem calendarItem) {
        HashSet<String> parsedTagNamesList = new HashSet<String>();
        for (int i = 0; i < parsedTagNames.length; i ++) {
            // Checking with overall tag list in db
            if (!calendarItem.getTagList().contains(parsedTagNames[i].trim())) {
                return true;
            }
            
            // Checking with the current array, if there are any duplicate tag names
            parsedTagNamesList.add(parsedTagNames[i]);
        }
        return parsedTagNamesList.size() != parsedTagNames.length;
    }

    /*
     * To be used to split tag names by comma if more than one is entered
     * @param tags
     *           tag names that is entered
     *           
     * @return an array of tag name that is split by comma
     */
    private String [] parseTags(String tags) {
        return tags.split(",");
    }

}
