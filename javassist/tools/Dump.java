/*    */ package javassist.tools;
/*    */ 
/*    */ import java.io.DataInputStream;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.PrintWriter;
/*    */ import javassist.bytecode.ClassFile;
/*    */ import javassist.bytecode.ClassFilePrinter;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Dump
/*    */ {
/*    */   public static void main(String[] args) throws Exception {
/* 46 */     if (args.length != 1) {
/* 47 */       System.err.println("Usage: java Dump <class file name>");
/*    */       
/*    */       return;
/*    */     } 
/* 51 */     DataInputStream in = new DataInputStream(new FileInputStream(args[0]));
/*    */     
/* 53 */     ClassFile w = new ClassFile(in);
/* 54 */     PrintWriter out = new PrintWriter(System.out, true);
/* 55 */     out.println("*** constant pool ***");
/* 56 */     w.getConstPool().print(out);
/* 57 */     out.println();
/* 58 */     out.println("*** members ***");
/* 59 */     ClassFilePrinter.print(w, out);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\tools\Dump.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */