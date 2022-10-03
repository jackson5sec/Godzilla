/*    */ package com.jediterm.terminal.model;
/*    */ 
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ public class SubCharBuffer extends CharBuffer {
/*    */   private final CharBuffer myParent;
/*    */   private final int myOffset;
/*    */   
/*    */   public SubCharBuffer(@NotNull CharBuffer parent, int offset, int length) {
/* 10 */     super(parent.getBuf(), parent.getStart() + offset, length);
/* 11 */     this.myParent = parent;
/* 12 */     this.myOffset = offset;
/*    */   }
/*    */   @NotNull
/*    */   public CharBuffer getParent() {
/* 16 */     if (this.myParent == null) $$$reportNull$$$0(1);  return this.myParent;
/*    */   }
/*    */   
/*    */   public int getOffset() {
/* 20 */     return this.myOffset;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\terminal\model\SubCharBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */