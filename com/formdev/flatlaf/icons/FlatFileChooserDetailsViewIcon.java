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
/*    */ public class FlatFileChooserDetailsViewIcon
/*    */   extends FlatAbstractIcon
/*    */ {
/*    */   public FlatFileChooserDetailsViewIcon() {
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
/*    */ 
/*    */   
/*    */   protected void paintIcon(Component c, Graphics2D g) {
/* 52 */     g.fillRect(2, 3, 2, 2);
/* 53 */     g.fillRect(2, 7, 2, 2);
/* 54 */     g.fillRect(2, 11, 2, 2);
/* 55 */     g.fillRect(6, 3, 8, 2);
/* 56 */     g.fillRect(6, 7, 8, 2);
/* 57 */     g.fillRect(6, 11, 8, 2);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\icons\FlatFileChooserDetailsViewIcon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */