package seedu.todo.ui;


import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import seedu.todo.MainApp;
import seedu.todo.commons.core.Config;
import seedu.todo.commons.core.GuiSettings;
import seedu.todo.commons.events.ui.ExitAppRequestEvent;
import seedu.todo.ui.components.Component;
import seedu.todo.ui.components.Console;
import seedu.todo.ui.components.Header;
import seedu.todo.ui.views.View;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 * 
 * @@author A0139812A
 */
public class MainWindow extends Component {

    private static final String FXML_PATH = "MainWindow.fxml";
    private static final String ICON_PATH = "/images/logo-512x512.png";
    private static final String OPEN_HELP_KEY_COMBINATION = "F1";
    public static final int MIN_HEIGHT = 600;
    public static final int MIN_WIDTH = 600;

    // Handles to elements of this Ui container
    private VBox rootLayout;
    private Scene scene;

    // FXML Components
    @FXML
    private MenuItem helpMenuItem;
    @FXML
    private AnchorPane childrenPlaceholder;
    @FXML
    private AnchorPane consoleInputPlaceholder;
    @FXML
    private AnchorPane headerPlaceholder;

    public void configure(Config config) {
        String appTitle = config.getAppTitle();

        // Configure the UI
        setTitle(appTitle);
        setIcon(ICON_PATH);
        setWindowMinSize();
        scene = new Scene(rootLayout);
        primaryStage.setScene(scene);

        // Bind accelerators
        setAccelerators();

        // Load other components.
        loadComponents();
    }

    protected void loadComponents() {
        // Load Header
        Header header = UiPartLoader.loadUiPart(primaryStage, getHeaderPlaceholder(), Header.class);
        header.appTitle = MainApp.getConfig().getAppTitle();
        header.versionString = MainApp.VERSION.toString();
        header.render();

        // Load ConsoleInput
        Console console = UiPartLoader.loadUiPart(primaryStage, getConsoleInputPlaceholder(), Console.class);
        console.consoleOutput = UiManager.getConsoleMessage();
        console.consoleInputValue = UiManager.getConsoleInputValue();
        console.render();
    }

    @Override
    public void setNode(Node node) {
        rootLayout = (VBox) node;
    }

    @Override
    public String getFxmlPath() {
        return FXML_PATH;
    }

    public void show() {
        primaryStage.show();
    }

    public void hide() {
        primaryStage.hide();
    }

    public void setTitle(String appTitle) {
        primaryStage.setTitle(appTitle);
    }

    public void setWindowMinSize() {
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.setMinWidth(MIN_WIDTH);
    }

    /**
     * Returns the current size and the position of the main Window.
     */
    public GuiSettings getCurrentGuiSetting() {
        return new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                               (int) primaryStage.getX(), (int) primaryStage.getY());
    }

    protected <T extends View> T loadView(Class<T> viewClass) {
        return load(primaryStage, getChildrenPlaceholder(), viewClass);
    }

    /** ================ FXML COMPONENTS ================== **/

    public AnchorPane getChildrenPlaceholder() {
        return childrenPlaceholder;
    }

    public AnchorPane getConsoleInputPlaceholder() {
        return consoleInputPlaceholder;
    }

    public AnchorPane getHeaderPlaceholder() {
        return headerPlaceholder;
    }

    /** ================ ACCELERATORS ================== **/

    private void setAccelerators() {
        helpMenuItem.setAccelerator(KeyCombination.valueOf(OPEN_HELP_KEY_COMBINATION));
    }

    /** ================ ACTION HANDLERS ================== **/

    @FXML
    public void handleHelp() {
        // TODO: Auto-generated method stub
    }

    @FXML
    private void handleExit() {
        raise(new ExitAppRequestEvent());
    }
}
