package org.asyn.java;

import org.asyn.java.dto.TaskServerDto;
import org.asyn.java.utils.Utils;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class AsyncTasksRunner {

  private final Random random = new Random();
  private static final Utils utils = new Utils();

  public static void main(String[] args) {
    utils.setBeginTime();
    new AsyncTasksRunner().run();
    utils.setEndTime();
  }

  public void run() {

    final List<Supplier<TaskServerDto>> taskServers = initServer();

    // Init CompletableFuture Object
    List<CompletableFuture<TaskServerDto>> futures = new ArrayList<>();
    for (Supplier<TaskServerDto> task : taskServers) {
      futures.add(CompletableFuture.supplyAsync(task));
    }

    // the join() will wait for the CompletableFuture to complete and return the data
    List<TaskServerDto> taskServerResponse = new ArrayList<>();
    for (CompletableFuture<TaskServerDto> future : futures) {
      taskServerResponse.add(future.join());
    }

    taskServerResponse.forEach(i -> System.out.println("[ASYNC] = " + i));

  }

  private List<Supplier<TaskServerDto>> initServer() {
    Supplier<TaskServerDto> taskServerA = createTaskServer("Server A");
    Supplier<TaskServerDto> taskServerB = createTaskServer("Server B");
    Supplier<TaskServerDto> taskServerC = createTaskServer("Server C");
    return List.of(taskServerA, taskServerB, taskServerC);
  }

  private Supplier<TaskServerDto> createTaskServer(String serverName) {
    return () -> {
      Instant begin = Instant.now();
      try {
        Thread.sleep(random.nextInt(80, 120));
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      Instant end = Instant.now();
      Duration duration = Duration.between(begin, end);
      return new TaskServerDto(serverName, (int) duration.toMillis());
    };
  }

}
