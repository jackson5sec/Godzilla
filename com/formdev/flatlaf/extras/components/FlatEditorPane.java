/*    */ package com.formdev.flatlaf.extras.components;
/*    */ 
/*    */ import javax.swing.JEditorPane;
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
/*    */ public class FlatEditorPane
/*    */   extends JEditorPane
/*    */   implements FlatComponentExtension
/*    */ {
/*    */   public int getMinimumWidth() {
/* 35 */     return getClientPropertyInt("JComponent.minimumWidth", "Component.minimumWidth");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setMinimumWidth(int minimumWidth) {
/* 42 */     putClientProperty("JComponent.minimumWidth", (minimumWidth >= 0) ? Integer.valueOf(minimumWidth) : null);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\extras\components\FlatEditorPane.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */