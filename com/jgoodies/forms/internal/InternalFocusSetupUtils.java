/*     */ package com.jgoodies.forms.internal;
/*     */ 
/*     */ import com.jgoodies.common.base.Preconditions;
/*     */ import com.jgoodies.forms.util.FocusTraversalType;
/*     */ import java.awt.Component;
/*     */ import java.awt.ContainerOrderFocusTraversalPolicy;
/*     */ import java.awt.FocusTraversalPolicy;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.LayoutFocusTraversalPolicy;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class InternalFocusSetupUtils
/*     */ {
/*     */   private static final String JGContainerOrderFocusTraversalPolicy_NAME = "com.jgoodies.jsdl.common.focus.JGContainerOrderFocusTraversalPolicy";
/*     */   private static final String JGLayoutFocusTraversalPolicy_NAME = "com.jgoodies.jsdl.common.focus.JGLayoutFocusTraversalPolicy";
/*  82 */   private static Constructor<FocusTraversalPolicy> containerOrderFTPConstructor = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  88 */   private static Constructor<FocusTraversalPolicy> layoutFTPConstructor = null;
/*     */   
/*     */   static {
/*  91 */     containerOrderFTPConstructor = getContainerOrderFTPConstructor();
/*  92 */     layoutFTPConstructor = getLayoutFTPConstructor();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void checkValidFocusTraversalSetup(FocusTraversalPolicy policy, FocusTraversalType type, Component initialComponent) {
/* 111 */     Preconditions.checkState(((policy != null && type == null && initialComponent == null) || policy == null), "Either use #focusTraversalPolicy or #focusTraversalType plus optional #initialComponent); don't mix them.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setupFocusTraversalPolicyAndProvider(JComponent container, FocusTraversalPolicy policy, FocusTraversalType type, Component initialComponent) {
/* 122 */     container.setFocusTraversalPolicy(getOrCreateFocusTraversalPolicy(policy, type, initialComponent));
/*     */     
/* 124 */     container.setFocusTraversalPolicyProvider(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static FocusTraversalPolicy getOrCreateFocusTraversalPolicy(FocusTraversalPolicy policy, FocusTraversalType type, Component initialComponent) {
/* 132 */     if (policy != null) {
/* 133 */       return policy;
/*     */     }
/* 135 */     if (type == FocusTraversalType.CONTAINER_ORDER) {
/* 136 */       return createContainerOrderFocusTraversalPolicy(initialComponent);
/*     */     }
/* 138 */     return createLayoutFocusTraversalPolicy(initialComponent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static FocusTraversalPolicy createContainerOrderFocusTraversalPolicy(Component initialComponent) {
/* 145 */     if (containerOrderFTPConstructor != null) {
/*     */       try {
/* 147 */         return containerOrderFTPConstructor.newInstance(new Object[] { initialComponent });
/* 148 */       } catch (IllegalArgumentException ex) {
/*     */       
/* 150 */       } catch (InstantiationException ex) {
/*     */       
/* 152 */       } catch (IllegalAccessException ex) {
/*     */       
/* 154 */       } catch (InvocationTargetException ex) {}
/*     */     }
/*     */ 
/*     */     
/* 158 */     return new ContainerOrderFocusTraversalPolicy();
/*     */   }
/*     */ 
/*     */   
/*     */   private static FocusTraversalPolicy createLayoutFocusTraversalPolicy(Component initialComponent) {
/* 163 */     if (layoutFTPConstructor != null) {
/*     */       try {
/* 165 */         return layoutFTPConstructor.newInstance(new Object[] { initialComponent });
/* 166 */       } catch (IllegalArgumentException ex) {
/*     */       
/* 168 */       } catch (InstantiationException ex) {
/*     */       
/* 170 */       } catch (IllegalAccessException ex) {
/*     */       
/* 172 */       } catch (InvocationTargetException ex) {}
/*     */     }
/*     */ 
/*     */     
/* 176 */     return new LayoutFocusTraversalPolicy();
/*     */   }
/*     */ 
/*     */   
/*     */   private static Constructor<FocusTraversalPolicy> getContainerOrderFTPConstructor() {
/*     */     try {
/* 182 */       return (Constructor)Class.forName("com.jgoodies.jsdl.common.focus.JGContainerOrderFocusTraversalPolicy").getConstructor(new Class[] { Component.class });
/* 183 */     } catch (SecurityException ex) {
/*     */     
/* 185 */     } catch (NoSuchMethodException ex) {
/*     */     
/* 187 */     } catch (ClassNotFoundException ex) {}
/*     */ 
/*     */     
/* 190 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private static Constructor<FocusTraversalPolicy> getLayoutFTPConstructor() {
/*     */     try {
/* 196 */       return (Constructor)Class.forName("com.jgoodies.jsdl.common.focus.JGLayoutFocusTraversalPolicy").getConstructor(new Class[] { Component.class });
/* 197 */     } catch (SecurityException ex) {
/*     */     
/* 199 */     } catch (NoSuchMethodException ex) {
/*     */     
/* 201 */     } catch (ClassNotFoundException ex) {}
/*     */ 
/*     */     
/* 204 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jgoodies\forms\internal\InternalFocusSetupUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */