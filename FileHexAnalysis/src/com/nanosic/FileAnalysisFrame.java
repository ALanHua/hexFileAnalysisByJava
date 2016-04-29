package com.nanosic;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileAnalysisFrame extends JFrame {
	private JTextField textField;
	private static final long serialVersionUID = 1L;
	private String getReadFilePath;
	public FileHexAnalysis fileAnalysis;
	public FileAnalysisFrame() {
		//文件解析对象
		fileAnalysis=new FileHexAnalysis();
		//JFrame 参数设置
		setTitle("文件copy工具");
		setBounds(100, 100, 500, 375);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//面板参数设置
		final JPanel panel=new JPanel();
		getContentPane().add(panel, BorderLayout.NORTH);
		//文本参数设置
		final JLabel lable=new JLabel();
		lable.setText("文件");
		panel.add(lable);
		//显示参数设置
		textField=new JTextField();
		textField.setColumns(20);//设置列数
		panel.add(textField);
		//按钮参数设置
		final JButton btn=new JButton();
		btn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFileChooser fileChooser=new JFileChooser();
				fileChooser.setFileFilter(new FileNameExtensionFilter("文件类型（.hex)", "hex"));
				int i=fileChooser.showOpenDialog(getContentPane());//弹出一个文件选择框
				if(i==JFileChooser.APPROVE_OPTION){
					File selectedFile=fileChooser.getSelectedFile();
					getReadFilePath=selectedFile.getPath();
					textField.setText(selectedFile.getPath());
				}
			}
		});
		btn.setText("打开文件");
		panel.add(btn);
		//转换按钮参数设置
		final JButton changeBtn=new JButton();
		changeBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					if(fileAnalysis.fileHexAnalysisMethod(getReadFilePath)){
					     JOptionPane.showMessageDialog(null,"转换成功");
					}else{
						JOptionPane.showMessageDialog(null,"转换失败");
					}
				} catch (HeadlessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		changeBtn.setText("转换");
		panel.add(changeBtn);
	}
}
