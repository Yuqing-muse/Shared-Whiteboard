/** 
 * name: Yuqing Chang
 * student number: 1044862
 * username: yuqchang
 */
package Manager;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import remote.IRemoteBoard;
import remote.IRemoteUser;
import java.awt.GridBagLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;

import java.awt.Insets;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Locale;
import java.awt.event.ActionEvent;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import client.DrawEvents;

import java.awt.SystemColor;
import java.awt.Rectangle;

public class BoardGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	// Canvas default size
	final int CANVAS_WIDTH = 1024;
	final int CANVAS_HEIGHT = 1024;
	static boolean isManager = true;
	private String username;
	public String act = "Line";													// instructions to draw
	private JFrame frame;
	private static DrawEvents drawEvent;
	public final String TITLE = "WhiteBoard";									// system's name
    private JTextArea textArea;
	private JPanel contentPane;													// store drawing canvas
	private JTextField textField;
	private IRemoteBoard board;
	private JList list;
	private Drawing drawingPanel;
	private IRemoteUser remoteUserList;
	private JFileChooser fileChooser;
	private File file;															// file to store
	private String path;
    private Thread t;
    Locale locale;
	/**
	 * Create the application.
	 */
	public BoardGUI(boolean isManager, String name) {
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
		frame.setBounds(0, 10, 900, 757);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e)
            {
                System.out.println("Closed");
                closeFrame(e);
                e.getWindow().dispose();
            }
        });
		contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
	      
        frame.setResizable(false);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {30, 30, 30, 30, 30, 0, 30, 30, 30, 0, 30, 30, 30, 0};
		gridBagLayout.rowHeights = new int[] {30, 30, 0, 30, 0, 0, 0, 0, 30, 30, 30, 30, 30, 30, 30, 30, 0, 30, 30, 30, 0, 0, 0, 30, 30, 0, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		frame.getContentPane().setLayout(gridBagLayout);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		mnFile.setFont(new Font("Monaco", Font.PLAIN, 14));
		menuBar.add(mnFile);
		
		fileChooser = new JFileChooser();
		JMenuItem open = new JMenuItem("Open");
		mnFile.add(open);
		open.setFont(new Font("Monaco", Font.PLAIN, 14));
		open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Set up the file chooser.
				if (fileChooser == null) {
					fileChooser = new JFileChooser();
					//(Accept All) file filter.
					fileChooser.addChoosableFileFilter(new MyFilter());
					fileChooser.setAcceptAllFileFilterUsed(false);
					
					//Add custom icons for file types.
					fileChooser.setFileView(new ImageFile());
				
				}
			
				//Show it.
				int returnVal = fileChooser.showOpenDialog(null);
				
				//Process the results.
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
				    try {
	                	BufferedImage bufImage = ImageIO.read(file);
	                	drawingPanel.load(bufImage);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	                drawingPanel.updateBoard();
					
					//Reset the file chooser for the next time it's shown.
					fileChooser.setSelectedFile(null);
				}
		}
		});
		JMenuItem newBoard = new JMenuItem("New");
		mnFile.add(newBoard);
		newBoard.setFont(new Font("Monaco", Font.PLAIN, 14));
		newBoard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 if (fileChooser == null) {
					 fileChooser = new JFileChooser();
					 locale = Locale.US;
				    //Add a custom file filter and disable the default
				    //(Accept All) file filter.
					 fileChooser.addChoosableFileFilter(new MyFilter());
					 fileChooser.setAcceptAllFileFilterUsed(false);

				    //Add custom icons for file types.
					 fileChooser.setFileView(new ImageFile());

			        }
				// Ask if you currently need to save
				int ifSave = JOptionPane.showConfirmDialog(null,"Do you want to save current board first?","Instruction", JOptionPane.YES_NO_OPTION);
				// yes
				if(ifSave == 0) {
					
					if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) { 
		                fileChooser.setCurrentDirectory(new File("."));	              
    	                String extension;
    	                try {
    	                	extension = ((MyFilter)fileChooser.getFileFilter()).getExtension(file);
    	                }
    	                catch(Exception e2) {
    	                	extension = ".jpg";
    	                }
    	                file = fileChooser.getSelectedFile();
		                File newFile = null;
		                try {
	                	   if (file.getAbsolutePath().toUpperCase().endsWith(extension.toUpperCase())) {
	                		    newFile = file;
	                		    path = file.getAbsolutePath();
	                	   } else {
	                		    newFile = new File(file.getAbsolutePath() + extension);
	                		    path = file.getAbsolutePath() + extension;
	                	   }
		                   extension = extension.substring(1);
						   ImageIO.write(drawingPanel.save(), extension, newFile);
						   JOptionPane.showMessageDialog(null, "Already save", "Information", JOptionPane.INFORMATION_MESSAGE);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			
						path = null;
						drawingPanel.clear();
						drawingPanel.repaint();
			            drawingPanel.updateBoard();
					
				}
		});
		
		JMenuItem saveAs = new JMenuItem("Save as");
		mnFile.add(saveAs);
		saveAs.setFont(new Font("Monaco", Font.PLAIN, 14));
		saveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 if (fileChooser == null) {
					 fileChooser = new JFileChooser();

					 fileChooser.addChoosableFileFilter(new MyFilter());
					 fileChooser.setAcceptAllFileFilterUsed(false);

				    //Add icons for file types.
					 fileChooser.setFileView(new ImageFile());
			        }
				 
				if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) { 
	                fileChooser.setCurrentDirectory(new File("."));	              
	                String extension;
	                try {
	                	File file = fileChooser.getSelectedFile();
	                	extension = ((MyFilter)fileChooser.getFileFilter()).getExtension(file);
	                }
	                catch(Exception e1) {
	                	extension = ".jpg";
	                }
	                file = fileChooser.getSelectedFile();
	                File newFile = null;
	                try {
	                	  if (file.getAbsolutePath().toUpperCase().endsWith(extension.toUpperCase())) {
	                		    newFile = file;
	                		    path = file.getAbsolutePath();
	                		  } else {
	                		    newFile = new File(file.getAbsolutePath() + extension);
	                		    path = file.getAbsolutePath() + extension;
	                		  }
	                	extension = extension.substring(1);
						ImageIO.write(drawingPanel.save(), extension, newFile);
						JOptionPane.showMessageDialog(null, "Already save", "Information", JOptionPane.INFORMATION_MESSAGE);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					}
				}
			});
		JMenuItem save = new JMenuItem("Save");
		mnFile.add(save);
		save.setFont(new Font("Monaco", Font.PLAIN, 14));
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 if (fileChooser == null) {
					 fileChooser = new JFileChooser();

					 fileChooser.addChoosableFileFilter(new MyFilter());
					 fileChooser.setAcceptAllFileFilterUsed(false);

				    //Add icons for file types.
					 fileChooser.setFileView(new ImageFile());
			        }
				 if(path != null)
					 file = new File(path);
				 else {
					if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) { 
		                fileChooser.setCurrentDirectory(new File("."));	              
		                String extension;
		                try {
		                	File file = fileChooser.getSelectedFile();
		                	extension = ((MyFilter)fileChooser.getFileFilter()).getExtension(file);
		                }
		                catch(Exception e1) {
		                	extension = ".jpg";
		                }
		                file = fileChooser.getSelectedFile();
		                File newFile = null;
		                try {
		                	  if (file.getAbsolutePath().toUpperCase().endsWith(extension.toUpperCase())) {
		                		    newFile = file;
		                		    path = file.getAbsolutePath();
		                		  } else {
		                		    newFile = new File(file.getAbsolutePath() + extension);
		                		    path = file.getAbsolutePath() + extension;
		                		  }
		                	extension = extension.substring(1);
							ImageIO.write(drawingPanel.save(), extension, newFile);
							JOptionPane.showMessageDialog(null, "Already save", "Information", JOptionPane.INFORMATION_MESSAGE);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					return;
				}
				 try {
	                	String[] paths = path.split("\\.");
	                	int l = paths.length - 1;
						ImageIO.write(drawingPanel.save(), paths[l],file);
						JOptionPane.showMessageDialog(null, "Already save", "Information", JOptionPane.INFORMATION_MESSAGE);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			}
		
			});
		
		JMenuItem close = new JMenuItem("Close");
		mnFile.add(close);
		close.setFont(new Font("Monaco", Font.PLAIN, 14));
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (fileChooser == null) {
					 fileChooser = new JFileChooser();

				    //Add a custom file filter and disable the default
				    //(Accept All) file filter.
					 fileChooser.addChoosableFileFilter(new MyFilter());
					 fileChooser.setAcceptAllFileFilterUsed(false);

				    //Add custom icons for file types.
					 fileChooser.setFileView(new ImageFile());

			        }
				
				int ifSave = JOptionPane.showConfirmDialog(null,"Do you want to save current board first?","Instruction", JOptionPane.YES_NO_OPTION);
				// yes
				if(ifSave == 0) {
					
					if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) { 
		                fileChooser.setCurrentDirectory(new File("."));	              
   	                String extension;
   	                try {
   	                	extension = ((MyFilter)fileChooser.getFileFilter()).getExtension(file);
   	                }
   	                catch(Exception e2) {
   	                	extension = ".jpg";
   	                }
   	                file = fileChooser.getSelectedFile();
		                File newFile = null;
		                try {
	                	   if (file.getAbsolutePath().toUpperCase().endsWith(extension.toUpperCase())) {
	                		    newFile = file;
	                		    path = file.getAbsolutePath();
	                	   } else {
	                		    newFile = new File(file.getAbsolutePath() + extension);
	                		    path = file.getAbsolutePath() + extension;
	                	   }
		                   extension = extension.substring(1);
						   ImageIO.write(drawingPanel.save(), extension, newFile);
						   JOptionPane.showMessageDialog(null, "Already save", "Information", JOptionPane.INFORMATION_MESSAGE);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			
						path = null;
						try {
							board.close();
						} catch (RemoteException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						System.exit(0);
					
				}
		});
		JMenu kick = new JMenu("Kick Out");
		kick.setFont(new Font("Monaco", Font.PLAIN, 14));
		menuBar.add(kick);
		
		
		JMenuItem kickOut = new JMenuItem("Choose a user");
		kick.add(kickOut);
		kickOut.setFont(new Font("Monaco", Font.PLAIN, 14));
		kickOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String kickedName = JOptionPane.showInputDialog( "Please input the username of user you want to kick out." );
				try {
					board.removeUser(kickedName, isManager);
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					System.out.println("Encounter an error when kick out the user.");
					//e1.printStackTrace();
				}
            }
		});
		
		String title;
		if (this.isManager) {
            // setup manager's app name
           title = TITLE + " (Manager)";
            frame.setTitle(title);
        } else {
            // setup normal user's app name
            title = TITLE + " (User)";
            frame.setTitle(title);
        }
        
        JButton lineButton = new JButton("Line");
        lineButton.setActionCommand("Line");
        lineButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		//drawEvent.drawLine()
        		drawEvent.setObjectName("Line");
        	}
        });
        
        JLabel jLabel = new JLabel("Username: " + this.username);
         jLabel.setFont(new Font("Microsoft Tai Le", Font.PLAIN, 14));
         jLabel.setSize(jLabel.getPreferredSize());
         jLabel.setLocation(700, 5);
         GridBagConstraints gbc_jLabel = new GridBagConstraints();
         gbc_jLabel.insets = new Insets(0, 0, 5, 5);
         gbc_jLabel.gridy = 7;
         gbc_jLabel.gridx = 1;
         frame.getContentPane().add(jLabel, gbc_jLabel);
        lineButton.setFont(new Font("Microsoft Tai Le", Font.PLAIN, 14));
        GridBagConstraints gbc_lineButton = new GridBagConstraints();
        gbc_lineButton.insets = new Insets(0, 0, 5, 5);
        gbc_lineButton.gridx = 9;
        gbc_lineButton.gridy = 7;
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
        gbc_circleButton.fill = GridBagConstraints.BOTH;
        gbc_circleButton.insets = new Insets(0, 0, 5, 5);
        gbc_circleButton.gridx = 10;
        gbc_circleButton.gridy = 7;
        frame.getContentPane().add(circleButton, gbc_circleButton);
        GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
        gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
        gbc_btnNewButton.gridx = 11;
        gbc_btnNewButton.gridy = 7;
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
        gbc_btnNewButton_2.gridy = 7;
        frame.getContentPane().add(btnNewButton_2, gbc_btnNewButton_2);
        
        JLabel lblChatbox = new JLabel("Chat Room");
        GridBagConstraints gbc_lblChatbox = new GridBagConstraints();
        gbc_lblChatbox.insets = new Insets(0, 0, 5, 5);
        gbc_lblChatbox.gridx = 1;
        gbc_lblChatbox.gridy = 10;
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
        gbc_scroll.gridy = 11;
        frame.getContentPane().add(scroll, gbc_scroll);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        scroll.setBounds(1, 0, 20, 20);
        
        textArea = new JTextArea();
        scroll.setViewportView(textArea);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setBounds(1, 0, 20, 20);
        
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
        gbc_textButton.gridy = 9;
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
        gbc_rectangleButton.gridy = 9;
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
        gbc_btnNewButton_1.gridy = 9;
        frame.getContentPane().add(btnNewButton_1, gbc_btnNewButton_1);
        
        
        // canvas setup
        drawingPanel = new Drawing(drawEvent);
        
        
        GridBagConstraints gbc_contentPane = new GridBagConstraints();
        gbc_contentPane.insets = new Insets(0, 0, 5, 0);
        gbc_contentPane.gridheight = 23;
        gbc_contentPane.gridwidth = 7;
        gbc_contentPane.fill = GridBagConstraints.BOTH;
        gbc_contentPane.gridx = 6;
        gbc_contentPane.gridy = 10;
        frame.getContentPane().add(contentPane, gbc_contentPane);
		
		
		drawingPanel.setBounds(10, 50, 570, 500);
		contentPane.add(drawingPanel, BorderLayout.CENTER);	  
		JPanel panel_1 = new JPanel();
		panel_1.setEnabled(false);
		panel_1.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel_1.setBounds(100, 0, 520, 350);
		contentPane.add(panel_1);
		
		 
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
		
		
		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.gridheight = 4;
		gbc_textField.gridwidth = 4;
		gbc_textField.fill = GridBagConstraints.BOTH;
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 21;
		frame.getContentPane().add(textField, gbc_textField);
		textField.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, Color.LIGHT_GRAY));
		textField.setBounds(0, 0, 130, 26);
		textField.setColumns(10);
		
		JButton btnSend = new JButton("Send");
		btnSend.setFont(new Font("Microsoft Tai Le", Font.PLAIN, 14));
		GridBagConstraints gbc_btnSend = new GridBagConstraints();
		gbc_btnSend.insets = new Insets(0, 0, 5, 5);
		gbc_btnSend.gridx = 4;
		gbc_btnSend.gridy = 25;
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
		
		JLabel lblUsers = new JLabel("All Users");
		GridBagConstraints gbc_lblUsers = new GridBagConstraints();
		gbc_lblUsers.insets = new Insets(0, 0, 5, 5);
		gbc_lblUsers.gridx = 1;
		gbc_lblUsers.gridy = 26;
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
		gbc_list.gridy = 27;
		frame.getContentPane().add(list, gbc_list);
		list.setBounds(0, 29, 207, 131);
		list.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, Color.LIGHT_GRAY));
	}
	/**
	 * Set the username
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * Close the manager's system
	 * @param e
	 */
	protected void closeFrame(WindowEvent e) {
		this.getJlist().setListData(new String[0]);
		String str = "Sorry, the manager has exited the board.\n";
		try {
			board.sendMessageToUsers(str);
			board.setExit(true);
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.exit(0);
	}
	
	/**
	 * Obtain chat room's text
	 * @return
	 */
	public JTextArea getMsgGUI() {
		return textArea;
	}

	/**
	 * Get the user list
	 * @return
	 */
	public JList getJlist() {
		return list;
	}

	/**
	 * Get the drawing canvas object
	 * @return
	 */
	public Drawing getDraw() {
		return drawingPanel;
	}

	/**
	 * Set the remote board object
	 * @param board
	 */
	public void setBoard(IRemoteBoard board) {
		this.board = board;
	}
}


