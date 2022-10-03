/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Ints;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class EnumMultiset<E extends Enum<E>>
/*     */   extends AbstractMultiset<E>
/*     */   implements Serializable
/*     */ {
/*     */   private transient Class<E> type;
/*     */   private transient E[] enumConstants;
/*     */   private transient int[] counts;
/*     */   private transient int distinctElements;
/*     */   private transient long size;
/*     */   @GwtIncompatible
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <E extends Enum<E>> EnumMultiset<E> create(Class<E> type) {
/*  52 */     return new EnumMultiset<>(type);
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
/*     */   public static <E extends Enum<E>> EnumMultiset<E> create(Iterable<E> elements) {
/*  64 */     Iterator<E> iterator = elements.iterator();
/*  65 */     Preconditions.checkArgument(iterator.hasNext(), "EnumMultiset constructor passed empty Iterable");
/*  66 */     EnumMultiset<E> multiset = new EnumMultiset<>(((Enum<E>)iterator.next()).getDeclaringClass());
/*  67 */     Iterables.addAll(multiset, elements);
/*  68 */     return multiset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E extends Enum<E>> EnumMultiset<E> create(Iterable<E> elements, Class<E> type) {
/*  78 */     EnumMultiset<E> result = create(type);
/*  79 */     Iterables.addAll(result, elements);
/*  80 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private EnumMultiset(Class<E> type) {
/*  91 */     this.type = type;
/*  92 */     Preconditions.checkArgument(type.isEnum());
/*  93 */     this.enumConstants = type.getEnumConstants();
/*  94 */     this.counts = new int[this.enumConstants.length];
/*     */   }
/*     */   
/*     */   private boolean isActuallyE(Object o) {
/*  98 */     if (o instanceof Enum) {
/*  99 */       Enum<?> e = (Enum)o;
/* 100 */       int index = e.ordinal();
/* 101 */       return (index < this.enumConstants.length && this.enumConstants[index] == e);
/*     */     } 
/* 103 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void checkIsE(Object element) {
/* 111 */     Preconditions.checkNotNull(element);
/* 112 */     if (!isActuallyE(element)) {
/* 113 */       throw new ClassCastException("Expected an " + this.type + " but got " + element);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   int distinctElements() {
/* 119 */     return this.distinctElements;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 124 */     return Ints.saturatedCast(this.size);
/*     */   }
/*     */ 
/*     */   
/*     */   public int count(Object element) {
/* 129 */     if (element == null || !isActuallyE(element)) {
/* 130 */       return 0;
/*     */     }
/* 132 */     Enum<?> e = (Enum)element;
/* 133 */     return this.counts[e.ordinal()];
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public int add(E element, int occurrences) {
/* 140 */     checkIsE(element);
/* 141 */     CollectPreconditions.checkNonnegative(occurrences, "occurrences");
/* 142 */     if (occurrences == 0) {
/* 143 */       return count(element);
/*     */     }
/* 145 */     int index = element.ordinal();
/* 146 */     int oldCount = this.counts[index];
/* 147 */     long newCount = oldCount + occurrences;
/* 148 */     Preconditions.checkArgument((newCount <= 2147483647L), "too many occurrences: %s", newCount);
/* 149 */     this.counts[index] = (int)newCount;
/* 150 */     if (oldCount == 0) {
/* 151 */       this.distinctElements++;
/*     */     }
/* 153 */     this.size += occurrences;
/* 154 */     return oldCount;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public int remove(Object element, int occurrences) {
/* 161 */     if (element == null || !isActuallyE(element)) {
/* 162 */       return 0;
/*     */     }
/* 164 */     Enum<?> e = (Enum)element;
/* 165 */     CollectPreconditions.checkNonnegative(occurrences, "occurrences");
/* 166 */     if (occurrences == 0) {
/* 167 */       return count(element);
/*     */     }
/* 169 */     int index = e.ordinal();
/* 170 */     int oldCount = this.counts[index];
/* 171 */     if (oldCount == 0)
/* 172 */       return 0; 
/* 173 */     if (oldCount <= occurrences) {
/* 174 */       this.counts[index] = 0;
/* 175 */       this.distinctElements--;
/* 176 */       this.size -= oldCount;
/*     */     } else {
/* 178 */       this.counts[index] = oldCount - occurrences;
/* 179 */       this.size -= occurrences;
/*     */     } 
/* 181 */     return oldCount;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public int setCount(E element, int count) {
/* 188 */     checkIsE(element);
/* 189 */     CollectPreconditions.checkNonnegative(count, "count");
/* 190 */     int index = element.ordinal();
/* 191 */     int oldCount = this.counts[index];
/* 192 */     this.counts[index] = count;
/* 193 */     this.size += (count - oldCount);
/* 194 */     if (oldCount == 0 && count > 0) {
/* 195 */       this.distinctElements++;
/* 196 */     } else if (oldCount > 0 && count == 0) {
/* 197 */       this.distinctElements--;
/*     */     } 
/* 199 */     return oldCount;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 204 */     Arrays.fill(this.counts, 0);
/* 205 */     this.size = 0L;
/* 206 */     this.distinctElements = 0;
/*     */   }
/*     */   
/*     */   abstract class Itr<T> implements Iterator<T> {
/* 210 */     int index = 0;
/* 211 */     int toRemove = -1;
/*     */ 
/*     */     
/*     */     abstract T output(int param1Int);
/*     */     
/*     */     public boolean hasNext() {
/* 217 */       for (; this.index < EnumMultiset.this.enumConstants.length; this.index++) {
/* 218 */         if (EnumMultiset.this.counts[this.index] > 0) {
/* 219 */           return true;
/*     */         }
/*     */       } 
/* 222 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public T next() {
/* 227 */       if (!hasNext()) {
/* 228 */         throw new NoSuchElementException();
/*     */       }
/* 230 */       T result = output(this.index);
/* 231 */       this.toRemove = this.index;
/* 232 */       this.index++;
/* 233 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 238 */       CollectPreconditions.checkRemove((this.toRemove >= 0));
/* 239 */       if (EnumMultiset.this.counts[this.toRemove] > 0) {
/* 240 */         EnumMultiset.this.distinctElements--;
/* 241 */         EnumMultiset.this.size = EnumMultiset.this.size - EnumMultiset.this.counts[this.toRemove];
/* 242 */         EnumMultiset.this.counts[this.toRemove] = 0;
/*     */       } 
/* 244 */       this.toRemove = -1;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   Iterator<E> elementIterator() {
/* 250 */     return new Itr<E>()
/*     */       {
/*     */         E output(int index) {
/* 253 */           return (E)EnumMultiset.this.enumConstants[index];
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   Iterator<Multiset.Entry<E>> entryIterator() {
/* 260 */     return new Itr<Multiset.Entry<E>>()
/*     */       {
/*     */         Multiset.Entry<E> output(final int index) {
/* 263 */           return new Multisets.AbstractEntry<E>()
/*     */             {
/*     */               public E getElement() {
/* 266 */                 return (E)EnumMultiset.this.enumConstants[index];
/*     */               }
/*     */ 
/*     */               
/*     */               public int getCount() {
/* 271 */                 return EnumMultiset.this.counts[index];
/*     */               }
/*     */             };
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEachEntry(ObjIntConsumer<? super E> action) {
/* 280 */     Preconditions.checkNotNull(action);
/* 281 */     for (int i = 0; i < this.enumConstants.length; i++) {
/* 282 */       if (this.counts[i] > 0) {
/* 283 */         action.accept(this.enumConstants[i], this.counts[i]);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<E> iterator() {
/* 290 */     return Multisets.iteratorImpl(this);
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 295 */     stream.defaultWriteObject();
/* 296 */     stream.writeObject(this.type);
/* 297 */     Serialization.writeMultiset(this, stream);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 306 */     stream.defaultReadObject();
/*     */     
/* 308 */     Class<E> localType = (Class<E>)stream.readObject();
/* 309 */     this.type = localType;
/* 310 */     this.enumConstants = this.type.getEnumConstants();
/* 311 */     this.counts = new int[this.enumConstants.length];
/* 312 */     Serialization.populateMultiset(this, stream);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\EnumMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */