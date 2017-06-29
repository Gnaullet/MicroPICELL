package soft;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.*;
import javax.swing.filechooser.*;
import javax.swing.table.TableModel;
import org.openslide.OpenSlide;
import ij.ImagePlus;
import ij.gui.Roi;
import ij.process.*;
import trainableSegmentation.*;



public class SOFTFenetre1 extends JFrame { 
	/**
	 * 
	 * @author guillaume
	 *
	 */
	public class MonTraitement implements Runnable {
		private int paramI = 0;
		private int paramJ = 0;

		public MonTraitement(int i, int j) {
			paramI =i;
			paramJ=j;
		}


		public void run() {
			long chrono = java.lang.System.currentTimeMillis() ;
			
			WekaSegmentation weka1 = new WekaSegmentation();
			
			/************  Changement  ***********/
			
			float ee = (float) (counttrhread)/(float) (((tabbuffereds.length)*(tabbuffereds[0].length)));
			ImagePlus ze = new ImagePlus("truc", tabbuffereds[paramI][paramJ]);
			weka1.setTrainingImage(ze);
			boolean aa = weka1.loadClassifier("titi");
			

			ImagePlus RoiClassified2 =weka1.applyClassifier(ze, 8, true);
			//cc.show();

			
			
			ImageProcessor overlaylocal = RoiClassified2.getImageStack().getProcessor(RoiClassified2.getCurrentSlice()).duplicate();
			ColorProcessor colorOverlay3 = overlaylocal.convertToColorProcessor();
			colorOverlay3.autoThreshold();
			colorOverlay3.setBinaryThreshold();
			
			tabbuffereds[paramI][paramJ] = colorOverlay3.getBufferedImage();
			Graphics gg = progress.getGraphics();
			progress.setValue((int) (ee*100));
			progress.updateUI();
			progress.update(gg);
			progress.setBorder(new LineBorder(Color.GRAY));
			progress.validate();
			progress.repaint();
			long chrono2 = java.lang.System.currentTimeMillis() ;
			
			long temps = chrono2 - chrono ;
			
			donnees[counttrhread][0]=counttrhread+1;
			donnees[counttrhread][1]=temps;
			
			counttrhread=counttrhread+1;
			
			if(paramI*paramJ==(tabbuffereds.length-1)*(tabbuffereds[0].length-1)){
				setTexte(new JLabel("Your segmentation is finish, you can go to count "));
			}
		}


	}
	public class MonTraitementtest implements Runnable {
		private int paramI = 0;
		private int paramJ = 0;
		public MonTraitementtest(int i, int j) {
			paramI =i;
			paramJ=j;
		}
		public void run() {
			System.out.println("future "+paramI+" "+paramJ+" Mon traitement " + Thread.currentThread().getName());
		}

	}

	public JPanel getCount() {
		return Count;
	}
	/**
	 * 
	 * @param count
	 */
	public void setCount(JPanel count) {
		Count = count;
	}
	/**
	 * 
	 */
	public Color colorTools = new Color(58, 142, 186);
	public Color colorsegmentation = new Color(53, 122, 183);
	public BufferedImage ROIClassified;
	public BufferedImage newBuffered;
	public String classifieur;
	public Object[][] donnees=null;
	public float ratioExtrapolation;
	public  JProgressBar progress;
	public JPanel aze = new JPanel();
	public BufferedImage thumbnails;
	public int CountExtrapolation = 0;
	private static final long serialVersionUID = 5449375878197333562L;
	private int widthThumbnail;
	private int heightThumbnail;
	public int[] tabPosition;
	public int variable = 200;
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
	public BufferedImage[][] tabbuffereds;
	public ImagePlus[][] tabIP;

	public WekaSegmentation[][] tabweka;
	public boolean applywholeimage = false;

	public String nomFichier;
	public String pathFichier;
	public boolean theboolean = false;
	private ImagePlus IP;
	int x;
	int y;
	private JButton OuvertureButton = null;
	public int counttrhread = 0;
	/******************************** Boolean button ******************/
	public boolean zoom = false;
	public boolean polygon = false;
	public boolean carre = false;
	public boolean trait = false;
	/******************************** Boolean button ******************/
	private File Filechoose;
	private JMenuBar menuBar = new JMenuBar();
	private JMenu ficMenu = new JMenu("Fichier");
	private JMenu editMenu = new JMenu("Edition");
	private JMenuItem openItem = new JMenuItem("ouvrir Fichier");
	private JMenuItem save = new JMenuItem("save");
	private JMenuItem exit = new JMenuItem("exit");
	private JMenuItem colItem = new JMenuItem("titi");
	private JPanel imagetransitoire = new JPanel();
	public OuvertureNDPI ouv;

	public int tabValeur[] = new int[4];
	private double CountZoom=1;
	private JTabbedPane onglet = new JTabbedPane(JTabbedPane.LEFT);

	public BufferedImage buffImage;
	private JPanel Bandeau = new JPanel();
	private JPanel panelPane = new JPanel();
	private LinkedList<Float> l = new LinkedList<Float>();
	private LinkedList<int[]> listCarreASegmenter = new LinkedList<int[]>();
	public LinkedList<BufferedImage[][]> listImageSegmenter = new LinkedList<BufferedImage[][]>();
	public LinkedList<BufferedImage> listImage = new LinkedList<BufferedImage>();
	public int getCountZoom() {
		return (int) CountZoom;
	}
	public void setCountZoom(int CountZoom) {
		this.CountZoom = CountZoom;
	}



	private Graphics g;
	/************ Pour class segmentation ***************/

	private boolean doneone=false;
	public String widhPixelOnMicron ;
	public String heightPixelOnMicron;
	public JPanel panelTotalSegmentation = new JPanel();
	public JPanel panelclass = new JPanel();
	private JButton trainButton = null;
	private JButton overlayButton = null;
	private JButton resultButton = null;
	private JButton probabilityButton = null;
	private JButton Validation=null;
	private JButton ApplyClassifieurinRoiOfimage = null;
	private JButton  ApplyClassifierInWholeImage = null;
	private int CounterButtonOverlay = 0;
	private Weka_Segmentation weka = new Weka_Segmentation();
	private WekaSegmentation weka2 = new WekaSegmentation();
	private JPanel Tools = new JPanel();
	public ImagePlus RoiClassified;
	public ImageProcessor overlay;
	public ColorProcessor colorOverlay;
	public BufferedImage overlayImage;
	public JPanel panneau = new JPanel();
	public JLabel texte;
	private JButton addClassButton = null;
	public JPanel panelSegmentation = new JPanel();
	public JPanel stockTransitoryImage = new JPanel();
	public boolean zoomasked=false;

	/************************** Initialisation de SOFTFenetre1 ******************************/

	public SOFTFenetre1(){
		super();
		build();//On initialise notre fenêtre
		

	}
	private void build(){
		setTitle("Soft Count Nucleus "); 
		Image icone = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/icone.jpg"));
		//ImagePlus EE = new ImagePlus("fioer",icone);
		//EE.show();
		setIconImage(icone);
		
		
		this.setSize((int)getToolkit().getScreenSize().getWidth(), ((int)getToolkit().getScreenSize().getHeight() - 40)); //On donne une taille à notre fenêtre

		setResizable(true); //On autorise le redimensionnement de la fenêtre
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //On dit à l'application de se fermer lors du clic sur la croix
		setContentPane(buildContentPane());
	}
	/**
	 * 
	 * @return
	 */
	private JPanel buildContentPane(){
		/**
		 * 
		 */
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter1 = new FileNameExtensionFilter( "Fichiers ndpi.", "ndpi");
		chooser.addChoosableFileFilter(filter1);
		chooser.setAcceptAllFileFilterUsed(false);
		ImagePreview preview = new ImagePreview(chooser);
		chooser.setAccessory(preview);
		chooser.setApproveButtonText("Choix du fichier (ndpi)..."); 
		tabValeur[0]=-1;
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

		
		OuvertureButton = new JButton("Open your image on NDPI format ");
		OuvertureButton.addActionListener(new ActionListener(){
			private OpenSlide open;
			public void actionPerformed(ActionEvent e){
				int returnVal = chooser.showOpenDialog(getParent());
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					nomFichier=chooser.getSelectedFile().getName();
					pathFichier=chooser.getSelectedFile().getPath();
					classifieur=nomFichier.toString();
					
					Filechoose = chooser.getSelectedFile();
					OpenSlide openend = null;
					try {
						openend = new OpenSlide(Filechoose);
					} catch (IOException e2) {
						e2.printStackTrace();
					}
					try {
						Image = openend.createThumbnailImage(0, 0, openend.getLevel0Width(), openend.getLevel0Height(), 1000);
					} catch (IOException e2) {
						e2.printStackTrace();
					}
					OuvertureNDPI ouv = new OuvertureNDPI(nomFichier, pathFichier, Filechoose, chooser);
					try {
						if(ZoomImage.getComponentCount()>=1){
							
							ZoomImage.remove(imagetransitoire); 
							ZoomImage.validate();
							ZoomImage.repaint();
						}
						imagetransitoire.add(ouv.ouvertureImage(nomFichier,pathFichier,Filechoose,chooser));
						ZoomImage.add(imagetransitoire);
						ZoomImage.validate();
						ZoomImage.repaint();
						
						panel.add(ZoomImage);
						new position();
						buffImage = ouv.ReturnBuffImage(nomFichier,pathFichier,Filechoose,chooser);
						IP = new ImagePlus(nomFichier, buffImage);
						
						weka2.setTrainingImage(IP);
						open = new OpenSlide(Filechoose);
						Map<String, String> properties = open.getProperties();
						properties.size();
						widhPixelOnMicron = properties.get("openslide.mpp-x");
						heightPixelOnMicron = properties.get("openslide.mpp-y");
						
						setVisible(true);
						location(Filechoose);
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
		Tools.add(ToolsBar());
		Tools.setBackground(colorTools);
		panelAffichage.setLayout(new BorderLayout());
		ZoomImage.setBackground(colorsegmentation);
		panelAffichage.add(Tools, BorderLayout.NORTH);
		panelAffichage.add(Bandeau, BorderLayout.SOUTH);
		ficMenu.add(openItem);
		ficMenu.add(save);
		ficMenu.add(exit);
		editMenu.add(colItem);
		ficMenu.setForeground(Color.white);
		editMenu.setForeground(Color.white);
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
						if(ZoomImage.getComponentCount()>=1){
							System.out.println("déja une image");
							ZoomImage.remove(imagetransitoire); 
							ZoomImage.validate();
						}
						imagetransitoire.add(ouv.ouvertureImage(nomFichier,pathFichier,Filechoose,chooser));
						ZoomImage.add(imagetransitoire);
						ZoomImage.validate();
						ZoomImage.repaint();
						panel.add(ZoomImage);
						new position();
						buffImage = ouv.ReturnBuffImage(nomFichier,pathFichier,Filechoose,chooser);
						IP = new ImagePlus(nomFichier, buffImage);
						//IP.show();
						
						weka2.setTrainingImage(IP);
						open = new OpenSlide(Filechoose);
						Map<String, String> properties = open.getProperties();
						properties.size();
						widhPixelOnMicron = properties.get("openslide.mpp-x");
						heightPixelOnMicron = properties.get("openslide.mpp-y");
					
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
		
		setJMenuBar(menuBar);
		menuBar.setBackground(new Color(30, 127, 203));
		Segmentation.add(panel);
		
		Segmentation.setBackground(colorsegmentation);
		panel.setBackground(colorsegmentation);
		Segmentation.setLayout(new BorderLayout());
		
		Segmentation.add(panel,BorderLayout.CENTER);
		Segmentation segm = new Segmentation();
		Segmentation.add(segm.createAPanelContaintSegmentationTools(),BorderLayout.WEST);

		onglet.add("segmentation", Segmentation);

		onglet.add("Count", Count);

		onglet.add("Batch", Batch);

		panelAffichage.add(onglet);
		panelAffichage.setBackground(Color.WHITE);
		CountNucleus cn = new CountNucleus(this);
		Batch batch = new Batch(this);
		onglet.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {

				if(onglet.getSelectedComponent().equals(Count)) {
					panelPane.removeAll();
					Count.setLayout(new BorderLayout());
					BufferedImage[] tabImageClassified = new BufferedImage[listImageSegmenter.size()];
					for(int l=0; l<listImageSegmenter.size();l++){
						tabbuffereds=listImageSegmenter.get(l);
						BufferedImage buffinal = null;
						BufferedImage buf = null;
						for(int i = 0; i<tabbuffereds.length; i++){
							buf = null;
							for(int j=0; j<tabbuffereds[0].length; j++){
								BufferedImage ii = buf;
								if(j==0){
									buf = tabbuffereds[i][0];
									
								}else{
									
									int w1 = ii.getWidth();
									int w2 = tabbuffereds[i][j].getWidth();
									int h2 = tabbuffereds[i][j].getHeight();
									int wMax = w1+w2;
									buf = new BufferedImage(wMax, h2, 1);
									Graphics2D g2 = buf.createGraphics();
									g2.drawImage(ii, 0, 0, null);
									g2.drawImage(tabbuffereds[i][j], w1, 0, null);
								}
							}
							if(i!=0){
								BufferedImage iii = buffinal;
								int h1 = iii.getHeight();
								int w2 = buf.getWidth();
								int h2 = buf.getHeight();
								int hMax = h1+h2;
								buffinal=null;
								buffinal = new BufferedImage(w2, hMax, 1);
								Graphics2D g2 = buffinal.createGraphics();
								g2.drawImage(iii, 0, 0, null);
								g2.drawImage(buf, 0, h1, null); 
							}else{
								buffinal=buf;
							}
						}
						overlayImage = buffinal;
						//ImagePlus ee = new ImagePlus("truc", overlayImage);
						//ee.show();
						tabImageClassified[l]=overlayImage;
					}
		
					Count.add(cn.returnTableOfCount(tabImageClassified, panel, listImage), BorderLayout.WEST);	
				}
				if(onglet.getSelectedComponent().equals(Batch)) {
					Batch.add(batch.ReturnPanelBatch());	
				}
		}
		});
		panelAffichage.setBackground(new Color(53, 122, 183));
		return panelAffichage;
	}
	/**
	 * @throws IOException 
	 * 
	 */
	/********************************************************************/
	public Point location(File Filechoose) throws IOException{

		@SuppressWarnings("resource")
		OpenSlide open = new OpenSlide(Filechoose);
		if(CountZoom==1){
			thumbnails = open.createThumbnailImage(0, 0, open.getLevel0Width(), open.getLevel0Height(), 100);
			Tools.setLayout(new BorderLayout());
			aze = caseImage(thumbnails);
			Tools.add(aze, BorderLayout.EAST);
		}else{
			thumbnails = open.createThumbnailImage(0, 0, open.getLevel0Width(), open.getLevel0Height(), 100);
			Graphics g = thumbnails.getGraphics();
			int x1 = x;
			int y1= y;
			g.setColor(Color.ORANGE);
			int x = 0,y = 0,w = 0,h = 0;
			x=(int) ((int) thumbnails.getWidth()*x1/(int) open.getLevel0Width()); 
			y=(int) ((int) thumbnails.getHeight()*y1/(int) open.getLevel0Height());;
			w= (int) ((int) thumbnails.getWidth()*(int) widthThumbnail/(int) open.getLevel0Width());
			
			h = (int) ((int) thumbnails.getHeight()*(int) heightThumbnail/(int) open.getLevel0Height());
			g.drawRect(x, y, w , h);
			Tools.remove(aze);
			Tools.validate();
			aze = caseImage(thumbnails);
			Tools.add(aze, BorderLayout.EAST);
		}
		return null;
	}
	/******************************* Prefixe *********************************/
	public class MonFiltre extends FileFilter {
		String [] lesSuffixes;
		String  laDescription;
		public MonFiltre(String []lesSuffixes, 
				String laDescription){
			this.lesSuffixes = lesSuffixes;
			this.laDescription = laDescription;
		}
		boolean appartient( String suffixe ){
			for( int i = 0; i<lesSuffixes.length; ++i)
				if(suffixe.equals(lesSuffixes[i]))
					return true;
			return false;
		}
		public boolean accept(File f) {
			if (f.isDirectory())  return true;
			String suffixe = null;
			String s = f.getName();
			int i = s.lastIndexOf('.');
			if(i > 0 &&  i < s.length() - 1)
				suffixe=s.substring(i+1).toLowerCase();
			return suffixe!=null&&appartient(suffixe);
		}
		public String getDescription() {
			return laDescription;
		}
	}
	
	/******************* Tools Bar ************************/
	
	public JPanel ToolsBar(){

		JPanel Tools = new JPanel();
		
		JButton buttonBox = new javax.swing.JButton(new ImageIcon(this.getClass().getResource("/carre.png")));
		JButton jButton2 = new javax.swing.JButton(new ImageIcon(getClass().getResource("/cercle.png")));
		JButton jButton3 = new javax.swing.JButton(new ImageIcon(getClass().getResource("/trait.png")));
		JButton jButton4 = new javax.swing.JButton(new ImageIcon(getClass().getResource("/loupe.png")));
		JButton jButton5 = new javax.swing.JButton(new ImageIcon(getClass().getResource("/texte.png")));

		buttonBox.setText("buttonBox");
		buttonBox.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		buttonBox.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		buttonBox.setBackground(Color.white);

		buttonBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				carre = true;
				trait = false;
				polygon = false;
				dessinerRec dr = new dessinerRec();
				dr.init();
			}
		});
		Tools.add(buttonBox);

		jButton2.setText("cercle");
		jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		Tools.add(jButton2);
		jButton2.setBackground(Color.white);

		jButton3.setText("trait");
		jButton3.setFocusable(false);
		jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		Tools.add(jButton3);
		jButton3.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				carre=false;
				polygon = false;
				trait=true;
				DessinTrait dt = new DessinTrait();
				dt.init();
			}
		});
		jButton3.setBackground(Color.white);

	

		
		JButton Polygon = new javax.swing.JButton(new ImageIcon(this.getClass().getResource("/polygone.png")));

		Polygon.setText("Polygon");
		Polygon.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		Polygon.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		Polygon.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				carre=false;
				polygon = true;
				trait=false;
				PolygonMaker dr = new PolygonMaker();
				dr.init();	
			}
		});
		Tools.add(Polygon);
		Polygon.setBackground(Color.white);

		jButton4.setText("zoom");
		jButton4.setFocusable(false);
		jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		Tools.add(jButton4);
		jButton4.addActionListener(new ActionListener(){


			public void actionPerformed(ActionEvent e){

				new Zoom();
			}


		});
		jButton4.setBackground(Color.white);
		jButton5.setText("annotation");
		jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		jButton5.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){

			}});
		jButton5.setBackground(Color.white);
		Tools.add(jButton5);
		Tools.setBackground(Color.white);
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


	/****************************** Dessin Trait ****************************/
	public class DessinTrait implements MouseListener, MouseMotionListener {

		private int x1, y1;
		Graphics g;

		public void init() {
			if(trait == true){
				imagetransitoire.addMouseListener(this);
				imagetransitoire.addMouseMotionListener(this);
			}

		}

		public void mousePressed(MouseEvent e){
			if(trait == true){
				int x,y;
				x = e.getX();
				y = e.getY();
				x1=x; y1=y;
			}
		}
		public void mouseDragged(MouseEvent e){
			if(trait == true){
				Graphics g = imagetransitoire.getGraphics();
				g.setColor(Color.ORANGE);
				g.drawLine(this.x1, this.y1, e.getX(), e.getY());
				mouseMoved(e);
				addPositionDraw();
			}
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
				if(CountExtrapolation!=0){
					float xdessin = (float) ((((float)(this.x1))/(2*(CountExtrapolation))));
					float ydessin = (float) ((((float)(this.y1))/(2*(CountExtrapolation))));
					
					l.add(xdessin);
					l.add(ydessin);
				}else{
					float xdessin = (float) ((((float)(this.x1))/((imagetransitoire.getWidth()))*(widthThumbnail)));
					float ydessin = (float) ((((float)(this.y1))/(imagetransitoire.getHeight())*(heightThumbnail)));
					l.add(xdessin);
					l.add(ydessin);
				}
			}

		}

	}
	/************************ Dessin Polygon ********************************************/
	public class PolygonMaker implements MouseListener, MouseMotionListener {
		int numberOfClicks;
		int[] xCoordinates;
		int[] yCoordinates;

		public void init()
		{
			
		   numberOfClicks = 0;
		   xCoordinates = new int [200];
		   yCoordinates = new int [200];
		   if(polygon == true){
			   imagetransitoire.addMouseListener(this);
		   }
		}

		public void paint(Graphics g)
		{
			g.setColor(Color.blue);
			
			if((Math.abs(xCoordinates[numberOfClicks-1]-xCoordinates[0]))<10
					&& (Math.abs(yCoordinates[numberOfClicks-1]-yCoordinates[0]))<10 ) {
				xCoordinates[numberOfClicks] = xCoordinates[0];
				yCoordinates[numberOfClicks] = yCoordinates[0];
				g.drawPolygon(xCoordinates,yCoordinates,numberOfClicks);
				//ij.gui.PolygonRoi roi = new ij.gui.PolygonRoi(xCoordinates, yCoordinates, numberOfClicks, Roi.POLYGON);
				//ImagePlus gg = new ImagePlus("tr", Image);
				//roi.setImage(gg);
				//roi.draw(g);
				
				//ImageProcessor vv = roi.getMask();
				//ImagePlus blbl = new ImagePlus("frzf",vv.getBufferedImage());
				//blbl.show();
				//roi.drawPixels(vv);
				//roi.drawOverlay(g);
				//	ImagePlus vv1 = roi.getImage();
				//	vv1.show();
				//if(xCoordinates[numberOfClicks] == xCoordinates[0]){
				//	numberOfClicks=0;
					//polygon=false;
				//}
				//zz.draw();
				//zz.show();
				CarreSegmenter();
				
				Validation.setEnabled(true);
			}
			
			

		}
		public void CarreSegmenter(){
			
			int xmin = 1000000;
			int xmax =0;
			int ymin = 1000000;
			int ymax =0;
			for(int i=0; i<(numberOfClicks+1);i++){
				if(xCoordinates[i]<xmin){
					xmin=xCoordinates[i];
				
				}
				if(xCoordinates[i]>xmax){
					xmax=xCoordinates[i];
					
				}
				if(yCoordinates[i]<ymin){
					ymin=yCoordinates[i];
					
				}
				if(yCoordinates[i]>ymax){
					ymax=yCoordinates[i];
				}
			}
			Graphics g = imagetransitoire.getGraphics();
			g.setColor(Color.ORANGE);
			g.drawRect(xmin, ymin, xmax-xmin , ymax-ymin);
			addPositionOfRectangle(xmin, ymin, xmax-xmin, ymax-ymin);	
			numberOfClicks=0;
		}
		
			public void mousePressed(MouseEvent e)
			{
				if(polygon == true){
					
					xCoordinates[numberOfClicks] = e.getX();
					yCoordinates[numberOfClicks] = e.getY();
					Graphics g=imagetransitoire.getGraphics();
					g.setColor(Color.RED);
					if(numberOfClicks!=0){
						g.drawLine(xCoordinates[numberOfClicks-1], yCoordinates[numberOfClicks-1], xCoordinates[numberOfClicks], yCoordinates[numberOfClicks]);
					}
					numberOfClicks++;
				if(numberOfClicks>2){
					paint(g);
				}
			}
			}
			
			public void mouseDragged(MouseEvent arg0) {}			
			public void mouseMoved(MouseEvent arg0) {}
			public void mouseClicked(MouseEvent arg0) {}
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mouseReleased(MouseEvent arg0) {}
			public void addPositionOfRectangle(int x1,int y1,int x,int y){
				tabValeur[0] = x1;
				tabValeur[1] = y1;
				tabValeur[2] = x;
				tabValeur[3] = y;
				listCarreASegmenter.add(tabValeur);
				tabValeur=null;
				tabValeur = new int[4];
			}
	
}
	/************************ DESSIN RECTANGLE ****************************************/

	public class dessinerRec implements MouseListener, MouseMotionListener {

		private int x1, y1;
		Graphics g;

		public void init() {
			if(carre==true){
				// écouteurs
				imagetransitoire.addMouseListener(this);
				imagetransitoire.addMouseMotionListener(this);
			}
		}

		public void mousePressed(MouseEvent e){
			if(carre==true){
				int x,y;
				x = e.getX();
				y = e.getY();
				x1=x; y1=y;
				
			}
		}

		//événement déplacement souris avec bouton enfoncé
		public void mouseReleased(MouseEvent e){
			if(carre==true){
				Graphics g = imagetransitoire.getGraphics();
				g.setColor(Color.ORANGE);
				int x,y;
				x=e.getX();
				y=e.getY();
				g.drawRect(x1, y1, x-x1, y-y1);
				addPositionOfRectangle(x1, y1, x-x1, y-y1);
			}
		}
		//événement lors du déplacement de la souris
		public void mouseMoved(MouseEvent e){}
		public void mouseEntered(MouseEvent event) {}  
		public void mouseExited(MouseEvent evt){}
		public void mouseClicked(MouseEvent event){}
		public void mouseDragged(MouseEvent arg0) {}
		public void addPositionOfRectangle(int x1,int y1,int x,int y){
			
			tabValeur[0] = x1;
			tabValeur[1] = y1;
			tabValeur[2] = x;
			tabValeur[3] = y;
		}
	}

	/**************************** Zoom  ************************************/

	public class Zoom implements MouseListener, MouseMotionListener, MouseWheelListener{

		public Zoom(){
			if (zoomasked==false){
				imagetransitoire.addMouseListener(this); 
				imagetransitoire.addMouseWheelListener(this); 
				zoomasked=true;
			}
		}
		public void mouseWheelMoved(MouseWheelEvent e) {
	        int notches = e.getWheelRotation();
	       

			OpenSlide open = null;
			
			try {
				open = new OpenSlide(getFilechoose());

			} catch (IOException e2) {
				
				e2.printStackTrace();
			}

			boolean Dezoom = false;
			 if (notches < 0) {
				 if(CountZoom==0){
						CountZoom=2;
					}else{
						CountZoom = CountZoom*2;
					}
					Dezoom = false;
	             //ici tu scroll ver le haut
	        } else {
	        	if(CountZoom != 1){
					CountZoom = CountZoom/2;
					Dezoom = true;
				}
	             //ici tu scroll vers le bas
	        }

			try {
				widthThumbnail = (int)(open.getLevel0Width()/(CountZoom));
				heightThumbnail = (int)(open.getLevel0Height()/(CountZoom));
				float w = (e.getX());
				float z = (e.getY());
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
					x=0; y=0; widthThumbnail= (int) open.getLevel0Width(); heightThumbnail=(int) open.getLevel0Height();
				}else{
					pcZoom = (int)CountZoom*2;
					Image = open.createThumbnailImage(x,y,widthThumbnail ,heightThumbnail ,1000);
				}
				widthThumbnail = (int)(open.getLevel0Width()/(CountZoom));
				heightThumbnail = (int)(open.getLevel0Height()/(CountZoom));
				IP = new ImagePlus("NewImagePlus", Image);
				newBuffered=Image;
				weka2.setTrainingImage(IP);
				if(widthThumbnail<800 && heightThumbnail<800){	
					if(Dezoom == false){
						CountExtrapolation = CountExtrapolation+1;
						BufferedImage before = Image;
						
						int w1 = before.getWidth();
						int h = before.getHeight();
						BufferedImage after = new BufferedImage(w1*(2*CountExtrapolation), h*(2*CountExtrapolation), BufferedImage.TYPE_INT_ARGB);
						AffineTransform at = new AffineTransform();
						at.scale(2*CountExtrapolation, 2*CountExtrapolation);
						AffineTransformOp scaleOp = 
								new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
						after = scaleOp.filter(before, after);
						Image=after;
					}else{
						CountExtrapolation = CountExtrapolation-1;
						BufferedImage before = Image;
						int w1 = before.getWidth();
						int h = before.getHeight();
						BufferedImage after = new BufferedImage(w1*(2*CountExtrapolation), h*(2*CountExtrapolation), BufferedImage.TYPE_INT_ARGB);
						AffineTransform at = new AffineTransform();
						at.scale(2*CountExtrapolation, 2*CountExtrapolation);
						AffineTransformOp scaleOp = 
								new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
						after = scaleOp.filter(before, after);
						Image=after;
					}
				}

				location(Filechoose);
				ZoomImage.remove(imagetransitoire);
				panel.validate();
				panel.remove(label);
				panel.revalidate();
				pcZoom = (int)CountZoom;
				panel.setLayout(new GridBagLayout());
				float f = Float.valueOf(widhPixelOnMicron.trim()).floatValue();
				float g = Float.valueOf(heightPixelOnMicron.trim()).floatValue();
				float widthReal = (float) (f*widthThumbnail);
				float heightReal = (float) (g*heightThumbnail);
				label = new JLabel( "<html>Niveau de Zoom : " +(pcZoom) +  " largeur de la ROI : " + widthReal + " micron " + "  hauteur de la ROI : "+ heightReal+ " micron </html>");
				panel.setLayout(new BorderLayout());
				label.setHorizontalAlignment(JLabel.CENTER);
				label.setVerticalAlignment(JLabel.CENTER);
				panel.add(label,BorderLayout.NORTH);
				
				imagetransitoire = caseImage(Image);
				new position();
				ZoomImage.add(imagetransitoire);	
				imagetransitoire.addMouseListener(this); 
				ZoomImage.revalidate();
				panel.add(ZoomImage);
				panel.repaint();
			}catch (IOException e1) {
				e1.printStackTrace();
			}
		
	    } 
		public void mouseClicked(MouseEvent e) {
			OpenSlide open = null;
			try {
				open = new OpenSlide(getFilechoose());

			} catch (IOException e2) {
				e2.printStackTrace();
			}


			int buttonDown = e.getButton();
			boolean Dezoom = false;
			if (buttonDown == MouseEvent.BUTTON1) { // action left-click
				//	zoomCounter = zoomCounter+1; 
				if(CountZoom==0){
					CountZoom=2;
				}else{
					CountZoom = CountZoom*2;
				}
				Dezoom = false;
			} 
			if(buttonDown == MouseEvent.BUTTON3) {
				if(CountZoom != 1){
					CountZoom = CountZoom/2;
					Dezoom = true;
				}
			}// TODO oyugfiuykbjopkl
			if (buttonDown == MouseEvent.BUTTON2) {
			}
			if (buttonDown == MouseEvent.BUTTON2) {
			}


			try {
				widthThumbnail = (int)(open.getLevel0Width()/(CountZoom));
				heightThumbnail = (int)(open.getLevel0Height()/(CountZoom));
				float w = (e.getX());
				float z = (e.getY());
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
					x=0; y=0; widthThumbnail= (int) open.getLevel0Width(); heightThumbnail=(int) open.getLevel0Height();
				}else{
					pcZoom = (int)CountZoom*2;
					Image = open.createThumbnailImage(x,y,widthThumbnail ,heightThumbnail ,1000);
				}
				widthThumbnail = (int)(open.getLevel0Width()/(CountZoom));
				heightThumbnail = (int)(open.getLevel0Height()/(CountZoom));
				IP = new ImagePlus("NewImagePlus", Image);
				newBuffered=Image;
				weka2.setTrainingImage(IP);
				if(widthThumbnail<800 && heightThumbnail<800){	
					if(Dezoom == false){
						CountExtrapolation = CountExtrapolation+1;
						BufferedImage before = Image;
						int w1 = before.getWidth();
						int h = before.getHeight();
						BufferedImage after = new BufferedImage(w1*(2*CountExtrapolation), h*(2*CountExtrapolation), BufferedImage.TYPE_INT_ARGB);
						AffineTransform at = new AffineTransform();
						at.scale(2*CountExtrapolation, 2*CountExtrapolation);
						AffineTransformOp scaleOp = 
								new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
						after = scaleOp.filter(before, after);
						Image=after;
					}else{
						CountExtrapolation = CountExtrapolation-1;
						BufferedImage before = Image;
						int w1 = before.getWidth();
						int h = before.getHeight();
						BufferedImage after = new BufferedImage(w1*(2*CountExtrapolation), h*(2*CountExtrapolation), BufferedImage.TYPE_INT_ARGB);
						AffineTransform at = new AffineTransform();
						at.scale(2*CountExtrapolation, 2*CountExtrapolation);
						AffineTransformOp scaleOp = 
								new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
						after = scaleOp.filter(before, after);
						Image=after;
					}
				}

				location(Filechoose);
				ZoomImage.remove(imagetransitoire);
				panel.validate();
				panel.remove(label);
				panel.revalidate();
				pcZoom = (int)CountZoom;
				panel.setLayout(new GridBagLayout());
				float f = Float.valueOf(widhPixelOnMicron.trim()).floatValue();
				float g = Float.valueOf(heightPixelOnMicron.trim()).floatValue();
				float widthReal = (float) (f*widthThumbnail);
				float heightReal = (float) (g*heightThumbnail);
				label = new JLabel( "<html>Niveau de Zoom : " +(pcZoom) +  " largeur de la ROI : " + widthReal + " micron " + "  hauteur de la ROI : "+ heightReal+ " micron </html>");
				panel.setLayout(new BorderLayout());
				label.setHorizontalAlignment(JLabel.CENTER);
				label.setVerticalAlignment(JLabel.CENTER);
				panel.add(label,BorderLayout.NORTH);
				
				imagetransitoire = caseImage(Image);
				new position();
				ZoomImage.add(imagetransitoire);	
				imagetransitoire.addMouseListener(this); 
				ZoomImage.revalidate();
				panel.add(ZoomImage);
				panel.repaint();

			}catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		public void mouseDragged(MouseEvent arg0) {}
		public void mouseMoved(MouseEvent arg0) {}
		public void mouseEntered(MouseEvent e){}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
	}

	/************************************ POSITION *******************************/

	public class position implements MouseListener, MouseMotionListener{
		position(){
			imagetransitoire.addMouseListener(this);
			Bandeau.setBackground(Color.WHITE);;
		}


		public void mouseExited(MouseEvent e) {}

		public void mouseDragged(MouseEvent e) {}
		public void mouseMoved(MouseEvent e) {Bandeau.setLayout(new BorderLayout());
		Bandeau.remove(label2);
		Bandeau.revalidate();		
		int r = Image.getRGB( e.getX(), e.getY())& 0xFFFFFF;
		Color ee = getColor(e.getX(),e.getY());
		label2 = new JLabel("<html>             x = " + e.getX() + " y =  " + e.getY() + " ca c'est la couleur sans alpha  "+r + " Color RGB : "+ ee +" </html>");
		Bandeau.add(label2, BorderLayout.CENTER);}

		public void mouseEntered(MouseEvent e){
			Bandeau.setLayout(new BorderLayout());
			Bandeau.remove(label2);
			Bandeau.revalidate();		
			int r = Image.getRGB( e.getX(), e.getY())& 0xFFFFFF;
			Color ee = getColor(e.getX(),e.getY());
			label2 = new JLabel("<html>             x = " + e.getX() + " y =  " + e.getY() + " ca c'est la couleur sans alpha  "+r + " Color RGB : "+ ee +" </html>");
			Bandeau.add(label2, BorderLayout.CENTER);
		}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
		public void mouseClicked(MouseEvent e) {
			Bandeau.setLayout(new BorderLayout());
			Bandeau.remove(label2);
			Bandeau.revalidate();		
			int r = Image.getRGB( e.getX(), e.getY())& 0xFFFFFF;
			Color ee = getColor(e.getX(),e.getY());
			label2 = new JLabel("<html>             x = " + e.getX() + " y =  " + e.getY() + " ca c'est la couleur sans alpha  "+r + " Color RGB : "+ ee +" </html>");
			Bandeau.add(label2, BorderLayout.CENTER);

		}
		public Color getColor(int x, int y) {
			int rgb = Image.getRGB(x,y);
			Color c = new Color(rgb);
			return c;
		}
	}

	/******************************** Panel Segmentation **************************/

	class Segmentation  {
		public JPanel createAPanelContaintSegmentationTools(){
			trainButton = new JButton("Train classifier");
			trainButton.setToolTipText("Start training the classifier");
			trainButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					boolean z = weka2.trainClassifier();
					System.out.println(z);
					weka2.applyClassifier(true);
					weka2.saveClassifier("titi");
					probabilityButton.setEnabled(true);
					overlayButton.setEnabled(true);
				}
			});

			overlayButton = new JButton("Toggle overlay");
			overlayButton.setToolTipText("Toggle between current segmentation and original image");
			overlayButton.setEnabled(false);
			overlayButton.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent e){
					//IP = new ImagePlus("NewImagePlus", Image);
					//weka2.setTrainingImage(IP);
					//weka2.applyClassifier(true);
					
					RoiClassified = weka2.getClassifiedImage();
					overlay = RoiClassified.getImageStack().getProcessor(RoiClassified.getCurrentSlice()).duplicate();
					colorOverlay = overlay.convertToColorProcessor();

					colorOverlay.getAutoThreshold();
					colorOverlay.autoThreshold();
					colorOverlay.setBinaryThreshold();
					overlayImage = colorOverlay.getBufferedImage();
					
					if(widthThumbnail<800 &&heightThumbnail<800){
					BufferedImage before = overlayImage;
					
					int w1 = before.getWidth();
					int h = before.getHeight();
					BufferedImage after = new BufferedImage(w1*(2*(CountExtrapolation)), h*(2*(CountExtrapolation)), BufferedImage.TYPE_INT_ARGB);
					AffineTransform at = new AffineTransform();
					at.scale(CountExtrapolation*2, CountExtrapolation*2);
					AffineTransformOp scaleOp = 
							new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
					after = scaleOp.filter(before, after);
					overlayImage=after;
					}
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
			});

			resultButton = new JButton("Create result");
			resultButton.setToolTipText("Generate result image");
			resultButton.setEnabled(false);
			resultButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					
				}
			});

			probabilityButton = new JButton("Get probability");
			probabilityButton.setToolTipText("Generate current probability maps");
			probabilityButton.setEnabled(false);
			probabilityButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					ImagePlus RoiClassified = weka2.getClassifiedImage();
					RoiClassified.show();
				}
			});
			
			
			ApplyClassifieurinRoiOfimage = new JButton("Apply Classifieur in Roi of image");
			ApplyClassifieurinRoiOfimage.setToolTipText("Select your ROI with ");
			ApplyClassifieurinRoiOfimage.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					polygon=true;
					
					/****************** On charge une thumbnail de l'image entière *****************/
					
					OpenSlide open = null;
					try {
						open = new OpenSlide(Filechoose);
					} catch (IOException e2) {
						System.out.println("ne marche pas");
						e2.printStackTrace();
					}

					BufferedImage image = null;
					try {
						image = open.createThumbnailImage( 0 ,0 , open.getLevel0Width() ,open.getLevel0Height() ,900);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					ZoomImage.remove(imagetransitoire);
					panel.validate();
					panel.revalidate();
					imagetransitoire = caseImage(image);
					
					ZoomImage.add(imagetransitoire);					
					ZoomImage.validate();
					panel.add(ZoomImage);
					panel.repaint();
					texte = new JLabel("Veuillez patienter pendant le chargement...");
					progress = new JProgressBar(0, 100); 
					progress.setStringPainted(true);
					panneau.add("Center", texte);
					panneau.add("Center", progress); 
					Bandeau.add(panneau, BorderLayout.EAST);
					Bandeau.setVisible(true);

					PolygonMaker rec = new PolygonMaker();
					rec.init();
					

				}		
			});
			Validation = new JButton("Valide your ROI");
			Validation.setToolTipText("Validation of your ROI TRACE");
			Validation.setEnabled(false);
			//probabilityButton.setEnabled(false);
			Validation.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					System.out.println(listCarreASegmenter.size());
					for(int i=0; i<listCarreASegmenter.size();i++){
						tabValeur=null;
						System.out.println(i+ " "+listCarreASegmenter.get(i));
						tabValeur=listCarreASegmenter.get(i);
					
					OpenSlide open = null;

					try {
						
						open = new OpenSlide(Filechoose);
						
						int largeur = ZoomImage.getWidth();
						int hauteur = ZoomImage.getHeight();
						x = (int) ((tabValeur[0]*(int)(open.getLevel0Width()))/largeur);
						y = (int) ((tabValeur[1]*(int)(open.getLevel0Height()))/hauteur);
						long larg =  (tabValeur[2]*(open.getLevel0Height())/largeur);
						long haut =  (tabValeur[3]*(open.getLevel0Height())/hauteur);
						int maxsize;
						if(larg-haut>=0){
							maxsize = (int) larg;
						}else{
							maxsize = (int) haut;
						}
						listImage.add(open.createThumbnailImage(x, y, larg, haut, maxsize));
						tabbuffereds = new BufferedImage[((int) haut/variable)+1][((int) larg/variable)+1];
							tabweka = new WekaSegmentation[((int) haut/variable)+1][((int) larg/variable)+1];
							tabIP = new ImagePlus[((int) haut/variable)+1][((int) larg/variable)+1];
							


							int counti=0;
							int county=0;

							for(int i1=0; i1<=haut;i1=i1+variable){


								long hauteurThumbnailToCreate;
								if ((haut-(i1))<variable){
									hauteurThumbnailToCreate = haut-(i1);
								}else{
									hauteurThumbnailToCreate=variable;
								}


								for(int j=0; j<=larg;j=j+variable){
									long largeurThumbnailToCreate;
									if ((larg-(j))<variable){
										largeurThumbnailToCreate = larg-(j);
									}else{
										largeurThumbnailToCreate=variable;
									}
									try {
										tabbuffereds[counti][county]= open.createThumbnailImage((x+j),(y+i1), largeurThumbnailToCreate, hauteurThumbnailToCreate, variable);
										BufferedImage aze = tabbuffereds[counti][county];


										tabIP[counti][county]=new ImagePlus("gvy",aze);;
										tabweka[counti][county]= new WekaSegmentation();
									} catch (IOException e2) {
										System.out.println("error in try");
										e2.printStackTrace();
									}
									county=county+1;
								}
								county=0;
								counti=counti+1;
							}
						
								donnees = new Object[tabbuffereds.length*tabbuffereds[0].length][2];
								int nbProcs = Runtime.getRuntime().availableProcessors();
								nbProcs -=Math.round(nbProcs*50/100);
								ExecutorService executorService = Executors.newFixedThreadPool(8);
								for(int k = 0; k< tabbuffereds.length; k++){
									for (int l =0; l<tabbuffereds[0].length; l++){
										Runnable runnable = new MonTraitement(k,l); 
										executorService.submit(runnable);
									}
								}

								executorService.shutdown();
								executorService.awaitTermination(99999, TimeUnit.SECONDS);
//								String[] entetes = {"nbdethread", "Time"};
//								JTable tableau = new JTable(donnees, entetes);
//								System.out.println("tableau : \n"+tableau);
//								for(int j = 0;j<(tabbuffereds.length*tabbuffereds[0].length);j++){
//									System.out.println(donnees[j][0]);
//									System.out.println(donnees[j][1]);
//								}	 
//								JFileChooser newJFileChoose = new JFileChooser();
//								int ee = JFileChooser.DIRECTORIES_ONLY;
//								System.out.println(ee);
//								newJFileChoose.setFileSelectionMode(1);
//								newJFileChoose.setApproveButtonText("Choose the location of your table ");
//								int returnVal = newJFileChoose.showOpenDialog(getParent());
//								if(returnVal == JFileChooser.APPROVE_OPTION) {			 
//									String filechoose = newJFileChoose.getSelectedFile().getPath();
//									exportTable(tableau, new File(filechoose+"/TestThread.csv")); 
//								}
							
						
					}catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e3) {
						// TODO Auto-generated catch block
						e3.printStackTrace();
					}
					System.out.println("finish");
					listImageSegmenter.add(tabbuffereds);///
					}
					System.out.println("finish finish");
					Bandeau.remove(panneau);
					Bandeau.validate();
					texte = new JLabel("Segmentation finish , You can go to panel Count ");
					progress.setEnabled(true);
					progress.setValue(100);
					panneau.removeAll();
					panneau.add(texte);
					panneau.add(progress); 


					Bandeau.add(panneau, BorderLayout.EAST);
					Bandeau.setVisible(true);
					Bandeau.revalidate();
					Bandeau.repaint();

					applywholeimage=true;
					theboolean=true;


				

				}
			}); 
			ApplyClassifierInWholeImage = new JButton("Apply Classifier In Whole Image");
			ApplyClassifierInWholeImage.setToolTipText("Start training the classifier");
			ApplyClassifierInWholeImage.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					ZoomImage.add(imagetransitoire);					
					ZoomImage.validate();
					panel.add(ZoomImage);
					panel.repaint();
					texte = new JLabel("Veuillez patienter pendant le chargement...");
					progress = new JProgressBar(0, 100); 
					progress.setStringPainted(true);
					panneau.add("Center", texte);
					panneau.add("Center", progress); 
					Bandeau.add(panneau, BorderLayout.EAST);
					Bandeau.setVisible(true);



					OpenSlide open = null;

					try {
						open = new OpenSlide(Filechoose);
					} catch (IOException e2) {

						e2.printStackTrace();

					}
					try {
						boolean tab = false;
						int maxsize ;
						if(open.getLevel0Width()-open.getLevel0Height()>=0){
							maxsize = (int) open.getLevel0Width();
						}else{
							maxsize = (int) open.getLevel0Height();
						}
						if (maxsize<1000){
							weka2.loadClassifier("titi");
							Image = open.createThumbnailImage(0, 0, open.getLevel0Width(), open.getLevel0Height(), maxsize);
							IP = new ImagePlus("NewImagePlus", Image);
							weka2.setTrainingImage(IP);
							weka2.applyClassifier(true);
							ZoomImage.remove(imagetransitoire);
							panel.validate();
							panel.revalidate();
						}else{
							tab = true;
							int variable= 2000;
							tabbuffereds = new BufferedImage[((int) open.getLevel0Height()/variable)+1][((int) open.getLevel0Width()/variable)+1];
							tabweka = new WekaSegmentation[((int) open.getLevel0Height()/variable)+1][((int) open.getLevel0Width()/variable)+1];
							tabIP = new ImagePlus[((int) open.getLevel0Height()/variable)+1][((int) open.getLevel0Width()/variable)+1];

							for(int i=0; i<=open.getLevel0Height();i=i+variable){
								long hauteurThumbnailToCreate;
								long largeurThumbnailToCreate;
								if ((open.getLevel0Height()-(i))<variable){
									hauteurThumbnailToCreate = open.getLevel0Height()-(i);
								}else{
									hauteurThumbnailToCreate=variable;
								}
								for(int j=0; j<=open.getLevel0Width();j=j+variable){
									if ((open.getLevel0Width()-(j))<variable){
										largeurThumbnailToCreate = open.getLevel0Width()-(j);
									}else{
										largeurThumbnailToCreate=variable;
									}
									try {
										tabbuffereds[i/variable][j/variable]= open.createThumbnailImage(j, i, largeurThumbnailToCreate, hauteurThumbnailToCreate, variable);
										tabIP[i/variable][j/variable]=new ImagePlus("gvy",tabbuffereds[i/variable][j/variable]);
										tabweka[i/variable][j/variable]= new WekaSegmentation();
									} catch (IOException e2) {
										System.out.println("error in try");
										e2.printStackTrace();
									}
								}
							}
						}
						if(tab==true){

							theboolean =true;
							
							JPanel panneau = new JPanel();
							JLabel texte = new JLabel("Veuillez patienter pendant le chargement...");
							progress = new JProgressBar(0, 100); 
							panneau.add("Center", progress); 
							panneau.add("Center", texte); 
							ExecutorService executorService = Executors.newFixedThreadPool(8);
							donnees = new Object[tabbuffereds.length*tabbuffereds[0].length+1][tabbuffereds.length*tabbuffereds[0].length+1];
							for(int k = 0; k< tabbuffereds.length; k++){
								for (int l =0; l<tabbuffereds[0].length; l++){
									Runnable runnable = new MonTraitement(k,l); 
									executorService.submit(runnable);
								}
							}
							executorService.shutdown();
							executorService.awaitTermination(99999, TimeUnit.SECONDS);
							
							String[] entetes = {"nbdethread", "Time"};
							JTable tableau = new JTable(donnees, entetes);
							System.out.println("tableau : \n"+tableau);
							 
							JFileChooser newJFileChoose = new JFileChooser();
							int ee = JFileChooser.DIRECTORIES_ONLY;
						
							newJFileChoose.setFileSelectionMode(1);
							newJFileChoose.setApproveButtonText("Choose the location of your table ");
							int returnVal = newJFileChoose.showOpenDialog(getParent());
							if(returnVal == JFileChooser.APPROVE_OPTION) {			 
								String filechoose = newJFileChoose.getSelectedFile().getPath();
								exportTable(tableau, new File(filechoose+"/TestThread.csv")); 
							}
						}


					} catch (InterruptedException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					applywholeimage=true;
					System.out.println("fini");
					System.out.println("finish");
					Bandeau.remove(panneau);
					Bandeau.validate();
					texte = new JLabel("Segmentation finish , You can go to panel Count ");
					progress.setStringPainted(true);
					progress.setValue(100);
					panneau.removeAll();
					panneau.add(texte);
					panneau.add(progress); 
					Bandeau.add(panneau, BorderLayout.EAST);
					Bandeau.setVisible(true);
					Bandeau.revalidate();
					Bandeau.repaint();
					applywholeimage=true;
					theboolean=true;
				}
			});
			
			JButton TEST = new JButton("TEST");
			TEST.setToolTipText("Start training the classifier");
			TEST.addActionListener(new ActionListener(){
				private OpenSlide openl;

				public void actionPerformed(ActionEvent e){
					try {
						openl = new OpenSlide(Filechoose);
						long[] tabtest = new long[64];
						Object[][] donnees = new Object[64][2];
						for(int i=0;i<64;i++){
							long chrono = java.lang.System.currentTimeMillis() ; 
							BufferedImage qsd = openl.createThumbnailImage(20000, 20000, 2000, 2000, 6000);
							ImagePlus IPtest = new ImagePlus("blbi", qsd);
							WekaSegmentation weka2 = new WekaSegmentation();
							weka2.setTrainingImage(IPtest);
							weka2.loadClassifier("titi");
							weka2.applyClassifier(i, true);
							long chrono2 = java.lang.System.currentTimeMillis() ;
							long temps = chrono2 - chrono ;
							tabtest[i]=temps;
							donnees[i][0]=i+1;
							donnees[i][1]=tabtest[i];
							System.out.println("Temps ecoule = " + temps + " ms") ;
						}
						String[] entetes = {"nbdethread", "Time"};
						JTable tableau = new JTable(donnees, entetes);
						System.out.println("tableau : \n"+tableau);
						for(int j = 0;j<64;j++){
							System.out.println(donnees[j][0]);
							System.out.println(donnees[j][1]);
						} 
							String filechoose = "/sandbox/gnaullet";
							exportTable(tableau, new File(filechoose+"/TestThread.csv"));
						} catch (IOException ex) {
							System.out.println(ex.getMessage());
							ex.printStackTrace();
					} 
				}
			});

			GridLayout gl = new GridLayout(15, 15);
			gl.setHgap(30); // 5 pixels d'espace entre les colonnes (H comme Horizontal)
			gl.setVgap(30);
			panelSegmentation.setLayout(gl);
			Color cc = new Color(240, 227, 107);
			trainButton.setBackground(cc);
			panelSegmentation.add(trainButton);
			overlayButton.setBackground(cc);
			panelSegmentation.add(overlayButton);
			resultButton.setBackground(cc);
			panelSegmentation.add(resultButton);
			probabilityButton.setBackground(cc);
			panelSegmentation.add(probabilityButton);
			ApplyClassifieurinRoiOfimage.setBackground(cc);
			panelSegmentation.add(ApplyClassifieurinRoiOfimage);
			Validation.setBackground(cc);
			panelSegmentation.add(Validation);
			ApplyClassifierInWholeImage.setBackground(cc);
			panelSegmentation.add(ApplyClassifierInWholeImage);
			//panelSegmentation.add(TEST);
			panelSegmentation.add(addtoClassBG());
			panelSegmentation.add(addtoClassNucleus());
			panelSegmentation.setBackground(colorsegmentation);
			panelTotalSegmentation.setLayout(new BorderLayout());
			panelTotalSegmentation.add(panelSegmentation,BorderLayout.WEST);
			return panelTotalSegmentation;
		}
		
		/****************************export table ****************/
		public void exportTable(JTable table, File file) throws IOException {
		TableModel model = table.getModel();
		FileWriter out = new FileWriter(file);

		for(int i=0; i < model.getColumnCount(); i++) {
			System.out.println(model.getColumnName(i));
			out.write(model.getColumnName(i) + ";");
		}
		out.write("\n");
		for(int i=0; i< model.getRowCount(); i++) {
			for(int j=0; j < model.getColumnCount(); j++) {
				System.out.println(model.getValueAt(i,j).toString());
				out.write(model.getValueAt(i,j).toString()+";");
			}
			out.write("\n");
		}
		out.close();
		System.out.println("write out to: " + file);
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
		public JButton addtoClassBG() {



			JButton buttonBg = new JButton("add to class background ");

			buttonBg.setBackground(Color.white);

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
					weka2.addExample(0, roi, 1);

				}
			});			
			return buttonBg;
		}
		public JButton addtoClassNucleus() {
			JButton buttonNucleus = new JButton("add to class Nucleus ");
			buttonNucleus.setBackground(Color.white);
			buttonNucleus.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					System.out.println("click sur button nucl");
					float[] tabx = new float[l.size()];
					int a = 0 ; //position du tableau des x
					float[] taby = new float[l.size()];
					int b = 0;  //position du tableau des y
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
			return buttonNucleus;
		} 

	}
	/******************** mail ******************/

	/*	public void sendmail() {
	Properties props = new Properties(); //info smtp dans mon exemple lemail expéditeur est en gmail donc j utilise des info de gmail
	props.put("mail.smtp.host", "smtp.gmail.com");
	props.put("mail.smtp.socketFactory.port", "587");
	props.put("mail.smtp.socketFactory.class",	"javax.net.ssl.SSLSocketFactory");
	props.put("mail.smtp.auth", "true");
	props.put("debug", "true");
	props.put("mail.smtp.port", "587");
	props.setProperty("proxySet","true");
	props.setProperty("socksProxyHost","proxyimap.univ-nantes.fr");
	props.setProperty("socksProxyPort","143");

	Session session = Session.getDefaultInstance(props,new javax.mail.Authenticator() {

		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication("mpc.test.44.irs.un","micropicell"); // dans cet exemle le compte mail c'est la 1ere partie sans l'@ j c po si ça marche ac
		}
	});

	try {	 
	Message message = new MimeMessage(session);
	message.setFrom(new InternetAddress("mpc.test.44.irs.un@gmail.com"));
	message.setRecipients(Message.RecipientType.TO,	InternetAddress.parse("naullet.guillaume@gmail.com"));  //destinataire
	message.setSubject("objet de mail");
	message.setText("corps du mail");
	Transport.send(message);	 
	} catch (MessagingException e) {
				throw new RuntimeException(e);
	}
}*/

	/*	public class email {

	    // Set up the SMTP server.
	    public void mail() throws AddressException, MessagingException {
	        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
	        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	        final String username = "mpc.test.44.irs.un@gmail.com";
	        final String password = "*******";
	        final String recipientEmail = "naullet.guillaume@gmail.com";
	        final String title ="Reciept for your remote print";
	        final String message = " Thank you theis test as worked";


	   

	        props.put("mail.smtps.quitwait", "false");


	        Session session = Session.getInstance(props, null);
	// -- Create a new message --
	        final MimeMessage msg = new MimeMessage(session);

	        // -- Set the FROM and TO fields --
	        msg.setFrom(new InternetAddress(username));
	        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail, false));



	        msg.setSubject(title);
	        msg.setText(message, "utf-8");


	        SMTPTransport t = (SMTPTransport) session.getTransport("smtps");

	        t.connect("smtp.gmail.com", username, password);
	        t.sendMessage(msg, msg.getAllRecipients());
	        t.close();
	        System.out.println(" normalement c'est terminer ");
	    }
	}*/
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
	public float getRatioExtrapolation() {
		return ratioExtrapolation;
	}
	public void setRatioExtrapolation(float ratioExtrapolation) {
		this.ratioExtrapolation = ratioExtrapolation;
	}
	public JProgressBar getProgress() {
		return progress;
	}
	public void setProgress(JProgressBar progress) {
		this.progress = progress;
	}
	public JPanel getAze() {
		return aze;
	}
	public void setAze(JPanel aze) {
		this.aze = aze;
	}
	public BufferedImage getThumbnails() {
		return thumbnails;
	}
	public void setThumbnails(BufferedImage thumbnails) {
		this.thumbnails = thumbnails;
	}
	public int getCountExtrapolation() {
		return CountExtrapolation;
	}
	public void setCountExtrapolation(int countExtrapolation) {
		CountExtrapolation = countExtrapolation;
	}
	public int getWidthThumbnail() {
		return widthThumbnail;
	}
	public void setWidthThumbnail(int widthThumbnail) {
		this.widthThumbnail = widthThumbnail;
	}
	public int getHeightThumbnail() {
		return heightThumbnail;
	}
	public void setHeightThumbnail(int heightThumbnail) {
		this.heightThumbnail = heightThumbnail;
	}
	public int[] getTabPosition() {
		return tabPosition;
	}
	public void setTabPosition(int[] tabPosition) {
		this.tabPosition = tabPosition;
	}
	public int getVariable() {
		return variable;
	}
	public void setVariable(int variable) {
		this.variable = variable;
	}
	public JPanel getBatch() {
		return Batch;
	}
	public void setBatch(JPanel batch) {
		Batch = batch;
	}
	public JLabel getLabel2() {
		return label2;
	}
	public void setLabel2(JLabel label2) {
		this.label2 = label2;
	}
	public long getLargeurImageTransitoire() {
		return largeurImageTransitoire;
	}
	public void setLargeurImageTransitoire(long largeurImageTransitoire) {
		this.largeurImageTransitoire = largeurImageTransitoire;
	}
	public long getHauteurImageTransitoire() {
		return hauteurImageTransitoire;
	}
	public void setHauteurImageTransitoire(long hauteurImageTransitoire) {
		this.hauteurImageTransitoire = hauteurImageTransitoire;
	}
	public BufferedImage[][] getTabbuffereds() {
		return tabbuffereds;
	}
	public void setTabbuffereds(BufferedImage[][] tabbuffereds) {
		this.tabbuffereds = tabbuffereds;
	}
	public ImagePlus[][] getTabIP() {
		return tabIP;
	}
	public void setTabIP(ImagePlus[][] tabIP) {
		this.tabIP = tabIP;
	}
	public WekaSegmentation[][] getTabweka() {
		return tabweka;
	}
	public void setTabweka(WekaSegmentation[][] tabweka) {
		this.tabweka = tabweka;
	}
	public boolean isApplywholeimage() {
		return applywholeimage;
	}
	public void setApplywholeimage(boolean applywholeimage) {
		this.applywholeimage = applywholeimage;
	}
	public boolean isTheboolean() {
		return theboolean;
	}
	public void setTheboolean(boolean theboolean) {
		this.theboolean = theboolean;
	}
	public ImagePlus getIP() {
		return IP;
	}
	public void setIP(ImagePlus iP) {
		IP = iP;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public JButton getOuvertureButton() {
		return OuvertureButton;
	}
	public void setOuvertureButton(JButton ouvertureButton) {
		OuvertureButton = ouvertureButton;
	}
	public boolean isCarre() {
		return carre;
	}
	public void setCarre(boolean carre) {
		this.carre = carre;
	}
	public boolean isTrait() {
		return trait;
	}
	public void setTrait(boolean trait) {
		this.trait = trait;
	}
	
	public BufferedImage getBuffImage() {
		return buffImage;
	}
	public void setBuffImage(BufferedImage buffImage) {
		this.buffImage = buffImage;
	}
	public JPanel getBandeau() {
		return Bandeau;
	}
	public void setBandeau(JPanel bandeau) {
		Bandeau = bandeau;
	}
	public JPanel getPanelPane() {
		return panelPane;
	}
	public void setPanelPane(JPanel panelPane) {
		this.panelPane = panelPane;
	}
	public LinkedList<Float> getL() {
		return l;
	}
	public void setL(LinkedList<Float> l) {
		this.l = l;
	}
	public boolean isDoneone() {
		return doneone;
	}
	public void setDoneone(boolean doneone) {
		this.doneone = doneone;
	}
	public String getWidhPixelOnMicron() {
		return widhPixelOnMicron;
	}
	public void setWidhPixelOnMicron(String widhPixelOnMicron) {
		this.widhPixelOnMicron = widhPixelOnMicron;
	}
	public String getHeightPixelOnMicron() {
		return heightPixelOnMicron;
	}
	public void setHeightPixelOnMicron(String heightPixelOnMicron) {
		this.heightPixelOnMicron = heightPixelOnMicron;
	}
	public JPanel getPanelTotalSegmentation() {
		return panelTotalSegmentation;
	}
	public void setPanelTotalSegmentation(JPanel panelTotalSegmentation) {
		this.panelTotalSegmentation = panelTotalSegmentation;
	}
	public JPanel getPanelclass() {
		return panelclass;
	}
	public void setPanelclass(JPanel panelclass) {
		this.panelclass = panelclass;
	}
	public JButton getTrainButton() {
		return trainButton;
	}
	public void setTrainButton(JButton trainButton) {
		this.trainButton = trainButton;
	}
	public JButton getOverlayButton() {
		return overlayButton;
	}
	public void setOverlayButton(JButton overlayButton) {
		this.overlayButton = overlayButton;
	}
	public JButton getResultButton() {
		return resultButton;
	}
	public void setResultButton(JButton resultButton) {
		this.resultButton = resultButton;
	}
	public JButton getProbabilityButton() {
		return probabilityButton;
	}
	public void setProbabilityButton(JButton probabilityButton) {
		this.probabilityButton = probabilityButton;
	}
	public JButton getValidation() {
		return Validation;
	}
	public void setValidation(JButton validation) {
		Validation = validation;
	}
	public JButton getApplyClassifieurinRoiOfimage() {
		return ApplyClassifieurinRoiOfimage;
	}
	public void setApplyClassifieurinRoiOfimage(JButton applyClassifieurinRoiOfimage) {
		ApplyClassifieurinRoiOfimage = applyClassifieurinRoiOfimage;
	}
	public JButton getApplyClassifierInWholeImage() {
		return ApplyClassifierInWholeImage;
	}
	public void setApplyClassifierInWholeImage(JButton applyClassifierInWholeImage) {
		ApplyClassifierInWholeImage = applyClassifierInWholeImage;
	}
	public int getCounterButtonOverlay() {
		return CounterButtonOverlay;
	}
	public void setCounterButtonOverlay(int counterButtonOverlay) {
		CounterButtonOverlay = counterButtonOverlay;
	}
	public WekaSegmentation getWeka2() {
		return weka2;
	}
	public void setWeka2(WekaSegmentation weka2) {
		this.weka2 = weka2;
	}
	public JPanel getTools() {
		return Tools;
	}
	public void setTools(JPanel tools) {
		Tools = tools;
	}
	public ImagePlus getRoiClassified() {
		return RoiClassified;
	}
	public void setRoiClassified(ImagePlus roiClassified) {
		RoiClassified = roiClassified;
	}
	public ImageProcessor getOverlay() {
		return overlay;
	}
	public void setOverlay(ImageProcessor overlay) {
		this.overlay = overlay;
	}
	public ColorProcessor getColorOverlay() {
		return colorOverlay;
	}
	public void setColorOverlay(ColorProcessor colorOverlay) {
		this.colorOverlay = colorOverlay;
	}
	public BufferedImage getOverlayImage() {
		return overlayImage;
	}
	public void setOverlayImage(BufferedImage overlayImage) {
		this.overlayImage = overlayImage;
	}
	public JPanel getPanneau() {
		return panneau;
	}
	public void setPanneau(JPanel panneau) {
		this.panneau = panneau;
	}
	public JLabel getTexte() {
		return texte;
	}
	public void setTexte(JLabel texte) {
		this.texte = texte;
	}
	public JPanel getPanelSegmentation() {
		return panelSegmentation;
	}
	public void setPanelSegmentation(JPanel panelSegmentation) {
		this.panelSegmentation = panelSegmentation;
	}
	public JPanel getStockTransitoryImage() {
		return stockTransitoryImage;
	}
	public void setStockTransitoryImage(JPanel stockTransitoryImage) {
		this.stockTransitoryImage = stockTransitoryImage;
	}
	public boolean isZoomasked() {
		return zoomasked;
	}
	public void setZoomasked(boolean zoomasked) {
		this.zoomasked = zoomasked;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public void setFilechoose(File filechoose) {
		Filechoose = filechoose;
	}
	public void setCountZoom(double countZoom) {
		CountZoom = countZoom;
	}	
}









