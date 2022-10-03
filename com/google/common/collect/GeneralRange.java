/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.util.Comparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(serializable = true)
/*     */ final class GeneralRange<T>
/*     */   implements Serializable
/*     */ {
/*     */   private final Comparator<? super T> comparator;
/*     */   private final boolean hasLowerBound;
/*     */   private final T lowerEndpoint;
/*     */   private final BoundType lowerBoundType;
/*     */   private final boolean hasUpperBound;
/*     */   private final T upperEndpoint;
/*     */   private final BoundType upperBoundType;
/*     */   private transient GeneralRange<T> reverse;
/*     */   
/*     */   static <T extends Comparable> GeneralRange<T> from(Range<T> range) {
/*  42 */     C c1 = range.hasLowerBound() ? (C)range.lowerEndpoint() : null;
/*  43 */     BoundType lowerBoundType = range.hasLowerBound() ? range.lowerBoundType() : BoundType.OPEN;
/*     */     
/*  45 */     C c2 = range.hasUpperBound() ? (C)range.upperEndpoint() : null;
/*  46 */     BoundType upperBoundType = range.hasUpperBound() ? range.upperBoundType() : BoundType.OPEN;
/*  47 */     return new GeneralRange<>(
/*  48 */         Ordering.natural(), range
/*  49 */         .hasLowerBound(), (T)c1, lowerBoundType, range
/*     */ 
/*     */         
/*  52 */         .hasUpperBound(), (T)c2, upperBoundType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T> GeneralRange<T> all(Comparator<? super T> comparator) {
/*  59 */     return new GeneralRange<>(comparator, false, null, BoundType.OPEN, false, null, BoundType.OPEN);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T> GeneralRange<T> downTo(Comparator<? super T> comparator, T endpoint, BoundType boundType) {
/*  68 */     return new GeneralRange<>(comparator, true, endpoint, boundType, false, null, BoundType.OPEN);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T> GeneralRange<T> upTo(Comparator<? super T> comparator, T endpoint, BoundType boundType) {
/*  77 */     return new GeneralRange<>(comparator, false, null, BoundType.OPEN, true, endpoint, boundType);
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
/*     */   static <T> GeneralRange<T> range(Comparator<? super T> comparator, T lower, BoundType lowerType, T upper, BoundType upperType) {
/*  90 */     return new GeneralRange<>(comparator, true, lower, lowerType, true, upper, upperType);
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
/*     */   private GeneralRange(Comparator<? super T> comparator, boolean hasLowerBound, T lowerEndpoint, BoundType lowerBoundType, boolean hasUpperBound, T upperEndpoint, BoundType upperBoundType) {
/* 109 */     this.comparator = (Comparator<? super T>)Preconditions.checkNotNull(comparator);
/* 110 */     this.hasLowerBound = hasLowerBound;
/* 111 */     this.hasUpperBound = hasUpperBound;
/* 112 */     this.lowerEndpoint = lowerEndpoint;
/* 113 */     this.lowerBoundType = (BoundType)Preconditions.checkNotNull(lowerBoundType);
/* 114 */     this.upperEndpoint = upperEndpoint;
/* 115 */     this.upperBoundType = (BoundType)Preconditions.checkNotNull(upperBoundType);
/*     */     
/* 117 */     if (hasLowerBound) {
/* 118 */       comparator.compare(lowerEndpoint, lowerEndpoint);
/*     */     }
/* 120 */     if (hasUpperBound) {
/* 121 */       comparator.compare(upperEndpoint, upperEndpoint);
/*     */     }
/* 123 */     if (hasLowerBound && hasUpperBound) {
/* 124 */       int cmp = comparator.compare(lowerEndpoint, upperEndpoint);
/*     */       
/* 126 */       Preconditions.checkArgument((cmp <= 0), "lowerEndpoint (%s) > upperEndpoint (%s)", lowerEndpoint, upperEndpoint);
/*     */       
/* 128 */       if (cmp == 0) {
/* 129 */         Preconditions.checkArgument(((lowerBoundType != BoundType.OPEN)) | ((upperBoundType != BoundType.OPEN)));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   Comparator<? super T> comparator() {
/* 135 */     return this.comparator;
/*     */   }
/*     */   
/*     */   boolean hasLowerBound() {
/* 139 */     return this.hasLowerBound;
/*     */   }
/*     */   
/*     */   boolean hasUpperBound() {
/* 143 */     return this.hasUpperBound;
/*     */   }
/*     */   
/*     */   boolean isEmpty() {
/* 147 */     return ((hasUpperBound() && tooLow(getUpperEndpoint())) || (
/* 148 */       hasLowerBound() && tooHigh(getLowerEndpoint())));
/*     */   }
/*     */   
/*     */   boolean tooLow(T t) {
/* 152 */     if (!hasLowerBound()) {
/* 153 */       return false;
/*     */     }
/* 155 */     T lbound = getLowerEndpoint();
/* 156 */     int cmp = this.comparator.compare(t, lbound);
/* 157 */     return ((cmp < 0) ? 1 : 0) | ((cmp == 0)) & ((getLowerBoundType() == BoundType.OPEN));
/*     */   }
/*     */   
/*     */   boolean tooHigh(T t) {
/* 161 */     if (!hasUpperBound()) {
/* 162 */       return false;
/*     */     }
/* 164 */     T ubound = getUpperEndpoint();
/* 165 */     int cmp = this.comparator.compare(t, ubound);
/* 166 */     return ((cmp > 0) ? 1 : 0) | ((cmp == 0)) & ((getUpperBoundType() == BoundType.OPEN));
/*     */   }
/*     */   
/*     */   boolean contains(T t) {
/* 170 */     return (!tooLow(t) && !tooHigh(t));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   GeneralRange<T> intersect(GeneralRange<T> other) {
/* 177 */     Preconditions.checkNotNull(other);
/* 178 */     Preconditions.checkArgument(this.comparator.equals(other.comparator));
/*     */     
/* 180 */     boolean hasLowBound = this.hasLowerBound;
/* 181 */     T lowEnd = getLowerEndpoint();
/* 182 */     BoundType lowType = getLowerBoundType();
/* 183 */     if (!hasLowerBound()) {
/* 184 */       hasLowBound = other.hasLowerBound;
/* 185 */       lowEnd = other.getLowerEndpoint();
/* 186 */       lowType = other.getLowerBoundType();
/* 187 */     } else if (other.hasLowerBound()) {
/* 188 */       int cmp = this.comparator.compare(getLowerEndpoint(), other.getLowerEndpoint());
/* 189 */       if (cmp < 0 || (cmp == 0 && other.getLowerBoundType() == BoundType.OPEN)) {
/* 190 */         lowEnd = other.getLowerEndpoint();
/* 191 */         lowType = other.getLowerBoundType();
/*     */       } 
/*     */     } 
/*     */     
/* 195 */     boolean hasUpBound = this.hasUpperBound;
/* 196 */     T upEnd = getUpperEndpoint();
/* 197 */     BoundType upType = getUpperBoundType();
/* 198 */     if (!hasUpperBound()) {
/* 199 */       hasUpBound = other.hasUpperBound;
/* 200 */       upEnd = other.getUpperEndpoint();
/* 201 */       upType = other.getUpperBoundType();
/* 202 */     } else if (other.hasUpperBound()) {
/* 203 */       int cmp = this.comparator.compare(getUpperEndpoint(), other.getUpperEndpoint());
/* 204 */       if (cmp > 0 || (cmp == 0 && other.getUpperBoundType() == BoundType.OPEN)) {
/* 205 */         upEnd = other.getUpperEndpoint();
/* 206 */         upType = other.getUpperBoundType();
/*     */       } 
/*     */     } 
/*     */     
/* 210 */     if (hasLowBound && hasUpBound) {
/* 211 */       int cmp = this.comparator.compare(lowEnd, upEnd);
/* 212 */       if (cmp > 0 || (cmp == 0 && lowType == BoundType.OPEN && upType == BoundType.OPEN)) {
/*     */         
/* 214 */         lowEnd = upEnd;
/* 215 */         lowType = BoundType.OPEN;
/* 216 */         upType = BoundType.CLOSED;
/*     */       } 
/*     */     } 
/*     */     
/* 220 */     return new GeneralRange(this.comparator, hasLowBound, lowEnd, lowType, hasUpBound, upEnd, upType);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 225 */     if (obj instanceof GeneralRange) {
/* 226 */       GeneralRange<?> r = (GeneralRange)obj;
/* 227 */       return (this.comparator.equals(r.comparator) && this.hasLowerBound == r.hasLowerBound && this.hasUpperBound == r.hasUpperBound && 
/*     */ 
/*     */         
/* 230 */         getLowerBoundType().equals(r.getLowerBoundType()) && 
/* 231 */         getUpperBoundType().equals(r.getUpperBoundType()) && 
/* 232 */         Objects.equal(getLowerEndpoint(), r.getLowerEndpoint()) && 
/* 233 */         Objects.equal(getUpperEndpoint(), r.getUpperEndpoint()));
/*     */     } 
/* 235 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 240 */     return Objects.hashCode(new Object[] { this.comparator, 
/*     */           
/* 242 */           getLowerEndpoint(), 
/* 243 */           getLowerBoundType(), 
/* 244 */           getUpperEndpoint(), 
/* 245 */           getUpperBoundType() });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   GeneralRange<T> reverse() {
/* 252 */     GeneralRange<T> result = this.reverse;
/* 253 */     if (result == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 262 */       result = new GeneralRange(Ordering.<T>from(this.comparator).reverse(), this.hasUpperBound, getUpperEndpoint(), getUpperBoundType(), this.hasLowerBound, getLowerEndpoint(), getLowerBoundType());
/* 263 */       result.reverse = this;
/* 264 */       return this.reverse = result;
/*     */     } 
/* 266 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 271 */     return this.comparator + ":" + ((this.lowerBoundType == BoundType.CLOSED) ? 91 : 40) + (this.hasLowerBound ? (String)this.lowerEndpoint : "-∞") + ',' + (this.hasUpperBound ? (String)this.upperEndpoint : "∞") + ((this.upperBoundType == BoundType.CLOSED) ? 93 : 41);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   T getLowerEndpoint() {
/* 281 */     return this.lowerEndpoint;
/*     */   }
/*     */   
/*     */   BoundType getLowerBoundType() {
/* 285 */     return this.lowerBoundType;
/*     */   }
/*     */   
/*     */   T getUpperEndpoint() {
/* 289 */     return this.upperEndpoint;
/*     */   }
/*     */   
/*     */   BoundType getUpperBoundType() {
/* 293 */     return this.upperBoundType;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\GeneralRange.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */