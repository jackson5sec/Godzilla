/*    */ package org.springframework.cglib.transform;
/*    */ 
/*    */ import org.springframework.asm.AnnotationVisitor;
/*    */ import org.springframework.cglib.core.Constants;
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
/*    */ public class AnnotationVisitorTee
/*    */   extends AnnotationVisitor
/*    */ {
/*    */   private AnnotationVisitor av1;
/*    */   private AnnotationVisitor av2;
/*    */   
/*    */   public static AnnotationVisitor getInstance(AnnotationVisitor av1, AnnotationVisitor av2) {
/* 25 */     if (av1 == null)
/* 26 */       return av2; 
/* 27 */     if (av2 == null)
/* 28 */       return av1; 
/* 29 */     return new AnnotationVisitorTee(av1, av2);
/*    */   }
/*    */   
/*    */   public AnnotationVisitorTee(AnnotationVisitor av1, AnnotationVisitor av2) {
/* 33 */     super(Constants.ASM_API);
/* 34 */     this.av1 = av1;
/* 35 */     this.av2 = av2;
/*    */   }
/*    */   
/*    */   public void visit(String name, Object value) {
/* 39 */     this.av2.visit(name, value);
/* 40 */     this.av2.visit(name, value);
/*    */   }
/*    */   
/*    */   public void visitEnum(String name, String desc, String value) {
/* 44 */     this.av1.visitEnum(name, desc, value);
/* 45 */     this.av2.visitEnum(name, desc, value);
/*    */   }
/*    */   
/*    */   public AnnotationVisitor visitAnnotation(String name, String desc) {
/* 49 */     return getInstance(this.av1.visitAnnotation(name, desc), this.av2
/* 50 */         .visitAnnotation(name, desc));
/*    */   }
/*    */   
/*    */   public AnnotationVisitor visitArray(String name) {
/* 54 */     return getInstance(this.av1.visitArray(name), this.av2.visitArray(name));
/*    */   }
/*    */   
/*    */   public void visitEnd() {
/* 58 */     this.av1.visitEnd();
/* 59 */     this.av2.visitEnd();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\transform\AnnotationVisitorTee.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */