package org.example.demo.model;

import javafx.beans.property.*;
import java.time.LocalDate;

public class OnlineJob {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final IntegerProperty leaveRequestId = new SimpleIntegerProperty(); // Reference to LeaveRequest
    private final StringProperty title = new SimpleStringProperty(); // Title of the job
    private final StringProperty post = new SimpleStringProperty(); // Post as a text field (description or note)
    private final ObjectProperty<LocalDate> startDate = new SimpleObjectProperty<>(); // Start date of the job
    private final ObjectProperty<LocalDate> endDate = new SimpleObjectProperty<>(); // End date of the job
    private final BooleanProperty isConfirmed = new SimpleBooleanProperty(); // Confirmation status

    // Getters and Setters for properties
    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getLeaveRequestId() {
        return leaveRequestId.get();
    }

    public IntegerProperty leaveRequestIdProperty() {
        return leaveRequestId;
    }

    public void setLeaveRequestId(int leaveRequestId) {
        this.leaveRequestId.set(leaveRequestId);
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getPost() {
        return post.get();
    }

    public StringProperty postProperty() {
        return post;
    }

    public void setPost(String post) {
        this.post.set(post);
    }

    public LocalDate getStartDate() {
        return startDate.get();
    }

    public ObjectProperty<LocalDate> startDateProperty() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate.set(startDate);
    }

    public LocalDate getEndDate() {
        return endDate.get();
    }

    public ObjectProperty<LocalDate> endDateProperty() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate.set(endDate);
    }

    public boolean isConfirmed() {
        return isConfirmed.get();
    }

    public BooleanProperty isConfirmedProperty() {
        return isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.isConfirmed.set(confirmed);
    }
}