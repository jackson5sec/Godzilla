/*    */ package com.google.common.io;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.io.File;
/*    */ import java.io.FilenameFilter;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Beta
/*    */ @GwtIncompatible
/*    */ public final class PatternFilenameFilter
/*    */   implements FilenameFilter
/*    */ {
/*    */   private final Pattern pattern;
/*    */   
/*    */   public PatternFilenameFilter(String patternStr) {
/* 46 */     this(Pattern.compile(patternStr));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PatternFilenameFilter(Pattern pattern) {
/* 55 */     this.pattern = (Pattern)Preconditions.checkNotNull(pattern);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean accept(File dir, String fileName) {
/* 60 */     return this.pattern.matcher(fileName).matches();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\io\PatternFilenameFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */