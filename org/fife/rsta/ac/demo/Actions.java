/*     */ package org.fife.rsta.ac.demo;
/*     */ 
/*     */ import java.awt.event.ActionEvent;
/*     */ import javax.swing.AbstractAction;
/*     */ import javax.swing.JFileChooser;
/*     */ import javax.swing.KeyStroke;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.text.DefaultHighlighter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ interface Actions
/*     */ {
/*     */   public static class AboutAction
/*     */     extends AbstractAction
/*     */   {
/*     */     private DemoRootPane demo;
/*     */     
/*     */     public AboutAction(DemoRootPane demo) {
/*  42 */       this.demo = demo;
/*  43 */       putValue("Name", "About RSTALanguageSupport...");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void actionPerformed(ActionEvent e) {
/*  49 */       AboutDialog ad = new AboutDialog((DemoApp)SwingUtilities.getWindowAncestor(this.demo));
/*  50 */       ad.setLocationRelativeTo(this.demo);
/*  51 */       ad.setVisible(true);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class ExitAction
/*     */     extends AbstractAction
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */ 
/*     */ 
/*     */     
/*     */     public ExitAction() {
/*  65 */       putValue("Name", "Exit");
/*  66 */       putValue("MnemonicKey", Integer.valueOf(120));
/*     */     }
/*     */ 
/*     */     
/*     */     public void actionPerformed(ActionEvent e) {
/*  71 */       System.exit(0);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class OpenAction
/*     */     extends AbstractAction
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     private DemoRootPane demo;
/*     */     
/*     */     private JFileChooser chooser;
/*     */ 
/*     */     
/*     */     public OpenAction(DemoRootPane demo) {
/*  88 */       this.demo = demo;
/*  89 */       putValue("Name", "Open...");
/*  90 */       putValue("MnemonicKey", Integer.valueOf(79));
/*  91 */       int mods = demo.getToolkit().getMenuShortcutKeyMask();
/*  92 */       KeyStroke ks = KeyStroke.getKeyStroke(79, mods);
/*  93 */       putValue("AcceleratorKey", ks);
/*     */     }
/*     */ 
/*     */     
/*     */     public void actionPerformed(ActionEvent e) {
/*  98 */       if (this.chooser == null) {
/*  99 */         this.chooser = new JFileChooser();
/* 100 */         this.chooser.setFileFilter(new ExtensionFileFilter("Java Source Files", "java"));
/*     */       } 
/*     */       
/* 103 */       int rc = this.chooser.showOpenDialog(this.demo);
/* 104 */       if (rc == 0) {
/* 105 */         this.demo.openFile(this.chooser.getSelectedFile());
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class LookAndFeelAction
/*     */     extends AbstractAction
/*     */   {
/*     */     private UIManager.LookAndFeelInfo info;
/*     */     
/*     */     private DemoRootPane demo;
/*     */ 
/*     */     
/*     */     public LookAndFeelAction(DemoRootPane demo, UIManager.LookAndFeelInfo info) {
/* 121 */       putValue("Name", info.getName());
/* 122 */       this.demo = demo;
/* 123 */       this.info = info;
/*     */     }
/*     */ 
/*     */     
/*     */     public void actionPerformed(ActionEvent e) {
/*     */       try {
/* 129 */         UIManager.setLookAndFeel(this.info.getClassName());
/* 130 */         SwingUtilities.updateComponentTreeUI(this.demo);
/* 131 */       } catch (RuntimeException re) {
/* 132 */         throw re;
/* 133 */       } catch (Exception ex) {
/* 134 */         ex.printStackTrace();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class StyleAction
/*     */     extends AbstractAction
/*     */   {
/*     */     private DemoRootPane demo;
/*     */     
/*     */     private String res;
/*     */     
/*     */     private String style;
/*     */ 
/*     */     
/*     */     public StyleAction(DemoRootPane demo, String name, String res, String style) {
/* 152 */       putValue("Name", name);
/* 153 */       this.demo = demo;
/* 154 */       this.res = res;
/* 155 */       this.style = style;
/*     */     }
/*     */ 
/*     */     
/*     */     public void actionPerformed(ActionEvent e) {
/* 160 */       this.demo.setText(this.res, this.style);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class ToggleLayeredHighlightsAction
/*     */     extends AbstractAction
/*     */   {
/*     */     private DemoRootPane demo;
/*     */     
/*     */     public ToggleLayeredHighlightsAction(DemoRootPane demo) {
/* 171 */       this.demo = demo;
/* 172 */       putValue("Name", "Layered Selection Highlights");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void actionPerformed(ActionEvent e) {
/* 178 */       DefaultHighlighter h = (DefaultHighlighter)this.demo.getTextArea().getHighlighter();
/* 179 */       h.setDrawsLayeredHighlights(!h.getDrawsLayeredHighlights());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\demo\Actions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */