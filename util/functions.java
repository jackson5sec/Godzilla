/*      */ package util;
/*      */ import java.awt.Desktop;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.File;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.Method;
/*      */ import java.net.HttpURLConnection;
/*      */ import java.net.InetAddress;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.security.MessageDigest;
/*      */ import java.security.NoSuchAlgorithmException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Random;
/*      */ import java.util.Vector;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ import java.util.zip.GZIPOutputStream;
/*      */ import javax.crypto.Mac;
/*      */ 
/*      */ public class functions {
/*   31 */   private static final char[] toBase64 = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };
/*      */ 
/*      */ 
/*      */   
/*   35 */   private static final char[] toBase64URL = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_' };
/*      */ 
/*      */   
/*      */   private static final double TOOLSKIT_WIDTH = 1920.0D;
/*      */ 
/*      */   
/*      */   private static final double TOOLSKIT_HEIGHT = 1080.0D;
/*      */   
/*   43 */   private static double CURRENT_WIDTH = 1920.0D;
/*   44 */   private static double CURRENT_HEIGHT = 1080.0D;
/*      */   
/*   46 */   private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/*      */   
/*      */   static {
/*   49 */     double _CURRENT_WIDTH = (Toolkit.getDefaultToolkit().getScreenSize()).width;
/*   50 */     double _CURRENT_HEIGHT = (Toolkit.getDefaultToolkit().getScreenSize()).height;
/*   51 */     if (_CURRENT_HEIGHT > 1080.0D && _CURRENT_WIDTH > 1920.0D) {
/*   52 */       CURRENT_WIDTH = _CURRENT_WIDTH;
/*   53 */       CURRENT_HEIGHT = _CURRENT_HEIGHT;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getNetworSpeedk(long size) {
/*   60 */     if (size < 1024L) {
/*   61 */       return String.valueOf(size) + "B";
/*      */     }
/*   63 */     size /= 1024L;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   68 */     if (size < 1024L) {
/*   69 */       return String.valueOf(size) + "KB";
/*      */     }
/*   71 */     size /= 1024L;
/*      */     
/*   73 */     if (size < 1024L) {
/*      */ 
/*      */       
/*   76 */       size *= 100L;
/*   77 */       return String.valueOf(size / 100L) + "." + 
/*   78 */         String.valueOf(size % 100L) + "MB";
/*      */     } 
/*      */     
/*   81 */     size = size * 100L / 1024L;
/*   82 */     return String.valueOf(size / 100L) + "." + 
/*   83 */       String.valueOf(size % 100L) + "GB";
/*      */   }
/*      */ 
/*      */   
/*      */   public static void concatMap(Map<String, List<String>> receiveMap, Map<String, List<String>> map) {
/*   88 */     Iterator<String> iterator = map.keySet().iterator();
/*   89 */     String key = null;
/*   90 */     while (iterator.hasNext()) {
/*   91 */       key = iterator.next();
/*   92 */       receiveMap.put(key, map.get(key));
/*      */     } 
/*      */   }
/*      */   public static boolean isMatch(String s, String p, boolean us) {
/*   96 */     if (us) {
/*   97 */       return isMatch(s, p);
/*      */     }
/*   99 */     return isMatch(s.toLowerCase(), p.toLowerCase());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static String SHA(byte[] data, String strType) {
/*  105 */     String strResult = null;
/*      */ 
/*      */     
/*  108 */     if (data != null && data.length > 0) {
/*      */       
/*      */       try {
/*      */ 
/*      */ 
/*      */         
/*  114 */         MessageDigest messageDigest = MessageDigest.getInstance(strType);
/*      */         
/*  116 */         messageDigest.update(data);
/*      */         
/*  118 */         byte[] byteBuffer = messageDigest.digest();
/*      */ 
/*      */         
/*  121 */         StringBuffer strHexString = new StringBuffer();
/*      */         
/*  123 */         for (int i = 0; i < byteBuffer.length; i++) {
/*      */           
/*  125 */           String hex = Integer.toHexString(0xFF & byteBuffer[i]);
/*  126 */           if (hex.length() == 1)
/*      */           {
/*  128 */             strHexString.append('0');
/*      */           }
/*  130 */           strHexString.append(hex);
/*      */         } 
/*      */         
/*  133 */         strResult = strHexString.toString();
/*      */       }
/*  135 */       catch (NoSuchAlgorithmException e) {
/*      */         
/*  137 */         e.printStackTrace();
/*      */       } 
/*      */     }
/*      */     
/*  141 */     return strResult;
/*      */   }
/*      */   
/*      */   public static boolean isMatch(String s, String p) {
/*  145 */     int i = 0;
/*  146 */     int j = 0;
/*  147 */     int starIndex = -1;
/*  148 */     int iIndex = -1;
/*  149 */     while (i < s.length()) {
/*  150 */       if (j < p.length() && (p.charAt(j) == '?' || p.charAt(j) == s.charAt(i))) {
/*  151 */         i++;
/*  152 */         j++; continue;
/*  153 */       }  if (j < p.length() && p.charAt(j) == '*') {
/*  154 */         starIndex = j;
/*  155 */         iIndex = i;
/*  156 */         j++; continue;
/*  157 */       }  if (starIndex != -1) {
/*  158 */         j = starIndex + 1;
/*  159 */         i = iIndex + 1;
/*  160 */         iIndex++; continue;
/*      */       } 
/*  162 */       return false;
/*      */     } 
/*      */     
/*  165 */     while (j < p.length() && p.charAt(j) == '*') {
/*  166 */       j++;
/*      */     }
/*  168 */     return (j == p.length());
/*      */   }
/*      */   public static void setWindowSize(Window window, int width, int height) {
/*  171 */     window.setSize((int)(width / 1920.0D * CURRENT_WIDTH), (int)(height / 1080.0D * CURRENT_HEIGHT));
/*      */   }
/*      */   
/*      */   public static byte[] HMACSHA256(byte[] data, byte[] key) throws Exception {
/*  175 */     Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
/*  176 */     SecretKeySpec secret_key = new SecretKeySpec(key, "HmacSHA256");
/*  177 */     sha256_HMAC.init(secret_key);
/*  178 */     byte[] array = sha256_HMAC.doFinal(data);
/*  179 */     return array;
/*      */   }
/*      */   public static void fireActionEventByJComboBox(JComboBox comboBox) {
/*      */     try {
/*  183 */       comboBox.setSelectedIndex(0);
/*  184 */     } catch (Exception e) {
/*  185 */       Log.error(e);
/*      */     } 
/*      */   }
/*      */   public static String readCString(ByteBuffer buff) {
/*  189 */     StringBuilder stringBuilder = new StringBuilder();
/*      */     byte c;
/*  191 */     while ((c = buff.get()) != 0) {
/*  192 */       stringBuilder.append((char)c);
/*      */     }
/*  194 */     return stringBuilder.toString();
/*      */   }
/*      */   
/*      */   public static byte[] ipToByteArray(String paramString) {
/*  198 */     String[] array2 = paramString.split("\\.");
/*  199 */     byte[] array = new byte[4];
/*  200 */     for (int i = 0; i < array2.length; i++) {
/*  201 */       array[i] = (byte)Integer.parseInt(array2[i]);
/*      */     }
/*  203 */     return array;
/*      */   }
/*      */   public static boolean isContainChinese(String str) {
/*  206 */     Pattern p = Pattern.compile("[一-龥]");
/*  207 */     Matcher m = p.matcher(str);
/*  208 */     if (m.find()) {
/*  209 */       return true;
/*      */     }
/*  211 */     return false;
/*      */   }
/*      */   public static byte[] shortToByteArray(short s) {
/*  214 */     byte[] targets = new byte[2];
/*  215 */     for (int i = 0; i < 2; i++) {
/*  216 */       int offset = (targets.length - 1 - i) * 8;
/*  217 */       targets[i] = (byte)(s >>> offset & 0xFF);
/*      */     } 
/*  219 */     return targets;
/*      */   }
/*      */   public static int random(int a, int b) {
/*  222 */     int temp = 0;
/*  223 */     if (b < 1 || a > b) {
/*  224 */       return 0;
/*      */     }
/*  226 */     if (a == b) {
/*  227 */       return a;
/*      */     }
/*      */     
/*      */     try {
/*  231 */       if (a > b) {
/*  232 */         temp = (new Random()).nextInt(a - b);
/*  233 */         return temp + b;
/*      */       } 
/*  235 */       temp = (new Random()).nextInt(b - a);
/*  236 */       return temp + a;
/*      */     }
/*  238 */     catch (Exception e) {
/*  239 */       Log.error(e);
/*      */       
/*  241 */       return temp + a;
/*      */     } 
/*      */   }
/*      */   public static String endTrim(String value) {
/*  245 */     int i = value.length();
/*  246 */     byte b = 0;
/*  247 */     char[] arrayOfChar = value.toCharArray();
/*  248 */     while (b < i && arrayOfChar[i - 1] <= ' ') {
/*  249 */       i--;
/*      */     }
/*  251 */     return (b > 0 || i < arrayOfChar.length) ? value.substring(b, i) : value;
/*      */   }
/*      */   public static String startTrim(String value) {
/*  254 */     int i = value.length();
/*  255 */     byte b = 0;
/*  256 */     char[] arrayOfChar = value.toCharArray();
/*  257 */     while (b < i && arrayOfChar[b] <= ' ') {
/*  258 */       b = (byte)(b + 1);
/*      */     }
/*  260 */     return (b > 0 || i < arrayOfChar.length) ? value.substring(b, i) : value;
/*      */   }
/*      */   public static byte[] intToBytes(int value) {
/*  263 */     byte[] src = new byte[4];
/*  264 */     src[0] = (byte)(value & 0xFF);
/*  265 */     src[1] = (byte)(value >> 8 & 0xFF);
/*  266 */     src[2] = (byte)(value >> 16 & 0xFF);
/*  267 */     src[3] = (byte)(value >> 24 & 0xFF);
/*  268 */     return src;
/*      */   }
/*      */   public static String getJarFileByClass(Class cs) {
/*  271 */     String fileString = null;
/*      */     
/*  273 */     if (cs != null) {
/*  274 */       String tmpString = cs.getProtectionDomain().getCodeSource().getLocation().getFile();
/*  275 */       if (tmpString.endsWith(".jar")) {
/*      */         try {
/*  277 */           fileString = URLDecoder.decode(tmpString, "utf-8");
/*  278 */         } catch (UnsupportedEncodingException e) {
/*  279 */           Log.error(e);
/*  280 */           fileString = URLDecoder.decode(tmpString);
/*      */         } 
/*      */       }
/*      */     } 
/*  284 */     return fileString;
/*      */   }
/*      */   public static String byteArrayToHexPrefix(byte[] bytes, String prefix) {
/*  287 */     String strHex = "";
/*  288 */     StringBuilder sb = new StringBuilder();
/*  289 */     for (int n = 0; n < bytes.length; n++) {
/*  290 */       strHex = Integer.toHexString(bytes[n] & 0xFF);
/*  291 */       sb.append(prefix);
/*  292 */       sb.append((strHex.length() == 1) ? ("0" + strHex) : strHex);
/*      */     } 
/*  294 */     return sb.toString().trim();
/*      */   }
/*      */   public static String byteArrayToHex(byte[] bytes) {
/*  297 */     return byteArrayToHexPrefix(bytes, "");
/*      */   }
/*      */   
/*      */   public static byte[] hexToByte(String hex) {
/*  301 */     int m = 0, n = 0;
/*  302 */     int byteLen = hex.length() / 2;
/*  303 */     byte[] ret = new byte[byteLen];
/*  304 */     for (int i = 0; i < byteLen; i++) {
/*  305 */       m = i * 2 + 1;
/*  306 */       n = m + 1;
/*  307 */       int intVal = Integer.decode("0x" + hex.substring(i * 2, m) + hex.substring(m, n)).intValue();
/*  308 */       ret[i] = Byte.valueOf((byte)intVal).byteValue();
/*      */     } 
/*  310 */     return ret;
/*      */   }
/*      */   
/*      */   public static boolean isGzipStream(byte[] data) {
/*  314 */     if (data != null && data.length >= 2) {
/*  315 */       int ss = data[0] & 0xFF | (data[1] & 0xFF) << 8;
/*  316 */       return (ss == 35615);
/*      */     } 
/*  318 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static Class loadClass(ClassLoader loader, String className) {
/*      */     try {
/*  324 */       return loader.loadClass(className);
/*  325 */     } catch (Exception e) {
/*  326 */       return null;
/*      */     } 
/*      */   }
/*      */   
/*      */   public static boolean appendFile(File file, byte[] content) {
/*  331 */     try (FileOutputStream fileOutputStream = new FileOutputStream(file, true)) {
/*  332 */       fileOutputStream.write(content);
/*  333 */       return true;
/*  334 */     } catch (Throwable e) {
/*  335 */       e.printStackTrace();
/*      */       
/*  337 */       return false;
/*      */     } 
/*      */   }
/*      */   public static String readFileBottomLine(File file, int number) {
/*  341 */     StringBuilder stringBuilder = new StringBuilder();
/*  342 */     try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
/*  343 */       ArrayList<String> arrayList = new ArrayList();
/*  344 */       String line = null;
/*  345 */       while ((line = bufferedReader.readLine()) != null) {
/*  346 */         arrayList.add(line);
/*      */       }
/*      */       
/*  349 */       if (arrayList.size() > number) {
/*  350 */         arrayList.subList(arrayList.size() - 1 - number, arrayList.size()).forEach(v -> {
/*      */               stringBuilder.append(v);
/*      */               
/*      */               stringBuilder.append('\n');
/*      */             });
/*      */       } else {
/*  356 */         arrayList.forEach(v -> {
/*      */               stringBuilder.append(v);
/*      */               stringBuilder.append('\n');
/*      */             });
/*      */       } 
/*  361 */     } catch (Exception exception) {}
/*      */ 
/*      */     
/*  364 */     return stringBuilder.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   public static Object concatArrays(Object array1, int array1_Start, int array1_End, Object array2, int array2_Start, int array2_End) {
/*  369 */     if (array1.getClass().isArray() && array2.getClass().isArray()) {
/*  370 */       if (array1_Start >= 0 && array1_Start >= 0 && array2_End >= 0 && array2_Start >= 0) {
/*  371 */         int array1len = (array1_Start != array1_End) ? (array1_End - array1_Start + 1) : 0;
/*  372 */         int array2len = (array2_Start != array2_End) ? (array2_End - array2_Start + 1) : 0;
/*  373 */         int maxLen = array1len + array2len;
/*  374 */         byte[] data = new byte[maxLen];
/*  375 */         System.arraycopy(array1, array1_Start, data, 0, array1len);
/*  376 */         System.arraycopy(array2, array2_Start, data, array1len, array2len);
/*      */         
/*  378 */         return data;
/*      */       } 
/*  380 */       return null;
/*      */     } 
/*      */     
/*  383 */     return null;
/*      */   }
/*      */   
/*      */   public static boolean delFiles(File file) {
/*  387 */     boolean result = false;
/*      */     try {
/*  389 */       if (file.isDirectory()) {
/*  390 */         File[] childrenFiles = file.listFiles();
/*  391 */         for (File childFile : childrenFiles) {
/*  392 */           result = delFiles(childFile);
/*  393 */           if (!result) {
/*  394 */             return result;
/*      */           }
/*      */         } 
/*      */       } 
/*  398 */       result = file.delete();
/*  399 */     } catch (Exception e) {
/*  400 */       e.printStackTrace();
/*      */     } 
/*  402 */     return result;
/*      */   }
/*      */   public static void addShutdownHook(final Class<?> cls, final Object object) {
/*  405 */     Runtime.getRuntime().addShutdownHook(new Thread(new Runnable()
/*      */           {
/*      */             public void run()
/*      */             {
/*      */               try {
/*  410 */                 cls.getMethod("Tclose", null).invoke(object, (Object[])null);
/*  411 */               } catch (Exception e) {
/*      */                 
/*  413 */                 e.printStackTrace();
/*      */               } 
/*      */             }
/*      */           }));
/*      */   }
/*      */ 
/*      */   
/*      */   public static short bytesToShort(byte[] bytes) {
/*  421 */     return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
/*      */   }
/*      */   
/*      */   public static int stringToint(String intString) {
/*  425 */     return stringToint(intString, 0);
/*      */   }
/*      */   public static int stringToint(String intString, int defaultValue) {
/*      */     try {
/*  429 */       return Integer.parseInt(intString.trim());
/*  430 */     } catch (Exception e) {
/*  431 */       return defaultValue;
/*      */     } 
/*      */   }
/*      */   public static Long stringToLong(String intString, long defaultValue) {
/*      */     try {
/*  436 */       return Long.valueOf(Long.parseLong(intString.trim()));
/*  437 */     } catch (Exception e) {
/*  438 */       return Long.valueOf(defaultValue);
/*      */     } 
/*      */   }
/*      */   
/*      */   public static byte[] readInputStream(InputStream inputStream) {
/*  443 */     byte[] temp = new byte[5120];
/*  444 */     int readOneNum = 0;
/*  445 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/*      */     try {
/*  447 */       while ((readOneNum = inputStream.read(temp)) != -1) {
/*  448 */         bos.write(temp, 0, readOneNum);
/*      */       }
/*  450 */     } catch (Exception e) {
/*  451 */       Log.error(e);
/*      */     } 
/*  453 */     return bos.toByteArray();
/*      */   }
/*      */   
/*      */   public static HashMap<String, String> matcherTwoChild(String data, String regex) {
/*  457 */     String rexString = regex;
/*  458 */     Pattern pattern = Pattern.compile(rexString);
/*  459 */     Matcher m = pattern.matcher(data);
/*  460 */     HashMap<String, String> hashMap = new HashMap<>();
/*  461 */     while (m.find()) {
/*      */       try {
/*  463 */         String v1 = m.group(1);
/*  464 */         String v2 = m.group(2);
/*  465 */         hashMap.put(v1, v2);
/*  466 */       } catch (Exception e) {
/*  467 */         Log.error(e);
/*      */       } 
/*      */     } 
/*  470 */     return hashMap;
/*      */   }
/*      */ 
/*      */   
/*      */   public static short[] toShortArray(byte[] src) {
/*  475 */     int count = src.length >> 1;
/*  476 */     short[] dest = new short[count];
/*  477 */     for (int i = 0; i < count; i++) {
/*  478 */       dest[i] = (short)(src[i * 2] << 8 | src[2 * i + 1] & 0xFF);
/*      */     }
/*  480 */     return dest;
/*      */   }
/*      */   
/*      */   public static byte[] stringToByteArray(String data, String encodng) {
/*      */     try {
/*  485 */       return data.getBytes(encodng);
/*  486 */     } catch (Exception e) {
/*  487 */       return data.getBytes();
/*      */     } 
/*      */   }
/*      */   
/*      */   public static String formatDir(String dirString) {
/*  492 */     if (dirString != null && dirString.length() > 0) {
/*  493 */       dirString = dirString.trim();
/*  494 */       dirString = dirString.replaceAll("\\\\+", "/").replaceAll("/+", "/").trim();
/*  495 */       if (!dirString.substring(dirString.length() - 1).equals("/")) {
/*  496 */         dirString = dirString + "/";
/*      */       }
/*  498 */       return dirString;
/*      */     } 
/*  500 */     return "";
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean filePutContent(String file, byte[] data) {
/*  506 */     return filePutContent(new File(file), data);
/*      */   }
/*      */   
/*      */   public static boolean filePutContent(File file, byte[] data) {
/*  510 */     boolean state = false;
/*      */     try {
/*  512 */       FileOutputStream outputStream = new FileOutputStream(file);
/*  513 */       outputStream.write(data);
/*  514 */       outputStream.flush();
/*  515 */       outputStream.close();
/*  516 */       state = true;
/*  517 */     } catch (Exception e) {
/*  518 */       Log.error(e);
/*  519 */       state = false;
/*      */     } 
/*  521 */     return state;
/*      */   }
/*      */   public static String getRandomString(int length) {
/*  524 */     String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
/*  525 */     Random random = new Random();
/*  526 */     StringBuffer sb = new StringBuffer();
/*  527 */     sb.append(str.charAt(random.nextInt(52)));
/*  528 */     str = str + "0123456789";
/*  529 */     for (int i = 0; i < length; i++) {
/*  530 */       int number = random.nextInt(62);
/*  531 */       sb.append(str.charAt(number));
/*      */     } 
/*  533 */     return sb.toString();
/*      */   }
/*      */   public static String concatCookie(String oldCookie, String newCookie) {
/*  536 */     oldCookie = oldCookie + ";";
/*  537 */     newCookie = newCookie + ";";
/*  538 */     StringBuffer cookieBuffer = new StringBuffer();
/*  539 */     Map<String, String> cookieMap = new HashMap<>();
/*  540 */     String[] tmpA = oldCookie.split(";");
/*      */     int i;
/*  542 */     for (i = 0; i < tmpA.length; i++) {
/*  543 */       String[] temB = tmpA[i].split("=");
/*  544 */       cookieMap.put(temB[0], temB[1]);
/*      */     } 
/*  546 */     tmpA = newCookie.split(";");
/*  547 */     for (i = 0; i < tmpA.length; i++) {
/*  548 */       String[] temB = tmpA[i].split("=");
/*  549 */       cookieMap.put(temB[0], temB[1]);
/*      */     } 
/*  551 */     Iterator<String> iterator = cookieMap.keySet().iterator();
/*      */     
/*  553 */     while (iterator.hasNext()) {
/*  554 */       String keyString = iterator.next();
/*  555 */       cookieBuffer.append(keyString);
/*  556 */       cookieBuffer.append("=");
/*  557 */       cookieBuffer.append(cookieMap.get(keyString));
/*  558 */       cookieBuffer.append(";");
/*      */     } 
/*  560 */     return cookieBuffer.toString();
/*      */   }
/*      */   
/*      */   public static Method getMethodByClass(Class cs, String methodName, Class... parameters) {
/*  564 */     Method method = null;
/*  565 */     while (cs != null) {
/*      */       try {
/*  567 */         method = cs.getDeclaredMethod(methodName, parameters);
/*  568 */         method.setAccessible(true);
/*  569 */         cs = null;
/*  570 */       } catch (Exception e) {
/*  571 */         cs = cs.getSuperclass();
/*      */       } 
/*      */     } 
/*  574 */     return method;
/*      */   }
/*      */   public static Object getFieldValue(Object obj, String fieldName) throws Exception {
/*  577 */     Field f = null;
/*  578 */     if (obj instanceof Field) {
/*  579 */       f = (Field)obj;
/*      */     } else {
/*  581 */       Method method = null;
/*  582 */       Class<?> cs = obj.getClass();
/*  583 */       while (cs != null) {
/*      */         try {
/*  585 */           f = cs.getDeclaredField(fieldName);
/*  586 */           cs = null;
/*  587 */         } catch (Exception e) {
/*  588 */           cs = cs.getSuperclass();
/*      */         } 
/*      */       } 
/*      */     } 
/*  592 */     f.setAccessible(true);
/*  593 */     return f.get(obj);
/*      */   }
/*      */   public static Object invoke(Object obj, String methodName, Object... parameters) {
/*      */     try {
/*  597 */       ArrayList<Class<?>> classes = new ArrayList();
/*  598 */       if (parameters != null) {
/*  599 */         for (int i = 0; i < parameters.length; i++) {
/*  600 */           Object o1 = parameters[i];
/*  601 */           if (o1 != null) {
/*  602 */             classes.add(o1.getClass());
/*      */           } else {
/*  604 */             classes.add(null);
/*      */           } 
/*      */         } 
/*      */       }
/*  608 */       Method method = getMethodByClass(obj.getClass(), methodName, (Class[])classes.<Class<?>[]>toArray((Class<?>[][])new Class[0]));
/*      */       
/*  610 */       return method.invoke(obj, parameters);
/*  611 */     } catch (Exception exception) {
/*      */       
/*  613 */       return null;
/*      */     } 
/*      */   }
/*      */   public static String md5(String s) {
/*  617 */     return byteArrayToHex(md5(s.getBytes()));
/*      */   }
/*      */   public static byte[] readInputStreamAutoClose(InputStream inputStream) {
/*  620 */     byte[] ret = new byte[0];
/*      */     try {
/*  622 */       ret = readInputStream(inputStream);
/*  623 */       inputStream.close();
/*  624 */     } catch (IOException e) {
/*  625 */       Log.error(e);
/*  626 */       throw new RuntimeException(e);
/*      */     } 
/*      */     
/*  629 */     return ret;
/*      */   }
/*      */   public static byte[] md5(byte[] data) {
/*  632 */     byte[] ret = null;
/*      */     
/*      */     try {
/*  635 */       MessageDigest m = MessageDigest.getInstance("MD5");
/*  636 */       m.update(data, 0, data.length);
/*  637 */       ret = m.digest();
/*  638 */     } catch (NoSuchAlgorithmException e) {
/*  639 */       Log.error(e);
/*      */     } 
/*  641 */     return ret;
/*      */   }
/*      */   public static String getCurrentTime() {
/*  644 */     return DATE_FORMAT.format(new Date());
/*      */   }
/*      */   public static byte[] base64Encode(byte[] src) {
/*  647 */     int off = 0;
/*  648 */     int end = src.length;
/*  649 */     byte[] dst = new byte[4 * (src.length + 2) / 3];
/*  650 */     int linemax = -1;
/*  651 */     boolean doPadding = true;
/*  652 */     char[] base64 = toBase64;
/*  653 */     int sp = off;
/*  654 */     int slen = (end - off) / 3 * 3;
/*  655 */     int sl = off + slen;
/*  656 */     if (linemax > 0 && slen > linemax / 4 * 3) {
/*  657 */       slen = linemax / 4 * 3;
/*      */     }
/*  659 */     int dp = 0;
/*  660 */     while (sp < sl) {
/*  661 */       int sl0 = Math.min(sp + slen, sl);
/*  662 */       for (int sp0 = sp, dp0 = dp; sp0 < sl0; ) {
/*  663 */         int bits = (src[sp0++] & 0xFF) << 16 | (src[sp0++] & 0xFF) << 8 | src[sp0++] & 0xFF;
/*  664 */         dst[dp0++] = (byte)base64[bits >>> 18 & 0x3F];
/*  665 */         dst[dp0++] = (byte)base64[bits >>> 12 & 0x3F];
/*  666 */         dst[dp0++] = (byte)base64[bits >>> 6 & 0x3F];
/*  667 */         dst[dp0++] = (byte)base64[bits & 0x3F];
/*      */       } 
/*  669 */       int dlen = (sl0 - sp) / 3 * 4;
/*  670 */       dp += dlen;
/*  671 */       sp = sl0;
/*      */     } 
/*  673 */     if (sp < end) {
/*  674 */       int b0 = src[sp++] & 0xFF;
/*  675 */       dst[dp++] = (byte)base64[b0 >> 2];
/*  676 */       if (sp == end) {
/*  677 */         dst[dp++] = (byte)base64[b0 << 4 & 0x3F];
/*  678 */         if (doPadding) {
/*  679 */           dst[dp++] = 61;
/*  680 */           dst[dp++] = 61;
/*      */         } 
/*      */       } else {
/*  683 */         int b1 = src[sp++] & 0xFF;
/*  684 */         dst[dp++] = (byte)base64[b0 << 4 & 0x3F | b1 >> 4];
/*  685 */         dst[dp++] = (byte)base64[b1 << 2 & 0x3F];
/*  686 */         if (doPadding) {
/*  687 */           dst[dp++] = 61;
/*      */         }
/*      */       } 
/*      */     } 
/*  691 */     return dst;
/*      */   }
/*      */   
/*      */   public static String base64EncodeToString(byte[] bytes) {
/*  695 */     return new String(base64Encode(bytes));
/*      */   }
/*      */   public static String base64DecodeToString(String base64Str) {
/*  698 */     return new String(base64Decode(base64Str));
/*      */   }
/*      */   
/*      */   public static byte[] base64Decode(String base64Str) {
/*  702 */     if (base64Str == null || base64Str.isEmpty()) {
/*  703 */       return new byte[0];
/*      */     }
/*  705 */     base64Str = base64Str.replace("\r", "").replace("\n", "").replace("\\/", "/").replace("\\\\", "\\");
/*  706 */     byte[] src = base64Str.getBytes();
/*  707 */     if (src.length == 0) {
/*  708 */       return src;
/*      */     }
/*  710 */     int sp = 0;
/*  711 */     int sl = src.length;
/*  712 */     int paddings = 0;
/*  713 */     int len = sl - sp;
/*  714 */     if (src[sl - 1] == 61) {
/*  715 */       paddings++;
/*  716 */       if (src[sl - 2] == 61) {
/*  717 */         paddings++;
/*      */       }
/*      */     } 
/*  720 */     if (paddings == 0 && (len & 0x3) != 0) {
/*  721 */       paddings = 4 - (len & 0x3);
/*      */     }
/*  723 */     byte[] dst = new byte[3 * (len + 3) / 4 - paddings];
/*  724 */     int[] base64 = new int[256];
/*  725 */     Arrays.fill(base64, -1);
/*  726 */     for (int i = 0; i < toBase64.length; i++) {
/*  727 */       base64[toBase64[i]] = i;
/*      */     }
/*  729 */     base64[61] = -2;
/*  730 */     int dp = 0;
/*  731 */     int bits = 0;
/*  732 */     int shiftto = 18;
/*  733 */     while (sp < sl) {
/*  734 */       int b = src[sp++] & 0xFF;
/*  735 */       if ((b = base64[b]) < 0 && 
/*  736 */         b == -2) {
/*  737 */         if ((shiftto == 6 && (sp == sl || src[sp++] != 61)) || shiftto == 18) {
/*  738 */           throw new IllegalArgumentException("Input byte array has wrong 4-byte ending unit");
/*      */         }
/*      */         
/*      */         break;
/*      */       } 
/*  743 */       bits |= b << shiftto;
/*  744 */       shiftto -= 6;
/*  745 */       if (shiftto < 0) {
/*  746 */         dst[dp++] = (byte)(bits >> 16);
/*  747 */         dst[dp++] = (byte)(bits >> 8);
/*  748 */         dst[dp++] = (byte)bits;
/*  749 */         shiftto = 18;
/*  750 */         bits = 0;
/*      */       } 
/*      */     } 
/*      */     
/*  754 */     if (shiftto == 6) {
/*  755 */       dst[dp++] = (byte)(bits >> 16);
/*  756 */     } else if (shiftto == 0) {
/*  757 */       dst[dp++] = (byte)(bits >> 16);
/*  758 */       dst[dp++] = (byte)(bits >> 8);
/*  759 */     } else if (shiftto == 12) {
/*      */       
/*  761 */       throw new IllegalArgumentException("Last unit does not have enough valid bits");
/*      */     } 
/*  763 */     if (dp != dst.length) {
/*  764 */       byte[] arrayOfByte = new byte[dp];
/*  765 */       System.arraycopy(dst, 0, arrayOfByte, 0, Math.min(dst.length, dp));
/*  766 */       dst = arrayOfByte;
/*      */     } 
/*  768 */     return dst;
/*      */   }
/*      */   
/*      */   public static String subMiddleStr(String data, String leftStr, String rightStr) {
/*  772 */     int leftIndex = data.indexOf(leftStr);
/*  773 */     leftIndex += leftStr.length();
/*  774 */     int rightIndex = data.indexOf(rightStr, leftIndex);
/*  775 */     if (leftIndex != -1 && rightIndex != -1) {
/*  776 */       return data.substring(leftIndex, rightIndex);
/*      */     }
/*  778 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] getResourceAsByteArray(Class cl, String name) {
/*  785 */     InputStream inputStream = cl.getResourceAsStream(name);
/*  786 */     byte[] data = null;
/*  787 */     data = readInputStream(inputStream);
/*      */     try {
/*  789 */       inputStream.close();
/*  790 */     } catch (Exception e) {
/*  791 */       Log.error(e);
/*      */     } 
/*  793 */     return data;
/*      */   }
/*      */ 
/*      */   
/*      */   public static byte[] getResourceAsByteArray(Object o, String name) {
/*  798 */     return getResourceAsByteArray(o.getClass(), name);
/*      */   }
/*      */   
/*      */   public static boolean saveDataViewToCsv(Vector columnVector, Vector<Vector> dataRows, String saveFile) {
/*  802 */     boolean state = false;
/*      */     try {
/*  804 */       FileOutputStream fileOutputStream = new FileOutputStream(saveFile);
/*  805 */       int columnNum = columnVector.size();
/*  806 */       byte cob = 44;
/*  807 */       byte newLine = 10;
/*  808 */       int rowNum = dataRows.size();
/*      */ 
/*      */       
/*  811 */       StringBuilder builder = new StringBuilder(); int i;
/*  812 */       for (i = 0; i < columnNum - 1; i++) {
/*  813 */         Object object = columnVector.get(i);
/*  814 */         fileOutputStream.write(formatStringByCsv(object.toString()).getBytes());
/*  815 */         fileOutputStream.write(cob);
/*      */       } 
/*  817 */       Object valueObject = columnVector.get(columnNum - 1);
/*  818 */       fileOutputStream.write(formatStringByCsv(valueObject.toString()).getBytes());
/*  819 */       fileOutputStream.write(newLine);
/*  820 */       for (i = 0; i < rowNum; i++) {
/*  821 */         Vector row = dataRows.get(i);
/*  822 */         for (int j = 0; j < columnNum - 1; j++) {
/*  823 */           valueObject = row.get(j);
/*  824 */           fileOutputStream.write(formatStringByCsv(String.valueOf(valueObject)).getBytes());
/*  825 */           fileOutputStream.write(cob);
/*      */         } 
/*  827 */         valueObject = row.get(columnNum - 1);
/*  828 */         fileOutputStream.write(formatStringByCsv(String.valueOf(valueObject)).getBytes());
/*  829 */         fileOutputStream.write(newLine);
/*      */       } 
/*  831 */       fileOutputStream.close();
/*  832 */       state = true;
/*  833 */     } catch (Exception e) {
/*  834 */       e.printStackTrace();
/*      */     } 
/*  836 */     return state;
/*      */   }
/*      */   
/*      */   public static String stringToUnicode(String unicode) {
/*  840 */     char[] chars = unicode.toCharArray();
/*  841 */     StringBuilder builder = new StringBuilder();
/*  842 */     for (int i = 0; i < chars.length; i++) {
/*      */       
/*  844 */       builder.append("\\u");
/*  845 */       String hx = Integer.toString(chars[i], 16);
/*  846 */       if (hx.length() < 4) {
/*  847 */         builder.append("0000".substring(hx.length())).append(hx);
/*      */       } else {
/*  849 */         builder.append(hx);
/*      */       } 
/*      */     } 
/*  852 */     return builder.toString();
/*      */   }
/*      */   
/*      */   public static String unicodeToString(String s) {
/*  856 */     char[] chars = s.toCharArray();
/*  857 */     StringBuilder stringBuilder = new StringBuilder();
/*  858 */     StringBuilder temBuilder = null;
/*  859 */     int index = 0;
/*  860 */     boolean isUn = false;
/*  861 */     char currentChar = '0';
/*  862 */     char nextChar = '0';
/*  863 */     char[] temChars = new char[4];
/*      */     
/*  865 */     String temStr = null;
/*  866 */     while (index < chars.length) {
/*  867 */       currentChar = chars[index];
/*  868 */       index++;
/*  869 */       if (currentChar == '\\') {
/*  870 */         temBuilder = new StringBuilder();
/*  871 */         temBuilder.append('\\');
/*  872 */         while (index + 1 < chars.length) {
/*  873 */           nextChar = chars[index];
/*  874 */           index++;
/*  875 */           if (nextChar == '\\') {
/*  876 */             index--;
/*  877 */             stringBuilder.append(temBuilder.toString());
/*      */             break;
/*      */           } 
/*  880 */           temBuilder.append(nextChar);
/*  881 */           if (nextChar == 'u') {
/*  882 */             isUn = true; continue;
/*      */           } 
/*  884 */           if (isUn) {
/*  885 */             if (index + 3 - 1 < chars.length) {
/*  886 */               temChars[0] = nextChar;
/*  887 */               temChars[1] = chars[index];
/*  888 */               index++;
/*  889 */               temChars[2] = chars[index];
/*  890 */               index++;
/*  891 */               temChars[3] = chars[index];
/*  892 */               index++;
/*  893 */               temStr = new String(temChars);
/*  894 */               temBuilder.append(temStr, 1, temChars.length);
/*  895 */               for (int i = 0; i < temChars.length; i++) {
/*  896 */                 char fixChar = temChars[i];
/*  897 */                 if ((fixChar >= '0' && fixChar <= '9') || (fixChar >= 'A' && fixChar <= 'F') || (fixChar >= 'a' && fixChar <= 'f')) {
/*  898 */                   isUn = true;
/*      */                 } else {
/*  900 */                   isUn = false;
/*      */                   break;
/*      */                 } 
/*      */               } 
/*  904 */               if (isUn) {
/*  905 */                 stringBuilder.append((char)Integer.parseInt(new String(temChars), 16));
/*  906 */                 isUn = false;
/*      */                 continue;
/*      */               } 
/*  909 */               stringBuilder.append(temBuilder.toString());
/*      */               
/*      */               continue;
/*      */             } 
/*  913 */             isUn = false;
/*  914 */             stringBuilder.append(temBuilder.toString());
/*      */             
/*      */             continue;
/*      */           } 
/*  918 */           isUn = false;
/*  919 */           stringBuilder.append(temBuilder.toString());
/*      */         } 
/*      */         
/*      */         continue;
/*      */       } 
/*  924 */       stringBuilder.append(currentChar);
/*      */     } 
/*      */     
/*  927 */     return stringBuilder.toString();
/*      */   }
/*      */   
/*      */   public static boolean sleep(int time) {
/*  931 */     boolean state = false;
/*      */     try {
/*  933 */       Thread.sleep(time);
/*  934 */       state = true;
/*  935 */     } catch (InterruptedException e) {
/*  936 */       Log.error(e);
/*      */     } 
/*  938 */     return state;
/*      */   }
/*      */   
/*      */   public static String toString(Object object) {
/*  942 */     return (object == null) ? "null" : object.toString();
/*      */   }
/*      */   
/*      */   public static String getLastFileName(String file) {
/*  946 */     String[] fs = formatDir(file).split("/");
/*  947 */     return fs[fs.length - 1];
/*      */   }
/*      */   
/*      */   private static String formatStringByCsv(String string) {
/*  951 */     string = string.replace("\"", "\"\"");
/*  952 */     return "\"" + string + "\"";
/*      */   }
/*      */ 
/*      */   
/*      */   public static int byteToInt2(byte[] b) {
/*  957 */     int mask = 255;
/*  958 */     int temp = 0;
/*  959 */     int n = 0;
/*  960 */     for (int i = 0; i < b.length; i++) {
/*  961 */       n <<= 8;
/*  962 */       temp = b[i] & mask;
/*  963 */       n |= temp;
/*      */     } 
/*  965 */     return n;
/*      */   }
/*      */   
/*      */   public static int bytesToInt(byte[] bytes) {
/*  969 */     int i = bytes[0] & 0xFF | (bytes[1] & 0xFF) << 8 | (bytes[2] & 0xFF) << 16 | (bytes[3] & 0xFF) << 24;
/*      */     
/*  971 */     return i;
/*      */   }
/*      */ 
/*      */   
/*      */   public static byte[] gzipE(byte[] data) {
/*      */     try {
/*  977 */       ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
/*      */       
/*  979 */       GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream);
/*      */       
/*  981 */       gzipOutputStream.write(data);
/*      */       
/*  983 */       gzipOutputStream.close();
/*      */       
/*  985 */       return outputStream.toByteArray();
/*  986 */     } catch (Exception e) {
/*  987 */       throw new RuntimeException(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   public static byte[] gzipD(byte[] data) {
/*  992 */     if (data.length == 0) {
/*  993 */       return data;
/*      */     }
/*      */     try {
/*  996 */       ByteArrayInputStream tStream = new ByteArrayInputStream(data);
/*  997 */       GZIPInputStream inputStream = new GZIPInputStream(tStream, data.length);
/*  998 */       return readInputStream(inputStream);
/*  999 */     } catch (Exception e) {
/* 1000 */       if (data.length < 200) {
/* 1001 */         Log.error(new String(data));
/*      */       }
/* 1003 */       throw new RuntimeException(e);
/*      */     } 
/*      */   }
/*      */   public static int randomInt(int max, int min) {
/* 1007 */     return min + (int)(Math.random() * (max - min + 1));
/*      */   }
/*      */   public static void openBrowseUrl(String url) {
/* 1010 */     if (Desktop.isDesktopSupported()) {
/*      */       
/*      */       try {
/* 1013 */         URI uri = URI.create(url);
/*      */         
/* 1015 */         Desktop dp = Desktop.getDesktop();
/*      */         
/* 1017 */         if (dp.isSupported(Desktop.Action.BROWSE))
/*      */         {
/* 1019 */           dp.browse(uri);
/*      */         }
/*      */       }
/* 1022 */       catch (Exception e) {
/* 1023 */         e.printStackTrace();
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static String joinCmdArgs(String[] commands) {
/* 1029 */     StringBuilder cmd = new StringBuilder();
/* 1030 */     boolean flag = false;
/* 1031 */     for (String s : commands) {
/* 1032 */       if (flag) {
/* 1033 */         cmd.append(' ');
/*      */       } else {
/* 1035 */         flag = true;
/*      */       } 
/*      */       
/* 1038 */       if (s.indexOf(' ') >= 0 || s.indexOf('\t') >= 0) {
/* 1039 */         if (s.charAt(0) != '"') {
/* 1040 */           cmd.append('"').append(s);
/*      */           
/* 1042 */           if (s.endsWith("\\")) {
/* 1043 */             cmd.append("\\");
/*      */           }
/* 1045 */           cmd.append('"');
/*      */         } else {
/* 1047 */           cmd.append(s);
/*      */         } 
/*      */       } else {
/* 1050 */         cmd.append(s);
/*      */       } 
/*      */     } 
/*      */     
/* 1054 */     return cmd.toString();
/*      */   }
/*      */   
/*      */   public static String[] SplitArgs(String input) {
/* 1058 */     return SplitArgs(input, 2147483647, false);
/*      */   }
/*      */   
/*      */   public static String[] SplitArgs(String input, int maxParts, boolean removeAllEscapeSequences) {
/* 1062 */     StringBuilder chars = new StringBuilder(input.trim());
/*      */ 
/*      */     
/* 1065 */     List<String> fragments = new ArrayList<>();
/*      */     
/* 1067 */     int parts = 0;
/* 1068 */     int nextFragmentStart = 0;
/* 1069 */     boolean inBounds = false;
/*      */     
/* 1071 */     for (int i = 0; i < chars.length(); i++) {
/*      */       
/* 1073 */       char c = chars.charAt(i);
/* 1074 */       if (c == '\\') {
/*      */         
/* 1076 */         if (removeAllEscapeSequences || (i + 1 < chars.length() && isEscapeable(chars.charAt(i + 1))))
/*      */         {
/*      */ 
/*      */           
/* 1080 */           chars.deleteCharAt(i);
/*      */ 
/*      */         
/*      */         }
/*      */       
/*      */       }
/* 1086 */       else if (c == '"' && (!inBounds ? (i == nextFragmentStart) : (i + 1 == chars
/*      */         
/* 1088 */         .length() || isSpace(chars.charAt(i + 1))))) {
/*      */         
/* 1090 */         inBounds = !inBounds;
/* 1091 */         chars.deleteCharAt(i);
/* 1092 */         i--;
/*      */ 
/*      */       
/*      */       }
/* 1096 */       else if (!inBounds) {
/*      */ 
/*      */ 
/*      */         
/* 1100 */         if (isSpace(c)) {
/*      */           
/* 1102 */           AddFragment(fragments, chars, nextFragmentStart, i);
/* 1103 */           nextFragmentStart = i + 1;
/*      */ 
/*      */           
/* 1106 */           if (++parts + 1 >= maxParts) {
/*      */             break;
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/* 1112 */     if (nextFragmentStart < chars.length()) {
/* 1113 */       AddFragment(fragments, chars, nextFragmentStart, -1);
/*      */     }
/*      */     
/* 1116 */     return fragments.<String>toArray(new String[0]);
/*      */   }
/*      */   
/*      */   private static boolean isSpace(char c) {
/* 1120 */     return (c == ' ' || c == '\t');
/*      */   }
/*      */   
/*      */   private static boolean isEscapeable(char c) {
/* 1124 */     switch (c) {
/*      */     
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1131 */     return false;
/*      */   }
/*      */   
/*      */   public static LinkedList<String> stringToIps(String str) {
/* 1135 */     LinkedList<String> ips = new LinkedList<>();
/* 1136 */     String[] strIps = str.split("\n");
/*      */     String[] array;
/* 1138 */     for (int length = (array = strIps).length, i = 0; i < length; i++) {
/* 1139 */       String stringa = array[i];
/* 1140 */       String string = stringa.trim();
/* 1141 */       if (isIPv4LiteralAddress(string)) {
/* 1142 */         ips.add(string);
/*      */       }
/* 1144 */       else if (string.lastIndexOf("-") != -1) {
/* 1145 */         String[] iph = string.split("-");
/* 1146 */         if (isIPv4LiteralAddress(iph[0])) {
/* 1147 */           String x = iph[0];
/* 1148 */           String[] ipx = x.split("\\.");
/* 1149 */           for (Integer start = Integer.valueOf(Integer.parseInt(ipx[3])), end = Integer.valueOf(Integer.parseInt(iph[1])); start.intValue() <= end.intValue(); start = Integer.valueOf(start.intValue() + 1)) {
/* 1150 */             String ip = String.valueOf(ipx[0]) + "." + ipx[1] + "." + ipx[2] + "." + start.toString();
/* 1151 */             ips.add(ip);
/*      */           }
/*      */         
/*      */         } 
/* 1155 */       } else if (string.lastIndexOf("/") != -1) {
/* 1156 */         String[] iph = string.split("/");
/* 1157 */         if (isIPv4LiteralAddress(iph[0])) {
/* 1158 */           Integer mask = Integer.valueOf(Integer.parseInt(iph[1]));
/* 1159 */           if (mask.intValue() <= 32 && mask.intValue() >= 1) {
/* 1160 */             ips.addAll(maskToIps(iph[0], mask));
/*      */           }
/*      */         } else {
/*      */           
/*      */           try {
/* 1165 */             String ip2 = InetAddress.getByName(iph[0]).getHostAddress();
/* 1166 */             Integer mask2 = Integer.valueOf(Integer.parseInt(iph[1]));
/* 1167 */             if (mask2.intValue() <= 32 && mask2.intValue() >= 1) {
/* 1168 */               ips.addAll(maskToIps(ip2, mask2));
/*      */             }
/*      */           }
/* 1171 */           catch (Exception e) {
/* 1172 */             Log.error(e);
/*      */           }
/*      */         
/*      */         } 
/* 1176 */       } else if (!string.equals("")) {
/* 1177 */         ips.add(string);
/*      */       } 
/*      */     } 
/* 1180 */     return ips;
/*      */   }
/*      */   
/*      */   public static LinkedList<String> maskToIps(String ip, Integer m) {
/* 1184 */     LinkedList<String> i = new LinkedList<>();
/*      */     try {
/* 1186 */       InetAddress inetAddress = InetAddress.getByName(ip);
/* 1187 */       int address = inetAddress.hashCode();
/* 1188 */       Integer n = Integer.valueOf(32 - m.intValue());
/* 1189 */       int startIp = address & -1 << n.intValue();
/* 1190 */       int endIp = address | -1 >>> m.intValue();
/* 1191 */       startIp++;
/* 1192 */       endIp--;
/* 1193 */       while (startIp <= endIp) {
/* 1194 */         byte[] startaddr = getAddress(startIp);
/* 1195 */         InetAddress from = InetAddress.getByAddress(startaddr);
/* 1196 */         String fromIp = from.getHostAddress();
/* 1197 */         i.add(fromIp);
/* 1198 */         startIp++;
/*      */       } 
/* 1200 */     } catch (Exception e) {
/* 1201 */       Log.error(e);
/*      */     } 
/* 1203 */     return i;
/*      */   }
/*      */   
/*      */   public static byte[] getAddress(int intIp) {
/* 1207 */     int address = intIp;
/* 1208 */     byte[] addr = { (byte)(address >>> 24 & 0xFF), (byte)(address >>> 16 & 0xFF), (byte)(address >>> 8 & 0xFF), (byte)(address & 0xFF) };
/* 1209 */     return addr;
/*      */   }
/*      */   
/*      */   public static LinkedList<Integer> stringToPorts(String str) {
/* 1213 */     String[] ports = str.split(",");
/* 1214 */     HashSet<Integer> portset = new HashSet<>();
/*      */     String[] array;
/* 1216 */     for (int length = (array = ports).length, i = 0; i < length; i++) {
/* 1217 */       String stringa = array[i];
/* 1218 */       String string = stringa.trim();
/* 1219 */       if (string.lastIndexOf("-") != -1) {
/* 1220 */         String[] strPorts = string.split("-");
/* 1221 */         for (Integer startPort = Integer.valueOf(Integer.parseInt(strPorts[0])), endPort = Integer.valueOf(Integer.parseInt(strPorts[1])); startPort.intValue() <= endPort.intValue(); startPort = Integer.valueOf(startPort.intValue() + 1)) {
/* 1222 */           if (startPort.intValue() >= 0 && startPort.intValue() <= 65535) {
/* 1223 */             portset.add(startPort);
/*      */           }
/*      */         } 
/*      */       } else {
/*      */         
/*      */         try {
/* 1229 */           Integer port = Integer.valueOf(Integer.parseInt(string));
/* 1230 */           if (port.intValue() >= 0 && port.intValue() <= 65535) {
/* 1231 */             portset.add(port);
/*      */           }
/*      */         }
/* 1234 */         catch (Exception exception) {}
/*      */       } 
/*      */     } 
/* 1237 */     LinkedList<Integer> portList = new LinkedList<>(portset);
/* 1238 */     return portList;
/*      */   }
/*      */   
/*      */   public static byte[] textToNumericFormatV4(String src) {
/* 1242 */     byte[] res = new byte[4];
/*      */     
/* 1244 */     long tmpValue = 0L;
/* 1245 */     int currByte = 0;
/* 1246 */     boolean newOctet = true;
/*      */     
/* 1248 */     int len = src.length();
/* 1249 */     if (len == 0 || len > 15) {
/* 1250 */       return null;
/*      */     }
/* 1252 */     for (int i = 0; i < len; i++) {
/* 1253 */       char c = src.charAt(i);
/* 1254 */       if (c == '.') {
/* 1255 */         if (newOctet || tmpValue < 0L || tmpValue > 255L || currByte == 3) {
/* 1256 */           return null;
/*      */         }
/* 1258 */         res[currByte++] = (byte)(int)(tmpValue & 0xFFL);
/* 1259 */         tmpValue = 0L;
/* 1260 */         newOctet = true;
/*      */       } else {
/* 1262 */         int digit = Character.digit(c, 10);
/* 1263 */         if (digit < 0) {
/* 1264 */           return null;
/*      */         }
/* 1266 */         tmpValue *= 10L;
/* 1267 */         tmpValue += digit;
/* 1268 */         newOctet = false;
/*      */       } 
/*      */     } 
/* 1271 */     if (newOctet || tmpValue < 0L || tmpValue >= 1L << (4 - currByte) * 8) {
/* 1272 */       return null;
/*      */     }
/* 1274 */     switch (currByte) {
/*      */       case 0:
/* 1276 */         res[0] = (byte)(int)(tmpValue >> 24L & 0xFFL);
/*      */       case 1:
/* 1278 */         res[1] = (byte)(int)(tmpValue >> 16L & 0xFFL);
/*      */       case 2:
/* 1280 */         res[2] = (byte)(int)(tmpValue >> 8L & 0xFFL);
/*      */       case 3:
/* 1282 */         res[3] = (byte)(int)(tmpValue >> 0L & 0xFFL); break;
/*      */     } 
/* 1284 */     return res;
/*      */   }
/*      */   public static boolean isIPv4LiteralAddress(String src) {
/* 1287 */     return (textToNumericFormatV4(src) != null);
/*      */   }
/*      */   
/*      */   private static void AddFragment(List<String> fragments, StringBuilder chars, int start, int end) {
/* 1291 */     if (end <= start && end >= 0) {
/*      */       return;
/*      */     }
/* 1294 */     if (end < 0) {
/* 1295 */       end = chars.length();
/*      */     }
/* 1297 */     String fragment = chars.substring(start, end);
/* 1298 */     fragments.add(fragment);
/*      */   }
/*      */   public static void dup2(InputStream inputStream, OutputStream outputStream) throws Exception {
/* 1301 */     byte[] readData = new byte[5120];
/* 1302 */     int readSize = -1;
/* 1303 */     while ((readSize = inputStream.read(readData)) != -1) {
/* 1304 */       outputStream.write(readData, 0, readSize);
/* 1305 */       Thread.sleep(10L);
/*      */     } 
/*      */   }
/*      */   
/*      */   public static String printStackTrace(Throwable e) {
/* 1310 */     ByteArrayOutputStream stream = new ByteArrayOutputStream();
/* 1311 */     PrintStream printStream = new PrintStream(stream);
/* 1312 */     e.printStackTrace(printStream);
/* 1313 */     printStream.flush();
/* 1314 */     printStream.close();
/* 1315 */     return new String(stream.toByteArray());
/*      */   }
/*      */   public static File getCurrentJarFile() {
/* 1318 */     String jarFileString = getJarFileByClass(ApplicationContext.class);
/* 1319 */     if (jarFileString != null) {
/* 1320 */       return new File(jarFileString);
/*      */     }
/* 1322 */     return null;
/*      */   }
/*      */   
/*      */   public static byte[] httpReqest(String urlString, String method, HashMap<String, String> headers, byte[] data) {
/* 1326 */     byte[] result = null;
/*      */     
/*      */     try {
/* 1329 */       URL url = new URL(urlString);
/*      */       
/* 1331 */       HttpURLConnection httpConn = (HttpURLConnection)url.openConnection();
/* 1332 */       httpConn.setDoInput(true);
/* 1333 */       httpConn.setDoOutput(!"GET".equals(method.toUpperCase()));
/* 1334 */       httpConn.setConnectTimeout(3000);
/* 1335 */       httpConn.setReadTimeout(3000);
/* 1336 */       httpConn.setRequestMethod(method.toUpperCase());
/* 1337 */       Http.addHttpHeader(httpConn, headers);
/* 1338 */       if (httpConn.getDoOutput() && data != null) {
/* 1339 */         httpConn.getOutputStream().write(data);
/*      */       }
/* 1341 */       InputStream inputStream = httpConn.getInputStream();
/* 1342 */       result = readInputStream(inputStream);
/* 1343 */     } catch (Exception e) {
/* 1344 */       Log.error(e);
/*      */     } 
/*      */     
/* 1347 */     return result;
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar\\util\functions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */