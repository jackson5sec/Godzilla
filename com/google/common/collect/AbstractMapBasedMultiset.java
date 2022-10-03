/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Ints;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectStreamException;
/*     */ import java.io.Serializable;
/*     */ import java.util.ConcurrentModificationException;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.ObjIntConsumer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ abstract class AbstractMapBasedMultiset<E>
/*     */   extends AbstractMultiset<E>
/*     */   implements Serializable
/*     */ {
/*     */   private transient Map<E, Count> backingMap;
/*     */   private transient long size;
/*     */   @GwtIncompatible
/*     */   private static final long serialVersionUID = -2250766705698539974L;
/*     */   
/*     */   protected AbstractMapBasedMultiset(Map<E, Count> backingMap) {
/*  61 */     Preconditions.checkArgument(backingMap.isEmpty());
/*  62 */     this.backingMap = backingMap;
/*     */   }
/*     */ 
/*     */   
/*     */   void setBackingMap(Map<E, Count> backingMap) {
/*  67 */     this.backingMap = backingMap;
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
/*     */   public Set<Multiset.Entry<E>> entrySet() {
/*  81 */     return super.entrySet();
/*     */   }
/*     */ 
/*     */   
/*     */   Iterator<E> elementIterator() {
/*  86 */     final Iterator<Map.Entry<E, Count>> backingEntries = this.backingMap.entrySet().iterator();
/*  87 */     return new Iterator<E>()
/*     */       {
/*     */         Map.Entry<E, Count> toRemove;
/*     */         
/*     */         public boolean hasNext() {
/*  92 */           return backingEntries.hasNext();
/*     */         }
/*     */ 
/*     */         
/*     */         public E next() {
/*  97 */           Map.Entry<E, Count> mapEntry = backingEntries.next();
/*  98 */           this.toRemove = mapEntry;
/*  99 */           return mapEntry.getKey();
/*     */         }
/*     */ 
/*     */         
/*     */         public void remove() {
/* 104 */           CollectPreconditions.checkRemove((this.toRemove != null));
/* 105 */           AbstractMapBasedMultiset.this.size = AbstractMapBasedMultiset.this.size - ((Count)this.toRemove.getValue()).getAndSet(0);
/* 106 */           backingEntries.remove();
/* 107 */           this.toRemove = null;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   Iterator<Multiset.Entry<E>> entryIterator() {
/* 114 */     final Iterator<Map.Entry<E, Count>> backingEntries = this.backingMap.entrySet().iterator();
/* 115 */     return (Iterator)new Iterator<Multiset.Entry<Multiset.Entry<E>>>()
/*     */       {
/*     */         Map.Entry<E, Count> toRemove;
/*     */         
/*     */         public boolean hasNext() {
/* 120 */           return backingEntries.hasNext();
/*     */         }
/*     */ 
/*     */         
/*     */         public Multiset.Entry<E> next() {
/* 125 */           final Map.Entry<E, Count> mapEntry = backingEntries.next();
/* 126 */           this.toRemove = mapEntry;
/* 127 */           return new Multisets.AbstractEntry<E>()
/*     */             {
/*     */               public E getElement() {
/* 130 */                 return (E)mapEntry.getKey();
/*     */               }
/*     */ 
/*     */               
/*     */               public int getCount() {
/* 135 */                 Count count = (Count)mapEntry.getValue();
/* 136 */                 if (count == null || count.get() == 0) {
/* 137 */                   Count frequency = (Count)AbstractMapBasedMultiset.this.backingMap.get(getElement());
/* 138 */                   if (frequency != null) {
/* 139 */                     return frequency.get();
/*     */                   }
/*     */                 } 
/* 142 */                 return (count == null) ? 0 : count.get();
/*     */               }
/*     */             };
/*     */         }
/*     */ 
/*     */         
/*     */         public void remove() {
/* 149 */           CollectPreconditions.checkRemove((this.toRemove != null));
/* 150 */           AbstractMapBasedMultiset.this.size = AbstractMapBasedMultiset.this.size - ((Count)this.toRemove.getValue()).getAndSet(0);
/* 151 */           backingEntries.remove();
/* 152 */           this.toRemove = null;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEachEntry(ObjIntConsumer<? super E> action) {
/* 159 */     Preconditions.checkNotNull(action);
/* 160 */     this.backingMap.forEach((element, count) -> action.accept(element, count.get()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 165 */     for (Count frequency : this.backingMap.values()) {
/* 166 */       frequency.set(0);
/*     */     }
/* 168 */     this.backingMap.clear();
/* 169 */     this.size = 0L;
/*     */   }
/*     */ 
/*     */   
/*     */   int distinctElements() {
/* 174 */     return this.backingMap.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 181 */     return Ints.saturatedCast(this.size);
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<E> iterator() {
/* 186 */     return new MapBasedMultisetIterator();
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
/*     */   private class MapBasedMultisetIterator
/*     */     implements Iterator<E>
/*     */   {
/* 201 */     final Iterator<Map.Entry<E, Count>> entryIterator = AbstractMapBasedMultiset.this.backingMap.entrySet().iterator();
/*     */     
/*     */     Map.Entry<E, Count> currentEntry;
/*     */     
/*     */     public boolean hasNext() {
/* 206 */       return (this.occurrencesLeft > 0 || this.entryIterator.hasNext());
/*     */     }
/*     */     int occurrencesLeft; boolean canRemove;
/*     */     
/*     */     public E next() {
/* 211 */       if (this.occurrencesLeft == 0) {
/* 212 */         this.currentEntry = this.entryIterator.next();
/* 213 */         this.occurrencesLeft = ((Count)this.currentEntry.getValue()).get();
/*     */       } 
/* 215 */       this.occurrencesLeft--;
/* 216 */       this.canRemove = true;
/* 217 */       return this.currentEntry.getKey();
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 222 */       CollectPreconditions.checkRemove(this.canRemove);
/* 223 */       int frequency = ((Count)this.currentEntry.getValue()).get();
/* 224 */       if (frequency <= 0) {
/* 225 */         throw new ConcurrentModificationException();
/*     */       }
/* 227 */       if (((Count)this.currentEntry.getValue()).addAndGet(-1) == 0) {
/* 228 */         this.entryIterator.remove();
/*     */       }
/* 230 */       AbstractMapBasedMultiset.this.size--;
/* 231 */       this.canRemove = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int count(Object element) {
/* 237 */     Count frequency = Maps.<Count>safeGet(this.backingMap, element);
/* 238 */     return (frequency == null) ? 0 : frequency.get();
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
/*     */   public int add(E element, int occurrences) {
/*     */     int oldCount;
/* 252 */     if (occurrences == 0) {
/* 253 */       return count(element);
/*     */     }
/* 255 */     Preconditions.checkArgument((occurrences > 0), "occurrences cannot be negative: %s", occurrences);
/* 256 */     Count frequency = this.backingMap.get(element);
/*     */     
/* 258 */     if (frequency == null) {
/* 259 */       oldCount = 0;
/* 260 */       this.backingMap.put(element, new Count(occurrences));
/*     */     } else {
/* 262 */       oldCount = frequency.get();
/* 263 */       long newCount = oldCount + occurrences;
/* 264 */       Preconditions.checkArgument((newCount <= 2147483647L), "too many occurrences: %s", newCount);
/* 265 */       frequency.add(occurrences);
/*     */     } 
/* 267 */     this.size += occurrences;
/* 268 */     return oldCount;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public int remove(Object element, int occurrences) {
/*     */     int numberRemoved;
/* 274 */     if (occurrences == 0) {
/* 275 */       return count(element);
/*     */     }
/* 277 */     Preconditions.checkArgument((occurrences > 0), "occurrences cannot be negative: %s", occurrences);
/* 278 */     Count frequency = this.backingMap.get(element);
/* 279 */     if (frequency == null) {
/* 280 */       return 0;
/*     */     }
/*     */     
/* 283 */     int oldCount = frequency.get();
/*     */ 
/*     */     
/* 286 */     if (oldCount > occurrences) {
/* 287 */       numberRemoved = occurrences;
/*     */     } else {
/* 289 */       numberRemoved = oldCount;
/* 290 */       this.backingMap.remove(element);
/*     */     } 
/*     */     
/* 293 */     frequency.add(-numberRemoved);
/* 294 */     this.size -= numberRemoved;
/* 295 */     return oldCount;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public int setCount(E element, int count) {
/*     */     int oldCount;
/* 302 */     CollectPreconditions.checkNonnegative(count, "count");
/*     */ 
/*     */ 
/*     */     
/* 306 */     if (count == 0) {
/* 307 */       Count existingCounter = this.backingMap.remove(element);
/* 308 */       oldCount = getAndSet(existingCounter, count);
/*     */     } else {
/* 310 */       Count existingCounter = this.backingMap.get(element);
/* 311 */       oldCount = getAndSet(existingCounter, count);
/*     */       
/* 313 */       if (existingCounter == null) {
/* 314 */         this.backingMap.put(element, new Count(count));
/*     */       }
/*     */     } 
/*     */     
/* 318 */     this.size += (count - oldCount);
/* 319 */     return oldCount;
/*     */   }
/*     */   
/*     */   private static int getAndSet(Count i, int count) {
/* 323 */     if (i == null) {
/* 324 */       return 0;
/*     */     }
/*     */     
/* 327 */     return i.getAndSet(count);
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private void readObjectNoData() throws ObjectStreamException {
/* 333 */     throw new InvalidObjectException("Stream data required");
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\AbstractMapBasedMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */