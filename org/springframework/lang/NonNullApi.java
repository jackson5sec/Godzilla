package org.springframework.lang;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;
import javax.annotation.meta.TypeQualifierDefault;

@Target({ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Nonnull
@TypeQualifierDefault({ElementType.METHOD, ElementType.PARAMETER})
public @interface NonNullApi {}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\lang\NonNullApi.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */