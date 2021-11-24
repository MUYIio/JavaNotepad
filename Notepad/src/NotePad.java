import java.io.*;
import java.awt.*;
import java.awt.Font;
import java.awt.event.*;
import java.util.Calendar;
import java.util.Enumeration;
import java.awt.event.ActionEvent;
import java.util.GregorianCalendar;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.undo.UndoManager;
import javax.swing.plaf.FontUIResource;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;

public class NotePad extends JFrame implements ActionListener // extends继承JFrame类
{
	int start = 0, end = 0;
	JMenuBar bar; 
	JMenu mu1, mu2, mu3, mu4, mu5;
	JMenuItem mt11, mt12, mt13, mt14, mt15; 
	JMenuItem mt21, mt22, mt23, mt24, mt25;
	JMenuItem mt31, mt32, mt33, mt34, mt35;
	JMenuItem mt41, mt42, mt43;
	JMenuItem mt51;
	
	JRadioButton en_utf_8_Item = null;
	JRadioButton en_gbk_Item = null;
	JRadioButton de_utf_8_Item = null;
	JRadioButton de_gbk_Item = null;
	
	UndoManager manager; // 添加布局管理器
	JFileChooser jfc; // 定义一个文件选择框
	JTextArea area; // 定义一个文本框
	JScrollPane jsp; // 定义一个视窗
	File file;
	
	// 底部状态栏
	JLabel labelLeft, labelCenter, labelRight;
	JToolBar jDown; // 定义一个工具栏
	
	// 编码格式
	String encode = "UTF-8";
	String decode = "UTF-8";
	String jsb = "新建记事本";

	// ctrl键是否按下
	boolean key_ctrl = false;
	// 是否在打开文件
	boolean isOpen = false;
	// 是否修改文件
	boolean isChanged = false;
	// 系统滚动条
	MouseWheelListener sysWheel;
	// 初始化文本域字体大小
	Font f = new Font("Serif", 0, 23);
	
	GregorianCalendar time = new GregorianCalendar(); // 定义一个标准时间
	int hour = time.get(Calendar.HOUR_OF_DAY);
	int min = time.get(Calendar.MINUTE);
	int second = time.get(Calendar.SECOND);

	public NotePad() 
	{
		initFrame();
		initGUI();
	}
	
	public void initFrame()
	{
		InitGlobalFont(new Font("宋体", Font.PLAIN, 20)); // 统一设置字体
		setTitle("新建记事本");
		setBounds(200, 100, 750, 900);	
		// 加载Label的板块
		this.launchLabel();		
		Clock clock = new Clock();
		clock.start();
		setLocationRelativeTo(null); // 窗口居中显示
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // 3设置JFram不要默认关闭，为了用对话框触发退出
		setVisible(true);
	}
	
	public void initGUI()
	{
		bar = new JMenuBar();
		mu1 = new JMenu("文件(F)");
		mu2 = new JMenu("编辑(E)");
		mu3 = new JMenu("格式(O)");
		mu4 = new JMenu("查看(V)");
		mu5 = new JMenu("帮助(H)");

		mt11 = new JMenuItem("新建(N)   Ctrl+N");
		mt12 = new JMenuItem("打开(O)   Ctrl+O");
		mt13 = new JMenuItem("保存(S)   Ctrl+S");
		mt14 = new JMenuItem("另存为(A)...");
		mt15 = new JMenuItem("退出(X)");
		mt11.addActionListener(this);
		mt12.addActionListener(this);
		mt13.addActionListener(this);
		mt14.addActionListener(this);
		mt15.addActionListener(this);
		
		mt21 = new JMenuItem("撤销(U)   Ctrl+Z");
		mt22 = new JMenuItem("剪切(T)   Ctrl+X");
		mt23 = new JMenuItem("复制(C)   Ctrl+C");
		mt24 = new JMenuItem("粘贴(P)   Ctrl+V");
		mt25 = new JMenuItem("删除(L)   Del");
		mt21.addActionListener(this);
		mt22.addActionListener(this);
		mt23.addActionListener(this);
		mt24.addActionListener(this);
		mt25.addActionListener(this);
		
		mt31 = new JCheckBoxMenuItem("自动换行", true);
		mt32 = new JMenuItem("字体选择(F)");
		mt33 = new JMenuItem("替换(R)||查找(F)");
		mt34 = new JMenu("编码");
		mt35 = new JMenu("解码");
		en_utf_8_Item = new JRadioButton("UTF-8",true);
		en_gbk_Item = new JRadioButton("GBK",false);
		de_utf_8_Item = new JRadioButton("UTF-8",true);
		de_gbk_Item = new JRadioButton("GBK",false);
		ButtonGroup group1 = new ButtonGroup(); 
		ButtonGroup group2 = new ButtonGroup();
		mt31.addActionListener(this);
		mt32.addActionListener(this);
		mt33.addActionListener(this);
		
		mt41 = new JMenuItem("字体颜色");
		mt42 = new JMenuItem("背景颜色");
		mt43 = new JMenuItem("关于记事本(About)");
		mt41.addActionListener(this);
		mt42.addActionListener(this);
		mt43.addActionListener(this);
		
		mt51 = new JMenuItem("帮助选项(H)");
		mt51.addActionListener(this);
		
		mu1.add(mt11);
		mu1.add(mt12);
		mu1.add(mt13);
		mu1.add(mt14);
		// 将默认大小的分隔符添加到工具栏的末尾
		mu1.addSeparator();
		mu1.add(mt15);
		mu2.add(mt21);
		mu2.add(mt22);
		mu2.add(mt23);
		mu2.add(mt24);
		mu2.add(mt25);
		mu3.add(mt31);
		mu3.add(mt32);
		mu3.add(mt33);
		mu3.add(mt34);
		mu3.add(mt35);
		mu4.add(mt41);
		mu4.add(mt42);
		mu4.add(mt43);
		mu5.add(mt51);
		group1.add(en_utf_8_Item);
		group1.add(en_gbk_Item);
		group2.add(de_utf_8_Item);
		group2.add(de_gbk_Item);
		mt34.add(en_utf_8_Item);
		mt34.add(en_gbk_Item);
		mt35.add(de_utf_8_Item);
		mt35.add(de_gbk_Item);
		
		bar.add(mu1);
		bar.add(mu2);
		bar.add(mu3);
		bar.add(mu4);
		bar.add(mu5);

		setJMenuBar(bar);
		
	    manager = new UndoManager(); // 撤销的监听器
		area = new JTextArea();
		jsp = new JScrollPane(area);
		jfc = new JFileChooser();
		
		area.setFont(f);
		area.setCaretColor(Color.blue); // 光标颜色 
		area.setSelectedTextColor(Color.blue); // 选中字体颜色 
		area.setSelectionColor(Color.green); // 选中背景颜色
		area.setLineWrap(true); // 是否换行  
		area.setWrapStyleWord(true); // 当输入单词为行末时，换行以显示完整单词
		area.setMargin(new Insets(3, 5, 3, 5)); // 文本区与边框的间距
		add(jsp, BorderLayout.CENTER);
		this.launchTextArea(); 
		// 全局按键事件监听
		this.addKeyListener();
		// 窗口的监听器
		this.myaddWindowListener();
	}
	
	/* 统一设置字体，父界面设置之后，所有由父界面进入的子界面都不需要再次设置字体 */
	private static void InitGlobalFont(Font font) {
		FontUIResource fontRes = new FontUIResource(font);
		//遍历字体集合元素
		for (Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements();) { 
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof FontUIResource) {
				UIManager.put(key, fontRes);
			}
		}
	}

	/* 文件格式过滤器 */
	public class filter extends javax.swing.filechooser.FileFilter // 继承 FileFilter 类
	{
		public boolean accept(File file)
		{
			String name = file.getName();
			name.toString(); // 该字符串中的数字被转换为字符
			
			/* 文件后缀是.txt且是个目录 */
			if (name.endsWith(".txt") || file.isDirectory()) // 判断文件后缀
			{
				return true;
			} else
				return false;
		}
 
		/* 将引用具体子类的子类对象的方法,不可以省略类中的getDescription(),原因是编译器只允许调用在类中声明的方法. */
		public String getDescription() 
		{
			return ".txt"; // 返回文件名
		}
	}
	
	
	/* 文本区域是否更改 */
	private class change implements DocumentListener {
		@Override
		public void insertUpdate(DocumentEvent e) {
			changedUpdate(e);
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			changedUpdate(e);
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			changed();
		}

		public void changed() {
			if(!isOpen) {
				isChanged = true;
				// 统计字数
				labelRight.setText("字数统计："  + tools.replaceBlank(area.getText())); // 调用外部方法
			}
		}
	}

	/* 打开文件保存对话框的方法，保存和另存为时调用此方法 */
	public void openSaveDialog() {
		int status = jfc.showSaveDialog(null);
		BufferedWriter bw = null;
		if (status == JFileChooser.APPROVE_OPTION) {
			file = jfc.getSelectedFile();
			try {
				OutputStreamWriter fWriter = new OutputStreamWriter(new FileOutputStream(file),encode);
				bw = new BufferedWriter(fWriter);
				String[] strs = area.getText().split("\n");
				for (String str : strs) {
					bw.write(str);
					bw.newLine();
					bw.flush();
				}
			} catch (FileNotFoundException e1) {
				area.setText(area.getText() + '\n' + "文件未找到");
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} finally {
				if (bw != null) {
					try {
						bw.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				isChanged = false;
				labelLeft.setText("状态：已保存");
				this.setTitle(file.getName());
			}
		}
	}

	/* 不打开文件保存对话框直接按照当前文件保存的方法 */
	public void onlySave() {
		BufferedWriter bw = null;
		try {
			OutputStreamWriter fWriter = new OutputStreamWriter(new FileOutputStream(file),encode);
			bw = new BufferedWriter(fWriter);
			String[] strs = area.getText().split("\n");
			for (String str : strs) {
				bw.write(str);
				bw.newLine();
				bw.flush();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			isChanged = false;
			labelLeft.setText("状态：已保存");
			this.setTitle(file.getName());
		}
	}

	/* 窗口的监听器，在退出时弹窗 */
	public void myaddWindowListener() {
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				// 如果修改未保存弹出对话框，保存了则直接退出
				if (isChanged == false) {
					System.exit(0);
				} else if (file == null) {
					new JOptionPane();
					int result = JOptionPane.showConfirmDialog(null, "文件还未保存，是否保存", "提示",
							JOptionPane.YES_NO_CANCEL_OPTION);
					// 选择是的时候调用保存文件的文件选择对话框,否的时候直接退出
					if (result == JOptionPane.YES_OPTION) {
						openSaveDialog();
					} else if (result == JOptionPane.NO_OPTION) {
						System.exit(0);
					}
				} else if (file != null) {
					new JOptionPane();
					int result = JOptionPane.showConfirmDialog(null, "文件还未保存，是否保存到" + file.getPath(), "提示",
							JOptionPane.YES_NO_CANCEL_OPTION);
					// 选择是的时候直接保存,否的时候直接退出
					if (result == JOptionPane.YES_OPTION) {
						onlySave();
					} else if (result == JOptionPane.NO_OPTION) {
						System.exit(0);
					}
				}
			}
		});
	}
	
	/* 全局按键事件监听 */
	public void addKeyListener() {
		Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
			public void eventDispatched(AWTEvent event) {
				if (((KeyEvent) event).getID() == KeyEvent.KEY_RELEASED) {
					int key = ((KeyEvent) event).getKeyCode();
					// System.out.println(key);
					if (key == 17) {
						key_ctrl = false; // 初始化 CTRL 状态为 false
					}
				}
				if (((KeyEvent) event).getID() == KeyEvent.KEY_PRESSED) {
					int key = ((KeyEvent) event).getKeyCode();
					// System.out.println(key);
					if (key == 17) {
						key_ctrl = true; // 表示已经按下 CTRL
					}
					if (key == 78 && key_ctrl == true) {
						System.out.println("ctrl + n"); // 控制台输出所按的键
						if (manager.canRedo()) { // 新建文件
							area.setText("");
						}
					}
					if (key == 79 && key_ctrl == true) {
						System.out.println("ctrl + o");
						if (manager.canRedo()) { // 打开文件
							
						}
					}
					if (key == 83 && key_ctrl == true) {
						System.out.println("ctrl + s");
						if (file == null) { // 如果文本为空则打开文件，否则保存文件
							openSaveDialog();
						} else {
							onlySave();
						}
					}
					if (key == 83 && key_ctrl == true) {
						if (key == 16) {
							System.out.println("ctrl + shift + s");
							onlySave(); // 另存为...
						}
						
					}
					if (key == 90 && key_ctrl == true) {
						System.out.println("ctrl + z");
						if (manager.canUndo()) { // 撤销
							manager.undo();
						}
					}
					if (key == 88 && key_ctrl == true) {
						System.out.println("ctrl + x");
						if (manager.canRedo()) { // 剪切
							manager.redo();
						}
					}
					if (key == 67 && key_ctrl == true) {
						System.out.println("ctrl + c");
						if (manager.canRedo()) { // 复制
							manager.redo();
						}
					}
					if (key == 86 && key_ctrl == true) {
						System.out.println("ctrl + v");
						if (manager.canRedo()) { // 粘贴
							manager.redo();
						}
					}
					if (key == 127) {
						System.out.println("Delete");
						if (manager.canRedo()) { // 删除
						    // 删除选定范围内的文本
							area.replaceRange("", area.getSelectionStart(), area.getSelectionEnd());
						}
					}
				}
			}
		}, AWTEvent.KEY_EVENT_MASK);
	}
	
	/* 加载文本域 */
	public void launchTextArea() {
		// 监听状态改变
		area.getDocument().addDocumentListener(new change());
		// 监听撤销
		area.getDocument().addUndoableEditListener(new UndoableEditListener() {// 注册撤销可编辑监听器
			public void undoableEditHappened(UndoableEditEvent e) {
				manager.addEdit(e.getEdit());
			}
		});// 编辑撤销的监听
		// 文本域滚动条
		jsp = new JScrollPane(area);
		//jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); //把滚动条全局作用在视窗 -_-难看
		sysWheel = jsp.getMouseWheelListeners()[0]; // 得到系统滚动事件
		jsp.removeMouseWheelListener(sysWheel); // 移除系统滚动，需要时添加
		jsp.addMouseWheelListener(new event());
		add(jsp, BorderLayout.CENTER);
	}
	
	/* Ctrl + 鼠标改变字体 */
	public class event extends MouseAdapter {
		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			if (e.isControlDown()) {// 当ctrl键被按下，滚动为放大缩小
				// System.out.println(e.getWheelRotation());
				if (e.getWheelRotation() < 0 && f.getSize() < 60) {// 如果滚动条向前就放大文本
					f = new Font(f.getFamily(), f.getStyle(), f.getSize() + 1);
					area.setFont(f);
				} else if (e.getWheelRotation() > 0 && f.getSize() > 0) {// 滚动条向后就缩小文本
					f = new Font(f.getFamily(), f.getStyle(), f.getSize() - 1);
					area.setFont(f);
				}
				labelCenter.setText("字体大小：" + f.getSize());
			} else {// 当ctrl没有被按下，则为系统滚动
				jsp.addMouseWheelListener(sysWheel);
				sysWheel.mouseWheelMoved(e);// 触发系统滚动事件。
				jsp.removeMouseWheelListener(sysWheel);
			}
		}
	}
	
	/* 模拟时钟 */
	class Clock extends Thread
	{ 
		public void run()
		{
			while (true)
			{
				GregorianCalendar time = new GregorianCalendar();
				int year = time.get(Calendar.YEAR);
				int month = time.get(Calendar.MONTH); // 月份是从 0 开始，所以输出时要 + 1
				int day = time.get(Calendar.DAY_OF_MONTH);
				int hour = time.get(Calendar.HOUR_OF_DAY);
				int min = time.get(Calendar.MINUTE);
				int second = time.get(Calendar.SECOND);
				// 设置时间格式完整格式：year + "/" + (month + 1) + "/" + day + "  " + hour + ":" + min + ":" + second
				// 由于太长就不设置了，难看 -_-
				labelLeft.setText(" 当前时间：" + hour + ":" + min + ":" + second);
				
				try
				{
					Thread.sleep(950);
				} 		
				catch (InterruptedException exception){}
			}
		}
	}
	
	/* 底部工具栏 */
	public void launchLabel() {
		jDown = new JToolBar(); // 底部Label的板块
        labelLeft = new JLabel(" 当前时间：" + hour + ":" + min + ":" + second);
		labelCenter = new JLabel("字体大小：" + f.getSize());
		labelRight = new JLabel("字数统计：" );
		jDown.setLayout(new GridLayout(1, 3)); // 三行一列的意思
		jDown.add(labelLeft);
		jDown.add(labelCenter);
		jDown.add(labelRight);
		add(jDown, BorderLayout.SOUTH);
		jDown.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == mt11) // 新建选项
		{
			area.setText("");
		} else if (e.getSource() == mt12) // 打开选项
		{
			JFileChooser fc = new JFileChooser("D:\\");
			int d = fc.showOpenDialog(this);
			if (d == 0)
			{
				try{
					File f = fc.getSelectedFile();
					FileReader fr = new FileReader(f);
					BufferedReader br = new BufferedReader(fr);
					String line = "";
					while ((line = br.readLine()) != null)
					{
						area.append(line + "\n");
					}
					br.close();
					fr.close();
				}
				catch(FileNotFoundException e2)
				{
					System.out.println("文件未找到");
				}
				catch(IOException e3)
				{
					System.out.println("文件无法读取");
				}
			}
			
		} else if (e.getSource() == mt13) // 保存选项
		{
			if (file == null)
				{
					try
					{
						jfc = new JFileChooser();
						jfc.setCurrentDirectory(null);
						jsb = JOptionPane.showInputDialog("请输入文件名：") + ".txt";

						jfc.setSelectedFile(new File(jsb));
						jfc.setFileFilter(new filter());
						int temp = jfc.showSaveDialog(null); // 获取当前对象
						if (temp == jfc.APPROVE_OPTION) // 获得选中的文件对象
						{
							if (file != null)
								file.delete();
							file = new File(jfc.getCurrentDirectory(), jsb);
							file.createNewFile();
							FileWriter fw = new FileWriter(file);
							fw.write(area.getText());
							fw.close();
						}
					} catch (Exception e2)
					{
						JOptionPane.showMessageDialog(null, e);
					}
				} else
				{
					try
					{
						FileWriter fw = new FileWriter(file);
						fw.write(area.getText());
						fw.close();
					} catch (Exception e3)
					{
						JOptionPane.showMessageDialog(null, e);
					}
				}
		} else if(e.getSource() == mt14) // 另存为选项
		{
			jfc = new JFileChooser();
				jfc.setCurrentDirectory(new File("."));
				try
				{
					if (file == null)
					{
						jsb = JOptionPane.showInputDialog("请输入文件名：") + ".txt";
					} else
						jsb = file.getName();
					jfc.setSelectedFile(new File(jsb));
					jfc.setFileFilter(new filter());
					int temp = jfc.showSaveDialog(null);
					if (temp == jfc.APPROVE_OPTION) // 获得选中的文件对象
					{
						if (file != null)
							file.delete();
						file = new File(jfc.getCurrentDirectory(), jsb);
						file.createNewFile();
						FileWriter fw = new FileWriter(file);
						fw.write(area.getText());
						fw.close();
					}
				} catch (Exception e4)
				{
					JOptionPane.showMessageDialog(null, e4);
				}
			
		} else if (e.getSource() == mt15) // 退出选项
		{
			int state = JOptionPane.showConfirmDialog(null, "您确定要退出？退出前请确定您的文件已保存");
			if (state == JOptionPane.OK_OPTION)
				System.exit(0);
		} else if (e.getSource() == mt21) // 撤销选项
		{
			if (manager.canUndo())
					manager.undo();
		} else if (e.getSource() == mt22) // 剪切选项
		{
			area.cut();
		} else if (e.getSource() == mt23) // 复制选项
		{
			area.copy();
		} else if (e.getSource() == mt24) // 粘贴选项
		{
			area.paste();
		} else if (e.getSource() == mt25) // 删除选项 使用空格替换文本
		{
			area.replaceRange("", area.getSelectionStart(), area.getSelectionEnd());
		} else if (e.getSource() == mt31) // 自动换行
		{
			//if (mt31.getState())
				//area.setLineWrap(true);
			//else
				//area.setLineWrap(false);
		} else if (e.getSource() == mt32) // 字体选择
		{
			/* 获取本地图形环境 */
			GraphicsEnvironment gr = GraphicsEnvironment.getLocalGraphicsEnvironment();
			/* 字体名称列表框 */
			JList fontnames = new JList(gr.getAvailableFontFamilyNames());
			/* JScrollPane 管理视口、可选的垂直和水平滚动条以及可选的行和列标题视口 */
			int selection = JOptionPane.showConfirmDialog(null, new JScrollPane(fontnames), "请选择字体",JOptionPane.OK_CANCEL_OPTION);
			Object selectedFont = fontnames.getSelectedValue();
			if (selection == JOptionPane.OK_OPTION && selectedFont != null)
			{
				area.setFont(new Font(fontnames.getSelectedValue().toString(), Font.PLAIN, 20));
			}
		} else if (e.getSource() == mt33) // 查找 || 替换 选项
		{
			JDialog search = new JDialog();
			search.setSize(300, 100);
			search.setLocation(450, 350);
			JLabel label_1 = new JLabel("查找的内容");
			JLabel label_2 = new JLabel("替换的内容");
			JTextField textField_1 = new JTextField(5);
			JTextField textField_2 = new JTextField(5);
			JButton buttonFind = new JButton("查找下一个");
			JButton buttonChange = new JButton("替换");
			JPanel panel = new JPanel(new GridLayout(2, 3));
			panel.add(label_1);
			panel.add(textField_1);
			panel.add(buttonFind);
			panel.add(label_2);
			panel.add(textField_2);
			panel.add(buttonChange);
			search.add(panel);
			search.setVisible(true);
				
			// 为查找下一个 按钮绑定监听事件
			buttonFind.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					String findText = textField_1.getText();// 查找的字符
					String textArea = area.getText();// 当前文本框的内容
 
					start = textArea.indexOf(findText, end);
					end = start + findText.length();
					if (start == -1) // 没有找到
					{
						JOptionPane.showMessageDialog(null, "没找到" + findText, "记事本", JOptionPane.WARNING_MESSAGE);
						area.select(start, end);
					} else
					{
						area.select(start, end);
					}
 
				}
			});
				
			// 为替换按钮绑定监听时间
			buttonChange.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					String changeText = textField_2.getText();// 替换的字符串
					/* 如果选定文件为真 */
					if (area.getSelectionStart() != area.getSelectionEnd())
						area.replaceRange(changeText, area.getSelectionStart(), area.getSelectionEnd());
				}
			});
		} else if (e.getSource() == mt41) // 字体颜色
		{
			JColorChooser cc = new JColorChooser();
			Color c = cc.showDialog(this, "选择颜色", Color.blue);
			area.setForeground(c);
		} else if (e.getSource() == mt42) // 背景颜色 
		{
			JColorChooser cc= new JColorChooser();
			Color c = cc.showDialog(this, "选择颜色", Color.green);
			area.setBackground(c);
		} else if (e.getSource() == mt43) // 关于记事本
		{
			JOptionPane.showMessageDialog(null, "记事本\n开发语言：JAVA\n开发者：赵晓龙\n联系方式：muyiio@outlook.com", "关于",
			JOptionPane.PLAIN_MESSAGE);
		} else if (e.getSource() == mt51) // 帮助选项
		{
			JOptionPane.showMessageDialog(null, "详细代码请移步\n博客：https://yshawlon.cn/", "帮助", JOptionPane.PLAIN_MESSAGE);
		}
	}	
}