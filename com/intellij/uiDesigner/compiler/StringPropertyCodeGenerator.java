/*     */ package com.intellij.uiDesigner.compiler;
/*     */ 
/*     */ import com.intellij.uiDesigner.core.SupportCode;
/*     */ import com.intellij.uiDesigner.lw.LwComponent;
/*     */ import com.intellij.uiDesigner.lw.LwIntrospectedProperty;
/*     */ import com.intellij.uiDesigner.lw.StringDescriptor;
/*     */ import java.util.HashSet;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.Set;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.JLabel;
/*     */ import org.objectweb.asm.Label;
/*     */ import org.objectweb.asm.MethodVisitor;
/*     */ import org.objectweb.asm.Opcodes;
/*     */ import org.objectweb.asm.Type;
/*     */ import org.objectweb.asm.commons.GeneratorAdapter;
/*     */ import org.objectweb.asm.commons.Method;
/*     */ 
/*     */ public class StringPropertyCodeGenerator
/*     */   extends PropertyCodeGenerator
/*     */   implements Opcodes
/*     */ {
/*  23 */   private static final Type myResourceBundleType = Type.getType(ResourceBundle.class);
/*  24 */   private final Method myGetBundleMethod = Method.getMethod("java.util.ResourceBundle getBundle(java.lang.String)");
/*  25 */   private final Method myGetStringMethod = Method.getMethod("java.lang.String getString(java.lang.String)");
/*  26 */   private static final Method myLoadLabelTextMethod = new Method("$$$loadLabelText$$$", Type.VOID_TYPE, new Type[] { Type.getType(JLabel.class), Type.getType(String.class) });
/*     */   
/*  28 */   private static final Method myLoadButtonTextMethod = new Method("$$$loadButtonText$$$", Type.VOID_TYPE, new Type[] { Type.getType(AbstractButton.class), Type.getType(String.class) });
/*     */ 
/*     */   
/*  31 */   private Set myClassesRequiringLoadLabelText = new HashSet();
/*  32 */   private Set myClassesRequiringLoadButtonText = new HashSet();
/*     */   private boolean myHaveSetDisplayedMnemonicIndex = false;
/*     */   
/*     */   public void generateClassStart(AsmCodeGenerator.FormClassVisitor visitor, String name, ClassLoader loader) {
/*  36 */     this.myClassesRequiringLoadLabelText.remove(name);
/*  37 */     this.myClassesRequiringLoadButtonText.remove(name);
/*     */     try {
/*  39 */       Class c = loader.loadClass(AbstractButton.class.getName());
/*  40 */       if (c.getMethod("getDisplayedMnemonicIndex", new Class[0]) != null) {
/*  41 */         this.myHaveSetDisplayedMnemonicIndex = true;
/*     */       }
/*     */     }
/*  44 */     catch (Exception e) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean generateCustomSetValue(LwComponent lwComponent, Class componentClass, LwIntrospectedProperty property, GeneratorAdapter generator, int componentLocal, String formClassName) {
/*  55 */     if ("text".equals(property.getName()) && (AbstractButton.class.isAssignableFrom(componentClass) || JLabel.class.isAssignableFrom(componentClass))) {
/*     */       
/*  57 */       StringDescriptor propertyValue = (StringDescriptor)lwComponent.getPropertyValue(property);
/*  58 */       if (propertyValue.getValue() != null) {
/*  59 */         SupportCode.TextWithMnemonic textWithMnemonic = SupportCode.parseText(propertyValue.getValue());
/*  60 */         if (textWithMnemonic.myMnemonicIndex >= 0) {
/*  61 */           String setMnemonicMethodName; generator.loadLocal(componentLocal);
/*  62 */           generator.push(textWithMnemonic.myText);
/*  63 */           generator.invokeVirtual(Type.getType(componentClass), new Method(property.getWriteMethodName(), Type.VOID_TYPE, new Type[] { Type.getType(String.class) }));
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  68 */           if (AbstractButton.class.isAssignableFrom(componentClass)) {
/*  69 */             setMnemonicMethodName = "setMnemonic";
/*     */           } else {
/*     */             
/*  72 */             setMnemonicMethodName = "setDisplayedMnemonic";
/*     */           } 
/*     */           
/*  75 */           generator.loadLocal(componentLocal);
/*  76 */           generator.push(textWithMnemonic.getMnemonicChar());
/*  77 */           generator.invokeVirtual(Type.getType(componentClass), new Method(setMnemonicMethodName, Type.VOID_TYPE, new Type[] { Type.CHAR_TYPE }));
/*     */ 
/*     */ 
/*     */           
/*  81 */           if (this.myHaveSetDisplayedMnemonicIndex) {
/*  82 */             generator.loadLocal(componentLocal);
/*  83 */             generator.push(textWithMnemonic.myMnemonicIndex);
/*  84 */             generator.invokeVirtual(Type.getType(componentClass), new Method("setDisplayedMnemonicIndex", Type.VOID_TYPE, new Type[] { Type.INT_TYPE }));
/*     */           } 
/*     */ 
/*     */           
/*  88 */           return true;
/*     */         } 
/*     */       } else {
/*     */         Method method;
/*     */         
/*  93 */         if (AbstractButton.class.isAssignableFrom(componentClass)) {
/*  94 */           this.myClassesRequiringLoadButtonText.add(formClassName);
/*  95 */           method = myLoadButtonTextMethod;
/*     */         } else {
/*     */           
/*  98 */           this.myClassesRequiringLoadLabelText.add(formClassName);
/*  99 */           method = myLoadLabelTextMethod;
/*     */         } 
/*     */         
/* 102 */         generator.loadThis();
/* 103 */         generator.loadLocal(componentLocal);
/* 104 */         generator.push(propertyValue.getBundleName());
/* 105 */         generator.invokeStatic(myResourceBundleType, this.myGetBundleMethod);
/* 106 */         generator.push(propertyValue.getKey());
/* 107 */         generator.invokeVirtual(myResourceBundleType, this.myGetStringMethod);
/* 108 */         generator.invokeVirtual(Type.getType("L" + formClassName + ";"), method);
/* 109 */         return true;
/*     */       } 
/*     */     } 
/* 112 */     return false;
/*     */   }
/*     */   
/*     */   public void generatePushValue(GeneratorAdapter generator, Object value) {
/* 116 */     StringDescriptor descriptor = (StringDescriptor)value;
/* 117 */     if (descriptor == null) {
/* 118 */       generator.push((String)null);
/*     */     }
/* 120 */     else if (descriptor.getValue() != null) {
/* 121 */       generator.push(descriptor.getValue());
/*     */     } else {
/*     */       
/* 124 */       generator.push(descriptor.getBundleName());
/* 125 */       generator.invokeStatic(myResourceBundleType, this.myGetBundleMethod);
/* 126 */       generator.push(descriptor.getKey());
/* 127 */       generator.invokeVirtual(myResourceBundleType, this.myGetStringMethod);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void generateClassEnd(AsmCodeGenerator.FormClassVisitor visitor) {
/* 132 */     if (this.myClassesRequiringLoadLabelText.contains(visitor.getClassName())) {
/* 133 */       generateLoadTextMethod(visitor, "$$$loadLabelText$$$", "javax/swing/JLabel", "setDisplayedMnemonic");
/* 134 */       this.myClassesRequiringLoadLabelText.remove(visitor.getClassName());
/*     */     } 
/* 136 */     if (this.myClassesRequiringLoadButtonText.contains(visitor.getClassName())) {
/* 137 */       generateLoadTextMethod(visitor, "$$$loadButtonText$$$", "javax/swing/AbstractButton", "setMnemonic");
/* 138 */       this.myClassesRequiringLoadButtonText.remove(visitor.getClassName());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void generateLoadTextMethod(AsmCodeGenerator.FormClassVisitor visitor, String methodName, String componentClass, String setMnemonicMethodName) {
/* 144 */     MethodVisitor mv = visitor.visitNewMethod(4098, methodName, "(L" + componentClass + ";Ljava/lang/String;)V", null, null);
/* 145 */     mv.visitCode();
/* 146 */     mv.visitTypeInsn(187, "java/lang/StringBuffer");
/* 147 */     mv.visitInsn(89);
/* 148 */     mv.visitMethodInsn(183, "java/lang/StringBuffer", "<init>", "()V");
/* 149 */     mv.visitVarInsn(58, 3);
/* 150 */     mv.visitInsn(3);
/* 151 */     mv.visitVarInsn(54, 4);
/* 152 */     mv.visitInsn(3);
/* 153 */     mv.visitVarInsn(54, 5);
/* 154 */     mv.visitInsn(2);
/* 155 */     mv.visitVarInsn(54, 6);
/* 156 */     mv.visitInsn(3);
/* 157 */     mv.visitVarInsn(54, 7);
/* 158 */     Label l0 = new Label();
/* 159 */     mv.visitLabel(l0);
/* 160 */     mv.visitVarInsn(21, 7);
/* 161 */     mv.visitVarInsn(25, 2);
/* 162 */     mv.visitMethodInsn(182, "java/lang/String", "length", "()I");
/* 163 */     Label l1 = new Label();
/* 164 */     mv.visitJumpInsn(162, l1);
/* 165 */     mv.visitVarInsn(25, 2);
/* 166 */     mv.visitVarInsn(21, 7);
/* 167 */     mv.visitMethodInsn(182, "java/lang/String", "charAt", "(I)C");
/* 168 */     mv.visitIntInsn(16, 38);
/* 169 */     Label l2 = new Label();
/* 170 */     mv.visitJumpInsn(160, l2);
/* 171 */     mv.visitIincInsn(7, 1);
/* 172 */     mv.visitVarInsn(21, 7);
/* 173 */     mv.visitVarInsn(25, 2);
/* 174 */     mv.visitMethodInsn(182, "java/lang/String", "length", "()I");
/* 175 */     Label l3 = new Label();
/* 176 */     mv.visitJumpInsn(160, l3);
/* 177 */     mv.visitJumpInsn(167, l1);
/* 178 */     mv.visitLabel(l3);
/* 179 */     mv.visitVarInsn(21, 4);
/* 180 */     mv.visitJumpInsn(154, l2);
/* 181 */     mv.visitVarInsn(25, 2);
/* 182 */     mv.visitVarInsn(21, 7);
/* 183 */     mv.visitMethodInsn(182, "java/lang/String", "charAt", "(I)C");
/* 184 */     mv.visitIntInsn(16, 38);
/* 185 */     mv.visitJumpInsn(159, l2);
/* 186 */     mv.visitInsn(4);
/* 187 */     mv.visitVarInsn(54, 4);
/* 188 */     mv.visitVarInsn(25, 2);
/* 189 */     mv.visitVarInsn(21, 7);
/* 190 */     mv.visitMethodInsn(182, "java/lang/String", "charAt", "(I)C");
/* 191 */     mv.visitVarInsn(54, 5);
/* 192 */     mv.visitVarInsn(25, 3);
/* 193 */     mv.visitMethodInsn(182, "java/lang/StringBuffer", "length", "()I");
/* 194 */     mv.visitVarInsn(54, 6);
/* 195 */     mv.visitLabel(l2);
/* 196 */     mv.visitVarInsn(25, 3);
/* 197 */     mv.visitVarInsn(25, 2);
/* 198 */     mv.visitVarInsn(21, 7);
/* 199 */     mv.visitMethodInsn(182, "java/lang/String", "charAt", "(I)C");
/* 200 */     mv.visitMethodInsn(182, "java/lang/StringBuffer", "append", "(C)Ljava/lang/StringBuffer;");
/* 201 */     mv.visitInsn(87);
/* 202 */     mv.visitIincInsn(7, 1);
/* 203 */     mv.visitJumpInsn(167, l0);
/* 204 */     mv.visitLabel(l1);
/* 205 */     mv.visitVarInsn(25, 1);
/* 206 */     mv.visitVarInsn(25, 3);
/* 207 */     mv.visitMethodInsn(182, "java/lang/StringBuffer", "toString", "()Ljava/lang/String;");
/* 208 */     mv.visitMethodInsn(182, componentClass, "setText", "(Ljava/lang/String;)V");
/* 209 */     mv.visitVarInsn(21, 4);
/* 210 */     Label l4 = new Label();
/* 211 */     mv.visitJumpInsn(153, l4);
/* 212 */     mv.visitVarInsn(25, 1);
/* 213 */     mv.visitVarInsn(21, 5);
/* 214 */     mv.visitMethodInsn(182, componentClass, setMnemonicMethodName, "(C)V");
/* 215 */     if (this.myHaveSetDisplayedMnemonicIndex) {
/* 216 */       mv.visitVarInsn(25, 1);
/* 217 */       mv.visitVarInsn(21, 6);
/* 218 */       mv.visitMethodInsn(182, componentClass, "setDisplayedMnemonicIndex", "(I)V");
/*     */     } 
/* 220 */     mv.visitLabel(l4);
/* 221 */     mv.visitInsn(177);
/* 222 */     mv.visitMaxs(3, 8);
/* 223 */     mv.visitEnd();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\compiler\StringPropertyCodeGenerator.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */