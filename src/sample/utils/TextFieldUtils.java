package sample.utils;


import javafx.collections.ObservableList;
import javafx.scene.control.TextField;

import java.util.Collections;

public class TextFieldUtils {

    public static void setSSNConstraint(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            String value = "";
            value = newValue.replaceAll("[^\\d]", "");
            ;
            if (value.length() > 9) {
                textField.setText(value.substring(0, 9));
            } else {
                textField.setText(value);
            }
        });
    }

    public static void setInputNumberConstraint(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            String value = "";
            textField.setText(newValue.replaceAll("[^\\d]", ""));
        });
    }

    public static boolean isValidInput(TextField textField) {
        ObservableList<String> styleClass = textField.getStyleClass();
        if (textField.getText().isEmpty()) {
            if (textField.getText().trim().length() == 0) {
                if (!styleClass.contains("error")) {
                    styleClass.add("error");

                }
            }
            return false;
        } else {
            // remove all occurrences:
            styleClass.removeAll(Collections.singleton("error"));
            return true;
        }
    }

}
