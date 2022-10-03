/*    */ package com.intellij.uiDesigner.compiler;
/*    */ 
/*    */ import com.intellij.uiDesigner.lw.LwComponent;
/*    */ import javax.swing.JSplitPane;
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
/*    */ public class SplitPaneLayoutCodeGenerator
/*    */   extends LayoutCodeGenerator
/*    */ {
/* 30 */   private final Type mySplitPaneType = Type.getType(JSplitPane.class);
/* 31 */   private final Method mySetLeftMethod = Method.getMethod("void setLeftComponent(java.awt.Component)");
/* 32 */   private final Method mySetRightMethod = Method.getMethod("void setRightComponent(java.awt.Component)");
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void generateComponentLayout(LwComponent lwComponent, GeneratorAdapter generator, int componentLocal, int parentLocal) {
/* 38 */     generator.loadLocal(parentLocal);
/* 39 */     generator.loadLocal(componentLocal);
/* 40 */     if ("left".equals(lwComponent.getCustomLayoutConstraints())) {
/* 41 */       generator.invokeVirtual(this.mySplitPaneType, this.mySetLeftMethod);
/*    */     } else {
/*    */       
/* 44 */       generator.invokeVirtual(this.mySplitPaneType, this.mySetRightMethod);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\compiler\SplitPaneLayoutCodeGenerator.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */