/*    */ package com.intellij.uiDesigner.compiler;
/*    */ 
/*    */ import com.intellij.uiDesigner.lw.LwComponent;
/*    */ import javax.swing.JScrollPane;
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
/*    */ public class ScrollPaneLayoutCodeGenerator
/*    */   extends LayoutCodeGenerator
/*    */ {
/* 30 */   private final Type myScrollPaneType = Type.getType(JScrollPane.class);
/* 31 */   private final Method mySetViewportViewMethod = Method.getMethod("void setViewportView(java.awt.Component)");
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void generateComponentLayout(LwComponent lwComponent, GeneratorAdapter generator, int componentLocal, int parentLocal) {
/* 37 */     generator.loadLocal(parentLocal);
/* 38 */     generator.loadLocal(componentLocal);
/* 39 */     generator.invokeVirtual(this.myScrollPaneType, this.mySetViewportViewMethod);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\compiler\ScrollPaneLayoutCodeGenerator.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */