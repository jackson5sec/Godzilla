/*   */ package com.jediterm.terminal.ui;
/*   */ 
/*   */ 
/*   */ 
/*   */ public abstract class TerminalActionProviderBase
/*   */   implements TerminalActionProvider
/*   */ {
/*   */   public TerminalActionProvider getNextProvider() {
/* 9 */     return null;
/*   */   }
/*   */   
/*   */   public void setNextProvider(TerminalActionProvider provider) {}
/*   */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\termina\\ui\TerminalActionProviderBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */