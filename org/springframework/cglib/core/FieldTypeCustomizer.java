package org.springframework.cglib.core;

import org.springframework.asm.Type;

public interface FieldTypeCustomizer extends KeyFactoryCustomizer {
  void customize(CodeEmitter paramCodeEmitter, int paramInt, Type paramType);
  
  Type getOutType(int paramInt, Type paramType);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\core\FieldTypeCustomizer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */