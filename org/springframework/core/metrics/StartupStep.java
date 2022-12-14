package org.springframework.core.metrics;

import java.util.function.Supplier;
import org.springframework.lang.Nullable;

public interface StartupStep {
  String getName();
  
  long getId();
  
  @Nullable
  Long getParentId();
  
  StartupStep tag(String paramString1, String paramString2);
  
  StartupStep tag(String paramString, Supplier<String> paramSupplier);
  
  Tags getTags();
  
  void end();
  
  public static interface Tag {
    String getKey();
    
    String getValue();
  }
  
  public static interface Tags extends Iterable<Tag> {}
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\metrics\StartupStep.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */