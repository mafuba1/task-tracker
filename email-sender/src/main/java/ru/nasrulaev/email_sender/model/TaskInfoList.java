package ru.nasrulaev.email_sender.model;

import java.util.List;

public class TaskInfoList {

    private List<TaskInfo> tasksInfo;

    public TaskInfoList() {
    }

    public TaskInfoList(List<TaskInfo> tasksInfo) {
    }

    public List<TaskInfo> getTasksInfo() {
        return tasksInfo;
    }

    public void setTasksInfo(List<TaskInfo> taskStats) {
        this.tasksInfo = taskStats;
    }
}
