import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.net.*;
import java.io.*;

public class ServerAgentThread extends Thread{
	Server father;//声明服务器类对象father
	Socket sc;//给数据流分配带宽和资源
	boolean flag=true;//代理线程生命期
	DataInputStream r;//定义数据输入流
	DataOutputStream w;//定义数据输出流
	public ServerAgentThread(Server father,Socket sc){
		this.father=father;
		this.sc=sc;
		try{
			r=new DataInputStream(sc.getInputStream());//创建数据输入流
			w=new DataOutputStream(sc.getOutputStream());//创建数据输出流
		}catch(Exception e){
			e.printStackTrace();//打印异常信息
		}
	}
	public void run(){
		while(flag){
			try{
				String msg=r.readUTF().trim();//使用字符串msg来接收客户端传来的信息
				  //trim()方法返回调用字符串对象的一个副本，所有起始和结尾的空格都被删除
				if(msg.startsWith("<#NICK_NAME>")){  //startsWith() 方法用于检测字符串是否以指定的前缀开始
					this.nick_name(msg);     //    收到新用户的信息
				}else if(msg.startsWith("<#CLIENT_LEAVE>")){
					this.client_leave(msg);  //  收到用户离线的信息
				}else if(msg.startsWith("<#TIAO_ZHAN>")){
					this.tiao_zhan(msg);    //收到提出方发出的挑战信息
				}else if(msg.startsWith("<#TONG_YI>")){
					this.tong_yi(msg);    //发送对方接受挑战的信息给提出方
				}else if(msg.startsWith("<#BUTONG_YI>")){
					this.butong_yi(msg);    //发送对方不同意挑战的信息给提出方
				}else if(msg.startsWith("<#BUSY>")){
					this.busy(msg);     //发送被挑战者忙的信息给提出方
				}else if(msg.startsWith("<#MOVE>")){
					this.move(msg);   //收到走棋的信息
				}else if(msg.startsWith("<#RENSHU>")){
					this.submit(msg);   //收到对方认输的消息
				}
			}catch(Exception e){
				e.printStackTrace();//处理异常
			}
		}
	}
	public void nick_name(String msg){
		try{
			String name=msg.substring(12);//获取用户的主机名
			//msg.substring(int beginIndex) 返回一个新的字符串，它是此字符串的一个子字符串.子字符串内容为从beginIndex下标后开始到msg下标的最后一个下标。
			this.setName(name);//用该昵称给该线程取名
			Vector v=father.onlineList;//获取在线用户列表v
			int size=v.size();//获取该列表的大小  vector类的size()函数返回该容器对象当前存储的元素个数
			boolean icm=false;//用来判断是否重名
			for(int i=0;i<size;i++){
				ServerAgentThread sat=(ServerAgentThread)v.get(i);
				if(sat.getName().equals(name)){
					icm=true;
					break;
				}
			}
			if(icm==true){  //如果新用户的昵称在在线用户里已经存在
				w.writeUTF("<#CHONG_MING>");//将重名信息发给客户端
				r.close();
				w.close();//关闭数据流
				sc.close();//关闭Socket
				flag=false;//终止该服务器代理线程
			}else{
				v.add(this);//将该线程添加到在线列表
				father.refreshList();//刷新服务器在线用户列表
				String nickListmsg=""; //
				size=v.size();//获取在线列表大小长度
				for(int i=0;i<size;i++){
					ServerAgentThread sat=(ServerAgentThread)v.get(i);//从这里开始分配具体的代理线程
					nickListmsg=nickListmsg+"|"+sat.getName();//将在线列表内容组织成字符串
				}
				nickListmsg="<#NICK_LIST>"+nickListmsg;//封装在线内容
				Vector tempv=father.onlineList;//将服务器最新列表存入集合类temp中
				size=tempv.size();//获取大小 
				for(int i=0;i<size;i++){
					ServerAgentThread sat=(ServerAgentThread)tempv.get(i);
					sat.w.writeUTF(nickListmsg);//将最新的列表信息发送到各个客户端上
					if(sat!=this){
						sat.w.writeUTF("<#MSG>"+this.getName()+"上线了！");//给其他客户端发送新用户上线的信息
					}
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public void client_leave(String msg){
		try{
			Vector tempv=father.onlineList;//获取在线列表
			tempv.remove(this);//移除该用户
			int size=tempv.size();//
			String n1="<#NICK_LIST>";//
			for(int i=0;i<size;i++){
				ServerAgentThread sat=(ServerAgentThread)tempv.get(i);
				sat.w.writeUTF("<#MSG>"+this.getName()+"下线了！");
				n1=n1+"|"+sat.getName();
			}
			for(int i=0;i<size;i++){
				ServerAgentThread sat=(ServerAgentThread)tempv.get(i);
				sat.w.writeUTF(n1);
			}
			this.flag=false;//终止该服务器代理线程
			father.refreshList();//更新服务器在线用户列表
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public void tiao_zhan(String msg){
		try{
			String name1=this.getName();//获得发出挑战信息用户的名字
			String name2=msg.substring(12);//获得被挑战的用户名字（是该客户端发出的名字可能服务器代理线程中该用户没有上线，所以需要遍历列表）
			Vector v=father.onlineList;//获得在线用户的列表
			int size=v.size();//获得在线用户列表的大小
			for(int i=0;i<size;i++){  //遍历列表，搜索被挑战的用户
				ServerAgentThread sat=(ServerAgentThread)v.get(i);
				if(sat.getName().equals(name2)){ //向该用户发送挑战信息，附带提出挑战者的名字
					sat.w.writeUTF("<#TIAO_ZHAN>"+name1);
					break;
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public void tong_yi(String msg){
		try{
			String name=msg.substring(10);//获得发出挑战的用户名字
			Vector v=father.onlineList;//获得在线用户的列表
			int size=v.size();//获取在线用户列表的大小
			for(int i=0;i<size;i++){//遍历搜索发出信息的用户
				ServerAgentThread sat=(ServerAgentThread)v.get(i);
				if(sat.getName().equals(name)){
					sat.w.writeUTF("<#TONG_YI>");	//向该用户发送对方同意挑战的信息
					break;
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public void butong_yi(String msg){
		try{
			String name=msg.substring(12);//获得发出挑战的用户名字
			Vector v=father.onlineList;//获得在线用户的列表
			int size=v.size();//获取在线用户列表的大小
			for(int i=0;i<size;i++){//遍历搜索发出信息的用户
				ServerAgentThread sat=(ServerAgentThread)v.get(i);
				if(sat.getName().equals(name)){
					sat.w.writeUTF("<#BUTONG_YI>");	//向提出方发送接收方不同意挑战的信息
					break;
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public void busy(String msg){
		try{
			String name=msg.substring(7);//获得发出挑战的用户名字
			Vector v=father.onlineList;//获得在线用户的列表
			int size=v.size();//获取在线用户列表的大小
			for(int i=0;i<size;i++){//遍历搜索发出信息的用户
				ServerAgentThread sat=(ServerAgentThread)v.get(i);
				if(sat.getName().equals(name)){
					sat.w.writeUTF("<#BUSY>");	//向提出方发送接收方正忙的信息
					break;
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public void move(String msg){
		try{
			String name=msg.substring(7,msg.length()-4);//获取接收方的名字
			Vector v=father.onlineList;//获得在线用户的列表
			int size=v.size();//获取在线用户列表的大小
			for(int i=0;i<size;i++){//遍历搜索接收方
				ServerAgentThread sat=(ServerAgentThread)v.get(i);
				if(sat.getName().equals(name)){
					sat.w.writeUTF(msg);	//发送走棋的消息给接收方
					break;
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public void submit(String msg){
		try{
			String name=msg.substring(9);//获取接收方的名字
			Vector v=father.onlineList;//获得在线用户的列表
			int size=v.size();//获取在线用户列表的大小
			for(int i=0;i<size;i++){//遍历搜索接收方
				ServerAgentThread sat=(ServerAgentThread)v.get(i);
				if(sat.getName().equals(name)){
					sat.w.writeUTF(msg);	//发送提出方认输的消息给接收方
					break;
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
