package soft;

import java.awt.*;
import java.awt.event.*;

import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;


import javax.swing.ImageIcon;
import javax.swing.JButton;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.plaf.synth.SynthOptionPaneUI;

import org.openslide.OpenSlide;



public class SOFTFenetre1 extends JFrame { 

	
//creation de la class Softfenetre
 	   private int index = 0;
	   public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getNomFichier() {
		return nomFichier;
	}
	public void setNomFichier(String nomFichier) {
		this.nomFichier = nomFichier;
	}
	public OuvertureNDPI getOuv() {
		return ouv;
	}
	public void setOuv(OuvertureNDPI ouv) {
		this.ouv = ouv;
	}
	public int getX0() {
		return x0;
	}
	public void setX0(int x0) {
		this.x0 = x0;
	}
	public int getY0() {
		return y0;
	}
	public void setY0(int y0) {
		this.y0 = y0;
	}
	public int[] getTabValeur() {
		return tabValeur;
	}
	public void setTabValeur(int[] tabValeur) {
		this.tabValeur = tabValeur;
	}
	
	public Boolean getNiveauSelectionner() {
		return NiveauSelectionner;
	}
	public void setNiveauSelectionner(Boolean niveauSelectionner) {
		NiveauSelectionner = niveauSelectionner;
	}
	public Graphics getG() {
		return g;
	}
	public void setG(Graphics g) {
		this.g = g;
	}

	public void setPathFichier(String pathFichier) {
		this.pathFichier = pathFichier;
	}
	public void setFilechoose(File filechoose) {
		Filechoose = filechoose;
	}
	   private JPanel panel = new JPanel();
	   private JPanel ZoomImage = new JPanel();
	   private JPanel panelImageOrigin = new JPanel();
	   private BufferedImage Image;
	   private String nomFichier;
 	   private String pathFichier;
 	   private File Filechoose;
	   private MenuBar menuBar = new MenuBar();
	   private Menu ficMenu = new Menu("Fichier");
	   private Menu editMenu = new Menu("Edition");
	   private MenuItem openItem = new MenuItem("ouvrir Fichier");
	   private MenuItem save = new MenuItem("save");
	   private MenuItem exit = new MenuItem("exit");
	   private MenuItem colItem = new MenuItem("toto");
	   private JPanel imagetransitoire = new JPanel();
	   public OuvertureNDPI ouv;
	   private int x0, y0;
	   public int tabValeur[] = new int[4];
	   private int CountOfZoom=1;


	public int getCountOfZoom() {
		return CountOfZoom;
	}
	public void setCountOfZoom(int countOfZoom) {
		CountOfZoom = countOfZoom;
	}

	private Boolean NiveauSelectionner = false;
	   
	   private Graphics g;
	   
	//   private OuvertureNDPI() ouv = new OuvertureNDPI("ouv");
	  
		 public SOFTFenetre1(){
				super();
				build();//On initialise notre fenêtre
				
			}
		 private void build(){
				setTitle("soft"); //On donne un titre à l'application
				this.setSize((int)getToolkit().getScreenSize().getWidth(), ((int)getToolkit().getScreenSize().getHeight() - 40)); //On donne une taille à notre fenêtre
				setResizable(true); //On autorise le redimensionnement de la fenêtre
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //On dit à l'application de se fermer lors du clic sur la croix
				if(getIndex()==0){
					setContentPane(buildContentPane());
				}
				if(getIndex()==1){
					System.out.println("bien pris en compte");
				}
			}
		 
		 private JPanel buildContentPane(){
			 
				
			 	
				panel.setBackground(new Color(1, 51, 102));
				JPanel Tools = new JPanel();
		        Tools = ToolsBar();
		        Tools.setMinimumSize(new Dimension((int)getToolkit().getScreenSize().getWidth(),40));
		        Tools.setBounds(0, 1, (int)getToolkit().getScreenSize().getWidth(),40);
		        
		          panel.add("NORTHWEST",Tools);
		          
		          ficMenu.add(openItem);
			      ficMenu.add(save);
				  ficMenu.add(exit);
			      editMenu.add(colItem);
			      menuBar.add(ficMenu);
			      menuBar.add(editMenu);

			      openItem.addActionListener(new ActionListener(){
			    
						public void actionPerformed(ActionEvent e){
					        JFileChooser chooser = new JFileChooser();
					        int returnVal = chooser.showOpenDialog(getParent());
					        if(returnVal == JFileChooser.APPROVE_OPTION) {

					        	String nomFichier=chooser.getSelectedFile().getName();
					        	String pathFichier=chooser.getSelectedFile().getPath();

					        	Filechoose = chooser.getSelectedFile();
						        
						        
						        OuvertureNDPI ouv = new OuvertureNDPI(nomFichier, pathFichier, Filechoose, chooser);
								
						        try {
									 //chargement du thumnail creer dans monImage
									
									 panel.setLayout(new GridBagLayout());
									 GridBagConstraints gbc = new GridBagConstraints();
									 gbc.gridx = 1;
									 gbc.gridy = 1;
									 panelImageOrigin.add(ouv.ouvertureImage(nomFichier,pathFichier,Filechoose,chooser));
									 panel.add(panelImageOrigin , gbc);
									 setVisible(true);
									 
								} catch (IOException e1) {
									e1.printStackTrace();
								}	
							}
					        else{
					        	
					        	System.out.println("ERROR");
					        }
						}
					});
				  
			        
			       	setMenuBar(menuBar);
		        
			        if(tabValeur[0] != 0 ){
						 System.out.println(tabValeur[0]);
						 System.out.println(tabValeur[1]);
						 System.out.println(tabValeur[2]);
						 System.out.println(tabValeur[3]);
					 }
			        
			        return panel;
			    }
		 public JPanel ToolsBar(){
				JPanel Tools = new JPanel();
		        
		        
		        JButton buttonBox = new javax.swing.JButton(new ImageIcon("/home/guillaume/Documents/Mycropycell/ImagesSoft/carre.png"));
		        JButton jButton2 = new javax.swing.JButton(new ImageIcon("/home/guillaume/Documents/Mycropycell/ImagesSoft/cercle.png"));
		        JButton jButton3 = new javax.swing.JButton(new ImageIcon("/home/guillaume/Documents/Mycropycell/ImagesSoft/trait.png"));
		        JButton jButton4 = new javax.swing.JButton(new ImageIcon("/home/guillaume/Documents/Mycropycell/ImagesSoft/loupe.png"));
		        JButton jButton5 = new javax.swing.JButton(new ImageIcon("/home/guillaume/Documents/Mycropycell/ImagesSoft/texte.png"));

		        buttonBox.setText("buttonBox");
		        buttonBox.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		        buttonBox.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		        
		        buttonBox.addActionListener(new ActionListener(){
		        	public void actionPerformed(ActionEvent e){
		        		System.out.println("clicksurbutton carre");
		        		dessinerRec dr = new dessinerRec();
		        		dr.init();
		        		}
		        	});
		        Tools.add(buttonBox);

		        jButton2.setText("cercle");
		        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		        Tools.add(jButton2);

		        jButton3.setText("trait");
		        jButton3.setFocusable(false);
		        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		        Tools.add(jButton3);
		        jButton3.addActionListener(new ActionListener(){
		        	public void actionPerformed(ActionEvent e){
		        		System.out.println("clicksurbutton trait");
		        		DessinTrait dt = new DessinTrait();
		        		dt.init();
		        	}
		        });
		        jButton4.setText("zoom");
		        jButton4.setFocusable(false);
		        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		        Tools.add(jButton4);
		        jButton4.addActionListener(new ActionListener(){
			        public void actionPerformed(ActionEvent e){
		        		Zoom zoom = new Zoom();
		        	}
	        });

		        jButton5.setText("annotation");
		    //    jButton5.setFocusable(false);
		        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		        jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		        Tools.add(jButton5);
				return Tools;
		 }
		
		 public String getnomFichier() {
			return this.nomFichier;
			}
		public String getPathFichier() {
			return this.pathFichier;
		}
		public File getFilechoose() {
			return Filechoose;
		}
		
		/***************************** DESSINER IMAGE ***********************************/
		public JPanel caseImage(BufferedImage monImage){
			JPanel MesIcon = new JPanel(); 
			MesIcon.setLayout(new FlowLayout());
			JLabel image = new JLabel( new ImageIcon(monImage));
			MesIcon.add(image);
			return MesIcon;
		}
		/***************************** DESSIN TRAIT ***********************************/

	
		public class DessinTrait implements MouseListener, MouseMotionListener { // fonction permettant de dessinner un trait
		    public void init() {
				g = getGraphics();
				addMouseListener(this);
				addMouseMotionListener(this);
			}
			public void mousePressed(MouseEvent e){
				int x,y;
				x = e.getX();
				y = e.getY();
				x0=x; y0=y;
			}
			public void mouseDragged(MouseEvent e){
				int x,y;
				x = e.getX();
				y = e.getY();
				g.drawLine(x0, y0, x, y);
				x0=x; y0=y;
			}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mouseClicked(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
			public void mouseMoved(MouseEvent e) {}
			
		}
		
		/************************ DESSIN RECTANGLE ****************************************/

		public class dessinerRec implements MouseListener, MouseMotionListener {
			// fonction permetant de dessiner un rectangle
			private int width;
			private int height;

			

			public void init() {
				
				g = getGraphics();
				addMouseListener(this);
				addMouseMotionListener(this);
				//position.recupPositionRoiSurImage(x0, y0, Math.abs( width - x0), Math.abs( height - y0));
				
			}
			
			public void mousePressed(MouseEvent e){
				Point startDrag = new Point(e.getX(), e.getY());
				System.out.println(startDrag);
				x0=e.getX();
				y0=e.getY();
				
			}
			
						
			public void mouseReleased(MouseEvent e){
				Point endDrag = new Point(e.getX(), e.getY());
				System.out.println(endDrag);
				 width = e.getX();
				 height = e.getY();
				 System.out.println("largeur = "+width);
				 int a;
				 int b;
				 if (width>x0){
					 a =  width - x0;
				 }else{a =  x0- width;
				 
				 }
				 if (height>y0){
					 b =  height - y0;
				 }else{
					 b =  y0-height;
				 }
				 if(x0>550 && y0>130 && a<(1000) && b<(1000)){
				 g.drawRect(x0, y0,a,b);
				 getSurface(x0, y0,a,b); //permet de stocker le placement du rectangle
				 }
			}

			public void mouseDragged(MouseEvent e) {}
			public void mouseMoved(MouseEvent e) {}
			
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			
			public int[] getSurface(int x, int y, int xfin, int yfin)
			{
				tabValeur[0]= x;
				tabValeur[1]= y;
				tabValeur[2]= xfin;
				tabValeur[3]= yfin;
				return tabValeur;
			}
			public void mouseClicked(MouseEvent arg0) {}
		}
		/**************************** Zoom  ************************************/
		public class Zoom implements MouseListener, MouseMotionListener{
			Zoom(){
				int x0; int y0;
				addMouseListener(this);
				addMouseMotionListener(this);
			}
			public void mouseClicked(MouseEvent e) {System.out.println("clicksurbutton zoom");	
			int buttonDown = e.getButton();
			 
		    if (buttonDown == MouseEvent.BUTTON1) {
		    	CountOfZoom = CountOfZoom*2;
		    } 
		    if(buttonDown == MouseEvent.BUTTON3) {
		    	if(CountOfZoom != 1){
		    	CountOfZoom = CountOfZoom/2;
		    	}
		    }
			System.out.println(CountOfZoom);
			try {
				OpenSlide open = new OpenSlide(getFilechoose());
				Point position = e.getPoint();
				x0 =position.x;
				System.out.println(x0);
				y0 = position.y;
				System.out.println(y0);
				// problem de zoom toujours en haut a gauche
				Image = open.createThumbnailImage(x0,y0, (int)(open.getLevel0Width()/CountOfZoom) ,(int)(open.getLevel0Height()/CountOfZoom) ,1000);
				System.out.println(Image);
				 
				 panel.remove(panelImageOrigin);
				 panel.validate();
				 ZoomImage.remove(imagetransitoire);
				 panel.revalidate();
				 panel.repaint();
				 imagetransitoire = caseImage(Image);
				 ZoomImage.add(imagetransitoire);
				 panel.setLayout(new GridBagLayout());
				 GridBagConstraints gbc = new GridBagConstraints();
				 gbc.gridx = 1;
				 gbc.gridy = 1;
				 panel.add(ZoomImage,gbc);
				 panel.revalidate();
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.out.println("ca marche pas");
			}
			}
			public void mouseDragged(MouseEvent arg0) {}
			public void mouseMoved(MouseEvent arg0) {}
			public void mouseEntered(MouseEvent e){}
			public void mouseExited(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
		}
		
/**************************** Curseur de la souris ************************************/
		// Acting on mouse movement - display mouse coordinates
		
		class MousePad extends JPanel implements MouseMotionListener {
		   JTextField mouse_x, mouse_y;
		   
		   MousePad (JTextField x, JTextField y) {
		      mouse_x = x;
		      mouse_y = y;
		      addMouseMotionListener(this);
		      }
		   
		   public void mouseDragged (MouseEvent event) { }
		   
		   public void mouseMoved (MouseEvent event) {
		      mouse_x.setText(String.valueOf(event.getX()));
		      mouse_y.setText(String.valueOf(event.getY()));
		   }
		}

		class ShowMouseCoordsFrame {
		   JTextField mouse_x, mouse_y;
		   int positionx;
		   int positiony;
		   public JPanel ShowMouseCoordsFrame() {
		      setLayout(new BorderLayout());
		      JPanel p = new JPanel();
		      p.setLayout(new GridLayout(1,4));
		      p.add(new JLabel("x: "+ positionx+ "y : "+ positiony));
		      p.add(mouse_x = new JTextField());
		      p.add(mouse_y = new JTextField());
		      add("North", p);
		      MousePad mp = new MousePad(mouse_x, mouse_y);
		      add("Center",mp);
			return p;
		   }
		}
}



//private int tabValeur[] = new int[4];
//public int[] recupPositionRoiSurImage(int x, int y, int xfin, int yfin){
//	
//	
//			//={x, y, xfin, yfin};
//	System.out.println("recupPositioninfo");
//	System.out.println(xfin);
//	return tabValeur;	
//}
//public int getWidth() {
//	int a = tabValeur[2];
//	System.out.println("Width "+a);
//	return a;
//}
//public int getheight() {
//	int a = tabValeur[3];
//	System.out.println("height "+a);
//	return a;
//}
//public int getPositionOrigineX() {
//	int a = tabValeur[0];
//	System.out.println("xorigine "+a);
//	return a;
//}}
/****************************************************************/
//		public class Zone  {
//			 
//			public JPanel Zone(){
//				JPanel p = new JPanel();
//				Contenu contenu = new Contenu();
//				p.add(contenu);
//				getContentPane().add(p);
//				return p;
//			}
//		 
//			class Contenu extends JPanel implements MouseListener, MouseMotionListener{
//				int xPrec, yPrec;
//				Graphics g;
//		 
//				public void Contenu(){
//					xPrec=0; yPrec=0;
//					g = getGraphics();
//					addMouseListener(this);
//					addMouseMotionListener(this);
//					afficherZone();
//					  
//				}
//		 
//				public void mousePressed(MouseEvent e) {}
//				public void mouseMoved(MouseEvent e) {
//					int x,y;
//					x = e.getX();
//					y = e.getY();
//					xPrec=x;
//					yPrec=y;
//				}
//				public void mouseClicked(MouseEvent e) {}
//				public void mouseEntered(MouseEvent e) {}
//				public void mouseExited(MouseEvent e) {}
//				public void mouseReleased(MouseEvent e) {}
//				public void mouseDragged(MouseEvent e) {}
//			}
//				public void afficherZone(){
//					   JTextArea area = new JTextArea();
//					   area.append("Ajopgijzt");
//				}
//	}

		

		
		
		
		
		
		
		