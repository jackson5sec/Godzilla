/*     */ package com.intellij.uiDesigner.compiler;
/*     */ 
/*     */ import com.intellij.uiDesigner.lw.FontDescriptor;
/*     */ import com.intellij.uiDesigner.lw.IComponent;
/*     */ import com.intellij.uiDesigner.lw.LwComponent;
/*     */ import com.intellij.uiDesigner.lw.LwIntrospectedProperty;
/*     */ import java.awt.Font;
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
/*     */ public class FontPropertyCodeGenerator
/*     */   extends PropertyCodeGenerator
/*     */ {
/*  32 */   private static final Type ourFontType = Type.getType(Font.class);
/*  33 */   private static final Type ourUIManagerType = Type.getType("Ljavax/swing/UIManager;");
/*  34 */   private static final Type ourObjectType = Type.getType(Object.class);
/*  35 */   private static final Type ourStringType = Type.getType(String.class);
/*     */   
/*  37 */   private static final Method ourInitMethod = Method.getMethod("void <init>(java.lang.String,int,int)");
/*  38 */   private static final Method ourUIManagerGetFontMethod = new Method("getFont", ourFontType, new Type[] { ourObjectType });
/*  39 */   private static final Method ourGetNameMethod = new Method("getName", ourStringType, new Type[0]);
/*  40 */   private static final Method ourGetSizeMethod = new Method("getSize", Type.INT_TYPE, new Type[0]);
/*  41 */   private static final Method ourGetStyleMethod = new Method("getStyle", Type.INT_TYPE, new Type[0]);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean generateCustomSetValue(LwComponent lwComponent, Class componentClass, LwIntrospectedProperty property, GeneratorAdapter generator, int componentLocal, String formClassName) {
/*  48 */     FontDescriptor descriptor = (FontDescriptor)property.getPropertyValue((IComponent)lwComponent);
/*  49 */     if (descriptor.isFixedFont() && !descriptor.isFullyDefinedFont()) {
/*  50 */       generator.loadLocal(componentLocal);
/*  51 */       generatePushFont(generator, componentLocal, lwComponent, descriptor, property.getReadMethodName());
/*     */       
/*  53 */       Method setFontMethod = new Method(property.getWriteMethodName(), Type.VOID_TYPE, new Type[] { ourFontType });
/*  54 */       Type componentType = AsmCodeGenerator.typeFromClassName(lwComponent.getComponentClassName());
/*  55 */       generator.invokeVirtual(componentType, setFontMethod);
/*  56 */       return true;
/*     */     } 
/*  58 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void generatePushFont(GeneratorAdapter generator, int componentLocal, LwComponent lwComponent, FontDescriptor descriptor, String readMethodName) {
/*  63 */     int fontLocal = generator.newLocal(ourFontType);
/*     */     
/*  65 */     generator.loadLocal(componentLocal);
/*  66 */     Type componentType = AsmCodeGenerator.typeFromClassName(lwComponent.getComponentClassName());
/*  67 */     Method getFontMethod = new Method(readMethodName, ourFontType, new Type[0]);
/*  68 */     generator.invokeVirtual(componentType, getFontMethod);
/*  69 */     generator.storeLocal(fontLocal);
/*     */     
/*  71 */     generator.newInstance(ourFontType);
/*  72 */     generator.dup();
/*  73 */     if (descriptor.getFontName() != null) {
/*  74 */       generator.push(descriptor.getFontName());
/*     */     } else {
/*     */       
/*  77 */       generator.loadLocal(fontLocal);
/*  78 */       generator.invokeVirtual(ourFontType, ourGetNameMethod);
/*     */     } 
/*     */     
/*  81 */     if (descriptor.getFontStyle() >= 0) {
/*  82 */       generator.push(descriptor.getFontStyle());
/*     */     } else {
/*     */       
/*  85 */       generator.loadLocal(fontLocal);
/*  86 */       generator.invokeVirtual(ourFontType, ourGetStyleMethod);
/*     */     } 
/*     */     
/*  89 */     if (descriptor.getFontSize() >= 0) {
/*  90 */       generator.push(descriptor.getFontSize());
/*     */     } else {
/*     */       
/*  93 */       generator.loadLocal(fontLocal);
/*  94 */       generator.invokeVirtual(ourFontType, ourGetSizeMethod);
/*     */     } 
/*  96 */     generator.invokeConstructor(ourFontType, ourInitMethod);
/*     */   }
/*     */   
/*     */   public void generatePushValue(GeneratorAdapter generator, Object value) {
/* 100 */     FontDescriptor descriptor = (FontDescriptor)value;
/* 101 */     if (descriptor.isFixedFont()) {
/* 102 */       if (!descriptor.isFullyDefinedFont()) throw new IllegalStateException("Unexpected font state"); 
/* 103 */       generator.newInstance(ourFontType);
/* 104 */       generator.dup();
/* 105 */       generator.push(descriptor.getFontName());
/* 106 */       generator.push(descriptor.getFontStyle());
/* 107 */       generator.push(descriptor.getFontSize());
/* 108 */       generator.invokeConstructor(ourFontType, ourInitMethod);
/*     */     }
/* 110 */     else if (descriptor.getSwingFont() != null) {
/* 111 */       generator.push(descriptor.getSwingFont());
/* 112 */       generator.invokeStatic(ourUIManagerType, ourUIManagerGetFontMethod);
/*     */     } else {
/*     */       
/* 115 */       throw new IllegalStateException("Unknown font type");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\compiler\FontPropertyCodeGenerator.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */