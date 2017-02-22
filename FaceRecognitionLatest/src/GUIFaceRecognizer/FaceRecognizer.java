package GUIFaceRecognizer;


/* Show a sequence of images snapped from a webcam in a picture panel (FaceRecogPanel). 
   A face is highlighted with a yellow rectangle, which is updated as the face
   moves. The highlighted part of the image can be recognized by the user pressing
   the "Recognize Face" button.

   Usage:
      > java FaceRecognizer
*/

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_objdetect;


public class FaceRecognizer extends JFrame 
{
  // GUI components
  private FaceRecogPanel facePanel;
  private JButton recogBut;
  private JTextField nameField;   // where the name (and distance info) appears


  public FaceRecognizer()
  {
    super("Face Recognizer");

    Container c = getContentPane();
    c.setLayout( new BorderLayout() );   

    // Preload the opencv_objdetect module to work around a known bug.
    Loader.load(opencv_objdetect.class);

    facePanel = new FaceRecogPanel(this); // the sequence of pictures appear here
    c.add( facePanel, BorderLayout.CENTER);


    // button for recognizing a highlighted face
    recogBut = new JButton("Recognize Face");
    recogBut.addActionListener( new ActionListener() {
       public void actionPerformed(ActionEvent e)
       { nameField.setText("");
         recogBut.setEnabled(false);
         facePanel.setRecog();
       }
    });

    nameField = new JTextField(20);   // for the name of the recognized face
    nameField.setEditable(false);

    JPanel p = new JPanel();
    p.add(recogBut);
    p.add( new JLabel("Name: "));
    p.add( nameField);
    c.add(p, BorderLayout.SOUTH);


    addWindowListener( new WindowAdapter() {
      public void windowClosing(WindowEvent e)
      { facePanel.closeDown();    // stop snapping pics
        System.exit(0);
      }
    });

    pack();  
    setResizable(false);
    setVisible(true);
  } // end of FaceRecognizer()



  public void setRecogName(final String faceName, final String dist)
  // update face name and its distance in the nameField; called from panel
  { 
    SwingUtilities.invokeLater(new Runnable() {
      public void run() 
      {  nameField.setText( faceName + " (" + dist + ")");  
         recogBut.setEnabled(true);
      }
    });
  }  // end of setRecogName()


  // -------------------------------------------------------

  public static void main( String args[] )
  {  new FaceRecognizer();  }

} // end of FaceRecognizer class
