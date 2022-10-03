/*     */ package com.intellij.uiDesigner.compiler;
/*     */ 
/*     */ import com.intellij.uiDesigner.core.GridConstraints;
/*     */ import com.intellij.uiDesigner.core.GridLayoutManager;
/*     */ import com.intellij.uiDesigner.lw.LwComponent;
/*     */ import com.intellij.uiDesigner.lw.LwContainer;
/*     */ import org.objectweb.asm.Type;
/*     */ import org.objectweb.asm.commons.GeneratorAdapter;
/*     */ import org.objectweb.asm.commons.Method;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GridLayoutCodeGenerator
/*     */   extends LayoutCodeGenerator
/*     */ {
/*  31 */   private static final Method myInitConstraintsMethod = Method.getMethod("void <init> (int,int,int,int,int,int,int,int,java.awt.Dimension,java.awt.Dimension,java.awt.Dimension)");
/*  32 */   private static final Method myInitConstraintsIndentMethod = Method.getMethod("void <init> (int,int,int,int,int,int,int,int,java.awt.Dimension,java.awt.Dimension,java.awt.Dimension,int)");
/*  33 */   private static final Method myInitConstraintsIndentParentMethod = Method.getMethod("void <init> (int,int,int,int,int,int,int,int,java.awt.Dimension,java.awt.Dimension,java.awt.Dimension,int,boolean)");
/*  34 */   private static final Method ourGridLayoutManagerConstructor = Method.getMethod("void <init> (int,int,java.awt.Insets,int,int,boolean,boolean)");
/*  35 */   private static final Type myGridLayoutManagerType = Type.getType(GridLayoutManager.class);
/*  36 */   private static final Type myGridConstraintsType = Type.getType(GridConstraints.class);
/*     */   
/*  38 */   public static GridLayoutCodeGenerator INSTANCE = new GridLayoutCodeGenerator();
/*     */   
/*     */   public void generateContainerLayout(LwContainer lwContainer, GeneratorAdapter generator, int componentLocal) {
/*  41 */     if (lwContainer.isGrid()) {
/*     */       
/*  43 */       generator.loadLocal(componentLocal);
/*     */ 
/*     */       
/*  46 */       GridLayoutManager layout = (GridLayoutManager)lwContainer.getLayout();
/*     */       
/*  48 */       generator.newInstance(myGridLayoutManagerType);
/*  49 */       generator.dup();
/*  50 */       generator.push(layout.getRowCount());
/*  51 */       generator.push(layout.getColumnCount());
/*  52 */       AsmCodeGenerator.pushPropValue(generator, "java.awt.Insets", layout.getMargin());
/*  53 */       generator.push(layout.getHGap());
/*  54 */       generator.push(layout.getVGap());
/*  55 */       generator.push(layout.isSameSizeHorizontally());
/*  56 */       generator.push(layout.isSameSizeVertically());
/*  57 */       generator.invokeConstructor(myGridLayoutManagerType, ourGridLayoutManagerConstructor);
/*     */       
/*  59 */       generator.invokeVirtual(ourContainerType, ourSetLayoutMethod);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void generateComponentLayout(LwComponent lwComponent, GeneratorAdapter generator, int componentLocal, int parentLocal) {
/*  67 */     generator.loadLocal(parentLocal);
/*  68 */     generator.loadLocal(componentLocal);
/*  69 */     addNewGridConstraints(generator, lwComponent);
/*  70 */     generator.invokeVirtual(ourContainerType, ourAddMethod);
/*     */   }
/*     */   
/*     */   private static void addNewGridConstraints(GeneratorAdapter generator, LwComponent lwComponent) {
/*  74 */     GridConstraints constraints = lwComponent.getConstraints();
/*     */     
/*  76 */     generator.newInstance(myGridConstraintsType);
/*  77 */     generator.dup();
/*  78 */     generator.push(constraints.getRow());
/*  79 */     generator.push(constraints.getColumn());
/*  80 */     generator.push(constraints.getRowSpan());
/*  81 */     generator.push(constraints.getColSpan());
/*  82 */     generator.push(constraints.getAnchor());
/*  83 */     generator.push(constraints.getFill());
/*  84 */     generator.push(constraints.getHSizePolicy());
/*  85 */     generator.push(constraints.getVSizePolicy());
/*  86 */     newDimensionOrNull(generator, constraints.myMinimumSize);
/*  87 */     newDimensionOrNull(generator, constraints.myPreferredSize);
/*  88 */     newDimensionOrNull(generator, constraints.myMaximumSize);
/*     */     
/*  90 */     if (constraints.isUseParentLayout()) {
/*  91 */       generator.push(constraints.getIndent());
/*  92 */       generator.push(constraints.isUseParentLayout());
/*  93 */       generator.invokeConstructor(myGridConstraintsType, myInitConstraintsIndentParentMethod);
/*     */     }
/*  95 */     else if (constraints.getIndent() != 0) {
/*  96 */       generator.push(constraints.getIndent());
/*  97 */       generator.invokeConstructor(myGridConstraintsType, myInitConstraintsIndentMethod);
/*     */     } else {
/*     */       
/* 100 */       generator.invokeConstructor(myGridConstraintsType, myInitConstraintsMethod);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\compiler\GridLayoutCodeGenerator.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */