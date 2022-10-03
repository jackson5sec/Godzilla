/*    */ package com.formdev.flatlaf.ui;
/*    */ 
/*    */ import com.formdev.flatlaf.util.UIScale;
/*    */ import java.awt.Component;
/*    */ import java.awt.Insets;
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
/*    */ 
/*    */ 
/*    */ public class FlatMenuItemBorder
/*    */   extends FlatMarginBorder
/*    */ {
/* 36 */   private final Insets menuBarItemMargins = UIManager.getInsets("MenuBar.itemMargins");
/*    */ 
/*    */   
/*    */   public Insets getBorderInsets(Component c, Insets insets) {
/* 40 */     if (c.getParent() instanceof javax.swing.JMenuBar) {
/* 41 */       insets.top = UIScale.scale(this.menuBarItemMargins.top);
/* 42 */       insets.left = UIScale.scale(this.menuBarItemMargins.left);
/* 43 */       insets.bottom = UIScale.scale(this.menuBarItemMargins.bottom);
/* 44 */       insets.right = UIScale.scale(this.menuBarItemMargins.right);
/* 45 */       return insets;
/*    */     } 
/* 47 */     return super.getBorderInsets(c, insets);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatMenuItemBorder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */