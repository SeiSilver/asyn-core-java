package org.asyn.java.utils;

import java.time.Duration;
import java.time.Instant;

public class Utils {

  private Instant begin;

  public void setBeginTime() {
    System.out.println("======================");
    begin = Instant.now();
  }

  public void setEndTime() {
    Instant end = Instant.now();
    Duration duration = Duration.between(begin, end);
    System.out.println("Total Time: " + duration.toMillis() + "ms");
    System.out.println("======================");
  }

}
