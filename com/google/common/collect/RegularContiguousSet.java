/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ final class RegularContiguousSet<C extends Comparable>
/*     */   extends ContiguousSet<C>
/*     */ {
/*     */   private final Range<C> range;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   RegularContiguousSet(Range<C> range, DiscreteDomain<C> domain) {
/*  39 */     super(domain);
/*  40 */     this.range = range;
/*     */   }
/*     */   
/*     */   private ContiguousSet<C> intersectionInCurrentDomain(Range<C> other) {
/*  44 */     return this.range.isConnected(other) ? 
/*  45 */       ContiguousSet.<C>create(this.range.intersection(other), this.domain) : new EmptyContiguousSet<>(this.domain);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   ContiguousSet<C> headSetImpl(C toElement, boolean inclusive) {
/*  51 */     return intersectionInCurrentDomain((Range)Range.upTo((Comparable<?>)toElement, BoundType.forBoolean(inclusive)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   ContiguousSet<C> subSetImpl(C fromElement, boolean fromInclusive, C toElement, boolean toInclusive) {
/*  57 */     if (fromElement.compareTo(toElement) == 0 && !fromInclusive && !toInclusive)
/*     */     {
/*  59 */       return new EmptyContiguousSet<>(this.domain);
/*     */     }
/*  61 */     return intersectionInCurrentDomain(
/*  62 */         (Range)Range.range((Comparable<?>)fromElement, 
/*  63 */           BoundType.forBoolean(fromInclusive), (Comparable<?>)toElement, 
/*  64 */           BoundType.forBoolean(toInclusive)));
/*     */   }
/*     */ 
/*     */   
/*     */   ContiguousSet<C> tailSetImpl(C fromElement, boolean inclusive) {
/*  69 */     return intersectionInCurrentDomain((Range)Range.downTo((Comparable<?>)fromElement, BoundType.forBoolean(inclusive)));
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   int indexOf(Object target) {
/*  75 */     return contains(target) ? (int)this.domain.distance(first(), (C)target) : -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public UnmodifiableIterator<C> iterator() {
/*  80 */     return new AbstractSequentialIterator<C>((Comparable)first()) {
/*  81 */         final C last = RegularContiguousSet.this.last();
/*     */ 
/*     */         
/*     */         protected C computeNext(C previous) {
/*  85 */           return RegularContiguousSet.equalsOrThrow((Comparable<?>)previous, (Comparable<?>)this.last) ? null : RegularContiguousSet.this.domain.next(previous);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public UnmodifiableIterator<C> descendingIterator() {
/*  93 */     return new AbstractSequentialIterator<C>((Comparable)last()) {
/*  94 */         final C first = RegularContiguousSet.this.first();
/*     */ 
/*     */         
/*     */         protected C computeNext(C previous) {
/*  98 */           return RegularContiguousSet.equalsOrThrow((Comparable<?>)previous, (Comparable<?>)this.first) ? null : RegularContiguousSet.this.domain.previous(previous);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private static boolean equalsOrThrow(Comparable<?> left, Comparable<?> right) {
/* 104 */     return (right != null && Range.compareOrThrow(left, right) == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/* 109 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public C first() {
/* 114 */     return this.range.lowerBound.leastValueAbove(this.domain);
/*     */   }
/*     */ 
/*     */   
/*     */   public C last() {
/* 119 */     return this.range.upperBound.greatestValueBelow(this.domain);
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableList<C> createAsList() {
/* 124 */     if (this.domain.supportsFastOffset) {
/* 125 */       return new ImmutableAsList<C>()
/*     */         {
/*     */           ImmutableSortedSet<C> delegateCollection() {
/* 128 */             return RegularContiguousSet.this;
/*     */           }
/*     */ 
/*     */           
/*     */           public C get(int i) {
/* 133 */             Preconditions.checkElementIndex(i, size());
/* 134 */             return RegularContiguousSet.this.domain.offset(RegularContiguousSet.this.first(), i);
/*     */           }
/*     */         };
/*     */     }
/* 138 */     return super.createAsList();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 144 */     long distance = this.domain.distance(first(), last());
/* 145 */     return (distance >= 2147483647L) ? Integer.MAX_VALUE : ((int)distance + 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Object object) {
/* 150 */     if (object == null) {
/* 151 */       return false;
/*     */     }
/*     */     try {
/* 154 */       return this.range.contains((C)object);
/* 155 */     } catch (ClassCastException e) {
/* 156 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsAll(Collection<?> targets) {
/* 162 */     return Collections2.containsAllImpl(this, targets);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 167 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public ContiguousSet<C> intersection(ContiguousSet<C> other) {
/* 172 */     Preconditions.checkNotNull(other);
/* 173 */     Preconditions.checkArgument(this.domain.equals(other.domain));
/* 174 */     if (other.isEmpty()) {
/* 175 */       return other;
/*     */     }
/* 177 */     Comparable<Comparable> comparable1 = (Comparable)Ordering.<Comparable>natural().max(first(), other.first());
/* 178 */     Comparable<Comparable> comparable2 = (Comparable)Ordering.<Comparable>natural().min(last(), other.last());
/* 179 */     return (comparable1.compareTo(comparable2) <= 0) ? 
/* 180 */       ContiguousSet.<C>create((Range)Range.closed(comparable1, comparable2), this.domain) : new EmptyContiguousSet<>(this.domain);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Range<C> range() {
/* 187 */     return range(BoundType.CLOSED, BoundType.CLOSED);
/*     */   }
/*     */ 
/*     */   
/*     */   public Range<C> range(BoundType lowerBoundType, BoundType upperBoundType) {
/* 192 */     return (Range)Range.create(this.range.lowerBound
/* 193 */         .withLowerBoundType(lowerBoundType, this.domain), this.range.upperBound
/* 194 */         .withUpperBoundType(upperBoundType, this.domain));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/* 199 */     if (object == this)
/* 200 */       return true; 
/* 201 */     if (object instanceof RegularContiguousSet) {
/* 202 */       RegularContiguousSet<?> that = (RegularContiguousSet)object;
/* 203 */       if (this.domain.equals(that.domain)) {
/* 204 */         return (first().equals(that.first()) && last().equals(that.last()));
/*     */       }
/*     */     } 
/* 207 */     return super.equals(object);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 213 */     return Sets.hashCodeImpl(this);
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private static final class SerializedForm<C extends Comparable> implements Serializable {
/*     */     final Range<C> range;
/*     */     final DiscreteDomain<C> domain;
/*     */     
/*     */     private SerializedForm(Range<C> range, DiscreteDomain<C> domain) {
/* 222 */       this.range = range;
/* 223 */       this.domain = domain;
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 227 */       return new RegularContiguousSet<>(this.range, this.domain);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   Object writeReplace() {
/* 234 */     return new SerializedForm<>(this.range, this.domain);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\RegularContiguousSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */