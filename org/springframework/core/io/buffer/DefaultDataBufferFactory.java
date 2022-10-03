/*     */ package org.springframework.core.io.buffer;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.List;
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
/*     */ public class DefaultDataBufferFactory
/*     */   implements DataBufferFactory
/*     */ {
/*     */   public static final int DEFAULT_INITIAL_CAPACITY = 256;
/*  45 */   public static final DefaultDataBufferFactory sharedInstance = new DefaultDataBufferFactory();
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean preferDirect;
/*     */ 
/*     */ 
/*     */   
/*     */   private final int defaultInitialCapacity;
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultDataBufferFactory() {
/*  58 */     this(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultDataBufferFactory(boolean preferDirect) {
/*  69 */     this(preferDirect, 256);
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
/*     */   public DefaultDataBufferFactory(boolean preferDirect, int defaultInitialCapacity) {
/*  81 */     Assert.isTrue((defaultInitialCapacity > 0), "'defaultInitialCapacity' should be larger than 0");
/*  82 */     this.preferDirect = preferDirect;
/*  83 */     this.defaultInitialCapacity = defaultInitialCapacity;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultDataBuffer allocateBuffer() {
/*  89 */     return allocateBuffer(this.defaultInitialCapacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultDataBuffer allocateBuffer(int initialCapacity) {
/*  96 */     ByteBuffer byteBuffer = this.preferDirect ? ByteBuffer.allocateDirect(initialCapacity) : ByteBuffer.allocate(initialCapacity);
/*  97 */     return DefaultDataBuffer.fromEmptyByteBuffer(this, byteBuffer);
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultDataBuffer wrap(ByteBuffer byteBuffer) {
/* 102 */     return DefaultDataBuffer.fromFilledByteBuffer(this, byteBuffer.slice());
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultDataBuffer wrap(byte[] bytes) {
/* 107 */     return DefaultDataBuffer.fromFilledByteBuffer(this, ByteBuffer.wrap(bytes));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultDataBuffer join(List<? extends DataBuffer> dataBuffers) {
/* 117 */     Assert.notEmpty(dataBuffers, "DataBuffer List must not be empty");
/* 118 */     int capacity = dataBuffers.stream().mapToInt(DataBuffer::readableByteCount).sum();
/* 119 */     DefaultDataBuffer result = allocateBuffer(capacity);
/* 120 */     dataBuffers.forEach(xva$0 -> rec$.write(new DataBuffer[] { xva$0 }));
/* 121 */     dataBuffers.forEach(DataBufferUtils::release);
/* 122 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 128 */     return "DefaultDataBufferFactory (preferDirect=" + this.preferDirect + ")";
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\io\buffer\DefaultDataBufferFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */