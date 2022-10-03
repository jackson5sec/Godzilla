/*     */ package org.springframework.core.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Predicate;
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
/*     */ abstract class AbstractMergedAnnotation<A extends Annotation>
/*     */   implements MergedAnnotation<A>
/*     */ {
/*     */   @Nullable
/*     */   private volatile A synthesizedAnnotation;
/*     */   
/*     */   public boolean isDirectlyPresent() {
/*  44 */     return (isPresent() && getDistance() == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isMetaPresent() {
/*  49 */     return (isPresent() && getDistance() > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasNonDefaultValue(String attributeName) {
/*  54 */     return !hasDefaultValue(attributeName);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getByte(String attributeName) {
/*  59 */     return ((Byte)getRequiredAttributeValue(attributeName, Byte.class)).byteValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getByteArray(String attributeName) {
/*  64 */     return getRequiredAttributeValue(attributeName, (Class)byte[].class);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getBoolean(String attributeName) {
/*  69 */     return ((Boolean)getRequiredAttributeValue(attributeName, Boolean.class)).booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean[] getBooleanArray(String attributeName) {
/*  74 */     return getRequiredAttributeValue(attributeName, (Class)boolean[].class);
/*     */   }
/*     */ 
/*     */   
/*     */   public char getChar(String attributeName) {
/*  79 */     return ((Character)getRequiredAttributeValue(attributeName, Character.class)).charValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public char[] getCharArray(String attributeName) {
/*  84 */     return getRequiredAttributeValue(attributeName, (Class)char[].class);
/*     */   }
/*     */ 
/*     */   
/*     */   public short getShort(String attributeName) {
/*  89 */     return ((Short)getRequiredAttributeValue(attributeName, Short.class)).shortValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public short[] getShortArray(String attributeName) {
/*  94 */     return getRequiredAttributeValue(attributeName, (Class)short[].class);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getInt(String attributeName) {
/*  99 */     return ((Integer)getRequiredAttributeValue(attributeName, Integer.class)).intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public int[] getIntArray(String attributeName) {
/* 104 */     return getRequiredAttributeValue(attributeName, (Class)int[].class);
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLong(String attributeName) {
/* 109 */     return ((Long)getRequiredAttributeValue(attributeName, Long.class)).longValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public long[] getLongArray(String attributeName) {
/* 114 */     return getRequiredAttributeValue(attributeName, (Class)long[].class);
/*     */   }
/*     */ 
/*     */   
/*     */   public double getDouble(String attributeName) {
/* 119 */     return ((Double)getRequiredAttributeValue(attributeName, Double.class)).doubleValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public double[] getDoubleArray(String attributeName) {
/* 124 */     return getRequiredAttributeValue(attributeName, (Class)double[].class);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getFloat(String attributeName) {
/* 129 */     return ((Float)getRequiredAttributeValue(attributeName, Float.class)).floatValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public float[] getFloatArray(String attributeName) {
/* 134 */     return getRequiredAttributeValue(attributeName, (Class)float[].class);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getString(String attributeName) {
/* 139 */     return getRequiredAttributeValue(attributeName, String.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getStringArray(String attributeName) {
/* 144 */     return getRequiredAttributeValue(attributeName, (Class)String[].class);
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getClass(String attributeName) {
/* 149 */     return getRequiredAttributeValue(attributeName, Class.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?>[] getClassArray(String attributeName) {
/* 154 */     return getRequiredAttributeValue(attributeName, (Class)Class[].class);
/*     */   }
/*     */ 
/*     */   
/*     */   public <E extends Enum<E>> E getEnum(String attributeName, Class<E> type) {
/* 159 */     Assert.notNull(type, "Type must not be null");
/* 160 */     return (E)getRequiredAttributeValue(attributeName, type);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <E extends Enum<E>> E[] getEnumArray(String attributeName, Class<E> type) {
/* 166 */     Assert.notNull(type, "Type must not be null");
/* 167 */     Class<?> arrayType = Array.newInstance(type, 0).getClass();
/* 168 */     return (E[])getRequiredAttributeValue(attributeName, arrayType);
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<Object> getValue(String attributeName) {
/* 173 */     return getValue(attributeName, Object.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> Optional<T> getValue(String attributeName, Class<T> type) {
/* 178 */     return Optional.ofNullable(getAttributeValue(attributeName, type));
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<Object> getDefaultValue(String attributeName) {
/* 183 */     return getDefaultValue(attributeName, Object.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public MergedAnnotation<A> filterDefaultValues() {
/* 188 */     return filterAttributes(this::hasNonDefaultValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public AnnotationAttributes asAnnotationAttributes(MergedAnnotation.Adapt... adaptations) {
/* 193 */     return (AnnotationAttributes)asMap(mergedAnnotation -> new AnnotationAttributes(mergedAnnotation.getType()), adaptations);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Optional<A> synthesize(Predicate<? super MergedAnnotation<A>> condition) throws NoSuchElementException {
/* 200 */     return condition.test(this) ? Optional.<A>of(synthesize()) : Optional.<A>empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public A synthesize() {
/* 205 */     if (!isPresent()) {
/* 206 */       throw new NoSuchElementException("Unable to synthesize missing annotation");
/*     */     }
/* 208 */     A synthesized = this.synthesizedAnnotation;
/* 209 */     if (synthesized == null) {
/* 210 */       synthesized = createSynthesized();
/* 211 */       this.synthesizedAnnotation = synthesized;
/*     */     } 
/* 213 */     return synthesized;
/*     */   }
/*     */   
/*     */   private <T> T getRequiredAttributeValue(String attributeName, Class<T> type) {
/* 217 */     T value = getAttributeValue(attributeName, type);
/* 218 */     if (value == null) {
/* 219 */       throw new NoSuchElementException("No attribute named '" + attributeName + "' present in merged annotation " + 
/* 220 */           getType().getName());
/*     */     }
/* 222 */     return value;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected abstract <T> T getAttributeValue(String paramString, Class<T> paramClass);
/*     */   
/*     */   protected abstract A createSynthesized();
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\annotation\AbstractMergedAnnotation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */