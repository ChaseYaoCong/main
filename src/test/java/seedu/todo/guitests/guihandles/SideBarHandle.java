package seedu.todo.guitests.guihandles;

import java.util.Optional;

import javafx.scene.Node;
import javafx.stage.Stage;
import seedu.todo.guitests.GuiRobot;

/**
 * @@author Tiong YaoCong A0139922Y
 */
public class SideBarHandle extends GuiHandle {

    private static final String TAGLIST_ID = "#sidebarTagsPlaceholder";

    public SideBarHandle(GuiRobot guiRobot, Stage primaryStage, String stageTitle) {
        super(guiRobot, primaryStage, stageTitle);
    }
    
    /** CHANGE THIS
     * Returns a TaskListDateItemHandle that corresponds to the name specified.
     * If it doesn't exist, it returns null.
     */
    public TagListItemHandle getTagListItem(String tagName) {
        Optional<Node> tagItemNode = guiRobot.lookup(TAGLIST_ID).queryAll().stream()
                .filter(node -> new TagListItemHandle(guiRobot, primaryStage, node).getName().equals(tagName))
                .findFirst();
        
        if (tagItemNode.isPresent()) {
            return new TagListItemHandle(guiRobot, primaryStage, tagItemNode.get());
        } else {
            return null;
        }
    }
}
