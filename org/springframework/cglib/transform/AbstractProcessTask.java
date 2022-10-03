/*    */ package org.springframework.cglib.transform;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.util.Collection;
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import java.util.Vector;
/*    */ import org.apache.tools.ant.BuildException;
/*    */ import org.apache.tools.ant.DirectoryScanner;
/*    */ import org.apache.tools.ant.Project;
/*    */ import org.apache.tools.ant.Task;
/*    */ import org.apache.tools.ant.types.FileSet;
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
/*    */ public abstract class AbstractProcessTask
/*    */   extends Task
/*    */ {
/* 27 */   private Vector filesets = new Vector();
/*    */   
/*    */   public void addFileset(FileSet set) {
/* 30 */     this.filesets.addElement(set);
/*    */   }
/*    */   
/*    */   protected Collection getFiles() {
/* 34 */     Map<Object, Object> fileMap = new HashMap<Object, Object>();
/* 35 */     Project p = getProject();
/* 36 */     for (int i = 0; i < this.filesets.size(); i++) {
/* 37 */       FileSet fs = this.filesets.elementAt(i);
/* 38 */       DirectoryScanner ds = fs.getDirectoryScanner(p);
/* 39 */       String[] srcFiles = ds.getIncludedFiles();
/* 40 */       File dir = fs.getDir(p);
/* 41 */       for (int j = 0; j < srcFiles.length; j++) {
/* 42 */         File src = new File(dir, srcFiles[j]);
/* 43 */         fileMap.put(src.getAbsolutePath(), src);
/*    */       } 
/*    */     } 
/* 46 */     return fileMap.values();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute() throws BuildException {
/* 52 */     beforeExecute();
/* 53 */     for (Iterator<File> it = getFiles().iterator(); it.hasNext();) {
/*    */       try {
/* 55 */         processFile(it.next());
/* 56 */       } catch (Exception e) {
/* 57 */         throw new BuildException(e);
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   protected void beforeExecute() throws BuildException {}
/*    */   
/*    */   protected abstract void processFile(File paramFile) throws Exception;
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\transform\AbstractProcessTask.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */