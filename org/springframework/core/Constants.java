/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ReflectionUtils;
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
/*     */ public class Constants
/*     */ {
/*     */   private final String className;
/*  53 */   private final Map<String, Object> fieldCache = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Constants(Class<?> clazz) {
/*  63 */     Assert.notNull(clazz, "Class must not be null");
/*  64 */     this.className = clazz.getName();
/*  65 */     Field[] fields = clazz.getFields();
/*  66 */     for (Field field : fields) {
/*  67 */       if (ReflectionUtils.isPublicStaticFinal(field)) {
/*  68 */         String name = field.getName();
/*     */         try {
/*  70 */           Object value = field.get(null);
/*  71 */           this.fieldCache.put(name, value);
/*     */         }
/*  73 */         catch (IllegalAccessException illegalAccessException) {}
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getClassName() {
/*  85 */     return this.className;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getSize() {
/*  92 */     return this.fieldCache.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Map<String, Object> getFieldCache() {
/* 100 */     return this.fieldCache;
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
/*     */   public Number asNumber(String code) throws ConstantException {
/* 113 */     Object obj = asObject(code);
/* 114 */     if (!(obj instanceof Number)) {
/* 115 */       throw new ConstantException(this.className, code, "not a Number");
/*     */     }
/* 117 */     return (Number)obj;
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
/*     */   public String asString(String code) throws ConstantException {
/* 129 */     return asObject(code).toString();
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
/*     */   public Object asObject(String code) throws ConstantException {
/* 141 */     Assert.notNull(code, "Code must not be null");
/* 142 */     String codeToUse = code.toUpperCase(Locale.ENGLISH);
/* 143 */     Object val = this.fieldCache.get(codeToUse);
/* 144 */     if (val == null) {
/* 145 */       throw new ConstantException(this.className, codeToUse, "not found");
/*     */     }
/* 147 */     return val;
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
/*     */   public Set<String> getNames(@Nullable String namePrefix) {
/* 162 */     String prefixToUse = (namePrefix != null) ? namePrefix.trim().toUpperCase(Locale.ENGLISH) : "";
/* 163 */     Set<String> names = new HashSet<>();
/* 164 */     for (String code : this.fieldCache.keySet()) {
/* 165 */       if (code.startsWith(prefixToUse)) {
/* 166 */         names.add(code);
/*     */       }
/*     */     } 
/* 169 */     return names;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<String> getNamesForProperty(String propertyName) {
/* 180 */     return getNames(propertyToConstantNamePrefix(propertyName));
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
/*     */   public Set<String> getNamesForSuffix(@Nullable String nameSuffix) {
/* 194 */     String suffixToUse = (nameSuffix != null) ? nameSuffix.trim().toUpperCase(Locale.ENGLISH) : "";
/* 195 */     Set<String> names = new HashSet<>();
/* 196 */     for (String code : this.fieldCache.keySet()) {
/* 197 */       if (code.endsWith(suffixToUse)) {
/* 198 */         names.add(code);
/*     */       }
/*     */     } 
/* 201 */     return names;
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
/*     */   public Set<Object> getValues(@Nullable String namePrefix) {
/* 216 */     String prefixToUse = (namePrefix != null) ? namePrefix.trim().toUpperCase(Locale.ENGLISH) : "";
/* 217 */     Set<Object> values = new HashSet();
/* 218 */     this.fieldCache.forEach((code, value) -> {
/*     */           if (code.startsWith(prefixToUse)) {
/*     */             values.add(value);
/*     */           }
/*     */         });
/* 223 */     return values;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Object> getValuesForProperty(String propertyName) {
/* 234 */     return getValues(propertyToConstantNamePrefix(propertyName));
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
/*     */   public Set<Object> getValuesForSuffix(@Nullable String nameSuffix) {
/* 248 */     String suffixToUse = (nameSuffix != null) ? nameSuffix.trim().toUpperCase(Locale.ENGLISH) : "";
/* 249 */     Set<Object> values = new HashSet();
/* 250 */     this.fieldCache.forEach((code, value) -> {
/*     */           if (code.endsWith(suffixToUse)) {
/*     */             values.add(value);
/*     */           }
/*     */         });
/* 255 */     return values;
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
/*     */   public String toCode(Object value, @Nullable String namePrefix) throws ConstantException {
/* 268 */     String prefixToUse = (namePrefix != null) ? namePrefix.trim().toUpperCase(Locale.ENGLISH) : "";
/* 269 */     for (Map.Entry<String, Object> entry : this.fieldCache.entrySet()) {
/* 270 */       if (((String)entry.getKey()).startsWith(prefixToUse) && entry.getValue().equals(value)) {
/* 271 */         return entry.getKey();
/*     */       }
/*     */     } 
/* 274 */     throw new ConstantException(this.className, prefixToUse, value);
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
/*     */   public String toCodeForProperty(Object value, String propertyName) throws ConstantException {
/* 287 */     return toCode(value, propertyToConstantNamePrefix(propertyName));
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
/*     */   public String toCodeForSuffix(Object value, @Nullable String nameSuffix) throws ConstantException {
/* 299 */     String suffixToUse = (nameSuffix != null) ? nameSuffix.trim().toUpperCase(Locale.ENGLISH) : "";
/* 300 */     for (Map.Entry<String, Object> entry : this.fieldCache.entrySet()) {
/* 301 */       if (((String)entry.getKey()).endsWith(suffixToUse) && entry.getValue().equals(value)) {
/* 302 */         return entry.getKey();
/*     */       }
/*     */     } 
/* 305 */     throw new ConstantException(this.className, suffixToUse, value);
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
/*     */   public String propertyToConstantNamePrefix(String propertyName) {
/* 323 */     StringBuilder parsedPrefix = new StringBuilder();
/* 324 */     for (int i = 0; i < propertyName.length(); i++) {
/* 325 */       char c = propertyName.charAt(i);
/* 326 */       if (Character.isUpperCase(c)) {
/* 327 */         parsedPrefix.append('_');
/* 328 */         parsedPrefix.append(c);
/*     */       } else {
/*     */         
/* 331 */         parsedPrefix.append(Character.toUpperCase(c));
/*     */       } 
/*     */     } 
/* 334 */     return parsedPrefix.toString();
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
/*     */   public static class ConstantException
/*     */     extends IllegalArgumentException
/*     */   {
/*     */     public ConstantException(String className, String field, String message) {
/* 352 */       super("Field '" + field + "' " + message + " in class [" + className + "]");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ConstantException(String className, String namePrefix, Object value) {
/* 362 */       super("No '" + namePrefix + "' field with value '" + value + "' found in class [" + className + "]");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\Constants.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */