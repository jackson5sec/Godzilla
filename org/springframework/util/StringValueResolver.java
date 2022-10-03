package org.springframework.util;

import org.springframework.lang.Nullable;

@FunctionalInterface
public interface StringValueResolver {
  @Nullable
  String resolveStringValue(String paramString);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\StringValueResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */