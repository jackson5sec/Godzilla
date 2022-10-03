/*     */ package shells.plugins.generic;
/*     */ import core.EasyI18N;
/*     */ import core.imp.Plugin;
/*     */ import core.shell.ShellEntity;
/*     */ import core.socksServer.HttpRequestHandle;
/*     */ import core.socksServer.HttpToSocks;
/*     */ import core.socksServer.PortForward;
/*     */ import core.socksServer.SocketStatus;
/*     */ import core.socksServer.SocksRelayInfo;
/*     */ import core.socksServer.SocksServerConfig;
/*     */ import core.ui.component.dialog.GOptionPane;
/*     */ import java.awt.CardLayout;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Vector;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JPopupMenu;
/*     */ import shells.plugins.generic.gui.SocksProxyManagePanel;
/*     */ import shells.plugins.generic.gui.dialog.SocksClientRetransmissionConfigManage;
/*     */ import shells.plugins.generic.model.Retransmission;
/*     */ import shells.plugins.generic.model.enums.RetransmissionType;
/*     */ import util.Log;
/*     */ import util.UiFunction;
/*     */ import util.automaticBindClick;
/*     */ import util.functions;
/*     */ 
/*     */ public class SocksProxy implements Plugin {
/*     */   SocksProxyManagePanel socksProxyManage;
/*     */   JPanel mainPanel;
/*     */   HttpToSocks proxyContext;
/*     */   
/*     */   public SocksProxy(HttpRequestHandle requestHandle) {
/*  37 */     this.retransmissionList = new ArrayList<>();
/*  38 */     this.socksProxyManage = new SocksProxyManagePanel();
/*     */     
/*  40 */     this.mainPanel = new JPanel(new GridBagLayout());
/*  41 */     this.mainPanel.add(new JLabel("下个版本开放"));
/*  42 */     this.rightClickMenu = new JPopupMenu();
/*  43 */     this.socksServerConfig = new SocksServerConfig("127.0.0.1", 1088);
/*  44 */     this.socksServerConfig.requestHandle = requestHandle;
/*  45 */     parseConfig();
/*  46 */     this.proxyContext = new HttpToSocks(this.socksServerConfig);
/*  47 */     automaticBindClick.bindJButtonClick(this.socksProxyManage.getClass(), this.socksProxyManage, SocksProxy.class, this);
/*     */     
/*  49 */     JMenuItem stopItem = new JMenuItem("停止代理");
/*  50 */     stopItem.setActionCommand("stop");
/*  51 */     JMenuItem removeItem = new JMenuItem("删除代理");
/*  52 */     removeItem.setActionCommand("remove");
/*  53 */     JMenuItem refreshItem = new JMenuItem("刷新代理");
/*  54 */     refreshItem.setActionCommand("refresh");
/*  55 */     this.rightClickMenu.add(stopItem);
/*  56 */     this.rightClickMenu.add(removeItem);
/*  57 */     this.rightClickMenu.add(refreshItem);
/*  58 */     automaticBindClick.bindMenuItemClick(this.rightClickMenu, null, this);
/*     */     
/*  60 */     this.socksProxyManage.proxyManageDataView.setRightClickMenu(this.rightClickMenu, true);
/*  61 */     EasyI18N.installObject(this);
/*  62 */     EasyI18N.installObject(this.socksProxyManage);
/*     */   }
/*     */   SocksServerConfig socksServerConfig; ArrayList<Retransmission> retransmissionList; JPopupMenu rightClickMenu;
/*     */   public static void main(String[] args) {
/*  66 */     JFrame frame = new JFrame("SocksProxyManagePanel");
/*  67 */     frame.setContentPane((new SocksProxy((HttpRequestHandle)new SimpleHttpRequestHandle())).getView());
/*  68 */     frame.setDefaultCloseOperation(3);
/*  69 */     frame.pack();
/*  70 */     frame.setVisible(true);
/*     */   }
/*     */   
/*     */   public void setHttpRequestHandle(HttpRequestHandle requestHandle) {
/*  74 */     this.socksServerConfig.requestHandle = requestHandle;
/*     */   }
/*     */   
/*     */   protected void parseConfig() {
/*  78 */     this.socksServerConfig.setBindAddress(this.socksProxyManage.socksBindAddressTextField.getText().trim());
/*  79 */     this.socksServerConfig.setBindPort(Integer.parseInt(this.socksProxyManage.socksBindPortTextField.getText().trim()));
/*  80 */     this.socksServerConfig.remoteProxyUrl = this.socksProxyManage.remoteSocksProxyUrlTextField.getText().trim();
/*  81 */     this.socksServerConfig.remoteKey = this.socksProxyManage.remoteKeyTextField.getText().trim();
/*  82 */     this.socksServerConfig.serverSocketOnceReadSize = Integer.parseInt(this.socksProxyManage.serverSocketOnceReadSizeTextField.getText().trim());
/*  83 */     this.socksServerConfig.serverPacketSize = Integer.parseInt(this.socksProxyManage.serverPacketSizeTextField.getText().trim());
/*  84 */     this.socksServerConfig.clientSocketOnceReadSize.set(Integer.parseInt(this.socksProxyManage.clientSocketOnceReadSizeTextField.getText().trim()));
/*  85 */     this.socksServerConfig.clientPacketSize.set(Integer.parseInt(this.socksProxyManage.clientPacketTextField.getText().trim()));
/*  86 */     this.socksServerConfig.capacity.set(Integer.parseInt(this.socksProxyManage.capacityTextField.getText().trim()));
/*  87 */     this.socksServerConfig.requestDelay.set(Integer.parseInt(this.socksProxyManage.requestDelayTextField.getText().trim()));
/*  88 */     this.socksServerConfig.requestErrRetry.set(Integer.parseInt(this.socksProxyManage.requestErrRetryTextField.getText().trim()));
/*  89 */     this.socksServerConfig.requestErrDelay.set(Integer.parseInt(this.socksProxyManage.requestErrDelayTextField.getText().trim()));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean testProxyContext(boolean initContext, boolean closeContext) throws UnsupportedOperationException {
/*  94 */     boolean flag = false;
/*     */     try {
/*  96 */       if (initContext) {
/*  97 */         this.proxyContext.reset();
/*  98 */         String sessionId = this.proxyContext.generateSessionId();
/*  99 */         if (sessionId == null) {
/* 100 */           new UnsupportedOperationException("未能获取到Session");
/*     */         }
/*     */       } 
/* 103 */       flag = this.proxyContext.testConnect();
/* 104 */       if (closeContext) {
/*     */         try {
/* 106 */           this.proxyContext.reset();
/* 107 */         } catch (Exception e) {
/* 108 */           e.printStackTrace();
/*     */         } 
/*     */       }
/* 111 */     } catch (Exception e) {
/* 112 */       throw new UnsupportedOperationException("通信时发生错误 请检查服务是否启动 网络是否畅通 密钥是否正确!", e);
/*     */     } 
/* 114 */     return flag;
/*     */   }
/*     */   
/*     */   private void stopMenuItemClick(ActionEvent e) {
/* 118 */     String identifier = this.socksProxyManage.proxyManageDataView.GetSelectRow1()[0];
/* 119 */     Retransmission ref = this.retransmissionList.stream().filter(retransmission -> retransmission.identifier.equals(identifier)).findFirst().get();
/* 120 */     if (ref.socketStatus.isActive()) {
/* 121 */       if (ref.socketStatus.stop()) {
/* 122 */         GOptionPane.showMessageDialog(UiFunction.getParentWindow(this.socksProxyManage.getMainPanel()), "关闭成功!");
/*     */       } else {
/* 124 */         GOptionPane.showMessageDialog(UiFunction.getParentWindow(this.socksProxyManage.getMainPanel()), "关闭失败!");
/*     */       } 
/*     */     } else {
/* 127 */       GOptionPane.showMessageDialog(UiFunction.getParentWindow(this.socksProxyManage.getMainPanel()), "已关闭!");
/*     */     } 
/* 129 */     refreshTable();
/*     */   }
/*     */   private void removeMenuItemClick(ActionEvent e) {
/* 132 */     String identifier = this.socksProxyManage.proxyManageDataView.GetSelectRow1()[0];
/* 133 */     Retransmission ref = this.retransmissionList.stream().filter(retransmission -> retransmission.identifier.equals(identifier)).findFirst().get();
/* 134 */     if (!ref.socketStatus.isActive()) {
/* 135 */       this.retransmissionList.remove(ref);
/* 136 */       GOptionPane.showMessageDialog(UiFunction.getParentWindow(this.socksProxyManage.getMainPanel()), "已删除!");
/*     */     } else {
/* 138 */       GOptionPane.showMessageDialog(UiFunction.getParentWindow(this.socksProxyManage.getMainPanel()), "目标是启动状态 需停止服务才可进行删除!");
/*     */     } 
/* 140 */     refreshTable();
/*     */   }
/*     */   private void refreshMenuItemClick(ActionEvent e) {
/* 143 */     refreshTable();
/*     */   }
/*     */   
/*     */   private void testButtonClick(ActionEvent actionEvent) {
/* 147 */     parseConfig();
/*     */     try {
/* 149 */       if (!testProxyContext(true, true)) {
/* 150 */         GOptionPane.showMessageDialog(UiFunction.getParentWindow(this.mainPanel), "无法进行通信 请检查服务是否启动 网络是否畅通!");
/*     */       } else {
/* 152 */         GOptionPane.showMessageDialog(UiFunction.getParentWindow(this.mainPanel), "successfully!");
/*     */       } 
/* 154 */     } catch (Exception e) {
/* 155 */       Log.error(e);
/* 156 */       GOptionPane.showMessageDialog(UiFunction.getParentWindow(this.mainPanel), e.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void startSocksServerButtonClick(ActionEvent actionEvent) {
/* 162 */     parseConfig();
/*     */     try {
/* 164 */       if (!testProxyContext(true, false)) {
/* 165 */         GOptionPane.showMessageDialog(UiFunction.getParentWindow(this.mainPanel), "无法进行通信 请检查服务是否启动 网络是否畅通!");
/*     */       }
/* 167 */       else if (this.proxyContext.start()) {
/* 168 */         GOptionPane.showMessageDialog(UiFunction.getParentWindow(this.mainPanel), "socks代理已启动!");
/* 169 */         ((CardLayout)this.mainPanel.getLayout()).show(this.mainPanel, "socksProxyManage");
/* 170 */         (new Thread(this::calcTips)).start();
/*     */       } else {
/* 172 */         GOptionPane.showMessageDialog(UiFunction.getParentWindow(this.mainPanel), "socks代理启动失败!");
/*     */       }
/*     */     
/* 175 */     } catch (Exception e) {
/* 176 */       Log.error(e);
/* 177 */       GOptionPane.showMessageDialog(UiFunction.getParentWindow(this.mainPanel), e.getMessage());
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addNewProxyButtonClick(ActionEvent actionEvent) {
/* 182 */     Retransmission choose = ChooseNewRetransmissionDialog.chooseNewProxy(UiFunction.getParentWindow(this.socksProxyManage.getMainPanel()));
/* 183 */     if (choose != null && choose.retransmissionType != RetransmissionType.NULL) {
/* 184 */       SocksRelayInfo socksRelayInfo; SocketStatus socketStatus = null;
/* 185 */       if (choose.retransmissionType == RetransmissionType.PORT_FORWARD) {
/* 186 */         PortForward portForward = new PortForward(new InetSocketAddress(choose.listenAddress, choose.listenPort), this.proxyContext, choose.targetAddress, String.valueOf(choose.targetPort));
/* 187 */         portForward.start();
/* 188 */         PortForward portForward1 = portForward;
/* 189 */       } else if (choose.retransmissionType == RetransmissionType.PORT_MAP) {
/* 190 */         socksRelayInfo = this.proxyContext.addBindMirror(choose.listenAddress, String.valueOf(choose.listenPort), choose.targetAddress, String.valueOf(choose.targetPort));
/*     */       } 
/*     */       try {
/* 193 */         Thread.sleep(1000L);
/* 194 */       } catch (InterruptedException e) {
/* 195 */         e.printStackTrace();
/*     */       } 
/* 197 */       choose.socketStatus = (SocketStatus)socksRelayInfo;
/* 198 */       this.retransmissionList.add(choose);
/* 199 */       if (socksRelayInfo.isActive()) {
/* 200 */         GOptionPane.showMessageDialog(UiFunction.getParentWindow(this.socksProxyManage.getMainPanel()), "启动成功!");
/*     */       } else {
/* 202 */         GOptionPane.showMessageDialog(UiFunction.getParentWindow(this.socksProxyManage.getMainPanel()), String.format(EasyI18N.getI18nString("启动失败! 错误信息:%s"), new Object[] { socksRelayInfo.getErrorMessage() }));
/*     */       } 
/* 204 */       refreshTable();
/*     */     } else {
/* 206 */       Log.error("用户取消选择.......");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void stopProxyButtonClick(ActionEvent actionEvent) {
/* 212 */     this.proxyContext.reset();
/* 213 */     this.retransmissionList.clear();
/* 214 */     refreshTable();
/* 215 */     ((CardLayout)this.mainPanel.getLayout()).show(this.mainPanel, "startSocksProxy");
/*     */   }
/*     */   
/*     */   private void serverProxyConfigButtonClick(ActionEvent actionEvent) {
/* 219 */     SocksClientRetransmissionConfigManage.socksServerConfig(UiFunction.getParentWindow(this.mainPanel), this.socksServerConfig);
/*     */   }
/*     */   
/*     */   protected void refreshTable() {
/* 223 */     this.socksProxyManage.proxyManageDataView.RemoveALL();
/* 224 */     this.retransmissionList.stream().forEach(retransmission -> {
/*     */           Vector<String> row = new Vector();
/*     */           row.add(retransmission.identifier);
/*     */           row.add(retransmission.listenAddress);
/*     */           row.add(Integer.valueOf(retransmission.listenPort));
/*     */           row.add(retransmission.retransmissionType);
/*     */           row.add(retransmission.targetAddress);
/*     */           row.add(Integer.valueOf(retransmission.targetPort));
/*     */           row.add(Boolean.valueOf(retransmission.socketStatus.isActive()));
/*     */           row.add(retransmission.socketStatus.getErrorMessage());
/*     */           this.socksProxyManage.proxyManageDataView.AddRow(row);
/*     */         });
/*     */   }
/*     */   
/*     */   protected void calcTips() {
/* 239 */     long lastUpload = 0L;
/* 240 */     long lastDownload = 0L;
/* 241 */     while (this.proxyContext.isAlive()) {
/* 242 */       long runtime = (System.currentTimeMillis() - this.proxyContext.getStartSocksTime()) / 1000L;
/* 243 */       long connNum = this.proxyContext.getSession().size();
/* 244 */       long uploadSpeed = this.proxyContext.getSummaryUploadBytes() - lastUpload;
/* 245 */       long downloadSpeed = this.proxyContext.getSummaryDownloadBytes() - lastDownload;
/* 246 */       lastUpload = this.proxyContext.getSummaryUploadBytes();
/* 247 */       lastDownload = this.proxyContext.getSummaryDownloadBytes();
/* 248 */       String status = String.format(
/* 249 */           EasyI18N.getI18nString("当前连接数:%d 当前上传速度:%s/s 当前下载速度:%s/s 发送成功请求:%s 发送失败请求:%s 监听地址:%s 监听端口:%d 已上传:%s 已下载:%s  运行时间:%ds"), new Object[] {
/* 250 */             Long.valueOf(connNum), functions.getNetworSpeedk(uploadSpeed), functions.getNetworSpeedk(downloadSpeed), Long.valueOf(this.proxyContext.getRequestSuccessNum()), Long.valueOf(this.proxyContext.getRequestFailureNum()), this.socksServerConfig.bindAddress, Integer.valueOf(this.socksServerConfig.bindPort), functions.getNetworSpeedk(lastUpload), functions.getNetworSpeedk(lastDownload), Long.valueOf(runtime)
/*     */           });
/* 252 */       this.socksProxyManage.statusLabel.setText(status);
/*     */       try {
/* 254 */         Thread.sleep(1000L);
/* 255 */       } catch (InterruptedException e) {
/* 256 */         e.printStackTrace();
/*     */         return;
/*     */       } 
/*     */     } 
/* 260 */     stopProxyButtonClick(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void init(ShellEntity shellEntity) {
/* 267 */     automaticBindClick.bindJButtonClick(SocksProxy.class, this, SocksProxy.class, this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JPanel getView() {
/* 273 */     return this.mainPanel;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\generic\SocksProxy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */