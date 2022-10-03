/*    */ package javassist;
/*    */ 
/*    */ import java.io.InputStream;
/*    */ import java.lang.ref.Reference;
/*    */ import java.lang.ref.WeakReference;
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
/*    */ public class LoaderClassPath
/*    */   implements ClassPath
/*    */ {
/*    */   private Reference<ClassLoader> clref;
/*    */   
/*    */   public LoaderClassPath(ClassLoader cl) {
/* 54 */     this.clref = new WeakReference<>(cl);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 59 */     return (this.clref.get() == null) ? "<null>" : ((ClassLoader)this.clref.get()).toString();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public InputStream openClassfile(String classname) throws NotFoundException {
/* 69 */     String cname = classname.replace('.', '/') + ".class";
/* 70 */     ClassLoader cl = this.clref.get();
/* 71 */     if (cl == null)
/* 72 */       return null; 
/* 73 */     InputStream is = cl.getResourceAsStream(cname);
/* 74 */     return is;
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
/*    */   public URL find(String classname) {
/* 86 */     String cname = classname.replace('.', '/') + ".class";
/* 87 */     ClassLoader cl = this.clref.get();
/* 88 */     if (cl == null)
/* 89 */       return null; 
/* 90 */     URL url = cl.getResource(cname);
/* 91 */     return url;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\LoaderClassPath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */