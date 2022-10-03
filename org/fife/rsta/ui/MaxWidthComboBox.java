/*    */ package org.fife.rsta.ui;
/*    */ 
/*    */ import java.awt.Dimension;
/*    */ import javax.swing.ComboBoxModel;
/*    */ import javax.swing.JComboBox;
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
/*    */ public class MaxWidthComboBox<E>
/*    */   extends JComboBox<E>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private int maxWidth;
/*    */   
/*    */   public MaxWidthComboBox(int maxWidth) {
/* 44 */     this.maxWidth = maxWidth;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MaxWidthComboBox(ComboBoxModel<E> model, int maxWidth) {
/* 55 */     super(model);
/* 56 */     this.maxWidth = maxWidth;
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
/*    */   public Dimension getMaximumSize() {
/* 68 */     Dimension size = super.getMaximumSize();
/* 69 */     size.width = Math.min(size.width, this.maxWidth);
/* 70 */     return size;
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
/*    */   public Dimension getMinimumSize() {
/* 82 */     Dimension size = super.getMinimumSize();
/* 83 */     size.width = Math.min(size.width, this.maxWidth);
/* 84 */     return size;
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
/*    */   public Dimension getPreferredSize() {
/* 96 */     Dimension size = super.getPreferredSize();
/* 97 */     size.width = Math.min(size.width, this.maxWidth);
/* 98 */     return size;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rst\\ui\MaxWidthComboBox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */