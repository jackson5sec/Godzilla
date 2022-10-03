/*     */ package org.fife.ui.rsyntaxtextarea;
/*     */ 
/*     */ import javax.swing.text.Segment;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TokenMap
/*     */ {
/*     */   private int size;
/*     */   private TokenMapToken[] tokenMap;
/*     */   private boolean ignoreCase;
/*     */   private static final int DEFAULT_TOKEN_MAP_SIZE = 52;
/*     */   
/*     */   public TokenMap() {
/*  44 */     this(52);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TokenMap(int size) {
/*  54 */     this(size, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TokenMap(boolean ignoreCase) {
/*  65 */     this(52, ignoreCase);
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
/*     */   public TokenMap(int size, boolean ignoreCase) {
/*  77 */     this.size = size;
/*  78 */     this.tokenMap = new TokenMapToken[size];
/*  79 */     this.ignoreCase = ignoreCase;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addTokenToBucket(int bucket, TokenMapToken token) {
/*  90 */     TokenMapToken old = this.tokenMap[bucket];
/*  91 */     token.nextToken = old;
/*  92 */     this.tokenMap[bucket] = token;
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
/*     */   public int get(Segment text, int start, int end) {
/* 107 */     return get(text.array, start, end);
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
/*     */   public int get(char[] array1, int start, int end) {
/* 123 */     int length1 = end - start + 1;
/*     */     
/* 125 */     int hash = getHashCode(array1, start, length1);
/* 126 */     TokenMapToken token = this.tokenMap[hash];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 139 */     if (!this.ignoreCase) {
/*     */ 
/*     */       
/* 142 */       while (token != null) {
/* 143 */         if (token.length == length1) {
/* 144 */           char[] array2 = token.text;
/* 145 */           int offset2 = token.offset;
/* 146 */           int offset1 = start;
/* 147 */           int length = length1;
/* 148 */           while (length-- > 0) {
/* 149 */             if (array1[offset1++] != array2[offset2++]) {
/* 150 */               token = token.nextToken;
/*     */             }
/*     */           } 
/*     */           
/* 154 */           return token.tokenType;
/*     */         } 
/* 156 */         token = token.nextToken;
/*     */ 
/*     */       
/*     */       }
/*     */ 
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 167 */       while (token != null) {
/* 168 */         if (token.length == length1) {
/* 169 */           char[] array2 = token.text;
/* 170 */           int offset2 = token.offset;
/* 171 */           int offset1 = start;
/* 172 */           int length = length1;
/* 173 */           while (length-- > 0) {
/* 174 */             if (RSyntaxUtilities.toLowerCase(array1[offset1++]) != array2[offset2++])
/*     */             {
/* 176 */               token = token.nextToken;
/*     */             }
/*     */           } 
/*     */           
/* 180 */           return token.tokenType;
/*     */         } 
/* 182 */         token = token.nextToken;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 188 */     return -1;
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
/*     */   private int getHashCode(char[] text, int offset, int length) {
/* 202 */     return (RSyntaxUtilities.toLowerCase(text[offset]) + 
/* 203 */       RSyntaxUtilities.toLowerCase(text[offset + length - 1])) % this.size;
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
/*     */   protected boolean isIgnoringCase() {
/* 215 */     return this.ignoreCase;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void put(String string, int tokenType) {
/* 226 */     if (isIgnoringCase()) {
/* 227 */       put(string.toLowerCase().toCharArray(), tokenType);
/*     */     } else {
/*     */       
/* 230 */       put(string.toCharArray(), tokenType);
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
/*     */   private void put(char[] string, int tokenType) {
/* 246 */     int hashCode = getHashCode(string, 0, string.length);
/* 247 */     addTokenToBucket(hashCode, new TokenMapToken(string, tokenType));
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class TokenMapToken
/*     */   {
/*     */     private char[] text;
/*     */     
/*     */     private int offset;
/*     */     
/*     */     private int length;
/*     */     
/*     */     private int tokenType;
/*     */     
/*     */     private TokenMapToken nextToken;
/*     */ 
/*     */     
/*     */     private TokenMapToken(char[] text, int tokenType) {
/* 265 */       this.text = text;
/* 266 */       this.offset = 0;
/* 267 */       this.length = text.length;
/* 268 */       this.tokenType = tokenType;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 273 */       return "[TokenMapToken: " + new String(this.text, this.offset, this.length) + "]";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\TokenMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */