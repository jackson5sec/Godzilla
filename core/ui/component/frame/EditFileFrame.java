/*    */ package core.ui.component.frame;
/*    */ 
/*    */ import core.EasyI18N;
/*    */ import core.ui.component.RTabbedPane;
/*    */ import core.ui.component.ShellRSFilePanel;
/*    */ import java.awt.Component;
/*    */ import java.awt.Container;
/*    */ import java.io.File;
/*    */ import javax.swing.JFrame;
/*    */ import javax.swing.JLabel;
/*    */ import javax.swing.event.ChangeEvent;
/*    */ import javax.swing.event.ChangeListener;
/*    */ import util.functions;
/*    */ 
/*    */ 
/*    */ public class EditFileFrame
/*    */   extends JFrame
/*    */ {
/*    */   private static final String TITLE = "{fileName}     shellId:{shellId}     - Godzilla-Notepad";
/* 20 */   private static final JLabel NO_FILE = new JLabel("NO_FILE");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 30 */     NO_FILE.setHorizontalAlignment(0);
/* 31 */   } private static EditFileFrame editFileMainFrame = new EditFileFrame(); private static boolean isShow;
/*    */   private RTabbedPane tabbedPane;
/*    */   private Container contentPane;
/*    */   
/*    */   private EditFileFrame() {
/* 36 */     super("Godzilla-Notepad");
/*    */     
/* 38 */     this.contentPane = getContentPane();
/* 39 */     this.tabbedPane = new RTabbedPane();
/*    */ 
/*    */     
/* 42 */     this.contentPane.add((Component)this.tabbedPane);
/* 43 */     setContentPane(NO_FILE);
/* 44 */     this.tabbedPane.setRemoveListener((currentSize, removeSzie) -> {
/*    */           if (currentSize <= 0) {
/*    */             setContentPane(NO_FILE);
/*    */           }
/*    */         });
/* 49 */     this.tabbedPane.addChangeListener(new ChangeListener()
/*    */         {
/*    */           public void stateChanged(ChangeEvent e) {
/* 52 */             ShellRSFilePanel rsFilePanel = (ShellRSFilePanel)EditFileFrame.this.tabbedPane.getSelectedComponent();
/* 53 */             if (rsFilePanel != null) {
/* 54 */               EditFileFrame.this.setTitle(EditFileFrame.getTitle(rsFilePanel.getFile(), rsFilePanel));
/*    */             } else {
/* 56 */               EditFileFrame.this.setTitle("Godzilla-Notepad");
/*    */             } 
/*    */           }
/*    */         });
/* 60 */     EasyI18N.installObject(this);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static void OpenNewEdit(ShellRSFilePanel shellRSFilePanel) {
/* 66 */     if (!isShow) {
/* 67 */       functions.setWindowSize(editFileMainFrame, 1400, 521);
/*    */     }
/* 69 */     if (shellRSFilePanel == null) {
/*    */       return;
/*    */     }
/* 72 */     if (editFileMainFrame.tabbedPane.getTabCount() == 0) {
/* 73 */       editFileMainFrame.setContentPane(editFileMainFrame.contentPane);
/*    */     }
/* 75 */     editFileMainFrame.tabbedPane.addTab((new File(shellRSFilePanel.getFile())).getName(), (Component)shellRSFilePanel);
/* 76 */     editFileMainFrame.tabbedPane.setSelectedComponent((Component)shellRSFilePanel);
/* 77 */     editFileMainFrame.setVisible(true);
/* 78 */     if (!isShow) {
/* 79 */       editFileMainFrame.setLocationRelativeTo(null);
/* 80 */       isShow = true;
/*    */     } 
/*    */   }
/*    */   
/*    */   public static void removeEdit(ShellRSFilePanel shellRSFilePanel) {
/* 85 */     editFileMainFrame.tabbedPane.remove((Component)shellRSFilePanel);
/*    */   }
/*    */   
/*    */   private static String getTitle(String fileName, ShellRSFilePanel shellRSFilePanel) {
/* 89 */     return "{fileName}     shellId:{shellId}     - Godzilla-Notepad".replace("{fileName}", fileName).replace("{shellId}", shellRSFilePanel.getShellId());
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\cor\\ui\component\frame\EditFileFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */