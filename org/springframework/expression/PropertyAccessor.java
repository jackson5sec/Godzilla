package org.springframework.expression;

import org.springframework.lang.Nullable;

public interface PropertyAccessor {
  @Nullable
  Class<?>[] getSpecificTargetClasses();
  
  boolean canRead(EvaluationContext paramEvaluationContext, @Nullable Object paramObject, String paramString) throws AccessException;
  
  TypedValue read(EvaluationContext paramEvaluationContext, @Nullable Object paramObject, String paramString) throws AccessException;
  
  boolean canWrite(EvaluationContext paramEvaluationContext, @Nullable Object paramObject, String paramString) throws AccessException;
  
  void write(EvaluationContext paramEvaluationContext, @Nullable Object paramObject1, String paramString, @Nullable Object paramObject2) throws AccessException;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\PropertyAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */