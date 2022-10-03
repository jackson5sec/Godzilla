/*    */ package com.jediterm.terminal.ui.settings;
/*    */ 
/*    */ import com.jediterm.terminal.TtyConnector;
/*    */ import com.jediterm.terminal.ui.TerminalActionPresentation;
/*    */ import com.jediterm.terminal.ui.UIUtil;
/*    */ import javax.swing.KeyStroke;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DefaultTabbedSettingsProvider
/*    */   extends DefaultSettingsProvider
/*    */   implements TabbedSettingsProvider
/*    */ {
/*    */   public boolean shouldCloseTabOnLogout(TtyConnector ttyConnector) {
/* 18 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public String tabName(TtyConnector ttyConnector, String sessionName) {
/* 23 */     return sessionName;
/*    */   }
/*    */   
/*    */   @NotNull
/*    */   public TerminalActionPresentation getPreviousTabActionPresentation() {
/* 28 */     return new TerminalActionPresentation("Previous Tab", UIUtil.isMac ? 
/* 29 */         KeyStroke.getKeyStroke(37, 128) : 
/* 30 */         KeyStroke.getKeyStroke(37, 512));
/*    */   }
/*    */   
/*    */   @NotNull
/*    */   public TerminalActionPresentation getNextTabActionPresentation() {
/* 35 */     return new TerminalActionPresentation("Next Tab", UIUtil.isMac ? 
/* 36 */         KeyStroke.getKeyStroke(39, 128) : 
/* 37 */         KeyStroke.getKeyStroke(39, 512));
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\termina\\ui\settings\DefaultTabbedSettingsProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */