/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.IOException;
/*     */ import java.util.AbstractList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ public class Joiner
/*     */ {
/*     */   private final String separator;
/*     */   
/*     */   public static Joiner on(String separator) {
/*  69 */     return new Joiner(separator);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Joiner on(char separator) {
/*  74 */     return new Joiner(String.valueOf(separator));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Joiner(String separator) {
/*  80 */     this.separator = Preconditions.<String>checkNotNull(separator);
/*     */   }
/*     */   
/*     */   private Joiner(Joiner prototype) {
/*  84 */     this.separator = prototype.separator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public <A extends Appendable> A appendTo(A appendable, Iterable<?> parts) throws IOException {
/*  93 */     return appendTo(appendable, parts.iterator());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public <A extends Appendable> A appendTo(A appendable, Iterator<?> parts) throws IOException {
/* 104 */     Preconditions.checkNotNull(appendable);
/* 105 */     if (parts.hasNext()) {
/* 106 */       appendable.append(toString(parts.next()));
/* 107 */       while (parts.hasNext()) {
/* 108 */         appendable.append(this.separator);
/* 109 */         appendable.append(toString(parts.next()));
/*     */       } 
/*     */     } 
/* 112 */     return appendable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final <A extends Appendable> A appendTo(A appendable, Object[] parts) throws IOException {
/* 121 */     return appendTo(appendable, Arrays.asList(parts));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final <A extends Appendable> A appendTo(A appendable, Object first, Object second, Object... rest) throws IOException {
/* 129 */     return appendTo(appendable, iterable(first, second, rest));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final StringBuilder appendTo(StringBuilder builder, Iterable<?> parts) {
/* 139 */     return appendTo(builder, parts.iterator());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final StringBuilder appendTo(StringBuilder builder, Iterator<?> parts) {
/*     */     try {
/* 152 */       appendTo(builder, parts);
/* 153 */     } catch (IOException impossible) {
/* 154 */       throw new AssertionError(impossible);
/*     */     } 
/* 156 */     return builder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final StringBuilder appendTo(StringBuilder builder, Object[] parts) {
/* 166 */     return appendTo(builder, Arrays.asList(parts));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final StringBuilder appendTo(StringBuilder builder, Object first, Object second, Object... rest) {
/* 177 */     return appendTo(builder, iterable(first, second, rest));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String join(Iterable<?> parts) {
/* 185 */     return join(parts.iterator());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String join(Iterator<?> parts) {
/* 195 */     return appendTo(new StringBuilder(), parts).toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String join(Object[] parts) {
/* 203 */     return join(Arrays.asList(parts));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String join(Object first, Object second, Object... rest) {
/* 211 */     return join(iterable(first, second, rest));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Joiner useForNull(final String nullText) {
/* 219 */     Preconditions.checkNotNull(nullText);
/* 220 */     return new Joiner(this)
/*     */       {
/*     */         CharSequence toString(Object part) {
/* 223 */           return (part == null) ? nullText : Joiner.this.toString(part);
/*     */         }
/*     */ 
/*     */         
/*     */         public Joiner useForNull(String nullText) {
/* 228 */           throw new UnsupportedOperationException("already specified useForNull");
/*     */         }
/*     */ 
/*     */         
/*     */         public Joiner skipNulls() {
/* 233 */           throw new UnsupportedOperationException("already specified useForNull");
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Joiner skipNulls() {
/* 243 */     return new Joiner(this)
/*     */       {
/*     */         public <A extends Appendable> A appendTo(A appendable, Iterator<?> parts) throws IOException {
/* 246 */           Preconditions.checkNotNull(appendable, "appendable");
/* 247 */           Preconditions.checkNotNull(parts, "parts");
/* 248 */           while (parts.hasNext()) {
/* 249 */             Object part = parts.next();
/* 250 */             if (part != null) {
/* 251 */               appendable.append(Joiner.this.toString(part));
/*     */               break;
/*     */             } 
/*     */           } 
/* 255 */           while (parts.hasNext()) {
/* 256 */             Object part = parts.next();
/* 257 */             if (part != null) {
/* 258 */               appendable.append(Joiner.this.separator);
/* 259 */               appendable.append(Joiner.this.toString(part));
/*     */             } 
/*     */           } 
/* 262 */           return appendable;
/*     */         }
/*     */ 
/*     */         
/*     */         public Joiner useForNull(String nullText) {
/* 267 */           throw new UnsupportedOperationException("already specified skipNulls");
/*     */         }
/*     */ 
/*     */         
/*     */         public Joiner.MapJoiner withKeyValueSeparator(String kvs) {
/* 272 */           throw new UnsupportedOperationException("can't use .skipNulls() with maps");
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MapJoiner withKeyValueSeparator(char keyValueSeparator) {
/* 284 */     return withKeyValueSeparator(String.valueOf(keyValueSeparator));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MapJoiner withKeyValueSeparator(String keyValueSeparator) {
/* 292 */     return new MapJoiner(this, keyValueSeparator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class MapJoiner
/*     */   {
/*     */     private final Joiner joiner;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final String keyValueSeparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private MapJoiner(Joiner joiner, String keyValueSeparator) {
/* 318 */       this.joiner = joiner;
/* 319 */       this.keyValueSeparator = Preconditions.<String>checkNotNull(keyValueSeparator);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public <A extends Appendable> A appendTo(A appendable, Map<?, ?> map) throws IOException {
/* 328 */       return appendTo(appendable, map.entrySet());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public StringBuilder appendTo(StringBuilder builder, Map<?, ?> map) {
/* 338 */       return appendTo(builder, map.entrySet());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Beta
/*     */     @CanIgnoreReturnValue
/*     */     public <A extends Appendable> A appendTo(A appendable, Iterable<? extends Map.Entry<?, ?>> entries) throws IOException {
/* 351 */       return appendTo(appendable, entries.iterator());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Beta
/*     */     @CanIgnoreReturnValue
/*     */     public <A extends Appendable> A appendTo(A appendable, Iterator<? extends Map.Entry<?, ?>> parts) throws IOException {
/* 364 */       Preconditions.checkNotNull(appendable);
/* 365 */       if (parts.hasNext()) {
/* 366 */         Map.Entry<?, ?> entry = parts.next();
/* 367 */         appendable.append(this.joiner.toString(entry.getKey()));
/* 368 */         appendable.append(this.keyValueSeparator);
/* 369 */         appendable.append(this.joiner.toString(entry.getValue()));
/* 370 */         while (parts.hasNext()) {
/* 371 */           appendable.append(this.joiner.separator);
/* 372 */           Map.Entry<?, ?> e = parts.next();
/* 373 */           appendable.append(this.joiner.toString(e.getKey()));
/* 374 */           appendable.append(this.keyValueSeparator);
/* 375 */           appendable.append(this.joiner.toString(e.getValue()));
/*     */         } 
/*     */       } 
/* 378 */       return appendable;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Beta
/*     */     @CanIgnoreReturnValue
/*     */     public StringBuilder appendTo(StringBuilder builder, Iterable<? extends Map.Entry<?, ?>> entries) {
/* 391 */       return appendTo(builder, entries.iterator());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Beta
/*     */     @CanIgnoreReturnValue
/*     */     public StringBuilder appendTo(StringBuilder builder, Iterator<? extends Map.Entry<?, ?>> entries) {
/*     */       try {
/* 405 */         appendTo(builder, entries);
/* 406 */       } catch (IOException impossible) {
/* 407 */         throw new AssertionError(impossible);
/*     */       } 
/* 409 */       return builder;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String join(Map<?, ?> map) {
/* 417 */       return join(map.entrySet());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Beta
/*     */     public String join(Iterable<? extends Map.Entry<?, ?>> entries) {
/* 428 */       return join(entries.iterator());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Beta
/*     */     public String join(Iterator<? extends Map.Entry<?, ?>> entries) {
/* 439 */       return appendTo(new StringBuilder(), entries).toString();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MapJoiner useForNull(String nullText) {
/* 447 */       return new MapJoiner(this.joiner.useForNull(nullText), this.keyValueSeparator);
/*     */     }
/*     */   }
/*     */   
/*     */   CharSequence toString(Object part) {
/* 452 */     Preconditions.checkNotNull(part);
/* 453 */     return (part instanceof CharSequence) ? (CharSequence)part : part.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private static Iterable<Object> iterable(final Object first, final Object second, final Object[] rest) {
/* 458 */     Preconditions.checkNotNull(rest);
/* 459 */     return new AbstractList()
/*     */       {
/*     */         public int size() {
/* 462 */           return rest.length + 2;
/*     */         }
/*     */ 
/*     */         
/*     */         public Object get(int index) {
/* 467 */           switch (index) {
/*     */             case 0:
/* 469 */               return first;
/*     */             case 1:
/* 471 */               return second;
/*     */           } 
/* 473 */           return rest[index - 2];
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\base\Joiner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */