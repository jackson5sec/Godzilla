/*    */ package com.formdev.flatlaf.ui;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import javax.swing.UIManager;
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
/*    */ public class FlatRoundBorder
/*    */   extends FlatBorder
/*    */ {
/* 32 */   protected final int arc = UIManager.getInt("Component.arc");
/*    */ 
/*    */   
/*    */   protected int getArc(Component c) {
/* 36 */     if (isCellEditor(c)) {
/* 37 */       return 0;
/*    */     }
/* 39 */     Boolean roundRect = FlatUIUtils.isRoundRect(c);
/* 40 */     return (roundRect != null) ? (roundRect.booleanValue() ? 32767 : 0) : this.arc;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatRoundBorder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */