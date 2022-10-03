package org.springframework.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Documented
public @interface Order {
  int value() default 2147483647;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\annotation\Order.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */