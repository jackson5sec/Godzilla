/*     */ package com.intellij.uiDesigner.lw;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.SystemColor;
/*     */ import java.lang.reflect.Field;
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
/*     */ public class ColorDescriptor
/*     */ {
/*     */   private Color myColor;
/*     */   private String mySwingColor;
/*     */   private String mySystemColor;
/*     */   private String myAWTColor;
/*     */   
/*     */   public static ColorDescriptor fromSwingColor(String swingColor) {
/*  32 */     ColorDescriptor result = new ColorDescriptor(null);
/*  33 */     result.myColor = null;
/*  34 */     result.mySwingColor = swingColor;
/*  35 */     return result;
/*     */   }
/*     */   
/*     */   public static ColorDescriptor fromSystemColor(String systemColor) {
/*  39 */     ColorDescriptor result = new ColorDescriptor(null);
/*  40 */     result.myColor = null;
/*  41 */     result.mySystemColor = systemColor;
/*  42 */     return result;
/*     */   }
/*     */   
/*     */   public static ColorDescriptor fromAWTColor(String awtColor) {
/*  46 */     ColorDescriptor result = new ColorDescriptor(null);
/*  47 */     result.myColor = null;
/*  48 */     result.myAWTColor = awtColor;
/*  49 */     return result;
/*     */   }
/*     */   
/*     */   private static Color getColorField(Class aClass, String fieldName) {
/*     */     try {
/*  54 */       Field field = aClass.getDeclaredField(fieldName);
/*  55 */       return (Color)field.get(null);
/*     */     }
/*  57 */     catch (NoSuchFieldException e) {
/*  58 */       return Color.black;
/*     */     }
/*  60 */     catch (IllegalAccessException e) {
/*  61 */       return Color.black;
/*     */     } 
/*     */   }
/*     */   
/*     */   public ColorDescriptor(Color color) {
/*  66 */     this.myColor = color;
/*     */   }
/*     */   
/*     */   public Color getResolvedColor() {
/*  70 */     if (this.myColor != null) {
/*  71 */       return this.myColor;
/*     */     }
/*  73 */     if (this.mySwingColor != null) {
/*  74 */       return UIManager.getColor(this.mySwingColor);
/*     */     }
/*  76 */     if (this.mySystemColor != null) {
/*  77 */       return getColorField(SystemColor.class, this.mySystemColor);
/*     */     }
/*  79 */     if (this.myAWTColor != null) {
/*  80 */       return getColorField(Color.class, this.myAWTColor);
/*     */     }
/*  82 */     return null;
/*     */   }
/*     */   
/*     */   public Color getColor() {
/*  86 */     return this.myColor;
/*     */   }
/*     */   
/*     */   public String getSwingColor() {
/*  90 */     return this.mySwingColor;
/*     */   }
/*     */   
/*     */   public String getSystemColor() {
/*  94 */     return this.mySystemColor;
/*     */   }
/*     */   
/*     */   public String getAWTColor() {
/*  98 */     return this.myAWTColor;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 102 */     if (this.mySwingColor != null) {
/* 103 */       return this.mySwingColor;
/*     */     }
/* 105 */     if (this.mySystemColor != null) {
/* 106 */       return this.mySystemColor;
/*     */     }
/* 108 */     if (this.myAWTColor != null) {
/* 109 */       return this.myAWTColor;
/*     */     }
/* 111 */     if (this.myColor != null) {
/* 112 */       return "[" + this.myColor.getRed() + "," + this.myColor.getGreen() + "," + this.myColor.getBlue() + "]";
/*     */     }
/* 114 */     return "null";
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj) {
/* 118 */     if (obj == null || !(obj instanceof ColorDescriptor)) {
/* 119 */       return false;
/*     */     }
/* 121 */     ColorDescriptor rhs = (ColorDescriptor)obj;
/* 122 */     if (this.myColor != null) {
/* 123 */       return this.myColor.equals(rhs.myColor);
/*     */     }
/* 125 */     if (this.mySwingColor != null) {
/* 126 */       return this.mySwingColor.equals(rhs.mySwingColor);
/*     */     }
/* 128 */     if (this.mySystemColor != null) {
/* 129 */       return this.mySystemColor.equals(rhs.mySystemColor);
/*     */     }
/* 131 */     if (this.myAWTColor != null) {
/* 132 */       return this.myAWTColor.equals(rhs.myAWTColor);
/*     */     }
/* 134 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isColorSet() {
/* 138 */     return (this.myColor != null || this.mySwingColor != null || this.mySystemColor != null || this.myAWTColor != null);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\lw\ColorDescriptor.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */