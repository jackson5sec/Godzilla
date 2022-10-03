/*    */ package javassist.util.proxy;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectOutputStream;
/*    */ import java.io.ObjectStreamClass;
/*    */ import java.io.OutputStream;
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
/*    */ public class ProxyObjectOutputStream
/*    */   extends ObjectOutputStream
/*    */ {
/*    */   public ProxyObjectOutputStream(OutputStream out) throws IOException {
/* 44 */     super(out);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void writeClassDescriptor(ObjectStreamClass desc) throws IOException {
/* 49 */     Class<?> cl = desc.forClass();
/* 50 */     if (ProxyFactory.isProxyClass(cl)) {
/* 51 */       writeBoolean(true);
/* 52 */       Class<?> superClass = cl.getSuperclass();
/* 53 */       Class<?>[] interfaces = cl.getInterfaces();
/* 54 */       byte[] signature = ProxyFactory.getFilterSignature(cl);
/* 55 */       String name = superClass.getName();
/* 56 */       writeObject(name);
/*    */       
/* 58 */       writeInt(interfaces.length - 1);
/* 59 */       for (int i = 0; i < interfaces.length; i++) {
/* 60 */         Class<?> interfaze = interfaces[i];
/* 61 */         if (interfaze != ProxyObject.class && interfaze != Proxy.class) {
/* 62 */           name = interfaces[i].getName();
/* 63 */           writeObject(name);
/*    */         } 
/*    */       } 
/* 66 */       writeInt(signature.length);
/* 67 */       write(signature);
/*    */     } else {
/* 69 */       writeBoolean(false);
/* 70 */       super.writeClassDescriptor(desc);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassis\\util\proxy\ProxyObjectOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */