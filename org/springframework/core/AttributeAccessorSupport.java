/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public abstract class AttributeAccessorSupport
/*     */   implements AttributeAccessor, Serializable
/*     */ {
/*  43 */   private final Map<String, Object> attributes = new LinkedHashMap<>();
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAttribute(String name, @Nullable Object value) {
/*  48 */     Assert.notNull(name, "Name must not be null");
/*  49 */     if (value != null) {
/*  50 */       this.attributes.put(name, value);
/*     */     } else {
/*     */       
/*  53 */       removeAttribute(name);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getAttribute(String name) {
/*  60 */     Assert.notNull(name, "Name must not be null");
/*  61 */     return this.attributes.get(name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T computeAttribute(String name, Function<String, T> computeFunction) {
/*  67 */     Assert.notNull(name, "Name must not be null");
/*  68 */     Assert.notNull(computeFunction, "Compute function must not be null");
/*  69 */     Object value = this.attributes.computeIfAbsent(name, computeFunction);
/*  70 */     Assert.state((value != null), () -> String.format("Compute function must not return null for attribute named '%s'", new Object[] { name }));
/*     */     
/*  72 */     return (T)value;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object removeAttribute(String name) {
/*  78 */     Assert.notNull(name, "Name must not be null");
/*  79 */     return this.attributes.remove(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasAttribute(String name) {
/*  84 */     Assert.notNull(name, "Name must not be null");
/*  85 */     return this.attributes.containsKey(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] attributeNames() {
/*  90 */     return StringUtils.toStringArray(this.attributes.keySet());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void copyAttributesFrom(AttributeAccessor source) {
/*  99 */     Assert.notNull(source, "Source must not be null");
/* 100 */     String[] attributeNames = source.attributeNames();
/* 101 */     for (String attributeName : attributeNames) {
/* 102 */       setAttribute(attributeName, source.getAttribute(attributeName));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object other) {
/* 109 */     return (this == other || (other instanceof AttributeAccessorSupport && this.attributes
/* 110 */       .equals(((AttributeAccessorSupport)other).attributes)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 115 */     return this.attributes.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\AttributeAccessorSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */