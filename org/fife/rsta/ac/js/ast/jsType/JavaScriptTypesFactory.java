/*     */ package org.fife.rsta.ac.js.ast.jsType;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.fife.rsta.ac.java.JarManager;
/*     */ import org.fife.rsta.ac.java.Util;
/*     */ import org.fife.rsta.ac.java.classreader.ClassFile;
/*     */ import org.fife.rsta.ac.java.classreader.FieldInfo;
/*     */ import org.fife.rsta.ac.java.classreader.MemberInfo;
/*     */ import org.fife.rsta.ac.java.classreader.MethodInfo;
/*     */ import org.fife.rsta.ac.java.classreader.Util;
/*     */ import org.fife.rsta.ac.js.SourceCompletionProvider;
/*     */ import org.fife.rsta.ac.js.ast.type.TypeDeclaration;
/*     */ import org.fife.rsta.ac.js.ast.type.TypeDeclarationFactory;
/*     */ import org.fife.rsta.ac.js.ast.type.ecma.TypeDeclarations;
/*     */ import org.fife.rsta.ac.js.completion.JSBeanCompletion;
/*     */ import org.fife.rsta.ac.js.completion.JSClassCompletion;
/*     */ import org.fife.rsta.ac.js.completion.JSCompletion;
/*     */ import org.fife.rsta.ac.js.completion.JSConstructorCompletion;
/*     */ import org.fife.rsta.ac.js.completion.JSFieldCompletion;
/*     */ import org.fife.rsta.ac.js.completion.JSFunctionCompletion;
/*     */ import org.fife.ui.autocomplete.Completion;
/*     */ import org.fife.ui.autocomplete.CompletionProvider;
/*     */ import org.fife.ui.autocomplete.DefaultCompletionProvider;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class JavaScriptTypesFactory
/*     */ {
/*  40 */   protected HashMap<TypeDeclaration, JavaScriptType> cachedTypes = new HashMap<>();
/*     */   
/*     */   private boolean useBeanproperties;
/*     */   
/*     */   protected TypeDeclarationFactory typesFactory;
/*     */   private static final List<String> UNSUPPORTED_COMPLETIONS;
/*  46 */   private static String SPECIAL_METHOD = "<clinit>";
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*  51 */     UNSUPPORTED_COMPLETIONS = new ArrayList<>();
/*  52 */     UNSUPPORTED_COMPLETIONS.add("java.lang.Object");
/*     */   }
/*     */ 
/*     */   
/*     */   public JavaScriptTypesFactory(TypeDeclarationFactory typesFactory) {
/*  57 */     this.typesFactory = typesFactory;
/*     */   }
/*     */   
/*     */   private static class DefaultJavaScriptTypeFactory
/*     */     extends JavaScriptTypesFactory {
/*     */     public DefaultJavaScriptTypeFactory(TypeDeclarationFactory typesFactory) {
/*  63 */       super(typesFactory);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static JavaScriptTypesFactory getDefaultJavaScriptTypesFactory(TypeDeclarationFactory typesFactory) {
/*  69 */     return new DefaultJavaScriptTypeFactory(typesFactory);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUseBeanProperties(boolean useBeanproperties) {
/*  74 */     this.useBeanproperties = useBeanproperties;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUseBeanProperties() {
/*  79 */     return this.useBeanproperties;
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
/*     */   public JavaScriptType getCachedType(TypeDeclaration type, JarManager manager, DefaultCompletionProvider provider, String text) {
/*  96 */     if (manager == null || type == null) {
/*  97 */       return null;
/*     */     }
/*     */     
/* 100 */     if (this.cachedTypes.containsKey(type)) {
/* 101 */       return this.cachedTypes.get(type);
/*     */     }
/*     */     
/* 104 */     ClassFile cf = getClassFile(manager, type);
/*     */     
/* 106 */     JavaScriptType cachedType = makeJavaScriptType(type);
/*     */     
/* 108 */     this.cachedTypes.put(type, cachedType);
/* 109 */     readClassFile(cachedType, cf, provider, manager, type);
/* 110 */     return cachedType;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassFile getClassFile(JarManager manager, TypeDeclaration type) {
/* 116 */     return (manager != null) ? manager.getClassEntry(type.getQualifiedName()) : null;
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
/*     */   private void readClassFile(JavaScriptType cachedType, ClassFile cf, DefaultCompletionProvider provider, JarManager manager, TypeDeclaration type) {
/* 133 */     if (cf != null) {
/* 134 */       readMethodsAndFieldsFromTypeDeclaration(cachedType, provider, manager, cf);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isBeanProperty(MethodInfo method) {
/* 144 */     return (method.getParameterCount() == 0 && (method
/* 145 */       .getName().startsWith("get") || method.getName()
/* 146 */       .startsWith("is")));
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
/*     */   private void readMethodsAndFieldsFromTypeDeclaration(JavaScriptType cachedType, DefaultCompletionProvider provider, JarManager jarManager, ClassFile cf) {
/* 165 */     boolean staticOnly = cachedType.getType().isStaticsOnly();
/* 166 */     boolean supportsBeanProperties = cachedType.getType().supportsBeanProperties();
/* 167 */     boolean isJSType = this.typesFactory.isJavaScriptType(cachedType.getType());
/*     */ 
/*     */     
/* 170 */     if (isJSType) {
/* 171 */       cachedType.setClassTypeCompletion((JSCompletion)new JSClassCompletion((CompletionProvider)provider, cf, false));
/*     */     }
/*     */ 
/*     */     
/* 175 */     int methodCount = cf.getMethodCount();
/* 176 */     for (int i = 0; i < methodCount; i++) {
/* 177 */       MethodInfo info = cf.getMethodInfo(i);
/* 178 */       if (!info.isConstructor() && !SPECIAL_METHOD.equals(info.getName())) {
/* 179 */         if (isAccessible(info.getAccessFlags(), staticOnly, isJSType) && ((staticOnly && info.isStatic()) || !staticOnly)) {
/* 180 */           JSFunctionCompletion completion = new JSFunctionCompletion((CompletionProvider)provider, info, true);
/* 181 */           cachedType.addCompletion((JSCompletion)completion);
/*     */         } 
/*     */ 
/*     */         
/* 185 */         if (!staticOnly && this.useBeanproperties && supportsBeanProperties && isBeanProperty(info)) {
/* 186 */           JSBeanCompletion beanCompletion = new JSBeanCompletion((CompletionProvider)provider, info, jarManager);
/*     */           
/* 188 */           cachedType.addCompletion((JSCompletion)beanCompletion);
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 193 */       if (isJSType && info.isConstructor() && !SPECIAL_METHOD.equals(info.getName()) && 
/* 194 */         this.typesFactory.canJavaScriptBeInstantiated(cachedType.getType().getQualifiedName())) {
/*     */         
/* 196 */         JSConstructorCompletion completion = new JSConstructorCompletion((CompletionProvider)provider, info);
/* 197 */         cachedType.addConstructor((JSCompletion)completion);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 203 */     int fieldCount = cf.getFieldCount();
/* 204 */     for (int j = 0; j < fieldCount; j++) {
/* 205 */       FieldInfo info = cf.getFieldInfo(j);
/* 206 */       if (isAccessible((MemberInfo)info, staticOnly, isJSType)) {
/* 207 */         JSFieldCompletion completion = new JSFieldCompletion((CompletionProvider)provider, info);
/* 208 */         cachedType.addCompletion((JSCompletion)completion);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 213 */     String superClassName = cf.getSuperClassName(true);
/* 214 */     ClassFile superClass = getClassFileFor(cf, superClassName, jarManager);
/* 215 */     if (superClass != null && !ignoreClass(superClassName)) {
/* 216 */       TypeDeclaration type = createNewTypeDeclaration(superClass, staticOnly, false);
/* 217 */       JavaScriptType extendedType = makeJavaScriptType(type);
/* 218 */       cachedType.addExtension(extendedType);
/* 219 */       readClassFile(extendedType, superClass, provider, jarManager, type);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 224 */     for (int k = 0; k < cf.getImplementedInterfaceCount(); k++) {
/* 225 */       String inter = cf.getImplementedInterfaceName(k, true);
/* 226 */       ClassFile intf = getClassFileFor(cf, inter, jarManager);
/* 227 */       if (intf != null && !ignoreClass(inter)) {
/* 228 */         TypeDeclaration type = createNewTypeDeclaration(intf, staticOnly, false);
/*     */         
/* 230 */         JavaScriptType extendedType = new JavaScriptType(type);
/* 231 */         cachedType.addExtension(extendedType);
/* 232 */         readClassFile(extendedType, intf, provider, jarManager, type);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean ignoreClass(String className) {
/* 239 */     return UNSUPPORTED_COMPLETIONS.contains(className);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isAccessible(MemberInfo info, boolean staticOnly, boolean isJJType) {
/* 245 */     int access = info.getAccessFlags();
/* 246 */     boolean accessible = isAccessible(access, staticOnly, isJJType);
/*     */     
/* 248 */     return ((!staticOnly && accessible) || (staticOnly && info.isStatic() && accessible));
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
/*     */   private boolean isAccessible(int access, boolean staticsOnly, boolean isJSType) {
/* 267 */     boolean accessible = false;
/* 268 */     if ((staticsOnly && Util.isPublic(access)) || (!staticsOnly && 
/* 269 */       Util.isPublic(access)) || (isJSType && Util.isProtected(access))) {
/* 270 */       accessible = true;
/*     */     }
/* 272 */     return accessible;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeDeclaration createNewTypeDeclaration(ClassFile cf, boolean staticOnly) {
/* 278 */     return createNewTypeDeclaration(cf, staticOnly, true);
/*     */   }
/*     */   
/*     */   public TypeDeclaration createNewTypeDeclaration(ClassFile cf, boolean staticOnly, boolean addToCache) {
/* 282 */     String className = cf.getClassName(false);
/* 283 */     String packageName = cf.getPackageName();
/*     */     
/* 285 */     if (staticOnly && !addToCache)
/*     */     {
/* 287 */       return new TypeDeclaration(packageName, className, cf
/* 288 */           .getClassName(true), staticOnly);
/*     */     }
/*     */     
/* 291 */     String qualified = cf.getClassName(true);
/*     */     
/* 293 */     TypeDeclaration td = this.typesFactory.getTypeDeclaration(qualified);
/* 294 */     if (td == null) {
/*     */       
/* 296 */       td = new TypeDeclaration(packageName, className, cf.getClassName(true), staticOnly);
/*     */       
/* 298 */       if (addToCache) {
/* 299 */         this.typesFactory.addType(qualified, td);
/*     */       }
/*     */     } 
/* 302 */     return td;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ClassFile getClassFileFor(ClassFile cf, String className, JarManager jarManager) {
/* 313 */     if (className == null) {
/* 314 */       return null;
/*     */     }
/*     */     
/* 317 */     ClassFile superClass = null;
/*     */ 
/*     */     
/* 320 */     if (!Util.isFullyQualified(className)) {
/*     */ 
/*     */       
/* 323 */       String pkg = cf.getPackageName();
/* 324 */       if (pkg != null) {
/* 325 */         String temp = pkg + "." + className;
/* 326 */         superClass = jarManager.getClassEntry(temp);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 331 */       superClass = jarManager.getClassEntry(className);
/*     */     } 
/*     */     
/* 334 */     return superClass;
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
/*     */   public void populateCompletionsForType(JavaScriptType cachedType, Set<Completion> completions) {
/* 349 */     if (cachedType != null) {
/* 350 */       Map<String, JSCompletion> completionsForType = cachedType.getMethodFieldCompletions();
/* 351 */       for (JSCompletion completion : completionsForType.values()) {
/* 352 */         completions.add(completion);
/*     */       }
/*     */ 
/*     */       
/* 356 */       List<JavaScriptType> extendedClasses = cachedType.getExtendedClasses();
/* 357 */       for (JavaScriptType extendedType : extendedClasses) {
/* 358 */         populateCompletionsForType(extendedType, completions);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeCachedType(TypeDeclaration typeDef) {
/* 365 */     this.cachedTypes.remove(typeDef);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearCache() {
/* 370 */     this.cachedTypes.clear();
/*     */   }
/*     */   
/*     */   public JavaScriptType makeJavaScriptType(TypeDeclaration type) {
/* 374 */     return new JavaScriptType(type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<JavaScriptType> getECMAObjectTypes(SourceCompletionProvider provider) {
/* 383 */     List<JavaScriptType> constructors = new ArrayList<>();
/*     */     
/* 385 */     Set<TypeDeclarations.JavaScriptObject> types = this.typesFactory.getECMAScriptObjects();
/* 386 */     JarManager manager = provider.getJarManager();
/* 387 */     for (TypeDeclarations.JavaScriptObject object : types) {
/* 388 */       TypeDeclaration type = this.typesFactory.getTypeDeclaration(object.getName());
/* 389 */       JavaScriptType js = getCachedType(type, manager, (DefaultCompletionProvider)provider, null);
/* 390 */       if (js != null) {
/* 391 */         constructors.add(js);
/*     */       }
/*     */     } 
/* 394 */     return constructors;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\ast\jsType\JavaScriptTypesFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */