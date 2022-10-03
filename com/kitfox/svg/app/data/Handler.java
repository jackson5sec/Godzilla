/*     */ package com.kitfox.svg.app.data;
/*     */ 
/*     */ import com.kitfox.svg.util.Base64InputStream;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.net.URLStreamHandler;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
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
/*     */ public class Handler
/*     */   extends URLStreamHandler
/*     */ {
/*     */   class Connection
/*     */     extends URLConnection
/*     */   {
/*     */     String mime;
/*     */     byte[] buf;
/*     */     
/*     */     public Connection(URL url) {
/*  62 */       super(url);
/*     */       
/*  64 */       String path = url.getPath();
/*  65 */       int idx = path.indexOf(';');
/*  66 */       this.mime = path.substring(0, idx);
/*  67 */       String content = path.substring(idx + 1);
/*     */       
/*  69 */       if (content.startsWith("base64,")) {
/*     */         
/*  71 */         content = content.substring(7);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/*  77 */           ByteArrayInputStream bis = new ByteArrayInputStream(content.getBytes());
/*  78 */           Base64InputStream b64is = new Base64InputStream(bis);
/*     */           
/*  80 */           ByteArrayOutputStream bout = new ByteArrayOutputStream();
/*  81 */           byte[] tmp = new byte[2056]; int size;
/*  82 */           for (size = b64is.read(tmp); size != -1; size = b64is.read(tmp))
/*     */           {
/*  84 */             bout.write(tmp, 0, size);
/*     */           }
/*  86 */           this.buf = bout.toByteArray();
/*     */         }
/*  88 */         catch (IOException e) {
/*     */           
/*  90 */           Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, (String)null, e);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void connect() throws IOException {}
/*     */ 
/*     */ 
/*     */     
/*     */     public String getHeaderField(String name) {
/* 103 */       if ("content-type".equals(name))
/*     */       {
/* 105 */         return this.mime;
/*     */       }
/*     */       
/* 108 */       return super.getHeaderField(name);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public InputStream getInputStream() throws IOException {
/* 114 */       return new ByteArrayInputStream(this.buf);
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
/*     */   protected URLConnection openConnection(URL u) throws IOException {
/* 126 */     return new Connection(u);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\app\data\Handler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */