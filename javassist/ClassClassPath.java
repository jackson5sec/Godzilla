/*    */ package javassist;
/*    */ 
/*    */ import java.io.InputStream;
/*    */ import java.net.URL;
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
/*    */ public class ClassClassPath
/*    */   implements ClassPath
/*    */ {
/*    */   private Class<?> thisClass;
/*    */   
/*    */   public ClassClassPath(Class<?> c) {
/* 59 */     this.thisClass = c;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   ClassClassPath() {
/* 71 */     this(Object.class);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public InputStream openClassfile(String classname) throws NotFoundException {
/* 79 */     String filename = '/' + classname.replace('.', '/') + ".class";
/* 80 */     return this.thisClass.getResourceAsStream(filename);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public URL find(String classname) {
/* 90 */     String filename = '/' + classname.replace('.', '/') + ".class";
/* 91 */     return this.thisClass.getResource(filename);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 96 */     return this.thisClass.getName() + ".class";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\ClassClassPath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */