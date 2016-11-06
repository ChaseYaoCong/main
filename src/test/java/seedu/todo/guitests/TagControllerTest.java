package seedu.todo.guitests;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import seedu.todo.commons.util.DateUtil;
import seedu.todo.controllers.TagController;
import seedu.todo.models.Task;

/*
 * @@author A0139922Y
 */
public class TagControllerTest extends GuiTest {
    // Date variables to be use to initialise DB
    private static final LocalDateTime TODAY = LocalDateTime.now();
    private static final String TODAY_STRING = DateUtil.formatDate(TODAY);
    private static final String TODAY_ISO_STRING = DateUtil.formatIsoDate(TODAY);
    
    // Command to be use to initialise DB
    private String commandAdd = String.format("add task Buy Coco by \"%s 8pm\"", TODAY_STRING);
    private Task task = new Task();
    private Task taskWithoutTag = new Task();

    // Set up DB
    public TagControllerTest() {
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
    public void tag_succcessfully() {
        String command = "tag 1 personal";
        assertTaskTagVisibleAfterCmd(command, task);
        assertEquals(console.getConsoleTextArea(), TagController.MESSAGE_TAG_SUCCESS);
    }
    
    @Test
    public void tag_with_duplicated_names() {
        // To run the command
        String command = "tag 1 personal";
        
        assertTaskTagVisibleAfterCmd(command, task);
        console.runCommand(command);
        
        // For console input
        int tag_index = 1;
        String expectedDisambiguationForConsoleInput = String.format(TagController.TAG_FORMAT, tag_index);
        assertEquals(console.getConsoleInputText(), expectedDisambiguationForConsoleInput);
        
        // For console text area to check error message
        String expectedDisambiguationForConsoleTextArea = formatConsoleOutputTextArea(TagController.MESSAGE_TAG_NAME_EXIST);
        assertEquals(console.getConsoleTextArea(), expectedDisambiguationForConsoleTextArea);
    }
    
    @Test
    public void tag_when_tag_list_is_full() {
        taskWithoutTag = generateTags(taskWithoutTag);
        // To run Command
        String command = "tag 1 personal";
        
        assertTaskTagListFull(command, taskWithoutTag);
        
        // For console input 
        String expectedDisambiguationForConsoleInput = TagController.COMMAND_SYNTAX;
        assertEquals(console.getConsoleInputText(), expectedDisambiguationForConsoleInput);
        
        // For console text area to check error message
        String expectedDisambiguationForConsoleTextArea = formatConsoleOutputTextArea(TagController.MESSAGE_EXCEED_TAG_SIZE);
        assertEquals(console.getConsoleTextArea(), expectedDisambiguationForConsoleTextArea);
    }
    
    @Test
    public void tag_without_index() {
        // To run command
        String command = "tag";
        console.runCommand(command);
        
        // For console input 
        String expectedDisambiguationForConsoleInput = TagController.COMMAND_SYNTAX;
        assertEquals(console.getConsoleInputText(), expectedDisambiguationForConsoleInput);
        
        // For console text area to check error message
        String expectedDisambiguationForConsoleTextArea = formatConsoleOutputTextArea(TagController.MESSAGE_MISSING_INDEX_AND_TAG_NAME);
        assertEquals(console.getConsoleTextArea(), expectedDisambiguationForConsoleTextArea);
    }
    
    @Test
    public void tag_with_invalid_index_out_of_range() {
        // To run command
        String command = "tag 2";
        console.runCommand(command);
        
        // For console input 
        int tag_index_out_of_range = 2;
        String expectedDisambiguationForConsoleInput = String.format(TagController.TAG_FORMAT, tag_index_out_of_range);
        assertEquals(console.getConsoleInputText(), expectedDisambiguationForConsoleInput);
        
        // For console text area to check error message
        String expectedDisambiguationForConsoleTextArea = formatConsoleOutputTextArea(TagController.MESSAGE_INDEX_OUT_OF_RANGE);
        assertEquals(console.getConsoleTextArea(), expectedDisambiguationForConsoleTextArea);
    }
    
    @Test
    public void tag_with_invalid_index_as_alphabets() {
        // To run command
        String command = "tag personal";
        console.runCommand(command);
        
        // For console input 
        String expectedDisambiguationForConsoleInput = TagController.COMMAND_SYNTAX;
        assertEquals(console.getConsoleInputText(), expectedDisambiguationForConsoleInput);
        
        // For console text area to check error message
        String expectedDisambiguationForConsoleTextArea = formatConsoleOutputTextArea(TagController.MESSAGE_INDEX_NOT_NUMBER);
        assertEquals(console.getConsoleTextArea(), expectedDisambiguationForConsoleTextArea);
    }
    
    @Test
    public void tag_without_name() {
        // To run command
        String command = "tag 1";
        console.runCommand(command);
        
        // For console input 
        String expectedDisambiguationForConsoleInput = TagController.COMMAND_SYNTAX;
        assertEquals(console.getConsoleInputText(), expectedDisambiguationForConsoleInput);
        
        // For console text area to check error message
        String expectedDisambiguationForConsoleTextArea = formatConsoleOutputTextArea(TagController.MESSAGE_TAG_NAME_NOT_FOUND);
        assertEquals(console.getConsoleTextArea(), expectedDisambiguationForConsoleTextArea);
    }
    
    private Task generateTags(Task task) {
        for (int i = 0; i < task.getTagListLimit(); i ++) {
            console.runCommand("tag 1 " + Integer.toString(i));
            task.addTag(Integer.toString(i));
        }
        return task;
    }
}
