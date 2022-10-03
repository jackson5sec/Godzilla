/*    */ package com.httpProxy.server.response;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ public class HttpResponseHeader
/*    */ {
/* 10 */   ArrayList<String[]> headers = (ArrayList)new ArrayList<>();
/*    */   
/*    */   public HttpResponseHeader addHeader(String name, String value) {
/* 13 */     String[] header = new String[2];
/* 14 */     header[0] = (name == null) ? "" : name.trim();
/* 15 */     header[1] = (value == null) ? "" : value.trim();
/*    */     
/* 17 */     if (name != null) {
/* 18 */       this.headers.add(header);
/*    */     }
/* 20 */     return this;
/*    */   }
/*    */   
/*    */   public HttpResponseHeader setHeader(String name, String value) {
/* 24 */     Iterator<String[]> headers = (Iterator)getHeaders().iterator();
/* 25 */     String[] kv = null;
/* 26 */     while (headers.hasNext()) {
/* 27 */       kv = headers.next();
/* 28 */       if (kv[0].equals(name)) {
/*    */         break;
/*    */       }
/* 31 */       kv = null;
/*    */     } 
/* 33 */     if (kv != null) {
/* 34 */       getHeaders().remove(kv);
/*    */     }
/* 36 */     addHeader(name, value);
/*    */     
/* 38 */     return this;
/*    */   }
/*    */   
/*    */   public HttpResponseHeader removeHeader(String name) {
/* 42 */     List<String[]> removeList = (List)new ArrayList<>();
/* 43 */     Iterator<String[]> headers = (Iterator)getHeaders().iterator();
/* 44 */     String[] kv = null;
/* 45 */     while (headers.hasNext()) {
/* 46 */       kv = headers.next();
/* 47 */       if (kv[0].equals(name)) {
/* 48 */         removeList.add(kv);
/*    */       }
/* 50 */       kv = null;
/*    */     } 
/* 52 */     for (int i = 0; i < removeList.size(); i++) {
/* 53 */       String[] s = removeList.get(i);
/* 54 */       getHeaders().remove(s);
/*    */     } 
/*    */     
/* 57 */     return this;
/*    */   }
/*    */   public String decode() {
/* 60 */     StringBuilder stringBuilder = new StringBuilder();
/* 61 */     Iterator<String> iterator = this.headers.iterator();
/* 62 */     while (iterator.hasNext()) {
/* 63 */       String[] ex = (String[])iterator.next();
/* 64 */       stringBuilder.append(ex[0]);
/* 65 */       stringBuilder.append(": ");
/* 66 */       stringBuilder.append(ex[1]);
/* 67 */       stringBuilder.append("\r\n");
/*    */     } 
/* 69 */     return stringBuilder.toString();
/*    */   }
/*    */   public List<String[]> getHeaders() {
/* 72 */     return (List<String[]>)this.headers;
/*    */   }
/*    */   
/*    */   public String[] getHeaderValue(String headerKey) {
/* 76 */     return (String[])this.headers.stream().filter(v -> 
/* 77 */         (v.length == 2 && headerKey.equals(v[0])))
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 83 */       .toArray(x$0 -> new String[x$0]);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 88 */     return "HttpResponseHeader{headers=" + this.headers + '}';
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\httpProxy\server\response\HttpResponseHeader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */