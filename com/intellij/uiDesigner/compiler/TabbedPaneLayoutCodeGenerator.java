/*    */ package com.intellij.uiDesigner.compiler;
/*    */ 
/*    */ import com.intellij.uiDesigner.lw.IComponent;
/*    */ import com.intellij.uiDesigner.lw.LwComponent;
/*    */ import com.intellij.uiDesigner.lw.LwTabbedPane;
/*    */ import javax.swing.Icon;
/*    */ import javax.swing.JTabbedPane;
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
/*    */ public class TabbedPaneLayoutCodeGenerator
/*    */   extends LayoutCodeGenerator
/*    */ {
/* 30 */   private final Type myTabbedPaneType = Type.getType(JTabbedPane.class);
/* 31 */   private final Method myAddTabMethod = Method.getMethod("void addTab(java.lang.String,javax.swing.Icon,java.awt.Component,java.lang.String)");
/* 32 */   private final Method mySetDisabledIconAtMethod = Method.getMethod("void setDisabledIconAt(int,javax.swing.Icon)");
/* 33 */   private final Method mySetEnabledAtMethod = Method.getMethod("void setEnabledAt(int,boolean)");
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void generateComponentLayout(LwComponent lwComponent, GeneratorAdapter generator, int componentLocal, int parentLocal) {
/* 39 */     generator.loadLocal(parentLocal);
/* 40 */     LwTabbedPane.Constraints tabConstraints = (LwTabbedPane.Constraints)lwComponent.getCustomLayoutConstraints();
/* 41 */     if (tabConstraints == null) {
/* 42 */       throw new IllegalArgumentException("tab constraints cannot be null: " + lwComponent.getId());
/*    */     }
/* 44 */     AsmCodeGenerator.pushPropValue(generator, String.class.getName(), tabConstraints.myTitle);
/* 45 */     if (tabConstraints.myIcon == null) {
/* 46 */       generator.push((String)null);
/*    */     } else {
/*    */       
/* 49 */       AsmCodeGenerator.pushPropValue(generator, Icon.class.getName(), tabConstraints.myIcon);
/*    */     } 
/* 51 */     generator.loadLocal(componentLocal);
/* 52 */     if (tabConstraints.myToolTip == null) {
/* 53 */       generator.push((String)null);
/*    */     } else {
/*    */       
/* 56 */       AsmCodeGenerator.pushPropValue(generator, String.class.getName(), tabConstraints.myToolTip);
/*    */     } 
/* 58 */     generator.invokeVirtual(this.myTabbedPaneType, this.myAddTabMethod);
/*    */     
/* 60 */     int index = lwComponent.getParent().indexOfComponent((IComponent)lwComponent);
/* 61 */     if (tabConstraints.myDisabledIcon != null) {
/* 62 */       generator.loadLocal(parentLocal);
/* 63 */       generator.push(index);
/* 64 */       AsmCodeGenerator.pushPropValue(generator, Icon.class.getName(), tabConstraints.myDisabledIcon);
/* 65 */       generator.invokeVirtual(this.myTabbedPaneType, this.mySetDisabledIconAtMethod);
/*    */     } 
/* 67 */     if (!tabConstraints.myEnabled) {
/* 68 */       generator.loadLocal(parentLocal);
/* 69 */       generator.push(index);
/* 70 */       generator.push(tabConstraints.myEnabled);
/* 71 */       generator.invokeVirtual(this.myTabbedPaneType, this.mySetEnabledAtMethod);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\compiler\TabbedPaneLayoutCodeGenerator.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */