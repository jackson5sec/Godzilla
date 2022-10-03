/*     */ package org.springframework.asm;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class MethodVisitor
/*     */ {
/*     */   private static final String REQUIRES_ASM5 = "This feature requires ASM5";
/*     */   protected final int api;
/*     */   protected MethodVisitor mv;
/*     */   
/*     */   public MethodVisitor(int api) {
/*  71 */     this(api, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodVisitor(int api, MethodVisitor methodVisitor) {
/*  83 */     if (api != 589824 && api != 524288 && api != 458752 && api != 393216 && api != 327680 && api != 262144 && api != 17432576)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  90 */       throw new IllegalArgumentException("Unsupported api " + api);
/*     */     }
/*     */     
/*  93 */     this.api = api;
/*  94 */     this.mv = methodVisitor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitParameter(String name, int access) {
/* 109 */     if (this.api < 327680) {
/* 110 */       throw new UnsupportedOperationException("This feature requires ASM5");
/*     */     }
/* 112 */     if (this.mv != null) {
/* 113 */       this.mv.visitParameter(name, access);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitAnnotationDefault() {
/* 126 */     if (this.mv != null) {
/* 127 */       return this.mv.visitAnnotationDefault();
/*     */     }
/* 129 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
/* 141 */     if (this.mv != null) {
/* 142 */       return this.mv.visitAnnotation(descriptor, visible);
/*     */     }
/* 144 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
/* 165 */     if (this.api < 327680) {
/* 166 */       throw new UnsupportedOperationException("This feature requires ASM5");
/*     */     }
/* 168 */     if (this.mv != null) {
/* 169 */       return this.mv.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
/*     */     }
/* 171 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitAnnotableParameterCount(int parameterCount, boolean visible) {
/* 189 */     if (this.mv != null) {
/* 190 */       this.mv.visitAnnotableParameterCount(parameterCount, visible);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitParameterAnnotation(int parameter, String descriptor, boolean visible) {
/* 210 */     if (this.mv != null) {
/* 211 */       return this.mv.visitParameterAnnotation(parameter, descriptor, visible);
/*     */     }
/* 213 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitAttribute(Attribute attribute) {
/* 222 */     if (this.mv != null) {
/* 223 */       this.mv.visitAttribute(attribute);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitCode() {
/* 229 */     if (this.mv != null) {
/* 230 */       this.mv.visitCode();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitFrame(int type, int numLocal, Object[] local, int numStack, Object[] stack) {
/* 297 */     if (this.mv != null) {
/* 298 */       this.mv.visitFrame(type, numLocal, local, numStack, stack);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitInsn(int opcode) {
/* 321 */     if (this.mv != null) {
/* 322 */       this.mv.visitInsn(opcode);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitIntInsn(int opcode, int operand) {
/* 341 */     if (this.mv != null) {
/* 342 */       this.mv.visitIntInsn(opcode, operand);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitVarInsn(int opcode, int var) {
/* 356 */     if (this.mv != null) {
/* 357 */       this.mv.visitVarInsn(opcode, var);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitTypeInsn(int opcode, String type) {
/* 371 */     if (this.mv != null) {
/* 372 */       this.mv.visitTypeInsn(opcode, type);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
/* 388 */     if (this.mv != null) {
/* 389 */       this.mv.visitFieldInsn(opcode, owner, name, descriptor);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void visitMethodInsn(int opcode, String owner, String name, String descriptor) {
/* 407 */     int opcodeAndSource = opcode | ((this.api < 327680) ? 256 : 0);
/* 408 */     visitMethodInsn(opcodeAndSource, owner, name, descriptor, (opcode == 185));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
/* 428 */     if (this.api < 327680 && (opcode & 0x100) == 0) {
/* 429 */       if (isInterface != ((opcode == 185))) {
/* 430 */         throw new UnsupportedOperationException("INVOKESPECIAL/STATIC on interfaces requires ASM5");
/*     */       }
/* 432 */       visitMethodInsn(opcode, owner, name, descriptor);
/*     */       return;
/*     */     } 
/* 435 */     if (this.mv != null) {
/* 436 */       this.mv.visitMethodInsn(opcode & 0xFFFFFEFF, owner, name, descriptor, isInterface);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
/* 456 */     if (this.api < 327680) {
/* 457 */       throw new UnsupportedOperationException("This feature requires ASM5");
/*     */     }
/* 459 */     if (this.mv != null) {
/* 460 */       this.mv.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitJumpInsn(int opcode, Label label) {
/* 475 */     if (this.mv != null) {
/* 476 */       this.mv.visitJumpInsn(opcode, label);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitLabel(Label label) {
/* 486 */     if (this.mv != null) {
/* 487 */       this.mv.visitLabel(label);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitLdcInsn(Object value) {
/* 539 */     if (this.api < 327680 && (value instanceof Handle || (value instanceof Type && ((Type)value)
/*     */       
/* 541 */       .getSort() == 11))) {
/* 542 */       throw new UnsupportedOperationException("This feature requires ASM5");
/*     */     }
/* 544 */     if (this.api < 458752 && value instanceof ConstantDynamic) {
/* 545 */       throw new UnsupportedOperationException("This feature requires ASM7");
/*     */     }
/* 547 */     if (this.mv != null) {
/* 548 */       this.mv.visitLdcInsn(value);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitIincInsn(int var, int increment) {
/* 559 */     if (this.mv != null) {
/* 560 */       this.mv.visitIincInsn(var, increment);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
/* 575 */     if (this.mv != null) {
/* 576 */       this.mv.visitTableSwitchInsn(min, max, dflt, labels);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
/* 589 */     if (this.mv != null) {
/* 590 */       this.mv.visitLookupSwitchInsn(dflt, keys, labels);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitMultiANewArrayInsn(String descriptor, int numDimensions) {
/* 601 */     if (this.mv != null) {
/* 602 */       this.mv.visitMultiANewArrayInsn(descriptor, numDimensions);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
/* 627 */     if (this.api < 327680) {
/* 628 */       throw new UnsupportedOperationException("This feature requires ASM5");
/*     */     }
/* 630 */     if (this.mv != null) {
/* 631 */       return this.mv.visitInsnAnnotation(typeRef, typePath, descriptor, visible);
/*     */     }
/* 633 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
/* 653 */     if (this.mv != null) {
/* 654 */       this.mv.visitTryCatchBlock(start, end, handler, type);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
/* 675 */     if (this.api < 327680) {
/* 676 */       throw new UnsupportedOperationException("This feature requires ASM5");
/*     */     }
/* 678 */     if (this.mv != null) {
/* 679 */       return this.mv.visitTryCatchAnnotation(typeRef, typePath, descriptor, visible);
/*     */     }
/* 681 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
/* 705 */     if (this.mv != null) {
/* 706 */       this.mv.visitLocalVariable(name, descriptor, signature, start, end, index);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, Label[] end, int[] index, String descriptor, boolean visible) {
/* 738 */     if (this.api < 327680) {
/* 739 */       throw new UnsupportedOperationException("This feature requires ASM5");
/*     */     }
/* 741 */     if (this.mv != null) {
/* 742 */       return this.mv.visitLocalVariableAnnotation(typeRef, typePath, start, end, index, descriptor, visible);
/*     */     }
/*     */     
/* 745 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitLineNumber(int line, Label start) {
/* 758 */     if (this.mv != null) {
/* 759 */       this.mv.visitLineNumber(line, start);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitMaxs(int maxStack, int maxLocals) {
/* 770 */     if (this.mv != null) {
/* 771 */       this.mv.visitMaxs(maxStack, maxLocals);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitEnd() {
/* 780 */     if (this.mv != null)
/* 781 */       this.mv.visitEnd(); 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\asm\MethodVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */