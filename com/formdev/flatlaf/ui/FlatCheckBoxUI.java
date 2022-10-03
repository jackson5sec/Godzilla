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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FlatCheckBoxUI
/*    */   extends FlatRadioButtonUI
/*    */ {
/*    */   public static ComponentUI createUI(JComponent c) {
/* 46 */     return FlatUIUtils.createSharedUI(FlatCheckBoxUI.class, FlatCheckBoxUI::new);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getPropertyPrefix() {
/* 51 */     return "CheckBox.";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatCheckBoxUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */