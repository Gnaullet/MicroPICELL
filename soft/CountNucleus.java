package soft;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.ScrollPaneLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import ij.ImagePlus;
import ij.gui.Roi;
import ij.measure.ResultsTable;
import ij.plugin.filter.Analyzer;
import ij.plugin.filter.ParticleAnalyzer;
import ij.plugin.filter.Writer;
import ij.plugin.frame.RoiManager;
import ij.process.*;


public class CountNucleus {
	public JPanel panelTable = new JPanel();
	public static int azerty;
	
	public JButton PlotResult = null;
	public JButton DownloadMask = null;
	public Analyzer analyse = new Analyzer();
	public JPanel panelCount = new JPanel();
	public JPanel panelTotalCount = new JPanel();
	public JPanel StockPanel = new JPanel();
	public JPanel panel = new JPanel();
	public int counterMaskView = 1;
	public int totalCount;
	int[] x;
	int[] y;
	public BufferedImage image;
	public JButton viewMask = null;
	public JButton Setup = null;
	public boolean Showdialog;
	public ImagePlus IP;
	public ImageProcessor IProcessor;
	public ImagePlus imageBinaire;
	public ImagePlus OutputImage;
	public BufferedImage bufferedOutPutimage;
	public int numberOfNucleusInImage;
	public JLabel label2;
	public ImagePlus[][] tabIP;
	public Object tabDonnees[][];
	public JTable TableAffiche;
	public RoiManager roiM;
	private SOFTFenetre1 SF;
	public static Object[][] donnees;
	public BufferedImage ROIClassified;
	JPanel pan = new JPanel();

	/**
	 * 
	 */

	private static final long serialVersionUID = -2389781670803118594L;
	
	/*************************** Constructor **************************/

	public CountNucleus(SOFTFenetre1 sf){this.SF=sf;}
	/************************* Return Table With Information of Nucleus **************/
	/**
	 * @param image
	 * @param panel2
	 * @return
	 */
	public JPanel returnTableOfCount (BufferedImage[] image2, JPanel panel2, LinkedList<BufferedImage> list){
		
		//roiM.close();
		
		int ee = panel.getComponentCount();
		image = image2[0];
		BufferedImage monImage = redimensionner(image);
		ROIClassified=list.get(0);
		System.out.println(ee);
		
		GridLayout gl = new GridLayout(2, 0);
		gl.setHgap(30); // 5 pixels d'espace entre les colonnes (H comme Horizontal)
		gl.setVgap(30);
		panel.setLayout(gl);
		pan = SF.caseImage(monImage);
		panel.add(pan);
		panel.validate();
		panel.setBackground(new Color(4, 139, 154));
		panel.repaint();
		JSlider slide = new JSlider();
		slide.setBackground(new Color(4, 139, 154));
		if(image2.length>1){
			
		    slide.setMaximum(image2.length);
		    slide.setMinimum(1);
		    slide.setValue(1);
		    slide.setPaintTicks(true);
		    slide.setPaintLabels(true);
		    slide.setMinorTickSpacing(1);
		    slide.setMajorTickSpacing(1);
		    slide.addChangeListener(new ChangeListener() {
		        public void stateChanged(ChangeEvent e) {
		          System.out.println("Slider2: " + slide.getValue());
		          image = image2[slide.getValue()-1];
		  		  BufferedImage monImage = redimensionner(image);
		  		  Showdialog=false;
		  		ROIClassified=list.get(slide.getValue()-1);
		  		IP = new ImagePlus("new Image", image);
				ImageConverter ic = new ImageConverter(IP);
				ic.convertToGray8();
	
				IProcessor = IP.getImageStack().getProcessor(IP.getCurrentSlice()).duplicate();
	
				IProcessor.setBinaryThreshold();
	
				imageBinaire = new ImagePlus("ImagePlus", IProcessor);
		  		
		  		panel.remove(pan);
		  		panel.validate();
		  		pan = SF.caseImage(monImage);
				panel.add(pan);
		  		panel.revalidate();
		  		panel.repaint();
		        }
		      });
		}
	    panelTotalCount.add(panel,BorderLayout.CENTER);
	    //panelTotalCount.add(slide,BorderLayout.AFTER_LAST_LINE);
		Setup = new JButton("Select setup and run count");
		Setup.setToolTipText("Charge setup for exclude the bad segmentation");
		Setup.addActionListener(new ActionListener(){
		
			public void actionPerformed(ActionEvent e){



				IP = new ImagePlus("new Image", image);
				ImageConverter ic = new ImageConverter(IP);
				ic.convertToGray8();

				IProcessor = IP.getImageStack().getProcessor(IP.getCurrentSlice()).duplicate();

				IProcessor.setBinaryThreshold();

				imageBinaire = new ImagePlus("ImagePlus", IProcessor);
				
				ParticleAnalyzer instanceOfAnalyseParticle = new ParticleAnalyzer(); 
				Showdialog = instanceOfAnalyseParticle.showDialog();
				
				
				if(Showdialog ==true ){
					panelTotalCount.remove(panelTable);
					panelTotalCount.validate();
					
					RoiManager roiManager = new RoiManager();
					instanceOfAnalyseParticle.analyze(imageBinaire, IProcessor);
					instanceOfAnalyseParticle.run(IProcessor);
				
					roiManager.close();
					roiManager.close();
					PlotResult.setEnabled(true);
					viewMask.setEnabled(true);
					returnTable(roiManager);
					
					
				}
			}
		});	
		/**
		 * 
		 */
		viewMask = new JButton("View image Base");
		viewMask.setToolTipText("Generate current probability maps");
		viewMask.setEnabled(false);
		viewMask.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				
					if(counterMaskView % 2 ==0){
						BufferedImage monImage = redimensionner(image);
						panel.remove(pan);
				  		panel.validate();
				  		pan = SF.caseImage(monImage);
						panel.add(pan);
				  		panel.revalidate();
				  		panel.repaint();
					}else{
						BufferedImage monImage = redimensionner(ROIClassified);
						panel.remove(pan);
				  		panel.validate();
						pan = SF.caseImage(monImage);
						panel.add(pan);
						panel.validate();
						panel.repaint();
					}
				counterMaskView = counterMaskView+1;
			}
		});	
		PlotResult = new JButton("Plot Result");
		PlotResult.setEnabled(false);
		PlotResult.setToolTipText("Generate profil of your result");		
		PlotResult.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				System.out.println(" on va faire un plot des résultats");
				Graphique.runPlot();
			}
		});
		DownloadMask= new JButton("Download your mask count");
		DownloadMask.setToolTipText("Download your mask count in tiff format");
		DownloadMask.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ImagePlus zz = new ImagePlus("overlay", image);
				ImageProcessor qsd = zz.getProcessor();
				Writer wr = new Writer();
				wr.setup("tiff", zz);
				wr.run(qsd);
			}
		});
	
		GridLayout gl1 = new GridLayout(5,5);
		gl1.setHgap(10); // 5 pixels d'espace entre les colonnes (H comme Horizontal)
		gl1.setVgap(10);
		Color colButt = new Color(240, 227, 107);
		panelCount.setLayout(gl1);
		Setup.setBackground(colButt);
		panelCount.add(Setup);
		viewMask.setBackground(colButt);
		panelCount.add(viewMask);
		PlotResult.setBackground(colButt);
		panelCount.add(PlotResult);
		DownloadMask.setBackground(colButt);
		panelCount.add(DownloadMask);
		panelCount.add(slide);
		panelCount.setBackground(new Color(4, 139, 154));
		GridLayout gl11 = new GridLayout(1,3);
		gl11.setHgap(0); // 5 pixels d'espace entre les colonnes (H comme Horizontal)
		gl11.setVgap(10);
		panelTotalCount.setLayout(gl11);
		panelTotalCount.setBackground(new Color(4, 139, 154));
		panelTotalCount.add(panelCount,BorderLayout.CENTER);
		panelTotalCount.add(panel,BorderLayout.CENTER);
		panelTotalCount.repaint();
		return panelTotalCount;

	}
	

	/**************************************************************************************/

	
	/******************************** Construction of Table result Count *******************/
	/**
	 * 
	 * @param roiManager
	 */
	public void returnTable(RoiManager roiManager){



		System.out.println("dans returnTable");

		azerty = roiManager.getCount()/2;
		System.out.println(azerty);
		donnees = new Object[azerty][3];


		for(int i=0; i<azerty; i++){

			Roi zzz = roiManager.getRoi(i);
			@SuppressWarnings("static-access")
			ResultsTable resultTable = analyse.getResultsTable();
			resultTable.getColumnHeadings();
			for(int j=0; j<3; j++){

				if(j==0){
					donnees[i][j]=i+1;
				}
				else{
					DecimalFormat df = new DecimalFormat("0.00");
					if(j==1){
						
						donnees[i][j]=df.format(zzz.getLength()*Double.parseDouble(SF.getWidhPixelOnMicron()));
					}else{
						donnees[i][j]=df.format(Double.parseDouble(resultTable.getStringValue(0, i))*Double.parseDouble(SF.getWidhPixelOnMicron())*Double.parseDouble(SF.getWidhPixelOnMicron()));
					}

				}
			}
		}
		TableAffiche = createTable(donnees);
		DefaultTableCellRenderer custom = new DefaultTableCellRenderer(); 
		custom.setHorizontalAlignment(JLabel.CENTER); 
		for (int i=0 ; i < TableAffiche.getColumnCount() ; i++){ 
			TableAffiche.getColumnModel().getColumn(i).setCellRenderer(custom); 
		}
		panelTable.removeAll();
		panelTable.validate();
		TableAffiche.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		TableAffiche.getColumn("id").setPreferredWidth(200);
		TableAffiche.getColumn("Perimeter (micron)").setPreferredWidth(200);
		TableAffiche.getColumn("Area (micron²)").setPreferredWidth(200);
		panelTable.setSize(new Dimension(600, 500));
		panelTable.setMinimumSize(new Dimension(600, 500));
		panelTable.setMaximumSize(new Dimension(600, 500));
		panelTable.setPreferredSize(new Dimension(600, 500));
		JScrollPane Table2 =new JScrollPane();
		Table2.setVisible(true);
		Table2.setLayout(new ScrollPaneLayout());
		Table2.setViewportView(TableAffiche);
		Table2.getHorizontalScrollBar();
		panelTable.add(Table2,BorderLayout.EAST);
		label2 = new JLabel("<html> There are "+azerty+ " nucleus. </html>");
		JButton download = new JButton("Download Table");
		download.setToolTipText("You can download this table in format .CSV");
		download.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try {
					JFileChooser newJFileChoose = new JFileChooser();
					int ee = JFileChooser.DIRECTORIES_ONLY;
					System.out.println(ee);
					FileNameExtensionFilter filter1 = new FileNameExtensionFilter( "Fichiers csv.", "csv");
					newJFileChoose.addChoosableFileFilter(filter1);
					newJFileChoose.setAcceptAllFileFilterUsed(false);
					newJFileChoose.setFileSelectionMode(0);
					newJFileChoose.setApproveButtonText("Choose the location of your table ");
					int returnVal = newJFileChoose.showOpenDialog(SF.getParent());
					if(returnVal == JFileChooser.APPROVE_OPTION) {			 
						String filechoose = newJFileChoose.getSelectedFile().getPath();
						
						exportTable(TableAffiche, new File(filechoose+".csv"));
					} }catch (IOException ex) {
						System.out.println(ex.getMessage());
						ex.printStackTrace();
					}
				System.out.println("download");
			}
		});
		panelTable.add(download,BorderLayout.SOUTH);
		panelTable.add(label2,BorderLayout.NORTH);
		panelTable.validate();
		panelTable.setBackground(new Color(4, 139, 154));
		panelTable.repaint();
		
		panelTotalCount.remove(panelTable);
		panelTotalCount.validate();
		panelTotalCount.add(panelTable,BorderLayout.EAST);
		panelTotalCount.validate();
		panelTotalCount.repaint();
		roiManager=null;
		System.out.println("sort returnTable");
	}
	/************************export table ********************/
	public void exportTable(JTable table, File file) throws IOException {
		TableModel model = table.getModel();
		FileWriter out = new FileWriter(file);

		for(int i=0; i < model.getColumnCount(); i++) {
			out.write(model.getColumnName(i) + ";");
		}
		out.write("\n");
		for(int i=0; i< model.getRowCount(); i++) {
			for(int j=0; j < model.getColumnCount(); j++) {
				out.write(model.getValueAt(i,j).toString()+";");
			}
			out.write("\n");
		}
		out.close();
		System.out.println("write out to: " + file);
	}
	/*
	 * 
	 */
	/******************************** CreateJtable *****************/
	public JTable createTable(Object[][] donnees){

		String[] entetes = {"id", "Perimeter (micron)", "Area (micron²)"};
		JTable tableau = new JTable(donnees, entetes);
		return tableau;
	}

	/******************************** Create thumbnail of buffered image **********************/
	public BufferedImage redimensionner(BufferedImage img) {
		float ratio2 = img.getWidth()/img.getHeight();
		System.out.println("ratio2"+ratio2);
		if(ratio2>1){
			float ratio = (float) 600/img.getWidth();
			float hauteur = (float) ratio*img.getHeight();
			System.out.println(hauteur);
			BufferedImage resizedImage = new BufferedImage(600,(int) hauteur,BufferedImage.TYPE_3BYTE_BGR);
			Graphics2D g = resizedImage.createGraphics();
			g.setComposite(AlphaComposite.Src);     
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			
			
			g.drawImage(img, 0, 0, 600 ,(int) hauteur, null);
			g.dispose();
			img = resizedImage;
		}else{
			float ratio = (float) 600/img.getHeight();
			float largeur = (float) ratio*img.getWidth();
			BufferedImage resizedImage = new BufferedImage((int) largeur ,600,BufferedImage.TYPE_3BYTE_BGR);
			Graphics2D g = resizedImage.createGraphics();
			g.setComposite(AlphaComposite.Src);     
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.drawImage(img, 0, 0, (int) largeur ,600, null);
			g.dispose();
			img = resizedImage;
		}
		return img;
		
	}
/******************************** Faire un plot ************************************/
	
	public static class Graphique extends JPanel {

		private static final long serialVersionUID = 1L;

		/** titre : Le titre du graphique affiché en haut */
		private String titre;
		/** ordonnee : le nom de l'axe des ordonnées */
		private String ordonnee;
		/** abscisses : le nom de l'axe des abscisses */
		private String abscisse;
		/** valeurs : les valeurs à afficher, elles sont triées par séries et par catégories*/
		private List<Float> valeurs;
		/** series : la liste des séries */
		private List<String> series;
		/** categories : la liste des categories */
		private List<String> categories;
		/** legende : booleen vrai si on affiche la légende */
		private boolean legende;
		/** couleurFond : la couleur du fond */
		private Color couleurFond;
		/** couleurBarres : les couleurs appliquées aux barres */
		private Color[] couleursBarres = {Color.white};

		/**
		 * Constructeur
		 * @param titre : le titre du graphique
		 * @param abscisse : le nom de l'axe des abscisses
		 * @param ordonnee : le nom de l'axe des ordonnées
		 * @param valeurs : les valeurs
		 * @param fond : la couleur de fond
		 * @param listeSeries : les séries
		 * @param listeCategory : les catégories
		 * @param legende : vrai si on affiche la légende
		 */
		public Graphique(String titre, String abscisse, String ordonnee, List<Float> valeurs, Color fond, List<String> listeSeries, List<String> listeCategory, boolean legende) {
			super(new GridLayout(1,0));
			this.titre=titre;
			this.ordonnee=ordonnee;
			this.abscisse=abscisse;
			this.valeurs=valeurs;
			this.series=listeSeries;
			this.categories=listeCategory;
			this.legende=legende;
			this.couleurFond=fond;
			initialiser();
		}

		/**
		 * Initialise le graphique
		 */
		private void initialiser(){
			DefaultCategoryDataset dataset = new DefaultCategoryDataset();
			int k = 0;
			for ( int j=0; j<categories.size(); j++){
				for (int i=0; i<series.size(); i++){
					dataset.addValue(valeurs.get(k), series.get(i), categories.get(j));
					k++;
				}

			}
			JFreeChart chart = ChartFactory.createBarChart(
					titre,   					// chart title
					abscisse,					// domain axis label
					ordonnee,   				// range axis label
					dataset,    				// data
					PlotOrientation.VERTICAL, 	// orientation
					legende,                    // include legend
					true,                     	// tooltips
					false                     	// URL
			);

			// definition de la couleur de fond
			chart.setBackgroundPaint(couleurFond);

			CategoryPlot plot = (CategoryPlot) chart.getPlot();

			//valeur comprise entre 0 et 1 transparence de la zone graphique
			plot.setBackgroundAlpha(0.9f);

			NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
			rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

			BarRenderer renderer = (BarRenderer) plot.getRenderer();
			renderer.setDrawBarOutline(false);

			// pour la couleur des barres pour chaque serie

			for (int s=0; s<series.size(); s++){
				GradientPaint gp0 = new GradientPaint(0.0f, 0.0f, couleursBarres[s],
						0.0f, 0.0f, Color.white);
				renderer.setSeriesPaint(s, gp0);

			}		

			ChartPanel chartPanel = new ChartPanel(chart);
			chartPanel.setFillZoomRectangle(true);
			chartPanel.setMouseWheelEnabled(true);
			chartPanel.setPreferredSize(new Dimension(500, 270));

			add(chartPanel);
		}

		/**
		 * Création d'un graphique
		 * @param a
		 */
		public static void runPlot(){
			List<Float> donnee = new ArrayList<Float>();
			List<String> l1 = new ArrayList<String>();
			List<String> l2 = new ArrayList<String>();
			
			l2.add("0");
			l1.add("0<->10");
			l1.add("10<->20");
			l1.add("20<->30");
			l1.add("30<->40");
			l1.add("40<->50");
			l1.add("50<->60");
			l1.add("60<->70");
			l1.add("70<->80");
			l1.add("80<->90");
			l1.add("90<->100");
			l1.add(">100");
			float Count0_10=0;
			float Count10_10=0;
			float Count20_10=0;
			float Count30_10=0;
			float Count40_10=0;
			float Count50_10=0;
			float Count60_10=0;
			float Count70_10=0;
			float Count80_10=0;
			float Count90_10=0;
			float Count100_10=0;
			float Count100010=0;
		
			for(int i=0; i<azerty; i++){
				System.out.println(donnees[i][2]);
				Object donne = donnees[i][2];
				System.out.println(donne);
				String zz = donne.toString();
				System.out.println(zz);
				zz =zz.toString();
				char[] charz = zz.toCharArray();
				for(int j=0; j<charz.length; j++){
					if(charz[j]==','){
						charz[j]='.';
						
					}
				}
				String vv = new String(charz);
			
				float thedata= Float.parseFloat(vv);
			
				if( thedata < 10 ){
					Count0_10=Count0_10+1;
				}else{
					if( thedata < 20 ){
					Count10_10=Count10_10+1;
					}else{
						if( thedata < 30 ){
							Count20_10=Count20_10+1;
						}else{
							if( thedata < 40 ){
								Count30_10=Count30_10+1;
								}else{
									if( thedata < 50 ){
										Count50_10=Count50_10+1;
										}else{
											if( thedata < 60 ){
												Count60_10=Count60_10+1;
											}else{
												if( thedata < 70 ){
													Count70_10=Count70_10+1;
												}else{
													if( thedata < 80 ){
														Count80_10=Count80_10+1;
													}else{
														if( thedata < 90 ){
															Count90_10=Count90_10+1;
														}else{
															if( thedata < 100 ){
																Count100_10=Count100_10+1;
															}else{
															
																Count100010=Count100010+1;
																
																
															}
														}
													}
												}
												}
											}
											}
										}
								}
						}
			}
			donnee.add(Count0_10);
			donnee.add(Count10_10);
			donnee.add(Count20_10);
			donnee.add(Count30_10);
			donnee.add(Count40_10);
			donnee.add(Count50_10);
			donnee.add(Count60_10);
			donnee.add(Count70_10);
			donnee.add(Count80_10);
			donnee.add(Count90_10);
			donnee.add(Count100_10);
			donnee.add(Count100010);
			JFrame f = new JFrame();
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			f.setBounds(20,20,1100,1000);
			String titre = "Nombre de noyau en fonction de leur taille ";
			String abscisse = "Taille des noyaux";
			String ordonnee = "Nombres de noyaux";
			Graphique g = new Graphique(titre, abscisse, ordonnee, donnee, Color.white, l2, l1, false);
			f.add(g);
			f.setVisible(true);
		}

	}
	/******************************** Getter and Setter ****************/
	

	public Analyzer getAnalyse() {
		return analyse;
	}

	public void setAnalyse(Analyzer analyse) {
		this.analyse = analyse;
	}

	public JPanel getPanelCount() {
		return panelCount;
	}

	public void setPanelCount(JPanel panelCount) {
		this.panelCount = panelCount;
	}

	public JPanel getPanelTotalCount() {
		return panelTotalCount;
	}

	public void setPanelTotalCount(JPanel panelTotalCount) {
		this.panelTotalCount = panelTotalCount;
	}

	public JPanel getStockPanel() {
		return StockPanel;
	}

	public void setStockPanel(JPanel stockPanel) {
		StockPanel = stockPanel;
	}

	public JPanel getPanel() {
		return panel;
	}

	public void setPanel(JPanel panel) {
		this.panel = panel;
	}

	public int getCounterMaskView() {
		return counterMaskView;
	}

	public void setCounterMaskView(int counterMaskView) {
		this.counterMaskView = counterMaskView;
	}

	public SOFTFenetre1 getSF() {
		return SF;
	}

	public void setSF(SOFTFenetre1 sF) {
		SF = sF;
	}

	public JButton getViewMask() {
		return viewMask;
	}

	public void setViewMask(JButton viewMask) {
		this.viewMask = viewMask;
	}

	public JButton getSetup() {
		return Setup;
	}

	public void setSetup(JButton setup) {
		Setup = setup;
	}

	public boolean isShowdialog() {
		return Showdialog;
	}

	public void setShowdialog(boolean showdialog) {
		Showdialog = showdialog;
	}

	public ImagePlus getIP() {
		return IP;
	}

	public void setIP(ImagePlus iP) {
		IP = iP;
	}

	public ImageProcessor getIProcessor() {
		return IProcessor;
	}
	public void setIProcessor(ImageProcessor iProcessor) {
		IProcessor = iProcessor;
	}

	public ImagePlus getImageBinaire() {
		return imageBinaire;
	}

	public void setImageBinaire(ImagePlus imageBinaire) {
		this.imageBinaire = imageBinaire;
	}

	public ImagePlus getOutputImage() {
		return OutputImage;
	}

	public void setOutputImage(ImagePlus outputImage) {
		OutputImage = outputImage;
	}

	public BufferedImage getBufferedOutPutimage() {
		return bufferedOutPutimage;
	}

	public void setBufferedOutPutimage(BufferedImage bufferedOutPutimage) {
		this.bufferedOutPutimage = bufferedOutPutimage;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Object[][] getDonnees() {
		return donnees;
	}
	public void setDonnees(Object[][] donnees) {
		this.donnees = donnees;
	}

}
