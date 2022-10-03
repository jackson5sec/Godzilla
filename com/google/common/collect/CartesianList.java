/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.math.IntMath;
/*     */ import java.util.AbstractList;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.RandomAccess;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ final class CartesianList<E>
/*     */   extends AbstractList<List<E>>
/*     */   implements RandomAccess
/*     */ {
/*     */   private final transient ImmutableList<List<E>> axes;
/*     */   private final transient int[] axesSizeProduct;
/*     */   
/*     */   static <E> List<List<E>> create(List<? extends List<? extends E>> lists) {
/*  39 */     ImmutableList.Builder<List<E>> axesBuilder = new ImmutableList.Builder<>(lists.size());
/*  40 */     for (List<? extends E> list : lists) {
/*  41 */       List<E> copy = ImmutableList.copyOf(list);
/*  42 */       if (copy.isEmpty()) {
/*  43 */         return ImmutableList.of();
/*     */       }
/*  45 */       axesBuilder.add(copy);
/*     */     } 
/*  47 */     return new CartesianList<>(axesBuilder.build());
/*     */   }
/*     */   
/*     */   CartesianList(ImmutableList<List<E>> axes) {
/*  51 */     this.axes = axes;
/*  52 */     int[] axesSizeProduct = new int[axes.size() + 1];
/*  53 */     axesSizeProduct[axes.size()] = 1;
/*     */     try {
/*  55 */       for (int i = axes.size() - 1; i >= 0; i--) {
/*  56 */         axesSizeProduct[i] = IntMath.checkedMultiply(axesSizeProduct[i + 1], ((List)axes.get(i)).size());
/*     */       }
/*  58 */     } catch (ArithmeticException e) {
/*  59 */       throw new IllegalArgumentException("Cartesian product too large; must have size at most Integer.MAX_VALUE");
/*     */     } 
/*     */     
/*  62 */     this.axesSizeProduct = axesSizeProduct;
/*     */   }
/*     */   
/*     */   private int getAxisIndexForProductIndex(int index, int axis) {
/*  66 */     return index / this.axesSizeProduct[axis + 1] % ((List)this.axes.get(axis)).size();
/*     */   }
/*     */ 
/*     */   
/*     */   public int indexOf(Object o) {
/*  71 */     if (!(o instanceof List)) {
/*  72 */       return -1;
/*     */     }
/*  74 */     List<?> list = (List)o;
/*  75 */     if (list.size() != this.axes.size()) {
/*  76 */       return -1;
/*     */     }
/*  78 */     ListIterator<?> itr = list.listIterator();
/*  79 */     int computedIndex = 0;
/*  80 */     while (itr.hasNext()) {
/*  81 */       int axisIndex = itr.nextIndex();
/*  82 */       int elemIndex = ((List)this.axes.get(axisIndex)).indexOf(itr.next());
/*  83 */       if (elemIndex == -1) {
/*  84 */         return -1;
/*     */       }
/*  86 */       computedIndex += elemIndex * this.axesSizeProduct[axisIndex + 1];
/*     */     } 
/*  88 */     return computedIndex;
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableList<E> get(final int index) {
/*  93 */     Preconditions.checkElementIndex(index, size());
/*  94 */     return new ImmutableList<E>()
/*     */       {
/*     */         public int size()
/*     */         {
/*  98 */           return CartesianList.this.axes.size();
/*     */         }
/*     */ 
/*     */         
/*     */         public E get(int axis) {
/* 103 */           Preconditions.checkElementIndex(axis, size());
/* 104 */           int axisIndex = CartesianList.this.getAxisIndexForProductIndex(index, axis);
/* 105 */           return ((List<E>)CartesianList.this.axes.get(axis)).get(axisIndex);
/*     */         }
/*     */ 
/*     */         
/*     */         boolean isPartialView() {
/* 110 */           return true;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 117 */     return this.axesSizeProduct[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Object o) {
/* 122 */     return (indexOf(o) != -1);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\CartesianList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */