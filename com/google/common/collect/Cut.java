/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Booleans;
/*     */ import java.io.Serializable;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ abstract class Cut<C extends Comparable>
/*     */   implements Comparable<Cut<C>>, Serializable
/*     */ {
/*     */   final C endpoint;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   Cut(C endpoint) {
/*  39 */     this.endpoint = endpoint;
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
/*     */   
/*     */   Cut<C> canonical(DiscreteDomain<C> domain) {
/*  65 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(Cut<C> that) {
/*  71 */     if (that == belowAll()) {
/*  72 */       return 1;
/*     */     }
/*  74 */     if (that == aboveAll()) {
/*  75 */       return -1;
/*     */     }
/*  77 */     int result = Range.compareOrThrow((Comparable)this.endpoint, (Comparable)that.endpoint);
/*  78 */     if (result != 0) {
/*  79 */       return result;
/*     */     }
/*     */     
/*  82 */     return Booleans.compare(this instanceof AboveValue, that instanceof AboveValue);
/*     */   }
/*     */   
/*     */   C endpoint() {
/*  86 */     return this.endpoint;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  92 */     if (obj instanceof Cut) {
/*     */       
/*  94 */       Cut<C> that = (Cut<C>)obj;
/*     */       try {
/*  96 */         int compareResult = compareTo(that);
/*  97 */         return (compareResult == 0);
/*  98 */       } catch (ClassCastException classCastException) {}
/*     */     } 
/*     */     
/* 101 */     return false;
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
/*     */   static <C extends Comparable> Cut<C> belowAll() {
/* 114 */     return BelowAll.INSTANCE;
/*     */   }
/*     */   
/*     */   private static final class BelowAll
/*     */     extends Cut<Comparable<?>>
/*     */   {
/* 120 */     private static final BelowAll INSTANCE = new BelowAll(); private static final long serialVersionUID = 0L;
/*     */     
/*     */     private BelowAll() {
/* 123 */       super(null);
/*     */     }
/*     */ 
/*     */     
/*     */     Comparable<?> endpoint() {
/* 128 */       throw new IllegalStateException("range unbounded on this side");
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isLessThan(Comparable<?> value) {
/* 133 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     BoundType typeAsLowerBound() {
/* 138 */       throw new IllegalStateException();
/*     */     }
/*     */ 
/*     */     
/*     */     BoundType typeAsUpperBound() {
/* 143 */       throw new AssertionError("this statement should be unreachable");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     Cut<Comparable<?>> withLowerBoundType(BoundType boundType, DiscreteDomain<Comparable<?>> domain) {
/* 149 */       throw new IllegalStateException();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     Cut<Comparable<?>> withUpperBoundType(BoundType boundType, DiscreteDomain<Comparable<?>> domain) {
/* 155 */       throw new AssertionError("this statement should be unreachable");
/*     */     }
/*     */ 
/*     */     
/*     */     void describeAsLowerBound(StringBuilder sb) {
/* 160 */       sb.append("(-∞");
/*     */     }
/*     */ 
/*     */     
/*     */     void describeAsUpperBound(StringBuilder sb) {
/* 165 */       throw new AssertionError();
/*     */     }
/*     */ 
/*     */     
/*     */     Comparable<?> leastValueAbove(DiscreteDomain<Comparable<?>> domain) {
/* 170 */       return domain.minValue();
/*     */     }
/*     */ 
/*     */     
/*     */     Comparable<?> greatestValueBelow(DiscreteDomain<Comparable<?>> domain) {
/* 175 */       throw new AssertionError();
/*     */     }
/*     */ 
/*     */     
/*     */     Cut<Comparable<?>> canonical(DiscreteDomain<Comparable<?>> domain) {
/*     */       try {
/* 181 */         return Cut.belowValue(domain.minValue());
/* 182 */       } catch (NoSuchElementException e) {
/* 183 */         return this;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public int compareTo(Cut<Comparable<?>> o) {
/* 189 */       return (o == this) ? 0 : -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 194 */       return System.identityHashCode(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 199 */       return "-∞";
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 203 */       return INSTANCE;
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
/*     */   static <C extends Comparable> Cut<C> aboveAll() {
/* 215 */     return AboveAll.INSTANCE;
/*     */   }
/*     */   
/*     */   private static final class AboveAll extends Cut<Comparable<?>> {
/* 219 */     private static final AboveAll INSTANCE = new AboveAll(); private static final long serialVersionUID = 0L;
/*     */     
/*     */     private AboveAll() {
/* 222 */       super(null);
/*     */     }
/*     */ 
/*     */     
/*     */     Comparable<?> endpoint() {
/* 227 */       throw new IllegalStateException("range unbounded on this side");
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isLessThan(Comparable<?> value) {
/* 232 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     BoundType typeAsLowerBound() {
/* 237 */       throw new AssertionError("this statement should be unreachable");
/*     */     }
/*     */ 
/*     */     
/*     */     BoundType typeAsUpperBound() {
/* 242 */       throw new IllegalStateException();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     Cut<Comparable<?>> withLowerBoundType(BoundType boundType, DiscreteDomain<Comparable<?>> domain) {
/* 248 */       throw new AssertionError("this statement should be unreachable");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     Cut<Comparable<?>> withUpperBoundType(BoundType boundType, DiscreteDomain<Comparable<?>> domain) {
/* 254 */       throw new IllegalStateException();
/*     */     }
/*     */ 
/*     */     
/*     */     void describeAsLowerBound(StringBuilder sb) {
/* 259 */       throw new AssertionError();
/*     */     }
/*     */ 
/*     */     
/*     */     void describeAsUpperBound(StringBuilder sb) {
/* 264 */       sb.append("+∞)");
/*     */     }
/*     */ 
/*     */     
/*     */     Comparable<?> leastValueAbove(DiscreteDomain<Comparable<?>> domain) {
/* 269 */       throw new AssertionError();
/*     */     }
/*     */ 
/*     */     
/*     */     Comparable<?> greatestValueBelow(DiscreteDomain<Comparable<?>> domain) {
/* 274 */       return domain.maxValue();
/*     */     }
/*     */ 
/*     */     
/*     */     public int compareTo(Cut<Comparable<?>> o) {
/* 279 */       return (o == this) ? 0 : 1;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 284 */       return System.identityHashCode(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 289 */       return "+∞";
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 293 */       return INSTANCE;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static <C extends Comparable> Cut<C> belowValue(C endpoint) {
/* 300 */     return new BelowValue<>(endpoint);
/*     */   }
/*     */   private static final class BelowValue<C extends Comparable> extends Cut<C> { private static final long serialVersionUID = 0L;
/*     */     
/*     */     BelowValue(C endpoint) {
/* 305 */       super((C)Preconditions.checkNotNull(endpoint));
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isLessThan(C value) {
/* 310 */       return (Range.compareOrThrow((Comparable)this.endpoint, (Comparable)value) <= 0);
/*     */     }
/*     */ 
/*     */     
/*     */     BoundType typeAsLowerBound() {
/* 315 */       return BoundType.CLOSED;
/*     */     }
/*     */ 
/*     */     
/*     */     BoundType typeAsUpperBound() {
/* 320 */       return BoundType.OPEN;
/*     */     }
/*     */     
/*     */     Cut<C> withLowerBoundType(BoundType boundType, DiscreteDomain<C> domain) {
/*     */       C previous;
/* 325 */       switch (boundType) {
/*     */         case CLOSED:
/* 327 */           return this;
/*     */         case OPEN:
/* 329 */           previous = domain.previous(this.endpoint);
/* 330 */           return (previous == null) ? Cut.<C>belowAll() : new Cut.AboveValue<>(previous);
/*     */       } 
/* 332 */       throw new AssertionError();
/*     */     }
/*     */ 
/*     */     
/*     */     Cut<C> withUpperBoundType(BoundType boundType, DiscreteDomain<C> domain) {
/*     */       C previous;
/* 338 */       switch (boundType) {
/*     */         case CLOSED:
/* 340 */           previous = domain.previous(this.endpoint);
/* 341 */           return (previous == null) ? Cut.<C>aboveAll() : new Cut.AboveValue<>(previous);
/*     */         case OPEN:
/* 343 */           return this;
/*     */       } 
/* 345 */       throw new AssertionError();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     void describeAsLowerBound(StringBuilder sb) {
/* 351 */       sb.append('[').append(this.endpoint);
/*     */     }
/*     */ 
/*     */     
/*     */     void describeAsUpperBound(StringBuilder sb) {
/* 356 */       sb.append(this.endpoint).append(')');
/*     */     }
/*     */ 
/*     */     
/*     */     C leastValueAbove(DiscreteDomain<C> domain) {
/* 361 */       return this.endpoint;
/*     */     }
/*     */ 
/*     */     
/*     */     C greatestValueBelow(DiscreteDomain<C> domain) {
/* 366 */       return domain.previous(this.endpoint);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 371 */       return this.endpoint.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 376 */       return "\\" + this.endpoint + "/";
/*     */     } } abstract boolean isLessThan(C paramC); abstract BoundType typeAsLowerBound(); abstract BoundType typeAsUpperBound(); abstract Cut<C> withLowerBoundType(BoundType paramBoundType, DiscreteDomain<C> paramDiscreteDomain);
/*     */   abstract Cut<C> withUpperBoundType(BoundType paramBoundType, DiscreteDomain<C> paramDiscreteDomain);
/*     */   abstract void describeAsLowerBound(StringBuilder paramStringBuilder);
/*     */   abstract void describeAsUpperBound(StringBuilder paramStringBuilder);
/*     */   abstract C leastValueAbove(DiscreteDomain<C> paramDiscreteDomain);
/*     */   static <C extends Comparable> Cut<C> aboveValue(C endpoint) {
/* 383 */     return new AboveValue<>(endpoint);
/*     */   }
/*     */   abstract C greatestValueBelow(DiscreteDomain<C> paramDiscreteDomain);
/*     */   public abstract int hashCode();
/*     */   private static final class AboveValue<C extends Comparable> extends Cut<C> { AboveValue(C endpoint) {
/* 388 */       super((C)Preconditions.checkNotNull(endpoint));
/*     */     }
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     boolean isLessThan(C value) {
/* 393 */       return (Range.compareOrThrow((Comparable)this.endpoint, (Comparable)value) < 0);
/*     */     }
/*     */ 
/*     */     
/*     */     BoundType typeAsLowerBound() {
/* 398 */       return BoundType.OPEN;
/*     */     }
/*     */ 
/*     */     
/*     */     BoundType typeAsUpperBound() {
/* 403 */       return BoundType.CLOSED;
/*     */     }
/*     */     
/*     */     Cut<C> withLowerBoundType(BoundType boundType, DiscreteDomain<C> domain) {
/*     */       C next;
/* 408 */       switch (boundType) {
/*     */         case OPEN:
/* 410 */           return this;
/*     */         case CLOSED:
/* 412 */           next = domain.next(this.endpoint);
/* 413 */           return (next == null) ? Cut.<C>belowAll() : belowValue(next);
/*     */       } 
/* 415 */       throw new AssertionError();
/*     */     }
/*     */ 
/*     */     
/*     */     Cut<C> withUpperBoundType(BoundType boundType, DiscreteDomain<C> domain) {
/*     */       C next;
/* 421 */       switch (boundType) {
/*     */         case OPEN:
/* 423 */           next = domain.next(this.endpoint);
/* 424 */           return (next == null) ? Cut.<C>aboveAll() : belowValue(next);
/*     */         case CLOSED:
/* 426 */           return this;
/*     */       } 
/* 428 */       throw new AssertionError();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     void describeAsLowerBound(StringBuilder sb) {
/* 434 */       sb.append('(').append(this.endpoint);
/*     */     }
/*     */ 
/*     */     
/*     */     void describeAsUpperBound(StringBuilder sb) {
/* 439 */       sb.append(this.endpoint).append(']');
/*     */     }
/*     */ 
/*     */     
/*     */     C leastValueAbove(DiscreteDomain<C> domain) {
/* 444 */       return domain.next(this.endpoint);
/*     */     }
/*     */ 
/*     */     
/*     */     C greatestValueBelow(DiscreteDomain<C> domain) {
/* 449 */       return this.endpoint;
/*     */     }
/*     */ 
/*     */     
/*     */     Cut<C> canonical(DiscreteDomain<C> domain) {
/* 454 */       C next = leastValueAbove(domain);
/* 455 */       return (next != null) ? belowValue(next) : Cut.<C>aboveAll();
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 460 */       return this.endpoint.hashCode() ^ 0xFFFFFFFF;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 465 */       return "/" + this.endpoint + "\\";
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\Cut.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */