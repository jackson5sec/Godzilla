package org.springframework.core.task;

@FunctionalInterface
public interface TaskDecorator {
  Runnable decorate(Runnable paramRunnable);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\task\TaskDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */