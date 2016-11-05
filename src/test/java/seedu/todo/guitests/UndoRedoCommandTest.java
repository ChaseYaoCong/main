package seedu.todo.guitests;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import seedu.todo.commons.util.DateUtil;
import seedu.todo.models.Task;
import seedu.todo.models.TodoListDB;

/**
 * @@author A0093907W
 */
public class UndoRedoCommandTest extends GuiTest {

    private final LocalDateTime oneDayFromNow = LocalDateTime.now().plusDays(1);
    private final String oneDayFromNowString = DateUtil.formatDate(oneDayFromNow);
    private final String oneDayFromNowIsoString = DateUtil.formatIsoDate(oneDayFromNow);
    private final LocalDateTime twoDaysFromNow = LocalDateTime.now().plusDays(2);
    private final String twoDaysFromNowString = DateUtil.formatDate(twoDaysFromNow);
    private final String twoDaysFromNowIsoString = DateUtil.formatIsoDate(twoDaysFromNow);
    
    String commandAdd1 = String.format("add task Buy KOI by \"%s 8pm\"", oneDayFromNowString);
    Task task1 = new Task();
    String commandAdd2 = String.format("add task Buy Milk by \"%s 9pm\"", twoDaysFromNowString);
    Task task2 = new Task();
    
    public UndoRedoCommandTest() {
        task1.setName("Buy KOI");
        task1.setCalendarDateTime(DateUtil.parseDateTime(String.format("%s 20:00:00", oneDayFromNowIsoString)));
        task2.setName("Buy Milk");
        task2.setDueDate(DateUtil.parseDateTime(String.format("%s 21:00:00", twoDaysFromNowIsoString)));
    }
    
    @Before
    public void resetDB() {
        TodoListDB db = TodoListDB.getInstance();
        db.destroyAllEvent();
        db.destroyAllTask();
    }
    
    @Test
    public void undo_single() {
        console.runCommand("clear");
        assertTaskVisibleAfterCmd(commandAdd1, task1);
        assertTaskVisibleAfterCmd(commandAdd2, task2);
        assertTaskNotVisibleAfterCmd("undo", task2);
    }
    
    @Test
    public void undo_multiple() {
        console.runCommand("clear");
        assertTaskVisibleAfterCmd(commandAdd1, task1);
        assertTaskVisibleAfterCmd(commandAdd2, task2);
        assertTaskNotVisibleAfterCmd("undo 2", task1);
        assertTaskNotVisibleAfterCmd("list", task2); // A li'l hacky but oh well
    }
    
    @Test
    public void undo_notavailable() {
        console.runCommand("clear");
        assertTaskVisibleAfterCmd(commandAdd1, task1);
        assertTaskNotVisibleAfterCmd("undo", task1);
        console.runCommand("undo");
        console.runCommand("undo");
        assertEquals(console.getConsoleTextArea(), "There is no command to undo!");
    }
    
    @Test
    public void undo_multiple_notavailable() {
        console.runCommand("clear");
        console.runCommand("undo 2");
        assertEquals(console.getConsoleTextArea(), "We cannot undo 2 commands! At most, you can undo 1 command.");
    }
    
    @Test
    public void redo_single() {
        console.runCommand("clear");
        assertTaskVisibleAfterCmd(commandAdd1, task1);
        assertTaskNotVisibleAfterCmd("undo", task1);
        assertTaskVisibleAfterCmd("redo", task1);
    }
    
    @Test
    public void redo_multiple() {
        console.runCommand("clear");
        assertTaskVisibleAfterCmd(commandAdd1, task1);
        assertTaskVisibleAfterCmd(commandAdd2, task2);
        assertTaskNotVisibleAfterCmd("undo 2", task1);
        assertTaskNotVisibleAfterCmd("list", task2);
        assertTaskVisibleAfterCmd("redo 2", task1);
        assertTaskVisibleAfterCmd("list", task2);
    }
    
    @Test
    public void redo_notavailable() {
        console.runCommand("clear");
        console.runCommand("redo");
        assertEquals(console.getConsoleTextArea(), "There is no command to redo!");
    }
    
    @Test
    public void redo_multiple_notavailable() {
        console.runCommand("clear");
        console.runCommand("undo");
        console.runCommand("redo 2");
        assertEquals(console.getConsoleTextArea(), "We cannot redo 2 commands! At most, you can redo 1 command.");
    }
}
