/*     */ package com.intellij.uiDesigner.lw;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class StringDescriptor
/*     */ {
/*     */   private final String myBundleName;
/*     */   private final String myKey;
/*     */   private final String myValue;
/*     */   private String myResolvedValue;
/*     */   private boolean myNoI18n;
/*     */   
/*     */   private StringDescriptor(String value) {
/*  46 */     if (value == null) {
/*  47 */       throw new IllegalArgumentException("value cannot be null");
/*     */     }
/*  49 */     this.myBundleName = null;
/*  50 */     this.myKey = null;
/*  51 */     this.myValue = value;
/*     */   }
/*     */   
/*     */   public StringDescriptor(String bundleName, String key) {
/*  55 */     if (bundleName == null) {
/*  56 */       throw new IllegalArgumentException("bundleName cannot be null");
/*     */     }
/*  58 */     if (key == null) {
/*  59 */       throw new IllegalArgumentException("key cannot be null");
/*     */     }
/*  61 */     this.myBundleName = bundleName.replace('.', '/');
/*  62 */     this.myKey = key;
/*  63 */     this.myValue = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static StringDescriptor create(String value) {
/*  70 */     return (value != null) ? new StringDescriptor(value) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getValue() {
/*  79 */     return this.myValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getBundleName() {
/*  86 */     return this.myBundleName;
/*     */   }
/*     */   
/*     */   public String getDottedBundleName() {
/*  90 */     return (this.myBundleName == null) ? null : this.myBundleName.replace('/', '.');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getKey() {
/*  97 */     return this.myKey;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getResolvedValue() {
/* 104 */     return this.myResolvedValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setResolvedValue(String resolvedValue) {
/* 111 */     this.myResolvedValue = resolvedValue;
/*     */   }
/*     */   
/*     */   public boolean isNoI18n() {
/* 115 */     return this.myNoI18n;
/*     */   }
/*     */   
/*     */   public void setNoI18n(boolean noI18n) {
/* 119 */     this.myNoI18n = noI18n;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/* 123 */     if (this == o) return true; 
/* 124 */     if (!(o instanceof StringDescriptor)) return false;
/*     */     
/* 126 */     StringDescriptor descriptor = (StringDescriptor)o;
/*     */     
/* 128 */     if ((this.myBundleName != null) ? !this.myBundleName.equals(descriptor.myBundleName) : (descriptor.myBundleName != null)) return false; 
/* 129 */     if ((this.myKey != null) ? !this.myKey.equals(descriptor.myKey) : (descriptor.myKey != null)) return false; 
/* 130 */     if ((this.myValue != null) ? !this.myValue.equals(descriptor.myValue) : (descriptor.myValue != null)) return false; 
/* 131 */     if (this.myNoI18n != descriptor.myNoI18n) return false;
/*     */     
/* 133 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 138 */     int result = (this.myBundleName != null) ? this.myBundleName.hashCode() : 0;
/* 139 */     result = 29 * result + ((this.myKey != null) ? this.myKey.hashCode() : 0);
/* 140 */     result = 29 * result + ((this.myValue != null) ? this.myValue.hashCode() : 0);
/* 141 */     return result;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 145 */     if (this.myValue != null) {
/* 146 */       return "[StringDescriptor:" + this.myValue + "]";
/*     */     }
/* 148 */     return "[StringDescriptor" + this.myBundleName + ":" + this.myKey + "]";
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\lw\StringDescriptor.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */