package org.jetbrains.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.TYPE_USE})
public @interface NotNull {
  String value() default "";
  
  Class<? extends Exception> exception() default Exception.class;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\jetbrains\annotations\NotNull.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */