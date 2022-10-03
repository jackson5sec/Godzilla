/*    */ package com.formdev.flatlaf.icons;
/*    */ 
/*    */ import com.formdev.flatlaf.ui.FlatUIUtils;
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
/*    */ public class FlatFileChooserNewFolderIcon
/*    */   extends FlatAbstractIcon
/*    */ {
/*    */   public FlatFileChooserNewFolderIcon() {
/* 35 */     super(16, 16, UIManager.getColor("Actions.Grey"));
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
/*    */   protected void paintIcon(Component c, Graphics2D g) {
/* 49 */     g.fill(FlatUIUtils.createPath(new double[] { 2.0D, 3.0D, 5.5D, 3.0D, 7.0D, 5.0D, 14.0D, 5.0D, 14.0D, 8.0D, 11.0D, 8.0D, 11.0D, 10.0D, 9.0D, 10.0D, 9.0D, 13.0D, 2.0D, 13.0D }));
/* 50 */     g.fill(FlatUIUtils.createPath(new double[] { 14.0D, 11.0D, 16.0D, 11.0D, 16.0D, 13.0D, 14.0D, 13.0D, 14.0D, 15.0D, 12.0D, 15.0D, 12.0D, 13.0D, 10.0D, 13.0D, 10.0D, 11.0D, 12.0D, 11.0D, 12.0D, 9.0D, 14.0D, 9.0D, 14.0D, 11.0D }));
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\icons\FlatFileChooserNewFolderIcon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */