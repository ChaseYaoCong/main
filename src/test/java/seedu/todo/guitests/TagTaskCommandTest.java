package seedu.todo.guitests;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import seedu.todo.models.Task;

/**
 * @@author Tiong YaoCong A0139922Y
 */
public class TagTaskCommandTest extends GuiTest {

    @Test
    public void tagTaskWithNewTagName() {
        
        ArrayList<String> tagList;
        String command; 
        Task task;
        
        //adding 1 tag
        task = new Task();
        String[] testCase1 = {"CS3216"};
        tagList = initializeTestCase(testCase1, task);
        command = "tag 1 CS3216";
        
        assertTaskTaggedSuccess(command, task, tagList);
        
        //adding multiple tags
        task = new Task();
        String[] testCase2 = {"CS3216" , "CS3217"};
        tagList = initializeTestCase(testCase2, task);
        command = "tag 1 CS3216, CS3217";
        assertTaskTaggedSuccess(command , task, tagList);
        
    }
    
    /*
     * Utility method for testing if task has been successfully tagged.
     * This runs a command and checks if TagList contains the list of tags
     * 
     * */
    private void assertTaskTaggedSuccess(String command, Task taskToCheck, ArrayList<String> tagList) {
        // Run the command in the console.
        console.runCommand(command);
        
        // Get the task tag list.
        ArrayList<String> taskTagList = taskToCheck.getTagList();

        // Check Task TagList contains the tag
        assertTrue(taskTagList.containsAll(tagList));
    }
    
    /**
     * Method to initialize test case
     * 
     * @@author Tiong YaoCong A0139922Y 
     */
    private ArrayList<String> initializeTestCase(String[] tagNameList, Task task) {
        ArrayList<String> result = new ArrayList<String>();
        for (int i = 0; i < tagNameList.length; i ++) {
            String tagName = tagNameList[i];
            task.addTag(tagName);
            result.add(tagName);
        }
        return result;
    }

}
