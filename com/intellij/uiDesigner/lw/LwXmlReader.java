/*     */ package com.intellij.uiDesigner.lw;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Insets;
/*     */ import java.lang.reflect.Method;
/*     */ import org.jdom.Attribute;
/*     */ import org.jdom.Element;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class LwXmlReader
/*     */ {
/*     */   public static Element getChild(Element element, String childName) {
/*  33 */     return element.getChild(childName, element.getNamespace());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Element getRequiredChild(Element element, String childName) {
/*  40 */     Element child = getChild(element, childName);
/*  41 */     if (child == null) {
/*  42 */       throw new IllegalArgumentException("subtag '" + childName + "' is required: " + element);
/*     */     }
/*  44 */     return child;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getString(Element element, String attributeName) {
/*  51 */     String value = element.getAttributeValue(attributeName);
/*  52 */     return (value != null) ? value.trim() : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getRequiredString(Element element, String attributeName) {
/*  59 */     String value = getString(element, attributeName);
/*  60 */     if (value != null) {
/*  61 */       return value;
/*     */     }
/*     */     
/*  64 */     throw new IllegalArgumentException("attribute '" + attributeName + "' is required: " + element);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getOptionalString(Element element, String attributeName, String defaultValue) {
/*  69 */     String value = element.getAttributeValue(attributeName);
/*  70 */     return (value != null) ? value.trim() : defaultValue;
/*     */   }
/*     */   
/*     */   public static int getRequiredInt(Element element, String attributeName) {
/*  74 */     String str = getRequiredString(element, attributeName);
/*     */     try {
/*  76 */       return Integer.parseInt(str);
/*     */     }
/*  78 */     catch (NumberFormatException e) {
/*  79 */       throw new IllegalArgumentException("attribute '" + attributeName + "' is not a proper integer: " + str);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static int getOptionalInt(Element element, String attributeName, int defaultValue) {
/*  84 */     String str = element.getAttributeValue(attributeName);
/*  85 */     if (str == null) {
/*  86 */       return defaultValue;
/*     */     }
/*     */     try {
/*  89 */       return Integer.parseInt(str);
/*     */     }
/*  91 */     catch (NumberFormatException e) {
/*  92 */       throw new IllegalArgumentException("attribute '" + attributeName + "' is not a proper integer: " + str);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean getOptionalBoolean(Element element, String attributeName, boolean defaultValue) {
/*  97 */     String str = element.getAttributeValue(attributeName);
/*  98 */     if (str == null) {
/*  99 */       return defaultValue;
/*     */     }
/* 101 */     return Boolean.valueOf(str).booleanValue();
/*     */   }
/*     */   
/*     */   public static double getRequiredDouble(Element element, String attributeName) {
/* 105 */     String str = getRequiredString(element, attributeName);
/*     */     try {
/* 107 */       return Double.parseDouble(str);
/*     */     }
/* 109 */     catch (NumberFormatException e) {
/* 110 */       throw new IllegalArgumentException("attribute '" + attributeName + "' is not a proper double: " + str);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static double getOptionalDouble(Element element, String attributeName, double defaultValue) {
/* 115 */     String str = element.getAttributeValue(attributeName);
/* 116 */     if (str == null) {
/* 117 */       return defaultValue;
/*     */     }
/*     */     try {
/* 120 */       return Double.parseDouble(str);
/*     */     }
/* 122 */     catch (NumberFormatException e) {
/* 123 */       throw new IllegalArgumentException("attribute '" + attributeName + "' is not a proper double: " + str);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static float getRequiredFloat(Element element, String attributeName) {
/* 128 */     String str = getRequiredString(element, attributeName);
/*     */     try {
/* 130 */       return Float.parseFloat(str);
/*     */     }
/* 132 */     catch (NumberFormatException e) {
/* 133 */       throw new IllegalArgumentException("attribute '" + attributeName + "' is not a proper float: " + str);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Object getRequiredPrimitiveTypeValue(Element element, String attributeName, Class valueClass) {
/* 138 */     String str = getRequiredString(element, attributeName);
/*     */     try {
/* 140 */       Method method = valueClass.getMethod("valueOf", new Class[] { String.class });
/*     */       
/* 142 */       return method.invoke(null, new Object[] { str });
/*     */     }
/* 144 */     catch (NumberFormatException e) {
/* 145 */       throw new IllegalArgumentException("attribute '" + attributeName + "' is not a proper float: " + str);
/*     */     }
/* 147 */     catch (Exception e) {
/* 148 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static StringDescriptor getStringDescriptor(Element element, String valueAttr, String bundleAttr, String keyAttr) {
/* 154 */     String title = element.getAttributeValue(valueAttr);
/* 155 */     if (title != null) {
/* 156 */       StringDescriptor descriptor = StringDescriptor.create(title);
/* 157 */       descriptor.setNoI18n(getOptionalBoolean(element, "noi18n", false));
/* 158 */       return descriptor;
/*     */     } 
/*     */     
/* 161 */     String bundle = element.getAttributeValue(bundleAttr);
/* 162 */     if (bundle != null) {
/* 163 */       String key = getRequiredString(element, keyAttr);
/* 164 */       return new StringDescriptor(bundle, key);
/*     */     } 
/*     */ 
/*     */     
/* 168 */     return null;
/*     */   }
/*     */   
/*     */   public static FontDescriptor getFontDescriptor(Element element) {
/* 172 */     String swingFont = element.getAttributeValue("swing-font");
/* 173 */     if (swingFont != null) {
/* 174 */       return FontDescriptor.fromSwingFont(swingFont);
/*     */     }
/*     */     
/* 177 */     String fontName = element.getAttributeValue("name");
/* 178 */     int fontStyle = getOptionalInt(element, "style", -1);
/* 179 */     int fontSize = getOptionalInt(element, "size", -1);
/* 180 */     return new FontDescriptor(fontName, fontStyle, fontSize);
/*     */   }
/*     */   
/*     */   public static ColorDescriptor getColorDescriptor(Element element) throws Exception {
/* 184 */     Attribute attr = element.getAttribute("color");
/* 185 */     if (attr != null) {
/* 186 */       return new ColorDescriptor(new Color(attr.getIntValue()));
/*     */     }
/* 188 */     String swingColor = element.getAttributeValue("swing-color");
/* 189 */     if (swingColor != null) {
/* 190 */       return ColorDescriptor.fromSwingColor(swingColor);
/*     */     }
/* 192 */     String systemColor = element.getAttributeValue("system-color");
/* 193 */     if (systemColor != null) {
/* 194 */       return ColorDescriptor.fromSystemColor(systemColor);
/*     */     }
/* 196 */     String awtColor = element.getAttributeValue("awt-color");
/* 197 */     if (awtColor != null) {
/* 198 */       return ColorDescriptor.fromAWTColor(awtColor);
/*     */     }
/* 200 */     return new ColorDescriptor(null);
/*     */   }
/*     */   
/*     */   public static ColorDescriptor getOptionalColorDescriptor(Element element) {
/* 204 */     if (element == null) return null; 
/*     */     try {
/* 206 */       return getColorDescriptor(element);
/*     */     }
/* 208 */     catch (Exception ex) {
/* 209 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Insets readInsets(Element element) {
/* 214 */     int top = getRequiredInt(element, "top");
/* 215 */     int left = getRequiredInt(element, "left");
/* 216 */     int bottom = getRequiredInt(element, "bottom");
/* 217 */     int right = getRequiredInt(element, "right");
/* 218 */     return new Insets(top, left, bottom, right);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\lw\LwXmlReader.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */