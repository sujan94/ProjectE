package sample.utils;

import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;

import java.util.Collections;

public class ChoiceBoxUtils {

    public static boolean isValidInput(ChoiceBox<? extends Object> choiceBox) {
        ObservableList<String> styleClass = choiceBox.getStyleClass();
        if (choiceBox.getValue() == null || choiceBox.getValue().toString().equals("None")) {
            if (!styleClass.contains("error")) {
                styleClass.add("error");

            }
            return false;
        } else {
            // remove all occurrences:
            styleClass.removeAll(Collections.singleton("error"));
            return true;
        }

    }
}
