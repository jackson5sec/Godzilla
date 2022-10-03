/*     */ package javassist.bytecode;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
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
/*     */ public class EnclosingMethodAttribute
/*     */   extends AttributeInfo
/*     */ {
/*     */   public static final String tag = "EnclosingMethod";
/*     */   
/*     */   EnclosingMethodAttribute(ConstPool cp, int n, DataInputStream in) throws IOException {
/*  35 */     super(cp, n, in);
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
/*     */   public EnclosingMethodAttribute(ConstPool cp, String className, String methodName, String methodDesc) {
/*  48 */     super(cp, "EnclosingMethod");
/*  49 */     int ci = cp.addClassInfo(className);
/*  50 */     int ni = cp.addNameAndTypeInfo(methodName, methodDesc);
/*  51 */     byte[] bvalue = new byte[4];
/*  52 */     bvalue[0] = (byte)(ci >>> 8);
/*  53 */     bvalue[1] = (byte)ci;
/*  54 */     bvalue[2] = (byte)(ni >>> 8);
/*  55 */     bvalue[3] = (byte)ni;
/*  56 */     set(bvalue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EnclosingMethodAttribute(ConstPool cp, String className) {
/*  67 */     super(cp, "EnclosingMethod");
/*  68 */     int ci = cp.addClassInfo(className);
/*  69 */     int ni = 0;
/*  70 */     byte[] bvalue = new byte[4];
/*  71 */     bvalue[0] = (byte)(ci >>> 8);
/*  72 */     bvalue[1] = (byte)ci;
/*  73 */     bvalue[2] = (byte)(ni >>> 8);
/*  74 */     bvalue[3] = (byte)ni;
/*  75 */     set(bvalue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int classIndex() {
/*  82 */     return ByteArray.readU16bit(get(), 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int methodIndex() {
/*  89 */     return ByteArray.readU16bit(get(), 2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String className() {
/*  96 */     return getConstPool().getClassInfo(classIndex());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String methodName() {
/* 105 */     ConstPool cp = getConstPool();
/* 106 */     int mi = methodIndex();
/* 107 */     if (mi == 0)
/* 108 */       return "<clinit>"; 
/* 109 */     int ni = cp.getNameAndTypeName(mi);
/* 110 */     return cp.getUtf8Info(ni);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String methodDescriptor() {
/* 117 */     ConstPool cp = getConstPool();
/* 118 */     int mi = methodIndex();
/* 119 */     int ti = cp.getNameAndTypeDescriptor(mi);
/* 120 */     return cp.getUtf8Info(ti);
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
/*     */   public AttributeInfo copy(ConstPool newCp, Map<String, String> classnames) {
/* 133 */     if (methodIndex() == 0)
/* 134 */       return new EnclosingMethodAttribute(newCp, className()); 
/* 135 */     return new EnclosingMethodAttribute(newCp, className(), 
/* 136 */         methodName(), methodDescriptor());
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\EnclosingMethodAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */