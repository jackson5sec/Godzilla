/*    */ package javassist.util.proxy;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.ObjectStreamClass;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ProxyObjectInputStream
/*    */   extends ObjectInputStream
/*    */ {
/*    */   private ClassLoader loader;
/*    */   
/*    */   public ProxyObjectInputStream(InputStream in) throws IOException {
/* 46 */     super(in);
/* 47 */     this.loader = Thread.currentThread().getContextClassLoader();
/* 48 */     if (this.loader == null) {
/* 49 */       this.loader = ClassLoader.getSystemClassLoader();
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setClassLoader(ClassLoader loader) {
/* 59 */     if (loader != null) {
/* 60 */       this.loader = loader;
/*    */     } else {
/* 62 */       loader = ClassLoader.getSystemClassLoader();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
/* 68 */     boolean isProxy = readBoolean();
/* 69 */     if (isProxy) {
/* 70 */       String name = (String)readObject();
/* 71 */       Class<?> superClass = this.loader.loadClass(name);
/* 72 */       int length = readInt();
/* 73 */       Class<?>[] interfaces = new Class[length];
/* 74 */       for (int i = 0; i < length; i++) {
/* 75 */         name = (String)readObject();
/* 76 */         interfaces[i] = this.loader.loadClass(name);
/*    */       } 
/* 78 */       length = readInt();
/* 79 */       byte[] signature = new byte[length];
/* 80 */       read(signature);
/* 81 */       ProxyFactory factory = new ProxyFactory();
/*    */ 
/*    */       
/* 84 */       factory.setUseCache(true);
/* 85 */       factory.setUseWriteReplace(false);
/* 86 */       factory.setSuperclass(superClass);
/* 87 */       factory.setInterfaces(interfaces);
/* 88 */       Class<?> proxyClass = factory.createClass(signature);
/* 89 */       return ObjectStreamClass.lookup(proxyClass);
/*    */     } 
/* 91 */     return super.readClassDescriptor();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassis\\util\proxy\ProxyObjectInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */