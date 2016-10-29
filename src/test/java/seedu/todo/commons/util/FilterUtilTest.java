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
        fail("Not yet implemented");
    }

    @Test
    public void testFilterCompletedTaskList() {
        fail("Not yet implemented");
    }

    @Test
    public void testFilterIncompletedTaskList() {
        fail("Not yet implemented");
    }

    @Test
    public void testFilterTaskBySingleDate() {
        fail("Not yet implemented");
    }

    @Test
    public void testFilterTaskWithDateRange() {
        fail("Not yet implemented");
    }

    @Test
    public void testFilterEventByNames() {
        fail("Not yet implemented");
    }

    @Test
    public void testFilterEventByTags() {
        fail("Not yet implemented");
    }

    @Test
    public void testFilterIsOverEventList() {
        fail("Not yet implemented");
    }

    @Test
    public void testFilterCurrentEventList() {
        fail("Not yet implemented");
    }

    @Test
    public void testFilterEventBySingleDate() {
        fail("Not yet implemented");
    }

    @Test
    public void testFilterEventWithDateRange() {
        fail("Not yet implemented");
    }

}
