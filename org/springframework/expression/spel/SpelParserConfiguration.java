/*     */ package org.springframework.expression.spel;
/*     */ 
/*     */ import org.springframework.core.SpringProperties;
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
/*     */ public class SpelParserConfiguration
/*     */ {
/*     */   public static final String SPRING_EXPRESSION_COMPILER_MODE_PROPERTY_NAME = "spring.expression.compiler.mode";
/*     */   private static final SpelCompilerMode defaultCompilerMode;
/*     */   private final SpelCompilerMode compilerMode;
/*     */   @Nullable
/*     */   private final ClassLoader compilerClassLoader;
/*     */   private final boolean autoGrowNullReferences;
/*     */   private final boolean autoGrowCollections;
/*     */   private final int maximumAutoGrowSize;
/*     */   
/*     */   static {
/*  40 */     String compilerMode = SpringProperties.getProperty("spring.expression.compiler.mode");
/*     */     
/*  42 */     defaultCompilerMode = (compilerMode != null) ? SpelCompilerMode.valueOf(compilerMode.toUpperCase()) : SpelCompilerMode.OFF;
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
/*     */   public SpelParserConfiguration() {
/*  62 */     this(null, null, false, false, 2147483647);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SpelParserConfiguration(@Nullable SpelCompilerMode compilerMode, @Nullable ClassLoader compilerClassLoader) {
/*  71 */     this(compilerMode, compilerClassLoader, false, false, 2147483647);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SpelParserConfiguration(boolean autoGrowNullReferences, boolean autoGrowCollections) {
/*  81 */     this(null, null, autoGrowNullReferences, autoGrowCollections, 2147483647);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SpelParserConfiguration(boolean autoGrowNullReferences, boolean autoGrowCollections, int maximumAutoGrowSize) {
/*  91 */     this(null, null, autoGrowNullReferences, autoGrowCollections, maximumAutoGrowSize);
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
/*     */   public SpelParserConfiguration(@Nullable SpelCompilerMode compilerMode, @Nullable ClassLoader compilerClassLoader, boolean autoGrowNullReferences, boolean autoGrowCollections, int maximumAutoGrowSize) {
/* 105 */     this.compilerMode = (compilerMode != null) ? compilerMode : defaultCompilerMode;
/* 106 */     this.compilerClassLoader = compilerClassLoader;
/* 107 */     this.autoGrowNullReferences = autoGrowNullReferences;
/* 108 */     this.autoGrowCollections = autoGrowCollections;
/* 109 */     this.maximumAutoGrowSize = maximumAutoGrowSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SpelCompilerMode getCompilerMode() {
/* 117 */     return this.compilerMode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ClassLoader getCompilerClassLoader() {
/* 125 */     return this.compilerClassLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAutoGrowNullReferences() {
/* 132 */     return this.autoGrowNullReferences;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAutoGrowCollections() {
/* 139 */     return this.autoGrowCollections;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaximumAutoGrowSize() {
/* 146 */     return this.maximumAutoGrowSize;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\SpelParserConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */