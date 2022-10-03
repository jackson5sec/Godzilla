package com.kichik.pecoff4j.asm;

public interface Instruction {
  int size();
  
  byte[] toCode();
  
  String toIntelAssembly();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\asm\Instruction.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */