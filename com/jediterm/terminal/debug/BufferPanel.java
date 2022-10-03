/*    */ package com.jediterm.terminal.debug;
/*    */ 
/*    */ import com.jediterm.terminal.ui.TerminalSession;
/*    */ import java.awt.BorderLayout;
/*    */ import java.awt.Font;
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.awt.event.ActionListener;
/*    */ import java.awt.event.ItemEvent;
/*    */ import java.awt.event.ItemListener;
/*    */ import javax.swing.JComboBox;
/*    */ import javax.swing.JPanel;
/*    */ import javax.swing.JScrollPane;
/*    */ import javax.swing.JTextArea;
/*    */ import javax.swing.Timer;
/*    */ 
/*    */ public class BufferPanel extends JPanel {
/*    */   public BufferPanel(final TerminalSession terminal) {
/* 18 */     super(new BorderLayout());
/* 19 */     final JTextArea area = new JTextArea();
/* 20 */     area.setEditable(false);
/*    */     
/* 22 */     add(area, "North");
/*    */     
/* 24 */     DebugBufferType[] choices = DebugBufferType.values();
/*    */     
/* 26 */     final JComboBox<DebugBufferType> chooser = new JComboBox<>(choices);
/* 27 */     add(chooser, "North");
/*    */     
/* 29 */     area.setFont(Font.decode("Monospaced-14"));
/* 30 */     add(new JScrollPane(area), "Center");
/*    */     class Updater
/*    */       implements ActionListener, ItemListener {
/* 33 */       private String myLastUpdate = "";
/*    */       
/*    */       void update() {
/* 36 */         DebugBufferType type = (DebugBufferType)chooser.getSelectedItem();
/* 37 */         String text = terminal.getBufferText(type);
/* 38 */         if (!text.equals(this.myLastUpdate)) {
/* 39 */           area.setText(text);
/* 40 */           this.myLastUpdate = text;
/*    */         } 
/*    */       }
/*    */       
/*    */       public void actionPerformed(ActionEvent e) {
/* 45 */         update();
/*    */       }
/*    */       
/*    */       public void itemStateChanged(ItemEvent e) {
/* 49 */         update();
/*    */       }
/*    */     };
/* 52 */     Updater up = new Updater();
/* 53 */     chooser.addItemListener(up);
/* 54 */     Timer timer = new Timer(1000, up);
/* 55 */     timer.setRepeats(true);
/* 56 */     timer.start();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\terminal\debug\BufferPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */