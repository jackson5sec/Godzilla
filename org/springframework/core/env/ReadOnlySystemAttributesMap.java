/*     */ package org.springframework.core.env;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ 
/*     */ abstract class ReadOnlySystemAttributesMap
/*     */   implements Map<String, String>
/*     */ {
/*     */   public boolean containsKey(Object key) {
/*  44 */     return (get(key) != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String get(Object key) {
/*  56 */     if (!(key instanceof String)) {
/*  57 */       throw new IllegalArgumentException("Type of key [" + key
/*  58 */           .getClass().getName() + "] must be java.lang.String");
/*     */     }
/*  60 */     return getSystemAttribute((String)key);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  65 */     return false;
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
/*     */   public int size() {
/*  80 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public String put(String key, String value) {
/*  85 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/*  90 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public String remove(Object key) {
/*  95 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 100 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> keySet() {
/* 105 */     return Collections.emptySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends String, ? extends String> map) {
/* 110 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<String> values() {
/* 115 */     return Collections.emptySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Map.Entry<String, String>> entrySet() {
/* 120 */     return Collections.emptySet();
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected abstract String getSystemAttribute(String paramString);
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\env\ReadOnlySystemAttributesMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */