/*    */ package com.intellij.uiDesigner.compiler;
/*    */ 
/*    */ import com.intellij.uiDesigner.lw.ColorDescriptor;
/*    */ import java.awt.Color;
/*    */ import java.awt.SystemColor;
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
/*    */ public class ColorPropertyCodeGenerator
/*    */   extends PropertyCodeGenerator
/*    */ {
/* 30 */   private static final Type ourColorType = Type.getType(Color.class);
/* 31 */   private static final Type ourObjectType = Type.getType(Object.class);
/* 32 */   private static final Type ourUIManagerType = Type.getType("Ljavax/swing/UIManager;");
/* 33 */   private static final Type ourSystemColorType = Type.getType(SystemColor.class);
/*    */   
/* 35 */   private static final Method ourInitMethod = Method.getMethod("void <init>(int)");
/* 36 */   private static final Method ourGetColorMethod = new Method("getColor", ourColorType, new Type[] { ourObjectType });
/*    */   
/*    */   public void generatePushValue(GeneratorAdapter generator, Object value) {
/* 39 */     ColorDescriptor descriptor = (ColorDescriptor)value;
/* 40 */     if (descriptor.getColor() != null) {
/* 41 */       generator.newInstance(ourColorType);
/* 42 */       generator.dup();
/* 43 */       generator.push(descriptor.getColor().getRGB());
/* 44 */       generator.invokeConstructor(ourColorType, ourInitMethod);
/*    */     }
/* 46 */     else if (descriptor.getSwingColor() != null) {
/* 47 */       generator.push(descriptor.getSwingColor());
/* 48 */       generator.invokeStatic(ourUIManagerType, ourGetColorMethod);
/*    */     }
/* 50 */     else if (descriptor.getSystemColor() != null) {
/* 51 */       generator.getStatic(ourSystemColorType, descriptor.getSystemColor(), ourSystemColorType);
/*    */     }
/* 53 */     else if (descriptor.getAWTColor() != null) {
/* 54 */       generator.getStatic(ourColorType, descriptor.getAWTColor(), ourColorType);
/*    */     }
/* 56 */     else if (descriptor.isColorSet()) {
/* 57 */       throw new IllegalStateException("Unknown color type");
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\compiler\ColorPropertyCodeGenerator.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */