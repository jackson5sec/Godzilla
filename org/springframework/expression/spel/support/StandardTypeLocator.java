/*     */ package org.springframework.expression.spel.support;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.TypeLocator;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
/*     */ import org.springframework.expression.spel.SpelMessage;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StandardTypeLocator
/*     */   implements TypeLocator
/*     */ {
/*     */   @Nullable
/*     */   private final ClassLoader classLoader;
/*  44 */   private final List<String> knownPackagePrefixes = new ArrayList<>(1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StandardTypeLocator() {
/*  52 */     this(ClassUtils.getDefaultClassLoader());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StandardTypeLocator(@Nullable ClassLoader classLoader) {
/*  60 */     this.classLoader = classLoader;
/*     */     
/*  62 */     registerImport("java.lang");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerImport(String prefix) {
/*  72 */     this.knownPackagePrefixes.add(prefix);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeImport(String prefix) {
/*  80 */     this.knownPackagePrefixes.remove(prefix);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> getImportPrefixes() {
/*  88 */     return Collections.unmodifiableList(this.knownPackagePrefixes);
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
/*     */   public Class<?> findType(String typeName) throws EvaluationException {
/* 101 */     String nameToLookup = typeName;
/*     */     try {
/* 103 */       return ClassUtils.forName(nameToLookup, this.classLoader);
/*     */     }
/* 105 */     catch (ClassNotFoundException classNotFoundException) {
/*     */ 
/*     */       
/* 108 */       for (String prefix : this.knownPackagePrefixes) {
/*     */         try {
/* 110 */           nameToLookup = prefix + '.' + typeName;
/* 111 */           return ClassUtils.forName(nameToLookup, this.classLoader);
/*     */         }
/* 113 */         catch (ClassNotFoundException classNotFoundException1) {}
/*     */       } 
/*     */ 
/*     */       
/* 117 */       throw new SpelEvaluationException(SpelMessage.TYPE_NOT_FOUND, new Object[] { typeName });
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\support\StandardTypeLocator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */