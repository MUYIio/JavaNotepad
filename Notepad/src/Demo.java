import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

class Demo
{
	public static void main(String [] args)
	{
		// 生成Windows风格的UI界面
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		new NotePad();
	}
}