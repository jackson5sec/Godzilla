/*    */ package com.intellij.uiDesigner.compiler;
/*    */ 
/*    */ import org.objectweb.asm.Type;
/*    */ import org.objectweb.asm.commons.GeneratorAdapter;
/*    */ import org.objectweb.asm.commons.Method;
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
/*    */ public class ListModelPropertyCodeGenerator
/*    */   extends PropertyCodeGenerator
/*    */ {
/*    */   private final Type myListModelType;
/* 28 */   private static final Method ourInitMethod = Method.getMethod("void <init>()");
/* 29 */   private static final Method ourAddElementMethod = Method.getMethod("void addElement(java.lang.Object)");
/*    */   
/*    */   public ListModelPropertyCodeGenerator(Class aClass) {
/* 32 */     this.myListModelType = Type.getType(aClass);
/*    */   }
/*    */   
/*    */   public void generatePushValue(GeneratorAdapter generator, Object value) {
/* 36 */     String[] items = (String[])value;
/* 37 */     int listModelLocal = generator.newLocal(this.myListModelType);
/*    */     
/* 39 */     generator.newInstance(this.myListModelType);
/* 40 */     generator.dup();
/* 41 */     generator.invokeConstructor(this.myListModelType, ourInitMethod);
/* 42 */     generator.storeLocal(listModelLocal);
/*    */     
/* 44 */     for (int i = 0; i < items.length; i++) {
/* 45 */       generator.loadLocal(listModelLocal);
/* 46 */       generator.push(items[i]);
/* 47 */       generator.invokeVirtual(this.myListModelType, ourAddElementMethod);
/*    */     } 
/*    */     
/* 50 */     generator.loadLocal(listModelLocal);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\compiler\ListModelPropertyCodeGenerator.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */