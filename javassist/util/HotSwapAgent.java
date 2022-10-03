/*     */ package javassist.util;
/*     */ 
/*     */ import com.sun.tools.attach.VirtualMachine;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.lang.instrument.ClassDefinition;
/*     */ import java.lang.instrument.Instrumentation;
/*     */ import java.lang.instrument.UnmodifiableClassException;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.util.jar.Attributes;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarOutputStream;
/*     */ import java.util.jar.Manifest;
/*     */ import javassist.CannotCompileException;
/*     */ import javassist.ClassPool;
/*     */ import javassist.CtClass;
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
/*     */ public class HotSwapAgent
/*     */ {
/*  81 */   private static Instrumentation instrumentation = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Instrumentation instrumentation() {
/*  88 */     return instrumentation;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void premain(String agentArgs, Instrumentation inst) throws Throwable {
/*  94 */     agentmain(agentArgs, inst);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void agentmain(String agentArgs, Instrumentation inst) throws Throwable {
/* 101 */     if (!inst.isRedefineClassesSupported()) {
/* 102 */       throw new RuntimeException("this JVM does not support redefinition of classes");
/*     */     }
/* 104 */     instrumentation = inst;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void redefine(Class<?> oldClass, CtClass newClass) throws NotFoundException, IOException, CannotCompileException {
/* 113 */     Class<?>[] old = new Class[] { oldClass };
/* 114 */     CtClass[] newClasses = { newClass };
/* 115 */     redefine(old, newClasses);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void redefine(Class<?>[] oldClasses, CtClass[] newClasses) throws NotFoundException, IOException, CannotCompileException {
/* 124 */     startAgent();
/* 125 */     ClassDefinition[] defs = new ClassDefinition[oldClasses.length];
/* 126 */     for (int i = 0; i < oldClasses.length; i++) {
/* 127 */       defs[i] = new ClassDefinition(oldClasses[i], newClasses[i].toBytecode());
/*     */     }
/*     */     try {
/* 130 */       instrumentation.redefineClasses(defs);
/*     */     }
/* 132 */     catch (ClassNotFoundException e) {
/* 133 */       throw new NotFoundException(e.getMessage(), e);
/*     */     }
/* 135 */     catch (UnmodifiableClassException e) {
/* 136 */       throw new CannotCompileException(e.getMessage(), e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void startAgent() throws NotFoundException {
/* 145 */     if (instrumentation != null) {
/*     */       return;
/*     */     }
/*     */     try {
/* 149 */       File agentJar = createJarFile();
/*     */       
/* 151 */       String nameOfRunningVM = ManagementFactory.getRuntimeMXBean().getName();
/* 152 */       String pid = nameOfRunningVM.substring(0, nameOfRunningVM.indexOf('@'));
/* 153 */       VirtualMachine vm = VirtualMachine.attach(pid);
/* 154 */       vm.loadAgent(agentJar.getAbsolutePath(), null);
/* 155 */       vm.detach();
/*     */     }
/* 157 */     catch (Exception e) {
/* 158 */       throw new NotFoundException("hotswap agent", e);
/*     */     } 
/*     */     
/* 161 */     for (int sec = 0; sec < 10; sec++) {
/* 162 */       if (instrumentation != null) {
/*     */         return;
/*     */       }
/*     */       try {
/* 166 */         Thread.sleep(1000L);
/*     */       }
/* 168 */       catch (InterruptedException e) {
/* 169 */         Thread.currentThread().interrupt();
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 174 */     throw new NotFoundException("hotswap agent (timeout)");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static File createAgentJarFile(String fileName) throws IOException, CannotCompileException, NotFoundException {
/* 183 */     return createJarFile(new File(fileName));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static File createJarFile() throws IOException, CannotCompileException, NotFoundException {
/* 189 */     File jar = File.createTempFile("agent", ".jar");
/* 190 */     jar.deleteOnExit();
/* 191 */     return createJarFile(jar);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static File createJarFile(File jar) throws IOException, CannotCompileException, NotFoundException {
/* 197 */     Manifest manifest = new Manifest();
/* 198 */     Attributes attrs = manifest.getMainAttributes();
/* 199 */     attrs.put(Attributes.Name.MANIFEST_VERSION, "1.0");
/* 200 */     attrs.put(new Attributes.Name("Premain-Class"), HotSwapAgent.class.getName());
/* 201 */     attrs.put(new Attributes.Name("Agent-Class"), HotSwapAgent.class.getName());
/* 202 */     attrs.put(new Attributes.Name("Can-Retransform-Classes"), "true");
/* 203 */     attrs.put(new Attributes.Name("Can-Redefine-Classes"), "true");
/*     */     
/* 205 */     JarOutputStream jos = null;
/*     */     try {
/* 207 */       jos = new JarOutputStream(new FileOutputStream(jar), manifest);
/* 208 */       String cname = HotSwapAgent.class.getName();
/* 209 */       JarEntry e = new JarEntry(cname.replace('.', '/') + ".class");
/* 210 */       jos.putNextEntry(e);
/* 211 */       ClassPool pool = ClassPool.getDefault();
/* 212 */       CtClass clazz = pool.get(cname);
/* 213 */       jos.write(clazz.toBytecode());
/* 214 */       jos.closeEntry();
/*     */     } finally {
/*     */       
/* 217 */       if (jos != null) {
/* 218 */         jos.close();
/*     */       }
/*     */     } 
/* 221 */     return jar;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassis\\util\HotSwapAgent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */