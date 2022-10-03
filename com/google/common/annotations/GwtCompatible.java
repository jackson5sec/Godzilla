package com.google.common.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@GwtCompatible
public @interface GwtCompatible {
  boolean serializable() default false;
  
  boolean emulated() default false;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\annotations\GwtCompatible.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */