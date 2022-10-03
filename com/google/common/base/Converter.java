/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.ForOverride;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import java.io.Serializable;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class Converter<A, B>
/*     */   implements Function<A, B>
/*     */ {
/*     */   private final boolean handleNullAutomatically;
/*     */   @LazyInit
/*     */   private transient Converter<B, A> reverse;
/*     */   
/*     */   protected Converter() {
/* 125 */     this(true);
/*     */   }
/*     */ 
/*     */   
/*     */   Converter(boolean handleNullAutomatically) {
/* 130 */     this.handleNullAutomatically = handleNullAutomatically;
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
/*     */   @ForOverride
/*     */   protected abstract B doForward(A paramA);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ForOverride
/*     */   protected abstract A doBackward(B paramB);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final B convert(A a) {
/* 169 */     return correctedDoForward(a);
/*     */   }
/*     */ 
/*     */   
/*     */   B correctedDoForward(A a) {
/* 174 */     if (this.handleNullAutomatically)
/*     */     {
/* 176 */       return (a == null) ? null : Preconditions.<B>checkNotNull(doForward(a));
/*     */     }
/* 178 */     return doForward(a);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   A correctedDoBackward(B b) {
/* 184 */     if (this.handleNullAutomatically)
/*     */     {
/* 186 */       return (b == null) ? null : Preconditions.<A>checkNotNull(doBackward(b));
/*     */     }
/* 188 */     return doBackward(b);
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
/*     */   @CanIgnoreReturnValue
/*     */   public Iterable<B> convertAll(final Iterable<? extends A> fromIterable) {
/* 202 */     Preconditions.checkNotNull(fromIterable, "fromIterable");
/* 203 */     return new Iterable<B>()
/*     */       {
/*     */         public Iterator<B> iterator() {
/* 206 */           return new Iterator<B>() {
/* 207 */               private final Iterator<? extends A> fromIterator = fromIterable.iterator();
/*     */ 
/*     */               
/*     */               public boolean hasNext() {
/* 211 */                 return this.fromIterator.hasNext();
/*     */               }
/*     */ 
/*     */               
/*     */               public B next() {
/* 216 */                 return (B)Converter.this.convert(this.fromIterator.next());
/*     */               }
/*     */ 
/*     */               
/*     */               public void remove() {
/* 221 */                 this.fromIterator.remove();
/*     */               }
/*     */             };
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
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public Converter<B, A> reverse() {
/* 238 */     Converter<B, A> result = this.reverse;
/* 239 */     return (result == null) ? (this.reverse = new ReverseConverter<>(this)) : result;
/*     */   }
/*     */   
/*     */   private static final class ReverseConverter<A, B> extends Converter<B, A> implements Serializable {
/*     */     final Converter<A, B> original;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ReverseConverter(Converter<A, B> original) {
/* 247 */       this.original = original;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected A doForward(B b) {
/* 259 */       throw new AssertionError();
/*     */     }
/*     */ 
/*     */     
/*     */     protected B doBackward(A a) {
/* 264 */       throw new AssertionError();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     A correctedDoForward(B b) {
/* 270 */       return this.original.correctedDoBackward(b);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     B correctedDoBackward(A a) {
/* 276 */       return this.original.correctedDoForward(a);
/*     */     }
/*     */ 
/*     */     
/*     */     public Converter<A, B> reverse() {
/* 281 */       return this.original;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object object) {
/* 286 */       if (object instanceof ReverseConverter) {
/* 287 */         ReverseConverter<?, ?> that = (ReverseConverter<?, ?>)object;
/* 288 */         return this.original.equals(that.original);
/*     */       } 
/* 290 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 295 */       return this.original.hashCode() ^ 0xFFFFFFFF;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 300 */       return this.original + ".reverse()";
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
/*     */   public final <C> Converter<A, C> andThen(Converter<B, C> secondConverter) {
/* 314 */     return doAndThen(secondConverter);
/*     */   }
/*     */ 
/*     */   
/*     */   <C> Converter<A, C> doAndThen(Converter<B, C> secondConverter) {
/* 319 */     return new ConverterComposition<>(this, Preconditions.<Converter<B, C>>checkNotNull(secondConverter));
/*     */   }
/*     */   
/*     */   private static final class ConverterComposition<A, B, C> extends Converter<A, C> implements Serializable {
/*     */     final Converter<A, B> first;
/*     */     final Converter<B, C> second;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ConverterComposition(Converter<A, B> first, Converter<B, C> second) {
/* 328 */       this.first = first;
/* 329 */       this.second = second;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected C doForward(A a) {
/* 341 */       throw new AssertionError();
/*     */     }
/*     */ 
/*     */     
/*     */     protected A doBackward(C c) {
/* 346 */       throw new AssertionError();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     C correctedDoForward(A a) {
/* 352 */       return this.second.correctedDoForward(this.first.correctedDoForward(a));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     A correctedDoBackward(C c) {
/* 358 */       return this.first.correctedDoBackward(this.second.correctedDoBackward(c));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object object) {
/* 363 */       if (object instanceof ConverterComposition) {
/* 364 */         ConverterComposition<?, ?, ?> that = (ConverterComposition<?, ?, ?>)object;
/* 365 */         return (this.first.equals(that.first) && this.second.equals(that.second));
/*     */       } 
/* 367 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 372 */       return 31 * this.first.hashCode() + this.second.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 377 */       return this.first + ".andThen(" + this.second + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   public final B apply(A a) {
/* 390 */     return convert(a);
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
/*     */   public boolean equals(Object object) {
/* 406 */     return super.equals(object);
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
/*     */   public static <A, B> Converter<A, B> from(Function<? super A, ? extends B> forwardFunction, Function<? super B, ? extends A> backwardFunction) {
/* 428 */     return new FunctionBasedConverter<>(forwardFunction, backwardFunction);
/*     */   }
/*     */   
/*     */   private static final class FunctionBasedConverter<A, B>
/*     */     extends Converter<A, B>
/*     */     implements Serializable
/*     */   {
/*     */     private final Function<? super A, ? extends B> forwardFunction;
/*     */     private final Function<? super B, ? extends A> backwardFunction;
/*     */     
/*     */     private FunctionBasedConverter(Function<? super A, ? extends B> forwardFunction, Function<? super B, ? extends A> backwardFunction) {
/* 439 */       this.forwardFunction = Preconditions.<Function<? super A, ? extends B>>checkNotNull(forwardFunction);
/* 440 */       this.backwardFunction = Preconditions.<Function<? super B, ? extends A>>checkNotNull(backwardFunction);
/*     */     }
/*     */ 
/*     */     
/*     */     protected B doForward(A a) {
/* 445 */       return this.forwardFunction.apply(a);
/*     */     }
/*     */ 
/*     */     
/*     */     protected A doBackward(B b) {
/* 450 */       return this.backwardFunction.apply(b);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object object) {
/* 455 */       if (object instanceof FunctionBasedConverter) {
/* 456 */         FunctionBasedConverter<?, ?> that = (FunctionBasedConverter<?, ?>)object;
/* 457 */         return (this.forwardFunction.equals(that.forwardFunction) && this.backwardFunction
/* 458 */           .equals(that.backwardFunction));
/*     */       } 
/* 460 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 465 */       return this.forwardFunction.hashCode() * 31 + this.backwardFunction.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 470 */       return "Converter.from(" + this.forwardFunction + ", " + this.backwardFunction + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Converter<T, T> identity() {
/* 477 */     return IdentityConverter.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class IdentityConverter<T>
/*     */     extends Converter<T, T>
/*     */     implements Serializable
/*     */   {
/* 485 */     static final IdentityConverter INSTANCE = new IdentityConverter();
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     protected T doForward(T t) {
/* 489 */       return t;
/*     */     }
/*     */ 
/*     */     
/*     */     protected T doBackward(T t) {
/* 494 */       return t;
/*     */     }
/*     */ 
/*     */     
/*     */     public IdentityConverter<T> reverse() {
/* 499 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     <S> Converter<T, S> doAndThen(Converter<T, S> otherConverter) {
/* 504 */       return Preconditions.<Converter<T, S>>checkNotNull(otherConverter, "otherConverter");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 514 */       return "Converter.identity()";
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 518 */       return INSTANCE;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\base\Converter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */