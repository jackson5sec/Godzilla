/*    */ package com.intellij.uiDesigner.compiler;
/*    */ 
/*    */ import java.awt.Insets;
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
/*    */ public class InsetsPropertyCodeGenerator
/*    */   extends PropertyCodeGenerator
/*    */ {
/* 17 */   private final Type myInsetsType = Type.getType(Insets.class);
/*    */   
/*    */   public void generatePushValue(GeneratorAdapter generator, Object value) {
/* 20 */     Insets insets = (Insets)value;
/* 21 */     generator.newInstance(this.myInsetsType);
/* 22 */     generator.dup();
/* 23 */     generator.push(insets.top);
/* 24 */     generator.push(insets.left);
/* 25 */     generator.push(insets.bottom);
/* 26 */     generator.push(insets.right);
/* 27 */     generator.invokeConstructor(this.myInsetsType, Method.getMethod("void <init>(int,int,int,int)"));
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\compiler\InsetsPropertyCodeGenerator.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */