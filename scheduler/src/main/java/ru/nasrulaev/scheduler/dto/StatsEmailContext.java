package ru.nasrulaev.scheduler.dto;

import java.util.List;

public class StatsEmailContext {
    private String to;
    private List<TaskInfo> taskDoneToday;
    private List<TaskInfo> tasksNotDone;

    public StatsEmailContext() {}

    public StatsEmailContext(String to, List<TaskInfo> taskDoneToday, List<TaskInfo> tasksNotDone) {
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

    public List<TaskInfo> getTaskDoneToday() {
        return taskDoneToday;
    }

    public void setTaskDoneToday(List<TaskInfo> taskDoneToday) {
        this.taskDoneToday = taskDoneToday;
    }

    public List<TaskInfo> getTasksNotDone() {
        return tasksNotDone;
    }

    public void setTasksNotDone(List<TaskInfo> tasksNotDone) {
        this.tasksNotDone = tasksNotDone;
    }
}
