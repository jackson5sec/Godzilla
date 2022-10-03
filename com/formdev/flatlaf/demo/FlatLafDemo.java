/*    */ package com.formdev.flatlaf.demo;
/*    */ 
/*    */ import com.formdev.flatlaf.util.SystemInfo;
/*    */ import java.awt.Component;
/*    */ import java.awt.Dimension;
/*    */ import javax.swing.JDialog;
/*    */ import javax.swing.JFrame;
/*    */ import javax.swing.SwingUtilities;
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
/*    */ public class FlatLafDemo
/*    */ {
/*    */   static final String PREFS_ROOT_PATH = "/flatlaf-demo";
/*    */   static final String KEY_TAB = "tab";
/* 36 */   static boolean screenshotsMode = Boolean.parseBoolean(System.getProperty("flatlaf.demo.screenshotsMode"));
/*    */ 
/*    */   
/*    */   public static void main(String[] args) {
/* 40 */     if (SystemInfo.isMacOS && System.getProperty("apple.laf.useScreenMenuBar") == null) {
/* 41 */       System.setProperty("apple.laf.useScreenMenuBar", "true");
/*    */     }
/*    */     
/* 44 */     if (screenshotsMode && !SystemInfo.isJava_9_orLater && System.getProperty("flatlaf.uiScale") == null) {
/* 45 */       System.setProperty("flatlaf.uiScale", "2x");
/*    */     }
/*    */     
/* 48 */     SwingUtilities.invokeLater(() -> {
/*    */           DemoPrefs.init("/flatlaf-demo");
/*    */           JFrame.setDefaultLookAndFeelDecorated(true);
/*    */           JDialog.setDefaultLookAndFeelDecorated(true);
/*    */           DemoPrefs.initLaf(args);
/*    */           DemoFrame frame = new DemoFrame();
/*    */           if (screenshotsMode)
/*    */             frame.setPreferredSize(new Dimension(1660, 840)); 
/*    */           frame.pack();
/*    */           frame.setLocationRelativeTo((Component)null);
/*    */           frame.setVisible(true);
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\demo\FlatLafDemo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */