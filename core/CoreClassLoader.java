/*    */ package core;
/*    */ 
/*    */ import java.net.MalformedURLException;
/*    */ import java.net.URL;
/*    */ import java.net.URLClassLoader;
/*    */ import java.net.URLStreamHandlerFactory;
/*    */ import java.security.ProtectionDomain;
/*    */ 
/*    */ public class CoreClassLoader
/*    */   extends URLClassLoader {
/*    */   public CoreClassLoader(URL[] urls, ClassLoader parent) {
/* 12 */     super(urls, parent);
/*    */   }
/*    */   
/*    */   public CoreClassLoader(ClassLoader parent) {
/* 16 */     this(new URL[0], parent);
/*    */   }
/*    */   
/*    */   public CoreClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
/* 20 */     super(urls, parent, factory); addURL((URL)null);
/*    */   }
/*    */   
/*    */   public void addJar(String fileName) throws MalformedURLException {
/* 24 */     addJar(new URL(fileName));
/*    */   }
/*    */   public void addJar(URL url) {
/* 27 */     addURL(url);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Class defineClass0(String name, byte[] b, int off, int len, ProtectionDomain protectionDomain) {
/* 33 */     return defineClass(name, b, off, len, protectionDomain);
/*    */   }
/*    */ 
/*    */   
/*    */   public static Class defineClass2(String name, byte[] b, int off, int len, ProtectionDomain protectionDomain) {
/*    */     try {
/* 39 */       return ApplicationContext.PLUGIN_CLASSLOADER.loadClass(name);
/* 40 */     } catch (Exception e) {
/* 41 */       return ApplicationContext.PLUGIN_CLASSLOADER.defineClass0(name, b, off, len, protectionDomain);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static Class defineClass3(String name, byte[] b, ProtectionDomain protectionDomain) {
/*    */     try {
/* 47 */       return ApplicationContext.PLUGIN_CLASSLOADER.loadClass(name);
/* 48 */     } catch (Exception e) {
/* 49 */       return ApplicationContext.PLUGIN_CLASSLOADER.defineClass0(name, b, 0, b.length, protectionDomain);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\core\CoreClassLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */