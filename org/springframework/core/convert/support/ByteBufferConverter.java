/*     */ package org.springframework.core.convert.support;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.core.convert.converter.ConditionalGenericConverter;
/*     */ import org.springframework.core.convert.converter.GenericConverter;
/*     */ import org.springframework.lang.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class ByteBufferConverter
/*     */   implements ConditionalGenericConverter
/*     */ {
/*  40 */   private static final TypeDescriptor BYTE_BUFFER_TYPE = TypeDescriptor.valueOf(ByteBuffer.class);
/*     */   
/*  42 */   private static final TypeDescriptor BYTE_ARRAY_TYPE = TypeDescriptor.valueOf(byte[].class);
/*     */   private static final Set<GenericConverter.ConvertiblePair> CONVERTIBLE_PAIRS;
/*     */   private final ConversionService conversionService;
/*     */   
/*     */   static {
/*  47 */     Set<GenericConverter.ConvertiblePair> convertiblePairs = new HashSet<>(4);
/*  48 */     convertiblePairs.add(new GenericConverter.ConvertiblePair(ByteBuffer.class, byte[].class));
/*  49 */     convertiblePairs.add(new GenericConverter.ConvertiblePair(byte[].class, ByteBuffer.class));
/*  50 */     convertiblePairs.add(new GenericConverter.ConvertiblePair(ByteBuffer.class, Object.class));
/*  51 */     convertiblePairs.add(new GenericConverter.ConvertiblePair(Object.class, ByteBuffer.class));
/*  52 */     CONVERTIBLE_PAIRS = Collections.unmodifiableSet(convertiblePairs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteBufferConverter(ConversionService conversionService) {
/*  60 */     this.conversionService = conversionService;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
/*  66 */     return CONVERTIBLE_PAIRS;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
/*  71 */     boolean byteBufferTarget = targetType.isAssignableTo(BYTE_BUFFER_TYPE);
/*  72 */     if (sourceType.isAssignableTo(BYTE_BUFFER_TYPE)) {
/*  73 */       return (byteBufferTarget || matchesFromByteBuffer(targetType));
/*     */     }
/*  75 */     return (byteBufferTarget && matchesToByteBuffer(sourceType));
/*     */   }
/*     */   
/*     */   private boolean matchesFromByteBuffer(TypeDescriptor targetType) {
/*  79 */     return (targetType.isAssignableTo(BYTE_ARRAY_TYPE) || this.conversionService
/*  80 */       .canConvert(BYTE_ARRAY_TYPE, targetType));
/*     */   }
/*     */   
/*     */   private boolean matchesToByteBuffer(TypeDescriptor sourceType) {
/*  84 */     return (sourceType.isAssignableTo(BYTE_ARRAY_TYPE) || this.conversionService
/*  85 */       .canConvert(sourceType, BYTE_ARRAY_TYPE));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/*  91 */     boolean byteBufferTarget = targetType.isAssignableTo(BYTE_BUFFER_TYPE);
/*  92 */     if (source instanceof ByteBuffer) {
/*  93 */       ByteBuffer buffer = (ByteBuffer)source;
/*  94 */       return byteBufferTarget ? buffer.duplicate() : convertFromByteBuffer(buffer, targetType);
/*     */     } 
/*  96 */     if (byteBufferTarget) {
/*  97 */       return convertToByteBuffer(source, sourceType);
/*     */     }
/*     */     
/* 100 */     throw new IllegalStateException("Unexpected source/target types");
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private Object convertFromByteBuffer(ByteBuffer source, TypeDescriptor targetType) {
/* 105 */     byte[] bytes = new byte[source.remaining()];
/* 106 */     source.get(bytes);
/*     */     
/* 108 */     if (targetType.isAssignableTo(BYTE_ARRAY_TYPE)) {
/* 109 */       return bytes;
/*     */     }
/* 111 */     return this.conversionService.convert(bytes, BYTE_ARRAY_TYPE, targetType);
/*     */   }
/*     */ 
/*     */   
/*     */   private Object convertToByteBuffer(@Nullable Object source, TypeDescriptor sourceType) {
/* 116 */     byte[] bytes = (source instanceof byte[]) ? (byte[])source : (byte[])this.conversionService.convert(source, sourceType, BYTE_ARRAY_TYPE);
/*     */     
/* 118 */     if (bytes == null) {
/* 119 */       return ByteBuffer.wrap(new byte[0]);
/*     */     }
/*     */     
/* 122 */     ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
/* 123 */     byteBuffer.put(bytes);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 128 */     return byteBuffer.rewind();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\convert\support\ByteBufferConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */