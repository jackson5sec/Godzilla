package org.fife.ui.autocomplete;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.CLASS)
public @interface SuppressFBWarnings {
  String[] value() default {};
  
  String justification() default "";
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\autocomplete\SuppressFBWarnings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */