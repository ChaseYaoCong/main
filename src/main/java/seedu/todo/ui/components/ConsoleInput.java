package seedu.todo.ui.components;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import seedu.todo.logic.Logic;
import seedu.todo.ui.components.Console;
import seedu.todo.ui.UiPartLoader;
import seedu.address.logic.commands.CommandResult;
//import seedu.todo.commons.events.ui.IncorrectCommandAttemptedEvent;
//import seedu.todo.logic.commands.CommandResult;
import seedu.todo.commons.core.LogsCenter;
import seedu.todo.ui.components.ConsoleInput;
import seedu.todo.commons.util.FxViewUtil;

public class ConsoleInput extends Component {
    private final Logger logger = LogsCenter.getLogger(ConsoleInput.class);
	private static final String FXML_PATH = "components/ConsoleInput.fxml";
	
	private AnchorPane placeHolderPane;
    private AnchorPane commandPane;
    private Console consoleDisplay;

    private LogicStub logic;
	
	// Props
	public String lastCommandEntered;
	@FXML
	private TextField commandTextField;
	private CommandResultStub mostRecentResult;
	
   public static ConsoleInput load(Stage primaryStage, AnchorPane commandBoxPlaceholder,
            Console resultDisplay, LogicStub logic) {
        ConsoleInput commandBox = UiPartLoader.loadUiPart(primaryStage, commandBoxPlaceholder, new ConsoleInput());
        commandBox.configure(resultDisplay, logic);
        commandBox.addToPlaceholder();
        return commandBox;
    }
    
    public void configure(Console resultDisplay, LogicStub logic) {
        this.consoleDisplay = resultDisplay;
        this.logic = logic;
        registerAsAnEventHandler(this);
    }

    private void addToPlaceholder() {
        SplitPane.setResizableWithParent(placeHolderPane, false);
        placeHolderPane.getChildren().add(commandTextField);
        FxViewUtil.applyAnchorBoundaryParameters(commandPane, 0.0, 0.0, 0.0, 0.0);
        FxViewUtil.applyAnchorBoundaryParameters(commandTextField, 0.0, 0.0, 0.0, 0.0);
    }
	
	@Override
	public String getFxmlPath() {
		return FXML_PATH;
	}
	
   @Override
    public void setNode(Node node) {
        commandPane = (AnchorPane) node;
    }	
   
   @Override
   public void setPlaceholder(Pane pane) {
       this.placeHolderPane = (AnchorPane) pane;
   }
   
	@Override
	public void componentDidMount() {
		// Makes ConsoleInput full width wrt parent container.
		FxViewUtil.makeFullWidth(this.mainNode);
	}

	/** ================ ACTION HANDLERS ================== **/
	@FXML
	public void handleConsoleInputChanged() {
        //Take a copy of the command text
	    lastCommandEntered = commandTextField.getText();

        /* We assume the command is correct. If it is incorrect, the command box will be changed accordingly
         * in the event handling code {@link #handleIncorrectCommandAttempted}
         */
        setStyleToIndicateCorrectCommand();
        mostRecentResult = logic.execute(lastCommandEntered);
        consoleDisplay.postMessage(mostRecentResult.feedbackToUser);
        logger.info("Result: " + mostRecentResult.feedbackToUser);
	}
	
	/**
     * Sets the command box style to indicate a correct command.
     */
    private void setStyleToIndicateCorrectCommand() {
        commandTextField.getStyleClass().remove("error");
        commandTextField.setText("");
    }

    @Subscribe
    private void handleIncorrectCommandAttempted(IncorrectCommandAttemptedEvent event){
        logger.info(LogsCenter.getEventHandlingLogMessage(event,"Invalid command: " + lastCommandEntered));
        setStyleToIndicateIncorrectCommand();
        restoreCommandText();
    }

    /**
     * Restores the command box text to the previously entered command
     */
    private void restoreCommandText() {
        commandTextField.setText(lastCommandEntered);
    }

    /**
     * Sets the command box style to indicate an error
     */
    private void setStyleToIndicateIncorrectCommand() {
        commandTextField.getStyleClass().add("error");
    }

    //CommandResultStub
    class CommandResultStub {
        public final String feedbackToUser;

        public CommandResultStub(String feedbackToUser) {
            assert feedbackToUser != null;
            this.feedbackToUser = feedbackToUser;
        }
    } 
    
    class LogicStub {
        CommandResultStub execute(String commandText) {
            return new CommandResultStub(commandText);
        }
    }
}


