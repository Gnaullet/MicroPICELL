package soft;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneLayout;
import javax.swing.table.DefaultTableCellRenderer;
import ij.ImagePlus;
import ij.gui.Roi;
import ij.measure.ResultsTable;
import ij.plugin.filter.*;

import ij.process.ImageConverter;
import ij.process.ImageProcessor;
import ij.plugin.frame.RoiManager;

public class CountNucleus extends SOFTFenetre1 {
	public ParticleAnalyzer instanceOfAnalyseParticle = new ParticleAnalyzer(); 

	public Analyzer analyse = new Analyzer();;
	public JPanel panelCount = new JPanel();
	public JPanel panelTotalCount = new JPanel();
	public JPanel StockPanel = new JPanel();
	public JPanel panel = new JPanel();
	public int counterMaskView = 1;
	public int totalCount;
	SOFTFenetre1 SF = new SOFTFenetre1();
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

	/**
	 * 
	 */

	private static final long serialVersionUID = -2389781670803118594L;
	private Object[][] donnees;
	/*************************** Constructor **************************/

	public CountNucleus(){}
	/************************* Return Table With Information of Nucleus **************/
	/**
	 * 
	 * @param image
	 * @param panel2
	 * @return
	 */
	public JPanel returnTableOfCount (BufferedImage image, JPanel panel2 ){

		panel = panel2;
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

					Showdialog = instanceOfAnalyseParticle.showDialog();
					if(Showdialog ==true ){
						RoiManager roiManager = new RoiManager();
						@SuppressWarnings("static-access")
						int vv = analyse.getMeasurements();
						System.out.println("analyse 1 "+vv);
						instanceOfAnalyseParticle.analyze(imageBinaire, IProcessor);
						
						instanceOfAnalyseParticle.run(IProcessor);
						OutputImage = instanceOfAnalyseParticle.getOutputImage();
					
						System.out.println(instanceOfAnalyseParticle.toString());
						System.out.println(OutputImage);
						@SuppressWarnings("static-access")
						int ee = analyse.getMeasurements();
						System.out.println("analyse 2 "+ee);
						returnTable(roiManager);
						
						System.out.println("1");

					}
			}
		});	
		/**
		 * 
		 */
		viewMask = new JButton("View mask count");
		viewMask.setToolTipText("Generate current probability maps");		
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


		panelTotalCount.add(panelCount,BorderLayout.WEST);
		panelTotalCount.add(panel,BorderLayout.CENTER);
		return panelTotalCount;

	}

	/******************************** Construction of Table result Count *******************/
	/**
	 * 
	 * @param roiManager
	 */
	public void returnTable(RoiManager roiManager){



		System.out.println("dans returnTable");

		int azerty = roiManager.getCount()/2;

		Object[][]donnees = new Object[azerty][3];


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
						donnees[i][j]=df.format(zzz.getLength());
					}else{
						donnees[i][j]=resultTable.getStringValue(0, i);
					}

				}
			}
		}

		JPanel panelTable = new JPanel();
		JTable TableAffiche = createTable(donnees);


		DefaultTableCellRenderer custom = new DefaultTableCellRenderer(); 
		custom.setHorizontalAlignment(JLabel.CENTER); 
		for (int i=0 ; i < TableAffiche.getColumnCount() ; i++){ 
			TableAffiche.getColumnModel().getColumn(i).setCellRenderer(custom); 
		}


		TableAffiche.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		TableAffiche.getColumn("id").setPreferredWidth(133);
		TableAffiche.getColumn("Perimeter").setPreferredWidth(133);
		TableAffiche.getColumn("Area").setPreferredWidth(133);
		panelTable.setSize(new Dimension(400, 500));
		panelTable.setMinimumSize(new Dimension(400, 500));
		panelTable.setMaximumSize(new Dimension(400, 500));
		panelTable.setPreferredSize(new Dimension(400, 500));

		//panelTable.add(TableAffiche.getTableHeader(),BorderLayout.NORTH);

		JScrollPane Table2 =new JScrollPane();
		Table2.setVisible(true);
		Table2.setLayout(new ScrollPaneLayout());

		Table2.setViewportView(TableAffiche);
		//Table2.setVerticalScrollBarPolicy(ScrollPaneLayout.HORIZONTAL_SCROLLBAR_ALWAYS);
		Table2.getHorizontalScrollBar();
		panelTable.add(Table2,BorderLayout.CENTER);

		label2 = new JLabel("<html> There are "+azerty+ " nucleus. </html>");

		panelTable.add(label2,BorderLayout.NORTH);
		panelTotalCount.add(panelTable,BorderLayout.EAST);

		panelTotalCount.repaint();
		System.out.println("sort returnTable");
	}

	/******************************** CreateJtable *****************/
	public JTable createTable(Object[][] donnees){

		String[] entetes = {"id", "Perimeter", "Area"};
		JTable tableau = new JTable(donnees, entetes);
		System.out.println("tableau " + tableau);

		return tableau;
	}
	/********************************create thumbnail of overlay ************/
	 
    public static BufferedImage createThumbnail(BufferedImage image, int requestedThumbSize) {
    float ratio = (float) image.getWidth() / (float) image.getHeight();
    int width = image.getWidth();
    BufferedImage thumb = image;

    do {
        width /= 2;
        if (width < requestedThumbSize) {
            width = requestedThumbSize;
        }

        BufferedImage temp = new BufferedImage(width, (int) (width / ratio), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = temp.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(thumb, 0, 0, temp.getWidth(), temp.getHeight(), null);
        g2.dispose();

        thumb = temp;
    } while (width != requestedThumbSize);

    return thumb;
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
	public Object[][] getDonnees() {
		return donnees;
	}
	public void setDonnees(Object[][] donnees) {
		this.donnees = donnees;
	}

}
