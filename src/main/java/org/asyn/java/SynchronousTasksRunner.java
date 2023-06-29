package org.asyn.java;

import org.asyn.java.dto.TaskServerDto;
import org.asyn.java.utils.Utils;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

public class SynchronousTasksRunner {

  private final Random random = new Random();
  private static final Utils utils = new Utils();

  public static void main(String[] args) {
    utils.setBeginTime();
    new SynchronousTasksRunner().run();
    utils.setEndTime();
  }

  public void run() {

    Callable<TaskServerDto> taskServerA = createTaskServer("Server A");
    Callable<TaskServerDto> taskServerB = createTaskServer("Server B");
    Callable<TaskServerDto> taskServerC = createTaskServer("Server C");

    var taskServers = List.of(taskServerA, taskServerB, taskServerC);

    // init sync with Callable
    List<TaskServerDto> taskServerResponse = taskServers
        .stream()
        .map(SynchronousTasksRunner::fetchQuotation).toList();

    taskServerResponse.forEach(i -> System.out.println("[SYNC] = " + i));

  }

  private Callable<TaskServerDto> createTaskServer(String serverName) {
    return () -> {
      Instant begin = Instant.now();
      Thread.sleep(random.nextInt(80, 120));
      Instant end = Instant.now();
      Duration duration = Duration.between(begin, end);
      return new TaskServerDto(serverName, duration.toMillis());
    };
  }

  private static TaskServerDto fetchQuotation(Callable<TaskServerDto> task) {
    try {
      return task.call();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
