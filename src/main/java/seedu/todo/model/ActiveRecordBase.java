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
	
    public static List<ActiveRecordBase> where(Function<ActiveRecordBase, Boolean> pred) {
        List<ActiveRecordBase> result = new ArrayList<ActiveRecordBase>();
        for (ActiveRecordBase record : records) {
            if (pred.apply(record))
                result.add(record);
        }
        return result;
    }
    
    public static ActiveRecordBase find(Function<ActiveRecordBase, Boolean> pred) throws RecordNotFoundException {
        for (ActiveRecordBase record : records) {
            if (pred.apply(record))
                return record;
        }
        throw new RecordNotFoundException();
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
