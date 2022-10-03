/*     */ package org.springframework.core.io;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class VfsUtils
/*     */ {
/*     */   private static final String VFS3_PKG = "org.jboss.vfs.";
/*     */   private static final String VFS_NAME = "VFS";
/*     */   private static final Method VFS_METHOD_GET_ROOT_URL;
/*     */   private static final Method VFS_METHOD_GET_ROOT_URI;
/*     */   private static final Method VIRTUAL_FILE_METHOD_EXISTS;
/*     */   private static final Method VIRTUAL_FILE_METHOD_GET_INPUT_STREAM;
/*     */   private static final Method VIRTUAL_FILE_METHOD_GET_SIZE;
/*     */   private static final Method VIRTUAL_FILE_METHOD_GET_LAST_MODIFIED;
/*     */   private static final Method VIRTUAL_FILE_METHOD_TO_URL;
/*     */   private static final Method VIRTUAL_FILE_METHOD_TO_URI;
/*     */   private static final Method VIRTUAL_FILE_METHOD_GET_NAME;
/*     */   private static final Method VIRTUAL_FILE_METHOD_GET_PATH_NAME;
/*     */   private static final Method VIRTUAL_FILE_METHOD_GET_PHYSICAL_FILE;
/*     */   private static final Method VIRTUAL_FILE_METHOD_GET_CHILD;
/*     */   protected static final Class<?> VIRTUAL_FILE_VISITOR_INTERFACE;
/*     */   protected static final Method VIRTUAL_FILE_METHOD_VISIT;
/*     */   private static final Field VISITOR_ATTRIBUTES_FIELD_RECURSE;
/*     */   
/*     */   static {
/*  70 */     ClassLoader loader = VfsUtils.class.getClassLoader();
/*     */     try {
/*  72 */       Class<?> vfsClass = loader.loadClass("org.jboss.vfs.VFS");
/*  73 */       VFS_METHOD_GET_ROOT_URL = vfsClass.getMethod("getChild", new Class[] { URL.class });
/*  74 */       VFS_METHOD_GET_ROOT_URI = vfsClass.getMethod("getChild", new Class[] { URI.class });
/*     */       
/*  76 */       Class<?> virtualFile = loader.loadClass("org.jboss.vfs.VirtualFile");
/*  77 */       VIRTUAL_FILE_METHOD_EXISTS = virtualFile.getMethod("exists", new Class[0]);
/*  78 */       VIRTUAL_FILE_METHOD_GET_INPUT_STREAM = virtualFile.getMethod("openStream", new Class[0]);
/*  79 */       VIRTUAL_FILE_METHOD_GET_SIZE = virtualFile.getMethod("getSize", new Class[0]);
/*  80 */       VIRTUAL_FILE_METHOD_GET_LAST_MODIFIED = virtualFile.getMethod("getLastModified", new Class[0]);
/*  81 */       VIRTUAL_FILE_METHOD_TO_URI = virtualFile.getMethod("toURI", new Class[0]);
/*  82 */       VIRTUAL_FILE_METHOD_TO_URL = virtualFile.getMethod("toURL", new Class[0]);
/*  83 */       VIRTUAL_FILE_METHOD_GET_NAME = virtualFile.getMethod("getName", new Class[0]);
/*  84 */       VIRTUAL_FILE_METHOD_GET_PATH_NAME = virtualFile.getMethod("getPathName", new Class[0]);
/*  85 */       VIRTUAL_FILE_METHOD_GET_PHYSICAL_FILE = virtualFile.getMethod("getPhysicalFile", new Class[0]);
/*  86 */       VIRTUAL_FILE_METHOD_GET_CHILD = virtualFile.getMethod("getChild", new Class[] { String.class });
/*     */       
/*  88 */       VIRTUAL_FILE_VISITOR_INTERFACE = loader.loadClass("org.jboss.vfs.VirtualFileVisitor");
/*  89 */       VIRTUAL_FILE_METHOD_VISIT = virtualFile.getMethod("visit", new Class[] { VIRTUAL_FILE_VISITOR_INTERFACE });
/*     */       
/*  91 */       Class<?> visitorAttributesClass = loader.loadClass("org.jboss.vfs.VisitorAttributes");
/*  92 */       VISITOR_ATTRIBUTES_FIELD_RECURSE = visitorAttributesClass.getField("RECURSE");
/*     */     }
/*  94 */     catch (Throwable ex) {
/*  95 */       throw new IllegalStateException("Could not detect JBoss VFS infrastructure", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected static Object invokeVfsMethod(Method method, @Nullable Object target, Object... args) throws IOException {
/*     */     try {
/* 101 */       return method.invoke(target, args);
/*     */     }
/* 103 */     catch (InvocationTargetException ex) {
/* 104 */       Throwable targetEx = ex.getTargetException();
/* 105 */       if (targetEx instanceof IOException) {
/* 106 */         throw (IOException)targetEx;
/*     */       }
/* 108 */       ReflectionUtils.handleInvocationTargetException(ex);
/*     */     }
/* 110 */     catch (Exception ex) {
/* 111 */       ReflectionUtils.handleReflectionException(ex);
/*     */     } 
/*     */     
/* 114 */     throw new IllegalStateException("Invalid code path reached");
/*     */   }
/*     */   
/*     */   static boolean exists(Object vfsResource) {
/*     */     try {
/* 119 */       return ((Boolean)invokeVfsMethod(VIRTUAL_FILE_METHOD_EXISTS, vfsResource, new Object[0])).booleanValue();
/*     */     }
/* 121 */     catch (IOException ex) {
/* 122 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   static boolean isReadable(Object vfsResource) {
/*     */     try {
/* 128 */       return (((Long)invokeVfsMethod(VIRTUAL_FILE_METHOD_GET_SIZE, vfsResource, new Object[0])).longValue() > 0L);
/*     */     }
/* 130 */     catch (IOException ex) {
/* 131 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   static long getSize(Object vfsResource) throws IOException {
/* 136 */     return ((Long)invokeVfsMethod(VIRTUAL_FILE_METHOD_GET_SIZE, vfsResource, new Object[0])).longValue();
/*     */   }
/*     */   
/*     */   static long getLastModified(Object vfsResource) throws IOException {
/* 140 */     return ((Long)invokeVfsMethod(VIRTUAL_FILE_METHOD_GET_LAST_MODIFIED, vfsResource, new Object[0])).longValue();
/*     */   }
/*     */   
/*     */   static InputStream getInputStream(Object vfsResource) throws IOException {
/* 144 */     return (InputStream)invokeVfsMethod(VIRTUAL_FILE_METHOD_GET_INPUT_STREAM, vfsResource, new Object[0]);
/*     */   }
/*     */   
/*     */   static URL getURL(Object vfsResource) throws IOException {
/* 148 */     return (URL)invokeVfsMethod(VIRTUAL_FILE_METHOD_TO_URL, vfsResource, new Object[0]);
/*     */   }
/*     */   
/*     */   static URI getURI(Object vfsResource) throws IOException {
/* 152 */     return (URI)invokeVfsMethod(VIRTUAL_FILE_METHOD_TO_URI, vfsResource, new Object[0]);
/*     */   }
/*     */   
/*     */   static String getName(Object vfsResource) {
/*     */     try {
/* 157 */       return (String)invokeVfsMethod(VIRTUAL_FILE_METHOD_GET_NAME, vfsResource, new Object[0]);
/*     */     }
/* 159 */     catch (IOException ex) {
/* 160 */       throw new IllegalStateException("Cannot get resource name", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   static Object getRelative(URL url) throws IOException {
/* 165 */     return invokeVfsMethod(VFS_METHOD_GET_ROOT_URL, null, new Object[] { url });
/*     */   }
/*     */   
/*     */   static Object getChild(Object vfsResource, String path) throws IOException {
/* 169 */     return invokeVfsMethod(VIRTUAL_FILE_METHOD_GET_CHILD, vfsResource, new Object[] { path });
/*     */   }
/*     */   
/*     */   static File getFile(Object vfsResource) throws IOException {
/* 173 */     return (File)invokeVfsMethod(VIRTUAL_FILE_METHOD_GET_PHYSICAL_FILE, vfsResource, new Object[0]);
/*     */   }
/*     */   
/*     */   static Object getRoot(URI url) throws IOException {
/* 177 */     return invokeVfsMethod(VFS_METHOD_GET_ROOT_URI, null, new Object[] { url });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected static Object getRoot(URL url) throws IOException {
/* 183 */     return invokeVfsMethod(VFS_METHOD_GET_ROOT_URL, null, new Object[] { url });
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected static Object doGetVisitorAttributes() {
/* 188 */     return ReflectionUtils.getField(VISITOR_ATTRIBUTES_FIELD_RECURSE, null);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected static String doGetPath(Object resource) {
/* 193 */     return (String)ReflectionUtils.invokeMethod(VIRTUAL_FILE_METHOD_GET_PATH_NAME, resource);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\io\VfsUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */