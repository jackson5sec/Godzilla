/*     */ package org.fife.rsta.ac.java.classreader;
/*     */ 
/*     */ import java.util.Stack;
/*     */ import org.fife.rsta.ac.java.classreader.attributes.Code;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Frame
/*     */ {
/*     */   private Stack<String> operandStack;
/*     */   private LocalVarInfo[] localVars;
/*     */   
/*     */   public Frame(Code code) {
/*  38 */     this.operandStack = new Stack<>();
/*     */     
/*  40 */     this.localVars = new LocalVarInfo[code.getMaxLocals()];
/*  41 */     int i = 0;
/*  42 */     MethodInfo mi = code.getMethodInfo();
/*     */ 
/*     */     
/*  45 */     if (!mi.isStatic()) {
/*  46 */       this.localVars[i++] = new LocalVarInfo("this", true);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  51 */     String[] paramTypes = mi.getParameterTypes();
/*  52 */     for (int param_i = 0; param_i < paramTypes.length; param_i++) {
/*  53 */       String type = paramTypes[param_i];
/*  54 */       if (type.indexOf('.') > -1) {
/*  55 */         type = type.substring(type.lastIndexOf('.') + 1);
/*     */       }
/*  57 */       String name = "localVar_" + type + "_" + param_i;
/*  58 */       this.localVars[i] = new LocalVarInfo(name, true);
/*  59 */       i++;
/*  60 */       if ("long".equals(type) || "double".equals(type)) {
/*  61 */         i++;
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  67 */     System.out.println("NOTE: " + (this.localVars.length - i) + " unknown localVars slots");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public LocalVarInfo getLocalVar(int index, String defaultType) {
/*  73 */     LocalVarInfo var = this.localVars[index];
/*  74 */     if (var == null) {
/*  75 */       String name = "localVar_" + defaultType + "_" + index;
/*  76 */       var = new LocalVarInfo(name, false);
/*  77 */       this.localVars[index] = var;
/*     */     } else {
/*     */       
/*  80 */       var.alreadyDeclared = true;
/*     */     } 
/*  82 */     return var;
/*     */   }
/*     */ 
/*     */   
/*     */   public String pop() {
/*  87 */     return this.operandStack.pop();
/*     */   }
/*     */ 
/*     */   
/*     */   public void push(String value) {
/*  92 */     this.operandStack.push(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public static class LocalVarInfo
/*     */   {
/*     */     private String value;
/*     */     private boolean alreadyDeclared;
/*     */     
/*     */     public LocalVarInfo(String value, boolean alreadyDeclared) {
/* 102 */       this.value = value;
/* 103 */       this.alreadyDeclared = alreadyDeclared;
/*     */     }
/*     */     
/*     */     public String getValue() {
/* 107 */       return this.value;
/*     */     }
/*     */     
/*     */     public boolean isAlreadyDeclared() {
/* 111 */       return this.alreadyDeclared;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\classreader\Frame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */