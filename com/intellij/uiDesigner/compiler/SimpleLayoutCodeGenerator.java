/*    */ package com.intellij.uiDesigner.compiler;
/*    */ 
/*    */ import com.intellij.uiDesigner.lw.LwComponent;
/*    */ import com.intellij.uiDesigner.lw.LwContainer;
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
/*    */ 
/*    */ public class SimpleLayoutCodeGenerator
/*    */   extends LayoutCodeGenerator
/*    */ {
/*    */   private final Type myLayoutType;
/* 32 */   private static Method ourConstructor = Method.getMethod("void <init>(int,int)");
/*    */   
/*    */   public SimpleLayoutCodeGenerator(Type layoutType) {
/* 35 */     this.myLayoutType = layoutType;
/*    */   }
/*    */   
/*    */   public void generateContainerLayout(LwContainer lwContainer, GeneratorAdapter generator, int componentLocal) {
/* 39 */     generator.loadLocal(componentLocal);
/*    */     
/* 41 */     generator.newInstance(this.myLayoutType);
/* 42 */     generator.dup();
/* 43 */     generator.push(Utils.getHGap(lwContainer.getLayout()));
/* 44 */     generator.push(Utils.getVGap(lwContainer.getLayout()));
/*    */     
/* 46 */     generator.invokeConstructor(this.myLayoutType, ourConstructor);
/*    */     
/* 48 */     generator.invokeVirtual(ourContainerType, ourSetLayoutMethod);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void generateComponentLayout(LwComponent lwComponent, GeneratorAdapter generator, int componentLocal, int parentLocal) {
/* 55 */     generator.loadLocal(parentLocal);
/* 56 */     generator.loadLocal(componentLocal);
/* 57 */     generator.push((String)lwComponent.getCustomLayoutConstraints());
/* 58 */     generator.invokeVirtual(ourContainerType, ourAddMethod);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\compiler\SimpleLayoutCodeGenerator.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */