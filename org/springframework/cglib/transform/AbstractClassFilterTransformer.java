/*    */ package org.springframework.cglib.transform;
/*    */ 
/*    */ import org.springframework.asm.AnnotationVisitor;
/*    */ import org.springframework.asm.Attribute;
/*    */ import org.springframework.asm.ClassVisitor;
/*    */ import org.springframework.asm.FieldVisitor;
/*    */ import org.springframework.asm.MethodVisitor;
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
/*    */ public abstract class AbstractClassFilterTransformer
/*    */   extends AbstractClassTransformer
/*    */ {
/*    */   private ClassTransformer pass;
/*    */   private ClassVisitor target;
/*    */   
/*    */   public void setTarget(ClassVisitor target) {
/* 25 */     super.setTarget(target);
/* 26 */     this.pass.setTarget(target);
/*    */   }
/*    */   
/*    */   protected AbstractClassFilterTransformer(ClassTransformer pass) {
/* 30 */     this.pass = pass;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract boolean accept(int paramInt1, int paramInt2, String paramString1, String paramString2, String paramString3, String[] paramArrayOfString);
/*    */ 
/*    */ 
/*    */   
/*    */   public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
/* 41 */     this.target = accept(version, access, name, signature, superName, interfaces) ? this.pass : this.cv;
/* 42 */     this.target.visit(version, access, name, signature, superName, interfaces);
/*    */   }
/*    */   
/*    */   public void visitSource(String source, String debug) {
/* 46 */     this.target.visitSource(source, debug);
/*    */   }
/*    */   
/*    */   public void visitOuterClass(String owner, String name, String desc) {
/* 50 */     this.target.visitOuterClass(owner, name, desc);
/*    */   }
/*    */   
/*    */   public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
/* 54 */     return this.target.visitAnnotation(desc, visible);
/*    */   }
/*    */   
/*    */   public void visitAttribute(Attribute attr) {
/* 58 */     this.target.visitAttribute(attr);
/*    */   }
/*    */   
/*    */   public void visitInnerClass(String name, String outerName, String innerName, int access) {
/* 62 */     this.target.visitInnerClass(name, outerName, innerName, access);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
/* 70 */     return this.target.visitField(access, name, desc, signature, value);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
/* 78 */     return this.target.visitMethod(access, name, desc, signature, exceptions);
/*    */   }
/*    */   
/*    */   public void visitEnd() {
/* 82 */     this.target.visitEnd();
/* 83 */     this.target = null;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\transform\AbstractClassFilterTransformer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */