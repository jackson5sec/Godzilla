/*     */ package org.springframework.core.io.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.stream.Collectors;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.UrlResource;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ConcurrentReferenceHashMap;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SpringFactoriesLoader
/*     */ {
/*     */   public static final String FACTORIES_RESOURCE_LOCATION = "META-INF/spring.factories";
/*  71 */   private static final Log logger = LogFactory.getLog(SpringFactoriesLoader.class);
/*     */   
/*  73 */   static final Map<ClassLoader, Map<String, List<String>>> cache = (Map<ClassLoader, Map<String, List<String>>>)new ConcurrentReferenceHashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> List<T> loadFactories(Class<T> factoryType, @Nullable ClassLoader classLoader) {
/*  96 */     Assert.notNull(factoryType, "'factoryType' must not be null");
/*  97 */     ClassLoader classLoaderToUse = classLoader;
/*  98 */     if (classLoaderToUse == null) {
/*  99 */       classLoaderToUse = SpringFactoriesLoader.class.getClassLoader();
/*     */     }
/* 101 */     List<String> factoryImplementationNames = loadFactoryNames(factoryType, classLoaderToUse);
/* 102 */     if (logger.isTraceEnabled()) {
/* 103 */       logger.trace("Loaded [" + factoryType.getName() + "] names: " + factoryImplementationNames);
/*     */     }
/* 105 */     List<T> result = new ArrayList<>(factoryImplementationNames.size());
/* 106 */     for (String factoryImplementationName : factoryImplementationNames) {
/* 107 */       result.add(instantiateFactory(factoryImplementationName, factoryType, classLoaderToUse));
/*     */     }
/* 109 */     AnnotationAwareOrderComparator.sort(result);
/* 110 */     return result;
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
/*     */   public static List<String> loadFactoryNames(Class<?> factoryType, @Nullable ClassLoader classLoader) {
/* 127 */     ClassLoader classLoaderToUse = classLoader;
/* 128 */     if (classLoaderToUse == null) {
/* 129 */       classLoaderToUse = SpringFactoriesLoader.class.getClassLoader();
/*     */     }
/* 131 */     String factoryTypeName = factoryType.getName();
/* 132 */     return loadSpringFactories(classLoaderToUse).getOrDefault(factoryTypeName, Collections.emptyList());
/*     */   }
/*     */   
/*     */   private static Map<String, List<String>> loadSpringFactories(ClassLoader classLoader) {
/* 136 */     Map<String, List<String>> result = cache.get(classLoader);
/* 137 */     if (result != null) {
/* 138 */       return result;
/*     */     }
/*     */     
/* 141 */     result = new HashMap<>();
/*     */     try {
/* 143 */       Enumeration<URL> urls = classLoader.getResources("META-INF/spring.factories");
/* 144 */       while (urls.hasMoreElements()) {
/* 145 */         URL url = urls.nextElement();
/* 146 */         UrlResource resource = new UrlResource(url);
/* 147 */         Properties properties = PropertiesLoaderUtils.loadProperties((Resource)resource);
/* 148 */         for (Map.Entry<?, ?> entry : properties.entrySet()) {
/* 149 */           String factoryTypeName = ((String)entry.getKey()).trim();
/*     */           
/* 151 */           String[] factoryImplementationNames = StringUtils.commaDelimitedListToStringArray((String)entry.getValue());
/* 152 */           for (String factoryImplementationName : factoryImplementationNames) {
/* 153 */             ((List<String>)result.computeIfAbsent(factoryTypeName, key -> new ArrayList()))
/* 154 */               .add(factoryImplementationName.trim());
/*     */           }
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 160 */       result.replaceAll((factoryType, implementations) -> (List)implementations.stream().distinct().collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList)));
/*     */       
/* 162 */       cache.put(classLoader, result);
/*     */     }
/* 164 */     catch (IOException ex) {
/* 165 */       throw new IllegalArgumentException("Unable to load factories from location [META-INF/spring.factories]", ex);
/*     */     } 
/*     */     
/* 168 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   private static <T> T instantiateFactory(String factoryImplementationName, Class<T> factoryType, ClassLoader classLoader) {
/*     */     try {
/* 174 */       Class<?> factoryImplementationClass = ClassUtils.forName(factoryImplementationName, classLoader);
/* 175 */       if (!factoryType.isAssignableFrom(factoryImplementationClass)) {
/* 176 */         throw new IllegalArgumentException("Class [" + factoryImplementationName + "] is not assignable to factory type [" + factoryType
/* 177 */             .getName() + "]");
/*     */       }
/* 179 */       return ReflectionUtils.accessibleConstructor(factoryImplementationClass, new Class[0]).newInstance(new Object[0]);
/*     */     }
/* 181 */     catch (Throwable ex) {
/* 182 */       throw new IllegalArgumentException("Unable to instantiate factory class [" + factoryImplementationName + "] for factory type [" + factoryType
/* 183 */           .getName() + "]", ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\io\support\SpringFactoriesLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */