/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.text.DecimalFormat;
/*     */ import java.text.NumberFormat;
/*     */ import java.text.ParseException;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
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
/*     */ 
/*     */ 
/*     */ public abstract class NumberUtils
/*     */ {
/*  41 */   private static final BigInteger LONG_MIN = BigInteger.valueOf(Long.MIN_VALUE);
/*     */   
/*  43 */   private static final BigInteger LONG_MAX = BigInteger.valueOf(Long.MAX_VALUE);
/*     */ 
/*     */ 
/*     */   
/*     */   public static final Set<Class<?>> STANDARD_NUMBER_TYPES;
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*  52 */     Set<Class<?>> numberTypes = new HashSet<>(8);
/*  53 */     numberTypes.add(Byte.class);
/*  54 */     numberTypes.add(Short.class);
/*  55 */     numberTypes.add(Integer.class);
/*  56 */     numberTypes.add(Long.class);
/*  57 */     numberTypes.add(BigInteger.class);
/*  58 */     numberTypes.add(Float.class);
/*  59 */     numberTypes.add(Double.class);
/*  60 */     numberTypes.add(BigDecimal.class);
/*  61 */     STANDARD_NUMBER_TYPES = Collections.unmodifiableSet(numberTypes);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T extends Number> T convertNumberToTargetClass(Number number, Class<T> targetClass) throws IllegalArgumentException {
/*  85 */     Assert.notNull(number, "Number must not be null");
/*  86 */     Assert.notNull(targetClass, "Target class must not be null");
/*     */     
/*  88 */     if (targetClass.isInstance(number)) {
/*  89 */       return (T)number;
/*     */     }
/*  91 */     if (Byte.class == targetClass) {
/*  92 */       long value = checkedLongValue(number, targetClass);
/*  93 */       if (value < -128L || value > 127L) {
/*  94 */         raiseOverflowException(number, targetClass);
/*     */       }
/*  96 */       return (T)Byte.valueOf(number.byteValue());
/*     */     } 
/*  98 */     if (Short.class == targetClass) {
/*  99 */       long value = checkedLongValue(number, targetClass);
/* 100 */       if (value < -32768L || value > 32767L) {
/* 101 */         raiseOverflowException(number, targetClass);
/*     */       }
/* 103 */       return (T)Short.valueOf(number.shortValue());
/*     */     } 
/* 105 */     if (Integer.class == targetClass) {
/* 106 */       long value = checkedLongValue(number, targetClass);
/* 107 */       if (value < -2147483648L || value > 2147483647L) {
/* 108 */         raiseOverflowException(number, targetClass);
/*     */       }
/* 110 */       return (T)Integer.valueOf(number.intValue());
/*     */     } 
/* 112 */     if (Long.class == targetClass) {
/* 113 */       long value = checkedLongValue(number, targetClass);
/* 114 */       return (T)Long.valueOf(value);
/*     */     } 
/* 116 */     if (BigInteger.class == targetClass) {
/* 117 */       if (number instanceof BigDecimal)
/*     */       {
/* 119 */         return (T)((BigDecimal)number).toBigInteger();
/*     */       }
/*     */ 
/*     */       
/* 123 */       return (T)BigInteger.valueOf(number.longValue());
/*     */     } 
/*     */     
/* 126 */     if (Float.class == targetClass) {
/* 127 */       return (T)Float.valueOf(number.floatValue());
/*     */     }
/* 129 */     if (Double.class == targetClass) {
/* 130 */       return (T)Double.valueOf(number.doubleValue());
/*     */     }
/* 132 */     if (BigDecimal.class == targetClass)
/*     */     {
/*     */       
/* 135 */       return (T)new BigDecimal(number.toString());
/*     */     }
/*     */     
/* 138 */     throw new IllegalArgumentException("Could not convert number [" + number + "] of type [" + number
/* 139 */         .getClass().getName() + "] to unsupported target class [" + targetClass.getName() + "]");
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
/*     */   private static long checkedLongValue(Number number, Class<? extends Number> targetClass) {
/* 153 */     BigInteger bigInt = null;
/* 154 */     if (number instanceof BigInteger) {
/* 155 */       bigInt = (BigInteger)number;
/*     */     }
/* 157 */     else if (number instanceof BigDecimal) {
/* 158 */       bigInt = ((BigDecimal)number).toBigInteger();
/*     */     } 
/*     */     
/* 161 */     if (bigInt != null && (bigInt.compareTo(LONG_MIN) < 0 || bigInt.compareTo(LONG_MAX) > 0)) {
/* 162 */       raiseOverflowException(number, targetClass);
/*     */     }
/* 164 */     return number.longValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void raiseOverflowException(Number number, Class<?> targetClass) {
/* 174 */     throw new IllegalArgumentException("Could not convert number [" + number + "] of type [" + number
/* 175 */         .getClass().getName() + "] to target class [" + targetClass.getName() + "]: overflow");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T extends Number> T parseNumber(String text, Class<T> targetClass) {
/* 200 */     Assert.notNull(text, "Text must not be null");
/* 201 */     Assert.notNull(targetClass, "Target class must not be null");
/* 202 */     String trimmed = StringUtils.trimAllWhitespace(text);
/*     */     
/* 204 */     if (Byte.class == targetClass) {
/* 205 */       return isHexNumber(trimmed) ? (T)Byte.decode(trimmed) : (T)Byte.valueOf(trimmed);
/*     */     }
/* 207 */     if (Short.class == targetClass) {
/* 208 */       return isHexNumber(trimmed) ? (T)Short.decode(trimmed) : (T)Short.valueOf(trimmed);
/*     */     }
/* 210 */     if (Integer.class == targetClass) {
/* 211 */       return isHexNumber(trimmed) ? (T)Integer.decode(trimmed) : (T)Integer.valueOf(trimmed);
/*     */     }
/* 213 */     if (Long.class == targetClass) {
/* 214 */       return isHexNumber(trimmed) ? (T)Long.decode(trimmed) : (T)Long.valueOf(trimmed);
/*     */     }
/* 216 */     if (BigInteger.class == targetClass) {
/* 217 */       return isHexNumber(trimmed) ? (T)decodeBigInteger(trimmed) : (T)new BigInteger(trimmed);
/*     */     }
/* 219 */     if (Float.class == targetClass) {
/* 220 */       return (T)Float.valueOf(trimmed);
/*     */     }
/* 222 */     if (Double.class == targetClass) {
/* 223 */       return (T)Double.valueOf(trimmed);
/*     */     }
/* 225 */     if (BigDecimal.class == targetClass || Number.class == targetClass) {
/* 226 */       return (T)new BigDecimal(trimmed);
/*     */     }
/*     */     
/* 229 */     throw new IllegalArgumentException("Cannot convert String [" + text + "] to target class [" + targetClass
/* 230 */         .getName() + "]");
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T extends Number> T parseNumber(String text, Class<T> targetClass, @Nullable NumberFormat numberFormat) {
/* 252 */     if (numberFormat != null) {
/* 253 */       Assert.notNull(text, "Text must not be null");
/* 254 */       Assert.notNull(targetClass, "Target class must not be null");
/* 255 */       DecimalFormat decimalFormat = null;
/* 256 */       boolean resetBigDecimal = false;
/* 257 */       if (numberFormat instanceof DecimalFormat) {
/* 258 */         decimalFormat = (DecimalFormat)numberFormat;
/* 259 */         if (BigDecimal.class == targetClass && !decimalFormat.isParseBigDecimal()) {
/* 260 */           decimalFormat.setParseBigDecimal(true);
/* 261 */           resetBigDecimal = true;
/*     */         } 
/*     */       } 
/*     */       try {
/* 265 */         Number number = numberFormat.parse(StringUtils.trimAllWhitespace(text));
/* 266 */         return (T)convertNumberToTargetClass(number, (Class)targetClass);
/*     */       }
/* 268 */       catch (ParseException ex) {
/* 269 */         throw new IllegalArgumentException("Could not parse number: " + ex.getMessage());
/*     */       } finally {
/*     */         
/* 272 */         if (resetBigDecimal) {
/* 273 */           decimalFormat.setParseBigDecimal(false);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 278 */     return parseNumber(text, targetClass);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isHexNumber(String value) {
/* 288 */     int index = value.startsWith("-") ? 1 : 0;
/* 289 */     return (value.startsWith("0x", index) || value.startsWith("0X", index) || value.startsWith("#", index));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static BigInteger decodeBigInteger(String value) {
/* 298 */     int radix = 10;
/* 299 */     int index = 0;
/* 300 */     boolean negative = false;
/*     */ 
/*     */     
/* 303 */     if (value.startsWith("-")) {
/* 304 */       negative = true;
/* 305 */       index++;
/*     */     } 
/*     */ 
/*     */     
/* 309 */     if (value.startsWith("0x", index) || value.startsWith("0X", index)) {
/* 310 */       index += 2;
/* 311 */       radix = 16;
/*     */     }
/* 313 */     else if (value.startsWith("#", index)) {
/* 314 */       index++;
/* 315 */       radix = 16;
/*     */     }
/* 317 */     else if (value.startsWith("0", index) && value.length() > 1 + index) {
/* 318 */       index++;
/* 319 */       radix = 8;
/*     */     } 
/*     */     
/* 322 */     BigInteger result = new BigInteger(value.substring(index), radix);
/* 323 */     return negative ? result.negate() : result;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\NumberUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */