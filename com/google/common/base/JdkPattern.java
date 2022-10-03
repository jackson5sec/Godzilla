/*    */ package com.google.common.base;
/*    */ 
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import java.io.Serializable;
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtIncompatible
/*    */ final class JdkPattern
/*    */   extends CommonPattern
/*    */   implements Serializable
/*    */ {
/*    */   private final Pattern pattern;
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   JdkPattern(Pattern pattern) {
/* 28 */     this.pattern = Preconditions.<Pattern>checkNotNull(pattern);
/*    */   }
/*    */ 
/*    */   
/*    */   public CommonMatcher matcher(CharSequence t) {
/* 33 */     return new JdkMatcher(this.pattern.matcher(t));
/*    */   }
/*    */ 
/*    */   
/*    */   public String pattern() {
/* 38 */     return this.pattern.pattern();
/*    */   }
/*    */ 
/*    */   
/*    */   public int flags() {
/* 43 */     return this.pattern.flags();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 48 */     return this.pattern.toString();
/*    */   }
/*    */   
/*    */   private static final class JdkMatcher extends CommonMatcher {
/*    */     final Matcher matcher;
/*    */     
/*    */     JdkMatcher(Matcher matcher) {
/* 55 */       this.matcher = Preconditions.<Matcher>checkNotNull(matcher);
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean matches() {
/* 60 */       return this.matcher.matches();
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean find() {
/* 65 */       return this.matcher.find();
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean find(int index) {
/* 70 */       return this.matcher.find(index);
/*    */     }
/*    */ 
/*    */     
/*    */     public String replaceAll(String replacement) {
/* 75 */       return this.matcher.replaceAll(replacement);
/*    */     }
/*    */ 
/*    */     
/*    */     public int end() {
/* 80 */       return this.matcher.end();
/*    */     }
/*    */ 
/*    */     
/*    */     public int start() {
/* 85 */       return this.matcher.start();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\base\JdkPattern.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */