package utn.ac.cr.fitwoman_management.Entities;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Goal implements Serializable {
    private String id;
    private String type; // "weight", "workouts_weekly", "workouts_total"
    private double targetValue;
    private double currentValue;
    private Date startDate;
    private Date targetDate;
    private boolean isCompleted;
    private String description;

    public Goal() {
        this.id = UUID.randomUUID().toString();
        this.startDate = new Date();
        this.isCompleted = false;
        this.currentValue = 0;
    }

    public Goal(String type, double targetValue, Date targetDate, String description) {
        this.id = UUID.randomUUID().toString();
        this.type = type;
        this.targetValue = targetValue;
        this.targetDate = targetDate;
        this.description = description;
        this.startDate = new Date();
        this.isCompleted = false;
        this.currentValue = 0;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(double targetValue) {
        this.targetValue = targetValue;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(Date targetDate) {
        this.targetDate = targetDate;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getProgressPercentage() {
        if (targetValue == 0) return 0;
        int progress = (int) ((currentValue / targetValue) * 100);
        return Math.min(progress, 100);
    }

    @Override
    public String toString() {
        return "Goal{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", targetValue=" + targetValue +
                ", currentValue=" + currentValue +
                ", progress=" + getProgressPercentage() + "%" +
                '}';
    }
}