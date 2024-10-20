package ru.nasrulaev.scheduler.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nasrulaev.scheduler.dto.StatsEmailContext;
import ru.nasrulaev.scheduler.dto.TaskInfo;
import ru.nasrulaev.scheduler.dto.TaskInfoList;
import ru.nasrulaev.scheduler.model.Task;
import ru.nasrulaev.scheduler.model.User;
import ru.nasrulaev.scheduler.repository.UsersRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class StatsService {
    private final UsersRepository usersRepository;
    private final ModelMapper modelMapper;
    private final KafkaService kafkaService;

    @Autowired
    public StatsService(UsersRepository usersRepository, ModelMapper modelMapper, KafkaService kafkaService) {
        this.usersRepository = usersRepository;
        this.modelMapper = modelMapper;
        this.kafkaService = kafkaService;
    }

    @Transactional(readOnly = true)
    public void iterateUsersAndSendStatsEmail() {
        List<User> subscribedUsers = usersRepository.findSubscribedUsers();
        subscribedUsers.forEach(this::sendStatsEmail);
    }

    private final static Timestamp YESTERDAY = Timestamp.valueOf(
            LocalDateTime.now()
                    .withSecond(0)
                    .withMinute(0)
                    .withHour(0)
                    .minusDays(1)
    );

    private void sendStatsEmail(User user) {
        List<Task> tasks = user.getTasks();

        TaskInfoList tasksDoneToday = new TaskInfoList(
                tasks.stream()
                        .filter(Task::isDone)
                        .filter(task ->
                                task.getDone_timestamp().after(YESTERDAY)
                        )
                        .map(this::convertToTaskInfo)
                        .toList()
        );

        TaskInfoList taskNotDone = new TaskInfoList(
                tasks.stream()
                        .filter(task ->
                                !task.isDone()
                        )
                        .map(this::convertToTaskInfo)
                        .toList()
        );

        StatsEmailContext statsEmailContext = new StatsEmailContext(
                user.getEmail(),
                tasksDoneToday,
                taskNotDone
        );

        kafkaService.sendMessage(statsEmailContext);
    }


    private TaskInfo convertToTaskInfo(Task task) {
        return modelMapper.map(task, TaskInfo.class);
    }
}
