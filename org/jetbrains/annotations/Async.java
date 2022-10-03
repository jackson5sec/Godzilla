package org.jetbrains.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public final class Async {
  @Retention(RetentionPolicy.CLASS)
  @Target({ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
  public static @interface Execute {}
  
  @Retention(RetentionPolicy.CLASS)
  @Target({ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
  public static @interface Schedule {}
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\jetbrains\annotations\Async.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */