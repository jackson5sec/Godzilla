/*    */ package com.jgoodies.common.internal;
/*    */ 
/*    */ import com.jgoodies.common.base.Preconditions;
/*    */ import com.jgoodies.common.base.Strings;
/*    */ import java.util.MissingResourceException;
/*    */ import java.util.ResourceBundle;
/*    */ import javax.swing.Icon;
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
/*    */ public final class ResourceBundleAccessor
/*    */   implements StringAndIconResourceAccessor
/*    */ {
/*    */   private final ResourceBundle bundle;
/*    */   
/*    */   public ResourceBundleAccessor(ResourceBundle bundle) {
/* 63 */     this.bundle = (ResourceBundle)Preconditions.checkNotNull(bundle, "The %1$s must not be null.", new Object[] { "resource bundle" });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Icon getIcon(String key) {
/* 71 */     return (Icon)this.bundle.getObject(key);
/*    */   }
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
/*    */   public String getString(String key, Object... args) {
/*    */     try {
/* 90 */       return Strings.get(this.bundle.getString(key), args);
/* 91 */     } catch (MissingResourceException mre) {
/* 92 */       return key;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jgoodies\common\internal\ResourceBundleAccessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */