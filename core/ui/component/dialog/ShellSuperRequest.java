/*     */ package core.ui.component.dialog;
/*     */ 
/*     */ import core.ApplicationContext;
/*     */ import core.Db;
/*     */ import core.EasyI18N;
/*     */ import core.ui.MainActivity;
/*     */ import core.ui.component.RTextArea;
/*     */ import core.ui.component.SimplePanel;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Frame;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JSplitPane;
/*     */ import javax.swing.JTabbedPane;
/*     */ import javax.swing.JTextField;
/*     */ import util.Log;
/*     */ import util.OpenC;
/*     */ import util.automaticBindClick;
/*     */ import util.functions;
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
/*     */ public class ShellSuperRequest
/*     */   extends JDialog
/*     */ {
/*     */   private final JTabbedPane tabbedPane;
/*     */   private final SimplePanel basicConfigPanel;
/*     */   private final SimplePanel randomUaPanel;
/*     */   private final SimplePanel requestParameterNamePanel;
/*     */   private JLabel openRandomRequestParameterLabel;
/*     */   private JCheckBox openRandomRequestParameterCheckBox;
/*     */   private JLabel randomRequestParameterNumLabel;
/*     */   private JTextField randomRequestParameterNumTextField;
/*     */   private JLabel randomRequestParameterNameSizeLabel;
/*     */   private JTextField randomRequestParameterNameSizeTextField;
/*     */   private JLabel randomRequestParameterSizeLabel;
/*     */   private JTextField randomRequestParameterSizeTextField;
/*     */   private JLabel openRandomUaLabel;
/*     */   private JCheckBox openRandomUaCheckBox;
/*     */   private JLabel openRandomReqParameterLabel;
/*     */   private JCheckBox openRandomReqParameterCheckBox;
/*     */   private JLabel randomReqParameterSizeLabel;
/*     */   private JTextField randomReqParameterSizeTextField;
/*     */   private RTextArea uaTextArea;
/*     */   private JButton basicConfigUpdateButton;
/*     */   private JButton uaUpdateButton;
/*     */   private JButton requestParameterNameUpdateButton;
/*     */   
/*     */   public ShellSuperRequest() {
/*  72 */     super((Frame)MainActivity.getFrame(), "ShellSuperRequest", true);
/*     */     
/*  74 */     this.tabbedPane = new JTabbedPane();
/*     */     
/*  76 */     this.basicConfigPanel = new SimplePanel();
/*  77 */     this.randomUaPanel = new SimplePanel(new BorderLayout(1, 1));
/*  78 */     this.requestParameterNamePanel = new SimplePanel(new BorderLayout(1, 1));
/*     */     
/*  80 */     initbasicConfigPanel();
/*  81 */     initRandomUaPanel();
/*     */     
/*  83 */     this.tabbedPane.addTab("配置", (Component)this.basicConfigPanel);
/*  84 */     this.tabbedPane.addTab("随机UA", (Component)this.randomUaPanel);
/*     */     
/*  86 */     add(this.tabbedPane);
/*     */     
/*  88 */     automaticBindClick.bindJButtonClick(this, this);
/*  89 */     functions.setWindowSize(this, 650, 500);
/*  90 */     setLocationRelativeTo((Component)MainActivity.getFrame());
/*  91 */     EasyI18N.installObject(this);
/*  92 */     setVisible(true);
/*     */   }
/*     */   
/*     */   private void initbasicConfigPanel() {
/*  96 */     this.openRandomRequestParameterLabel = new JLabel("随机请求参数: ");
/*  97 */     this.openRandomUaLabel = new JLabel("随机Ua: ");
/*  98 */     this.randomRequestParameterSizeLabel = new JLabel("随机请求参数值大小: ");
/*  99 */     this.randomRequestParameterNumLabel = new JLabel("随机请求参数数量: ");
/* 100 */     this.randomRequestParameterNameSizeLabel = new JLabel("随机请求参数名大小: ");
/* 101 */     this.openRandomReqParameterLabel = new JLabel("随机ReqParameter: ");
/* 102 */     this.randomReqParameterSizeLabel = new JLabel("随机ReqParameter大小: ");
/*     */     
/* 104 */     this.openRandomRequestParameterCheckBox = new JCheckBox("开启", ApplicationContext.isOpenC("openRandomRequestParameter"));
/* 105 */     this.openRandomUaCheckBox = new JCheckBox("开启", ApplicationContext.isOpenC("openRandomUa"));
/* 106 */     this.openRandomReqParameterCheckBox = new JCheckBox("开启", ApplicationContext.isOpenC("openRandomReqParameter"));
/*     */     
/* 108 */     String v = Db.getSetingValue("RandomRequestParameterNum");
/*     */     
/* 110 */     this.randomRequestParameterNumTextField = new JTextField((v == null) ? "1-5" : v);
/*     */     
/* 112 */     v = Db.getSetingValue("RandomRequestParameterSize");
/*     */     
/* 114 */     this.randomRequestParameterSizeTextField = new JTextField((v == null) ? "10-30" : v);
/*     */     
/* 116 */     v = Db.getSetingValue("RandomRequestParameterNameSize");
/*     */     
/* 118 */     this.randomRequestParameterNameSizeTextField = new JTextField((v == null) ? "3-7" : v);
/*     */     
/* 120 */     v = Db.getSetingValue("RandomReqParameterSize");
/*     */     
/* 122 */     this.randomReqParameterSizeTextField = new JTextField((v == null) ? "10-20" : v);
/*     */     
/* 124 */     this.randomRequestParameterNumTextField.setColumns(8);
/* 125 */     this.randomRequestParameterSizeTextField.setColumns(8);
/* 126 */     this.randomRequestParameterNameSizeTextField.setColumns(8);
/* 127 */     this.randomReqParameterSizeTextField.setColumns(8);
/*     */     
/* 129 */     this.basicConfigUpdateButton = new JButton("修改");
/*     */     
/* 131 */     this.openRandomRequestParameterCheckBox.addActionListener((ActionListener)new OpenC("openRandomRequestParameter", this.openRandomRequestParameterCheckBox));
/* 132 */     this.openRandomUaCheckBox.addActionListener((ActionListener)new OpenC("openRandomUa", this.openRandomUaCheckBox));
/* 133 */     this.openRandomReqParameterCheckBox.addActionListener((ActionListener)new OpenC("openRandomReqParameter", this.openRandomReqParameterCheckBox));
/*     */     
/* 135 */     this.basicConfigPanel.setSetup(-270);
/*     */     
/* 137 */     this.basicConfigPanel.addX(new Component[] { this.openRandomRequestParameterLabel, this.openRandomRequestParameterCheckBox });
/* 138 */     this.basicConfigPanel.addX(new Component[] { this.openRandomUaLabel, this.openRandomUaCheckBox });
/* 139 */     this.basicConfigPanel.addX(new Component[] { this.openRandomReqParameterLabel, this.openRandomReqParameterCheckBox });
/* 140 */     this.basicConfigPanel.addX(new Component[] { this.randomRequestParameterNumLabel, this.randomRequestParameterNumTextField });
/* 141 */     this.basicConfigPanel.addX(new Component[] { this.randomRequestParameterNameSizeLabel, this.randomRequestParameterNameSizeTextField });
/* 142 */     this.basicConfigPanel.addX(new Component[] { this.randomRequestParameterSizeLabel, this.randomRequestParameterSizeTextField });
/* 143 */     this.basicConfigPanel.addX(new Component[] { this.randomReqParameterSizeLabel, this.randomReqParameterSizeTextField });
/* 144 */     this.basicConfigPanel.addX(new Component[] { this.basicConfigUpdateButton });
/*     */   }
/*     */   
/*     */   private void initRandomUaPanel() {
/* 148 */     this.uaTextArea = new RTextArea();
/* 149 */     this.uaUpdateButton = new JButton("修改");
/* 150 */     this.uaTextArea.setText(Db.getSetingValue("RandomUa"));
/* 151 */     Dimension dimension = new Dimension();
/* 152 */     dimension.height = 30;
/* 153 */     JSplitPane splitPane = new JSplitPane();
/* 154 */     splitPane.setOrientation(0);
/* 155 */     JPanel bottomPanel = new JPanel();
/* 156 */     splitPane.setTopComponent(new JScrollPane((Component)this.uaTextArea));
/* 157 */     bottomPanel.add(this.uaUpdateButton);
/* 158 */     bottomPanel.setMaximumSize(dimension);
/* 159 */     bottomPanel.setMinimumSize(dimension);
/* 160 */     splitPane.setBottomComponent(bottomPanel);
/*     */     
/* 162 */     splitPane.setResizeWeight(0.9D);
/*     */     
/* 164 */     this.randomUaPanel.add(splitPane);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void basicConfigUpdateButtonClick(ActionEvent actionEvent) {
/* 171 */     String randomRequestParameterNum = this.randomRequestParameterNumTextField.getText();
/*     */     
/* 173 */     String randomRequestParameterSize = this.randomRequestParameterSizeTextField.getText();
/* 174 */     String randomRequestParameterNameSize = this.randomRequestParameterNameSizeTextField.getText();
/* 175 */     String randomReqParameterSize = this.randomReqParameterSizeTextField.getText();
/*     */     
/* 177 */     if (Db.updateSetingKV("RandomReqParameterSize", randomReqParameterSize) && Db.updateSetingKV("RandomRequestParameterNameSize", randomRequestParameterNameSize) && Db.updateSetingKV("RandomRequestParameterSize", randomRequestParameterSize) && Db.updateSetingKV("RandomRequestParameterNum", randomRequestParameterNum)) {
/* 178 */       GOptionPane.showMessageDialog(this, "修改成功!", "提示", 1);
/*     */     } else {
/* 180 */       GOptionPane.showMessageDialog(this, "修改失败!", "提示", 2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void uaUpdateButtonClick(ActionEvent actionEvent) {
/* 186 */     String ua = this.uaTextArea.getText();
/* 187 */     if (Db.updateSetingKV("RandomUa", ua)) {
/* 188 */       GOptionPane.showMessageDialog(this, "修改成功!", "提示", 1);
/*     */     } else {
/* 190 */       GOptionPane.showMessageDialog(this, "修改失败!", "提示", 2);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String randomUa() {
/* 195 */     String ret = null;
/*     */     try {
/* 197 */       String allUaString = Db.getSetingValue("RandomUa");
/* 198 */       if (allUaString != null && ApplicationContext.isOpenC("openRandomUa")) {
/* 199 */         String[] uas = allUaString.split("\n");
/* 200 */         int index = functions.random(0, uas.length);
/* 201 */         ret = uas[index].trim();
/*     */       } 
/* 203 */     } catch (Exception e) {
/* 204 */       Log.error(e);
/*     */     } 
/* 206 */     return ret;
/*     */   }
/*     */   
/*     */   public static String randomReqParameter() {
/* 210 */     String ret = null;
/*     */     try {
/* 212 */       if (ApplicationContext.isOpenC("openRandomReqParameter")) {
/* 213 */         String[] pms = Db.getSetingValue("RandomReqParameterSize").split("-");
/* 214 */         if (pms.length == 2) {
/* 215 */           int startIndex = Integer.valueOf(pms[0]).intValue();
/* 216 */           int endIndex = Integer.valueOf(pms[1]).intValue();
/* 217 */           int r = functions.random(startIndex, endIndex);
/* 218 */           ret = functions.getRandomString(r);
/*     */         } 
/*     */       } 
/* 221 */     } catch (Exception e) {
/* 222 */       Log.error(e);
/*     */     } 
/* 224 */     return ret;
/*     */   }
/*     */   
/*     */   public static String randomRequestParameter(int num) {
/* 228 */     String ret = "";
/*     */     
/*     */     try {
/* 231 */       if (ApplicationContext.isOpenC("openRandomRequestParameter")) {
/* 232 */         StringBuilder builder = new StringBuilder();
/* 233 */         for (int i = 0; i < num; i++) {
/* 234 */           builder.append(randomOneRequestParameter());
/* 235 */           builder.append("&");
/*     */         } 
/* 237 */         if (builder.length() > 1) {
/* 238 */           builder.deleteCharAt(builder.length() - 1);
/*     */         }
/* 240 */         ret = builder.toString();
/*     */       } 
/* 242 */     } catch (Exception e) {
/* 243 */       e.printStackTrace();
/*     */     } 
/*     */ 
/*     */     
/* 247 */     return ret;
/*     */   }
/*     */   
/*     */   public static int getRandomRequestParameterNum() {
/* 251 */     int ret = -1;
/*     */     
/*     */     try {
/* 254 */       String[] pms = Db.getSetingValue("RandomRequestParameterNum").split("-");
/* 255 */       int s = Integer.valueOf(pms[0]).intValue();
/* 256 */       int e = Integer.valueOf(pms[1]).intValue();
/*     */       
/* 258 */       ret = functions.random(s, e);
/*     */     }
/* 260 */     catch (Exception e) {
/* 261 */       Log.error(e);
/*     */     } 
/*     */ 
/*     */     
/* 265 */     return ret;
/*     */   }
/*     */   
/*     */   private static String randomOneRequestParameter() {
/* 269 */     String[] v = Db.getSetingValue("RandomRequestParameterNameSize").split("-");
/* 270 */     int nameStartIndex = 0;
/* 271 */     int nameEndIndex = 0;
/*     */     
/* 273 */     int valueStartIndex = 0;
/* 274 */     int valueEndIndex = 0;
/*     */     
/* 276 */     if (v.length == 2) {
/* 277 */       nameStartIndex = Integer.valueOf(v[0]).intValue();
/* 278 */       nameEndIndex = Integer.valueOf(v[1]).intValue();
/*     */     } 
/* 280 */     v = Db.getSetingValue("RandomRequestParameterSize").split("-");
/* 281 */     if (v.length == 2) {
/* 282 */       valueStartIndex = Integer.valueOf(v[0]).intValue();
/* 283 */       valueEndIndex = Integer.valueOf(v[1]).intValue();
/*     */     } 
/*     */     
/* 286 */     return functions.getRandomString(functions.random(nameStartIndex, nameEndIndex)) + "=" + functions.getRandomString(functions.random(valueStartIndex, valueEndIndex));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\cor\\ui\component\dialog\ShellSuperRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */