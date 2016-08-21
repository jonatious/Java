import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.io.File;

public class ImageReaderSpike {
    public static void main(String[] args) {

        String path = "C:\\Users\\Karthikk\\Pictures\\white_image.png";

        try{
            File imageFile = new File(path);
            BufferedImage img = ImageIO.read(imageFile);
            Raster imageData = img.getData();
            System.out.println("The Data Bounds are : "+ imageData.getBounds());
            System.out.println("The Num Of Bands are : "+ imageData.getNumBands());
            System.out.println("The Num of DataElements are : "+ imageData.getNumDataElements());
            System.out.println("The Raster is : "+ imageData);
            System.out.println("The sample data is : "+ imageData.getSample(200,200,2));

            DataBuffer dataBuffer = imageData.getDataBuffer();

            System.out.println(dataBuffer.getSize());

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
