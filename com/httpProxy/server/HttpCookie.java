/*    */ package com.httpProxy.server;
/*    */ 
/*    */ import com.httpProxy.server.response.HttpResponse;
/*    */ import com.httpProxy.server.response.HttpResponseHeader;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ public class HttpCookie
/*    */ {
/* 11 */   private HashMap<String, String> cookieMap = new HashMap<>();
/*    */ 
/*    */   
/*    */   public String addCookiie(String key, String value) {
/* 15 */     return this.cookieMap.put(key, value);
/*    */   }
/*    */   
/*    */   public String getCookie(String key) {
/* 19 */     return this.cookieMap.get(key);
/*    */   }
/*    */   public String removeCookie(String key) {
/* 22 */     return this.cookieMap.remove(key);
/*    */   }
/*    */   
/*    */   public static HttpCookie parse(HttpResponse httpResponse) {
/* 26 */     return parse(httpResponse.getHttpResponseHeader());
/*    */   }
/*    */   public static HttpCookie parse(HttpResponseHeader httpResponseHeader) {
/* 29 */     HttpCookie httpCookie = new HttpCookie();
/* 30 */     List<String[]> headers = httpResponseHeader.getHeaders();
/* 31 */     headers.forEach(v -> {
/*    */           if (v.length >= 2 && "set-cookie".equals(v[0].toLowerCase())) {
/*    */             String[] _cookie = v[1].split(";")[0].split("=");
/*    */             
/*    */             httpCookie.addCookiie(_cookie[0], _cookie[1]);
/*    */           } 
/*    */         });
/*    */     
/* 39 */     return httpCookie;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\httpProxy\server\HttpCookie.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */