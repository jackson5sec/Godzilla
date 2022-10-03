package javassist.compiler;

import javassist.bytecode.Bytecode;
import javassist.compiler.ast.ASTList;

public interface ProceedHandler {
  void doit(JvstCodeGen paramJvstCodeGen, Bytecode paramBytecode, ASTList paramASTList) throws CompileError;
  
  void setReturnType(JvstTypeChecker paramJvstTypeChecker, ASTList paramASTList) throws CompileError;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\compiler\ProceedHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */