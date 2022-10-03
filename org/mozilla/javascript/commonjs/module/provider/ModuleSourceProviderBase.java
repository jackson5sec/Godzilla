/*     */ package org.mozilla.javascript.commonjs.module.provider;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import org.mozilla.javascript.ScriptRuntime;
/*     */ import org.mozilla.javascript.Scriptable;
/*     */ import org.mozilla.javascript.ScriptableObject;
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
/*     */ public abstract class ModuleSourceProviderBase
/*     */   implements ModuleSourceProvider, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   public ModuleSource loadSource(String moduleId, Scriptable paths, Object validator) throws IOException, URISyntaxException {
/*  37 */     if (!entityNeedsRevalidation(validator)) {
/*  38 */       return NOT_MODIFIED;
/*     */     }
/*     */     
/*  41 */     ModuleSource moduleSource = loadFromPrivilegedLocations(moduleId, validator);
/*     */     
/*  43 */     if (moduleSource != null) {
/*  44 */       return moduleSource;
/*     */     }
/*  46 */     if (paths != null) {
/*  47 */       moduleSource = loadFromPathArray(moduleId, paths, validator);
/*     */       
/*  49 */       if (moduleSource != null) {
/*  50 */         return moduleSource;
/*     */       }
/*     */     } 
/*  53 */     return loadFromFallbackLocations(moduleId, validator);
/*     */   }
/*     */ 
/*     */   
/*     */   public ModuleSource loadSource(URI uri, URI base, Object validator) throws IOException, URISyntaxException {
/*  58 */     return loadFromUri(uri, base, validator);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private ModuleSource loadFromPathArray(String moduleId, Scriptable paths, Object validator) throws IOException {
/*  64 */     long llength = ScriptRuntime.toUint32(ScriptableObject.getProperty(paths, "length"));
/*     */ 
/*     */     
/*  67 */     int ilength = (llength > 2147483647L) ? Integer.MAX_VALUE : (int)llength;
/*     */ 
/*     */     
/*  70 */     for (int i = 0; i < ilength; i++) {
/*  71 */       String path = ensureTrailingSlash((String)ScriptableObject.getTypedProperty(paths, i, String.class));
/*     */       
/*     */       try {
/*  74 */         URI uri = new URI(path);
/*  75 */         if (!uri.isAbsolute()) {
/*  76 */           uri = (new File(path)).toURI().resolve("");
/*     */         }
/*  78 */         ModuleSource moduleSource = loadFromUri(uri.resolve(moduleId), uri, validator);
/*     */         
/*  80 */         if (moduleSource != null) {
/*  81 */           return moduleSource;
/*     */         }
/*     */       }
/*  84 */       catch (URISyntaxException e) {
/*  85 */         throw new MalformedURLException(e.getMessage());
/*     */       } 
/*     */     } 
/*  88 */     return null;
/*     */   }
/*     */   
/*     */   private static String ensureTrailingSlash(String path) {
/*  92 */     return path.endsWith("/") ? path : path.concat("/");
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
/*     */   
/*     */   protected boolean entityNeedsRevalidation(Object validator) {
/* 108 */     return true;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract ModuleSource loadFromUri(URI paramURI1, URI paramURI2, Object paramObject) throws IOException, URISyntaxException;
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
/*     */   protected ModuleSource loadFromPrivilegedLocations(String moduleId, Object validator) throws IOException, URISyntaxException {
/* 149 */     return null;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ModuleSource loadFromFallbackLocations(String moduleId, Object validator) throws IOException, URISyntaxException {
/* 170 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\commonjs\module\provider\ModuleSourceProviderBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */