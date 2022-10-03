/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public class LinkedMultiValueMap<K, V>
/*     */   extends MultiValueMapAdapter<K, V>
/*     */   implements Serializable, Cloneable
/*     */ {
/*     */   private static final long serialVersionUID = 3801124242820219131L;
/*     */   
/*     */   public LinkedMultiValueMap() {
/*  48 */     super(new LinkedHashMap<>());
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
/*     */   public LinkedMultiValueMap(int expectedSize) {
/*  60 */     super(CollectionUtils.newLinkedHashMap(expectedSize));
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
/*     */   public LinkedMultiValueMap(Map<K, List<V>> otherMap) {
/*  72 */     super(new LinkedHashMap<>(otherMap));
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
/*     */   public LinkedMultiValueMap<K, V> deepCopy() {
/*  86 */     LinkedMultiValueMap<K, V> copy = new LinkedMultiValueMap(size());
/*  87 */     forEach((key, values) -> copy.put(key, new ArrayList(values)));
/*  88 */     return copy;
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
/*     */   public LinkedMultiValueMap<K, V> clone() {
/* 104 */     return new LinkedMultiValueMap(this);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\LinkedMultiValueMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */