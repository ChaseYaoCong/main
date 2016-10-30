package seedu.todo.commons.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;

import seedu.todo.models.Task;

public class FilterUtilTest {
    
    @Test
    public void testFilterTaskByNames_filter_correctly() {
        List<Task> tasks = new ArrayList<Task>();
        HashSet<String> nameList = new HashSet<String>();
        assertEquals(tasks, FilterUtil.filterTaskByNames(tasks, nameList));
    }

    @Test
    public void testFilterTaskByTags() {
        //TODO
    }

    @Test
    public void testFilterCompletedTaskList() {
        //TODO
    }

    @Test
    public void testFilterIncompletedTaskList() {
        //TODO
    }

    @Test
    public void testFilterTaskBySingleDate() {
        //TODO
    }

    @Test
    public void testFilterTaskWithDateRange() {
        //TODO
    }

    @Test
    public void testFilterEventByNames() {
        //TODO
    }

    @Test
    public void testFilterEventByTags() {
        //TODO
    }

    @Test
    public void testFilterIsOverEventList() {
        //TODO
    }

    @Test
    public void testFilterCurrentEventList() {
        //TODO        
    }

    @Test
    public void testFilterEventBySingleDate() {
        //TODO
    }

    @Test
    public void testFilterEventWithDateRange() {
        //TODO
    }

}
