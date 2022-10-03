/*    */ package org.fife.rsta.ui;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Component;
/*    */ import java.awt.ComponentOrientation;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Graphics;
/*    */ import javax.swing.Icon;
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
/*    */ public class SizeGripIcon
/*    */   implements Icon
/*    */ {
/*    */   private static final int SIZE = 16;
/*    */   
/*    */   public int getIconHeight() {
/* 32 */     return 16;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getIconWidth() {
/* 43 */     return 16;
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
/*    */   public void paintIcon(Component c, Graphics g, int x, int y) {
/* 58 */     Dimension dim = c.getSize();
/* 59 */     Color c1 = UIManager.getColor("Label.disabledShadow");
/* 60 */     Color c2 = UIManager.getColor("Label.disabledForeground");
/*    */     
/* 62 */     ComponentOrientation orientation = c.getComponentOrientation();
/* 63 */     int height = dim.height -= 3;
/*    */     
/* 65 */     if (orientation.isLeftToRight()) {
/* 66 */       int width = dim.width -= 3;
/* 67 */       g.setColor(c1);
/* 68 */       g.fillRect(width - 9, height - 1, 3, 3);
/* 69 */       g.fillRect(width - 5, height - 1, 3, 3);
/* 70 */       g.fillRect(width - 1, height - 1, 3, 3);
/* 71 */       g.fillRect(width - 5, height - 5, 3, 3);
/* 72 */       g.fillRect(width - 1, height - 5, 3, 3);
/* 73 */       g.fillRect(width - 1, height - 9, 3, 3);
/* 74 */       g.setColor(c2);
/* 75 */       g.fillRect(width - 9, height - 1, 2, 2);
/* 76 */       g.fillRect(width - 5, height - 1, 2, 2);
/* 77 */       g.fillRect(width - 1, height - 1, 2, 2);
/* 78 */       g.fillRect(width - 5, height - 5, 2, 2);
/* 79 */       g.fillRect(width - 1, height - 5, 2, 2);
/* 80 */       g.fillRect(width - 1, height - 9, 2, 2);
/*    */     } else {
/*    */       
/* 83 */       g.setColor(c1);
/* 84 */       g.fillRect(10, height - 1, 3, 3);
/* 85 */       g.fillRect(6, height - 1, 3, 3);
/* 86 */       g.fillRect(2, height - 1, 3, 3);
/* 87 */       g.fillRect(6, height - 5, 3, 3);
/* 88 */       g.fillRect(2, height - 5, 3, 3);
/* 89 */       g.fillRect(2, height - 9, 3, 3);
/* 90 */       g.setColor(c2);
/* 91 */       g.fillRect(10, height - 1, 2, 2);
/* 92 */       g.fillRect(6, height - 1, 2, 2);
/* 93 */       g.fillRect(2, height - 1, 2, 2);
/* 94 */       g.fillRect(6, height - 5, 2, 2);
/* 95 */       g.fillRect(2, height - 5, 2, 2);
/* 96 */       g.fillRect(2, height - 9, 2, 2);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rst\\ui\SizeGripIcon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */