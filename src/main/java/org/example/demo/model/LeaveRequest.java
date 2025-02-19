package org.example.demo.model;

import javafx.beans.property.*;
import java.time.LocalDate;

public class LeaveRequest {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final IntegerProperty employeeId = new SimpleIntegerProperty();
    private final IntegerProperty companyId = new SimpleIntegerProperty(); // Add companyId
    private final ObjectProperty<LocalDate> startDate = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate> endDate = new SimpleObjectProperty<>();
    private final StringProperty description = new SimpleStringProperty();
    private final StringProperty leaveType = new SimpleStringProperty();
    private final StringProperty pdfPath = new SimpleStringProperty();
    private final BooleanProperty isConfirmed = new SimpleBooleanProperty();

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

    public int getEmployeeId() {
        return employeeId.get();
    }

    public IntegerProperty employeeIdProperty() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId.set(employeeId);
    }

    public int getCompanyId() {
        return companyId.get();
    }

    public IntegerProperty companyIdProperty() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId.set(companyId);
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

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getLeaveType() {
        return leaveType.get();
    }

    public StringProperty leaveTypeProperty() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType.set(leaveType);
    }

    public String getPdfPath() {
        return pdfPath.get();
    }

    public StringProperty pdfPathProperty() {
        return pdfPath;
    }

    public void setPdfPath(String pdfPath) {
        this.pdfPath.set(pdfPath);
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