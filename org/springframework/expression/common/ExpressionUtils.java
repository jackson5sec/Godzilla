/*     */ package org.springframework.expression.common;
/*     */ 
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.TypeConverter;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ExpressionUtils
/*     */ {
/*     */   @Nullable
/*     */   public static <T> T convertTypedValue(@Nullable EvaluationContext context, TypedValue typedValue, @Nullable Class<T> targetType) {
/*  52 */     Object value = typedValue.getValue();
/*  53 */     if (targetType == null) {
/*  54 */       return (T)value;
/*     */     }
/*  56 */     if (context != null) {
/*  57 */       return (T)context.getTypeConverter().convertValue(value, typedValue
/*  58 */           .getTypeDescriptor(), TypeDescriptor.valueOf(targetType));
/*     */     }
/*  60 */     if (ClassUtils.isAssignableValue(targetType, value)) {
/*  61 */       return (T)value;
/*     */     }
/*  63 */     throw new EvaluationException("Cannot convert value '" + value + "' to type '" + targetType.getName() + "'");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int toInt(TypeConverter typeConverter, TypedValue typedValue) {
/*  70 */     return ((Integer)convertValue(typeConverter, typedValue, Integer.class)).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean toBoolean(TypeConverter typeConverter, TypedValue typedValue) {
/*  77 */     return ((Boolean)convertValue(typeConverter, typedValue, Boolean.class)).booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double toDouble(TypeConverter typeConverter, TypedValue typedValue) {
/*  84 */     return ((Double)convertValue(typeConverter, typedValue, Double.class)).doubleValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long toLong(TypeConverter typeConverter, TypedValue typedValue) {
/*  91 */     return ((Long)convertValue(typeConverter, typedValue, Long.class)).longValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static char toChar(TypeConverter typeConverter, TypedValue typedValue) {
/*  98 */     return ((Character)convertValue(typeConverter, typedValue, Character.class)).charValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static short toShort(TypeConverter typeConverter, TypedValue typedValue) {
/* 105 */     return ((Short)convertValue(typeConverter, typedValue, Short.class)).shortValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float toFloat(TypeConverter typeConverter, TypedValue typedValue) {
/* 112 */     return ((Float)convertValue(typeConverter, typedValue, Float.class)).floatValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte toByte(TypeConverter typeConverter, TypedValue typedValue) {
/* 119 */     return ((Byte)convertValue(typeConverter, typedValue, Byte.class)).byteValue();
/*     */   }
/*     */ 
/*     */   
/*     */   private static <T> T convertValue(TypeConverter typeConverter, TypedValue typedValue, Class<T> targetType) {
/* 124 */     Object result = typeConverter.convertValue(typedValue.getValue(), typedValue.getTypeDescriptor(), 
/* 125 */         TypeDescriptor.valueOf(targetType));
/* 126 */     if (result == null) {
/* 127 */       throw new IllegalStateException("Null conversion result for value [" + typedValue.getValue() + "]");
/*     */     }
/* 129 */     return (T)result;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\common\ExpressionUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */