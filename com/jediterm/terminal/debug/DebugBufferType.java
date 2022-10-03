/*    */ package com.jediterm.terminal.debug;
/*    */ 
/*    */ import com.jediterm.terminal.LoggingTtyConnector;
/*    */ import com.jediterm.terminal.ui.TerminalSession;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum DebugBufferType
/*    */ {
/* 10 */   Back {
/*    */     public String getValue(TerminalSession session) {
/* 12 */       return session.getTerminalTextBuffer().getScreenLines();
/*    */     }
/*    */   },
/* 15 */   BackStyle {
/*    */     public String getValue(TerminalSession session) {
/* 17 */       return session.getTerminalTextBuffer().getStyleLines();
/*    */     }
/*    */   },
/* 20 */   Scroll {
/*    */     public String getValue(TerminalSession session) {
/* 22 */       return session.getTerminalTextBuffer().getHistoryBuffer().getLines();
/*    */     }
/*    */   },
/*    */   
/* 26 */   ControlSequences {
/* 27 */     private ControlSequenceVisualizer myVisualizer = new ControlSequenceVisualizer();
/*    */     
/*    */     public String getValue(TerminalSession session) {
/* 30 */       if (session.getTtyConnector() instanceof LoggingTtyConnector) {
/* 31 */         return this.myVisualizer.getVisualizedString(((LoggingTtyConnector)session.getTtyConnector()).getChunks());
/*    */       }
/* 33 */       return "Control sequences aren't logged";
/*    */     }
/*    */   };
/*    */   
/*    */   public abstract String getValue(TerminalSession paramTerminalSession);
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\terminal\debug\DebugBufferType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */