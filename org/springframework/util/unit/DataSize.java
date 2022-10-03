/*     */ package org.springframework.util.unit;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class DataSize
/*     */   implements Comparable<DataSize>, Serializable
/*     */ {
/*  58 */   private static final Pattern PATTERN = Pattern.compile("^([+\\-]?\\d+)([a-zA-Z]{0,2})$");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final long BYTES_PER_KB = 1024L;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final long BYTES_PER_MB = 1048576L;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final long BYTES_PER_GB = 1073741824L;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final long BYTES_PER_TB = 1099511627776L;
/*     */ 
/*     */ 
/*     */   
/*     */   private final long bytes;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private DataSize(long bytes) {
/*  85 */     this.bytes = bytes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DataSize ofBytes(long bytes) {
/*  95 */     return new DataSize(bytes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DataSize ofKilobytes(long kilobytes) {
/* 104 */     return new DataSize(Math.multiplyExact(kilobytes, 1024L));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DataSize ofMegabytes(long megabytes) {
/* 113 */     return new DataSize(Math.multiplyExact(megabytes, 1048576L));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DataSize ofGigabytes(long gigabytes) {
/* 122 */     return new DataSize(Math.multiplyExact(gigabytes, 1073741824L));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DataSize ofTerabytes(long terabytes) {
/* 131 */     return new DataSize(Math.multiplyExact(terabytes, 1099511627776L));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DataSize of(long amount, DataUnit unit) {
/* 141 */     Assert.notNull(unit, "Unit must not be null");
/* 142 */     return new DataSize(Math.multiplyExact(amount, unit.size().toBytes()));
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
/*     */   public static DataSize parse(CharSequence text) {
/* 160 */     return parse(text, null);
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
/*     */   public static DataSize parse(CharSequence text, @Nullable DataUnit defaultUnit) {
/* 180 */     Assert.notNull(text, "Text must not be null");
/*     */     try {
/* 182 */       Matcher matcher = PATTERN.matcher(text);
/* 183 */       Assert.state(matcher.matches(), "Does not match data size pattern");
/* 184 */       DataUnit unit = determineDataUnit(matcher.group(2), defaultUnit);
/* 185 */       long amount = Long.parseLong(matcher.group(1));
/* 186 */       return of(amount, unit);
/*     */     }
/* 188 */     catch (Exception ex) {
/* 189 */       throw new IllegalArgumentException("'" + text + "' is not a valid data size", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static DataUnit determineDataUnit(String suffix, @Nullable DataUnit defaultUnit) {
/* 194 */     DataUnit defaultUnitToUse = (defaultUnit != null) ? defaultUnit : DataUnit.BYTES;
/* 195 */     return StringUtils.hasLength(suffix) ? DataUnit.fromSuffix(suffix) : defaultUnitToUse;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNegative() {
/* 203 */     return (this.bytes < 0L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long toBytes() {
/* 211 */     return this.bytes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long toKilobytes() {
/* 219 */     return this.bytes / 1024L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long toMegabytes() {
/* 227 */     return this.bytes / 1048576L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long toGigabytes() {
/* 235 */     return this.bytes / 1073741824L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long toTerabytes() {
/* 243 */     return this.bytes / 1099511627776L;
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(DataSize other) {
/* 248 */     return Long.compare(this.bytes, other.bytes);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 253 */     return String.format("%dB", new Object[] { Long.valueOf(this.bytes) });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object other) {
/* 259 */     if (this == other) {
/* 260 */       return true;
/*     */     }
/* 262 */     if (other == null || getClass() != other.getClass()) {
/* 263 */       return false;
/*     */     }
/* 265 */     DataSize otherSize = (DataSize)other;
/* 266 */     return (this.bytes == otherSize.bytes);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 271 */     return Long.hashCode(this.bytes);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\uti\\unit\DataSize.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */