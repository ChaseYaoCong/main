package seedu.todo.ui.components;

import java.util.logging.Logger;

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
//import seedu.address.logic.commands.CommandResult;
import seedu.todo.commons.core.LogsCenter;
import seedu.todo.ui.components.ConsoleInput;
import seedu.todo.commons.util.FxViewUtil;

public class ConsoleInput extends Component {
    private final Logger logger = LogsCenter.getLogger(ConsoleInput.class);
	private static final String FXML_PATH = "components/ConsoleInput.fxml";
	
	private AnchorPane placeHolderPane;
    private AnchorPane commandPane;
    private Console consoleDisplay;

    private Logic logic;
	
	// Props
	public String lastCommandEntered;
	
   public static ConsoleInput load(Stage primaryStage, AnchorPane commandBoxPlaceholder,
            Console resultDisplay, Logic logic) {
        ConsoleInput commandBox = UiPartLoader.loadUiPart(primaryStage, commandBoxPlaceholder, new ConsoleInput());
        commandBox.configure(resultDisplay, logic);
        commandBox.addToPlaceholder();
        return commandBox;
    }
    
    public void configure(Console resultDisplay, Logic logic) {
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
	
	@FXML
    private TextField commandTextField;
	//Command result by logic commands uncomment the line below when backend is done
    //private CommandResult mostRecentResult;
	
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
		// TODO
	}
	

}
