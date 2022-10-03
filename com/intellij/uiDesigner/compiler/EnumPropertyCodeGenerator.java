/*    */ package com.intellij.uiDesigner.compiler;
/*    */ 
/*    */ import org.objectweb.asm.Type;
/*    */ import org.objectweb.asm.commons.GeneratorAdapter;
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
/*    */ public class EnumPropertyCodeGenerator
/*    */   extends PropertyCodeGenerator
/*    */ {
/*    */   public void generatePushValue(GeneratorAdapter generator, Object value) {
/* 27 */     Type enumType = Type.getType(value.getClass());
/* 28 */     generator.getStatic(enumType, value.toString(), enumType);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\compiler\EnumPropertyCodeGenerator.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */