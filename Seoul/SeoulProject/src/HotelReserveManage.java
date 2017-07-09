import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class HotelReserveManage extends JFrame implements ActionListener{
	JButton[] bt=new JButton[4];
	String[] str= {"공지사항관리", "예약리스트", "지점별 예약 차트", "로그아웃"};
	
	public HotelReserveManage() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("호텔예약관리");
		
		JPanel p=new JPanel();
		for(int i=0; i<4; i++) {
			bt[i]=new JButton(str[i]);
			p.add(bt[i]);
		}
		p.setBorder(new EmptyBorder(0, 15, 0, 15));
		add(p);
		pack();
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==bt[0])
	}

}
