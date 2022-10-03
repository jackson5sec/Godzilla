/*     */ package org.sqlite.date;
/*     */ 
/*     */ import java.text.DateFormat;
/*     */ import java.text.Format;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Arrays;
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class FormatCache<F extends Format>
/*     */ {
/*     */   static final int NONE = -1;
/*  41 */   private final ConcurrentMap<MultipartKey, F> cInstanceCache = new ConcurrentHashMap<>(7);
/*     */ 
/*     */   
/*  44 */   private static final ConcurrentMap<MultipartKey, String> cDateTimeInstanceCache = new ConcurrentHashMap<>(7);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public F getInstance() {
/*  54 */     return getDateTimeInstance(3, 3, TimeZone.getDefault(), Locale.getDefault());
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
/*     */   public F getInstance(String pattern, TimeZone timeZone, Locale locale) {
/*  70 */     if (pattern == null) {
/*  71 */       throw new NullPointerException("pattern must not be null");
/*     */     }
/*  73 */     if (timeZone == null) {
/*  74 */       timeZone = TimeZone.getDefault();
/*     */     }
/*  76 */     if (locale == null) {
/*  77 */       locale = Locale.getDefault();
/*     */     }
/*  79 */     MultipartKey key = new MultipartKey(new Object[] { pattern, timeZone, locale });
/*  80 */     Format format = (Format)this.cInstanceCache.get(key);
/*  81 */     if (format == null) {
/*  82 */       format = (Format)createInstance(pattern, timeZone, locale);
/*  83 */       Format format1 = (Format)this.cInstanceCache.putIfAbsent(key, (F)format);
/*  84 */       if (format1 != null)
/*     */       {
/*     */         
/*  87 */         format = format1;
/*     */       }
/*     */     } 
/*  90 */     return (F)format;
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
/*     */   protected abstract F createInstance(String paramString, TimeZone paramTimeZone, Locale paramLocale);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private F getDateTimeInstance(Integer dateStyle, Integer timeStyle, TimeZone timeZone, Locale locale) {
/* 121 */     if (locale == null) {
/* 122 */       locale = Locale.getDefault();
/*     */     }
/* 124 */     String pattern = getPatternForStyle(dateStyle, timeStyle, locale);
/* 125 */     return getInstance(pattern, timeZone, locale);
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
/*     */   F getDateTimeInstance(int dateStyle, int timeStyle, TimeZone timeZone, Locale locale) {
/* 143 */     return getDateTimeInstance(Integer.valueOf(dateStyle), Integer.valueOf(timeStyle), timeZone, locale);
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
/*     */   F getDateInstance(int dateStyle, TimeZone timeZone, Locale locale) {
/* 160 */     return getDateTimeInstance(Integer.valueOf(dateStyle), (Integer)null, timeZone, locale);
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
/*     */   F getTimeInstance(int timeStyle, TimeZone timeZone, Locale locale) {
/* 177 */     return getDateTimeInstance((Integer)null, Integer.valueOf(timeStyle), timeZone, locale);
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
/*     */   static String getPatternForStyle(Integer dateStyle, Integer timeStyle, Locale locale) {
/* 191 */     MultipartKey key = new MultipartKey(new Object[] { dateStyle, timeStyle, locale });
/*     */     
/* 193 */     String pattern = cDateTimeInstanceCache.get(key);
/* 194 */     if (pattern == null) {
/*     */       try {
/*     */         DateFormat formatter;
/* 197 */         if (dateStyle == null) {
/* 198 */           formatter = DateFormat.getTimeInstance(timeStyle.intValue(), locale);
/*     */         }
/* 200 */         else if (timeStyle == null) {
/* 201 */           formatter = DateFormat.getDateInstance(dateStyle.intValue(), locale);
/*     */         } else {
/*     */           
/* 204 */           formatter = DateFormat.getDateTimeInstance(dateStyle.intValue(), timeStyle.intValue(), locale);
/*     */         } 
/* 206 */         pattern = ((SimpleDateFormat)formatter).toPattern();
/* 207 */         String previous = cDateTimeInstanceCache.putIfAbsent(key, pattern);
/* 208 */         if (previous != null)
/*     */         {
/*     */ 
/*     */           
/* 212 */           pattern = previous;
/*     */         }
/* 214 */       } catch (ClassCastException ex) {
/* 215 */         throw new IllegalArgumentException("No date time pattern for locale: " + locale);
/*     */       } 
/*     */     }
/* 218 */     return pattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class MultipartKey
/*     */   {
/*     */     private final Object[] keys;
/*     */ 
/*     */     
/*     */     private int hashCode;
/*     */ 
/*     */ 
/*     */     
/*     */     public MultipartKey(Object... keys) {
/* 234 */       this.keys = keys;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 245 */       return Arrays.equals(this.keys, ((MultipartKey)obj).keys);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 253 */       if (this.hashCode == 0) {
/* 254 */         int rc = 0;
/* 255 */         for (Object key : this.keys) {
/* 256 */           if (key != null) {
/* 257 */             rc = rc * 7 + key.hashCode();
/*     */           }
/*     */         } 
/* 260 */         this.hashCode = rc;
/*     */       } 
/* 262 */       return this.hashCode;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\sqlite\date\FormatCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */