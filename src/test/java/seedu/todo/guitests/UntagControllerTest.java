package seedu.todo.guitests;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import seedu.todo.commons.util.DateUtil;
import seedu.todo.controllers.concerns.Renderer;
import seedu.todo.models.Task;

/*
 * @@author A0139922Y
 */
public class UntagControllerTest extends GuiTest {
    // Date variables to be use to initialise DB
    private static final LocalDateTime TODAY = LocalDateTime.now();
    private static final String TODAY_STRING = DateUtil.formatDate(TODAY);
    private static final String TODAY_ISO_STRING = DateUtil.formatIsoDate(TODAY);
    
    // Command to be use to initialise DB
    private String commandAdd = String.format("add task Buy Coco by \"%s 8pm\"", TODAY_STRING);
    private Task task = new Task();
    private Task taskWithoutTag = new Task();
    
    // Set up DB
    public UntagControllerTest() {
        task.setName("Buy Coco");
        task.setCalendarDateTime(DateUtil.parseDateTime(
                String.format("%s 20:00:00", TODAY_ISO_STRING)));
        task.addTag("personal");
        taskWithoutTag.setName("Buy Coco");
        taskWithoutTag.setCalendarDateTime(DateUtil.parseDateTime(
                String.format("%s 20:00:00", TODAY_ISO_STRING)));
    }
    
    // Re-use
    @Before
    public void initFixtures() {
        console.runCommand("clear");
        assertTaskVisibleAfterCmd(commandAdd, task);
    }
    
    // Re-use
    @Test
    public void fixtures_test() {
        console.runCommand("clear");
        assertTaskNotVisibleAfterCmd("list", task);
    }
    
    @Test
    public void untag_succesfully() {
        console.runCommand("tag 1 personal");
        String command = "untag 1 personal";
        assertTaskTagNotVisibleAfterCmd(command, taskWithoutTag);
    }
    
    @Test
    public void untag_task_with_invalid_name() {
        // To run command
        console.runCommand("tag 1 test");
        String command = "untag 1 personal";
        console.runCommand(command);
        
        // For console input 
        String expectedDisambiguationForConsoleInput = "untag 1";
        assertEquals(console.getConsoleInputText(), expectedDisambiguationForConsoleInput);
        
        // For console text area to check error message
        String tag_controller_message = "Could not untag task/event : Tag name does not exist or Duplicate Tag name detected!";
        String expectedDisambiguationForConsoleTextArea = Renderer.MESSAGE_DISAMBIGUATE + "\n\n" + tag_controller_message;
        assertEquals(console.getConsoleTextArea(), expectedDisambiguationForConsoleTextArea);
    }
    
    @Test
    public void untag_without_name() {
        // To run command
        String command = "untag 1";
        console.runCommand(command);
        
        // For console input 
        String expectedDisambiguationForConsoleInput = "untag <index> <tag name>";
        assertEquals(console.getConsoleInputText(), expectedDisambiguationForConsoleInput);
        
        // For console text area to check error message
        String tag_controller_message = "Could not untag task/event: Tag name not provided!";
        String expectedDisambiguationForConsoleTextArea = Renderer.MESSAGE_DISAMBIGUATE + "\n\n" + tag_controller_message;
        assertEquals(console.getConsoleTextArea(), expectedDisambiguationForConsoleTextArea);
    }
    
    @Test
    public void untag_without_index() {
        // To run command
        String command = "untag";
        console.runCommand(command);
        
        // For console input 
        String expectedDisambiguationForConsoleInput = "untag <index> <tag name>";
        assertEquals(console.getConsoleInputText(), expectedDisambiguationForConsoleInput);
        
        // For console text area to check error message
        String tag_controller_message = "Please specify the index of the item and the tag name to untag.";
        String expectedDisambiguationForConsoleTextArea = Renderer.MESSAGE_DISAMBIGUATE + "\n\n" + tag_controller_message;
        assertEquals(console.getConsoleTextArea(), expectedDisambiguationForConsoleTextArea);
    }
    
    @Test
    public void untag_with_invalid_index() {
        // To run command
        String command = "untag 2";
        console.runCommand(command);
        
        // For console input 
        String expectedDisambiguationForConsoleInput = "untag 2";
        assertEquals(console.getConsoleInputText(), expectedDisambiguationForConsoleInput);
        
        // For console text area to check error message
        String tag_controller_message = "Could not untag task/event: Invalid index provided!";
        String expectedDisambiguationForConsoleTextArea = Renderer.MESSAGE_DISAMBIGUATE + "\n\n" + tag_controller_message;
        assertEquals(console.getConsoleTextArea(), expectedDisambiguationForConsoleTextArea);
    }
}
