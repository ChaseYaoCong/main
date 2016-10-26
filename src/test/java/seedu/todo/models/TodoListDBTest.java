package seedu.todo.models;

import org.junit.*;
import static org.junit.Assert.*;

import java.time.LocalDateTime;

public class TodoListDBTest {
    
    @Test
    public void check_singleton() {
        TodoListDB one = TodoListDB.getInstance();
        TodoListDB two = TodoListDB.getInstance();
        assertEquals(one, two);
    }
    
    @Test
    public void test_aliases() {
        TodoListDB.getInstance().getAliases().put("key", "value");
        assertEquals(TodoListDB.getInstance().getAliases().get("key"), "value");
    }
    
    @Test
    public void test_CRUD_tasks() {
        TodoListDB.getInstance().destroyAllTask();
        Task task = TodoListDB.getInstance().createTask();
        assertNotEquals(TodoListDB.getInstance().getAllTasks().indexOf(task), -1);
    }
    
    @Test
    public void test_incomplete_tasks() {
        TodoListDB.getInstance().destroyAllTask();
        TodoListDB.getInstance().createTask();
        assertEquals(TodoListDB.getInstance().countIncompleteTasks(), 1);
    }
    
    @Test
    public void test_overdue_tasks() {
        TodoListDB.getInstance().destroyAllTask();
        Task task = TodoListDB.getInstance().createTask();
        task.setDueDate(LocalDateTime.now().minusSeconds(10));
        assertEquals(TodoListDB.getInstance().countOverdueTasks(), 1);
    }
    

}
