/*     */ package org.sqlite.util;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ public class OSInfo
/*     */ {
/*  42 */   private static HashMap<String, String> archMapping = new HashMap<>();
/*     */   
/*     */   public static final String X86 = "x86";
/*     */   
/*     */   public static final String X86_64 = "x86_64";
/*     */   public static final String IA64_32 = "ia64_32";
/*     */   public static final String IA64 = "ia64";
/*     */   public static final String PPC = "ppc";
/*     */   public static final String PPC64 = "ppc64";
/*     */   
/*     */   static {
/*  53 */     archMapping.put("x86", "x86");
/*  54 */     archMapping.put("i386", "x86");
/*  55 */     archMapping.put("i486", "x86");
/*  56 */     archMapping.put("i586", "x86");
/*  57 */     archMapping.put("i686", "x86");
/*  58 */     archMapping.put("pentium", "x86");
/*     */ 
/*     */     
/*  61 */     archMapping.put("x86_64", "x86_64");
/*  62 */     archMapping.put("amd64", "x86_64");
/*  63 */     archMapping.put("em64t", "x86_64");
/*  64 */     archMapping.put("universal", "x86_64");
/*     */ 
/*     */     
/*  67 */     archMapping.put("ia64", "ia64");
/*  68 */     archMapping.put("ia64w", "ia64");
/*     */ 
/*     */     
/*  71 */     archMapping.put("ia64_32", "ia64_32");
/*  72 */     archMapping.put("ia64n", "ia64_32");
/*     */ 
/*     */     
/*  75 */     archMapping.put("ppc", "ppc");
/*  76 */     archMapping.put("power", "ppc");
/*  77 */     archMapping.put("powerpc", "ppc");
/*  78 */     archMapping.put("power_pc", "ppc");
/*  79 */     archMapping.put("power_rs", "ppc");
/*     */ 
/*     */     
/*  82 */     archMapping.put("ppc64", "ppc64");
/*  83 */     archMapping.put("power64", "ppc64");
/*  84 */     archMapping.put("powerpc64", "ppc64");
/*  85 */     archMapping.put("power_pc64", "ppc64");
/*  86 */     archMapping.put("power_rs64", "ppc64");
/*     */   }
/*     */ 
/*     */   
/*     */   public static void main(String[] args) {
/*  91 */     if (args.length >= 1) {
/*  92 */       if ("--os".equals(args[0])) {
/*  93 */         System.out.print(getOSName());
/*     */         return;
/*     */       } 
/*  96 */       if ("--arch".equals(args[0])) {
/*  97 */         System.out.print(getArchName());
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 102 */     System.out.print(getNativeLibFolderPathForCurrentOS());
/*     */   }
/*     */   
/*     */   public static String getNativeLibFolderPathForCurrentOS() {
/* 106 */     return getOSName() + "/" + getArchName();
/*     */   }
/*     */   
/*     */   public static String getOSName() {
/* 110 */     return translateOSNameToFolderName(System.getProperty("os.name"));
/*     */   }
/*     */   
/*     */   public static boolean isAndroid() {
/* 114 */     return System.getProperty("java.runtime.name", "").toLowerCase().contains("android");
/*     */   }
/*     */   
/*     */   public static boolean isAlpine() {
/*     */     try {
/* 119 */       Process p = Runtime.getRuntime().exec("cat /etc/os-release | grep ^ID");
/* 120 */       p.waitFor(300L, TimeUnit.MILLISECONDS);
/*     */       
/* 122 */       InputStream in = p.getInputStream();
/*     */       try {
/* 124 */         int readLen = 0;
/* 125 */         ByteArrayOutputStream b = new ByteArrayOutputStream();
/* 126 */         byte[] buf = new byte[32];
/* 127 */         while ((readLen = in.read(buf, 0, buf.length)) >= 0) {
/* 128 */           b.write(buf, 0, readLen);
/*     */         }
/* 130 */         return b.toString().toLowerCase().contains("alpine");
/*     */       } finally {
/*     */         
/* 133 */         if (in != null) {
/* 134 */           in.close();
/*     */         }
/*     */       }
/*     */     
/* 138 */     } catch (Throwable e) {
/* 139 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static String getHardwareName() {
/*     */     try {
/* 146 */       Process p = Runtime.getRuntime().exec("uname -m");
/* 147 */       p.waitFor();
/*     */       
/* 149 */       InputStream in = p.getInputStream();
/*     */       try {
/* 151 */         int readLen = 0;
/* 152 */         ByteArrayOutputStream b = new ByteArrayOutputStream();
/* 153 */         byte[] buf = new byte[32];
/* 154 */         while ((readLen = in.read(buf, 0, buf.length)) >= 0) {
/* 155 */           b.write(buf, 0, readLen);
/*     */         }
/* 157 */         return b.toString();
/*     */       } finally {
/*     */         
/* 160 */         if (in != null) {
/* 161 */           in.close();
/*     */         }
/*     */       }
/*     */     
/* 165 */     } catch (Throwable e) {
/* 166 */       System.err.println("Error while running uname -m: " + e.getMessage());
/* 167 */       return "unknown";
/*     */     } 
/*     */   }
/*     */   
/*     */   static String resolveArmArchType() {
/* 172 */     if (System.getProperty("os.name").contains("Linux")) {
/* 173 */       String armType = getHardwareName();
/*     */       
/* 175 */       if (armType.startsWith("armv6"))
/*     */       {
/* 177 */         return "armv6";
/*     */       }
/* 179 */       if (armType.startsWith("armv7"))
/*     */       {
/* 181 */         return "armv7";
/*     */       }
/* 183 */       if (armType.startsWith("armv5"))
/*     */       {
/* 185 */         return "arm";
/*     */       }
/* 187 */       if (armType.equals("aarch64"))
/*     */       {
/* 189 */         return "arm64";
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 194 */       String abi = System.getProperty("sun.arch.abi");
/* 195 */       if (abi != null && abi.startsWith("gnueabihf")) {
/* 196 */         return "armv7";
/*     */       }
/*     */ 
/*     */       
/* 200 */       String javaHome = System.getProperty("java.home");
/*     */       
/*     */       try {
/* 203 */         int exitCode = Runtime.getRuntime().exec("which readelf").waitFor();
/* 204 */         if (exitCode == 0) {
/* 205 */           String[] cmdarray = { "/bin/sh", "-c", "find '" + javaHome + "' -name 'libjvm.so' | head -1 | xargs readelf -A | grep 'Tag_ABI_VFP_args: VFP registers'" };
/*     */ 
/*     */           
/* 208 */           exitCode = Runtime.getRuntime().exec(cmdarray).waitFor();
/* 209 */           if (exitCode == 0) {
/* 210 */             return "armv7";
/*     */           }
/*     */         } else {
/* 213 */           System.err.println("WARNING! readelf not found. Cannot check if running on an armhf system, armel architecture will be presumed.");
/*     */         }
/*     */       
/*     */       }
/* 217 */       catch (IOException iOException) {
/*     */ 
/*     */       
/* 220 */       } catch (InterruptedException interruptedException) {}
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 225 */     return "arm";
/*     */   }
/*     */   
/*     */   public static String getArchName() {
/* 229 */     String osArch = System.getProperty("os.arch");
/*     */     
/* 231 */     if (isAndroid()) {
/* 232 */       return "android-arm";
/*     */     }
/*     */     
/* 235 */     if (osArch.startsWith("arm")) {
/* 236 */       osArch = resolveArmArchType();
/*     */     } else {
/*     */       
/* 239 */       String lc = osArch.toLowerCase(Locale.US);
/* 240 */       if (archMapping.containsKey(lc))
/* 241 */         return archMapping.get(lc); 
/*     */     } 
/* 243 */     return translateArchNameToFolderName(osArch);
/*     */   }
/*     */   
/*     */   static String translateOSNameToFolderName(String osName) {
/* 247 */     if (osName.contains("Windows")) {
/* 248 */       return "Windows";
/*     */     }
/* 250 */     if (osName.contains("Mac") || osName.contains("Darwin")) {
/* 251 */       return "Mac";
/*     */     }
/* 253 */     if (isAlpine()) {
/* 254 */       return "Linux-Alpine";
/*     */     }
/* 256 */     if (osName.contains("Linux")) {
/* 257 */       return "Linux";
/*     */     }
/* 259 */     if (osName.contains("AIX")) {
/* 260 */       return "AIX";
/*     */     }
/*     */ 
/*     */     
/* 264 */     return osName.replaceAll("\\W", "");
/*     */   }
/*     */ 
/*     */   
/*     */   static String translateArchNameToFolderName(String archName) {
/* 269 */     return archName.replaceAll("\\W", "");
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\sqlit\\util\OSInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */