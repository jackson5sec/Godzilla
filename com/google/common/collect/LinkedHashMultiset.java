/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.ObjectOutputStream;
/*    */ import java.util.Iterator;
/*    */ import java.util.LinkedHashMap;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtCompatible(serializable = true, emulated = true)
/*    */ public final class LinkedHashMultiset<E>
/*    */   extends AbstractMapBasedMultiset<E>
/*    */ {
/*    */   @GwtIncompatible
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   public static <E> LinkedHashMultiset<E> create() {
/* 46 */     return new LinkedHashMultiset<>();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <E> LinkedHashMultiset<E> create(int distinctElements) {
/* 57 */     return new LinkedHashMultiset<>(distinctElements);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <E> LinkedHashMultiset<E> create(Iterable<? extends E> elements) {
/* 68 */     LinkedHashMultiset<E> multiset = create(Multisets.inferDistinctElements(elements));
/* 69 */     Iterables.addAll(multiset, elements);
/* 70 */     return multiset;
/*    */   }
/*    */   
/*    */   private LinkedHashMultiset() {
/* 74 */     super(new LinkedHashMap<>());
/*    */   }
/*    */   
/*    */   private LinkedHashMultiset(int distinctElements) {
/* 78 */     super(Maps.newLinkedHashMapWithExpectedSize(distinctElements));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @GwtIncompatible
/*    */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 87 */     stream.defaultWriteObject();
/* 88 */     Serialization.writeMultiset(this, stream);
/*    */   }
/*    */   
/*    */   @GwtIncompatible
/*    */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 93 */     stream.defaultReadObject();
/* 94 */     int distinctElements = Serialization.readCount(stream);
/* 95 */     setBackingMap(new LinkedHashMap<>());
/* 96 */     Serialization.populateMultiset(this, stream, distinctElements);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\LinkedHashMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */