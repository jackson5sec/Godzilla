/*     */ package com.intellij.uiDesigner.lw;
/*     */ 
/*     */ import com.intellij.uiDesigner.compiler.Utils;
/*     */ import java.awt.Component;
/*     */ import java.beans.BeanInfo;
/*     */ import java.beans.Introspector;
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.HashMap;
/*     */ import javax.swing.ListModel;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CompiledClassPropertiesProvider
/*     */   implements PropertiesProvider
/*     */ {
/*     */   private final ClassLoader myLoader;
/*     */   private final HashMap myCache;
/*     */   
/*     */   public CompiledClassPropertiesProvider(ClassLoader loader) {
/*  37 */     if (loader == null) {
/*  38 */       throw new IllegalArgumentException("loader cannot be null");
/*     */     }
/*  40 */     this.myLoader = loader;
/*  41 */     this.myCache = new HashMap();
/*     */   } public HashMap getLwProperties(String className) {
/*     */     Class aClass;
/*     */     BeanInfo beanInfo;
/*  45 */     if (this.myCache.containsKey(className)) {
/*  46 */       return (HashMap)this.myCache.get(className);
/*     */     }
/*     */     
/*  49 */     if (Utils.validateJComponentClass(this.myLoader, className, false) != null) {
/*  50 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/*  55 */       aClass = Class.forName(className, false, this.myLoader);
/*     */     }
/*  57 */     catch (ClassNotFoundException exc) {
/*  58 */       throw new RuntimeException(exc.toString());
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/*  63 */       beanInfo = Introspector.getBeanInfo(aClass);
/*     */     }
/*  65 */     catch (Throwable e) {
/*  66 */       return null;
/*     */     } 
/*     */     
/*  69 */     HashMap result = new HashMap();
/*  70 */     PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
/*  71 */     for (int i = 0; i < descriptors.length; i++) {
/*  72 */       PropertyDescriptor descriptor = descriptors[i];
/*     */       
/*  74 */       Method readMethod = descriptor.getReadMethod();
/*  75 */       Method writeMethod = descriptor.getWriteMethod();
/*  76 */       if (writeMethod != null && readMethod != null) {
/*     */ 
/*     */ 
/*     */         
/*  80 */         String name = descriptor.getName();
/*     */         
/*  82 */         LwIntrospectedProperty property = propertyFromClass(descriptor.getPropertyType(), name);
/*     */         
/*  84 */         if (property != null) {
/*  85 */           property.setDeclaringClassName(descriptor.getReadMethod().getDeclaringClass().getName());
/*  86 */           result.put(name, property);
/*     */         } 
/*     */       } 
/*     */     } 
/*  90 */     this.myCache.put(className, result);
/*     */     
/*  92 */     return result;
/*     */   }
/*     */   
/*     */   public static LwIntrospectedProperty propertyFromClass(Class propertyType, String name) {
/*  96 */     LwIntrospectedProperty property = propertyFromClassName(propertyType.getName(), name);
/*  97 */     if (property == null) {
/*  98 */       if (Component.class.isAssignableFrom(propertyType)) {
/*  99 */         property = new LwIntroComponentProperty(name, propertyType.getName());
/*     */       }
/* 101 */       else if (ListModel.class.isAssignableFrom(propertyType)) {
/* 102 */         property = new LwIntroListModelProperty(name, propertyType.getName());
/*     */       }
/* 104 */       else if (propertyType.getSuperclass() != null && "java.lang.Enum".equals(propertyType.getSuperclass().getName())) {
/* 105 */         property = new LwIntroEnumProperty(name, propertyType);
/*     */       } 
/*     */     }
/* 108 */     return property;
/*     */   }
/*     */   
/*     */   public static LwIntrospectedProperty propertyFromClassName(String propertyClassName, String name) {
/*     */     LwIntrospectedProperty property;
/* 113 */     if (int.class.getName().equals(propertyClassName)) {
/* 114 */       property = new LwIntroIntProperty(name);
/*     */     }
/* 116 */     else if (boolean.class.getName().equals(propertyClassName)) {
/* 117 */       property = new LwIntroBooleanProperty(name);
/*     */     }
/* 119 */     else if (double.class.getName().equals(propertyClassName)) {
/* 120 */       property = new LwIntroPrimitiveTypeProperty(name, Double.class);
/*     */     }
/* 122 */     else if (float.class.getName().equals(propertyClassName)) {
/* 123 */       property = new LwIntroPrimitiveTypeProperty(name, Float.class);
/*     */     }
/* 125 */     else if (long.class.getName().equals(propertyClassName)) {
/* 126 */       property = new LwIntroPrimitiveTypeProperty(name, Long.class);
/*     */     }
/* 128 */     else if (byte.class.getName().equals(propertyClassName)) {
/* 129 */       property = new LwIntroPrimitiveTypeProperty(name, Byte.class);
/*     */     }
/* 131 */     else if (short.class.getName().equals(propertyClassName)) {
/* 132 */       property = new LwIntroPrimitiveTypeProperty(name, Short.class);
/*     */     }
/* 134 */     else if (char.class.getName().equals(propertyClassName)) {
/* 135 */       property = new LwIntroCharProperty(name);
/*     */     }
/* 137 */     else if (String.class.getName().equals(propertyClassName)) {
/* 138 */       property = new LwRbIntroStringProperty(name);
/*     */     }
/* 140 */     else if ("java.awt.Insets".equals(propertyClassName)) {
/* 141 */       property = new LwIntroInsetsProperty(name);
/*     */     }
/* 143 */     else if ("java.awt.Dimension".equals(propertyClassName)) {
/* 144 */       property = new LwIntroDimensionProperty(name);
/*     */     }
/* 146 */     else if ("java.awt.Rectangle".equals(propertyClassName)) {
/* 147 */       property = new LwIntroRectangleProperty(name);
/*     */     }
/* 149 */     else if ("java.awt.Color".equals(propertyClassName)) {
/* 150 */       property = new LwIntroColorProperty(name);
/*     */     }
/* 152 */     else if ("java.awt.Font".equals(propertyClassName)) {
/* 153 */       property = new LwIntroFontProperty(name);
/*     */     }
/* 155 */     else if ("javax.swing.Icon".equals(propertyClassName)) {
/* 156 */       property = new LwIntroIconProperty(name);
/*     */     } else {
/*     */       
/* 159 */       property = null;
/*     */     } 
/* 161 */     return property;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\lw\CompiledClassPropertiesProvider.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */