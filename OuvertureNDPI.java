package soft;


import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.openslide.*;
import org.openslide.gui.OpenSlideView;




public class OuvertureNDPI {
	
	public JPanel lepanel = new JPanel(); // le panel de l'image
	public int SelectLvlImage = 100;
	public int lvlselected; //stock le radio button selectionné
	private JPanel panelOpened = new JPanel();
	private int index;
	JPanel a = new JPanel();
	
	public OuvertureNDPI(String nomFichier, String pathFichier, File filechoose, JFileChooser chooser) {
	}



	public JPanel ouvertureImage(String fileName, String Path,File filechoose,JFileChooser chooser) throws IOException{
		
		OpenSlide open = new OpenSlide(filechoose);
		
		int x = open.getLevelCount(); //permet de connaitre le nombre de niveau de l'image +1
		System.out.println(x);
		
		BufferedImage image = open.createThumbnailImage( 0	 ,0, (int)(open.getLevel0Width()) ,(int)(open.getLevel0Height()) ,1000);
		lepanel.add(caseImage(image));
	    this.panelOpened.add(lepanel);
	    return panelOpened;
	}
	public Map<String, AssociatedImage> afficheMapping(String fileName, String Path,File filechoose,JFileChooser chooser) throws IOException{
		OpenSlide open= new OpenSlide(filechoose);
		
		Map<String, AssociatedImage> a = open.getAssociatedImages();
		//System.out.println(a);
		return a;
	}
	
	/************************* Affichage de la selection du lvl  ***************************************/

	public JPanel InformationImage(String nomFichier, String pathFichier, File filechoose, JFileChooser chooser) throws IOException {
		
		OpenSlide open = new OpenSlide(filechoose);
		JPanel choix = new JPanel();
		
		
		int nbNiv = open.getLevelCount();
		//choix.setSize(120, 1000);
		
		
	    //On ajoute le bouton au content pane de la JFrame

		 choix.setLayout(new GridLayout((nbNiv+3),1));
		 //choix.setLayout(new GridBagLayout());
	        
			JLabel label = new JLabel( "<html><br/>Choix du niveau de l'image à traiter <br> choisir parmis les "+ (nbNiv-1) + "niveaux:  </html>"); 
			choix.add(label);
			ButtonGroup group = new ButtonGroup();
			ArrayList <JRadioButton> radioList = new ArrayList<JRadioButton>() ;	
		
			for(int i=0; i<nbNiv; i++){

			
			JRadioButton test1  = new JRadioButton("<html> Level : "+i+"<br/>Height(px): "+ open.getLevelHeight(i) +"<br/>"+ "Width(px): "+ open.getLevelWidth(i)+"<br/></html>");
			radioList.add(test1);
			radioList.size();
			//BufferedImage monimage = open.createThumbnailImage(50);
		
			//choix.add(CaseImage(monimage),gbc);
			group.add(test1);
			choix.add(test1);
			test1.setBackground(Color.white);
		}
			
		 JButton bouton = new JButton("submit");
		 choix.add(bouton);
		 this.panelOpened.add(choix);
		 choix.setBackground(new Color(86, 115, 154));
		 System.out.println("6");
		 bouton.addActionListener(new ActionListener(){
			    public void actionPerformed(ActionEvent e){
							System.out.println("Clicked sur bouton");
								for(JRadioButton i : radioList){
									
									if(i.isSelected()){
										lvlselected = radioList.indexOf(i);
										System.out.println(lvlselected);
										
										try {
											
											setSelectLvlImage(OpenWholeImage(lvlselected, nomFichier, pathFichier, filechoose, chooser));
											System.out.println(SelectLvlImage);
											
										} catch (IOException e1) {
											System.out.println("error of selection of the image");
											e1.printStackTrace();
										}
										
										
										
										try {
											 Map<String, AssociatedImage> r = open.getAssociatedImages();
											 Map<String, String> z = open.getProperties();
											 int sizeofmap = z.size();
											 Collection<String> valeur = z.values();
											 System.out.println(z);
											 System.out.println(sizeofmap);
											 System.out.println(valeur);
											 long width = (long)(Double.parseDouble(z.get("openslide.level["+SelectLvlImage+"].width")));
											 long  height = (long)(Double.parseDouble(z.get("openslide.level["+SelectLvlImage+"].height")));
											 
											 BufferedImage lama = open.createThumbnailImage( (int)(open.getLevel0Width()/4) ,(int)(open.getLevel0Height()/4), (int)(open.getLevel0Width()/2) ,(int)(open.getLevel0Height()/2) ,(int)width);
											 a.add(caseImage(lama));
											 panelOpened.removeAll();
											 panelOpened.validate();
											 panelOpened.add(a);
											 panelOpened.revalidate();
											 
										} catch (IOException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
									}
								}	
								System.out.println("level choose : "+SelectLvlImage);
						}
				});
		 		return panelOpened;
		}
	public BufferedImage ReturnBuffImage(String fileName, String Path,File filechoose,JFileChooser chooser) throws IOException{
		OpenSlide open = new OpenSlide(filechoose);
		BufferedImage image = open.createThumbnailImage( 0	 ,0, (int)(open.getLevel0Width()) ,(int)(open.getLevel0Height()) ,1000);
		return image;
		
	}
	public String getrecupererNom(JFileChooser chooser) {
		String nom= chooser.getSelectedFile().getName();
		return nom;
	}	
	
	public JPanel OpenROI(){
		
		return null;	
	}
	
	public int OpenWholeImage(int level, String nomFichier, String pathFichier, File filechoose, JFileChooser chooser) throws IOException{
		System.out.println("lvl :" +level);
		OpenSlide open = new OpenSlide(filechoose);
		Map<String, String> properties = open.getProperties();
		System.out.println(properties.size());
		return level;	
	}

	public JPanel caseImage(BufferedImage monImage){
		JPanel MesIcon = new JPanel(); 
		MesIcon.setLayout(new FlowLayout());
		JLabel image = new JLabel( new ImageIcon(monImage));
		MesIcon.add(image);
		return MesIcon;
	}
	
	public int getSelectLvlImage() {
		return SelectLvlImage;
	}

	public void setSelectLvlImage(int selectLvlImage) {
		SelectLvlImage = selectLvlImage;
	}



	public int getLvlselected() {
		return lvlselected;
	}
	public void setLvlselected(int lvlselected) {
		this.lvlselected = lvlselected;
	}
	public JPanel getLepanel() {
		return lepanel;
	}
	public void setLepanel(JPanel lepanel) {
		this.lepanel = lepanel;
	}
	public JPanel getPanelOpened() {
		return panelOpened;
	}
	public void setPanelOpened(JPanel panelOpened) {
		this.panelOpened = panelOpened;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public JPanel getA() {
		return a;
	}
	public void setA(JPanel a) {
		this.a = a;
	}
	
}
