/*    */ package com.intellij.uiDesigner.compiler;
/*    */ 
/*    */ import com.intellij.uiDesigner.lw.IconDescriptor;
/*    */ import javax.swing.ImageIcon;
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
/*    */ public class IconPropertyCodeGenerator
/*    */   extends PropertyCodeGenerator
/*    */ {
/* 29 */   private static final Type ourImageIconType = Type.getType(ImageIcon.class);
/* 30 */   private static final Method ourInitMethod = Method.getMethod("void <init>(java.net.URL)");
/* 31 */   private static final Method ourGetResourceMethod = Method.getMethod("java.net.URL getResource(java.lang.String)");
/* 32 */   private static final Method ourGetClassMethod = new Method("getClass", "()Ljava/lang/Class;");
/* 33 */   private static final Type ourObjectType = Type.getType(Object.class);
/* 34 */   private static final Type ourClassType = Type.getType(Class.class);
/*    */   
/*    */   public void generatePushValue(GeneratorAdapter generator, Object value) {
/* 37 */     IconDescriptor descriptor = (IconDescriptor)value;
/* 38 */     generator.newInstance(ourImageIconType);
/* 39 */     generator.dup();
/*    */     
/* 41 */     generator.loadThis();
/* 42 */     generator.invokeVirtual(ourObjectType, ourGetClassMethod);
/* 43 */     generator.push("/" + descriptor.getIconPath());
/* 44 */     generator.invokeVirtual(ourClassType, ourGetResourceMethod);
/*    */     
/* 46 */     generator.invokeConstructor(ourImageIconType, ourInitMethod);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\compiler\IconPropertyCodeGenerator.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */