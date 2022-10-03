package com.kitfox.svg.animation.parser;

import java.io.IOException;

public interface CharStream {
  char readChar() throws IOException;
  
  int getBeginColumn();
  
  int getBeginLine();
  
  int getEndColumn();
  
  int getEndLine();
  
  void backup(int paramInt);
  
  char beginToken() throws IOException;
  
  String getImage();
  
  char[] getSuffix(int paramInt);
  
  void done();
  
  void setTabSize(int paramInt);
  
  int getTabSize();
  
  void setTrackLineColumn(boolean paramBoolean);
  
  boolean isTrackLineColumn();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\animation\parser\CharStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */