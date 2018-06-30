package bichromate.servers;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import bichromate.core.sTestOSInformationFactory;

public class sTestServerIndicator extends JPanel implements ActionListener {

	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ImageIcon images[];
	private int totalImages = 6, currentImage = 0, animationDelay = 500;
	private Timer animationTimer;
	private sTestOSInformationFactory osInfo;
	private String imageFile;
	private ImageIcon image;
	private Dimension d = null;
	public sTestServerIndicator() {
		osInfo = new sTestOSInformationFactory();
	    images = new ImageIcon[totalImages];
	    for (int i = 1; i <= images.length; ++i){
	    	imageFile = new String(osInfo.getAnimationDirectory()+"p"+ + i + ".gif");
	    	image = new ImageIcon(imageFile);
	    	images[i-1] = image;
	    }
	    d = new Dimension(32,32);
	    setPreferredSize(d);
	    setMinimumSize(d);
	    setMaximumSize(d);
	    setSize(d);
	}
	public Dimension getPreferredSize(){
		return d;
	}
	public Dimension getMinimumSize(){
		return d;
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//if (images[currentImage].getImageLoadStatus() == MediaTracker.COMPLETE) {
			images[currentImage].paintIcon(this, g, 0, 0);
			currentImage = (currentImage + 1) % totalImages;
			if(null == animationTimer)
				currentImage = 0;
		//}
	}

	  public void actionPerformed(ActionEvent e) {
	    repaint();
	  }

	  public void startAnimation() {
	    if (animationTimer == null) {
	      currentImage = 0;
	      animationTimer = new Timer(animationDelay, this);
	      animationTimer.start();
	    } else if (!animationTimer.isRunning())
	      animationTimer.restart();
	  }

	  public void stopAnimation() {
	    animationTimer.stop();
	    animationTimer = null;
	  }
	
	
	
	//
	// Inner class for testing on the command line
	//
	public static class Test
	{
		public static void main(final String[] args){
			
			
			sTestServerIndicator aniGif = null;
			
			aniGif = new sTestServerIndicator ();
			if(aniGif != null){
			    JFrame app = new JFrame("Animator test");
			    app.add(aniGif, BorderLayout.CENTER);
			    app.setSize(600,600);
			    app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			    app.setSize(aniGif.getPreferredSize().width + 10, aniGif.getPreferredSize().height + 30);
			    app.pack();
			    app.setVisible(true);
			}
			
		} // end Main
	 } // end Inner class Test
}//sTestServerIndicator
