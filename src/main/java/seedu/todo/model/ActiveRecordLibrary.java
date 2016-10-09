package seedu.todo.model;

import java.io.*;
import java.util.*;
import java.util.function.*;
import seedu.todo.commons.exceptions.*;
import seedu.todo.commons.util.*;

public class ActiveRecordLibrary<E extends ActiveRecordBase> {
    private Class<E> type;
    private Set<E> records = new HashSet<E>();
    
    /**
     * Type-safe workaround to using reflection >_<
     * @param type
     */
    public ActiveRecordLibrary(Class<E> type) {
        this.type = type;
    }
    
    public boolean add(E record) {
        return records.add(record);
    }
    
    public List<E> all() {
        return new ArrayList<E>(records);
    }
    
    public List<E> where(Function<E, Boolean> pred) {
        List<E> result = new ArrayList<E>();
        for (E record : records) {
            if (pred.apply(record))
                result.add(record);
        }
        return result;
    }
    
    public E find(Function<E, Boolean> pred) throws RecordNotFoundException {
        for (E record : records) {
            if (pred.apply(record))
                return record;
        }
        throw new RecordNotFoundException();
    }
    
    public boolean save(E record) throws RecordInvalidException {
        if (record.validate())
            return true; // TODO
        throw new RecordInvalidException();
    }
    
    private File getStorageFile() {
        return new File(type.getSimpleName() + ".json");
    }
    
    public boolean save() {
        try {
            FileUtil.writeToFile(getStorageFile(), JsonUtil.toJsonString(records));
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    public boolean load() {
        try {
            records = JsonUtil.fromJsonString(FileUtil.readFromFile(getStorageFile()), records.getClass());
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    public boolean destroy(E record) {
        return records.remove(record);
    }

}
