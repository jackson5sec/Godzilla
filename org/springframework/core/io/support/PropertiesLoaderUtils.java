/*     */ package org.springframework.core.io.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Properties;
/*     */ import org.springframework.core.SpringProperties;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.PropertiesPersister;
/*     */ import org.springframework.util.ResourceUtils;
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
/*     */ public abstract class PropertiesLoaderUtils
/*     */ {
/*     */   private static final String XML_FILE_EXTENSION = ".xml";
/*  57 */   private static final boolean shouldIgnoreXml = SpringProperties.getFlag("spring.xml.ignore");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Properties loadProperties(EncodedResource resource) throws IOException {
/*  66 */     Properties props = new Properties();
/*  67 */     fillProperties(props, resource);
/*  68 */     return props;
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
/*     */   public static void fillProperties(Properties props, EncodedResource resource) throws IOException {
/*  81 */     fillProperties(props, resource, (PropertiesPersister)ResourcePropertiesPersister.INSTANCE);
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
/*     */   static void fillProperties(Properties props, EncodedResource resource, PropertiesPersister persister) throws IOException {
/*  94 */     InputStream stream = null;
/*  95 */     Reader reader = null;
/*     */     try {
/*  97 */       String filename = resource.getResource().getFilename();
/*  98 */       if (filename != null && filename.endsWith(".xml")) {
/*  99 */         if (shouldIgnoreXml) {
/* 100 */           throw new UnsupportedOperationException("XML support disabled");
/*     */         }
/* 102 */         stream = resource.getInputStream();
/* 103 */         persister.loadFromXml(props, stream);
/*     */       }
/* 105 */       else if (resource.requiresReader()) {
/* 106 */         reader = resource.getReader();
/* 107 */         persister.load(props, reader);
/*     */       } else {
/*     */         
/* 110 */         stream = resource.getInputStream();
/* 111 */         persister.load(props, stream);
/*     */       } 
/*     */     } finally {
/*     */       
/* 115 */       if (stream != null) {
/* 116 */         stream.close();
/*     */       }
/* 118 */       if (reader != null) {
/* 119 */         reader.close();
/*     */       }
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
/*     */   public static Properties loadProperties(Resource resource) throws IOException {
/* 132 */     Properties props = new Properties();
/* 133 */     fillProperties(props, resource);
/* 134 */     return props;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void fillProperties(Properties props, Resource resource) throws IOException {
/* 144 */     try (InputStream is = resource.getInputStream()) {
/* 145 */       String filename = resource.getFilename();
/* 146 */       if (filename != null && filename.endsWith(".xml")) {
/* 147 */         if (shouldIgnoreXml) {
/* 148 */           throw new UnsupportedOperationException("XML support disabled");
/*     */         }
/* 150 */         props.loadFromXML(is);
/*     */       } else {
/*     */         
/* 153 */         props.load(is);
/*     */       } 
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
/*     */   public static Properties loadAllProperties(String resourceName) throws IOException {
/* 168 */     return loadAllProperties(resourceName, null);
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
/*     */   public static Properties loadAllProperties(String resourceName, @Nullable ClassLoader classLoader) throws IOException {
/* 183 */     Assert.notNull(resourceName, "Resource name must not be null");
/* 184 */     ClassLoader classLoaderToUse = classLoader;
/* 185 */     if (classLoaderToUse == null) {
/* 186 */       classLoaderToUse = ClassUtils.getDefaultClassLoader();
/*     */     }
/*     */     
/* 189 */     Enumeration<URL> urls = (classLoaderToUse != null) ? classLoaderToUse.getResources(resourceName) : ClassLoader.getSystemResources(resourceName);
/* 190 */     Properties props = new Properties();
/* 191 */     while (urls.hasMoreElements()) {
/* 192 */       URL url = urls.nextElement();
/* 193 */       URLConnection con = url.openConnection();
/* 194 */       ResourceUtils.useCachesIfNecessary(con);
/* 195 */       try (InputStream is = con.getInputStream()) {
/* 196 */         if (resourceName.endsWith(".xml")) {
/* 197 */           if (shouldIgnoreXml) {
/* 198 */             throw new UnsupportedOperationException("XML support disabled");
/*     */           }
/* 200 */           props.loadFromXML(is);
/*     */         } else {
/*     */           
/* 203 */           props.load(is);
/*     */         } 
/*     */       } 
/*     */     } 
/* 207 */     return props;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\io\support\PropertiesLoaderUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */