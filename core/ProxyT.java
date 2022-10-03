/*    */ package core;
/*    */ 
/*    */ import core.shell.ShellEntity;
/*    */ import java.net.InetSocketAddress;
/*    */ import java.net.Proxy;
/*    */ 
/*    */ 
/*    */ public class ProxyT
/*    */ {
/* 10 */   private static final String[] PTOXY_TYPES = new String[] { "NO_PROXY", "HTTP", "SOCKS", "GLOBAL_PROXY" };
/*    */ 
/*    */ 
/*    */   
/*    */   public static Proxy getProxy(ShellEntity context) {
/*    */     try {
/* 16 */       String type = context.getProxyType();
/* 17 */       InetSocketAddress inetSocketAddress = new InetSocketAddress(context.getProxyHost(), context.getProxyPort());
/* 18 */       if ("SOCKS".equalsIgnoreCase(type))
/* 19 */         return new Proxy(Proxy.Type.SOCKS, inetSocketAddress); 
/* 20 */       if ("HTTP".equalsIgnoreCase(type))
/* 21 */         return new Proxy(Proxy.Type.HTTP, inetSocketAddress); 
/* 22 */       if ("GLOBAL_PROXY".equalsIgnoreCase(type)) {
/* 23 */         inetSocketAddress = new InetSocketAddress(Db.tryGetSetingValue("globalProxyHost", "127.0.0.1"), Integer.parseInt(Db.tryGetSetingValue("globalProxyPort", "8888")));
/* 24 */         type = Db.tryGetSetingValue("globalProxyType", "NO_PROXY");
/* 25 */         if ("SOCKS".equalsIgnoreCase(type))
/* 26 */           return new Proxy(Proxy.Type.SOCKS, inetSocketAddress); 
/* 27 */         if ("HTTP".equalsIgnoreCase(type)) {
/* 28 */           return new Proxy(Proxy.Type.HTTP, inetSocketAddress);
/*    */         }
/*    */       } else {
/* 31 */         return Proxy.NO_PROXY;
/*    */       } 
/* 33 */     } catch (Exception e) {
/* 34 */       return Proxy.NO_PROXY;
/*    */     } 
/* 36 */     return Proxy.NO_PROXY;
/*    */   }
/*    */   public static String[] getAllProxyType() {
/* 39 */     return PTOXY_TYPES;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\core\ProxyT.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */