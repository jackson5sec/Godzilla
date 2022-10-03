/*    */ package com.formdev.flatlaf.extras.components;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Insets;
/*    */ import javax.swing.UIManager;
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
/*    */ public interface FlatComponentExtension
/*    */ {
/*    */   Object getClientProperty(Object paramObject);
/*    */   
/*    */   void putClientProperty(Object paramObject1, Object paramObject2);
/*    */   
/*    */   default boolean getClientPropertyBoolean(Object key, String defaultValueKey) {
/* 44 */     Object value = getClientProperty(key);
/* 45 */     return (value instanceof Boolean) ? ((Boolean)value).booleanValue() : UIManager.getBoolean(defaultValueKey);
/*    */   }
/*    */   
/*    */   default boolean getClientPropertyBoolean(Object key, boolean defaultValue) {
/* 49 */     Object value = getClientProperty(key);
/* 50 */     return (value instanceof Boolean) ? ((Boolean)value).booleanValue() : defaultValue;
/*    */   }
/*    */   
/*    */   default void putClientPropertyBoolean(Object key, boolean value, boolean defaultValue) {
/* 54 */     putClientProperty(key, (value != defaultValue) ? Boolean.valueOf(value) : null);
/*    */   }
/*    */ 
/*    */   
/*    */   default int getClientPropertyInt(Object key, String defaultValueKey) {
/* 59 */     Object value = getClientProperty(key);
/* 60 */     return (value instanceof Integer) ? ((Integer)value).intValue() : UIManager.getInt(defaultValueKey);
/*    */   }
/*    */   
/*    */   default int getClientPropertyInt(Object key, int defaultValue) {
/* 64 */     Object value = getClientProperty(key);
/* 65 */     return (value instanceof Integer) ? ((Integer)value).intValue() : defaultValue;
/*    */   }
/*    */ 
/*    */   
/*    */   default Color getClientPropertyColor(Object key, String defaultValueKey) {
/* 70 */     Object value = getClientProperty(key);
/* 71 */     return (value instanceof Color) ? (Color)value : UIManager.getColor(defaultValueKey);
/*    */   }
/*    */   
/*    */   default Insets getClientPropertyInsets(Object key, String defaultValueKey) {
/* 75 */     Object value = getClientProperty(key);
/* 76 */     return (value instanceof Insets) ? (Insets)value : UIManager.getInsets(defaultValueKey);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default <T extends Enum<T>> T getClientPropertyEnumString(Object key, Class<T> enumType, String defaultValueKey, T defaultValue) {
/* 83 */     Object value = getClientProperty(key);
/* 84 */     if (!(value instanceof String) && defaultValueKey != null)
/* 85 */       value = UIManager.getString(defaultValueKey); 
/* 86 */     if (value instanceof String) {
/*    */       try {
/* 88 */         return Enum.valueOf(enumType, (String)value);
/* 89 */       } catch (IllegalArgumentException ex) {
/* 90 */         ex.printStackTrace();
/*    */       } 
/*    */     }
/* 93 */     return defaultValue;
/*    */   }
/*    */   
/*    */   default <T extends Enum<T>> void putClientPropertyEnumString(Object key, Enum<T> value) {
/* 97 */     putClientProperty(key, (value != null) ? value.toString() : null);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\extras\components\FlatComponentExtension.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */