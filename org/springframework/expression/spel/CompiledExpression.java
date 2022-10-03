package org.springframework.expression.spel;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.EvaluationException;
import org.springframework.lang.Nullable;

public abstract class CompiledExpression {
  public abstract Object getValue(@Nullable Object paramObject, @Nullable EvaluationContext paramEvaluationContext) throws EvaluationException;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\CompiledExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */