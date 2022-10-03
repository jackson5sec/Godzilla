/*    */ package core.shell.cache;
/*    */ 
/*    */ import core.imp.Payload;
/*    */ import core.shell.ShellEntity;
/*    */ import java.io.File;
/*    */ import util.RC4;
/*    */ import util.http.ReqParameter;
/*    */ 
/*    */ public abstract class PayloadCacheHandler
/*    */ {
/* 11 */   public static final String[] blackMethod = new String[] { "bigFileDownload", "bigFileUpload", "include", "uploadFile" };
/*    */   
/*    */   protected ShellEntity shellEntity;
/*    */   protected Payload payload;
/*    */   protected CacheDb cacheDb;
/*    */   protected boolean initialized;
/*    */   protected String currentDirectory;
/*    */   protected String shellId;
/*    */   protected RC4 rc4;
/*    */   
/*    */   protected PayloadCacheHandler(ShellEntity entity, Payload payload) {
/* 22 */     this.shellEntity = entity;
/* 23 */     this.payload = payload;
/* 24 */     initialized();
/*    */   }
/*    */   
/*    */   protected void initialized() {
/* 28 */     if (!this.initialized) {
/* 29 */       this.rc4 = new RC4();
/* 30 */       this.shellId = this.shellEntity.getId();
/*    */       
/* 32 */       File file = new File("GodzillaCache");
/* 33 */       if (file.exists() && !file.isDirectory()) {
/* 34 */         file.delete();
/*    */       }
/* 36 */       file.mkdirs();
/*    */       
/* 38 */       this.currentDirectory = String.format("%s/%s/", new Object[] { "GodzillaCache", this.shellId });
/* 39 */       file = new File(this.currentDirectory);
/* 40 */       if (file.exists() && !file.isDirectory()) {
/* 41 */         file.delete();
/*    */       }
/* 43 */       file.mkdirs();
/*    */       
/* 45 */       this.cacheDb = new CacheDb(this.shellEntity.getId());
/*    */     } 
/*    */   }
/*    */   
/*    */   public abstract byte[] evalFunc(byte[] paramArrayOfbyte, String paramString1, String paramString2, ReqParameter paramReqParameter);
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\core\shell\cache\PayloadCacheHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */