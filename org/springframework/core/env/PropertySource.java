/*     */ package org.springframework.core.env;
/*     */ 
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ public abstract class PropertySource<T>
/*     */ {
/*  62 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */   
/*     */   protected final String name;
/*     */ 
/*     */ 
/*     */   
/*     */   protected final T source;
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertySource(String name, T source) {
/*  75 */     Assert.hasText(name, "Property source name must contain at least one character");
/*  76 */     Assert.notNull(source, "Property source must not be null");
/*  77 */     this.name = name;
/*  78 */     this.source = source;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertySource(String name) {
/*  89 */     this(name, (T)new Object());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  97 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T getSource() {
/* 104 */     return this.source;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsProperty(String name) {
/* 115 */     return (getProperty(name) != null);
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
/*     */   @Nullable
/*     */   public abstract Object getProperty(String paramString);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object other) {
/* 138 */     return (this == other || (other instanceof PropertySource && 
/* 139 */       ObjectUtils.nullSafeEquals(getName(), ((PropertySource)other).getName())));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 148 */     return ObjectUtils.nullSafeHashCode(getName());
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
/*     */   public String toString() {
/* 162 */     if (this.logger.isDebugEnabled()) {
/* 163 */       return getClass().getSimpleName() + "@" + System.identityHashCode(this) + " {name='" + 
/* 164 */         getName() + "', properties=" + getSource() + "}";
/*     */     }
/*     */     
/* 167 */     return getClass().getSimpleName() + " {name='" + getName() + "'}";
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PropertySource<?> named(String name) {
/* 190 */     return new ComparisonPropertySource(name);
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
/*     */   public static class StubPropertySource
/*     */     extends PropertySource<Object>
/*     */   {
/*     */     public StubPropertySource(String name) {
/* 209 */       super(name, new Object());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String getProperty(String name) {
/* 218 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class ComparisonPropertySource
/*     */     extends StubPropertySource
/*     */   {
/*     */     private static final String USAGE_ERROR = "ComparisonPropertySource instances are for use with collection comparison only";
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ComparisonPropertySource(String name) {
/* 235 */       super(name);
/*     */     }
/*     */ 
/*     */     
/*     */     public Object getSource() {
/* 240 */       throw new UnsupportedOperationException("ComparisonPropertySource instances are for use with collection comparison only");
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsProperty(String name) {
/* 245 */       throw new UnsupportedOperationException("ComparisonPropertySource instances are for use with collection comparison only");
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String getProperty(String name) {
/* 251 */       throw new UnsupportedOperationException("ComparisonPropertySource instances are for use with collection comparison only");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\env\PropertySource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */