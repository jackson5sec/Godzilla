/*     */ package org.springframework.core.io;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Arrays;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class ByteArrayResource
/*     */   extends AbstractResource
/*     */ {
/*     */   private final byte[] byteArray;
/*     */   private final String description;
/*     */   
/*     */   public ByteArrayResource(byte[] byteArray) {
/*  55 */     this(byteArray, "resource loaded from byte array");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteArrayResource(byte[] byteArray, @Nullable String description) {
/*  64 */     Assert.notNull(byteArray, "Byte array must not be null");
/*  65 */     this.byteArray = byteArray;
/*  66 */     this.description = (description != null) ? description : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final byte[] getByteArray() {
/*  74 */     return this.byteArray;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean exists() {
/*  82 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long contentLength() {
/*  90 */     return this.byteArray.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getInputStream() throws IOException {
/* 100 */     return new ByteArrayInputStream(this.byteArray);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDescription() {
/* 109 */     return "Byte array resource [" + this.description + "]";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object other) {
/* 119 */     return (this == other || (other instanceof ByteArrayResource && 
/* 120 */       Arrays.equals(((ByteArrayResource)other).byteArray, this.byteArray)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 129 */     return Arrays.hashCode(this.byteArray);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\io\ByteArrayResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */