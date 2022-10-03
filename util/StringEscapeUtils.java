/*     */ package util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.StringWriter;
/*     */ import java.io.Writer;
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
/*     */ public class StringEscapeUtils
/*     */ {
/*     */   public static final char LF = '\n';
/*     */   public static final char CR = '\r';
/*     */   private static final char CSV_DELIMITER = ',';
/*     */   private static final char CSV_QUOTE = '"';
/*  30 */   private static final String CSV_QUOTE_STR = String.valueOf('"');
/*  31 */   private static final char[] CSV_SEARCH_CHARS = new char[] { ',', '"', '\r', '\n' };
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
/*     */   public static String escapeJava(String str) {
/*  71 */     return escapeJavaStyleString(str, false);
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
/*     */   public static void escapeJava(Writer out, String str) throws IOException {
/*  87 */     escapeJavaStyleString(out, str, false);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String escapeJavaScript(String str) {
/* 112 */     return escapeJavaStyleString(str, true);
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
/*     */   public static void escapeJavaScript(Writer out, String str) throws IOException {
/* 128 */     escapeJavaStyleString(out, str, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String escapeJavaStyleString(String str, boolean escapeSingleQuotes) {
/* 139 */     if (str == null) {
/* 140 */       return null;
/*     */     }
/*     */     try {
/* 143 */       StringWriter writer = new StringWriter(str.length() * 2);
/* 144 */       escapeJavaStyleString(writer, str, escapeSingleQuotes);
/* 145 */       return writer.toString();
/* 146 */     } catch (IOException ioe) {
/*     */       
/* 148 */       ioe.printStackTrace();
/* 149 */       return null;
/*     */     } 
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
/*     */   private static void escapeJavaStyleString(Writer out, String str, boolean escapeSingleQuote) throws IOException {
/* 162 */     if (out == null) {
/* 163 */       throw new IllegalArgumentException("The Writer must not be null");
/*     */     }
/* 165 */     if (str == null) {
/*     */       return;
/*     */     }
/*     */     
/* 169 */     int sz = str.length();
/* 170 */     for (int i = 0; i < sz; i++) {
/* 171 */       char ch = str.charAt(i);
/*     */ 
/*     */       
/* 174 */       if (ch > '࿿') {
/* 175 */         out.write("\\u" + hex(ch));
/* 176 */       } else if (ch > 'ÿ') {
/* 177 */         out.write("\\u0" + hex(ch));
/* 178 */       } else if (ch > '') {
/* 179 */         out.write("\\u00" + hex(ch));
/* 180 */       } else if (ch < ' ') {
/* 181 */         switch (ch) {
/*     */           case '\b':
/* 183 */             out.write(92);
/* 184 */             out.write(98);
/*     */             break;
/*     */           case '\n':
/* 187 */             out.write(92);
/* 188 */             out.write(110);
/*     */             break;
/*     */           case '\t':
/* 191 */             out.write(92);
/* 192 */             out.write(116);
/*     */             break;
/*     */           case '\f':
/* 195 */             out.write(92);
/* 196 */             out.write(102);
/*     */             break;
/*     */           case '\r':
/* 199 */             out.write(92);
/* 200 */             out.write(114);
/*     */             break;
/*     */           default:
/* 203 */             if (ch > '\017') {
/* 204 */               out.write("\\u00" + hex(ch)); break;
/*     */             } 
/* 206 */             out.write("\\u000" + hex(ch));
/*     */             break;
/*     */         } 
/*     */       
/*     */       } else {
/* 211 */         switch (ch) {
/*     */           case '\'':
/* 213 */             if (escapeSingleQuote) {
/* 214 */               out.write(92);
/*     */             }
/* 216 */             out.write(39);
/*     */             break;
/*     */           case '"':
/* 219 */             out.write(92);
/* 220 */             out.write(34);
/*     */             break;
/*     */           case '\\':
/* 223 */             out.write(92);
/* 224 */             out.write(92);
/*     */             break;
/*     */           case '/':
/* 227 */             out.write(92);
/* 228 */             out.write(47);
/*     */             break;
/*     */           default:
/* 231 */             out.write(ch);
/*     */             break;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String hex(char ch) {
/* 246 */     return Integer.toHexString(ch).toUpperCase();
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
/*     */   public static String unescapeJava(String str) {
/* 259 */     if (str == null) {
/* 260 */       return null;
/*     */     }
/*     */     try {
/* 263 */       StringWriter writer = new StringWriter(str.length());
/* 264 */       unescapeJava(writer, str);
/* 265 */       return writer.toString();
/* 266 */     } catch (IOException ioe) {
/*     */       
/* 268 */       ioe.printStackTrace();
/* 269 */       return null;
/*     */     } 
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
/*     */   
/*     */   public static void unescapeJava(Writer out, String str) throws IOException {
/* 289 */     if (out == null) {
/* 290 */       throw new IllegalArgumentException("The Writer must not be null");
/*     */     }
/* 292 */     if (str == null) {
/*     */       return;
/*     */     }
/* 295 */     int sz = str.length();
/* 296 */     StringBuffer unicode = new StringBuffer(4);
/* 297 */     boolean hadSlash = false;
/* 298 */     boolean inUnicode = false;
/* 299 */     for (int i = 0; i < sz; i++) {
/* 300 */       char ch = str.charAt(i);
/* 301 */       if (inUnicode) {
/*     */ 
/*     */         
/* 304 */         unicode.append(ch);
/* 305 */         if (unicode.length() == 4) {
/*     */           
/*     */           try {
/*     */             
/* 309 */             int value = Integer.parseInt(unicode.toString(), 16);
/* 310 */             out.write((char)value);
/* 311 */             unicode.setLength(0);
/* 312 */             inUnicode = false;
/* 313 */             hadSlash = false;
/* 314 */           } catch (NumberFormatException nfe) {
/* 315 */             throw new RuntimeException("Unable to parse unicode value: " + unicode, nfe);
/*     */           }
/*     */         
/*     */         }
/*     */       }
/* 320 */       else if (hadSlash) {
/*     */         
/* 322 */         hadSlash = false;
/* 323 */         switch (ch) {
/*     */           case '\\':
/* 325 */             out.write(92);
/*     */             break;
/*     */           case '\'':
/* 328 */             out.write(39);
/*     */             break;
/*     */           case '"':
/* 331 */             out.write(34);
/*     */             break;
/*     */           case 'r':
/* 334 */             out.write(13);
/*     */             break;
/*     */           case 'f':
/* 337 */             out.write(12);
/*     */             break;
/*     */           case 't':
/* 340 */             out.write(9);
/*     */             break;
/*     */           case 'n':
/* 343 */             out.write(10);
/*     */             break;
/*     */           case 'b':
/* 346 */             out.write(8);
/*     */             break;
/*     */ 
/*     */           
/*     */           case 'u':
/* 351 */             inUnicode = true;
/*     */             break;
/*     */           
/*     */           default:
/* 355 */             out.write(ch);
/*     */             break;
/*     */         } 
/*     */       
/* 359 */       } else if (ch == '\\') {
/* 360 */         hadSlash = true;
/*     */       } else {
/*     */         
/* 363 */         out.write(ch);
/*     */       } 
/* 365 */     }  if (hadSlash)
/*     */     {
/*     */       
/* 368 */       out.write(92);
/*     */     }
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
/*     */   public static String unescapeJavaScript(String str) {
/* 384 */     return unescapeJava(str);
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
/*     */ 
/*     */   
/*     */   public static void unescapeJavaScript(Writer out, String str) throws IOException {
/* 404 */     unescapeJava(out, str);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar\\util\StringEscapeUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */