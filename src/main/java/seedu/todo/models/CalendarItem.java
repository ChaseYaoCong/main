package seedu.todo.models;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * @@author A0093907W
 * 
 * CalendarItem interface
 */
public interface CalendarItem {
    
    /**
     * Get the display name of the calendar item.
     * @return name
     */
    public String getName();
    
    /**
     * Set the display name of the calendar item.
     * @param name
     */
    public void setName(String name);

    /**
     * Get the calendar date of the calendar item. This is mostly for display
     * and sorting purposes.
     * 
     * @return datetime
     */
    public LocalDateTime getCalendarDateTime();
    
    /**
     * Set the calendar date of the calendar item. The behavior of this will
     * depend on the implementation, but it is guaranteed that the variable
     * being set by this method is the variable that is returned by
     * <code>getCalendarDT()</code>.<br>
     * 
     * It is unclear why one would ever set a calendar item's datetime using
     * this method, but it is here for completeness.
     * 
     * @param datetime
     */
    public void setCalendarDateTime(LocalDateTime datetime);
    
    /**
     * Returns true if the calendar item has passed, false otherwise.
     * 
     * @return isOver
     */
    public boolean isOver();
    
    /**
     * @@author A0139922Y
     * Returns the current tag list that belong to the CalendarItem, mainly for displaying purpose
     * 
     * @return ArrayList<String> tags
     */
    public ArrayList<String> getTagList();
   
    /**
     * @@author A0139922Y
     * Add a new tag in the list of tag of the calendar item. 
     * 
     * @param tagName <String>
     * @return true if it has not reached the max tag list size, false if tag list already reach the max size
     */
    public boolean addTag(String tagName);
    
    /**
     * @@author A0139922Y
     * Remove a existing tag in the tag list of tag of the calendar item. 
     * 
     * @param tagName <String>
     * @return true if tagName is removed successfully, false if failed to remove tagName due to unable to find
     */
    public boolean removeTag(String tagName);

    /**
     * @@author A0139922Y
     * Get the limit of the tag that is allowed for calendar item
     * 
     * @return the limit of tag list
     */    
    public int getTagListLimit();
}
