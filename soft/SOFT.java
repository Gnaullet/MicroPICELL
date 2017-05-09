package soft;
import javax.swing.SwingUtilities;

public class SOFT {
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				SOFTFenetre1 fenetre = new SOFTFenetre1();
				fenetre.setVisible(true);
				
			}
		
		});
	}
}