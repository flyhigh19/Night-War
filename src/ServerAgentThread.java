import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.net.*;
import java.io.*;

public class ServerAgentThread extends Thread{
	Server father;//���������������father
	Socket sc;//������������������Դ
	boolean flag=true;//�����߳�������
	DataInputStream r;//��������������
	DataOutputStream w;//�������������
	public ServerAgentThread(Server father,Socket sc){
		this.father=father;
		this.sc=sc;
		try{
			r=new DataInputStream(sc.getInputStream());//��������������
			w=new DataOutputStream(sc.getOutputStream());//�������������
		}catch(Exception e){
			e.printStackTrace();//��ӡ�쳣��Ϣ
		}
	}
	public void run(){
		while(flag){
			try{
				String msg=r.readUTF().trim();//ʹ���ַ���msg�����տͻ��˴�������Ϣ
				  //trim()�������ص����ַ��������һ��������������ʼ�ͽ�β�Ŀո񶼱�ɾ��
				if(msg.startsWith("<#NICK_NAME>")){  //startsWith() �������ڼ���ַ����Ƿ���ָ����ǰ׺��ʼ
					this.nick_name(msg);     //    �յ����û�����Ϣ
				}else if(msg.startsWith("<#CLIENT_LEAVE>")){
					this.client_leave(msg);  //  �յ��û����ߵ���Ϣ
				}else if(msg.startsWith("<#TIAO_ZHAN>")){
					this.tiao_zhan(msg);    //�յ��������������ս��Ϣ
				}else if(msg.startsWith("<#TONG_YI>")){
					this.tong_yi(msg);    //���ͶԷ�������ս����Ϣ�������
				}else if(msg.startsWith("<#BUTONG_YI>")){
					this.butong_yi(msg);    //���ͶԷ���ͬ����ս����Ϣ�������
				}else if(msg.startsWith("<#BUSY>")){
					this.busy(msg);     //���ͱ���ս��æ����Ϣ�������
				}else if(msg.startsWith("<#MOVE>")){
					this.move(msg);   //�յ��������Ϣ
				}else if(msg.startsWith("<#RENSHU>")){
					this.submit(msg);   //�յ��Է��������Ϣ
				}
			}catch(Exception e){
				e.printStackTrace();//�����쳣
			}
		}
	}
	public void nick_name(String msg){
		try{
			String name=msg.substring(12);//��ȡ�û���������
			//msg.substring(int beginIndex) ����һ���µ��ַ��������Ǵ��ַ�����һ�����ַ���.���ַ�������Ϊ��beginIndex�±��ʼ��msg�±�����һ���±ꡣ
			this.setName(name);//�ø��ǳƸ����߳�ȡ��
			Vector v=father.onlineList;//��ȡ�����û��б�v
			int size=v.size();//��ȡ���б�Ĵ�С  vector���size()�������ظ���������ǰ�洢��Ԫ�ظ���
			boolean icm=false;//�����ж��Ƿ�����
			for(int i=0;i<size;i++){
				ServerAgentThread sat=(ServerAgentThread)v.get(i);
				if(sat.getName().equals(name)){
					icm=true;
					break;
				}
			}
			if(icm==true){  //������û����ǳ��������û����Ѿ�����
				w.writeUTF("<#CHONG_MING>");//��������Ϣ�����ͻ���
				r.close();
				w.close();//�ر�������
				sc.close();//�ر�Socket
				flag=false;//��ֹ�÷����������߳�
			}else{
				v.add(this);//�����߳���ӵ������б�
				father.refreshList();//ˢ�·����������û��б�
				String nickListmsg=""; //
				size=v.size();//��ȡ�����б��С����
				for(int i=0;i<size;i++){
					ServerAgentThread sat=(ServerAgentThread)v.get(i);//�����￪ʼ�������Ĵ����߳�
					nickListmsg=nickListmsg+"|"+sat.getName();//�������б�������֯���ַ���
				}
				nickListmsg="<#NICK_LIST>"+nickListmsg;//��װ��������
				Vector tempv=father.onlineList;//�������������б���뼯����temp��
				size=tempv.size();//��ȡ��С 
				for(int i=0;i<size;i++){
					ServerAgentThread sat=(ServerAgentThread)tempv.get(i);
					sat.w.writeUTF(nickListmsg);//�����µ��б���Ϣ���͵������ͻ�����
					if(sat!=this){
						sat.w.writeUTF("<#MSG>"+this.getName()+"�����ˣ�");//�������ͻ��˷������û����ߵ���Ϣ
					}
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public void client_leave(String msg){
		try{
			Vector tempv=father.onlineList;//��ȡ�����б�
			tempv.remove(this);//�Ƴ����û�
			int size=tempv.size();//
			String n1="<#NICK_LIST>";//
			for(int i=0;i<size;i++){
				ServerAgentThread sat=(ServerAgentThread)tempv.get(i);
				sat.w.writeUTF("<#MSG>"+this.getName()+"�����ˣ�");
				n1=n1+"|"+sat.getName();
			}
			for(int i=0;i<size;i++){
				ServerAgentThread sat=(ServerAgentThread)tempv.get(i);
				sat.w.writeUTF(n1);
			}
			this.flag=false;//��ֹ�÷����������߳�
			father.refreshList();//���·����������û��б�
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public void tiao_zhan(String msg){
		try{
			String name1=this.getName();//��÷�����ս��Ϣ�û�������
			String name2=msg.substring(12);//��ñ���ս���û����֣��Ǹÿͻ��˷��������ֿ��ܷ����������߳��и��û�û�����ߣ�������Ҫ�����б�
			Vector v=father.onlineList;//��������û����б�
			int size=v.size();//��������û��б�Ĵ�С
			for(int i=0;i<size;i++){  //�����б���������ս���û�
				ServerAgentThread sat=(ServerAgentThread)v.get(i);
				if(sat.getName().equals(name2)){ //����û�������ս��Ϣ�����������ս�ߵ�����
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
			String name=msg.substring(10);//��÷�����ս���û�����
			Vector v=father.onlineList;//��������û����б�
			int size=v.size();//��ȡ�����û��б�Ĵ�С
			for(int i=0;i<size;i++){//��������������Ϣ���û�
				ServerAgentThread sat=(ServerAgentThread)v.get(i);
				if(sat.getName().equals(name)){
					sat.w.writeUTF("<#TONG_YI>");	//����û����ͶԷ�ͬ����ս����Ϣ
					break;
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public void butong_yi(String msg){
		try{
			String name=msg.substring(12);//��÷�����ս���û�����
			Vector v=father.onlineList;//��������û����б�
			int size=v.size();//��ȡ�����û��б�Ĵ�С
			for(int i=0;i<size;i++){//��������������Ϣ���û�
				ServerAgentThread sat=(ServerAgentThread)v.get(i);
				if(sat.getName().equals(name)){
					sat.w.writeUTF("<#BUTONG_YI>");	//����������ͽ��շ���ͬ����ս����Ϣ
					break;
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public void busy(String msg){
		try{
			String name=msg.substring(7);//��÷�����ս���û�����
			Vector v=father.onlineList;//��������û����б�
			int size=v.size();//��ȡ�����û��б�Ĵ�С
			for(int i=0;i<size;i++){//��������������Ϣ���û�
				ServerAgentThread sat=(ServerAgentThread)v.get(i);
				if(sat.getName().equals(name)){
					sat.w.writeUTF("<#BUSY>");	//����������ͽ��շ���æ����Ϣ
					break;
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public void move(String msg){
		try{
			String name=msg.substring(7,msg.length()-4);//��ȡ���շ�������
			Vector v=father.onlineList;//��������û����б�
			int size=v.size();//��ȡ�����û��б�Ĵ�С
			for(int i=0;i<size;i++){//�����������շ�
				ServerAgentThread sat=(ServerAgentThread)v.get(i);
				if(sat.getName().equals(name)){
					sat.w.writeUTF(msg);	//�����������Ϣ�����շ�
					break;
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public void submit(String msg){
		try{
			String name=msg.substring(9);//��ȡ���շ�������
			Vector v=father.onlineList;//��������û����б�
			int size=v.size();//��ȡ�����û��б�Ĵ�С
			for(int i=0;i<size;i++){//�����������շ�
				ServerAgentThread sat=(ServerAgentThread)v.get(i);
				if(sat.getName().equals(name)){
					sat.w.writeUTF(msg);	//����������������Ϣ�����շ�
					break;
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
