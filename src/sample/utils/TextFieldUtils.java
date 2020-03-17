package sample.utils;


import javafx.scene.control.TextField;

public class TextFieldUtils {

    public static void setSSNConstraint(TextField textField){
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

    public static void setInputNumberConstraint(TextField textField){
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            String value = "";
            textField.setText(newValue.replaceAll("[^\\d]", ""));
        });
    }

    public static void setInputDateConstraint(TextField textField){
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            String value = "";
            textField.setText(newValue.replaceAll("[^\\d]", ""));
        });
    }
}
