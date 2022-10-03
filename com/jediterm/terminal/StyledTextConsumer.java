package com.jediterm.terminal;

import com.jediterm.terminal.model.CharBuffer;
import org.jetbrains.annotations.NotNull;

public interface StyledTextConsumer {
  void consume(int paramInt1, int paramInt2, @NotNull TextStyle paramTextStyle, @NotNull CharBuffer paramCharBuffer, int paramInt3);
  
  void consumeNul(int paramInt1, int paramInt2, int paramInt3, @NotNull TextStyle paramTextStyle, @NotNull CharBuffer paramCharBuffer, int paramInt4);
  
  void consumeQueue(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\terminal\StyledTextConsumer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */