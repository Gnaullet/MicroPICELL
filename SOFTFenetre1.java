package soft;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import org.openslide.OpenSlide;

import ij.IJ;
import ij.ImagePlus;


public class SOFTFenetre1 extends JFrame { 

	
	   private JPanel zoomSeg = new JPanel();
	   private JPanel Feature = new JPanel();
	   private JPanel panelAffichage = new JPanel();
	   private JPanel Count = new JPanel();
	   private JPanel Segmentation = new JPanel();
	   private JPanel Batch = new JPanel();
	   private JLabel label = new JLabel();
	   private JLabel label2 = new JLabel();
	   private JPanel panel = new JPanel();
	   private JPanel ZoomImage = new JPanel();
	   private JPanel panelImageOrigin = new JPanel();
	   private BufferedImage Image;
	   private long largeurImageTransitoire=0;
	   private long hauteurImageTransitoire=0;
	   public String nomFichier;
 	   public String pathFichier;
 	   private int zoomCounter=1;
 	   int x;
	   int y;
 	   public JFileChooser getChooser() {
		return chooser;
	}
	public void setChooser(JFileChooser chooser) {
		this.chooser = chooser;
	}



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
	   private int x0 =0 , y0=0;
	   public int tabValeur[] = new int[4];
	   private double CountZoom=1;
	   private JTabbedPane onglet = new JTabbedPane(JTabbedPane.LEFT);
	   public JFileChooser chooser = new JFileChooser();
	   public BufferedImage buffImage;
	   private JPanel Bandeau = new JPanel();
	   private position ps =null;
	 
	   
	public int getCountZoom() {
		return (int) CountZoom;
	}
	public void setCountZoom(int CountZoom) {
		CountZoom = CountZoom;
	}
	
	
	   
	   private Graphics g;
	private boolean doneone=false;
	   
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
				setContentPane(buildContentPane());
				
			}
		 
		 private JPanel buildContentPane(){
			    JPanel Tools = new JPanel();
			 	panel.setBackground(Color.white);
				Tools = ToolsBar();
		        panelAffichage.setLayout(new BorderLayout());
		        ZoomImage.setBackground(Color.white);
		        
		        
		          panelAffichage.add(Tools, BorderLayout.NORTH);
		          
		          
		          panelAffichage.add(Bandeau, BorderLayout.SOUTH);
		        //  position ps = new position();
		          ficMenu.add(openItem);
			      ficMenu.add(save);
				  ficMenu.add(exit);
			      editMenu.add(colItem);
			      menuBar.add(ficMenu);
			      menuBar.add(editMenu);
			      openItem.addActionListener(new ActionListener(){
					    
						public void actionPerformed(ActionEvent e){
							System.out.println("Fermeture");
						}
					});
			      openItem.addActionListener(new ActionListener(){
			    
						public void actionPerformed(ActionEvent e){
					        
					        int returnVal = chooser.showOpenDialog(getParent());
					        if(returnVal == JFileChooser.APPROVE_OPTION) {

					        	nomFichier=chooser.getSelectedFile().getName();
					        	pathFichier=chooser.getSelectedFile().getPath();

					        	Filechoose = chooser.getSelectedFile();
						        
						        
						        OuvertureNDPI ouv = new OuvertureNDPI(nomFichier, pathFichier, Filechoose, chooser);
								
						        try {
									 //chargement du thumnail creer dans monImage
									 
									 imagetransitoire.add(ouv.ouvertureImage(nomFichier,pathFichier,Filechoose,chooser));
									 
									 //ZoomImage.setBorder(javax.swing.BorderFactory.createEmptyBorder());
									 ZoomImage.add(imagetransitoire);
									 panel.add(ZoomImage);
									 ps = new position(imagetransitoire);
									// buffImage = ouv.ReturnBuffImage(nomFichier,pathFichier,Filechoose,chooser);
									// ImagePlus IP = new ImagePlus(nomFichier, buffImage);
									// java.awt.Image image = IP.getImage();
							    	//	IP.getCanvas();
									//IP.show();
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
		            onglet.add("open Image" , panel);
			       // Feature.add(Feature());
			        //onglet.add("Selection of features of image", Feature);
			        Segmentation.setLayout(new BorderLayout());
			        Segmentation segm = new Segmentation();
			        Segmentation.add(segm.createAPanelContaintSegmentationTools(),BorderLayout.WEST);
			        onglet.add("segmentation", Segmentation);
			        onglet.add("Count", Count);
			        onglet.add("Batch", Batch);
			        panelAffichage.add(onglet);
			        panelAffichage.setBackground(Color.WHITE);
			        
			       
			        return panelAffichage;
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
			        	System.out.println("click sur zoom");
		        		Zoom zoom = new Zoom();
		        	}
	        });

		        jButton5.setText("annotation");
		    //    jButton5.setFocusable(false);
		        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		        jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		        jButton5.addActionListener(new ActionListener(){
		        public void actionPerformed(ActionEvent e){
	        		System.out.println("clicksurbutton Trait");
	        					
	        		}
	        	});
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
		/***************************** Panel of features ***********************************/
		
		public JPanel Feature(){
			JPanel gui = new JPanel();
			gui.setSize(400, 400);
			gui.setLayout(new BorderLayout());
			gui.add(new Label("Selection Of Your Feature"), BorderLayout.NORTH);
			ButtonGroup group = new ButtonGroup();
			ArrayList <JCheckBox> radioList = new ArrayList<JCheckBox>() ;	
			if (doneone==false){
				String[] TabFeature = {"Gaussian", "Hessian"};
				int a = TabFeature.length;
				System.out.println(a);
				doneone=true;
			
			
		for(String tab : TabFeature){
			JCheckBox tab1 = new JCheckBox("<html> <br>" + tab + "<br> </html>");
			radioList.add(tab1);
			radioList.size();	
			System.out.println(tab1);

		      imagetransitoire= null;
		group.add(tab1);
			gui.add(tab1);
		}}
			
			
			
			
			
		 JButton bouton = new JButton("submit");
		 gui.add(bouton,BorderLayout.PAGE_END);
			return gui;
		}
		
		/***************************** DESSIN TRAIT ***********************************/
		public class DessinTrait implements MouseListener, MouseMotionListener {
		    private JPanel drawArea;
		    private int x1, y1;
		    Graphics g;
		 
		    public void init() {
		     
		        // écouteurs
		    	imagetransitoire.addMouseListener(this);
		    	imagetransitoire.addMouseMotionListener(this);
		 
		    
		    }
		 
		    public void mousePressed(MouseEvent e){
		        int x,y;
		        x = e.getX();
		        y = e.getY();
		        x1=x; y1=y;
		    }
		 
		    //événement déplacement souris avec bouton enfoncé
		    public void mouseDragged(MouseEvent e){
		    	Graphics g = imagetransitoire.getGraphics();//A remplasser par drawArea.getGraphics();
				g.drawLine(this.x1, this.y1, e.getX(), e.getY());
				mouseMoved(e);
			}
		 
			//événement lors du déplacement de la souris
			public void mouseMoved(MouseEvent e){
				this.x1 = e.getX();
				this.y1 = e.getY();
			}
		 
		    public void mouseEntered(MouseEvent event) {}  
		    public void mouseExited(MouseEvent evt){}
		    public void mouseClicked(MouseEvent event){}
			public void mouseReleased(MouseEvent arg0) {}
		}
		
		/************************ DESSIN RECTANGLE ****************************************/

		public class dessinerRec implements MouseListener, MouseMotionListener{
			// fonction permetant de dessiner un rectangle
			private int width;
			private int height;

			public void init() {
				g = getGraphics();
				imagetransitoire.addMouseListener(this);
				imagetransitoire.addMouseMotionListener(this);
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
				 }else{
					 a =  x0- width;
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
			
				imagetransitoire.addMouseListener(this);
				imagetransitoire.addMouseListener(this);
				//addMouseMotionListener(this);
				//System.out.println("clicksurbutton zoom");	
			
			}
			public void mouseClicked(MouseEvent e) {
				
			int buttonDown = e.getButton();
			boolean Dezoom = false;
		    if (buttonDown == MouseEvent.BUTTON1) {
		    	zoomCounter = zoomCounter+1;
		    	System.out.println("zoomCounter"+zoomCounter);
		    	CountZoom = CountZoom*2;
		    	int reste = (zoomCounter-1) % 2; 
		    	if (reste == 0 ){
		    		CountZoom = Math.pow(2,(zoomCounter/2));  
		    	Dezoom = false;
		    	}
		    	
		    	System.out.println("0: "+CountZoom);
		    } 
		    if(buttonDown == MouseEvent.BUTTON3) {
		    	if(CountZoom != 1){
		    	//	System.out.println("je comprend  le click");
		    		zoomCounter = zoomCounter-1;
			    	System.out.println("zoomCounter"+zoomCounter);
			    	CountZoom = CountZoom*2;
			    	int reste = (zoomCounter-1) % 2; 
			    	if (reste == 0 ){
			    		CountZoom = Math.pow(2,(zoomCounter/2));  
			    		Dezoom = true;
			    	}
		    	}
		    }
			
		    System.out.println("1: "+CountZoom);
			
		    try {
				OpenSlide open = new OpenSlide(getFilechoose());
				Point position = e.getPoint();
				System.out.println("2: "+CountZoom);
				
				
		//		System.out.println("z = " + z);
		//		System.out.println(y);
				// problem de zoom toujours en haut a gauche
				
				int width = (int)(open.getLevel0Width()/(CountZoom));
				int height = (int)(open.getLevel0Height()/(CountZoom));
				int a;
				System.out.println("la largeur est de "+width);
				System.out.println("la longueur est de "+height);
				if ((width-height)>=0){
					a = 1; // width plus grand ou egal
				}else {
					a =2; //height plus grand
				}
				if(width<1000 ){
					width = 1000;
				}
				if( height<800){
					height = 762;
				}
				float w = (e.getX());
				
				float z = (e.getY());
				int reste = (zoomCounter-1) % 2; 
		    	if (reste == 0 ){
		    		
		    		System.out.println("w "+w);
		    		if(Dezoom == false){
					x = (int) (largeurImageTransitoire+(w/((imagetransitoire.getWidth()))*(width)));
					largeurImageTransitoire = x;
					//int x = (int) (w);
					System.out.println("open.getLevel0Width() "+open.getLevel0Width());
					System.out.println("x "+x);
				//	System.out.println(e.getX());
				//	System.out.println(x);
					System.out.println("3: "+CountZoom);
					
					System.out.println("z"+z);
					//int y = (int) (z);
					
					System.out.println("hauteur panel "+imagetransitoire.getHeight());
					System.out.println("largeur panel "+imagetransitoire.getWidth());
					y = (int) (hauteurImageTransitoire+(z/(imagetransitoire.getHeight())*(int)(height)));
					hauteurImageTransitoire = y;
					System.out.println("y"+y);
		    		}
		    		if(Dezoom == true){
						x = (int) (largeurImageTransitoire-(w/(imagetransitoire.getWidth())*(width)));
						largeurImageTransitoire = x;
						//int x = (int) (w);
						System.out.println("open.getLevel0Width() "+open.getLevel0Width());
						System.out.println("x "+x);
					//	System.out.println(e.getX());
					//	System.out.println(x);
						System.out.println("3: "+CountZoom);
						
						System.out.println("z"+z);
						//int y = (int) (z);
						
						System.out.println("hauteur panel "+imagetransitoire.getHeight());
						System.out.println("largeur panel "+imagetransitoire.getWidth());
						y = (int) (hauteurImageTransitoire-(z/(imagetransitoire.getHeight())*(int)(height)));
						hauteurImageTransitoire = y;
						System.out.println("y"+y);
			    		}
					if (CountZoom == 1){
						int pcZoom = 1;
						Image = open.createThumbnailImage(0,0,width ,height ,1000);
					}else{
						int pcZoom = (int)CountZoom*2;
						Image = open.createThumbnailImage(x,y,width ,height ,1000);
			//		System.out.println(Image);
					}
					
					 ZoomImage.remove(imagetransitoire);
					  
					 panel.validate();
					 panel.remove(label);
					 panel.revalidate();
					 
					 
					 int pcZoom = (int)CountZoom;
					 panel.setLayout(new GridBagLayout());
					 label = new JLabel( "<html>Niveau de Zoom : " +(pcZoom) +   "</html>");
					 panel.setLayout(new BorderLayout());
					 panel.add(label,BorderLayout.NORTH);
					 imagetransitoire = caseImage(Image);
					 
					 ZoomImage.add(imagetransitoire);
					// ZoomImage.setSize(new Dimension(imagetransitoire.getWidth(),imagetransitoire.getHeight()));
					
					 ZoomImage.revalidate();
					 panel.add(ZoomImage);
					 System.out.println("5: "+CountZoom);
					 panel.repaint();
		    	}}

	/***************************/
//					 Segmentation.remove(panelImageOrigin);
//					 Segmentation.validate();
//					 ZoomImage.remove(imagetransitoire);
//					 Segmentation.validate();
//					 ZoomImage.remove(label);
//					 Segmentation.revalidate();
//					 
//					 Segmentation.repaint();
//					
//					 Segmentation.setLayout(new GridBagLayout());
//					 
//					 label = new JLabel( "<html>Niveau de Zoom : " +pcZoom +   "</html>");
//					 
//					 ZoomImage.add(label);
//					 imagetransitoire = caseImage(Image);
//					 ZoomImage.add(imagetransitoire);
//					 
//					 JPanel zz = ZoomImage;
//					 Segmentation.add(zz);
//					 Segmentation.revalidate();
					
				 catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					System.out.println("Loading to new thumbnail fail");
				}
		    }
			public void mouseDragged(MouseEvent arg0) {}
			public void mouseMoved(MouseEvent arg0) {}
			public void mouseEntered(MouseEvent e){}
			public void mouseExited(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
		}
		public class position implements MouseListener, MouseMotionListener{
			position(JPanel PositionOfPanel){
				PositionOfPanel.addMouseListener(this);
				PositionOfPanel.addMouseListener(this);
				//addMouseMotionListener(this);
				System.out.println("clicksurbutton zoom");	
			}
			
			public void mouseExited(MouseEvent e) {
				Bandeau.setLayout(new BorderLayout());
				Bandeau.remove(label2);
				Bandeau.revalidate();
			    //	System.out.println(e.getX());
				label2 = new JLabel("<html> x = " + e.getX() + " y =  " + e.getY() + "</html>");
				Bandeau.add(label2, BorderLayout.NORTH);
			}
			
			public void mouseDragged(MouseEvent arg0) {}
			public void mouseMoved(MouseEvent arg0) {}
			public void mouseEntered(MouseEvent e){}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
			public void mouseClicked(MouseEvent e) {}
			}
		
		public JPanel getZoomSeg() {
			return zoomSeg;
		}
		public void setZoomSeg(JPanel zoomSeg) {
			this.zoomSeg = zoomSeg;
		}
		public JPanel getPanelAffichage() {
			return panelAffichage;
		}
		public void setPanelAffichage(JPanel panelAffichage) {
			this.panelAffichage = panelAffichage;
		}
		public JPanel getSegmentation() {
			return Segmentation;
		}
		public void setSegmentation(JPanel segmentation) {
			Segmentation = segmentation;
		}
		public JLabel getLabel() {
			return label;
		}
		public void setLabel(JLabel label) {
			this.label = label;
		}
		public JPanel getPanel() {
			return panel;
		}
		public void setPanel(JPanel panel) {
			this.panel = panel;
		}
		public JPanel getZoomImage() {
			return ZoomImage;
		}
		public void setZoomImage(JPanel zoomImage) {
			ZoomImage = zoomImage;
		}
		public JPanel getPanelImageOrigin() {
			return panelImageOrigin;
		}
		public void setPanelImageOrigin(JPanel panelImageOrigin) {
			this.panelImageOrigin = panelImageOrigin;
		}
		public BufferedImage getImage() {
			return Image;
		}
		public void setImage(BufferedImage image) {
			Image = image;
		}
		public String getNomFichier() {
			return nomFichier;
		}
		public void setNomFichier(String nomFichier) {
			this.nomFichier = nomFichier;
		}
		public MenuItem getOpenItem() {
			return openItem;
		}
		public void setOpenItem(MenuItem openItem) {
			this.openItem = openItem;
		}
		public MenuItem getSave() {
			return save;
		}
		public void setSave(MenuItem save) {
			this.save = save;
		}
		public JPanel getImagetransitoire() {
			return imagetransitoire;
		}
		public void setImagetransitoire(JPanel imagetransitoire) {
			this.imagetransitoire = imagetransitoire;
		}
		public OuvertureNDPI getOuv() {
			return ouv;
		}
		public void setOuv(OuvertureNDPI ouv) {
			this.ouv = ouv;
		}
		public int[] getTabValeur() {
			return tabValeur;
		}
		public void setTabValeur(int[] tabValeur) {
			this.tabValeur = tabValeur;
		}
		public JTabbedPane getOnglet() {
			return onglet;
		}
		public void setOnglet(JTabbedPane onglet) {
			this.onglet = onglet;
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
		
/**************************** Curseur de la souris ************************************/
		// Acting on mouse movement - display mouse coordinates
//		
//		class MousePad extends JPanel implements MouseMotionListener {
//		   JTextField mouse_x, mouse_y;
//		   
//		   MousePad (JTextField x, JTextField y) {
//		      mouse_x = x;
//		      mouse_y = y;
//		      addMouseMotionListener(this);
//		      }
//		   
//		   public void mouseDragged (MouseEvent event) { }
//		   
//		   public void mouseMoved (MouseEvent event) {
//		      mouse_x.setText(String.valueOf(event.getX()));
//		      mouse_y.setText(String.valueOf(event.getY()));
//		   }
//		}
//
//		class ShowMouseCoordsFrame {
//		   JTextField mouse_x, mouse_y;
//		   int positionx;
//		   int positiony;
//		   public JPanel ShowMouseCoordsFrame() {
//		      setLayout(new BorderLayout());
//		      JPanel p = new JPanel();
//		      p.setLayout(new GridLayout(1,4));
//		      p.add(new JLabel("x: "+ positionx+ "y : "+ positiony));
//		      p.add(mouse_x = new JTextField());
//		      p.add(mouse_y = new JTextField());
//		      add("North", p);
//		      MousePad mp = new MousePad(mouse_x, mouse_y);
//		      add("Center",mp);
//			return p;
//		   }
//		}
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

		

		
		
		
		
		
		
		