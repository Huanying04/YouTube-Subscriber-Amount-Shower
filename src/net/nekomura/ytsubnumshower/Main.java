package net.nekomura.ytsubnumshower;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.Window.Type;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Main {
	static int mouseAtX;
	static int mouseAtY;
	static SystemTray tray = SystemTray.getSystemTray();
	static TrayIcon trayIcon = null;
	public static JLabel headLabel;
	public static JLabel num = new JLabel();
	public static JLabel channelName = new JLabel();
	
	public static void main(String[] args) throws IOException, AWTException {
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension sc = kit.getScreenSize();
		JFrame mainFrame = new JFrame();
		mainFrame.setSize(290,155);
		mainFrame.setType(Type.UTILITY);
		mainFrame.setTitle("YouTube訂閱數顯示器");
		mainFrame.setIconImage(Image.getIconImage().getImage());
		mainFrame.setLocation(39*sc.width/45-290,1*sc.height/9);
		mainFrame.setUndecorated(true);
		mainFrame.setAlwaysOnTop(true);
		mainFrame.setBackground(new Color(0,0,0,0));
		
		trayIcon = new TrayIcon(Image.getIconImage().getImage(), "YouTube訂閱數顯示器", new PopupMenu());
		trayIcon.setImageAutoSize(true);
		tray.add(trayIcon);
		
		mainFrame.addMouseListener(new MouseAdapter() {
                    public void mousePressed(MouseEvent e) 
                    {                 
                       mouseAtX = e.getPoint().x;
                       mouseAtY= e.getPoint().y;
                    }
		});
		
		mainFrame.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
            	mainFrame.setLocation((e.getXOnScreen()-mouseAtX),(e.getYOnScreen()-mouseAtY));
            }
        });
		
		//獲取Config當中的資料
		ConfigUtils.get();
		
		//如果API KEY是空的
		if(ConfigUtils.appid.isEmpty() || ConfigUtils.channelID.isEmpty()) {
			//不得為空的錯誤訊息
			StringBuffer sb = new StringBuffer();
			if(ConfigUtils.appid.isEmpty()) {
				sb.append("API KEY");
			}
			if(ConfigUtils.channelID.isEmpty()) {
				if(sb.toString().isEmpty())
					sb.append("Channel ID");
				else
					sb.append("和Channel ID");
			}
			
			//顯示錯誤視窗
			JOptionPane.showOptionDialog(null, sb + "不得為空", "錯誤", JOptionPane.DEFAULT_OPTION, 
					JOptionPane.ERROR_MESSAGE, null, null, null);
			ConfigUtils.set();
		}
		
		JButton exitButton = new JButton(Image.getCrossButtonImage());
		exitButton.setContentAreaFilled(false);
		exitButton.setBorderPainted(false);
		exitButton.setBounds(270, 0, 20, 20);
		exitButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        System.exit(1);
		    }
		});
		
		JButton setButton = new JButton(Image.getSettingButtonImage());
		setButton.setContentAreaFilled(false);
		setButton.setBorderPainted(false);
		setButton.setBounds(248, 0, 20, 20);
		setButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	try {
		    		ConfigUtils.set();
		    	}catch(IOException ioe) {
		    		
		    	}
		    }
		});
		
		JLabel bgLabel = new JLabel(Image.getBgImage());
		bgLabel.setBounds(50, 20, 240, 135);
		
		JLabel nametagLabel = new JLabel(Image.getStripeImage());
		nametagLabel.setBounds(82, 12, 150, 40);
		
		//YouTube相關
		num = new JLabel();
		num.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 28));
		num.setForeground(new Color(255, 255, 255));
		num.setHorizontalAlignment(JLabel.CENTER);
		num.setBounds(50, 20, 240, 135);
		
		channelName = new JLabel();
		channelName.setFont(new Font("Microsoft JhengHei", channelName.getFont().getStyle(), 20));
		channelName.setForeground(new Color(0, 0, 0));
		channelName.setHorizontalAlignment(JLabel.LEFT);
		channelName.setBounds(90, 12, 140, 40);
		
		
		if(APIUtils.getSubscriberAmountinInt(ConfigUtils.appid, ConfigUtils.channelID) != -1 &&
				APIUtils.getSubscriberAmountinInt(ConfigUtils.appid, ConfigUtils.channelID) != -2 &&
				APIUtils.getSubscriberAmountinInt(ConfigUtils.appid, ConfigUtils.channelID) != -3) { //如果沒有API ERROR
			num.setText(NumberFormat.getInstance().format(APIUtils.getSubscriberAmountinInt(ConfigUtils.appid, ConfigUtils.channelID)));
			
			headLabel = new JLabel(new ImageIcon(ImageUtils.getFromURL(APIUtils.getAvatarURL())));
			headLabel.setBounds(0, 0, new ImageIcon(ImageUtils.getFromURL(APIUtils.getAvatarURL())).getIconWidth(), new ImageIcon(ImageUtils.getFromURL(APIUtils.getAvatarURL())).getIconHeight());
			
			channelName.setText(APIUtils.getChannelName());
		}else { //如果有API ERROR
			JOptionPane.showOptionDialog(null, "API傳回錯誤！\n請更正設定內容，否則將可能導致程式無法使用。", "錯誤", JOptionPane.DEFAULT_OPTION, 
					JOptionPane.ERROR_MESSAGE, null, null, null);
			ConfigUtils.set();
			
			headLabel = new JLabel(Image.getNoAdvatarImage());
			headLabel.setBounds(0, 0, 88, 88);
			
			num.setText("???");
			
			channelName.setText("???");
		}
		
		JPanel p = new JPanel();
		p.setLayout(null);
		p.add(headLabel);
		p.add(num);
		p.add(channelName);
		p.add(nametagLabel);
		p.add(exitButton);
		p.add(setButton);
		p.add(bgLabel);
		p.setOpaque(false);
		mainFrame.getContentPane().add(p);
		mainFrame.setVisible(true);
		
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				try {
					if(APIUtils.getSubscriberAmountinInt(ConfigUtils.appid, ConfigUtils.channelID) != -1 &&
							APIUtils.getSubscriberAmountinInt(ConfigUtils.appid, ConfigUtils.channelID) != -2 &&
							APIUtils.getSubscriberAmountinInt(ConfigUtils.appid, ConfigUtils.channelID) != -3) { //如果沒有API ERROR
						headLabel.setIcon(new ImageIcon(ImageUtils.getFromURL(APIUtils.getAvatarURL())));
						num.setText(NumberFormat.getInstance().format(APIUtils.getSubscriberAmountinInt(ConfigUtils.appid, ConfigUtils.channelID)));
						channelName.setText(APIUtils.getChannelName());
					}
				}catch(Throwable err) {
					
				}
 			}
		}, 0L, (long)ConfigUtils.refreshFreq);
	}
}