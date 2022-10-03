/*     */ package javassist.tools.reflect;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import javassist.ClassPool;
/*     */ import javassist.CtClass;
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
/*     */ public class Compiler
/*     */ {
/*     */   public static void main(String[] args) throws Exception {
/*  75 */     if (args.length == 0) {
/*  76 */       help(System.err);
/*     */       
/*     */       return;
/*     */     } 
/*  80 */     CompiledClass[] entries = new CompiledClass[args.length];
/*  81 */     int n = parse(args, entries);
/*     */     
/*  83 */     if (n < 1) {
/*  84 */       System.err.println("bad parameter.");
/*     */       
/*     */       return;
/*     */     } 
/*  88 */     processClasses(entries, n);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void processClasses(CompiledClass[] entries, int n) throws Exception {
/*  94 */     Reflection implementor = new Reflection();
/*  95 */     ClassPool pool = ClassPool.getDefault();
/*  96 */     implementor.start(pool);
/*     */     int i;
/*  98 */     for (i = 0; i < n; i++) {
/*  99 */       CtClass c = pool.get((entries[i]).classname);
/* 100 */       if ((entries[i]).metaobject != null || (entries[i]).classobject != null) {
/*     */         String metaobj, classobj;
/*     */ 
/*     */         
/* 104 */         if ((entries[i]).metaobject == null) {
/* 105 */           metaobj = "javassist.tools.reflect.Metaobject";
/*     */         } else {
/* 107 */           metaobj = (entries[i]).metaobject;
/*     */         } 
/* 109 */         if ((entries[i]).classobject == null) {
/* 110 */           classobj = "javassist.tools.reflect.ClassMetaobject";
/*     */         } else {
/* 112 */           classobj = (entries[i]).classobject;
/*     */         } 
/* 114 */         if (!implementor.makeReflective(c, pool.get(metaobj), pool
/* 115 */             .get(classobj))) {
/* 116 */           System.err.println("Warning: " + c.getName() + " is reflective.  It was not changed.");
/*     */         }
/*     */         
/* 119 */         System.err.println(c.getName() + ": " + metaobj + ", " + classobj);
/*     */       }
/*     */       else {
/*     */         
/* 123 */         System.err.println(c.getName() + ": not reflective");
/*     */       } 
/*     */     } 
/* 126 */     for (i = 0; i < n; i++) {
/* 127 */       implementor.onLoad(pool, (entries[i]).classname);
/* 128 */       pool.get((entries[i]).classname).writeFile();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static int parse(String[] args, CompiledClass[] result) {
/* 133 */     int n = -1;
/* 134 */     for (int i = 0; i < args.length; i++) {
/* 135 */       String a = args[i];
/* 136 */       if (a.equals("-m"))
/* 137 */       { if (n < 0 || i + 1 > args.length) {
/* 138 */           return -1;
/*     */         }
/* 140 */         (result[n]).metaobject = args[++i]; }
/* 141 */       else if (a.equals("-c"))
/* 142 */       { if (n < 0 || i + 1 > args.length) {
/* 143 */           return -1;
/*     */         }
/* 145 */         (result[n]).classobject = args[++i]; }
/* 146 */       else { if (a.charAt(0) == '-') {
/* 147 */           return -1;
/*     */         }
/* 149 */         CompiledClass cc = new CompiledClass();
/* 150 */         cc.classname = a;
/* 151 */         cc.metaobject = null;
/* 152 */         cc.classobject = null;
/* 153 */         result[++n] = cc; }
/*     */     
/*     */     } 
/*     */     
/* 157 */     return n + 1;
/*     */   }
/*     */   
/*     */   private static void help(PrintStream out) {
/* 161 */     out.println("Usage: java javassist.tools.reflect.Compiler");
/* 162 */     out.println("            (<class> [-m <metaobject>] [-c <class metaobject>])+");
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\tools\reflect\Compiler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */