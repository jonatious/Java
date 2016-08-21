import org.junit.Before;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class ImageReaderTest {

    ImageReader imageReaderTest;
    String path = "C:\\Users\\Karthikk\\Pictures\\ganesh-vtv.jpg";
    String whiteImage = "C:\\Users\\Karthikk\\Pictures\\white_image.png";

    @Before
    public void init(){
        imageReaderTest = new ImageReader();
    }

    @Test
    public void Canary(){
        assertTrue(true);
    }

    @Test
    public void readImagereadsAnImageFile(){
        assertNotEquals(null, imageReaderTest.readImage(path));
    }

    @Test
    public void getRasterDataForImageGetsTheInputDataRequired() throws IOException {
        File imageFile = new File(path);
        BufferedImage img = ImageIO.read(imageFile);
        assertNotEquals(null, imageReaderTest.getRasterDataForImage(img));
    }

    @Test
    public void getAmplitudeValueForGivenPixelLocationReturnsValueLessThan1() throws IOException {
        File imageFile = new File(path);
        BufferedImage img = ImageIO.read(imageFile);
        Raster imageRaster = imageReaderTest.getRasterDataForImage(img);
        boolean value = false;
        if (imageReaderTest.getAmplitudeValueForPixel(10,10,imageRaster) <= 1){
            value = true;
        }else{
            value = false;
        }
        assertTrue(value);
    }

    @Test
    public void getAmplitudeValueForAWhiteImageReturnsAValueOf1() throws IOException {
        File imageFile = new File(whiteImage);
        BufferedImage img = ImageIO.read(imageFile);
        Raster imageRaster = imageReaderTest.getRasterDataForImage(img);
        boolean value = false;
        if (imageReaderTest.getAmplitudeValueForPixel(10,10,imageRaster) == 1){
            value = true;
        }else{
            value = false;
        }
        assertTrue(value);
    }

    @Test
    public void getImageBoundsTest() throws IOException {
        File imageFile = new File(path);
        BufferedImage img = ImageIO.read(imageFile);
        Raster imageRaster = imageReaderTest.getRasterDataForImage(img);
        assertEquals(512,imageReaderTest.getImageBounds(imageRaster));
    }

    @Test
    public void getXandYPaddingValuesReturnTheValueToBePadded() throws IOException {
        File imageFile = new File(path);
        BufferedImage img = ImageIO.read(imageFile);
        Raster imageRaster = imageReaderTest.getRasterDataForImage(img);
        Hashtable expectedValues = new Hashtable();
        expectedValues.put("xPadding",93);
        expectedValues.put("yPadding",119);
        assertEquals(expectedValues,imageReaderTest.getXAndYPaddingValues(imageRaster));
    }
}
