package com.jediterm.terminal.ui.settings;

import com.jediterm.terminal.TtyConnector;
import com.jediterm.terminal.ui.TerminalActionPresentation;
import org.jetbrains.annotations.NotNull;

public interface TabbedSettingsProvider extends SettingsProvider {
  boolean shouldCloseTabOnLogout(TtyConnector paramTtyConnector);
  
  String tabName(TtyConnector paramTtyConnector, String paramString);
  
  @NotNull
  TerminalActionPresentation getPreviousTabActionPresentation();
  
  @NotNull
  TerminalActionPresentation getNextTabActionPresentation();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\termina\\ui\settings\TabbedSettingsProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */