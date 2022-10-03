/*     */ package javassist.bytecode;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class FieldInfo
/*     */ {
/*     */   ConstPool constPool;
/*     */   int accessFlags;
/*     */   int name;
/*     */   String cachedName;
/*     */   String cachedType;
/*     */   int descriptor;
/*     */   List<AttributeInfo> attribute;
/*     */   
/*     */   private FieldInfo(ConstPool cp) {
/*  49 */     this.constPool = cp;
/*  50 */     this.accessFlags = 0;
/*  51 */     this.attribute = null;
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
/*     */   public FieldInfo(ConstPool cp, String fieldName, String desc) {
/*  64 */     this(cp);
/*  65 */     this.name = cp.addUtf8Info(fieldName);
/*  66 */     this.cachedName = fieldName;
/*  67 */     this.descriptor = cp.addUtf8Info(desc);
/*     */   }
/*     */   
/*     */   FieldInfo(ConstPool cp, DataInputStream in) throws IOException {
/*  71 */     this(cp);
/*  72 */     read(in);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  80 */     return getName() + " " + getDescriptor();
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
/*     */   void compact(ConstPool cp) {
/*  92 */     this.name = cp.addUtf8Info(getName());
/*  93 */     this.descriptor = cp.addUtf8Info(getDescriptor());
/*  94 */     this.attribute = AttributeInfo.copyAll(this.attribute, cp);
/*  95 */     this.constPool = cp;
/*     */   }
/*     */   
/*     */   void prune(ConstPool cp) {
/*  99 */     List<AttributeInfo> newAttributes = new ArrayList<>();
/*     */     
/* 101 */     AttributeInfo invisibleAnnotations = getAttribute("RuntimeInvisibleAnnotations");
/* 102 */     if (invisibleAnnotations != null) {
/* 103 */       invisibleAnnotations = invisibleAnnotations.copy(cp, null);
/* 104 */       newAttributes.add(invisibleAnnotations);
/*     */     } 
/*     */ 
/*     */     
/* 108 */     AttributeInfo visibleAnnotations = getAttribute("RuntimeVisibleAnnotations");
/* 109 */     if (visibleAnnotations != null) {
/* 110 */       visibleAnnotations = visibleAnnotations.copy(cp, null);
/* 111 */       newAttributes.add(visibleAnnotations);
/*     */     } 
/*     */ 
/*     */     
/* 115 */     AttributeInfo signature = getAttribute("Signature");
/* 116 */     if (signature != null) {
/* 117 */       signature = signature.copy(cp, null);
/* 118 */       newAttributes.add(signature);
/*     */     } 
/*     */     
/* 121 */     int index = getConstantValue();
/* 122 */     if (index != 0) {
/* 123 */       index = this.constPool.copy(index, cp, null);
/* 124 */       newAttributes.add(new ConstantAttribute(cp, index));
/*     */     } 
/*     */     
/* 127 */     this.attribute = newAttributes;
/* 128 */     this.name = cp.addUtf8Info(getName());
/* 129 */     this.descriptor = cp.addUtf8Info(getDescriptor());
/* 130 */     this.constPool = cp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConstPool getConstPool() {
/* 138 */     return this.constPool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 145 */     if (this.cachedName == null) {
/* 146 */       this.cachedName = this.constPool.getUtf8Info(this.name);
/*     */     }
/* 148 */     return this.cachedName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String newName) {
/* 155 */     this.name = this.constPool.addUtf8Info(newName);
/* 156 */     this.cachedName = newName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAccessFlags() {
/* 165 */     return this.accessFlags;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAccessFlags(int acc) {
/* 174 */     this.accessFlags = acc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDescriptor() {
/* 183 */     return this.constPool.getUtf8Info(this.descriptor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDescriptor(String desc) {
/* 192 */     if (!desc.equals(getDescriptor())) {
/* 193 */       this.descriptor = this.constPool.addUtf8Info(desc);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getConstantValue() {
/* 203 */     if ((this.accessFlags & 0x8) == 0) {
/* 204 */       return 0;
/*     */     }
/*     */     
/* 207 */     ConstantAttribute attr = (ConstantAttribute)getAttribute("ConstantValue");
/* 208 */     if (attr == null)
/* 209 */       return 0; 
/* 210 */     return attr.getConstantValue();
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
/*     */   public List<AttributeInfo> getAttributes() {
/* 224 */     if (this.attribute == null) {
/* 225 */       this.attribute = new ArrayList<>();
/*     */     }
/* 227 */     return this.attribute;
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
/*     */   public AttributeInfo getAttribute(String name) {
/* 243 */     return AttributeInfo.lookup(this.attribute, name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AttributeInfo removeAttribute(String name) {
/* 254 */     return AttributeInfo.remove(this.attribute, name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAttribute(AttributeInfo info) {
/* 264 */     if (this.attribute == null) {
/* 265 */       this.attribute = new ArrayList<>();
/*     */     }
/* 267 */     AttributeInfo.remove(this.attribute, info.getName());
/* 268 */     this.attribute.add(info);
/*     */   }
/*     */   
/*     */   private void read(DataInputStream in) throws IOException {
/* 272 */     this.accessFlags = in.readUnsignedShort();
/* 273 */     this.name = in.readUnsignedShort();
/* 274 */     this.descriptor = in.readUnsignedShort();
/* 275 */     int n = in.readUnsignedShort();
/* 276 */     this.attribute = new ArrayList<>();
/* 277 */     for (int i = 0; i < n; i++)
/* 278 */       this.attribute.add(AttributeInfo.read(this.constPool, in)); 
/*     */   }
/*     */   
/*     */   void write(DataOutputStream out) throws IOException {
/* 282 */     out.writeShort(this.accessFlags);
/* 283 */     out.writeShort(this.name);
/* 284 */     out.writeShort(this.descriptor);
/* 285 */     if (this.attribute == null) {
/* 286 */       out.writeShort(0);
/*     */     } else {
/* 288 */       out.writeShort(this.attribute.size());
/* 289 */       AttributeInfo.writeAll(this.attribute, out);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\FieldInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */