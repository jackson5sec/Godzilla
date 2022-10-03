/*    */ package com.formdev.flatlaf.ui;
/*    */ 
/*    */ import java.util.function.Supplier;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.plaf.ComponentUI;
/*    */ import javax.swing.plaf.basic.BasicPanelUI;
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
/*    */ public class FlatPanelUI
/*    */   extends BasicPanelUI
/*    */ {
/*    */   public static ComponentUI createUI(JComponent c) {
/* 39 */     return FlatUIUtils.createSharedUI(FlatPanelUI.class, FlatPanelUI::new);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatPanelUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */