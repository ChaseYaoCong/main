package seedu.todo.commons.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Helper functions for handling strings.
 */
public class StringUtil {
    public static boolean containsIgnoreCase(String source, String query) {
        String[] split = source.toLowerCase().split("\\s+");
        List<String> strings = Arrays.asList(split);
        return strings.stream().filter(s -> s.equals(query.toLowerCase())).count() > 0;
    }

    /**
     * Returns a detailed message of the t, including the stack trace.
     */
    public static String getDetails(Throwable t){
        assert t != null;
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return t.getMessage() + "\n" + sw.toString();
    }

    /**
     * Returns true if s represents an unsigned integer e.g. 1, 2, 3, ... <br>
     *   Will return false for null, empty string, "-1", "0", "+1", and " 2 " (untrimmed) "3 0" (contains whitespace).
     * @param s Should be trimmed.
     */
    public static boolean isUnsignedInteger(String s){
        return s != null && s.matches("^0*[1-9]\\d*$");
    }
    
    public static String pluralizer(int num, String singular, String plural) {
    	return num == 1 ? singular : plural;
    }
    
    /**
     * Returns <code>string</code> if empty, <code>replaceString</code> otherwise.
     * @param string String to check
     * @param replaceString String to return if <code>string</code> is empty
     */
    public static String replaceEmpty(String string, String replaceString) {
        return (string == null || string.isEmpty()) ? replaceString : string;
    }
    
    /*
     * Format the display message depending on the number of tasks and events 
     * 
     * @param numTasks
     *          the number of tasks found 
     * @param numEvents
     *          the number of events found    
     *        
     * @return the display message for console message output           
     */
    public static String displayNumberOfTaskAndEventFoundWithPuralizer (int numTasks, int numEvents) {
        if (numTasks != 0 && numEvents != 0) {
            return String.format("%s and %s", formatNumberOfTaskWithPuralizer(numTasks), formatNumberOfEventWithPuralizer(numEvents));
        } else if (numTasks != 0) {
            return formatNumberOfTaskWithPuralizer(numTasks);
        } else if (numEvents != 0){
            return formatNumberOfEventWithPuralizer(numEvents);
        } else {
            return "No item found!";
        }
    }
    
    /*
     * Format the number of events found based on the events found
     * 
     *  @param numEvents 
     *          the number of events found
     */
    public static String formatNumberOfEventWithPuralizer (int numEvents) {
        return String.format("%d %s", numEvents, pluralizer(numEvents, "event", "events"));
    }
    
    /*
     * Format the number of tasks found based on the tasks found
     * 
     *  @param numTasks 
     *          the number of tasks found
     */
    public static String formatNumberOfTaskWithPuralizer (int numTasks) {
        return String.format("%d %s", numTasks, pluralizer(numTasks, "task", "tasks"));
    }
    
    /**
     * Returns <code>string</code> with aliased keys replaced with their
     * respective values. This method only matches complete words and not word
     * partials.
     * 
     * @param string
     * @param alias
     * @return
     */
    public static String replaceAliases(String string, Map<String, String> alias) {
        String newString = string;
        for (Map.Entry<String, String> entry : alias.entrySet()) {
            newString = newString.replaceAll(String.format("\\b%s\\b", entry.getKey()), entry.getValue());
        }
        return newString;
    }
    
    /**
     * Makes a best effort to sanitize input string.
     * 
     * @param alias     string to sanitize
     * @return          sanitized string
     */
    public static String sanitize(String alias) {
        return (alias == null) ? null : alias.replaceAll("[^A-Za-z]+", "");
    }
    
    public static String checkEmptyList(ArrayList<String> list) {
        return (list.size() == 0) ? "" : list.toString();
    }
    
    /*
     * Convert input into individual input by splitting with space  
     * 
     */
    public static String[] splitStringBySpace(String input) {
        return (input == null) ? null : input.trim().split(" ");
    }
}
