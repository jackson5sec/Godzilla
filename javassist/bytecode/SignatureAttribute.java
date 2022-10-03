/*      */ package javassist.bytecode;
/*      */ 
/*      */ import java.io.DataInputStream;
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import javassist.CtClass;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class SignatureAttribute
/*      */   extends AttributeInfo
/*      */ {
/*      */   public static final String tag = "Signature";
/*      */   
/*      */   SignatureAttribute(ConstPool cp, int n, DataInputStream in) throws IOException {
/*   40 */     super(cp, n, in);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public SignatureAttribute(ConstPool cp, String signature) {
/*   50 */     super(cp, "Signature");
/*   51 */     int index = cp.addUtf8Info(signature);
/*   52 */     byte[] bvalue = new byte[2];
/*   53 */     bvalue[0] = (byte)(index >>> 8);
/*   54 */     bvalue[1] = (byte)index;
/*   55 */     set(bvalue);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getSignature() {
/*   66 */     return getConstPool().getUtf8Info(ByteArray.readU16bit(get(), 0));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSignature(String sig) {
/*   77 */     int index = getConstPool().addUtf8Info(sig);
/*   78 */     ByteArray.write16bit(index, this.info, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AttributeInfo copy(ConstPool newCp, Map<String, String> classnames) {
/*   91 */     return new SignatureAttribute(newCp, getSignature());
/*      */   }
/*      */ 
/*      */   
/*      */   void renameClass(String oldname, String newname) {
/*   96 */     String sig = renameClass(getSignature(), oldname, newname);
/*   97 */     setSignature(sig);
/*      */   }
/*      */ 
/*      */   
/*      */   void renameClass(Map<String, String> classnames) {
/*  102 */     String sig = renameClass(getSignature(), classnames);
/*  103 */     setSignature(sig);
/*      */   }
/*      */   
/*      */   static String renameClass(String desc, String oldname, String newname) {
/*  107 */     Map<String, String> map = new HashMap<>();
/*  108 */     map.put(oldname, newname);
/*  109 */     return renameClass(desc, map);
/*      */   }
/*      */   
/*      */   static String renameClass(String desc, Map<String, String> map) {
/*  113 */     if (map == null) {
/*  114 */       return desc;
/*      */     }
/*  116 */     StringBuilder newdesc = new StringBuilder();
/*  117 */     int head = 0;
/*  118 */     int i = 0; while (true) {
/*      */       char c;
/*  120 */       int j = desc.indexOf('L', i);
/*  121 */       if (j < 0) {
/*      */         break;
/*      */       }
/*  124 */       StringBuilder nameBuf = new StringBuilder();
/*  125 */       int k = j;
/*      */ 
/*      */       
/*  128 */       try { while ((c = desc.charAt(++k)) != ';') {
/*  129 */           nameBuf.append(c);
/*  130 */           if (c == '<') {
/*  131 */             while ((c = desc.charAt(++k)) != '>') {
/*  132 */               nameBuf.append(c);
/*      */             }
/*  134 */             nameBuf.append(c);
/*      */           }
/*      */         
/*      */         }  }
/*  138 */       catch (IndexOutOfBoundsException e) { break; }
/*  139 */        i = k + 1;
/*  140 */       String name = nameBuf.toString();
/*  141 */       String name2 = map.get(name);
/*  142 */       if (name2 != null) {
/*  143 */         newdesc.append(desc.substring(head, j));
/*  144 */         newdesc.append('L');
/*  145 */         newdesc.append(name2);
/*  146 */         newdesc.append(c);
/*  147 */         head = i;
/*      */       } 
/*      */     } 
/*      */     
/*  151 */     if (head == 0)
/*  152 */       return desc; 
/*  153 */     int len = desc.length();
/*  154 */     if (head < len) {
/*  155 */       newdesc.append(desc.substring(head, len));
/*      */     }
/*  157 */     return newdesc.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean isNamePart(int c) {
/*  162 */     return (c != 59 && c != 60);
/*      */   }
/*      */   
/*      */   private static class Cursor {
/*  166 */     int position = 0;
/*      */     
/*      */     int indexOf(String s, int ch) throws BadBytecode {
/*  169 */       int i = s.indexOf(ch, this.position);
/*  170 */       if (i < 0)
/*  171 */         throw SignatureAttribute.error(s); 
/*  172 */       this.position = i + 1;
/*  173 */       return i;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private Cursor() {}
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class ClassSignature
/*      */   {
/*      */     SignatureAttribute.TypeParameter[] params;
/*      */     
/*      */     SignatureAttribute.ClassType superClass;
/*      */     
/*      */     SignatureAttribute.ClassType[] interfaces;
/*      */ 
/*      */     
/*      */     public ClassSignature(SignatureAttribute.TypeParameter[] params, SignatureAttribute.ClassType superClass, SignatureAttribute.ClassType[] interfaces) {
/*  193 */       this.params = (params == null) ? new SignatureAttribute.TypeParameter[0] : params;
/*  194 */       this.superClass = (superClass == null) ? SignatureAttribute.ClassType.OBJECT : superClass;
/*  195 */       this.interfaces = (interfaces == null) ? new SignatureAttribute.ClassType[0] : interfaces;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ClassSignature(SignatureAttribute.TypeParameter[] p) {
/*  204 */       this(p, null, null);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public SignatureAttribute.TypeParameter[] getParameters() {
/*  213 */       return this.params;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public SignatureAttribute.ClassType getSuperClass() {
/*  219 */       return this.superClass;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public SignatureAttribute.ClassType[] getInterfaces() {
/*  226 */       return this.interfaces;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  233 */       StringBuffer sbuf = new StringBuffer();
/*      */       
/*  235 */       SignatureAttribute.TypeParameter.toString(sbuf, this.params);
/*  236 */       sbuf.append(" extends ").append(this.superClass);
/*  237 */       if (this.interfaces.length > 0) {
/*  238 */         sbuf.append(" implements ");
/*  239 */         SignatureAttribute.Type.toString(sbuf, (SignatureAttribute.Type[])this.interfaces);
/*      */       } 
/*      */       
/*  242 */       return sbuf.toString();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String encode() {
/*  249 */       StringBuffer sbuf = new StringBuffer();
/*  250 */       if (this.params.length > 0) {
/*  251 */         sbuf.append('<');
/*  252 */         for (int j = 0; j < this.params.length; j++) {
/*  253 */           this.params[j].encode(sbuf);
/*      */         }
/*  255 */         sbuf.append('>');
/*      */       } 
/*      */       
/*  258 */       this.superClass.encode(sbuf);
/*  259 */       for (int i = 0; i < this.interfaces.length; i++) {
/*  260 */         this.interfaces[i].encode(sbuf);
/*      */       }
/*  262 */       return sbuf.toString();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class MethodSignature
/*      */   {
/*      */     SignatureAttribute.TypeParameter[] typeParams;
/*      */ 
/*      */     
/*      */     SignatureAttribute.Type[] params;
/*      */ 
/*      */     
/*      */     SignatureAttribute.Type retType;
/*      */ 
/*      */     
/*      */     SignatureAttribute.ObjectType[] exceptions;
/*      */ 
/*      */ 
/*      */     
/*      */     public MethodSignature(SignatureAttribute.TypeParameter[] tp, SignatureAttribute.Type[] params, SignatureAttribute.Type ret, SignatureAttribute.ObjectType[] ex) {
/*  285 */       this.typeParams = (tp == null) ? new SignatureAttribute.TypeParameter[0] : tp;
/*  286 */       this.params = (params == null) ? new SignatureAttribute.Type[0] : params;
/*  287 */       this.retType = (ret == null) ? new SignatureAttribute.BaseType("void") : ret;
/*  288 */       this.exceptions = (ex == null) ? new SignatureAttribute.ObjectType[0] : ex;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public SignatureAttribute.TypeParameter[] getTypeParameters() {
/*  296 */       return this.typeParams;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public SignatureAttribute.Type[] getParameterTypes() {
/*  303 */       return this.params;
/*      */     }
/*      */ 
/*      */     
/*      */     public SignatureAttribute.Type getReturnType() {
/*  308 */       return this.retType;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public SignatureAttribute.ObjectType[] getExceptionTypes() {
/*  316 */       return this.exceptions;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  323 */       StringBuffer sbuf = new StringBuffer();
/*      */       
/*  325 */       SignatureAttribute.TypeParameter.toString(sbuf, this.typeParams);
/*  326 */       sbuf.append(" (");
/*  327 */       SignatureAttribute.Type.toString(sbuf, this.params);
/*  328 */       sbuf.append(") ");
/*  329 */       sbuf.append(this.retType);
/*  330 */       if (this.exceptions.length > 0) {
/*  331 */         sbuf.append(" throws ");
/*  332 */         SignatureAttribute.Type.toString(sbuf, (SignatureAttribute.Type[])this.exceptions);
/*      */       } 
/*      */       
/*  335 */       return sbuf.toString();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String encode() {
/*  342 */       StringBuffer sbuf = new StringBuffer();
/*  343 */       if (this.typeParams.length > 0) {
/*  344 */         sbuf.append('<');
/*  345 */         for (int j = 0; j < this.typeParams.length; j++) {
/*  346 */           this.typeParams[j].encode(sbuf);
/*      */         }
/*  348 */         sbuf.append('>');
/*      */       } 
/*      */       
/*  351 */       sbuf.append('('); int i;
/*  352 */       for (i = 0; i < this.params.length; i++) {
/*  353 */         this.params[i].encode(sbuf);
/*      */       }
/*  355 */       sbuf.append(')');
/*  356 */       this.retType.encode(sbuf);
/*  357 */       if (this.exceptions.length > 0) {
/*  358 */         for (i = 0; i < this.exceptions.length; i++) {
/*  359 */           sbuf.append('^');
/*  360 */           this.exceptions[i].encode(sbuf);
/*      */         } 
/*      */       }
/*  363 */       return sbuf.toString();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static class TypeParameter
/*      */   {
/*      */     String name;
/*      */     
/*      */     SignatureAttribute.ObjectType superClass;
/*      */     
/*      */     SignatureAttribute.ObjectType[] superInterfaces;
/*      */ 
/*      */     
/*      */     TypeParameter(String sig, int nb, int ne, SignatureAttribute.ObjectType sc, SignatureAttribute.ObjectType[] si) {
/*  378 */       this.name = sig.substring(nb, ne);
/*  379 */       this.superClass = sc;
/*  380 */       this.superInterfaces = si;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public TypeParameter(String name, SignatureAttribute.ObjectType superClass, SignatureAttribute.ObjectType[] superInterfaces) {
/*  392 */       this.name = name;
/*  393 */       this.superClass = superClass;
/*  394 */       if (superInterfaces == null) {
/*  395 */         this.superInterfaces = new SignatureAttribute.ObjectType[0];
/*      */       } else {
/*  397 */         this.superInterfaces = superInterfaces;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public TypeParameter(String name) {
/*  407 */       this(name, null, null);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getName() {
/*  414 */       return this.name;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public SignatureAttribute.ObjectType getClassBound() {
/*  420 */       return this.superClass;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public SignatureAttribute.ObjectType[] getInterfaceBound() {
/*  427 */       return this.superInterfaces;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  434 */       StringBuffer sbuf = new StringBuffer(getName());
/*  435 */       if (this.superClass != null) {
/*  436 */         sbuf.append(" extends ").append(this.superClass.toString());
/*      */       }
/*  438 */       int len = this.superInterfaces.length;
/*  439 */       if (len > 0) {
/*  440 */         for (int i = 0; i < len; i++) {
/*  441 */           if (i > 0 || this.superClass != null) {
/*  442 */             sbuf.append(" & ");
/*      */           } else {
/*  444 */             sbuf.append(" extends ");
/*      */           } 
/*  446 */           sbuf.append(this.superInterfaces[i].toString());
/*      */         } 
/*      */       }
/*      */       
/*  450 */       return sbuf.toString();
/*      */     }
/*      */     
/*      */     static void toString(StringBuffer sbuf, TypeParameter[] tp) {
/*  454 */       sbuf.append('<');
/*  455 */       for (int i = 0; i < tp.length; i++) {
/*  456 */         if (i > 0) {
/*  457 */           sbuf.append(", ");
/*      */         }
/*  459 */         sbuf.append(tp[i]);
/*      */       } 
/*      */       
/*  462 */       sbuf.append('>');
/*      */     }
/*      */     
/*      */     void encode(StringBuffer sb) {
/*  466 */       sb.append(this.name);
/*  467 */       if (this.superClass == null) {
/*  468 */         sb.append(":Ljava/lang/Object;");
/*      */       } else {
/*  470 */         sb.append(':');
/*  471 */         this.superClass.encode(sb);
/*      */       } 
/*      */       
/*  474 */       for (int i = 0; i < this.superInterfaces.length; i++) {
/*  475 */         sb.append(':');
/*  476 */         this.superInterfaces[i].encode(sb);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class TypeArgument
/*      */   {
/*      */     SignatureAttribute.ObjectType arg;
/*      */     
/*      */     char wildcard;
/*      */ 
/*      */     
/*      */     TypeArgument(SignatureAttribute.ObjectType a, char w) {
/*  491 */       this.arg = a;
/*  492 */       this.wildcard = w;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public TypeArgument(SignatureAttribute.ObjectType t) {
/*  503 */       this(t, ' ');
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public TypeArgument() {
/*  510 */       this(null, '*');
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static TypeArgument subclassOf(SignatureAttribute.ObjectType t) {
/*  520 */       return new TypeArgument(t, '+');
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static TypeArgument superOf(SignatureAttribute.ObjectType t) {
/*  530 */       return new TypeArgument(t, '-');
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public char getKind() {
/*  539 */       return this.wildcard;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isWildcard() {
/*  545 */       return (this.wildcard != ' ');
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public SignatureAttribute.ObjectType getType() {
/*  554 */       return this.arg;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  561 */       if (this.wildcard == '*') {
/*  562 */         return "?";
/*      */       }
/*  564 */       String type = this.arg.toString();
/*  565 */       if (this.wildcard == ' ')
/*  566 */         return type; 
/*  567 */       if (this.wildcard == '+') {
/*  568 */         return "? extends " + type;
/*      */       }
/*  570 */       return "? super " + type;
/*      */     }
/*      */     
/*      */     static void encode(StringBuffer sb, TypeArgument[] args) {
/*  574 */       sb.append('<');
/*  575 */       for (int i = 0; i < args.length; i++) {
/*  576 */         TypeArgument ta = args[i];
/*  577 */         if (ta.isWildcard()) {
/*  578 */           sb.append(ta.wildcard);
/*      */         }
/*  580 */         if (ta.getType() != null) {
/*  581 */           ta.getType().encode(sb);
/*      */         }
/*      */       } 
/*  584 */       sb.append('>');
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static abstract class Type
/*      */   {
/*      */     abstract void encode(StringBuffer param1StringBuffer);
/*      */     
/*      */     static void toString(StringBuffer sbuf, Type[] ts) {
/*  594 */       for (int i = 0; i < ts.length; i++) {
/*  595 */         if (i > 0) {
/*  596 */           sbuf.append(", ");
/*      */         }
/*  598 */         sbuf.append(ts[i]);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String jvmTypeName() {
/*  607 */       return toString();
/*      */     }
/*      */   }
/*      */   
/*      */   public static class BaseType extends Type {
/*      */     char descriptor;
/*      */     
/*      */     BaseType(char c) {
/*  615 */       this.descriptor = c;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public BaseType(String typeName) {
/*  623 */       this(Descriptor.of(typeName).charAt(0));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public char getDescriptor() {
/*  631 */       return this.descriptor;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public CtClass getCtlass() {
/*  638 */       return Descriptor.toPrimitiveClass(this.descriptor);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  646 */       return Descriptor.toClassName(Character.toString(this.descriptor));
/*      */     }
/*      */ 
/*      */     
/*      */     void encode(StringBuffer sb) {
/*  651 */       sb.append(this.descriptor);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static abstract class ObjectType
/*      */     extends Type
/*      */   {
/*      */     public String encode() {
/*  664 */       StringBuffer sb = new StringBuffer();
/*  665 */       encode(sb);
/*  666 */       return sb.toString();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static class ClassType
/*      */     extends ObjectType
/*      */   {
/*      */     String name;
/*      */     
/*      */     SignatureAttribute.TypeArgument[] arguments;
/*      */     
/*      */     static ClassType make(String s, int b, int e, SignatureAttribute.TypeArgument[] targs, ClassType parent) {
/*  679 */       if (parent == null)
/*  680 */         return new ClassType(s, b, e, targs); 
/*  681 */       return new SignatureAttribute.NestedClassType(s, b, e, targs, parent);
/*      */     }
/*      */     
/*      */     ClassType(String signature, int begin, int end, SignatureAttribute.TypeArgument[] targs) {
/*  685 */       this.name = signature.substring(begin, end).replace('/', '.');
/*  686 */       this.arguments = targs;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  692 */     public static ClassType OBJECT = new ClassType("java.lang.Object", null);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ClassType(String className, SignatureAttribute.TypeArgument[] args) {
/*  702 */       this.name = className;
/*  703 */       this.arguments = args;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ClassType(String className) {
/*  713 */       this(className, null);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getName() {
/*  720 */       return this.name;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public SignatureAttribute.TypeArgument[] getTypeArguments() {
/*  728 */       return this.arguments;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ClassType getDeclaringClass() {
/*  736 */       return null;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  743 */       StringBuffer sbuf = new StringBuffer();
/*  744 */       ClassType parent = getDeclaringClass();
/*  745 */       if (parent != null) {
/*  746 */         sbuf.append(parent.toString()).append('.');
/*      */       }
/*  748 */       return toString2(sbuf);
/*      */     }
/*      */     
/*      */     private String toString2(StringBuffer sbuf) {
/*  752 */       sbuf.append(this.name);
/*  753 */       if (this.arguments != null) {
/*  754 */         sbuf.append('<');
/*  755 */         int n = this.arguments.length;
/*  756 */         for (int i = 0; i < n; i++) {
/*  757 */           if (i > 0) {
/*  758 */             sbuf.append(", ");
/*      */           }
/*  760 */           sbuf.append(this.arguments[i].toString());
/*      */         } 
/*      */         
/*  763 */         sbuf.append('>');
/*      */       } 
/*      */       
/*  766 */       return sbuf.toString();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String jvmTypeName() {
/*  776 */       StringBuffer sbuf = new StringBuffer();
/*  777 */       ClassType parent = getDeclaringClass();
/*  778 */       if (parent != null) {
/*  779 */         sbuf.append(parent.jvmTypeName()).append('$');
/*      */       }
/*  781 */       return toString2(sbuf);
/*      */     }
/*      */ 
/*      */     
/*      */     void encode(StringBuffer sb) {
/*  786 */       sb.append('L');
/*  787 */       encode2(sb);
/*  788 */       sb.append(';');
/*      */     }
/*      */     
/*      */     void encode2(StringBuffer sb) {
/*  792 */       ClassType parent = getDeclaringClass();
/*  793 */       if (parent != null) {
/*  794 */         parent.encode2(sb);
/*  795 */         sb.append('$');
/*      */       } 
/*      */       
/*  798 */       sb.append(this.name.replace('.', '/'));
/*  799 */       if (this.arguments != null) {
/*  800 */         SignatureAttribute.TypeArgument.encode(sb, this.arguments);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static class NestedClassType
/*      */     extends ClassType
/*      */   {
/*      */     SignatureAttribute.ClassType parent;
/*      */     
/*      */     NestedClassType(String s, int b, int e, SignatureAttribute.TypeArgument[] targs, SignatureAttribute.ClassType p) {
/*  811 */       super(s, b, e, targs);
/*  812 */       this.parent = p;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public NestedClassType(SignatureAttribute.ClassType parent, String className, SignatureAttribute.TypeArgument[] args) {
/*  824 */       super(className, args);
/*  825 */       this.parent = parent;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public SignatureAttribute.ClassType getDeclaringClass() {
/*  833 */       return this.parent;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class ArrayType
/*      */     extends ObjectType
/*      */   {
/*      */     int dim;
/*      */ 
/*      */     
/*      */     SignatureAttribute.Type componentType;
/*      */ 
/*      */ 
/*      */     
/*      */     public ArrayType(int d, SignatureAttribute.Type comp) {
/*  850 */       this.dim = d;
/*  851 */       this.componentType = comp;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public int getDimension() {
/*  857 */       return this.dim;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public SignatureAttribute.Type getComponentType() {
/*  863 */       return this.componentType;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  871 */       StringBuffer sbuf = new StringBuffer(this.componentType.toString());
/*  872 */       for (int i = 0; i < this.dim; i++) {
/*  873 */         sbuf.append("[]");
/*      */       }
/*  875 */       return sbuf.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     void encode(StringBuffer sb) {
/*  880 */       for (int i = 0; i < this.dim; i++) {
/*  881 */         sb.append('[');
/*      */       }
/*  883 */       this.componentType.encode(sb);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static class TypeVariable
/*      */     extends ObjectType
/*      */   {
/*      */     String name;
/*      */     
/*      */     TypeVariable(String sig, int begin, int end) {
/*  894 */       this.name = sig.substring(begin, end);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public TypeVariable(String name) {
/*  903 */       this.name = name;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getName() {
/*  910 */       return this.name;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  918 */       return this.name;
/*      */     }
/*      */ 
/*      */     
/*      */     void encode(StringBuffer sb) {
/*  923 */       sb.append('T').append(this.name).append(';');
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ClassSignature toClassSignature(String sig) throws BadBytecode {
/*      */     try {
/*  940 */       return parseSig(sig);
/*      */     }
/*  942 */     catch (IndexOutOfBoundsException e) {
/*  943 */       throw error(sig);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static MethodSignature toMethodSignature(String sig) throws BadBytecode {
/*      */     try {
/*  960 */       return parseMethodSig(sig);
/*      */     }
/*  962 */     catch (IndexOutOfBoundsException e) {
/*  963 */       throw error(sig);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ObjectType toFieldSignature(String sig) throws BadBytecode {
/*      */     try {
/*  979 */       return parseObjectType(sig, new Cursor(), false);
/*      */     }
/*  981 */     catch (IndexOutOfBoundsException e) {
/*  982 */       throw error(sig);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Type toTypeSignature(String sig) throws BadBytecode {
/*      */     try {
/*  996 */       return parseType(sig, new Cursor());
/*      */     }
/*  998 */     catch (IndexOutOfBoundsException e) {
/*  999 */       throw error(sig);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static ClassSignature parseSig(String sig) throws BadBytecode, IndexOutOfBoundsException {
/* 1006 */     Cursor cur = new Cursor();
/* 1007 */     TypeParameter[] tp = parseTypeParams(sig, cur);
/* 1008 */     ClassType superClass = parseClassType(sig, cur);
/* 1009 */     int sigLen = sig.length();
/* 1010 */     List<ClassType> ifArray = new ArrayList<>();
/* 1011 */     while (cur.position < sigLen && sig.charAt(cur.position) == 'L') {
/* 1012 */       ifArray.add(parseClassType(sig, cur));
/*      */     }
/*      */     
/* 1015 */     ClassType[] ifs = ifArray.<ClassType>toArray(new ClassType[ifArray.size()]);
/* 1016 */     return new ClassSignature(tp, superClass, ifs);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static MethodSignature parseMethodSig(String sig) throws BadBytecode {
/* 1022 */     Cursor cur = new Cursor();
/* 1023 */     TypeParameter[] tp = parseTypeParams(sig, cur);
/* 1024 */     if (sig.charAt(cur.position++) != '(') {
/* 1025 */       throw error(sig);
/*      */     }
/* 1027 */     List<Type> params = new ArrayList<>();
/* 1028 */     while (sig.charAt(cur.position) != ')') {
/* 1029 */       Type t = parseType(sig, cur);
/* 1030 */       params.add(t);
/*      */     } 
/*      */     
/* 1033 */     cur.position++;
/* 1034 */     Type ret = parseType(sig, cur);
/* 1035 */     int sigLen = sig.length();
/* 1036 */     List<ObjectType> exceptions = new ArrayList<>();
/* 1037 */     while (cur.position < sigLen && sig.charAt(cur.position) == '^') {
/* 1038 */       cur.position++;
/* 1039 */       ObjectType t = parseObjectType(sig, cur, false);
/* 1040 */       if (t instanceof ArrayType) {
/* 1041 */         throw error(sig);
/*      */       }
/* 1043 */       exceptions.add(t);
/*      */     } 
/*      */     
/* 1046 */     Type[] p = params.<Type>toArray(new Type[params.size()]);
/* 1047 */     ObjectType[] ex = exceptions.<ObjectType>toArray(new ObjectType[exceptions.size()]);
/* 1048 */     return new MethodSignature(tp, p, ret, ex);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static TypeParameter[] parseTypeParams(String sig, Cursor cur) throws BadBytecode {
/* 1054 */     List<TypeParameter> typeParam = new ArrayList<>();
/* 1055 */     if (sig.charAt(cur.position) == '<') {
/* 1056 */       cur.position++;
/* 1057 */       while (sig.charAt(cur.position) != '>') {
/* 1058 */         int nameBegin = cur.position;
/* 1059 */         int nameEnd = cur.indexOf(sig, 58);
/* 1060 */         ObjectType classBound = parseObjectType(sig, cur, true);
/* 1061 */         List<ObjectType> ifBound = new ArrayList<>();
/* 1062 */         while (sig.charAt(cur.position) == ':') {
/* 1063 */           cur.position++;
/* 1064 */           ObjectType t = parseObjectType(sig, cur, false);
/* 1065 */           ifBound.add(t);
/*      */         } 
/*      */ 
/*      */         
/* 1069 */         TypeParameter p = new TypeParameter(sig, nameBegin, nameEnd, classBound, ifBound.<ObjectType>toArray(new ObjectType[ifBound.size()]));
/* 1070 */         typeParam.add(p);
/*      */       } 
/*      */       
/* 1073 */       cur.position++;
/*      */     } 
/*      */     
/* 1076 */     return typeParam.<TypeParameter>toArray(new TypeParameter[typeParam.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static ObjectType parseObjectType(String sig, Cursor c, boolean dontThrow) throws BadBytecode {
/* 1083 */     int i, begin = c.position;
/* 1084 */     switch (sig.charAt(begin)) {
/*      */       case 'L':
/* 1086 */         return parseClassType2(sig, c, (ClassType)null);
/*      */       case 'T':
/* 1088 */         i = c.indexOf(sig, 59);
/* 1089 */         return new TypeVariable(sig, begin + 1, i);
/*      */       case '[':
/* 1091 */         return parseArray(sig, c);
/*      */     } 
/* 1093 */     if (dontThrow)
/* 1094 */       return null; 
/* 1095 */     throw error(sig);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static ClassType parseClassType(String sig, Cursor c) throws BadBytecode {
/* 1102 */     if (sig.charAt(c.position) == 'L')
/* 1103 */       return parseClassType2(sig, c, (ClassType)null); 
/* 1104 */     throw error(sig);
/*      */   }
/*      */   
/*      */   private static ClassType parseClassType2(String sig, Cursor c, ClassType parent) throws BadBytecode {
/*      */     char t;
/*      */     TypeArgument[] targs;
/* 1110 */     int start = ++c.position;
/*      */     
/*      */     do {
/* 1113 */       t = sig.charAt(c.position++);
/* 1114 */     } while (t != '$' && t != '<' && t != ';');
/* 1115 */     int end = c.position - 1;
/*      */     
/* 1117 */     if (t == '<') {
/* 1118 */       targs = parseTypeArgs(sig, c);
/* 1119 */       t = sig.charAt(c.position++);
/*      */     } else {
/*      */       
/* 1122 */       targs = null;
/*      */     } 
/* 1124 */     ClassType thisClass = ClassType.make(sig, start, end, targs, parent);
/* 1125 */     if (t == '$' || t == '.') {
/* 1126 */       c.position--;
/* 1127 */       return parseClassType2(sig, c, thisClass);
/*      */     } 
/* 1129 */     return thisClass;
/*      */   }
/*      */   
/*      */   private static TypeArgument[] parseTypeArgs(String sig, Cursor c) throws BadBytecode {
/* 1133 */     List<TypeArgument> args = new ArrayList<>();
/*      */     char t;
/* 1135 */     while ((t = sig.charAt(c.position++)) != '>') {
/*      */       TypeArgument ta;
/* 1137 */       if (t == '*') {
/* 1138 */         ta = new TypeArgument(null, '*');
/*      */       } else {
/* 1140 */         if (t != '+' && t != '-') {
/* 1141 */           t = ' ';
/* 1142 */           c.position--;
/*      */         } 
/*      */         
/* 1145 */         ta = new TypeArgument(parseObjectType(sig, c, false), t);
/*      */       } 
/*      */       
/* 1148 */       args.add(ta);
/*      */     } 
/*      */     
/* 1151 */     return args.<TypeArgument>toArray(new TypeArgument[args.size()]);
/*      */   }
/*      */   
/*      */   private static ObjectType parseArray(String sig, Cursor c) throws BadBytecode {
/* 1155 */     int dim = 1;
/* 1156 */     while (sig.charAt(++c.position) == '[') {
/* 1157 */       dim++;
/*      */     }
/* 1159 */     return new ArrayType(dim, parseType(sig, c));
/*      */   }
/*      */   
/*      */   private static Type parseType(String sig, Cursor c) throws BadBytecode {
/* 1163 */     Type t = parseObjectType(sig, c, true);
/* 1164 */     if (t == null) {
/* 1165 */       t = new BaseType(sig.charAt(c.position++));
/*      */     }
/* 1167 */     return t;
/*      */   }
/*      */   
/*      */   private static BadBytecode error(String sig) {
/* 1171 */     return new BadBytecode("bad signature: " + sig);
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\SignatureAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */