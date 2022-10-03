/*     */ package org.springframework.core.io.support;
/*     */ 
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.util.Properties;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.PropertiesPersister;
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
/*     */ public abstract class PropertiesLoaderSupport
/*     */ {
/*  44 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   @Nullable
/*     */   protected Properties[] localProperties;
/*     */   
/*     */   protected boolean localOverride = false;
/*     */   
/*     */   @Nullable
/*     */   private Resource[] locations;
/*     */   
/*     */   private boolean ignoreResourceNotFound = false;
/*     */   
/*     */   @Nullable
/*     */   private String fileEncoding;
/*     */   
/*  59 */   private PropertiesPersister propertiesPersister = (PropertiesPersister)ResourcePropertiesPersister.INSTANCE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProperties(Properties properties) {
/*  68 */     this.localProperties = new Properties[] { properties };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPropertiesArray(Properties... propertiesArray) {
/*  76 */     this.localProperties = propertiesArray;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLocation(Resource location) {
/*  85 */     this.locations = new Resource[] { location };
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
/*     */   public void setLocations(Resource... locations) {
/*  98 */     this.locations = locations;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLocalOverride(boolean localOverride) {
/* 108 */     this.localOverride = localOverride;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIgnoreResourceNotFound(boolean ignoreResourceNotFound) {
/* 117 */     this.ignoreResourceNotFound = ignoreResourceNotFound;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFileEncoding(String encoding) {
/* 128 */     this.fileEncoding = encoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPropertiesPersister(@Nullable PropertiesPersister propertiesPersister) {
/* 137 */     this.propertiesPersister = (propertiesPersister != null) ? propertiesPersister : (PropertiesPersister)ResourcePropertiesPersister.INSTANCE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Properties mergeProperties() throws IOException {
/* 147 */     Properties result = new Properties();
/*     */     
/* 149 */     if (this.localOverride)
/*     */     {
/* 151 */       loadProperties(result);
/*     */     }
/*     */     
/* 154 */     if (this.localProperties != null) {
/* 155 */       for (Properties localProp : this.localProperties) {
/* 156 */         CollectionUtils.mergePropertiesIntoMap(localProp, result);
/*     */       }
/*     */     }
/*     */     
/* 160 */     if (!this.localOverride)
/*     */     {
/* 162 */       loadProperties(result);
/*     */     }
/*     */     
/* 165 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void loadProperties(Properties props) throws IOException {
/* 175 */     if (this.locations != null)
/* 176 */       for (Resource location : this.locations) {
/* 177 */         if (this.logger.isTraceEnabled()) {
/* 178 */           this.logger.trace("Loading properties file from " + location);
/*     */         }
/*     */         try {
/* 181 */           PropertiesLoaderUtils.fillProperties(props, new EncodedResource(location, this.fileEncoding), this.propertiesPersister);
/*     */         
/*     */         }
/* 184 */         catch (FileNotFoundException|java.net.UnknownHostException|java.net.SocketException ex) {
/* 185 */           if (this.ignoreResourceNotFound) {
/* 186 */             if (this.logger.isDebugEnabled()) {
/* 187 */               this.logger.debug("Properties resource not found: " + ex.getMessage());
/*     */             }
/*     */           } else {
/*     */             
/* 191 */             throw ex;
/*     */           } 
/*     */         } 
/*     */       }  
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\io\support\PropertiesLoaderSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */