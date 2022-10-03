/*     */ package javassist.util;
/*     */ 
/*     */ import com.sun.jdi.Bootstrap;
/*     */ import com.sun.jdi.ReferenceType;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import com.sun.jdi.connect.AttachingConnector;
/*     */ import com.sun.jdi.connect.Connector;
/*     */ import com.sun.jdi.connect.IllegalConnectorArgumentsException;
/*     */ import com.sun.jdi.event.Event;
/*     */ import com.sun.jdi.event.EventIterator;
/*     */ import com.sun.jdi.event.EventQueue;
/*     */ import com.sun.jdi.event.EventSet;
/*     */ import com.sun.jdi.request.EventRequest;
/*     */ import com.sun.jdi.request.EventRequestManager;
/*     */ import com.sun.jdi.request.MethodEntryRequest;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public class HotSwapper
/*     */ {
/* 101 */   private static final String TRIGGER_NAME = Trigger.class.getName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HotSwapper(int port) throws IOException, IllegalConnectorArgumentsException {
/* 111 */     this(Integer.toString(port));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 122 */   private VirtualMachine jvm = null;
/* 123 */   private MethodEntryRequest request = null;
/* 124 */   private Map<ReferenceType, byte[]> newClassFiles = null;
/* 125 */   private Trigger trigger = new Trigger();
/*     */   public HotSwapper(String port) throws IOException, IllegalConnectorArgumentsException {
/* 127 */     AttachingConnector connector = (AttachingConnector)findConnector("com.sun.jdi.SocketAttach");
/*     */     
/* 129 */     Map<String, Connector.Argument> arguments = connector.defaultArguments();
/* 130 */     ((Connector.Argument)arguments.get("hostname")).setValue("localhost");
/* 131 */     ((Connector.Argument)arguments.get("port")).setValue(port);
/* 132 */     this.jvm = connector.attach(arguments);
/* 133 */     EventRequestManager manager = this.jvm.eventRequestManager();
/* 134 */     this.request = methodEntryRequests(manager, TRIGGER_NAME);
/*     */   }
/*     */   private static final String HOST_NAME = "localhost";
/*     */   private Connector findConnector(String connector) throws IOException {
/* 138 */     List<Connector> connectors = Bootstrap.virtualMachineManager().allConnectors();
/*     */     
/* 140 */     for (Connector con : connectors) {
/* 141 */       if (con.name().equals(connector))
/* 142 */         return con; 
/*     */     } 
/* 144 */     throw new IOException("Not found: " + connector);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static MethodEntryRequest methodEntryRequests(EventRequestManager manager, String classpattern) {
/* 150 */     MethodEntryRequest mereq = manager.createMethodEntryRequest();
/* 151 */     mereq.addClassFilter(classpattern);
/* 152 */     mereq.setSuspendPolicy(1);
/* 153 */     return mereq;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void deleteEventRequest(EventRequestManager manager, MethodEntryRequest request) {
/* 161 */     manager.deleteEventRequest((EventRequest)request);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reload(String className, byte[] classFile) {
/* 171 */     ReferenceType classtype = toRefType(className);
/* 172 */     Map<ReferenceType, byte[]> map = (Map)new HashMap<>();
/* 173 */     map.put(classtype, classFile);
/* 174 */     reload2(map, className);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reload(Map<String, byte[]> classFiles) {
/* 186 */     Map<ReferenceType, byte[]> map = (Map)new HashMap<>();
/* 187 */     String className = null;
/* 188 */     for (Map.Entry<String, byte[]> e : classFiles.entrySet()) {
/* 189 */       className = e.getKey();
/* 190 */       map.put(toRefType(className), e.getValue());
/*     */     } 
/*     */     
/* 193 */     if (className != null)
/* 194 */       reload2(map, className + " etc."); 
/*     */   }
/*     */   
/*     */   private ReferenceType toRefType(String className) {
/* 198 */     List<ReferenceType> list = this.jvm.classesByName(className);
/* 199 */     if (list == null || list.isEmpty())
/* 200 */       throw new RuntimeException("no such class: " + className); 
/* 201 */     return list.get(0);
/*     */   }
/*     */   
/*     */   private void reload2(Map<ReferenceType, byte[]> map, String msg) {
/* 205 */     synchronized (this.trigger) {
/* 206 */       startDaemon();
/* 207 */       this.newClassFiles = map;
/* 208 */       this.request.enable();
/* 209 */       this.trigger.doSwap();
/* 210 */       this.request.disable();
/* 211 */       Map<ReferenceType, byte[]> ncf = this.newClassFiles;
/* 212 */       if (ncf != null) {
/* 213 */         this.newClassFiles = null;
/* 214 */         throw new RuntimeException("failed to reload: " + msg);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void startDaemon() {
/* 220 */     (new Thread() {
/*     */         private void errorMsg(Throwable e) {
/* 222 */           System.err.print("Exception in thread \"HotSwap\" ");
/* 223 */           e.printStackTrace(System.err);
/*     */         }
/*     */ 
/*     */         
/*     */         public void run() {
/* 228 */           EventSet events = null;
/*     */           try {
/* 230 */             events = HotSwapper.this.waitEvent();
/* 231 */             EventIterator iter = events.eventIterator();
/* 232 */             while (iter.hasNext()) {
/* 233 */               Event event = iter.nextEvent();
/* 234 */               if (event instanceof com.sun.jdi.event.MethodEntryEvent) {
/* 235 */                 HotSwapper.this.hotswap();
/*     */                 
/*     */                 break;
/*     */               } 
/*     */             } 
/* 240 */           } catch (Throwable e) {
/* 241 */             errorMsg(e);
/*     */           } 
/*     */           try {
/* 244 */             if (events != null) {
/* 245 */               events.resume();
/*     */             }
/* 247 */           } catch (Throwable e) {
/* 248 */             errorMsg(e);
/*     */           } 
/*     */         }
/* 251 */       }).start();
/*     */   }
/*     */   
/*     */   EventSet waitEvent() throws InterruptedException {
/* 255 */     EventQueue queue = this.jvm.eventQueue();
/* 256 */     return queue.remove();
/*     */   }
/*     */   
/*     */   void hotswap() {
/* 260 */     Map<ReferenceType, byte[]> map = this.newClassFiles;
/* 261 */     this.jvm.redefineClasses(map);
/* 262 */     this.newClassFiles = null;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassis\\util\HotSwapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */