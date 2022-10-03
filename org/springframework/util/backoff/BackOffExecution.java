package org.springframework.util.backoff;

@FunctionalInterface
public interface BackOffExecution {
  public static final long STOP = -1L;
  
  long nextBackOff();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\backoff\BackOffExecution.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */