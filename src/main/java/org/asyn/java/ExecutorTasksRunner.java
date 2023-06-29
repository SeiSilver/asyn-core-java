package org.asyn.java;

import org.asyn.java.dto.TaskServerDto;
import org.asyn.java.utils.Utils;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorTasksRunner {

  private final Random random = new Random();
  private static final Utils utils = new Utils();

  public static void main(String[] args) throws ExecutionException, InterruptedException {
    utils.setBeginTime();
    new ExecutorTasksRunner().run();
    utils.setEndTime();
  }

  public void run() throws ExecutionException, InterruptedException {

    List<Callable<TaskServerDto>> quotationTasks = initData();

    var executor = Executors.newFixedThreadPool(4);

    List<Future<TaskServerDto>> futures = new ArrayList<>();
    for (Callable<TaskServerDto> task : quotationTasks) {
      Future<TaskServerDto> future = executor.submit(task);
      futures.add(future);
    }

    // the get() will wait for the CompletableFuture to complete and return the data
    List<TaskServerDto> taskServerResponse = new ArrayList<>();
    for (Future<TaskServerDto> future : futures) {
      TaskServerDto quotation = future.get();
      taskServerResponse.add(quotation);
    }

    taskServerResponse.forEach(i -> System.out.println("[ES] = " + i));

    executor.shutdown();
  }

  private List<Callable<TaskServerDto>> initData() {
    Callable<TaskServerDto> taskServerA = createTaskServer("Server A");
    Callable<TaskServerDto> taskServerB = createTaskServer("Server B");
    Callable<TaskServerDto> taskServerC = createTaskServer("Server C");
    return List.of(taskServerA, taskServerB, taskServerC);
  }

  private Callable<TaskServerDto> createTaskServer(String serverName) {
    return () -> {
      Instant begin = Instant.now();
      Thread.sleep(random.nextInt(80, 120));
      Instant end = Instant.now();
      Duration duration = Duration.between(begin, end);
      return new TaskServerDto(serverName, (int) duration.toMillis());
    };
  }
}
