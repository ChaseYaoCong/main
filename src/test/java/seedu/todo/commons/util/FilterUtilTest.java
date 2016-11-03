package seedu.todo.commons.util;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;

import seedu.todo.models.Task;

//@@author A0139922Y
public class FilterUtilTest {
    public static final LocalDateTime today = LocalDateTime.now();
    public static final LocalDateTime tmr = LocalDateTime.now().plusDays(1);
    Task firstTestTask = generateFirstTestTask();
    Task secondTestTask = generateSecondTestTask();
    
    @Test
    public void testFilterTaskByNames_filter_equal() {
        List<Task> tasks = getEmptyTaskList();
        HashSet<String> nameList = new HashSet<String>();
        //empty task list and name list
        assertEquals(tasks, FilterUtil.filterTaskByNames(tasks, nameList));
        //empty task list
        nameList.add("Nothing");
        assertEquals(tasks, FilterUtil.filterTaskByNames(tasks, nameList));
        
        //filter out first test task
        nameList.add("Buy");
        tasks.add(firstTestTask);
        assertEquals(tasks, FilterUtil.filterTaskByNames(tasks, nameList));
        nameList.remove("Buy");
        nameList.add("Milk");
        assertEquals(tasks, FilterUtil.filterTaskByNames(tasks, nameList));
        tasks.add(secondTestTask);
        List<Task> filteredResult = getEmptyTaskList();
        filteredResult.add(firstTestTask);
        assertEquals(filteredResult, (FilterUtil.filterTaskByNames(tasks, nameList)));
        
        //empty task list with name list
        tasks.remove(firstTestTask);
        tasks.remove(secondTestTask);
        assertEquals(tasks, (FilterUtil.filterTaskByNames(tasks, nameList)));
    }
    
    @Test
    public void testFilterTaskByNames_filter_not_equal() {
        List<Task> tasks = getEmptyTaskList();
        HashSet<String> nameList = new HashSet<String>();
        tasks.add(firstTestTask);
        nameList.add("Nothing");
        assertNotEquals(tasks, FilterUtil.filterTaskByNames(tasks, nameList));
        
        nameList.add("Buy");
        assertNotEquals(getEmptyTaskList(), FilterUtil.filterTaskByNames(tasks, nameList));
    }

    @Test
    public void testFilterTaskByTags_equals() {
        List<Task> tasks = getEmptyTaskList();
        HashSet<String> nameList = new HashSet<String>();
        //empty task list and name list
        assertEquals(tasks, FilterUtil.filterTaskByTags(tasks, nameList));
        //empty task list
        nameList.add("Nothing");
        assertEquals(tasks, FilterUtil.filterTaskByTags(tasks, nameList));
        
        //filter out first test task
        nameList.add("personal");
        tasks.add(firstTestTask);
        assertEquals(tasks, FilterUtil.filterTaskByTags(tasks, nameList));
        
        //filter out both test tasks
        tasks.add(secondTestTask);
        nameList.add("CS2103");
        assertEquals(tasks, (FilterUtil.filterTaskByTags(tasks, nameList)));
        
        //empty task list with name list
        tasks.remove(firstTestTask);
        tasks.remove(secondTestTask);
        assertEquals(tasks, (FilterUtil.filterTaskByNames(tasks, nameList)));
    }
    
    @Test
    public void testFilterTaskByTags_not_equals() {
        List<Task> tasks = getEmptyTaskList();
        HashSet<String> nameList = new HashSet<String>();

        //filter out first test task
        nameList.add("Buy");
        tasks.add(firstTestTask);
        assertNotEquals(tasks, FilterUtil.filterTaskByTags(tasks, nameList));
        nameList.remove("Buy");
        nameList.add("Milk");
        assertNotEquals(tasks, FilterUtil.filterTaskByTags(tasks, nameList));
        tasks.add(secondTestTask);
        nameList.add("personal");
        assertNotEquals(tasks, (FilterUtil.filterTaskByTags(tasks, nameList)));
    }

    @Test
    public void testFilterCompletedTaskList_equals() {
        List<Task> tasks = getEmptyTaskList();
        assertEquals(tasks, FilterUtil.filterTasksByStatus(tasks, true));
        tasks.add(firstTestTask);
        tasks.add(secondTestTask);
        
        //filter out completed tasks
        List<Task> filteredTasks = getEmptyTaskList();
        filteredTasks.add(firstTestTask);
        assertEquals(filteredTasks, FilterUtil.filterTasksByStatus(tasks, true));
    }
    
    @Test
    public void testFilterCompletedTaskList_not_equals() {
        List<Task> tasks = getEmptyTaskList();
        tasks.add(firstTestTask);
        tasks.add(secondTestTask);
        
        //filter out completed tasks
        assertNotEquals(tasks, FilterUtil.filterTasksByStatus(tasks, true));
    }

    @Test
    public void testFilterIncompletedTaskList_equals() {
        List<Task> tasks = getEmptyTaskList();
        assertEquals(tasks, FilterUtil.filterTasksByStatus(tasks, false));
        tasks.add(firstTestTask);
        tasks.add(secondTestTask);
        
        //filter out incompleted tasks
        List<Task> filteredTasks = getEmptyTaskList();
        filteredTasks.add(secondTestTask);
        assertEquals(filteredTasks, FilterUtil.filterTasksByStatus(tasks, false));
    }
    
    @Test
    public void testFilterIncompletedTaskList_not_equals() {
        List<Task> tasks = getEmptyTaskList();
        tasks.add(firstTestTask);
        tasks.add(secondTestTask);
        
        //filter out incompleted tasks
        assertNotEquals(tasks, FilterUtil.filterTasksByStatus(tasks, false));
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
    
    private Task generateFirstTestTask() {
        Task task = new Task();
        task.setName("Buy Milk");
        task.setCalendarDT(today);
        task.addTag("personal");
        task.setCompleted();
        return task;
    }
    
    private Task generateSecondTestTask() {
        Task task = new Task();
        task.setName("CS2103");
        task.setCalendarDT(tmr);
        task.addTag("CS2103");
        return task;
    }
    
    private List<Task> getEmptyTaskList() {
        return new ArrayList<Task>();
    }
}
