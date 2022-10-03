/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ import java.util.Map;
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
/*     */ public class SimpleRouteMatcher
/*     */   implements RouteMatcher
/*     */ {
/*     */   private final PathMatcher pathMatcher;
/*     */   
/*     */   public SimpleRouteMatcher(PathMatcher pathMatcher) {
/*  45 */     Assert.notNull(pathMatcher, "PathMatcher is required");
/*  46 */     this.pathMatcher = pathMatcher;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PathMatcher getPathMatcher() {
/*  53 */     return this.pathMatcher;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public RouteMatcher.Route parseRoute(String route) {
/*  59 */     return new DefaultRoute(route);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPattern(String route) {
/*  64 */     return this.pathMatcher.isPattern(route);
/*     */   }
/*     */ 
/*     */   
/*     */   public String combine(String pattern1, String pattern2) {
/*  69 */     return this.pathMatcher.combine(pattern1, pattern2);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean match(String pattern, RouteMatcher.Route route) {
/*  74 */     return this.pathMatcher.match(pattern, route.value());
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Map<String, String> matchAndExtract(String pattern, RouteMatcher.Route route) {
/*  80 */     if (!match(pattern, route)) {
/*  81 */       return null;
/*     */     }
/*  83 */     return this.pathMatcher.extractUriTemplateVariables(pattern, route.value());
/*     */   }
/*     */ 
/*     */   
/*     */   public Comparator<String> getPatternComparator(RouteMatcher.Route route) {
/*  88 */     return this.pathMatcher.getPatternComparator(route.value());
/*     */   }
/*     */   
/*     */   private static class DefaultRoute
/*     */     implements RouteMatcher.Route
/*     */   {
/*     */     private final String path;
/*     */     
/*     */     DefaultRoute(String path) {
/*  97 */       this.path = path;
/*     */     }
/*     */ 
/*     */     
/*     */     public String value() {
/* 102 */       return this.path;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 107 */       return value();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\SimpleRouteMatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */