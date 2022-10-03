/*    */ package com.intellij.uiDesigner.compiler;
/*    */ 
/*    */ import com.intellij.uiDesigner.lw.LwComponent;
/*    */ import com.intellij.uiDesigner.lw.LwContainer;
/*    */ import java.awt.Container;
/*    */ import java.awt.Dimension;
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
/*    */ 
/*    */ 
/*    */ public abstract class LayoutCodeGenerator
/*    */ {
/* 32 */   protected static final Method ourSetLayoutMethod = Method.getMethod("void setLayout(java.awt.LayoutManager)");
/* 33 */   protected static final Type ourContainerType = Type.getType(Container.class);
/* 34 */   protected static final Method ourAddMethod = Method.getMethod("void add(java.awt.Component,java.lang.Object)");
/* 35 */   protected static final Method ourAddNoConstraintMethod = Method.getMethod("java.awt.Component add(java.awt.Component)");
/*    */ 
/*    */ 
/*    */   
/*    */   public void generateContainerLayout(LwContainer lwContainer, GeneratorAdapter generator, int componentLocal) {}
/*    */ 
/*    */ 
/*    */   
/*    */   protected static void newDimensionOrNull(GeneratorAdapter generator, Dimension dimension) {
/* 44 */     if (dimension.width == -1 && dimension.height == -1) {
/* 45 */       generator.visitInsn(1);
/*    */     } else {
/*    */       
/* 48 */       AsmCodeGenerator.pushPropValue(generator, "java.awt.Dimension", dimension);
/*    */     } 
/*    */   }
/*    */   
/*    */   public String mapComponentClass(String componentClassName) {
/* 53 */     return componentClassName;
/*    */   }
/*    */   
/*    */   public abstract void generateComponentLayout(LwComponent paramLwComponent, GeneratorAdapter paramGeneratorAdapter, int paramInt1, int paramInt2);
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\compiler\LayoutCodeGenerator.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */