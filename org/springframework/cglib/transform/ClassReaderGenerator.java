/*    */ package org.springframework.cglib.transform;
/*    */ 
/*    */ import org.springframework.asm.Attribute;
/*    */ import org.springframework.asm.ClassReader;
/*    */ import org.springframework.asm.ClassVisitor;
/*    */ import org.springframework.cglib.core.ClassGenerator;
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
/*    */ public class ClassReaderGenerator
/*    */   implements ClassGenerator
/*    */ {
/*    */   private final ClassReader r;
/*    */   private final Attribute[] attrs;
/*    */   private final int flags;
/*    */   
/*    */   public ClassReaderGenerator(ClassReader r, int flags) {
/* 29 */     this(r, null, flags);
/*    */   }
/*    */   
/*    */   public ClassReaderGenerator(ClassReader r, Attribute[] attrs, int flags) {
/* 33 */     this.r = r;
/* 34 */     this.attrs = (attrs != null) ? attrs : new Attribute[0];
/* 35 */     this.flags = flags;
/*    */   }
/*    */   
/*    */   public void generateClass(ClassVisitor v) {
/* 39 */     this.r.accept(v, this.attrs, this.flags);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\transform\ClassReaderGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */