import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.net.*;

public class XiangQi extends JFrame implements ActionListener{
	public static final Color bgcolor=new Color(0,0,0); //���̵ı���ɫ���� final���ܱ��̳У�����������
	public static final Color focusbg=new Color(242,242,242); //����ѡ��֮�����ɫ
	public static final Color focusz=new Color(96,65,91);  //����ѡ��֮�������ֵ���ɫ
	public static final Color color1=new Color(0,0,205); //�췽��ɫ
	public static final Color color2=new Color(255,0,0);  //�׷���ɫ
	JLabel zjm=new JLabel("������");  //���ñ�ǩ����������zjm
	JLabel dkh=new JLabel("�˿ں�");  //���ñ�ǩ���˿ںš�dkh
	JLabel nc=new JLabel("�ǳ�");  //���ñ�ǩ���ǳơ�nc
	JTextField zjmk=new JTextField("127.0.0.1"); //�����������ı���zjmk
	JTextField dkhk=new JTextField("9999");  //���ö˿ں��ı���dkhk
	JTextField nck=new JTextField("play1");  //�����ǳ��ı���nck
	JButton lj=new JButton("����");   //���÷��������Ӱ�ťlj
	JButton dk=new JButton("�Ͽ�");   //���÷������Ͽ���ťdk
	JComboBox xllb=new JComboBox();  //������ʾ�û��������б��
	JButton tz=new JButton("��ս");  //������ս��ťtz
	JButton rs=new JButton("����");  //�������䰴ťrs
	JButton jstz=new JButton("������ս");  //���ý�����ս��ťjstz
	JButton jjtz=new JButton("�ܾ���ս");  //���þܾ���ս��ťjjtz
	int width=60;  //��������֮�����ߵľ���
	qizi qizi[][]=new qizi[9][10];  //������������
	QiPan jpz=new QiPan(qizi,width,this);//��������
	//JPanel qp=new JPanel();  //��һ��JPanel����������ʾ
	JPanel jp=new JPanel();  //����һ����JPanel
	JSplitPane jsp=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,jpz,jp); //�����������ҷָ�
	boolean cp=false;  //�Ƿ�����ı�־λ�����̲����ã�
	int color=0;//0����췽��1����׷�
	Socket sc;//����Socket����
	ClientAgentThread cat;//�����ͻ��˴����̵߳�����
	public XiangQi(){
		this.initialComponent(); //��ʼ���ؼ�
		this.addListener(); //Ϊ��Ӧ�ؼ�ע��ʱ�������
		this.initialState(); //��ʼ��״̬
		this.initialqizi();  //��ʼ������
		this.initialFrame(); //��ʼ������
	}
	public void initialComponent(){
		 jp.setLayout(null);//���ղ���
		 this.zjm.setBounds(25,10,50,20);
		 jp.add(this.zjm);  //��������ǩ��Сλ������
		 this.zjmk.setBounds(80,10,90,20);
		 jp.add(this.zjmk); //�������ı����С��λ������/
		 this.dkh.setBounds(25,40,50,20);
		 jp.add(this.dkh);  //���ö˿ںű�ǩλ�úʹ�С
		 this.dkhk.setBounds(80,40,90,20);
		 jp.add(this.dkhk);  //���ö˿ں��ı����λ�úʹ�С/
		 this.nc.setBounds(25,70,50,20);
		 jp.add(this.nc);   //�����ǳƱ�ǩ��λ�úʹ�С
		 this.nck.setBounds(80,70,90,20);
		 jp.add(this.nck);   //�����ǳ��ı����λ�úʹ�С/
		 this.lj.setBounds(10,100,80,20);
		 jp.add(this.lj);   //�������Ӱ�ť��λ�úʹ�С
		 this.dk.setBounds(100,100,80,20);
		 jp.add(this.dk);   //���öϿ���ť��λ�úʹ�С
		 this.xllb.setBounds(32,130,130,20);
		 jp.add(this.xllb);  //������������λ�úʹ�С/
		 this.tz.setBounds(10,160,80,20);
		 jp.add(this.tz);   //������ս��ť��λ�úʹ�С
		 this.rs.setBounds(100,160,80,20);
		 jp.add(this.rs);   //�������䰴ť��λ�úʹ�С
		 this.jstz.setBounds(5,190,86,20);
		 jp.add(this.jstz);  //���ý�����ս��ť��λ�úʹ�С
		 this.jjtz.setBounds(100,190,86,20);
		 jp.add(this.jjtz);  //���þܾ���ս��ť��λ�úʹ�С
		 jp.setLayout(null);//��������Ϊ�ղ���
		 jp.setBounds(0,0,700,700);//���ô�С
	}
	public void addListener(){
		this.lj.addActionListener(this);   //Ϊ�����ӡ���ťע���¼�������
		this.dk.addActionListener(this);   //Ϊ���Ͽ�����ťע���¼�������
		this.tz.addActionListener(this);   //Ϊ����ս����ťע���¼�������
		this.rs.addActionListener(this);   //Ϊ�����䡱��ťע���¼�������
		this.jstz.addActionListener(this); //Ϊ��������ս����ťע���¼�������
		this.jjtz.addActionListener(this); //Ϊ���ܾ���ս����ťע���¼������� 
	}
	public void initialState(){
		this.dk.setEnabled(false);   //�����Ͽ�����ť����Ϊ������
		this.tz.setEnabled(false);   //������ս����ť����Ϊ������
		this.rs.setEnabled(false);   //�������䡰��ť����Ϊ������
		this.xllb.setEnabled(false); //�������б������Ϊ������
		this.jstz.setEnabled(false); //����������ս����ť����Ϊ������
		this.jjtz.setEnabled(false); //�����ܾ���ս����ť����Ϊ������
	}
	public void initialqizi(){
		//��ʼ����������
		qizi[0][0]=new qizi(color1,"܇",0,0);
		qizi[1][0]=new qizi(color1,"�R",1,0);
		qizi[2][0]=new qizi(color1,"��",2,0);
		qizi[3][0]=new qizi(color1,"ʿ",3,0);
		qizi[4][0]=new qizi(color1,"��",4,0);
		qizi[5][0]=new qizi(color1,"ʿ",5,0);
		qizi[6][0]=new qizi(color1,"��",6,0);
		qizi[7][0]=new qizi(color1,"�R",7,0);
		qizi[8][0]=new qizi(color1,"܇",8,0);
		qizi[1][2]=new qizi(color1,"��",1,2);
		qizi[7][2]=new qizi(color1,"��",7,2);
		qizi[0][3]=new qizi(color1,"��",0,4);
		qizi[2][3]=new qizi(color1,"��",2,4);
		qizi[4][3]=new qizi(color1,"��",4,4);
		qizi[6][3]=new qizi(color1,"��",6,4);
		qizi[8][3]=new qizi(color1,"��",8,4);
		qizi[0][6]=new qizi(color2,"��",0,6);
		qizi[2][6]=new qizi(color2,"��",2,6);
		qizi[4][6]=new qizi(color2,"��",4,6);
		qizi[6][6]=new qizi(color2,"��",6,6);
		qizi[8][6]=new qizi(color2,"��",8,6);
		qizi[1][7]=new qizi(color2,"��",1,7);
		qizi[7][7]=new qizi(color2,"��",7,7);
		qizi[0][9]=new qizi(color2,"܇",0,9);
		qizi[1][9]=new qizi(color2,"�R",1,9);
		qizi[2][9]=new qizi(color2,"��",2,9);
		qizi[3][9]=new qizi(color2,"ʿ",3,9);
		qizi[4][9]=new qizi(color2,"��",4,9);
		qizi[5][9]=new qizi(color2,"ʿ",5,9);
		qizi[6][9]=new qizi(color2,"��",6,9);
		qizi[7][9]=new qizi(color2,"�R",7,9);
		qizi[8][9]=new qizi(color2,"܇",8,9);
	}
	public void initialFrame(){
		this.setTitle("��ҹ֮ս�������ͻ���");  //���ô������
		this.add(this.jsp);  //����������ҷָ����
		jsp.setDividerLocation(698);
		jsp.setDividerSize(7);  //���÷ָ���λ�úͿ��
		this.setBounds(185,0,912,730);  //���ô����С
		this.setVisible(true);   //���ô���ɼ�
		this.addWindowListener(   //��������Ӽ�����
				new WindowAdapter()
				{
					public void windowClosing(WindowEvent e)
					{
						if(cat==null)//�ͻ��˴����߳�Ϊ�գ�ֱ���˳�
						{
							System.exit(0);//�˳�
							return;
						}
						try
						{
							if(cat.tiaozhanzhe!=null)//����������
							{
								try
								{
									//����������Ϣ
									cat.w.writeUTF("<#RENSHU>"+cat.tiaozhanzhe);
								}
								catch(Exception ee)
								{
									ee.printStackTrace();
								}
							}
							cat.w.writeUTF("<#CLIENT_LEAVE>");//������������뿪��Ϣ
							cat.flag=false;//��ֹ�ͻ��˴����߳�
							cat=null;
							
						}
						catch(Exception ee)
						{
							ee.printStackTrace();
						}
						System.exit(0);//�˳�
					}
					
				}
				);
	}
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==this.lj){  //���������ӡ���ťʱ
			this.lj_event();
		}else if(e.getSource()==this.dk){  //�������Ͽ�����ťʱ
			this.dk_event();
		}else if(e.getSource()==this.tz){  //��������ս����ťʱ
			this.tz_event();
		}else if(e.getSource()==this.rs){  //���������䡱��ťʱ
			this.rs_event();
		}else if(e.getSource()==this.jstz){  //������������ս����ťʱ
			this.jstz_event();
		}else if(e.getSource()==this.jjtz){   //�������ܾ���ս����ťʱ
			this.jjtz_event();
	    }
	}
	public void lj_event(){      //�����ӡ���ť��ҵ�������
		int port=0;
		try{  //��ȡ�û�����Ķ˿ںŲ�ת��������
			port=Integer.parseInt(this.dkhk.getText().trim());
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
		String name=this.nck.getText().trim();  //����ǳ�
		if(name.length()==0){
			JOptionPane.showMessageDialog(this,"          �ǳƲ���Ϊ��","����0",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		try{//����socket�˿� д������������ 	�������������ͺͶ�ȡ��ȡ����˵���Ϣ
			sc=new Socket(this.zjmk.getText().trim(),port);  //�������Ͷ˿ں�
			//Socket�ǽ�����������ʱʹ�õġ������ӳɹ�ʱ��Ӧ�ó������˶������һ��Socketʵ��
			cat=new ClientAgentThread(this);//�����ͻ��˴����߳�	
			cat.start();//�����ͻ��˴����߳�	
			this.zjmk.setEnabled(false);   //�������������ı�������Ϊ������
			this.nck.setEnabled(false);    //�����ǳ������ı�������Ϊ������
			this.dkhk.setEnabled(false);   //�����˿ںš��ı�������Ϊ������
			this.lj.setEnabled(false);   //�������ӡ���ť����Ϊ������
			this.dk.setEnabled(true);    //�����Ͽ�����ť����Ϊ����
			this.xllb.setEnabled(true);  //�������б������Ϊ����
			this.tz.setEnabled(true);    //������ս����ť����Ϊ����
			this.rs.setEnabled(false);   //�������䡰��ť����Ϊ������
			this.jstz.setEnabled(false); //����������ս����ť����Ϊ������
			this.jjtz.setEnabled(false); //�����ܾ���ս����ť����Ϊ������
			JOptionPane.showMessageDialog(this,"         �����ӵ�������","��ʾ",
			        JOptionPane.INFORMATION_MESSAGE);  //�������������ӳɹ�����ʾ��Ϣ
		}catch(Exception ee){
			JOptionPane.showMessageDialog(this,"         ����������ʧ��","����",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
	}
	public void dk_event(){  //���Ͽ�����ť��ҵ�������
		try{
			this.cat.w.writeUTF("<#CLIENT_LEAVE>");//������������뿪����Ϣ
			this.cat.flag=false;//��ֹ�ͻ��˴����߳�
			this.cat=null;
			this.zjmk.setEnabled(true);   //�������������ı�������Ϊ����
			this.nck.setEnabled(true);    //�����ǳ������ı�������Ϊ����
			this.dkhk.setEnabled(true);   //�����˿ںš��ı�������Ϊ����
			this.lj.setEnabled(true);    //�������ӡ���ť����Ϊ����
			this.dk.setEnabled(false);   //�����Ͽ�����ť����Ϊ������
			this.xllb.setEnabled(false); //�������б������Ϊ������
			this.tz.setEnabled(false);   //������ս����ť����Ϊ������
			this.rs.setEnabled(false);   //�������䡰��ť����Ϊ������
			this.jstz.setEnabled(false); //����������ս����ť����Ϊ������
			this.jjtz.setEnabled(false); //�����ܾ���ս����ť����Ϊ������
		}catch(Exception ee){
			ee.printStackTrace();	
		}
	}
	public void tz_event(){   //��ս֮���ҵ�������
		//����û�ѡ�е���ս����
		Object o=this.xllb.getSelectedItem();
		if((o==null)&&((String)o).equals("")){
			JOptionPane.showMessageDialog(this,"          ��ѡ�жԷ�����","����",
					JOptionPane.ERROR_MESSAGE);
		}else{
			String name2=(String)this.xllb.getSelectedItem();  //�����ս����
			try{
				this.zjmk.setEnabled(false);  //�������������ı�������Ϊ������
				this.nck.setEnabled(false);   //�����ǳ������ı�������Ϊ������
				this.dkhk.setEnabled(false);  //�����˿ںš��ı�������Ϊ������
				this.lj.setEnabled(false);   //�������ӡ���ť����Ϊ������
				this.dk.setEnabled(false);   //�����Ͽ�����ť����Ϊ������
				this.xllb.setEnabled(true); //�������б������Ϊ����
				this.tz.setEnabled(false);   //������ս����ť����Ϊ������
				this.rs.setEnabled(false);   //�������䡰��ť����Ϊ������
				this.jstz.setEnabled(false); //����������ս����ť����Ϊ������
				this.jjtz.setEnabled(false); //�����ܾ���ս����ť����Ϊ������
				this.cat.tiaozhanzhe=name2;//������ս����
				this.cp=true; //���̿���
				this.color=0; //��color����Ϊ0
				this.cat.w.writeUTF("<#TIAO_ZHAN>"+name2);//������ս��Ϣ
				JOptionPane.showMessageDialog(this,"       �������ս���ȴ��Է���Ӧ","��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
			}catch(Exception ee){
				ee.printStackTrace();
			}
		}
	}
	public void rs_event(){   //�����������Ϣ
		try{
			this.cat.w.writeUTF("<#RENSHU>"+this.cat.tiaozhanzhe);
			this.cat.tiaozhanzhe=null;//��tiaoZhanZhe��Ϊ��
			this.color=0;//��color��Ϊ0
			this.cp=false;//��caiPan��Ϊfalse
			this.next();//��ʼ����һ��
			this.zjmk.setEnabled(false);  //�������������ı�������Ϊ������
			this.nck.setEnabled(false);   //�����ǳ������ı�������Ϊ������
			this.dkhk.setEnabled(false);  //�����˿ںš��ı�������Ϊ������
			this.lj.setEnabled(false);    //�������ӡ���ť����Ϊ������
			this.dk.setEnabled(true);     //�����Ͽ�����ť����Ϊ����
			this.xllb.setEnabled(true);   //�������б������Ϊ����
			this.tz.setEnabled(true);     //������ս����ť����Ϊ����
			this.rs.setEnabled(false);    //�������䡰��ť����Ϊ������
			this.jstz.setEnabled(false);  //����������ս����ť����Ϊ������
			this.jjtz.setEnabled(false);  //�����ܾ���ս����ť����Ϊ������
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
	public void jstz_event(){  //ͬ����ս����Ϣ
		try{
			this.cat.w.writeUTF("<#TONG_YI>"+this.cat.tiaozhanzhe);
			this.cp=false;//��caiPan��Ϊfalse
			this.color=1;//��color��Ϊ1
			this.zjmk.setEnabled(false);   //�������������ı�������Ϊ������
			this.nck.setEnabled(false);    //�����ǳ������ı�������Ϊ������
			this.dkhk.setEnabled(false);   //�����˿ںš��ı�������Ϊ������
			this.lj.setEnabled(false);   //�������ӡ���ť����Ϊ������
			this.dk.setEnabled(false);   //�����Ͽ�����ť����Ϊ������
			this.xllb.setEnabled(true);  //�������б������Ϊ����
			this.tz.setEnabled(false);   //������ս����ť����Ϊ������
			this.rs.setEnabled(true);    //�������䡰��ť����Ϊ����
			this.jstz.setEnabled(false); //����������ս����ť����Ϊ������
			this.jjtz.setEnabled(false); //�����ܾ���ս����ť����Ϊ������
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
	public void jjtz_event(){   //���;ܾ���ս����Ϣ
		try{
			this.cat.w.writeUTF("<#BUTONG_YI>"+this.cat.tiaozhanzhe);
			this.cat.tiaozhanzhe=null;//��tiaoZhanZhe��Ϊ��
			this.zjmk.setEnabled(false);   //�������������ı�������Ϊ������
			this.nck.setEnabled(false);    //�����ǳ������ı�������Ϊ������
			this.dkhk.setEnabled(false);   //�����˿ںš��ı�������Ϊ������
			this.lj.setEnabled(false);   //�������ӡ���ť����Ϊ������
			this.dk.setEnabled(true);    //�����Ͽ�����ť����Ϊ����
			this.xllb.setEnabled(true);  //�������б������Ϊ����
			this.tz.setEnabled(true);    //������ս����ť����Ϊ����
			this.rs.setEnabled(false);   //�������䡰��ť����Ϊ����
			this.jstz.setEnabled(false); //����������ս����ť����Ϊ������
			this.jjtz.setEnabled(false); //�����ܾ���ս����ť����Ϊ������
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
	public void next(){    //�ָ���ʼ������״̬
		for(int i=0;i<9;i++){
			for(int j=0;i<9;i++){
				this.qizi[i][j]=null;
			}
		}
		this.cp=false;   //���̲�����
		this.initialqizi();   //���³�ʼ������
		this.repaint();      //�ػ棨��һ���ػ�һ�����̣�
	}
	public static void main(String args[]){ 
		new XiangQi();
	}
}
