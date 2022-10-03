package org.mozilla.javascript.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD})
public @interface JSConstructor {}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\annotations\JSConstructor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */