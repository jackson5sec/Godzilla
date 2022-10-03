/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ public class PropertyPlaceholderHelper
/*     */ {
/*  44 */   private static final Log logger = LogFactory.getLog(PropertyPlaceholderHelper.class);
/*     */   
/*  46 */   private static final Map<String, String> wellKnownSimplePrefixes = new HashMap<>(4);
/*     */   
/*     */   static {
/*  49 */     wellKnownSimplePrefixes.put("}", "{");
/*  50 */     wellKnownSimplePrefixes.put("]", "[");
/*  51 */     wellKnownSimplePrefixes.put(")", "(");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final String placeholderPrefix;
/*     */ 
/*     */   
/*     */   private final String placeholderSuffix;
/*     */ 
/*     */   
/*     */   private final String simplePrefix;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private final String valueSeparator;
/*     */ 
/*     */   
/*     */   private final boolean ignoreUnresolvablePlaceholders;
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyPlaceholderHelper(String placeholderPrefix, String placeholderSuffix) {
/*  74 */     this(placeholderPrefix, placeholderSuffix, null, true);
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
/*     */   public PropertyPlaceholderHelper(String placeholderPrefix, String placeholderSuffix, @Nullable String valueSeparator, boolean ignoreUnresolvablePlaceholders) {
/*  89 */     Assert.notNull(placeholderPrefix, "'placeholderPrefix' must not be null");
/*  90 */     Assert.notNull(placeholderSuffix, "'placeholderSuffix' must not be null");
/*  91 */     this.placeholderPrefix = placeholderPrefix;
/*  92 */     this.placeholderSuffix = placeholderSuffix;
/*  93 */     String simplePrefixForSuffix = wellKnownSimplePrefixes.get(this.placeholderSuffix);
/*  94 */     if (simplePrefixForSuffix != null && this.placeholderPrefix.endsWith(simplePrefixForSuffix)) {
/*  95 */       this.simplePrefix = simplePrefixForSuffix;
/*     */     } else {
/*     */       
/*  98 */       this.simplePrefix = this.placeholderPrefix;
/*     */     } 
/* 100 */     this.valueSeparator = valueSeparator;
/* 101 */     this.ignoreUnresolvablePlaceholders = ignoreUnresolvablePlaceholders;
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
/*     */   public String replacePlaceholders(String value, Properties properties) {
/* 113 */     Assert.notNull(properties, "'properties' must not be null");
/* 114 */     return replacePlaceholders(value, properties::getProperty);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String replacePlaceholders(String value, PlaceholderResolver placeholderResolver) {
/* 125 */     Assert.notNull(value, "'value' must not be null");
/* 126 */     return parseStringValue(value, placeholderResolver, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected String parseStringValue(String value, PlaceholderResolver placeholderResolver, @Nullable Set<String> visitedPlaceholders) {
/* 132 */     int startIndex = value.indexOf(this.placeholderPrefix);
/* 133 */     if (startIndex == -1) {
/* 134 */       return value;
/*     */     }
/*     */     
/* 137 */     StringBuilder result = new StringBuilder(value);
/* 138 */     while (startIndex != -1) {
/* 139 */       int endIndex = findPlaceholderEndIndex(result, startIndex);
/* 140 */       if (endIndex != -1) {
/* 141 */         String placeholder = result.substring(startIndex + this.placeholderPrefix.length(), endIndex);
/* 142 */         String originalPlaceholder = placeholder;
/* 143 */         if (visitedPlaceholders == null) {
/* 144 */           visitedPlaceholders = new HashSet<>(4);
/*     */         }
/* 146 */         if (!visitedPlaceholders.add(originalPlaceholder)) {
/* 147 */           throw new IllegalArgumentException("Circular placeholder reference '" + originalPlaceholder + "' in property definitions");
/*     */         }
/*     */ 
/*     */         
/* 151 */         placeholder = parseStringValue(placeholder, placeholderResolver, visitedPlaceholders);
/*     */         
/* 153 */         String propVal = placeholderResolver.resolvePlaceholder(placeholder);
/* 154 */         if (propVal == null && this.valueSeparator != null) {
/* 155 */           int separatorIndex = placeholder.indexOf(this.valueSeparator);
/* 156 */           if (separatorIndex != -1) {
/* 157 */             String actualPlaceholder = placeholder.substring(0, separatorIndex);
/* 158 */             String defaultValue = placeholder.substring(separatorIndex + this.valueSeparator.length());
/* 159 */             propVal = placeholderResolver.resolvePlaceholder(actualPlaceholder);
/* 160 */             if (propVal == null) {
/* 161 */               propVal = defaultValue;
/*     */             }
/*     */           } 
/*     */         } 
/* 165 */         if (propVal != null) {
/*     */ 
/*     */           
/* 168 */           propVal = parseStringValue(propVal, placeholderResolver, visitedPlaceholders);
/* 169 */           result.replace(startIndex, endIndex + this.placeholderSuffix.length(), propVal);
/* 170 */           if (logger.isTraceEnabled()) {
/* 171 */             logger.trace("Resolved placeholder '" + placeholder + "'");
/*     */           }
/* 173 */           startIndex = result.indexOf(this.placeholderPrefix, startIndex + propVal.length());
/*     */         }
/* 175 */         else if (this.ignoreUnresolvablePlaceholders) {
/*     */           
/* 177 */           startIndex = result.indexOf(this.placeholderPrefix, endIndex + this.placeholderSuffix.length());
/*     */         } else {
/*     */           
/* 180 */           throw new IllegalArgumentException("Could not resolve placeholder '" + placeholder + "' in value \"" + value + "\"");
/*     */         } 
/*     */         
/* 183 */         visitedPlaceholders.remove(originalPlaceholder);
/*     */         continue;
/*     */       } 
/* 186 */       startIndex = -1;
/*     */     } 
/*     */     
/* 189 */     return result.toString();
/*     */   }
/*     */   
/*     */   private int findPlaceholderEndIndex(CharSequence buf, int startIndex) {
/* 193 */     int index = startIndex + this.placeholderPrefix.length();
/* 194 */     int withinNestedPlaceholder = 0;
/* 195 */     while (index < buf.length()) {
/* 196 */       if (StringUtils.substringMatch(buf, index, this.placeholderSuffix)) {
/* 197 */         if (withinNestedPlaceholder > 0) {
/* 198 */           withinNestedPlaceholder--;
/* 199 */           index += this.placeholderSuffix.length();
/*     */           continue;
/*     */         } 
/* 202 */         return index;
/*     */       } 
/*     */       
/* 205 */       if (StringUtils.substringMatch(buf, index, this.simplePrefix)) {
/* 206 */         withinNestedPlaceholder++;
/* 207 */         index += this.simplePrefix.length();
/*     */         continue;
/*     */       } 
/* 210 */       index++;
/*     */     } 
/*     */     
/* 213 */     return -1;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface PlaceholderResolver {
/*     */     @Nullable
/*     */     String resolvePlaceholder(String param1String);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\PropertyPlaceholderHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */