package com.jediterm.terminal;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TerminalCopyPasteHandler {
  void setContents(@NotNull String paramString, boolean paramBoolean);
  
  @Nullable
  String getContents(boolean paramBoolean);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\terminal\TerminalCopyPasteHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */