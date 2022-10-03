package org.springframework.expression;

public interface MethodExecutor {
  TypedValue execute(EvaluationContext paramEvaluationContext, Object paramObject, Object... paramVarArgs) throws AccessException;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\MethodExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */