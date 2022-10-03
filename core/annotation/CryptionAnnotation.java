package core.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CryptionAnnotation {
  String payloadName();
  
  String Name();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\core\annotation\CryptionAnnotation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */