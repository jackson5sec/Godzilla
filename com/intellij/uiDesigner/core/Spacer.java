/*    */ package com.intellij.uiDesigner.core;
/*    */ 
/*    */ import java.awt.Dimension;
/*    */ import javax.swing.JComponent;
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
/*    */ public class Spacer
/*    */   extends JComponent
/*    */ {
/*    */   public Dimension getMinimumSize() {
/* 23 */     return new Dimension(0, 0);
/*    */   }
/*    */   
/*    */   public final Dimension getPreferredSize() {
/* 27 */     return getMinimumSize();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\core\Spacer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */