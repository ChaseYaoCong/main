package seedu.todo.ui.components;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seedu.todo.commons.EphemeralDB;
import seedu.todo.commons.util.DateUtil;
import seedu.todo.models.CalendarItem;
import seedu.todo.models.Event;
import seedu.todo.models.Task;
import seedu.todo.ui.UiPartLoader;

public class TaskList extends Component {
    
    public static final LocalDateTime NO_DATE_VALUE = LocalDateTime.MIN;

    private static final String FXML_PATH = "components/TaskList.fxml";
    private static EphemeralDB ephemeralDb = EphemeralDB.getInstance();

    // Props
    public List<Task> tasks = new ArrayList<>();
    public List<Event> events = new ArrayList<>();

    // FXML
    @FXML
    private VBox taskListDateItemsPlaceholder;

    @Override
    public String getFxmlPath() {
        return FXML_PATH;
    }

    @Override
    public void componentDidMount() {
        loadTasks();
    }

    public static TaskList load(Stage primaryStage, Pane placeholderPane) {
        return UiPartLoader.loadUiPart(primaryStage, placeholderPane, new TaskList());
    }

    private void loadTasks() {
        TaskListDateItem.reset(taskListDateItemsPlaceholder);

        // Clears displayedCalendarItems in EphemeralDB.
        ephemeralDb.clearDisplayedCalendarItems();

        // Get a list of tasks mapped to each date
        HashMap<LocalDateTime, ArrayList<Task>> tasksByDate = getItemsByDate(tasks);
        HashMap<LocalDateTime, ArrayList<Event>> eventsByDate = getItemsByDate(events);

        // Get unique task/event dates
        Set<LocalDateTime> uniqueDateSet = new HashSet<>();
        uniqueDateSet.addAll(tasksByDate.keySet());
        uniqueDateSet.addAll(eventsByDate.keySet());
        
        // Sort the dates
        List<LocalDateTime> sortedUniqueDates = new ArrayList<>();
        sortedUniqueDates.addAll(uniqueDateSet);
        java.util.Collections.sort(sortedUniqueDates);

        // For each dateTime, individually render a single TaskListDateItem.
        for (LocalDateTime dateTime : uniqueDateSet) {
            List<Task> tasksForDate = tasksByDate.get(dateTime);
            List<Event> eventsForDate = eventsByDate.get(dateTime);
            
            TaskListDateItem item = TaskListDateItem.load(primaryStage, taskListDateItemsPlaceholder);
            item.dateTime = dateTime;
            
            if (tasksForDate != null) {
                item.tasks = tasksForDate;
            }
            
            if (eventsForDate != null) {
                item.events = eventsForDate;
            }
            
            item.render();
        }
    }

    private <T extends CalendarItem> HashMap<LocalDateTime, ArrayList<T>> getItemsByDate(List<T> calendarItems) {
        HashMap<LocalDateTime, ArrayList<T>> itemsByDate = new HashMap<>();

        for (T item : calendarItems) {
            LocalDateTime itemDate = DateUtil.floorDate(item.getCalendarDT());
            
            // Handle tasks without a date
            if (itemDate == null)
                itemDate = NO_DATE_VALUE;

            // Creates ArrayList if not already exists.
            if (!itemsByDate.containsKey(itemDate)) {
                itemsByDate.put(itemDate, new ArrayList<T>());
            }
            // Adds to the ArrayList.
            itemsByDate.get(itemDate).add(item);
        }

        return itemsByDate;
    }

}
