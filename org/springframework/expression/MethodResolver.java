package org.springframework.expression;

import java.util.List;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.lang.Nullable;

public interface MethodResolver {
  @Nullable
  MethodExecutor resolve(EvaluationContext paramEvaluationContext, Object paramObject, String paramString, List<TypeDescriptor> paramList) throws AccessException;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\MethodResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */