package seedu.todo.model;

import java.util.*;
import java.util.function.*;
import seedu.todo.commons.exceptions.*;

/**
 * @author louietyj
 *
 */
public class ActiveRecordBase {
    public static Set<ActiveRecordBase> records = new HashSet<ActiveRecordBase>();
    
    public ActiveRecordBase() {
        records.add(this);
    }
	
    public static boolean save() {
        return true;
    }
    
    public boolean destroy() {
        return records.remove(this);
    }
    
    public boolean validate() {
        return true;
    }

}
