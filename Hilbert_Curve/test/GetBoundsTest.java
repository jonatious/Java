import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class GetBoundsTest {
    ImageReader imageReaderTest;
    String path = "C:\\Users\\Karthikk\\Pictures\\ganesh-vtv.jpg";
    Raster imageRaster;
    private int xValue;
    private int yValue;
    private float expectedValue;

    @Before
    public void init() throws IOException {
        imageReaderTest = new ImageReader();
        File imageFile = new File(path);
        BufferedImage img = ImageIO.read(imageFile);
        imageRaster = imageReaderTest.getRasterDataForImage(img);
    }

    public GetBoundsTest(int xValue,int yValue,float expectedValue){
        this.xValue = xValue;
        this.yValue = yValue;
        this.expectedValue = expectedValue;
    }

    @Parameterized.Parameters
    public static Collection xyValues(){
        return Arrays.asList(new Object[][] {
                {1,1,0},
                {328,0,0}
        });
    }

    @Test
    public void checkAndReturnPixelValueReturnsValue(){
        assertEquals(expectedValue,imageReaderTest.checkAndReturnPixelValue(xValue,yValue,imageRaster,imageReaderTest.getXAndYPaddingValues(imageRaster)),0.001);
    }

}
