package seedu.todo.guitests.guihandles;

import java.time.LocalDate;
import java.util.Optional;

import javafx.scene.Node;
import javafx.stage.Stage;
import seedu.todo.commons.util.DateUtil;
import seedu.todo.guitests.GuiRobot;
import seedu.todo.models.Event;
import seedu.todo.models.Task;

/**
 * @@author A0139812A
 */
public class TaskListDateItemHandle extends GuiHandle {

    private static final String TASKLISTDATEITEM_DATE_ID = "#dateLabel";
    private static final String TASKLISTTASKITEM_ID = "#taskListTaskItem";
    private static final String TASKLISTEVENTITEM_ID = "#taskListEventItem";
    private Node node;

    public TaskListDateItemHandle(GuiRobot guiRobot, Stage primaryStage, Node node){
        super(guiRobot, primaryStage, null);
        this.node = node;
    }
    
    /**
     * Gets the LocalDate parsed from the date label, which is in the date format {@code ddd dd MMM} (e.g. Thu 27 Oct).
     * If the date item is for floating tasks, it will return {@code NO_DATE_VALUE}.
     * 
     * @return Parsed LocalDate.
     */
    public LocalDate getDate() {
        String shortDateString = getStringFromText(TASKLISTDATEITEM_DATE_ID, node);
        LocalDate parsed = DateUtil.parseShortDate(shortDateString);
        
        if (parsed == null) {
            return DateUtil.NO_DATE_VALUE;
        } else {
            return parsed;
        }
    }
    
    /**
     * Returns a TaskListTaskItemHandle that corresponds to the task's data.
     * If it doesn't exist, it returns null.
     */
    public TaskListTaskItemHandle getTaskListTaskItem(Task taskToCompare) {
        Optional<Node> taskItemNode = guiRobot.lookup(TASKLISTTASKITEM_ID).queryAll().stream()
                .filter(node -> new TaskListTaskItemHandle(guiRobot, primaryStage, node).isEqualsToTask(taskToCompare))
                .findFirst();
        
        if (taskItemNode.isPresent()) {
            return new TaskListTaskItemHandle(guiRobot, primaryStage, taskItemNode.get());
        } else {
            return null;
        }
    }
    
    /**
     * Returns a TaskListEventItemHandle that corresponds to the event's data.
     * If it doesn't exist, it returns null.
     */
    public TaskListEventItemHandle getTaskListEventItem(Event eventToCompare) {
        Optional<Node> eventItemNode = guiRobot.lookup(TASKLISTEVENTITEM_ID).queryAll().stream()
                .filter(node -> new TaskListEventItemHandle(guiRobot, primaryStage, node).isEqualsToEvent(eventToCompare))
                .findFirst();
        
        if (eventItemNode.isPresent()) {
            return new TaskListEventItemHandle(guiRobot, primaryStage, eventItemNode.get());
        } else {
            return null;
        }
    }

}
