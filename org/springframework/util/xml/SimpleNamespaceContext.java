/*     */ package org.springframework.util.xml;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class SimpleNamespaceContext
/*     */   implements NamespaceContext
/*     */ {
/*  43 */   private final Map<String, String> prefixToNamespaceUri = new HashMap<>();
/*     */   
/*  45 */   private final Map<String, Set<String>> namespaceUriToPrefixes = new HashMap<>();
/*     */   
/*  47 */   private String defaultNamespaceUri = "";
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNamespaceURI(String prefix) {
/*  52 */     Assert.notNull(prefix, "No prefix given");
/*  53 */     if ("xml".equals(prefix)) {
/*  54 */       return "http://www.w3.org/XML/1998/namespace";
/*     */     }
/*  56 */     if ("xmlns".equals(prefix)) {
/*  57 */       return "http://www.w3.org/2000/xmlns/";
/*     */     }
/*  59 */     if ("".equals(prefix)) {
/*  60 */       return this.defaultNamespaceUri;
/*     */     }
/*  62 */     if (this.prefixToNamespaceUri.containsKey(prefix)) {
/*  63 */       return this.prefixToNamespaceUri.get(prefix);
/*     */     }
/*  65 */     return "";
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getPrefix(String namespaceUri) {
/*  71 */     Set<String> prefixes = getPrefixesSet(namespaceUri);
/*  72 */     return !prefixes.isEmpty() ? prefixes.iterator().next() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<String> getPrefixes(String namespaceUri) {
/*  77 */     return getPrefixesSet(namespaceUri).iterator();
/*     */   }
/*     */   
/*     */   private Set<String> getPrefixesSet(String namespaceUri) {
/*  81 */     Assert.notNull(namespaceUri, "No namespaceUri given");
/*  82 */     if (this.defaultNamespaceUri.equals(namespaceUri)) {
/*  83 */       return Collections.singleton("");
/*     */     }
/*  85 */     if ("http://www.w3.org/XML/1998/namespace".equals(namespaceUri)) {
/*  86 */       return Collections.singleton("xml");
/*     */     }
/*  88 */     if ("http://www.w3.org/2000/xmlns/".equals(namespaceUri)) {
/*  89 */       return Collections.singleton("xmlns");
/*     */     }
/*     */     
/*  92 */     Set<String> prefixes = this.namespaceUriToPrefixes.get(namespaceUri);
/*  93 */     return (prefixes != null) ? Collections.<String>unmodifiableSet(prefixes) : Collections.<String>emptySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBindings(Map<String, String> bindings) {
/* 103 */     bindings.forEach(this::bindNamespaceUri);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void bindDefaultNamespaceUri(String namespaceUri) {
/* 111 */     bindNamespaceUri("", namespaceUri);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void bindNamespaceUri(String prefix, String namespaceUri) {
/* 120 */     Assert.notNull(prefix, "No prefix given");
/* 121 */     Assert.notNull(namespaceUri, "No namespaceUri given");
/* 122 */     if ("".equals(prefix)) {
/* 123 */       this.defaultNamespaceUri = namespaceUri;
/*     */     } else {
/*     */       
/* 126 */       this.prefixToNamespaceUri.put(prefix, namespaceUri);
/*     */       
/* 128 */       Set<String> prefixes = this.namespaceUriToPrefixes.computeIfAbsent(namespaceUri, k -> new LinkedHashSet());
/* 129 */       prefixes.add(prefix);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeBinding(@Nullable String prefix) {
/* 138 */     if ("".equals(prefix)) {
/* 139 */       this.defaultNamespaceUri = "";
/*     */     }
/* 141 */     else if (prefix != null) {
/* 142 */       String namespaceUri = this.prefixToNamespaceUri.remove(prefix);
/* 143 */       if (namespaceUri != null) {
/* 144 */         Set<String> prefixes = this.namespaceUriToPrefixes.get(namespaceUri);
/* 145 */         if (prefixes != null) {
/* 146 */           prefixes.remove(prefix);
/* 147 */           if (prefixes.isEmpty()) {
/* 148 */             this.namespaceUriToPrefixes.remove(namespaceUri);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 159 */     this.prefixToNamespaceUri.clear();
/* 160 */     this.namespaceUriToPrefixes.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<String> getBoundPrefixes() {
/* 167 */     return this.prefixToNamespaceUri.keySet().iterator();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\xml\SimpleNamespaceContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */