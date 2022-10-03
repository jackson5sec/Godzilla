package com.kitfox.svg.animation.parser;

import java.io.Serializable;

public interface Node extends Serializable {
  void jjtOpen();
  
  void jjtClose();
  
  void jjtSetParent(Node paramNode);
  
  Node jjtGetParent();
  
  void jjtAddChild(Node paramNode, int paramInt);
  
  Node jjtGetChild(int paramInt);
  
  int jjtGetNumChildren();
  
  int getId();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\animation\parser\Node.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */