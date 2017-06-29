package soft;

import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.openslide.OpenSlide;

import ij.ImagePlus;
import ij.process.ColorProcessor;
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
			RoiClassified2.show();

			
			
			ImageProcessor overlaylocal = RoiClassified2.getImageStack().getProcessor(RoiClassified2.getCurrentSlice()).duplicate();
			ColorProcessor colorOverlay3 = overlaylocal.convertToColorProcessor();
			colorOverlay3.autoThreshold();
			colorOverlay3.setBinaryThreshold();
			
			tabbuffereds[paramI][paramJ] = colorOverlay3.getBufferedImage();
			ImagePlus aze= new ImagePlus("aze", tabbuffereds[paramI][paramJ]);
			aze.show();


	}
	}
	public SOFTFenetre1 SF;
	public BufferedImage[][] tabbuffereds = null;
	
	
	public Batch(SOFTFenetre1 sf){this.SF=sf;}
	
	public JPanel ReturnPanelBatch (){
		JPanel panel = new JPanel();
		JLabel label = new JLabel("This option only allows to count the nucleus on the whole image");
		JButton OuvertureButton = new JButton("Open yours images on NDPI format in .rar folder");
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
											tabbuffereds[i1/variable][j/variable]= open.createThumbnailImage(j, i1, largeurThumbnailToCreate, hauteurThumbnailToCreate, variable);
										} catch (IOException e2) {
											System.out.println("error in try");
											e2.printStackTrace();
										}
									}
								}
							}
							if(tab==true){

							//	NumberFormat format = NumberFormat.getInstance();
							//	Runtime runtime = Runtime.getRuntime();
								
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
		panel.add(label);
		panel.add(OuvertureButton);
		return panel;

	}
	}

