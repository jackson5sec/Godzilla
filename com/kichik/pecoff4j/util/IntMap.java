/*     */ package com.kichik.pecoff4j.util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IntMap
/*     */ {
/*     */   private Entry[] hashtable;
/*     */   private int size;
/*     */   
/*     */   public IntMap() {
/*  17 */     this(16);
/*     */   }
/*     */   
/*     */   public IntMap(int size) {
/*  21 */     this.hashtable = new Entry[size];
/*     */   }
/*     */   
/*     */   public void remove(int key) {
/*  25 */     int pos = Math.abs(key % this.hashtable.length);
/*  26 */     Entry e = this.hashtable[pos];
/*  27 */     if (e == null)
/*     */       return; 
/*  29 */     if (e.key == key) {
/*  30 */       this.hashtable[pos] = e.next;
/*  31 */       this.size--;
/*     */     } else {
/*  33 */       Entry prev = e;
/*  34 */       e = e.next;
/*  35 */       while (e != null) {
/*  36 */         if (e.key == key) {
/*  37 */           prev.next = e.next;
/*  38 */           this.size--;
/*     */         } 
/*  40 */         prev = e;
/*  41 */         e = e.next;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void put(int key, Object value) {
/*  47 */     int pos = Math.abs(key % this.hashtable.length);
/*  48 */     Entry e = this.hashtable[pos];
/*  49 */     if (e == null) {
/*  50 */       this.hashtable[pos] = new Entry(key, value);
/*  51 */       this.size++;
/*  52 */     } else if (e.key == key) {
/*  53 */       e.value = value;
/*     */     } else {
/*  55 */       while (e.next != null) {
/*  56 */         if (e.next.key == key) {
/*  57 */           e.next.value = value;
/*     */           return;
/*     */         } 
/*  60 */         e = e.next;
/*     */       } 
/*  62 */       e.next = new Entry(key, value);
/*  63 */       this.size++;
/*     */     } 
/*     */   }
/*     */   
/*     */   public Object get(int key) {
/*  68 */     int pos = Math.abs(key % this.hashtable.length);
/*  69 */     Entry e = this.hashtable[pos];
/*  70 */     if (e == null)
/*  71 */       return null; 
/*  72 */     if (e.key == key) {
/*  73 */       return e.value;
/*     */     }
/*  75 */     while (e.next != null) {
/*  76 */       if (e.next.key == key) {
/*  77 */         return e.next.value;
/*     */       }
/*  79 */       e = e.next;
/*     */     } 
/*     */     
/*  82 */     return null;
/*     */   }
/*     */   
/*     */   public int[] keySet() {
/*  86 */     int[] keys = new int[this.size];
/*  87 */     int idx = 0;
/*  88 */     for (int i = 0; i < this.hashtable.length; i++) {
/*  89 */       Entry e = this.hashtable[i];
/*  90 */       while (e != null) {
/*  91 */         keys[idx++] = e.key;
/*  92 */         e = e.next;
/*     */       } 
/*     */     } 
/*  95 */     return keys;
/*     */   }
/*     */   
/*     */   public int size() {
/*  99 */     return this.size;
/*     */   }
/*     */   
/*     */   private class Entry {
/*     */     public int key;
/*     */     public Object value;
/*     */     public Entry next;
/*     */     
/*     */     public Entry(int key, Object value) {
/* 108 */       this.key = key;
/* 109 */       this.value = value;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4\\util\IntMap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */