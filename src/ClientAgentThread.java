import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.net.*;

public class ClientAgentThread extends Thread {   //单独开辟一个客户端线程用来区别每一个连线的用户
	XiangQi father;  //客户端主类声明
	DataInputStream  r;//数据输入流声明
	DataOutputStream w;//数据输出流声明
	boolean flag=true;  //控制线程的标志位
	String tiaozhanzhe=null; //设置一个挑战者表示是否空闲
	public ClientAgentThread(XiangQi father){  //构造器
		this.father=father;
		try{
			r=new DataInputStream(father.sc.getInputStream());  //创建数据输入流
			w=new DataOutputStream(father.sc.getOutputStream()); //创建数据输出流
			String name=father.nck.getText().trim();    //获得昵称
			w.writeUTF("<#NICK_NAME>"+name);     //向服务器发送昵称
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void run(){
		while(flag){
			try{
				String msg=r.readUTF().trim();   //用msg来接收服务器传来的信息
				if(msg.startsWith("<#CHONG_MING>")){   //如果重名
					this.chong_ming();
				}else if(msg.startsWith("<#NICK_LIST>")){ //获得昵称列表
					this.nc_list(msg);
				}else if(msg.startsWith("<#SERVER_DOWN>")){  //服务器人为关闭
					this.server_down();
				}else if(msg.startsWith("<#TIAO_ZHAN>")){    //XXX向你发起挑战
					this.tiao_zhan(msg);
				}else if(msg.startsWith("<#TONG_YI>")){      //同意XXX的挑战
					this.tong_yi();
				}else if(msg.startsWith("<#BUTONG_YI>")){    //不同意XXX的挑战
					this.butong_yi();
				}else if(msg.startsWith("<#BUSY>")){     //对方繁忙
					this.busy();
				}else if(msg.startsWith("<#RENSHU>")){    //您获胜，对方认输
					this.renshu();
				}else if(msg.startsWith("<#MOVE>")){      //开始走棋
					this.move(msg);
				}
				}catch(Exception ee){
					ee.printStackTrace();
			}
		}
	}
	public void chong_ming(){   //如果重名，事件的处理代码
		try{
			JOptionPane.showMessageDialog(this.father,"该玩家已经存在，请重新填写！","错误",
					JOptionPane.ERROR_MESSAGE);  //给出该玩家已存在的提示信息
			r.close();  //关闭数据流（客户端资源关闭）
			w.close();
			this.father.zjmk.setEnabled(true);   //将”主机名“文本框设置为可用
			this.father.nck.setEnabled(true);    //将”昵称名“文本框设置为可用
			this.father.dkhk.setEnabled(true);   //将”端口号“文本框设置为可用
			this.father.lj.setEnabled(true);    //将”连接“按钮设置为可用
			this.father.dk.setEnabled(false);   //将”断开“按钮设置为不可用
			this.father.tz.setEnabled(false);   //将”挑战“按钮设置为不可用
			this.father.rs.setEnabled(false);   //将”认输“按钮设置为不可用
			this.father.jstz.setEnabled(false); //将”接受挑战“按钮设置为不可用
			this.father.jjtz.setEnabled(false); //将”拒绝挑战“按钮设置为不可用
			father.sc.close();  //关闭socket连接
			father.sc=null;    
			father.cat=null;
			flag=false;  //终止该客户端线程
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
	public void nc_list(String msg){     //获得昵称列表(有参方法)
		String s=msg.substring(12);  //获得用户信息
		String fj[]=s.split("\\|");  //用\\|进行分解得到主机名和昵称
		Vector v=new Vector();  //创建一个集合类对象v
		for(int i=0;i<fj.length;i++){
			if(fj[i].trim().length()!=0&&(!fj[i].trim().equals(father.nck.getText().trim()))){
				v.add(fj[i]);      //将昵称全部添加到Vector中
			}
		}
		father.xllb.setModel(new DefaultComboBoxModel(v));   //设置下拉列表的值
	}
	public void server_down(){    //服务器人为的关闭后事件处理代码
		this.father.zjmk.setEnabled(true);   //将”主机名“文本框设置为可用
		this.father.nck.setEnabled(true);    //将”昵称名“文本框设置为可用
		this.father.dkhk.setEnabled(true);   //将”端口号“文本框设置为可用
		this.father.lj.setEnabled(true);    //将”连接“按钮设置为可用
		this.father.dk.setEnabled(false);   //将”断开“按钮设置为不可用
		this.father.tz.setEnabled(false);   //将”挑战“按钮设置为不可用
		this.father.rs.setEnabled(false);   //将”认输“按钮设置为不可用
		this.father.jstz.setEnabled(false); //将”接受挑战“按钮设置为不可用
		this.father.jjtz.setEnabled(false); //将”拒绝挑战“按钮设置为不可用
		this.flag=false;    
		this.father.cat=null;//终止客户端代理线程
		JOptionPane.showMessageDialog(this.father,"服务器已停止！！！","提示",
				JOptionPane.INFORMATION_MESSAGE);  //给出服务器已停止的提示信息
	}
	public void tiao_zhan(String msg){
		try{
			String name=msg.substring(12);   //获得挑战者的昵称
			if(this.tiaozhanzhe==null){
				    tiaozhanzhe=msg.substring(12);   //获得挑战者的昵称
				    this.father.zjmk.setEnabled(false);   //将”主机名“文本框设置为不可用
					this.father.nck.setEnabled(false);    //将”昵称名“文本框设置为不可用
					this.father.dkhk.setEnabled(false);   //将”端口号“文本框设置为不可用
					this.father.lj.setEnabled(false);    //将”连接“按钮设置为不可用
					this.father.dk.setEnabled(false);   //将”断开“按钮设置为不可用
					this.father.tz.setEnabled(false);   //将”挑战“按钮设置为不可用
					this.father.rs.setEnabled(false);   //将”认输“按钮设置为不可用
					this.father.jstz.setEnabled(true); //将”接受挑战“按钮设置为可用
					this.father.jjtz.setEnabled(true); //将”拒绝挑战“按钮设置为可用
					JOptionPane.showMessageDialog(this.father,tiaozhanzhe+"向你挑战","提示",
							JOptionPane.INFORMATION_MESSAGE);  //给出XXX挑战者向你挑战的提示信息
			}
			else{   //如果该玩家忙碌中，则返回一个以<#BUSY>为开头的信息
				this.w.writeUTF("<#BUSY>"+name);
			}
		}catch(IOException ee){
			ee.printStackTrace();
		}
	}
	public void tong_yi(){   //同意挑战后的事件处理代码
		this.father.nck.setEnabled(false);    //将”昵称名“文本框设置为不可用
		this.father.dkhk.setEnabled(false);   //将”端口号“文本框设置为不可用
		this.father.lj.setEnabled(false);    //将”连接“按钮设置为不可用
		this.father.dk.setEnabled(true);   //将”断开“按钮设置为可用
		this.father.tz.setEnabled(false);   //将”挑战“按钮设置为不可用
		this.father.rs.setEnabled(true);   //将”认输“按钮设置为可用
		this.father.jstz.setEnabled(false); //将”接受挑战“按钮设置为不可用
		this.father.jjtz.setEnabled(false); //将”拒绝挑战“按钮设置为不可用
		JOptionPane.showMessageDialog(this.father,"对方同意接受您的挑战，您先走（红棋）","提示",
				JOptionPane.INFORMATION_MESSAGE);  //给出XXX挑战者向你挑战的提示信息
	}
	public void butong_yi(){   //对方不同意您的挑战事件的业务处理代码
		this.father.cp=false;  //棋盘不可用
		this.father.color=0;   //您变为红方
		this.father.nck.setEnabled(false);    //将”昵称名“文本框设置为不可用
		this.father.dkhk.setEnabled(false);   //将”端口号“文本框设置为不可用
		this.father.lj.setEnabled(false);    //将”连接“按钮设置为不可用
		this.father.dk.setEnabled(true);   //将”断开“按钮设置为可用
		this.father.tz.setEnabled(true);   //将”挑战“按钮设置为可用
		this.father.rs.setEnabled(false);   //将”认输“按钮设置为不可用
		this.father.jstz.setEnabled(false); //将”接受挑战“按钮设置为不可用
		this.father.jjtz.setEnabled(false); //将”拒绝挑战“按钮设置为不可用
		JOptionPane.showMessageDialog(this.father,"对方不同意接受您的挑战","提示",
				JOptionPane.INFORMATION_MESSAGE);  //给出XXX挑战者向你挑战的提示信息
		this.tiaozhanzhe=null;
	}
	public void busy(){
		this.father.cp=false;  //棋盘不可用
		this.father.color=0;   //您变为红方
		this.father.nck.setEnabled(false);    //将”昵称名“文本框设置为不可用
		this.father.dkhk.setEnabled(false);   //将”端口号“文本框设置为不可用
		this.father.lj.setEnabled(false);    //将”连接“按钮设置为不可用
		this.father.dk.setEnabled(true);   //将”断开“按钮设置为可用
		this.father.tz.setEnabled(true);   //将”挑战“按钮设置为可用
		this.father.rs.setEnabled(false);   //将”认输“按钮设置为不可用
		this.father.jstz.setEnabled(false); //将”接受挑战“按钮设置为不可用
		this.father.jjtz.setEnabled(false); //将”拒绝挑战“按钮设置为不可用
		JOptionPane.showMessageDialog(this.father,"对方忙碌中！！","提示",
				JOptionPane.INFORMATION_MESSAGE);  //给出XXX挑战者向你挑战的提示信息
		this.tiaozhanzhe=null;
	}
	public void renshu(){
		JOptionPane.showMessageDialog(this.father,"恭喜你，你获胜，对方认输！！","提示",
				JOptionPane.INFORMATION_MESSAGE);  //给出XXX挑战者向你挑战的提示信息	
		this.tiaozhanzhe=null;
		this.father.cp=false;  //棋盘不可用
		this.father.color=0;   //您变为红方
		this.father.next();    //开始进行下一把
		this.father.nck.setEnabled(false);    //将”昵称名“文本框设置为不可用
		this.father.dkhk.setEnabled(false);   //将”端口号“文本框设置为不可用
		this.father.lj.setEnabled(false);    //将”连接“按钮设置为不可用
		this.father.dk.setEnabled(true);   //将”断开“按钮设置为可用
		this.father.tz.setEnabled(true);   //将”挑战“按钮设置为可用
		this.father.rs.setEnabled(false);   //将”认输“按钮设置为不可用
		this.father.jstz.setEnabled(false); //将”接受挑战“按钮设置为不可用
		this.father.jjtz.setEnabled(false); //将”拒绝挑战“按钮设置为不可用
	}
	public void move(String msg){
		int length=msg.length();
		int startI=Integer.parseInt(msg.substring(length-4,length-3));  //获得棋子的原始位置
		int startJ=Integer.parseInt(msg.substring(length-3,length-2));
		int endI=Integer.parseInt(msg.substring(length-2,length-1));  //获得棋子走后的位置
		int endJ=Integer.parseInt(msg.substring(length-1));
		this.father.jpz.move(startI,startJ,endI,endJ);
		this.father.cp=true;   //将走棋变成true
	}
}
