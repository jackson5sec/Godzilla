/*     */ package core;
/*     */ 
/*     */ import core.shell.ShellEntity;
/*     */ import java.io.File;
/*     */ import java.sql.Connection;
/*     */ import java.sql.DriverManager;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.ResultSetMetaData;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import java.util.Vector;
/*     */ import util.Log;
/*     */ import util.functions;
/*     */ 
/*     */ 
/*     */ public class Db
/*     */ {
/*     */   private static Connection dbConn;
/*     */   private static final String Drivde = "org.sqlite.JDBC";
/*     */   private static final String DB_URL = "jdbc:sqlite:data.db";
/*     */   private static final String CREATE_SHELL_TABLE = "CREATE TABLE \"shell\" ( \"id\" text NOT NULL,  \"url\" TEXT NOT NULL,  \"password\" TEXT NOT NULL,  \"secretKey\" TEXT NOT NULL,  \"payload\" TEXT NOT NULL,  \"cryption\" TEXT NOT NULL,  \"encoding\" TEXT NOT NULL,  \"headers\" TEXT NOT NULL,  \"reqLeft\" TEXT NOT NULL,  \"reqRight\" TEXT NOT NULL,  \"connTimeout\" integer NOT NULL,  \"readTimeout\" integer NOT NULL,  \"proxyType\" TEXT NOT NULL,  \"proxyHost\" TEXT NOT NULL,  \"proxyPort\" TEXT NOT NULL,  \"remark\" TEXT NOT NULL,  \"note\" TEXT NOT NULL,  \"createTime\" TEXT NOT NULL,  \"updateTime\" text NOT NULL,  PRIMARY KEY (\"id\"))";
/*     */   
/*     */   static {
/*     */     try {
/*  31 */       Class.forName("org.sqlite.JDBC");
/*  32 */       dbConn = DriverManager.getConnection("jdbc:sqlite:data.db");
/*  33 */       if (!tableExists("shell")) {
/*  34 */         dbConn.createStatement().execute("CREATE TABLE \"shell\" ( \"id\" text NOT NULL,  \"url\" TEXT NOT NULL,  \"password\" TEXT NOT NULL,  \"secretKey\" TEXT NOT NULL,  \"payload\" TEXT NOT NULL,  \"cryption\" TEXT NOT NULL,  \"encoding\" TEXT NOT NULL,  \"headers\" TEXT NOT NULL,  \"reqLeft\" TEXT NOT NULL,  \"reqRight\" TEXT NOT NULL,  \"connTimeout\" integer NOT NULL,  \"readTimeout\" integer NOT NULL,  \"proxyType\" TEXT NOT NULL,  \"proxyHost\" TEXT NOT NULL,  \"proxyPort\" TEXT NOT NULL,  \"remark\" TEXT NOT NULL,  \"note\" TEXT NOT NULL,  \"createTime\" TEXT NOT NULL,  \"updateTime\" text NOT NULL,  PRIMARY KEY (\"id\"))");
/*     */       }
/*  36 */       if (!tableExists("plugin")) {
/*  37 */         dbConn.createStatement().execute("CREATE TABLE plugin (pluginJarFile TEXT NOT NULL,PRIMARY KEY (\"pluginJarFile\"))");
/*     */       }
/*  39 */       if (!tableExists("seting")) {
/*  40 */         dbConn.createStatement().execute("CREATE TABLE seting (\"key\" TEXT NOT NULL,\"value\" TEXT NOT NULL,PRIMARY KEY (\"key\"))");
/*     */       }
/*  42 */       if (!tableExists("shellEnv")) {
/*  43 */         dbConn.createStatement().execute("CREATE TABLE shellEnv (\"shellId\" text NOT NULL,\"key\" TEXT NOT NULL,\"value\" TEXT);");
/*     */       }
/*  45 */       if (!tableExists("shellGroup")) {
/*  46 */         dbConn.createStatement().execute("CREATE TABLE shellGroup (\"groupId\" text NOT NULL,  PRIMARY KEY (\"groupId\"));");
/*     */       }
/*     */       
/*  49 */       dbConn.setAutoCommit(true);
/*  50 */       functions.addShutdownHook(Db.class, null);
/*  51 */     } catch (Exception e) {
/*  52 */       Log.error(e);
/*     */     } 
/*     */   }
/*     */   private static final String CREATE_SHELLENV_TABLE = "CREATE TABLE shellEnv (\"shellId\" text NOT NULL,\"key\" TEXT NOT NULL,\"value\" TEXT);"; private static final String CREATE_PLUGIN_TABLE = "CREATE TABLE plugin (pluginJarFile TEXT NOT NULL,PRIMARY KEY (\"pluginJarFile\"))"; private static final String CREATE_SETING_TABLE = "CREATE TABLE seting (\"key\" TEXT NOT NULL,\"value\" TEXT NOT NULL,PRIMARY KEY (\"key\"))"; private static final String CREATE_SHELLGROUP_TABLE = "CREATE TABLE shellGroup (\"groupId\" text NOT NULL,  PRIMARY KEY (\"groupId\"));";
/*     */   
/*     */   public static boolean tableExists(String tableName) {
/*  58 */     String selectTable = "SELECT COUNT(1) as result FROM sqlite_master WHERE name=?";
/*  59 */     boolean ret = false;
/*     */     try {
/*  61 */       PreparedStatement preparedStatement = getPreparedStatement(selectTable);
/*  62 */       preparedStatement.setString(1, tableName);
/*  63 */       ResultSet resultSet = preparedStatement.executeQuery();
/*  64 */       resultSet.next();
/*  65 */       int result = resultSet.getInt("result");
/*  66 */       if (result == 1) {
/*  67 */         ret = true;
/*     */       }
/*  69 */       resultSet.close();
/*  70 */       preparedStatement.close();
/*  71 */     } catch (Exception e) {
/*  72 */       Log.error(e);
/*     */     } 
/*     */     
/*  75 */     return ret;
/*     */   }
/*     */   public static synchronized Vector<Vector<String>> getAllShell() {
/*  78 */     String selectShell = "SELECT id,url,payload,cryption,encoding,proxyType,remark,createTime,updateTime FROM shell";
/*  79 */     Vector<Vector<String>> rows = new Vector<>();
/*     */ 
/*     */     
/*     */     try {
/*  83 */       Statement statement = getStatement();
/*  84 */       ResultSet resultSet = statement.executeQuery(selectShell);
/*  85 */       Vector<String> columns = getAllcolumn(resultSet.getMetaData());
/*  86 */       rows.add(columns);
/*  87 */       while (resultSet.next()) {
/*  88 */         Vector<String> rowVector = new Vector<>();
/*  89 */         for (int i = 0; i < columns.size(); i++) {
/*  90 */           rowVector.add(resultSet.getString(i + 1));
/*     */         }
/*  92 */         rows.add(rowVector);
/*     */       } 
/*  94 */       resultSet.close();
/*  95 */       statement.close();
/*  96 */       return rows;
/*  97 */     } catch (Exception e) {
/*  98 */       Log.error(e);
/*  99 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static synchronized Vector<Vector<String>> getAllShell(String groupId) {
/* 104 */     if ("/".equals(groupId)) {
/* 105 */       return getAllShell();
/*     */     }
/* 107 */     String selectShell = "SELECT shell.id,shell.url,shell.payload,shell.cryption,shell.encoding,shell.proxyType,shell.remark,shell.createTime,shell.updateTime FROM shellEnv  LEFT JOIN shell ON shell.id = shellId  WHERE key='ENV_GROUP_ID' and value LIKE ?";
/* 108 */     Vector<Vector<String>> rows = new Vector<>();
/*     */ 
/*     */     
/*     */     try {
/* 112 */       Statement statement = getStatement();
/* 113 */       PreparedStatement preparedStatement = getPreparedStatement(selectShell);
/* 114 */       preparedStatement.setString(1, groupId + "%");
/* 115 */       ResultSet resultSet = preparedStatement.executeQuery();
/* 116 */       Vector<String> columns = getAllcolumn(resultSet.getMetaData());
/* 117 */       rows.add(columns);
/* 118 */       while (resultSet.next()) {
/* 119 */         Vector<String> rowVector = new Vector<>();
/* 120 */         for (int i = 0; i < columns.size(); i++) {
/* 121 */           rowVector.add(resultSet.getString(i + 1));
/*     */         }
/* 123 */         rows.add(rowVector);
/*     */       } 
/* 125 */       resultSet.close();
/* 126 */       statement.close();
/* 127 */       return rows;
/* 128 */     } catch (Exception e) {
/* 129 */       Log.error(e);
/* 130 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static synchronized ShellEntity getOneShell(String id) {
/* 135 */     String selectShell = "SELECT id,url,password,secretKey,payload,cryption,encoding,headers,reqLeft,reqRight,connTimeout,readTimeout,proxyType,proxyHost,proxyPort,remark FROM SHELL WHERE id = ?";
/*     */     try {
/* 137 */       PreparedStatement preparedStatement = getPreparedStatement(selectShell);
/* 138 */       preparedStatement.setString(1, id);
/* 139 */       ResultSet resultSet = preparedStatement.executeQuery();
/* 140 */       if (resultSet.next()) {
/* 141 */         ShellEntity context = new ShellEntity();
/* 142 */         context.setId(resultSet.getString("id"));
/* 143 */         context.setUrl(resultSet.getString("url"));
/* 144 */         context.setPassword(resultSet.getString("password"));
/* 145 */         context.setPayload(resultSet.getString("payload"));
/* 146 */         context.setSecretKey(resultSet.getString("secretKey"));
/* 147 */         context.setCryption(resultSet.getString("cryption"));
/* 148 */         context.setEncoding(resultSet.getString("encoding"));
/* 149 */         context.setRemark(resultSet.getString("remark"));
/* 150 */         context.setHeader(resultSet.getString("headers"));
/* 151 */         context.setReqLeft(resultSet.getString("reqLeft"));
/* 152 */         context.setReqRight(resultSet.getString("reqRight"));
/* 153 */         context.setConnTimeout(resultSet.getInt("connTimeout"));
/* 154 */         context.setReadTimeout(resultSet.getInt("readTimeout"));
/* 155 */         context.setProxyType(resultSet.getString("proxyType"));
/* 156 */         context.setProxyHost(resultSet.getString("proxyHost"));
/* 157 */         context.setProxyPort(resultSet.getInt("proxyPort"));
/* 158 */         resultSet.close();
/* 159 */         preparedStatement.close();
/* 160 */         return context;
/*     */       } 
/* 162 */       return null;
/*     */     }
/* 164 */     catch (Exception e) {
/* 165 */       Log.error(e);
/* 166 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static synchronized int addShell(ShellEntity shellContext) {
/* 171 */     String uuid = UUID.randomUUID().toString();
/* 172 */     String addShellSql = "INSERT INTO \"shell\"(\"id\", \"url\", \"password\", \"secretKey\", \"payload\", \"cryption\", \"encoding\", \"headers\", \"reqLeft\", \"reqRight\", \"connTimeout\", \"readTimeout\", \"proxyType\", \"proxyHost\", \"proxyPort\", \"remark\", \"note\", \"createTime\", \"updateTime\") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
/* 173 */     String createTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());
/* 174 */     PreparedStatement preparedStatement = getPreparedStatement(addShellSql);
/* 175 */     shellContext.setId(uuid);
/*     */     try {
/* 177 */       preparedStatement.setString(1, uuid);
/* 178 */       preparedStatement.setString(2, shellContext.getUrl());
/* 179 */       preparedStatement.setString(3, shellContext.getPassword());
/* 180 */       preparedStatement.setString(4, shellContext.getSecretKey());
/* 181 */       preparedStatement.setString(5, shellContext.getPayload());
/* 182 */       preparedStatement.setString(6, shellContext.getCryption());
/* 183 */       preparedStatement.setString(7, shellContext.getEncoding());
/* 184 */       preparedStatement.setString(8, shellContext.getHeaderS());
/* 185 */       preparedStatement.setString(9, shellContext.getReqLeft());
/* 186 */       preparedStatement.setString(10, shellContext.getReqRight());
/* 187 */       preparedStatement.setInt(11, shellContext.getConnTimeout());
/* 188 */       preparedStatement.setInt(12, shellContext.getReadTimeout());
/* 189 */       preparedStatement.setString(13, shellContext.getProxyType());
/* 190 */       preparedStatement.setString(14, shellContext.getProxyHost());
/* 191 */       preparedStatement.setInt(15, shellContext.getProxyPort());
/* 192 */       preparedStatement.setString(16, shellContext.getRemark());
/* 193 */       preparedStatement.setString(17, "");
/* 194 */       preparedStatement.setString(18, createTime);
/* 195 */       preparedStatement.setString(19, createTime);
/* 196 */       int affectNum = preparedStatement.executeUpdate();
/* 197 */       preparedStatement.close();
/* 198 */       return affectNum;
/* 199 */     } catch (Exception e) {
/* 200 */       Log.error(e);
/* 201 */       return 0;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static synchronized int updateShell(ShellEntity shellContext) {
/* 206 */     String updateShell = "UPDATE \"shell\" SET \"url\" = ?, \"password\" = ?, \"secretKey\" = ?, \"payload\" = ?, \"cryption\" = ?, \"encoding\" = ?, \"headers\" = ?, \"reqLeft\" = ?, \"reqRight\" = ?, \"connTimeout\" = ?, \"readTimeout\" = ?, \"proxyType\" = ?, \"proxyHost\" = ?, \"proxyPort\" = ?, \"remark\" = ?, \"updateTime\" = ? WHERE id = ?";
/* 207 */     String updateTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());
/* 208 */     PreparedStatement preparedStatement = getPreparedStatement(updateShell);
/*     */     try {
/* 210 */       preparedStatement.setString(1, shellContext.getUrl());
/* 211 */       preparedStatement.setString(2, shellContext.getPassword());
/* 212 */       preparedStatement.setString(3, shellContext.getSecretKey());
/* 213 */       preparedStatement.setString(4, shellContext.getPayload());
/* 214 */       preparedStatement.setString(5, shellContext.getCryption());
/* 215 */       preparedStatement.setString(6, shellContext.getEncoding());
/* 216 */       preparedStatement.setString(7, shellContext.getHeaderS());
/* 217 */       preparedStatement.setString(8, shellContext.getReqLeft());
/* 218 */       preparedStatement.setString(9, shellContext.getReqRight());
/* 219 */       preparedStatement.setInt(10, shellContext.getConnTimeout());
/* 220 */       preparedStatement.setInt(11, shellContext.getReadTimeout());
/* 221 */       preparedStatement.setString(12, shellContext.getProxyType());
/* 222 */       preparedStatement.setString(13, shellContext.getProxyHost());
/* 223 */       preparedStatement.setInt(14, shellContext.getProxyPort());
/* 224 */       preparedStatement.setString(15, shellContext.getRemark());
/* 225 */       preparedStatement.setString(16, updateTime);
/* 226 */       preparedStatement.setString(17, shellContext.getId());
/* 227 */       int affectNum = preparedStatement.executeUpdate();
/* 228 */       preparedStatement.close();
/* 229 */       return affectNum;
/* 230 */     } catch (Exception e) {
/* 231 */       Log.error(e);
/* 232 */       return 0;
/*     */     } 
/*     */   }
/*     */   public static synchronized int removeShell(String id) {
/* 236 */     String addShellSql = "DELETE FROM shell WHERE \"id\"= ?";
/* 237 */     PreparedStatement preparedStatement = getPreparedStatement(addShellSql);
/*     */     try {
/* 239 */       preparedStatement.setString(1, id);
/* 240 */       int affectNum = preparedStatement.executeUpdate();
/* 241 */       functions.delFiles(new File(String.format("%s/%s", new Object[] { "GodzillaCache", id })));
/* 242 */       preparedStatement.close();
/* 243 */       clearShellEnv(id);
/* 244 */       return affectNum;
/* 245 */     } catch (Exception e) {
/* 246 */       e.printStackTrace();
/* 247 */       return 0;
/*     */     } 
/*     */   }
/*     */   public static List getAllGroup() {
/* 251 */     String addShellSql = "SELECT groupId FROM shellGroup";
/* 252 */     ArrayList<String> ids = new ArrayList();
/* 253 */     PreparedStatement preparedStatement = getPreparedStatement(addShellSql);
/*     */     try {
/* 255 */       ResultSet resultSet = preparedStatement.executeQuery();
/* 256 */       while (resultSet.next()) {
/* 257 */         ids.add(resultSet.getString(1));
/*     */       }
/* 259 */     } catch (Exception e) {
/* 260 */       e.printStackTrace();
/*     */     } 
/* 262 */     return ids;
/*     */   }
/*     */   public static synchronized int removeShellByGroup(String groupId) {
/* 265 */     String addShellSql = "SELECT id FROM shellEnv  LEFT JOIN shell ON shell.id = shellId  WHERE key='ENV_GROUP_ID' and value LIKE ?";
/* 266 */     PreparedStatement preparedStatement = getPreparedStatement(addShellSql);
/*     */     try {
/* 268 */       preparedStatement.setString(1, groupId + "%");
/* 269 */       ResultSet resultSet = preparedStatement.executeQuery();
/*     */       
/* 271 */       ArrayList<String> ids = new ArrayList();
/* 272 */       while (resultSet.next()) {
/* 273 */         ids.add(resultSet.getString(1));
/*     */       }
/* 275 */       int affectNum = ids.stream().mapToInt(id -> removeShell(id.toString())).sum();
/* 276 */       return affectNum;
/* 277 */     } catch (Exception e) {
/* 278 */       e.printStackTrace();
/* 279 */       return 0;
/*     */     } 
/*     */   }
/*     */   public static synchronized int removeGroup(String groupId, String defaultGroupId) {
/* 283 */     String addShellSql = "DELETE FROM shellGroup WHERE groupId LIKE ?";
/* 284 */     PreparedStatement preparedStatement = getPreparedStatement(addShellSql);
/*     */     try {
/* 286 */       preparedStatement.setString(1, groupId + "%");
/* 287 */       int affectNum = preparedStatement.executeUpdate();
/* 288 */       preparedStatement = getPreparedStatement("UPDATE shellEnv SET value=?  WHERE key='ENV_GROUP_ID' AND value LIKE ?");
/* 289 */       preparedStatement.setString(1, defaultGroupId);
/* 290 */       preparedStatement.setString(2, groupId + "%");
/* 291 */       affectNum += preparedStatement.executeUpdate();
/* 292 */       return affectNum;
/* 293 */     } catch (Exception e) {
/* 294 */       e.printStackTrace();
/* 295 */       return 0;
/*     */     } 
/*     */   }
/*     */   public static synchronized int addGroup(String groupId) {
/* 299 */     String addShellSql = "INSERT INTO shellGroup (groupId) VALUES(?)";
/* 300 */     PreparedStatement preparedStatement = getPreparedStatement(addShellSql);
/*     */     try {
/* 302 */       preparedStatement.setString(1, groupId);
/* 303 */       int affectNum = preparedStatement.executeUpdate();
/* 304 */       return affectNum;
/* 305 */     } catch (Exception e) {
/* 306 */       e.printStackTrace();
/* 307 */       return 0;
/*     */     } 
/*     */   }
/*     */   public static synchronized int renameGroup(String groupId, String newGroupId) {
/* 311 */     String addShellSql = "UPDATE shellEnv SET  value = ? || SUBSTR(value,LENGTH(?)+1) WHERE key = 'ENV_GROUP_ID' AND value LIKE ?";
/* 312 */     PreparedStatement preparedStatement = getPreparedStatement(addShellSql);
/*     */     try {
/* 314 */       preparedStatement.setString(1, newGroupId);
/* 315 */       preparedStatement.setString(2, groupId);
/* 316 */       preparedStatement.setString(3, groupId + "%");
/* 317 */       int affectNum = preparedStatement.executeUpdate();
/*     */       
/* 319 */       preparedStatement = getPreparedStatement("UPDATE shellGroup SET  groupId = ? || SUBSTR(groupId,LENGTH(?)+1) WHERE groupId LIKE ?");
/* 320 */       preparedStatement.setString(1, newGroupId);
/* 321 */       preparedStatement.setString(2, groupId);
/* 322 */       preparedStatement.setString(3, groupId + "%");
/* 323 */       affectNum += preparedStatement.executeUpdate();
/*     */       
/* 325 */       return affectNum;
/* 326 */     } catch (Exception e) {
/* 327 */       e.printStackTrace();
/* 328 */       return 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static synchronized int updateShellNote(String id, String note) {
/* 335 */     String updateNote = "UPDATE \"shell\" SET \"note\" = ?, \"updateTime\" = ? WHERE id = ?";
/* 336 */     String updateTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());
/* 337 */     PreparedStatement preparedStatement = getPreparedStatement(updateNote);
/*     */     try {
/* 339 */       preparedStatement.setString(1, note);
/* 340 */       preparedStatement.setString(2, updateTime);
/* 341 */       preparedStatement.setString(3, id);
/* 342 */       int affectNum = preparedStatement.executeUpdate();
/* 343 */       preparedStatement.close();
/* 344 */       return affectNum;
/* 345 */     } catch (Exception e) {
/* 346 */       Log.error(e);
/* 347 */       return 0;
/*     */     } 
/*     */   }
/*     */   public static synchronized String getShellNote(String id) {
/* 351 */     String selectShell = "SELECT note FROM shell WHERE id = ?";
/*     */     try {
/* 353 */       PreparedStatement preparedStatement = getPreparedStatement(selectShell);
/* 354 */       preparedStatement.setString(1, id);
/* 355 */       String note = preparedStatement.executeQuery().getString("note");
/* 356 */       preparedStatement.close();
/* 357 */       return note;
/* 358 */     } catch (Exception e) {
/* 359 */       Log.error(e);
/* 360 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String[] getAllPlugin() {
/* 365 */     String selectPlugin = "SELECT pluginJarFile FROM plugin";
/* 366 */     ArrayList<String> pluginArrayList = new ArrayList();
/*     */     
/*     */     try {
/* 369 */       Statement statement = getStatement();
/* 370 */       ResultSet resultSet = statement.executeQuery(selectPlugin);
/* 371 */       while (resultSet.next()) {
/* 372 */         pluginArrayList.add(resultSet.getString("pluginJarFile"));
/*     */       }
/*     */       
/* 375 */       resultSet.close();
/* 376 */       statement.close();
/* 377 */     } catch (Exception e) {
/* 378 */       Log.error(e);
/*     */     } 
/* 380 */     return pluginArrayList.<String>toArray(new String[0]);
/*     */   }
/*     */   public static synchronized int removePlugin(String jarFile) {
/* 383 */     String addShellSql = "DELETE FROM plugin WHERE pluginJarFile=?";
/* 384 */     PreparedStatement preparedStatement = getPreparedStatement(addShellSql);
/*     */     try {
/* 386 */       preparedStatement.setString(1, jarFile);
/* 387 */       int affectNum = preparedStatement.executeUpdate();
/* 388 */       preparedStatement.close();
/* 389 */       return affectNum;
/* 390 */     } catch (Exception e) {
/* 391 */       e.printStackTrace();
/* 392 */       return 0;
/*     */     } 
/*     */   }
/*     */   public static synchronized int addPlugin(String jarFile) {
/* 396 */     String addPluginSql = "INSERT INTO plugin (pluginJarFile) VALUES (?)";
/* 397 */     PreparedStatement preparedStatement = getPreparedStatement(addPluginSql);
/*     */     try {
/* 399 */       preparedStatement.setString(1, jarFile);
/* 400 */       int affectNum = preparedStatement.executeUpdate();
/* 401 */       preparedStatement.close();
/* 402 */       return affectNum;
/* 403 */     } catch (Exception e) {
/* 404 */       e.printStackTrace();
/* 405 */       return 0;
/*     */     } 
/*     */   }
/*     */   public static synchronized boolean addSetingKV(String key, String value) {
/* 409 */     if (existsSetingKey(key)) {
/* 410 */       return updateSetingKV(key, value);
/*     */     }
/* 412 */     String updateSetingSql = "INSERT INTO seting (\"key\", \"value\") VALUES (?, ?)";
/* 413 */     PreparedStatement preparedStatement = getPreparedStatement(updateSetingSql);
/*     */     try {
/* 415 */       preparedStatement.setString(1, key);
/* 416 */       preparedStatement.setString(2, value);
/* 417 */       int affectNum = preparedStatement.executeUpdate();
/* 418 */       preparedStatement.close();
/* 419 */       return (affectNum > 0);
/* 420 */     } catch (Exception e) {
/* 421 */       e.printStackTrace();
/* 422 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static synchronized boolean updateSetingKV(String key, String value) {
/* 427 */     if (ApplicationContext.isOpenC("isSuperLog")) {
/* 428 */       Log.log(String.format("updateSetingKV key:%s value:%s", new Object[] { key, value }), new Object[0]);
/*     */     }
/* 430 */     if (existsSetingKey(key)) {
/* 431 */       String updateSetingSql = "UPDATE seting set value=? WHERE key=?";
/* 432 */       PreparedStatement preparedStatement = getPreparedStatement(updateSetingSql);
/*     */       try {
/* 434 */         preparedStatement.setString(1, value);
/* 435 */         preparedStatement.setString(2, key);
/* 436 */         int affectNum = preparedStatement.executeUpdate();
/* 437 */         preparedStatement.close();
/* 438 */         return (affectNum > 0);
/* 439 */       } catch (Exception e) {
/* 440 */         e.printStackTrace();
/* 441 */         return false;
/*     */       } 
/*     */     } 
/* 444 */     return addSetingKV(key, value);
/*     */   }
/*     */   
/*     */   public static synchronized boolean removeSetingK(String key) {
/* 448 */     String updateSetingSql = "DELETE FROM seting WHERE key=?";
/* 449 */     PreparedStatement preparedStatement = getPreparedStatement(updateSetingSql);
/*     */     try {
/* 451 */       preparedStatement.setString(1, key);
/* 452 */       int affectNum = preparedStatement.executeUpdate();
/* 453 */       preparedStatement.close();
/* 454 */       return (affectNum > 0);
/* 455 */     } catch (Exception e) {
/* 456 */       e.printStackTrace();
/* 457 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void clearShellEnv(String shellId) {
/* 462 */     String updateSetingSql = "DELETE FROM shellEnv WHERE shellId=?";
/* 463 */     PreparedStatement preparedStatement = getPreparedStatement(updateSetingSql);
/*     */     try {
/* 465 */       preparedStatement.setString(1, shellId);
/* 466 */       int affectNum = preparedStatement.executeUpdate();
/* 467 */       preparedStatement.close();
/* 468 */     } catch (Exception e) {
/* 469 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String getSetingValue(String key, String defaultVal) {
/* 474 */     String ret = getSetingValue(key);
/* 475 */     if (ret == null) {
/* 476 */       updateSetingKV(key, defaultVal);
/* 477 */       ret = defaultVal;
/*     */     } 
/* 479 */     return ret;
/*     */   }
/*     */   public static String getSetingValue(String key) {
/* 482 */     String getSetingValueSql = "SELECT value FROM seting WHERE key=?";
/*     */     try {
/* 484 */       PreparedStatement preparedStatement = getPreparedStatement(getSetingValueSql);
/* 485 */       preparedStatement.setString(1, key);
/* 486 */       ResultSet resultSet = preparedStatement.executeQuery();
/* 487 */       String value = resultSet.next() ? resultSet.getString("value") : null;
/* 488 */       resultSet.close();
/* 489 */       preparedStatement.close();
/* 490 */       return value;
/* 491 */     } catch (Exception e) {
/* 492 */       Log.error(e);
/* 493 */       return null;
/*     */     } 
/*     */   }
/*     */   public static boolean getSetingBooleanValue(String key) {
/* 497 */     return getSetingBooleanValue(key, false);
/*     */   }
/*     */   public static boolean getSetingBooleanValue(String key, boolean defaultValue) {
/* 500 */     String valueString = getSetingValue(key);
/* 501 */     boolean ret = defaultValue;
/* 502 */     if (valueString != null) {
/*     */       try {
/* 504 */         ret = Boolean.valueOf(valueString).booleanValue();
/* 505 */       } catch (Exception e) {
/* 506 */         Log.error(e);
/* 507 */         updateSetingKV(key, String.valueOf(ret));
/*     */       } 
/*     */     } else {
/* 510 */       updateSetingKV(key, String.valueOf(ret));
/*     */     } 
/* 512 */     return ret;
/*     */   }
/*     */   public static int getSetingIntValue(String key) {
/* 515 */     return getSetingIntValue(key, -1);
/*     */   }
/*     */   public static int getSetingIntValue(String key, int defaultValue) {
/* 518 */     String valueString = getSetingValue(key);
/* 519 */     int ret = defaultValue;
/* 520 */     if (valueString != null) {
/*     */       try {
/* 522 */         ret = Integer.valueOf(valueString).intValue();
/* 523 */       } catch (Exception e) {
/* 524 */         Log.error(e);
/* 525 */         updateSetingKV(key, String.valueOf(ret));
/*     */       } 
/*     */     } else {
/* 528 */       updateSetingKV(key, String.valueOf(ret));
/*     */     } 
/* 530 */     return ret;
/*     */   }
/*     */   public static String tryGetSetingValue(String key, String ret) {
/* 533 */     String tRet = getSetingValue(key);
/* 534 */     if (tRet == null) {
/* 535 */       return ret;
/*     */     }
/* 537 */     return tRet;
/*     */   }
/*     */   
/*     */   public static boolean existsSetingKey(String key) {
/* 541 */     String selectKeyNumSql = "SELECT COUNT(1) as c FROM seting WHERE key=?";
/*     */     try {
/* 543 */       PreparedStatement preparedStatement = getPreparedStatement(selectKeyNumSql);
/* 544 */       preparedStatement.setString(1, key);
/* 545 */       int c = preparedStatement.executeQuery().getInt("c");
/* 546 */       preparedStatement.close();
/* 547 */       return (c > 0);
/* 548 */     } catch (Exception e) {
/* 549 */       Log.error(e);
/* 550 */       return false;
/*     */     } 
/*     */   }
/*     */   public static PreparedStatement getPreparedStatement(String sql) {
/* 554 */     if (dbConn != null) {
/*     */       try {
/* 556 */         return dbConn.prepareStatement(sql);
/* 557 */       } catch (SQLException e) {
/*     */         
/* 559 */         Log.error(e);
/* 560 */         return null;
/*     */       } 
/*     */     }
/* 563 */     return null;
/*     */   }
/*     */   
/*     */   public static Statement getStatement() {
/* 567 */     if (dbConn != null) {
/*     */       try {
/* 569 */         return dbConn.createStatement();
/* 570 */       } catch (SQLException e) {
/* 571 */         Log.error(e);
/* 572 */         return null;
/*     */       } 
/*     */     }
/* 575 */     return null;
/*     */   }
/*     */   
/*     */   private static Vector<String> getAllcolumn(ResultSetMetaData metaData) {
/* 579 */     if (metaData != null) {
/* 580 */       Vector<String> columns = new Vector<>();
/*     */       try {
/* 582 */         int columnNum = metaData.getColumnCount();
/* 583 */         for (int i = 0; i < columnNum; i++) {
/* 584 */           columns.add(metaData.getColumnName(i + 1));
/*     */         }
/* 586 */         return columns;
/* 587 */       } catch (Exception e) {
/* 588 */         Log.error(e);
/* 589 */         return columns;
/*     */       } 
/*     */     } 
/* 592 */     return null;
/*     */   }
/*     */   
/*     */   public static void Tclose() {
/*     */     try {
/* 597 */       if (dbConn != null && !dbConn.isClosed()) {
/* 598 */         dbConn.close();
/*     */       }
/* 600 */     } catch (SQLException e) {
/* 601 */       Log.error(e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\core\Db.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */