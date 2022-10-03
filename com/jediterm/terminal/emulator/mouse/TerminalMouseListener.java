package com.jediterm.terminal.emulator.mouse;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public interface TerminalMouseListener {
  void mousePressed(int paramInt1, int paramInt2, MouseEvent paramMouseEvent);
  
  void mouseReleased(int paramInt1, int paramInt2, MouseEvent paramMouseEvent);
  
  void mouseMoved(int paramInt1, int paramInt2, MouseEvent paramMouseEvent);
  
  void mouseDragged(int paramInt1, int paramInt2, MouseEvent paramMouseEvent);
  
  void mouseWheelMoved(int paramInt1, int paramInt2, MouseWheelEvent paramMouseWheelEvent);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\terminal\emulator\mouse\TerminalMouseListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */