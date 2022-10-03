/*     */ package com.intellij.uiDesigner.compiler;
/*     */ 
/*     */ import com.intellij.uiDesigner.core.Spacer;
/*     */ import com.intellij.uiDesigner.lw.LwComponent;
/*     */ import com.intellij.uiDesigner.lw.LwContainer;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.Insets;
/*     */ import javax.swing.JPanel;
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
/*     */ public class GridBagLayoutCodeGenerator
/*     */   extends LayoutCodeGenerator
/*     */ {
/*  32 */   private static Type ourGridBagLayoutType = Type.getType(GridBagLayout.class);
/*  33 */   private static Type ourGridBagConstraintsType = Type.getType(GridBagConstraints.class);
/*  34 */   private static Method ourDefaultConstructor = Method.getMethod("void <init> ()");
/*     */   
/*  36 */   private static Type myPanelType = Type.getType(JPanel.class);
/*     */   
/*     */   public String mapComponentClass(String componentClassName) {
/*  39 */     if (componentClassName.equals(Spacer.class.getName())) {
/*  40 */       return JPanel.class.getName();
/*     */     }
/*  42 */     return super.mapComponentClass(componentClassName);
/*     */   }
/*     */   
/*     */   public void generateContainerLayout(LwContainer lwContainer, GeneratorAdapter generator, int componentLocal) {
/*  46 */     generator.loadLocal(componentLocal);
/*     */     
/*  48 */     generator.newInstance(ourGridBagLayoutType);
/*  49 */     generator.dup();
/*  50 */     generator.invokeConstructor(ourGridBagLayoutType, ourDefaultConstructor);
/*     */     
/*  52 */     generator.invokeVirtual(ourContainerType, ourSetLayoutMethod);
/*     */   }
/*     */   
/*     */   private void generateFillerPanel(GeneratorAdapter generator, int parentLocal, GridBagConverter.Result result) {
/*  56 */     int panelLocal = generator.newLocal(myPanelType);
/*     */     
/*  58 */     generator.newInstance(myPanelType);
/*  59 */     generator.dup();
/*  60 */     generator.invokeConstructor(myPanelType, ourDefaultConstructor);
/*  61 */     generator.storeLocal(panelLocal);
/*     */     
/*  63 */     generateConversionResult(generator, result, panelLocal, parentLocal);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void generateComponentLayout(LwComponent component, GeneratorAdapter generator, int componentLocal, int parentLocal) {
/*     */     GridBagConstraints gbc;
/*  72 */     if (component.getCustomLayoutConstraints() instanceof GridBagConstraints) {
/*  73 */       gbc = (GridBagConstraints)component.getCustomLayoutConstraints();
/*     */     } else {
/*     */       
/*  76 */       gbc = new GridBagConstraints();
/*     */     } 
/*     */     
/*  79 */     GridBagConverter.constraintsToGridBag(component.getConstraints(), gbc);
/*     */     
/*  81 */     generateGridBagConstraints(generator, gbc, componentLocal, parentLocal);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void generateConversionResult(GeneratorAdapter generator, GridBagConverter.Result result, int componentLocal, int parentLocal) {
/*  86 */     checkSetSize(generator, componentLocal, "setMinimumSize", result.minimumSize);
/*  87 */     checkSetSize(generator, componentLocal, "setPreferredSize", result.preferredSize);
/*  88 */     checkSetSize(generator, componentLocal, "setMaximumSize", result.maximumSize);
/*     */     
/*  90 */     generateGridBagConstraints(generator, result.constraints, componentLocal, parentLocal);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void generateGridBagConstraints(GeneratorAdapter generator, GridBagConstraints constraints, int componentLocal, int parentLocal) {
/*  95 */     int gbcLocal = generator.newLocal(ourGridBagConstraintsType);
/*     */     
/*  97 */     generator.newInstance(ourGridBagConstraintsType);
/*  98 */     generator.dup();
/*  99 */     generator.invokeConstructor(ourGridBagConstraintsType, ourDefaultConstructor);
/* 100 */     generator.storeLocal(gbcLocal);
/*     */     
/* 102 */     GridBagConstraints defaults = new GridBagConstraints();
/* 103 */     if (defaults.gridx != constraints.gridx) {
/* 104 */       setIntField(generator, gbcLocal, "gridx", constraints.gridx);
/*     */     }
/* 106 */     if (defaults.gridy != constraints.gridy) {
/* 107 */       setIntField(generator, gbcLocal, "gridy", constraints.gridy);
/*     */     }
/* 109 */     if (defaults.gridwidth != constraints.gridwidth) {
/* 110 */       setIntField(generator, gbcLocal, "gridwidth", constraints.gridwidth);
/*     */     }
/* 112 */     if (defaults.gridheight != constraints.gridheight) {
/* 113 */       setIntField(generator, gbcLocal, "gridheight", constraints.gridheight);
/*     */     }
/* 115 */     if (defaults.weightx != constraints.weightx) {
/* 116 */       setDoubleField(generator, gbcLocal, "weightx", constraints.weightx);
/*     */     }
/* 118 */     if (defaults.weighty != constraints.weighty) {
/* 119 */       setDoubleField(generator, gbcLocal, "weighty", constraints.weighty);
/*     */     }
/* 121 */     if (defaults.anchor != constraints.anchor) {
/* 122 */       setIntField(generator, gbcLocal, "anchor", constraints.anchor);
/*     */     }
/* 124 */     if (defaults.fill != constraints.fill) {
/* 125 */       setIntField(generator, gbcLocal, "fill", constraints.fill);
/*     */     }
/* 127 */     if (defaults.ipadx != constraints.ipadx) {
/* 128 */       setIntField(generator, gbcLocal, "ipadx", constraints.ipadx);
/*     */     }
/* 130 */     if (defaults.ipady != constraints.ipady) {
/* 131 */       setIntField(generator, gbcLocal, "ipady", constraints.ipady);
/*     */     }
/* 133 */     if (!defaults.insets.equals(constraints.insets)) {
/* 134 */       generator.loadLocal(gbcLocal);
/* 135 */       AsmCodeGenerator.pushPropValue(generator, Insets.class.getName(), constraints.insets);
/* 136 */       generator.putField(ourGridBagConstraintsType, "insets", Type.getType(Insets.class));
/*     */     } 
/*     */     
/* 139 */     generator.loadLocal(parentLocal);
/* 140 */     generator.loadLocal(componentLocal);
/* 141 */     generator.loadLocal(gbcLocal);
/*     */     
/* 143 */     generator.invokeVirtual(ourContainerType, ourAddMethod);
/*     */   }
/*     */   
/*     */   private static void checkSetSize(GeneratorAdapter generator, int componentLocal, String methodName, Dimension dimension) {
/* 147 */     if (dimension != null) {
/* 148 */       generator.loadLocal(componentLocal);
/* 149 */       AsmCodeGenerator.pushPropValue(generator, "java.awt.Dimension", dimension);
/* 150 */       generator.invokeVirtual(Type.getType(Component.class), new Method(methodName, Type.VOID_TYPE, new Type[] { Type.getType(Dimension.class) }));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void setIntField(GeneratorAdapter generator, int local, String fieldName, int value) {
/* 156 */     generator.loadLocal(local);
/* 157 */     generator.push(value);
/* 158 */     generator.putField(ourGridBagConstraintsType, fieldName, Type.INT_TYPE);
/*     */   }
/*     */   
/*     */   private static void setDoubleField(GeneratorAdapter generator, int local, String fieldName, double value) {
/* 162 */     generator.loadLocal(local);
/* 163 */     generator.push(value);
/* 164 */     generator.putField(ourGridBagConstraintsType, fieldName, Type.DOUBLE_TYPE);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\compiler\GridBagLayoutCodeGenerator.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */