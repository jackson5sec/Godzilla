/*     */ package com.jgoodies.forms;
/*     */ 
/*     */ import com.jgoodies.forms.factories.ComponentFactory;
/*     */ import com.jgoodies.forms.factories.DefaultComponentFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FormsSetup
/*     */ {
/*     */   private static final String DEBUG_TOOL_TIPS_ENABLED_KEY = "FormsSetup.debugToolTipsEnabled";
/*     */   private static ComponentFactory componentFactoryDefault;
/*     */   private static boolean labelForFeatureEnabledDefault = true;
/*     */   private static boolean opaqueDefault = false;
/*  81 */   private static boolean debugToolTipsEnabled = getDebugToolTipSystemProperty();
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
/*     */   public static ComponentFactory getComponentFactoryDefault() {
/* 102 */     if (componentFactoryDefault == null) {
/* 103 */       componentFactoryDefault = (ComponentFactory)new DefaultComponentFactory();
/*     */     }
/* 105 */     return componentFactoryDefault;
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
/*     */   public static void setComponentFactoryDefault(ComponentFactory factory) {
/* 117 */     componentFactoryDefault = factory;
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
/*     */   public static boolean getLabelForFeatureEnabledDefault() {
/* 130 */     return labelForFeatureEnabledDefault;
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
/*     */   public static void setLabelForFeatureEnabledDefault(boolean b) {
/* 145 */     labelForFeatureEnabledDefault = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean getOpaqueDefault() {
/* 154 */     return opaqueDefault;
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
/*     */   public static void setOpaqueDefault(boolean b) {
/* 167 */     opaqueDefault = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean getDebugToolTipsEnabledDefault() {
/* 177 */     return debugToolTipsEnabled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setDebugToolTipsEnabled(boolean b) {
/* 187 */     debugToolTipsEnabled = b;
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean getDebugToolTipSystemProperty() {
/*     */     try {
/* 193 */       String value = System.getProperty("FormsSetup.debugToolTipsEnabled");
/* 194 */       return "true".equalsIgnoreCase(value);
/* 195 */     } catch (SecurityException e) {
/* 196 */       return false;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jgoodies\forms\FormsSetup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */