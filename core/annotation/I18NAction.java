package core.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface I18NAction {
  Class<?> targetClass();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\core\annotation\I18NAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */