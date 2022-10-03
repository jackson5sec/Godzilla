/*     */ package org.springframework.core.type.filter;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.type.ClassMetadata;
/*     */ import org.springframework.core.type.classreading.MetadataReader;
/*     */ import org.springframework.core.type.classreading.MetadataReaderFactory;
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
/*     */ public abstract class AbstractTypeHierarchyTraversingFilter
/*     */   implements TypeFilter
/*     */ {
/*  43 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   private final boolean considerInherited;
/*     */   
/*     */   private final boolean considerInterfaces;
/*     */ 
/*     */   
/*     */   protected AbstractTypeHierarchyTraversingFilter(boolean considerInherited, boolean considerInterfaces) {
/*  51 */     this.considerInherited = considerInherited;
/*  52 */     this.considerInterfaces = considerInterfaces;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
/*  62 */     if (matchSelf(metadataReader)) {
/*  63 */       return true;
/*     */     }
/*  65 */     ClassMetadata metadata = metadataReader.getClassMetadata();
/*  66 */     if (matchClassName(metadata.getClassName())) {
/*  67 */       return true;
/*     */     }
/*     */     
/*  70 */     if (this.considerInherited) {
/*  71 */       String superClassName = metadata.getSuperClassName();
/*  72 */       if (superClassName != null) {
/*     */         
/*  74 */         Boolean superClassMatch = matchSuperClass(superClassName);
/*  75 */         if (superClassMatch != null) {
/*  76 */           if (superClassMatch.booleanValue()) {
/*  77 */             return true;
/*     */           }
/*     */         } else {
/*     */ 
/*     */           
/*     */           try {
/*  83 */             if (match(metadata.getSuperClassName(), metadataReaderFactory)) {
/*  84 */               return true;
/*     */             }
/*     */           }
/*  87 */           catch (IOException ex) {
/*  88 */             if (this.logger.isDebugEnabled()) {
/*  89 */               this.logger.debug("Could not read super class [" + metadata.getSuperClassName() + "] of type-filtered class [" + metadata
/*  90 */                   .getClassName() + "]");
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  97 */     if (this.considerInterfaces) {
/*  98 */       for (String ifc : metadata.getInterfaceNames()) {
/*     */         
/* 100 */         Boolean interfaceMatch = matchInterface(ifc);
/* 101 */         if (interfaceMatch != null) {
/* 102 */           if (interfaceMatch.booleanValue()) {
/* 103 */             return true;
/*     */           }
/*     */         } else {
/*     */ 
/*     */           
/*     */           try {
/* 109 */             if (match(ifc, metadataReaderFactory)) {
/* 110 */               return true;
/*     */             }
/*     */           }
/* 113 */           catch (IOException ex) {
/* 114 */             if (this.logger.isDebugEnabled()) {
/* 115 */               this.logger.debug("Could not read interface [" + ifc + "] for type-filtered class [" + metadata
/* 116 */                   .getClassName() + "]");
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 123 */     return false;
/*     */   }
/*     */   
/*     */   private boolean match(String className, MetadataReaderFactory metadataReaderFactory) throws IOException {
/* 127 */     return match(metadataReaderFactory.getMetadataReader(className), metadataReaderFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean matchSelf(MetadataReader metadataReader) {
/* 136 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean matchClassName(String className) {
/* 143 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Boolean matchSuperClass(String superClassName) {
/* 151 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Boolean matchInterface(String interfaceName) {
/* 159 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\type\filter\AbstractTypeHierarchyTraversingFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */