/*    */ package com.kichik.pecoff4j.util;
/*    */ 
/*    */ import com.kichik.pecoff4j.PE;
/*    */ import com.kichik.pecoff4j.ResourceDirectory;
/*    */ import com.kichik.pecoff4j.io.PEParser;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
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
/*    */ public class RCEdit
/*    */ {
/*    */   public static void main(String[] args) throws Exception {
/* 21 */     launch(new String[0]);
/* 22 */     launch(new String[] { "/I", "test/WinRun4J.exe", "test/eclipse.ico" });
/*    */   }
/*    */   
/*    */   public static void launch(String[] args) throws Exception {
/* 26 */     assertArgCount(args, 2, 3);
/*    */     
/* 28 */     String option = args[0].toUpperCase();
/* 29 */     if ("/I".equals(option)) {
/* 30 */       assertArgCount(args, 3, 3);
/* 31 */       addIcon(args[1], args[2]);
/* 32 */     } else if ("/N".equals(option)) {
/* 33 */       assertArgCount(args, 3, 3);
/* 34 */       setIni(args[1], args[2]);
/* 35 */     } else if ("/S".equals(option)) {
/* 36 */       assertArgCount(args, 3, 3);
/* 37 */       setSplash(args[1], args[2]);
/*    */     } 
/*    */   }
/*    */   
/*    */   private static void addIcon(String exe, String icon) throws IOException {
/* 42 */     PE pe = PEParser.parse(exe);
/* 43 */     IconFile ic = IconFile.parse(icon);
/*    */   }
/*    */   
/*    */   private static void setIni(String exe, String ini) throws IOException {
/* 47 */     PE pe = PEParser.parse(exe);
/* 48 */     byte[] inib = IO.toBytes(new File(ini));
/* 49 */     ResourceDirectory rd = pe.getImageData().getResourceTable();
/* 50 */     if (rd != null);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static void setSplash(String exe, String splash) throws IOException {
/* 56 */     PE pe = PEParser.parse(exe);
/* 57 */     byte[] spb = IO.toBytes(new File(splash));
/* 58 */     ResourceDirectory rd = pe.getImageData().getResourceTable();
/*    */   }
/*    */ 
/*    */   
/*    */   private static void assertArgCount(String[] args, int min, int max) {
/* 63 */     if (args.length < min || args.length > max) {
/* 64 */       printUsage();
/* 65 */       System.exit(1);
/*    */     } 
/*    */   }
/*    */   
/*    */   private static void printUsage() {
/* 70 */     printf("WinRun4J Resource Editor v2.0 (winrun4j.sf.net)\n\n");
/* 71 */     printf("Edits resources in executables (EXE) and dynamic link-libraries (DLL).\n\n");
/* 72 */     printf("RCEDIT <option> <exe/dll> [resource]\n\n");
/* 73 */     printf("  filename\tSpecifies the filename of the EXE/DLL.\n");
/* 74 */     printf("  resource\tSpecifies the name of the resource to add to the EXE/DLL.\n");
/* 75 */     printf("  /I\t\tSet the icon as the default icon for the executable.\n");
/* 76 */     printf("  /A\t\tAdds an icon to the EXE/DLL.\n");
/* 77 */     printf("  /N\t\tSets the INI file.\n");
/* 78 */     printf("  /J\t\tAdds a JAR file.\n");
/* 79 */     printf("  /E\t\tExtracts a JAR file from the EXE/DLL.\n");
/* 80 */     printf("  /S\t\tSets the splash image.\n");
/* 81 */     printf("  /C\t\tClears all resources from the EXE/DLL.\n");
/* 82 */     printf("  /L\t\tLists the resources in the EXE/DLL.\n");
/* 83 */     printf("  /P\t\tOutputs the contents of the INI file in the EXE.\n");
/*    */   }
/*    */   
/*    */   private static void printf(String s) {
/* 87 */     System.out.print(s);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4\\util\RCEdit.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */