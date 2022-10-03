/*     */ package org.mozilla.javascript.commonjs.module.provider;
/*     */ 
/*     */ import java.io.Reader;
/*     */ import java.io.Serializable;
/*     */ import java.net.URI;
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
/*     */ public class ModuleSource
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private final Reader reader;
/*     */   private final Object securityDomain;
/*     */   private final URI uri;
/*     */   private final URI base;
/*     */   private final Object validator;
/*     */   
/*     */   public ModuleSource(Reader reader, Object securityDomain, URI uri, URI base, Object validator) {
/*  54 */     this.reader = reader;
/*  55 */     this.securityDomain = securityDomain;
/*  56 */     this.uri = uri;
/*  57 */     this.base = base;
/*  58 */     this.validator = validator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Reader getReader() {
/*  68 */     return this.reader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getSecurityDomain() {
/*  78 */     return this.securityDomain;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URI getUri() {
/*  86 */     return this.uri;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URI getBase() {
/*  95 */     return this.base;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getValidator() {
/* 105 */     return this.validator;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\commonjs\module\provider\ModuleSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */