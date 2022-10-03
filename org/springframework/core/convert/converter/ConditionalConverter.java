package org.springframework.core.convert.converter;

import org.springframework.core.convert.TypeDescriptor;

public interface ConditionalConverter {
  boolean matches(TypeDescriptor paramTypeDescriptor1, TypeDescriptor paramTypeDescriptor2);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\convert\converter\ConditionalConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */