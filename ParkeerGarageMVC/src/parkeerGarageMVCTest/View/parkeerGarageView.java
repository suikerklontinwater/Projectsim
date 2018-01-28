//Ik heb geen idee waarom hij 2x de file opent.
//Het maakt een JFrame aan met een tekstfile die om de zoveel seconde\n
//laat zien hoeveel er auto's er verwacht worden.
package parkeerGarageMVCTest.View;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.util.Timer;

public class parkeerGarageView {

		private JFrame jf;
		private JPanel jp;
		private JButton jb;
		private JTextArea jta;
		
		public parkeerGarageView() {
			gui();
			timers();
		}

		public void gui() {
			//Maakt een JFrame aan met een naam.
			jf = new JFrame("Alle simulaties en tekstvelden");
			jf.setVisible(true);
			jf.setSize(800, 600);
			jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			//Maakt een JPanel aan.
			//jp = new JPanel(new BorderLayout());
			///jp.setLayout(new BoxLayout(jp, BoxLayout.PAGE_AXIS));
			jp = new JPanel();
			jp.setBackground(Color.darkGray);
			//jp.setBorder(BorderFactory.createEmptyBorder(500,500,500,500));
			
			//Maakt een JButton aan met een actionlistener.
			jb = new JButton("Button test");
			jb.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					System.out.println("werkt");			
				}		
			});
			//Zorgt voor de positie van het object.
			GridBagConstraints c = new GridBagConstraints();
			c.insets = new Insets(1000,20,1830,1530);
			c.gridx = 50;
			c.gridy = 20;
			
			//Maakt een JTextarea aan.
			jta = new JTextArea(20, 40);
			Font font = new Font(
					Font.MONOSPACED,
					Font.PLAIN,
					jta.getFont().getSize());
			jta.setFont(font);
			
			//Voegt de button en de textarea met de gridbag toe aan de panel.
			jp.add(jb);
			jp.add(jta, c);
			//voegt de jpanel toe aan de jframe.
			jf.add(jp);

		}
		//Ochtend = 06.00 - 12.00.
		//Middag = 12.00 - 18.00.
		//Avond  = 18.00 - 00.00.
		//Nacht = 00.00 - 06.00.
		public void showMaandag() {
			//setText overwrite de text wat ervoor stond.
			//append zorgt ervoor dat er text wordt bijgeschreven.
			jta.append("Maandag:\n" );
			jta.append("Ochtend: 0-50 auto's.\n");
			jta.append("Middag: ~100~ auto's.\n");
			jta.append("Avond: ~150~ auto's.\n");
			jta.append("Nacht: ~75~ auto's.\n");

		}
		public void showDinsdag() {
			jta.setText("Dinsdag:\n" );
			jta.append("Ochtend: auto's.\n");
			jta.append("Middag: 50 auto's.\n");
			jta.append("Avond: gemiddeld 150 auto's.\n");
			jta.append("Nacht: 50 auto's.\n");
		}
		public void showWoensdag() {
			jta.setText("Woensdag:\n" );
			jta.append("Ochtend: auto's.\n");
			jta.append("Middag: 50 auto's.\n");
			jta.append("Avond: gemiddeld 150 auto's.\n");
			jta.append("Nacht: 50 auto's.\n");
		}
		public void showDonderdag() {
			jta.setText("Donderdag:\n" );
			jta.append("Ochtend: gesloten.\n");
			jta.append("Middag: 50 mensen.\n");
			jta.append("Avond: gemiddeld 150 mensen.\n");
			jta.append("Nacht: 50 mensen.\n");
		}
		public void showVrijdag() {
			jta.setText("Vrijdag:\n" );
			jta.append("Ochtend: gesloten.\n");
			jta.append("Middag: 50 mensen.\n");
			jta.append("Avond: gemiddeld 150 mensen.\n");
			jta.append("Nacht: 50 mensen.\n");
		}
		public void showZaterdag() {
			jta.setText("Zaterdag:\n" );
			jta.append("Ochtend: gesloten.\n");
			jta.append("Middag: 50 mensen.\n");
			jta.append("Avond: gemiddeld 150 mensen.\n");
			jta.append("Nacht: 50 mensen.\n");
		}
		public void showZondag() {
			jta.setText("Zondag:\n" );
			jta.append("Ochtend: \n");
			jta.append("Middag: 50 auto's.\n");
			jta.append("Avond: gemiddeld 150 auto's.\n");
			jta.append("Nacht: 50 auto's.\n");
		}
		
		public void timer() {
			//Maakt een nieuwe timer aan die om de zoveel seconde de volgende dag laat zien.
			Timer t = new Timer();
			t.schedule(new TimerTask() {

			@Override
			public void run() {		
				showMaandag();		
				}		
			}, 1);
		}	
		
			public void timer2() {
				
				Timer t2 = new Timer();
				t2.schedule(new TimerTask(){

					@Override
					public void run() {
						showDinsdag();
					}
				},6000);
			}
			
		public void timer3() {
				
				Timer t3 = new Timer();
				t3.schedule(new TimerTask(){

					@Override
					public void run() {
						showWoensdag();	
					}
				}, 12000);
			}
		
		public void timer4() {
			
			Timer t4 = new Timer();
			t4.schedule(new TimerTask(){

				@Override
				public void run() {
					showDonderdag();			
				}
			}, 18000);		
		}
		
		public void timer5() {
			
			Timer t5 = new Timer();
			t5.schedule(new TimerTask(){

				@Override
				public void run() {
					showVrijdag();			
				}
			}, 24000);		
		}
		
		public void timer6() {
			
			Timer t6 = new Timer();
			t6.schedule(new TimerTask(){

				@Override
				public void run() {
					showZaterdag();				
				}
			}, 30000);	
		}
		
		public void timer7() {
			
			Timer t7 = new Timer();
			t7.schedule(new TimerTask(){

				@Override
				public void run() {
					showZondag();
					t7.cancel();			
				}
			}, 36000);
		}
		//Roep alle timers aan.
		public void timers() {
			timer7();
			timer6();
			timer5();
			timer4();
			timer3();
			timer2();
			timer();
		
	}
}
