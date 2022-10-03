/*     */ package com.jgoodies.forms.internal;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import javax.swing.AbstractButton;
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
/*     */ public final class FocusTraversalUtilsAccessor
/*     */ {
/*     */   private static final String FOCUS_TRAVERSAL_UTILS_NAME = "com.jgoodies.jsdl.common.focus.FocusTraversalUtils";
/*  64 */   private static Method groupMethod = null;
/*     */ 
/*     */   
/*     */   static {
/*  68 */     groupMethod = getGroupMethod();
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
/*     */   public static void tryToBuildAFocusGroup(AbstractButton... buttons) {
/*  84 */     if (groupMethod == null) {
/*     */       return;
/*     */     }
/*     */     try {
/*  88 */       groupMethod.invoke(null, new Object[] { buttons });
/*  89 */     } catch (IllegalAccessException e) {
/*     */     
/*  91 */     } catch (InvocationTargetException e) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Method getGroupMethod() {
/*     */     try {
/* 101 */       Class<?> clazz = Class.forName("com.jgoodies.jsdl.common.focus.FocusTraversalUtils");
/* 102 */       return clazz.getMethod("group", new Class[] { AbstractButton[].class });
/* 103 */     } catch (ClassNotFoundException e) {
/*     */     
/* 105 */     } catch (SecurityException e) {
/*     */     
/* 107 */     } catch (NoSuchMethodException e) {}
/*     */ 
/*     */     
/* 110 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jgoodies\forms\internal\FocusTraversalUtilsAccessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */