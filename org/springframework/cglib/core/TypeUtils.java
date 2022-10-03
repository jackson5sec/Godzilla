/*     */ package org.springframework.cglib.core;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.asm.Type;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TypeUtils
/*     */ {
/*  22 */   private static final Map transforms = new HashMap<Object, Object>();
/*  23 */   private static final Map rtransforms = new HashMap<Object, Object>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*  29 */     transforms.put("void", "V");
/*  30 */     transforms.put("byte", "B");
/*  31 */     transforms.put("char", "C");
/*  32 */     transforms.put("double", "D");
/*  33 */     transforms.put("float", "F");
/*  34 */     transforms.put("int", "I");
/*  35 */     transforms.put("long", "J");
/*  36 */     transforms.put("short", "S");
/*  37 */     transforms.put("boolean", "Z");
/*     */     
/*  39 */     CollectionUtils.reverse(transforms, rtransforms);
/*     */   }
/*     */   
/*     */   public static Type getType(String className) {
/*  43 */     return Type.getType("L" + className.replace('.', '/') + ";");
/*     */   }
/*     */   
/*     */   public static boolean isFinal(int access) {
/*  47 */     return ((0x10 & access) != 0);
/*     */   }
/*     */   
/*     */   public static boolean isStatic(int access) {
/*  51 */     return ((0x8 & access) != 0);
/*     */   }
/*     */   
/*     */   public static boolean isProtected(int access) {
/*  55 */     return ((0x4 & access) != 0);
/*     */   }
/*     */   
/*     */   public static boolean isPublic(int access) {
/*  59 */     return ((0x1 & access) != 0);
/*     */   }
/*     */   
/*     */   public static boolean isAbstract(int access) {
/*  63 */     return ((0x400 & access) != 0);
/*     */   }
/*     */   
/*     */   public static boolean isInterface(int access) {
/*  67 */     return ((0x200 & access) != 0);
/*     */   }
/*     */   
/*     */   public static boolean isPrivate(int access) {
/*  71 */     return ((0x2 & access) != 0);
/*     */   }
/*     */   
/*     */   public static boolean isSynthetic(int access) {
/*  75 */     return ((0x1000 & access) != 0);
/*     */   }
/*     */   
/*     */   public static boolean isBridge(int access) {
/*  79 */     return ((0x40 & access) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getPackageName(Type type) {
/*  84 */     return getPackageName(getClassName(type));
/*     */   }
/*     */   
/*     */   public static String getPackageName(String className) {
/*  88 */     int idx = className.lastIndexOf('.');
/*  89 */     return (idx < 0) ? "" : className.substring(0, idx);
/*     */   }
/*     */   
/*     */   public static String upperFirst(String s) {
/*  93 */     if (s == null || s.length() == 0) {
/*  94 */       return s;
/*     */     }
/*  96 */     return Character.toUpperCase(s.charAt(0)) + s.substring(1);
/*     */   }
/*     */   
/*     */   public static String getClassName(Type type) {
/* 100 */     if (isPrimitive(type))
/* 101 */       return (String)rtransforms.get(type.getDescriptor()); 
/* 102 */     if (isArray(type)) {
/* 103 */       return getClassName(getComponentType(type)) + "[]";
/*     */     }
/* 105 */     return type.getClassName();
/*     */   }
/*     */ 
/*     */   
/*     */   public static Type[] add(Type[] types, Type extra) {
/* 110 */     if (types == null) {
/* 111 */       return new Type[] { extra };
/*     */     }
/* 113 */     List<Type> list = Arrays.asList(types);
/* 114 */     if (list.contains(extra)) {
/* 115 */       return types;
/*     */     }
/* 117 */     Type[] copy = new Type[types.length + 1];
/* 118 */     System.arraycopy(types, 0, copy, 0, types.length);
/* 119 */     copy[types.length] = extra;
/* 120 */     return copy;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Type[] add(Type[] t1, Type[] t2) {
/* 126 */     Type[] all = new Type[t1.length + t2.length];
/* 127 */     System.arraycopy(t1, 0, all, 0, t1.length);
/* 128 */     System.arraycopy(t2, 0, all, t1.length, t2.length);
/* 129 */     return all;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Type fromInternalName(String name) {
/* 134 */     return Type.getType("L" + name + ";");
/*     */   }
/*     */   
/*     */   public static Type[] fromInternalNames(String[] names) {
/* 138 */     if (names == null) {
/* 139 */       return null;
/*     */     }
/* 141 */     Type[] types = new Type[names.length];
/* 142 */     for (int i = 0; i < names.length; i++) {
/* 143 */       types[i] = fromInternalName(names[i]);
/*     */     }
/* 145 */     return types;
/*     */   }
/*     */   
/*     */   public static int getStackSize(Type[] types) {
/* 149 */     int size = 0;
/* 150 */     for (int i = 0; i < types.length; i++) {
/* 151 */       size += types[i].getSize();
/*     */     }
/* 153 */     return size;
/*     */   }
/*     */   
/*     */   public static String[] toInternalNames(Type[] types) {
/* 157 */     if (types == null) {
/* 158 */       return null;
/*     */     }
/* 160 */     String[] names = new String[types.length];
/* 161 */     for (int i = 0; i < types.length; i++) {
/* 162 */       names[i] = types[i].getInternalName();
/*     */     }
/* 164 */     return names;
/*     */   }
/*     */   
/*     */   public static Signature parseSignature(String s) {
/* 168 */     int space = s.indexOf(' ');
/* 169 */     int lparen = s.indexOf('(', space);
/* 170 */     int rparen = s.indexOf(')', lparen);
/* 171 */     String returnType = s.substring(0, space);
/* 172 */     String methodName = s.substring(space + 1, lparen);
/* 173 */     StringBuffer sb = new StringBuffer();
/* 174 */     sb.append('(');
/* 175 */     for (Iterator it = parseTypes(s, lparen + 1, rparen).iterator(); it.hasNext();) {
/* 176 */       sb.append(it.next());
/*     */     }
/* 178 */     sb.append(')');
/* 179 */     sb.append(map(returnType));
/* 180 */     return new Signature(methodName, sb.toString());
/*     */   }
/*     */   
/*     */   public static Type parseType(String s) {
/* 184 */     return Type.getType(map(s));
/*     */   }
/*     */   
/*     */   public static Type[] parseTypes(String s) {
/* 188 */     List<String> names = parseTypes(s, 0, s.length());
/* 189 */     Type[] types = new Type[names.size()];
/* 190 */     for (int i = 0; i < types.length; i++) {
/* 191 */       types[i] = Type.getType(names.get(i));
/*     */     }
/* 193 */     return types;
/*     */   }
/*     */   
/*     */   public static Signature parseConstructor(Type[] types) {
/* 197 */     StringBuffer sb = new StringBuffer();
/* 198 */     sb.append("(");
/* 199 */     for (int i = 0; i < types.length; i++) {
/* 200 */       sb.append(types[i].getDescriptor());
/*     */     }
/* 202 */     sb.append(")");
/* 203 */     sb.append("V");
/* 204 */     return new Signature("<init>", sb.toString());
/*     */   }
/*     */   
/*     */   public static Signature parseConstructor(String sig) {
/* 208 */     return parseSignature("void <init>(" + sig + ")");
/*     */   }
/*     */   
/*     */   private static List parseTypes(String s, int mark, int end) {
/* 212 */     List<String> types = new ArrayList(5);
/*     */     while (true) {
/* 214 */       int next = s.indexOf(',', mark);
/* 215 */       if (next < 0) {
/*     */         break;
/*     */       }
/* 218 */       types.add(map(s.substring(mark, next).trim()));
/* 219 */       mark = next + 1;
/*     */     } 
/* 221 */     types.add(map(s.substring(mark, end).trim()));
/* 222 */     return types;
/*     */   }
/*     */   
/*     */   private static String map(String type) {
/* 226 */     if (type.equals("")) {
/* 227 */       return type;
/*     */     }
/* 229 */     String t = (String)transforms.get(type);
/* 230 */     if (t != null)
/* 231 */       return t; 
/* 232 */     if (type.indexOf('.') < 0) {
/* 233 */       return map("java.lang." + type);
/*     */     }
/* 235 */     StringBuffer sb = new StringBuffer();
/* 236 */     int index = 0;
/* 237 */     while ((index = type.indexOf("[]", index) + 1) > 0) {
/* 238 */       sb.append('[');
/*     */     }
/* 240 */     type = type.substring(0, type.length() - sb.length() * 2);
/* 241 */     sb.append('L').append(type.replace('.', '/')).append(';');
/* 242 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static Type getBoxedType(Type type) {
/* 247 */     switch (type.getSort()) {
/*     */       case 2:
/* 249 */         return Constants.TYPE_CHARACTER;
/*     */       case 1:
/* 251 */         return Constants.TYPE_BOOLEAN;
/*     */       case 8:
/* 253 */         return Constants.TYPE_DOUBLE;
/*     */       case 6:
/* 255 */         return Constants.TYPE_FLOAT;
/*     */       case 7:
/* 257 */         return Constants.TYPE_LONG;
/*     */       case 5:
/* 259 */         return Constants.TYPE_INTEGER;
/*     */       case 4:
/* 261 */         return Constants.TYPE_SHORT;
/*     */       case 3:
/* 263 */         return Constants.TYPE_BYTE;
/*     */     } 
/* 265 */     return type;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Type getUnboxedType(Type type) {
/* 270 */     if (Constants.TYPE_INTEGER.equals(type))
/* 271 */       return Type.INT_TYPE; 
/* 272 */     if (Constants.TYPE_BOOLEAN.equals(type))
/* 273 */       return Type.BOOLEAN_TYPE; 
/* 274 */     if (Constants.TYPE_DOUBLE.equals(type))
/* 275 */       return Type.DOUBLE_TYPE; 
/* 276 */     if (Constants.TYPE_LONG.equals(type))
/* 277 */       return Type.LONG_TYPE; 
/* 278 */     if (Constants.TYPE_CHARACTER.equals(type))
/* 279 */       return Type.CHAR_TYPE; 
/* 280 */     if (Constants.TYPE_BYTE.equals(type))
/* 281 */       return Type.BYTE_TYPE; 
/* 282 */     if (Constants.TYPE_FLOAT.equals(type))
/* 283 */       return Type.FLOAT_TYPE; 
/* 284 */     if (Constants.TYPE_SHORT.equals(type)) {
/* 285 */       return Type.SHORT_TYPE;
/*     */     }
/* 287 */     return type;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isArray(Type type) {
/* 292 */     return (type.getSort() == 9);
/*     */   }
/*     */   
/*     */   public static Type getComponentType(Type type) {
/* 296 */     if (!isArray(type)) {
/* 297 */       throw new IllegalArgumentException("Type " + type + " is not an array");
/*     */     }
/* 299 */     return Type.getType(type.getDescriptor().substring(1));
/*     */   }
/*     */   
/*     */   public static boolean isPrimitive(Type type) {
/* 303 */     switch (type.getSort()) {
/*     */       case 9:
/*     */       case 10:
/* 306 */         return false;
/*     */     } 
/* 308 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String emulateClassGetName(Type type) {
/* 313 */     if (isArray(type)) {
/* 314 */       return type.getDescriptor().replace('/', '.');
/*     */     }
/* 316 */     return getClassName(type);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isConstructor(MethodInfo method) {
/* 321 */     return method.getSignature().getName().equals("<init>");
/*     */   }
/*     */   
/*     */   public static Type[] getTypes(Class[] classes) {
/* 325 */     if (classes == null) {
/* 326 */       return null;
/*     */     }
/* 328 */     Type[] types = new Type[classes.length];
/* 329 */     for (int i = 0; i < classes.length; i++) {
/* 330 */       types[i] = Type.getType(classes[i]);
/*     */     }
/* 332 */     return types;
/*     */   }
/*     */   
/*     */   public static int ICONST(int value) {
/* 336 */     switch (value) { case -1:
/* 337 */         return 2;
/* 338 */       case 0: return 3;
/* 339 */       case 1: return 4;
/* 340 */       case 2: return 5;
/* 341 */       case 3: return 6;
/* 342 */       case 4: return 7;
/* 343 */       case 5: return 8; }
/*     */     
/* 345 */     return -1;
/*     */   }
/*     */   
/*     */   public static int LCONST(long value) {
/* 349 */     if (value == 0L)
/* 350 */       return 9; 
/* 351 */     if (value == 1L) {
/* 352 */       return 10;
/*     */     }
/* 354 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int FCONST(float value) {
/* 359 */     if (value == 0.0F)
/* 360 */       return 11; 
/* 361 */     if (value == 1.0F)
/* 362 */       return 12; 
/* 363 */     if (value == 2.0F) {
/* 364 */       return 13;
/*     */     }
/* 366 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int DCONST(double value) {
/* 371 */     if (value == 0.0D)
/* 372 */       return 14; 
/* 373 */     if (value == 1.0D) {
/* 374 */       return 15;
/*     */     }
/* 376 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int NEWARRAY(Type type) {
/* 381 */     switch (type.getSort()) {
/*     */       case 3:
/* 383 */         return 8;
/*     */       case 2:
/* 385 */         return 5;
/*     */       case 8:
/* 387 */         return 7;
/*     */       case 6:
/* 389 */         return 6;
/*     */       case 5:
/* 391 */         return 10;
/*     */       case 7:
/* 393 */         return 11;
/*     */       case 4:
/* 395 */         return 9;
/*     */       case 1:
/* 397 */         return 4;
/*     */     } 
/* 399 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String escapeType(String s) {
/* 404 */     StringBuffer sb = new StringBuffer();
/* 405 */     for (int i = 0, len = s.length(); i < len; i++) {
/* 406 */       char c = s.charAt(i);
/* 407 */       switch (c) { case '$':
/* 408 */           sb.append("$24"); break;
/* 409 */         case '.': sb.append("$2E"); break;
/* 410 */         case '[': sb.append("$5B"); break;
/* 411 */         case ';': sb.append("$3B"); break;
/* 412 */         case '(': sb.append("$28"); break;
/* 413 */         case ')': sb.append("$29"); break;
/* 414 */         case '/': sb.append("$2F"); break;
/*     */         default:
/* 416 */           sb.append(c); break; }
/*     */     
/*     */     } 
/* 419 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\core\TypeUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */