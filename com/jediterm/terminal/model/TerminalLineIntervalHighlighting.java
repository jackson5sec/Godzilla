/*    */ package com.jediterm.terminal.model;
/*    */ 
/*    */ import com.jediterm.terminal.TerminalColor;
/*    */ import com.jediterm.terminal.TextStyle;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ public abstract class TerminalLineIntervalHighlighting {
/*    */   private final TerminalLine myLine;
/*    */   private final int myStartOffset;
/*    */   private final int myEndOffset;
/*    */   private final TextStyle myStyle;
/*    */   private boolean myDisposed = false;
/*    */   
/*    */   TerminalLineIntervalHighlighting(@NotNull TerminalLine line, int startOffset, int length, @NotNull TextStyle style) {
/* 15 */     if (startOffset < 0) {
/* 16 */       throw new IllegalArgumentException("Negative startOffset: " + startOffset);
/*    */     }
/* 18 */     if (length < 0) {
/* 19 */       throw new IllegalArgumentException("Negative length: " + length);
/*    */     }
/* 21 */     this.myLine = line;
/* 22 */     this.myStartOffset = startOffset;
/* 23 */     this.myEndOffset = startOffset + length;
/* 24 */     this.myStyle = style;
/*    */   }
/*    */   @NotNull
/*    */   public TerminalLine getLine() {
/* 28 */     if (this.myLine == null) $$$reportNull$$$0(2);  return this.myLine;
/*    */   }
/*    */   
/*    */   public int getStartOffset() {
/* 32 */     return this.myStartOffset;
/*    */   }
/*    */   
/*    */   public int getEndOffset() {
/* 36 */     return this.myEndOffset;
/*    */   }
/*    */   
/*    */   public int getLength() {
/* 40 */     return this.myEndOffset - this.myStartOffset;
/*    */   }
/*    */   
/*    */   public boolean isDisposed() {
/* 44 */     return this.myDisposed;
/*    */   }
/*    */   
/*    */   public final void dispose() {
/* 48 */     doDispose();
/* 49 */     this.myDisposed = true;
/*    */   }
/*    */   
/*    */   protected abstract void doDispose();
/*    */   
/*    */   public boolean intersectsWith(int otherStartOffset, int otherEndOffset) {
/* 55 */     return (this.myEndOffset > otherStartOffset && otherEndOffset > this.myStartOffset);
/*    */   }
/*    */   @NotNull
/*    */   public TextStyle mergeWith(@NotNull TextStyle style) {
/* 59 */     if (style == null) $$$reportNull$$$0(3);  TerminalColor foreground = this.myStyle.getForeground();
/* 60 */     if (foreground == null) {
/* 61 */       foreground = style.getForeground();
/*    */     }
/* 63 */     TerminalColor background = this.myStyle.getBackground();
/* 64 */     if (background == null) {
/* 65 */       background = style.getBackground();
/*    */     }
/* 67 */     return new TextStyle(foreground, background);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 72 */     return "startOffset=" + this.myStartOffset + ", endOffset=" + this.myEndOffset + ", disposed=" + this.myDisposed;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\terminal\model\TerminalLineIntervalHighlighting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */