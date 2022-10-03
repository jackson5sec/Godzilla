/*    */ package org.mozilla.javascript.commonjs.module.provider;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.StringTokenizer;
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
/*    */ public final class ParsedContentType
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private final String contentType;
/*    */   private final String encoding;
/*    */   
/*    */   public ParsedContentType(String mimeType) {
/* 29 */     String contentType = null;
/* 30 */     String encoding = null;
/* 31 */     if (mimeType != null) {
/* 32 */       StringTokenizer tok = new StringTokenizer(mimeType, ";");
/* 33 */       if (tok.hasMoreTokens()) {
/* 34 */         contentType = tok.nextToken().trim();
/* 35 */         while (tok.hasMoreTokens()) {
/* 36 */           String param = tok.nextToken().trim();
/* 37 */           if (param.startsWith("charset=")) {
/* 38 */             encoding = param.substring(8).trim();
/* 39 */             int l = encoding.length();
/* 40 */             if (l > 0) {
/* 41 */               if (encoding.charAt(0) == '"') {
/* 42 */                 encoding = encoding.substring(1);
/*    */               }
/* 44 */               if (encoding.charAt(l - 1) == '"') {
/* 45 */                 encoding = encoding.substring(0, l - 1);
/*    */               }
/*    */             } 
/*    */             break;
/*    */           } 
/*    */         } 
/*    */       } 
/*    */     } 
/* 53 */     this.contentType = contentType;
/* 54 */     this.encoding = encoding;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getContentType() {
/* 63 */     return this.contentType;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getEncoding() {
/* 72 */     return this.encoding;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\commonjs\module\provider\ParsedContentType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */