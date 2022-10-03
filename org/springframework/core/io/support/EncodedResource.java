/*     */ package org.springframework.core.io.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.nio.charset.Charset;
/*     */ import org.springframework.core.io.InputStreamSource;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ public class EncodedResource
/*     */   implements InputStreamSource
/*     */ {
/*     */   private final Resource resource;
/*     */   @Nullable
/*     */   private final String encoding;
/*     */   @Nullable
/*     */   private final Charset charset;
/*     */   
/*     */   public EncodedResource(Resource resource) {
/*  62 */     this(resource, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EncodedResource(Resource resource, @Nullable String encoding) {
/*  72 */     this(resource, encoding, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EncodedResource(Resource resource, @Nullable Charset charset) {
/*  82 */     this(resource, null, charset);
/*     */   }
/*     */ 
/*     */   
/*     */   private EncodedResource(Resource resource, @Nullable String encoding, @Nullable Charset charset) {
/*  87 */     Assert.notNull(resource, "Resource must not be null");
/*  88 */     this.resource = resource;
/*  89 */     this.encoding = encoding;
/*  90 */     this.charset = charset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Resource getResource() {
/*  98 */     return this.resource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public final String getEncoding() {
/* 107 */     return this.encoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public final Charset getCharset() {
/* 116 */     return this.charset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean requiresReader() {
/* 127 */     return (this.encoding != null || this.charset != null);
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
/*     */   public Reader getReader() throws IOException {
/* 139 */     if (this.charset != null) {
/* 140 */       return new InputStreamReader(this.resource.getInputStream(), this.charset);
/*     */     }
/* 142 */     if (this.encoding != null) {
/* 143 */       return new InputStreamReader(this.resource.getInputStream(), this.encoding);
/*     */     }
/*     */     
/* 146 */     return new InputStreamReader(this.resource.getInputStream());
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
/*     */   public InputStream getInputStream() throws IOException {
/* 159 */     return this.resource.getInputStream();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object other) {
/* 165 */     if (this == other) {
/* 166 */       return true;
/*     */     }
/* 168 */     if (!(other instanceof EncodedResource)) {
/* 169 */       return false;
/*     */     }
/* 171 */     EncodedResource otherResource = (EncodedResource)other;
/* 172 */     return (this.resource.equals(otherResource.resource) && 
/* 173 */       ObjectUtils.nullSafeEquals(this.charset, otherResource.charset) && 
/* 174 */       ObjectUtils.nullSafeEquals(this.encoding, otherResource.encoding));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 179 */     return this.resource.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 184 */     return this.resource.toString();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\io\support\EncodedResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */