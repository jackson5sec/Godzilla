/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InstanceFilter<T>
/*     */ {
/*     */   private final Collection<? extends T> includes;
/*     */   private final Collection<? extends T> excludes;
/*     */   private final boolean matchIfEmpty;
/*     */   
/*     */   public InstanceFilter(@Nullable Collection<? extends T> includes, @Nullable Collection<? extends T> excludes, boolean matchIfEmpty) {
/*  60 */     this.includes = (includes != null) ? includes : Collections.<T>emptyList();
/*  61 */     this.excludes = (excludes != null) ? excludes : Collections.<T>emptyList();
/*  62 */     this.matchIfEmpty = matchIfEmpty;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean match(T instance) {
/*  70 */     Assert.notNull(instance, "Instance to match must not be null");
/*     */     
/*  72 */     boolean includesSet = !this.includes.isEmpty();
/*  73 */     boolean excludesSet = !this.excludes.isEmpty();
/*  74 */     if (!includesSet && !excludesSet) {
/*  75 */       return this.matchIfEmpty;
/*     */     }
/*     */     
/*  78 */     boolean matchIncludes = match(instance, this.includes);
/*  79 */     boolean matchExcludes = match(instance, this.excludes);
/*  80 */     if (!includesSet) {
/*  81 */       return !matchExcludes;
/*     */     }
/*  83 */     if (!excludesSet) {
/*  84 */       return matchIncludes;
/*     */     }
/*  86 */     return (matchIncludes && !matchExcludes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean match(T instance, T candidate) {
/*  97 */     return instance.equals(candidate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean match(T instance, Collection<? extends T> candidates) {
/* 108 */     for (T candidate : candidates) {
/* 109 */       if (match(instance, candidate)) {
/* 110 */         return true;
/*     */       }
/*     */     } 
/* 113 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 118 */     StringBuilder sb = new StringBuilder(getClass().getSimpleName());
/* 119 */     sb.append(": includes=").append(this.includes);
/* 120 */     sb.append(", excludes=").append(this.excludes);
/* 121 */     sb.append(", matchIfEmpty=").append(this.matchIfEmpty);
/* 122 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\InstanceFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */