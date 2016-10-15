package seedu.todo.controllers;

import java.util.HashMap;
import java.util.Map;

import seedu.todo.commons.exceptions.UnmatchedQuotesException;
import seedu.todo.commons.util.StringUtil;
import seedu.todo.models.TodoListDB;
import seedu.todo.ui.UiManager;
import seedu.todo.ui.views.IndexView;

public class UndoController implements Controller {
    
    private static final String MESSAGE_SUCCESS = "Successfully undid %s %s!\nTo redo, type \"redo\".";
    private static final String MESSAGE_MULTIPLE_FAILURE = "We cannot undo %s %s! At most, you can undo %s %s.";
    private static final String MESSAGE_FAILURE = "There is no command to undo!";

    @Override
    public float inputConfidence(String input) {
        return input.startsWith("undo") ? 1 : 0;
    }
    
    private static Map<String, String[]> getTokenDefinitions() {
        Map<String, String[]> tokenDefinitions = new HashMap<String, String[]>();
        tokenDefinitions.put("default", new String[] {"undo"});
        return tokenDefinitions;
    }

    @Override
    public void process(String input) {
        
        Map<String, String[]> parsedResult;
        try {
            parsedResult = Tokenizer.tokenize(getTokenDefinitions(), input);            
        } catch (UnmatchedQuotesException e) {
            System.out.println("Unmatched quote!");
            return;
        }
        
        int numUndo = 1;
        if (parsedResult.get("default")[1] != null) {
            numUndo = Integer.parseInt(parsedResult.get("default")[1]);
        }
        
        // We don't really have a nice way to support SQL transactions, so yeah >_<
        TodoListDB db = TodoListDB.getInstance();
        if (numUndo <= 0) {
            UiManager.updateConsoleMessage(MESSAGE_FAILURE);
            return;
        }
        if (db.undoSize() < numUndo) {
            UiManager.updateConsoleMessage(String.format(MESSAGE_MULTIPLE_FAILURE,
                    numUndo, StringUtil.pluralizer(numUndo, "command", "commands"),
                    db.undoSize(), StringUtil.pluralizer(db.undoSize(), "command", "commands")));
            return;
        }
        for (int i = 0; i < numUndo; i++) {
            if (!db.undo()) {
                UiManager.updateConsoleMessage(MESSAGE_FAILURE);
                return;
            }
        }
        db = TodoListDB.getInstance();
        
        // Render
        IndexView view = UiManager.loadView(IndexView.class);
        view.tasks = db.getAllTasks();
        view.events = db.getAllEvents();
        UiManager.renderView(view);
        
        // Update console message
        UiManager.updateConsoleMessage(String.format(MESSAGE_SUCCESS, numUndo,
                StringUtil.pluralizer(numUndo, "command", "commands")));
    }
}
