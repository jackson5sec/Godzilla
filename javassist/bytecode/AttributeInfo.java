/*     */ package javassist.bytecode;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public class AttributeInfo
/*     */ {
/*     */   protected ConstPool constPool;
/*     */   int name;
/*     */   byte[] info;
/*     */   
/*     */   protected AttributeInfo(ConstPool cp, int attrname, byte[] attrinfo) {
/*  43 */     this.constPool = cp;
/*  44 */     this.name = attrname;
/*  45 */     this.info = attrinfo;
/*     */   }
/*     */   
/*     */   protected AttributeInfo(ConstPool cp, String attrname) {
/*  49 */     this(cp, attrname, (byte[])null);
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
/*     */   public AttributeInfo(ConstPool cp, String attrname, byte[] attrinfo) {
/*  61 */     this(cp, cp.addUtf8Info(attrname), attrinfo);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected AttributeInfo(ConstPool cp, int n, DataInputStream in) throws IOException {
/*  67 */     this.constPool = cp;
/*  68 */     this.name = n;
/*  69 */     int len = in.readInt();
/*  70 */     this.info = new byte[len];
/*  71 */     if (len > 0) {
/*  72 */       in.readFully(this.info);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static AttributeInfo read(ConstPool cp, DataInputStream in) throws IOException {
/*  78 */     int name = in.readUnsignedShort();
/*  79 */     String nameStr = cp.getUtf8Info(name);
/*  80 */     char first = nameStr.charAt(0);
/*  81 */     if (first < 'E') {
/*  82 */       if (nameStr.equals("AnnotationDefault"))
/*  83 */         return new AnnotationDefaultAttribute(cp, name, in); 
/*  84 */       if (nameStr.equals("BootstrapMethods"))
/*  85 */         return new BootstrapMethodsAttribute(cp, name, in); 
/*  86 */       if (nameStr.equals("Code"))
/*  87 */         return new CodeAttribute(cp, name, in); 
/*  88 */       if (nameStr.equals("ConstantValue"))
/*  89 */         return new ConstantAttribute(cp, name, in); 
/*  90 */       if (nameStr.equals("Deprecated"))
/*  91 */         return new DeprecatedAttribute(cp, name, in); 
/*     */     } 
/*  93 */     if (first < 'M') {
/*  94 */       if (nameStr.equals("EnclosingMethod"))
/*  95 */         return new EnclosingMethodAttribute(cp, name, in); 
/*  96 */       if (nameStr.equals("Exceptions"))
/*  97 */         return new ExceptionsAttribute(cp, name, in); 
/*  98 */       if (nameStr.equals("InnerClasses"))
/*  99 */         return new InnerClassesAttribute(cp, name, in); 
/* 100 */       if (nameStr.equals("LineNumberTable"))
/* 101 */         return new LineNumberAttribute(cp, name, in); 
/* 102 */       if (nameStr.equals("LocalVariableTable"))
/* 103 */         return new LocalVariableAttribute(cp, name, in); 
/* 104 */       if (nameStr.equals("LocalVariableTypeTable"))
/* 105 */         return new LocalVariableTypeAttribute(cp, name, in); 
/*     */     } 
/* 107 */     if (first < 'S') {
/*     */       
/* 109 */       if (nameStr.equals("MethodParameters"))
/* 110 */         return new MethodParametersAttribute(cp, name, in); 
/* 111 */       if (nameStr.equals("NestHost"))
/* 112 */         return new NestHostAttribute(cp, name, in); 
/* 113 */       if (nameStr.equals("NestMembers"))
/* 114 */         return new NestMembersAttribute(cp, name, in); 
/* 115 */       if (nameStr.equals("RuntimeVisibleAnnotations") || nameStr
/* 116 */         .equals("RuntimeInvisibleAnnotations"))
/*     */       {
/* 118 */         return new AnnotationsAttribute(cp, name, in); } 
/* 119 */       if (nameStr.equals("RuntimeVisibleParameterAnnotations") || nameStr
/* 120 */         .equals("RuntimeInvisibleParameterAnnotations"))
/* 121 */         return new ParameterAnnotationsAttribute(cp, name, in); 
/* 122 */       if (nameStr.equals("RuntimeVisibleTypeAnnotations") || nameStr
/* 123 */         .equals("RuntimeInvisibleTypeAnnotations"))
/* 124 */         return new TypeAnnotationsAttribute(cp, name, in); 
/*     */     } 
/* 126 */     if (first >= 'S') {
/* 127 */       if (nameStr.equals("Signature"))
/* 128 */         return new SignatureAttribute(cp, name, in); 
/* 129 */       if (nameStr.equals("SourceFile"))
/* 130 */         return new SourceFileAttribute(cp, name, in); 
/* 131 */       if (nameStr.equals("Synthetic"))
/* 132 */         return new SyntheticAttribute(cp, name, in); 
/* 133 */       if (nameStr.equals("StackMap"))
/* 134 */         return new StackMap(cp, name, in); 
/* 135 */       if (nameStr.equals("StackMapTable"))
/* 136 */         return new StackMapTable(cp, name, in); 
/*     */     } 
/* 138 */     return new AttributeInfo(cp, name, in);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 145 */     return this.constPool.getUtf8Info(this.name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ConstPool getConstPool() {
/* 151 */     return this.constPool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/* 159 */     return this.info.length + 6;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] get() {
/* 169 */     return this.info;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void set(byte[] newinfo) {
/* 178 */     this.info = newinfo;
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
/*     */   public AttributeInfo copy(ConstPool newCp, Map<String, String> classnames) {
/* 190 */     return new AttributeInfo(newCp, getName(), Arrays.copyOf(this.info, this.info.length));
/*     */   }
/*     */ 
/*     */   
/*     */   void write(DataOutputStream out) throws IOException {
/* 195 */     out.writeShort(this.name);
/* 196 */     out.writeInt(this.info.length);
/* 197 */     if (this.info.length > 0)
/* 198 */       out.write(this.info); 
/*     */   }
/*     */   
/*     */   static int getLength(List<AttributeInfo> attributes) {
/* 202 */     int size = 0;
/*     */     
/* 204 */     for (AttributeInfo attr : attributes) {
/* 205 */       size += attr.length();
/*     */     }
/* 207 */     return size;
/*     */   }
/*     */   
/*     */   static AttributeInfo lookup(List<AttributeInfo> attributes, String name) {
/* 211 */     if (attributes == null) {
/* 212 */       return null;
/*     */     }
/* 214 */     for (AttributeInfo ai : attributes) {
/* 215 */       if (ai.getName().equals(name))
/* 216 */         return ai; 
/*     */     } 
/* 218 */     return null;
/*     */   }
/*     */   
/*     */   static synchronized AttributeInfo remove(List<AttributeInfo> attributes, String name) {
/* 222 */     if (attributes == null) {
/* 223 */       return null;
/*     */     }
/* 225 */     for (AttributeInfo ai : attributes) {
/* 226 */       if (ai.getName().equals(name) && 
/* 227 */         attributes.remove(ai))
/* 228 */         return ai; 
/*     */     } 
/* 230 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static void writeAll(List<AttributeInfo> attributes, DataOutputStream out) throws IOException {
/* 236 */     if (attributes == null) {
/*     */       return;
/*     */     }
/* 239 */     for (AttributeInfo attr : attributes)
/* 240 */       attr.write(out); 
/*     */   }
/*     */   
/*     */   static List<AttributeInfo> copyAll(List<AttributeInfo> attributes, ConstPool cp) {
/* 244 */     if (attributes == null) {
/* 245 */       return null;
/*     */     }
/* 247 */     List<AttributeInfo> newList = new ArrayList<>();
/* 248 */     for (AttributeInfo attr : attributes) {
/* 249 */       newList.add(attr.copy(cp, null));
/*     */     }
/* 251 */     return newList;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void renameClass(String oldname, String newname) {}
/*     */ 
/*     */ 
/*     */   
/*     */   void renameClass(Map<String, String> classnames) {}
/*     */ 
/*     */   
/*     */   static void renameClass(List<AttributeInfo> attributes, String oldname, String newname) {
/* 264 */     if (attributes == null) {
/*     */       return;
/*     */     }
/* 267 */     for (AttributeInfo ai : attributes)
/* 268 */       ai.renameClass(oldname, newname); 
/*     */   }
/*     */   
/*     */   static void renameClass(List<AttributeInfo> attributes, Map<String, String> classnames) {
/* 272 */     if (attributes == null) {
/*     */       return;
/*     */     }
/* 275 */     for (AttributeInfo ai : attributes)
/* 276 */       ai.renameClass(classnames); 
/*     */   }
/*     */   
/*     */   void getRefClasses(Map<String, String> classnames) {}
/*     */   
/*     */   static void getRefClasses(List<AttributeInfo> attributes, Map<String, String> classnames) {
/* 282 */     if (attributes == null) {
/*     */       return;
/*     */     }
/* 285 */     for (AttributeInfo ai : attributes)
/* 286 */       ai.getRefClasses(classnames); 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\AttributeInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */