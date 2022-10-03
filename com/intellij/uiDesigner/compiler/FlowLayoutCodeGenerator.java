/*    */ package com.intellij.uiDesigner.compiler;
/*    */ 
/*    */ import com.intellij.uiDesigner.lw.LwComponent;
/*    */ import com.intellij.uiDesigner.lw.LwContainer;
/*    */ import java.awt.FlowLayout;
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
/*    */ public class FlowLayoutCodeGenerator
/*    */   extends LayoutCodeGenerator
/*    */ {
/* 31 */   private static Type ourFlowLayoutType = Type.getType(FlowLayout.class);
/* 32 */   private static Method ourConstructor = Method.getMethod("void <init>(int,int,int)");
/*    */   
/*    */   public void generateContainerLayout(LwContainer lwContainer, GeneratorAdapter generator, int componentLocal) {
/* 35 */     generator.loadLocal(componentLocal);
/*    */     
/* 37 */     FlowLayout flowLayout = (FlowLayout)lwContainer.getLayout();
/* 38 */     generator.newInstance(ourFlowLayoutType);
/* 39 */     generator.dup();
/* 40 */     generator.push(flowLayout.getAlignment());
/* 41 */     generator.push(flowLayout.getHgap());
/* 42 */     generator.push(flowLayout.getVgap());
/* 43 */     generator.invokeConstructor(ourFlowLayoutType, ourConstructor);
/*    */     
/* 45 */     generator.invokeVirtual(ourContainerType, ourSetLayoutMethod);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void generateComponentLayout(LwComponent lwComponent, GeneratorAdapter generator, int componentLocal, int parentLocal) {
/* 51 */     generator.loadLocal(parentLocal);
/* 52 */     generator.loadLocal(componentLocal);
/* 53 */     generator.invokeVirtual(ourContainerType, ourAddNoConstraintMethod);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\compiler\FlowLayoutCodeGenerator.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */