/*     */ package org.springframework.util.xml;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.CharConversionException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XmlValidationModeDetector
/*     */ {
/*     */   public static final int VALIDATION_NONE = 0;
/*     */   public static final int VALIDATION_AUTO = 1;
/*     */   public static final int VALIDATION_DTD = 2;
/*     */   public static final int VALIDATION_XSD = 3;
/*     */   private static final String DOCTYPE = "DOCTYPE";
/*     */   private static final String START_COMMENT = "<!--";
/*     */   private static final String END_COMMENT = "-->";
/*     */   private boolean inComment;
/*     */   
/*     */   public int detectValidationMode(InputStream inputStream) throws IOException {
/*  93 */     try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
/*  94 */       boolean isDtdValidated = false;
/*     */       String content;
/*  96 */       while ((content = reader.readLine()) != null) {
/*  97 */         content = consumeCommentTokens(content);
/*  98 */         if (this.inComment || !StringUtils.hasText(content)) {
/*     */           continue;
/*     */         }
/* 101 */         if (hasDoctype(content)) {
/* 102 */           isDtdValidated = true;
/*     */           break;
/*     */         } 
/* 105 */         if (hasOpeningTag(content)) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */       
/* 110 */       return isDtdValidated ? 2 : 3;
/*     */     }
/* 112 */     catch (CharConversionException ex) {
/*     */ 
/*     */       
/* 115 */       return 1;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean hasDoctype(String content) {
/* 124 */     return content.contains("DOCTYPE");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean hasOpeningTag(String content) {
/* 133 */     if (this.inComment) {
/* 134 */       return false;
/*     */     }
/* 136 */     int openTagIndex = content.indexOf('<');
/* 137 */     return (openTagIndex > -1 && content.length() > openTagIndex + 1 && 
/* 138 */       Character.isLetter(content.charAt(openTagIndex + 1)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String consumeCommentTokens(String line) {
/* 148 */     int indexOfStartComment = line.indexOf("<!--");
/* 149 */     if (indexOfStartComment == -1 && !line.contains("-->")) {
/* 150 */       return line;
/*     */     }
/*     */     
/* 153 */     String result = "";
/* 154 */     String currLine = line;
/* 155 */     if (indexOfStartComment >= 0) {
/* 156 */       result = line.substring(0, indexOfStartComment);
/* 157 */       currLine = line.substring(indexOfStartComment);
/*     */     } 
/*     */     
/* 160 */     while ((currLine = consume(currLine)) != null) {
/* 161 */       if (!this.inComment && !currLine.trim().startsWith("<!--")) {
/* 162 */         return result + currLine;
/*     */       }
/*     */     } 
/* 165 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String consume(String line) {
/* 174 */     int index = this.inComment ? endComment(line) : startComment(line);
/* 175 */     return (index == -1) ? null : line.substring(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int startComment(String line) {
/* 183 */     return commentToken(line, "<!--", true);
/*     */   }
/*     */   
/*     */   private int endComment(String line) {
/* 187 */     return commentToken(line, "-->", false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int commentToken(String line, String token, boolean inCommentIfPresent) {
/* 196 */     int index = line.indexOf(token);
/* 197 */     if (index > -1) {
/* 198 */       this.inComment = inCommentIfPresent;
/*     */     }
/* 200 */     return (index == -1) ? index : (index + token.length());
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\xml\XmlValidationModeDetector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */