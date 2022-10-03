/*    */ package org.fife.rsta.ui;
/*    */ 
/*    */ import java.awt.Graphics;
/*    */ import java.awt.LayoutManager;
/*    */ import javax.swing.JPanel;
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
/*    */ public class ResizableFrameContentPane
/*    */   extends JPanel
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private SizeGripIcon gripIcon;
/*    */   
/*    */   public ResizableFrameContentPane() {
/* 36 */     this.gripIcon = new SizeGripIcon();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ResizableFrameContentPane(LayoutManager layout) {
/* 46 */     super(layout);
/* 47 */     this.gripIcon = new SizeGripIcon();
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
/*    */   public void paint(Graphics g) {
/* 63 */     super.paint(g);
/* 64 */     this.gripIcon.paintIcon(this, g, getX(), getY());
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rst\\ui\ResizableFrameContentPane.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */