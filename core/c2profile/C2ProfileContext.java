/*    */ package core.c2profile;
/*    */ 
/*    */ import org.yaml.snakeyaml.DumperOptions;
/*    */ import org.yaml.snakeyaml.Yaml;
/*    */ import org.yaml.snakeyaml.introspector.BeanAccess;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class C2ProfileContext
/*    */ {
/*    */   public RequestChannelType requestChannelType;
/*    */   public ResponseChannelType responseChannelType;
/*    */   public C2Profile c2Profile;
/*    */   
/*    */   public static void main(String[] args) {
/* 20 */     DumperOptions dumperOptions = new DumperOptions();
/* 21 */     dumperOptions.setProcessComments(true);
/* 22 */     dumperOptions.setPrettyFlow(true);
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 27 */     Yaml yaml = new Yaml(dumperOptions);
/* 28 */     yaml.setBeanAccess(BeanAccess.FIELD);
/*    */     
/* 30 */     System.out.println(yaml.dumpAsMap(new C2ProfileContext()));
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\core\c2profile\C2ProfileContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */