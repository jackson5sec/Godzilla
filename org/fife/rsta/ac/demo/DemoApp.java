/*    */ package org.fife.rsta.ac.demo;
/*    */ 
/*    */ import java.awt.Toolkit;
/*    */ import javax.swing.JFrame;
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
/*    */ public class DemoApp
/*    */   extends JFrame
/*    */ {
/*    */   public DemoApp() {
/* 27 */     setRootPane(new DemoRootPane());
/* 28 */     setDefaultCloseOperation(3);
/* 29 */     setTitle("RSTA Language Support Demo Application");
/* 30 */     pack();
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
/*    */   public void setVisible(boolean visible) {
/* 42 */     super.setVisible(visible);
/* 43 */     if (visible) {
/* 44 */       ((DemoRootPane)getRootPane()).focusTextArea();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public static void main(String[] args) {
/* 50 */     SwingUtilities.invokeLater(() -> {
/*    */ 
/*    */           
/*    */           try {
/*    */             UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
/* 55 */           } catch (Exception e) {
/*    */             e.printStackTrace();
/*    */           } 
/*    */           Toolkit.getDefaultToolkit().setDynamicLayout(true);
/*    */           (new DemoApp()).setVisible(true);
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\demo\DemoApp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */