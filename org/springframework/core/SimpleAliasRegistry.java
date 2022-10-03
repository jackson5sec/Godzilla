/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.util.StringValueResolver;
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
/*     */ public class SimpleAliasRegistry
/*     */   implements AliasRegistry
/*     */ {
/*  46 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */ 
/*     */   
/*  49 */   private final Map<String, String> aliasMap = new ConcurrentHashMap<>(16);
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerAlias(String name, String alias) {
/*  54 */     Assert.hasText(name, "'name' must not be empty");
/*  55 */     Assert.hasText(alias, "'alias' must not be empty");
/*  56 */     synchronized (this.aliasMap) {
/*  57 */       if (alias.equals(name)) {
/*  58 */         this.aliasMap.remove(alias);
/*  59 */         if (this.logger.isDebugEnabled()) {
/*  60 */           this.logger.debug("Alias definition '" + alias + "' ignored since it points to same name");
/*     */         }
/*     */       } else {
/*     */         
/*  64 */         String registeredName = this.aliasMap.get(alias);
/*  65 */         if (registeredName != null) {
/*  66 */           if (registeredName.equals(name)) {
/*     */             return;
/*     */           }
/*     */           
/*  70 */           if (!allowAliasOverriding()) {
/*  71 */             throw new IllegalStateException("Cannot define alias '" + alias + "' for name '" + name + "': It is already registered for name '" + registeredName + "'.");
/*     */           }
/*     */           
/*  74 */           if (this.logger.isDebugEnabled()) {
/*  75 */             this.logger.debug("Overriding alias '" + alias + "' definition for registered name '" + registeredName + "' with new target name '" + name + "'");
/*     */           }
/*     */         } 
/*     */         
/*  79 */         checkForAliasCircle(name, alias);
/*  80 */         this.aliasMap.put(alias, name);
/*  81 */         if (this.logger.isTraceEnabled()) {
/*  82 */           this.logger.trace("Alias definition '" + alias + "' registered for name '" + name + "'");
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean allowAliasOverriding() {
/*  93 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasAlias(String name, String alias) {
/* 103 */     String registeredName = this.aliasMap.get(alias);
/* 104 */     return (ObjectUtils.nullSafeEquals(registeredName, name) || (registeredName != null && 
/* 105 */       hasAlias(name, registeredName)));
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeAlias(String alias) {
/* 110 */     synchronized (this.aliasMap) {
/* 111 */       String name = this.aliasMap.remove(alias);
/* 112 */       if (name == null) {
/* 113 */         throw new IllegalStateException("No alias '" + alias + "' registered");
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAlias(String name) {
/* 120 */     return this.aliasMap.containsKey(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getAliases(String name) {
/* 125 */     List<String> result = new ArrayList<>();
/* 126 */     synchronized (this.aliasMap) {
/* 127 */       retrieveAliases(name, result);
/*     */     } 
/* 129 */     return StringUtils.toStringArray(result);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void retrieveAliases(String name, List<String> result) {
/* 138 */     this.aliasMap.forEach((alias, registeredName) -> {
/*     */           if (registeredName.equals(name)) {
/*     */             result.add(alias);
/*     */             retrieveAliases(alias, result);
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resolveAliases(StringValueResolver valueResolver) {
/* 154 */     Assert.notNull(valueResolver, "StringValueResolver must not be null");
/* 155 */     synchronized (this.aliasMap) {
/* 156 */       Map<String, String> aliasCopy = new HashMap<>(this.aliasMap);
/* 157 */       aliasCopy.forEach((alias, registeredName) -> {
/*     */             String resolvedAlias = valueResolver.resolveStringValue(alias);
/*     */             String resolvedName = valueResolver.resolveStringValue(registeredName);
/*     */             if (resolvedAlias == null || resolvedName == null || resolvedAlias.equals(resolvedName)) {
/*     */               this.aliasMap.remove(alias);
/*     */             } else if (!resolvedAlias.equals(alias)) {
/*     */               String existingName = this.aliasMap.get(resolvedAlias);
/*     */               if (existingName != null) {
/*     */                 if (existingName.equals(resolvedName)) {
/*     */                   this.aliasMap.remove(alias);
/*     */                   return;
/*     */                 } 
/*     */                 throw new IllegalStateException("Cannot register resolved alias '" + resolvedAlias + "' (original: '" + alias + "') for name '" + resolvedName + "': It is already registered for name '" + registeredName + "'.");
/*     */               } 
/*     */               checkForAliasCircle(resolvedName, resolvedAlias);
/*     */               this.aliasMap.remove(alias);
/*     */               this.aliasMap.put(resolvedAlias, resolvedName);
/*     */             } else if (!registeredName.equals(resolvedName)) {
/*     */               this.aliasMap.put(alias, resolvedName);
/*     */             } 
/*     */           });
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkForAliasCircle(String name, String alias) {
/* 197 */     if (hasAlias(alias, name)) {
/* 198 */       throw new IllegalStateException("Cannot register alias '" + alias + "' for name '" + name + "': Circular reference - '" + name + "' is a direct or indirect alias for '" + alias + "' already");
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
/*     */   public String canonicalName(String name) {
/* 210 */     String canonicalName = name;
/*     */ 
/*     */     
/*     */     while (true) {
/* 214 */       String resolvedName = this.aliasMap.get(canonicalName);
/* 215 */       if (resolvedName != null) {
/* 216 */         canonicalName = resolvedName;
/*     */       }
/*     */       
/* 219 */       if (resolvedName == null)
/* 220 */         return canonicalName; 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\SimpleAliasRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */