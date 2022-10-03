/*     */ package org.springframework.cglib.transform;
/*     */ 
/*     */ import org.springframework.asm.AnnotationVisitor;
/*     */ import org.springframework.asm.Attribute;
/*     */ import org.springframework.asm.Handle;
/*     */ import org.springframework.asm.Label;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.asm.TypePath;
/*     */ import org.springframework.cglib.core.Constants;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MethodVisitorTee
/*     */   extends MethodVisitor
/*     */ {
/*     */   private final MethodVisitor mv1;
/*     */   private final MethodVisitor mv2;
/*     */   
/*     */   public MethodVisitorTee(MethodVisitor mv1, MethodVisitor mv2) {
/*  26 */     super(Constants.ASM_API);
/*  27 */     this.mv1 = mv1;
/*  28 */     this.mv2 = mv2;
/*     */   }
/*     */   
/*     */   public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
/*  32 */     this.mv1.visitFrame(type, nLocal, local, nStack, stack);
/*  33 */     this.mv2.visitFrame(type, nLocal, local, nStack, stack);
/*     */   }
/*     */   
/*     */   public AnnotationVisitor visitAnnotationDefault() {
/*  37 */     return AnnotationVisitorTee.getInstance(this.mv1.visitAnnotationDefault(), this.mv2
/*  38 */         .visitAnnotationDefault());
/*     */   }
/*     */   
/*     */   public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
/*  42 */     return AnnotationVisitorTee.getInstance(this.mv1.visitAnnotation(desc, visible), this.mv2
/*  43 */         .visitAnnotation(desc, visible));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {
/*  49 */     return AnnotationVisitorTee.getInstance(this.mv1.visitParameterAnnotation(parameter, desc, visible), this.mv2
/*  50 */         .visitParameterAnnotation(parameter, desc, visible));
/*     */   }
/*     */   
/*     */   public void visitAttribute(Attribute attr) {
/*  54 */     this.mv1.visitAttribute(attr);
/*  55 */     this.mv2.visitAttribute(attr);
/*     */   }
/*     */   
/*     */   public void visitCode() {
/*  59 */     this.mv1.visitCode();
/*  60 */     this.mv2.visitCode();
/*     */   }
/*     */   
/*     */   public void visitInsn(int opcode) {
/*  64 */     this.mv1.visitInsn(opcode);
/*  65 */     this.mv2.visitInsn(opcode);
/*     */   }
/*     */   
/*     */   public void visitIntInsn(int opcode, int operand) {
/*  69 */     this.mv1.visitIntInsn(opcode, operand);
/*  70 */     this.mv2.visitIntInsn(opcode, operand);
/*     */   }
/*     */   
/*     */   public void visitVarInsn(int opcode, int var) {
/*  74 */     this.mv1.visitVarInsn(opcode, var);
/*  75 */     this.mv2.visitVarInsn(opcode, var);
/*     */   }
/*     */   
/*     */   public void visitTypeInsn(int opcode, String desc) {
/*  79 */     this.mv1.visitTypeInsn(opcode, desc);
/*  80 */     this.mv2.visitTypeInsn(opcode, desc);
/*     */   }
/*     */   
/*     */   public void visitFieldInsn(int opcode, String owner, String name, String desc) {
/*  84 */     this.mv1.visitFieldInsn(opcode, owner, name, desc);
/*  85 */     this.mv2.visitFieldInsn(opcode, owner, name, desc);
/*     */   }
/*     */   
/*     */   public void visitMethodInsn(int opcode, String owner, String name, String desc) {
/*  89 */     this.mv1.visitMethodInsn(opcode, owner, name, desc);
/*  90 */     this.mv2.visitMethodInsn(opcode, owner, name, desc);
/*     */   }
/*     */   
/*     */   public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
/*  94 */     this.mv1.visitMethodInsn(opcode, owner, name, desc, itf);
/*  95 */     this.mv2.visitMethodInsn(opcode, owner, name, desc, itf);
/*     */   }
/*     */   
/*     */   public void visitJumpInsn(int opcode, Label label) {
/*  99 */     this.mv1.visitJumpInsn(opcode, label);
/* 100 */     this.mv2.visitJumpInsn(opcode, label);
/*     */   }
/*     */   
/*     */   public void visitLabel(Label label) {
/* 104 */     this.mv1.visitLabel(label);
/* 105 */     this.mv2.visitLabel(label);
/*     */   }
/*     */   
/*     */   public void visitLdcInsn(Object cst) {
/* 109 */     this.mv1.visitLdcInsn(cst);
/* 110 */     this.mv2.visitLdcInsn(cst);
/*     */   }
/*     */   
/*     */   public void visitIincInsn(int var, int increment) {
/* 114 */     this.mv1.visitIincInsn(var, increment);
/* 115 */     this.mv2.visitIincInsn(var, increment);
/*     */   }
/*     */   
/*     */   public void visitTableSwitchInsn(int min, int max, Label dflt, Label[] labels) {
/* 119 */     this.mv1.visitTableSwitchInsn(min, max, dflt, labels);
/* 120 */     this.mv2.visitTableSwitchInsn(min, max, dflt, labels);
/*     */   }
/*     */   
/*     */   public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
/* 124 */     this.mv1.visitLookupSwitchInsn(dflt, keys, labels);
/* 125 */     this.mv2.visitLookupSwitchInsn(dflt, keys, labels);
/*     */   }
/*     */   
/*     */   public void visitMultiANewArrayInsn(String desc, int dims) {
/* 129 */     this.mv1.visitMultiANewArrayInsn(desc, dims);
/* 130 */     this.mv2.visitMultiANewArrayInsn(desc, dims);
/*     */   }
/*     */   
/*     */   public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
/* 134 */     this.mv1.visitTryCatchBlock(start, end, handler, type);
/* 135 */     this.mv2.visitTryCatchBlock(start, end, handler, type);
/*     */   }
/*     */   
/*     */   public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
/* 139 */     this.mv1.visitLocalVariable(name, desc, signature, start, end, index);
/* 140 */     this.mv2.visitLocalVariable(name, desc, signature, start, end, index);
/*     */   }
/*     */   
/*     */   public void visitLineNumber(int line, Label start) {
/* 144 */     this.mv1.visitLineNumber(line, start);
/* 145 */     this.mv2.visitLineNumber(line, start);
/*     */   }
/*     */   
/*     */   public void visitMaxs(int maxStack, int maxLocals) {
/* 149 */     this.mv1.visitMaxs(maxStack, maxLocals);
/* 150 */     this.mv2.visitMaxs(maxStack, maxLocals);
/*     */   }
/*     */   
/*     */   public void visitEnd() {
/* 154 */     this.mv1.visitEnd();
/* 155 */     this.mv2.visitEnd();
/*     */   }
/*     */   
/*     */   public void visitParameter(String name, int access) {
/* 159 */     this.mv1.visitParameter(name, access);
/* 160 */     this.mv2.visitParameter(name, access);
/*     */   }
/*     */   
/*     */   public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
/* 164 */     return AnnotationVisitorTee.getInstance(this.mv1.visitTypeAnnotation(typeRef, typePath, desc, visible), this.mv2
/* 165 */         .visitTypeAnnotation(typeRef, typePath, desc, visible));
/*     */   }
/*     */   
/*     */   public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs) {
/* 169 */     this.mv1.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
/* 170 */     this.mv2.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
/*     */   }
/*     */   
/*     */   public AnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
/* 174 */     return AnnotationVisitorTee.getInstance(this.mv1.visitInsnAnnotation(typeRef, typePath, desc, visible), this.mv2
/* 175 */         .visitInsnAnnotation(typeRef, typePath, desc, visible));
/*     */   }
/*     */   
/*     */   public AnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
/* 179 */     return AnnotationVisitorTee.getInstance(this.mv1.visitTryCatchAnnotation(typeRef, typePath, desc, visible), this.mv2
/* 180 */         .visitTryCatchAnnotation(typeRef, typePath, desc, visible));
/*     */   }
/*     */   
/*     */   public AnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, Label[] end, int[] index, String desc, boolean visible) {
/* 184 */     return AnnotationVisitorTee.getInstance(this.mv1.visitLocalVariableAnnotation(typeRef, typePath, start, end, index, desc, visible), this.mv2
/* 185 */         .visitLocalVariableAnnotation(typeRef, typePath, start, end, index, desc, visible));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\transform\MethodVisitorTee.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */