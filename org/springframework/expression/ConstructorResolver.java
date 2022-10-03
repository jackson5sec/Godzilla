package org.springframework.expression;

import java.util.List;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.lang.Nullable;

@FunctionalInterface
public interface ConstructorResolver {
  @Nullable
  ConstructorExecutor resolve(EvaluationContext paramEvaluationContext, String paramString, List<TypeDescriptor> paramList) throws AccessException;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\ConstructorResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */