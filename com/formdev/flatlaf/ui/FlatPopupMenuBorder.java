/*    */ package com.formdev.flatlaf.ui;
/*    */ 
/*    */ import com.formdev.flatlaf.util.UIScale;
/*    */ import java.awt.Component;
/*    */ import java.awt.Container;
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
/*    */ public class FlatPopupMenuBorder
/*    */   extends FlatLineBorder
/*    */ {
/*    */   public FlatPopupMenuBorder() {
/* 38 */     super(UIManager.getInsets("PopupMenu.borderInsets"), 
/* 39 */         UIManager.getColor("PopupMenu.borderColor"));
/*    */   }
/*    */ 
/*    */   
/*    */   public Insets getBorderInsets(Component c, Insets insets) {
/* 44 */     if (c instanceof Container && ((Container)c)
/* 45 */       .getComponentCount() > 0 && ((Container)c)
/* 46 */       .getComponent(0) instanceof javax.swing.JScrollPane) {
/*    */ 
/*    */       
/* 49 */       insets.left = insets.top = insets.right = insets.bottom = UIScale.scale(1);
/* 50 */       return insets;
/*    */     } 
/*    */     
/* 53 */     return super.getBorderInsets(c, insets);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatPopupMenuBorder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */