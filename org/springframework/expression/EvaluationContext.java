package org.springframework.expression;

import java.util.List;
import org.springframework.lang.Nullable;

public interface EvaluationContext {
  TypedValue getRootObject();
  
  List<PropertyAccessor> getPropertyAccessors();
  
  List<ConstructorResolver> getConstructorResolvers();
  
  List<MethodResolver> getMethodResolvers();
  
  @Nullable
  BeanResolver getBeanResolver();
  
  TypeLocator getTypeLocator();
  
  TypeConverter getTypeConverter();
  
  TypeComparator getTypeComparator();
  
  OperatorOverloader getOperatorOverloader();
  
  void setVariable(String paramString, @Nullable Object paramObject);
  
  @Nullable
  Object lookupVariable(String paramString);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\EvaluationContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */