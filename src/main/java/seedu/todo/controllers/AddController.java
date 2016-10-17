package seedu.todo.controllers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.joestelmach.natty.*;

import seedu.todo.commons.exceptions.UnmatchedQuotesException;
import seedu.todo.models.Event;
import seedu.todo.models.Task;
import seedu.todo.models.TodoListDB;
import seedu.todo.ui.UiManager;
import seedu.todo.ui.views.IndexView;

/**
 * Controller to add an event or task.
 * 
 * @author louietyj
 *
 */
public class AddController implements Controller {
    
    private static String NAME = "Add";
    private static String DESCRIPTION = "Adds a task / event to the to-do list.";
    private static String COMMAND_SYNTAX = "add <task> by <deadline> || add <event> at <time>";
    
    private static final String MESSAGE_ADD_SUCCESS = "Item successfully added!";
    
    private static CommandDefinition commandDefinition =
            new CommandDefinition(NAME, DESCRIPTION, COMMAND_SYNTAX); 

    public static CommandDefinition getCommandDefinition() {
        return commandDefinition;
    }

    @Override
    public float inputConfidence(String input) {
        // TODO
        return (input.startsWith("add")) ? 1 : 0;
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
        tokenDefinitions.put("default", new String[] {"add"});
        tokenDefinitions.put("eventType", new String[] { "event", "task" });
        tokenDefinitions.put("time", new String[] { "at", "by", "on", "before", "time" });
        tokenDefinitions.put("timeFrom", new String[] { "from" });
        tokenDefinitions.put("timeTo", new String[] { "to" });
        tokenDefinitions.put("tag", new String[] { "tag" });
        return tokenDefinitions;
    }

    @Override
    public void process(String input) {
        
        Map<String, String[]> parsedResult;
        try {
            parsedResult = Tokenizer.tokenize(getTokenDefinitions(), input);            
        } catch (UnmatchedQuotesException e) {
            System.out.println("Unmatched quote!");
            return;
        }
        
        // Task or event?
        boolean isTask = parseIsTask(parsedResult);
        
        // Name
        String name = parseName(parsedResult);
        String tagName = parseTagName(parsedResult);
        
        // Time
        String[] naturalDates = parseDates(parsedResult);
        String naturalFrom = naturalDates[0];
        String naturalTo = naturalDates[1];
        
        // Validate isTask, name and times.
        if (validateParams(isTask, name, naturalFrom, naturalTo)) {
            renderDisambiguation(parsedResult);
            return;
        }
        
        // Parse natural date using Natty.
        LocalDateTime dateFrom = naturalFrom == null ? null : parseNatural(naturalFrom); 
        LocalDateTime dateTo = naturalTo == null ? null : parseNatural(naturalTo);
        
        // Create and persist task / event.
        TodoListDB db = TodoListDB.getInstance();
        createCalendarItem(db, isTask, name, dateFrom, dateTo, tagName);
        
        // Re-render
        renderIndex(db);
    }

    private String parseTagName(Map<String, String[]> parsedResult) {
        if (parsedResult.get("tag") != null) {
            //TODO : if we support more than 1 tag
            return parsedResult.get("tag")[1];
        } else {
            return null;
        }

    }

    /**
     * Renders the indexView.
     * 
     * @param db
     */
    private void renderIndex(TodoListDB db) {
        IndexView view = UiManager.loadView(IndexView.class);
        view.tasks = db.getAllTasks();
        view.events = db.getAllEvents();
        UiManager.renderView(view);
        UiManager.updateConsoleMessage(MESSAGE_ADD_SUCCESS);
    }

    /**
     * Creates and persists a CalendarItem to the DB.
     * 
     * @param db
     *            TodoListDB object
     * @param isTask
     *            true if CalendarItem should be a Task, false if Event
     * @param name
     *            Display name of CalendarItem object
     * @param dateFrom
     *            Due date for Task or start date for Event
     * @param dateTo
     *            End date for Event
     * @param tagName
     *            Display name of tag for CalendarItem object          
     */
    private void createCalendarItem(TodoListDB db, 
            boolean isTask, String name, LocalDateTime dateFrom, LocalDateTime dateTo, String tagName) {
        if (isTask) {
            Task newTask = db.createTask();
            newTask.setName(name);
            newTask.setDueDate(dateFrom);
            newTask.setTag(tagName);
        } else {
            Event newEvent = db.createEvent();
            newEvent.setName(name);
            newEvent.setStartDate(dateFrom);
            newEvent.setEndDate(dateTo);
            newEvent.setTag(tagName);
        }
        db.save();
    }

    
    /**
     * Validates the parsed parameters.
     * 
     * <ul>
     * <li>Fail if name is null.</li>
     * <li>Fail if "to" exists without "from"</li>
     * <li>Fail if task, but "from" and "to" exist</li>
     * </ul>
     * 
     * @param isTask
     *            true if CalendarItem should be a Task, false if Event
     * @param name
     *            Display name of CalendarItem object
     * @param naturalFrom
     *            Raw input for due date for Task or start date for Event
     * @param naturalTo
     *            Raw input for end date for Event
     * @return true if validation passed, false otherwise
     */
    private boolean validateParams(boolean isTask, String name, String naturalFrom, String naturalTo) {
        return (name == null ||
                (naturalFrom == null && naturalTo != null) || (isTask && naturalTo != null));
    }
    
    /**
     * Extracts the natural dates from parsedResult.
     * 
     * @param parsedResult
     * @return { naturalFrom, naturalTo }
     */
    private String[] parseDates(Map<String, String[]> parsedResult) {
        String naturalFrom = null;
        String naturalTo = null;
        setTime: {
            if (parsedResult.get("time") != null && parsedResult.get("time")[1] != null) {
                naturalFrom = parsedResult.get("time")[1];
                break setTime;
            }
            if (parsedResult.get("timeFrom") != null && parsedResult.get("timeFrom")[1] != null)
                naturalFrom = parsedResult.get("timeFrom")[1];
            if (parsedResult.get("timeTo") != null && parsedResult.get("timeTo")[1] != null)
                naturalTo = parsedResult.get("timeTo")[1];
        }
        return new String[] { naturalFrom, naturalTo };
    }

    /**
     * Extracts the display name of the CalendarItem from parsedResult.
     * 
     * @param parsedResult
     * @return name
     */
    private String parseName(Map<String, String[]> parsedResult) {
        String name = null;
        if (parsedResult.get("default") != null && parsedResult.get("default")[1] != null)
            name = parsedResult.get("default")[1];
        if (parsedResult.get("eventType") != null && parsedResult.get("eventType")[1] != null)
            name = parsedResult.get("eventType")[1];
        return name;
    }

    /**
     * Extracts the intended CalendarItem type from parsedResult.
     * 
     * @param parsedResult
     * @return true if Task, false if Event
     */
    private boolean parseIsTask(Map<String, String[]> parsedResult) {
        boolean isTask = true;
        if (parsedResult.get("eventType") != null && parsedResult.get("eventType")[0].equals("event"))
            isTask = false;
        return isTask;
    }

    /**
     * Parse a natural date into a LocalDateTime object.
     * 
     * @param natural
     * @return LocalDateTime object
     */
    private LocalDateTime parseNatural(String natural) {
        Parser parser = new Parser();
        List<DateGroup> groups = parser.parse(natural);
        Date date = null;
        try {
            date = groups.get(0).getDates().get(0);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Error!"); // TODO
            return null;
        }
        LocalDateTime ldt = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        return ldt;
    }
    
    private void renderDisambiguation(Map<String, String[]> parsedResult) {
        System.out.println("Disambiguate!");
    }
    
}
