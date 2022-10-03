/*     */ package org.springframework.core.env;
/*     */ 
/*     */ import java.util.Map;
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
/*     */ public class SystemEnvironmentPropertySource
/*     */   extends MapPropertySource
/*     */ {
/*     */   public SystemEnvironmentPropertySource(String name, Map<String, Object> source) {
/*  73 */     super(name, source);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsProperty(String name) {
/*  83 */     return (getProperty(name) != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getProperty(String name) {
/*  93 */     String actualName = resolvePropertyName(name);
/*  94 */     if (this.logger.isDebugEnabled() && !name.equals(actualName)) {
/*  95 */       this.logger.debug("PropertySource '" + getName() + "' does not contain property '" + name + "', but found equivalent '" + actualName + "'");
/*     */     }
/*     */     
/*  98 */     return super.getProperty(actualName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final String resolvePropertyName(String name) {
/* 107 */     Assert.notNull(name, "Property name must not be null");
/* 108 */     String resolvedName = checkPropertyName(name);
/* 109 */     if (resolvedName != null) {
/* 110 */       return resolvedName;
/*     */     }
/* 112 */     String uppercasedName = name.toUpperCase();
/* 113 */     if (!name.equals(uppercasedName)) {
/* 114 */       resolvedName = checkPropertyName(uppercasedName);
/* 115 */       if (resolvedName != null) {
/* 116 */         return resolvedName;
/*     */       }
/*     */     } 
/* 119 */     return name;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String checkPropertyName(String name) {
/* 125 */     if (containsKey(name)) {
/* 126 */       return name;
/*     */     }
/*     */     
/* 129 */     String noDotName = name.replace('.', '_');
/* 130 */     if (!name.equals(noDotName) && containsKey(noDotName)) {
/* 131 */       return noDotName;
/*     */     }
/*     */     
/* 134 */     String noHyphenName = name.replace('-', '_');
/* 135 */     if (!name.equals(noHyphenName) && containsKey(noHyphenName)) {
/* 136 */       return noHyphenName;
/*     */     }
/*     */     
/* 139 */     String noDotNoHyphenName = noDotName.replace('-', '_');
/* 140 */     if (!noDotName.equals(noDotNoHyphenName) && containsKey(noDotNoHyphenName)) {
/* 141 */       return noDotNoHyphenName;
/*     */     }
/*     */     
/* 144 */     return null;
/*     */   }
/*     */   
/*     */   private boolean containsKey(String name) {
/* 148 */     return isSecurityManagerPresent() ? this.source.keySet().contains(name) : this.source.containsKey(name);
/*     */   }
/*     */   
/*     */   protected boolean isSecurityManagerPresent() {
/* 152 */     return (System.getSecurityManager() != null);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\env\SystemEnvironmentPropertySource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */