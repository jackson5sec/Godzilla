/*    */ package com.intellij.uiDesigner.compiler;
/*    */ 
/*    */ import java.awt.Dimension;
/*    */ import org.objectweb.asm.Type;
/*    */ import org.objectweb.asm.commons.GeneratorAdapter;
/*    */ import org.objectweb.asm.commons.Method;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DimensionPropertyCodeGenerator
/*    */   extends PropertyCodeGenerator
/*    */ {
/* 14 */   private static final Type myDimensionType = Type.getType(Dimension.class);
/* 15 */   private static final Method myInitMethod = Method.getMethod("void <init>(int,int)");
/*    */   
/*    */   public void generatePushValue(GeneratorAdapter generator, Object value) {
/* 18 */     Dimension dimension = (Dimension)value;
/* 19 */     generator.newInstance(myDimensionType);
/* 20 */     generator.dup();
/* 21 */     generator.push(dimension.width);
/* 22 */     generator.push(dimension.height);
/* 23 */     generator.invokeConstructor(myDimensionType, myInitMethod);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\compiler\DimensionPropertyCodeGenerator.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */