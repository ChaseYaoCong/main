package seedu.todo.controllers.concerns;

import seedu.todo.models.TodoListDB;
import seedu.todo.ui.UiManager;
import seedu.todo.ui.views.ConfigView;
import seedu.todo.ui.views.IndexView;

/**
 * Class to store rendering methods to be shared across controllers.
 * 
 * @author louietyj
 *
 */
public class Renderer {

    private static final String MESSAGE_DISAMBIGUATE = "Your last command wasn't clear, please fix your command and try again.";
    
    /**
     * Renders an error message in both the console and the input field, leave null or empty string if not needed.
     * @param replacedCommand    Value to display in the input field
     * @param detailedError       Message to be rendered in the console
     */
    public static void renderDisambiguation(String replacedCommand, String detailedError) {
        // Update console input field
        if (replacedCommand != null && replacedCommand.length() > 0) {
            UiManager.updateConsoleInputValue(replacedCommand);
        }
        
        // Update console message
        if (detailedError != null && detailedError.length() > 0) {
            UiManager.updateConsoleMessage(String.format("%s\n\n%s", MESSAGE_DISAMBIGUATE, detailedError));
        } else {
            UiManager.updateConsoleMessage(String.format("%s", MESSAGE_DISAMBIGUATE));
        }
    }
    
    /**
     * Renders the indexView.
     * 
     * @param db
     * @param consoleMessage to be rendered in console, leave null if not needed
     */
    public static void renderIndex(TodoListDB db, String consoleMessage) {
        IndexView view = UiManager.loadView(IndexView.class);
        view.tasks = db.getAllTasks();
        view.events = db.getAllEvents();
        UiManager.renderView(view);
        
        if (consoleMessage != null) {
            UiManager.updateConsoleMessage(consoleMessage);
        }
    }
    
    /**
     * Renders the ConfigView.
     * 
     * @param consoleMessage to be rendered in console, leave null if not needed
     */
    public static void renderConfig(String consoleMessage) {
        ConfigView view = UiManager.loadView(ConfigView.class);
        UiManager.renderView(view);
        
        if (consoleMessage != null) {
            UiManager.updateConsoleMessage(consoleMessage);
        }
    }

}