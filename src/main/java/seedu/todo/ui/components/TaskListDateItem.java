package seedu.todo.ui.components;

import java.time.LocalDateTime;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import seedu.todo.commons.EphemeralDB;
import seedu.todo.commons.util.DateUtil;
import seedu.todo.commons.util.StringUtil;
import seedu.todo.models.Task;
import seedu.todo.ui.UiPartLoader;

public class TaskListDateItem extends MultiComponent {

    private static final String FXML_PATH = "components/TaskListDateItem.fxml";
    private static EphemeralDB ephemeralDb = EphemeralDB.getInstance();
    
    private static final String NO_DATE_STRING = "No Deadline";

    // Props
    public LocalDateTime dateTime;
    public List<Task> tasks;

    // FXML
    @FXML
    private Text dateText;
    @FXML
    private VBox dateTaskItemsPlaceholder;

    public static TaskListDateItem load(Stage primaryStage, Pane placeholderPane) {
        return UiPartLoader.loadUiPart(primaryStage, placeholderPane, new TaskListDateItem());
    }

    @Override
    public String getFxmlPath() {
        return FXML_PATH;
    }

    @Override
    public void componentDidMount() {
        String dateHeader;
        
        // Set header for DateItem
        if (dateTime == TaskList.NO_DATE_VALUE) {
            dateHeader = NO_DATE_STRING;
        } else {
            dateHeader = String.format("%s (%d %s)", 
                                              DateUtil.formatDay(dateTime), 
                                              tasks.size(), 
                                              StringUtil.pluralizer(tasks.size(), "task", "tasks"));
        }
        dateText.setText(dateHeader);

        // Load task items
        loadTaskItems();
    }

    private void loadTaskItems() {
        TaskListTaskItem.reset(dateTaskItemsPlaceholder);

        for (Task task : tasks) {
            TaskListTaskItem item = TaskListTaskItem.load(primaryStage, dateTaskItemsPlaceholder);

            // Add to EphemeralDB and get the index.
            int displayIndex = ephemeralDb.addToDisplayedTask(task);

            // Set the props and render the TaskListTaskItem.
            item.task = task;
            item.displayIndex = displayIndex;
            item.render();
        }
    }

}
