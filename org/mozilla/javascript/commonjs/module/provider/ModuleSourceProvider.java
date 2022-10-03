/*    */ package org.mozilla.javascript.commonjs.module.provider;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.URI;
/*    */ import java.net.URISyntaxException;
/*    */ import org.mozilla.javascript.Scriptable;
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
/*    */ public interface ModuleSourceProvider
/*    */ {
/* 26 */   public static final ModuleSource NOT_MODIFIED = new ModuleSource(null, null, null, null, null);
/*    */   
/*    */   ModuleSource loadSource(String paramString, Scriptable paramScriptable, Object paramObject) throws IOException, URISyntaxException;
/*    */   
/*    */   ModuleSource loadSource(URI paramURI1, URI paramURI2, Object paramObject) throws IOException, URISyntaxException;
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\commonjs\module\provider\ModuleSourceProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */