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
/*     */ public class BootstrapMethodsAttribute
/*     */   extends AttributeInfo
/*     */ {
/*     */   public static final String tag = "BootstrapMethods";
/*     */   
/*     */   public static class BootstrapMethod
/*     */   {
/*     */     public int methodRef;
/*     */     public int[] arguments;
/*     */     
/*     */     public BootstrapMethod(int method, int[] args) {
/*  24 */       this.methodRef = method;
/*  25 */       this.arguments = args;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   BootstrapMethodsAttribute(ConstPool cp, int n, DataInputStream in) throws IOException {
/*  43 */     super(cp, n, in);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BootstrapMethodsAttribute(ConstPool cp, BootstrapMethod[] methods) {
/*  53 */     super(cp, "BootstrapMethods");
/*  54 */     int size = 2;
/*  55 */     for (int i = 0; i < methods.length; i++) {
/*  56 */       size += 4 + (methods[i]).arguments.length * 2;
/*     */     }
/*  58 */     byte[] data = new byte[size];
/*  59 */     ByteArray.write16bit(methods.length, data, 0);
/*  60 */     int pos = 2;
/*  61 */     for (int j = 0; j < methods.length; j++) {
/*  62 */       ByteArray.write16bit((methods[j]).methodRef, data, pos);
/*  63 */       ByteArray.write16bit((methods[j]).arguments.length, data, pos + 2);
/*  64 */       int[] args = (methods[j]).arguments;
/*  65 */       pos += 4;
/*  66 */       for (int k = 0; k < args.length; k++) {
/*  67 */         ByteArray.write16bit(args[k], data, pos);
/*  68 */         pos += 2;
/*     */       } 
/*     */     } 
/*     */     
/*  72 */     set(data);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BootstrapMethod[] getMethods() {
/*  83 */     byte[] data = get();
/*  84 */     int num = ByteArray.readU16bit(data, 0);
/*  85 */     BootstrapMethod[] methods = new BootstrapMethod[num];
/*  86 */     int pos = 2;
/*  87 */     for (int i = 0; i < num; i++) {
/*  88 */       int ref = ByteArray.readU16bit(data, pos);
/*  89 */       int len = ByteArray.readU16bit(data, pos + 2);
/*  90 */       int[] args = new int[len];
/*  91 */       pos += 4;
/*  92 */       for (int k = 0; k < len; k++) {
/*  93 */         args[k] = ByteArray.readU16bit(data, pos);
/*  94 */         pos += 2;
/*     */       } 
/*     */       
/*  97 */       methods[i] = new BootstrapMethod(ref, args);
/*     */     } 
/*     */     
/* 100 */     return methods;
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
/* 113 */     BootstrapMethod[] methods = getMethods();
/* 114 */     ConstPool thisCp = getConstPool();
/* 115 */     for (int i = 0; i < methods.length; i++) {
/* 116 */       BootstrapMethod m = methods[i];
/* 117 */       m.methodRef = thisCp.copy(m.methodRef, newCp, classnames);
/* 118 */       for (int k = 0; k < m.arguments.length; k++) {
/* 119 */         m.arguments[k] = thisCp.copy(m.arguments[k], newCp, classnames);
/*     */       }
/*     */     } 
/* 122 */     return new BootstrapMethodsAttribute(newCp, methods);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\BootstrapMethodsAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */