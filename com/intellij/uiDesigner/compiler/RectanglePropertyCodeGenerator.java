/*    */ package com.intellij.uiDesigner.compiler;
/*    */ 
/*    */ import java.awt.Rectangle;
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
/*    */ public class RectanglePropertyCodeGenerator
/*    */   extends PropertyCodeGenerator
/*    */ {
/* 28 */   private static Type myRectangleType = Type.getType(Rectangle.class);
/* 29 */   private static Method myInitMethod = Method.getMethod("void <init>(int,int,int,int)");
/*    */   
/*    */   public void generatePushValue(GeneratorAdapter generator, Object value) {
/* 32 */     Rectangle rc = (Rectangle)value;
/* 33 */     generator.newInstance(myRectangleType);
/* 34 */     generator.dup();
/* 35 */     generator.push(rc.x);
/* 36 */     generator.push(rc.y);
/* 37 */     generator.push(rc.width);
/* 38 */     generator.push(rc.height);
/* 39 */     generator.invokeConstructor(myRectangleType, myInitMethod);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\compiler\RectanglePropertyCodeGenerator.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */