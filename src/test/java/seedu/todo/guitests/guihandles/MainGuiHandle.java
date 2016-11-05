package seedu.todo.guitests.guihandles;

import javafx.stage.Stage;
import seedu.todo.TestApp;
import seedu.todo.guitests.GuiRobot;

/**
 * @@author A0139812A
 */
public class MainGuiHandle extends GuiHandle {
    
    public MainGuiHandle(GuiRobot guiRobot, Stage primaryStage) {
        super(guiRobot, primaryStage, TestApp.APP_TITLE);
    }
    
    public ConsoleHandle getConsole() {
        return new ConsoleHandle(guiRobot, primaryStage, TestApp.APP_TITLE);
    }
    
    public TaskListHandle getTaskList() {
        return new TaskListHandle(guiRobot, primaryStage, TestApp.APP_TITLE);
    }
    
    public SideBarHandle getSideBar() {
        return new SideBarHandle(guiRobot, primaryStage, TestApp.APP_TITLE);
    }
}
