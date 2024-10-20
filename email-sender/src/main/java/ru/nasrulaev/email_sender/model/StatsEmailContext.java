package ru.nasrulaev.email_sender.model;

public class StatsEmailContext {
    private String to;
    private TaskInfoList taskDoneToday;
    private TaskInfoList tasksNotDone;

    public StatsEmailContext() {}

    public StatsEmailContext(String to, TaskInfoList taskDoneToday, TaskInfoList tasksNotDone) {
        this.to = to;
        this.taskDoneToday = taskDoneToday;
        this.tasksNotDone = tasksNotDone;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public TaskInfoList getTaskDoneToday() {
        return taskDoneToday;
    }

    public void setTaskDoneToday(TaskInfoList taskDoneToday) {
        this.taskDoneToday = taskDoneToday;
    }

    public TaskInfoList getTasksNotDone() {
        return tasksNotDone;
    }

    public void setTasksNotDone(TaskInfoList tasksNotDone) {
        this.tasksNotDone = tasksNotDone;
    }
}
