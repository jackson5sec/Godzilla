/*    */ package com.jgoodies.common.internal;
/*    */ 
/*    */ import com.jgoodies.common.base.Preconditions;
/*    */ import java.util.MissingResourceException;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class StringResourceAccess
/*    */ {
/*    */   public static String getResourceString(StringResourceAccessor accessor, String key, Object... args) {
/* 74 */     Preconditions.checkNotNull(accessor, "To use the internationalization support you must provide a ResourceBundle, ResourceMap, or a StringResourceAccessor. See #resources.");
/*    */ 
/*    */ 
/*    */     
/*    */     try {
/* 79 */       return accessor.getString(key, args);
/* 80 */     } catch (MissingResourceException ex) {
/* 81 */       Logger.getLogger(StringResourceAccess.class.getName()).log(Level.WARNING, "Missing internationalized text", ex);
/*    */       
/* 83 */       return key;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jgoodies\common\internal\StringResourceAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */