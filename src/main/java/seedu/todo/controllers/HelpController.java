package seedu.todo.controllers;

import java.util.ArrayList;
import java.util.Arrays;

import seedu.todo.ui.UiManager;
import seedu.todo.ui.views.HelpView;
import seedu.todo.ui.views.IndexView;

public class HelpController implements Controller {

    private static String NAME = "Help";
    private static String DESCRIPTION = "Shows documentation for all valid commands.";
    private static String COMMAND_SYNTAX = "help";
    
    private static CommandDefinition commandDefinition =
            new CommandDefinition(NAME, DESCRIPTION, COMMAND_SYNTAX); 

    public static CommandDefinition getCommandDefinition() {
        return commandDefinition;
    }

    @Override
    public float inputConfidence(String input) {
        return (input.startsWith("help")) ? 1 : 0;
    }

    @Override
    public void process(String input) {
        HelpView view = UiManager.loadView(HelpView.class);
        view.commandDefinitions = Arrays.asList(getAllCommandDefinitions());
        view.render();
    }
    
    private CommandDefinition[] getAllCommandDefinitions() {
        return new CommandDefinition[] { HelpController.getCommandDefinition(),
                                         AddController.getCommandDefinition(),
                                         ListController.getCommandDefinition(),
                                         UpdateController.getCommandDefinition(),
                                         DestroyController.getCommandDefinition() };
    }
}