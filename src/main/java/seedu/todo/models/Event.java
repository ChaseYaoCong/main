package seedu.todo.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * @@author A0093907W
 * 
 * Event model
 */
public class Event implements CalendarItem {
    
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private ArrayList<String> tagList = new ArrayList<String>();
    
    private static final int MAX_TAG_LIST_SIZE = 20;
    
    /**
     * Get the start date of an Event.
     * @return startDate
     */
    public LocalDateTime getStartDate() {
        return startDate;
    }

    /**
     * Set the start date of an Event.
     * @param startDate
     */
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    /**
     * Get the end date of an Event.
     * @return endDate
     */
    public LocalDateTime getEndDate() {
        return endDate;
    }

    /**
     * Set the end date of an Event.
     * @param endDate
     */
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public LocalDateTime getCalendarDateTime() {
        return getStartDate(); 
    }

    @Override
    public void setCalendarDateTime(LocalDateTime datetime) {
        setStartDate(datetime);
    }
    
    @Override
    public boolean isOver() {
        if (endDate == null) {
            return false;
        } else {
            return endDate.isBefore(LocalDateTime.now());
        }
    }

    /**
     * @@author A0139922Y
     * Return the tag list that belong to the calendar item
     * 
     */
    @Override
    public ArrayList<String> getTagList() {
        return tagList;
    }

    /**
     * @@author A0139922Y
     * Adding the tag into the tag list that belong to the calendar item
     * @param tagName
     *               name of the tag
     *               
     * @return true tag name is successfully added, false if tag list if full         
     */
    @Override
    public boolean addTag(String tagName) {
        if(tagList.size() < MAX_TAG_LIST_SIZE) {
            tagList.add(tagName);
            return true;
        } else {
            return false;
        }
    }

    /**
     * @@author A0139922Y
     * Removing the tag from the tag list that belong to the calendar item
     * @param tagName
     *               name of the tag
     *               
     * @return true tag name is successfully removed, false if tag name does not exist    
     */
    @Override
    public boolean removeTag(String tagName) {
        return tagList.remove(tagName);
    }
    
    /**
     * @@author A0093907W
     * 
     * Filtering methods intended to replace hacky one-filter-method-per-permutation from Yaocong.
     * Seriously, why??!!
     */
    public static List<Event> where(List<Predicate<Event>> predicates) {
        List<Event> result = TodoListDB.getInstance().getAllEvents();
        for (Predicate<Event> predicate : predicates) {
            filter(predicate, result);
        }
        return result;
    }
    
    public static Predicate<Event> predByName(String name) {
        return (Event event) -> Pattern.compile(String.format("\\b%s", name), Pattern.CASE_INSENSITIVE)
                .matcher(event.getName()).find();
    }
    
    public static Predicate<Event> predStartBefore(LocalDateTime date) {
        return (Event event) -> event.getStartDate().isBefore(date);
    }
    
    public static Predicate<Event> predStartAfter(LocalDateTime date) {
        return (Event event) -> event.getStartDate().isAfter(date);
    }
    
    public static Predicate<Event> predEndBefore(LocalDateTime date) {
        return (Event event) -> event.getEndDate().isBefore(date);
    }
    
    public static Predicate<Event> predEndAfter(LocalDateTime date) {
        return (Event event) -> event.getEndDate().isAfter(date);
    }
    
    public static Predicate<Event> predTag(String tag) {
        return (Event event) -> {
            for (String currTag : event.getTagList()) {
                if (currTag.toLowerCase().equals(tag.toLowerCase())) {
                    return true;
                }
            }
            return false;
        };
    }
    
    public static void filter(Predicate<Event> predicate, List<Event> eventList) {
        for (int i = eventList.size() - 1; i >= 0; i--) {
            if (!predicate.test(eventList.get(i))) {
                eventList.remove(i);
            }
        }
    }

    /**
     * @@author A0139922Y
     * Returning the maximum tag list size that is allowed
     *               
     */
    @Override
    public int getTagListLimit() {
        return MAX_TAG_LIST_SIZE;
    }

}
