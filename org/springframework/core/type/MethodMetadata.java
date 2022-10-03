package org.springframework.core.type;

public interface MethodMetadata extends AnnotatedTypeMetadata {
  String getMethodName();
  
  String getDeclaringClassName();
  
  String getReturnTypeName();
  
  boolean isAbstract();
  
  boolean isStatic();
  
  boolean isFinal();
  
  boolean isOverridable();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\type\MethodMetadata.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */