package org.fife.rsta.ac.java.rjc.ast;

import org.fife.rsta.ac.java.rjc.lang.Modifiers;
import org.fife.rsta.ac.java.rjc.lang.Type;

public interface Member extends ASTNode {
  String getDocComment();
  
  int getNameEndOffset();
  
  int getNameStartOffset();
  
  Modifiers getModifiers();
  
  String getName();
  
  TypeDeclaration getParentTypeDeclaration();
  
  Type getType();
  
  boolean isDeprecated();
  
  boolean isStatic();
  
  void setParentTypeDeclaration(TypeDeclaration paramTypeDeclaration);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\rjc\ast\Member.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */