/*     */ package org.springframework.core.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InputStreamResource
/*     */   extends AbstractResource
/*     */ {
/*     */   private final InputStream inputStream;
/*     */   private final String description;
/*     */   private boolean read = false;
/*     */   
/*     */   public InputStreamResource(InputStream inputStream) {
/*  59 */     this(inputStream, "resource loaded through InputStream");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStreamResource(InputStream inputStream, @Nullable String description) {
/*  68 */     Assert.notNull(inputStream, "InputStream must not be null");
/*  69 */     this.inputStream = inputStream;
/*  70 */     this.description = (description != null) ? description : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean exists() {
/*  79 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOpen() {
/*  87 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getInputStream() throws IOException, IllegalStateException {
/*  96 */     if (this.read) {
/*  97 */       throw new IllegalStateException("InputStream has already been read - do not use InputStreamResource if a stream needs to be read multiple times");
/*     */     }
/*     */     
/* 100 */     this.read = true;
/* 101 */     return this.inputStream;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDescription() {
/* 110 */     return "InputStream resource [" + this.description + "]";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object other) {
/* 119 */     return (this == other || (other instanceof InputStreamResource && ((InputStreamResource)other).inputStream
/* 120 */       .equals(this.inputStream)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 128 */     return this.inputStream.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\io\InputStreamResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */