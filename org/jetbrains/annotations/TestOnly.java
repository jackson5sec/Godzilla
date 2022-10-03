package org.jetbrains.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.TYPE})
public @interface TestOnly {}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\jetbrains\annotations\TestOnly.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */