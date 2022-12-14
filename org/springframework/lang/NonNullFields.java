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
@TypeQualifierDefault({ElementType.FIELD})
public @interface NonNullFields {}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\lang\NonNullFields.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */