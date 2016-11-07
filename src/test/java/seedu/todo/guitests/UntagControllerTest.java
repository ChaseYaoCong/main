package seedu.todo.guitests;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import seedu.todo.commons.util.DateUtil;
import seedu.todo.controllers.UntagController;
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
    
    //@@author A0139922Y-reused
    @Before
    public void initFixtures() {
        console.runCommand("clear");
        assertTaskVisibleAfterCmd(commandAdd, task);
    }
    
    //@@author A0139922Y-reused
    @Test
    public void fixtures_test() {
        console.runCommand("clear");
        assertTaskNotVisibleAfterCmd("list", task);
    }
    //@@author
    
    //@@author A0139922Y
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
        int tag_index = 1;
        String expectedDisambiguationForConsoleInput = String.format(UntagController.UNTAG_FORMAT, tag_index);
        assertEquals(console.getConsoleInputText(), expectedDisambiguationForConsoleInput);
        
        // For console text area to check error message
        String expectedDisambiguationForConsoleTextArea = formatConsoleOutputTextArea(UntagController.MESSAGE_TAG_NAME_EXIST);
        assertEquals(console.getConsoleTextArea(), expectedDisambiguationForConsoleTextArea);
    }
    
    @Test
    public void untag_without_name() {
        // To run command
        String command = "untag 1";
        console.runCommand(command);
        
        // For console input 
        String expectedDisambiguationForConsoleInput = UntagController.COMMAND_SYNTAX;
        assertEquals(console.getConsoleInputText(), expectedDisambiguationForConsoleInput);
        
        // For console text area to check error message
        String expectedDisambiguationForConsoleTextArea = formatConsoleOutputTextArea(UntagController.MESSAGE_TAG_NAME_NOT_FOUND);
        assertEquals(console.getConsoleTextArea(), expectedDisambiguationForConsoleTextArea);
    }
    
    @Test
    public void untag_without_index() {
        // To run command
        String command = "untag";
        console.runCommand(command);
        
        // For console input 
        String expectedDisambiguationForConsoleInput = UntagController.COMMAND_SYNTAX;
        assertEquals(console.getConsoleInputText(), expectedDisambiguationForConsoleInput);
        
        // For console text area to check error message
        String expectedDisambiguationForConsoleTextArea = formatConsoleOutputTextArea(UntagController.MESSAGE_MISSING_INDEX_AND_TAG_NAME);
        assertEquals(console.getConsoleTextArea(), expectedDisambiguationForConsoleTextArea);
    }
    
    @Test
    public void untag_with_invalid_index() {
        // To run command
        String command = "untag 2";
        console.runCommand(command);
        
        // For console input 
        int index_out_of_range = 2;
        String expectedDisambiguationForConsoleInput = String.format(UntagController.UNTAG_FORMAT, index_out_of_range);
        assertEquals(console.getConsoleInputText(), expectedDisambiguationForConsoleInput);
        
        // For console text area to check error message
        String expectedDisambiguationForConsoleTextArea = formatConsoleOutputTextArea(UntagController.MESSAGE_INDEX_OUT_OF_RANGE);
        assertEquals(console.getConsoleTextArea(), expectedDisambiguationForConsoleTextArea);
    }
}
