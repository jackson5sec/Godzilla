/*    */ package org.fife.rsta.ac.demo;
/*    */ 
/*    */ import javax.swing.JApplet;
/*    */ import javax.swing.SwingUtilities;
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
/*    */ public class DemoApplet
/*    */   extends JApplet
/*    */ {
/*    */   public void init() {
/* 34 */     super.init();
/* 35 */     SwingUtilities.invokeLater(() -> {
/*    */           String laf = UIManager.getSystemLookAndFeelClassName();
/*    */           try {
/*    */             UIManager.setLookAndFeel(laf);
/* 39 */           } catch (Exception e) {
/*    */             e.printStackTrace();
/*    */           } 
/*    */           setRootPane(new DemoRootPane());
/*    */         });
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
/*    */   public void setVisible(boolean visible) {
/* 57 */     super.setVisible(visible);
/* 58 */     if (visible)
/* 59 */       ((DemoRootPane)getRootPane()).focusTextArea(); 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\demo\DemoApplet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */