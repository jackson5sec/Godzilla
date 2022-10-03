/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.ObjectOutputStream;
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.Set;
/*    */ import java.util.function.ObjIntConsumer;
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
/*    */ @GwtCompatible(serializable = true, emulated = true)
/*    */ public final class HashMultiset<E>
/*    */   extends AbstractMapBasedMultiset<E>
/*    */ {
/*    */   @GwtIncompatible
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   public static <E> HashMultiset<E> create() {
/* 38 */     return new HashMultiset<>();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <E> HashMultiset<E> create(int distinctElements) {
/* 49 */     return new HashMultiset<>(distinctElements);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <E> HashMultiset<E> create(Iterable<? extends E> elements) {
/* 60 */     HashMultiset<E> multiset = create(Multisets.inferDistinctElements(elements));
/* 61 */     Iterables.addAll(multiset, elements);
/* 62 */     return multiset;
/*    */   }
/*    */   
/*    */   private HashMultiset() {
/* 66 */     super(new HashMap<>());
/*    */   }
/*    */   
/*    */   private HashMultiset(int distinctElements) {
/* 70 */     super(Maps.newHashMapWithExpectedSize(distinctElements));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @GwtIncompatible
/*    */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 79 */     stream.defaultWriteObject();
/* 80 */     Serialization.writeMultiset(this, stream);
/*    */   }
/*    */   
/*    */   @GwtIncompatible
/*    */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 85 */     stream.defaultReadObject();
/* 86 */     int distinctElements = Serialization.readCount(stream);
/* 87 */     setBackingMap(Maps.newHashMap());
/* 88 */     Serialization.populateMultiset(this, stream, distinctElements);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\HashMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */