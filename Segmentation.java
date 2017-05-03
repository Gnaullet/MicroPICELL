package soft;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import ij.ImagePlus;
import ij.ImagePlus.*;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.openslide.AssociatedImage;

import trainableSegmentation.WekaSegmentation;
import trainableSegmentation.Weka_Segmentation;

	class Segmentation  {
		
		private int numOfClasses = 2;
		public JPanel panelTotalSegmentation = new JPanel();
		public JPanel panelclass = new JPanel();
		private JButton trainButton = null;
		private JButton overlayButton = null;
		private JButton resultButton = null;
		private JButton probabilityButton = null;
		private JButton applyButton = null;
		private JButton loadClassifierButton = null;
		private JButton saveClassifierButton = null;
		private JButton loadDataButton = null;
		private JButton buttonAddClass;
		private JButton saveDataButton = null;
		private JButton settingsButton = null;
		//private Weka_Segmentation weka = new Weka_Segmentation();
		//private WekaSegmentation weka2 = new WekaSegmentation();
//		private SOFTFenetre1 sf = new SOFTFenetre1();
//		private OuvertureNDPI ouv = new OuvertureNDPI(sf.getnomFichier(), sf.getPathFichier(), sf.getFilechoose(), sf.getChooser());
//		private ImagePlus IP = new ImagePlus(sf.getNomFichier(), ouv.getLama());
	private JButton addClassButton = null;
	public JPanel panelSegmentation = new JPanel();
	//Weka_Segmentation weka = new Weka_Segmentation();
	
	public JPanel createAPanelContaintSegmentationTools(){
		
		//System.out.println(IP);
	
	
		trainButton = new JButton("Train classifier");
		trainButton.setToolTipText("Start training the classifier");
		trainButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		System.out.println("aEntraine le classifier");
        		 // weka.run("Train classifier");
        		}
        });

		overlayButton = new JButton("Toggle overlay");
		overlayButton.setToolTipText("Toggle between current segmentation and original image");
		//overlayButton.setEnabled(false);

		resultButton = new JButton("Create result");
		resultButton.setToolTipText("Generate result image");
		//resultButton.setEnabled(false);

		probabilityButton = new JButton("Get probability");
		probabilityButton.setToolTipText("Generate current probability maps");
		//probabilityButton.setEnabled(false);

		
		
		applyButton = new JButton ("Apply classifier");
		applyButton.setToolTipText("Apply current classifier to a single image or stack");
		//applyButton.setEnabled(false);

		loadClassifierButton = new JButton ("Load classifier");
		loadClassifierButton.setToolTipText("Load Weka classifier from a file");

		saveClassifierButton = new JButton ("Save classifier");
		saveClassifierButton.setToolTipText("Save current classifier into a file");
		//saveClassifierButton.setEnabled(false);

		loadDataButton = new JButton ("Load data");
		loadDataButton.setToolTipText("Load previous segmentation from an ARFF file");

		saveDataButton = new JButton ("Save data");
		saveDataButton.setToolTipText("Save current segmentation into an ARFF file");
		//saveDataButton.setEnabled(false);

		addClassButton = new JButton ("Create new class");
		addClassButton.setToolTipText("Add one more label to mark different areas");
		addClassButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		System.out.println("ajout d'une class");
        		//weka2.addClass();
        		//int aaaa = weka2.getNumOfClasses();
        		//System.out.println(aaaa);
        		addClass();
        		//System.out.println(weka2.getNumRandomFeatures()); 
        		//numberRandom Feature = 2 
        	}
        });

		settingsButton = new JButton ("Settings");
		settingsButton.setToolTipText("Display settings dialog");

		//panelSegmentation.setLayout(new BorderLayout());
		panelSegmentation.setLayout(new GridLayout(11, 0,0,0));
		panelSegmentation.add(trainButton);
		panelSegmentation.add(overlayButton);
		panelSegmentation.add(resultButton);
		panelSegmentation.add(probabilityButton);
		panelSegmentation.add(applyButton);
		panelSegmentation.add(loadClassifierButton);
		panelSegmentation.add(saveClassifierButton);
		panelSegmentation.add(loadDataButton);
		panelSegmentation.add(saveDataButton);
		panelSegmentation.add(addClassButton);
		System.out.println("nb de class : "+ numOfClasses);
		panelSegmentation.add(settingsButton);
		addClass();
		panelTotalSegmentation.setLayout(new BorderLayout());
		panelTotalSegmentation.add(panelSegmentation,BorderLayout.WEST);
//		SOFTFenetre1 image = new SOFTFenetre1();
//		panelTotalSegmentation.add(image.getPanel());
		
		panelTotalSegmentation.add(panelclass);
		return panelTotalSegmentation;

	}

	
	public void addClass() {
		
		numOfClasses = numOfClasses+1 ;
		ButtonGroup group = new ButtonGroup();	
		System.out.println("number of class = "+numOfClasses);
		JPanel test = new JPanel();
		if (numOfClasses <= 5){

			JButton buttonBg = new JButton("add to class background ");
			JButton buttonNucleus = new JButton("add to class Nucleus ");
			buttonBg.setBackground(Color.white);
			buttonNucleus.setBackground(Color.white);
			buttonBg.addActionListener(new ActionListener(){
	        	public void actionPerformed(ActionEvent e){
	        		System.out.println("click sur button bg");
	        		//weka.addTrace("0", "1");
	        		//weka2.addExample(0, roi, 1); Deffinir une roi
	        		}
	        });
			buttonNucleus.addActionListener(new ActionListener(){
	        	public void actionPerformed(ActionEvent e){
	        		System.out.println("click sur button nucl");
	        		 
	        		}
	        });
			//BufferedImage monimage = open.createThumbnailImage(50);
		    //choix.add(CaseImage(monimage),gbc);
	    panelclass.add(buttonNucleus);
		panelclass.add(buttonBg);
		panelTotalSegmentation.add(panelclass);
		} else {
			System.out.println(" Number of class max atteingned");
		}
	}
}
