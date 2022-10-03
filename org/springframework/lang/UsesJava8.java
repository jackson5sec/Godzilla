package org.springframework.lang;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.TYPE})
@Documented
@Deprecated
public @interface UsesJava8 {}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\lang\UsesJava8.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */