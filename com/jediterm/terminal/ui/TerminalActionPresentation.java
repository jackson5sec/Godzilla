/*    */ package com.jediterm.terminal.ui;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import javax.swing.KeyStroke;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ public class TerminalActionPresentation
/*    */ {
/*    */   private final String myName;
/*    */   private final List<KeyStroke> myKeyStrokes;
/*    */   
/*    */   public TerminalActionPresentation(@NotNull String name, @NotNull KeyStroke keyStroke) {
/* 14 */     this(name, Collections.singletonList(keyStroke));
/*    */   }
/*    */   
/*    */   public TerminalActionPresentation(@NotNull String name, @NotNull List<KeyStroke> keyStrokes) {
/* 18 */     this.myName = name;
/* 19 */     this.myKeyStrokes = keyStrokes;
/*    */   }
/*    */   @NotNull
/*    */   public String getName() {
/* 23 */     if (this.myName == null) $$$reportNull$$$0(4);  return this.myName;
/*    */   }
/*    */   @NotNull
/*    */   public List<KeyStroke> getKeyStrokes() {
/* 27 */     if (this.myKeyStrokes == null) $$$reportNull$$$0(5);  return this.myKeyStrokes;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\termina\\ui\TerminalActionPresentation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */