package core.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface PluginAnnotation {
  String payloadName();
  
  String Name();
  
  String DisplayName();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\core\annotation\PluginAnnotation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */