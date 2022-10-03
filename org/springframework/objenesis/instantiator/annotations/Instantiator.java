package org.springframework.objenesis.instantiator.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Instantiator {
  Typology value();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\objenesis\instantiator\annotations\Instantiator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */