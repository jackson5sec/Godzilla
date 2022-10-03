/*    */ package com.jediterm.terminal.emulator.charset;
/*    */ 
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GraphicSet
/*    */ {
/*    */   private final int myIndex;
/*    */   private CharacterSet myDesignation;
/*    */   
/*    */   public GraphicSet(int index) {
/* 15 */     if (index < 0 || index > 3)
/*    */     {
/* 17 */       throw new IllegalArgumentException("Invalid index!");
/*    */     }
/* 19 */     this.myIndex = index;
/*    */     
/* 21 */     this.myDesignation = CharacterSet.valueOf((index == 1) ? 48 : 66);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CharacterSet getDesignation() {
/* 29 */     return this.myDesignation;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getIndex() {
/* 37 */     return this.myIndex;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int map(char original, int index) {
/* 52 */     int result = this.myDesignation.map(index);
/* 53 */     if (result < 0)
/*    */     {
/*    */       
/* 56 */       result = original;
/*    */     }
/* 58 */     return result;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setDesignation(@NotNull CharacterSet designation) {
/* 66 */     if (designation == null) $$$reportNull$$$0(0);  this.myDesignation = designation;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\terminal\emulator\charset\GraphicSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */