/*     */ package org.springframework.core.io.buffer;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.function.Predicate;
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
/*     */ public class LimitedDataBufferList
/*     */   extends ArrayList<DataBuffer>
/*     */ {
/*     */   private final int maxByteCount;
/*     */   private int byteCount;
/*     */   
/*     */   public LimitedDataBufferList(int maxByteCount) {
/*  52 */     this.maxByteCount = maxByteCount;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean add(DataBuffer buffer) {
/*  58 */     updateCount(buffer.readableByteCount());
/*  59 */     return super.add(buffer);
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(int index, DataBuffer buffer) {
/*  64 */     super.add(index, buffer);
/*  65 */     updateCount(buffer.readableByteCount());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addAll(Collection<? extends DataBuffer> collection) {
/*  70 */     boolean result = super.addAll(collection);
/*  71 */     collection.forEach(buffer -> updateCount(buffer.readableByteCount()));
/*  72 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addAll(int index, Collection<? extends DataBuffer> collection) {
/*  77 */     boolean result = super.addAll(index, collection);
/*  78 */     collection.forEach(buffer -> updateCount(buffer.readableByteCount()));
/*  79 */     return result;
/*     */   }
/*     */   
/*     */   private void updateCount(int bytesToAdd) {
/*  83 */     if (this.maxByteCount < 0) {
/*     */       return;
/*     */     }
/*  86 */     if (bytesToAdd > Integer.MAX_VALUE - this.byteCount) {
/*  87 */       raiseLimitException();
/*     */     } else {
/*     */       
/*  90 */       this.byteCount += bytesToAdd;
/*  91 */       if (this.byteCount > this.maxByteCount) {
/*  92 */         raiseLimitException();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void raiseLimitException() {
/*  99 */     throw new DataBufferLimitException("Exceeded limit on max bytes to buffer : " + this.maxByteCount);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DataBuffer remove(int index) {
/* 105 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean remove(Object o) {
/* 110 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void removeRange(int fromIndex, int toIndex) {
/* 115 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeAll(Collection<?> c) {
/* 120 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeIf(Predicate<? super DataBuffer> filter) {
/* 125 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public DataBuffer set(int index, DataBuffer element) {
/* 130 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 135 */     this.byteCount = 0;
/* 136 */     super.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void releaseAndClear() {
/* 144 */     forEach(buf -> {
/*     */           
/*     */           try {
/*     */             DataBufferUtils.release(buf);
/* 148 */           } catch (Throwable throwable) {}
/*     */         });
/*     */ 
/*     */     
/* 152 */     clear();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\io\buffer\LimitedDataBufferList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */