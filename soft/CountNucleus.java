package soft;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;


import javax.swing.JButton;

import javax.swing.JPanel;
import javax.swing.JTable;

import ij.ImagePlus;
import ij.gui.Roi;
import ij.plugin.filter.*;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;
import ij.plugin.frame.RoiManager;
public class CountNucleus extends SOFTFenetre1 {
	public ParticleAnalyzer instanceOfAnalyseParticle = new ParticleAnalyzer(); 
	public Analyzer analyse = null;
	public JPanel panelCount = new JPanel();
	public JPanel panelTotalCount = new JPanel();
	public JPanel StockPanel = new JPanel();
	public JPanel panel = new JPanel();
	public int counterMaskView = 1;
	SOFTFenetre1 SF = new SOFTFenetre1();
	public JButton viewMask = null;
	public JButton Setup = null;
	public boolean Showdialog;
	public ImagePlus IP;
	public ImageProcessor IProcessor;
	public ImagePlus imageBinaire;
	public ImagePlus OutputImage;
	public BufferedImage bufferedOutPutimage;
	
	/**
	 * 
	 */
	
	private static final long serialVersionUID = -2389781670803118594L;
	/*************************** Constructor **************************/
	
	public CountNucleus(){}
	public enum Entete {
        TENNIS,
        FOOTBALL,
        NATATION,
        RIEN;
    }
	/************************* Return Table With Information of Nucleus **************/
	public JPanel returnTableOfCount (BufferedImage image, JPanel panel2){
		panel = panel2;
		
		Setup = new JButton("Select setup and run count");
		Setup.setToolTipText("Charge setup for exclude the bad segmentation");
		Setup.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		IP = new ImagePlus("new Image", image);
        		
        		ImageConverter ic = new ImageConverter(IP);
        		
        		//instanceOfAnalyseParticle.setHideOutputImage(false);
        		ic.convertToGray8();
        		System.out.println(IP.isThreshold());
        		System.out.println(IP.getBitDepth());
        		
        		IProcessor = IP.getImageStack().getProcessor(IP.getCurrentSlice()).duplicate();
        		System.out.println(IProcessor);
        		IProcessor.setBinaryThreshold();
        		System.out.println(IProcessor);

        		System.out.println(IProcessor);
        		
        		System.out.println(IProcessor.isBinary());
        		imageBinaire = new ImagePlus("ImagePlus", IProcessor);
        	   
        		Showdialog = instanceOfAnalyseParticle.showDialog();
        		if(Showdialog ==true ){
        			
        			 instanceOfAnalyseParticle.analyze(imageBinaire, IProcessor);
        			 
        			 instanceOfAnalyseParticle.run(IProcessor);
        			 OutputImage = instanceOfAnalyseParticle.getOutputImage();
        			// bufferedOutPutimage = OutputImage.getBufferedImage();
        			System.out.println(OutputImage);
        			RoiManager roiManager = new RoiManager();
        			Roi[] ee = roiManager.getRoisAsArray();
        			for (int i =0; i<ee.length; i++){
        				System.out.println("roi count "+  ee[i]);
        				System.out.println("1");
        			}
        			System.out.println("1");
        			panelCount.add(returnTable(ee), BorderLayout.SOUTH);
        		}
        		Showdialog=false;
        	}
        });	
		
		viewMask = new JButton("View mask count");
		viewMask.setToolTipText("Generate current probability maps");
		panelCount.setLayout(new BorderLayout());
		panelCount.add(panel);
		
		viewMask.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		
        			
        			System.out.println(bufferedOutPutimage);
        			
        			if (bufferedOutPutimage != null){
        				if(counterMaskView % 2 ==0){
        					
        					panelTotalCount.remove(panel);
        					panelTotalCount.revalidate();
        					panel = StockPanel;
        					panelTotalCount.add(panel);
        					panelTotalCount.repaint();
        				}else{
        					System.out.println("l'image n'est pas null");
        					StockPanel=panel;
        					panelTotalCount.remove(panel);
        					panelTotalCount.revalidate();
        					panel = SF.caseImage(bufferedOutPutimage);
        					panelTotalCount.add(panel);
        					panelTotalCount.repaint();
        				}
        			}
        		
        		counterMaskView = counterMaskView+1;
        		}
        });	
		panelCount.setLayout(new GridLayout(30, 0,0,0));
		panelCount.add(Setup);
		panelCount.add(viewMask);
		panelTotalCount.setLayout(new BorderLayout());
		panelTotalCount.add(panel, BorderLayout.EAST);
		panelTotalCount.add(panelCount, BorderLayout.WEST);
		
		return panelTotalCount;
		
	}
	/******************************** Construction of Table result Count *******************/
	public TextArea returnTable(Roi[] tableResult){
		TextArea t = new TextArea(4, tableResult.length);  
		
		for (int i =0; i<tableResult.length; i++){
			System.out.println("roi count "+  tableResult[i]);
			Roi a = tableResult[i];
			System.out.println("1");
			t.setText(a.toString());	
		}
//		Roi entetes = tableResult[0];
//		
//		Roi[] donnees;
//		for (int i=1;i<tableResult.length; i++){
//			donnees[i-1]=tableResult[i];
//		}
//		JTable tableau = new JTable(donnees, entetes);
//		return tableau;
		return t;
	}
	/******************************** Getter and Setter ****************/
	 public ParticleAnalyzer getInstanceOfAnalyseParticle() {
		return instanceOfAnalyseParticle;
	}

	public void setInstanceOfAnalyseParticle(ParticleAnalyzer instanceOfAnalyseParticle) {
		this.instanceOfAnalyseParticle = instanceOfAnalyseParticle;
	}

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
	
}
