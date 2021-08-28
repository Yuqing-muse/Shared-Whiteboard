/** 
 * name: Yuqing Chang
 * student number: 1044862
 * username: yuqchang
 */
package client;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import remote.IRemoteBoard;
import java.awt.GridBagLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import java.awt.Insets;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.awt.event.ActionEvent;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import Manager.BoardGUI;
import Manager.Drawing;

import java.awt.SystemColor;
import java.awt.Rectangle;

public class JoinGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	// Canvas default size
	final int CANVAS_WIDTH = 1024;
	final int CANVAS_HEIGHT = 1024;
	static boolean isManager = false;
	public String act = "Line";										// instructions to draw
	private String username;
	private JFrame frame;
	private static DrawEvents drawEvent;
	public final String TITLE = "WhiteBoard";						// system's name
    private JTextArea textArea;
	private JPanel contentPane;										// store drawing canvas
	private JTextField textField;
	private IRemoteBoard board;
	private JList list;
	private Drawing drawingPanel;
	private Thread t;
	
	
	/**
	 * Create the application.
	 */
	public JoinGUI(boolean isManager, String name) {
		this.isManager = isManager;
		this.username = name;
		drawEvent = new DrawEvents();
		initialize();
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setVisible(true);
		frame.setBounds(0, 10, 900, 757);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e)
            {
                System.out.println("Closed");
                try {
					if (board.getExit() == false)
						closeFrame(e);
					else
						e.getWindow().dispose();
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                	
            }
        });
		contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
	        
        frame.setResizable(false);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {30, 30, 30, 30, 30, 0, 30, 30, 30, 0, 30, 30, 30, 0};
		gridBagLayout.rowHeights = new int[] {30, 30, 0, 30, 0, 30, 30, 30, 30, 30, 30, 30, 30, 0, 30, 30, 30, 0, 0, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		frame.getContentPane().setLayout(gridBagLayout);
		
		/**
		 * Set the title of the sysyem's GUI, depending on the identity of user
		 */
		String title;
		if (this.isManager) {
           title = TITLE + " (Manager)";
            frame.setTitle(title);
        } else {
            title = TITLE + " (User)";
            frame.setTitle(title);
        }
        
        JButton lineButton = new JButton("Line");
        lineButton.setActionCommand("Line");
        lineButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		drawEvent.setObjectName("Line");
        	}
        });
        
         JLabel jLabel = new JLabel("Username: " + this.username);
         jLabel.setFont(new Font("Microsoft Tai Le", Font.PLAIN, 14));
         jLabel.setSize(jLabel.getPreferredSize());
         jLabel.setLocation(700, 5);
         GridBagConstraints gbc_jLabel = new GridBagConstraints();
         gbc_jLabel.insets = new Insets(0, 0, 5, 5);
         gbc_jLabel.gridy = 4;
         gbc_jLabel.gridx = 1;
         frame.getContentPane().add(jLabel, gbc_jLabel);
        lineButton.setFont(new Font("Microsoft Tai Le", Font.PLAIN, 14));
        GridBagConstraints gbc_lineButton = new GridBagConstraints();
        gbc_lineButton.insets = new Insets(0, 0, 5, 5);
        gbc_lineButton.gridx = 9;
        gbc_lineButton.gridy = 4;
        frame.getContentPane().add(lineButton, gbc_lineButton);
        
        JButton btnNewButton = new JButton("Pen");
        btnNewButton.setFont(new Font("Microsoft Tai Le", Font.PLAIN, 14));
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		drawEvent.setObjectName("Pen");
        	}
        });
        
        JButton circleButton = new JButton("Circle");
        circleButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		drawEvent.setObjectName("Circle");
        	}
        });
        circleButton.setFont(new Font("Microsoft Tai Le", Font.PLAIN, 14));
        GridBagConstraints gbc_circleButton = new GridBagConstraints();
        gbc_circleButton.insets = new Insets(0, 0, 5, 5);
        gbc_circleButton.gridx = 10;
        gbc_circleButton.gridy = 4;
        frame.getContentPane().add(circleButton, gbc_circleButton);
        GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
        gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
        gbc_btnNewButton.gridx = 11;
        gbc_btnNewButton.gridy = 4;
        frame.getContentPane().add(btnNewButton, gbc_btnNewButton);
        
        JButton btnNewButton_2 = new JButton("Eraser");
        btnNewButton_2.setFont(new Font("Microsoft Tai Le", Font.PLAIN, 14));
        btnNewButton_2.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		drawEvent.setObjectName("Eraser");
        	}
        });
        GridBagConstraints gbc_btnNewButton_2 = new GridBagConstraints();
        gbc_btnNewButton_2.insets = new Insets(0, 0, 5, 0);
        gbc_btnNewButton_2.gridx = 12;
        gbc_btnNewButton_2.gridy = 4;
        frame.getContentPane().add(btnNewButton_2, gbc_btnNewButton_2);
        
        
        
        textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.gridheight = 3;
		gbc_textField.gridwidth = 4;
		gbc_textField.fill = GridBagConstraints.BOTH;
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 18;
		frame.getContentPane().add(textField, gbc_textField);
		textField.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, Color.LIGHT_GRAY));
		textField.setBounds(0, 0, 130, 26);
		textField.setColumns(10);
        
        JButton textButton = new JButton("Text");
        textButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		drawEvent.setObjectName("Text");
        	}
        });
        textButton.setFont(new Font("Microsoft Tai Le", Font.PLAIN, 14));
        GridBagConstraints gbc_textButton = new GridBagConstraints();
        gbc_textButton.insets = new Insets(0, 0, 5, 5);
        gbc_textButton.gridx = 9;
        gbc_textButton.gridy = 6;
        frame.getContentPane().add(textButton, gbc_textButton);
        
        JButton rectangleButton = new JButton("Rectangle");
        rectangleButton.setFont(new Font("Microsoft Tai Le", Font.PLAIN, 14));
        rectangleButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		drawEvent.setObjectName("Rectangle");
        	}
        });
        GridBagConstraints gbc_rectangleButton = new GridBagConstraints();
        gbc_rectangleButton.insets = new Insets(0, 0, 5, 5);
        gbc_rectangleButton.gridx = 10;
        gbc_rectangleButton.gridy = 6;
        frame.getContentPane().add(rectangleButton, gbc_rectangleButton);
        
        JButton btnNewButton_1 = new JButton("Oval");
        btnNewButton_1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		drawEvent.setObjectName("Oval");
        	}
        });
        btnNewButton_1.setFont(new Font("Microsoft Tai Le", Font.PLAIN, 14));
        GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
        gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 5);
        gbc_btnNewButton_1.gridx = 11;
        gbc_btnNewButton_1.gridy = 6;
        frame.getContentPane().add(btnNewButton_1, gbc_btnNewButton_1);
        
        
        drawingPanel = new Drawing(drawEvent);
        
        
        GridBagConstraints gbc_contentPane = new GridBagConstraints();
        gbc_contentPane.insets = new Insets(0, 0, 5, 0);
        gbc_contentPane.gridheight = 21;
        gbc_contentPane.gridwidth = 7;
        gbc_contentPane.fill = GridBagConstraints.BOTH;
        gbc_contentPane.gridx = 6;
        gbc_contentPane.gridy = 7;
        frame.getContentPane().add(contentPane, gbc_contentPane);
		
		drawingPanel.setBounds(10, 50, 570, 500);
		contentPane.add(drawingPanel, BorderLayout.CENTER);	  
		JPanel panel_1 = new JPanel();
		panel_1.setEnabled(false);
		panel_1.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel_1.setBounds(100, 0, 520, 350);
		contentPane.add(panel_1);
		
		 /**
		  * Set color board
		  */
		 JButton btnSel = new JButton("");
		 panel_1.add(btnSel);
		 btnSel.setEnabled(false);
		 btnSel.setBackground(Color.BLACK);
		 btnSel.setBounds(142, 42, 22, 22);
		 btnSel.setOpaque(true);
		 btnSel.setBorderPainted(false);
		
		JLabel lblPalette = new JLabel("Color board");
		panel_1.add(lblPalette);
		lblPalette.setFont(new Font("Microsoft Tai Le", Font.PLAIN, 14));
		lblPalette.setBounds(176, 48, 73, 16);
		
		JButton btnBlue = new JButton("");
		panel_1.add(btnBlue);
		btnBlue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("");
				btnSel.setBackground(Color.BLUE);
				drawingPanel.setColor(Color.BLUE);
			}
		});
		btnBlue.setBounds(243, 42, 22, 22);
		btnBlue.setBackground(Color.BLUE);
		btnBlue.setOpaque(true);
		btnBlue.setBorderPainted(false);
		btnBlue.setOpaque(true);
		btnBlue.setBorderPainted(false);
		btnBlue.setOpaque(true);
		btnBlue.setBorderPainted(false);
		
		JButton btnOrag = new JButton("");
		panel_1.add(btnOrag);
		btnOrag.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("");
				btnSel.setBackground(Color.PINK);
				drawingPanel.setColor(Color.PINK);
			}
		});
		btnOrag.setBounds(271, 42, 22, 22);
		btnOrag.setBackground(Color.PINK);
		btnOrag.setOpaque(true);
		btnOrag.setBorderPainted(false);
		
		
		JButton btnYelo = new JButton("");
		panel_1.add(btnYelo);
		btnYelo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("");
				btnSel.setBackground(Color.YELLOW);
				drawingPanel.setColor(Color.YELLOW);

			}
		});
		btnYelo.setBounds(327, 42, 22, 22);
		btnYelo.setBackground(Color.YELLOW);
		btnYelo.setOpaque(true);
		btnYelo.setBorderPainted(false);
		
		JButton btnGree = new JButton("");
		panel_1.add(btnGree);
		btnGree.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("");
				btnSel.setBackground(Color.GREEN);
				drawingPanel.setColor(Color.GREEN);
			}
		});
		btnGree.setBounds(355, 42, 22, 22);
		btnGree.setBackground(Color.GREEN);
		btnGree.setOpaque(true);
		btnGree.setBorderPainted(false);
		
		JButton btnRed = new JButton("");
		panel_1.add(btnRed);
		btnRed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("");
				btnSel.setBackground(Color.RED);
				drawingPanel.setColor(Color.RED);
			}
		});
		btnRed.setBounds(383, 42, 22, 22);
		btnRed.setBackground(Color.RED);
		btnRed.setOpaque(true);
		btnRed.setBorderPainted(false);
		
		JButton btnOthers = new JButton("More colors");
		btnOthers.setBounds(new Rectangle(10, 0, 0, 0));
		panel_1.add(btnOthers);
		btnOthers.setFont(new Font("Microsoft Tai Le", Font.PLAIN, 14));
		btnOthers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color initial = null;
	            Color selected = JColorChooser.showDialog(null, "Show color chooser", initial);
	            System.out.println("");
	            btnSel.setBackground(selected);
	            btnOthers.setBackground(selected);
	            drawingPanel.setColor(selected);
			}
		});
		btnOthers.setBounds(467, 42, 92, 22);
		btnOthers.setBackground(SystemColor.activeCaption);
		
		JButton btnSend = new JButton("Send");
		btnSend.setFont(new Font("Microsoft Tai Le", Font.PLAIN, 14));
		GridBagConstraints gbc_btnSend = new GridBagConstraints();
		gbc_btnSend.insets = new Insets(0, 0, 5, 5);
		gbc_btnSend.gridx = 4;
		gbc_btnSend.gridy = 21;
		frame.getContentPane().add(btnSend, gbc_btnSend);
		btnSend.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				String Msg = textField.getText();
				try {
					if(!Msg.equals("")) {
						board.sendMessageToUsers(username + " says: " + Msg);
					}
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, "Sorry, the manager has left the room", "Canvas Error", JOptionPane.WARNING_MESSAGE);
					e1.printStackTrace();
					System.exit(0);
				}
				textField.setText(null);;	
			}
		});
		btnSend.setBounds(774, 403, 80, 29);
		
		 JLabel lblChatbox = new JLabel("Chat Room");
        GridBagConstraints gbc_lblChatbox = new GridBagConstraints();
        gbc_lblChatbox.insets = new Insets(0, 0, 5, 5);
        gbc_lblChatbox.gridx = 1;
        gbc_lblChatbox.gridy = 7;
        frame.getContentPane().add(lblChatbox, gbc_lblChatbox);
        lblChatbox.setFont(new Font("Microsoft Tai Le", Font.PLAIN, 14));
        lblChatbox.setBounds(1, 161, 66, 25);
        JScrollPane scroll = new JScrollPane();
        GridBagConstraints gbc_scroll = new GridBagConstraints();
        gbc_scroll.gridheight = 9;
        gbc_scroll.fill = GridBagConstraints.BOTH;
        gbc_scroll.gridwidth = 5;
        gbc_scroll.insets = new Insets(0, 0, 5, 5);
        gbc_scroll.gridx = 1;
        gbc_scroll.gridy = 8;
        frame.getContentPane().add(scroll, gbc_scroll);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        scroll.setBounds(1, 0, 20, 20);
	        
        textArea = new JTextArea();
        scroll.setViewportView(textArea);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setBounds(1, 0, 20, 20);
		
		JLabel lblUsers = new JLabel("All Users");
		GridBagConstraints gbc_lblUsers = new GridBagConstraints();
		gbc_lblUsers.insets = new Insets(0, 0, 5, 5);
		gbc_lblUsers.gridx = 1;
		gbc_lblUsers.gridy = 22;
		frame.getContentPane().add(lblUsers, gbc_lblUsers);
		lblUsers.setFont(new Font("Monaco", Font.PLAIN, 14));
		lblUsers.setBounds(1, 0, 66, 25);
		
		list = new JList();
		GridBagConstraints gbc_list = new GridBagConstraints();
		gbc_list.fill = GridBagConstraints.BOTH;
		gbc_list.gridwidth = 4;
		gbc_list.gridheight = 5;
		gbc_list.insets = new Insets(0, 0, 5, 5);
		gbc_list.gridx = 1;
		gbc_list.gridy = 23;
		frame.getContentPane().add(list, gbc_list);
		list.setBounds(0, 29, 207, 131);
		list.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, Color.LIGHT_GRAY));
	}
	/**
	 * Set user name
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	

    
	protected void closeFrame(WindowEvent e) {
		try {
			board.exit(username);
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		e.getWindow().dispose();
	}
	
	public JTextArea getMsgGUI() {
		return textArea;
	}

	public JList getJlist() {
		return list;
	}

	public Drawing getDraw() {
		return drawingPanel;
	}

	public void setBoard(IRemoteBoard board) {
		this.board = board;
	}

}
