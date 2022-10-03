/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ public class AutoPopulatingList<E>
/*     */   implements List<E>, Serializable
/*     */ {
/*     */   private final List<E> backingList;
/*     */   private final ElementFactory<E> elementFactory;
/*     */   
/*     */   public AutoPopulatingList(Class<? extends E> elementClass) {
/*  67 */     this(new ArrayList<>(), elementClass);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AutoPopulatingList(List<E> backingList, Class<? extends E> elementClass) {
/*  76 */     this(backingList, new ReflectiveElementFactory<>(elementClass));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AutoPopulatingList(ElementFactory<E> elementFactory) {
/*  84 */     this(new ArrayList<>(), elementFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AutoPopulatingList(List<E> backingList, ElementFactory<E> elementFactory) {
/*  92 */     Assert.notNull(backingList, "Backing List must not be null");
/*  93 */     Assert.notNull(elementFactory, "Element factory must not be null");
/*  94 */     this.backingList = backingList;
/*  95 */     this.elementFactory = elementFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(int index, E element) {
/* 101 */     this.backingList.add(index, element);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean add(E o) {
/* 106 */     return this.backingList.add(o);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addAll(Collection<? extends E> c) {
/* 111 */     return this.backingList.addAll(c);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addAll(int index, Collection<? extends E> c) {
/* 116 */     return this.backingList.addAll(index, c);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 121 */     this.backingList.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Object o) {
/* 126 */     return this.backingList.contains(o);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsAll(Collection<?> c) {
/* 131 */     return this.backingList.containsAll(c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public E get(int index) {
/* 140 */     int backingListSize = this.backingList.size();
/* 141 */     E element = null;
/* 142 */     if (index < backingListSize) {
/* 143 */       element = this.backingList.get(index);
/* 144 */       if (element == null) {
/* 145 */         element = this.elementFactory.createElement(index);
/* 146 */         this.backingList.set(index, element);
/*     */       } 
/*     */     } else {
/*     */       
/* 150 */       for (int x = backingListSize; x < index; x++) {
/* 151 */         this.backingList.add(null);
/*     */       }
/* 153 */       element = this.elementFactory.createElement(index);
/* 154 */       this.backingList.add(element);
/*     */     } 
/* 156 */     return element;
/*     */   }
/*     */ 
/*     */   
/*     */   public int indexOf(Object o) {
/* 161 */     return this.backingList.indexOf(o);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 166 */     return this.backingList.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<E> iterator() {
/* 171 */     return this.backingList.iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public int lastIndexOf(Object o) {
/* 176 */     return this.backingList.lastIndexOf(o);
/*     */   }
/*     */ 
/*     */   
/*     */   public ListIterator<E> listIterator() {
/* 181 */     return this.backingList.listIterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public ListIterator<E> listIterator(int index) {
/* 186 */     return this.backingList.listIterator(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public E remove(int index) {
/* 191 */     return this.backingList.remove(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean remove(Object o) {
/* 196 */     return this.backingList.remove(o);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeAll(Collection<?> c) {
/* 201 */     return this.backingList.removeAll(c);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean retainAll(Collection<?> c) {
/* 206 */     return this.backingList.retainAll(c);
/*     */   }
/*     */ 
/*     */   
/*     */   public E set(int index, E element) {
/* 211 */     return this.backingList.set(index, element);
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 216 */     return this.backingList.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<E> subList(int fromIndex, int toIndex) {
/* 221 */     return this.backingList.subList(fromIndex, toIndex);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] toArray() {
/* 226 */     return this.backingList.toArray();
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T[] toArray(T[] a) {
/* 231 */     return this.backingList.toArray(a);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object other) {
/* 237 */     return this.backingList.equals(other);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 242 */     return this.backingList.hashCode();
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
/*     */   @FunctionalInterface
/*     */   public static interface ElementFactory<E>
/*     */   {
/*     */     E createElement(int param1Int) throws AutoPopulatingList.ElementInstantiationException;
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
/*     */   public static class ElementInstantiationException
/*     */     extends RuntimeException
/*     */   {
/*     */     public ElementInstantiationException(String msg) {
/* 271 */       super(msg);
/*     */     }
/*     */     
/*     */     public ElementInstantiationException(String message, Throwable cause) {
/* 275 */       super(message, cause);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ReflectiveElementFactory<E>
/*     */     implements ElementFactory<E>, Serializable
/*     */   {
/*     */     private final Class<? extends E> elementClass;
/*     */ 
/*     */ 
/*     */     
/*     */     public ReflectiveElementFactory(Class<? extends E> elementClass) {
/* 289 */       Assert.notNull(elementClass, "Element class must not be null");
/* 290 */       Assert.isTrue(!elementClass.isInterface(), "Element class must not be an interface type");
/* 291 */       Assert.isTrue(!Modifier.isAbstract(elementClass.getModifiers()), "Element class cannot be an abstract class");
/* 292 */       this.elementClass = elementClass;
/*     */     }
/*     */ 
/*     */     
/*     */     public E createElement(int index) {
/*     */       try {
/* 298 */         return ReflectionUtils.<E>accessibleConstructor((Class)this.elementClass, new Class[0]).newInstance(new Object[0]);
/*     */       }
/* 300 */       catch (NoSuchMethodException ex) {
/* 301 */         throw new AutoPopulatingList.ElementInstantiationException("No default constructor on element class: " + this.elementClass
/* 302 */             .getName(), ex);
/*     */       }
/* 304 */       catch (InstantiationException ex) {
/* 305 */         throw new AutoPopulatingList.ElementInstantiationException("Unable to instantiate element class: " + this.elementClass
/* 306 */             .getName(), ex);
/*     */       }
/* 308 */       catch (IllegalAccessException ex) {
/* 309 */         throw new AutoPopulatingList.ElementInstantiationException("Could not access element constructor: " + this.elementClass
/* 310 */             .getName(), ex);
/*     */       }
/* 312 */       catch (InvocationTargetException ex) {
/* 313 */         throw new AutoPopulatingList.ElementInstantiationException("Failed to invoke element constructor: " + this.elementClass
/* 314 */             .getName(), ex.getTargetException());
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\AutoPopulatingList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */