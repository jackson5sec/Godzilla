/*     */ package core;
/*     */ import com.formdev.flatlaf.demo.intellijthemes.IJThemeInfo;
/*     */ import com.formdev.flatlaf.demo.intellijthemes.IJThemesPanel;
/*     */ import com.httpProxy.server.CertUtil;
/*     */ import core.annotation.CryptionAnnotation;
/*     */ import core.annotation.PayloadAnnotation;
/*     */ import core.annotation.PluginAnnotation;
/*     */ import core.imp.Cryption;
/*     */ import core.imp.Payload;
/*     */ import core.imp.Plugin;
/*     */ import core.shell.ShellEntity;
/*     */ import java.awt.Font;
/*     */ import java.awt.Toolkit;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.File;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.net.Proxy;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.security.KeyPair;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.security.spec.PKCS8EncodedKeySpec;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarFile;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.FontUIResource;
/*     */ import util.Log;
/*     */ import util.functions;
/*     */ import util.http.Http;
/*     */ 
/*     */ public class ApplicationContext {
/*     */   public static final String VERSION = "4.01";
/*     */   private static final HashMap<String, Class<?>> payloadMap;
/*  47 */   public static int windowWidth = (Toolkit.getDefaultToolkit().getScreenSize()).width; private static final HashMap<String, HashMap<String, Class<?>>> cryptionMap; private static final HashMap<String, HashMap<String, Class<?>>> pluginMap; private static File[] pluginJarFiles;
/*  48 */   public static int windowsHeight = (Toolkit.getDefaultToolkit().getScreenSize()).height;
/*     */   
/*  50 */   public static ThreadLocal<Boolean> isShowHttpProgressBar = new ThreadLocal<>();
/*     */   
/*  52 */   public static final CoreClassLoader PLUGIN_CLASSLOADER = new CoreClassLoader(ApplicationContext.class.getClassLoader());
/*     */ 
/*     */   
/*     */   public static boolean easterEgg = true;
/*     */   
/*     */   private static Font font;
/*     */   
/*     */   private static Map<String, String> headerMap;
/*     */ 
/*     */   
/*     */   static {
/*  63 */     payloadMap = new HashMap<>();
/*  64 */     cryptionMap = new HashMap<>();
/*  65 */     pluginMap = new HashMap<>();
/*     */   }
/*     */   public static void init() {
/*  68 */     initFont();
/*  69 */     initHttpHeader();
/*  70 */     scanPluginJar();
/*  71 */     scanPayload();
/*  72 */     scanCryption();
/*  73 */     scanPlugin();
/*     */   }
/*     */ 
/*     */   
/*     */   private static void initFont() {
/*  78 */     String fontName = Db.getSetingValue("font-name");
/*  79 */     String fontType = Db.getSetingValue("font-type");
/*  80 */     String fontSize = Db.getSetingValue("font-size");
/*  81 */     if (fontName != null && fontType != null && fontSize != null) {
/*  82 */       font = new Font(fontName, Integer.parseInt(fontType), Integer.parseInt(fontSize));
/*  83 */       InitGlobalFont(font);
/*     */     } 
/*     */   }
/*     */   private static void initHttpHeader() {
/*  87 */     String headerString = getGloballHttpHeader();
/*  88 */     if (headerString != null) {
/*  89 */       String[] reqLines = headerString.split("\n");
/*     */ 
/*     */ 
/*     */       
/*  93 */       headerMap = new Hashtable<>();
/*  94 */       for (int i = 0; i < reqLines.length; i++) {
/*  95 */         if (!reqLines[i].trim().isEmpty()) {
/*  96 */           int index = reqLines[i].indexOf(":");
/*  97 */           if (index > 1) {
/*  98 */             String keyName = reqLines[i].substring(0, index).trim();
/*  99 */             String keyValue = reqLines[i].substring(index + 1).trim();
/* 100 */             headerMap.put(keyName, keyValue);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private static void scanPayload() {
/*     */     try {
/* 108 */       URL url = ApplicationContext.class.getResource("/shells/payloads/");
/* 109 */       ArrayList<Class<?>> destList = new ArrayList<>();
/* 110 */       int loadNum = scanClass(url.toURI(), "shells.payloads", Payload.class, PayloadAnnotation.class, destList);
/* 111 */       destList.forEach(t -> {
/*     */             try {
/*     */               Annotation annotation = (Annotation)t.getAnnotation(PayloadAnnotation.class);
/*     */               String name = (String)annotation.annotationType().getMethod("Name", new Class[0]).invoke(annotation, null);
/*     */               payloadMap.put(name, t);
/*     */               cryptionMap.put(name, new HashMap<>());
/*     */               pluginMap.put(name, new HashMap<>());
/* 118 */             } catch (Exception e) {
/*     */               Log.error(e);
/*     */             } 
/*     */           });
/* 122 */       Log.log(String.format("load payload success! payloadMaxNum:%s onceLoadPayloadNum:%s", new Object[] { Integer.valueOf(payloadMap.size()), Integer.valueOf(loadNum) }), new Object[0]);
/* 123 */     } catch (Exception e) {
/* 124 */       Log.error(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void scanCryption() {
/*     */     try {
/* 130 */       URL url = ApplicationContext.class.getResource("/shells/cryptions/");
/* 131 */       ArrayList<Class<?>> destList = new ArrayList<>();
/* 132 */       int loadNum = scanClass(url.toURI(), "shells.cryptions", Cryption.class, CryptionAnnotation.class, destList);
/* 133 */       int pluginMaxNum = 0;
/* 134 */       destList.forEach(t -> {
/*     */             try {
/*     */               Annotation annotation = (Annotation)t.getAnnotation(CryptionAnnotation.class);
/*     */               String name = (String)annotation.annotationType().getMethod("Name", new Class[0]).invoke(annotation, null);
/*     */               String payloadName = (String)annotation.annotationType().getMethod("payloadName", new Class[0]).invoke(annotation, null);
/*     */               HashMap<String, Class<?>> destMap = cryptionMap.get(payloadName);
/*     */               if (destMap == null) {
/*     */                 cryptionMap.put(payloadName, new HashMap<>());
/*     */                 destMap = cryptionMap.get(payloadName);
/*     */               } 
/*     */               destMap.put(name, t);
/* 145 */             } catch (Exception e) {
/*     */               e.printStackTrace();
/*     */               Log.error(e);
/*     */             } 
/*     */           });
/* 150 */       Iterator<String> iterator = cryptionMap.keySet().iterator();
/* 151 */       while (iterator.hasNext()) {
/* 152 */         String keyString = iterator.next();
/* 153 */         HashMap<String, Class<?>> map = cryptionMap.get(keyString);
/* 154 */         if (map != null) {
/* 155 */           pluginMaxNum += map.size();
/*     */         }
/*     */       } 
/* 158 */       Log.log(String.format("load cryption success! cryptionMaxNum:%s onceLoadCryptionNum:%s", new Object[] { Integer.valueOf(pluginMaxNum), Integer.valueOf(loadNum) }), new Object[0]);
/* 159 */     } catch (Exception e) {
/* 160 */       Log.error(e);
/*     */     } 
/*     */   }
/*     */   private static void scanPlugin() {
/*     */     try {
/* 165 */       URL url = ApplicationContext.class.getResource("/shells/plugins/");
/* 166 */       ArrayList<Class<?>> destList = new ArrayList<>();
/* 167 */       int loadNum = scanClass(url.toURI(), "shells.plugins", Plugin.class, PluginAnnotation.class, destList);
/* 168 */       int pluginMaxNum = 0;
/* 169 */       destList.forEach(t -> {
/*     */             try {
/*     */               Annotation annotation = (Annotation)t.getAnnotation(PluginAnnotation.class);
/*     */               String name = (String)annotation.annotationType().getMethod("Name", new Class[0]).invoke(annotation, null);
/*     */               String payloadName = (String)annotation.annotationType().getMethod("payloadName", new Class[0]).invoke(annotation, null);
/*     */               HashMap<String, Class<?>> destMap = pluginMap.get(payloadName);
/*     */               if (destMap == null) {
/*     */                 pluginMap.put(payloadName, new HashMap<>());
/*     */                 destMap = pluginMap.get(payloadName);
/*     */               } 
/*     */               destMap.put(name, t);
/* 180 */             } catch (Exception e) {
/*     */               Log.error(e);
/*     */             } 
/*     */           });
/* 184 */       Iterator<String> iterator = pluginMap.keySet().iterator();
/* 185 */       while (iterator.hasNext()) {
/* 186 */         String keyString = iterator.next();
/* 187 */         HashMap<String, Class<?>> map = pluginMap.get(keyString);
/* 188 */         if (map != null) {
/* 189 */           pluginMaxNum += map.size();
/*     */         }
/*     */       } 
/* 192 */       Log.log(String.format("load plugin success! pluginMaxNum:%s onceLoadPluginNum:%s", new Object[] { Integer.valueOf(pluginMaxNum), Integer.valueOf(loadNum) }), new Object[0]);
/* 193 */     } catch (Exception e) {
/* 194 */       Log.error(e);
/*     */     } 
/*     */   }
/*     */   private static void scanPluginJar() {
/* 198 */     String[] pluginJars = Db.getAllPlugin();
/* 199 */     ArrayList<File> list = new ArrayList();
/*     */     
/* 201 */     for (int i = 0; i < pluginJars.length; i++) {
/* 202 */       File jarFile = new File(pluginJars[i]);
/* 203 */       if (jarFile.exists() && jarFile.isFile()) {
/* 204 */         addJar(jarFile);
/* 205 */         list.add(jarFile);
/*     */       } else {
/* 207 */         Log.error(String.format("PluginJarFile : %s no found", new Object[] { pluginJars[i] }));
/*     */       } 
/*     */     } 
/* 210 */     pluginJarFiles = list.<File>toArray(new File[0]);
/* 211 */     Log.log(String.format("load pluginJar success! pluginJarNum:%s LoadPluginJarSuccessNum:%s", new Object[] { Integer.valueOf(pluginJars.length), Integer.valueOf(pluginJars.length) }), new Object[0]);
/*     */   }
/*     */   private static int scanClass(URI uri, String packageName, Class<?> parentClass, Class<?> annotationClass, ArrayList<Class<?>> destList) {
/* 214 */     int num = scanClassX(uri, packageName, parentClass, annotationClass, destList);
/*     */     
/* 216 */     for (int i = 0; i < pluginJarFiles.length; i++) {
/* 217 */       File jarFile = pluginJarFiles[i];
/* 218 */       num += scanClassByJar(jarFile, packageName, parentClass, annotationClass, destList);
/*     */     } 
/*     */     
/* 221 */     return num;
/*     */   }
/*     */   private static int scanClassX(URI uri, String packageName, Class<?> parentClass, Class<?> annotationClass, ArrayList<Class<?>> destList) {
/*     */     String jarFileString;
/* 225 */     if ((jarFileString = functions.getJarFileByClass(ApplicationContext.class)) != null) {
/* 226 */       return scanClassByJar(new File(jarFileString), packageName, parentClass, annotationClass, destList);
/*     */     }
/* 228 */     int addNum = 0;
/*     */     try {
/* 230 */       File file = new File(uri);
/* 231 */       File[] file2 = file.listFiles();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 239 */       for (int i = 0; i < file2.length; i++) {
/* 240 */         File objectFile = file2[i];
/* 241 */         if (objectFile.isDirectory()) {
/* 242 */           File[] objectFiles = objectFile.listFiles();
/* 243 */           for (int j = 0; j < objectFiles.length; j++) {
/* 244 */             File objectClassFile = objectFiles[j];
/* 245 */             if (objectClassFile.getPath().endsWith(".class")) {
/*     */               try {
/* 247 */                 String objectClassName = String.format("%s.%s.%s", new Object[] { packageName, objectFile.getName(), objectClassFile.getName().substring(0, objectClassFile.getName().length() - ".class".length()) });
/* 248 */                 Class<?> objectClass = Class.forName(objectClassName, true, PLUGIN_CLASSLOADER);
/* 249 */                 if (parentClass.isAssignableFrom(objectClass) && objectClass.isAnnotationPresent((Class)annotationClass)) {
/* 250 */                   destList.add(objectClass);
/* 251 */                   addNum++;
/*     */                 } 
/* 253 */               } catch (Exception e) {
/* 254 */                 Log.error(e);
/*     */               } 
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/* 260 */     } catch (Exception e) {
/* 261 */       Log.error(e);
/*     */     } 
/* 263 */     return addNum;
/*     */   }
/*     */   private static int scanClassByJar(File srcJarFile, String packageName, Class<?> parentClass, Class<?> annotationClass, ArrayList<Class<?>> destList) {
/* 266 */     int addNum = 0;
/*     */     try {
/* 268 */       JarFile jarFile = new JarFile(srcJarFile);
/* 269 */       Enumeration<JarEntry> jarFiles = jarFile.entries();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 275 */       packageName = packageName.replace(".", "/");
/* 276 */       while (jarFiles.hasMoreElements()) {
/* 277 */         JarEntry jarEntry = jarFiles.nextElement();
/* 278 */         String name = jarEntry.getName();
/* 279 */         if (name.startsWith(packageName) && name.endsWith(".class")) {
/* 280 */           name = name.replace("/", ".");
/* 281 */           name = name.substring(0, name.length() - 6);
/*     */           try {
/* 283 */             String objectClassName = name;
/* 284 */             Class<?> objectClass = Class.forName(objectClassName, true, PLUGIN_CLASSLOADER);
/* 285 */             if (parentClass.isAssignableFrom(objectClass) && objectClass.isAnnotationPresent((Class)annotationClass)) {
/* 286 */               destList.add(objectClass);
/* 287 */               addNum++;
/*     */             } 
/* 289 */           } catch (Exception e) {
/* 290 */             Log.error(e);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 295 */       jarFile.close();
/* 296 */     } catch (Exception e) {
/* 297 */       Log.error(e);
/*     */     } 
/* 299 */     return addNum;
/*     */   }
/*     */   public static String[] getAllPayload() {
/* 302 */     Set<String> keys = payloadMap.keySet();
/* 303 */     return keys.<String>toArray(new String[0]);
/*     */   }
/*     */   public static Payload getPayload(String payloadName) {
/* 306 */     Class<?> payloadClass = payloadMap.get(payloadName);
/* 307 */     Payload payload = null;
/* 308 */     if (payloadClass != null) {
/*     */       try {
/* 310 */         payload = (Payload)payloadClass.newInstance();
/* 311 */       } catch (Exception e) {
/* 312 */         Log.error(e);
/*     */       } 
/*     */     }
/* 315 */     return payload;
/*     */   }
/*     */   public static Plugin[] getAllPlugin(String payloadName) {
/* 318 */     HashMap<String, Class<?>> pluginSrcMap = pluginMap.get(payloadName);
/* 319 */     ArrayList<Plugin> list = new ArrayList<>();
/* 320 */     Class<?> payloadClass = payloadMap.get(payloadName);
/* 321 */     while (payloadClass != null && (payloadClass = payloadClass.getSuperclass()) != null) {
/* 322 */       if (payloadClass != null && payloadClass.isAnnotationPresent((Class)PayloadAnnotation.class)) {
/* 323 */         list.addAll(new CopyOnWriteArrayList<>(getAllPlugin(payloadClass)));
/*     */       }
/*     */     } 
/* 326 */     if (pluginSrcMap != null) {
/* 327 */       Iterator<String> keys = pluginSrcMap.keySet().iterator();
/*     */ 
/*     */       
/* 330 */       while (keys.hasNext()) {
/* 331 */         String cryptionName = keys.next();
/* 332 */         Class<?> pluginClass = pluginSrcMap.get(cryptionName);
/* 333 */         if (pluginClass != null) {
/* 334 */           PluginAnnotation pluginAnnotation = pluginClass.<PluginAnnotation>getAnnotation(PluginAnnotation.class);
/* 335 */           if (pluginAnnotation.payloadName().equals(payloadName)) {
/*     */             try {
/* 337 */               Plugin plugin = (Plugin)pluginClass.newInstance();
/* 338 */               list.add(plugin);
/* 339 */             } catch (Exception e) {
/* 340 */               Log.error(e);
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 348 */     return list.<Plugin>toArray(new Plugin[0]);
/*     */   }
/*     */   public static Plugin[] getAllPlugin(Class payloadClass) {
/* 351 */     Annotation annotation = (Annotation)payloadClass.getAnnotation(PayloadAnnotation.class);
/* 352 */     if (annotation != null) {
/* 353 */       PayloadAnnotation payloadAnnotation = (PayloadAnnotation)annotation;
/* 354 */       return getAllPlugin(payloadAnnotation.Name());
/*     */     } 
/* 356 */     return new Plugin[0];
/*     */   }
/*     */   public static String[] getAllCryption(String payloadName) {
/* 359 */     HashMap<String, Class<?>> cryptionSrcMap = cryptionMap.get(payloadName);
/* 360 */     ArrayList<String> list = new ArrayList<>();
/* 361 */     if (cryptionSrcMap != null) {
/* 362 */       Iterator<String> keys = cryptionSrcMap.keySet().iterator();
/*     */       
/* 364 */       while (keys.hasNext()) {
/* 365 */         String cryptionName = keys.next();
/* 366 */         Class<?> cryptionClass = cryptionSrcMap.get(cryptionName);
/* 367 */         if (cryptionClass != null) {
/* 368 */           CryptionAnnotation cryptionAnnotation = cryptionClass.<CryptionAnnotation>getAnnotation(CryptionAnnotation.class);
/* 369 */           if (cryptionAnnotation.payloadName().equals(payloadName)) {
/* 370 */             list.add(cryptionName);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 376 */     return list.<String>toArray(new String[0]);
/*     */   }
/*     */   public static Cryption getCryption(String payloadName, String crytionName) {
/* 379 */     HashMap<String, Class<?>> cryptionSrcMap = cryptionMap.get(payloadName);
/* 380 */     if (cryptionSrcMap != null) {
/* 381 */       Class<?> cryptionClass = cryptionSrcMap.get(crytionName);
/* 382 */       if (cryptionMap != null) {
/* 383 */         CryptionAnnotation cryptionAnnotation = cryptionClass.<CryptionAnnotation>getAnnotation(CryptionAnnotation.class);
/* 384 */         if (cryptionAnnotation.payloadName().equals(payloadName)) {
/* 385 */           Cryption cryption = null;
/*     */           try {
/* 387 */             cryption = (Cryption)cryptionClass.newInstance();
/* 388 */             return cryption;
/* 389 */           } catch (Exception e) {
/* 390 */             Log.error(e);
/* 391 */             return null;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 396 */     return null;
/*     */   }
/*     */   
/*     */   private static void addJar(File jarPath) {
/*     */     try {
/* 401 */       PLUGIN_CLASSLOADER.addJar(jarPath.toURI().toURL());
/* 402 */     } catch (Exception e) {
/* 403 */       Log.error(e);
/*     */     } 
/*     */   }
/*     */   private static void InitGlobalFont(Font font) {
/* 407 */     FontUIResource fontRes = new FontUIResource(font);
/* 408 */     Enumeration<Object> keys = UIManager.getDefaults().keys();
/* 409 */     while (keys.hasMoreElements()) {
/* 410 */       Object key = keys.nextElement();
/* 411 */       Object value = UIManager.get(key);
/* 412 */       if (value instanceof FontUIResource)
/* 413 */         UIManager.put(key, fontRes); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Proxy getProxy(ShellEntity shellContext) {
/* 418 */     return ProxyT.getProxy(shellContext);
/*     */   }
/*     */   public static String[] getAllProxy() {
/* 421 */     return ProxyT.getAllProxyType();
/*     */   }
/*     */   public static String[] getAllEncodingTypes() {
/* 424 */     return Encoding.getAllEncodingTypes();
/*     */   }
/*     */   public static Http getHttp(ShellEntity shellEntity) {
/* 427 */     Http httpx = new Http(shellEntity);
/* 428 */     return httpx;
/*     */   }
/*     */   public static Font getFont() {
/* 431 */     return font;
/*     */   }
/*     */   public static void setFont(Font font) {
/* 434 */     Db.updateSetingKV("font-name", font.getName());
/* 435 */     Db.updateSetingKV("font-type", Integer.toString(font.getStyle()));
/* 436 */     Db.updateSetingKV("font-size", Integer.toString(font.getSize()));
/* 437 */     ApplicationContext.font = font;
/*     */   }
/*     */   public static void resetFont() {
/* 440 */     Db.removeSetingK("font-name");
/* 441 */     Db.removeSetingK("font-type");
/* 442 */     Db.removeSetingK("font-size");
/*     */   }
/*     */   public static String getGloballHttpHeader() {
/* 445 */     return Db.getSetingValue("globallHttpHeader");
/*     */   }
/*     */   public static Map<String, String> getGloballHttpHeaderX() {
/* 448 */     return headerMap;
/*     */   }
/*     */   public static boolean updateGloballHttpHeader(String header) {
/* 451 */     boolean state = Db.updateSetingKV("globallHttpHeader", header);
/* 452 */     initHttpHeader();
/* 453 */     return state;
/*     */   }
/*     */   public static boolean isGodMode() {
/* 456 */     return Boolean.valueOf(Db.getSetingValue("godMode")).booleanValue();
/*     */   }
/*     */   public static boolean setGodMode(boolean state) {
/* 459 */     return Db.updateSetingKV("godMode", String.valueOf(state));
/*     */   }
/*     */   public static boolean isOpenC(String k) {
/* 462 */     return Boolean.valueOf(Db.getSetingValue(k)).booleanValue();
/*     */   }
/*     */   public static boolean setOpenC(String k, boolean state) {
/* 465 */     return Db.updateSetingKV(k, String.valueOf(state));
/*     */   }
/*     */   public static boolean isOpenCache() {
/* 468 */     return Db.getSetingBooleanValue("shellOpenCache", true);
/*     */   }
/*     */   public static boolean setOpenCache(boolean state) {
/* 471 */     return setOpenC("shellOpenCache", state);
/*     */   }
/*     */   public static void initUi() {
/* 474 */     if (SystemInfo.isMacOS && System.getProperty("apple.laf.useScreenMenuBar") == null) {
/* 475 */       System.setProperty("apple.laf.useScreenMenuBar", "true");
/*     */     }
/* 477 */     UIManager.put("Table.showHorizontalLines", Boolean.valueOf(true));
/* 478 */     UIManager.put("Table.showVerticalLines", Boolean.valueOf(true));
/* 479 */     JFrame.setDefaultLookAndFeelDecorated(true);
/* 480 */     JDialog.setDefaultLookAndFeelDecorated(true);
/*     */     
/* 482 */     String resourceNameString = Db.getSetingValue("ui-resourceName");
/* 483 */     String lafClassNameString = Db.getSetingValue("ui-lafClassName");
/* 484 */     if (resourceNameString == null && lafClassNameString == null) {
/* 485 */       Db.updateSetingKV("ui-lafClassName", "com.formdev.flatlaf.FlatIntelliJLaf");
/*     */     }
/* 487 */     lafClassNameString = Db.getSetingValue("ui-lafClassName");
/* 488 */     IJThemesPanel.setTheme(new IJThemeInfo(resourceNameString, lafClassNameString));
/*     */   }
/*     */   
/*     */   public static boolean saveUi(IJThemeInfo themeInfo) {
/*     */     try {
/* 493 */       String resourceNameString = themeInfo.getResourceName();
/* 494 */       String lafClassNameString = themeInfo.getLafClassName();
/* 495 */       if (resourceNameString != null && lafClassNameString == null) {
/* 496 */         Db.updateSetingKV("ui-resourceName", resourceNameString);
/* 497 */         Db.removeSetingK("ui-lafClassName");
/*     */       } 
/* 499 */       if (lafClassNameString != null && resourceNameString == null) {
/* 500 */         Db.updateSetingKV("ui-lafClassName", lafClassNameString);
/* 501 */         Db.removeSetingK("ui-resourceName");
/*     */       } 
/*     */       
/* 504 */       if (lafClassNameString == null && resourceNameString == null) {
/* 505 */         return false;
/*     */       }
/*     */     }
/* 508 */     catch (Exception e) {
/* 509 */       Log.error(e);
/* 510 */       return false;
/*     */     } 
/* 512 */     return true;
/*     */   }
/*     */   public static void genHttpsConfig() {
/*     */     try {
/* 516 */       KeyPair keyPair = CertUtil.genKeyPair();
/* 517 */       String base64HttpsCert = functions.base64EncodeToString(CertUtil.genCACert("C=CN, ST=GD, L=SZ, O=lee, OU=study, CN=HttpsProxy", new Date(), new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(3650L)), keyPair).getEncoded());
/* 518 */       String base64HttpsPrivateKey = functions.base64EncodeToString((new PKCS8EncodedKeySpec(keyPair.getPrivate().getEncoded())).getEncoded());
/* 519 */       Db.addSetingKV("HttpsPrivateKey", base64HttpsPrivateKey);
/* 520 */       Db.addSetingKV("HttpsCert", base64HttpsCert);
/* 521 */     } catch (Exception e) {
/* 522 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */   public static PrivateKey getHttpsPrivateKey() {
/*     */     try {
/* 527 */       String base64String = Db.getSetingValue("HttpsPrivateKey");
/* 528 */       if (base64String == null) {
/* 529 */         genHttpsConfig();
/*     */       }
/* 531 */       return CertUtil.loadPriKey(functions.base64Decode(Db.getSetingValue("HttpsPrivateKey")));
/* 532 */     } catch (Exception e) {
/* 533 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */   public static X509Certificate getHttpsCert() {
/*     */     try {
/* 538 */       String base64String = Db.getSetingValue("HttpsCert");
/* 539 */       if (base64String == null) {
/* 540 */         genHttpsConfig();
/*     */       }
/* 542 */       return CertUtil.loadCert(new ByteArrayInputStream(functions.base64Decode(Db.getSetingValue("HttpsCert"))));
/* 543 */     } catch (Exception e) {
/* 544 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\core\ApplicationContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */