package seedu.todo.guitests.guihandles;

import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;
import seedu.todo.TestApp;
import seedu.todo.commons.core.LogsCenter;
import seedu.todo.guitests.GuiRobot;

import java.util.Optional;
import java.util.logging.Logger;

/**
 * Base class for all GUI Handles used in testing.
 * 
 * @@author A0139812A-reused
 */
public class GuiHandle {
    protected final GuiRobot guiRobot;
    protected final Stage primaryStage;
    /**
     * An optional stage that exists in the App other than the primaryStage, could be a alert dialog, popup window, etc.
     */
    protected Optional<Stage> intermediateStage = Optional.empty();
    protected final String stageTitle;

    private final Logger logger = LogsCenter.getLogger(this.getClass());

    public GuiHandle(GuiRobot guiRobot, Stage primaryStage, String stageTitle) {
        this.guiRobot = guiRobot;
        this.primaryStage = primaryStage;
        this.stageTitle = stageTitle;
        focusOnSelf();
    }

    public void focusOnWindow(String stageTitle) {
        logger.info("Focusing " + stageTitle);
        Optional<Window> window = guiRobot.listTargetWindows()
                .stream()
                .filter(w -> w instanceof Stage && ((Stage) w).getTitle().equals(stageTitle)).findAny();

        if (!window.isPresent()) {
            logger.warning("Can't find stage " + stageTitle + ", Therefore, aborting focusing");
            return;
        }
        intermediateStage = Optional.ofNullable((Stage) window.get());
        guiRobot.targetWindow(window.get());
        guiRobot.interact(() -> window.get().requestFocus());
        logger.info("Finishing focus " + stageTitle);
    }

    protected Node getNode(String query) {
        return guiRobot.lookup(query).tryQuery().get();
    }

    protected String getTextFieldText(String filedName) {
        return ((TextField) getNode(filedName)).getText();
    }
    
    protected String getTextAreaText(String filedName) {
        return ((TextArea) getNode(filedName)).getText();
    }

    protected void setTextField(String textFieldId, String newText) {
        guiRobot.clickOn(textFieldId);
        ((TextField) guiRobot.lookup(textFieldId).tryQuery().get()).setText(newText);
        guiRobot.sleep(500); // so that the texts stays visible on the GUI for a short period
    }

    public void pressEnter() {
        guiRobot.type(KeyCode.ENTER).sleep(500);
    }

    protected String getStringFromText(String fieldId, Node parentNode) {
        com.google.common.base.Optional<Node> text = guiRobot.from(parentNode).lookup(fieldId).tryQuery();
        if (text.isPresent()) {
            return ((Text) text.get()).getText();
        } else {
            return "";
        }
    }

    protected String getStringFromText(String fieldId) {
        return ((Text) guiRobot.lookup(fieldId).tryQuery().get()).getText();
    }

    public void focusOnSelf() {
        if (stageTitle != null) {
            focusOnWindow(stageTitle);
        }
    }

    public void focusOnMainApp() {
        this.focusOnWindow(TestApp.APP_TITLE);
    }

    public void closeWindow() {
        Optional<Window> window = guiRobot.listTargetWindows()
                .stream()
                .filter(w -> w instanceof Stage && ((Stage) w).getTitle().equals(stageTitle)).findAny();

        if (!window.isPresent()) {
            return;
        }

        guiRobot.targetWindow(window.get());
        guiRobot.interact(() -> ((Stage) window.get()).close());
        focusOnMainApp();
    }
}