/*    */ package core.shell.cache;
/*    */ 
/*    */ import core.ApplicationContext;
/*    */ import core.CoreClassLoader;
/*    */ import core.imp.Payload;
/*    */ import core.shell.ShellEntity;
/*    */ import java.lang.reflect.Field;
/*    */ import javassist.ClassPool;
/*    */ import javassist.CtClass;
/*    */ import javassist.CtField;
/*    */ import javassist.CtMethod;
/*    */ import util.functions;
/*    */ import util.http.ReqParameter;
/*    */ 
/*    */ 
/*    */ public class CachePayload
/*    */ {
/*    */   public static final String OPEN_CACHE_PAYLOAD = "_Cache";
/*    */   public static final String OPEN_USE_CACHE_PAYLOAD = "_Cache2";
/* 20 */   public static final String HANDLER_FIELD = "usePayloadCacheHandler" + System.currentTimeMillis();
/*    */   public static final String DirectoryName = "GodzillaCache";
/*    */   
/*    */   public static Payload openCachePayload(ShellEntity entity, Class<Payload> payloadType) {
/* 24 */     String className = payloadType.getName() + "_Cache";
/*    */     
/* 26 */     Class payloadClass = functions.loadClass((ClassLoader)ApplicationContext.PLUGIN_CLASSLOADER, className);
/* 27 */     if (payloadClass == null) {
/*    */       try {
/* 29 */         CtClass ctClass = classToCtClass(payloadType);
/* 30 */         CtMethod ctMethod = ctClass.getDeclaredMethod("evalFunc", new CtClass[] { classToCtClass(String.class), classToCtClass(String.class), classToCtClass(ReqParameter.class) });
/* 31 */         CtField ctField = new CtField(classToCtClass(PayloadCacheHandler.class), HANDLER_FIELD, ctClass);
/* 32 */         ctClass.addField(ctField);
/* 33 */         ctMethod.insertAfter(String.format("return this.%s.evalFunc($_,$1,$2,$3);", new Object[] { HANDLER_FIELD }));
/* 34 */         ctClass.setName(className);
/* 35 */         payloadClass = CoreClassLoader.defineClass3(ctClass.getName(), ctClass.toBytecode(), null);
/* 36 */         ctClass.detach();
/* 37 */       } catch (Exception e) {
/* 38 */         e.printStackTrace();
/*    */       } 
/*    */     }
/*    */     try {
/* 42 */       if (payloadClass == null) {
/* 43 */         return payloadType.newInstance();
/*    */       }
/* 45 */       Object payload = payloadClass.newInstance();
/* 46 */       Field handlerField = payloadClass.getDeclaredField(HANDLER_FIELD);
/* 47 */       handlerField.setAccessible(true);
/* 48 */       handlerField.set(payload, new UpdatePayloadCacheHandler(entity, (Payload)payload));
/* 49 */       return (Payload)payload;
/*    */     }
/* 51 */     catch (Throwable e) {
/* 52 */       e.printStackTrace();
/*    */ 
/*    */       
/* 55 */       return null;
/*    */     } 
/*    */   }
/*    */   public static Payload openUseCachePayload(ShellEntity entity, Class<Payload> payloadType) {
/* 59 */     String className = payloadType.getName() + "_Cache2";
/*    */     
/* 61 */     Class payloadClass = functions.loadClass((ClassLoader)ApplicationContext.PLUGIN_CLASSLOADER, className);
/* 62 */     if (payloadClass == null) {
/*    */       try {
/* 64 */         CtClass ctClass = classToCtClass(payloadType);
/* 65 */         CtMethod ctMethod = ctClass.getDeclaredMethod("evalFunc", new CtClass[] { classToCtClass(String.class), classToCtClass(String.class), classToCtClass(ReqParameter.class) });
/* 66 */         CtField ctField = new CtField(classToCtClass(PayloadCacheHandler.class), HANDLER_FIELD, ctClass);
/* 67 */         ctClass.addField(ctField);
/* 68 */         ctMethod.insertBefore(String.format("return this.%s.evalFunc(null,$1,$2,$3);", new Object[] { HANDLER_FIELD }));
/* 69 */         ctClass.setName(className);
/* 70 */         payloadClass = CoreClassLoader.defineClass3(ctClass.getName(), ctClass.toBytecode(), null);
/* 71 */         ctClass.detach();
/* 72 */       } catch (Exception e) {
/* 73 */         e.printStackTrace();
/*    */       } 
/*    */     }
/*    */     try {
/* 77 */       if (payloadClass == null) {
/* 78 */         return payloadType.newInstance();
/*    */       }
/* 80 */       Object payload = payloadClass.newInstance();
/* 81 */       Field handlerField = payloadClass.getDeclaredField(HANDLER_FIELD);
/* 82 */       handlerField.setAccessible(true);
/* 83 */       handlerField.set(payload, new UsePayloadCacheHandler(entity, (Payload)payload));
/* 84 */       return (Payload)payload;
/*    */     }
/* 86 */     catch (Throwable e) {
/* 87 */       e.printStackTrace();
/*    */ 
/*    */       
/* 90 */       return null;
/*    */     } 
/*    */   } private static CtClass classToCtClass(Class cs) {
/* 93 */     CtClass ctClass = null;
/*    */     try {
/* 95 */       ctClass = ClassPool.getDefault().get(cs.getName());
/* 96 */     } catch (Throwable e) {
/* 97 */       e.printStackTrace();
/*    */     } 
/* 99 */     return ctClass;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\core\shell\cache\CachePayload.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */