package seedu.todo.ui.components;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import seedu.todo.commons.util.FxViewUtil;
import seedu.todo.ui.UiPartLoader;

public class Console extends Component {

    private static final String FXML_PATH = "components/Console.fxml";

    // Props
    public String consoleOutput;
    
    // FXML
    @FXML
    private TextArea consoleTextArea;

    public static Console load(Stage primaryStage, Pane placeholderPane) {
        return UiPartLoader.loadUiPart(primaryStage, placeholderPane, new Console());
    }

    @Override
    public String getFxmlPath() {
        return FXML_PATH;
    }

    @Override
    public void componentDidMount() {
        // Makes Console full width wrt parent container.
        FxViewUtil.makeFullWidth(this.mainNode);
        
        // Show console output
        consoleTextArea.setText(consoleOutput);
    }

}