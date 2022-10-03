/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
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
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public final class Closeables
/*     */ {
/*     */   @VisibleForTesting
/*  37 */   static final Logger logger = Logger.getLogger(Closeables.class.getName());
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
/*     */   public static void close(Closeable closeable, boolean swallowIOException) throws IOException {
/*  74 */     if (closeable == null) {
/*     */       return;
/*     */     }
/*     */     try {
/*  78 */       closeable.close();
/*  79 */     } catch (IOException e) {
/*  80 */       if (swallowIOException) {
/*  81 */         logger.log(Level.WARNING, "IOException thrown while closing Closeable.", e);
/*     */       } else {
/*  83 */         throw e;
/*     */       } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void closeQuietly(InputStream inputStream) {
/*     */     try {
/* 104 */       close(inputStream, true);
/* 105 */     } catch (IOException impossible) {
/* 106 */       throw new AssertionError(impossible);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void closeQuietly(Reader reader) {
/*     */     try {
/* 125 */       close(reader, true);
/* 126 */     } catch (IOException impossible) {
/* 127 */       throw new AssertionError(impossible);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\io\Closeables.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */