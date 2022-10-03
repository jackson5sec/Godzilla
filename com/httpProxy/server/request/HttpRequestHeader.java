/*    */ package com.httpProxy.server.request;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ 
/*    */ public class HttpRequestHeader {
/*  8 */   ArrayList<String[]> headers = (ArrayList)new ArrayList<>();
/*    */   
/*    */   public HttpRequestHeader addHeader(String name, String value) {
/* 11 */     String[] header = new String[2];
/* 12 */     header[0] = (name == null) ? "" : name.trim();
/* 13 */     header[1] = (value == null) ? "" : value.trim();
/*    */     
/* 15 */     if (name != null) {
/* 16 */       this.headers.add(header);
/*    */     }
/* 18 */     return this;
/*    */   }
/*    */   
/*    */   public HttpRequestHeader setHeader(String name, String value) {
/* 22 */     Iterator<String[]> headers = (Iterator)getHeaders().iterator();
/* 23 */     String[] kv = null;
/* 24 */     while (headers.hasNext()) {
/* 25 */       kv = headers.next();
/* 26 */       if (kv[0].equals(name)) {
/*    */         break;
/*    */       }
/* 29 */       kv = null;
/*    */     } 
/* 31 */     if (kv != null) {
/* 32 */       getHeaders().remove(kv);
/*    */     }
/* 34 */     addHeader(name, value);
/*    */     
/* 36 */     return this;
/*    */   }
/*    */   
/*    */   public String getHeader(String key) {
/* 40 */     Iterator<String[]> headers = (Iterator)getHeaders().iterator();
/* 41 */     String[] kv = null;
/* 42 */     while (headers.hasNext()) {
/* 43 */       kv = headers.next();
/* 44 */       if (kv[0].equals(key)) {
/*    */         break;
/*    */       }
/* 47 */       kv = null;
/*    */     } 
/* 49 */     if (kv != null) {
/* 50 */       return kv[1];
/*    */     }
/* 52 */     return null;
/*    */   }
/*    */   
/*    */   public HttpRequestHeader removeHeader(String name) {
/* 56 */     List<String[]> removeList = (List)new ArrayList<>();
/* 57 */     Iterator<String[]> headers = (Iterator)getHeaders().iterator();
/* 58 */     String[] kv = null;
/* 59 */     while (headers.hasNext()) {
/* 60 */       kv = headers.next();
/* 61 */       if (kv[0].equals(name)) {
/* 62 */         removeList.add(kv);
/*    */       }
/* 64 */       kv = null;
/*    */     } 
/* 66 */     for (int i = 0; i < removeList.size(); i++) {
/* 67 */       String[] s = removeList.get(i);
/* 68 */       getHeaders().remove(s);
/*    */     } 
/*    */     
/* 71 */     return this;
/*    */   }
/*    */   
/*    */   public HttpRequestHeader setContentType(String value) {
/* 75 */     return setHeader("Content-Type", value);
/*    */   }
/*    */   
/*    */   public String decode() {
/* 79 */     StringBuilder stringBuilder = new StringBuilder();
/* 80 */     Iterator<String> iterator = this.headers.iterator();
/* 81 */     while (iterator.hasNext()) {
/* 82 */       String[] ex = (String[])iterator.next();
/* 83 */       stringBuilder.append(ex[0]);
/* 84 */       stringBuilder.append(": ");
/* 85 */       stringBuilder.append(ex[1]);
/* 86 */       stringBuilder.append("\r\n");
/*    */     } 
/* 88 */     return stringBuilder.toString();
/*    */   }
/*    */   
/*    */   public List<String[]> getHeaders() {
/* 92 */     return (List<String[]>)this.headers;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 97 */     return "HttpResponseHeader{headers=" + this.headers + '}';
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\httpProxy\server\request\HttpRequestHeader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */