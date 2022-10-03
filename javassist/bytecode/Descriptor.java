/*     */ package javassist.bytecode;
/*     */ 
/*     */ import java.util.Map;
/*     */ import javassist.ClassPool;
/*     */ import javassist.CtClass;
/*     */ import javassist.CtPrimitiveType;
/*     */ import javassist.NotFoundException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Descriptor
/*     */ {
/*     */   public static String toJvmName(String classname) {
/*  40 */     return classname.replace('.', '/');
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
/*     */   public static String toJavaName(String classname) {
/*  53 */     return classname.replace('/', '.');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toJvmName(CtClass clazz) {
/*  61 */     if (clazz.isArray())
/*  62 */       return of(clazz); 
/*  63 */     return toJvmName(clazz.getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toClassName(String descriptor) {
/*     */     String name;
/*  72 */     int arrayDim = 0;
/*  73 */     int i = 0;
/*  74 */     char c = descriptor.charAt(0);
/*  75 */     while (c == '[') {
/*  76 */       arrayDim++;
/*  77 */       c = descriptor.charAt(++i);
/*     */     } 
/*     */ 
/*     */     
/*  81 */     if (c == 'L') {
/*  82 */       int i2 = descriptor.indexOf(';', i++);
/*  83 */       name = descriptor.substring(i, i2).replace('/', '.');
/*  84 */       i = i2;
/*     */     }
/*  86 */     else if (c == 'V') {
/*  87 */       name = "void";
/*  88 */     } else if (c == 'I') {
/*  89 */       name = "int";
/*  90 */     } else if (c == 'B') {
/*  91 */       name = "byte";
/*  92 */     } else if (c == 'J') {
/*  93 */       name = "long";
/*  94 */     } else if (c == 'D') {
/*  95 */       name = "double";
/*  96 */     } else if (c == 'F') {
/*  97 */       name = "float";
/*  98 */     } else if (c == 'C') {
/*  99 */       name = "char";
/* 100 */     } else if (c == 'S') {
/* 101 */       name = "short";
/* 102 */     } else if (c == 'Z') {
/* 103 */       name = "boolean";
/*     */     } else {
/* 105 */       throw new RuntimeException("bad descriptor: " + descriptor);
/*     */     } 
/* 107 */     if (i + 1 != descriptor.length()) {
/* 108 */       throw new RuntimeException("multiple descriptors?: " + descriptor);
/*     */     }
/* 110 */     if (arrayDim == 0)
/* 111 */       return name; 
/* 112 */     StringBuffer sbuf = new StringBuffer(name);
/*     */     do {
/* 114 */       sbuf.append("[]");
/* 115 */     } while (--arrayDim > 0);
/*     */     
/* 117 */     return sbuf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String of(String classname) {
/* 124 */     if (classname.equals("void"))
/* 125 */       return "V"; 
/* 126 */     if (classname.equals("int"))
/* 127 */       return "I"; 
/* 128 */     if (classname.equals("byte"))
/* 129 */       return "B"; 
/* 130 */     if (classname.equals("long"))
/* 131 */       return "J"; 
/* 132 */     if (classname.equals("double"))
/* 133 */       return "D"; 
/* 134 */     if (classname.equals("float"))
/* 135 */       return "F"; 
/* 136 */     if (classname.equals("char"))
/* 137 */       return "C"; 
/* 138 */     if (classname.equals("short"))
/* 139 */       return "S"; 
/* 140 */     if (classname.equals("boolean")) {
/* 141 */       return "Z";
/*     */     }
/* 143 */     return "L" + toJvmName(classname) + ";";
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
/*     */   public static String rename(String desc, String oldname, String newname) {
/* 157 */     if (desc.indexOf(oldname) < 0) {
/* 158 */       return desc;
/*     */     }
/* 160 */     StringBuffer newdesc = new StringBuffer();
/* 161 */     int head = 0;
/* 162 */     int i = 0;
/*     */     while (true) {
/* 164 */       int j = desc.indexOf('L', i);
/* 165 */       if (j < 0)
/*     */         break; 
/* 167 */       if (desc.startsWith(oldname, j + 1) && desc
/* 168 */         .charAt(j + oldname.length() + 1) == ';') {
/* 169 */         newdesc.append(desc.substring(head, j));
/* 170 */         newdesc.append('L');
/* 171 */         newdesc.append(newname);
/* 172 */         newdesc.append(';');
/* 173 */         head = i = j + oldname.length() + 2;
/*     */         continue;
/*     */       } 
/* 176 */       i = desc.indexOf(';', j) + 1;
/* 177 */       if (i < 1) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */     
/* 182 */     if (head == 0)
/* 183 */       return desc; 
/* 184 */     int len = desc.length();
/* 185 */     if (head < len) {
/* 186 */       newdesc.append(desc.substring(head, len));
/*     */     }
/* 188 */     return newdesc.toString();
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
/*     */   public static String rename(String desc, Map<String, String> map) {
/* 200 */     if (map == null) {
/* 201 */       return desc;
/*     */     }
/* 203 */     StringBuffer newdesc = new StringBuffer();
/* 204 */     int head = 0;
/* 205 */     int i = 0;
/*     */     while (true) {
/* 207 */       int j = desc.indexOf('L', i);
/* 208 */       if (j < 0) {
/*     */         break;
/*     */       }
/* 211 */       int k = desc.indexOf(';', j);
/* 212 */       if (k < 0) {
/*     */         break;
/*     */       }
/* 215 */       i = k + 1;
/* 216 */       String name = desc.substring(j + 1, k);
/* 217 */       String name2 = map.get(name);
/* 218 */       if (name2 != null) {
/* 219 */         newdesc.append(desc.substring(head, j));
/* 220 */         newdesc.append('L');
/* 221 */         newdesc.append(name2);
/* 222 */         newdesc.append(';');
/* 223 */         head = i;
/*     */       } 
/*     */     } 
/*     */     
/* 227 */     if (head == 0)
/* 228 */       return desc; 
/* 229 */     int len = desc.length();
/* 230 */     if (head < len) {
/* 231 */       newdesc.append(desc.substring(head, len));
/*     */     }
/* 233 */     return newdesc.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String of(CtClass type) {
/* 240 */     StringBuffer sbuf = new StringBuffer();
/* 241 */     toDescriptor(sbuf, type);
/* 242 */     return sbuf.toString();
/*     */   }
/*     */   
/*     */   private static void toDescriptor(StringBuffer desc, CtClass type) {
/* 246 */     if (type.isArray()) {
/* 247 */       desc.append('[');
/*     */       try {
/* 249 */         toDescriptor(desc, type.getComponentType());
/*     */       }
/* 251 */       catch (NotFoundException e) {
/* 252 */         desc.append('L');
/* 253 */         String name = type.getName();
/* 254 */         desc.append(toJvmName(name.substring(0, name.length() - 2)));
/* 255 */         desc.append(';');
/*     */       }
/*     */     
/* 258 */     } else if (type.isPrimitive()) {
/* 259 */       CtPrimitiveType pt = (CtPrimitiveType)type;
/* 260 */       desc.append(pt.getDescriptor());
/*     */     } else {
/*     */       
/* 263 */       desc.append('L');
/* 264 */       desc.append(type.getName().replace('.', '/'));
/* 265 */       desc.append(';');
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String ofConstructor(CtClass[] paramTypes) {
/* 276 */     return ofMethod(CtClass.voidType, paramTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String ofMethod(CtClass returnType, CtClass[] paramTypes) {
/* 287 */     StringBuffer desc = new StringBuffer();
/* 288 */     desc.append('(');
/* 289 */     if (paramTypes != null) {
/* 290 */       int n = paramTypes.length;
/* 291 */       for (int i = 0; i < n; i++) {
/* 292 */         toDescriptor(desc, paramTypes[i]);
/*     */       }
/*     */     } 
/* 295 */     desc.append(')');
/* 296 */     if (returnType != null) {
/* 297 */       toDescriptor(desc, returnType);
/*     */     }
/* 299 */     return desc.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String ofParameters(CtClass[] paramTypes) {
/* 310 */     return ofMethod(null, paramTypes);
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
/*     */   public static String appendParameter(String classname, String desc) {
/* 323 */     int i = desc.indexOf(')');
/* 324 */     if (i < 0)
/* 325 */       return desc; 
/* 326 */     StringBuffer newdesc = new StringBuffer();
/* 327 */     newdesc.append(desc.substring(0, i));
/* 328 */     newdesc.append('L');
/* 329 */     newdesc.append(classname.replace('.', '/'));
/* 330 */     newdesc.append(';');
/* 331 */     newdesc.append(desc.substring(i));
/* 332 */     return newdesc.toString();
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
/*     */   public static String insertParameter(String classname, String desc) {
/* 346 */     if (desc.charAt(0) != '(')
/* 347 */       return desc; 
/* 348 */     return "(L" + classname.replace('.', '/') + ';' + desc
/* 349 */       .substring(1);
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
/*     */   public static String appendParameter(CtClass type, String descriptor) {
/* 361 */     int i = descriptor.indexOf(')');
/* 362 */     if (i < 0)
/* 363 */       return descriptor; 
/* 364 */     StringBuffer newdesc = new StringBuffer();
/* 365 */     newdesc.append(descriptor.substring(0, i));
/* 366 */     toDescriptor(newdesc, type);
/* 367 */     newdesc.append(descriptor.substring(i));
/* 368 */     return newdesc.toString();
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
/*     */   public static String insertParameter(CtClass type, String descriptor) {
/* 381 */     if (descriptor.charAt(0) != '(')
/* 382 */       return descriptor; 
/* 383 */     return "(" + of(type) + descriptor.substring(1);
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
/*     */   public static String changeReturnType(String classname, String desc) {
/* 395 */     int i = desc.indexOf(')');
/* 396 */     if (i < 0)
/* 397 */       return desc; 
/* 398 */     StringBuffer newdesc = new StringBuffer();
/* 399 */     newdesc.append(desc.substring(0, i + 1));
/* 400 */     newdesc.append('L');
/* 401 */     newdesc.append(classname.replace('.', '/'));
/* 402 */     newdesc.append(';');
/* 403 */     return newdesc.toString();
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
/*     */   public static CtClass[] getParameterTypes(String desc, ClassPool cp) throws NotFoundException {
/* 417 */     if (desc.charAt(0) != '(')
/* 418 */       return null; 
/* 419 */     int num = numOfParameters(desc);
/* 420 */     CtClass[] args = new CtClass[num];
/* 421 */     int n = 0;
/* 422 */     int i = 1;
/*     */     while (true) {
/* 424 */       i = toCtClass(cp, desc, i, args, n++);
/* 425 */       if (i <= 0) {
/* 426 */         return args;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean eqParamTypes(String desc1, String desc2) {
/* 435 */     if (desc1.charAt(0) != '(') {
/* 436 */       return false;
/*     */     }
/* 438 */     for (int i = 0;; i++) {
/* 439 */       char c = desc1.charAt(i);
/* 440 */       if (c != desc2.charAt(i)) {
/* 441 */         return false;
/*     */       }
/* 443 */       if (c == ')') {
/* 444 */         return true;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getParamDescriptor(String decl) {
/* 454 */     return decl.substring(0, decl.indexOf(')') + 1);
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
/*     */   public static CtClass getReturnType(String desc, ClassPool cp) throws NotFoundException {
/* 468 */     int i = desc.indexOf(')');
/* 469 */     if (i < 0)
/* 470 */       return null; 
/* 471 */     CtClass[] type = new CtClass[1];
/* 472 */     toCtClass(cp, desc, i + 1, type, 0);
/* 473 */     return type[0];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int numOfParameters(String desc) {
/* 483 */     int n = 0;
/* 484 */     int i = 1;
/*     */     while (true) {
/* 486 */       char c = desc.charAt(i);
/* 487 */       if (c == ')') {
/*     */         break;
/*     */       }
/* 490 */       while (c == '[') {
/* 491 */         c = desc.charAt(++i);
/*     */       }
/* 493 */       if (c == 'L') {
/* 494 */         i = desc.indexOf(';', i) + 1;
/* 495 */         if (i <= 0) {
/* 496 */           throw new IndexOutOfBoundsException("bad descriptor");
/*     */         }
/*     */       } else {
/* 499 */         i++;
/*     */       } 
/* 501 */       n++;
/*     */     } 
/*     */     
/* 504 */     return n;
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
/*     */   public static CtClass toCtClass(String desc, ClassPool cp) throws NotFoundException {
/* 523 */     CtClass[] clazz = new CtClass[1];
/* 524 */     int res = toCtClass(cp, desc, 0, clazz, 0);
/* 525 */     if (res >= 0) {
/* 526 */       return clazz[0];
/*     */     }
/*     */     
/* 529 */     return cp.get(desc.replace('/', '.'));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int toCtClass(ClassPool cp, String desc, int i, CtClass[] args, int n) throws NotFoundException {
/*     */     int i2;
/*     */     String name;
/* 539 */     int arrayDim = 0;
/* 540 */     char c = desc.charAt(i);
/* 541 */     while (c == '[') {
/* 542 */       arrayDim++;
/* 543 */       c = desc.charAt(++i);
/*     */     } 
/*     */     
/* 546 */     if (c == 'L') {
/* 547 */       i2 = desc.indexOf(';', ++i);
/* 548 */       name = desc.substring(i, i2++).replace('/', '.');
/*     */     } else {
/*     */       
/* 551 */       CtClass type = toPrimitiveClass(c);
/* 552 */       if (type == null) {
/* 553 */         return -1;
/*     */       }
/* 555 */       i2 = i + 1;
/* 556 */       if (arrayDim == 0) {
/* 557 */         args[n] = type;
/* 558 */         return i2;
/*     */       } 
/* 560 */       name = type.getName();
/*     */     } 
/*     */     
/* 563 */     if (arrayDim > 0) {
/* 564 */       StringBuffer sbuf = new StringBuffer(name);
/* 565 */       while (arrayDim-- > 0) {
/* 566 */         sbuf.append("[]");
/*     */       }
/* 568 */       name = sbuf.toString();
/*     */     } 
/*     */     
/* 571 */     args[n] = cp.get(name);
/* 572 */     return i2;
/*     */   }
/*     */   
/*     */   static CtClass toPrimitiveClass(char c) {
/* 576 */     CtClass type = null;
/* 577 */     switch (c) {
/*     */       case 'Z':
/* 579 */         type = CtClass.booleanType;
/*     */         break;
/*     */       case 'C':
/* 582 */         type = CtClass.charType;
/*     */         break;
/*     */       case 'B':
/* 585 */         type = CtClass.byteType;
/*     */         break;
/*     */       case 'S':
/* 588 */         type = CtClass.shortType;
/*     */         break;
/*     */       case 'I':
/* 591 */         type = CtClass.intType;
/*     */         break;
/*     */       case 'J':
/* 594 */         type = CtClass.longType;
/*     */         break;
/*     */       case 'F':
/* 597 */         type = CtClass.floatType;
/*     */         break;
/*     */       case 'D':
/* 600 */         type = CtClass.doubleType;
/*     */         break;
/*     */       case 'V':
/* 603 */         type = CtClass.voidType;
/*     */         break;
/*     */     } 
/*     */     
/* 607 */     return type;
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
/*     */   public static int arrayDimension(String desc) {
/* 619 */     int dim = 0;
/* 620 */     while (desc.charAt(dim) == '[') {
/* 621 */       dim++;
/*     */     }
/* 623 */     return dim;
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
/*     */   public static String toArrayComponent(String desc, int dim) {
/* 636 */     return desc.substring(dim);
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
/*     */   public static int dataSize(String desc) {
/* 651 */     return dataSize(desc, true);
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
/*     */   public static int paramSize(String desc) {
/* 664 */     return -dataSize(desc, false);
/*     */   }
/*     */   
/*     */   private static int dataSize(String desc, boolean withRet) {
/* 668 */     int n = 0;
/* 669 */     char c = desc.charAt(0);
/* 670 */     if (c == '(') {
/* 671 */       int i = 1;
/*     */       while (true) {
/* 673 */         c = desc.charAt(i);
/* 674 */         if (c == ')') {
/* 675 */           c = desc.charAt(i + 1);
/*     */           
/*     */           break;
/*     */         } 
/* 679 */         boolean array = false;
/* 680 */         while (c == '[') {
/* 681 */           array = true;
/* 682 */           c = desc.charAt(++i);
/*     */         } 
/*     */         
/* 685 */         if (c == 'L') {
/* 686 */           i = desc.indexOf(';', i) + 1;
/* 687 */           if (i <= 0) {
/* 688 */             throw new IndexOutOfBoundsException("bad descriptor");
/*     */           }
/*     */         } else {
/* 691 */           i++;
/*     */         } 
/* 693 */         if (!array && (c == 'J' || c == 'D')) {
/* 694 */           n -= 2; continue;
/*     */         } 
/* 696 */         n--;
/*     */       } 
/*     */     } 
/*     */     
/* 700 */     if (withRet)
/* 701 */       if (c == 'J' || c == 'D') {
/* 702 */         n += 2;
/* 703 */       } else if (c != 'V') {
/* 704 */         n++;
/*     */       }  
/* 706 */     return n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toString(String desc) {
/* 717 */     return PrettyPrinter.toString(desc);
/*     */   }
/*     */   
/*     */   static class PrettyPrinter {
/*     */     static String toString(String desc) {
/* 722 */       StringBuffer sbuf = new StringBuffer();
/* 723 */       if (desc.charAt(0) == '(') {
/* 724 */         int pos = 1;
/* 725 */         sbuf.append('(');
/* 726 */         while (desc.charAt(pos) != ')') {
/* 727 */           if (pos > 1) {
/* 728 */             sbuf.append(',');
/*     */           }
/* 730 */           pos = readType(sbuf, pos, desc);
/*     */         } 
/*     */         
/* 733 */         sbuf.append(')');
/*     */       } else {
/*     */         
/* 736 */         readType(sbuf, 0, desc);
/*     */       } 
/* 738 */       return sbuf.toString();
/*     */     }
/*     */     
/*     */     static int readType(StringBuffer sbuf, int pos, String desc) {
/* 742 */       char c = desc.charAt(pos);
/* 743 */       int arrayDim = 0;
/* 744 */       while (c == '[') {
/* 745 */         arrayDim++;
/* 746 */         c = desc.charAt(++pos);
/*     */       } 
/*     */       
/* 749 */       if (c == 'L') {
/*     */         while (true) {
/* 751 */           c = desc.charAt(++pos);
/* 752 */           if (c == ';') {
/*     */             break;
/*     */           }
/* 755 */           if (c == '/') {
/* 756 */             c = '.';
/*     */           }
/* 758 */           sbuf.append(c);
/*     */         } 
/*     */       } else {
/* 761 */         CtClass t = Descriptor.toPrimitiveClass(c);
/* 762 */         sbuf.append(t.getName());
/*     */       } 
/*     */       
/* 765 */       while (arrayDim-- > 0) {
/* 766 */         sbuf.append("[]");
/*     */       }
/* 768 */       return pos + 1;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Iterator
/*     */   {
/*     */     private String desc;
/*     */     
/*     */     private int index;
/*     */     
/*     */     private int curPos;
/*     */     
/*     */     private boolean param;
/*     */ 
/*     */     
/*     */     public Iterator(String s) {
/* 786 */       this.desc = s;
/* 787 */       this.index = this.curPos = 0;
/* 788 */       this.param = false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 795 */       return (this.index < this.desc.length());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isParameter() {
/* 801 */       return this.param;
/*     */     }
/*     */ 
/*     */     
/*     */     public char currentChar() {
/* 806 */       return this.desc.charAt(this.curPos);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean is2byte() {
/* 812 */       char c = currentChar();
/* 813 */       return (c == 'D' || c == 'J');
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int next() {
/* 821 */       int nextPos = this.index;
/* 822 */       char c = this.desc.charAt(nextPos);
/* 823 */       if (c == '(') {
/* 824 */         this.index++;
/* 825 */         c = this.desc.charAt(++nextPos);
/* 826 */         this.param = true;
/*     */       } 
/*     */       
/* 829 */       if (c == ')') {
/* 830 */         this.index++;
/* 831 */         c = this.desc.charAt(++nextPos);
/* 832 */         this.param = false;
/*     */       } 
/*     */       
/* 835 */       while (c == '[') {
/* 836 */         c = this.desc.charAt(++nextPos);
/*     */       }
/* 838 */       if (c == 'L') {
/* 839 */         nextPos = this.desc.indexOf(';', nextPos) + 1;
/* 840 */         if (nextPos <= 0) {
/* 841 */           throw new IndexOutOfBoundsException("bad descriptor");
/*     */         }
/*     */       } else {
/* 844 */         nextPos++;
/*     */       } 
/* 846 */       this.curPos = this.index;
/* 847 */       this.index = nextPos;
/* 848 */       return this.curPos;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\Descriptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */