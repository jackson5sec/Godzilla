package org.springframework.core.task;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public interface AsyncTaskExecutor extends TaskExecutor {
  public static final long TIMEOUT_IMMEDIATE = 0L;
  
  public static final long TIMEOUT_INDEFINITE = 9223372036854775807L;
  
  void execute(Runnable paramRunnable, long paramLong);
  
  Future<?> submit(Runnable paramRunnable);
  
  <T> Future<T> submit(Callable<T> paramCallable);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\task\AsyncTaskExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */