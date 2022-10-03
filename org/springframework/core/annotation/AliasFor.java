package org.springframework.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface AliasFor {
  @AliasFor("attribute")
  String value() default "";
  
  @AliasFor("value")
  String attribute() default "";
  
  Class<? extends Annotation> annotation() default Annotation.class;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\annotation\AliasFor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */