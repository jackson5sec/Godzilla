/*    */ package com.google.common.html;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.escape.Escaper;
/*    */ import com.google.common.escape.Escapers;
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
/*    */ @GwtCompatible
/*    */ public final class HtmlEscapers
/*    */ {
/*    */   public static Escaper htmlEscaper() {
/* 52 */     return HTML_ESCAPER;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 59 */   private static final Escaper HTML_ESCAPER = Escapers.builder()
/* 60 */     .addEscape('"', "&quot;")
/*    */     
/* 62 */     .addEscape('\'', "&#39;")
/* 63 */     .addEscape('&', "&amp;")
/* 64 */     .addEscape('<', "&lt;")
/* 65 */     .addEscape('>', "&gt;")
/* 66 */     .build();
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\html\HtmlEscapers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */