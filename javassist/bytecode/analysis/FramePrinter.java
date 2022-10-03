/*     */ package javassist.bytecode.analysis;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import javassist.CtClass;
/*     */ import javassist.CtMethod;
/*     */ import javassist.Modifier;
/*     */ import javassist.NotFoundException;
/*     */ import javassist.bytecode.BadBytecode;
/*     */ import javassist.bytecode.CodeAttribute;
/*     */ import javassist.bytecode.CodeIterator;
/*     */ import javassist.bytecode.ConstPool;
/*     */ import javassist.bytecode.Descriptor;
/*     */ import javassist.bytecode.InstructionPrinter;
/*     */ import javassist.bytecode.MethodInfo;
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
/*     */ public final class FramePrinter
/*     */ {
/*     */   private final PrintStream stream;
/*     */   
/*     */   public FramePrinter(PrintStream stream) {
/*  45 */     this.stream = stream;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void print(CtClass clazz, PrintStream stream) {
/*  52 */     (new FramePrinter(stream)).print(clazz);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void print(CtClass clazz) {
/*  59 */     CtMethod[] methods = clazz.getDeclaredMethods();
/*  60 */     for (int i = 0; i < methods.length; i++) {
/*  61 */       print(methods[i]);
/*     */     }
/*     */   }
/*     */   
/*     */   private String getMethodString(CtMethod method) {
/*     */     try {
/*  67 */       return Modifier.toString(method.getModifiers()) + " " + method
/*  68 */         .getReturnType().getName() + " " + method.getName() + 
/*  69 */         Descriptor.toString(method.getSignature()) + ";";
/*  70 */     } catch (NotFoundException e) {
/*  71 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void print(CtMethod method) {
/*     */     Frame[] frames;
/*  79 */     this.stream.println("\n" + getMethodString(method));
/*  80 */     MethodInfo info = method.getMethodInfo2();
/*  81 */     ConstPool pool = info.getConstPool();
/*  82 */     CodeAttribute code = info.getCodeAttribute();
/*  83 */     if (code == null) {
/*     */       return;
/*     */     }
/*     */     
/*     */     try {
/*  88 */       frames = (new Analyzer()).analyze(method.getDeclaringClass(), info);
/*  89 */     } catch (BadBytecode e) {
/*  90 */       throw new RuntimeException(e);
/*     */     } 
/*     */     
/*  93 */     int spacing = String.valueOf(code.getCodeLength()).length();
/*     */     
/*  95 */     CodeIterator iterator = code.iterator();
/*  96 */     while (iterator.hasNext()) {
/*     */       int pos;
/*     */       try {
/*  99 */         pos = iterator.next();
/* 100 */       } catch (BadBytecode e) {
/* 101 */         throw new RuntimeException(e);
/*     */       } 
/*     */       
/* 104 */       this.stream.println(pos + ": " + InstructionPrinter.instructionString(iterator, pos, pool));
/*     */       
/* 106 */       addSpacing(spacing + 3);
/* 107 */       Frame frame = frames[pos];
/* 108 */       if (frame == null) {
/* 109 */         this.stream.println("--DEAD CODE--");
/*     */         continue;
/*     */       } 
/* 112 */       printStack(frame);
/*     */       
/* 114 */       addSpacing(spacing + 3);
/* 115 */       printLocals(frame);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void printStack(Frame frame) {
/* 121 */     this.stream.print("stack [");
/* 122 */     int top = frame.getTopIndex();
/* 123 */     for (int i = 0; i <= top; i++) {
/* 124 */       if (i > 0)
/* 125 */         this.stream.print(", "); 
/* 126 */       Type type = frame.getStack(i);
/* 127 */       this.stream.print(type);
/*     */     } 
/* 129 */     this.stream.println("]");
/*     */   }
/*     */   
/*     */   private void printLocals(Frame frame) {
/* 133 */     this.stream.print("locals [");
/* 134 */     int length = frame.localsLength();
/* 135 */     for (int i = 0; i < length; i++) {
/* 136 */       if (i > 0)
/* 137 */         this.stream.print(", "); 
/* 138 */       Type type = frame.getLocal(i);
/* 139 */       this.stream.print((type == null) ? "empty" : type.toString());
/*     */     } 
/* 141 */     this.stream.println("]");
/*     */   }
/*     */   
/*     */   private void addSpacing(int count) {
/* 145 */     while (count-- > 0)
/* 146 */       this.stream.print(' '); 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\analysis\FramePrinter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */