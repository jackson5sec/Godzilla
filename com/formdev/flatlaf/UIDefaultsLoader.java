/*     */ package com.formdev.flatlaf;
/*     */ 
/*     */ import com.formdev.flatlaf.ui.FlatEmptyBorder;
/*     */ import com.formdev.flatlaf.ui.FlatLineBorder;
/*     */ import com.formdev.flatlaf.util.ColorFunctions;
/*     */ import com.formdev.flatlaf.util.DerivedColor;
/*     */ import com.formdev.flatlaf.util.GrayFilter;
/*     */ import com.formdev.flatlaf.util.HSLColor;
/*     */ import com.formdev.flatlaf.util.StringUtils;
/*     */ import com.formdev.flatlaf.util.SystemInfo;
/*     */ import com.formdev.flatlaf.util.UIScale;
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Insets;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.function.Function;
/*     */ import java.util.logging.Level;
/*     */ import javax.swing.UIDefaults;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ColorUIResource;
/*     */ import javax.swing.plaf.DimensionUIResource;
/*     */ import javax.swing.plaf.InsetsUIResource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class UIDefaultsLoader
/*     */ {
/*     */   private static final String TYPE_PREFIX = "{";
/*     */   private static final String TYPE_PREFIX_END = "}";
/*     */   private static final String VARIABLE_PREFIX = "@";
/*     */   private static final String PROPERTY_PREFIX = "$";
/*     */   private static final String OPTIONAL_PREFIX = "?";
/*     */   private static final String WILDCARD_PREFIX = "*.";
/*     */   
/*     */   static void loadDefaultsFromProperties(Class<?> lookAndFeelClass, List<FlatDefaultsAddon> addons, Properties additionalDefaults, boolean dark, UIDefaults defaults) {
/*  79 */     ArrayList<Class<?>> lafClasses = new ArrayList<>();
/*  80 */     Class<?> lafClass = lookAndFeelClass;
/*  81 */     for (; FlatLaf.class.isAssignableFrom(lafClass); 
/*  82 */       lafClass = lafClass.getSuperclass())
/*     */     {
/*  84 */       lafClasses.add(0, lafClass);
/*     */     }
/*     */     
/*  87 */     loadDefaultsFromProperties(lafClasses, addons, additionalDefaults, dark, defaults);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void loadDefaultsFromProperties(List<Class<?>> lafClasses, List<FlatDefaultsAddon> addons, Properties additionalDefaults, boolean dark, UIDefaults defaults) {
/*     */     try {
/*  95 */       Properties properties = new Properties();
/*  96 */       for (Class<?> lafClass : lafClasses) {
/*  97 */         String propertiesName = '/' + lafClass.getName().replace('.', '/') + ".properties";
/*  98 */         try (InputStream in = lafClass.getResourceAsStream(propertiesName)) {
/*  99 */           if (in != null) {
/* 100 */             properties.load(in);
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 105 */       for (FlatDefaultsAddon addon : addons) {
/* 106 */         for (Class<?> lafClass : lafClasses) {
/* 107 */           try (InputStream in = addon.getDefaults(lafClass)) {
/* 108 */             if (in != null) {
/* 109 */               properties.load(in);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 115 */       List<ClassLoader> addonClassLoaders = new ArrayList<>();
/* 116 */       for (FlatDefaultsAddon addon : addons) {
/* 117 */         ClassLoader addonClassLoader = addon.getClass().getClassLoader();
/* 118 */         if (!addonClassLoaders.contains(addonClassLoader)) {
/* 119 */           addonClassLoaders.add(addonClassLoader);
/*     */         }
/*     */       } 
/*     */       
/* 123 */       List<Object> customDefaultsSources = FlatLaf.getCustomDefaultsSources();
/* 124 */       int size = (customDefaultsSources != null) ? customDefaultsSources.size() : 0;
/* 125 */       for (int i = 0; i < size; i++) {
/* 126 */         Object source = customDefaultsSources.get(i);
/* 127 */         if (source instanceof String && i + 1 < size) {
/*     */           
/* 129 */           String packageName = (String)source;
/* 130 */           ClassLoader classLoader = (ClassLoader)customDefaultsSources.get(++i);
/*     */ 
/*     */           
/* 133 */           if (classLoader != null && !addonClassLoaders.contains(classLoader)) {
/* 134 */             addonClassLoaders.add(classLoader);
/*     */           }
/* 136 */           packageName = packageName.replace('.', '/');
/* 137 */           if (classLoader == null) {
/* 138 */             classLoader = FlatLaf.class.getClassLoader();
/*     */           }
/* 140 */           for (Class<?> lafClass : lafClasses) {
/* 141 */             String propertiesName = packageName + '/' + lafClass.getSimpleName() + ".properties";
/* 142 */             try (InputStream in = classLoader.getResourceAsStream(propertiesName)) {
/* 143 */               if (in != null)
/* 144 */                 properties.load(in); 
/*     */             } 
/*     */           } 
/* 147 */         } else if (source instanceof File) {
/*     */           
/* 149 */           File folder = (File)source;
/* 150 */           for (Class<?> lafClass : lafClasses) {
/* 151 */             File propertiesFile = new File(folder, lafClass.getSimpleName() + ".properties");
/* 152 */             if (!propertiesFile.isFile()) {
/*     */               continue;
/*     */             }
/* 155 */             try (InputStream in = new FileInputStream(propertiesFile)) {
/* 156 */               properties.load(in);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 163 */       if (additionalDefaults != null) {
/* 164 */         properties.putAll(additionalDefaults);
/*     */       }
/*     */       
/* 167 */       ArrayList<String> platformSpecificKeys = new ArrayList<>();
/* 168 */       for (Object okey : properties.keySet()) {
/* 169 */         String key = (String)okey;
/* 170 */         if (key.startsWith("[") && (key
/* 171 */           .startsWith("[win]") || key
/* 172 */           .startsWith("[mac]") || key
/* 173 */           .startsWith("[linux]") || key
/* 174 */           .startsWith("[light]") || key
/* 175 */           .startsWith("[dark]"))) {
/* 176 */           platformSpecificKeys.add(key);
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 181 */       if (!platformSpecificKeys.isEmpty()) {
/*     */         
/* 183 */         String lightOrDarkPrefix = dark ? "[dark]" : "[light]";
/* 184 */         for (String key : platformSpecificKeys) {
/* 185 */           if (key.startsWith(lightOrDarkPrefix)) {
/* 186 */             properties.put(key.substring(lightOrDarkPrefix.length()), properties.remove(key));
/*     */           }
/*     */         } 
/*     */         
/* 190 */         String platformPrefix = SystemInfo.isWindows ? "[win]" : (SystemInfo.isMacOS ? "[mac]" : (SystemInfo.isLinux ? "[linux]" : "[unknown]"));
/*     */ 
/*     */ 
/*     */         
/* 194 */         for (String key : platformSpecificKeys) {
/* 195 */           Object value = properties.remove(key);
/* 196 */           if (key.startsWith(platformPrefix)) {
/* 197 */             properties.put(key.substring(platformPrefix.length()), value);
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 202 */       HashMap<String, String> wildcards = new HashMap<>();
/* 203 */       Iterator<Map.Entry<Object, Object>> it = properties.entrySet().iterator();
/* 204 */       while (it.hasNext()) {
/* 205 */         Map.Entry<Object, Object> e = it.next();
/* 206 */         String key = (String)e.getKey();
/* 207 */         if (key.startsWith("*.")) {
/* 208 */           wildcards.put(key.substring("*.".length()), (String)e.getValue());
/* 209 */           it.remove();
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 214 */       for (Object key : defaults.keySet()) {
/*     */         int dot;
/* 216 */         if (!(key instanceof String) || properties
/* 217 */           .containsKey(key) || (
/* 218 */           dot = ((String)key).lastIndexOf('.')) < 0) {
/*     */           continue;
/*     */         }
/* 221 */         String wildcardKey = ((String)key).substring(dot + 1);
/* 222 */         String wildcardValue = wildcards.get(wildcardKey);
/* 223 */         if (wildcardValue != null) {
/* 224 */           properties.put(key, wildcardValue);
/*     */         }
/*     */       } 
/* 227 */       Function<String, String> propertiesGetter = key -> properties.getProperty(key);
/*     */ 
/*     */       
/* 230 */       Function<String, String> resolver = value -> resolveValue(value, propertiesGetter);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 235 */       for (Map.Entry<Object, Object> e : properties.entrySet()) {
/* 236 */         String key = (String)e.getKey();
/* 237 */         if (key.startsWith("@")) {
/*     */           continue;
/*     */         }
/* 240 */         String value = resolveValue((String)e.getValue(), propertiesGetter);
/*     */         try {
/* 242 */           defaults.put(key, parseValue(key, value, null, resolver, addonClassLoaders));
/* 243 */         } catch (RuntimeException ex) {
/* 244 */           logParseError(Level.SEVERE, key, value, ex);
/*     */         } 
/*     */       } 
/* 247 */     } catch (IOException ex) {
/* 248 */       FlatLaf.LOG.log(Level.SEVERE, "FlatLaf: Failed to load properties files.", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   static void logParseError(Level level, String key, String value, RuntimeException ex) {
/* 253 */     FlatLaf.LOG.log(level, "FlatLaf: Failed to parse: '" + key + '=' + value + '\'', ex);
/*     */   }
/*     */   
/*     */   static String resolveValue(String value, Function<String, String> propertiesGetter) {
/* 257 */     value = value.trim();
/* 258 */     String value0 = value;
/*     */     
/* 260 */     if (value.startsWith("$")) {
/* 261 */       value = value.substring("$".length());
/* 262 */     } else if (!value.startsWith("@")) {
/* 263 */       return value;
/*     */     } 
/* 265 */     boolean optional = false;
/* 266 */     if (value.startsWith("?")) {
/* 267 */       value = value.substring("?".length());
/* 268 */       optional = true;
/*     */     } 
/*     */     
/* 271 */     String newValue = propertiesGetter.apply(value);
/* 272 */     if (newValue == null) {
/* 273 */       if (optional) {
/* 274 */         return "null";
/*     */       }
/* 276 */       throw new IllegalArgumentException("variable or property '" + value + "' not found");
/*     */     } 
/*     */     
/* 279 */     if (newValue.equals(value0)) {
/* 280 */       throw new IllegalArgumentException("endless recursion in variable or property '" + value + "'");
/*     */     }
/* 282 */     return resolveValue(newValue, propertiesGetter);
/*     */   }
/*     */   
/* 285 */   enum ValueType { UNKNOWN, STRING, BOOLEAN, CHARACTER, INTEGER, FLOAT, BORDER, ICON, INSETS, DIMENSION, COLOR,
/* 286 */     SCALEDINTEGER, SCALEDFLOAT, SCALEDINSETS, SCALEDDIMENSION, INSTANCE, CLASS, GRAYFILTER, NULL, LAZY; }
/*     */   
/* 288 */   private static ValueType[] tempResultValueType = new ValueType[1];
/*     */   
/*     */   static Object parseValue(String key, String value) {
/* 291 */     return parseValue(key, value, null, v -> v, Collections.emptyList());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static Object parseValue(String key, String value, ValueType[] resultValueType, Function<String, String> resolver, List<ClassLoader> addonClassLoaders) {
/* 297 */     if (resultValueType == null) {
/* 298 */       resultValueType = tempResultValueType;
/*     */     }
/* 300 */     value = value.trim();
/*     */ 
/*     */     
/* 303 */     switch (value) { case "null":
/* 304 */         resultValueType[0] = ValueType.NULL; return null;
/* 305 */       case "false": resultValueType[0] = ValueType.BOOLEAN; return Boolean.valueOf(false);
/* 306 */       case "true": resultValueType[0] = ValueType.BOOLEAN; return Boolean.valueOf(true); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 311 */     if (value.startsWith("lazy(") && value.endsWith(")")) {
/* 312 */       resultValueType[0] = ValueType.LAZY;
/* 313 */       String uiKey = value.substring(5, value.length() - 1).trim();
/* 314 */       return t -> lazyUIManagerGet(uiKey);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 319 */     ValueType valueType = ValueType.UNKNOWN;
/*     */ 
/*     */     
/* 322 */     if (value.startsWith("#")) {
/* 323 */       valueType = ValueType.COLOR;
/* 324 */     } else if (value.startsWith("\"") && value.endsWith("\"")) {
/* 325 */       valueType = ValueType.STRING;
/* 326 */       value = value.substring(1, value.length() - 1);
/* 327 */     } else if (value.startsWith("{")) {
/* 328 */       int end = value.indexOf("}");
/* 329 */       if (end != -1) {
/*     */         try {
/* 331 */           String typeStr = value.substring("{".length(), end);
/* 332 */           valueType = ValueType.valueOf(typeStr.toUpperCase(Locale.ENGLISH));
/*     */ 
/*     */           
/* 335 */           value = value.substring(end + "}".length());
/* 336 */         } catch (IllegalArgumentException illegalArgumentException) {}
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 343 */     if (valueType == ValueType.UNKNOWN) {
/* 344 */       if (key.endsWith("UI")) {
/* 345 */         valueType = ValueType.STRING;
/* 346 */       } else if (key.endsWith("Color") || (key
/* 347 */         .endsWith("ground") && (key
/* 348 */         .endsWith(".background") || key.endsWith("Background") || key
/* 349 */         .endsWith(".foreground") || key.endsWith("Foreground")))) {
/* 350 */         valueType = ValueType.COLOR;
/* 351 */       } else if (key.endsWith(".border") || key.endsWith("Border")) {
/* 352 */         valueType = ValueType.BORDER;
/* 353 */       } else if (key.endsWith(".icon") || key.endsWith("Icon")) {
/* 354 */         valueType = ValueType.ICON;
/* 355 */       } else if (key.endsWith(".margin") || key.endsWith(".padding") || key
/* 356 */         .endsWith("Margins") || key.endsWith("Insets")) {
/* 357 */         valueType = ValueType.INSETS;
/* 358 */       } else if (key.endsWith("Size")) {
/* 359 */         valueType = ValueType.DIMENSION;
/* 360 */       } else if (key.endsWith("Width") || key.endsWith("Height")) {
/* 361 */         valueType = ValueType.INTEGER;
/* 362 */       } else if (key.endsWith("Char")) {
/* 363 */         valueType = ValueType.CHARACTER;
/* 364 */       } else if (key.endsWith("grayFilter")) {
/* 365 */         valueType = ValueType.GRAYFILTER;
/*     */       } 
/*     */     }
/* 368 */     resultValueType[0] = valueType;
/*     */ 
/*     */     
/* 371 */     switch (valueType) { case STRING:
/* 372 */         return value;
/* 373 */       case CHARACTER: return parseCharacter(value);
/* 374 */       case INTEGER: return parseInteger(value, true);
/* 375 */       case FLOAT: return parseFloat(value, true);
/* 376 */       case BORDER: return parseBorder(value, resolver, addonClassLoaders);
/* 377 */       case ICON: return parseInstance(value, addonClassLoaders);
/* 378 */       case INSETS: return parseInsets(value);
/* 379 */       case DIMENSION: return parseDimension(value);
/* 380 */       case COLOR: return parseColorOrFunction(value, resolver, true);
/* 381 */       case SCALEDINTEGER: return parseScaledInteger(value);
/* 382 */       case SCALEDFLOAT: return parseScaledFloat(value);
/* 383 */       case SCALEDINSETS: return parseScaledInsets(value);
/* 384 */       case SCALEDDIMENSION: return parseScaledDimension(value);
/* 385 */       case INSTANCE: return parseInstance(value, addonClassLoaders);
/* 386 */       case CLASS: return parseClass(value, addonClassLoaders);
/* 387 */       case GRAYFILTER: return parseGrayFilter(value); }
/*     */ 
/*     */ 
/*     */     
/* 391 */     Object color = parseColorOrFunction(value, resolver, false);
/* 392 */     if (color != null) {
/* 393 */       resultValueType[0] = ValueType.COLOR;
/* 394 */       return color;
/*     */     } 
/*     */ 
/*     */     
/* 398 */     Integer integer = parseInteger(value, false);
/* 399 */     if (integer != null) {
/* 400 */       resultValueType[0] = ValueType.INTEGER;
/* 401 */       return integer;
/*     */     } 
/*     */ 
/*     */     
/* 405 */     Float f = parseFloat(value, false);
/* 406 */     if (f != null) {
/* 407 */       resultValueType[0] = ValueType.FLOAT;
/* 408 */       return f;
/*     */     } 
/*     */ 
/*     */     
/* 412 */     resultValueType[0] = ValueType.STRING;
/* 413 */     return value;
/*     */   }
/*     */ 
/*     */   
/*     */   private static Object parseBorder(String value, Function<String, String> resolver, List<ClassLoader> addonClassLoaders) {
/* 418 */     if (value.indexOf(',') >= 0) {
/*     */       
/* 420 */       List<String> parts = split(value, ',');
/* 421 */       Insets insets = parseInsets(value);
/*     */       
/* 423 */       ColorUIResource lineColor = (parts.size() >= 5) ? (ColorUIResource)parseColorOrFunction(resolver.apply(parts.get(4)), resolver, true) : null;
/*     */       
/* 425 */       float lineThickness = (parts.size() >= 6) ? parseFloat(parts.get(5), true).floatValue() : 1.0F;
/*     */       
/* 427 */       return t -> (lineColor != null) ? new FlatLineBorder(insets, lineColor, lineThickness) : new FlatEmptyBorder(insets);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 433 */     return parseInstance(value, addonClassLoaders);
/*     */   }
/*     */   
/*     */   private static Object parseInstance(String value, List<ClassLoader> addonClassLoaders) {
/* 437 */     return t -> {
/*     */         try {
/*     */           return findClass(value, addonClassLoaders).newInstance();
/* 440 */         } catch (InstantiationException|IllegalAccessException|ClassNotFoundException ex) {
/*     */           FlatLaf.LOG.log(Level.SEVERE, "FlatLaf: Failed to instantiate '" + value + "'.", ex);
/*     */           return null;
/*     */         } 
/*     */       };
/*     */   }
/*     */   
/*     */   private static Object parseClass(String value, List<ClassLoader> addonClassLoaders) {
/* 448 */     return t -> {
/*     */         try {
/*     */           return findClass(value, addonClassLoaders);
/* 451 */         } catch (ClassNotFoundException ex) {
/*     */           FlatLaf.LOG.log(Level.SEVERE, "FlatLaf: Failed to find class '" + value + "'.", ex);
/*     */           return null;
/*     */         } 
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Class<?> findClass(String className, List<ClassLoader> addonClassLoaders) throws ClassNotFoundException {
/*     */     try {
/* 462 */       return Class.forName(className);
/* 463 */     } catch (ClassNotFoundException ex) {
/*     */       
/* 465 */       for (ClassLoader addonClassLoader : addonClassLoaders) {
/*     */         try {
/* 467 */           return addonClassLoader.loadClass(className);
/* 468 */         } catch (ClassNotFoundException classNotFoundException) {}
/*     */       } 
/*     */ 
/*     */       
/* 472 */       throw ex;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Insets parseInsets(String value) {
/* 477 */     List<String> numbers = split(value, ',');
/*     */     try {
/* 479 */       return new InsetsUIResource(
/* 480 */           Integer.parseInt(numbers.get(0)), 
/* 481 */           Integer.parseInt(numbers.get(1)), 
/* 482 */           Integer.parseInt(numbers.get(2)), 
/* 483 */           Integer.parseInt(numbers.get(3)));
/* 484 */     } catch (NumberFormatException ex) {
/* 485 */       throw new IllegalArgumentException("invalid insets '" + value + "'");
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Dimension parseDimension(String value) {
/* 490 */     List<String> numbers = split(value, ',');
/*     */     try {
/* 492 */       return new DimensionUIResource(
/* 493 */           Integer.parseInt(numbers.get(0)), 
/* 494 */           Integer.parseInt(numbers.get(1)));
/* 495 */     } catch (NumberFormatException ex) {
/* 496 */       throw new IllegalArgumentException("invalid size '" + value + "'");
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Object parseColorOrFunction(String value, Function<String, String> resolver, boolean reportError) {
/* 501 */     if (value.endsWith(")")) {
/* 502 */       return parseColorFunctions(value, resolver, reportError);
/*     */     }
/* 504 */     return parseColor(value, reportError);
/*     */   }
/*     */   
/*     */   static ColorUIResource parseColor(String value) {
/* 508 */     return parseColor(value, false);
/*     */   }
/*     */   
/*     */   private static ColorUIResource parseColor(String value, boolean reportError) {
/*     */     try {
/* 513 */       int rgba = parseColorRGBA(value);
/* 514 */       return ((rgba & 0xFF000000) == -16777216) ? new ColorUIResource(rgba) : new ColorUIResource(new Color(rgba, true));
/*     */     
/*     */     }
/* 517 */     catch (IllegalArgumentException ex) {
/* 518 */       if (reportError) {
/* 519 */         throw new IllegalArgumentException("invalid color '" + value + "'");
/*     */       }
/*     */ 
/*     */       
/* 523 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int parseColorRGBA(String value) {
/* 534 */     int len = value.length();
/* 535 */     if ((len != 4 && len != 5 && len != 7 && len != 9) || value.charAt(0) != '#') {
/* 536 */       throw new IllegalArgumentException();
/*     */     }
/*     */     
/* 539 */     int n = 0;
/* 540 */     for (int i = 1; i < len; i++) {
/* 541 */       int digit; char ch = value.charAt(i);
/*     */ 
/*     */       
/* 544 */       if (ch >= '0' && ch <= '9') {
/* 545 */         digit = ch - 48;
/* 546 */       } else if (ch >= 'a' && ch <= 'f') {
/* 547 */         digit = ch - 97 + 10;
/* 548 */       } else if (ch >= 'A' && ch <= 'F') {
/* 549 */         digit = ch - 65 + 10;
/*     */       } else {
/* 551 */         throw new IllegalArgumentException();
/*     */       } 
/* 553 */       n = n << 4 | digit;
/*     */     } 
/*     */     
/* 556 */     if (len <= 5) {
/*     */       
/* 558 */       int n1 = n & 0xF000;
/* 559 */       int n2 = n & 0xF00;
/* 560 */       int n3 = n & 0xF0;
/* 561 */       int n4 = n & 0xF;
/* 562 */       n = n1 << 16 | n1 << 12 | n2 << 12 | n2 << 8 | n3 << 8 | n3 << 4 | n4 << 4 | n4;
/*     */     } 
/*     */     
/* 565 */     return (len == 4 || len == 7) ? (0xFF000000 | n) : (n >> 8 & 0xFFFFFF | (n & 0xFF) << 24);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Object parseColorFunctions(String value, Function<String, String> resolver, boolean reportError) {
/* 571 */     int paramsStart = value.indexOf('(');
/* 572 */     if (paramsStart < 0) {
/* 573 */       if (reportError)
/* 574 */         throw new IllegalArgumentException("missing opening parenthesis in function '" + value + "'"); 
/* 575 */       return null;
/*     */     } 
/*     */     
/* 578 */     String function = value.substring(0, paramsStart).trim();
/* 579 */     List<String> params = splitFunctionParams(value.substring(paramsStart + 1, value.length() - 1), ',');
/* 580 */     if (params.isEmpty()) {
/* 581 */       throw new IllegalArgumentException("missing parameters in function '" + value + "'");
/*     */     }
/* 583 */     switch (function) { case "rgb":
/* 584 */         return parseColorRgbOrRgba(false, params, resolver, reportError);
/* 585 */       case "rgba": return parseColorRgbOrRgba(true, params, resolver, reportError);
/* 586 */       case "hsl": return parseColorHslOrHsla(false, params);
/* 587 */       case "hsla": return parseColorHslOrHsla(true, params);
/* 588 */       case "lighten": return parseColorHSLIncreaseDecrease(2, true, params, resolver, reportError);
/* 589 */       case "darken": return parseColorHSLIncreaseDecrease(2, false, params, resolver, reportError);
/* 590 */       case "saturate": return parseColorHSLIncreaseDecrease(1, true, params, resolver, reportError);
/* 591 */       case "desaturate": return parseColorHSLIncreaseDecrease(1, false, params, resolver, reportError);
/* 592 */       case "fadein": return parseColorHSLIncreaseDecrease(3, true, params, resolver, reportError);
/* 593 */       case "fadeout": return parseColorHSLIncreaseDecrease(3, false, params, resolver, reportError);
/* 594 */       case "fade": return parseColorFade(params, resolver, reportError);
/* 595 */       case "spin": return parseColorSpin(params, resolver, reportError); }
/*     */ 
/*     */     
/* 598 */     throw new IllegalArgumentException("unknown color function '" + value + "'");
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
/*     */   private static ColorUIResource parseColorRgbOrRgba(boolean hasAlpha, List<String> params, Function<String, String> resolver, boolean reportError) {
/* 611 */     if (hasAlpha && params.size() == 2) {
/*     */ 
/*     */ 
/*     */       
/* 615 */       String colorStr = params.get(0);
/* 616 */       int i = parseInteger(params.get(1), 0, 255, true).intValue();
/*     */       
/* 618 */       ColorUIResource color = (ColorUIResource)parseColorOrFunction(resolver.apply(colorStr), resolver, reportError);
/* 619 */       return new ColorUIResource(new Color((i & 0xFF) << 24 | color.getRGB() & 0xFFFFFF, true));
/*     */     } 
/*     */     
/* 622 */     int red = parseInteger(params.get(0), 0, 255, true).intValue();
/* 623 */     int green = parseInteger(params.get(1), 0, 255, true).intValue();
/* 624 */     int blue = parseInteger(params.get(2), 0, 255, true).intValue();
/* 625 */     int alpha = hasAlpha ? parseInteger(params.get(3), 0, 255, true).intValue() : 255;
/*     */     
/* 627 */     return hasAlpha ? new ColorUIResource(new Color(red, green, blue, alpha)) : new ColorUIResource(red, green, blue);
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
/*     */   private static ColorUIResource parseColorHslOrHsla(boolean hasAlpha, List<String> params) {
/* 640 */     int hue = parseInteger(params.get(0), 0, 360, false).intValue();
/* 641 */     int saturation = parsePercentage(params.get(1));
/* 642 */     int lightness = parsePercentage(params.get(2));
/* 643 */     int alpha = hasAlpha ? parsePercentage(params.get(3)) : 100;
/*     */     
/* 645 */     float[] hsl = { hue, saturation, lightness };
/* 646 */     return new ColorUIResource(HSLColor.toRGB(hsl, alpha / 100.0F));
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
/*     */   private static Object parseColorHSLIncreaseDecrease(int hslIndex, boolean increase, List<String> params, Function<String, String> resolver, boolean reportError) {
/* 660 */     String colorStr = params.get(0);
/* 661 */     int amount = parsePercentage(params.get(1));
/* 662 */     boolean relative = false;
/* 663 */     boolean autoInverse = false;
/* 664 */     boolean lazy = false;
/* 665 */     boolean derived = false;
/*     */     
/* 667 */     if (params.size() > 2) {
/* 668 */       String options = params.get(2);
/* 669 */       relative = options.contains("relative");
/* 670 */       autoInverse = options.contains("autoInverse");
/* 671 */       lazy = options.contains("lazy");
/* 672 */       derived = options.contains("derived");
/*     */ 
/*     */       
/* 675 */       if (derived && !options.contains("noAutoInverse")) {
/* 676 */         autoInverse = true;
/*     */       }
/*     */     } 
/*     */     
/* 680 */     ColorFunctions.HSLIncreaseDecrease hSLIncreaseDecrease = new ColorFunctions.HSLIncreaseDecrease(hslIndex, increase, amount, relative, autoInverse);
/*     */ 
/*     */     
/* 683 */     if (lazy) {
/* 684 */       return t -> {
/*     */           Object color = lazyUIManagerGet(colorStr);
/*     */ 
/*     */           
/*     */           return (color instanceof Color) ? new ColorUIResource(ColorFunctions.applyFunctions((Color)color, new ColorFunctions.ColorFunction[] { function })) : null;
/*     */         };
/*     */     }
/*     */ 
/*     */     
/* 693 */     return parseFunctionBaseColor(colorStr, (ColorFunctions.ColorFunction)hSLIncreaseDecrease, derived, resolver, reportError);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Object parseColorFade(List<String> params, Function<String, String> resolver, boolean reportError) {
/* 703 */     String colorStr = params.get(0);
/* 704 */     int amount = parsePercentage(params.get(1));
/* 705 */     boolean derived = false;
/*     */     
/* 707 */     if (params.size() > 2) {
/* 708 */       String options = params.get(2);
/* 709 */       derived = options.contains("derived");
/*     */     } 
/*     */ 
/*     */     
/* 713 */     ColorFunctions.Fade fade = new ColorFunctions.Fade(amount);
/*     */ 
/*     */     
/* 716 */     return parseFunctionBaseColor(colorStr, (ColorFunctions.ColorFunction)fade, derived, resolver, reportError);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Object parseColorSpin(List<String> params, Function<String, String> resolver, boolean reportError) {
/* 726 */     String colorStr = params.get(0);
/* 727 */     int amount = parseInteger(params.get(1), true).intValue();
/* 728 */     boolean derived = false;
/*     */     
/* 730 */     if (params.size() > 2) {
/* 731 */       String options = params.get(2);
/* 732 */       derived = options.contains("derived");
/*     */     } 
/*     */ 
/*     */     
/* 736 */     ColorFunctions.HSLIncreaseDecrease hSLIncreaseDecrease = new ColorFunctions.HSLIncreaseDecrease(0, true, amount, false, false);
/*     */ 
/*     */     
/* 739 */     return parseFunctionBaseColor(colorStr, (ColorFunctions.ColorFunction)hSLIncreaseDecrease, derived, resolver, reportError);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Object parseFunctionBaseColor(String colorStr, ColorFunctions.ColorFunction function, boolean derived, Function<String, String> resolver, boolean reportError) {
/* 746 */     String resolvedColorStr = resolver.apply(colorStr);
/* 747 */     ColorUIResource baseColor = (ColorUIResource)parseColorOrFunction(resolvedColorStr, resolver, reportError);
/* 748 */     if (baseColor == null) {
/* 749 */       return null;
/*     */     }
/*     */     
/* 752 */     Color newColor = ColorFunctions.applyFunctions(baseColor, new ColorFunctions.ColorFunction[] { function });
/*     */     
/* 754 */     if (derived) {
/*     */       ColorFunctions.ColorFunction[] functions;
/* 756 */       if (baseColor instanceof DerivedColor && resolvedColorStr == colorStr) {
/*     */ 
/*     */         
/* 759 */         ColorFunctions.ColorFunction[] baseFunctions = ((DerivedColor)baseColor).getFunctions();
/* 760 */         functions = new ColorFunctions.ColorFunction[baseFunctions.length + 1];
/* 761 */         System.arraycopy(baseFunctions, 0, functions, 0, baseFunctions.length);
/* 762 */         functions[baseFunctions.length] = function;
/*     */       } else {
/* 764 */         functions = new ColorFunctions.ColorFunction[] { function };
/*     */       } 
/* 766 */       return new DerivedColor(newColor, functions);
/*     */     } 
/*     */     
/* 769 */     return new ColorUIResource(newColor);
/*     */   }
/*     */   private static int parsePercentage(String value) {
/*     */     int val;
/* 773 */     if (!value.endsWith("%")) {
/* 774 */       throw new NumberFormatException("invalid percentage '" + value + "'");
/*     */     }
/*     */     
/*     */     try {
/* 778 */       val = Integer.parseInt(value.substring(0, value.length() - 1));
/* 779 */     } catch (NumberFormatException ex) {
/* 780 */       throw new NumberFormatException("invalid percentage '" + value + "'");
/*     */     } 
/*     */     
/* 783 */     if (val < 0 || val > 100)
/* 784 */       throw new IllegalArgumentException("percentage out of range (0-100%) '" + value + "'"); 
/* 785 */     return val;
/*     */   }
/*     */   
/*     */   private static Character parseCharacter(String value) {
/* 789 */     if (value.length() != 1)
/* 790 */       throw new IllegalArgumentException("invalid character '" + value + "'"); 
/* 791 */     return Character.valueOf(value.charAt(0));
/*     */   }
/*     */   
/*     */   private static Integer parseInteger(String value, int min, int max, boolean allowPercentage) {
/* 795 */     if (allowPercentage && value.endsWith("%")) {
/* 796 */       int percent = parsePercentage(value);
/* 797 */       return Integer.valueOf(max * percent / 100);
/*     */     } 
/*     */     
/* 800 */     Integer integer = parseInteger(value, true);
/* 801 */     if (integer.intValue() < min || integer.intValue() > max)
/* 802 */       throw new NumberFormatException("integer '" + value + "' out of range (" + min + '-' + max + ')'); 
/* 803 */     return integer;
/*     */   }
/*     */   
/*     */   private static Integer parseInteger(String value, boolean reportError) {
/*     */     try {
/* 808 */       return Integer.valueOf(Integer.parseInt(value));
/* 809 */     } catch (NumberFormatException ex) {
/* 810 */       if (reportError) {
/* 811 */         throw new NumberFormatException("invalid integer '" + value + "'");
/*     */       }
/* 813 */       return null;
/*     */     } 
/*     */   }
/*     */   private static Float parseFloat(String value, boolean reportError) {
/*     */     try {
/* 818 */       return Float.valueOf(Float.parseFloat(value));
/* 819 */     } catch (NumberFormatException ex) {
/* 820 */       if (reportError) {
/* 821 */         throw new NumberFormatException("invalid float '" + value + "'");
/*     */       }
/* 823 */       return null;
/*     */     } 
/*     */   }
/*     */   private static UIDefaults.ActiveValue parseScaledInteger(String value) {
/* 827 */     int val = parseInteger(value, true).intValue();
/* 828 */     return t -> Integer.valueOf(UIScale.scale(val));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static UIDefaults.ActiveValue parseScaledFloat(String value) {
/* 834 */     float val = parseFloat(value, true).floatValue();
/* 835 */     return t -> Float.valueOf(UIScale.scale(val));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static UIDefaults.ActiveValue parseScaledInsets(String value) {
/* 841 */     Insets insets = parseInsets(value);
/* 842 */     return t -> UIScale.scale(insets);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static UIDefaults.ActiveValue parseScaledDimension(String value) {
/* 848 */     Dimension dimension = parseDimension(value);
/* 849 */     return t -> UIScale.scale(dimension);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Object parseGrayFilter(String value) {
/* 855 */     List<String> numbers = split(value, ',');
/*     */     try {
/* 857 */       int brightness = Integer.parseInt(numbers.get(0));
/* 858 */       int contrast = Integer.parseInt(numbers.get(1));
/* 859 */       int alpha = Integer.parseInt(numbers.get(2));
/*     */       
/* 861 */       return t -> new GrayFilter(brightness, contrast, alpha);
/*     */     
/*     */     }
/* 864 */     catch (NumberFormatException ex) {
/* 865 */       throw new IllegalArgumentException("invalid gray filter '" + value + "'");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static List<String> split(String str, char delim) {
/* 873 */     List<String> result = StringUtils.split(str, delim);
/*     */ 
/*     */     
/* 876 */     int size = result.size();
/* 877 */     for (int i = 0; i < size; i++) {
/* 878 */       result.set(i, ((String)result.get(i)).trim());
/*     */     }
/* 880 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static List<String> splitFunctionParams(String str, char delim) {
/* 888 */     ArrayList<String> strs = new ArrayList<>();
/* 889 */     int nestLevel = 0;
/* 890 */     int start = 0;
/* 891 */     int strlen = str.length();
/* 892 */     for (int i = 0; i < strlen; i++) {
/* 893 */       char ch = str.charAt(i);
/* 894 */       if (ch == '(') {
/* 895 */         nestLevel++;
/* 896 */       } else if (ch == ')') {
/* 897 */         nestLevel--;
/* 898 */       } else if (nestLevel == 0 && ch == delim) {
/* 899 */         strs.add(str.substring(start, i).trim());
/* 900 */         start = i + 1;
/*     */       } 
/*     */     } 
/* 903 */     strs.add(str.substring(start).trim());
/*     */     
/* 905 */     return strs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Object lazyUIManagerGet(String uiKey) {
/* 913 */     boolean optional = false;
/* 914 */     if (uiKey.startsWith("?")) {
/* 915 */       uiKey = uiKey.substring("?".length());
/* 916 */       optional = true;
/*     */     } 
/*     */     
/* 919 */     Object value = UIManager.get(uiKey);
/* 920 */     if (value == null && !optional)
/* 921 */       FlatLaf.LOG.log(Level.SEVERE, "FlatLaf: '" + uiKey + "' not found in UI defaults."); 
/* 922 */     return value;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\UIDefaultsLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */