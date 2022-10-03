package org.springframework.expression;

import org.springframework.lang.Nullable;

public interface TypeComparator {
  boolean canCompare(@Nullable Object paramObject1, @Nullable Object paramObject2);
  
  int compare(@Nullable Object paramObject1, @Nullable Object paramObject2) throws EvaluationException;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\TypeComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */