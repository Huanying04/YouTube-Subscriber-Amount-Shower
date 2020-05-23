package net.nekomura.ytsubnumshower;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.json.JSONObject;

public class ConfigUtils {
	
	public static String appid = new String();
	public static String channelID = new String();
	public static int refreshFreq;
	
	public static void get() throws IOException {
		File config = new File("config\\config.conf");
		String content = FileUtils.readFileToString(config, "utf-8");
		JSONObject configJSON = new JSONObject(content);
		appid = configJSON.getString("appid");
		channelID = configJSON.getString("cid");
		refreshFreq = configJSON.getInt("refreshFreq");
	}
	
	public static void set() throws IOException{
		    JLabel appidLabel = new JLabel("API Key:", SwingConstants.RIGHT);
		    JComboBox<String> channelIDCombo = new JComboBox<String>();
		    ((JLabel)channelIDCombo.getRenderer()).setHorizontalAlignment(SwingConstants.RIGHT);
		    channelIDCombo.addItem("頻道ID:");
		    channelIDCombo.addItem("用戶ID:");
		    JLabel refreshFreqLabel = new JLabel("更新頻率(毫秒/次):", SwingConstants.RIGHT);
		    
		    JTextField appidField = new JTextField(appid, 35);
		    JTextField channelIDField = new JTextField(channelID, 35);
		    JTextField refreshFreqField = new JTextField(String.valueOf(refreshFreq), 35);
		    
		    JButton okButton = new JButton("OK");
		    okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		    
			Container cp = new Container();
			cp.setLayout(new GridBagLayout());
			cp.setBackground(UIManager.getColor("control"));
			GridBagConstraints c = new GridBagConstraints();
			
			c.gridx = 0;
		    c.gridy = GridBagConstraints.RELATIVE;
		    c.gridwidth = 1;
		    c.gridheight = 1;
		    c.insets = new Insets(2, 2, 2, 2);
		    c.anchor = GridBagConstraints.EAST;
		    
		    cp.add(appidLabel, c);
		    cp.add(channelIDCombo, c);
		    cp.add(refreshFreqLabel, c);

		    c.gridx = 1;
		    c.gridy = 0;
		    c.weightx = 1.0;
		    c.fill = GridBagConstraints.HORIZONTAL;
		    c.anchor = GridBagConstraints.CENTER;

		    cp.add(appidField, c);
		    c.gridx = 1;
		    c.gridy = GridBagConstraints.RELATIVE;
		    cp.add(channelIDField, c);
		    cp.add(refreshFreqField, c);
		    c.weightx = 0.0;
		    c.fill = GridBagConstraints.NONE;
		    
		    int result = JOptionPane.showOptionDialog(null, cp,"設定",JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, null, null);
		    if (result == 0) {
		    	if (appidField.getText().isEmpty()) {
		    		JOptionPane.showMessageDialog(null, "API Key不得為空。", "錯誤", JOptionPane.WARNING_MESSAGE);
		    		set();
		    		return;
		    	}else if (((String)channelIDCombo.getSelectedItem()).equals("頻道ID:") && channelIDField.getText().isEmpty()) {
		    		JOptionPane.showMessageDialog(null, "頻道ID不得為空。", "錯誤", JOptionPane.WARNING_MESSAGE);
		    		set();
		    		return;
		    	}else if (((String)channelIDCombo.getSelectedItem()).equals("用戶ID:") && channelIDField.getText().isEmpty()) {
		    		JOptionPane.showMessageDialog(null, "用戶ID不得為空。", "錯誤", JOptionPane.WARNING_MESSAGE);
		    		set();
		    		return;
		    	}else if (refreshFreqField.getText().isEmpty()) {
		    		JOptionPane.showMessageDialog(null, "更新頻率不得為空。", "錯誤", JOptionPane.WARNING_MESSAGE);
		    		set();
		    		return;
		    	}else if (!(NumberUtils.isCreatable(refreshFreqField.getText()))) {
		    		JOptionPane.showMessageDialog(null, "更新頻率只得輸入數字。", "錯誤", JOptionPane.WARNING_MESSAGE);
		    		set();
		    		return;
		    	}else if (!refreshFreqField.getText().matches("[0-9]+") || Integer.valueOf(refreshFreqField.getText()) <= 0) { 
		    		JOptionPane.showMessageDialog(null, "更新頻率只得輸入正整數。", "錯誤", JOptionPane.WARNING_MESSAGE);
		    		set();
		    		return;
		    	}
		    	
		    	appid = appidField.getText();
		    	if(((String)channelIDCombo.getSelectedItem()).equals("頻道ID:"))
		    		channelID = channelIDField.getText();
		    	else if(((String)channelIDCombo.getSelectedItem()).equals("用戶ID:"))
		    		channelID = APIUtils.userIDtoChannelID(channelIDField.getText());
		    	refreshFreq = Integer.valueOf( refreshFreqField.getText());
		    	
		    	if(APIUtils.getSubscriberAmountinInt(ConfigUtils.appid, ConfigUtils.channelID) == -1 ||
						APIUtils.getSubscriberAmountinInt(ConfigUtils.appid, ConfigUtils.channelID) == -2 ||
						APIUtils.getSubscriberAmountinInt(ConfigUtils.appid, ConfigUtils.channelID) == -3) {
					JOptionPane.showOptionDialog(null, "API傳回錯誤！\n請更正設定內容，否則將可能導致程式無法使用。", "錯誤", JOptionPane.DEFAULT_OPTION, 
							JOptionPane.ERROR_MESSAGE, null, null, null);
					Main.headLabel = new JLabel(Image.getNoAdvatarImage());
					Main.headLabel.setBounds(0, 0, 88, 88);
					
					Main.num.setText("???");
					
					Main.channelName.setText("???");
					ConfigUtils.set();
				}
		    	
		    	write();
		    }
	}
	
	public static void write() {
		try {
    		File configFile = new File("config\\config.conf");
    		configFile.createNewFile();
    		OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream("config\\config.conf"),"UTF-8");
    		JSONObject configJSON = new JSONObject();
    		configJSON.put("appid", appid);
    		configJSON.put("cid", channelID);
    		configJSON.put("refreshFreq", refreshFreq);
    		out.write(configJSON.toString());
    		out.close();
    	}catch (IOException exc) {
    		
    	}
	}
}