/*     */ package com.jgoodies.forms.util;
/*     */ 
/*     */ import com.jgoodies.common.base.SystemUtils;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.UIManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class FormUtils
/*     */ {
/*     */   private static LookAndFeel cachedLookAndFeel;
/*     */   private static Boolean cachedIsLafAqua;
/*     */   
/*     */   public static boolean isLafAqua() {
/*  67 */     ensureValidCache();
/*  68 */     if (cachedIsLafAqua == null) {
/*  69 */       cachedIsLafAqua = Boolean.valueOf(SystemUtils.isLafAqua());
/*     */     }
/*  71 */     return cachedIsLafAqua.booleanValue();
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
/*     */   
/*     */   public static void clearLookAndFeelBasedCaches() {
/*  91 */     cachedIsLafAqua = null;
/*  92 */     DefaultUnitConverter.getInstance().clearCache();
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
/*     */ 
/*     */   
/*     */   static void ensureValidCache() {
/* 113 */     LookAndFeel currentLookAndFeel = UIManager.getLookAndFeel();
/* 114 */     if (currentLookAndFeel != cachedLookAndFeel) {
/* 115 */       clearLookAndFeelBasedCaches();
/* 116 */       cachedLookAndFeel = currentLookAndFeel;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jgoodies\form\\util\FormUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */