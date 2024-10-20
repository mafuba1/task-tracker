package ru.nasrulaev.scheduler.dto;

import java.util.List;

public class TaskInfoList {

    private List<TaskInfo> tasksInfo;

    public TaskInfoList() {
    }

    public TaskInfoList(List<TaskInfo> taskStats) {
    }

    public List<TaskInfo> getTaskStats() {
        return tasksInfo;
    }

    public void setTaskStats(List<TaskInfo> taskStats) {
        this.tasksInfo = taskStats;
    }
}
