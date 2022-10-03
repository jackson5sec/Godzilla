/*    */ package org.fife.rsta.ac.java;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.awt.Dimension;
/*    */ import javax.swing.JList;
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
/*    */ public class JavaParamListCellRenderer
/*    */   extends JavaCellRenderer
/*    */ {
/*    */   public JavaParamListCellRenderer() {
/* 33 */     setSimpleText(true);
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
/* 45 */     Dimension d = super.getPreferredSize();
/* 46 */     d.width += 32;
/* 47 */     return d;
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
/*    */   public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean selected, boolean hasFocus) {
/* 63 */     super.getListCellRendererComponent(list, value, index, selected, hasFocus);
/*    */     
/* 65 */     JavaSourceCompletion ajsc = (JavaSourceCompletion)value;
/* 66 */     setIcon(ajsc.getIcon());
/* 67 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\JavaParamListCellRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */