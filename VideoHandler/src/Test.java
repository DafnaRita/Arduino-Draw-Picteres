import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.Timer;



public class Test extends JFrame {

    JSlider spinnerOffset;
    JSlider spinnerMul;

    static SerialPort serialPort;
    static  double mul, offset;
    public void showTestBufferedImage(BufferedImage comImg,BufferedImage comImgGrayScale, BufferedImage comImgBlackWhite) {
        this.setTitle("Test eban'y");
        this.setSize(500, 400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel compressedImage = new JLabel(new ImageIcon(comImg));
        JLabel compressedImageGrayScale = new JLabel(new ImageIcon(comImgGrayScale));
        JLabel compressedImageBlackWhite = new JLabel(new ImageIcon(comImgBlackWhite));

        JLabel original = new JLabel("Original:");
        JLabel grayscale = new JLabel("Grayscale:");
        JLabel blackwhite = new JLabel("Black-white:");
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(8,1,5,5));


        jPanel.add(original);
        jPanel.add(compressedImage);
        jPanel.add(grayscale);
        jPanel.add(compressedImageGrayScale);

        jPanel.add(blackwhite);
        jPanel.add(compressedImageBlackWhite);
        jPanel.add(spinnerOffset);
        jPanel.add(spinnerMul);


        spinnerMul.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                mul = spinnerMul.getValue();
            }
        });

        spinnerOffset.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                offset = spinnerOffset.getValue();

            }
        });
        this.add(jPanel);
        this.setVisible(true);
    }
    void init(){
        spinnerOffset = new JSlider(JSlider.HORIZONTAL,
                -10, 10, 0);
        spinnerOffset.setValue(-2);
        spinnerMul = new JSlider(JSlider.HORIZONTAL,
                -10, 10, 1);
        spinnerMul.setValue(6);
        mul = -7;
        offset = 4;
        spinnerMul.setPaintTicks(true);
        spinnerMul.setMajorTickSpacing(1);
        spinnerOffset.setPaintTicks(true);
        spinnerOffset.setMajorTickSpacing(1);

        spinnerMul.setPaintTicks(true);
        spinnerMul.setPaintLabels(true);

        spinnerOffset.setPaintTicks(true);
        spinnerOffset.setPaintLabels(true);
    }
    public static void main(String[] args) throws IOException, FileNotFoundException {

        GetBufFromVideo fromVideo = new GetBufFromVideo();
        BlackWhite blackWhite = new BlackWhite();
        Scale scale = new Scale();
        Send send = new Send();
        Test test = new Test();
        test.init();
        Contrast contrast = new Contrast();
        GrayScale grayScale = new GrayScale();


        serialPort = new SerialPort("/dev/ttyUSB0");
        try {
            serialPort.openPort();
            serialPort.setParams(SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN |
                    SerialPort.FLOWCONTROL_RTSCTS_OUT);
            serialPort.addEventListener(new Serial());

        }
        catch (SerialPortException ex) {
            System.out.println(ex);
        }



    new Timer("Timer name").schedule(new TimerTask() {

        @Override
        public void run() {
            {
                BufferedImage imageOriginal = new BufferedImage(84, 48, BufferedImage.TYPE_INT_RGB);
                BufferedImage imageGrayScale = new BufferedImage(84, 48, BufferedImage.TYPE_INT_RGB);
                BufferedImage imageContrast = new BufferedImage(84, 48, BufferedImage.TYPE_INT_RGB);
                BufferedImage image = fromVideo.get();
                image = scale.filter(image);
                imageOriginal = scale.filter(image);
                imageContrast = contrast.ContrastImage(grayScale.filter(scale.filter(image)), mul, offset);
                try {
                    image = blackWhite.toBlackWhiteArray(contrast.ContrastImage(grayScale.filter(scale.filter(image)), mul,  offset));
                    test.showTestBufferedImage(imageOriginal, imageContrast,  image);
                    send.sending(image, serialPort);

                    // test.setVisible(true);

                } catch (IOException e) {
                    System.out.print("IO error:" + e.getMessage());
                }

            }
        }
    }, 0, 100);




    }
    static class Serial implements SerialPortEventListener {

        public void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR()) {//If data is available
                //System.out.println(event.getEventValue());
                if (event.getEventValue() > 4) {//Check bytes count in the input buffer

                    //Read data, if 10 bytes available
                    try {
                        byte buffer[] = serialPort.readBytes(1);
                        System.out.println(Integer.toBinaryString(buffer[0]));
                    } catch (SerialPortException ex) {
                        System.out.println(ex);
                    }
                }
            } else if (event.isCTS()) {//If CTS line has changed state
                if (event.getEventValue() == 1) {//If line is ON
                    System.out.println("CTS - ON");
                } else {
                    System.out.println("CTS - OFF");
                }
            } else if (event.isDSR()) {///If DSR line has changed state
                if (event.getEventValue() == 1) {//If line is ON
                    System.out.println("DSR - ON");
                } else {
                    System.out.println("DSR - OFF");
                }
            }
        }
    }
}


