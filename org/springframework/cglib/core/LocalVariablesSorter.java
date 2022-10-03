/*     */ package org.springframework.cglib.core;
/*     */ 
/*     */ import org.springframework.asm.Label;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.asm.Type;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LocalVariablesSorter
/*     */   extends MethodVisitor
/*     */ {
/*     */   protected final int firstLocal;
/*     */   private final State state;
/*     */   
/*     */   private static class State
/*     */   {
/*     */     private State() {}
/*     */     
/*  54 */     int[] mapping = new int[40];
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int nextLocal;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LocalVariablesSorter(int access, String desc, MethodVisitor mv) {
/*  66 */     super(Constants.ASM_API, mv);
/*  67 */     this.state = new State();
/*  68 */     Type[] args = Type.getArgumentTypes(desc);
/*  69 */     this.state.nextLocal = ((0x8 & access) != 0) ? 0 : 1;
/*  70 */     for (int i = 0; i < args.length; i++) {
/*  71 */       this.state.nextLocal += args[i].getSize();
/*     */     }
/*  73 */     this.firstLocal = this.state.nextLocal;
/*     */   }
/*     */   
/*     */   public LocalVariablesSorter(LocalVariablesSorter lvs) {
/*  77 */     super(Constants.ASM_API, lvs.mv);
/*  78 */     this.state = lvs.state;
/*  79 */     this.firstLocal = lvs.firstLocal;
/*     */   }
/*     */   
/*     */   public void visitVarInsn(int opcode, int var) {
/*     */     int size;
/*  84 */     switch (opcode) {
/*     */       case 22:
/*     */       case 24:
/*     */       case 55:
/*     */       case 57:
/*  89 */         size = 2;
/*     */         break;
/*     */       default:
/*  92 */         size = 1; break;
/*     */     } 
/*  94 */     this.mv.visitVarInsn(opcode, remap(var, size));
/*     */   }
/*     */   
/*     */   public void visitIincInsn(int var, int increment) {
/*  98 */     this.mv.visitIincInsn(remap(var, 1), increment);
/*     */   }
/*     */   
/*     */   public void visitMaxs(int maxStack, int maxLocals) {
/* 102 */     this.mv.visitMaxs(maxStack, this.state.nextLocal);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
/* 113 */     this.mv.visitLocalVariable(name, desc, signature, start, end, remap(index));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected int newLocal(int size) {
/* 119 */     int var = this.state.nextLocal;
/* 120 */     this.state.nextLocal += size;
/* 121 */     return var;
/*     */   }
/*     */   
/*     */   private int remap(int var, int size) {
/* 125 */     if (var < this.firstLocal) {
/* 126 */       return var;
/*     */     }
/* 128 */     int key = 2 * var + size - 1;
/* 129 */     int length = this.state.mapping.length;
/* 130 */     if (key >= length) {
/* 131 */       int[] newMapping = new int[Math.max(2 * length, key + 1)];
/* 132 */       System.arraycopy(this.state.mapping, 0, newMapping, 0, length);
/* 133 */       this.state.mapping = newMapping;
/*     */     } 
/* 135 */     int value = this.state.mapping[key];
/* 136 */     if (value == 0) {
/* 137 */       value = this.state.nextLocal + 1;
/* 138 */       this.state.mapping[key] = value;
/* 139 */       this.state.nextLocal += size;
/*     */     } 
/* 141 */     return value - 1;
/*     */   }
/*     */   
/*     */   private int remap(int var) {
/* 145 */     if (var < this.firstLocal) {
/* 146 */       return var;
/*     */     }
/* 148 */     int key = 2 * var;
/* 149 */     int value = (key < this.state.mapping.length) ? this.state.mapping[key] : 0;
/* 150 */     if (value == 0) {
/* 151 */       value = (key + 1 < this.state.mapping.length) ? this.state.mapping[key + 1] : 0;
/*     */     }
/* 153 */     if (value == 0) {
/* 154 */       throw new IllegalStateException("Unknown local variable " + var);
/*     */     }
/* 156 */     return value - 1;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\core\LocalVariablesSorter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */