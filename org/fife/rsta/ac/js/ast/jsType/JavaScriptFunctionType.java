/*     */ package org.fife.rsta.ac.js.ast.jsType;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.fife.rsta.ac.java.classreader.ClassFile;
/*     */ import org.fife.rsta.ac.js.JavaScriptHelper;
/*     */ import org.fife.rsta.ac.js.SourceCompletionProvider;
/*     */ import org.fife.rsta.ac.js.ast.type.TypeDeclaration;
/*     */ import org.fife.rsta.ac.js.ast.type.TypeDeclarationFactory;
/*     */ import org.mozilla.javascript.Kit;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JavaScriptFunctionType
/*     */ {
/*  17 */   public static int CONVERSION_NONE = 999;
/*  18 */   public static int CONVERSION_JS = 99;
/*     */   
/*  20 */   public static Class<?> BooleanClass = Kit.classOrNull("java.lang.Boolean");
/*  21 */   public static Class<?> ByteClass = Kit.classOrNull("java.lang.Byte");
/*  22 */   public static Class<?> CharacterClass = Kit.classOrNull("java.lang.Character");
/*  23 */   public static Class<?> ClassClass = Kit.classOrNull("java.lang.Class");
/*  24 */   public static Class<?> DoubleClass = Kit.classOrNull("java.lang.Double");
/*  25 */   public static Class<?> FloatClass = Kit.classOrNull("java.lang.Float");
/*  26 */   public static Class<?> IntegerClass = Kit.classOrNull("java.lang.Integer");
/*  27 */   public static Class<?> LongClass = Kit.classOrNull("java.lang.Long");
/*  28 */   public static Class<?> NumberClass = Kit.classOrNull("java.lang.Number");
/*  29 */   public static Class<?> ObjectClass = Kit.classOrNull("java.lang.Object");
/*  30 */   public static Class<?> ShortClass = Kit.classOrNull("java.lang.Short");
/*  31 */   public static Class<?> StringClass = Kit.classOrNull("java.lang.String");
/*  32 */   public static Class<?> DateClass = Kit.classOrNull("java.util.Date");
/*  33 */   public static Class<?> JSBooleanClass = null;
/*  34 */   public static Class<?> JSStringClass = null;
/*  35 */   public static Class<?> JSNumberClass = null;
/*  36 */   public static Class<?> JSObjectClass = null;
/*  37 */   public static Class<?> JSDateClass = null;
/*  38 */   public static Class<?> JSArray = null;
/*     */   
/*     */   private String name;
/*     */   
/*     */   private List<TypeDeclaration> arguments;
/*     */   
/*     */   private static final int JSTYPE_UNDEFINED = 0;
/*     */   private static final int JSTYPE_BOOLEAN = 1;
/*     */   private static final int JSTYPE_NUMBER = 2;
/*     */   private static final int JSTYPE_STRING = 3;
/*     */   private static final int JSTYPE_ARRAY = 4;
/*     */   private static final int JSTYPE_OBJECT = 5;
/*     */   
/*     */   private JavaScriptFunctionType(String name, SourceCompletionProvider provider) {
/*  52 */     this(name, new ArrayList<>(), provider);
/*     */   }
/*     */ 
/*     */   
/*     */   private JavaScriptFunctionType(String name, List<TypeDeclaration> arguments, SourceCompletionProvider provider) {
/*  57 */     this.name = name;
/*  58 */     this.arguments = arguments;
/*  59 */     JSBooleanClass = Kit.classOrNull(provider.getTypesFactory().getClassName("JSBoolean"));
/*  60 */     JSStringClass = Kit.classOrNull(provider.getTypesFactory().getClassName("JSString"));
/*  61 */     JSNumberClass = Kit.classOrNull(provider.getTypesFactory().getClassName("JSNumber"));
/*  62 */     JSObjectClass = Kit.classOrNull(provider.getTypesFactory().getClassName("JSObject"));
/*  63 */     JSDateClass = Kit.classOrNull(provider.getTypesFactory().getClassName("JSDate"));
/*  64 */     JSArray = Kit.classOrNull(provider.getTypesFactory().getClassName("JSArray"));
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/*  69 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<TypeDeclaration> getArguments() {
/*  74 */     return this.arguments;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addArgument(TypeDeclaration type) {
/*  79 */     if (this.arguments == null) {
/*  80 */       this.arguments = new ArrayList<>();
/*     */     }
/*  82 */     this.arguments.add(type);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getArgumentCount() {
/*  87 */     return (this.arguments != null) ? this.arguments.size() : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeDeclaration getArgument(int index) {
/*  92 */     return (this.arguments != null) ? this.arguments.get(index) : null;
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
/*     */   public int compare(JavaScriptFunctionType compareType, SourceCompletionProvider provider, boolean isJavaScriptType) {
/* 105 */     if (!compareType.getName().equals(getName())) {
/* 106 */       return CONVERSION_NONE;
/*     */     }
/*     */ 
/*     */     
/* 110 */     boolean argsMatch = (compareType.getArgumentCount() == getArgumentCount());
/*     */ 
/*     */     
/* 113 */     if (!isJavaScriptType && !argsMatch)
/* 114 */       return CONVERSION_NONE; 
/* 115 */     if (isJavaScriptType && !argsMatch) {
/* 116 */       return CONVERSION_JS;
/*     */     }
/*     */     
/* 119 */     int weight = 0;
/*     */     
/* 121 */     for (int i = 0; i < getArgumentCount(); i++) {
/* 122 */       TypeDeclaration param = getArgument(i);
/* 123 */       TypeDeclaration compareParam = compareType.getArgument(i);
/* 124 */       weight += compareParameters(param, compareParam, provider);
/* 125 */       if (weight >= CONVERSION_NONE) {
/*     */         break;
/*     */       }
/*     */     } 
/* 129 */     return weight;
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
/*     */   private TypeDeclaration convertParamType(TypeDeclaration type, SourceCompletionProvider provider) {
/* 141 */     ClassFile cf = provider.getJavaScriptTypesFactory().getClassFile(provider
/* 142 */         .getJarManager(), type);
/* 143 */     if (cf != null) {
/* 144 */       return provider.getJavaScriptTypesFactory()
/* 145 */         .createNewTypeDeclaration(cf, type.isStaticsOnly(), false);
/*     */     }
/* 147 */     return type;
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
/*     */   private int compareParameters(TypeDeclaration param, TypeDeclaration compareParam, SourceCompletionProvider provider) {
/* 162 */     if (compareParam.equals(param)) {
/* 163 */       return 0;
/*     */     }
/* 165 */     param = convertParamType(param, provider);
/* 166 */     compareParam = convertParamType(compareParam, provider);
/*     */     
/*     */     try {
/* 169 */       int fromCode = getJSTypeCode(param.getQualifiedName(), provider.getTypesFactory());
/* 170 */       Class<?> to = convertClassToJavaClass(compareParam.getQualifiedName(), provider.getTypesFactory());
/* 171 */       Class<?> from = convertClassToJavaClass(param.getQualifiedName(), provider.getTypesFactory());
/* 172 */       switch (fromCode) {
/*     */         case 0:
/* 174 */           if (to == StringClass || to == ObjectClass) {
/* 175 */             return 1;
/*     */           }
/*     */           break;
/*     */ 
/*     */ 
/*     */         
/*     */         case 1:
/* 182 */           if (to == boolean.class) {
/* 183 */             return 1;
/*     */           }
/* 185 */           if (to == BooleanClass) {
/* 186 */             return 2;
/*     */           }
/* 188 */           if (to == ObjectClass) {
/* 189 */             return 3;
/*     */           }
/* 191 */           if (to == StringClass) {
/* 192 */             return 4;
/*     */           }
/*     */           break;
/*     */         
/*     */         case 2:
/* 197 */           if (to.isPrimitive()) {
/* 198 */             if (to == double.class) {
/* 199 */               return 1;
/*     */             }
/* 201 */             if (to != boolean.class) {
/* 202 */               return 1 + getSizeRank(to);
/*     */             }
/*     */             break;
/*     */           } 
/* 206 */           if (to == StringClass)
/*     */           {
/* 208 */             return 9;
/*     */           }
/* 210 */           if (to == ObjectClass) {
/* 211 */             return 10;
/*     */           }
/* 213 */           if (NumberClass.isAssignableFrom(to))
/*     */           {
/* 215 */             return 2;
/*     */           }
/*     */           break;
/*     */ 
/*     */         
/*     */         case 3:
/* 221 */           if (to == StringClass) {
/* 222 */             return 1;
/*     */           }
/* 224 */           if (to.isPrimitive()) {
/* 225 */             if (to == char.class) {
/* 226 */               return 3;
/*     */             }
/* 228 */             if (to != boolean.class) {
/* 229 */               return 4;
/*     */             }
/*     */           } 
/*     */           break;
/*     */ 
/*     */         
/*     */         case 4:
/* 236 */           if (to == JSArray) {
/* 237 */             return 1;
/*     */           }
/* 239 */           if (to == StringClass) {
/* 240 */             return 2;
/*     */           }
/* 242 */           if (to.isPrimitive() && to != boolean.class) {
/* 243 */             return (fromCode == 4) ? CONVERSION_NONE : (2 + 
/* 244 */               getSizeRank(to));
/*     */           }
/*     */           break;
/*     */ 
/*     */         
/*     */         case 5:
/* 250 */           if (to != ObjectClass && from.isAssignableFrom(to))
/*     */           {
/*     */             
/* 253 */             return 1;
/*     */           }
/* 255 */           if (to.isArray()) {
/* 256 */             if (from == JSArray || from.isArray())
/*     */             {
/*     */ 
/*     */ 
/*     */               
/* 261 */               return 1; } 
/*     */             break;
/*     */           } 
/* 264 */           if (to == ObjectClass) {
/* 265 */             return 2;
/*     */           }
/* 267 */           if (to == StringClass) {
/* 268 */             return 3;
/*     */           }
/* 270 */           if (to == DateClass) {
/* 271 */             if (from == DateClass)
/*     */             {
/* 273 */               return 1;
/*     */             }
/*     */             break;
/*     */           } 
/* 277 */           if (from.isPrimitive() && to != boolean.class) {
/* 278 */             return 3 + getSizeRank(from);
/*     */           }
/*     */           break;
/*     */       } 
/*     */     
/* 283 */     } catch (ClassNotFoundException classNotFoundException) {}
/*     */ 
/*     */     
/* 286 */     TypeDeclarationFactory typesFactory = provider.getTypesFactory();
/*     */     
/* 288 */     String paramJSType = typesFactory.convertJavaScriptType(param.getQualifiedName(), true);
/* 289 */     String compareParamJSType = typesFactory.convertJavaScriptType(compareParam.getQualifiedName(), true);
/*     */     
/*     */     try {
/* 292 */       Class<?> paramClzz = Class.forName(paramJSType);
/* 293 */       Class<?> compareParamClzz = Class.forName(compareParamJSType);
/* 294 */       if (compareParamClzz.isAssignableFrom(paramClzz))
/* 295 */         return 3; 
/* 296 */     } catch (ClassNotFoundException classNotFoundException) {}
/*     */ 
/*     */ 
/*     */     
/* 300 */     if (compareParam.equals(typesFactory.getDefaultTypeDeclaration())) {
/* 301 */       return 4;
/*     */     }
/*     */     
/* 304 */     return CONVERSION_NONE;
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
/*     */   private Class<?> convertClassToJavaClass(String name, TypeDeclarationFactory typesFactory) throws ClassNotFoundException {
/* 317 */     if (name.equals("any")) {
/* 318 */       return ObjectClass;
/*     */     }
/*     */     
/* 321 */     TypeDeclaration type = typesFactory.getTypeDeclaration(name);
/*     */     
/* 323 */     String clsName = (type != null) ? type.getQualifiedName() : name;
/*     */     
/* 325 */     Class<?> cls = Class.forName(clsName);
/*     */     
/* 327 */     if (cls == JSStringClass) {
/* 328 */       cls = StringClass;
/*     */     }
/* 330 */     else if (cls == JSBooleanClass) {
/* 331 */       cls = BooleanClass;
/*     */     }
/* 333 */     else if (cls == JSNumberClass) {
/* 334 */       cls = NumberClass;
/*     */     }
/* 336 */     else if (cls == JSDateClass) {
/* 337 */       cls = DateClass;
/*     */     }
/* 339 */     else if (cls == JSObjectClass) {
/* 340 */       cls = ObjectClass;
/*     */     } 
/*     */     
/* 343 */     return cls;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JavaScriptFunctionType parseFunction(String function, SourceCompletionProvider provider) {
/* 383 */     int paramStartIndex = function.indexOf('(');
/* 384 */     int paramEndIndex = function.indexOf(')');
/*     */     
/* 386 */     JavaScriptFunctionType functionType = new JavaScriptFunctionType(function.substring(0, paramStartIndex), provider);
/*     */     
/* 388 */     if (paramStartIndex > -1 && paramEndIndex > -1) {
/*     */ 
/*     */       
/* 391 */       String paramsStr = function.substring(paramStartIndex + 1, paramEndIndex).trim();
/*     */       
/* 393 */       if (paramsStr.length() > 0) {
/* 394 */         String[] params = paramsStr.split(",");
/* 395 */         for (int i = 0; i < params.length; i++) {
/* 396 */           String param = provider.getTypesFactory().convertJavaScriptType(params[i], true);
/*     */           
/* 398 */           TypeDeclaration type = provider.getTypesFactory().getTypeDeclaration(param);
/* 399 */           if (type != null) {
/* 400 */             functionType.addArgument(type);
/*     */           }
/*     */           else {
/*     */             
/* 404 */             functionType.addArgument(
/* 405 */                 JavaScriptHelper.createNewTypeDeclaration(param));
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 411 */     return functionType;
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
/*     */   private static int getJSTypeCode(String clsName, TypeDeclarationFactory typesFactory) throws ClassNotFoundException {
/* 424 */     if (clsName.equals("any")) {
/* 425 */       return 0;
/*     */     }
/*     */     
/* 428 */     TypeDeclaration dec = typesFactory.getTypeDeclaration(clsName);
/* 429 */     clsName = (dec != null) ? dec.getQualifiedName() : clsName;
/*     */     
/* 431 */     Class<?> cls = Class.forName(clsName);
/*     */     
/* 433 */     if (cls == BooleanClass || cls == JSBooleanClass) {
/* 434 */       return 1;
/*     */     }
/*     */     
/* 437 */     if (NumberClass.isAssignableFrom(cls) || cls == JSNumberClass) {
/* 438 */       return 2;
/*     */     }
/*     */     
/* 441 */     if (StringClass.isAssignableFrom(cls) || cls == JSStringClass) {
/* 442 */       return 3;
/*     */     }
/*     */     
/* 445 */     if (cls.isArray() || cls == JSArray) {
/* 446 */       return 4;
/*     */     }
/*     */     
/* 449 */     return 5;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static int getSizeRank(Class<?> aType) {
/* 455 */     if (aType == double.class) {
/* 456 */       return 1;
/*     */     }
/* 458 */     if (aType == float.class) {
/* 459 */       return 2;
/*     */     }
/* 461 */     if (aType == long.class) {
/* 462 */       return 3;
/*     */     }
/* 464 */     if (aType == int.class) {
/* 465 */       return 4;
/*     */     }
/* 467 */     if (aType == short.class) {
/* 468 */       return 5;
/*     */     }
/* 470 */     if (aType == char.class) {
/* 471 */       return 6;
/*     */     }
/* 473 */     if (aType == byte.class) {
/* 474 */       return 7;
/*     */     }
/* 476 */     if (aType == boolean.class) {
/* 477 */       return CONVERSION_NONE;
/*     */     }
/*     */     
/* 480 */     return 8;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\ast\jsType\JavaScriptFunctionType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */