package com.jediterm.terminal.ui;

import com.jediterm.terminal.Terminal;
import com.jediterm.terminal.TtyConnector;
import com.jediterm.terminal.debug.DebugBufferType;
import com.jediterm.terminal.model.TerminalTextBuffer;

public interface TerminalSession {
  void start();
  
  String getBufferText(DebugBufferType paramDebugBufferType);
  
  TerminalTextBuffer getTerminalTextBuffer();
  
  Terminal getTerminal();
  
  TtyConnector getTtyConnector();
  
  String getSessionName();
  
  void close();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\termina\\ui\TerminalSession.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */