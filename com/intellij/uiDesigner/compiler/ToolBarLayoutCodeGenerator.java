/*    */ package com.intellij.uiDesigner.compiler;
/*    */ 
/*    */ import com.intellij.uiDesigner.lw.LwComponent;
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
/*    */ public class ToolBarLayoutCodeGenerator
/*    */   extends LayoutCodeGenerator
/*    */ {
/* 27 */   private static final Method ourAddMethod = Method.getMethod("java.awt.Component add(java.awt.Component)");
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void generateComponentLayout(LwComponent lwComponent, GeneratorAdapter generator, int componentLocal, int parentLocal) {
/* 33 */     generator.loadLocal(parentLocal);
/* 34 */     generator.loadLocal(componentLocal);
/* 35 */     generator.invokeVirtual(ourContainerType, ourAddMethod);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\compiler\ToolBarLayoutCodeGenerator.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */