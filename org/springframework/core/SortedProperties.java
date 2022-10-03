/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.StringWriter;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
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
/*     */ class SortedProperties
/*     */   extends Properties
/*     */ {
/*  52 */   static final String EOL = System.lineSeparator();
/*     */   
/*  54 */   private static final Comparator<Object> keyComparator = Comparator.comparing(String::valueOf);
/*     */   
/*  56 */   private static final Comparator<Map.Entry<Object, Object>> entryComparator = Map.Entry.comparingByKey(keyComparator);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean omitComments;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   SortedProperties(boolean omitComments) {
/*  69 */     this.omitComments = omitComments;
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
/*     */   
/*     */   SortedProperties(Properties properties, boolean omitComments) {
/*  84 */     this(omitComments);
/*  85 */     putAll(properties);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void store(OutputStream out, @Nullable String comments) throws IOException {
/*  91 */     ByteArrayOutputStream baos = new ByteArrayOutputStream();
/*  92 */     super.store(baos, this.omitComments ? null : comments);
/*  93 */     String contents = baos.toString(StandardCharsets.ISO_8859_1.name());
/*  94 */     for (String line : contents.split(EOL)) {
/*  95 */       if (!this.omitComments || !line.startsWith("#")) {
/*  96 */         out.write((line + EOL).getBytes(StandardCharsets.ISO_8859_1));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void store(Writer writer, @Nullable String comments) throws IOException {
/* 103 */     StringWriter stringWriter = new StringWriter();
/* 104 */     super.store(stringWriter, this.omitComments ? null : comments);
/* 105 */     String contents = stringWriter.toString();
/* 106 */     for (String line : contents.split(EOL)) {
/* 107 */       if (!this.omitComments || !line.startsWith("#")) {
/* 108 */         writer.write(line + EOL);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void storeToXML(OutputStream out, @Nullable String comments) throws IOException {
/* 115 */     super.storeToXML(out, this.omitComments ? null : comments);
/*     */   }
/*     */ 
/*     */   
/*     */   public void storeToXML(OutputStream out, @Nullable String comments, String encoding) throws IOException {
/* 120 */     super.storeToXML(out, this.omitComments ? null : comments, encoding);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Enumeration<Object> keys() {
/* 129 */     return Collections.enumeration(keySet());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Object> keySet() {
/* 140 */     Set<Object> sortedKeys = new TreeSet(keyComparator);
/* 141 */     sortedKeys.addAll(super.keySet());
/* 142 */     return Collections.synchronizedSet(sortedKeys);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Map.Entry<Object, Object>> entrySet() {
/* 153 */     Set<Map.Entry<Object, Object>> sortedEntries = new TreeSet<>(entryComparator);
/* 154 */     sortedEntries.addAll(super.entrySet());
/* 155 */     return Collections.synchronizedSet(sortedEntries);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\SortedProperties.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */