/*     */ package org.mozilla.javascript.tools.shell;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.nio.charset.Charset;
/*     */ import org.mozilla.javascript.Kit;
/*     */ import org.mozilla.javascript.Scriptable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ShellConsole
/*     */ {
/*  31 */   private static final Class[] NO_ARG = new Class[0];
/*  32 */   private static final Class[] BOOLEAN_ARG = new Class[] { boolean.class };
/*  33 */   private static final Class[] STRING_ARG = new Class[] { String.class };
/*  34 */   private static final Class[] CHARSEQ_ARG = new Class[] { CharSequence.class };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Object tryInvoke(Object obj, String method, Class[] paramTypes, Object... args) {
/*     */     
/*  79 */     try { Method m = obj.getClass().getDeclaredMethod(method, paramTypes);
/*  80 */       if (m != null) {
/*  81 */         return m.invoke(obj, args);
/*     */       } }
/*  83 */     catch (NoSuchMethodException e) {  }
/*  84 */     catch (IllegalArgumentException e) {  }
/*  85 */     catch (IllegalAccessException e) {  }
/*  86 */     catch (InvocationTargetException e) {}
/*     */     
/*  88 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private static class JLineShellConsoleV1
/*     */     extends ShellConsole
/*     */   {
/*     */     private final Object reader;
/*     */     private final InputStream in;
/*     */     
/*     */     JLineShellConsoleV1(Object reader, Charset cs) {
/*  99 */       this.reader = reader;
/* 100 */       this.in = new ShellConsole.ConsoleInputStream(this, cs);
/*     */     }
/*     */ 
/*     */     
/*     */     public InputStream getIn() {
/* 105 */       return this.in;
/*     */     }
/*     */ 
/*     */     
/*     */     public String readLine() throws IOException {
/* 110 */       return (String)ShellConsole.tryInvoke(this.reader, "readLine", ShellConsole.NO_ARG, new Object[0]);
/*     */     }
/*     */ 
/*     */     
/*     */     public String readLine(String prompt) throws IOException {
/* 115 */       return (String)ShellConsole.tryInvoke(this.reader, "readLine", ShellConsole.STRING_ARG, new Object[] { prompt });
/*     */     }
/*     */ 
/*     */     
/*     */     public void flush() throws IOException {
/* 120 */       ShellConsole.tryInvoke(this.reader, "flushConsole", ShellConsole.NO_ARG, new Object[0]);
/*     */     }
/*     */ 
/*     */     
/*     */     public void print(String s) throws IOException {
/* 125 */       ShellConsole.tryInvoke(this.reader, "printString", ShellConsole.STRING_ARG, new Object[] { s });
/*     */     }
/*     */ 
/*     */     
/*     */     public void println() throws IOException {
/* 130 */       ShellConsole.tryInvoke(this.reader, "printNewline", ShellConsole.NO_ARG, new Object[0]);
/*     */     }
/*     */ 
/*     */     
/*     */     public void println(String s) throws IOException {
/* 135 */       ShellConsole.tryInvoke(this.reader, "printString", ShellConsole.STRING_ARG, new Object[] { s });
/* 136 */       ShellConsole.tryInvoke(this.reader, "printNewline", ShellConsole.NO_ARG, new Object[0]);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class JLineShellConsoleV2
/*     */     extends ShellConsole
/*     */   {
/*     */     private final Object reader;
/*     */     private final InputStream in;
/*     */     
/*     */     JLineShellConsoleV2(Object reader, Charset cs) {
/* 148 */       this.reader = reader;
/* 149 */       this.in = new ShellConsole.ConsoleInputStream(this, cs);
/*     */     }
/*     */ 
/*     */     
/*     */     public InputStream getIn() {
/* 154 */       return this.in;
/*     */     }
/*     */ 
/*     */     
/*     */     public String readLine() throws IOException {
/* 159 */       return (String)ShellConsole.tryInvoke(this.reader, "readLine", ShellConsole.NO_ARG, new Object[0]);
/*     */     }
/*     */ 
/*     */     
/*     */     public String readLine(String prompt) throws IOException {
/* 164 */       return (String)ShellConsole.tryInvoke(this.reader, "readLine", ShellConsole.STRING_ARG, new Object[] { prompt });
/*     */     }
/*     */ 
/*     */     
/*     */     public void flush() throws IOException {
/* 169 */       ShellConsole.tryInvoke(this.reader, "flush", ShellConsole.NO_ARG, new Object[0]);
/*     */     }
/*     */ 
/*     */     
/*     */     public void print(String s) throws IOException {
/* 174 */       ShellConsole.tryInvoke(this.reader, "print", ShellConsole.CHARSEQ_ARG, new Object[] { s });
/*     */     }
/*     */ 
/*     */     
/*     */     public void println() throws IOException {
/* 179 */       ShellConsole.tryInvoke(this.reader, "println", ShellConsole.NO_ARG, new Object[0]);
/*     */     }
/*     */ 
/*     */     
/*     */     public void println(String s) throws IOException {
/* 184 */       ShellConsole.tryInvoke(this.reader, "println", ShellConsole.CHARSEQ_ARG, new Object[] { s });
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ConsoleInputStream
/*     */     extends InputStream
/*     */   {
/* 193 */     private static final byte[] EMPTY = new byte[0];
/*     */     private final ShellConsole console;
/*     */     private final Charset cs;
/* 196 */     private byte[] buffer = EMPTY;
/* 197 */     private int cursor = -1;
/*     */     private boolean atEOF = false;
/*     */     
/*     */     public ConsoleInputStream(ShellConsole console, Charset cs) {
/* 201 */       this.console = console;
/* 202 */       this.cs = cs;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public synchronized int read(byte[] b, int off, int len) throws IOException {
/* 208 */       if (b == null)
/* 209 */         throw new NullPointerException(); 
/* 210 */       if (off < 0 || len < 0 || len > b.length - off)
/* 211 */         throw new IndexOutOfBoundsException(); 
/* 212 */       if (len == 0) {
/* 213 */         return 0;
/*     */       }
/* 215 */       if (!ensureInput()) {
/* 216 */         return -1;
/*     */       }
/* 218 */       int n = Math.min(len, this.buffer.length - this.cursor);
/* 219 */       for (int i = 0; i < n; i++) {
/* 220 */         b[off + i] = this.buffer[this.cursor + i];
/*     */       }
/* 222 */       if (n < len) {
/* 223 */         b[off + n++] = 10;
/*     */       }
/* 225 */       this.cursor += n;
/* 226 */       return n;
/*     */     }
/*     */ 
/*     */     
/*     */     public synchronized int read() throws IOException {
/* 231 */       if (!ensureInput()) {
/* 232 */         return -1;
/*     */       }
/* 234 */       if (this.cursor == this.buffer.length) {
/* 235 */         this.cursor++;
/* 236 */         return 10;
/*     */       } 
/* 238 */       return this.buffer[this.cursor++];
/*     */     }
/*     */     
/*     */     private boolean ensureInput() throws IOException {
/* 242 */       if (this.atEOF) {
/* 243 */         return false;
/*     */       }
/* 245 */       if (this.cursor < 0 || this.cursor > this.buffer.length) {
/* 246 */         if (readNextLine() == -1) {
/* 247 */           this.atEOF = true;
/* 248 */           return false;
/*     */         } 
/* 250 */         this.cursor = 0;
/*     */       } 
/* 252 */       return true;
/*     */     }
/*     */     
/*     */     private int readNextLine() throws IOException {
/* 256 */       String line = this.console.readLine(null);
/* 257 */       if (line != null) {
/* 258 */         this.buffer = line.getBytes(this.cs);
/* 259 */         return this.buffer.length;
/*     */       } 
/* 261 */       this.buffer = EMPTY;
/* 262 */       return -1;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class SimpleShellConsole
/*     */     extends ShellConsole {
/*     */     private final InputStream in;
/*     */     private final PrintWriter out;
/*     */     private final BufferedReader reader;
/*     */     
/*     */     SimpleShellConsole(InputStream in, PrintStream ps, Charset cs) {
/* 273 */       this.in = in;
/* 274 */       this.out = new PrintWriter(ps);
/* 275 */       this.reader = new BufferedReader(new InputStreamReader(in, cs));
/*     */     }
/*     */ 
/*     */     
/*     */     public InputStream getIn() {
/* 280 */       return this.in;
/*     */     }
/*     */ 
/*     */     
/*     */     public String readLine() throws IOException {
/* 285 */       return this.reader.readLine();
/*     */     }
/*     */ 
/*     */     
/*     */     public String readLine(String prompt) throws IOException {
/* 290 */       if (prompt != null) {
/* 291 */         this.out.write(prompt);
/* 292 */         this.out.flush();
/*     */       } 
/* 294 */       return this.reader.readLine();
/*     */     }
/*     */ 
/*     */     
/*     */     public void flush() throws IOException {
/* 299 */       this.out.flush();
/*     */     }
/*     */ 
/*     */     
/*     */     public void print(String s) throws IOException {
/* 304 */       this.out.print(s);
/*     */     }
/*     */ 
/*     */     
/*     */     public void println() throws IOException {
/* 309 */       this.out.println();
/*     */     }
/*     */ 
/*     */     
/*     */     public void println(String s) throws IOException {
/* 314 */       this.out.println(s);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ShellConsole getConsole(InputStream in, PrintStream ps, Charset cs) {
/* 324 */     return new SimpleShellConsole(in, ps, cs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ShellConsole getConsole(Scriptable scope, Charset cs) {
/* 335 */     ClassLoader classLoader = ShellConsole.class.getClassLoader();
/* 336 */     if (classLoader == null)
/*     */     {
/*     */       
/* 339 */       classLoader = ClassLoader.getSystemClassLoader();
/*     */     }
/* 341 */     if (classLoader == null)
/*     */     {
/*     */       
/* 344 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 348 */     try { Class<?> readerClass = Kit.classOrNull(classLoader, "jline.console.ConsoleReader");
/*     */       
/* 350 */       if (readerClass != null) {
/* 351 */         return getJLineShellConsoleV2(classLoader, readerClass, scope, cs);
/*     */       }
/*     */       
/* 354 */       readerClass = Kit.classOrNull(classLoader, "jline.ConsoleReader");
/* 355 */       if (readerClass != null) {
/* 356 */         return getJLineShellConsoleV1(classLoader, readerClass, scope, cs);
/*     */       } }
/* 358 */     catch (NoSuchMethodException e) {  }
/* 359 */     catch (IllegalAccessException e) {  }
/* 360 */     catch (InstantiationException e) {  }
/* 361 */     catch (InvocationTargetException e) {}
/*     */     
/* 363 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static JLineShellConsoleV1 getJLineShellConsoleV1(ClassLoader classLoader, Class<?> readerClass, Scriptable scope, Charset cs) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
/* 371 */     Constructor<?> c = readerClass.getConstructor(new Class[0]);
/* 372 */     Object reader = c.newInstance(new Object[0]);
/*     */ 
/*     */     
/* 375 */     tryInvoke(reader, "setBellEnabled", BOOLEAN_ARG, new Object[] { Boolean.FALSE });
/*     */ 
/*     */     
/* 378 */     Class<?> completorClass = Kit.classOrNull(classLoader, "jline.Completor");
/*     */     
/* 380 */     Object completor = Proxy.newProxyInstance(classLoader, new Class[] { completorClass }, new FlexibleCompletor(completorClass, scope));
/*     */ 
/*     */     
/* 383 */     tryInvoke(reader, "addCompletor", new Class[] { completorClass }, new Object[] { completor });
/*     */     
/* 385 */     return new JLineShellConsoleV1(reader, cs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static JLineShellConsoleV2 getJLineShellConsoleV2(ClassLoader classLoader, Class<?> readerClass, Scriptable scope, Charset cs) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
/* 393 */     Constructor<?> c = readerClass.getConstructor(new Class[0]);
/* 394 */     Object reader = c.newInstance(new Object[0]);
/*     */ 
/*     */     
/* 397 */     tryInvoke(reader, "setBellEnabled", BOOLEAN_ARG, new Object[] { Boolean.FALSE });
/*     */ 
/*     */     
/* 400 */     Class<?> completorClass = Kit.classOrNull(classLoader, "jline.console.completer.Completer");
/*     */     
/* 402 */     Object completor = Proxy.newProxyInstance(classLoader, new Class[] { completorClass }, new FlexibleCompletor(completorClass, scope));
/*     */ 
/*     */     
/* 405 */     tryInvoke(reader, "addCompleter", new Class[] { completorClass }, new Object[] { completor });
/*     */     
/* 407 */     return new JLineShellConsoleV2(reader, cs);
/*     */   }
/*     */   
/*     */   public abstract InputStream getIn();
/*     */   
/*     */   public abstract String readLine() throws IOException;
/*     */   
/*     */   public abstract String readLine(String paramString) throws IOException;
/*     */   
/*     */   public abstract void flush() throws IOException;
/*     */   
/*     */   public abstract void print(String paramString) throws IOException;
/*     */   
/*     */   public abstract void println() throws IOException;
/*     */   
/*     */   public abstract void println(String paramString) throws IOException;
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\tools\shell\ShellConsole.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */