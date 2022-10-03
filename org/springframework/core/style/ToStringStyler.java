package org.springframework.core.style;

import org.springframework.lang.Nullable;

public interface ToStringStyler {
  void styleStart(StringBuilder paramStringBuilder, Object paramObject);
  
  void styleEnd(StringBuilder paramStringBuilder, Object paramObject);
  
  void styleField(StringBuilder paramStringBuilder, String paramString, @Nullable Object paramObject);
  
  void styleValue(StringBuilder paramStringBuilder, Object paramObject);
  
  void styleFieldSeparator(StringBuilder paramStringBuilder);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\style\ToStringStyler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */