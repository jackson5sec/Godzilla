/*     */ package com.google.common.net;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.escape.Escaper;
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
/*     */ @GwtCompatible
/*     */ public final class UrlEscapers
/*     */ {
/*     */   static final String URL_FORM_PARAMETER_OTHER_SAFE_CHARS = "-_.*";
/*     */   static final String URL_PATH_OTHER_SAFE_CHARS_LACKING_PLUS = "-._~!$'()*,;&=@:";
/*     */   
/*     */   public static Escaper urlFormParameterEscaper() {
/*  75 */     return URL_FORM_PARAMETER_ESCAPER;
/*     */   }
/*     */   
/*  78 */   private static final Escaper URL_FORM_PARAMETER_ESCAPER = (Escaper)new PercentEscaper("-_.*", true);
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
/*     */   public static Escaper urlPathSegmentEscaper() {
/* 111 */     return URL_PATH_SEGMENT_ESCAPER;
/*     */   }
/*     */   
/* 114 */   private static final Escaper URL_PATH_SEGMENT_ESCAPER = (Escaper)new PercentEscaper("-._~!$'()*,;&=@:+", false);
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
/*     */   public static Escaper urlFragmentEscaper() {
/* 143 */     return URL_FRAGMENT_ESCAPER;
/*     */   }
/*     */   
/* 146 */   private static final Escaper URL_FRAGMENT_ESCAPER = (Escaper)new PercentEscaper("-._~!$'()*,;&=@:+/?", false);
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\net\UrlEscapers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */