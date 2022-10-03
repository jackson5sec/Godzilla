/*     */ package com.kichik.pecoff4j;
/*     */ 
/*     */ import com.kichik.pecoff4j.util.IntMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
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
/*     */ public class SectionTable
/*     */ {
/*     */   public static final String RESOURCE_TABLE = ".rsrc";
/*     */   public static final String EXPORT_TABLE = ".edata";
/*     */   public static final String IMPORT_TABLE = ".idata";
/*     */   public static final String LOAD_CONFIG_TABLE = ".rdata";
/*  27 */   private List<SectionHeader> headers = new ArrayList<>();
/*  28 */   private IntMap sections = new IntMap();
/*     */   private RVAConverter rvaConverter;
/*     */   
/*     */   public void add(SectionHeader header) {
/*  32 */     this.headers.add(header);
/*     */   }
/*     */   
/*     */   public int getNumberOfSections() {
/*  36 */     return this.headers.size();
/*     */   }
/*     */   
/*     */   public SectionHeader getHeader(int index) {
/*  40 */     return this.headers.get(index);
/*     */   }
/*     */   
/*     */   public SectionData getSection(int index) {
/*  44 */     return (SectionData)this.sections.get(index);
/*     */   }
/*     */   
/*     */   public void put(int index, SectionData data) {
/*  48 */     this.sections.put(index, data);
/*     */   }
/*     */   
/*     */   public RVAConverter getRVAConverter() {
/*  52 */     return this.rvaConverter;
/*     */   }
/*     */   
/*     */   public void setRvaConverter(RVAConverter rvaConverter) {
/*  56 */     this.rvaConverter = rvaConverter;
/*     */   }
/*     */   
/*     */   public int getFirstSectionRawDataPointer() {
/*  60 */     int pointer = 0;
/*  61 */     for (int i = 0; i < this.headers.size(); i++) {
/*  62 */       SectionHeader sh = this.headers.get(i);
/*  63 */       if (sh.getVirtualSize() > 0 && (pointer == 0 || sh
/*  64 */         .getPointerToRawData() < pointer)) {
/*  65 */         pointer = sh.getPointerToRawData();
/*     */       }
/*     */     } 
/*     */     
/*  69 */     return pointer;
/*     */   }
/*     */   
/*     */   public SectionHeader getLastSectionRawPointerSorted() {
/*  73 */     SectionHeader[] headers = getHeadersPointerSorted();
/*  74 */     if (headers == null || headers.length == 0)
/*  75 */       return null; 
/*  76 */     return headers[headers.length - 1];
/*     */   }
/*     */   
/*     */   public SectionHeader[] getHeadersPointerSorted() {
/*  80 */     List<SectionHeader> headers = new ArrayList<>();
/*  81 */     for (int i = 0; i < getNumberOfSections(); i++) {
/*  82 */       headers.add(getHeader(i));
/*     */     }
/*     */     
/*  85 */     SectionHeader[] sorted = headers.<SectionHeader>toArray(new SectionHeader[0]);
/*  86 */     Arrays.sort(sorted, new Comparator<SectionHeader>()
/*     */         {
/*     */           public int compare(SectionHeader o1, SectionHeader o2) {
/*  89 */             return o1.getVirtualAddress() - o2.getVirtualAddress();
/*     */           }
/*     */         });
/*     */     
/*  93 */     return sorted;
/*     */   }
/*     */   
/*     */   public SectionHeader findHeader(String name) {
/*  97 */     for (SectionHeader sh : this.headers) {
/*  98 */       if (sh.getName().equals(name)) {
/*  99 */         return sh;
/*     */       }
/*     */     } 
/* 102 */     return null;
/*     */   }
/*     */   
/*     */   public SectionData findSection(String name) {
/* 106 */     for (int i = 0; i < this.headers.size(); i++) {
/* 107 */       SectionHeader sh = this.headers.get(i);
/* 108 */       if (sh.getName().equals(name)) {
/* 109 */         return (SectionData)this.sections.get(i);
/*     */       }
/*     */     } 
/* 112 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\SectionTable.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */