/*    */ package com.jediterm.terminal;
/*    */ 
/*    */ import org.apache.log4j.Logger;
/*    */ 
/*    */ public enum TerminalMode {
/*  6 */   Null,
/*  7 */   CursorKey
/*    */   {
/*    */     public void setEnabled(Terminal terminal, boolean enabled) {
/* 10 */       terminal.setApplicationArrowKeys(enabled);
/*    */     }
/*    */   },
/* 13 */   ANSI,
/* 14 */   WideColumn
/*    */   {
/*    */     
/*    */     public void setEnabled(Terminal terminal, boolean enabled)
/*    */     {
/* 19 */       terminal.clearScreen();
/* 20 */       terminal.resetScrollRegions();
/*    */     }
/*    */   },
/* 23 */   CursorVisible
/*    */   {
/*    */     public void setEnabled(Terminal terminal, boolean enabled) {
/* 26 */       terminal.setCursorVisible(enabled);
/*    */     }
/*    */   },
/* 29 */   AlternateBuffer
/*    */   {
/*    */     public void setEnabled(Terminal terminal, boolean enabled) {
/* 32 */       terminal.useAlternateBuffer(enabled);
/*    */     }
/*    */   },
/* 35 */   SmoothScroll,
/* 36 */   ReverseVideo,
/* 37 */   OriginMode
/*    */   {
/*    */     
/*    */     public void setEnabled(Terminal terminal, boolean enabled) {}
/*    */   },
/* 42 */   AutoWrap
/*    */   {
/*    */ 
/*    */     
/*    */     public void setEnabled(Terminal terminal, boolean enabled) {}
/*    */   },
/* 48 */   AutoRepeatKeys,
/* 49 */   Interlace,
/* 50 */   Keypad
/*    */   {
/*    */     public void setEnabled(Terminal terminal, boolean enabled) {
/* 53 */       terminal.setApplicationKeypad(enabled);
/*    */     }
/*    */   },
/* 56 */   StoreCursor
/*    */   {
/*    */     public void setEnabled(Terminal terminal, boolean enabled) {
/* 59 */       if (enabled) {
/* 60 */         terminal.saveCursor();
/*    */       } else {
/*    */         
/* 63 */         terminal.restoreCursor();
/*    */       } 
/*    */     }
/*    */   },
/* 67 */   CursorBlinking
/*    */   {
/*    */     public void setEnabled(Terminal terminal, boolean enabled) {
/* 70 */       terminal.setBlinkingCursor(enabled);
/*    */     }
/*    */   },
/* 73 */   AllowWideColumn,
/* 74 */   ReverseWrapAround,
/* 75 */   AutoNewLine
/*    */   {
/*    */     public void setEnabled(Terminal terminal, boolean enabled) {
/* 78 */       terminal.setAutoNewLine(enabled);
/*    */     }
/*    */   },
/* 81 */   KeyboardAction,
/* 82 */   InsertMode,
/* 83 */   SendReceive,
/* 84 */   EightBitInput,
/*    */ 
/*    */   
/* 87 */   AltSendsEscape
/*    */   {
/*    */     public void setEnabled(Terminal terminal, boolean enabled)
/*    */     {
/* 91 */       terminal.setAltSendsEscape(enabled);
/*    */     }
/*    */   };
/*    */   
/*    */   static {
/* 96 */     LOG = Logger.getLogger(TerminalMode.class);
/*    */   } private static final Logger LOG;
/*    */   public void setEnabled(Terminal terminal, boolean enabled) {
/* 99 */     LOG.error("Mode " + name() + " is not implemented, setting to " + enabled);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\terminal\TerminalMode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */