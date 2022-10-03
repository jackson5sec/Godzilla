package org.springframework.expression;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.lang.Nullable;

public interface TypeConverter {
  boolean canConvert(@Nullable TypeDescriptor paramTypeDescriptor1, TypeDescriptor paramTypeDescriptor2);
  
  @Nullable
  Object convertValue(@Nullable Object paramObject, @Nullable TypeDescriptor paramTypeDescriptor1, TypeDescriptor paramTypeDescriptor2);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\TypeConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */