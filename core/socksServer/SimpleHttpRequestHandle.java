/*    */ package core.socksServer;
/*    */ 
/*    */ import com.httpProxy.server.request.HttpRequest;
/*    */ import com.httpProxy.server.response.HttpResponse;
/*    */ import com.httpProxy.server.response.HttpResponseStatus;
/*    */ import java.net.HttpURLConnection;
/*    */ import java.net.URL;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import util.functions;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SimpleHttpRequestHandle
/*    */   implements HttpRequestHandle
/*    */ {
/*    */   public HttpResponse sendHttpRequest(HttpRequest httpRequest) {
/* 20 */     HttpResponse ret = null;
/*    */     try {
/* 22 */       HttpURLConnection httpURLConnection = (HttpURLConnection)(new URL(httpRequest.getUrl())).openConnection();
/*    */       
/* 24 */       httpURLConnection.setRequestMethod(httpRequest.getMethod());
/* 25 */       httpURLConnection.setDoInput(true);
/* 26 */       httpURLConnection.setDoOutput(true);
/* 27 */       List<String[]> headers = httpRequest.getHttpRequestHeader().getHeaders();
/* 28 */       for (int i = 0; i < headers.size(); i++) {
/* 29 */         String[] hk = headers.get(i);
/* 30 */         httpURLConnection.setRequestProperty(hk[0], hk[1]);
/*    */       } 
/* 32 */       httpURLConnection.getOutputStream().write(httpRequest.getRequestData());
/* 33 */       httpURLConnection.getOutputStream().flush();
/* 34 */       HttpResponse httpResponse = new HttpResponse(new HttpResponseStatus(httpURLConnection.getResponseCode(), httpURLConnection.getResponseMessage()));
/* 35 */       Map<String, List<String>> headerFields = httpURLConnection.getHeaderFields();
/* 36 */       Iterator<String> iterator = headerFields.keySet().iterator();
/* 37 */       while (iterator.hasNext()) {
/* 38 */         String next = iterator.next();
/* 39 */         if (next != null) {
/* 40 */           List<String> values = headerFields.get(next);
/* 41 */           for (int j = 0; j < values.size(); j++) {
/* 42 */             String v = values.get(j);
/* 43 */             httpResponse.getHttpResponseHeader().addHeader(next, v);
/*    */           } 
/*    */         } 
/*    */       } 
/*    */       
/* 48 */       httpResponse.setResponseData(functions.readInputStream(httpURLConnection.getInputStream()));
/* 49 */       ret = httpResponse;
/* 50 */     } catch (Exception e) {
/* 51 */       e.printStackTrace();
/*    */     } 
/* 53 */     return ret;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\core\socksServer\SimpleHttpRequestHandle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */