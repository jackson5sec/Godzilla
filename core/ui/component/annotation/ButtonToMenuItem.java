package core.ui.component.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ButtonToMenuItem {
  String name() default "";
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\cor\\ui\component\annotation\ButtonToMenuItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */