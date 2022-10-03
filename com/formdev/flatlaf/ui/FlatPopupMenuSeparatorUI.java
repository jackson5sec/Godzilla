/*    */ package com.formdev.flatlaf.ui;
/*    */ 
/*    */ import java.util.function.Supplier;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.plaf.ComponentUI;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FlatPopupMenuSeparatorUI
/*    */   extends FlatSeparatorUI
/*    */ {
/*    */   public static ComponentUI createUI(JComponent c) {
/* 42 */     return FlatUIUtils.createSharedUI(FlatPopupMenuSeparatorUI.class, FlatPopupMenuSeparatorUI::new);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getPropertyPrefix() {
/* 47 */     return "PopupMenuSeparator";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatPopupMenuSeparatorUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */