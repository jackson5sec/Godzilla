/*     */ package org.fife.ui.rsyntaxtextarea;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class RtfToText
/*     */ {
/*     */   private Reader r;
/*     */   private StringBuilder sb;
/*     */   private StringBuilder controlWord;
/*     */   private int blockCount;
/*     */   private boolean inControlWord;
/*     */   
/*     */   private RtfToText(Reader r) {
/*  47 */     this.r = r;
/*  48 */     this.sb = new StringBuilder();
/*  49 */     this.controlWord = new StringBuilder();
/*  50 */     this.blockCount = 0;
/*  51 */     this.inControlWord = false;
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
/*     */   private String convert() throws IOException {
/*  66 */     int i = this.r.read();
/*  67 */     if (i != 123) {
/*  68 */       throw new IOException("Invalid RTF file");
/*     */     }
/*     */     
/*  71 */     while ((i = this.r.read()) != -1) {
/*     */       
/*  73 */       char ch = (char)i;
/*  74 */       switch (ch) {
/*     */         case '{':
/*  76 */           if (this.inControlWord && this.controlWord.length() == 0) {
/*  77 */             this.sb.append('{');
/*  78 */             this.controlWord.setLength(0);
/*  79 */             this.inControlWord = false;
/*     */             continue;
/*     */           } 
/*  82 */           this.blockCount++;
/*     */           continue;
/*     */         
/*     */         case '}':
/*  86 */           if (this.inControlWord && this.controlWord.length() == 0) {
/*  87 */             this.sb.append('}');
/*  88 */             this.controlWord.setLength(0);
/*  89 */             this.inControlWord = false;
/*     */             continue;
/*     */           } 
/*  92 */           this.blockCount--;
/*     */           continue;
/*     */         
/*     */         case '\\':
/*  96 */           if (this.blockCount == 0) {
/*  97 */             if (this.inControlWord) {
/*  98 */               if (this.controlWord.length() == 0) {
/*  99 */                 this.sb.append('\\');
/* 100 */                 this.controlWord.setLength(0);
/* 101 */                 this.inControlWord = false;
/*     */                 continue;
/*     */               } 
/* 104 */               endControlWord();
/* 105 */               this.inControlWord = true;
/*     */               
/*     */               continue;
/*     */             } 
/* 109 */             this.inControlWord = true;
/*     */           } 
/*     */           continue;
/*     */         
/*     */         case ' ':
/* 114 */           if (this.blockCount == 0) {
/* 115 */             if (this.inControlWord) {
/* 116 */               endControlWord();
/*     */               continue;
/*     */             } 
/* 119 */             this.sb.append(' ');
/*     */           } 
/*     */           continue;
/*     */         
/*     */         case '\n':
/*     */         case '\r':
/* 125 */           if (this.blockCount == 0 && 
/* 126 */             this.inControlWord) {
/* 127 */             endControlWord();
/*     */           }
/*     */           continue;
/*     */       } 
/*     */ 
/*     */       
/* 133 */       if (this.blockCount == 0) {
/* 134 */         if (this.inControlWord) {
/* 135 */           this.controlWord.append(ch);
/*     */           continue;
/*     */         } 
/* 138 */         this.sb.append(ch);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 146 */     return this.sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void endControlWord() {
/* 157 */     String word = this.controlWord.toString();
/* 158 */     if ("par".equals(word) || "line".equals(word)) {
/* 159 */       this.sb.append('\n');
/*     */     }
/* 161 */     else if ("tab".equals(word)) {
/* 162 */       this.sb.append('\t');
/*     */     }
/* 164 */     else if (isUnicodeEscape(word)) {
/* 165 */       this.sb.append((char)Integer.valueOf(word.substring(1)).intValue());
/*     */     } 
/* 167 */     this.controlWord.setLength(0);
/* 168 */     this.inControlWord = false;
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isUnicodeEscape(String controlWord) {
/* 173 */     if (controlWord.startsWith("u") && controlWord.length() > 1) {
/* 174 */       for (int i = 1; i < controlWord.length(); i++) {
/* 175 */         char ch = controlWord.charAt(i);
/* 176 */         if (ch < '0' || ch > '9') {
/* 177 */           return false;
/*     */         }
/*     */       } 
/* 180 */       return true;
/*     */     } 
/* 182 */     return false;
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
/*     */   public static String getPlainText(byte[] rtf) throws IOException {
/* 195 */     return getPlainText(new ByteArrayInputStream(rtf));
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
/*     */   public static String getPlainText(File file) throws IOException {
/* 207 */     return getPlainText(new BufferedReader(new FileReader(file)));
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
/*     */   public static String getPlainText(InputStream in) throws IOException {
/* 221 */     return getPlainText(new InputStreamReader(in, StandardCharsets.US_ASCII));
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
/*     */   private static String getPlainText(Reader r) throws IOException {
/*     */     try {
/* 235 */       RtfToText converter = new RtfToText(r);
/* 236 */       return converter.convert();
/*     */     } finally {
/* 238 */       r.close();
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
/*     */   public static String getPlainText(String rtf) throws IOException {
/* 251 */     return getPlainText(new StringReader(rtf));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\RtfToText.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */