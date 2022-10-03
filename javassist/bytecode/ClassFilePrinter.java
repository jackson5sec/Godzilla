/*     */ package javassist.bytecode;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.util.List;
/*     */ import javassist.Modifier;
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
/*     */ public class ClassFilePrinter
/*     */ {
/*     */   public static void print(ClassFile cf) {
/*  34 */     print(cf, new PrintWriter(System.out, true));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void print(ClassFile cf, PrintWriter out) {
/*  45 */     int mod = AccessFlag.toModifier(cf.getAccessFlags() & 0xFFFFFFDF);
/*     */     
/*  47 */     out.println("major: " + cf.major + ", minor: " + cf.minor + " modifiers: " + 
/*  48 */         Integer.toHexString(cf.getAccessFlags()));
/*  49 */     out.println(Modifier.toString(mod) + " class " + cf
/*  50 */         .getName() + " extends " + cf.getSuperclass());
/*     */     
/*  52 */     String[] infs = cf.getInterfaces();
/*  53 */     if (infs != null && infs.length > 0) {
/*  54 */       out.print("    implements ");
/*  55 */       out.print(infs[0]);
/*  56 */       for (int i = 1; i < infs.length; i++) {
/*  57 */         out.print(", " + infs[i]);
/*     */       }
/*  59 */       out.println();
/*     */     } 
/*     */     
/*  62 */     out.println();
/*  63 */     List<FieldInfo> fields = cf.getFields();
/*  64 */     for (FieldInfo finfo : fields) {
/*  65 */       int acc = finfo.getAccessFlags();
/*  66 */       out.println(Modifier.toString(AccessFlag.toModifier(acc)) + " " + finfo
/*  67 */           .getName() + "\t" + finfo
/*  68 */           .getDescriptor());
/*  69 */       printAttributes(finfo.getAttributes(), out, 'f');
/*     */     } 
/*     */     
/*  72 */     out.println();
/*  73 */     List<MethodInfo> methods = cf.getMethods();
/*  74 */     for (MethodInfo minfo : methods) {
/*  75 */       int acc = minfo.getAccessFlags();
/*  76 */       out.println(Modifier.toString(AccessFlag.toModifier(acc)) + " " + minfo
/*  77 */           .getName() + "\t" + minfo
/*  78 */           .getDescriptor());
/*  79 */       printAttributes(minfo.getAttributes(), out, 'm');
/*  80 */       out.println();
/*     */     } 
/*     */     
/*  83 */     out.println();
/*  84 */     printAttributes(cf.getAttributes(), out, 'c');
/*     */   }
/*     */   
/*     */   static void printAttributes(List<AttributeInfo> list, PrintWriter out, char kind) {
/*  88 */     if (list == null) {
/*     */       return;
/*     */     }
/*  91 */     for (AttributeInfo ai : list) {
/*  92 */       if (ai instanceof CodeAttribute) {
/*  93 */         CodeAttribute ca = (CodeAttribute)ai;
/*  94 */         out.println("attribute: " + ai.getName() + ": " + ai
/*  95 */             .getClass().getName());
/*  96 */         out.println("max stack " + ca.getMaxStack() + ", max locals " + ca
/*  97 */             .getMaxLocals() + ", " + ca
/*  98 */             .getExceptionTable().size() + " catch blocks");
/*     */         
/* 100 */         out.println("<code attribute begin>");
/* 101 */         printAttributes(ca.getAttributes(), out, kind);
/* 102 */         out.println("<code attribute end>"); continue;
/*     */       } 
/* 104 */       if (ai instanceof AnnotationsAttribute) {
/* 105 */         out.println("annnotation: " + ai.toString()); continue;
/*     */       } 
/* 107 */       if (ai instanceof ParameterAnnotationsAttribute) {
/* 108 */         out.println("parameter annnotations: " + ai.toString()); continue;
/*     */       } 
/* 110 */       if (ai instanceof StackMapTable) {
/* 111 */         out.println("<stack map table begin>");
/* 112 */         StackMapTable.Printer.print((StackMapTable)ai, out);
/* 113 */         out.println("<stack map table end>"); continue;
/*     */       } 
/* 115 */       if (ai instanceof StackMap) {
/* 116 */         out.println("<stack map begin>");
/* 117 */         ((StackMap)ai).print(out);
/* 118 */         out.println("<stack map end>"); continue;
/*     */       } 
/* 120 */       if (ai instanceof SignatureAttribute) {
/* 121 */         SignatureAttribute sa = (SignatureAttribute)ai;
/* 122 */         String sig = sa.getSignature();
/* 123 */         out.println("signature: " + sig);
/*     */         try {
/*     */           String s;
/* 126 */           if (kind == 'c') {
/* 127 */             s = SignatureAttribute.toClassSignature(sig).toString();
/* 128 */           } else if (kind == 'm') {
/* 129 */             s = SignatureAttribute.toMethodSignature(sig).toString();
/*     */           } else {
/* 131 */             s = SignatureAttribute.toFieldSignature(sig).toString();
/*     */           } 
/* 133 */           out.println("           " + s);
/*     */         }
/* 135 */         catch (BadBytecode e) {
/* 136 */           out.println("           syntax error");
/*     */         } 
/*     */         continue;
/*     */       } 
/* 140 */       out.println("attribute: " + ai.getName() + " (" + (ai
/* 141 */           .get()).length + " byte): " + ai
/* 142 */           .getClass().getName());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\ClassFilePrinter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */