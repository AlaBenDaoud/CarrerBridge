package org.example.pi.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AnswerDialogController {

    @FXML
    private TextField titleField;

    @FXML
    private TextArea messageField;

    public String getTitle() {
        return titleField.getText();
    }

    public String getMessage() {
        return messageField.getText();
    }
}