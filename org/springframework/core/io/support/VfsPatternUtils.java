/*    */ package org.springframework.core.io.support;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.lang.reflect.InvocationHandler;
/*    */ import java.lang.reflect.Proxy;
/*    */ import java.net.URL;
/*    */ import org.springframework.core.io.VfsUtils;
/*    */ import org.springframework.lang.Nullable;
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
/*    */ abstract class VfsPatternUtils
/*    */   extends VfsUtils
/*    */ {
/*    */   @Nullable
/*    */   static Object getVisitorAttributes() {
/* 38 */     return doGetVisitorAttributes();
/*    */   }
/*    */   
/*    */   static String getPath(Object resource) {
/* 42 */     String path = doGetPath(resource);
/* 43 */     return (path != null) ? path : "";
/*    */   }
/*    */   
/*    */   static Object findRoot(URL url) throws IOException {
/* 47 */     return getRoot(url);
/*    */   }
/*    */   
/*    */   static void visit(Object resource, InvocationHandler visitor) throws IOException {
/* 51 */     Object visitorProxy = Proxy.newProxyInstance(VIRTUAL_FILE_VISITOR_INTERFACE
/* 52 */         .getClassLoader(), new Class[] { VIRTUAL_FILE_VISITOR_INTERFACE }, visitor);
/*    */     
/* 54 */     invokeVfsMethod(VIRTUAL_FILE_METHOD_VISIT, resource, new Object[] { visitorProxy });
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\io\support\VfsPatternUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */