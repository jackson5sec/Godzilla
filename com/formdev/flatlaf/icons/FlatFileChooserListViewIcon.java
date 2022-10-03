/*    */ package com.formdev.flatlaf.icons;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.awt.Graphics2D;
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
/*    */ public class FlatFileChooserListViewIcon
/*    */   extends FlatAbstractIcon
/*    */ {
/*    */   public FlatFileChooserListViewIcon() {
/* 34 */     super(16, 16, UIManager.getColor("Actions.Grey"));
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
/*    */   
/*    */   protected void paintIcon(Component c, Graphics2D g) {
/* 50 */     g.fillRect(3, 3, 4, 4);
/* 51 */     g.fillRect(3, 9, 4, 4);
/* 52 */     g.fillRect(9, 9, 4, 4);
/* 53 */     g.fillRect(9, 3, 4, 4);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\icons\FlatFileChooserListViewIcon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */