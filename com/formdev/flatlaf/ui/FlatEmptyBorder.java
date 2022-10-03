/*    */ package com.formdev.flatlaf.ui;
/*    */ 
/*    */ import com.formdev.flatlaf.util.UIScale;
/*    */ import java.awt.Component;
/*    */ import java.awt.Insets;
/*    */ import javax.swing.plaf.BorderUIResource;
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
/*    */ public class FlatEmptyBorder
/*    */   extends BorderUIResource.EmptyBorderUIResource
/*    */ {
/*    */   public FlatEmptyBorder() {
/* 35 */     super(0, 0, 0, 0);
/*    */   }
/*    */   
/*    */   public FlatEmptyBorder(int top, int left, int bottom, int right) {
/* 39 */     super(top, left, bottom, right);
/*    */   }
/*    */   
/*    */   public FlatEmptyBorder(Insets insets) {
/* 43 */     super(insets);
/*    */   }
/*    */ 
/*    */   
/*    */   public Insets getBorderInsets() {
/* 48 */     return new Insets(UIScale.scale(this.top), UIScale.scale(this.left), UIScale.scale(this.bottom), UIScale.scale(this.right));
/*    */   }
/*    */ 
/*    */   
/*    */   public Insets getBorderInsets(Component c, Insets insets) {
/* 53 */     boolean leftToRight = (this.left == this.right || c.getComponentOrientation().isLeftToRight());
/* 54 */     insets.left = UIScale.scale(leftToRight ? this.left : this.right);
/* 55 */     insets.top = UIScale.scale(this.top);
/* 56 */     insets.right = UIScale.scale(leftToRight ? this.right : this.left);
/* 57 */     insets.bottom = UIScale.scale(this.bottom);
/* 58 */     return insets;
/*    */   }
/*    */   
/*    */   public Insets getUnscaledBorderInsets() {
/* 62 */     return super.getBorderInsets();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatEmptyBorder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */