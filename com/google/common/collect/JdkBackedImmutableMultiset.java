/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.common.primitives.Ints;
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtCompatible
/*    */ final class JdkBackedImmutableMultiset<E>
/*    */   extends ImmutableMultiset<E>
/*    */ {
/*    */   private final Map<E, Integer> delegateMap;
/*    */   private final ImmutableList<Multiset.Entry<E>> entries;
/*    */   private final long size;
/*    */   private transient ImmutableSet<E> elementSet;
/*    */   
/*    */   static <E> ImmutableMultiset<E> create(Collection<? extends Multiset.Entry<? extends E>> entries) {
/* 39 */     Multiset.Entry[] arrayOfEntry = entries.<Multiset.Entry>toArray(new Multiset.Entry[0]);
/* 40 */     Map<E, Integer> delegateMap = Maps.newHashMapWithExpectedSize(arrayOfEntry.length);
/* 41 */     long size = 0L;
/* 42 */     for (int i = 0; i < arrayOfEntry.length; i++) {
/* 43 */       Multiset.Entry<E> entry = arrayOfEntry[i];
/* 44 */       int count = entry.getCount();
/* 45 */       size += count;
/* 46 */       E element = (E)Preconditions.checkNotNull(entry.getElement());
/* 47 */       delegateMap.put(element, Integer.valueOf(count));
/* 48 */       if (!(entry instanceof Multisets.ImmutableEntry)) {
/* 49 */         arrayOfEntry[i] = Multisets.immutableEntry(element, count);
/*    */       }
/*    */     } 
/* 52 */     return new JdkBackedImmutableMultiset<>(delegateMap, 
/* 53 */         ImmutableList.asImmutableList((Object[])arrayOfEntry), size);
/*    */   }
/*    */ 
/*    */   
/*    */   private JdkBackedImmutableMultiset(Map<E, Integer> delegateMap, ImmutableList<Multiset.Entry<E>> entries, long size) {
/* 58 */     this.delegateMap = delegateMap;
/* 59 */     this.entries = entries;
/* 60 */     this.size = size;
/*    */   }
/*    */ 
/*    */   
/*    */   public int count(Object element) {
/* 65 */     return ((Integer)this.delegateMap.getOrDefault(element, Integer.valueOf(0))).intValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ImmutableSet<E> elementSet() {
/* 72 */     ImmutableSet<E> result = this.elementSet;
/* 73 */     return (result == null) ? (this.elementSet = new ImmutableMultiset.ElementSet<>(this.entries, this)) : result;
/*    */   }
/*    */ 
/*    */   
/*    */   Multiset.Entry<E> getEntry(int index) {
/* 78 */     return this.entries.get(index);
/*    */   }
/*    */ 
/*    */   
/*    */   boolean isPartialView() {
/* 83 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 88 */     return Ints.saturatedCast(this.size);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\JdkBackedImmutableMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */