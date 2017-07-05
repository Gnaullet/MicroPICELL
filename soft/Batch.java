package soft;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
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
import org.openslide.OpenSlide;

import ij.ImagePlus;
import ij.gui.Roi;
import ij.measure.ResultsTable;
import ij.plugin.filter.Analyzer;
import ij.plugin.filter.ParticleAnalyzer;
import ij.plugin.filter.Writer;
import ij.plugin.frame.RoiManager;
import ij.process.ColorProcessor;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;
import trainableSegmentation.WekaSegmentation;


public class Batch {
	public class MonTraitement implements Runnable {
		private int paramI = 0;
		private int paramJ = 0;

		public MonTraitement(int i, int j) {
			paramI =i;
			paramJ=j;
		}


		public void run() {

			long chrono = java.lang.System.currentTimeMillis() ;
			System.out.println("chrono "+chrono);
			WekaSegmentation weka1 = new WekaSegmentation();

			ImagePlus ze = new ImagePlus("truc", tabbuffereds[paramI][paramJ]);
			weka1.setTrainingImage(ze);
			boolean aa = weka1.loadClassifier("titi");
			System.out.println("load = "+aa);
			System.out.println("Mon traitement " + Thread.currentThread().getName());
			System.out.println(" "+paramI+ " "+paramJ);

			ImagePlus RoiClassified2 =weka1.applyClassifier(ze, 8, true);




			ImageProcessor overlaylocal = RoiClassified2.getImageStack().getProcessor(RoiClassified2.getCurrentSlice()).duplicate();
			ColorProcessor colorOverlay3 = overlaylocal.convertToColorProcessor();
			colorOverlay3.autoThreshold();
			colorOverlay3.setBinaryThreshold();

			tabbuffereds[paramI][paramJ] = colorOverlay3.getBufferedImage();



		}
	}
	public static Object[][] donnees;
	public int counterMaskView =0;
	public SOFTFenetre1 SF;
	public BufferedImage[][] tabbuffereds = null;
	private JPanel pan = new JPanel();
	public int tabValeur[] = new int[4];
	public File filechoose;
	public int idapply = 0;
	public int idImageSelect=0;
	@SuppressWarnings("rawtypes")
	public List[][] listTotal;
	public int variable= 200;
	public BufferedImage[][] tabImageClassified;
	public BufferedImage TheClassified;
	public BufferedImage TheNONClassified;
	public ImagePlus IP;
	public JPanel panelTable = new JPanel();
	public Object tabDonnees[][];
	public JTable TableAffiche;
	public static int azerty;
	public Analyzer analyse = new Analyzer();
	public JPanel thepanel = new JPanel();
	public Batch(SOFTFenetre1 sf){this.SF=sf;}

	public JPanel ReturnPanelBatch (){
		JPanel panel = new JPanel();

		JLabel label = new JLabel("<html>This option only allows to count <br> the nucleus on the whole image : <html>");
		JButton OuvertureButton = new JButton("Open yours images on NDPI format : ");
		OuvertureButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e){
				JFileChooser chooser = new JFileChooser();
				chooser.setAcceptAllFileFilterUsed(false);
				FileNameExtensionFilter filter1 = new FileNameExtensionFilter( "Fichiers ndpi.", "ndpi");
				chooser.addChoosableFileFilter(filter1);
				ImagePreview preview = new ImagePreview(chooser);
				chooser.setAccessory(preview);
				chooser.setApproveButtonText("Choix du fichier (ndpi)..."); //intitulé du bouton
				chooser.setMultiSelectionEnabled(true);
				int returnVal = chooser.showOpenDialog(SF.getParent());
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					File[] tabFile = chooser.getSelectedFiles();


					for(int i=0;i<tabFile.length;i++){
						System.out.println(tabFile[i]);

						/********************************************/

						OpenSlide open = null;

						try {
							open = new OpenSlide(tabFile[i]);
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
							System.out.println(maxsize);

							if (maxsize<1000){
								WekaSegmentation weka2 = new WekaSegmentation();
								boolean zz = weka2.loadClassifier("titi");
								System.out.println(zz);
								BufferedImage Image = open.createThumbnailImage(0, 0, open.getLevel0Width(), open.getLevel0Height(), maxsize);
								ImagePlus IP = new ImagePlus("NewImagePlus", Image);
								weka2.setTrainingImage(IP);
								weka2.applyClassifier(true);
								//weka2.getClassifiedImage().show();
							}else{
								tab = true;
								int variable= 2000;
								tabbuffereds = new BufferedImage[((int) open.getLevel0Height()/variable)+1][((int) open.getLevel0Width()/variable)+1];
								//tabbuffereds = new BufferedImage[4][4];

								System.out.println(tabbuffereds.length + " autre "+tabbuffereds[0].length);
								for(int i1=0; i1<=open.getLevel0Height();i1=i1+variable){
									long hauteurThumbnailToCreate;
									long largeurThumbnailToCreate;
									if ((open.getLevel0Height()-(i1))<variable){
										hauteurThumbnailToCreate = open.getLevel0Height()-(i1);
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
											System.out.println("hauteur : "+ hauteurThumbnailToCreate);
											System.out.println("largeur : "+ largeurThumbnailToCreate);
											tabbuffereds[i1/variable][j/variable] = open.createThumbnailImage(j, i1, largeurThumbnailToCreate, hauteurThumbnailToCreate, variable);
										} catch (IOException e2) {
											System.out.println("error in try");
											e2.printStackTrace();
										}
									}
								}
							}
							if(tab==true){
								ExecutorService executorService = Executors.newFixedThreadPool(8);

								for(int k = 0; k< tabbuffereds.length; k++){
									for (int l =0; l<tabbuffereds[0].length; l++){

										Runnable runnable = new MonTraitement(k,l); 
										executorService.submit(runnable);							
									}
								}
								executorService.shutdown();
								executorService.awaitTermination(99999, TimeUnit.SECONDS);
							}
							BufferedImage buffinal = null;
							BufferedImage buf = null;
							for(int i1 = 0; i1<tabbuffereds.length; i1++){
								buf = null;
								for(int j=0; j<tabbuffereds[0].length; j++){
									BufferedImage ii = buf;
									if(j==0){
										System.out.println("truc");
										buf = tabbuffereds[i1][0];
										int aa = buf.getType();
										System.out.println(aa);
										System.out.println(buf);

										System.out.println(" "+i1+" "+j);
									}else{
										System.out.println("machin");

										System.out.println(" "+i1+" "+j);
										int w1 = ii.getWidth();
										int h1 = ii.getHeight();
										int w2 = tabbuffereds[i1][j].getWidth();
										int h2 = tabbuffereds[i1][j].getHeight();
										int wMax = w1+w2;
										System.out.println("h1"+h1);
										System.out.println("h2"+h2);
										System.out.println("w1"+w1);
										System.out.println("w2"+w2);
										buf = new BufferedImage(wMax, h2, 1);
										Graphics2D g2 = buf.createGraphics();
										g2.drawImage(ii, 0, 0, null);
										g2.drawImage(tabbuffereds[i1][j], w1, 0, null);
									}
								}
								if(i1!=0){
									BufferedImage iii = buffinal;
									int w1 = iii.getWidth();
									int h1 = iii.getHeight();
									int w2 = buf.getWidth();
									int h2 = buf.getHeight();
									System.out.println("h1"+h1);
									System.out.println("h2"+h2);
									System.out.println("w1"+w1);
									System.out.println("w2"+w2);
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
							//BufferedImage overlayImage = buffinal;
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						/*****************************************/
					}
				}
			}
		});
		JLabel label1 = new JLabel("<html> This option only allows to count <br> the nucleus on ROIs of image : <html>");
		JButton OuvertureButton1 = new JButton("Open yours images on NDPI format : ");
		OuvertureButton1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				System.out.println("faire des ROI");
				JFileChooser chooser = new JFileChooser();
				chooser.setAcceptAllFileFilterUsed(false);
				FileNameExtensionFilter filter1 = new FileNameExtensionFilter( "Fichiers ndpi.", "ndpi");
				chooser.addChoosableFileFilter(filter1);
				ImagePreview preview = new ImagePreview(chooser);
				chooser.setAccessory(preview);
				chooser.setApproveButtonText("Choix du fichier (ndpi)..."); //intitulé du bouton
				chooser.setMultiSelectionEnabled(true);
				int returnVal = chooser.showOpenDialog(SF.getParent());
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					File[] tabFile = chooser.getSelectedFiles();

					listTotal = new List[tabFile.length][4];
					JPanel Panel = new JPanel();
					BufferedImage[] tabbuffered = new BufferedImage[tabFile.length];
					for(int i=0;i<tabFile.length;i++){
						LinkedList<int[]> listCarreASegmenter = new LinkedList<int[]>();
						LinkedList<BufferedImage[]> listImageSegmenter = new LinkedList<BufferedImage[]>();
						LinkedList<BufferedImage[]> listImageNONSegmenter = new LinkedList<BufferedImage[]>();
						LinkedList<Roi> roilistcorrected = new LinkedList<Roi>();
						listTotal[i][0] = listCarreASegmenter;
						listTotal[i][1]= listImageSegmenter;
						listTotal[i][2]= roilistcorrected;
						listTotal[i][3]= listImageNONSegmenter;
						System.out.println(i);
						System.out.println(tabFile[i]);
						OpenSlide open = null;
						try {
							open = new OpenSlide(tabFile[i]);
							tabbuffered[i]= open.createThumbnailImage(800);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					pan = SF.caseImage(tabbuffered[0]);
					Panel.add(pan);
					Panel.revalidate();
					Panel.repaint();
					JLabel label1 = new JLabel("create Your ROI for all your image : ");

					JSlider slide11 = new JSlider();
					slide11.setBackground(new Color(4, 139, 154));
					OuvertureButton1.setEnabled(false);		
					if(tabFile.length>1){

						slide11.setMaximum(tabFile.length);
						slide11.setMinimum(1);
						slide11.setValue(1);
						slide11.setPaintTicks(true);
						slide11.setPaintLabels(true);
						slide11.setMinorTickSpacing(1);
						slide11.setMajorTickSpacing(1);
						slide11.addChangeListener(new ChangeListener() {

							public void stateChanged(ChangeEvent e) {
								idImageSelect = slide11.getValue()-1;
								filechoose = tabFile[slide11.getValue()-1];
								System.out.println("Slider2: " + slide11.getValue());
								BufferedImage image;
								image = tabbuffered[slide11.getValue()-1];
								Panel.remove(pan);
								Panel.validate();
								pan = SF.caseImage(image);
								Panel.add(pan);
								Panel.revalidate();
								Panel.repaint();

								/************************* Begin   Here we segmented the roi **************************************/

								PolygonMaker pl = new PolygonMaker();
								pl.init();



								/************************* End   Here we segmented the roi **************************************/
							}
						});
						JButton Validation = new JButton("Valide your ROI");
						Validation.addActionListener(new ActionListener(){
							@SuppressWarnings("unchecked")
							public void actionPerformed(ActionEvent e){
								System.out.println("on valide nos roi à segmenter");
								for(int i=0; i<listTotal.length; i++){
									System.out.println("carre a segmenter de l image " + i + "est de "+listTotal[i][0]);
									System.out.println("roi  " + i + "est de "+listTotal[i][2]);
									OpenSlide open;
									try {
										open = new OpenSlide(tabFile[i]);
										
										List<?> listCarrePourUneImage = listTotal[i][0];
										tabImageClassified = new BufferedImage[listTotal[i][0].size()][listTotal[i][0].size()];
										for (int j=0;j< listCarrePourUneImage.size(); j++){
											
											
											int[] zz = (int[]) listCarrePourUneImage.get(j);
											int x = zz[0];
											int y = zz[1];
											long larg = zz[2];
											long haut = zz[3];
											int maxSize=0;
											if(larg<haut){
											maxSize=(int) haut;
											}else{
											maxSize=(int) larg;
												}
											
											BufferedImage mythumbnail = open.createThumbnailImage(x, y, larg, haut, maxSize);
											listTotal[i][3].add(mythumbnail);
											System.out.println("x "+x+" y "+y+" larg "+larg+" haut "+haut);
											tabbuffereds = new BufferedImage[((int) haut/variable)+1][((int) larg/variable)+1];
											
											int counti=0;
											int county=0;

											for(int i1=0; i1<=haut;i1=i1+variable){


												long hauteurThumbnailToCreate;
												if ((haut-(i1))<variable){
													hauteurThumbnailToCreate = haut-(i1);
												}else{
													hauteurThumbnailToCreate=variable;
												}


												for(int j1=0; j1<=larg;j1=j1+variable){
													long largeurThumbnailToCreate;
													if ((larg-(j1))<variable){
														largeurThumbnailToCreate = larg-(j1);
													}else{
														largeurThumbnailToCreate=variable;
													}
													try {
														tabbuffereds[counti][county]= open.createThumbnailImage((x+j1),(y+i1), largeurThumbnailToCreate, hauteurThumbnailToCreate, variable);



													} catch (IOException e2) {
														System.out.println("error in try");
														e2.printStackTrace();
													}
													county=county+1;
												}
												county=0;
												counti=counti+1;
											}


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


											BufferedImage buffinal = null;
											BufferedImage buf = null;
											for(int i1 = 0; i1<tabbuffereds.length; i1++){
												buf = null;
												for(int j1=0; j1<tabbuffereds[0].length; j1++){
													BufferedImage ii = buf;
													if(j1==0){
														buf = tabbuffereds[i1][0];

													}else{

														int w1 = ii.getWidth();
														int w2 = tabbuffereds[i1][j1].getWidth();
														int h2 = tabbuffereds[i1][j1].getHeight();
														int wMax = w1+w2;
														buf = new BufferedImage(wMax, h2, 1);
														Graphics2D g2 = buf.createGraphics();
														g2.drawImage(ii, 0, 0, null);
														g2.drawImage(tabbuffereds[i1][j1], w1, 0, null);
													}
												}
												if(i1!=0){
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
											BufferedImage overlayImage = buffinal;
											ImagePlus ee = new ImagePlus("truc", overlayImage);
											List<?> qsd = listTotal[i][2];
											Roi vvvv = (Roi) qsd.get(j);
											vvvv.getMask().invert();
											vvvv.getMask().setColor(Color.white);
											ee.getProcessor().fill(vvvv.getMask());
											listTotal[i][1].add(ee.getBufferedImage());
											


										}

									} catch (IOException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									} catch (InterruptedException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								}

								BufferedImage zer = (BufferedImage) listTotal[0][1].get(0);
								Panel.remove(pan);
								zer = redimensionner(zer);
								Panel.revalidate();
								pan = SF.caseImage(zer);
								Panel.add(pan);
								Panel.revalidate();
								Panel.repaint();
								panel.remove(label1);
								panel.remove(slide11);
								panel.validate();
								panel.repaint();
								
								JSlider slider = new JSlider();
								JSlider slider1 = new JSlider();
								slider.setBackground(new Color(4, 139, 154));
								slider.setMaximum(listTotal.length);
								slider.setMinimum(1);
								slider.setValue(1);
								slider.setPaintTicks(true);
								slider.setPaintLabels(true);
								slider.setMinorTickSpacing(1);
								slider.setMajorTickSpacing(1);
								slider.addChangeListener(new ChangeListener() {
									public void stateChanged(ChangeEvent e) {
										idapply = slider.getValue()-1;
										BufferedImage ert = (BufferedImage) listTotal[slider.getValue()-1][1].get(0);
										TheClassified = ert;
										TheNONClassified = (BufferedImage) listTotal[slider.getValue()-1][3].get(0);
										ert = redimensionner(ert);
										Panel.removeAll();
										Panel.revalidate();
										pan = SF.caseImage(ert);
										Panel.add(pan);
										Panel.revalidate();
										Panel.repaint();
										slider1.setMaximum(listTotal[idapply][1].size());
										slider1.setValue(1);
										System.out.println("blblbeppetgrpgo "+listTotal[idapply][1].size());
									}
									});
									
									slider1.setBackground(new Color(4, 139, 154));
									slider1.setMinimum(1);
									slider1.setMaximum(listTotal[0][1].size());
									slider1.setValue(1);
									slider1.setPaintTicks(true);
									slider1.setPaintLabels(true);
									slider1.setMinorTickSpacing(1);
									slider1.setMajorTickSpacing(1);
									slider1.addChangeListener(new ChangeListener() {
										public void stateChanged(ChangeEvent e) {
											BufferedImage ert = (BufferedImage) listTotal[idapply][1].get(slider1.getValue()-1);
											TheClassified = ert;
											TheNONClassified = (BufferedImage) listTotal[idapply][3].get(slider1.getValue()-1);
											ert = redimensionner(ert);
											pan= SF.caseImage(ert);
											Panel.removeAll();
											Panel.revalidate();
											Panel.add(pan);
											Panel.revalidate();
											Panel.repaint();
											System.out.println(slider.getValue());
											System.out.println(idapply);
											System.out.println(slider1.getValue());
										}
								});
									JButton viewMask = new JButton("View image Base");
									viewMask.setToolTipText("Generate current probability maps");
								
									viewMask.addActionListener(new ActionListener(){
										public void actionPerformed(ActionEvent e){
											if(counterMaskView % 2 ==0){
												BufferedImage monImage = redimensionner(TheClassified);
												Panel.remove(pan);
												Panel.validate();
												pan = SF.caseImage(monImage);
												Panel.add(pan);
												Panel.revalidate();
												Panel.repaint();
											}else{
												BufferedImage monImage = redimensionner(TheNONClassified);
												Panel.remove(pan);
												Panel.validate();
												pan = SF.caseImage(monImage);
												Panel.add(pan);
												Panel.validate();
												Panel.repaint();
											}
											counterMaskView = counterMaskView+1;
										}
									});	
									JButton Setup = new JButton("Select setup and run count");
									Setup.setToolTipText("Charge setup for exclude the bad segmentation");
								
									Setup.addActionListener(new ActionListener(){

										public void actionPerformed(ActionEvent e){

											IP = new ImagePlus("new Image", TheClassified);
											ImageConverter ic = new ImageConverter(IP);
											ic.convertToGray8();
											ImageProcessor IProcessor = IP.getImageStack().getProcessor(IP.getCurrentSlice()).duplicate();
											IProcessor.setBinaryThreshold();
											ImagePlus imageBinaire = new ImagePlus("ImagePlus", IProcessor);
											ParticleAnalyzer instanceOfAnalyseParticle = new ParticleAnalyzer(); 
											instanceOfAnalyseParticle.showDialog();			
											thepanel.remove(panelTable);
											thepanel.validate();
											RoiManager roiManager = new RoiManager();
											instanceOfAnalyseParticle.analyze(imageBinaire, IProcessor);
											instanceOfAnalyseParticle.run(IProcessor);
											int vv = instanceOfAnalyseParticle.SHOW_MASKS;
											System.out.println("show mask = "+vv);
											roiManager.close();
										
											
											returnTable(roiManager);

										}
									});	
									JButton PlotResult = new JButton("Plot Result");
									PlotResult.setToolTipText("Generate profil of your result");		
									PlotResult.addActionListener(new ActionListener(){
										public void actionPerformed(ActionEvent e){
											System.out.println(" on va faire un plot des résultats");
											Graphique.runPlot();
										}
									});
									JButton DownloadMask = new JButton("Download your mask count");
									DownloadMask.setToolTipText("Download your mask count in tiff format");
									DownloadMask.addActionListener(new ActionListener(){
										public void actionPerformed(ActionEvent e){
											ImagePlus zz = new ImagePlus("overlay", TheClassified);
											ImageProcessor qsd = zz.getProcessor();
											Writer wr = new Writer();
											wr.setup("tiff", zz);
											wr.run(qsd);
										}
									});
								panel.add(slider);
								panel.add(slider1);
								panel.add(viewMask);
								panel.add(Setup);
								panel.add(PlotResult);
								panel.add(DownloadMask);
							}
						});
						panel.add(label1);
						panel.add(slide11);
						panel.add(Validation);
						thepanel.add(Panel,BorderLayout.EAST);




					}
				}
			}

		});

		GridLayout gl = new GridLayout(20,0);
		gl.setHgap(10); // 5 pixels d'espace entre les colonnes (H comme Horizontal)
		gl.setVgap(10);
		panel.setLayout(gl);
		Color color = new Color(4, 139, 154);
		panel.setBackground(color);
		panel.add(label);
		panel.add(OuvertureButton);
		panel.add(label1);
		panel.add(OuvertureButton1);

		thepanel.add(panel, BorderLayout.WEST);
		return thepanel;

	}
	/************************ Redimmensionner *******************************/
	public BufferedImage redimensionner(BufferedImage img) {
		float ratio2 = img.getWidth()/img.getHeight();
		System.out.println("ratio2"+ratio2);
		if(ratio2>1){

			float ratio = (float) 500/img.getWidth();
			float hauteur = (float) ratio*img.getHeight();
			System.out.println(hauteur);
			BufferedImage resizedImage = new BufferedImage(500,(int) hauteur,BufferedImage.TYPE_3BYTE_BGR);
			Graphics2D g = resizedImage.createGraphics();
			g.setComposite(AlphaComposite.Src);     
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.drawImage(img, 0, 0, 500 ,(int) hauteur, null);
			g.dispose();
			img = resizedImage;

		}else{

			float ratio = (float) 500/img.getHeight();
			float largeur = (float) ratio*img.getWidth();
			BufferedImage resizedImage = new BufferedImage((int) largeur ,500,BufferedImage.TYPE_3BYTE_BGR);
			Graphics2D g = resizedImage.createGraphics();
			g.setComposite(AlphaComposite.Src);     
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.drawImage(img, 0, 0, (int) largeur ,500, null);
			g.dispose();
			img = resizedImage;
		}
		return img;

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


			pan.addMouseListener(this);

		}

		@SuppressWarnings("unchecked")
		public void paint(Graphics g)
		{
			g.setColor(Color.blue);

			if((Math.abs(xCoordinates[numberOfClicks-1]-xCoordinates[0]))<10
					&& (Math.abs(yCoordinates[numberOfClicks-1]-yCoordinates[0]))<10 ) {
				xCoordinates[numberOfClicks] = xCoordinates[0];
				yCoordinates[numberOfClicks] = yCoordinates[0];
				g.drawPolygon(xCoordinates,yCoordinates,numberOfClicks);

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
				g.drawRect(xmin, ymin, xmax-xmin , ymax-ymin);
				OpenSlide open = null;
				long larg = 0;
				long haut = 0;
				try {
					open = new OpenSlide(filechoose);
					int largeur = pan.getWidth();
					int hauteur = pan.getHeight();
					larg =   ((xmax-xmin)*(open.getLevel0Height())/largeur);
					haut = ((ymax-ymin)*(open.getLevel0Height())/hauteur);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				int zzzz = numberOfClicks;
				CarreSegmenter();
				for(int j= 0; j<(zzzz+1); j++){
					xCoordinates[j] = (int) ((xCoordinates[j]-xmin)*larg/(xmax-xmin));
					yCoordinates[j] = (int) ((yCoordinates[j]-ymin)*haut/(ymax-ymin));
				}
				ij.gui.PolygonRoi roi = new ij.gui.PolygonRoi(xCoordinates, yCoordinates, zzzz, Roi.POLYGON);			
				listTotal[idImageSelect][2].add(roi);
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
			Graphics g = pan.getGraphics();
			g.setColor(Color.ORANGE);
			g.drawRect(xmin, ymin, xmax-xmin , ymax-ymin);
			addPositionOfRectangle(xmin, ymin, xmax-xmin, ymax-ymin);	
			numberOfClicks=0;
		}

		public void mousePressed(MouseEvent e)
		{
			xCoordinates[numberOfClicks] = e.getX();
			yCoordinates[numberOfClicks] = e.getY();
			Graphics g=pan.getGraphics();
			g.setColor(Color.RED);
			if(numberOfClicks!=0){
				g.drawLine(xCoordinates[numberOfClicks-1], yCoordinates[numberOfClicks-1], xCoordinates[numberOfClicks], yCoordinates[numberOfClicks]);
			}
			numberOfClicks++;
			if(numberOfClicks>2){
				paint(g);
			}
		}

		public void mouseDragged(MouseEvent arg0) {}			
		public void mouseMoved(MouseEvent arg0) {}
		public void mouseClicked(MouseEvent arg0) {}
		public void mouseEntered(MouseEvent arg0) {}
		public void mouseExited(MouseEvent arg0) {}
		public void mouseReleased(MouseEvent arg0) {}
		@SuppressWarnings("unchecked")
		public void addPositionOfRectangle(int x1,int y1,int x,int y){
			OpenSlide open;
			try {
				open = new OpenSlide(filechoose);
				int largeur = pan.getWidth();
				int hauteur = pan.getHeight();
				int x2 = (int) ((x1*(int)(open.getLevel0Width()))/largeur);
				int y2 = (int) ((y1*(int)(open.getLevel0Height()))/hauteur);
				int larg =  (int) (x*(open.getLevel0Height())/largeur);
				int haut =  (int) (y*(open.getLevel0Height())/hauteur);
				System.out.println(larg+ "largeur dans validation");
				tabValeur[0] = x2;
				tabValeur[1] = y2;
				tabValeur[2] = larg;
				tabValeur[3] = haut;
				listTotal[idImageSelect][0].add(tabValeur);
				tabValeur=null;
				tabValeur = new int[4];
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		}
	}
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
					OpenSlide open = null;
					try {
						open = new OpenSlide(filechoose);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Map<String, String> properties = open.getProperties();
					properties.size();
					String widhPixelOnMicron = properties.get("openslide.mpp-x");
					if(j==1){
						
						donnees[i][j]=df.format(zzz.getLength()*Double.parseDouble(widhPixelOnMicron));
					}else{
						donnees[i][j]=df.format(Double.parseDouble(resultTable.getStringValue(0, i))*Double.parseDouble(widhPixelOnMicron)*Double.parseDouble(widhPixelOnMicron));
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
		JLabel label2 = new JLabel("<html> There are "+azerty+ " nucleus. </html>");
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
		
		thepanel.remove(panelTable);
		thepanel.validate();
		thepanel.add(panelTable,BorderLayout.EAST);
		thepanel.validate();
		thepanel.repaint();
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
			JDialog f = new JDialog();
			
			f.setBounds(20,20,1100,1000);
			String titre = "Nombre de noyau en fonction de leur taille ";
			String abscisse = "Taille des noyaux";
			String ordonnee = "Nombres de noyaux";
			Graphique g = new Graphique(titre, abscisse, ordonnee, donnee, Color.white, l2, l1, false);
			f.add(g);
			f.setVisible(true);
		}
	}
}

