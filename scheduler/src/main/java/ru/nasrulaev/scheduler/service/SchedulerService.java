package ru.nasrulaev.scheduler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SchedulerService {

    private final StatsService statsService;

    @Autowired
    public SchedulerService(StatsService statsService) {
        this.statsService = statsService;
    }

    @Scheduled(cron = "${cron_schedule}")
    public void sendStats() {
        statsService.iterateUsersAndSendStatsEmail();
    }
}
