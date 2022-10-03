/*    */ package com.google.common.base;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import java.nio.charset.Charset;
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
/*    */ @GwtCompatible(emulated = true)
/*    */ public final class Charsets
/*    */ {
/*    */   @GwtIncompatible
/* 45 */   public static final Charset US_ASCII = Charset.forName("US-ASCII");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 54 */   public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 63 */   public static final Charset UTF_8 = Charset.forName("UTF-8");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @GwtIncompatible
/* 73 */   public static final Charset UTF_16BE = Charset.forName("UTF-16BE");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @GwtIncompatible
/* 83 */   public static final Charset UTF_16LE = Charset.forName("UTF-16LE");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @GwtIncompatible
/* 94 */   public static final Charset UTF_16 = Charset.forName("UTF-16");
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\base\Charsets.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */