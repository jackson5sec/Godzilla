package com.jediterm.terminal;

import com.jediterm.terminal.model.CharBuffer;

public class StyledTextConsumerAdapter implements StyledTextConsumer {
  public void consume(int x, int y, TextStyle style, CharBuffer characters, int startRow) {}
  
  public void consumeNul(int x, int y, int nulIndex, TextStyle style, CharBuffer characters, int startRow) {}
  
  public void consumeQueue(int x, int y, int nulIndex, int startRow) {}
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\terminal\StyledTextConsumerAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */