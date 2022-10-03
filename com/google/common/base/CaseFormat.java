/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.io.Serializable;
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
/*     */ public enum CaseFormat
/*     */ {
/*  33 */   LOWER_HYPHEN(CharMatcher.is('-'), "-")
/*     */   {
/*     */     String normalizeWord(String word) {
/*  36 */       return Ascii.toLowerCase(word);
/*     */     }
/*     */ 
/*     */     
/*     */     String convert(CaseFormat format, String s) {
/*  41 */       if (format == LOWER_UNDERSCORE) {
/*  42 */         return s.replace('-', '_');
/*     */       }
/*  44 */       if (format == UPPER_UNDERSCORE) {
/*  45 */         return Ascii.toUpperCase(s.replace('-', '_'));
/*     */       }
/*  47 */       return super.convert(format, s);
/*     */     }
/*     */   },
/*     */ 
/*     */   
/*  52 */   LOWER_UNDERSCORE(CharMatcher.is('_'), "_")
/*     */   {
/*     */     String normalizeWord(String word) {
/*  55 */       return Ascii.toLowerCase(word);
/*     */     }
/*     */ 
/*     */     
/*     */     String convert(CaseFormat format, String s) {
/*  60 */       if (format == LOWER_HYPHEN) {
/*  61 */         return s.replace('_', '-');
/*     */       }
/*  63 */       if (format == UPPER_UNDERSCORE) {
/*  64 */         return Ascii.toUpperCase(s);
/*     */       }
/*  66 */       return super.convert(format, s);
/*     */     }
/*     */   },
/*     */ 
/*     */   
/*  71 */   LOWER_CAMEL(CharMatcher.inRange('A', 'Z'), "")
/*     */   {
/*     */     String normalizeWord(String word) {
/*  74 */       return firstCharOnlyToUpper(word);
/*     */     }
/*     */   },
/*     */ 
/*     */   
/*  79 */   UPPER_CAMEL(CharMatcher.inRange('A', 'Z'), "")
/*     */   {
/*     */     String normalizeWord(String word) {
/*  82 */       return firstCharOnlyToUpper(word);
/*     */     }
/*     */   },
/*     */ 
/*     */   
/*  87 */   UPPER_UNDERSCORE(CharMatcher.is('_'), "_")
/*     */   {
/*     */     String normalizeWord(String word) {
/*  90 */       return Ascii.toUpperCase(word);
/*     */     }
/*     */ 
/*     */     
/*     */     String convert(CaseFormat format, String s) {
/*  95 */       if (format == LOWER_HYPHEN) {
/*  96 */         return Ascii.toLowerCase(s.replace('_', '-'));
/*     */       }
/*  98 */       if (format == LOWER_UNDERSCORE) {
/*  99 */         return Ascii.toLowerCase(s);
/*     */       }
/* 101 */       return super.convert(format, s);
/*     */     }
/*     */   };
/*     */   
/*     */   private final CharMatcher wordBoundary;
/*     */   private final String wordSeparator;
/*     */   
/*     */   CaseFormat(CharMatcher wordBoundary, String wordSeparator) {
/* 109 */     this.wordBoundary = wordBoundary;
/* 110 */     this.wordSeparator = wordSeparator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String to(CaseFormat format, String str) {
/* 119 */     Preconditions.checkNotNull(format);
/* 120 */     Preconditions.checkNotNull(str);
/* 121 */     return (format == this) ? str : convert(format, str);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   String convert(CaseFormat format, String s) {
/* 127 */     StringBuilder out = null;
/* 128 */     int i = 0;
/* 129 */     int j = -1;
/* 130 */     while ((j = this.wordBoundary.indexIn(s, ++j)) != -1) {
/* 131 */       if (i == 0) {
/*     */         
/* 133 */         out = new StringBuilder(s.length() + 4 * this.wordSeparator.length());
/* 134 */         out.append(format.normalizeFirstWord(s.substring(i, j)));
/*     */       } else {
/* 136 */         out.append(format.normalizeWord(s.substring(i, j)));
/*     */       } 
/* 138 */       out.append(format.wordSeparator);
/* 139 */       i = j + this.wordSeparator.length();
/*     */     } 
/* 141 */     return (i == 0) ? format
/* 142 */       .normalizeFirstWord(s) : out
/* 143 */       .append(format.normalizeWord(s.substring(i))).toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Converter<String, String> converterTo(CaseFormat targetFormat) {
/* 152 */     return new StringConverter(this, targetFormat);
/*     */   }
/*     */   
/*     */   private static final class StringConverter
/*     */     extends Converter<String, String> implements Serializable {
/*     */     private final CaseFormat sourceFormat;
/*     */     private final CaseFormat targetFormat;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     StringConverter(CaseFormat sourceFormat, CaseFormat targetFormat) {
/* 162 */       this.sourceFormat = Preconditions.<CaseFormat>checkNotNull(sourceFormat);
/* 163 */       this.targetFormat = Preconditions.<CaseFormat>checkNotNull(targetFormat);
/*     */     }
/*     */ 
/*     */     
/*     */     protected String doForward(String s) {
/* 168 */       return this.sourceFormat.to(this.targetFormat, s);
/*     */     }
/*     */ 
/*     */     
/*     */     protected String doBackward(String s) {
/* 173 */       return this.targetFormat.to(this.sourceFormat, s);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object object) {
/* 178 */       if (object instanceof StringConverter) {
/* 179 */         StringConverter that = (StringConverter)object;
/* 180 */         return (this.sourceFormat.equals(that.sourceFormat) && this.targetFormat.equals(that.targetFormat));
/*     */       } 
/* 182 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 187 */       return this.sourceFormat.hashCode() ^ this.targetFormat.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 192 */       return this.sourceFormat + ".converterTo(" + this.targetFormat + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String normalizeFirstWord(String word) {
/* 201 */     return (this == LOWER_CAMEL) ? Ascii.toLowerCase(word) : normalizeWord(word);
/*     */   }
/*     */   
/*     */   private static String firstCharOnlyToUpper(String word) {
/* 205 */     return word.isEmpty() ? word : (
/*     */       
/* 207 */       Ascii.toUpperCase(word.charAt(0)) + Ascii.toLowerCase(word.substring(1)));
/*     */   }
/*     */   
/*     */   abstract String normalizeWord(String paramString);
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\base\CaseFormat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */