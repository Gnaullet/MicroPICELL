package soft;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.openslide.OpenSlide;

import ij.ImagePlus;
import ij.gui.Roi;
import ij.process.ColorProcessor;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;
import ij.process.StackConverter;
import trainableSegmentation.WekaSegmentation;
import trainableSegmentation.Weka_Segmentation;


public class SOFTFenetre1 extends JFrame { 

	   public JPanel getCount() {
		return Count;
	}
	public void setCount(JPanel count) {
		Count = count;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 5449375878197333562L;
	private int widthThumbnail;
	   private int heightThumbnail;
	   private JPanel zoomSeg = new JPanel();
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
	   private BufferedImage Image2 ;
	   private long largeurImageTransitoire=0;
	   private long hauteurImageTransitoire=0;
	   public String nomFichier;
 	   public String pathFichier;
 	   private int zoomCounter=1;
 	   private ImagePlus IP;
 	   int x;
	   int y;
	   private JButton OuvertureButton = null;
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
	   private JPanel panelPane = new JPanel();
	   private LinkedList<Float> l = new LinkedList<Float>();
	   
	public int getCountZoom() {
		return (int) CountZoom;
	}
	public void setCountZoom(int CountZoom) {
		this.CountZoom = CountZoom;
	}
	
	
	   
	   private Graphics g;
	   /************ Pour class segmentation ***************/
	public boolean classifierWholeImage = false;
	private boolean doneone=false;
	public String widhPixelOnMicron ;
	public String heightPixelOnMicron;
	private int numOfClasses = 2;
	public JPanel panelTotalSegmentation = new JPanel();
	public JPanel panelclass = new JPanel();
	private JButton trainButton = null;
	private JButton overlayButton = null;
	private JButton resultButton = null;
	private JButton probabilityButton = null;
	private JButton  ApplyClassifierInWholeImage = null;
	private int CounterButtonOverlay = 0;
	private Weka_Segmentation weka = new Weka_Segmentation();
	private WekaSegmentation weka2 = new WekaSegmentation();
	private ImagePlus zer;
	public ImagePlus RoiClassified;
	public ImageProcessor overlay;
	public ColorProcessor colorOverlay;
	public BufferedImage overlayImage;
//	private SOFTFenetre1 sf = new SOFTFenetre1();
//	private OuvertureNDPI ouv = new OuvertureNDPI(sf.getnomFichier(), sf.getPathFichier(), sf.getFilechoose(), sf.getChooser());
//	private ImagePlus IP = new ImagePlus(sf.getNomFichier(), ouv.getLama());
private JButton addClassButton = null;
public JPanel panelSegmentation = new JPanel();
public JPanel stockTransitoryImage = new JPanel();
// Weka_Segmentation weka = new Weka_Segmentation();
// private OuvertureNDPI() ouv = new OuvertureNDPI("ouv");

	  /************************** Initialisation de SOFTFenetre1 ******************************/

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
			 
			
			 String property = System.getProperty("java.library.path");
			 StringTokenizer parser = new StringTokenizer(property, ";");
			 while (parser.hasMoreTokens()) {
			     System.err.println(parser.nextToken());
			     }
			 try {
				File tmpf=new File("ToutFinal/libopenslide-jni.so");
			     System.load(tmpf.getAbsolutePath());
			    } catch (UnsatisfiedLinkError e) {
			      System.err.println("Native Open Slide code library failed to load.\n" + e);
			      System.exit(1);
			    }
			    JPanel Tools = new JPanel();
			 	panel.setBackground(Color.white);
			 	OuvertureButton = new JButton("Open your image on NDPI format ");
			 	OuvertureButton.addActionListener(new ActionListener(){
				    
					private OpenSlide open;

					public void actionPerformed(ActionEvent e){
				        
				        int returnVal = chooser.showOpenDialog(getParent());
				        if(returnVal == JFileChooser.APPROVE_OPTION) {

				        	nomFichier=chooser.getSelectedFile().getName();
				        	pathFichier=chooser.getSelectedFile().getPath();

				        	Filechoose = chooser.getSelectedFile();
					        
					        
					        OuvertureNDPI ouv = new OuvertureNDPI(nomFichier, pathFichier, Filechoose, chooser);
							
					        try {
								 //chargement du thumnail creer dans monImage
								 
					        	if(ZoomImage.getComponentCount()>=1){
									 System.out.println("déja une image");
									 ZoomImage.remove(imagetransitoire); 
									 ZoomImage.validate();
								 }
					        	
								 imagetransitoire.add(ouv.ouvertureImage(nomFichier,pathFichier,Filechoose,chooser));
								
								 //ZoomImage.setBorder(javax.swing.BorderFactory.createEmptyBorder());
								 ZoomImage.add(imagetransitoire);
								 ZoomImage.validate();
								 ZoomImage.repaint();
								 panel.add(ZoomImage);
								 new position();
								 buffImage = ouv.ReturnBuffImage(nomFichier,pathFichier,Filechoose,chooser);
								 IP = new ImagePlus(nomFichier, buffImage);
								 //IP.show();
								 System.out.println("getCurentslice"+IP.getCurrentSlice());
								 weka2.setTrainingImage(IP);
								 open = new OpenSlide(Filechoose);
								 Map<String, String> properties = open.getProperties();
								 properties.size();
								 widhPixelOnMicron = properties.get("openslide.mpp-x");
								 heightPixelOnMicron = properties.get("openslide.mpp-y");
								 System.out.println("width = "+widhPixelOnMicron+ "height = "+ heightPixelOnMicron);
   								 setVisible(true);
								 
							} catch (IOException e1) {
								e1.printStackTrace();
							}
					        panel.remove(OuvertureButton);
					        panel.validate();
					        panel.repaint();
						}
				        else{
				        	System.out.println("ERROR");
				        }
					}
				});	 
			 	panel.add(OuvertureButton);
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
			      exit.addActionListener(new ActionListener(){
					    
						public void actionPerformed(ActionEvent e){
							System.exit(0);
						}
					});
			      openItem.addActionListener(new ActionListener(){
			    
						private OpenSlide open;

						public void actionPerformed(ActionEvent e){
					        
					        int returnVal = chooser.showOpenDialog(getParent());
					        if(returnVal == JFileChooser.APPROVE_OPTION) {

					        	nomFichier=chooser.getSelectedFile().getName();
					        	pathFichier=chooser.getSelectedFile().getPath();

					        	Filechoose = chooser.getSelectedFile();
						        
						        
						        OuvertureNDPI ouv = new OuvertureNDPI(nomFichier, pathFichier, Filechoose, chooser);
								
						        try {
									 //chargement du thumnail creer dans monImage
									 
						        	if(ZoomImage.getComponentCount()>=1){
										 System.out.println("déja une image");
										 ZoomImage.remove(imagetransitoire); 
										 ZoomImage.validate();
									 }
						        	
									 imagetransitoire.add(ouv.ouvertureImage(nomFichier,pathFichier,Filechoose,chooser));
									
									 //ZoomImage.setBorder(javax.swing.BorderFactory.createEmptyBorder());
									 ZoomImage.add(imagetransitoire);
									 ZoomImage.validate();
									 ZoomImage.repaint();
									 panel.add(ZoomImage);
									 new position();
									 buffImage = ouv.ReturnBuffImage(nomFichier,pathFichier,Filechoose,chooser);
									 IP = new ImagePlus(nomFichier, buffImage);
									 //IP.show();
									 System.out.println("getCurentslice"+IP.getCurrentSlice());
									 weka2.setTrainingImage(IP);
									 open = new OpenSlide(Filechoose);
									 Map<String, String> properties = open.getProperties();
									 properties.size();
									 widhPixelOnMicron = properties.get("openslide.mpp-x");
									 heightPixelOnMicron = properties.get("openslide.mpp-y");
									 System.out.println("width = "+widhPixelOnMicron+ "height = "+ heightPixelOnMicron);
	   								 setVisible(true);
	   								 
	   								 panel.remove(OuvertureButton);
	   						         panel.validate();
	   						         panel.repaint();
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
				    
				    panelPane.add(panel);
		            onglet.add("open Image" , panelPane);
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
			        onglet.addChangeListener(new ChangeListener() {
			            public void stateChanged(ChangeEvent e) {
			                
			                if(onglet.getSelectedComponent().equals(Segmentation)) {
			                	panelPane.removeAll();
					        	panel.validate();
					        	Segmentation.add(panel);
					        	Segmentation.revalidate();
			                }
			                
			                if(onglet.getSelectedComponent().equals(Count)) {
			                	panelPane.removeAll();
					        	CountNucleus cn = new CountNucleus();
					        	Count.add(cn.returnTableOfCount(overlayImage, panel));	
			                }
			            }
			        });
			        return panelAffichage;
			    }
		 
         /******************* Tools Bar ************************/
		 public JPanel ToolsBar(){
			 
			    JPanel Tools = new JPanel();
			    
			    //System.out.println(Carre);
		        JButton buttonBox = new javax.swing.JButton(new ImageIcon(this.getClass().getResource("/carre.png")));
		        JButton jButton2 = new javax.swing.JButton(new ImageIcon(getClass().getResource("/cercle.png")));
		        JButton jButton3 = new javax.swing.JButton(new ImageIcon(getClass().getResource("/trait.png")));
		        JButton jButton4 = new javax.swing.JButton(new ImageIcon(getClass().getResource("/loupe.png")));
		        JButton jButton5 = new javax.swing.JButton(new ImageIcon(getClass().getResource("/texte.png")));

		        buttonBox.setText("buttonBox");
		        buttonBox.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		        buttonBox.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		        
		        buttonBox.addActionListener(new ActionListener(){
		        	public void actionPerformed(ActionEvent e){
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
			        private Zoom zoom;

					public void actionPerformed(ActionEvent e){
			        	
		        		setZoom(new Zoom());
		        	}

					@SuppressWarnings("unused")
					public Zoom getZoom() {
						return zoom;
					}

					public void setZoom(Zoom zoom) {
						this.zoom = zoom;
					}
	        });

		        jButton5.setText("annotation");
		        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		        jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		        jButton5.addActionListener(new ActionListener(){
		        public void actionPerformed(ActionEvent e){
		        	
		        }});
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
		/*************************** Implement of Panel Count ****************************/		
		
		public void implementOfPanelCount(){
			
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
				
				
				doneone=true;
			
			
		for(String tab : TabFeature){
			JCheckBox tab1 = new JCheckBox("<html> <br>" + tab + "<br> </html>");
			radioList.add(tab1);
			radioList.size();	
		
		      imagetransitoire= null;
		group.add(tab1);
			gui.add(tab1);
		}}
			
			
			
			
			
		 JButton bouton = new JButton("submit");
		 gui.add(bouton,BorderLayout.PAGE_END);
			return gui;
		}
		
		/***************************** DESSIN TRAIT ***********************************/
//		public class DessinTrait implements MouseListener, MouseMotionListener {
//		   
//		    private int x1, y1;
//		    Graphics g;
//		    
//		    public void init() {
//		     
//		        // écouteurs
//		    	imagetransitoire.addMouseListener(this);
//		    	imagetransitoire.addMouseMotionListener(this);
//		 
//		    
//		    }
//		 
//		    public void mousePressed(MouseEvent e){
//		        int x,y;
//		        x = e.getX();
//		        y = e.getY();
//		        x1=x; y1=y;
//		    }
//		 
//		    //événement déplacement souris avec bouton enfoncé
//		    public void mouseDragged(MouseEvent e){
//		    	Graphics g = imagetransitoire.getGraphics();
//				g.setColor(Color.ORANGE);
//		    	g.drawLine(this.x1, this.y1, e.getX(), e.getY());
//				mouseMoved(e);
//				addPositionDraw();
//			}
//		 
//			//événement lors du déplacement de la souris
//			public void mouseMoved(MouseEvent e){
//				this.x1 = e.getX();
//				this.y1 = e.getY();
//			}
//		    public void mouseEntered(MouseEvent event) {}  
//		    public void mouseExited(MouseEvent evt){}
//		    public void mouseClicked(MouseEvent event){}
//			public void mouseReleased(MouseEvent arg0) {}
//			public void addPositionDraw(){
//				if (CountZoom != 1){
//					float xdessin = (float) (largeurImageTransitoire+(((float)(this.x1))/((imagetransitoire.getWidth()))*(widthThumbnail)));
//					float ydessin = (float) (hauteurImageTransitoire+(((float)(this.y1))/(imagetransitoire.getHeight())*(heightThumbnail)));
//									
//					l.add(xdessin);
//				    l.add(ydessin);
//				}
//			}
//		}
		/****************************** New Position ****************************/
		public class DessinTrait implements MouseListener, MouseMotionListener {
			   
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
		    	Graphics g = imagetransitoire.getGraphics();
				g.setColor(Color.ORANGE);
		    	g.drawLine(this.x1, this.y1, e.getX(), e.getY());
				mouseMoved(e);
				addPositionDraw();
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
			public void addPositionDraw(){
				if (CountZoom != 1){
					float xdessin = (float) ((((float)(this.x1))/((imagetransitoire.getWidth()))*(widthThumbnail)));
					float ydessin = (float) ((((float)(this.y1))/(imagetransitoire.getHeight())*(heightThumbnail)));
									
					l.add(xdessin);
				    l.add(ydessin);
				}
			}
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
				
				 
				 g.drawRect(x0, y0,width,height);
				 getSurface(x0, y0,width,height); //permet de stocker le placement du rectangle
				 
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
			private int a;
			//TODO rectifier le dezoom qui devient noire
			//TODO Ne pas devoir a haque fois cliquer sur zoom
			Zoom(){
			    imagetransitoire.addMouseListener(this);  // Understand click on image transitoire
				imagetransitoire.addMouseListener(this);
			}
			public void mouseClicked(MouseEvent e) {
			OpenSlide open = null;
			
			try {
				open = new OpenSlide(getFilechoose());
				//long ee = open.getLevel0Height()*open.getLevel0Width();
					 System.out.println(open.getLevel0Height());
					 System.out.println(open.getLevel0Width());
			} catch (IOException e2) {
				System.out.println("ca marche pas");
				e2.printStackTrace();
			}
	
			int buttonDown = e.getButton();
			boolean Dezoom = false;
		    if (buttonDown == MouseEvent.BUTTON1) { // action left-click
		        zoomCounter = zoomCounter+1;  
		    	
		    	
		    	int reste = (zoomCounter-1) % 2; 
		    	if (reste == 0 ){
//		    		if( (int)(open.getLevel0Width()/(CountZoom))<1000 &&  (int)(open.getLevel0Height()/(CountZoom))<800){
//		    			CountZoom = CountZoom;
//			    	}else{
		    		CountZoom = Math.pow(2,(zoomCounter/2));  
//			    	}
		    	Dezoom = false;
		    	
		    	}
		    	
		    	
		    } 
		    if(buttonDown == MouseEvent.BUTTON3) {
		    	if(CountZoom != 1){
		    	
		    		zoomCounter = zoomCounter-1;
			    	
			    	CountZoom = CountZoom*2;
			    	int reste = (zoomCounter-1) % 2; 
			    	if (reste == 0 ){
			    		CountZoom = Math.pow(2,(zoomCounter/2));  
			    		Dezoom = true;
			    	}
		    	}
		    }
			
		   
			
		    try {
				widthThumbnail = (int)(open.getLevel0Width()/(CountZoom));
				heightThumbnail = (int)(open.getLevel0Height()/(CountZoom));
				if ((widthThumbnail-heightThumbnail)>=0){
					setA(1); // width plus grand ou egal
				}else {
					setA(2); //height plus grand
				}
				if(widthThumbnail<100 ){ // Solution temporaire (expendre la selection agrandir le width et le heighte
					widthThumbnail = 100;
					
				}
				if( heightThumbnail<80){
					heightThumbnail = 76;
				}
				
				float w = (e.getX());
				
				float z = (e.getY());
				int reste = (zoomCounter-1) % 2; 
		    	if (reste == 0 ){
		    		
		    		
		    		if(Dezoom == false){
					x = (int) (largeurImageTransitoire+(w/((imagetransitoire.getWidth()))*(widthThumbnail)));
					largeurImageTransitoire = x;
				
					y = (int) (hauteurImageTransitoire+(z/(imagetransitoire.getHeight())*(int)(heightThumbnail)));
					hauteurImageTransitoire = y;
				     
		    		}
		    		if(Dezoom == true){
						x = (int) (largeurImageTransitoire-(w/(imagetransitoire.getWidth())*(widthThumbnail)));
						largeurImageTransitoire = x;
						
						y = (int) (hauteurImageTransitoire-(z/(imagetransitoire.getHeight())*(int)(heightThumbnail)));
						hauteurImageTransitoire = y;
						
			    		}
		    		
		    		int pcZoom;
					if (CountZoom == 1){ 
						 pcZoom = 1;
						Image = open.createThumbnailImage(0,0,widthThumbnail ,heightThumbnail ,1000);
					}else{
						 pcZoom = (int)CountZoom*2;
						Image = open.createThumbnailImage(x,y,widthThumbnail ,heightThumbnail ,1000);
						
					}
					 ZoomImage.remove(imagetransitoire);
					 panel.validate();
					 panel.remove(label);
					 panel.revalidate();
					 pcZoom = (int)CountZoom;
					 panel.setLayout(new GridBagLayout());
//					 public static float strToFloat(String s)
//					 {
					 float f = Float.valueOf(widhPixelOnMicron.trim()).floatValue();
					 float g = Float.valueOf(heightPixelOnMicron.trim()).floatValue();
//					 return f;
//					 }
					 float widthReal = (float) (f*widthThumbnail);
					 float heightReal = (float) (g*heightThumbnail);
					 label = new JLabel( "<html>Niveau de Zoom : " +(pcZoom) +  "largeur de la ROI : " + widthReal + " micron ; Number of pixel largeur "+ widthThumbnail + "  hauteur de la ROI : "+ heightReal+ "</html>");
					 panel.setLayout(new BorderLayout());
					 panel.add(label,BorderLayout.NORTH);
					 IP = new ImagePlus("NewImagePlus", Image);
					 weka2.setTrainingImage(IP);
					 imagetransitoire = caseImage(Image); 
					 ZoomImage.add(imagetransitoire);					
					 ZoomImage.revalidate();
					 panel.add(ZoomImage);
					 panel.repaint();
		    	}
		    }catch (IOException e1) {
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
			public int getA() {
				return a;
			}
			public void setA(int a) {
				this.a = a;
			}
		}
		
   /************************************ POSITION *******************************/
		
		public class position implements MouseListener, MouseMotionListener{
			position(){
				imagetransitoire.addMouseListener(this);
				imagetransitoire.addMouseListener(this);
			}
			
			public void mouseExited(MouseEvent e) {
				Bandeau.setLayout(new BorderLayout());
				Bandeau.remove(label2);
				Bandeau.revalidate();
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
		/******************************** Panel Segmentation **************************/
		
		class Segmentation  {
			
			
		
		public JPanel createAPanelContaintSegmentationTools(){
			
			trainButton = new JButton("Train classifier");
			trainButton.setToolTipText("Start training the classifier");
			trainButton.addActionListener(new ActionListener(){
	        	public void actionPerformed(ActionEvent e){
	        		System.out.println("Entraine le classifier");
	        		System.out.println("feature stack array : "+weka2.getFeatureStackArray());
	        		boolean z = weka2.trainClassifier();
	        		System.out.println(z);
	        		weka2.applyClassifier(true);
	        		weka2.saveClassifier("toto");
	        		}
	        });
			
				
			
			overlayButton = new JButton("Toggle overlay");
			overlayButton.setToolTipText("Toggle between current segmentation and original image");
			//overlayButton.setEnabled(false);
			overlayButton.addActionListener(new ActionListener(){
	        	
				public void actionPerformed(ActionEvent e){
	        		
					RoiClassified = weka2.getClassifiedImage();
	        		overlay = RoiClassified.getImageStack().getProcessor(RoiClassified.getCurrentSlice()).duplicate();
	        		colorOverlay = overlay.convertToColorProcessor();
	        		
	        		int threshold = colorOverlay.getAutoThreshold();
	        		colorOverlay.autoThreshold();
	        		System.out.println("threshold : "+ threshold);
	        		colorOverlay.setBinaryThreshold();
	        		overlayImage = colorOverlay.getBufferedImage();
					   		 
	        		if (CounterButtonOverlay % 2 ==0 ){
	        			 stockTransitoryImage = imagetransitoire;
	        			 ZoomImage.remove(imagetransitoire);
						 panel.validate();
						 panel.revalidate();
						
						 
						 imagetransitoire = caseImage(overlayImage); 
						 
						 ZoomImage.add(imagetransitoire);					
						 ZoomImage.revalidate();
						 panel.add(ZoomImage);
						 panel.repaint();
						 CounterButtonOverlay = CounterButtonOverlay+1;
	        		}else{
	        			 ZoomImage.remove(imagetransitoire);
						 panel.validate();
						 
						 panel.revalidate();
						 imagetransitoire = stockTransitoryImage;
						 
						 ZoomImage.add(imagetransitoire);					
						 ZoomImage.revalidate();
						 panel.add(ZoomImage);
						 panel.repaint();
						 CounterButtonOverlay = CounterButtonOverlay+1;
	        		}
	        	}

				@SuppressWarnings("unused")
				public ImagePlus getZer() {
					return zer;
				}

				
	        });

			resultButton = new JButton("Create result");
			resultButton.setToolTipText("Generate result image");
			//resultButton.setEnabled(false);
			resultButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
				
        		}
			});

			probabilityButton = new JButton("Get probability");
			probabilityButton.setToolTipText("Generate current probability maps");
			//probabilityButton.setEnabled(false);
			probabilityButton.addActionListener(new ActionListener(){
	        	public void actionPerformed(ActionEvent e){
	        		ImagePlus RoiClassified = weka2.getClassifiedImage();
	        		//RoiClassified.show();
	        		RoiClassified.show();
	        		
	        		}
	        });
			ApplyClassifierInWholeImage = new JButton("Apply Classifier In Whole Image");
			ApplyClassifierInWholeImage.setToolTipText("Start training the classifier");
			ApplyClassifierInWholeImage.addActionListener(new ActionListener(){
	        	public void actionPerformed(ActionEvent e){
	        		System.out.println("ApplyClassifierInWholeImage");
	        		classifierWholeImage = true;
	        		boolean zz = weka2.loadClassifier("toto");
	        		System.out.println(zz);
	        		OpenSlide open = null;
	        		
					try {
						open = new OpenSlide(Filechoose);
					} catch (IOException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
						
					}
	        		try {
	        			int maxsize ;
	        			if(open.getLevel0Width()-open.getLevel0Height()>=0){
	        				maxsize = (int) open.getLevel0Width();
	        			}else{
	        				maxsize = (int) open.getLevel0Height();
	        			}
	        			System.out.println(maxsize);
	        			Image = open.createThumbnailImage(0, 0, open.getLevel0Width(), open.getLevel0Height(), maxsize);
	        			IP = new ImagePlus("NewImagePlus", Image);
						
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					 weka2.setTrainingImage(IP);
					 weka2.applyClassifier(true);
					 weka2.getClassifiedImage().show();
					 ZoomImage.remove(imagetransitoire);
					 panel.validate();
					 panel.revalidate();
					 imagetransitoire = caseImage(Image);
					 ZoomImage.add(imagetransitoire);					
					 ZoomImage.revalidate();
					 panel.add(ZoomImage);
					 panel.repaint();
					 CounterButtonOverlay =0;
					 CountZoom =1;
					 zoomCounter =1;
					}
	        });
			
			
			panelSegmentation.setLayout(new GridLayout(30, 0,0,0));
			panelSegmentation.add(trainButton);
			panelSegmentation.add(overlayButton);
			panelSegmentation.add(resultButton);
			
			panelSegmentation.add(probabilityButton);
			panelSegmentation.add(ApplyClassifierInWholeImage);
			addtoClass();
			panelTotalSegmentation.setLayout(new BorderLayout());
			panelTotalSegmentation.add(panelSegmentation,BorderLayout.WEST);
//			SOFTFenetre1 image = new SOFTFenetre1();
//			panelTotalSegmentation.add(image.getPanel());
			
			panelTotalSegmentation.add(panelclass);
			return panelTotalSegmentation;
		}
		
       
		
	    /******************************** convert image to bufferedImage **************/	
		public BufferedImage toBufferedImage(Image img)
		{
		    if (img instanceof BufferedImage)
		    {
		        return (BufferedImage) img;
		    }

		    // Create a buffered image with transparency
		    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		    // Draw the image on to the buffered image
		    Graphics2D bGr = bimage.createGraphics();
		    bGr.drawImage(img, 0, 0, null);
		    bGr.dispose();

		    // Return the buffered image
		    return bimage;
		}
		/**************************************** Creation Button for add trace to class ***************************/
		public void addtoClass() {
			
			
			
		
			if (numOfClasses <= 5){

				JButton buttonBg = new JButton("add to class background ");
				JButton buttonNucleus = new JButton("add to class Nucleus ");
				buttonBg.setBackground(Color.white);
				buttonNucleus.setBackground(Color.white);
				buttonBg.addActionListener(new ActionListener(){
		        	public void actionPerformed(ActionEvent e){
		        		
		        		float[] tabx = new float[l.size()];
		    			int a = 0 ; //position du tableau des x
		    			float[] taby = new float[l.size()];
		    			int b = 0;  //position du tableau des x
		    			
		    			for(int i = 0; i < l.size(); i++){
		    			     if(i%2 == 0){
		    			    	 tabx[a]=(float)(l.get(i));
		    			    	 a = a+1;
		    			     }
		    			     
		    			     if(i%2 == 1){
		    			    	 taby[a]=(float)(l.get(i));
		    			    	 b = b+1;
		    			     }
		    			}
		    			l.remove();
		    			ij.gui.PolygonRoi roi = new ij.gui.PolygonRoi(tabx, taby, Roi.FREELINE);	
		    			
		    			//Weka_Segmentation.addTrace("0", "1");
		        		weka2.addExample(0, roi, 1);
		        		
		        		}
		        });
				buttonNucleus.addActionListener(new ActionListener(){
		        	public void actionPerformed(ActionEvent e){
		        		System.out.println("click sur button nucl");
		        		float[] tabx = new float[l.size()];
		    			int a = 0 ; //position du tableau des x
		    			float[] taby = new float[l.size()];
		    			int b = 0;  //position du tableau des x
		    			//ij.gui.PolygonRoi roi = new ij.gui.PolygonRoi(null, null, Roi.POLYLINE);	
		    			for(int i = 0; i < l.size(); i++){
		    			     if(i%2 == 0){
		    			    	 tabx[a]= (float) l.get(i);
		    			    	 a = a+1;
		    			     }
		    			     
		    			     if(i%2 == 1){
		    			    	 taby[b]=(float) l.get(i);
		    			    	 b = b+1;
		    			     }
		    			}	
		    			l.remove();
		    			ij.gui.PolygonRoi roi = new ij.gui.PolygonRoi(tabx, taby, Roi.FREELINE);
		    			weka2.addExample(1, roi, 1);
		        		}
		        });
			panelclass.setLayout(new GridLayout(30, 0,0,0));
		    panelclass.add(buttonNucleus);
			panelclass.add(buttonBg);
			panelTotalSegmentation.add(panelclass);
			} else {
				System.out.println(" Number of class max atteingned");
			}
		}
	}
	/******************* GETTER AND SETTER ******************************/	
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
		public Weka_Segmentation getWeka() {
			return weka;
		}
		public void setWeka(Weka_Segmentation weka) {
			this.weka = weka;
		}
		public JButton getAddClassButton() {
			return addClassButton;
		}
		public void setAddClassButton(JButton addClassButton) {
			this.addClassButton = addClassButton;
		}
		public BufferedImage getImage2() {
			return Image2;
		}
		public void setImage2(BufferedImage image2) {
			Image2 = image2;
		}	
}

		

		
		
		
		
		
		
		