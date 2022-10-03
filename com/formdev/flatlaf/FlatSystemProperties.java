/*     */ package com.formdev.flatlaf;
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
/*     */ public interface FlatSystemProperties
/*     */ {
/*     */   public static final String UI_SCALE = "flatlaf.uiScale";
/*     */   public static final String UI_SCALE_ENABLED = "flatlaf.uiScale.enabled";
/*     */   public static final String USE_UBUNTU_FONT = "flatlaf.useUbuntuFont";
/*     */   public static final String USE_WINDOW_DECORATIONS = "flatlaf.useWindowDecorations";
/*     */   public static final String USE_JETBRAINS_CUSTOM_DECORATIONS = "flatlaf.useJetBrainsCustomDecorations";
/*     */   public static final String MENUBAR_EMBEDDED = "flatlaf.menuBarEmbedded";
/*     */   public static final String ANIMATION = "flatlaf.animation";
/*     */   public static final String USE_TEXT_Y_CORRECTION = "flatlaf.useTextYCorrection";
/*     */   
/*     */   static boolean getBoolean(String key, boolean defaultValue) {
/* 117 */     String value = System.getProperty(key);
/* 118 */     return (value != null) ? Boolean.parseBoolean(value) : defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Boolean getBooleanStrict(String key, Boolean defaultValue) {
/* 127 */     String value = System.getProperty(key);
/* 128 */     if ("true".equalsIgnoreCase(value))
/* 129 */       return Boolean.TRUE; 
/* 130 */     if ("false".equalsIgnoreCase(value))
/* 131 */       return Boolean.FALSE; 
/* 132 */     return defaultValue;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\FlatSystemProperties.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */