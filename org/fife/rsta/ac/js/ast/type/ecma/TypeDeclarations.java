/*     */ package org.fife.rsta.ac.js.ast.type.ecma;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.fife.rsta.ac.js.JavaScriptHelper;
/*     */ import org.fife.rsta.ac.js.ast.type.ArrayTypeDeclaration;
/*     */ import org.fife.rsta.ac.js.ast.type.TypeDeclaration;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class TypeDeclarations
/*     */ {
/*     */   private static final String ECMA_DEFAULT_PACKAGE = "org.fife.rsta.ac.js.ecma.api";
/*     */   public static final String ECMA_ARRAY = "JSArray";
/*     */   public static final String ECMA_BOOLEAN = "JSBoolean";
/*     */   public static final String ECMA_DATE = "JSDate";
/*     */   public static final String ECMA_ERROR = "JSError";
/*     */   public static final String ECMA_FUNCTION = "JSFunction";
/*     */   public static final String ECMA_MATH = "JSMath";
/*     */   public static final String ECMA_NUMBER = "JSNumber";
/*     */   public static final String ECMA_OBJECT = "JSObject";
/*     */   public static final String ECMA_REGEXP = "JSRegExp";
/*     */   public static final String ECMA_STRING = "JSString";
/*     */   public static final String ECMA_GLOBAL = "JSGlobal";
/*     */   public static final String ECMA_JSON = "JSJSON";
/*     */   public static final String ECMA_NAMESPACE = "E4XNamespace";
/*     */   public static final String ECMA_QNAME = "E4XQName";
/*     */   public static final String ECMA_XML = "E4XXML";
/*     */   public static final String ECMA_XMLLIST = "E4XXMLList";
/*     */   public static final String FUNCTION_CALL = "FC";
/*     */   public static final String ANY = "any";
/*  41 */   public static String NULL_TYPE = "void";
/*     */   
/*  43 */   private final HashMap<String, TypeDeclaration> types = new HashMap<>();
/*     */ 
/*     */   
/*  46 */   private final HashMap<String, String> javascriptReverseLookup = new HashMap<>();
/*  47 */   private final HashSet<JavaScriptObject> ecmaObjects = new HashSet<>();
/*     */ 
/*     */   
/*     */   public TypeDeclarations() {
/*  51 */     loadTypes();
/*  52 */     loadExtensions();
/*  53 */     loadReverseLookup();
/*  54 */     loadJavaScriptConstructors();
/*     */   }
/*     */ 
/*     */   
/*     */   private void loadExtensions() {
/*  59 */     addTypeDeclaration("FC", new TypeDeclaration(null, "FC", "FC", false, false));
/*     */     
/*  61 */     addTypeDeclaration("any", new TypeDeclaration(null, "any", "any"));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void loadJavaScriptConstructors() {
/*  66 */     addECMAObject("JSString", true);
/*  67 */     addECMAObject("JSDate", true);
/*  68 */     addECMAObject("JSNumber", true);
/*  69 */     addECMAObject("JSMath", false);
/*  70 */     addECMAObject("JSObject", true);
/*  71 */     addECMAObject("JSFunction", true);
/*  72 */     addECMAObject("JSBoolean", true);
/*  73 */     addECMAObject("JSRegExp", true);
/*  74 */     addECMAObject("JSArray", true);
/*  75 */     addECMAObject("JSError", true);
/*  76 */     addECMAObject("JSJSON", false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addECMAObject(String type, boolean canBeInstantiated) {
/*  82 */     this.ecmaObjects.add(new JavaScriptObject(type, canBeInstantiated));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void loadReverseLookup() {
/*  89 */     addJavaScriptLookup("String", "JSString");
/*  90 */     addJavaScriptLookup("Date", "JSDate");
/*  91 */     addJavaScriptLookup("RegExp", "JSRegExp");
/*  92 */     addJavaScriptLookup("Number", "JSNumber");
/*  93 */     addJavaScriptLookup("Math", "JSMath");
/*  94 */     addJavaScriptLookup("Function", "JSFunction");
/*  95 */     addJavaScriptLookup("Object", "JSObject");
/*  96 */     addJavaScriptLookup("Array", "JSArray");
/*  97 */     addJavaScriptLookup("Boolean", "JSBoolean");
/*  98 */     addJavaScriptLookup("Error", "JSError");
/*  99 */     addJavaScriptLookup("java.lang.String", "JSString");
/* 100 */     addJavaScriptLookup("java.lang.Number", "JSNumber");
/* 101 */     addJavaScriptLookup("java.lang.Short", "JSNumber");
/* 102 */     addJavaScriptLookup("java.lang.Long", "JSNumber");
/* 103 */     addJavaScriptLookup("java.lang.Float", "JSNumber");
/* 104 */     addJavaScriptLookup("java.lang.Byte", "JSNumber");
/* 105 */     addJavaScriptLookup("java.lang.Double", "JSNumber");
/* 106 */     addJavaScriptLookup("java.lang.Boolean", "JSBoolean");
/* 107 */     addJavaScriptLookup("short", "JSNumber");
/* 108 */     addJavaScriptLookup("long", "JSNumber");
/* 109 */     addJavaScriptLookup("float", "JSNumber");
/* 110 */     addJavaScriptLookup("byte", "JSNumber");
/* 111 */     addJavaScriptLookup("double", "JSNumber");
/* 112 */     addJavaScriptLookup("int", "JSNumber");
/* 113 */     addJavaScriptLookup("boolean", "JSBoolean");
/* 114 */     addJavaScriptLookup("JSON", "JSJSON");
/*     */     
/* 116 */     addJavaScriptLookup("Namespace", "E4XNamespace");
/* 117 */     addJavaScriptLookup("QName", "E4XQName");
/* 118 */     addJavaScriptLookup("XML", "E4XXML");
/* 119 */     addJavaScriptLookup("XMLList", "E4XXMLList");
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract void loadTypes();
/*     */ 
/*     */   
/*     */   public void addTypeDeclaration(String name, TypeDeclaration dec) {
/* 127 */     this.types.put(name, dec);
/*     */     
/* 129 */     addJavaScriptLookup(dec.getQualifiedName(), name);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getClassName(String lookupType) {
/* 134 */     TypeDeclaration dec = this.types.get(lookupType);
/* 135 */     return (dec != null) ? dec.getQualifiedName() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> getAllClasses() {
/* 140 */     List<String> classes = new ArrayList<>();
/*     */     
/* 142 */     for (String name : this.types.keySet()) {
/* 143 */       TypeDeclaration dec = this.types.get(name);
/* 144 */       if (dec != null) {
/* 145 */         classes.add(dec.getQualifiedName());
/*     */       }
/*     */     } 
/* 148 */     return classes;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<TypeDeclaration> getAllJavaScriptTypeDeclarations() {
/* 153 */     List<TypeDeclaration> jsTypes = new ArrayList<>();
/*     */     
/* 155 */     for (String name : this.types.keySet()) {
/* 156 */       TypeDeclaration dec = this.types.get(name);
/* 157 */       if (isJavaScriptType(dec)) {
/* 158 */         jsTypes.add(dec);
/*     */       }
/*     */     } 
/* 161 */     return jsTypes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addJavaScriptLookup(String apiName, String jsName) {
/* 172 */     this.javascriptReverseLookup.put(apiName, jsName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeType(String name) {
/* 183 */     this.types.remove(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isJavaScriptType(TypeDeclaration td) {
/* 194 */     return (td != null && td.getPackageName() != null && td
/* 195 */       .getPackageName().startsWith("org.fife.rsta.ac.js.ecma.api"));
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
/*     */   public TypeDeclaration getTypeDeclaration(String name) {
/* 208 */     if (name == null) {
/* 209 */       return null;
/*     */     }
/* 211 */     TypeDeclaration typeDeclation = this.types.get(name);
/* 212 */     if (typeDeclation == null) {
/* 213 */       typeDeclation = getJSType(name);
/*     */     }
/* 215 */     return typeDeclation;
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
/*     */   private TypeDeclaration getJSType(String lookupName) {
/* 229 */     if (lookupName.indexOf('[') > -1 && lookupName.indexOf(']') > -1) {
/* 230 */       TypeDeclaration arrayType = getTypeDeclaration("JSArray");
/*     */ 
/*     */       
/* 233 */       ArrayTypeDeclaration arrayDec = new ArrayTypeDeclaration(arrayType.getPackageName(), arrayType.getAPITypeName(), arrayType.getJSName());
/*     */ 
/*     */       
/* 236 */       String arrayTypeName = lookupName.substring(0, lookupName
/* 237 */           .indexOf('['));
/*     */       
/* 239 */       TypeDeclaration containerType = JavaScriptHelper.createNewTypeDeclaration(arrayTypeName);
/* 240 */       arrayDec.setArrayType(containerType);
/* 241 */       return (TypeDeclaration)arrayDec;
/*     */     } 
/*     */     
/* 244 */     String name = this.javascriptReverseLookup.get(lookupName);
/* 245 */     if (name != null) {
/* 246 */       return this.types.get(name);
/*     */     }
/*     */ 
/*     */     
/* 250 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<JavaScriptObject> getJavaScriptObjects() {
/* 255 */     return this.ecmaObjects;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canECMAObjectBeInstantiated(String name) {
/* 266 */     String tempName = this.javascriptReverseLookup.get(name);
/* 267 */     if (tempName != null) {
/* 268 */       name = tempName;
/*     */     }
/* 270 */     for (JavaScriptObject jo : this.ecmaObjects) {
/* 271 */       if (jo.getName().equals(name)) {
/* 272 */         return jo.canBeInstantiated();
/*     */       }
/*     */     } 
/*     */     
/* 276 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class JavaScriptObject
/*     */   {
/*     */     private String name;
/*     */ 
/*     */     
/*     */     private boolean canBeInstantiated;
/*     */ 
/*     */     
/*     */     public JavaScriptObject(String name, boolean canBeInstantiated) {
/* 290 */       this.name = name;
/* 291 */       this.canBeInstantiated = canBeInstantiated;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getName() {
/* 296 */       return this.name;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canBeInstantiated() {
/* 301 */       return this.canBeInstantiated;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object jsObj) {
/* 306 */       if (jsObj == this) {
/* 307 */         return true;
/*     */       }
/* 309 */       if (jsObj instanceof JavaScriptObject)
/*     */       {
/* 311 */         return ((JavaScriptObject)jsObj).getName().equals(getName());
/*     */       }
/*     */       
/* 314 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 320 */       return this.name.hashCode();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\ast\type\ecma\TypeDeclarations.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */