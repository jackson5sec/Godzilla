/*     */ package javassist.bytecode.annotation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javassist.ClassPool;
/*     */ import javassist.CtClass;
/*     */ import javassist.CtMethod;
/*     */ import javassist.NotFoundException;
/*     */ import javassist.bytecode.ConstPool;
/*     */ import javassist.bytecode.Descriptor;
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
/*     */ 
/*     */ 
/*     */ public class Annotation
/*     */ {
/*     */   ConstPool pool;
/*     */   int typeIndex;
/*     */   Map<String, Pair> members;
/*     */   
/*     */   static class Pair
/*     */   {
/*     */     int name;
/*     */     MemberValue value;
/*     */   }
/*     */   
/*     */   public Annotation(int type, ConstPool cp) {
/*  72 */     this.pool = cp;
/*  73 */     this.typeIndex = type;
/*  74 */     this.members = null;
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
/*     */   public Annotation(String typeName, ConstPool cp) {
/*  87 */     this(cp.addUtf8Info(Descriptor.of(typeName)), cp);
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
/*     */   public Annotation(ConstPool cp, CtClass clazz) throws NotFoundException {
/* 103 */     this(cp.addUtf8Info(Descriptor.of(clazz.getName())), cp);
/*     */     
/* 105 */     if (!clazz.isInterface()) {
/* 106 */       throw new RuntimeException("Only interfaces are allowed for Annotation creation.");
/*     */     }
/*     */     
/* 109 */     CtMethod[] methods = clazz.getDeclaredMethods();
/* 110 */     if (methods.length > 0) {
/* 111 */       this.members = new LinkedHashMap<>();
/*     */     }
/* 113 */     for (CtMethod m : methods) {
/* 114 */       addMemberValue(m.getName(), 
/* 115 */           createMemberValue(cp, m.getReturnType()));
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
/*     */   public static MemberValue createMemberValue(ConstPool cp, CtClass type) throws NotFoundException {
/* 129 */     if (type == CtClass.booleanType)
/* 130 */       return new BooleanMemberValue(cp); 
/* 131 */     if (type == CtClass.byteType)
/* 132 */       return new ByteMemberValue(cp); 
/* 133 */     if (type == CtClass.charType)
/* 134 */       return new CharMemberValue(cp); 
/* 135 */     if (type == CtClass.shortType)
/* 136 */       return new ShortMemberValue(cp); 
/* 137 */     if (type == CtClass.intType)
/* 138 */       return new IntegerMemberValue(cp); 
/* 139 */     if (type == CtClass.longType)
/* 140 */       return new LongMemberValue(cp); 
/* 141 */     if (type == CtClass.floatType)
/* 142 */       return new FloatMemberValue(cp); 
/* 143 */     if (type == CtClass.doubleType)
/* 144 */       return new DoubleMemberValue(cp); 
/* 145 */     if (type.getName().equals("java.lang.Class"))
/* 146 */       return new ClassMemberValue(cp); 
/* 147 */     if (type.getName().equals("java.lang.String"))
/* 148 */       return new StringMemberValue(cp); 
/* 149 */     if (type.isArray()) {
/* 150 */       CtClass arrayType = type.getComponentType();
/* 151 */       MemberValue member = createMemberValue(cp, arrayType);
/* 152 */       return new ArrayMemberValue(member, cp);
/*     */     } 
/* 154 */     if (type.isInterface()) {
/* 155 */       Annotation info = new Annotation(cp, type);
/* 156 */       return new AnnotationMemberValue(info, cp);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 162 */     EnumMemberValue emv = new EnumMemberValue(cp);
/* 163 */     emv.setType(type.getName());
/* 164 */     return emv;
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
/*     */   public void addMemberValue(int nameIndex, MemberValue value) {
/* 178 */     Pair p = new Pair();
/* 179 */     p.name = nameIndex;
/* 180 */     p.value = value;
/* 181 */     addMemberValue(p);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addMemberValue(String name, MemberValue value) {
/* 191 */     Pair p = new Pair();
/* 192 */     p.name = this.pool.addUtf8Info(name);
/* 193 */     p.value = value;
/* 194 */     if (this.members == null) {
/* 195 */       this.members = new LinkedHashMap<>();
/*     */     }
/* 197 */     this.members.put(name, p);
/*     */   }
/*     */   
/*     */   private void addMemberValue(Pair pair) {
/* 201 */     String name = this.pool.getUtf8Info(pair.name);
/* 202 */     if (this.members == null) {
/* 203 */       this.members = new LinkedHashMap<>();
/*     */     }
/* 205 */     this.members.put(name, pair);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 213 */     StringBuffer buf = new StringBuffer("@");
/* 214 */     buf.append(getTypeName());
/* 215 */     if (this.members != null) {
/* 216 */       buf.append("(");
/* 217 */       for (String name : this.members.keySet()) {
/* 218 */         buf.append(name).append("=")
/* 219 */           .append(getMemberValue(name))
/* 220 */           .append(", ");
/*     */       }
/* 222 */       buf.setLength(buf.length() - 2);
/* 223 */       buf.append(")");
/*     */     } 
/*     */     
/* 226 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTypeName() {
/* 235 */     return Descriptor.toClassName(this.pool.getUtf8Info(this.typeIndex));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<String> getMemberNames() {
/* 244 */     if (this.members == null)
/* 245 */       return null; 
/* 246 */     return this.members.keySet();
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
/*     */   public MemberValue getMemberValue(String name) {
/* 265 */     if (this.members == null || this.members.get(name) == null)
/* 266 */       return null; 
/* 267 */     return ((Pair)this.members.get(name)).value;
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
/*     */   public Object toAnnotationType(ClassLoader cl, ClassPool cp) throws ClassNotFoundException, NoSuchClassError {
/* 284 */     Class<?> clazz = MemberValue.loadClass(cl, getTypeName());
/*     */     try {
/* 286 */       return AnnotationImpl.make(cl, clazz, cp, this);
/*     */     }
/* 288 */     catch (IllegalArgumentException e) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 293 */       throw new ClassNotFoundException(clazz.getName(), e);
/*     */     }
/* 295 */     catch (IllegalAccessError e2) {
/*     */       
/* 297 */       throw new ClassNotFoundException(clazz.getName(), e2);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(AnnotationsWriter writer) throws IOException {
/* 308 */     String typeName = this.pool.getUtf8Info(this.typeIndex);
/* 309 */     if (this.members == null) {
/* 310 */       writer.annotation(typeName, 0);
/*     */       
/*     */       return;
/*     */     } 
/* 314 */     writer.annotation(typeName, this.members.size());
/* 315 */     for (Pair pair : this.members.values()) {
/* 316 */       writer.memberValuePair(pair.name);
/* 317 */       pair.value.write(writer);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 323 */     return getTypeName().hashCode() + (
/* 324 */       (this.members == null) ? 0 : this.members.hashCode());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 333 */     if (obj == this)
/* 334 */       return true; 
/* 335 */     if (obj == null || !(obj instanceof Annotation)) {
/* 336 */       return false;
/*     */     }
/* 338 */     Annotation other = (Annotation)obj;
/*     */     
/* 340 */     if (!getTypeName().equals(other.getTypeName())) {
/* 341 */       return false;
/*     */     }
/* 343 */     Map<String, Pair> otherMembers = other.members;
/* 344 */     if (this.members == otherMembers)
/* 345 */       return true; 
/* 346 */     if (this.members == null) {
/* 347 */       return (otherMembers == null);
/*     */     }
/* 349 */     if (otherMembers == null) {
/* 350 */       return false;
/*     */     }
/* 352 */     return this.members.equals(otherMembers);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\annotation\Annotation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */