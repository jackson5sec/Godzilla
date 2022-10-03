/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.URL;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.List;
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
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public final class Resources
/*     */ {
/*     */   public static ByteSource asByteSource(URL url) {
/*  56 */     return new UrlByteSource(url);
/*     */   }
/*     */   
/*     */   private static final class UrlByteSource
/*     */     extends ByteSource
/*     */   {
/*     */     private final URL url;
/*     */     
/*     */     private UrlByteSource(URL url) {
/*  65 */       this.url = (URL)Preconditions.checkNotNull(url);
/*     */     }
/*     */ 
/*     */     
/*     */     public InputStream openStream() throws IOException {
/*  70 */       return this.url.openStream();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  75 */       return "Resources.asByteSource(" + this.url + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CharSource asCharSource(URL url, Charset charset) {
/*  85 */     return asByteSource(url).asCharSource(charset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] toByteArray(URL url) throws IOException {
/*  96 */     return asByteSource(url).read();
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
/*     */   public static String toString(URL url, Charset charset) throws IOException {
/* 109 */     return asCharSource(url, charset).read();
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
/*     */   @CanIgnoreReturnValue
/*     */   public static <T> T readLines(URL url, Charset charset, LineProcessor<T> callback) throws IOException {
/* 126 */     return asCharSource(url, charset).readLines(callback);
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
/*     */   public static List<String> readLines(URL url, Charset charset) throws IOException {
/* 145 */     return readLines(url, charset, new LineProcessor<List<String>>()
/*     */         {
/*     */ 
/*     */           
/* 149 */           final List<String> result = Lists.newArrayList();
/*     */ 
/*     */           
/*     */           public boolean processLine(String line) {
/* 153 */             this.result.add(line);
/* 154 */             return true;
/*     */           }
/*     */ 
/*     */           
/*     */           public List<String> getResult() {
/* 159 */             return this.result;
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void copy(URL from, OutputStream to) throws IOException {
/* 172 */     asByteSource(from).copyTo(to);
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
/*     */   @CanIgnoreReturnValue
/*     */   public static URL getResource(String resourceName) {
/* 192 */     ClassLoader loader = (ClassLoader)MoreObjects.firstNonNull(
/* 193 */         Thread.currentThread().getContextClassLoader(), Resources.class.getClassLoader());
/* 194 */     URL url = loader.getResource(resourceName);
/* 195 */     Preconditions.checkArgument((url != null), "resource %s not found.", resourceName);
/* 196 */     return url;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static URL getResource(Class<?> contextClass, String resourceName) {
/* 206 */     URL url = contextClass.getResource(resourceName);
/* 207 */     Preconditions.checkArgument((url != null), "resource %s relative to %s not found.", resourceName, contextClass
/* 208 */         .getName());
/* 209 */     return url;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\io\Resources.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */