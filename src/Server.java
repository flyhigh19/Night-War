import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.net.*;
import java.io.*;
//������ͨ������һ���˿ں�socket�ȴ��ͻ��˽��룬��ͨ�����������н��պͷ������Կͻ��˵�����
public class Server extends JFrame implements ActionListener {
	JLabel jb=new JLabel("�˿ں�");//������ʾ����˿ںŵı�ǩjb
	JTextField jtf=new JTextField("9999");//��������˿ںŵ��ı���jtf
	JButton jbStart=new JButton("����");//����������ťjbstart
	JButton jbStop=new JButton("�ر�");//�����رհ�ťjbstop
	JPanel jpl=new JPanel();//����һ��JPanel������
	JList jls=new JList();//����������ʾ��ǰ�û��Ķ����б�jls
	JScrollPane jsp=new JScrollPane(jls);//����������jsp���������б��������
	JSplitPane jspx=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,jsp,jpl);//�������ҷָ�
	ServerSocket s;//����ServerSocket����
	ServerThread st;//�߳������st������
	Vector onlineList=new Vector();//������ŵ�ǰ�����û���Vector����
	public Server(){
		this.initialComponent();//��ʼ���ؼ�
		this.addlistener();//Ϊ��Ӧ�Ŀؼ�ע���¼�������
		this.initialJFrame();//��ʼ������
	}
	public void initialComponent(){//��ʼ���ؼ�����
		 jpl.setLayout(null);//���ղ���
		 jb.setBounds(27,20,50,20);//��ǩλ�úʹ�С
		 jpl.add(jb);//���������ʾ����˵ı�ǩ
		 this.jtf.setBounds(85,20,60,20);
		 jpl.add(this.jtf);//���������ʾ����˿ںŵ��ı���
		 this.jbStart.setBounds(18,50,60,20);
		 jpl.add(this.jbStart);//��ӿ�ʼ��ť
		 this.jbStop.setBounds(85, 50, 60, 20);
		 jpl.add(this.jbStop);//��ӹرհ�ť
		 this.jbStop.setEnabled(false);//���ùرհ�ť����ѡ
	}
	public void addlistener(){
		this.jbStart.addActionListener(this);//Ϊ��ʼ��ť�͹رհ�ťע��ʱ�������
		this.jbStop.addActionListener(this);
	}
	public void initialJFrame(){
		this.setTitle("��ҹ֮ս--��������");//���ô������
		this.add(jspx);//���ָ�����ӵ�������
		jspx.setDividerLocation(250);
		jspx.setDividerSize(4);//���÷ָ��ߵ�λ�úͿ��
		this.setBounds(500,200,420,320);
		this.setVisible(true);//���ô���ɼ���
		this.addWindowListener(  //Ϊ����ر�ע�������
			new WindowAdapter()
			{
				public void WindowClosing(WindowEvent e)
				{
					if(st==null)
					{  //���������߳�Ϊ��ʱֱ���˳�
						System.exit(0); //�˳�
						return;
					}
					try{
						Vector v=onlineList;
						int size=v.size();
						for(int i=0;i<size;i++){  //����Ϊ��ʱ�������û�����������Ϣ
							ServerAgentThread satemp=(ServerAgentThread)v.get(i);//Ĭ����object��
							satemp.w.writeUTF("<#SERVER_DOWN>");
							satemp.flag=false;//��ֹ�����������߳�
						}
						st.flag=false; //��ֹ�������߳�
						st=null;
						s.close();//�ر�serverSocket
						v.clear();//�������û��б����
						refreshList();//ˢ���б�
					}catch(Exception ee){
						ee.printStackTrace();
					}
					System.exit(0);  //�˳�
				}
			}
		);
	}
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==this.jbStart){
			//System.out.println("�����������ɹ�����");//����������ťʱ�������������ɹ�
			this.jbStart_event();
		}else if(e.getSource()==this.jbStop){
			//System.out.println("�����������رգ���");//�����رհ�ťʱ�������������ر�
			this.jbStop_event();
		}
	}
	public void jbStart_event(){ //��������������ť��ҵ�������
		int port=0;
		try{  //�����ߴӷ�����������Ķ˿ںŲ�ת��������
			port=Integer.parseInt(this.jtf.getText().trim());
		}catch(Exception ee){
			//�˿ںŲ���������������ʾ��Ϣ������һ���Ի���
			JOptionPane.showMessageDialog(this,"         �˿ں�ֻ��Ϊ����","����",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if(port>65535||port<0){
			//�˿ںŲ��Ϸ�,������ʾ��Ϣ
			JOptionPane.showMessageDialog(this," �˿ں�ֻ��Ϊ0-65535������ֵ","����",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		try{
			this.jbStart.setEnabled(false);//����ʼ��ť����Ϊ������
			this.jtf.setEnabled(false);//������˿ںŵ��ı�����Ϊ������
			this.jbStop.setEnabled(true);//���رհ�ť����Ϊ����
			s=new ServerSocket(port);//����serversocket����sΨһ��ʾ
			st=new ServerThread(this);//�����������߳�
			st.start();//�����������߳�
			JOptionPane.showMessageDialog(this,"         �����������ɹ�","��ʾ",
					        JOptionPane.INFORMATION_MESSAGE);  //������������������ʾ��Ϣ
		}catch(Exception ee){
			//��������������ʧ�ܵ���ʾ��Ϣ
			JOptionPane.showMessageDialog(this,"         ����������ʧ��","����",
					JOptionPane.ERROR_MESSAGE);
			this.jbStart.setEnabled(true);//����ʼ��ť����Ϊ����
			this.jtf.setEnabled(true);//������˿ںŵ��ı�����Ϊ����
			this.jbStop.setEnabled(false);//���رհ�ť����Ϊ������
		}
	}
	public void jbStop_event(){
		  //�������رա���ť֮���ҵ�������
		try{
			Vector v=onlineList;
			int size=v.size();
			for(int i=0;i<size;i++){
				ServerAgentThread satemp=(ServerAgentThread)v.get(i);//Ĭ����object��
				satemp.w.writeUTF("<#SERVER_DOWN>");
				satemp.flag=false;//��ֹ�����������߳�
			}
			st.flag=false; //��ֹ�������߳�
			st=null;
			s.close();//�ر�Socket
			v.clear();//�������û��б����
			refreshList();//ˢ���б�
			this.jbStart.setEnabled(true);//����ʼ��ť����Ϊ����
			this.jtf.setEnabled(true);//������˿ںŵ��ı�����Ϊ����
			this.jbStop.setEnabled(false);//���رհ�ť����Ϊ������
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
	public void refreshList(){
		 //���������û��б��ҵ�������
		Vector v=new Vector();
		int size=this.onlineList.size();
		for(int i=0;i<size;i++){
			ServerAgentThread satemp1=(ServerAgentThread)this.onlineList.get(i);//Ĭ����object��
	        String temps=satemp1.sc.getInetAddress().toString();
	        temps=temps+"|"+satemp1.getName();  //��ȡ������Ϣ
	        v.add(temps); //����Ϣ��ӵ�Vector��
		}
		this.jls.setListData(v);//�����б�����
	}
	public static void main(String args[]){
		new Server();
	}
	
}
