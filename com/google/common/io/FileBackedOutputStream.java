/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
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
/*     */ public final class FileBackedOutputStream
/*     */   extends OutputStream
/*     */ {
/*     */   private final int fileThreshold;
/*     */   private final boolean resetOnFinalize;
/*     */   private final ByteSource source;
/*     */   private OutputStream out;
/*     */   private MemoryOutput memory;
/*     */   private File file;
/*     */   
/*     */   private static class MemoryOutput
/*     */     extends ByteArrayOutputStream
/*     */   {
/*     */     private MemoryOutput() {}
/*     */     
/*     */     byte[] getBuffer() {
/*  54 */       return this.buf;
/*     */     }
/*     */     
/*     */     int getCount() {
/*  58 */       return this.count;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   synchronized File getFile() {
/*  65 */     return this.file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileBackedOutputStream(int fileThreshold) {
/*  75 */     this(fileThreshold, false);
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
/*     */   public FileBackedOutputStream(int fileThreshold, boolean resetOnFinalize) {
/*  87 */     this.fileThreshold = fileThreshold;
/*  88 */     this.resetOnFinalize = resetOnFinalize;
/*  89 */     this.memory = new MemoryOutput();
/*  90 */     this.out = this.memory;
/*     */     
/*  92 */     if (resetOnFinalize) {
/*  93 */       this.source = new ByteSource()
/*     */         {
/*     */           public InputStream openStream() throws IOException
/*     */           {
/*  97 */             return FileBackedOutputStream.this.openInputStream();
/*     */           }
/*     */ 
/*     */           
/*     */           protected void finalize() {
/*     */             try {
/* 103 */               FileBackedOutputStream.this.reset();
/* 104 */             } catch (Throwable t) {
/* 105 */               t.printStackTrace(System.err);
/*     */             } 
/*     */           }
/*     */         };
/*     */     } else {
/* 110 */       this.source = new ByteSource()
/*     */         {
/*     */           public InputStream openStream() throws IOException
/*     */           {
/* 114 */             return FileBackedOutputStream.this.openInputStream();
/*     */           }
/*     */         };
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteSource asByteSource() {
/* 126 */     return this.source;
/*     */   }
/*     */   
/*     */   private synchronized InputStream openInputStream() throws IOException {
/* 130 */     if (this.file != null) {
/* 131 */       return new FileInputStream(this.file);
/*     */     }
/* 133 */     return new ByteArrayInputStream(this.memory.getBuffer(), 0, this.memory.getCount());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void reset() throws IOException {
/*     */     try {
/* 145 */       close();
/*     */     } finally {
/* 147 */       if (this.memory == null) {
/* 148 */         this.memory = new MemoryOutput();
/*     */       } else {
/* 150 */         this.memory.reset();
/*     */       } 
/* 152 */       this.out = this.memory;
/* 153 */       if (this.file != null) {
/* 154 */         File deleteMe = this.file;
/* 155 */         this.file = null;
/* 156 */         if (!deleteMe.delete()) {
/* 157 */           throw new IOException("Could not delete: " + deleteMe);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void write(int b) throws IOException {
/* 165 */     update(1);
/* 166 */     this.out.write(b);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void write(byte[] b) throws IOException {
/* 171 */     write(b, 0, b.length);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void write(byte[] b, int off, int len) throws IOException {
/* 176 */     update(len);
/* 177 */     this.out.write(b, off, len);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void close() throws IOException {
/* 182 */     this.out.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void flush() throws IOException {
/* 187 */     this.out.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void update(int len) throws IOException {
/* 195 */     if (this.file == null && this.memory.getCount() + len > this.fileThreshold) {
/* 196 */       File temp = File.createTempFile("FileBackedOutputStream", null);
/* 197 */       if (this.resetOnFinalize)
/*     */       {
/*     */         
/* 200 */         temp.deleteOnExit();
/*     */       }
/* 202 */       FileOutputStream transfer = new FileOutputStream(temp);
/* 203 */       transfer.write(this.memory.getBuffer(), 0, this.memory.getCount());
/* 204 */       transfer.flush();
/*     */ 
/*     */       
/* 207 */       this.out = transfer;
/* 208 */       this.file = temp;
/* 209 */       this.memory = null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\io\FileBackedOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */