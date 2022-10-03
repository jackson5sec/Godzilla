package org.springframework.core.task;

import java.util.concurrent.Executor;

@FunctionalInterface
public interface TaskExecutor extends Executor {
  void execute(Runnable paramRunnable);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\task\TaskExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */