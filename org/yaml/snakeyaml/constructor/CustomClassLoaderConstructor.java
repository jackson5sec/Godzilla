/*    */ package org.yaml.snakeyaml.constructor;
/*    */ 
/*    */ import org.yaml.snakeyaml.SecClass;
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
/*    */ public class CustomClassLoaderConstructor
/*    */   extends Constructor
/*    */ {
/* 24 */   private ClassLoader loader = CustomClassLoaderConstructor.class.getClassLoader();
/*    */   
/*    */   public CustomClassLoaderConstructor(ClassLoader cLoader) {
/* 27 */     this(Object.class, cLoader);
/*    */   }
/*    */   
/*    */   public CustomClassLoaderConstructor(Class<? extends Object> theRoot, ClassLoader theLoader) {
/* 31 */     super(theRoot);
/* 32 */     if (theLoader == null) {
/* 33 */       throw new NullPointerException("Loader must be provided.");
/*    */     }
/* 35 */     this.loader = theLoader;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Class<?> getClassForName(String name) throws ClassNotFoundException {
/* 40 */     return SecClass.forName(name, true, this.loader);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\yaml\snakeyaml\constructor\CustomClassLoaderConstructor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */