/*     */ package org.springframework.core.env;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Spliterator;
/*     */ import java.util.Spliterators;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import java.util.stream.Stream;
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
/*     */ public class MutablePropertySources
/*     */   implements PropertySources
/*     */ {
/*  44 */   private final List<PropertySource<?>> propertySourceList = new CopyOnWriteArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MutablePropertySources() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MutablePropertySources(PropertySources propertySources) {
/*  58 */     this();
/*  59 */     for (PropertySource<?> propertySource : (Iterable<PropertySource<?>>)propertySources) {
/*  60 */       addLast(propertySource);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<PropertySource<?>> iterator() {
/*  67 */     return this.propertySourceList.iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public Spliterator<PropertySource<?>> spliterator() {
/*  72 */     return Spliterators.spliterator(this.propertySourceList, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public Stream<PropertySource<?>> stream() {
/*  77 */     return this.propertySourceList.stream();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(String name) {
/*  82 */     for (PropertySource<?> propertySource : this.propertySourceList) {
/*  83 */       if (propertySource.getName().equals(name)) {
/*  84 */         return true;
/*     */       }
/*     */     } 
/*  87 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public PropertySource<?> get(String name) {
/*  93 */     for (PropertySource<?> propertySource : this.propertySourceList) {
/*  94 */       if (propertySource.getName().equals(name)) {
/*  95 */         return propertySource;
/*     */       }
/*     */     } 
/*  98 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFirst(PropertySource<?> propertySource) {
/* 106 */     synchronized (this.propertySourceList) {
/* 107 */       removeIfPresent(propertySource);
/* 108 */       this.propertySourceList.add(0, propertySource);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addLast(PropertySource<?> propertySource) {
/* 116 */     synchronized (this.propertySourceList) {
/* 117 */       removeIfPresent(propertySource);
/* 118 */       this.propertySourceList.add(propertySource);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addBefore(String relativePropertySourceName, PropertySource<?> propertySource) {
/* 127 */     assertLegalRelativeAddition(relativePropertySourceName, propertySource);
/* 128 */     synchronized (this.propertySourceList) {
/* 129 */       removeIfPresent(propertySource);
/* 130 */       int index = assertPresentAndGetIndex(relativePropertySourceName);
/* 131 */       addAtIndex(index, propertySource);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAfter(String relativePropertySourceName, PropertySource<?> propertySource) {
/* 140 */     assertLegalRelativeAddition(relativePropertySourceName, propertySource);
/* 141 */     synchronized (this.propertySourceList) {
/* 142 */       removeIfPresent(propertySource);
/* 143 */       int index = assertPresentAndGetIndex(relativePropertySourceName);
/* 144 */       addAtIndex(index + 1, propertySource);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int precedenceOf(PropertySource<?> propertySource) {
/* 152 */     return this.propertySourceList.indexOf(propertySource);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public PropertySource<?> remove(String name) {
/* 161 */     synchronized (this.propertySourceList) {
/* 162 */       int index = this.propertySourceList.indexOf(PropertySource.named(name));
/* 163 */       return (index != -1) ? this.propertySourceList.remove(index) : null;
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
/*     */   public void replace(String name, PropertySource<?> propertySource) {
/* 175 */     synchronized (this.propertySourceList) {
/* 176 */       int index = assertPresentAndGetIndex(name);
/* 177 */       this.propertySourceList.set(index, propertySource);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 185 */     return this.propertySourceList.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 190 */     return this.propertySourceList.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void assertLegalRelativeAddition(String relativePropertySourceName, PropertySource<?> propertySource) {
/* 198 */     String newPropertySourceName = propertySource.getName();
/* 199 */     if (relativePropertySourceName.equals(newPropertySourceName)) {
/* 200 */       throw new IllegalArgumentException("PropertySource named '" + newPropertySourceName + "' cannot be added relative to itself");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void removeIfPresent(PropertySource<?> propertySource) {
/* 209 */     this.propertySourceList.remove(propertySource);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addAtIndex(int index, PropertySource<?> propertySource) {
/* 216 */     removeIfPresent(propertySource);
/* 217 */     this.propertySourceList.add(index, propertySource);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int assertPresentAndGetIndex(String name) {
/* 226 */     int index = this.propertySourceList.indexOf(PropertySource.named(name));
/* 227 */     if (index == -1) {
/* 228 */       throw new IllegalArgumentException("PropertySource named '" + name + "' does not exist");
/*     */     }
/* 230 */     return index;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\env\MutablePropertySources.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */